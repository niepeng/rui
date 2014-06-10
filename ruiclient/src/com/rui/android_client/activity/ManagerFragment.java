package com.rui.android_client.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.android_client.R;
import com.rui.android_client.download.Downloader;
import com.rui.android_client.download.LoadInfo;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.utils.JsonUtil;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class ManagerFragment extends Fragment {

	private RuiApp mApp;

	private String DIR_NAME = "rui";
	private File rootFile;

	private ArrayList<AppInfo> mAppInfos;

	private ListView mListView;
	private ListAdapter mListAdapter;

	private LoadInstalledAppsTask mLoadInstalledAppsTask;

//	// 存放各个下载器
//	private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	// 存放与下载器对应的进度条
	private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();

	public static ManagerFragment newInstance() {
		ManagerFragment fragment = new ManagerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApp = (RuiApp) getActivity().getApplication();

		rootFile = createDirIfNotExists(DIR_NAME);

		View rootView = inflater.inflate(R.layout.fragment_manager, null);
		mListView = (ListView) rootView.findViewById(R.id.list_view);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo app = mListAdapter.getItem(position);
				if (app.getId() == 0) {
					return;
				}
				Intent intent = new Intent(getActivity(),
						AppDetailActivity.class);
				intent.putExtra("ID", app.getId());
				getActivity().startActivity(intent);
			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadInstalledApps();
	}

	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mAppInfos == null) {
				return 0;
			}
			return mAppInfos.size();
		}

		@Override
		public AppInfo getItem(int position) {
			return mAppInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder(getActivity());
			} else {
				holder = (ViewHolder) convertView;
			}
			AppInfo app = getItem(position);
			holder.setViewContent(position, app);
			return holder;
		}

		protected class ViewHolder extends LinearLayout {

			public ImageView iconView;
			public TextView titleView;
			public Button openBtn;
			public Button updateBtn;
			public ProgressBar downloadProgress;

			public ViewHolder(Context context) {
				super(context);
				View.inflate(context, R.layout.layout_app_listview_item, this);
				iconView = (ImageView) findViewById(R.id.icon);
				titleView = (TextView) findViewById(R.id.title);
				openBtn = (Button) findViewById(R.id.open_btn);
				updateBtn = (Button) findViewById(R.id.update_btn);
				downloadProgress = (ProgressBar) findViewById(R.id.download_progress);

				openBtn.setOnClickListener(new OpenAppClick());
				updateBtn.setOnClickListener(new OnUpdateClickListener());
			}

			public void setViewContent(int position, AppInfo item) {
				iconView.setImageDrawable(item.getIcon());
				titleView.setText(item.getMainTitle());
				updateBtn.setTag(position);
				openBtn.setTag(position);
				if (item.isNeedUpdate()) {
					updateBtn.setVisibility(View.VISIBLE);
					openBtn.setVisibility(View.GONE);
				} else {
					updateBtn.setVisibility(View.GONE);
					openBtn.setVisibility(View.VISIBLE);
				}
//				Downloader downloader = downloaders.get(item.getDownUrl());
//				if (downloader != null
//						&& (downloader.isDownloading() || downloader.isPause())) {
//					downloadProgress.setVisibility(View.VISIBLE);
//					downloadProgress.setMax(item.getFileSize());
//				} else {
//					downloadProgress.setVisibility(View.GONE);
//				}
			}

			private class OpenAppClick implements View.OnClickListener {

				@Override
				public void onClick(View v) {
					AppInfo appInfo = mListAdapter.getItem(Integer.parseInt(v
							.getTag().toString()));
					PackageManager pm = getActivity().getPackageManager();
					Intent appStartIntent = pm
							.getLaunchIntentForPackage(appInfo.getPackageName());
					if (null != appStartIntent) {
						getActivity().startActivity(appStartIntent);
					}
				}

			}

		}

	}

	private void loadInstalledApps() {
		if (mLoadInstalledAppsTask != null) {
			return;
		}
		mLoadInstalledAppsTask = new LoadInstalledAppsTask();
		mLoadInstalledAppsTask.execute();
	}

	private class LoadInstalledAppsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			mAppInfos = mApp.getInstalledAppInfos();
			String url = Config.getConfig().getProperty(
					Config.Names.INSTALLED_APPS_PACKAGE);
			ArrayList<String> packageNames = new ArrayList<String>();
			for (AppInfo item : mAppInfos) {
				packageNames.add(item.getPackageName());
			}
			RemoteManager remoteManager = RemoteManager
					.getPostOnceRemoteManager();
			Request request = remoteManager.createPostRequest(url);
			request.addParameter("packages", packageNames);

			Response response = remoteManager.execute(request);
			if (response != null && response.isSuccess()) {
				JSONObject json = JsonUtil.getJsonObject(response.getModel());
				JSONArray jsonArray = JsonUtil.getJsonArray(json, "data");
				if (jsonArray != null) {
					for (int i = 0, size = jsonArray.length(); i < size; i++) {
						parserAppInfo(jsonArray, i);
					}
				}
			}

			return null;
		}

		private void parserAppInfo(JSONArray jsonArray, int i) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String packageName = JsonUtil.getString(jsonObject,
						"packageName", null);
				AppInfo appInfoFromServer = AppInfoParser.getInstance().parse(
						jsonObject);
				if (appInfoFromServer == null) {
					return;
				}
				AppInfo appInfo = mApp.getInstalledAppInfo(packageName);
				appInfo.setVersionValue(appInfoFromServer.getVersionValue());
				long newVersionCode = Long.parseLong(appInfo.getVersionValue());
				String downUrl = appInfoFromServer.getDownUrl();
				if (appInfo.getLocalVersionCode() < newVersionCode) {
					appInfo.setNeedUpdate(true);
				}
				appInfo.setDownUrl(downUrl);
				appInfo.setId(appInfoFromServer.getId());
				appInfo.setFileSize(appInfoFromServer.getFileSize());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mListAdapter.notifyDataSetChanged();
			mLoadInstalledAppsTask = null;
		}

	}

	/**
	 * 31 * 利用消息处理机制适时更新进度条 32
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
//				String url = (String) msg.obj;
//				int length = msg.arg1;
//				ProgressBar bar = ProgressBars.get(url);
//				if (bar != null) {
//					// 设置进度条按读取的length长度更新
//					bar.incrementProgressBy(length);
//					if (bar.getProgress() == bar.getMax()) {
//						Toast.makeText(getActivity(), "下载完成！", 0).show();
//						// 下载完成后清除进度条并将map中的数据清空
//						LinearLayout layout = (LinearLayout) bar.getParent();
//						layout.removeView(bar);
//						ProgressBars.remove(url);
//						downloaders.get(url).delete(url);
//						downloaders.get(url).reset();
//						downloaders.remove(url);
//					}
//				}
			}
		}
	};

	/**
	 * 显示进度条
	 */
	private void showProgress(LoadInfo loadInfo, String url) {
		ProgressBar bar = ProgressBars.get(url);
		if (bar == null) {
			return;
		}
		bar.setVisibility(View.VISIBLE);
		bar.setMax(loadInfo.getFileSize());
		bar.setProgress(loadInfo.getComplete());
	}

	/**
	 * 响应暂停下载按钮的点击事件
	 */
	public void pauseDownload(View v) {
		// LinearLayout layout = (LinearLayout) v.getParent();
		// String musicName = ((TextView) layout
		// .findViewById(R.id.tv_resouce_name)).getText().toString();
		// String urlstr = URL + musicName;
		// TODO
//		String urlstr = "";
//		downloaders.get(urlstr).pause();
	}

	private class OnUpdateClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			startDownload(v);
		}

	}

	/**
	 * 83 * 响应开始下载按钮的点击事件 84
	 */
	private void startDownload(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		AppInfo appInfo = mListAdapter.getItem(position);
		String urlstr = appInfo.getDownUrl();
		String localfile = rootFile.getPath() + File.pathSeparator
				+ appInfo.getPackageName() + ".apk";
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), com.rui.android_client.service.DownloadService.class);
		intent.putExtra("fileName", localfile);
		intent.putExtra("downloadUrl", urlstr);
		intent.putExtra("flag","setState");//标志着数据从localdownactivity传送
		getActivity().startService(intent);//这里启动service
		
//		int threadcount = 4;
//		// 初始化一个downloader下载器
//		Downloader downloader = downloaders.get(urlstr);
//		if (downloader == null) {
//			downloader = new Downloader(urlstr, localfile, threadcount,
//					getActivity(), mHandler);
//			downloaders.put(urlstr, downloader);
//		}
//		if (downloader.isDownloading())
//			return;
//		// 得到下载信息类的个数组成集合
//		LoadInfo loadInfo = downloader.getDownloaderInfors();
//		// 显示进度条
//		// TODO
//		if (ProgressBars.get(urlstr) == null) {
//			ProgressBar bar = (ProgressBar) ((View) v.getParent().getParent())
//					.findViewById(R.id.download_progress);
//			ProgressBars.put(urlstr, bar);
//		}
//		showProgress(loadInfo, urlstr);
//		// 调用方法开始下载
//		downloader.download();
	}

	private File createDirIfNotExists(String path) {
		boolean success = true;

		File file = new File(Environment.getExternalStorageDirectory(), path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				success = false;
			}
		}
		if (success) {
			return file;
		}
		return null;
	}

}

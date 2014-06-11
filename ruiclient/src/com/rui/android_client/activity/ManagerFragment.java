package com.rui.android_client.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.rui.android_client.R;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.model.DownloadInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.service.DownloadService;
import com.rui.android_client.utils.CollectionUtil;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.StringUtil;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class ManagerFragment extends Fragment {

	private RuiApp mApp;

	private String DIR_NAME = "rui";
	private File rootFile;
	
	private UpdateProgressReceiver mUpdateProgressReceiver;

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
		
		mUpdateProgressReceiver = new UpdateProgressReceiver();
		getActivity().registerReceiver(mUpdateProgressReceiver, new IntentFilter(DownloadService.ACTION_UPDATE_PROGRESS));
		
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
	
	@Override
	public void onDestroyView() {
		if (mUpdateProgressReceiver != null) {
			getActivity().unregisterReceiver(mUpdateProgressReceiver);
		}
		super.onDestroyView();
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
				if (CollectionUtil.isEmpty(item.getDownloadInfos())) {
					downloadProgress.setVisibility(View.GONE);
				} else {
					int completedSize = 0;
					for (DownloadInfo info : item.getDownloadInfos()) {
						completedSize += info.getCompeleteSize();
					}
					downloadProgress.setVisibility(View.VISIBLE);
					downloadProgress.setMax(item.getFileSize()); // TODO
					downloadProgress.setProgress(completedSize);
				}
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
				if (StringUtil.isNotBlank(item.getDownUrl())) {
					List<DownloadInfo> downloadInfos = RuiApp.mPersist.downloadInfoDao.getInfos(item.getDownUrl());
					item.setDownloadInfos(downloadInfos);
				}
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
		String localfile = rootFile.getPath() + File.pathSeparator + appInfo.getPackageName() + ".apk";
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), com.rui.android_client.service.DownloadService.class);
		intent.putExtra("fileName", localfile);
		intent.putExtra("downloadUrl", urlstr);
		intent.putExtra("flag","setState");//标志着数据从localdownactivity传送
		getActivity().startService(intent);//这里启动service
		
		showProgressBar(v, urlstr);
	}

	private void showProgressBar(View v, String urlstr) {
		ProgressBar bar = ProgressBars.get(urlstr);
		if (bar == null) {
			bar = (ProgressBar) ((View) v.getParent().getParent())
					.findViewById(R.id.download_progress);
			ProgressBars.put(urlstr, bar);
		}
		bar.setVisibility(View.VISIBLE);
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
	
	private class UpdateProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String url = intent.getStringExtra("downloadUrl");
			int fileSize = intent.getIntExtra("fileSize", 0);
			int completedSize = intent.getIntExtra("completedSize", 0);
			ProgressBar bar = ProgressBars.get(url);
			if (bar == null) {
				return;
			}
			bar.setMax(fileSize);
			bar.setProgress(completedSize);
		}

	}

}

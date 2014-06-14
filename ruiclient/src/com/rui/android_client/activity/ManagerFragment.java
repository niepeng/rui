package com.rui.android_client.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.rui.android_client.utils.DownloadUtils;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.StringUtil;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class ManagerFragment extends Fragment {

	private static final int REQUEST_INSTALL_APK_CODE = 1;

	private RuiApp mApp;

	private String DIR_NAME = "rui";
	private File rootFile;

	private UpdateProgressReceiver mUpdateProgressReceiver;

	private ArrayList<AppInfo> mAppInfos;

	private ListView mListView;
	private ListAdapter mListAdapter;

	private LoadInstalledAppsTask mLoadInstalledAppsTask;

	public static ManagerFragment newInstance() {
		ManagerFragment fragment = new ManagerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_INSTALL_APK_CODE) {

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mUpdateProgressReceiver = new UpdateProgressReceiver();
		getActivity().registerReceiver(mUpdateProgressReceiver,
				new IntentFilter(DownloadService.ACTION_UPDATE_PROGRESS));

		mApp = (RuiApp) getActivity().getApplication();

		rootFile = DownloadUtils.createDirIfNotExists();

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
			public Button cancelBtn;
			public Button installBtn;
			public ProgressBar downloadProgress;

			public ViewHolder(Context context) {
				super(context);
				View.inflate(context, R.layout.layout_app_listview_item, this);
				iconView = (ImageView) findViewById(R.id.icon);
				titleView = (TextView) findViewById(R.id.title);
				openBtn = (Button) findViewById(R.id.open_btn);
				updateBtn = (Button) findViewById(R.id.update_btn);
				cancelBtn = (Button) findViewById(R.id.cancel_btn);
				installBtn = (Button) findViewById(R.id.install_btn);
				downloadProgress = (ProgressBar) findViewById(R.id.download_progress);

				openBtn.setOnClickListener(new OpenAppClick());
				updateBtn.setOnClickListener(new OnUpdateClickListener());
				cancelBtn.setOnClickListener(new OnCancelClickListener());
				installBtn.setOnClickListener(new OnInstallApkClickListener());
			}

			public void setViewContent(int position, AppInfo item) {
				iconView.setImageDrawable(item.getIcon());
				titleView.setText(item.getMainTitle());

				openBtn.setVisibility(View.GONE);
				updateBtn.setVisibility(View.GONE);
				cancelBtn.setVisibility(View.GONE);
				installBtn.setVisibility(View.GONE);

				openBtn.setTag(position);
				updateBtn.setTag(position);
				cancelBtn.setTag(position);
				installBtn.setTag(position);

				if (CollectionUtil.isEmpty(item.getDownloadInfos())) {
					if (item.isNeedUpdate()) {
						updateBtn.setVisibility(View.VISIBLE);
					} else {
						openBtn.setVisibility(View.VISIBLE);
					}
					downloadProgress.setVisibility(View.GONE);
				} else {
					int fileSize = 0;
					int completedSize = 0;
					for (DownloadInfo info : item.getDownloadInfos()) {
						completedSize += info.getCompeleteSize();
						fileSize += info.getEndPos() - info.getStartPos();
					}
					downloadProgress.setVisibility(View.VISIBLE);
					downloadProgress.setMax(fileSize);
					downloadProgress.setProgress(completedSize);
					if (DownloadUtils.isReadyInstalled(item.getPackageName(),
							fileSize, completedSize)) {
						installBtn.setVisibility(View.VISIBLE);
					} else if (RuiApp.downloaders.get(item.getDownUrl()) != null
							&& DownloadUtils.isDownloading(fileSize,
									completedSize)) {
						cancelBtn.setVisibility(View.VISIBLE);
					} else {
						if (item.isNeedUpdate()) {
							updateBtn.setVisibility(View.VISIBLE);
						} else {
							openBtn.setVisibility(View.VISIBLE);
						}
					}
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

				if (StringUtil.isNotBlank(appInfo.getDownUrl())) {
					List<DownloadInfo> downloadInfos = RuiApp.mPersist.downloadInfoDao
							.getInfos(appInfo.getDownUrl());
					appInfo.setDownloadInfos(downloadInfos);
				}
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
		String downloadUrl = appInfo.getDownUrl();
		String fileName = rootFile.getAbsolutePath() + File.separator
				+ appInfo.getPackageName() + ".apk";

		Intent intent = new Intent();
		intent.setClass(getActivity(),
				com.rui.android_client.service.DownloadService.class);
		intent.putExtra("packageName", appInfo.getPackageName());
		intent.putExtra("fileName", fileName);
		intent.putExtra("downloadUrl", downloadUrl);
		intent.putExtra("flag", "start_download");// 标志着数据从localdownactivi
		getActivity().startService(intent);// 这里启动service

		showProgressBar(v, downloadUrl);
	}

	private void showProgressBar(View v, String downloadUrl) {
		View root = (View) v.getParent().getParent();
		ProgressBar bar = (ProgressBar) root
				.findViewById(R.id.download_progress);
		v.setVisibility(View.GONE);
		Button cancelBtn = (Button) root.findViewById(R.id.cancel_btn);
		cancelBtn.setVisibility(View.VISIBLE);
		bar.setVisibility(View.VISIBLE);
	}

	private class OnCancelClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int position = Integer.parseInt(v.getTag().toString());
			AppInfo appInfo = mListAdapter.getItem(position);

			Intent intent = new Intent();
			intent.setClass(getActivity(),
					com.rui.android_client.service.DownloadService.class);
			intent.putExtra("downloadUrl", appInfo.getDownUrl());
			intent.putExtra("flag", "cancel");// 标志着数据从localdownactivi
			getActivity().startService(intent);// 这里启动service
		}
	}

	private class OnInstallApkClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int position = Integer.parseInt(v.getTag().toString());
			AppInfo appInfo = mListAdapter.getItem(position);

			File file = new File(Environment.getExternalStorageDirectory(),
					DIR_NAME);
			if (!file.exists()) {
				return;
			}
			String path = file.getAbsolutePath() + File.separator
					+ appInfo.getPackageName() + ".apk";
			File installFile = new File(path);
			if (!installFile.exists()) {
				return;
			}

			String type = "application/vnd.android.package-archive";
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(installFile), type);
			startActivityForResult(intent, REQUEST_INSTALL_APK_CODE);
		}
	}

	private class UpdateProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String url = intent.getStringExtra("downloadUrl");
			String packageName = intent.getStringExtra("packageName");
			for (AppInfo item : mAppInfos) {
				if (StringUtil.isNotBlank(url) && url.equals(item.getDownUrl())) {
					item.setDownloadInfos(RuiApp.mPersist.downloadInfoDao
							.getInfos(url));
					mListAdapter.notifyDataSetChanged();
					break;
				}
				if (StringUtil.isNotBlank(packageName)
						&& packageName.equals(item.getPackageName())) {
					item.setDownloadInfos(RuiApp.mPersist.downloadInfoDao
							.getInfosByPackage(packageName));
					mListAdapter.notifyDataSetChanged();
					break;
				}
			}
		}

	}

}

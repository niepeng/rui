package com.rui.android_client.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.rui.android_client.R;
import com.rui.android_client.component.AppInfoViewHolder;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.model.DownloadInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.StringUtil;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class ManagerFragment extends RecevicerUpdateProgressFragment {

	private static final int REQUEST_INSTALL_APK_CODE = 1;

	private RuiApp mApp;

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
		super.onCreateView(inflater, container, savedInstanceState);
		mApp = (RuiApp) getActivity().getApplication();

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
			AppInfoViewHolder holder;
			if (convertView == null) {
				holder = new AppInfoViewHolder(getActivity());
			} else {
				holder = (AppInfoViewHolder) convertView;
			}
			AppInfo app = getItem(position);
			holder.setViewContent(position, app);
			return holder;
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
				String downUrl = appInfoFromServer.getDownUrl();
				appInfo.setInstalled(appInfo.isInstalled());
				appInfo.setNeedUpdate(appInfoFromServer.isNeedUpdate());
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

	@Override
	protected void updateProgress(String downloadUrl, String packageName) {
		super.updateProgress(mAppInfos, mListAdapter, downloadUrl, packageName);
	}

}

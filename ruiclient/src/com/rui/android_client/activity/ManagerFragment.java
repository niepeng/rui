package com.rui.android_client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.utils.JsonUtil;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class ManagerFragment extends Fragment {

	private RuiApp mApp;

	private ArrayList<InstalledAppInfo> mAppInfos;
	private HashMap<String, Integer> mInstalledAppInfosIndex;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApp = (RuiApp) getActivity().getApplication();
		View rootView = inflater.inflate(R.layout.fragment_manager, null);
		mListView = (ListView) rootView.findViewById(R.id.list_view);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);
		loadInstalledApps();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
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
		public InstalledAppInfo getItem(int position) {
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
			InstalledAppInfo app = getItem(position);
			holder.setViewContent(position, app);
			return holder;
		}

		protected class ViewHolder extends LinearLayout {

			public ImageView iconView;
			public TextView titleView;
			public Button openBtn;
			public Button updateBtn;

			public ViewHolder(Context context) {
				super(context);
				View.inflate(context, R.layout.layout_app_listview_item, this);
				iconView = (ImageView) findViewById(R.id.icon);
				titleView = (TextView) findViewById(R.id.title);
				openBtn = (Button) findViewById(R.id.open_btn);
				updateBtn = (Button) findViewById(R.id.update_btn);

				openBtn.setOnClickListener(new OpenAppClick());
			}

			public void setViewContent(int position, InstalledAppInfo item) {
				iconView.setImageDrawable(item.appLogo);
				titleView.setText(item.appName);
				if (item.needUpdate) {
					updateBtn.setVisibility(View.VISIBLE);
				} else {
					updateBtn.setVisibility(View.GONE);
				}

				openBtn.setTag(position);
			}

			private class OpenAppClick implements View.OnClickListener {

				@Override
				public void onClick(View v) {
					InstalledAppInfo appInfo = mListAdapter.getItem(Integer
							.parseInt(v.getTag().toString()));
					PackageManager pm = getActivity().getPackageManager();
					Intent appStartIntent = pm
							.getLaunchIntentForPackage(appInfo.packageName);
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
			mAppInfos = getInstalledApps(false);
			String url = Config.getConfig().getProperty(
					Config.Names.INSTALLED_APPS_PACKAGE);
			ArrayList<String> packageNames = new ArrayList<String>();
			for (InstalledAppInfo item : mAppInfos) {
				packageNames.add(item.packageName);
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
				if (!mInstalledAppInfosIndex.containsKey(packageName)) {
					return;
				}
				long versionCode = Long.parseLong(JsonUtil.getString(
						jsonObject, "versionValue", null));
				String downUrl = JsonUtil
						.getString(jsonObject, "downUrl", null);
				int index = mInstalledAppInfosIndex.get(packageName);
				InstalledAppInfo appInfo = mAppInfos.get(index);
				if (appInfo.versionCode < versionCode) {
					appInfo.needUpdate = true;
				}
				appInfo.downUrl = downUrl;
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

	private class InstalledAppInfo {
		public String appName = null;
		public String packageName = null;
		public String versionName = null;
		public int versionCode = 0;
		public Drawable appLogo;

		public boolean needUpdate;
		public String downUrl;

	}

	private ArrayList<InstalledAppInfo> getInstalledApps(boolean includeSysApps) {
		PackageManager packageManager = getActivity().getPackageManager();
		mInstalledAppInfosIndex = new HashMap<String, Integer>();
		ArrayList<InstalledAppInfo> res = new ArrayList<InstalledAppInfo>();
		List<PackageInfo> packs = packageManager.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo pkInfo = packs.get(i);

			if ((!includeSysApps)
					&& ((pkInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
				continue;
			}
			String packageName = pkInfo.packageName;

			if (packageManager.getLaunchIntentForPackage(packageName) == null) {
				continue;
			}

			InstalledAppInfo appInfo = new InstalledAppInfo();
			appInfo.appName = pkInfo.applicationInfo.loadLabel(packageManager)
					.toString();
			appInfo.packageName = packageName;
			appInfo.versionName = pkInfo.versionName;
			appInfo.versionCode = pkInfo.versionCode;
			appInfo.appLogo = pkInfo.applicationInfo.loadIcon(packageManager);
			mInstalledAppInfosIndex.put(appInfo.packageName, i);

			res.add(appInfo);
		}
		return res;
	}

}

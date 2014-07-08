package com.rui.android_client.component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.rui.android_client.R;
import com.rui.android_client.activity.AppDetailActivity;
import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.model.DownloadInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.utils.CollectionUtil;
import com.rui.android_client.utils.DownloadUtils;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.StringUtil;
import com.rui.android_client.utils.ThreadAid;
import com.rui.android_client.utils.ThreadListener;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class AppListView {

	protected Activity mActivity;
	protected RuiApp mApp;
	protected String mDownloadUrl;
	protected HashMap<String, Object> mParams;

	protected ListView mListView;
	protected ListAdapter mAdapter;
	protected View footerView;

	protected int mVisibleLastIndex = 0; // 最后的可视项索引
	protected int mVisibleItemCount; // 当前窗口可见项总数

	protected ArrayList<AppInfo> mAppInfos = new ArrayList<AppInfo>();

	protected boolean isLoading = false;

	protected int mPage = 1;
	
	private File rootFile;
	
	public BaseAdapter getAdapter() {
		return mAdapter;
	}
	
	public ArrayList<AppInfo> getAppInfos() {
		return mAppInfos;
	}

	public AppListView(Activity activity, ListView listView) {
		mActivity = activity;
		mApp = (RuiApp) mActivity.getApplication();
		mListView = listView;
		footerView = View.inflate(activity, R.layout.listview_foot, null);
		mListView.addFooterView(footerView);
		footerView.setVisibility(View.GONE);
		
		rootFile = DownloadUtils.createDirIfNotExists();

		initAdapter();

		mListView.setOnScrollListener(mOnScrollListener);
		mListView.setOnItemClickListener(mOnItemClickListener);
	}

	public void loadApps(String url, HashMap<String, Object> params) {
		mDownloadUrl = url;
		mParams = params;
		loadMore();
	}
	
	public void clearApps() {
		mPage = 1;
		mVisibleLastIndex = 0;
		mVisibleLastIndex = 0;
		mAppInfos.clear();
		mAdapter.notifyDataSetChanged();
	}

	protected void initAdapter() {
		mAdapter = new ListAdapter();
		mListView.setAdapter(mAdapter);
	}

	protected class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mAppInfos == null) {
				return 0;
			}
			return mAppInfos.size();
		}

		@Override
		public AppInfo getItem(int position) {
			if (CollectionUtil.isEmpty(mAppInfos)) {
				return null;
			}
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
				holder = new AppInfoViewHolder(mActivity);
			} else {
				holder = (AppInfoViewHolder) convertView;
			}
			AppInfo app = getItem(position);
			holder.setViewContent(position, app);
			return holder;
		}

	}

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			int itemsLastIndex = mAdapter.getCount() - 1; // 数据集最后一项的索引
			if (mVisibleLastIndex >= itemsLastIndex
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				// 如果是自动加载,可以在这里放置异步加载数据的代码
				Log.i("LOADMORE", "loading...");
				loadMore();
			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mVisibleItemCount = visibleItemCount;
			mVisibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		}
	};

	private void loadMore() {
		if (StringUtil.isBlank(mDownloadUrl)) {
			new Thread(" app listview url is blank!");
		}
		if (isLoading) {
			return;
		}
		isLoading = true;
		footerView.setVisibility(View.VISIBLE);

		RemoteManager remoteManager = RemoteManager.getPostOnceRemoteManager();
		Request request = remoteManager.createPostRequest(mDownloadUrl);
		if (mParams != null) {
			for (String keyword : mParams.keySet()) {
				request.addParameter(keyword, mParams.get(keyword));
			}
		}
		request.addParameter("page", mPage);
		mApp.asyInvoke(new ThreadAid(new GetAppListCallbackListener(), request));
	}

	private class GetAppListCallbackListener implements ThreadListener {

		@Override
		public void onPostExecute(final Response response) {
			final boolean isSuccess = response != null && response.isSuccess();
			ArrayList<AppInfo> tempAppInfos = null;
			if (isSuccess) {
				// 解析数据
				tempAppInfos = parseCallbackBody(response);
				mPage++;
			} else {
				// TODO load failed
			}
			final ArrayList<AppInfo> appInfos = tempAppInfos;
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (isSuccess) {
						mAppInfos.addAll(appInfos);
						mAdapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
						mListView.setSelection(mVisibleLastIndex
								- mVisibleItemCount + 1); // 设置选中项
					}
					footerView.setVisibility(View.GONE);
				}
			});
			isLoading = false;
		}

	}

	protected ArrayList<AppInfo> parseCallbackBody(final Response response) {
		List<AppInfo> localApps = mApp.getInstalledAppInfos();
		HashMap<String, Integer> localAppVersionCodes = new HashMap<String, Integer>();
		for (AppInfo item : localApps) {
			localAppVersionCodes.put(item.getPackageName(), item.getLocalVersionCode());
		}
		ArrayList<AppInfo> appinfos = new ArrayList<AppInfo>();
		JSONObject json = JsonUtil.getJsonObject(response.getModel());
		JSONArray jsonArray = JsonUtil.getJsonArray(json, "data");
		if (jsonArray != null) {
			for (int i = 0, size = jsonArray.length(); i < size; i++) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					AppInfo appInfo = AppInfoParser.getInstance().parse(
							jsonObject);
					
					if (localAppVersionCodes.containsKey(appInfo.getPackageName())) {
						if (Integer.parseInt(appInfo.getVersionValue()) > localAppVersionCodes.get(appInfo.getPackageName())) {
							appInfo.setNeedUpdate(true);
						}
					}
					if (StringUtil.isNotBlank(appInfo.getDownUrl())) {
						List<DownloadInfo> downloadInfos = RuiApp.mPersist.downloadInfoDao
								.getInfos(appInfo.getDownUrl());
						appInfo.setDownloadInfos(downloadInfos);
					}
					
					appinfos.add(appInfo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return appinfos;
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AppInfo app = mAdapter.getItem(position);
			if (app == null) {
				return;
			}
			Intent intent = new Intent(mActivity, AppDetailActivity.class);
			intent.putExtra("ID", app.getId());
			mActivity.startActivity(intent);
		}
	};
	
}

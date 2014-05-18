package com.rui.android_client.component;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.activity.AppDetailActivity;
import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.StringUtil;
import com.rui.android_client.utils.ThreadAid;
import com.rui.android_client.utils.ThreadListener;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class AppListView {

	private Activity mActivity;
	private RuiApp mApp;
	private String mDownloadUrl;
	private HashMap<String, Object> mParams;
	private int mPager;

	private ListView mListView;
	private ListAdapter mAdapter;
	private View footerView;

	private int mVisibleLastIndex = 0; // 最后的可视项索引
	private int mVisibleItemCount; // 当前窗口可见项总数

	private ArrayList<AppInfo> mAppInfos = new ArrayList<AppInfo>();

	private boolean isLoading = false;

	private int mPage = 1;

	public AppListView(Activity activity, ListView listView) {
		mActivity = activity;
		mApp = (RuiApp) mActivity.getApplication();
		mListView = listView;
		footerView = View.inflate(activity, R.layout.listview_foot, null);
		mListView.addFooterView(footerView);
		footerView.setVisibility(View.GONE);

		mAdapter = new ListAdapter();
		mListView.setAdapter(mAdapter);

		mListView.setOnScrollListener(mOnScrollListener);
		mListView.setOnItemClickListener(mOnItemClickListener);
	}

	public void loadApps(String url, HashMap<String, Object> params) {
		mDownloadUrl = url;
		mParams = params;
		loadMore();
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
				holder = new ViewHolder(mActivity);
			} else {
				holder = (ViewHolder) convertView;
			}
			AppInfo app = getItem(position);
			holder.setViewContent(app);
			return holder;
		}

		private class ViewHolder extends LinearLayout {

			public ImageView iconView;
			public TextView titleView;

			public ViewHolder(Context context) {
				super(context);
				View.inflate(context, R.layout.layout_app_listview_item, this);
				iconView = (ImageView) findViewById(R.id.icon);
				titleView = (TextView) findViewById(R.id.title);
			}

			public void setViewContent(AppInfo app) {
				// 异步加载图片
				RuiApp.fb.display(iconView, app.getIconUrl());
				titleView.setText(app.getMainTitle());
			}

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
			for(String keyword : mParams.keySet()) {
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
			if (isSuccess) {
				// 解析数据
				JSONObject json = JsonUtil.getJsonObject(response.getModel());
				JSONArray jsonArray = JsonUtil.getJsonArray(json, "data");
				if (jsonArray != null) {
					for (int i = 0, size = jsonArray.length(); i < size; i++) {
						try {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							mAppInfos.add(AppInfoParser.getInstance().parse(jsonObject));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				mPage++;
			} else {
				// TODO load failed
			}
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (isSuccess) {
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

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AppInfo app = mAdapter.getItem(position);
			Intent intent = new Intent(mActivity, AppDetailActivity.class);
			intent.putExtra("ID", app.getId());
			mActivity.startActivity(intent);
		}
	};

}

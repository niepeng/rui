package com.rui.android_client.component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

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
import com.rui.android_client.model.App;

public class AppListView {

	private Context mContext;
	private String mDownloadUrl;
	private int mPager;

	private ListView mListView;
	private ListAdapter mAdapter;
	private View footerView;

	private int mVisibleLastIndex = 0; // 最后的可视项索引
	private int mVisibleItemCount; // 当前窗口可见项总数

	private ArrayList<App> mApps = new ArrayList<App>();

	private LoadMoreAppTask mLoadMoreAppTask;

	private boolean isLoading = false;

	public AppListView(Context context, ListView listView) {
		mContext = context;
		mListView = listView;
		footerView = View.inflate(context, R.layout.listview_foot, null);
		mListView.addFooterView(footerView);

		mAdapter = new ListAdapter();
		mListView.setAdapter(mAdapter);

		mListView.setOnScrollListener(mOnScrollListener);
		mListView.setOnItemClickListener(mOnItemClickListener);
	}

	public void loadApps(String url) {
		mDownloadUrl = url;
		// loadMore();
		// TODO test
		for (int i = 0; i < 10; i++) {
			App app = new App();
			app.setIconUrl("http://youimg1.c-ctrip.com/target/tg/689/159/837/7434168f76ff4ef3b78686a4cb7a63ae.jpg");
			app.setMainTitle(" app : " + i);
			mApps.add(app);
		}
		mAdapter.notifyDataSetChanged();
	}

	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mApps == null) {
				return 0;
			}
			return mApps.size();
		}

		@Override
		public App getItem(int position) {
			return mApps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder(mContext);
			} else {
				holder = (ViewHolder) convertView;
			}
			App app = getItem(position);
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

			public void setViewContent(App app) {
				// TODO 异步加载图片
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
		if (isLoading) {
			return;
		}
		isLoading = true;
		if (mLoadMoreAppTask == null || mLoadMoreAppTask.isCancelled()) {
			mLoadMoreAppTask = new LoadMoreAppTask();
			try {
				// TODO 表忘记分页
				URL url = new URL(mDownloadUrl);
				mLoadMoreAppTask.execute(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private class LoadMoreAppTask extends AsyncTask<URL, Void, HttpResponse> {

		@Override
		protected void onPreExecute() {
			footerView.setVisibility(View.VISIBLE);
			// TODO listview add loading more foot view
			super.onPreExecute();
		}

		@Override
		protected HttpResponse doInBackground(URL... params) {
			// TODO 网络请求
			return null;
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			super.onPostExecute(result);
			if (result != null && result.getStatusLine().getStatusCode() == 200) {
				// TODO 解析数据
				mAdapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				mListView.setSelection(mVisibleLastIndex - mVisibleItemCount
						+ 1); // 设置选中项
			} else {
				// TODO load failed
			}
			// TODO remove footView
			footerView.setVisibility(View.GONE);
			mLoadMoreAppTask = null;
			isLoading = false;
		}

	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			App app = mAdapter.getItem(position);
			Intent intent = new Intent(mContext, AppDetailActivity.class);
			intent.putExtra("ID", app.getId());
			mContext.startActivity(intent);
		}
	};

}

package com.rui.android_client.component;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;

import com.rui.android_client.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AppListView {

	private Context mContext;
	private ListView mListView;
	private ListAdapter mAdapter;

	private int mVisibleLastIndex = 0; // 最后的可视项索引
	private int mVisibleItemCount; // 当前窗口可见项总数

	LoadMoreAppTask mLoadMoreAppTask;
	LoadImageTask mLoadImageTask;

	public AppListView(Context context, ListView listView) {
		mContext = context;
		mListView = listView;

		mAdapter = new ListAdapter();
		mListView.setAdapter(mAdapter);

		mListView.setOnScrollListener(mOnScrollListener);
	}

	public void loadApps() {
		loadMore();
	}

	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
		private class ViewHolder extends LinearLayout {

			public ViewHolder(Context context) {
				super(context);
				View.inflate(context, R.layout.layout_app_listview_item, this);
			}
			
		}

	}

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			int itemsLastIndex = mAdapter.getCount() - 1; // 数据集最后一项的索引
			int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& mVisibleLastIndex == lastIndex) {
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
		if (mLoadMoreAppTask == null || mLoadMoreAppTask.isCancelled()) {
			mLoadMoreAppTask = new LoadMoreAppTask();
			try {
				// TODO
				URL url = new URL(" api ");
				mLoadMoreAppTask.execute(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private class LoadMoreAppTask extends AsyncTask<URL, Void, HttpResponse> {

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
				loadAppImages();
			} else {
				// TODO load failed
			}
			mLoadMoreAppTask = null;
		}

	}

	// TODO 异步加载图片
	private void loadAppImages() {
		if (mLoadImageTask == null || mLoadImageTask.isCancelled()) {
			mLoadImageTask = new LoadImageTask();
			try {
				// TODO
				URL url = new URL(" api ");
				mLoadImageTask.execute(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private class LoadImageTask extends AsyncTask<URL, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(URL... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			mLoadImageTask = null;
		}

	}

}

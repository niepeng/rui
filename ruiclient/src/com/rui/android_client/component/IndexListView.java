package com.rui.android_client.component;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.rui.android_client.R;
import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.model.BannerInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.parse.BannerInfoParser;
import com.rui.android_client.utils.JsonUtil;
import com.rui.http.Response;

public class IndexListView extends AppListView {

	private ArrayList<BannerInfo> mBannerInfos = new ArrayList<BannerInfo>();
	private ArrayList<View> bannerImageViewList = new ArrayList<View>();

	public IndexListView(Activity activity, ListView listView) {
		super(activity, listView);
	}

	@Override
	protected void parseCallbackBody(Response response) {
		JSONObject json = JsonUtil.getJsonObject(response.getModel());
		JSONObject jsonData = JsonUtil.getJSONObject(json, "data");
		JSONArray bannerList = JsonUtil.getJsonArray(jsonData, "bannerList");
		if (bannerList != null) {
			for (int i = 0, size = bannerList.length(); i < size; i++) {
				try {
					JSONObject jsonObject = bannerList.getJSONObject(i);
					mBannerInfos.add(BannerInfoParser.getInstance().parse(
							jsonObject));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		JSONArray appList = JsonUtil.getJsonArray(jsonData, "appList");
		if (appList != null) {
			for (int i = 0, size = appList.length(); i < size; i++) {
				try {
					JSONObject jsonObject = appList.getJSONObject(i);
					mAppInfos
							.add(AppInfoParser.getInstance().parse(jsonObject));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void initAdapter() {
		mAdapter = new IndexListAdapter();
		mListView.setAdapter(mAdapter);
	}

	private class IndexListAdapter extends ListAdapter {

		private final static int TYPE_BANNER = 0;
		private final static int TYPE_APP_INFO = 1;

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			if (mBannerInfos != null && position == 0) {
				return TYPE_BANNER;
			}
			return TYPE_APP_INFO;
		}

		@Override
		public int getCount() {
			int count = super.getCount();
			if (mBannerInfos != null) {
				count++;
			}
			return count;
		}

		@Override
		public AppInfo getItem(int position) {
			if (getItemViewType(0) == TYPE_BANNER) {
				position--;
			}
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			if (type == TYPE_BANNER) {
				BannerViewHolder viewHolder = null;
				if (convertView == null) {
					viewHolder = new BannerViewHolder(mActivity);
				} else {
					viewHolder = (BannerViewHolder) convertView;
				}
				viewHolder.setBannerViewContent();
				return viewHolder;
			}
			return super.getView(position, convertView, parent);
		}
	}

	private class BannerViewHolder extends ViewHolder {

		BannerPagerAdapter mBannerPageAdapter;
		ViewPager mBannerViewPager;
		LinePageIndicator mBannerViewIndicator;

		public BannerViewHolder(Context context) {
			super(context);
			View.inflate(mActivity, R.layout.layout_banner, this);
			mBannerPageAdapter = new BannerPagerAdapter();
			mBannerViewPager = (ViewPager) findViewById(R.id.banner_viewpage);
			mBannerViewPager.setAdapter(mBannerPageAdapter);
			mBannerViewIndicator = (LinePageIndicator) findViewById(R.id.indicator);
			mBannerViewIndicator.setViewPager(mBannerViewPager);
		}

		public void setBannerViewContent() {
			bannerImageViewList.clear();
			for (BannerInfo item : mBannerInfos) {
				ImageView view = new ImageView(mActivity);
				view.setScaleType(ScaleType.FIT_XY);
				view.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				RuiApp.fb.display(view, item.getImageUrl());
				bannerImageViewList.add(view);
			}
			mBannerPageAdapter.notifyDataSetChanged();
		}

		class BannerPagerAdapter extends PagerAdapter {

			@Override
			public int getCount() {
				if (bannerImageViewList == null) {
					return 0;
				}
				return bannerImageViewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				if (position < getCount()) {
					container.removeView(bannerImageViewList.get(position));
				}
			}

			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(bannerImageViewList.get(position));
				return bannerImageViewList.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

		}

	}

}

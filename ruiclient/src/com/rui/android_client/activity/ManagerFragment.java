package com.rui.android_client.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rui.android_client.R;

public class ManagerFragment extends Fragment {

	private ArrayList<InstalledAppInfo> mAppInfos;

	private ListView mListView;
	private ListAdapter mListAdapter;

	public static ManagerFragment newInstance() {
		ManagerFragment fragment = new ManagerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_manager, null);
		mListView = (ListView) rootView.findViewById(R.id.list_view);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAppInfos = getPackages();
		mListAdapter.notifyDataSetChanged();
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
			holder.setViewContent(app);
			return holder;
		}

		protected class ViewHolder extends LinearLayout {

			public ImageView iconView;
			public TextView titleView;

			public ViewHolder(Context context) {
				super(context);
				View.inflate(context, R.layout.layout_app_listview_item, this);
				iconView = (ImageView) findViewById(R.id.icon);
				titleView = (TextView) findViewById(R.id.title);
			}

			public void setViewContent(InstalledAppInfo item) {
				iconView.setImageDrawable(item.appLogo);
				titleView.setText(item.appName);
			}

		}

	}

	private class InstalledAppInfo {
		public String appName = null;
		public String packageName = null;
		public String versionName = null;
		public int versionCode = 0;
		public Drawable appLogo;
		
		public boolean needUpdate;

		private void prettyPrint() {
			Log.d("installed app info : ", appName + "\t" + packageName + "\t"
					+ versionName + "\t" + versionCode);
		}
	}
	
	

	private ArrayList<InstalledAppInfo> getPackages() {
		ArrayList<InstalledAppInfo> apps = getInstalledApps(false);
		final int max = apps.size();
		for (int i = 0; i < max; i++) {
			apps.get(i).prettyPrint();
		}
		return apps;
	}

	private ArrayList<InstalledAppInfo> getInstalledApps(boolean getSysPackages) {
		Context context = getActivity();
		ArrayList<InstalledAppInfo> res = new ArrayList<InstalledAppInfo>();
		List<PackageInfo> packs = context.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((!getSysPackages) && (p.versionName == null)) {
				continue;
			}
			InstalledAppInfo newInfo = new InstalledAppInfo();
			newInfo.appName = p.applicationInfo.loadLabel(
					context.getPackageManager()).toString();
			newInfo.packageName = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			newInfo.appLogo = p.applicationInfo.loadIcon(context
					.getPackageManager());
			res.add(newInfo);
		}
		return res;
	}

}

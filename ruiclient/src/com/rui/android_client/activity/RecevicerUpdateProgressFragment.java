package com.rui.android_client.activity;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rui.android_client.model.AppInfo;
import com.rui.android_client.service.DownloadService;
import com.rui.android_client.utils.StringUtil;

public abstract class RecevicerUpdateProgressFragment extends Fragment {

	private UpdateProgressReceiver mUpdateProgressReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mUpdateProgressReceiver = new UpdateProgressReceiver();
		getActivity().registerReceiver(mUpdateProgressReceiver,
				new IntentFilter(DownloadService.ACTION_UPDATE_PROGRESS));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		if (mUpdateProgressReceiver != null) {
			getActivity().unregisterReceiver(mUpdateProgressReceiver);
		}
		super.onDestroyView();
	}

	private class UpdateProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String downloadUrl = intent.getStringExtra("downloadUrl");
			String packageName = intent.getStringExtra("packageName");
			updateProgress(downloadUrl, packageName);
		}
	}

	protected abstract void updateProgress(String downloadUrl,
			String packageName);

	protected void updateProgress(ArrayList<AppInfo> mAppInfos,
			BaseAdapter mListAdapter, String downloadUrl, String packageName) {
		for (AppInfo item : mAppInfos) {
			if (StringUtil.isNotBlank(downloadUrl)
					&& downloadUrl.equals(item.getDownUrl())) {
				item.setDownloadInfos(RuiApp.mPersist.downloadInfoDao
						.getInfos(downloadUrl));
			}
			if (StringUtil.isNotBlank(packageName)
					&& packageName.equals(item.getPackageName())) {
				item.setDownloadInfos(RuiApp.mPersist.downloadInfoDao
						.getInfosByPackage(packageName));
			}
		}
		mListAdapter.notifyDataSetChanged();
	}
}

package com.rui.android_client.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rui.android_client.R;
import com.rui.android_client.component.AppListView;

public class YouLikeFragment extends Fragment {

	private AppListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ListView rootView = (ListView) inflater.inflate(
				R.layout.layout_app_listview, null);
		mListView = new AppListView(getActivity(), rootView);
		// TODO
		mListView.loadApps("");
		return rootView;
	}

}

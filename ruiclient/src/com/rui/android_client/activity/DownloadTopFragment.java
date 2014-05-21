package com.rui.android_client.activity;

import java.util.HashMap;

import com.rui.android_client.R;
import com.rui.android_client.component.AppListView;
import com.rui.android_client.component.IndexListView;
import com.rui.http.Config;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DownloadTopFragment extends Fragment {
	
	private AppListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_app_listview, null);
		mListView = new IndexListView(getActivity(),
				(ListView) rootView.findViewById(R.id.list_view));
		
		String url = Config.getConfig().getProperty(Config.Names.GAME_LIST);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", 3);
		mListView.loadApps(url, params);
		return rootView;
	}

}

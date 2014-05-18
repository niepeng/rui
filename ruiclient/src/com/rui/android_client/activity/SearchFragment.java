package com.rui.android_client.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.rui.android_client.R;
import com.rui.android_client.component.AppListView;
import com.rui.android_client.utils.StringUtil;

public class SearchFragment extends Fragment {
	
	private EditText mSearchKeyView;
	private Button mSearchBtn;
	private AppListView mListView;
	
	public static SearchFragment newInstance() {
		SearchFragment fragment = new SearchFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, null);

		mSearchKeyView = (EditText) rootView.findViewById(R.id.search_key);
		mSearchBtn = (Button) rootView.findViewById(R.id.search_btn);
		ListView listView = (ListView) rootView.findViewById(R.id.list_view);
		mListView = new AppListView(getActivity(), listView);
		
		mSearchBtn.setOnClickListener(onSearchClick);
		
		return rootView;
	}
	
	private View.OnClickListener onSearchClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (StringUtil.isBlank(mSearchKeyView.getText().toString())) {
				// TODO
				String url = "";
				mListView.loadApps(url);
			}
		}
	};

}

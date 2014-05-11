package com.rui.android_client.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.component.DProgressDialog;
import com.rui.android_client.model.AppCategory;

public class CategoryFragment extends Fragment {

	private GetAppTypesTask mGetAppTypesTask;

	private ListView mListView;
	private ListAdapter mListAdapter;

	private ArrayList<AppCategory> categories = new ArrayList<AppCategory>();

	public static CategoryFragment newInstance() {
		CategoryFragment fragment = new CategoryFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_category, null);
		mListView = (ListView) rootView.findViewById(R.id.list_view);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);
		return rootView;
	}

	@Override
	public void onResume() {
		loadTypes();
		super.onResume();
	}

	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return categories.size();
		}

		@Override
		public AppCategory getItem(int position) {
			return categories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		private class ViewHolder extends LinearLayout {

			public ImageView iconView;
			public TextView titleView;

			public ViewHolder() {
				super(getActivity());
				View.inflate(getActivity(),
						R.layout.layout_category_listview_item, this);
				iconView = (ImageView) findViewById(R.id.icon);
				titleView = (TextView) findViewById(R.id.title);
			}

			public void setViewContent(AppCategory category) {
				// TODO 异步加载图片
				titleView.setText(category.getName());
			}

		}

	}

	private void loadTypes() {
		if (mGetAppTypesTask == null || mGetAppTypesTask.isCancelled()) {
			mGetAppTypesTask = new GetAppTypesTask();
			try {
				// TODO
				URL url = new URL("http://baidu.com");
				mGetAppTypesTask.execute(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private class GetAppTypesTask extends AsyncTask<URL, Void, HttpResponse> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			DProgressDialog.show(getActivity(), null, null);
		}

		@Override
		protected HttpResponse doInBackground(URL... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			DProgressDialog.closeDialog(getActivity());
			mGetAppTypesTask = null;
		}

	}

}

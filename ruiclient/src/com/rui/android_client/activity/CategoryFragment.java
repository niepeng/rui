package com.rui.android_client.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.model.AppCategory;
import com.rui.android_client.parse.AppCategoryParser;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.ThreadAid;
import com.rui.android_client.utils.ThreadListener;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class CategoryFragment extends Fragment {
	
	private RuiApp mApp;

	private ListView mListView;
	private ListAdapter mListAdapter;

	private ArrayList<AppCategory> mCategories = new ArrayList<AppCategory>();

	public static CategoryFragment newInstance() {
		CategoryFragment fragment = new CategoryFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApp = (RuiApp) getActivity().getApplication();
		
		View rootView = inflater.inflate(R.layout.fragment_category, null);
		mListView = (ListView) rootView.findViewById(R.id.list_view);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppCategory item = mListAdapter.getItem(position);
				Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
				intent.putExtra("sub_categories", item.getSubCategories());
				getActivity().startActivity(intent);
			}
		});
		
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
			return mCategories.size();
		}

		@Override
		public AppCategory getItem(int position) {
			return mCategories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
			} else {
				holder = (ViewHolder) convertView;
			}
			holder.setViewContent(getItem(position));
			return holder;
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
				RuiApp.fb.display(iconView, category.getIconUrl());
				titleView.setText(category.getName());
			}

		}

	}

	private void loadTypes() {
		String url = Config.getConfig().getProperty(Config.Names.CAT_LIST_URL);
		RemoteManager remoteManager = RemoteManager.getPostOnceRemoteManager();
		Request request = remoteManager.createPostRequest(url);
		mApp.asyInvoke(new ThreadAid(new GetAppCategoryListCallbackListener(), request));
	}
	
	private class GetAppCategoryListCallbackListener implements ThreadListener {

		@Override
		public void onPostExecute(final Response response) {
			final boolean isSuccess = response != null && response.isSuccess();
			if (isSuccess) {
				// 解析数据
				mCategories.clear();
				JSONObject json = JsonUtil.getJsonObject(response.getModel());
				JSONArray jsonArray = JsonUtil.getJsonArray(json, "data");
				if (jsonArray != null) {
					for (int i = 0, size = jsonArray.length(); i < size; i++) {
						try {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							mCategories.add(AppCategoryParser.getInstance().parse(jsonObject));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				// TODO load failed
			}
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mListAdapter.notifyDataSetChanged();
				}
			});
		}

	}

}

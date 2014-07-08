package com.rui.android_client.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
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
import com.rui.android_client.model.SubCategory;

public class SubCategoryActivity extends BaseActivity {

	private RuiApp mApp;

	private ListView mListView;
	private ListAdapter mListAdapter;

	private ArrayList<SubCategory> mCategories = new ArrayList<SubCategory>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_category);
		mCategories = (ArrayList<SubCategory>) getIntent().getSerializableExtra(
				"sub_categories");
		mListView = (ListView) findViewById(R.id.list_view);
		mListAdapter = new ListAdapter();
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SubCategory item = mListAdapter.getItem(position);
				Intent intent = new Intent(SubCategoryActivity.this,
						CategoryItemActivity.class);
				intent.putExtra("catId", item.getId());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mCategories.size();
		}

		@Override
		public SubCategory getItem(int position) {
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
				super(SubCategoryActivity.this);
				View.inflate(SubCategoryActivity.this,
						R.layout.layout_category_listview_item, this);
				iconView = (ImageView) findViewById(R.id.icon);
				titleView = (TextView) findViewById(R.id.title);
			}

			public void setViewContent(SubCategory category) {
				RuiApp.fb.display(iconView, category.getIconUrl());
				titleView.setText(category.getName());
			}

		}

	}

}

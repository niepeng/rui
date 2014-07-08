package com.rui.android_client.activity;

import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.rui.android_client.R;
import com.rui.android_client.component.AppListView;
import com.rui.http.Config;

public class CategoryItemActivity extends BaseActivity {

	private AppListView mListView;

	private long mCatId;
	private String mUrl;
	private HashMap<String, Object> mParams;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_app_listview);
		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		init();
	}

	private void init() {
		initView();
		initData();
		initViewContent();
	}

	private void initView() {
		initActionBar();
		mListView = new AppListView(this,
				(ListView) findViewById(R.id.list_view));
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		// TODO
		actionBar.setTitle(R.string.detail);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
	}

	private void initData() {
		mCatId = getIntent().getLongExtra("catId", -1);
		mUrl = Config.getConfig().getProperty(Config.Names.APP_LIST);
		mParams = new HashMap<String, Object>();
		mParams.put("catId", mCatId);
	}

	private void initViewContent() {
		mListView.loadApps(mUrl, mParams);
	}

}

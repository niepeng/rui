package com.rui.android_client.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.rui.android_client.R;

public class AboutUsActivity extends BaseActivity {

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
		setContentView(R.layout.activity_aboutus);
		init();
	}

	private void init() {
		initView();
		TextView currentVersionView = (TextView) findViewById(R.id.current_version);
		currentVersionView.setText("当前版本：" + RuiApp.getVersionName());
		TextView contactMeView = (TextView) findViewById(R.id.contact_me);
		contactMeView.setText("联系我：139-8949-8009");
	}

	private void initView() {
		initActionBar();
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.about_us);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
	}

}

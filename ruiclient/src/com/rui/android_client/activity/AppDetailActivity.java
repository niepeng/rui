package com.rui.android_client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.rui.android_client.R;

public class AppDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_detail);
		init();
	}
	
	private void init() {
		initView();
		initData();
		initViewContent();
	}
	
	private void initView() {
	}
	
	private void initData() {
		
	}
	
	private void initViewContent() {
		
	}

}

package com.rui.android_client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rui.android_client.R;

public class AppDetailActivity extends Activity {
	
	private long appId;
	
	private ImageView iconView;
	private TextView titleView;

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
		appId = getIntent().getLongExtra("ID", 0);
	}
	
	private void initViewContent() {
		
	}

}

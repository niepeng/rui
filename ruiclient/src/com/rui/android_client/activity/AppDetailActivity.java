package com.rui.android_client.activity;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.component.DProgressDialog;

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
	}
	
	private void initView() {
	}
	
	private void initData() {
		appId = getIntent().getLongExtra("ID", 0);
	}
	
	private void initViewContent() {
		
	}
	
	private class getAppDetail extends AsyncTask<Void, Void, HttpResponse> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			DProgressDialog.show(AppDetailActivity.this, null, null);
		}

		@Override
		protected HttpResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(HttpResponse result) {
			super.onPostExecute(result);
			if (result != null && result.getStatusLine().getStatusCode() == 200) {
				// TODO 解析body数据
				initViewContent();
			} else {
				// TODO 网络问题或其他，怎么破？
			}
			DProgressDialog.closeDialog(AppDetailActivity.this);
		}
		
	}

}

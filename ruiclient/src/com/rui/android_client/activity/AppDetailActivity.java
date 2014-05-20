package com.rui.android_client.activity;

import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.ThreadAid;
import com.rui.android_client.utils.ThreadListener;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class AppDetailActivity extends BaseActivity {

	private long appId;

	private AppInfo mAppInfo;

	private ImageView iconView;
	private TextView mainTitleView, subTitleView, detailInfoView;
	private Button downloadBtn;

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
		setContentView(R.layout.activity_app_detail);
		init();
	}

	private void init() {
		initView();
		initData();
	}

	private void initView() {
		initActionBar();
		iconView = (ImageView) findViewById(R.id.icon);
		mainTitleView = (TextView) findViewById(R.id.main_title);
		subTitleView = (TextView) findViewById(R.id.sub_title);
		detailInfoView = (TextView) findViewById(R.id.detail_info);
		downloadBtn = (Button) findViewById(R.id.download_btn);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.detail);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
	}

	private void initData() {
		appId = getIntent().getLongExtra("ID", 0);
		RemoteManager remoteManager = RemoteManager.getPostOnceRemoteManager();
		Request request = remoteManager.createPostRequest(Config.getConfig()
				.getProperty(Config.Names.APP_DETAIL_URL));
		request.addParameter("id", appId);
		mApp.asyInvoke(new ThreadAid(new GetAppInfoCallbackListener(), request));
	}

	private void initViewContent() {
		if (mAppInfo == null) {
			return;
		}
		RuiApp.fb.display(iconView, mAppInfo.getIconUrl());
		mainTitleView.setText(mAppInfo.getMainTitle());
	}

	private class GetAppInfoCallbackListener implements ThreadListener {

		@Override
		public void onPostExecute(Response response) {
			if (response != null && response.isSuccess()) {
				Object result = response.getModel();
				JSONObject jsonRoot = JsonUtil.getJsonObject(result);
				JSONObject jsonAppDetail = JsonUtil.getJSONObject(
						JsonUtil.getJSONObject(jsonRoot, "data"), "appDetail");
				if (jsonAppDetail != null) {
					mAppInfo = AppInfoParser.getInstance().parse(jsonAppDetail);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							initViewContent();
						}
					});
				}
			} else {
				// TODO
			}
		}

	}

}

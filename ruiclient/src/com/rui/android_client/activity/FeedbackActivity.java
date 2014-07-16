package com.rui.android_client.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rui.android_client.R;
import com.rui.android_client.utils.PreferenceUtil;
import com.rui.android_client.utils.StringUtil;
import com.rui.android_client.utils.ThreadAid;
import com.rui.android_client.utils.ThreadListener;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class FeedbackActivity extends BaseActivity {

	private EditText mInputView;
	private Button mSubmitBtn;

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
		setContentView(R.layout.activity_feedback);
		init();
	}

	private void init() {
		initView();
	}

	private void initView() {
		initActionBar();
		mInputView = (EditText) findViewById(R.id.input);
		mSubmitBtn = (Button) findViewById(R.id.submit_btn);
		mSubmitBtn.setOnClickListener(onSubmitFeedbackClick);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.feedback);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
	}

	private View.OnClickListener onSubmitFeedbackClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String text = mInputView.getText().toString();
			if (StringUtil.isBlank(text)) {
				return;
			}

			RemoteManager remoteManager = RemoteManager
					.getPostOnceRemoteManager();
			Request request = remoteManager.createPostRequest(Config
					.getConfig().getProperty(Config.Names.FEEDBACK_URL));
			request.addParameter("deviceId", mApp.getDeviceId());
			request.addParameter("userId", PreferenceUtil.getUserId());
			request.addParameter("content", text);
			mApp.asyInvoke(new ThreadAid(new SubmitFeedbackCallbackListener(),
					request));
		}
	};

	private class SubmitFeedbackCallbackListener implements ThreadListener {

		@Override
		public void onPostExecute(final Response response) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (response != null && response.isSuccess()) {
						Toast.makeText(FeedbackActivity.this,
								R.string.feedback_success, Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Toast.makeText(FeedbackActivity.this,
								R.string.feedback_failed, Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
		}
	}

}

package com.rui.android_client.activity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rui.android_client.R;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.ThreadAid;
import com.rui.android_client.utils.ThreadListener;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class MoreFragment extends Fragment {

	private Button upgradeBtn, aboutUsBtn, feedbackBtn;

	private RuiApp mApp;

	public static MoreFragment newInstance() {
		MoreFragment fragment = new MoreFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mApp = (RuiApp) getActivity().getApplication();

		View rootView = inflater.inflate(R.layout.fragment_more, null);
		upgradeBtn = (Button) rootView.findViewById(R.id.upgrade_version_btn);
		aboutUsBtn = (Button) rootView.findViewById(R.id.about_us_btn);
		feedbackBtn = (Button) rootView.findViewById(R.id.feedback_btn);

		upgradeBtn.setOnClickListener(onUpgradeVersionClick);
		aboutUsBtn.setOnClickListener(onAboutUsClick);
		feedbackBtn.setOnClickListener(onFeedbackClick);

		return rootView;
	}

	private View.OnClickListener onUpgradeVersionClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RemoteManager remoteManager = RemoteManager
					.getPostOnceRemoteManager();
			Request request = remoteManager.createPostRequest(Config
					.getConfig().getProperty(Config.Names.CHECK_UPDATE_URL));
			mApp.asyInvoke(new ThreadAid(new CheckVersionCallbackListener(),
					request));
		}
	};

	private View.OnClickListener onAboutUsClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), AboutUsActivity.class);
			getActivity().startActivity(intent);
		}
	};

	private View.OnClickListener onFeedbackClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), FeedbackActivity.class);
			getActivity().startActivity(intent);
		}
	};

	private class CheckVersionCallbackListener implements ThreadListener {

		@Override
		public void onPostExecute(Response response) {
			if (response != null && response.isSuccess()) {
				JSONObject json = JsonUtil.getJsonObject(response.getModel());
				JSONObject jsonData = JsonUtil.getJSONObject(json, "data");
				long versionCode = JsonUtil.getLong(jsonData,
						"lastAndroidVersion", -1l);
				if (versionCode > RuiApp.getVersionCode()) {
					alertNewVersionFoundDialog(jsonData);
				} else {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getActivity(),
									R.string.no_new_version, Toast.LENGTH_LONG)
									.show();
						}
					});
				}
			} else {

			}
		}

		private void alertNewVersionFoundDialog(JSONObject jsonData) {
			final String lastAndroidClientUrl = JsonUtil.getString(jsonData,
					"lastAndroidUrl", null);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					final AlertDialog dialog = builder.create();
					dialog.setTitle(R.string.found_new_version);
					dialog.setButton(AlertDialog.BUTTON_POSITIVE,
							getString(R.string.cancel),
							onCloseUpgradeDialogClick(dialog));
					dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
							getString(R.string.upgrade),
							onUpgradeVersionClick(lastAndroidClientUrl));
					dialog.show();

				}

				private android.content.DialogInterface.OnClickListener onCloseUpgradeDialogClick(
						final AlertDialog alertDialog) {
					return new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.dismiss();
						}
					};
				}

				private android.content.DialogInterface.OnClickListener onUpgradeVersionClick(
						final String lastAndroidClientUrl) {
					return new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.putExtra("lastAndroidClientUrl",
									lastAndroidClientUrl);
							intent.setClass(getActivity(),
									UpdateClientActivity.class);
							getActivity().startActivity(intent);
						}

					};
				}
			});
		}

	}

}

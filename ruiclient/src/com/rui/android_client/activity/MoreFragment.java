package com.rui.android_client.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.rui.android_client.R;

public class MoreFragment extends Fragment {

	private Button upgradeBtn, aboutUsBtn, feedbackBtn, helpCenterBtn;

	public static MoreFragment newInstance() {
		MoreFragment fragment = new MoreFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_more, null);
		upgradeBtn = (Button) rootView.findViewById(R.id.upgrade_version_btn);
		aboutUsBtn = (Button) rootView.findViewById(R.id.about_us_btn);
		feedbackBtn = (Button) rootView.findViewById(R.id.feedback_btn);
		helpCenterBtn = (Button) rootView.findViewById(R.id.help_center_btn);

		upgradeBtn.setOnClickListener(onUpgradeVersionClick);
		aboutUsBtn.setOnClickListener(onAboutUsClick);
		feedbackBtn.setOnClickListener(onFeedbackClick);
		helpCenterBtn.setOnClickListener(onHelpCenterClick);
		
		return rootView;
	}

	private View.OnClickListener onUpgradeVersionClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	private View.OnClickListener onAboutUsClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	private View.OnClickListener onFeedbackClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	private View.OnClickListener onHelpCenterClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

}

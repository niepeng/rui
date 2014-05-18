package com.rui.android_client.component;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.utils.StringUtil;

public class DProgressDialog extends DialogFragment {

	public static final String DIALOG_TAG_TRANSPARENT_PROGRESS = "DIALOG_TAG_TRANSPARENT_PROGRESS";

	private final static String KEY_TITLE = "title";
	private final static String KEY_MESSAGE = "message";
	private final static String KEY_INDETERMINATE = "indeterminate";
	private final static String KEY_CANCELABLE = "cancelable";

	private String mTitle, mMessage;
	private boolean mIndeterminate, mCancelable;

	public static DProgressDialog show(Activity context, CharSequence title,
			CharSequence message) {
		return show(context, title, message, false);
	}

	public static DProgressDialog show(Activity context, CharSequence title,
			CharSequence message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}

	public static DProgressDialog show(Activity context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable, null);
	}

	public static void closeDialog(Activity activity) {
		if (activity == null || activity.isFinishing()) {
			return;
		}
		DProgressDialog progressDialog = (DProgressDialog) activity
				.getFragmentManager().findFragmentByTag(
						DIALOG_TAG_TRANSPARENT_PROGRESS);
		if (progressDialog != null) {
			progressDialog.dismissAllowingStateLoss();
		}
	}

	private static DProgressDialog show(Activity context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		closeDialog(context);
		DProgressDialog dialog = new DProgressDialog();
		Bundle args = new Bundle();
		if (title != null) {
			args.putString(KEY_TITLE, title.toString());
		}
		if (message != null) {
			args.putString(KEY_MESSAGE, message.toString());
		}
		args.putBoolean(KEY_INDETERMINATE, indeterminate);
		args.putBoolean(KEY_CANCELABLE, cancelable);
		dialog.setArguments(args);
		dialog.show(context.getFragmentManager(),
				DIALOG_TAG_TRANSPARENT_PROGRESS);
		return dialog;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_TITLE, getArguments().getString(KEY_TITLE));
		outState.putString(KEY_MESSAGE, getArguments().getString(KEY_MESSAGE));
		outState.putBoolean(KEY_INDETERMINATE,
				getArguments().getBoolean(KEY_INDETERMINATE));
		outState.putBoolean(KEY_CANCELABLE,
				getArguments().getBoolean(KEY_CANCELABLE));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL,
				R.style.Theme_Rui_Light_TransparentProgressDialog);
		if (savedInstanceState != null) {
			mTitle = savedInstanceState.getString(KEY_TITLE);
			mMessage = savedInstanceState.getString(KEY_MESSAGE);
			mIndeterminate = savedInstanceState.getBoolean(KEY_INDETERMINATE);
			mCancelable = savedInstanceState.getBoolean(KEY_CANCELABLE);
		} else {
			mTitle = getArguments().getString(KEY_TITLE);
			mMessage = getArguments().getString(KEY_MESSAGE);
			mIndeterminate = getArguments().getBoolean(KEY_INDETERMINATE);
			mCancelable = getArguments().getBoolean(KEY_CANCELABLE);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(mCancelable);

		Dialog dialog;
		if (StringUtil.isBlank(mTitle) && StringUtil.isBlank(mMessage)) {
			dialog = new Dialog(getActivity(),
					R.style.Theme_Rui_Light_TransparentProgressDialog);
		} else {
			dialog = new Dialog(getActivity(), R.style.Theme_Rui_Light_Dialog);
		}
		dialog.setCancelable(mCancelable);
		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View bar = layoutInflater.inflate(R.layout.progress_small, null);
		View titleWrap = bar.findViewById(R.id.title_wrap);
		TextView messageView = (TextView) bar
				.findViewById(R.id.progress_message);
		if (StringUtil.isNotBlank(mTitle)) {
			titleWrap.setVisibility(View.VISIBLE);
			((TextView) bar.findViewById(R.id.title)).setText(mTitle);
		} else {
			titleWrap.setVisibility(View.GONE);
		}
		if (StringUtil.isNotBlank(mMessage)) {
			messageView.setVisibility(View.VISIBLE);
			messageView.setText(mMessage);
		} else {
			messageView.setVisibility(View.GONE);
		}
		dialog.addContentView(bar, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		return dialog;
	}

}

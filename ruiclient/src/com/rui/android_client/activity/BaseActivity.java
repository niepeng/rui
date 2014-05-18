package com.rui.android_client.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Message;

import com.rui.android_client.R;

public class BaseActivity extends Activity {
	
	protected void alert(CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alertDialog = builder.create();
		alertDialog.setMessage(message);
		String buttonLabel = getString(R.string.ok);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonLabel, (Message) null);
		alertDialog.show();
	}

}

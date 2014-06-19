package com.rui.android_client.utils;


import java.util.Formatter;

import com.rui.android_client.activity.RuiApp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ViewUtils {

	@SuppressWarnings("resource")
	public static String format(String message, Object... arg) {
		return new Formatter().format(message, arg).toString();
	}
	
	@SuppressWarnings("resource")
	public static String format(int message, Object... arg) {
		return new Formatter().format(getText(message), arg).toString();
	}

	public static String getText(int id) {
		return RuiApp.context.getResources().getString(id);
	}

	public static int getColor(int id) {
		return RuiApp.context.getResources().getColor(id);
	}

	public static ColorStateList getColors(int id) {
		try {
			XmlResourceParser parser = RuiApp.context.getResources()
					.getXml(id);
			ColorStateList colors = ColorStateList.createFromXml(
					RuiApp.context.getResources(), parser);
			return colors;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Drawable getDrawable(int id) {
		return RuiApp.context.getResources().getDrawable(id);
	}

	public static int getId(String variableName, String type) {
		int resourceId = RuiApp.context.getResources().getIdentifier(
				variableName, type, RuiApp.context.getPackageName());
		if (resourceId == 0) {
			return -1;
		} else {
			return resourceId;
		}

	}

	public static int getInteger(int id) {
		return RuiApp.context.getResources().getInteger(id);
	}

	public static float getDimens(int id) {
		return RuiApp.context.getResources().getDimension(id);
	}

	public static void hideSoftKeyboard(Context context, EditText editText) {
		if (editText == null) {
			return;
		}
		InputMethodManager mInputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),
				0);
	}

	public static void showSoftKeyboardDelayed(final Context context,
			final EditText editText, long delayMillis) {
		editText.postDelayed(new Runnable() {

			@Override
			public void run() {
				editText.setSelection(editText.getText().toString().length());
			}
		}, delayMillis);
	}
	
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}

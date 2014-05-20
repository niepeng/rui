package com.rui.android_client.utils;

import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
	public static String getGMTOffset() {
		int offset = TimeZone.getDefault().getRawOffset();
		String text = String.format(Locale.US, "GMT%s%02d:%02d",
				offset >= 0 ? "+" : "-", Math.abs(offset / 3600000),
				Math.abs((offset / 60000) % 60));
		return text;
	}

}

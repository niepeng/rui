package com.rui.android_client.utils;

import java.math.BigDecimal;

public class NumberUtils {

	public static int parse(String s) {
		if (s == null) {
			return 0;
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static float setScale(float value) {
		return setScale(value, 2);
	}
	
	public static float setScale(float value, int scale) {
		BigDecimal b = new BigDecimal(value);
		float result = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return result;
	}
}
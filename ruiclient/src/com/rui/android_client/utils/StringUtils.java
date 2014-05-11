package com.rui.android_client.utils;

import java.util.List;

public class StringUtils {

	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String subString(String str, int maxLength) {
		if (isNotEmpty(str) && str.length() > maxLength) {
			str = str.substring(0, maxLength);
		}
		return str;
	}

	public static String join(List<String> values, String separator) {
		if (values == null || values.size() == 0) {
			return null;
		}
		String first = values.get(0);
		if (values.size() == 1) {
			return first;
		}
		StringBuffer buf = new StringBuffer(256);
		buf.append(first);
		for (int i = 1; i < values.size(); i++) {
			buf.append(separator);
			buf.append(values.get(i));
		}
		return buf.toString();
	}

	public static boolean isInvalidLength(String str, int min, int max) {
		if (str == null)
			return false;
		int length = str.trim().length();
		if (length < min || length > max) {
			return true;
		}
		return false;
	}

	/**
	 * 空对象和空字符串认为是相等的
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEqual(String str1, String str2) {
		if (isEmpty(str1) && isEmpty(str2)) {
			return true;
		}
		if (str1 != null) {
			return str1.equals(str2);
		} else
			return str2.equals(str1);
	}
	
	public static boolean isNotEqual(String str1, String str2) {
		return !isEqual(str1, str2);
	}

}

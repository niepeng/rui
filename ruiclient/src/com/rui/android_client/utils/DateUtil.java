package com.rui.android_client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lsb
 *
 * @date 2012-5-29 下午11:31:09
 */
public class DateUtil {
	private static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_FMT_YMD = "MM-dd";
	
	public static final String DATE_MMddHHmm = "MM-dd HH:mm";
	
	public static final String DATE_YMD_H = "yyyy-MM-dd HH点";
	
	/**
	 * date1比date2更新（更迟，更晚），返回true，否则返回false
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isNewer(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		long d1 = date1.getTime();
		long d2 = date2.getTime();
		return d1 > d2;
	}
	
	public static Date parse(String input, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			return sdf.parse(input);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Date parseNoException(String input) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FMT);
		try {
			return sdf.parse(input);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date parse(String input) {
		return parse(input, DEFAULT_DATE_FMT);
	}
	
	public static String format(Date date, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}
	
	public static String format(Date date) {
		return format(date, DEFAULT_DATE_FMT);
	}
	
	public static long getAlarmTime() {
		Date date = new Date();
		return date.getTime() + 1000 * 30;
	}

}

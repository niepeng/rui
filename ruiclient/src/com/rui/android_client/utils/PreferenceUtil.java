package com.rui.android_client.utils;

import android.content.SharedPreferences.Editor;

import com.rui.android_client.activity.RuiApp;

public class PreferenceUtil {

	public static final String SP_NAME = "com.linkshop.client.uxiang";

	public static class KEYS {

		public static final String CITY = "city";
		public static final String CITY_ID = "city_id";
		public static final String CITY_COOPNAME = "city_coop_name";

		public static final String LOGIN_FLAG = "login_flag";
		public static final String DEVICE_ID = "device_id";
		public static final String USER_ID = "user_id";
		public static final String USER_NAME = "user_name";
		public static final String USER_PSW = "user_psw";
		public static final String USER_FACE = "user_face";
		public static final String USER_UBI = "user_ubi";

		public static final String LAST_PAYWAY = "last_payway";
		public static final String CHECKED_ADDRESSS_ID = "checked_addresss_id";

		// 每个城市购物车不同，添加城市id才是真正的key
		public static final String SHOPPINGLIST_PREFIX = "shoppinglist_prefix";
	}

	// --------------------------- 城市name ---------------------------
	public static void updateCityInfo(String city, long cityId, String coopName) {
		Editor editor = RuiApp.mPref.edit();
		editor.putString(KEYS.CITY, city);
		editor.putLong(KEYS.CITY_ID, cityId);
		editor.putString(KEYS.CITY_COOPNAME, coopName);
		editor.commit();
	}

	// --------------------------- 设备编号 ----------------------------
	public static void addDeviceId(String deviceId) {
		Editor editor = RuiApp.mPref.edit();
		editor.putString(KEYS.DEVICE_ID, deviceId);
		editor.commit();
	}

	public static String getDeviceId() {
		return RuiApp.mPref.getString(KEYS.DEVICE_ID, null);
	}

	// --------------------------- 用户id ----------------------------
	public static long getUserId() {
		return RuiApp.mPref.getLong(KEYS.USER_ID, 0L);
	}

	public static void setUserId(long userId) {
		Editor editor = RuiApp.mPref.edit();
		editor.putLong(KEYS.USER_ID, userId);
		editor.commit();
	}

	public static boolean getLoginFlag() {
		return RuiApp.mPref.getBoolean(KEYS.LOGIN_FLAG, false);
	}

	public static void setLoginFlag(boolean flag) {
		Editor editor = RuiApp.mPref.edit();
		editor.putBoolean(KEYS.LOGIN_FLAG, flag);
		editor.commit();
	}

}

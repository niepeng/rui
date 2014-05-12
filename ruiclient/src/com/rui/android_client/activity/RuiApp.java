package com.rui.android_client.activity;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.tsz.afinal.FinalBitmap;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.rui.android_client.utils.DateUtils;
import com.rui.android_client.utils.StringUtils;

public class RuiApp extends Application {
	
	public static Context context;
	public static int SDK_INT;
	
	public static FinalBitmap fb;
	
	private boolean needInit = true;
	
	private static int mScreenWidth, mScreenHeight;

	private static String mUserAgentInfo;
	
	private static int mVersionCode;
	private static String mVersionName;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (needInit) {
			init();
		}
	}
	
	private void init() {
		context = getApplicationContext();
		SDK_INT = android.os.Build.VERSION.SDK_INT;
		getScreenSize();
		needInit = false;
		
		fb = FinalBitmap.create(this);// 初始化FinalBitmap模块
		fb.configCompressFormat(CompressFormat.PNG);
	}
	
	public static void getScreenSize() {
		WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		mScreenHeight = metrics.heightPixels;
		if (SDK_INT >= 14 && SDK_INT < 17) {
			try {
				// used when 17 > SDK_INT >= 14; includes window decorations
				// (statusbar bar/menu bar)
				mScreenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
				mScreenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		if (SDK_INT >= 17) {
			try {
				// used when SDK_INT >= 17; includes window decorations
				// (statusbar bar/menu bar)
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
				mScreenWidth = realSize.x;
				mScreenHeight = realSize.y;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
	}
	
	public static String getUserAgentInfo() {
		if (StringUtils.isEmpty(mUserAgentInfo)) {
			StringBuffer appInfo = new StringBuffer();
			appInfo.append("Doit.im for Android").append(getVersionName());
			StringBuffer deviceInfo = new StringBuffer();
			try {
				String divider = "; ";
				deviceInfo.append(" (");
				deviceInfo.append(context.getPackageName()).append(divider).append(getVersionCode()).append(divider).append("[")
						.append(mScreenWidth).append(",").append(mScreenHeight).append("]").append("; ) ");
				TimeZone tz = TimeZone.getDefault();
				deviceInfo.append(" (").append("Android; ").append(Build.VERSION.RELEASE).append(divider).append(Build.DEVICE).append(divider)
						.append(Locale.getDefault().toString()).append(divider)
						.append(tz.getID()).append(" (")
						.append(DateUtils.getGMTOffset())
						.append(")").append(" offset ").append(tz.getRawOffset() / 1000);
				if (tz.inDaylightTime(new Date())) {
					deviceInfo.append(" (Daylight)");
				}
				deviceInfo.append(divider);
				deviceInfo.append("manufacturer:").append(Build.MANUFACTURER).append(divider).append("Model:").append(Build.MODEL);
				deviceInfo.append(") ");
			} catch (Exception e) {
				e.printStackTrace();
			}

			mUserAgentInfo = appInfo.append(deviceInfo).toString();
		}
		return mUserAgentInfo;
	}
	
	/**
	 * 当前版本信息
	 */
	public static int getVersionCode() {
		if (mVersionCode == 0) {
			try {
				PackageManager packageManager = context.getPackageManager();
				PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
				mVersionCode = packageInfo.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mVersionCode;
	}

	/**
	 * 当前版本信息
	 */
	public static String getVersionName() {
		if (mVersionName == null) {
			try {
				PackageManager packageManager = context.getPackageManager();
				PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
				mVersionName = packageInfo.versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mVersionName;
	}

}

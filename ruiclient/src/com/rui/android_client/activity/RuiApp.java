package com.rui.android_client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.tsz.afinal.FinalBitmap;
import android.R.mipmap;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.rui.android_client.db.Persist;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.utils.CollectionUtil;
import com.rui.android_client.utils.DateUtils;
import com.rui.android_client.utils.PreferenceUtil;
import com.rui.android_client.utils.StringUtil;
import com.rui.http.AsynchronizedInvoke;
import com.rui.http.Config;
import com.rui.http.RemoteManager;

public class RuiApp extends Application {
	
	public static Context context;
	public static int SDK_INT;
	
	public static FinalBitmap fb;
	
	public static SharedPreferences mPref;
	
	public static Persist mPersist;
	
	private boolean needInit = true;
	
	private static int mScreenWidth, mScreenHeight;

	private static String mUserAgentInfo;
	
	private static int mVersionCode;
	private static String mVersionName;
	
	private AsynchronizedInvoke asynchronizedInvoke;
	
	private TelephonyManager telephonyManager;
	
	private static ArrayList<String> mInstalledAppPackageNames = new ArrayList<String>();
	private static ArrayList<AppInfo> mInstalledApps = new ArrayList<AppInfo>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		registerActivityLifecycleCallbacks(new MyLifecycleHandler());
		if (needInit) {
			init();
		}
	}
	
	private void init() {
		context = getApplicationContext();
		SDK_INT = android.os.Build.VERSION.SDK_INT;
		getScreenSize();
		
		Config config = Config.getConfig();
		config.init(this);
		
		mPersist = new Persist(context);
		
		fb = FinalBitmap.create(this);// 初始化FinalBitmap模块
		fb.configCompressFormat(CompressFormat.PNG);
		
		RemoteManager.init(this);
		
		asynchronizedInvoke = new AsynchronizedInvoke();
		asynchronizedInvoke.init();
		
		telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		
		mPref = getSharedPreferences(PreferenceUtil.SP_NAME, MODE_PRIVATE);
		
		needInit = false;
	}
	
	public boolean isAppInstalled(String packageName) {
		if(CollectionUtil.isEmpty(mInstalledAppPackageNames)) {
			mInstalledAppPackageNames = new ArrayList<String>();
			for (AppInfo item : getInstalledAppInfos()) {
				mInstalledAppPackageNames.add(item.getPackageName());
			}
		}
		return mInstalledAppPackageNames.contains(packageName);
	}
	
	public AppInfo getInstalledAppInfo(String packageName) {
		if (isAppInstalled(packageName)) {
			int index = mInstalledAppPackageNames.indexOf(packageName);
			if (index >= 0 && index < mInstalledApps.size()) {
				return mInstalledApps.get(index);
			}
		}
		return null;
	}
	
	public void setInstalledApps(ArrayList<AppInfo> installedApps) {
		mInstalledApps = installedApps;
		mInstalledAppPackageNames = null;
	}
	
	public ArrayList<AppInfo> getInstalledAppInfos() {
		if (CollectionUtil.isEmpty(mInstalledApps)) {
			initInstalledApps(false);
		}
		return mInstalledApps;
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
		if (StringUtil.isBlank(mUserAgentInfo)) {
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
	
	public <V> Future<V> asyInvoke(Callable<V> callable) { 
		return asynchronizedInvoke.invoke(callable);
	}

	public void asyCall(Runnable runnable) {
		asynchronizedInvoke.call(runnable);
	}
	
	private Set<Activity> mActivities = new HashSet<Activity>();
	private static int resumed;
	private static int stopped;

	public class MyLifecycleHandler implements ActivityLifecycleCallbacks {

		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			mActivities.add(activity);
		}

		public void onActivityDestroyed(Activity activity) {
			if (mActivities.contains(activity)) {
				mActivities.remove(activity);
			}
		}

		public void onActivityResumed(Activity activity) {
			++resumed;
		}

		public void onActivityPaused(Activity activity) {
		}

		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		}

		public void onActivityStarted(Activity activity) {
		}

		public void onActivityStopped(Activity activity) {
			++stopped;
		}

	}

	public static boolean isApplicationInForeground() {
		return resumed > stopped;
	}
	
	public void finishAllActivities() {
		synchronized(mActivities) {
			for (Activity a : mActivities) {
				a.finish();
			}
			mActivities.clear();
		}
	}
	
	public String getDeviceId() {
		String deviceId = PreferenceUtil.getDeviceId();
		if (!StringUtil.isBlank(deviceId)) {
			return deviceId;
		}

		deviceId = telephonyManager.getDeviceId();
		if (!StringUtil.isBlank(deviceId)) {
			addDevice(deviceId);
			return deviceId;
		}

		// 如果上面获取不到值，那么直接获取当前时间作为唯一编号
		deviceId = (int) (Math.random() * 100) + "-" + System.currentTimeMillis();
		addDevice(deviceId);
		return deviceId;
	}
	
	private void addDevice(String deviceId) {
		PreferenceUtil.addDeviceId(deviceId);
	}
	
	private void initInstalledApps(boolean includeSysApps) {
		ArrayList<AppInfo> installedApps = new ArrayList<AppInfo>();
		PackageManager packageManager = getPackageManager();
		List<PackageInfo> packs = packageManager.getInstalledPackages(0);
		ArrayList<String> installedPackages = new ArrayList<String>();
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo pkInfo = packs.get(i);

			if ((!includeSysApps)
					&& ((pkInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
				continue;
			}
			String packageName = pkInfo.packageName;

			if (packageManager.getLaunchIntentForPackage(packageName) == null) {
				continue;
			}

			installedPackages.add(packageName);

			AppInfo appInfo = new AppInfo();
			appInfo.setMainTitle(pkInfo.applicationInfo.loadLabel(packageManager)
					.toString());
			appInfo.setPackageName(packageName);
			appInfo.setVersionName(pkInfo.versionName);
			appInfo.setLocalVersionCode(pkInfo.versionCode);
			appInfo.setIcon(pkInfo.applicationInfo.loadIcon(packageManager));
			
			installedApps.add(appInfo);
		}
		setInstalledApps(installedApps);
	}

}

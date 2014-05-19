package com.baibutao.apps.linkshop.ruiserver.test;

import java.io.File;

import net.erdfelt.android.apk.AndroidApk;


/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 23, 2014  8:11:32 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestParseAPK {

	public static void main(String[] args) throws Exception {
//		String apkPath = "/Volumes/util/wandoujia-wandoujia_web_4.5.1.5488.apk";
		String apkPath = "/Users/lsb/Desktop/qq.apk";

		File apkfile = new File(apkPath);
		if (!apkfile.exists()) {
			System.err.println("Error: File Not Found: " + apkfile);
			System.exit(-1);
		}

		AndroidApk apk = new AndroidApk(apkfile);
		System.out.println("APK: " + apkfile);
		System.out.println("  .packageName    = " + apk.getPackageName());
		System.out.println("  .appVersion     = " + apk.getAppVersion());
		System.out.println("  .appVersionCode = " + apk.getAppVersionCode());
		System.out.println("  .Permission = " );
		for(String s : apk.getUsesPermission()) {
			System.out.println(s);
		}
	}
}


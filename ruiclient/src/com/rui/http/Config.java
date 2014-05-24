package com.rui.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * @author lsb
 * 
 * @date 2012-5-29 下午11:10:13
 */
public class Config {

	public interface Names {

		// 本地存储的时候，线上与测试环境分开
		String IMG_FOLDER = "shenzhu.img.folder";
		
		String USE_SIGN = "shenzhu.use.sign";
		
		String SIGN_KEY = "shenzhu.sign.key";
		
		String USER_REGISTER_URL = "uxiang.register.url";
		
		String CITY_LIST_URL = "uxiang.city.list.url";

		String COUPON_LIST_URL = "uxiang.coupon.list.url";
		
		String COUPON_BINDING_URL = "uxiang.coupon.binding.url";
		
		String ITEM_LIST_URL = "uxiang.item.list.url";
		
		String BARCODE_ITEM_URL = "uxiang.barcode.url";
		
		String ADDRESS_URL = "uxiang.address.url";
		
		String SHOPPING_INDEX_URL = "uxiang.shopping.index.url";
		
		String SHOPPING_SETTLEMENT_URL = "uxiang.shopping.settlement.url";
		
		String SHOPPING_CREATE_URL = "uxiang.shopping.create.url";
		
		String SHOPPING_ORDER_LIST_URL = "uxiang.shopping.orderlist.url";
		
		String SHOPPING_ORDER_DETAIL_URL = "uxiang.shopping.orderdetail.url";
		
		String SHOPPING_ORDER_CANNEL_URL = "uxiang.shopping.ordercannel.url";
		
		String ADDRESS_POSTINFO_URL = "uxiang.address.postinfo.url";

		String UXIANG_WAPPAY_URL = "uxiang.wappay.url";
		
		String USERLINK_URL = "uxiang.userlink.url";
		
		String ADD_TEST_CITY = "uxiang.addtest.city";
		
		String BANNER_INFO_URL = "uxiang.bannerinfo.url";
		
		String BANNER_INFO_CLICK = "uxiang.bannerinfo.click";
		
		String UXIANG_COLLECTION_URL = "uxiang.collection.url";
		
		String POST_AREA_URL = "uxiang.postarea.url";
 
		String MY_ADDRESS_URL = "uxiang.myaddress.url";

		String ADDRESS_ADD_UPDATE_URL = "uxiang.addressaddoruodate.url";
 
		String DEL_ADDRESS_URL = "uxiang.deladdress.url"; 
 
		String SET_DEFAULT_ADDRESS_URL = "uxiang.setdefaultaddress.url";
		
		
		String CHECK_UPDATE_URL = "check.update";
		String USER_LOGIN_URL = "login.url";
		String FEEDBACK_URL = "feedback.url";
		String SEARCH_URL = "search.url";
		String APP_DETAIL_URL = "app.detail.url";
		String GAME_LIST = "applist.game.url";
		String CAT_LIST_URL = "cat.list.url";
		String APP_LIST = "applist.url";
	}

	private static final String ENV_PATH = "configs/env.properties";
	private AtomicBoolean inited = new AtomicBoolean(false);

	private Properties properties = new Properties();

	private Properties versionProperties = new Properties();

	private static Config config = new Config();
	
	private String companyCode;

	private Config() {
	}

	public static Config getConfig() {
		return config;
	}

	public String getVersionPerperty(String name) {
		return versionProperties.getProperty(name);
	}

	public String getProperty(String name) {
		return properties.getProperty(name);
	}

	public String getProperty(String name, String defaultValue) {
		return properties.getProperty(name, defaultValue);
	}

	public void init(Context context) {
		if (!inited.compareAndSet(false, true)) {
			return;
		}
		AssetManager assetManager = context.getAssets();
		try {
			String env = loadEnv(assetManager);
			String configFileName = getConfigFileName(env);
			InputStream is = assetManager.open(configFileName);
			properties.load(is);
			is.close();

			InputStream versionIs = assetManager.open("configs/version.properties");
			versionProperties.load(versionIs);
			versionIs.close();
			
//			InputStream inJxsInfo = assetManager.open("jxsinfo");
//			companyCode = inputStream2String(inJxsInfo);
//			inJxsInfo.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String inputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		in.close();
		return buffer.toString();
	}
	
	public String getCompanyCode() {
		return companyCode;
	}

	private static String getConfigFileName(String env) {
		return "configs/config-" + env + ".properties";
	}

	private static String loadEnv(AssetManager assetManager) throws IOException {
		InputStream is = assetManager.open(ENV_PATH);
		Properties envProperties = new Properties();
		envProperties.load(is);
		is.close();

		String env = envProperties.getProperty("env", "daily");
		Log.d("Config", "env:" + env);
		return env;
	}

}

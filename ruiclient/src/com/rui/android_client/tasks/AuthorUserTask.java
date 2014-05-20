package com.rui.android_client.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.utils.CollectionUtil;
import com.rui.android_client.utils.PreferenceUtil;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class AuthorUserTask implements Callable<String> {

	private RuiApp mApplication;

	private boolean success;

	static final Pattern pattern = Pattern.compile("baibutao-(\\d+).apk");

	public AuthorUserTask(RuiApp app) {
		super();
		this.mApplication = app;
	}

	@Override
	public String call() throws Exception {
		try {
			RemoteManager remoteManager = RemoteManager.getSecurityRemoteManager();
			Response loginResponse = doLogin(remoteManager);
			if (!loginResponse.isSuccess()) {
				Log.d("author task", "unkown result code:" + loginResponse.getCode());
			}
			success = loginResponse.isSuccess();
			if (success) {
				JSONObject jsonObject = (JSONObject) loginResponse.getModel();
				JSONObject data = jsonObject.getJSONObject("data");
				long fromServerUserId = data.getLong("userId");
				long userId = PreferenceUtil.getUserId();
				if (userId==0) {//用户没有登录的情况 登录了就不处理
					if (userId != fromServerUserId) {
						PreferenceUtil.setUserId(fromServerUserId);
					}
					// deleteOldAPKfile();
				}
			}
			return null;
		} catch (Exception e) {
			Log.e("author task", "author use failed", e);
			success = false;
			return null;
		}
	}

	private void deleteOldAPKfile() {
		try {
			List<File> appFiles = getAppFiles(pattern);
			if (!CollectionUtil.isEmpty(appFiles)) {
				for (File file : appFiles) {
					file.delete();
				}
			}
		} catch (Exception e) {
			Log.d("AuthorUserTask", "delete old apk file error", e);
		}
	}

	private Request createLoginRequest(RemoteManager remoteManager, RuiApp app) {
		String loginUrl = Config.getConfig().getProperty(Config.Names.USER_LOGIN_URL);
		Request request = remoteManager.createPostRequest(loginUrl);
		request.addParameter("deviceId", app.getDeviceId());
		return request;
	}

	private Response doLogin(RemoteManager remoteManager) {
		Request request = createLoginRequest(remoteManager, mApplication);

		Response response = null;
		int tryTimes = 1;
		while (true) {
			response = remoteManager.execute(request);
			if (response.isSuccess()) {
				return response;
			}
			if (tryTimes >= 3) {
				// 如果尝试了10次都没成功，那就放弃吧
				Log.w("author task", "网络连接失败，尝试了" + tryTimes + "次都没成功，放弃了");
				return response;
			}

			++tryTimes;
		}
	}

	private static List<File> getAppFiles(final Pattern pattern) {
		List<File> result = new ArrayList<File>();
		try {
			File sdDir = Environment.getExternalStorageDirectory();

			/*
			 * File[] appFiles = sdDir.listFiles(new FilenameFilter() {
			 * 
			 * @Override public boolean accept(File dir, String filename) {
			 * return pattern.matcher(filename).find(); } });
			 */

			List<File> folders = new ArrayList<File>();
			for (File tempFile : sdDir.listFiles()) {
				if (tempFile.isDirectory() && tempFile.getName().indexOf("download") > -1) {
					folders.add(tempFile);
				} else if (pattern.matcher(tempFile.getName()).find()) {
					result.add(tempFile);
				}
			}

			for (File folder : folders) {
				for (File file : folder.listFiles()) {
					if (file.isFile() && pattern.matcher(file.getName()).find()) {
						result.add(file);
					}
				}
			}
		} catch (Exception e) {
			Log.e(AuthorUserTask.class.getSimpleName(), "get apk files error", e);
		}
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

}

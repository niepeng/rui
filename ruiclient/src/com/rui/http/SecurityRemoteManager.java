package com.rui.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.rui.android_client.utils.CollectionUtil;
import com.rui.android_client.utils.MD5;
import com.rui.android_client.utils.StringUtil;

/**
 * @author lsb
 *
 * @date 2012-5-29 下午11:36:31
 */
public class SecurityRemoteManager extends RemoteManager {

	private RemoteManager remoteManager;
	
	public SecurityRemoteManager(RemoteManager remoteManager) {
		super();
		this.remoteManager = remoteManager;
	}

	@Override
	public Request createPostRequest(String target) {
		return remoteManager.createPostRequest(target);
	}

	@Override
	public Request createQueryRequest(String target) {
		return remoteManager.createQueryRequest(target);
	}

	@Override
	public Response execute(Request request) {
		boolean useSign = Boolean.parseBoolean(Config.getConfig().getProperty(Config.Names.USE_SIGN));
		if (useSign) {
			long ts = System.currentTimeMillis();
			request.addParameter("x_ts", ts);
			String signKey = Config.getConfig().getProperty(Config.Names.SIGN_KEY);
			String sign = genSign(request, signKey);
			request.addParameter("x_sign", sign);
		}
		return remoteManager.execute(request);
	}

	
	/**
	 * 签名方法，把所有参数名转成小写(参数有helloWorld的转成hello_world)，然后按字母排序，name和value(url encoding utf-8)用=链接，每个parameter用&连接，如果是二进制字段（比如文件上传）先把内容md5（小写）后当做值处理
	 * 最后把拼接的结果再最后面加上signKey后md5
	 */
	private String genSign(Request request, String signKey) {
		List<Parameter> parameters = request.getParameters();
		Map<String, String> sortedParameters = CollectionUtil.newTreeMap();
		for (Parameter parameter : parameters) {
			if (parameter == null) {
				continue;
			}
			if (StringUtil.isEmpty(parameter.name)) {
				continue;
			}
			try {
				sortedParameters.put(StringUtil.camelToUnderLineString(parameter.name).toLowerCase(), URLEncoder.encode(parameter.value, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// ignore
			}
		}
		List<BinaryItem> binaryItems = request.getBinaryItems();
		for (BinaryItem binaryItem : binaryItems) {
			if (binaryItem == null) {
				continue;
			}
			if (StringUtil.isEmpty(binaryItem.name)) {
				continue;
			}
			String dataMD5 = MD5.getMD5(binaryItem.data);
			sortedParameters.put(StringUtil.camelToUnderLineString(binaryItem.name).toLowerCase(), dataMD5.toLowerCase());
		}
		String orgString = CollectionUtil.join(sortedParameters, "=", "&");
		String orgStringKey = orgString + signKey;
	//	Log.d("sign", "orgString：" + orgString);
		return MD5.getMD5(orgStringKey.getBytes()).toLowerCase();
	}
	
	
}


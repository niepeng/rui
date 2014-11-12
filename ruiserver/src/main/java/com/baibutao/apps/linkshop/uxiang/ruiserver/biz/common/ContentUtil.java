package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bean.CatchDataBean;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;

public class ContentUtil {

	public static String getContent(String url, int timeout, String charset) {
		String response = "";
		try {
			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();

			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(is, charset));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			response = buffer.toString();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	public static String getContent(String url, int timeout, String charset, boolean isStopRedirect) {
	    String response = "";
	    String cookie = "";
	    try {
	        URL serverUrl = new URL(url);
	        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
	        if(isStopRedirect){
	            if(cookie.length() != 0){
	                conn.setRequestProperty("Cookie", cookie);
	            }
	            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0)");
	            conn.setInstanceFollowRedirects(false);
	        }

	        conn.setConnectTimeout(timeout);
	        conn.setReadTimeout(timeout);
	        conn.setRequestMethod("GET");
	        conn.setDoOutput(true);
	        conn.connect();

	        InputStream is = conn.getInputStream();

	        BufferedReader in = new BufferedReader(new InputStreamReader(is, charset));
	        StringBuffer buffer = new StringBuffer();
	        String line = "";
	        while ((line = in.readLine()) != null) {
	            buffer.append(line);
	        }
	        response = buffer.toString();
	        conn.disconnect();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return response;

	}


	public static String getContent(String url, String referUrl, int timeout, String charset) {
		String response = "";
		try {
			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			conn.addRequestProperty("Referer", referUrl);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(is, charset));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			response = buffer.toString();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}


    /**
     * 执行一个HTTP GET请求，返回请求响应的HTML
     *
     * @param url                 请求的URL地址
     * @param queryString 请求的查询参数,可以为null
     * @param charset         字符集
     * @param pretty            是否美化
     * @return 返回请求响应的HTML
     */
	public static String doGet(String url, String queryString, String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			if (null != queryString && !queryString.equals(""))
				// 对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串
				method.setQueryString(URIUtil.encodeQuery(queryString));
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (URIException e) {
			// log.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e);
		} catch (IOException e) {
			// log.error("执行HTTP Get请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     * 建议调用该类的httpPost这个方法
     *
     * @param url         请求的URL地址
     * @param params    请求的查询参数,可以为null
     * @param charset 字符集
     * @param pretty    是否美化
     * @return 返回请求响应的HTML
     */
	public static String doPost(String url, Map<String, String> params, String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		// 设置Http Post数据
		if (params != null) {
			HttpMethodParams p = new HttpMethodParams();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				p.setParameter(entry.getKey(), entry.getValue());
			}
			method.setParams(p);
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			// log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}


	public static String httpPost(String url, Map<String, String> params, String charset) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}
//		System.out.println("send_url:" + url);
//		System.out.println("send_data:" + sb.toString());

		// 尝试发送请求

		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}

		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
			}
		} catch (Exception e) {
		}
		return buffer.toString();
	}


    public static String getContent(String url, int timeout) {
        return getContent(url, timeout, "UTF-8");
    }

    public static String getContent(String url, int timeout, boolean isStopRedirect) {
        return getContent(url, timeout, "UTF-8", isStopRedirect);
    }

	public static String getContent(String url, String referUrl, int timeout) {
		return getContent(url, referUrl,timeout, "UTF-8");
	}

	public static boolean saveFile(String filepathAndName, String downloadUrl) {
		// 下载网络文件
		int byteread = 0;
		try {
			URL url = new URL(downloadUrl);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(filepathAndName);

			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static List<AppInfoDO> catchApps(String name, int maxNum) {
//		String url = "http://apps.wandoujia.com/api/v1/apps?ads_count=0&tag=通信聊天&max=2";
		List<AppInfoDO> appInfoDOList = CollectionUtil.newArrayList();
		try {
			String url = "http://apps.wandoujia.com/api/v1/apps?ads_count=0&tag="+URLEncoder.encode(name, "utf-8")+"&max=" + maxNum;
			String content = ContentUtil.getContent(url, 2000);

			Thread.sleep(3000);
			JSONObject jsonContentObject = new JSONObject("{\"value\":" + content + "}");
			JSONArray tmp = JsonUtil.getJsonArray(jsonContentObject, "value");
			JSONArray jsonArray = JsonUtil.getJsonArray(tmp.getJSONObject(0), "apps");
			for (int i = 0; i < jsonArray.length(); i++) {
				AppInfoDO appInfoDO = new AppInfoDO();

				JSONObject currentObject = jsonArray.getJSONObject(i);
				String mainTitle = JsonUtil.getString(currentObject, "title", null);
				String subTitle = JsonUtil.getString(currentObject, "tagline", null);
				int favNum = JsonUtil.getInt(currentObject, "likesCount", 0);
				int downloadNum = JsonUtil.getInt(currentObject, "downloadCount", 0);

				String info = JsonUtil.getString(currentObject, "description", null);
				String updateInfo = JsonUtil.getString(currentObject, "changelog", null);
				JSONObject screenshotsObject = JsonUtil.getJSONObject(currentObject, "screenshots");
				JSONArray strArray = JsonUtil.getJsonArray(screenshotsObject, "normal");
				String resultImgs = "";
				for(int index=0;index <strArray.length();index++) {
					String img = strArray.getString(index);
					if(!StringUtil.isBlank(img)) {
						resultImgs += (img + ",");
					}
				}

				String iconUrl = JsonUtil.getJSONObject(currentObject,"icons").getString("px256");

				JSONObject apkInfoObject = JsonUtil.getJSONObject(currentObject,"latestApk");
//				JSONArray downloadUrlArray = apkInfoObject.getJSONArray("downloadUrls");
//				String downloadUrl = downloadUrlArray.getJSONObject(0).getString("url");
				String packageName = apkInfoObject.getString("packageName");

				if(StringUtil.isBlank(packageName)) {
					continue;
				}

				appInfoDO.setPackageName(packageName);
				appInfoDO.setMainTitle(mainTitle);
				appInfoDO.setSubTitle(subTitle);
				appInfoDO.setFavNum(favNum);
				appInfoDO.setDownloadNum(downloadNum);
				appInfoDO.setScreenshots(resultImgs.substring(0, resultImgs.length() - 1));
				appInfoDO.setInfo(info);
				appInfoDO.setUpdateInfo(updateInfo);

				CatchDataBean catchDataBean = new CatchDataBean();
				catchDataBean.downloadUrl = "http://apps.wandoujia.com/apps/" + appInfoDO.getPackageName() + "/download";
				catchDataBean.iconUrl = iconUrl;

				appInfoDO.setCatchDataBean(catchDataBean);

				appInfoDOList.add(appInfoDO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appInfoDOList;
	}

}

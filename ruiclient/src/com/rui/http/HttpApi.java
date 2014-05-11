package com.rui.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;

import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.utils.Logger;
import com.rui.android_client.utils.StringUtils;

public class HttpApi {

	private static final String JSON_TYPE = "application/json;charset=utf-8";
	private static final int CONN_TIMEOUT = 30;
	private static final int TIMEOUT = 30;

	protected final DefaultHttpClient mHttpClient;
	protected AuthScope mAuthScope;

	public HttpApi(String host, int port) {
		setAuthScope(host, port);
		mHttpClient = createHttpClient();
	}

	public DefaultHttpClient createHttpClient() {
		// Sets up the http part of the service.
		final SchemeRegistry supportedSchemes = new SchemeRegistry();

		// Register the "http" protocol scheme, it is required
		// by the default operator to look up socket factories.
		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));
		try {
			TrustAllSSLSocketFactory tasslf = new TrustAllSSLSocketFactory();
			supportedSchemes.register(new Scheme("https", tasslf, 443));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set some client http client parameter defaults.
		final HttpParams httpParams = createHttpParams();
		HttpClientParams.setRedirecting(httpParams, false);

		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
		return new DefaultHttpClient(ccm, httpParams);
	}

	private HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();

		// Turn off stale checking. Our connections break all the time anyway,
		// and it's not worth it to pay the penalty of checking every time.
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		return params;
	}

	public void setCredentials(String username, String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			mHttpClient.getCredentialsProvider().clear();
		} else {
			mHttpClient.getCredentialsProvider().setCredentials(mAuthScope, new UsernamePasswordCredentials(username, password));
		}
	}

	public boolean hasCredentials() {
		return mHttpClient.getCredentialsProvider().getCredentials(mAuthScope) != null;
	}

	public void setAuthScope(String host, int port) {
		mAuthScope = new AuthScope(host, port);
	}

	public RuiResponse doHttpPost(String url, String json) {
		HttpPost httpPost = createHttpPost(url, json);
		return handleRequest(httpPost);
	}

	public RuiResponse doHttpPut(String url, String json) {
		HttpPut httpPut = createHttpPut(url, json);
		return handleRequest(httpPut);
	}

	public RuiResponse doHttpGet(String url) {
		HttpGet httpGet = createHttpGet(url);
		return handleRequest(httpGet);
	}

	private RuiResponse handleRequest(HttpRequestBase request) {
		HttpResponse response;
		try {
			response = executeHttpRequest(request);
			return handleResponse(response);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new RuiResponse(444, "Error");
	}

	private RuiResponse handleResponse(HttpResponse response) throws IOException {
		RuiResponse resp = new RuiResponse();
		switch (response.getStatusLine().getStatusCode()) {
		case 200:
		case 400:
		case 403:
		case 500:
			try {
				resp.code = response.getStatusLine().getStatusCode();
				resp.body = getBody(response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case 401:
			resp.code = 401;
			resp.body = getBody(response);
			break;
		case 404:
			response.getEntity().consumeContent();
			resp.code = 404;
			break;
		default:
			try {
				resp.code = response.getStatusLine().getStatusCode();
				resp.body = getBody(response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		}
		return resp;
	}

	private String getBody(HttpResponse response) {
		try {
			HttpEntity resEntity = response.getEntity();
			InputStream is = resEntity.getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			bis.mark(2);
			// 取前两个字节
			byte[] header = new byte[2];
			int result = bis.read(header);
			// reset输入流到开始位置
			bis.reset();
			// 判断是否是GZIP格式
			if (result != -1 && getShort(header) == 0x8b1f) {
				Logger.d("use Gzip");
				is = new GZIPInputStream(bis);
			} else {
				Logger.d("not use Gzip");
				// 取前两个字节
				is = bis;
			}

			InputStreamReader reader = new InputStreamReader(is, HTTP.UTF_8);
			char[] data = new char[100];
			int readSize;
			StringBuffer sb = new StringBuffer();
			while ((readSize = reader.read(data)) > 0) {
				sb.append(data, 0, readSize);
			}
			String body = sb.toString();
			bis.close();
			reader.close();
			return body;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}

	private HttpGet createHttpGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		addHeader(httpGet);
		return httpGet;
	}

	private void addHeader(HttpRequestBase httRequest) {
		httRequest.addHeader("User-Agent", RuiApp.getUserAgentInfo());
		httRequest.setHeader("Accept", JSON_TYPE);
		httRequest.setHeader("Content-type", JSON_TYPE);
		httRequest.addHeader("Accept-Encoding", "gzip,deflate");
	}

	private HttpPost createHttpPost(String url, String json) {
		HttpPost httpPost = new HttpPost(url);
		addHeader(httpPost);
		try {
			addBody(json, httpPost);
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException("Unable to encode http parameters.");
		}
		return httpPost;
	}

	private HttpPut createHttpPut(String url, String json) {
		HttpPut httpPut = new HttpPut(url);
		addHeader(httpPut);
		try {
			addBody(json, httpPut);
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException("Unable to encode http parameters.");
		}
		return httpPut;
	}

	private void addBody(String json, HttpEntityEnclosingRequestBase request) throws UnsupportedEncodingException {
		if (json != null) {
			StringEntity entity = new StringEntity(json, HTTP.UTF_8);
			entity.setContentType(JSON_TYPE);
			request.setEntity(entity);
		}
	}

	private HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
		try {
			mHttpClient.getConnectionManager().closeExpiredConnections();
			return mHttpClient.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}

	public byte[] getImageFromServer(String url) {
		DefaultHttpClient httpClient = null;
		try {
			// Sets up the http part of the service.
			final SchemeRegistry supportedSchemes = new SchemeRegistry();

			// Register the "http" protocol scheme, it is required
			// by the default operator to look up socket factories.
			final SocketFactory sf = PlainSocketFactory.getSocketFactory();
			supportedSchemes.register(new Scheme("http", sf, 80));
			try {
				TrustAllSSLSocketFactory tasslf = new TrustAllSSLSocketFactory();
				supportedSchemes.register(new Scheme("https", tasslf, 443));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Set some client http client parameter defaults.
			final HttpParams httpParams = createHttpParams();
			HttpClientParams.setRedirecting(httpParams, true);

			final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
			httpClient = new DefaultHttpClient(ccm, httpParams);

			HttpGet request = new HttpGet(url);
			request.addHeader("User-Agent", RuiApp.getUserAgentInfo());

			httpClient.setRedirectHandler(new RedirectHandler() {

				@Override
				public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 302) {
						return true;
					}
					return false;
				}

				@Override
				public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
					int statusCode = response.getStatusLine().getStatusCode();
					if ((statusCode == 302)) {
						// 此处重定向处理
						Header[] headers = response.getHeaders("location");
						String url = null;
						for (Header header : headers) {
							url = header.getValue();
						}
						try {
							return new URI(url);
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
					return null;
				}
			});

			HttpResponse response = httpClient.execute(request);
			if (response == null) {
				return null;
			}

			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream in = response.getEntity().getContent();
				if (in != null) {
					ByteArrayBuffer baf = new ByteArrayBuffer(1024);
					int current = 0;
					while ((current = in.read()) != -1) {
						baf.append(current);
					}
					in.close();
					return baf.toByteArray();
				}
			}
			response.getEntity().consumeContent();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return null;
	}

}

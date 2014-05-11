package com.rui.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class HttpApiWithBasicAuth extends HttpApi {

	public HttpApiWithBasicAuth(String host, int port) {
		super(host, port);
		mHttpClient.addRequestInterceptor(preemptiveAuth, 0);

		mHttpClient.addRequestInterceptor(new HttpRequestInterceptor() {

			@Override
			public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});
		mHttpClient.addResponseInterceptor(new HttpResponseInterceptor() {

			public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					HeaderElement[] codecs = ceheader.getElements();
					for (int i = 0; i < codecs.length; i++) {
						if (codecs[i].getName().equalsIgnoreCase("gzip")) {
							response.setEntity(new GzipDecompressingEntity(response.getEntity()));
							return;
						}
					}
				}
			}

		});
	}

	private HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

		@Override
		public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {

			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

			// If not auth scheme has been initialized yet
			if (authState.getAuthScheme() == null) {
				AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
				// Obtain credentials matching the target host
				org.apache.http.auth.Credentials creds = credsProvider.getCredentials(authScope);
				// If found, generate BasicScheme preemptively
				if (creds != null) {
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				}
			}
		}

	};

	private class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException, IllegalStateException {
			InputStream wrappedin = wrappedEntity.getContent();
			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			return -1;
		}

	}
}

package com.itlao.utils.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.itlao.utils.cmd.CommonCmd;



import android.util.Log;


public class HttpConnectionUtil {
	private final static String DEBUG = "HttpConnectionUtil";
	public static String sessionId;
	public static final String TAG = "HttpConnectionUtil";
	public static final String HTTP_STATE_FAILED = "响应失败,请求终止";
	public static final String HTTP_STATE_ERROR = "请求失败";
	public static final String HTTP_STATE_TIMEOUT = "连接超时";
	public static final String HTTP_STATE_IOEXCEPTION = "IO异常";
	public static String userCode, password;
	public static int login = -1;
	public static Thread requestThread = null;
	public static final HttpContext localContext = new BasicHttpContext();

	public static final String CHARSET="GBK";
	public static enum HttpMethod {
		GET, POST
	}

	private static DefaultHttpClient client = null;
	private static final int REQUEST_TIMEOUT = 8 * 1000;// 设置请求超时8秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	
	/** 
     * 每个路由最大连接数 
     */
	private static final int MAX_ROUTE_CONNECTIONS = 300;
	private static final int MAX_TOTAL_CONNECTIONS = 1000;

	private HttpConnectionUtil() {

	}

	public synchronized static HttpClient getHttpClient() {
		if (client == null) {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
			
			// 设置最大连接数
			ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS); 
			// 设置每个路由最大连接数
			ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);  
			ConnManagerParams.setMaxConnectionsPerRoute(params,connPerRoute);
						
			// 设置基本参数
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			// 使用线程安全的连接管理来创建HttpClient
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
					params, schemeRegistry);
			client = new DefaultHttpClient(cm, params);
			HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
				@Override
				public boolean retryRequest(IOException exception,
						int executionCount, HttpContext context) {
					if (executionCount >= 3) {
						// Do not retry if over max retry count
						return false;
					}
					if (exception instanceof InterruptedIOException) {
						// Timeout
						return true;
					}
					if (exception instanceof UnknownHostException) {
						// Unknown host
						return false;
					}
					if (exception instanceof ConnectException) {
						// Connection refused
						return false;
					}
					if (exception instanceof SSLException) {
						// SSL handshake exception
						return false;
					}
					HttpRequest request = (HttpRequest) context
							.getAttribute(ExecutionContext.HTTP_REQUEST);
					boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
					if (idempotent) {
						// Retry if the request is considered idempotent
						return true;
					}
					return false;
				}
			};
			client.setHttpRequestRetryHandler(myRetryHandler);
			CookieStore cookieStore = new BasicCookieStore();
			client.setCookieStore(cookieStore);
		}
		return client;
	}

	/**
	 * 异步访问网络方法
	 *
	 * @param url
	 *            String
	 * @param method
	 *            HttpMethod {GET, POST}
	 * @param callback
	 *            HttpConnectionCallback
	 */
	public static void asyncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		asyncConnect(url, null, method, callback);
	}

	/**
	 * 同步访问网络方法
	 *
	 * @param url
	 *            String
	 * @param method
	 *            HttpMethod {GET, POST}
	 * @param callback
	 *            HttpConnectionCallback
	 */
	public static Object syncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		return syncConnect(url, null, method, callback);
	}

	/**
	 * 异步访问网络方法
	 *
	 * @param url
	 *            String
	 * @param params
	 *            Map<String, String>
	 * @param method
	 *            HttpMethod {GET, POST}
	 * @param callback
	 *            HttpConnectionCallback
	 * @return
	 */
	public static void asyncConnect(final String url,
			final Map params, final HttpMethod method,
			final HttpConnectionCallback callback) {
		Thread th = new Thread() {
			@Override
			public void run() {
				syncConnect(url, params, method, callback);
			}
		};
		th.start();
	}
	public static void asyncConnect(final String url,
			final Map<String, Object> params,
			final HttpConnectionCallback callback) {
		asyncConnect(url,params,HttpMethod.POST,callback);
	}

	public static Object syncUploadConnect(final String url,
			final Map params,
			final HttpConnectionCallback callback) {
		String json = "";
		BufferedReader reader = null;
		HttpUriRequest request = null;
		try {
			HttpClient client = getHttpClient();
			request = getUploadRequest(url, params);
			HttpResponse response = client.execute(request);
			if (callback instanceof NetBinaryCallback) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					response.getEntity().writeTo(baos);
					((NetBinaryCallback) callback).execute(baos.toByteArray());
					return null;
				}
			} else {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					reader = new BufferedReader(new InputStreamReader(response
							.getEntity().getContent(), CHARSET));
					StringBuilder sb = new StringBuilder();
					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						sb.append(s);
					}
					json = sb.toString();

				}
			}
		} catch (ClientProtocolException e) {
			json = "请求失败.";
			Log.e("HttpConnectionUtil", e.getMessage(), e);
			if (callback instanceof NetCallback) {
				return ((NetCallback) callback).onError(params,
						NetCallback.STATUS_ERROR, "访问地址错误");
			}
		} catch (SocketTimeoutException e) {
			if (callback instanceof NetCallback) {
				return ((NetCallback) callback).onTimeout(params,
						NetCallback.STATUS_TIMEOUT);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			json = "IO异常.";
			e.printStackTrace();
			if (callback instanceof NetCallback) {
				return ((NetCallback) callback).onError(params,
						NetCallback.STATUS_ERROR, "发生异常.");
			}
		}  finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		return callback.execute(json);
	}

	public static Thread asyncUploadConnect(final String url,
			final Map<String, Object> params,
			final HttpConnectionCallback callback) {
		Thread th = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				syncUploadConnect(url, params, callback);
			}
		};
		th.start();
		return th;
	}

	public static class CallThread extends Thread {
		private String url;
		Map params;
		HttpMethod method;
		HttpConnectionCallback callback;
		boolean running = false;
		HttpUriRequest request;
		// 手动中断
		boolean isAbort;

		public CallThread() {
			super();
			// TODO Auto-generated constructor stub
		}

		public CallThread(Runnable runnable) {
			super(runnable);
		}

		public CallThread(final String url, final Map  params,
				final HttpMethod method, final HttpConnectionCallback callback) {
			super();
			this.url = url;
			this.params = params;
			this.method = method;
			this.callback = callback;
		}

		@Override
		public void run() {
			String json = null;
			BufferedReader reader = null;
			try {

				HttpClient client = getHttpClient();
				if (!this.running)
					return;
				request = getRequest(url, params, method);
				if (!this.running)
					return;
				HttpResponse response = client.execute(request);
				if (!this.running)
					return;
				if (callback instanceof NetBinaryCallback) {
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						response.getEntity().writeTo(baos);
						((NetBinaryCallback) callback).execute(baos
								.toByteArray());
						return;
					}
				} else {
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						reader = new BufferedReader(new InputStreamReader(
								response.getEntity().getContent(), CHARSET));
						StringBuilder sb = new StringBuilder();
						for (String s = reader.readLine(); s != null; s = reader
								.readLine()) {
							sb.append(s);
						}
						json = sb.toString();

					}
				}
			} catch (ClientProtocolException e) {
				json = "请求失败.";
				Log.e("HttpConnectionUtil", e.getMessage(), e);
				if (callback instanceof NetCallback) {
					((NetCallback) callback).onError(params,
							NetCallback.STATUS_ERROR, "访问地址错误.");
					return;
				}
			} catch (SocketTimeoutException e) {
				if (callback instanceof NetCallback) {
					((NetCallback) callback).onTimeout(params,
							NetCallback.STATUS_TIMEOUT);
					return;
				}
			} catch (Exception e) {
				json = "IO异常.";
				if (!isAbort) {
					Log.e("HttpConnectionUtil", e.getMessage(), e);
					if (callback instanceof NetCallback) {
						((NetCallback) callback).onError(params,
								NetCallback.STATUS_ERROR, "发生异常.");
						return;
					}
				}
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
				}
			}
			if (!this.running)
				return;
			if (json == null)
				return;
			// Log.i("json","json:"+json);
			callback.execute(json);
		}

		@Override
		public synchronized void start() {
			this.running = true;
			super.start();
		}

		@Override
		public void interrupt() {
			this.running = false;
			isAbort = true;
			try {
				request.abort();
			} catch (Exception ex) {
			}
			super.interrupt();
		}

	}

	public static CallThread call(final String url,
			final Map params, final HttpMethod method,
			final HttpConnectionCallback callback) throws Exception {
		CallThread ct = new CallThread(url, params, HttpMethod.POST, callback);
		ct.start();
		return ct;
	}

	/**
	 * 同步访问网络方法
	 *
	 * @param url
	 *            String
	 * @param params
	 *            Map<String, String>
	 * @param method
	 *            HttpMethod {GET, POST}
	 * @param callback
	 *            HttpConnectionCallback
	 */
	public static Object syncConnect(final String url,
			final Map  params, final HttpMethod method,
			final HttpConnectionCallback callback) {
		String json = "";
		BufferedReader reader = null;
		HttpUriRequest request = null;
		try {
			HttpClient client = getHttpClient();
			request = getRequest(url, params, method);
			HttpResponse response = client.execute(request);
			if (callback instanceof NetBinaryCallback) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					response.getEntity().writeTo(baos);
					return ((NetBinaryCallback) callback).execute(baos.toByteArray());
				}
			} else {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					reader = new BufferedReader(new InputStreamReader(response
							.getEntity().getContent(), CHARSET));
					StringBuilder sb = new StringBuilder();
					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						sb.append(s);
					}
					json = sb.toString();

				}
			}
		} catch (ClientProtocolException e) {
			json = "请求失败.";
			Log.e("HttpConnectionUtil", e.getMessage(), e);
			if (callback instanceof NetCallback) {
				return ((NetCallback) callback).onError(params,
						NetCallback.STATUS_ERROR, "访问地址错误");
			}
		} catch (SocketTimeoutException e) {
			if (callback instanceof NetCallback) {
				return ((NetCallback) callback).onTimeout(params,
						NetCallback.STATUS_TIMEOUT);
			}
		} catch (Exception e) {
			json = "IO异常.";
			e.printStackTrace();
			if (callback instanceof NetCallback) {
				return ((NetCallback) callback).onError(params,
						NetCallback.STATUS_ERROR, "发生异常.");
			}
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		return callback.execute(json);
	}

	public static HttpUriRequest getUploadRequest(String url,
			Map<String, Object> params) throws UnsupportedEncodingException {
		if (CommonCmd.userCode != null) {
			if (params == null)
				params = new HashMap<String, Object>(3, 1);
			params.put("userCode", CommonCmd.userCode);
			params.put("pass", CommonCmd.passWord);
			params.put("login", Integer.toString(CommonCmd.login));
		}
		MultipartEntity mpEntity = new MultipartEntity();
		for (Entry<String, Object> entry : params.entrySet()) {
			if (!(entry.getValue() instanceof File)){
				if(entry.getValue()!=null){
					mpEntity.addPart(entry.getKey(), new StringBody(entry
							.getValue().toString(),Charset.forName(CHARSET)));
				}
			}
			else if (entry.getValue() instanceof File)
				mpEntity.addPart(entry.getKey(),
						new FileBody((File) entry.getValue()));

		}
		Log.d(DEBUG, "设置post参数"+ url);
		HttpPost request = new HttpPost(url);
		request.setEntity(mpEntity);
		return request;
	}

	public static HttpUriRequest getRequest(String url,
			Map<String, Object> params, HttpMethod method) {

		if (CommonCmd.userCode != null) {
			if (params == null)
				params = new HashMap<String, Object>(3, 1);
			params.put("userCode", CommonCmd.userCode);
			params.put("pass", CommonCmd.passWord);
			params.put("login", Integer.toString(CommonCmd.login));
		}
		if (method.equals(HttpMethod.POST)) {
			List<NameValuePair> listParams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (String name : params.keySet()) {
					if(params.get(name)!=null){
					listParams.add(new BasicNameValuePair(name, params
							.get(name).toString()));
					}
				}
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						listParams, CHARSET);
				Log.d(DEBUG, "设置post参数" + url);
				HttpPost request = new HttpPost(url);
				request.setEntity(entity);
				return request;
			} catch (UnsupportedEncodingException e) {
				throw new java.lang.RuntimeException(e.getMessage(), e);
			}
		} else {
			if (url.indexOf("?") < 0) {
				url += "?";
			}
			if (params != null) {
				for (String name : params.keySet()) {
					try {
						url += "&" + name + "="
								+ URLEncoder.encode(String.valueOf(params.get(name)), CHARSET);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			// Log.d(DEBUG, "设置get参数�? + url);
			HttpGet request = new HttpGet(url);
			return request;
		}
	}


	/**
	 *
	 * 用于返回完整的url请求地址字符�?更详细的说明
	 *
	 * @param webContentPath
	 *            如：http://www.eshinetech.com/
	 * @param controller
	 *            如：login/login.do
	 * @param action
	 *            如：submit
	 * @return 返回完整的url请求地址字符�?
	 *         http://www.eshinetech.com/login/login.do?action=submit
	 * @see 相关项目（例如类名）
	 * @since 1.1
	 *
	 *        <p>
	 *        ----------------------------------------------------------------
	 *        </p>
	 *        <p>
	 *        Date Mender Reason
	 *        </p>
	 *        <p>
	 *        2012-7-11 qiushuzhong
	 *        </p>
	 */
	public static String getUrl(String webContentPath, String controller,
			String action) {
		if (webContentPath == null || controller == null || action == null
				|| webContentPath.equals("") || controller.equals("")
				|| action.equals("")) {
			return null;
		} else {
			return webContentPath + controller + "?action=" + action;
		}

	}

	/**
	 * 用户使用的方�?功能：请求服务器，返回字符串
	 *
	 * @param uri
	 *            字符串形式的请求地址
	 * @param params
	 *            请求的键值对
	 * @param requestLimit
	 *            �?��允许的请求失败次�?
	 * @return
	 */
	public static String getString(String uri, List<BasicNameValuePair> params,
			int requestLimit) {
		HttpResponse response;
		int currCount = 0; // 当前请求次数
		String result = null;
		HttpPost post = getPost(uri, params);
		HttpClient httpClient = getHttpClient();
		currCount++;
		try {
			response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.v(TAG, "响应成功.");
				return EntityUtils.toString(response.getEntity());
			} else {
				Log.v(TAG, "响应失败,请求终止.");
				result = "响应失败,请求终止.";
			}
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			result = "请求失败.";
			System.out.println("ClientProtocolException");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			if (e instanceof ConnectTimeoutException) {
				result = "连接超时.";
			} else {
				result = "IO异常.";
			}
		}
		return result;
	}

	/**
	 * 获得Post请求对象
	 *
	 * @param uri
	 *            请求地址，也可以带参�?
	 * @param params
	 *            如果为null，则不添加由BasicNameValue封装的参�?
	 * @return
	 */
	public static HttpPost getPost(String uri, List<BasicNameValuePair> params) {
		HttpPost post = new HttpPost(uri);
		try {
			if (params != null) {
				post.setEntity(new UrlEncodedFormEntity(params, "GB2312"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return post;
	}


	/**
	 * 用户使用的方�?功能：请求服务器，返回字符串
	 *
	 * @param uri
	 *            字符串形式的请求地址
	 * @param requestLimit
	 *            �?��允许的请求失败次�?
	 * @return
	 */
	public static String getString(String uri, int requestLimit) {

		if (requestLimit < 1) {
			return null;
		}
		HttpResponse response;
		int currCount = 0; // 当前请求次数
		String result = null;
		HttpPost post = getPost(uri, null);
		HttpClient httpClient = getHttpClient();
		currCount++;
		try {
			response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.v(TAG, "响应成功.");
				return EntityUtils.toString(response.getEntity());
			} else {
				// post.abort();
				Log.v(TAG, "响应失败,请求终止.");
				result = "响应失败,请求终止.";
				// result = HTTP_STATE_ERROR;
			}
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			if (currCount > requestLimit) {
				result = HTTP_STATE_ERROR;
			}
			System.out.println("ClientProtocolException");
		} catch (Exception e) {
			// Log.e(TAG, e.getMessage());
			if (e instanceof ConnectTimeoutException) {
				result = "连接超时.";
			} else {
				result = "IO异常.";
			}
		} finally {
			System.out.println("finally");
		}
		return result;
	}

	public static void shutdown() {
		try {
			client.getConnectionManager().shutdown();
		} catch (Exception ex) {

		}
	}

	public static void closeIdleConnections() {
		// client.getConnectionManager().closeIdleConnections(10,
		// TimeUnit.MILLISECONDS);

	}

	public static void closeExpiredConnections() {
		// client.getConnectionManager().closeExpiredConnections();
	}
}

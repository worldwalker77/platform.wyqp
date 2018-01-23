package cn.worldwalker.game.wyqp.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

public class HttpClientUtil {
	
	private final static Log log = LogFactory.getLog(HttpClientUtil.class);
	
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			try {
				jsonObject = JSONObject.fromObject(buffer.toString());
			} catch (Exception e) {
				String jStr = XmlConverUtil.xmltoJson(buffer.toString());
				jsonObject = JSONObject.fromObject(jStr);
			}

		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}
}

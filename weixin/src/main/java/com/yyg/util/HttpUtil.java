package com.yyg.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

public class HttpUtil {

	


	public static String post(String url, Map<String, String> paramsMap) {
		String result = null;
		PostMethod postMethod = null;
		HttpClient httpClient = new HttpClient();

		httpClient.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		postMethod = new PostMethod(url);

		if (paramsMap != null && paramsMap.size() > 0) {
			NameValuePair[] datas = new NameValuePair[paramsMap.size()];
			int index = 0;
			for (String key : paramsMap.keySet()) {
				datas[index++] = new NameValuePair(key, paramsMap.get(key));
			}
			postMethod.setRequestBody(datas);

		}

		HttpClientParams httparams = new HttpClientParams();
		httparams.setSoTimeout(60000);
		postMethod.setParams(httparams);

		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
			} else {
				System.out.println(" http response status is " + statusCode);
			}

		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

		return result;
	}
	
	/*
	 * 下载APP(包括图片)
	 */
	public static void httpDownload(String url, String AllFileName) {

		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(url);
		try {
			client.executeMethod(httpGet);
			if (httpGet.getResponseHeaders("Content-Disposition").length != 0) {

				System.out.println(httpGet
						.getResponseHeaders("Content-Disposition")[0]
						.getValue());
//				fileName = httpGet.getResponseHeaders("Content-Disposition")[0]
//						.getValue().split(";")[1].split("\"")[1];
//				System.out.println(fileName);
			}
//			String iconName = fileName + url.substring(url.length() - 4);
			InputStream in = httpGet.getResponseBodyAsStream();
			FileOutputStream out = new FileOutputStream(new File(AllFileName));

			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			in.close();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		//System.out.println("download, success!!");
	}
}



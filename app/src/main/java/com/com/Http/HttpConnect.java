package com.com.Http;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpConnect {

	//private final String url = "http://192.168.9.17:8080/HelloWeb/";
	private final String characterEncoding = "UTF-8";

	public String connectPost(String url, HashMap<String, String> params) {

		HttpClient httpClient = new DefaultHttpClient();

		//String uri = url + servlet;
		System.out.println("url---------" + url);
		System.out.println("send---------" + params);

		try {
			HttpPost postMethod = new HttpPost(url);

			ArrayList<BasicNameValuePair> reqParams = new ArrayList<BasicNameValuePair>();
			
			for(Map.Entry<String, String> entry : params.entrySet())
			{
				BasicNameValuePair bnvp = new BasicNameValuePair(entry.getKey(), entry.getValue());
				reqParams.add(bnvp);
			}
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(reqParams, characterEncoding);

			postMethod.setEntity(entity);

			HttpResponse response = httpClient.execute(postMethod);

			System.out.println(response.getStatusLine().getStatusCode());
			
			String result = EntityUtils.toString(response.getEntity(),
					characterEncoding);

			if (result == null) {
				return null;
			} else {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public String connectGet(String url, HashMap<String, String> params) {

		HttpClient httpClient = new DefaultHttpClient();
		
		//String uri = url + servlet + "?";
		String uri = url + "?";
		for(Map.Entry<String, String> entry : params.entrySet())
		{
			uri += entry.getKey() + "=" + entry.getValue() + "&";
		}
		
		uri += new Date().getTime();

		try {
			HttpGet getMethod = new HttpGet(uri);

			HttpResponse response = httpClient.execute(getMethod);

			String result = EntityUtils.toString(response.getEntity(),
					characterEncoding);

			if (result == null) {
				return null;
			} else {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}

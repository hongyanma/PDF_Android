package com.com.Http;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

public class HttpTask extends AsyncTask<HashMap<String, String>, Integer, JSONObject> {

	private HttpCallback callback = null;
	
	public void setHttpCallback(HttpCallback callback) {
		this.callback = callback;
	}
	
	private String servlet = null;
	
	public void setServlet(String servlet)
	{
		this.servlet = servlet;
	}
	
	public HttpTask() {}
	
	public HttpTask(HttpCallback callback, String servlet) {
		this.callback = callback;
		this.servlet = servlet;
	}
	
	@Override
	protected JSONObject doInBackground(HashMap<String, String>... arg0) {
		// TODO Auto-generated method stub
		
	
		String result = new HttpConnect().connectPost(servlet, arg0[0]);
		
		JSONObject json = null;
		try {
			if(result != null)
				json = new JSONObject(result);
			    System.out.println("json-------" + json);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("json异常-------" + json);
			System.out.println("json异常-------" + result);
		}
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		if(result != null)
		{
			if(callback != null)
				callback.getResult(result);
		}
	}
	
	

}

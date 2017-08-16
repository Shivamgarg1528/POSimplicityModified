package com.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class NotificationApi extends AsyncTask<Void, Integer, String> implements PrefrenceKeyConst {

	private String baseUrl;
	private String transactionId;
	private String response;
	private Context mContext;
	private String ordeStaus;

	public NotificationApi(Context mContext,String orderStatus) {

		this.mContext          = mContext;
		this.ordeStaus         = orderStatus;
		this.baseUrl           = MyPreferences.getMyPreference(STORE_NAME, mContext);
		this.transactionId     = MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext);
	}

	@Override
	protected String doInBackground(Void... params) {
		excuteApiForResponse();
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("RESPONSE :"+response.trim());	

	}

	public String excuteApiForResponse() {			

		HttpPost hPost     = new HttpPost(baseUrl+"?tag=send_notification");
		HttpClient hClient = new DefaultHttpClient();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("transId", transactionId));
		nameValuePairs.add(new BasicNameValuePair("orderStatus", ordeStaus));
		nameValuePairs.add(new BasicNameValuePair("storeName", MyPreferences.getMyPreference(STORE, mContext)));
		try {
			UrlEncodedFormEntity urlEncodedFormEntity  = new UrlEncodedFormEntity(nameValuePairs);
			hPost.setEntity(urlEncodedFormEntity);
			ResponseHandler<String> rHandler = new BasicResponseHandler();
			System.out.println(baseUrl+"?tag=send_notification&"+EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs)));
			boolean isConnected = InternetConnectionDetector.isInternetAvailable(mContext);
			if (isConnected) 		
				response = hClient.execute(hPost, rHandler);			
			else			
				response = "No Internet";
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response = "ExceptionOccur";
			return response;
		} 
	}
}


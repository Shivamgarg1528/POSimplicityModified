package com.AsyncTasks;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.utils.Variables;

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

public class ShareOrderWithCustomer extends AsyncTask<Void, Void, Boolean> implements PrefrenceKeyConst{

	private Context mContext;
	private String baseUrl;
	private String billingAddress;
	private int customerId;

	public ShareOrderWithCustomer(Context context ){
		this.mContext       = context;
		this.baseUrl        = MyPreferences.getMyPreference(STORE_NAME, mContext);
		this.billingAddress = Variables.billToName;
		this.customerId     = Variables.customerId;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost     = new HttpPost(baseUrl+"?tag=sales_post"); 
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email_id",billingAddress));  
			nameValuePairs.add(new BasicNameValuePair("customer_id",""+customerId));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
			System.out.println(baseUrl+"?tag=sales_post&"+EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs)));

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			Log.v("RESPONSE",response.trim());

			if(response.equalsIgnoreCase("successful"))
				System.out.println("Mail Sent Successfully");
			else
				System.out.println("Mail Not Sent");

		}catch(Exception e){
			System.out.println("Exception : " + e.getMessage());
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}
}

package com.posimplicity.gateway;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.CustomControls.ProgressHUD;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CreditCardHelper;

import com.utils.MyPreferences;
import com.posimplicity.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class ProPayGateway implements PrefrenceKeyConst{

	private final String BillerID  = "5747881770773031";
	private final String AuthToken = "457ff74f-b004-4896-8b17-3db6755445b0";
	private Context context;
	private String payerID,payerMethodId;
	private String PAYERURL = "https://xmltestapi.propay.com/ProtectPay/Payers";

	private String HOSTURL  = "xmltestapi.propay.com";
	private OnCallBacks listener;
	private ProgressHUD progressHUD;

	public ProPayGateway(Context context, OnCallBacks listener) {
		super();
		this.context   = context;
		this.listener  = listener;
		payerID        = MyPreferences.getMyPreference(PAYER_ID, context);
		payerMethodId = MyPreferences.getMyPreference(PAYMENT_METHOD_ID_PROPAY, context);
	}

	public ProPayGateway(Context mContext) {
		this.context   = mContext;
		payerID        = MyPreferences.getMyPreference(PAYER_ID, context);
		payerMethodId = MyPreferences.getMyPreference(PAYMENT_METHOD_ID_PROPAY, context);
	}

	public void onCreatePayer() {
		new GetPropayId().execute();
	}

	public void onPayerIdDelete() {
		new DeletePayerIds().execute();
	}


	class GetPropayId extends AsyncTask<Void, Void, String> implements OnCancelListener {
		@Override
		protected void onPreExecute() {
			progressHUD = ProgressHUD.show(context, "IdCreating...", true, false, this);
		}

		@Override
		protected String doInBackground(Void... params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPut httput = new HttpPut(PAYERURL);
			String responseBody = "";
			HttpResponse response = null;

			String	base64EncodedCredentials = "Basic " + Base64.encodeToString((BillerID+":"+AuthToken).getBytes(),Base64.NO_WRAP);
			JSONObject obja = new JSONObject();
			try {
				obja.put("Name",R.string.app_name);
				System.out.println(obja.toString());
				StringEntity stEn = new StringEntity(obja.toString());
				httput = setHeaderss(httput, base64EncodedCredentials);
				httput.setEntity(stEn);
				response = httpclient.execute(httput);
				responseBody = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				Log.d("Response Line:  ","" + response.getStatusLine().getStatusCode());
				System.out.println(responseBody);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return responseBody;
		}

		@Override
		protected void onPostExecute(String result) {
			progressHUD.dismiss();
			listener.onSuccessfullCreation(result);
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			progressHUD.dismiss();
		}
	}



	class DeletePayerIds extends AsyncTask<Void, Void, String> implements OnCancelListener{

		@Override
		protected void onPreExecute() {
			progressHUD = ProgressHUD.show(context, "IdDeleting...", true, false, this);
		}

		@Override
		protected String doInBackground(Void... params) {
			String DELETEPAYERURL = "https://xmltestapi.propay.com/ProtectPay/Payers/"+payerID+"/";
			System.out.println(DELETEPAYERURL);
			HttpClient httpclient = new DefaultHttpClient();
			HttpDelete httpDelete = new HttpDelete(DELETEPAYERURL);
			String responseBody = "";
			HttpResponse response = null;

			String	base64EncodedCredentials = "Basic " + Base64.encodeToString((BillerID+":"+AuthToken).getBytes(),Base64.NO_WRAP);
			try {
				httpDelete = setDeletesHeaders(httpDelete, base64EncodedCredentials);
				response = httpclient.execute(httpDelete);
				responseBody = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				Log.d("Response Line:  ","" + response.getStatusLine().getStatusCode());
				System.out.println(responseBody);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return responseBody;
		}
		@Override
		protected void onPostExecute(String result) {
			progressHUD.dismiss();
			listener.onSuccessfullCreation(result);
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			progressHUD.dismiss();
		}
	}

	public HttpPut setHeaderss(HttpPut httput,String base64EncodedCredentials) {
		httput.setHeader("Accept-Encoding", "deflate");
		httput.setHeader("Authorization", base64EncodedCredentials);
		httput.setHeader("Content-Type", "application/json");
		httput.setHeader("Connection", "Keep-Alive");
		httput.setHeader("Host", HOSTURL);
		httput.setHeader("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
		return httput;
	}

	public HttpDelete setDeletesHeaders(HttpDelete httpDelete,String base64EncodedCredentials) {
		httpDelete.setHeader("Accept-Encoding", "deflate");
		httpDelete.setHeader("Authorization", base64EncodedCredentials);
		httpDelete.setHeader("Content-Type", "application/json");
		httpDelete.setHeader("Connection", "Keep-Alive");
		httpDelete.setHeader("Host", HOSTURL);
		httpDelete.setHeader("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
		return httpDelete;
	}

	public  static interface OnCallBacks{
		public void onSuccessfullCreation(String key);
	}

	public boolean onKeyedTransaction(String amountToPaid,String cardNumber, String expMonthAndYear, String personName) {
		String response  = onMethodCreation(cardNumber,expMonthAndYear,personName);
		if(response.isEmpty())
			return false;
		else{
			try{
				JSONObject jsonObject = new JSONObject(response);
				JSONObject innerJsonObj = jsonObject.getJSONObject("RequestResult");
				String resultValue = innerJsonObj.getString("ResultValue");
				if(resultValue.equalsIgnoreCase("ORDER_SUCCESS")){
					payerMethodId = jsonObject.getString("PaymentMethodId");
					MyPreferences.setMyPreference(PAYMENT_METHOD_ID_PROPAY, payerMethodId, context);
					return onDoActualPayment( amountToPaid, cardNumber,  expMonthAndYear,  personName, payerMethodId);
				}
				else
					return false;
			}
			catch(Exception ex){
				return false;
			}
		}
	}

	private boolean onDoActualPayment(String amountToPaid, String cardNumber, String expMonthAndYear, String personName, String payerMethodId2) {

		int amount = (int) Math.floor(Double.parseDouble(amountToPaid));


		String PAYERMETHODURL = "https://xmltestapi.propay.com/ProtectPay/Payers/"+payerID+"/PaymentMethods/ProcessedTransactions/";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httput = new HttpPut(PAYERMETHODURL);
		String responseBody = "";
		HttpResponse response = null;

		String	base64EncodedCredentials = "Basic " + Base64.encodeToString((BillerID+":"+AuthToken).getBytes(),Base64.NO_WRAP);
		System.out.println(base64EncodedCredentials);

		JSONObject obja = new JSONObject();
		try {
			obja.put("PayerAccountId",payerID);
			obja.put("paymentMethodID",payerMethodId);
			obja.put("Amount",amount);
			obja.put("CurrencyCode", "USD");
			System.out.println(obja.toString());
			StringEntity stEn = new StringEntity(obja.toString());
			httput = setHeaderss(httput, base64EncodedCredentials);
			httput.setEntity(stEn);
			response = httpclient.execute(httput);
			responseBody = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			Log.d("Response Line:  ","" + response.getStatusLine().getStatusCode());
			System.out.println(responseBody);
			JSONObject jsonObject = new JSONObject(responseBody);
			JSONObject secondJSONObj = jsonObject.getJSONObject("RequestResult");
			String resultValue = secondJSONObj.getString("ResultValue");
			if(resultValue.equalsIgnoreCase("ORDER_SUCCESS")){
				JSONObject firstJsonObj  = jsonObject.getJSONObject("Transaction");
				String transactionStatus = firstJsonObj.getString("TransactionResult");
				if(transactionStatus.equalsIgnoreCase("Success")){
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
		catch(Exception ew){
			ew.printStackTrace();
			return false;
		}
	}

	private String onMethodCreation(String cardNumber, String expMonthAndYear, String personName) {
		
		CreditCardHelper cardValidatorUtil = new CreditCardHelper();
		boolean isValidCard = cardValidatorUtil.isCardOk(cardNumber);
		String ccType = "";
		if(isValidCard){
		 ccType =	cardValidatorUtil.getCCType(cardNumber) != null ? cardValidatorUtil.getCCType(cardNumber) : "";
		 if(ccType.isEmpty())
			 return "";
		}
		else
			return "";
		String PAYERMETHODURL = "https://xmltestapi.propay.com/ProtectPay/Payers/"+payerID+"/PaymentMethods/";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httput = new HttpPut(PAYERMETHODURL);
		String responseBody = "";
		HttpResponse response = null;

		String	base64EncodedCredentials = "Basic " + Base64.encodeToString((BillerID+":"+AuthToken).getBytes(),Base64.NO_WRAP);
		System.out.println(base64EncodedCredentials);

		JSONObject obja = new JSONObject();
		try {
			obja.put("AccountNumber",cardNumber);
			obja.put("ExpirationDate",expMonthAndYear);
			obja.put("AccountName",personName);
			obja.put("PaymentMethodType", ccType);
			obja.put("PayerAccountId",payerID);

			System.out.println(obja.toString());
			StringEntity stEn = new StringEntity(obja.toString());
			httput = setHeaderss(httput, base64EncodedCredentials);
			httput.setEntity(stEn);
			response = httpclient.execute(httput);
			responseBody = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			Log.d("Response Line:  ","" + response.getStatusLine().getStatusCode());
			System.out.println(responseBody);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBody;
	}
}

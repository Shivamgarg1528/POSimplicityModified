package com.AsyncTasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.dialog.BackGroundDialog;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.database.local.SaveTransactionDetails;
import com.PosInterfaces.PrefrenceKeyConst;

import com.utils.CreateFormatOnMagentoCall;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.HomeActivity;

public class ManualReportInMagento extends AsyncTask<Void, Integer, String> implements OnCancelListener,PrefrenceKeyConst {

	private String baseUrl;
	private HomeActivity instance;
	private String transactionId;
	private String response;
	private Context mContext;
	private ProgressDialog progressHUD;
	private JSONObject jsonObject;
	private float cash,credit,check,gift,rewards,custom1,custom2;

	public ManualReportInMagento(Context mContext, String transId, int selectedPosition) {

		this.mContext           = mContext;
		this.transactionId      = transId;
		this.instance           = HomeActivity.localInstance;
		this.baseUrl            = MyPreferences.getMyPreference(STORE_NAME, mContext);
		
		switch (selectedPosition) {
		case 0:
			cash = Variables.totalBillAmount;
			break;
		case 1:
			credit = Variables.totalBillAmount;
			break;
		case 2:
			check = Variables.totalBillAmount;
			break;
		case 3:
			gift = Variables.totalBillAmount;
			break;
			
		case 4:
			rewards = Variables.totalBillAmount;
			break;

		case 5:
			custom1 = Variables.totalBillAmount;
			break;

		case 6:
			custom2 = Variables.totalBillAmount;
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPreExecute() {
		progressHUD = ProgressDialog.show(mContext, "", "Saving...", true, false);	
		jsonObject  = new CreateFormatOnMagentoCall(instance.dataList).createJSONObjForRequest();		
	}

	@Override
	protected String doInBackground(Void... params) {
		return excuteApiForResponse();
	}

	@Override
	protected void onPostExecute(String result) {
		progressHUD.dismiss();

		if(response.equalsIgnoreCase("No Internet")){
			BackGroundDialog.noInternetDialog(mContext);
		}
		else if(response.equalsIgnoreCase("ExceptionOccur")){

		}

		else 
		{
			try {
				JSONObject jsonObject= new JSONObject(response);
				if(jsonObject.getString("msg").equalsIgnoreCase("saved in magento database")){
					SaveTransactionDetails.saveTransactionInDataBase(mContext, Variables.itemsAmount, Variables.taxAmount, Variables.totalBillAmount,cash, credit, check, gift, rewards, ReportsTable.NO_REFUND, ReportsTable.MANUALLY_ENTRY,custom1 ,custom2);
					SaveTransactionDetails.saveTransactionInDataBase(mContext, -Variables.itemsAmount, -Variables.taxAmount, -Variables.totalBillAmount,-cash, -credit, -check, -gift, -rewards, ReportsTable.NO_REFUND, ReportsTable.FAILED,-custom1,-custom2);
					ToastUtils.showOwnToast(mContext, "Saved Order Successfully In Magento");
					instance.resetAllData(mContext,1);
					((Activity) mContext).finish();
				}
				else {
					ToastUtils.showOwnToast(mContext, "Failed To Save Order In Magento");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String excuteApiForResponse() {			

		HttpPost hPost = new HttpPost(baseUrl+"?tag=save_orders_in_magento");
		HttpClient hClient = new DefaultHttpClient();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("customerEmail",Variables.billToName));
		nameValuePairs.add(new BasicNameValuePair("discount"     ,   ""+Variables.orderLevelDiscount));	
		nameValuePairs.add(new BasicNameValuePair("transId"      , transactionId));
		nameValuePairs.add(new BasicNameValuePair("orderStatus"  , "complete"));
		nameValuePairs.add(new BasicNameValuePair("details"  ,jsonObject.toString()));
		nameValuePairs.add(new BasicNameValuePair("fee", ""+Variables.fees));


		try {
			UrlEncodedFormEntity urlEncodedFormEntity  = new UrlEncodedFormEntity(nameValuePairs);
			hPost.setEntity(urlEncodedFormEntity);
			System.out.println(baseUrl+"?tag=save_orders_in_magento&"+EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs)));
			ResponseHandler<String> rHandler = new BasicResponseHandler();
			boolean isConnected = InternetConnectionDetector.isInternetAvailable(mContext);
			if (isConnected) 		
				response = hClient.execute(hPost, rHandler);			
			else			
				response = "No Internet";

			Log.v("RESPONSE :",response.trim());	
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response = "ExceptionOccur";
			Log.v("RESPONSE :",response.trim());	
			return response;
		} 
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		progressHUD.dismiss();
	}
}

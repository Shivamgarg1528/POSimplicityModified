package com.posimplicity.gateway;

import org.json.JSONArray;
import org.json.JSONObject;
import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.PosInterfaces.PrefrenceKeyConst;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.WebCallPost;
import com.utils.WebCallPostListener;
import com.utils.WebServiceCall;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class NobleGatway implements PrefrenceKeyConst,WebServiceCallObjectIds, WebCallPostListener{

	private	Context mContext;
	private String cardNumber,amount,nobleApiKey,nobleName,nobleApiKeyName;
	private NobleResponse nobleResponse;
	//private String URL = "https://pay1.plugnpay.com/api/merchant/adjustment";
	//private String url = "https://173.56.11.61/api/merchant/adjustment";
	 
	private String url = "https://pay.noblept.com/api/merchant/adjustment";

	 public static interface NobleResponse{
		 public void onNobleRespose(String jsonString);
	 }

	 public void setInterface(NobleResponse nobleResponse){
		 this.nobleResponse = nobleResponse;
	 }


	 public NobleGatway(Context mContext,String cardNumber,String amount) {
		 super();
		 this.mContext          = mContext;
		 this.cardNumber        = cardNumber;
		 this.amount            = amount;
		 this.nobleApiKeyName   = MyPreferences.getMyPreference(NOBLE_API_KEY_NAME, mContext);		
		 this.nobleApiKey       = MyPreferences.getMyPreference(NOBLE_API_KEY, mContext);		
		 this.nobleName         = MyPreferences.getMyPreference(NOBLE_NAME, mContext);	
	 }

	 public void  onExecute(){
		 onNobleSessionId();
	 }

	 private void onNobleSessionId()
	 {
		 if( TextUtils.isEmpty(nobleApiKeyName)|| TextUtils.isEmpty(nobleApiKey) || TextUtils.isEmpty(nobleName)){
			 Toast.makeText(mContext, "Please Provide NobleInfo First Under Admin Section", Toast.LENGTH_SHORT).show();
			 return;
		 }
		 else{
			 try{
				 JSONObject firstObj    = new JSONObject();
				 JSONArray firstArray   = new JSONArray();				 
				 JSONObject secondObj   = new JSONObject();

				 secondObj.put("cardNumber"        , cardNumber);
				 secondObj.put("paymentVehicleType", "CARD");
				 secondObj.put("transactionAmount" , amount);
				 secondObj.put("paymentVehicleIdentifier", "noblePayment_"+cardNumber+"_");
				 secondObj.put("transactionIdentifier", 1);
				 firstArray.put(secondObj);
				 firstObj.put("transactionInformation", firstArray);
				 WebCallPost webServiceCall = new WebCallPost(url, "Please wait...", OBJECT_ID_1, "Noble Adjustment :-> ", mContext, firstObj.toString(), NobleGatway.this, true, false, true);
				 webServiceCall.execute();
			 }
			 catch(Exception ex){
				 ex.printStackTrace();				
			 }
		 }
	 }

	 @Override
	 public void onCallBack(WebCallPost webServiceCall, String responseData,int responseCode) {
		 webServiceCall.onDismissProgDialog();

		 switch (responseCode) {

		 case WebServiceCall.WEBSERVICE_CALL_EXCEPTION:
			 ExceptionDialog.onExceptionOccur(mContext);
			 break;

		 case WebServiceCall.WEBSERVICE_CALL_NO_INTERENET:	
			 NoInternetDialog.noInternetDialogShown(mContext);
			 break;

		 case WebServiceCall.WEBSERVICE_CALL_RESULT_VALID:		

			 switch (webServiceCall.getWebServiceId()) {

			 case OBJECT_ID_1:

				 try{
					 JSONObject jsonObject1 = new JSONObject(responseData);
					
					 if(jsonObject1.has("error")){
						ToastUtils.showOwnToast(mContext, jsonObject1.getString("error"));
						 return;
					 }
					 
					 JSONObject jsonObject2 = jsonObject1.getJSONObject("content");
					 JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
					 JSONObject jsonObject4 = jsonObject3.getJSONObject("noblePayment_"+cardNumber+"_");
					 JSONObject jsonObject5 = jsonObject4.getJSONObject("calculatedAdjustment");
					 String     adjustmentAmt = jsonObject5.getString("adjustment");
					 nobleResponse.onNobleRespose(adjustmentAmt);
				 }
				 catch(Exception ex)
				 {
					 ex.printStackTrace();
					 ToastUtils.showOwnToast(mContext, "Response Error");
				 }
				 break;
				 
			 default:
				 break;
			 }
			 break;
			 
		 default:
			 break;
		 }
	 }
}

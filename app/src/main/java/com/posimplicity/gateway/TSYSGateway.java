package com.posimplicity.gateway;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.Beans.TSYSResponseModel;
import com.posimplicity.database.local.SaveTransactionDetails;
import com.JsonPakage.JSONObject;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.EncryptedCardInfo;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.Variables;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class TSYSGateway implements PrefrenceKeyConst{
	//TEST URL 	
	//private static final String URL = "https://stagegw.transnox.com/servlets/TransNox_API_Server";	

	// LIVE URL
	private static final String URL = "https://gateway.transit-pass.com/servlets/TransNox_API_Server";


	//private final String TAG = "TSYSGateway";
	private Context mContext;
	private String transactionKey,cardNumber,expMonthAndDate,ksn,
	cardHolderName,trackdata1,trackdata2,transAmount,cardInfo,tsysUserName,tsysPassword,tsysDeviceId,tsysMerchantId,cvv2NumValue,tipAmount;
	private ProgressDialog pHud;
	private OnCallBackForTSYS listener;

	private static final String TSYS_DEVELOPER_ID = "002601G001";
	public static final int TSYS_KEY_GENERATION = 1;
	public static final int TSYS_KEY_UPGRADTION = 2;
	public static final int TSYS_SWIPE_SALES    = 3;
	public static final int TSYS_KEYED_SALES    = 4;
	public static final int TSYS_REFUND         = 5;
	public static final int TSYS_TIP_ADJUSTMENT = 6;
	public static final int TSYS_SWIPE_SALES_WITH_ENCRYPTION = 7;
	private int requestedCode  = 0,tsysTranAmount = 0,tsysTipAmount = 0;

	public TSYSGateway(Context mContext, String transAmount, int requestedCode) {
		extractAllRequiredValuesFromPreferences(mContext,transAmount,requestedCode,"0.00");		
	}


	public TSYSGateway(Context mContext, String transAmount, int requestedCode,String tipAmount) {
		extractAllRequiredValuesFromPreferences(mContext,transAmount,requestedCode,tipAmount);		
	}



	public TSYSGateway(Context mContext, String transAmount, String cardInfo,int requestedCode) {
		extractAllRequiredValuesFromPreferences(mContext,transAmount,requestedCode,"0.00");
		this.cardInfo        = cardInfo;
		this.trackdata1      = this.cardInfo.substring(0,this.cardInfo.indexOf("?;")+1);
		this.trackdata2      = this.cardInfo.substring(this.cardInfo.indexOf("?;")+1,this.cardInfo.lastIndexOf("?")+1);

	}

	public TSYSGateway(Context mContext, String cardNumber,	String expMonthAndDate, String cardHolderName, String transAmount,int requestedCode,String cvv2Num,String tipAmount) {
		extractAllRequiredValuesFromPreferences(mContext,transAmount,requestedCode,tipAmount);
		this.cardNumber      = cardNumber;
		this.expMonthAndDate = expMonthAndDate;
		this.cardHolderName  = cardHolderName;
		this.cvv2NumValue    = cvv2Num;
	}



	public TSYSGateway(Context mContext, String transAmount,EncryptedCardInfo encryptedCardInfo,int requestedCode) {
		extractAllRequiredValuesFromPreferences(mContext,transAmount,requestedCode,"0.00");
		this.trackdata1      = encryptedCardInfo.getTrackData1();
		this.trackdata2      = encryptedCardInfo.getTrackData2();
		this.ksn             = encryptedCardInfo.getDeviceSerialNo();
	}



	private void extractAllRequiredValuesFromPreferences(Context mContext, String transAmount, int requestedCode,String tipAmount) {
		this.mContext        = mContext;
		this.transAmount     = transAmount;
		this.tipAmount       = tipAmount; 
		this.requestedCode   = requestedCode;		
		this.transactionKey  = MyPreferences.getMyPreference(TSYS_TRANSACTION_KEY, mContext);
		this.tsysUserName    = MyPreferences.getMyPreference(TSYS_USER_NAME, mContext);
		this.tsysPassword    = MyPreferences.getMyPreference(TSYS_PASSWORD, mContext);
		this.tsysDeviceId    = MyPreferences.getMyPreference(TSYS_DEVICE_ID, mContext);
		this.tsysMerchantId  = MyPreferences.getMyPreference(TSYS_MERCHA_ID, mContext);
	}

	public static interface OnCallBackForTSYS {
		public void onTSYSResponse(String responseDate, int requestedCodeReturn);		
	}


	/**
	 * 
	 * @param amountValue   that needs to be convert
	 * @return converted    value if success else 0 return 
	 */


	private int doFormatingForAmount() {		
		try{
			if(transAmount.isEmpty())
				return tsysTranAmount;
			this.tsysTranAmount  = (int)(Float.parseFloat(transAmount) * 100.0f);			 
		}
		catch(Exception ex){  ex.printStackTrace(); 
		}
		return this.tsysTranAmount;
	}


	/**
	 * 
	 * @param amountValue   that needs to be convert
	 * @return converted    value if success else 0 return 
	 */


	private int doFormatingForTipAmount() {		
		try{
			if(tipAmount.isEmpty())
				return tsysTipAmount;
			this.tsysTipAmount  = (int)(Float.parseFloat(tipAmount) * 100.0f);			 
		}
		catch(Exception ex){  ex.printStackTrace(); 
		}
		return this.tsysTipAmount;
	}

	/**
	 * 
	 * @return true, if transactionkey exist otherwise return false 
	 */


	public boolean isTransactionKeyExist(){
		return MyPreferences.getMyPreference(TSYS_TRANSACTION_KEY, mContext).isEmpty() ? false : true;
	}	


	public void doExection(){
		new LocalAysnTask().execute();
	}

	class LocalAysnTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			pHud = ProgressDialog.show(mContext, "","Please Wait...",true,false);
		}

		@Override
		protected String doInBackground(Void... params) {
			return requestDataOnTSYSServer();
		}

		@Override
		protected void onPostExecute(String result) {

			if(pHud!= null && pHud.isShowing()){
				pHud.dismiss();
			}
			listener.onTSYSResponse(result,requestedCode);
		}
	}


	public String requestDataOnTSYSServer(){
		doFormatingForAmount();
		doFormatingForTipAmount();
		String response = "Error";
		try{			
			JSONObject firstLevelObj     = new JSONObject();
			Map<String, Object> mapObj   = new LinkedHashMap<String, Object>();

			switch (requestedCode) {

			case TSYS_KEY_UPGRADTION :
			case TSYS_KEY_GENERATION :	

				mapObj.put("mid"     ,    tsysMerchantId);
				mapObj.put("userID"  ,    tsysUserName);
				mapObj.put("password",    tsysPassword);

				if(isTransactionKeyExist())
					mapObj.put("transactionKey", transactionKey);
				mapObj.put("developerID", TSYS_DEVELOPER_ID);

				JSONObject secondLevelObj = new JSONObject(mapObj);
				firstLevelObj.put("GenerateKey", secondLevelObj);				

				response = onHttpRequestOnTSYS(firstLevelObj.toString());			

				break;

			case TSYS_KEYED_SALES:	

				mapObj.put("deviceID"         , tsysDeviceId);
				mapObj.put("transactionKey"   , transactionKey);
				mapObj.put("cardDataSource"   , "MANUAL");
				mapObj.put("transactionAmount", ""+tsysTranAmount);
				mapObj.put("tip",              ""+tsysTipAmount);
				mapObj.put("cardNumber"       , cardNumber);
				mapObj.put("expirationDate"   , expMonthAndDate);
				mapObj.put("cvv2"             , cvv2NumValue);
				mapObj.put("cardHolderName"   , cardHolderName);
				mapObj.put("developerID"      , TSYS_DEVELOPER_ID);

				secondLevelObj = new JSONObject(mapObj);
				firstLevelObj.put("Sale", secondLevelObj);
				response = onHttpRequestOnTSYS(firstLevelObj.toString());

				break;

			case TSYS_SWIPE_SALES:

				mapObj.put("deviceID"         , tsysDeviceId);
				mapObj.put("transactionKey"   , transactionKey);
				mapObj.put("cardDataSource"   , "SWIPE");
				mapObj.put("transactionAmount", ""+tsysTranAmount);
				mapObj.put("tip",              ""+tsysTipAmount);
				mapObj.put("track1Data"       , trackdata1);
				mapObj.put("track2Data"       , trackdata2);
				mapObj.put("developerID"      , TSYS_DEVELOPER_ID);

				secondLevelObj = new JSONObject(mapObj);
				firstLevelObj.put("Sale", secondLevelObj);
				response = onHttpRequestOnTSYS(firstLevelObj.toString());
				break;			

			case TSYS_REFUND:		

				mapObj.put("deviceID"         ,tsysDeviceId);
				mapObj.put("transactionKey"   ,transactionKey);
				mapObj.put("transactionAmount", ""+tsysTranAmount);
				mapObj.put("tip",             ""+tsysTipAmount);
				mapObj.put("transactionID"    , Variables.gateWayTrasId);
				mapObj.put("developerID"      , TSYS_DEVELOPER_ID);

				secondLevelObj = new JSONObject(mapObj);
				firstLevelObj.put("Return", secondLevelObj);
				response = onHttpRequestOnTSYS(firstLevelObj.toString());
				break;

			case TSYS_TIP_ADJUSTMENT:

				mapObj.put("deviceID"         ,tsysDeviceId);
				mapObj.put("transactionKey"   ,transactionKey);
				mapObj.put("tip", ""+tsysTranAmount);
				mapObj.put("transactionID"    , Variables.gateWayTrasId);
				mapObj.put("developerID"      , TSYS_DEVELOPER_ID);

				secondLevelObj = new JSONObject(mapObj);
				firstLevelObj.put("TipAdjustment", secondLevelObj);
				response       = onHttpRequestOnTSYS(firstLevelObj.toString());

				break;

			case TSYS_SWIPE_SALES_WITH_ENCRYPTION:
				mapObj.put("deviceID"         , tsysDeviceId);
				mapObj.put("transactionKey"   , transactionKey);
				mapObj.put("cardDataSource"   , "SWIPE");
				mapObj.put("transactionAmount", ""+tsysTranAmount);
				mapObj.put("tip",               ""+tsysTipAmount);
				//mapObj.put("track1Data"       , trackdata1);
				mapObj.put("track2Data"       , trackdata2);
				mapObj.put("encryptionType"   , "TDES");
				mapObj.put("ksn"              , ksn);
				mapObj.put("tokenRequired"    , "Y");
				mapObj.put("developerID"      , TSYS_DEVELOPER_ID);

				secondLevelObj = new JSONObject(mapObj);
				firstLevelObj.put("Sale", secondLevelObj);
				response = onHttpRequestOnTSYS(firstLevelObj.toString());

				break;

			default:
				break;
			}
		}

		catch(Exception ex){

			ex.printStackTrace();
			response = "Error";
		}
		return response;
	}	

	public TSYSResponseModel paresTSYSResponse(String response){

		TSYSResponseModel responseObj = new TSYSResponseModel("TSYS Transaction Failed !!!", false,"");	

		if(response.isEmpty() || response.equalsIgnoreCase("Error"))
			return responseObj;
		else{
			try{
				JSONObject resultObj         = new JSONObject(response);
				Iterator<String>    keyss    = resultObj.keys();
				if(keyss.hasNext()){
					String string            = (String) keyss.next();
					JSONObject innJsonObject = resultObj.getJSONObject(string);
					String result            = innJsonObject.getString("status");
					if(result.equalsIgnoreCase("ORDER_FAIL")){
						responseObj.setResponseMsg(JSONObJValidator.stringTagValidate(innJsonObject, "responseMessage", "Failed"));
						return responseObj;
					}
					else if(result.equalsIgnoreCase("PASS"))
					{
						responseObj.setResponseMsg(JSONObJValidator.stringTagValidate(innJsonObject, "responseMessage", "Failed"));
						responseObj.setSuccess(true);
						responseObj.setTsysResponse(response);

						switch (requestedCode) {

						case TSYS_KEY_UPGRADTION:
						case TSYS_KEY_GENERATION:
							transactionKey = JSONObJValidator.stringTagValidate(innJsonObject,"transactionKey",transactionKey);
							MyPreferences.setMyPreference(TSYS_TRANSACTION_KEY, transactionKey, mContext);
							break;

						case TSYS_KEYED_SALES:
						case TSYS_SWIPE_SALES:
							Variables.gateWayTrasId = JSONObJValidator.stringTagValidate(innJsonObject, "transactionID", "");
							SaveTransactionDetails.saveTransactionWithId(Variables.gateWayTrasId, "", mContext);
							break;

						default:
							break;
						}
						return responseObj;
					}
					else 
						return responseObj;

				}	
				else 
					return responseObj;
			}
			catch(Exception ex){ 
				ex.printStackTrace();
				return responseObj;
			}			
		}
	}	

	public String onHttpRequestOnTSYS(String stringEntity) throws Exception {
		//Log.e(TAG, "Requested_Data :- "+stringEntity);
		//CreateTextFileInSdCard.onWrittenData(stringEntity,ImageProcessing.BAR_CODE_IMAGE,MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext) +"- RequesetData");
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppostreq = new HttpPost(URL);
		httppostreq.setHeader("user-agent", "infonox");
		httppostreq.setHeader(HTTP.CONTENT_TYPE, "application/json");
		httppostreq.setEntity(new StringEntity(stringEntity, "UTF-8"));
		HttpResponse httpresponse = httpclient.execute(httppostreq);
		//Log.v(TAG,"Response_Status :- " +httpresponse.getStatusLine().getStatusCode());
		String responseBody = EntityUtils.toString(httpresponse.getEntity(), HTTP.UTF_8);
		//Log.e(TAG,"Response_Data   :- " +responseBody);
		//CreateTextFileInSdCard.onWrittenData(responseBody,ImageProcessing.BAR_CODE_IMAGE,MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext) +"- ResponseData");
		return responseBody;
	}


	public void throurghXML (){
		/*		StringBuilder sb = new StringBuilder();
		sb.append("<GenerateKey>");
		sb.append("<mid>"+"887000002601"+"</mid>");
		sb.append("<userID>"+"002601"+"</userID>");
		sb.append("<password>1008$Runner</password>");
		sb.append("</GenerateKey>");
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost("https://stagegw.transnox.com/servlets/TransNox_API_Server");
			HttpParams params2 = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params2, 30000);
			httppostreq.setHeader("user-agent", "infonox");
			httppostreq.setHeader("Content-Type", "text/xml");
			StringEntity se = new StringEntity(sb.toString(), HTTP.UTF_8);
			httppostreq.setEntity(se);
			HttpResponse httpresponse = httpclient.execute(httppostreq);

			String responseBody = EntityUtils.toString(httpresponse.getEntity(), HTTP.UTF_8);
			Log.d("Response Line:  ","" + httpresponse.getStatusLine().getStatusCode());
			System.out.println(responseBody);

		}*/
	}

	public void onInterfaceRegister(OnCallBackForTSYS substitute){
		this.listener = substitute;
	}
}

package com.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class WebServiceCall extends AsyncTask<Void, Integer, Void> {

	public static final int WEBSERVICE_CALL_NO_INTERENET = 0x0001;
	public static final int WEBSERVICE_CALL_EXCEPTION    = 0x0002;
	public static final int WEBSERVICE_CALL_RESULT_VALID = 0x0003;

	private String requetsedUrl;
	private String progessMsg;
	private String response;
	private int webServiceId;
	private String apiName;
	private Context mContext;
	private ArrayList<NameValuePair> postParameters;
	private WebCallBackListener listener;
	private boolean isInternetAvailable;
	private boolean showProgessBar;
	private boolean showCancelble;
	private boolean isEncyNeed;
	private boolean isTaskCompleted;
	private boolean isNoInterent;
	private boolean isExceptionOcuur;
	private ProgressDialog pDialog;


	/** 
	 * @param requetsedUrl where request will go on Server 
	 * @param progessMsg  msg during Request on Screen
	 * @param webServiceId caller Id
	 * @param apiName name of requested API
	 * @param mContext context where Service call
	 * @param postParameters postRequestedData 
	 * @param listener responsive listener before , after call of Service
	 * @param showProgessBar want to show Progress Bar
	 * @param showCancelble  want to dismiss PDialog when back button is pressed or not
	 * @param isEncyNeed 
	 */

	public WebServiceCall(String requetsedUrl, String progessMsg,
			int webServiceId, String apiName, Context mContext,
			ArrayList<NameValuePair> postParameters,
			WebCallBackListener listener, boolean showProgessBar, boolean showCancelble, boolean isEncyNeed) {
		super();
		this.requetsedUrl   = requetsedUrl;
		this.progessMsg     = progessMsg;
		this.webServiceId   = webServiceId;
		this.apiName        = apiName;
		this.mContext       = mContext;
		this.postParameters = postParameters;
		this.listener       = listener;
		this.showProgessBar = showProgessBar;
		this.showCancelble  = showCancelble;
		this.isEncyNeed     = isEncyNeed;
	}



	public WebServiceCall() {
		super();
	}

	public boolean isNoInterent() {
		return isNoInterent;
	}

	public void setNoInterent(boolean isNoInterent) {
		this.isNoInterent = isNoInterent;
	}


	public boolean isExceptionOcuur() {
		return isExceptionOcuur;
	}

	public void setExceptionOcuur(boolean isExceptionOcuur) {
		this.isExceptionOcuur = isExceptionOcuur;
	}



	public boolean isTaskCompleted() {
		return isTaskCompleted;
	}

	public void setTaskCompleted(boolean isTaskCompleted) {
		this.isTaskCompleted = isTaskCompleted;
	}

	public String getRequetsedUrl() {
		return requetsedUrl;
	}

	public void setRequetsedUrl(String requetsedUrl) {
		this.requetsedUrl = requetsedUrl;
	}

	public String getProgessMsg() {
		return progessMsg;
	}

	public void setProgessMsg(String progessMsg) {
		this.progessMsg = progessMsg;
	}

	public int getWebServiceId() {
		return webServiceId;
	}

	public void setWebServiceId(int webServiceId) {
		this.webServiceId = webServiceId;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public ArrayList<NameValuePair> getPostParameters() {
		return postParameters;
	}

	public void setPostParameters(ArrayList<NameValuePair> postParameters) {
		this.postParameters = postParameters;
	}

	public WebCallBackListener getListener() {
		return listener;
	}

	public void setListener(WebCallBackListener listener) {
		this.listener = listener;
	}

	public boolean isInternetAvailable() {
		return isInternetAvailable;
	}

	public void setInternetAvailable(boolean isInternetAvailable) {
		this.isInternetAvailable = isInternetAvailable;
	}

	@Override
	protected void onPreExecute() {

		if(showProgessBar){
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage(progessMsg.isEmpty()?"":progessMsg);
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(showCancelble);
			pDialog.show();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			HttpPost hPost     = new HttpPost(requetsedUrl);
			HttpClient hClient = new DefaultHttpClient();

			if(isEncyNeed){
				UrlEncodedFormEntity urlEncodedFormEntity  = new UrlEncodedFormEntity(postParameters);
				Log.v("WebServiceCall", apiName +"API :-> "+ requetsedUrl+EntityUtils.toString(urlEncodedFormEntity));
				hPost.setEntity(urlEncodedFormEntity);
			}
			else
				Log.v("WebServiceCall", apiName +"API :-> "+ requetsedUrl);

			ResponseHandler<String> rHandler = new BasicResponseHandler();
			isInternetAvailable              = InternetConnectionDetector.isInternetAvailable(mContext);

			if (isInternetAvailable){ 		
				response = hClient.execute(hPost, rHandler);
				Log.i("WebServiceCall", apiName +"API Response :-> "+ response);				

				if(response != null && response.isEmpty()){
					isExceptionOcuur = true;
					publishProgress(2);
				}
				else{
					publishProgress(0);	
				}
			}	
			else{
				isNoInterent = true;
				Log.w("WebServiceCall", "Internet Not Available");

				publishProgress(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			isExceptionOcuur = true;
			Log.e("WebServiceCall", "Some Exception Occur " + ex.getMessage());
			publishProgress(2);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if(values[0] == 0)
			listener.onCallBack(this, response, WEBSERVICE_CALL_RESULT_VALID);
		else if(values[0] == 1)
			listener.onCallBack(this, response, WEBSERVICE_CALL_NO_INTERENET);
		else if(values[0] == 2)
			listener.onCallBack(this, response, WEBSERVICE_CALL_EXCEPTION);
	}

	public void onDismissProgDialog(){

		if(showProgessBar && pDialog.isShowing()){
			pDialog.dismiss();
		}
	}
}

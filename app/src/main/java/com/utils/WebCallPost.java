package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

@SuppressWarnings("deprecation")
public class WebCallPost extends AsyncTask<Void, Integer, Void> {

	public static final int WEBSERVICE_CALL_NO_INTERENET = 0x0001;
	public static final int WEBSERVICE_CALL_EXCEPTION    = 0x0002;
	public static final int WEBSERVICE_CALL_RESULT_VALID = 0x0003;

	private String requetsedUrl;
	private String progessMsg;
	private String response;
	private int webServiceId;
	private String apiName;
	private Context mContext;
	private String jsonData;
	private WebCallPostListener listener;
	private boolean isInternetAvailable;
	private boolean showProgessBar;
	private boolean showCancelble;
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

	public WebCallPost(String requetsedUrl, String progessMsg,
			int webServiceId, String apiName, Context mContext,
			String jsonData,
			WebCallPostListener listener, boolean showProgessBar, boolean showCancelble, boolean isEncyNeed) {
		super();
		this.requetsedUrl   = requetsedUrl;
		this.progessMsg     = progessMsg;
		this.webServiceId   = webServiceId;
		this.apiName        = apiName;
		this.mContext       = mContext;
		this.jsonData       = jsonData;
		this.listener       = listener;
		this.showProgessBar = showProgessBar;
		this.showCancelble  = showCancelble;
	}



	public WebCallPost() {
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
			HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			DefaultHttpClient client = new DefaultHttpClient();
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
			new DefaultHttpClient(mgr, client.getParams());
			// Set verifier     
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

			HttpClient httpclient = new DefaultHttpClient(); 
			HttpPost httpPost     = new HttpPost(requetsedUrl);
			StringEntity se       = new StringEntity(jsonData);
			httpPost.setEntity(se);
			httpPost.setHeader("accept"                , "application/json");
			httpPost.setHeader("content-type"          , "application/json");
			httpPost.setHeader("x-gateway-account"     , "nobledemo");
			httpPost.setHeader("x-gateway-api-key-name", "test_key");
			httpPost.setHeader("x-gateway-api-key"     , "052D0956BADCA3AA65FA11E58DF4B207D820A1F7BC8");
			
			/*			httpPost.setHeader("x-gateway-account"     , MyPreferences.getMyPreference(PrefrenceKeyConst.NOBLE_NAME, mContext));
			httpPost.setHeader("x-gateway-api-key-name", MyPreferences.getMyPreference(PrefrenceKeyConst.NOBLE_API_KEY_NAME, mContext));
			httpPost.setHeader("x-gateway-api-key"     , MyPreferences.getMyPreference(PrefrenceKeyConst.NOBLE_API_KEY, mContext));
			 */
			isInternetAvailable              = InternetConnectionDetector.isInternetAvailable(mContext);
			if (isInternetAvailable){ 	
				HttpResponse httpResponse = httpclient.execute(httpPost);
				System.out.println(httpResponse.getStatusLine().getStatusCode()+"..!!");
				InputStream inputStream   = httpResponse.getEntity().getContent();			

				if(inputStream != null)
					response = convertInputStreamToString(inputStream);
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

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

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

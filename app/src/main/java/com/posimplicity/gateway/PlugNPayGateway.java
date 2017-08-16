package com.posimplicity.gateway;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.utils.Variables;

import android.content.Context;
import android.text.TextUtils;

public class PlugNPayGateway implements PrefrenceKeyConst{

	private final String STRING_ENCODING = "UTF-8";
	private final String PLUG_N_PAY_URL  = "https://pay1.plugnpay.com/payment/pnpremote.cgi";
	private String apiResponse           = null;
	private Context mContext;
	private String plugNPayId            = "";


	public PlugNPayGateway(Context context) {
		super();
		mContext     = context;
		plugNPayId   = MyPreferences.getMyPreference(PLUG_PAY_ID, mContext);
	}


	public boolean plugNPayGatewayMarkTransaction(String plugPayId,String amount,  String expDate, String cardNumber, String nameOnCard){

		try {

			HttpPost httppost = new HttpPost(PLUG_N_PAY_URL);
			ArrayList<NameValuePair> postPar = new ArrayList<NameValuePair>();			

			postPar.add(new BasicNameValuePair("publisher-name",plugPayId));
			postPar.add(new BasicNameValuePair("publisher-password", "55pnpdemo55"));
			postPar.add(new BasicNameValuePair("card-amount", amount));
			postPar.add(new BasicNameValuePair("orderID", MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext)));						
			postPar.add(new BasicNameValuePair("notify-email", "shivam@keyss.in"));			
			postPar.add(new BasicNameValuePair("mode", "mark"));

			httppost.setEntity(new UrlEncodedFormEntity(postPar));

			System.out.println(" API: " + PLUG_N_PAY_URL+EntityUtils.toString(new UrlEncodedFormEntity(postPar)));
			HttpClient  httpclient=new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			apiResponse = httpclient.execute(httppost, responseHandler);
			System.out.println(apiResponse);

			if(apiResponse != null){					
				Properties properties = decodeString(apiResponse);
				if(properties.get("success").toString().equalsIgnoreCase("yes")){
					return true;
				}
				else 
					return false;
			}				
			else
				return false;
		}

		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}	

	public boolean plugNPayGatewayPayment(String plugPayId,String amount,  String expDate, String cardNumber, String nameOnCard){

		try {

			HttpPost httppost= new HttpPost(PLUG_N_PAY_URL);
			ArrayList<NameValuePair> postPar = new ArrayList<NameValuePair>();			

			postPar.add(new BasicNameValuePair("publisher-name",plugPayId));
			postPar.add(new BasicNameValuePair("card-exp",expDate));
			postPar.add(new BasicNameValuePair("card-number", cardNumber));	
			postPar.add(new BasicNameValuePair("card-amount", amount));
			postPar.add(new BasicNameValuePair("card-name", nameOnCard));
			postPar.add(new BasicNameValuePair("authtype", "authpostauth"));						
			postPar.add(new BasicNameValuePair("currency", "USD"));			
			postPar.add(new BasicNameValuePair("mode", "auth"));

			httppost.setEntity(new UrlEncodedFormEntity(postPar));

			System.out.println(" API: " + PLUG_N_PAY_URL+EntityUtils.toString(new UrlEncodedFormEntity(postPar)));
			HttpClient  httpclient=new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			apiResponse = httpclient.execute(httppost, responseHandler);
			System.out.println(apiResponse);

			if(apiResponse != null){					
				Properties properties = decodeString(apiResponse);
				if(properties.get("success").toString().equalsIgnoreCase("yes")){
					if(properties.containsKey("surcharge")){
						String surchargeAmt  = properties.get("surcharge").toString();
						if(!TextUtils.isEmpty(surchargeAmt)){							
							try{
								surchargeAmt = surchargeAmt.replace("%2e", ".");
								Variables.subChargeAmount = Float.parseFloat(surchargeAmt);
							}
							catch(Exception ex){
								ex.printStackTrace();
							}						
						}							
					}
					return true;
				}
				else 
					return false;
			}				
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			e.getMessage();
			e.getCause();
			return false;
		}		
	}

	public boolean plugNPayCcSwipePayment(String cardInfo, String amount){	

		//String cardInfo1 = "%B5241330010601671^PRAMOD KUMAR KAUSHIK^1508101116711921?;5241330010601671=15081011167119200001?";



		try{

			HttpPost httppost= new HttpPost(PLUG_N_PAY_URL);	
			ArrayList<NameValuePair> postPar = new ArrayList<NameValuePair>();
			postPar.add(new BasicNameValuePair("publisher-name",plugNPayId));
			postPar.add(new BasicNameValuePair("authtype", "authpostauth"));
			postPar.add(new BasicNameValuePair("magstripe", cardInfo));			
			postPar.add(new BasicNameValuePair("currency", "USD"));
			postPar.add(new BasicNameValuePair("card-amount", amount));
			postPar.add(new BasicNameValuePair("mode", "auth"));
			httppost.setEntity(new UrlEncodedFormEntity(postPar));
			System.out.println(" API: " + PLUG_N_PAY_URL+EntityUtils.toString(new UrlEncodedFormEntity(postPar)));
			HttpClient  httpclient=new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			apiResponse = null;
			try{
				apiResponse = httpclient.execute(httppost, responseHandler);
				System.out.println(apiResponse);
				if(apiResponse != null){					
					Properties properties = decodeString(apiResponse);
					if(properties.get("success").toString().equalsIgnoreCase("yes")){
						if(properties.containsKey("surcharge")){
							String surchargeAmt  = properties.get("surcharge").toString();
							if(!TextUtils.isEmpty(surchargeAmt)){							
								try{
									surchargeAmt = surchargeAmt.replace("%2e", ".");
									Variables.subChargeAmount = Float.parseFloat(surchargeAmt);
								}
								catch(Exception ex){
									ex.printStackTrace();
								}						
							}							
						}
						return true;
					}
					else 
						return false;
				}				
				else
					return false;
			}catch(Exception e){
				e.printStackTrace();
				e.getMessage();
				e.getCause();
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			e.getMessage();
			e.getCause();
			return false;
		}		
	}

	public boolean onPlugNPayEncryptedData(String cardInfo, String amount){


		//String cardInfo1 = "%B5241330010601671^PRAMOD KUMAR KAUSHIK^1508101116711921?;5241330010601671=15081011167119200001?";
		/*String info = 
				"%B5424180000001732^TGATE/TESTCARD^25120000000000000000?;5424180000001732=25120000000000000000?|0600|601F92D2213F4C33279E100CD32F143ABE929BDF5B830E05CA27254724B4FD69F30E9701A7EBDF739C2CCA9D2697836C32774CD931A99C8B|29ED54540038936B56D53AF95CCF25173C83F55051C69BF5333A3542FDC7A4B0653F023105D8D3CB||61403000|951759FA9368A9EE63467929EFD06E806BCB4A3E71588BF83902E8AB4691A37FAF6A0DAC8AECB29D3CCB36DE6AB78FB8ED5A127DBF4C8527|B299EAA112014AA|A5E8AE7A80ACF5B2|9012510B299EAA000007|A109||1000";
		 */

		try{

			HttpPost httppost= new HttpPost(PLUG_N_PAY_URL);	
			ArrayList<NameValuePair> postPar = new ArrayList<NameValuePair>();
			postPar.add(new BasicNameValuePair("publisher-name",plugNPayId));
			postPar.add(new BasicNameValuePair("authtype", "authpostauth"));
			postPar.add(new BasicNameValuePair("swipedevice", "KYB"));	
			postPar.add(new BasicNameValuePair("magensacc", cardInfo));	
			postPar.add(new BasicNameValuePair("currency", "USD"));
			postPar.add(new BasicNameValuePair("card-amount", amount));
			postPar.add(new BasicNameValuePair("mode", "auth"));
			httppost.setEntity(new UrlEncodedFormEntity(postPar));
			System.out.println(" API: " + PLUG_N_PAY_URL+EntityUtils.toString(new UrlEncodedFormEntity(postPar)));
			HttpClient  httpclient=new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			apiResponse = httpclient.execute(httppost, responseHandler);
			System.out.println(apiResponse);
			if(!apiResponse.isEmpty()){					
				Properties properties = decodeString(apiResponse);
				if(properties.get("success").toString().equalsIgnoreCase("yes")){
					if(properties.containsKey("surcharge")){
						String surchargeAmt  = properties.get("surcharge").toString();
						if(!TextUtils.isEmpty(surchargeAmt)){							
							try{
								surchargeAmt = surchargeAmt.replace("%2e", ".");
								Variables.subChargeAmount = Float.parseFloat(surchargeAmt);
							}
							catch(Exception ex){
								ex.printStackTrace();
							}						
						}							
					}
					return true;
				}
				else 
					return false;
			}				
			else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			e.getMessage();
			e.getCause();
			return false;
		}
	}

	public boolean cardAuthenticationWithPlugNPay( String plugPayId,String amount,  String expDate, String cardNumber, String nameOnCard){

		try {
			HttpPost httppost= new HttpPost(PLUG_N_PAY_URL);	
			ArrayList<NameValuePair> postPar = new ArrayList<NameValuePair>();			
			postPar.add(new BasicNameValuePair("publisher-name",plugPayId));

			/*postPar.add(new BasicNameValuePair("card-exp",expDate));
			postPar.add(new BasicNameValuePair("card-number", cardNumber));	
			postPar.add(new BasicNameValuePair("card-amount", amount));
			postPar.add(new BasicNameValuePair("card-name", nameOnCard));
			postPar.add(new BasicNameValuePair("authtype", "authpostauth"));						
			postPar.add(new BasicNameValuePair("currency", "USD"));			
			postPar.add(new BasicNameValuePair("mode", "auth"));*/


			postPar.add(new BasicNameValuePair("card-exp",expDate));
			postPar.add(new BasicNameValuePair("card-number", cardNumber));	
			postPar.add(new BasicNameValuePair("card-amount", "0.00"));
			postPar.add(new BasicNameValuePair("card-name", nameOnCard));
			postPar.add(new BasicNameValuePair("transflags", "avsonly"));				
			httppost.setEntity(new UrlEncodedFormEntity(postPar));

			System.out.println(" API: " + PLUG_N_PAY_URL+EntityUtils.toString(new UrlEncodedFormEntity(postPar)));
			HttpClient  httpclient=new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try{
				apiResponse = httpclient.execute(httppost, responseHandler);
				System.out.println(apiResponse);
				if(apiResponse != null){					
					Properties properties = decodeString(apiResponse);

					if(properties.get("success").toString().equalsIgnoreCase("yes")){							
						return true;
					}
					else 
						return false;
				}				
				else
					return false;
			}catch(Exception e){
				e.printStackTrace();
				e.getMessage();
				e.getCause();
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			e.getMessage();
			e.getCause();
			return false;
		}	
	}





	private Properties decodeString (String encodedString) {
		// result to be returned
		Properties decodeResult = new Properties();

		String[] pairs = encodedString.split("&");
		for (int pos=0;pos<pairs.length;pos++) {
			String[] pair = pairs[pos].split("=",2);
			if (pair[0] != null) {
				try {
					pair[0] = URLDecoder.decode(pair[0], STRING_ENCODING);
					pair[1] = URLDecoder.decode(pair[1], STRING_ENCODING);
					decodeResult.setProperty(pair[0], pair[1]);
				}
				catch (UnsupportedEncodingException e) {

				}
				catch (ArrayIndexOutOfBoundsException e) {

				}
			} 
		}
		return(decodeResult);
	}

}

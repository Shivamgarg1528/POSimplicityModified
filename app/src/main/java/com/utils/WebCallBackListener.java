package com.utils;

/**
 * @author Shivam Garg
 *  WebCallBackInterface methods will be invoked when data recieved from Services
 *
 */  
public interface WebCallBackListener {
	
/**
 *  onCallBack Method will pass the control where this interface was attached with following information 
 *  @param WebServiceCall this object who invoked the Services
 *  @param String contains the response from Service
 *  @param int responseCode that will tell about the status of request
 */
	
	public void onCallBack(WebServiceCall webServiceCall, String responseData, int responseCode);

}

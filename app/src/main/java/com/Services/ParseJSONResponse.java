package com.Services;

import com.JsonPakage.JSONObject;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;

import android.content.Context;

public class ParseJSONResponse implements PrefrenceKeyConst{

	public  static boolean isStoreValid(String responseData ,Context mContext){
		boolean isStoreExist = false;
		try{
			JSONObject jsonObject = new JSONObject(responseData);
			String validStore     = JSONObJValidator.stringTagValidate(jsonObject, "msg", "");
			if(validStore.isEmpty() || !validStore.equalsIgnoreCase("Valid Store"))
				isStoreExist = false;
			else
				isStoreExist = true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			isStoreExist = false;
		}
		return isStoreExist;		
	}

	public static void storeServerTime(String responseData, Context mContext) {		
		try{
			JSONObject jsonObject = new JSONObject(responseData);
			String serverTime     = JSONObJValidator.stringTagValidate(jsonObject, "server_time", "");
			MyPreferences.setMyPreference(SETUP_TIME, serverTime, mContext);			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

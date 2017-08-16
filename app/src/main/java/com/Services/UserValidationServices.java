package com.Services;

import com.JsonPakage.JSONObject;

import android.content.Context;

public class UserValidationServices {

	public static boolean iSUserValid(String responseData, Context mContext){

		boolean validUser = false;
		try{
			JSONObject jsonObject = new JSONObject(responseData);
			if(jsonObject.getString("success_msg").equalsIgnoreCase("User Found") || jsonObject.getString("success_msg").equalsIgnoreCase("Customers Deleted Successfully"))
				validUser = true;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}		
		return validUser;
	}
}

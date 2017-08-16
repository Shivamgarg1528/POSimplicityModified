package com.Services;

import org.json.JSONObject;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;

import android.content.Context;

public class LastOrderIdServices implements PrefrenceKeyConst{

	public static void getLastId(String reponseData, Context mContext, String deviceId) {
		try{
			JSONObject jsonObject = new JSONObject(reponseData);
			String deviceCode     = JSONObJValidator.stringTagValidate(jsonObject, "device_code", "0");
			String orderIdCode    = JSONObJValidator.stringTagValidate(jsonObject, "last_unique_order_id", "0");
			
			MyPreferences.setMyPreference(DEVICE_CODE, deviceCode, mContext);
			
			if(!deviceCode.isEmpty() && !orderIdCode.isEmpty()){
				String actuaId = orderIdCode.substring(orderIdCode.lastIndexOf("-") + 1);
				MyPreferences.setLongPreferences(TRANSACTION_ID, Long.parseLong(actuaId), mContext);
			}
			else
				MyPreferences.setLongPreferences(TRANSACTION_ID, 0, mContext);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

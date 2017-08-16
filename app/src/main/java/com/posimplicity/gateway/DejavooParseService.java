package com.posimplicity.gateway;

import android.content.Context;

import com.Beans.DejavooResponse;
import com.JsonPakage.JSONObject;
import com.JsonPakage.XML;
import com.PosInterfaces.PrefrenceKeyConst;
import com.google.gson.GsonBuilder;
import com.utils.MyPreferences;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class DejavooParseService {

	public LinkedHashMap<String, String> allParsedData = new LinkedHashMap<>();
	public DejavooResponse dejavooResponse    = null;
	private boolean isDejavooOk               = false;
	private String  responseData       = "";
	public  String [] array            = {"CardType","BatchNum","AcntLast4"};

	public DejavooParseService(String responseData) {
		super();
		this.responseData = responseData;
	}	

	public boolean parseData(Context context){
		try{
			JSONObject jsonObject       = XML.toJSONObject(responseData);
			dejavooResponse             = new GsonBuilder().create().fromJson(jsonObject.toString(), DejavooResponse.class);
			if(dejavooResponse.getXmp().getResponse().getMessage().equalsIgnoreCase("Approved")){
				List<String> responeData = Arrays.asList(dejavooResponse.getXmp().getResponse().getExtData().split(","));
				for(int index = 0; index < responeData.size(); index++){
					List<String> eachString = Arrays.asList(responeData.get(index).split("="));
					if(eachString.size() > 1){
						allParsedData.put(eachString.get(0), eachString.get(1));
						if(eachString.get(0).equalsIgnoreCase("InvNum")){
							MyPreferences.setMyPreference(PrefrenceKeyConst.INV_NUM, eachString.get(1), context);
						}
						else if(eachString.get(0).equalsIgnoreCase("AcntLast4")){
							MyPreferences.setMyPreference(PrefrenceKeyConst.ACNTLAST4,  eachString.get(1), context);
						}
					}
				}
				isDejavooOk =  true;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			isDejavooOk = false;
		}
		return isDejavooOk;
	}

	public String getKeyValue(String key){
		if(isDejavooOk){
			return allParsedData.get(key) != null ? allParsedData.get(key) : "";
		}
		return "";
	}
}

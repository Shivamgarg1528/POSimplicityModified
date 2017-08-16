package com.utils;

import com.PosInterfaces.PrefrenceKeyConst;

import android.content.Context;

public class TenderEnable {

	public static boolean isCheckTenderEnable(Context mContext){
		return MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.CHECK_TENDER, mContext);
	}
	
	public static boolean isCashTenderEnable(Context mContext){
		return MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.CASH_TENDER, mContext);
	}
	
	public static boolean isCreditTenderEnable(Context mContext){
		return MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.CREDIT_TENDER, mContext);
	}
	
	public static boolean isTenderTenderEnable(Context mContext){
		return MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.TENDER_TENDER, mContext);
	}
	
	public static boolean isUnRecordedTenderEnable(Context mContext){
		return MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.UNRECORED_TENDER, mContext);
	}
	
	public static boolean isCustom1TenderEnable(Context mContext){
		return MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.CUSTOM_1_TENDER, mContext);
	}
	
	public static boolean isCustom2TenderEnable(Context mContext){
		return MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.CUSTOM_2_TENDER, mContext);
	}
}

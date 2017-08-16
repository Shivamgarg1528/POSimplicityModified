package com.utils;

import com.Fragments.MaintFragmentOtherSetting;
import com.PosInterfaces.PrefrenceKeyConst;

import android.content.Context;

public class LocationOfPosApp implements PrefrenceKeyConst{
	
	public static boolean isRetailsIsActive(Context mContext){
		boolean isActive = false;
		
		if(MaintFragmentOtherSetting.RETAILS_ == MyPreferences.getLongPreference(POS_STORE_TYPE, mContext)){
			isActive = true;
		}	
		
		return isActive;		
	}
	
	public static boolean isQuickIsActive(Context mContext){
		boolean isActive = false;
		
		if(MaintFragmentOtherSetting.QUICK_ == MyPreferences.getLongPreference(POS_STORE_TYPE, mContext)){
			isActive = true;
		}	
		
		return isActive;		
	}
	
	public static boolean isRestraActive(Context mContext){
		boolean isActive = false;
		
		if(MaintFragmentOtherSetting.RESTAURANT_ == MyPreferences.getLongPreference(POS_STORE_TYPE, mContext)){
			isActive = true;
		}			
		return isActive;		
	}
	
	public static boolean isBarActive(Context mContext){
		boolean isActive = false;
		
		if(MaintFragmentOtherSetting.BAR_ == MyPreferences.getLongPreference(POS_STORE_TYPE, mContext)){
			isActive = true;
		}		
		return isActive;		
	}
}

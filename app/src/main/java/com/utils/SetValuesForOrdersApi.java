package com.utils;

import com.Fragments.MaintFragmentOtherSetting;
import com.PosInterfaces.PrefrenceKeyConst;

import android.content.Context;

public class SetValuesForOrdersApi implements PrefrenceKeyConst {

	public String  orderStatus = "complete";
	public Context mContext;


	public SetValuesForOrdersApi(Context mContext) {
		super();
		this.mContext = mContext;
	}	

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public void setallRequestedValues(){
		
		if(MyPreferences.getLongPreference(POS_STORE_TYPE, mContext) != MaintFragmentOtherSetting.RETAILS_)
			orderStatus = "pending";
		
	}
}

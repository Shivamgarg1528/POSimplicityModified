package com.utils;

import android.content.Context;
import android.widget.EditText;

public class VerifyFieldNotEmpty {

	public static boolean isFieldEmpty (Context mContext,EditText view,String msg){
		if(view == null || view.getText().toString().isEmpty()){
			view.setError(msg);
			return true;
		}
		return false;
	}
}

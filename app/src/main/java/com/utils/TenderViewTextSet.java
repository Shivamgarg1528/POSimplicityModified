package com.utils;

import android.widget.TextView;

public class TenderViewTextSet {
	
	public static void setTextOnView(TextView tv, StringBuilder stringBuilder, String pressedKey,int keyPurpose,boolean specialCase){
		if(keyPurpose == FixedStorage.ADD_CHAR)
			tv.setText(InputCalculation.onKeyDown(stringBuilder, pressedKey,specialCase));
		else if(keyPurpose == FixedStorage.REM_CHAR)
			tv.setText(InputCalculation.onKeyUp(stringBuilder));
	}

	public static float getAmountFromTextView(TextView tv){
		float amount = 0.0f;
		try{
			amount       = Float.parseFloat(tv.getText().toString());	
			amount       = Float.parseFloat(MyStringFormat.formatWith2DecimalPlaces(amount));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}		
		return amount;
	}
}

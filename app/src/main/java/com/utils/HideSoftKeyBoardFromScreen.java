package com.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HideSoftKeyBoardFromScreen {
	
	public static void onHideSoftKeyBoard(Context mContext ,View obtainedEditText){
		
		InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(obtainedEditText.getWindowToken(), 0);
		
	}
}

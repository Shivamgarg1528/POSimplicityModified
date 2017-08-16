package com.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	
	public static void showOwnToast(Context mcontext,String toastMsg){
		Toast.makeText(mcontext, toastMsg, Toast.LENGTH_SHORT).show();
	}
	public static void showOwnToastLong(Context mcontext,String toastMsg){
		Toast.makeText(mcontext, toastMsg, Toast.LENGTH_LONG).show();
	}
	
}

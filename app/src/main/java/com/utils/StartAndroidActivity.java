package com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class StartAndroidActivity {

	public static <T> void onActivityStart(boolean noHistoryTrue, Context mContext,Class<T> classs) {			
		
		mContext.startActivity(new Intent(mContext,classs));		
		if(noHistoryTrue)
			((Activity) mContext).finish();
	}
}

package com.PosBroadcast;

import java.util.Calendar;

import android.app.AlarmManager;
import android.content.Context;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;

public class AutologoutConnectionClass extends PosAbstractService implements PrefrenceKeyConst {

	public AutologoutConnectionClass(Context mContext) {
		super(mContext);
	}

	public void startService(){
		super.onPosServiceSetup(AutologoutService.class);
		setupAlarm();
	}

	public void stopService(){

		if(alarmManager != null && pendingIntent != null){
			alarmManager.cancel(pendingIntent);
		}
	}

	@Override
	public void setupAlarm() {

		long hours = MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.HOURS_OF_DAY, mContext);
		long mint  = MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.MINUTES, mContext);
		
		Calendar calendar = Calendar.getInstance();
		
		System.out.println(hours);
		System.out.println(mint);
		
		long frequency = 1 * 1000;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent); 
	}
}


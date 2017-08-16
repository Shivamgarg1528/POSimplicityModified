package com.PosBroadcast;

import java.util.Calendar;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.posimplicity.SplashActivity;
import android.app.IntentService;
import android.content.Intent;

public class AutologoutService extends IntentService {

	public AutologoutService() {
		super(AutologoutService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {	
		
		long hours = MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.HOURS_OF_DAY, this);
		long mint  = MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.MINUTES, this);
		//System.out.println(hours +"-"+mint);
		
		Calendar c = Calendar.getInstance();
		//System.out.println(c.get(Calendar.HOUR_OF_DAY) +"-"+c.get(Calendar.MINUTE) +"-"+c.get(Calendar.SECOND));
		if(c.get(Calendar.HOUR_OF_DAY) == hours && c.get(Calendar.MINUTE) == mint && c.get(Calendar.SECOND) == 0){
			Intent intent1 = new Intent(this, SplashActivity.class);
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent1);	
		}
	}
}

package com.PosBroadcast;

import java.util.Calendar;
import android.app.AlarmManager;
import android.content.Context;
import com.PosInterfaces.PrefrenceKeyConst;

public class SocketConnectionClass  extends PosAbstractService implements PrefrenceKeyConst {

	public SocketConnectionClass(Context mContext) {
		super(mContext);
	}

	public void startService(){
		super.onPosServiceSetup(SocketConnectionService.class);
		setupAlarm();
	}

	public void stopService(){

		if(alarmManager != null && pendingIntent != null){
			alarmManager.cancel(pendingIntent);
		}

		if(gApp.getSocketIo() != null){
			gApp.getSocketIo().disconnetSocket();
		}
	}

	@Override
	public void setupAlarm() {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);
		long frequency = 10 * 1000;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent); 
	}
}

package com.PosBroadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.PosInterfaces.PrefrenceKeyConst;
import com.Socket.SocketIO;
import com.utils.CheckAppStatus;
import com.utils.POSApp;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.posimplicity.HomeActivity;

public class SocketConnectionService extends Service implements PrefrenceKeyConst{ 

	POSApp gApp = POSApp.getInstance();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean appIsRunning = CheckAppStatus.isAppRunning(this); 
		if(appIsRunning && InternetConnectionDetector.isInternetAvailable(this) && MyPreferences.getBooleanPrefrences(IS_SOCKET_NEEDED, this))
		{
			if(gApp.getSocketIo() != null){
				if(gApp.getSocketIOClient() == null)
					gApp.getSocketIo().connectSocket();		
				Log.e(this.getClass().getCanonicalName(), this.getClass().getName()+" is Running... = Creating Socket Connection with inner if part");
			}
			else
			{
				if(HomeActivity.localInstance != null ){
					SocketIO socketIO = new SocketIO(this);
					socketIO.setSocketRecievedData(HomeActivity.localInstance);
					socketIO.connectSocket();
					gApp.setSocketIo(socketIO);
					Log.e(this.getClass().getCanonicalName(), this.getClass().getName()+" is Running... = Creating Socket Connection with inner else part");
				}
			}
		}
		else
			Log.e(this.getClass().getCanonicalName(), this.getClass().getName()+" is Running... else()");

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}	
}

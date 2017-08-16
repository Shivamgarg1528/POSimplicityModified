package com.posimplicity.service;

import android.app.IntentService;
import android.content.Intent;

import com.Fragments.MaintFragmentPrinterSetting;
import com.PosInterfaces.PrefrenceKeyConst;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.WifiPR;
import com.utils.CheckAppStatus;
import com.utils.POSApp;
import com.utils.MyPreferences;

public class WFService extends IntentService implements PrinterCallBack {		

	private BasePR basePrinter;
	private POSApp gApplication = POSApp.getInstance();
	public  static boolean runningService  = true;

	public WFService() {
		super(WFService.class.getCanonicalName());
		basePrinter = new WifiPR(this,this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while(runningService){
			try {
				boolean appIsRunning = CheckAppStatus.isAppRunning(this);
				((WifiPR)basePrinter).setIpAddress(MyPreferences.getMyPreference(PrefrenceKeyConst.WIFI_IP_ADDRESS, this));
				if(!basePrinter.isConnected() && appIsRunning && !MyPreferences.getMyPreference(PrefrenceKeyConst.WIFI_IP_ADDRESS, this).isEmpty() && MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_WIFI_ON_PS, this))
					basePrinter.onConnectionStart();

				Thread.sleep(13000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onConnected(BasePR pPrinter) {
		gApplication.setPrinterWF(pPrinter);
	}

	@Override
	public void onDisconnected() {
		updateUiWhenSameFragmentOpen("onConnectionStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(basePrinter != null && ((WifiPR)basePrinter).getWifiCommunication() != null){
			((WifiPR)basePrinter).getWifiCommunication().close();
			return;
		}
		else
			updateUiWhenSameFragmentOpen("onConnectionStop");
	}

	private void updateUiWhenSameFragmentOpen(String calledMethod){
		System.out.println(WFService.class.getSimpleName().concat(" ( ").concat(calledMethod).concat(" ) "));
		gApplication.setPrinterWF(null);
		ServiceUtils.operateWFService(this, false);
		if(gApplication.getVisibleFragment() != null ){
			if (gApplication.getVisibleFragment() instanceof MaintFragmentPrinterSetting){		
				MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_WIFI_ON_PS,false,this);				
				MaintFragmentPrinterSetting settingsfrg = (MaintFragmentPrinterSetting) gApplication.getVisibleFragment();
				settingsfrg.loadAndRefereshList();
			} 
		}
	}
}

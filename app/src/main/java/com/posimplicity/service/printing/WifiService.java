package com.posimplicity.service.printing;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.WifiPR;
import com.posimplicity.BuildConfig;
import com.utils.Constants;
import com.utils.POSApp;

public class WifiService extends IntentService implements PrinterCallBack {

    private final String TAG = this.getClass().getName();

    private WifiPR mWifiPrinter;
    private static boolean sServiceRunning = true;

    public WifiService() {
        super(WifiService.class.getName());
        mWifiPrinter = new WifiPR(this, this);
        mWifiPrinter.setIpAddress(mWifiPrinter.getPrinterSetting().getWifiAddress());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onHandleIntent() called with: intent = [" + intent + "]");
        }
        boolean connectionRequired = mWifiPrinter.getPrinterSetting().getDetail().get(mWifiPrinter.getPrinterSetting().getWifiPrinting()).isEnable();
        Log.i(TAG, "WIFI Printer Connection Required ? (true/false) = " + connectionRequired);
        if (!connectionRequired) {
            mWifiPrinter.onConnectionStop();
            return;
        }
        while (sServiceRunning) {
            if (!mWifiPrinter.isConnected()) {
                mWifiPrinter.onConnectionStart();
            }
            SystemClock.sleep(13000);
        }
        mWifiPrinter.onConnectionStop();
    }

    @Override
    public void onConnected(BasePR pPrinter) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onConnected() called with: pPrinter = [" + pPrinter + "]");
        }
        POSApp.getInstance().setPrinterWF(pPrinter);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.ACTION_WIFI_CONNECTED));
    }

    @Override
    public void onDisconnected() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDisconnected() called");
        }
        sServiceRunning = false;
        POSApp.getInstance().setPrinterWF(null);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.ACTION_WIFI_DISCONNECTED));
    }

    public static void startService(Context pContext) {
        sServiceRunning = true;
        Intent intent = new Intent(pContext, WifiService.class);
        pContext.startService(intent);
    }

    public static void stopService() {
        sServiceRunning = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}

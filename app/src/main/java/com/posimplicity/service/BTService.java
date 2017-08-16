package com.posimplicity.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.SetupPrinter.BasePR;
import com.SetupPrinter.BluetPR;
import com.SetupPrinter.PrinterCallBack;
import com.posimplicity.BuildConfig;
import com.posimplicity.R;
import com.utils.BluetoothHelper;
import com.utils.Constants;
import com.utils.POSApp;

public class BTService extends IntentService implements PrinterCallBack {

    private final String TAG = this.getClass().getName();

    private BluetPR mBlueToothPrinter;
    public static boolean sServiceRunning = true;

    public BTService() {
        super(BTService.class.getName());
        mBlueToothPrinter = new BluetPR(this, this);
        mBlueToothPrinter.setMacAddress(mBlueToothPrinter.getPrinterSetting().getBluetoothAddress());
    }

    @Override
    protected void onHandleIntent(Intent pIntent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onHandleIntent() called with: intent = [" + pIntent + "]");
        }
        if (!BluetoothHelper.isBluetoothAvailable()) {
            Log.i(TAG, getString(R.string.string_bluetooth_not_supported_by_device));
            mBlueToothPrinter.onConnectionStop();
            return;
        }
        boolean connectionRequired = mBlueToothPrinter.getPrinterSetting().getDetail().get(mBlueToothPrinter.getPrinterSetting().getBluetoothPrinting()).isEnable();
        Log.i(TAG, "BT Printer Connection Required ? (true/false) = " + connectionRequired);
        if (!connectionRequired) {
            mBlueToothPrinter.onConnectionStop();
            return;
        }
        while (sServiceRunning) {
            if (BluetoothHelper.isBluetoothOpen()) {
                if (!mBlueToothPrinter.isConnected()) {
                    mBlueToothPrinter.onConnectionStart();
                }
            } else {
                BluetoothHelper.openBluetoothSocketManually();
            }
            SystemClock.sleep(5000);
        }
        mBlueToothPrinter.onConnectionStop();
    }

    @Override
    public void onConnected(BasePR pPrinter) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onConnected() called with: pPrinter = [" + pPrinter + "]");
        }
        POSApp.getInstance().setPrinterBT(pPrinter);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.ACTION_BLUETOOTH_CONNECTED));
    }

    @Override
    public void onDisconnected() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDisconnected() called");
        }
        sServiceRunning = false;
        POSApp.getInstance().setPrinterBT(null);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.ACTION_BLUETOOTH_DISCONNECTED));
    }

    public static void startService(Context pContext) {
        sServiceRunning = true;
        Intent intent = new Intent(pContext, BTService.class);
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

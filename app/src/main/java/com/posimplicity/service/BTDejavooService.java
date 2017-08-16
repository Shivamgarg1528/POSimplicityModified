package com.posimplicity.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.Bluetooths.ConnectionCallBack;
import com.Fragments.DejavooFragment;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.BluetoothHelper;
import com.utils.POSApp;
import com.utils.MyPreferences;

public class BTDejavooService extends IntentService implements ConnectionCallBack {

    public static boolean runningService = true;
    private static POSApp gApplication = POSApp.getInstance();
    private static GetReceivedData s_getReceivedDataCallBack;

    public BTDejavooService() {
        super(BTDejavooService.class.getSimpleName());
    }

    public static boolean isBtConnectionAvailable() {
        return gApplication.bluetoothConnector != null && gApplication.bluetoothConnector.isConnected();
    }
    public interface GetReceivedData{
        void onData(String st);
    }

    public static void getData(GetReceivedData getReceivedData){
        s_getReceivedDataCallBack = getReceivedData;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        gApplication.bluetoothConnector.setConnectionCallBack(this);
        while (runningService) {

            if (BluetoothHelper.isBluetoothOpen() && !TextUtils.isEmpty(MyPreferences.getMyPreference(PrefrenceKeyConst.REMOTE_DEVICE_ADDRESS, this)) && MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PAYMENT_VIA_BLUETOOTH, this)) {
                gApplication.bluetoothConnector.setDeviceMacAddress(MyPreferences.getMyPreference(PrefrenceKeyConst.REMOTE_DEVICE_ADDRESS, this));
                if (!gApplication.bluetoothConnector.isConnected())
                    gApplication.bluetoothConnector.connect();
                else
                    gApplication.bluetoothConnector.sendDatOverTerminal("\0");
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateUiWhenSameFragmentOpen("onDestroy");
    }

    private void updateUiWhenSameFragmentOpen(String calledMethod) {
        Log.e(BTDejavooService.this.getClass().getName(), BTDejavooService.class.getSimpleName().concat(" ( ").concat(calledMethod).concat(" ) "));
        gApplication.bluetoothConnector.forceDisconnect();
    }

    private void changeState(){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (gApplication.getVisibleFragment() != null)
                    if (gApplication.getVisibleFragment() instanceof DejavooFragment) {
                        DejavooFragment dejavooFragment = (DejavooFragment) gApplication.getVisibleFragment();
                        dejavooFragment.setMacAddressOnTextView();
                        dejavooFragment.mSwitchState.setChecked(BTDejavooService.isBtConnectionAvailable());
                    }
            }
        });
    }

    @Override
    public void onConnected() {
        changeState();
    }

    @Override
    public void onDataReceived(String data) {
        s_getReceivedDataCallBack.onData(data);
    }

    @Override
    public void onDisconnected() {
        changeState();
    }
}

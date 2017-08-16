package com.posimplicity.service;

import android.content.Context;
import android.content.Intent;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.BluetoothHelper;
import com.utils.MyPreferences;
import com.utils.Variables;

public class ServiceUtils {

    public static void operateBTService(final Context mContext, boolean start) {
        if (start) {
            if (!MyPreferences.getMyPreference(PrefrenceKeyConst.BLUETOOTH_DEVICE_ADDRESS_PRINTING, mContext).isEmpty() && BluetoothHelper.isBluetoothOpen()) {
                BTService.sServiceRunning = true;
                mContext.startService(new Intent(mContext, BTService.class));
            }
        } else {
            BTService.sServiceRunning = false;
            mContext.stopService(new Intent(mContext, BTService.class));
        }
    }

    public static void operateWFService(final Context mContext, boolean start) {
        if (start) {
            BTService.sServiceRunning = true;
            mContext.startService(new Intent(mContext, WFService.class));
        } else {
            BTService.sServiceRunning = false;
            mContext.stopService(new Intent(mContext, WFService.class));
        }
    }

    public static void operateBTDejavooService(final Context mContext, boolean start) {
        BTDejavooService.runningService = start;
        if (start) {
            mContext.startService(new Intent(mContext, BTDejavooService.class));
        } else {
            Variables.forceCloseBluetooth = true;
            mContext.stopService(new Intent(mContext, BTDejavooService.class));
        }
    }
}

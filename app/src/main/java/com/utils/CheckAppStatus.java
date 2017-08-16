package com.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

public class CheckAppStatus {

    @SuppressWarnings("deprecation")
    public static boolean isAppRunning(Context mContext) {

        String ownAppPackageName = mContext.getPackageName();
        String runningAppPackageName = "";

        if (Build.VERSION.SDK_INT < 21) {
            runningAppPackageName = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName();
        } else {
            final List<ActivityManager.RunningAppProcessInfo> pis = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            if (pis != null) {
                for (ActivityManager.RunningAppProcessInfo pi : pis) {
                    if (pi.pkgList.length == 1) {
                        runningAppPackageName = pi.pkgList[0];
                        break;
                    }
                }
            }
        }

        //System.out.println("Running App Package Name ->   "+ runningAppPackageName);
        //System.out.println("Own     App Package Name ->   "+ ownAppPackageName);

        if (runningAppPackageName != null && !runningAppPackageName.isEmpty() && ownAppPackageName.equalsIgnoreCase(runningAppPackageName))
            return true;
        else
            return false;
    }
}

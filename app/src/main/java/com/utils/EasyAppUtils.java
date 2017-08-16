package com.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.posimplicity.BuildConfig;

import java.util.List;

/**
 * Created by sachin.gupta on 28-03-2016.
 */
public class EasyAppUtils {

    private static final String LOG_TAG = EasyAppUtils.class.getSimpleName();

    /**
     * @param pContext
     * @return
     */
    public static String getApplicationName(Context pContext) {
        return pContext.getApplicationInfo().loadLabel(pContext.getPackageManager()).toString();
    }

    /**
     * @param pContext
     * @return
     */
    public static int getApplicationVersionCode(Context pContext) {
        PackageManager packageManager = pContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(pContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * @param pContext
     * @return
     */
    public static String getApplicationVersionName(Context pContext) {
        PackageManager packageManager = pContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(pContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return "";
        }
    }

    /**
     * @param pContext
     * @return
     */
    public static Bundle getMetadata(Context pContext) {
        try {
            PackageManager pm = pContext.getPackageManager();
            ApplicationInfo aiWithMeta = pm.getApplicationInfo(pContext.getPackageName(), PackageManager.GET_META_DATA);
            return aiWithMeta.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * @param pContext
     * @return
     */
    public static boolean isPackageInstalled(Context pContext, String pPackageName) {
        try {
            pContext.getPackageManager().getPackageInfo(pPackageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * @param pContext
     * @param pPackageName
     * @param pActivityFqcn
     * @return
     */
    public static ActivityInfo getActivityInfo(Context pContext, String pPackageName, String pActivityFqcn) {
        PackageInfo packageInfo;
        try {
            packageInfo = pContext.getPackageManager().getPackageInfo(pPackageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return null;
        }
        if (packageInfo.activities == null || packageInfo.activities.length == 0) {
            return null;
        }
        String activityFqcn;
        for (ActivityInfo activityInfo : packageInfo.activities) {
            activityFqcn = getFqcn(pPackageName, activityInfo.name);
            if (activityFqcn.equals(pActivityFqcn)) {
                return activityInfo;
            }
        }
        return null;
    }

    /**
     * @param pPackageName
     * @param pActivityName
     * @return
     */
    public static String getFqcn(String pPackageName, String pActivityName) {
        if (pActivityName.startsWith(".")) {
            return pPackageName + pActivityName;
        }
        if (pActivityName.indexOf('.') == -1) {
            return pPackageName + '.' + pActivityName;
        }
        return pActivityName;
    }

    /**
     * @param pContext
     * @return
     * @see ActivityManager#getRunningTasks(int)
     */
    @Deprecated
    public static ComponentName getActivityOnTop(Context pContext) {
        ActivityManager activityManager = (ActivityManager) pContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        return tasks != null && !tasks.isEmpty() ? tasks.get(0).topActivity : null;
    }

    /**
     * @param pContext
     * @param pComponentClazz
     * @param pEnable
     */
    public static void enable(Context pContext, Class pComponentClazz, boolean pEnable) {
        if (BuildConfig.DEBUG) {
            Log.v(LOG_TAG, "enable() " + pComponentClazz + ", " + pEnable);
        }
        PackageManager packageManager = pContext.getPackageManager();
        int newState;
        if (pEnable) {
            newState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        } else {
            newState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        }
        ComponentName componentName = new ComponentName(pContext.getPackageName(), pComponentClazz.getCanonicalName());
        packageManager.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP);
    }

    /**
     * @param pPackageInfo
     * @return
     */
    public static boolean isSystem(PackageInfo pPackageInfo) {
        return (pPackageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;
    }

    /**
     * @param pPackageInfo
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isGame(PackageInfo pPackageInfo) {
        return (pPackageInfo.applicationInfo.flags & ApplicationInfo.FLAG_IS_GAME) == ApplicationInfo.FLAG_IS_GAME;
    }

}

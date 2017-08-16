package com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.easylibs.utils.JsonUtils;
import com.posimplicity.model.local.OrderModel;
import com.posimplicity.model.local.Setting;

public class AppSharedPrefs {

    private static final String KEY_ORDER_MODEL = "keyOrderModel";
    private static final String KEY_SETTING_MODEL = "keySettingModel";

    private static AppSharedPrefs sAppSharedPrefs;

    public static synchronized AppSharedPrefs getInstance(Context pContext) {
        if (sAppSharedPrefs == null) {
            sAppSharedPrefs = new AppSharedPrefs(pContext);
        }
        return sAppSharedPrefs;
    }

    private AppSharedPrefs(Context pContext) {
        mSharedPreferences = pContext.getSharedPreferences("POSimplicityPref", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public synchronized OrderModel getOrderModel() {
        String json = mSharedPreferences.getString(KEY_ORDER_MODEL, null);
        return JsonUtils.objectify(json, OrderModel.class);
    }

    public synchronized void setOrderModel(OrderModel pOrderModel) {
        mEditor.putString(KEY_ORDER_MODEL, JsonUtils.jsonify(pOrderModel));
        mEditor.commit();
    }

    public void setSetting(Setting pSetting) {
        mEditor.putString(KEY_SETTING_MODEL, JsonUtils.jsonify(pSetting));
        mEditor.commit();
        POSApp.getInstance().setSettings(pSetting);
    }

    public Setting getSetting() {
        String json = mSharedPreferences.getString(KEY_SETTING_MODEL, null);
        return JsonUtils.objectify(json, Setting.class);
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }
}

package com.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyPreferences{
	
	private static String MyPreference = "POSimplicityPrefs";

	public static void setMyPreference(String key, String value, Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyPreference,Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getMyPreference(String key, Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}
	
	public static String getMyPreference(String key, Context context,String defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}
	
	public static void setBooleanPrefrences(String key,boolean value,Context context){
		SharedPreferences preferences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static boolean getBooleanPrefrences(String key,Context context){
		SharedPreferences prefrences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		return prefrences.getBoolean(key, false);
	}
	
	public static boolean getBooleanPreferencesWithDefalutTrue(String key,Context context){
		SharedPreferences prefrences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		return prefrences.getBoolean(key, true);
	}
	
	public static void setLongPreferences(String key ,long value,Context context){
		SharedPreferences preferences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	public static long getLongPreference(String key,Context context){
		SharedPreferences prefrences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		return prefrences.getLong(key, 0);
	}
	
	public static long getLongPreferenceWithDiffDefValue(String key,Context context){
		SharedPreferences prefrences = context.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		return prefrences.getLong(key, -1);
	}
	
	public static void resetAllPreferences(Context mContext) {		
		SharedPreferences preferences = mContext.getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}

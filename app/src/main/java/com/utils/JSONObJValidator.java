package com.utils;

import org.json.JSONObject;

public class JSONObJValidator {

	public static String stringTagValidate(JSONObject jsonObject, String tag, String defaultValue){
		try{

			if(!jsonObject.isNull(tag)){
				return jsonObject.has(tag)?jsonObject.getString(tag):defaultValue;
			}
			else
				return defaultValue;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return defaultValue;
		}
	}
	
	public static String stringTagValidateOne(JSONObject jsonObject, String tag, String defaultValue){
		try{

			if(!jsonObject.isNull(tag)){
				String str = jsonObject.has(tag)?jsonObject.getString(tag):defaultValue;
				if(str.isEmpty())
					str = defaultValue;
				return str;
			}
			else
				return defaultValue;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return defaultValue;
		}
	}

	public static String stringFromObj(JSONObject jsonObject, String tag, String defaultValue){
		try{
			if(!jsonObject.isNull(tag)){
				return jsonObject.has(tag)?jsonObject.getJSONArray(tag).get(0).toString():defaultValue;
			}
			else
				return defaultValue;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return defaultValue;
		}
	}

	public static int intTagValidate(JSONObject jsonObject, String tag, int defaultValue){
		try{
			if(!jsonObject.isNull(tag)){
				return jsonObject.has(tag)?jsonObject.getInt(tag):defaultValue;
			}
			else
				return defaultValue;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return defaultValue;
		}
	}

	public static float floatTagValidate(JSONObject jsonObject, String tag, float defaultValue){
		try{
			if(!jsonObject.isNull(tag)){
				return jsonObject.has(tag)?(float)jsonObject.getDouble(tag):defaultValue;
			}
			else
				return defaultValue;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return defaultValue;
		}
	}

	public static String stringTagValidate(com.JsonPakage.JSONObject jsonObject,
			String tag, String defaultValue) {
		try{

			if(!jsonObject.isNull(tag)){
				return jsonObject.has(tag)?jsonObject.getString(tag):defaultValue;
			}
			else
				return defaultValue;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return defaultValue;
		}
	}

}

package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import android.content.Context;

public class ReadFileFromAsset {

	public static String readAsString(Context mContext,String fileName){		
		BufferedReader in;
		StringBuilder buf = new StringBuilder("");
		try {			
			InputStream json  = mContext.getAssets().open(fileName);
			in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
			String str = "";
			while ((str=in.readLine()) != null) {
				buf.append(str);
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
}

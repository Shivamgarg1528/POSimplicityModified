package com.utils;

import java.util.Date;
import android.text.format.DateFormat;

public class CurrentDate {
	
	public static String returnCurrentDateWithTime(){
		return (String) DateFormat.format("yyyy/MM/dd hh:mm:ss",new Date().getTime());
	}
	public static String returnCurrentDate(){
		return (String) DateFormat.format("yyyy/MM/dd",new Date().getTime());
	}
}

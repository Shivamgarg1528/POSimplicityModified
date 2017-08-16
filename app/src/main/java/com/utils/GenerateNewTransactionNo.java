package com.utils;

import java.util.Date;

import com.PosInterfaces.PrefrenceKeyConst;

import android.content.Context;
import android.text.format.DateFormat;

public class GenerateNewTransactionNo implements PrefrenceKeyConst{

	public static String onNewTransactionNumber(Context mContext){
		StringBuilder transactionNumer = new StringBuilder();
		try{

			Variables.transId = MyPreferences.getLongPreference(TRANSACTION_ID, mContext);
			Variables.transId ++;
			String deviceCode              = MyPreferences.getMyPreference(DEVICE_CODE, mContext);
			transactionNumer.append(deviceCode).append("-");
			String timeAndDate = (String) DateFormat.format("yyyyMMdd-hhmmss",new Date().getTime());
			transactionNumer.append(timeAndDate).append("-").append(Variables.transId);
			MyPreferences.setLongPreferences(TRANSACTION_ID,Variables.transId, mContext);
			MyPreferences.setMyPreference(MOST_RECENTLY_TRANSACTION_ID,transactionNumer.toString(), mContext);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return transactionNumer.toString();
	}

	public static String onNewTransactionNoForDejavoo(Context mContext){

		StringBuilder transactionNumer = new StringBuilder();
		try{
			Variables.transIdDejavoo = MyPreferences.getLongPreference(TRANSACTION_ID_DEJAVOO, mContext);
			Variables.transIdDejavoo ++;
			String timeAndDate = (String) DateFormat.format("mmss",new Date().getTime());
			transactionNumer.append(timeAndDate).append("-").append(Variables.transIdDejavoo);
			MyPreferences.setLongPreferences(TRANSACTION_ID_DEJAVOO,Variables.transIdDejavoo, mContext);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		MyPreferences.setMyPreference(INV_NUM, transactionNumer.toString(), mContext);
		return transactionNumer.toString();

	}
	
	public static String onNewSocketKey(Context mContext){
		StringBuilder transactionNumer = new StringBuilder();
		try{
			long id             = MyPreferences.getLongPreference(SOCKET_KEY, mContext);
			id++;
			String deviceCode   = MyPreferences.getMyPreference(DEVICE_ID, mContext);
			transactionNumer.append(deviceCode).append("-").append(id);
			MyPreferences.setMyPreference(SOCKET_EMIT,transactionNumer.toString(), mContext);
			MyPreferences.setLongPreferences(SOCKET_KEY, id, mContext);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return transactionNumer.toString();
	}

}

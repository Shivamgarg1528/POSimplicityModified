package com.utils;

import java.util.Arrays;
import java.util.List;

import android.content.Context;

public class EncryptedCardInfo {

	private Context mContext;
	private String trackData1 = "";
	private String trackData2 = "";
	private String emulatedData = "";
	private String readerEncryptionStatus  = "";
	private String encryptedMagnePrintData = "";
	private String deviceSerialNo = "";
	private String encryptedSessionId = "";
	private String cardInfo;

	private final String SEPERATOR = "\\|";
	private final int  MUST_HAVE_PIPES = 12;


	public EncryptedCardInfo(Context mContext,	String cardInfo) {
		super();
		this.mContext = mContext;
		this.cardInfo = cardInfo;
	}

	public String getEmulatedData() {
		return emulatedData;
	}
	
	public void setEmulatedData(String emulatedData) {
		this.emulatedData = emulatedData;
	}

	public boolean parseData(){
		
		if(cardInfo == null || cardInfo.isEmpty())
			return false;
		else{
			List<String> listOfAllParesedData = Arrays.asList(cardInfo.split(SEPERATOR));
			int sizeOfList                    = listOfAllParesedData.size();
			if(sizeOfList == MUST_HAVE_PIPES + 1){
				
				trackData1   = getDataFromPostionInList(listOfAllParesedData,2);
				trackData2   = getDataFromPostionInList(listOfAllParesedData,3);
				emulatedData = getDataFromPostionInList(listOfAllParesedData,6);
				
				deviceSerialNo = getDataFromPostionInList(listOfAllParesedData, 9);
				
				return trackData1.isEmpty()|| trackData2.isEmpty()?false:true;				
			}
			else
				return false;
		}
	}

	private String getDataFromPostionInList(List<String> listOfAllParesedData , int index){
		try{
			return listOfAllParesedData.get(index);
		}
		catch(Exception ex){
			ex.printStackTrace();
			return "";
		}	
	}

	public Context getmContext() {
		return mContext;
	}



	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}



	public String getTrackData1() {
		return trackData1;
	}



	public void setTrackData1(String trackData1) {
		this.trackData1 = trackData1;
	}



	public String getTrackData2() {
		return trackData2;
	}



	public void setTrackData2(String trackData2) {
		this.trackData2 = trackData2;
	}



	public String getReaderEncryptionStatus() {
		return readerEncryptionStatus;
	}



	public void setReaderEncryptionStatus(String readerEncryptionStatus) {
		this.readerEncryptionStatus = readerEncryptionStatus;
	}



	public String getEncryptedMagnePrintData() {
		return encryptedMagnePrintData;
	}



	public void setEncryptedMagnePrintData(String encryptedMagnePrintData) {
		this.encryptedMagnePrintData = encryptedMagnePrintData;
	}



	public String getDeviceSerialNo() {
		return deviceSerialNo;
	}



	public void setDeviceSerialNo(String deviceSerialNo) {
		this.deviceSerialNo = deviceSerialNo;
	}



	public String getEncryptedSessionId() {
		return encryptedSessionId;
	}



	public void setEncryptedSessionId(String encryptedSessionId) {
		this.encryptedSessionId = encryptedSessionId;
	}



	public String getCardInfo() {
		return cardInfo;
	}



	public void setCardInfo(String cardInfo) {
		this.cardInfo = cardInfo;
	}	



}

package com.PosInterfaces;

import org.json.JSONArray;

public interface SocketInterface {	
	public void onDataRecieved(JSONArray arry);		
	public void onSocketStateChanged(int state);	
}

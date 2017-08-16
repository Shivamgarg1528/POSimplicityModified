package com.Socket;

import org.json.JSONArray;
import android.content.Context;
import com.PosInterfaces.PrefrenceKeyConst;
import com.PosInterfaces.SocketInterface;
import com.utils.POSApp;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.ExceptionCallback;
import com.koushikdutta.async.http.socketio.ReconnectCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;


public class SocketIO implements PrefrenceKeyConst ,ConnectCallback ,ReconnectCallback, DisconnectCallback, ExceptionCallback{

	private SocketInterface socketInterface;
	public static final int CONNECTED_   = 0x001;
	public static final int DISONNECTED_ = 0x002;
	public static final int ERROR_       = 0x003;
	private POSApp gloabApp   = POSApp.getInstance();
	public Context mContext;

	public SocketIO(Context mContext) {
		super();
		this.mContext  = mContext;
	}

	public void setSocketRecievedData(SocketInterface socketInterface)
	{
		this.socketInterface = socketInterface;
	}

	public void connectSocket() {
		try{
			SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), SOCKET_URL,this);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void disconnetSocket()
	{
		if (nothingIsNull()) {
			try {
				gloabApp.getSocketIOClient().disconnect();								
			} 
			catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}

	public boolean nothingIsNull(){
		if(gloabApp.getSocketIOClient() != null && gloabApp.getSocketIOClient().isConnected())
			return true;
		else
			return false;
	}


	@Override
	public void onConnectCompleted(Exception ex, SocketIOClient client) {

		if (ex != null) {
			ex.printStackTrace();
			socketInterface.onSocketStateChanged(ERROR_);
		}
		else {
			socketInterface.onSocketStateChanged(CONNECTED_);
			gloabApp.setSocketIOClient(client);

			client.setReconnectCallback(this); 
			client.setDisconnectCallback(this);
			client.setExceptionCallback(this);
			
			client.on("event", new EventCallback() {
				@Override
				public void onEvent(JSONArray jsonArray, Acknowledge acknowledge) {
					socketInterface.onDataRecieved(jsonArray);					
				}
			});	
		}					
	}

	@Override
	public void onReconnect() {
		System.out.println("Reconnect SuccessFully...");
		socketInterface.onSocketStateChanged(CONNECTED_);
	}

	@Override
	public void onDisconnect(Exception e) {
		if(e != null ){
			System.out.println("Failed to disconnect:");
		}
		else
		{
			gloabApp.setSocketIo(null);
			gloabApp.setSocketIOClient(null);
			socketInterface.onSocketStateChanged(DISONNECTED_);
		}
	}

	private boolean isSocketIoNull(){
		return gloabApp.getSocketIOClient() == null ;
	}

	@Override
	public void onException(Exception e) {
		if(e != null ){
			System.out.println("Exception Occured");
			socketInterface.onSocketStateChanged(DISONNECTED_);
			if(!isSocketIoNull()){
				gloabApp.getSocketIOClient().reconnect();
			}
		}
	}
}

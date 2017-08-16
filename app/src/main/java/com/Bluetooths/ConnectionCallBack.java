package com.Bluetooths;

/**
 * Created by shivam.garg on 1/21/2017.
 */

public interface ConnectionCallBack {
    void onConnected();
    void onDisconnected();
    void onDataReceived(String data);
}

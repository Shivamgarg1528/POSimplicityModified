package com.Bluetooths;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.utils.BluetoothHelper;
import com.utils.Variables;

import java.util.UUID;

public class BluetoothConnector {

    protected BluetoothSocketWrapper bluetoothSocket;
    private String deviceMacAddress;
    private boolean isConnected;
    private ConnectionCallBack mConnectionCallBack;

    public BluetoothConnector() {}

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect() {
        try {
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceMacAddress);
            bluetoothSocket = new NativeBluetoothSocket(device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")));
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            bluetoothSocket.connect();
            isConnected = true;
        } catch (Exception e) {
            try {
                bluetoothSocket = new FallbackBluetoothSocket(bluetoothSocket.getUnderlyingSocket());
                bluetoothSocket.connect();
                isConnected = true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (isConnected) {
            new BluetoothReadThread(this, mConnectionCallBack).start();
            if (mConnectionCallBack != null)
                mConnectionCallBack.onConnected();
        } else {
            if (mConnectionCallBack != null)
                mConnectionCallBack.onDisconnected();
        }
    }

    public void forceDisconnect() {
        try {
            BluetoothHelper.closeBluetoothSocketManually();
            if(Variables.forceCloseBluetooth) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Variables.forceCloseBluetooth = false;
                        BluetoothHelper.openBluetoothSocketManually();
                    }
                }, 100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            isConnected = false;
            bluetoothSocket = null;
            if (mConnectionCallBack != null)
                mConnectionCallBack.onDisconnected();
        }
    }
    public void normalDisconnect(){
        try {
            isConnected    = false;
            bluetoothSocket = null;
            if (mConnectionCallBack != null)
                mConnectionCallBack.onDisconnected();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setConnectionCallBack(ConnectionCallBack connectionCallBack) {
        mConnectionCallBack = connectionCallBack;
    }

    public boolean sendDatOverTerminal(String pData) {
        try {
            bluetoothSocket.getOutputStream().write(pData.getBytes());
        } catch (Exception ex) {
            normalDisconnect();
            return false;
        }
        return true;
    }
}
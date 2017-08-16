package com.Bluetooths;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shivam.garg on 1/21/2017.
 */

public class NativeBluetoothSocket implements BluetoothSocketWrapper {

    private BluetoothSocket socket;

    public NativeBluetoothSocket(BluetoothSocket tmp) {
        this.socket = tmp;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    @Override
    public String getRemoteDeviceName() {
        return socket.getRemoteDevice().getName();
    }

    @Override
    public void connect() throws IOException {
        socket.connect();
    }

    @Override
    public String getRemoteDeviceAddress() {
        return socket.getRemoteDevice().getAddress();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public BluetoothSocket getUnderlyingSocket() {
        return socket;
    }
}
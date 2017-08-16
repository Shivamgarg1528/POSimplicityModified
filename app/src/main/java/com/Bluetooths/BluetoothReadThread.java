package com.Bluetooths;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by shivam.garg on 1/21/2017.
 */

public class BluetoothReadThread extends Thread {

    private BluetoothConnector bluetoothSocket;
    private ConnectionCallBack mDataCallBack;
    private InputStream inputStream;

    public BluetoothReadThread(BluetoothConnector bluetoothSocket, ConnectionCallBack pDataCallBack) {
        this.bluetoothSocket = bluetoothSocket;
        this.mDataCallBack = pDataCallBack;
    }

    @Override
    public void run() {
        super.run();
        boolean isTrue = true;
        try {
            inputStream = bluetoothSocket.bluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            isTrue = false;
            bluetoothSocket.normalDisconnect();
        }
        int bytes;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        while (isTrue) {
            try {
                bytes = 0;
                if (inputStream.available() > 0) {
                    bytes = inputStream.read(buffer);
                    byteBuffer.write(buffer, 0, bytes);
                }

                if (bytes == 0) {
                    if (byteBuffer.size() > 0) {
                        String packet = new String(byteBuffer.toByteArray(), Charset.forName("UTF-8"));
                        byteBuffer = new ByteArrayOutputStream();
                        mDataCallBack.onDataReceived(packet);
                    }
                }
                Thread.sleep(500);

            } catch (Exception e) {
                e.printStackTrace();
                isTrue = false;
                bluetoothSocket.normalDisconnect();
            }
        }
    }
}

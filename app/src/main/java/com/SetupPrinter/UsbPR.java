package com.SetupPrinter;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Message;

import com.zj.usbsdk.UsbController;

public class UsbPR extends BasePR {

    private UsbController usbCtrl = null;
    private UsbDevice dev = null;


    public UsbPR(Context pContext, PrinterCallBack pCallBack) {
        super(pContext, pCallBack);
    }

    @Override
    protected void start() {
        usbCtrl = new UsbController((Activity) getContext(), mUsbHandler);
        int[][] mUsbInfo = new int[5][2];
        mUsbInfo[0][0] = 0x1CBE;
        mUsbInfo[0][1] = 0x0003;
        mUsbInfo[1][0] = 0x1CB0;
        mUsbInfo[1][1] = 0x0003;
        mUsbInfo[2][0] = 0x0483;
        mUsbInfo[2][1] = 0x5740;
        mUsbInfo[3][0] = 0x0493;
        mUsbInfo[3][1] = 0x8760;
        mUsbInfo[4][0] = 0x0416;
        mUsbInfo[4][1] = 0x5011;

        for (int i = 0; i < 5; i++) {
            dev = usbCtrl.getDev(mUsbInfo[i][0], mUsbInfo[i][1]);
            if (dev != null)
                break;
        }

        if (dev == null) {
            Message message = mUsbHandler.obtainMessage();
            message.what = UsbController.USB_DISCONNECTED;
            mUsbHandler.sendMessage(message);
        } else {
            if (!(usbCtrl.isHasPermission(dev))) {
                usbCtrl.getPermission(dev);
            } else {
                Message message = mUsbHandler.obtainMessage();
                message.what = UsbController.USB_CONNECTED;
                mUsbHandler.dispatchMessage(message);
            }
        }
    }

    @Override
    protected void stop() {
        usbCtrl.close();
    }

    @Override
    public void playBuzzerCmd(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }

    @Override
    public void smallTextCmd(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }

    @Override
    public void largeTextCmd(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }

    @Override
    public void openDrawerCmd(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }

    @Override
    public void cutPaperCmd(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }

    @Override
    public void print1D(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }

    @Override
    public void print2D(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }

    @Override
    public void printCharacter(String data) {
        usbCtrl.sendMsg(data, CHARACTER_SET, dev);
    }

    @Override
    public void underLine(byte[] cmd) {
        usbCtrl.sendByte(cmd, dev);
    }
}

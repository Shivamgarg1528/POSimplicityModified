package com.SetupPrinter;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.zj.btsdk.BluetoothService;

import android.content.Context;

public class BluetPR extends BasePR {

    private BluetoothService mBluetoothService;
    private String mMacAddress;

    public BluetPR(Context pContext, PrinterCallBack pCallBack) {
        super(pContext, pCallBack);
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String mMacAddress) {
        this.mMacAddress = mMacAddress;
    }

    public BluetoothService getBluetoothService() {
        return mBluetoothService;
    }


    @Override
    protected void start() {
        mBluetoothService = new BluetoothService(getContext(), mBluetoothHandler);
        mBluetoothService.connect(mBluetoothService.getDevByMac(mMacAddress));
    }

    @Override
    protected void stop() {
        mBluetoothService.stop();
    }

    @Override
    public void playBuzzerCmd(byte[] cmd) {
        mBluetoothService.write(cmd);
    }

    @Override
    public void smallTextCmd(byte[] cmd) {
        mBluetoothService.write(cmd);
    }

    @Override
    public void largeTextCmd(byte[] cmd) {
        mBluetoothService.write(cmd);
    }

    @Override
    public void openDrawerCmd(byte[] cmd) {
        mBluetoothService.write(cmd);
    }

    @Override
    public void cutPaperCmd(byte[] cmd) {
        mBluetoothService.write(cmd);
    }

    @Override
    public void print1D(byte[] cmd) {
        mBluetoothService.write(cmd);
    }

    @Override
    public void print2D(byte[] cmd) {
        mBluetoothService.write(cmd);
    }

    @Override
    public void printCharacter(String data) {
        mBluetoothService.sendMessage(data, CHARACTER_SET);
    }

    @Override
    public void underLine(byte[] cmd) {
        mBluetoothService.write(cmd);
    }
}

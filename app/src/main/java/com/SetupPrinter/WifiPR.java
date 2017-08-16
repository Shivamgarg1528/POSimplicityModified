package com.SetupPrinter;

import android.content.Context;

import com.zj.wfsdk.WifiCommunication;

public class WifiPR extends BasePR {

    private String mIpAddress;
    private WifiCommunication mWifiCommunication;

    public WifiPR(Context pContext, PrinterCallBack pCallBack) {
        super(pContext, pCallBack);
    }

    public void setIpAddress(String pIpAddress) {
        this.mIpAddress = pIpAddress;
    }

    public WifiCommunication getWifiCommunication() {
        return mWifiCommunication;
    }

    @Override
    public void playBuzzerCmd(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }

    @Override
    public void smallTextCmd(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }

    @Override
    public void largeTextCmd(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }

    @Override
    public void openDrawerCmd(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }

    @Override
    public void cutPaperCmd(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }

    @Override
    public void printCharacter(String pData) {
        mWifiCommunication.sendMsg(pData + "\n", CHARACTER_SET);
    }

    @Override
    public void start() {
        mWifiCommunication = new WifiCommunication(mWifiHandler);
        mWifiCommunication.initSocket(mIpAddress, 9100);
    }

    @Override
    public void stop() {
        mWifiCommunication.close();
    }

    @Override
    public void print1D(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }

    @Override
    public void print2D(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }

    @Override
    public void underLine(byte[] cmd) {
        mWifiCommunication.sndByte(cmd);
    }
}

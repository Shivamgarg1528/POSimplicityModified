package com.SetupPrinter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.posimplicity.BuildConfig;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;
import com.zj.btsdk.BluetoothService;
import com.zj.usbsdk.UsbController;
import com.zj.wfsdk.WifiCommunication;

public abstract class BasePR {

    private final String TAG = this.getClass().getName();
    static final String CHARACTER_SET = "GBK";
    protected boolean isConnected = false;

    private Context mContext;
    private PrinterCallBack mCallBack;
    private Setting.AppSetting.Printer mPrinterSetting;

    protected abstract void start();

    protected abstract void stop();

    protected abstract void playBuzzerCmd(byte[] cmd);

    protected abstract void smallTextCmd(byte[] cmd);

    protected abstract void largeTextCmd(byte[] cmd);

    protected abstract void openDrawerCmd(byte[] cmd);

    protected abstract void cutPaperCmd(byte[] cmd);

    protected abstract void print1D(byte[] cmd);

    protected abstract void print2D(byte[] cmd);

    protected abstract void underLine(byte[] cmd);

    protected abstract void printCharacter(String data);


    // This class method
    public BasePR(Context pContext, PrinterCallBack pCallBack) {
        this.mContext = pContext;
        this.mCallBack = pCallBack;
        this.mPrinterSetting = AppSharedPrefs.getInstance(getContext()).getSetting().getAppSetting().getPrinter();
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public Setting.AppSetting.Printer getPrinterSetting() {
        return mPrinterSetting;
    }

    private void setConnected(boolean connected) {
        isConnected = connected;
    }

    private void disconnected() {
        setConnected(false);
        mCallBack.onDisconnected();
    }

    private void connected() {
        setConnected(true);
        mCallBack.onConnected(this);
    }

    public void onConnectionStart() {
        try {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onConnectionStart() called " + this);
            }
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onConnectionStop() {
        try {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onConnectionStop() called " + this);
            }
            stop();
        } catch (Exception e) {
            e.printStackTrace();
            disconnected();
        }
    }

    public void playBuzzer() {
        byte[] playBuzzerCmd = new byte[4];
        playBuzzerCmd[0] = 0x1B;
        playBuzzerCmd[1] = 0x42;
        playBuzzerCmd[2] = 0x04;
        playBuzzerCmd[3] = 0x01;
        try {
            if (mPrinterSetting.getDetail().get(mPrinterSetting.getPrinterSound()).isEnable()) {
                playBuzzerCmd(playBuzzerCmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void smallText() {
        byte[] smallTextCmd = new byte[3];
        smallTextCmd[0] = 0x1b;
        smallTextCmd[1] = 0x21;
        smallTextCmd[2] &= 0xEF;
        try {
            smallTextCmd(smallTextCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void largeText() {
        byte[] largeTextCmd = new byte[3];
        largeTextCmd[0] = 0x1b;
        largeTextCmd[1] = 0x21;
        largeTextCmd[2] |= 0x10;
        try {
            largeTextCmd(largeTextCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cutterCmd() {
        byte[] cutPaperCmd = new byte[4];
        cutPaperCmd[0] = 0x1D;
        cutPaperCmd[1] = 0x56;
        cutPaperCmd[2] = 0x42;
        cutPaperCmd[3] = (byte) 0x90;
        try {
            cutPaperCmd(cutPaperCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDrawer() {
        byte[] openDrawerCmd = new byte[5];
        openDrawerCmd[0] = 0x1B;
        openDrawerCmd[1] = 0x70;
        openDrawerCmd[2] = 0x00;
        openDrawerCmd[3] = 0x40;
        openDrawerCmd[4] = 0x50;
        try {
            openDrawerCmd(openDrawerCmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printData(String pData) {
        try {
            printCharacter(pData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print1DBarCode(String data) {
        try {
            int fixedValue = 8;
            int lengthOfString = data.length();
            byte[] printBarcmd = new byte[lengthOfString + fixedValue];

            printBarcmd[0] = 0x1d;
            printBarcmd[1] = 0x48;
            printBarcmd[2] = 0x02;
            printBarcmd[3] = 0x1d;
            printBarcmd[4] = 0x6b;
            printBarcmd[5] = 0x02;

            byte[] requestedArray = data.getBytes();

            for (int index = 0; index < lengthOfString; index++) {
                printBarcmd[fixedValue] = requestedArray[index];
                fixedValue++;
            }

            printBarcmd[18] = 0x00;
            printBarcmd[19] = 0x0A;
            if (mPrinterSetting.getDetail().get(mPrinterSetting.getBarCodePrint()).isEnable()) {
                print1D(printBarcmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print2DQrCode(String data) {
        try {
            int fixedValue = 6;
            int lengthOfString = data.length();
            byte[] printQR = new byte[lengthOfString + fixedValue];

            printQR[0] = 0x1f;
            printQR[1] = 0x1c;
            printQR[2] = 0x08;
            printQR[3] = 0x00;
            printQR[4] = 0x06;
            printQR[5] = 0x00;

            byte[] requestedArray = data.getBytes();

            for (int index = 0; index < lengthOfString; index++) {
                printQR[fixedValue] = requestedArray[index];
                fixedValue++;
            }
            if (mPrinterSetting.getDetail().get(mPrinterSetting.getQrCodePrint()).isEnable()) {
                print2D(printQR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void underLine(boolean underLineNeeded) {
        try {
            byte[] playBuzzerCmd = new byte[3];
            playBuzzerCmd[0] = 0x1B;
            playBuzzerCmd[1] = 0x2D;
            if (underLineNeeded)
                playBuzzerCmd[2] = 0x02;
            else
                playBuzzerCmd[2] = 0x00;
            underLine(playBuzzerCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    final Handler mWifiHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case WifiCommunication.WFPRINTER_CONNECTED:
                    connected();
                    break;

                default:
                    disconnected();
                    break;
            }
        }
    };

    final Handler mBluetoothHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE: {
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            connected();
                            break;
                    }
                    break;
                }

                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    disconnected();

                default:
                    break;
            }
        }
    };

    final Handler mUsbHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case UsbController.USB_CONNECTED:
                    connected();
                    break;

                default:
                    disconnected();
                    break;
            }
        }
    };
}

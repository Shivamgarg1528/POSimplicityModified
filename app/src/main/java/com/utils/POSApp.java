package com.utils;

import android.app.Application;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.Bluetooths.BluetoothConnector;
import com.Fragments.BaseFragment;
import com.SetupPrinter.BasePR;
import com.Socket.SocketIO;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.posimplicity.model.local.AmountPaidModel;
import com.posimplicity.model.local.OrderModel;
import com.posimplicity.model.local.Setting;

public class POSApp extends Application {

    private static POSApp singleton;
    private int deviceWidth, deviceHeight;
    private float deviceDensity;
    private SocketIOClient socketIOClient;
    private SocketIO socketIo;
    private BaseFragment visibleFragment;
    public BluetoothConnector bluetoothConnector = new BluetoothConnector();


    // New & Rectified Data
    public OrderModel mOrderModel = new OrderModel();
    private Setting mSettings;
    private BasePR mPrinterBT, mPrinterWF;

    public void onScreenSize() {
        DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
        deviceWidth = displaymetrics.widthPixels;
        deviceHeight = displaymetrics.heightPixels;
        deviceDensity = displaymetrics.density;
    }

    public BaseFragment getVisibleFragment() {
        return visibleFragment;
    }


    public void setVisibleFragment(BaseFragment fragment) {
        this.visibleFragment = fragment;
    }


    public SocketIO getSocketIo() {
        return socketIo;
    }

    public void setSocketIo(SocketIO socketIo) {
        this.socketIo = socketIo;
    }

    public float getDeviceDensity() {
        return deviceDensity;
    }


    public void setDeviceDensity(float deviceDensity) {
        this.deviceDensity = deviceDensity;
    }


    public int getDeviceWidth() {
        return deviceWidth;
    }


    public void setDeviceWidth(int deviceWidth) {
        this.deviceWidth = deviceWidth;
    }


    public int getDeviceHeight() {
        return deviceHeight;
    }


    public void setDeviceHeight(int deviceHeight) {
        this.deviceHeight = deviceHeight;
    }


    public static POSApp getInstance() {
        return singleton;
    }

    public SocketIOClient getSocketIOClient() {
        return socketIOClient;
    }


    public void setSocketIOClient(SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public void clearOrderModel() {
        mOrderModel.orderCheckoutItemsList.clear();
        mOrderModel.orderAmountPaidModel = new AmountPaidModel();

        mOrderModel.setOrderId(null);
        mOrderModel.setOrderStatus(null);
        mOrderModel.setOrderPaymentMode(null);
        mOrderModel.setOrderComment(null);
        mOrderModel.setOrderSavedInDb(true);

        mOrderModel.setOrderDiscountApplied(false);
        mOrderModel.setOrderDisPercentage(0.0f);
        mOrderModel.setOrderDiscountDollar(0.0f);

        if (mOrderModel.getOrderAssignClerk() != null) {
            mOrderModel.setOrderAssignClerk(null);
        }
        if (mOrderModel.getOrderAssignCustomer() != null) {
            mOrderModel.setOrderAssignCustomer(null);
        }
    }

    public BasePR getPrinterWF() {
        return mPrinterWF;
    }

    public void setPrinterWF(BasePR mBasePrinterWF) {
        this.mPrinterWF = mBasePrinterWF;
    }

    public BasePR getPrinterBT() {
        return mPrinterBT;
    }

    public void setPrinterBT(BasePR mBasePrinterBT) {
        this.mPrinterBT = mBasePrinterBT;
    }

    public void setSettings(Setting pSettings) {
        this.mSettings = pSettings;
    }

    public Setting getSettings() {
        return mSettings;
    }
}
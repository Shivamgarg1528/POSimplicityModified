package com.utils;

import android.widget.Toast;

import com.posimplicity.activity.BaseActivity;
import com.posimplicity.model.local.Setting;

public class PrinterHelper {

    private static boolean isBTPrinterConnected() {
        POSApp posApp = POSApp.getInstance();
        return posApp.getPrinterBT() != null && posApp.getPrinterBT().isConnected();
    }

    public static boolean isWifiPrinterConnected() {
        POSApp posApp = POSApp.getInstance();
        return posApp.getPrinterWF() != null && posApp.getPrinterWF().isConnected();
    }

    public static boolean isCustomerReceiptUsbOk() {
        Setting.AppSetting.Printer printer = POSApp.getInstance().getSettings().getAppSetting().getPrinter();
        return SettingHelper.isSettingEnable(printer.getDetail(), printer.getUsbPrinting()) && SettingHelper.isSettingEnable(printer.getCustomerReceiptArray(), printer.getCustomerReceiptUsb());
    }

    public static boolean isCustomerReceiptBTOk() {
        Setting.AppSetting.Printer printer = POSApp.getInstance().getSettings().getAppSetting().getPrinter();
        return SettingHelper.isSettingEnable(printer.getDetail(), printer.getBluetoothPrinting()) && SettingHelper.isSettingEnable(printer.getCustomerReceiptArray(), printer.getCustomerReceiptBT()) && isBTPrinterConnected();
    }

    public static boolean isKitchenReceiptUsbOk() {
        Setting.AppSetting.Printer printer = POSApp.getInstance().getSettings().getAppSetting().getPrinter();
        return SettingHelper.isSettingEnable(printer.getDetail(), printer.getUsbPrinting()) && SettingHelper.isSettingEnable(printer.getKitchenReceiptArray(), printer.getCustomerReceiptUsb());
    }

    public static boolean isKitchenReceiptBTOk() {
        Setting.AppSetting.Printer printer = POSApp.getInstance().getSettings().getAppSetting().getPrinter();
        return SettingHelper.isSettingEnable(printer.getDetail(), printer.getBluetoothPrinting()) && SettingHelper.isSettingEnable(printer.getKitchenReceiptArray(), printer.getCustomerReceiptBT()) && isBTPrinterConnected();
    }


    public static void showConnectionNotAvailable(BaseActivity mBaseActivity) {
        Toast.makeText(mBaseActivity, "Printer Connection Not Available", Toast.LENGTH_SHORT).show();
    }
}

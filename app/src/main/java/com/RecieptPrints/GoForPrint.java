package com.RecieptPrints;

import android.content.Context;

import com.dialog.ChangeAmountDialog;
import com.dialog.ReceiptPrintPrompt;
import com.CustomControls.ToastHelper;
import com.Fragments.MaintFragmentOtherSetting;
import com.PosInterfaces.PrefrenceKeyConst;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.Socket.ConvertStringOfJson;
import com.utils.POSApp;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.StartAndroidActivity;
import com.utils.Variables;
import com.posimplicity.HomeActivity;

public class GoForPrint implements PrefrenceKeyConst {

    private Context mContext;
    private HomeActivity localInsatance;
    private POSApp gApp;
    private boolean isActivityNeededToFinish;
    private boolean isPrintPromptNeeded;
    private boolean isReceiptNeededToPrint;
    private boolean isDuplicateReceiptNeededToPrint;
    private boolean isCustomOptionNeedToPrint;

    public static final int CASH_FRAGMENT = 0;
    public static final int CREDIT_FRAGMENT = 1;
    public static final int CHECQUE_FRAGMENT = 2;
    public static final int REWARDS_FRAGMENT = 3;

    public int numdersOfReciepts = 0x01;
    private int paymentMode = 0x00;


    public GoForPrint(Context mContext, int paymentMode, boolean activityNeedToFinish) {
        this.mContext = mContext;
        this.paymentMode = paymentMode;
        this.localInsatance = HomeActivity.localInstance;
        this.gApp = POSApp.getInstance();
        this.isActivityNeededToFinish = activityNeedToFinish;
        this.isPrintPromptNeeded = MyPreferences.getBooleanPrefrences(IS_RECEIPT_PROMPT_ON_PS, mContext);
        this.isDuplicateReceiptNeededToPrint = MyPreferences.getBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS, mContext);
        this.isCustomOptionNeedToPrint = MyPreferences.getBooleanPrefrences(IS_CUSTOM_OPTION_PRINT_ON_OFF, mContext);
    }

    public void onExectue() {

        new ConvertStringOfJson(mContext).onFullPayment();

        if (isDuplicateReceiptNeededToPrint) {
            numdersOfReciepts++;
        }

        if (isPrintPromptNeeded)
            new ReceiptPrintPrompt(this, mContext, paymentMode).showReceiptPrintPrompt();

        else {
            if (paymentMode == CREDIT_FRAGMENT)
                numdersOfReciepts++;
            onPreExecute(true);
        }
    }


    public void onPreExecute(boolean bool) {

        isReceiptNeededToPrint = bool;
        boolean isAnyConnectionAvailable = false;

        // Printing Kitchen Receipt ; ------------------ Start

        if (PrintSettings.isAbleToPrintKitchenReceiptThroughBluetooth(mContext)) {
            for (int index = 0; index < 1; index++) {
                if (MyPreferences.getLongPreference(POS_STORE_TYPE, mContext) == MaintFragmentOtherSetting.QUICK_ && isReceiptNeededToPrint)
                    KitchenReceipt.onPrintKitchenReciept(mContext, HomeActivity.localInstance, gApp.getPrinterBT());
            }
        }

        if (PrintSettings.isAbleToPrintKitchenReceiptThroughUsb(mContext)) {
            new UsbPR(mContext, new PrinterCallBack() {

                @Override
                public void onDisconnected() {
                }

                @Override
                public void onConnected(BasePR pPrinter) {
                    for (int index = 0; index < 1; index++) {
                        if (MyPreferences.getLongPreference(POS_STORE_TYPE, mContext) == MaintFragmentOtherSetting.QUICK_ && isReceiptNeededToPrint)
                            KitchenReceipt.onPrintKitchenReciept(mContext, HomeActivity.localInstance, pPrinter);
                    }
                }
            });
        }

        if (PrintSettings.canPrintWifiSlip(mContext)) {
            for (int index = 0; index < 1; index++) {
                if (MyPreferences.getLongPreference(POS_STORE_TYPE, mContext) == MaintFragmentOtherSetting.QUICK_ && isReceiptNeededToPrint)
                    KitchenReceipt.onPrintKitchenReciept(mContext, HomeActivity.localInstance, gApp.getPrinterWF());
            }
        }

        // Printing Kitchen Receipt ; ------------------ End


        if (PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)) {
            BasePR printerCmmdO = gApp.getPrinterBT();
            PrintReceiptCustomer pReciept = new PrintReceiptCustomer(mContext);
            for (int index = 0; index < numdersOfReciepts; index++) {
                if (isReceiptNeededToPrint) {
                    if (numdersOfReciepts == 2 && Variables.paymentByCC)
                        pReciept.onPrintRecieptCustomer(printerCmmdO, !isCustomOptionNeedToPrint);

                    else if (numdersOfReciepts > 1 && index == numdersOfReciepts - 1)
                        pReciept.onPrintRecieptCustomer(printerCmmdO, true);

                    else
                        pReciept.onPrintRecieptCustomer(printerCmmdO, !isCustomOptionNeedToPrint);
                }
            }
        }

        if (PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)) {
            isAnyConnectionAvailable = true;
            new UsbPR(mContext, new PrinterCallBack() {

                @Override
                public void onDisconnected() {
                    onPostExecute();
                }

                @Override
                public void onConnected(BasePR pPrinter) {
                    PrintReceiptCustomer pReciept = new PrintReceiptCustomer(mContext);
                    for (int index = 0; index < numdersOfReciepts; index++) {
                        if (isReceiptNeededToPrint) {
                            if (numdersOfReciepts == 2 && Variables.paymentByCC)
                                pReciept.onPrintRecieptCustomer(pPrinter, !isCustomOptionNeedToPrint);

                            else if (numdersOfReciepts > 1 && index == numdersOfReciepts - 1)
                                pReciept.onPrintRecieptCustomer(pPrinter, true);

                            else
                                pReciept.onPrintRecieptCustomer(pPrinter, !isCustomOptionNeedToPrint);
                        }
                    }
                    onPostExecute();
                }
            }).onConnectionStart();
        }

        if (!isAnyConnectionAvailable)
            onPostExecute();
    }

    public void onPostExecute() {

        boolean anyDrawerOptionIsTrue = false;

        switch (paymentMode) {

            case CASH_FRAGMENT:
                anyDrawerOptionIsTrue = MyPreferences.getBooleanPreferencesWithDefalutTrue(DRAWER_CASH, mContext);
                localInsatance.changeAmtTv.setText(MyStringFormat.formatWith2DecimalPlaces(Variables.changeAmt));
                localInsatance.cashTv.setText(MyStringFormat.formatWith2DecimalPlaces(Variables.cashAmount));
                break;

            case CHECQUE_FRAGMENT:
                anyDrawerOptionIsTrue = MyPreferences.getBooleanPrefrences(DRAWER_CHECK, mContext);
                break;

            case CREDIT_FRAGMENT:
                anyDrawerOptionIsTrue = MyPreferences.getBooleanPrefrences(DRAWER_CC, mContext);
                ToastHelper.showApprovedToast(mContext);

            case REWARDS_FRAGMENT:
                break;

            default:
                break;
        }

        if (anyDrawerOptionIsTrue) {
            if (PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)) {
                new UsbPR(mContext, new PrinterCallBack() {

                    @Override
                    public void onDisconnected() {
                    }

                    @Override
                    public void onConnected(BasePR pPrinter) {
                        PrintExtraReceipt.onOpenCashDrawer(pPrinter);
                    }

                }).onConnectionStart();
            }

            if (PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)) {
                PrintExtraReceipt.onOpenCashDrawer(gApp.getPrinterBT());
            }
        }

        if (Variables.changeAmt > 0)
            ChangeAmountDialog.showChangeAmount(mContext, localInsatance, Variables.changeAmt);
        else {
            localInsatance.resetAllData(mContext, 0);
            if (isActivityNeededToFinish)
                StartAndroidActivity.onActivityStart(false, mContext, HomeActivity.class);
        }
    }
}

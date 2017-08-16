package com.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.PosInterfaces.PrefrenceKeyConst;
import com.controller.ApisController;
import com.posimplicity.database.local.PosReportTable;
import com.posimplicity.model.local.OrderModel;
import com.posimplicity.model.local.ReportParent;
import com.posimplicity.model.response.other.Validate;
import com.posimplicity.BuildConfig;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyPreferences;

import java.util.HashMap;

public class BackgroundService extends IntentService {

    public static final String ACTION_CREATE_ORDER = "com.pos.action.create.order";
    public static final String ACTION_SAVE_ORDER = "com.pos.action.save.order";
    public static final String ACTION_SAVE_ORDER_MANUAL = "com.pos.action.save.order.manual";
    public static final String ACTION_PRINT_ORDER = "com.pos.action.print.order";
    public static final String ACTION_PRINT_SAMPLE = "com.pos.action.print.sample";

    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String actionTag = intent.getAction();
        if (Helper.isBlank(actionTag)) {
            if (BuildConfig.DEBUG) {
                Log.d("BackgroundService", "Action Tag Is Empty");
            }
            return;
        }
        OrderModel orderModel = AppSharedPrefs.getInstance(this).getOrderModel();
        switch (actionTag) {
            case ACTION_CREATE_ORDER: {
                // Create Order
                Validate validate = ApisController.createOrder(BackgroundService.this, orderModel, null);
                boolean isOrderCreated = Helper.verifyCreateOrder(this, validate, true);

                // updating saved orderModel...
                orderModel.setOrderSavedOnServer(isOrderCreated);
                AppSharedPrefs.getInstance(this).setOrderModel(orderModel);

                if (orderModel.isOrderSavedOnServer()) {
                    // Send notification to all device with same store
                    HashMap<String, String> pushQueryMap = new HashMap<>();
                    pushQueryMap.put("transId", orderModel.getOrderId());
                    pushQueryMap.put("orderStatus", orderModel.getOrderStatus());
                    pushQueryMap.put("storeName", MyPreferences.getMyPreference(PrefrenceKeyConst.STORE_NAME, this));
                    ApisController.pushNotification(this, pushQueryMap);

                    // Share order with customer iff customer assign only....
                    if (orderModel.getOrderAssignCustomer() != null) {
                        HashMap<String, String> customerQueryMap = new HashMap<>();
                        customerQueryMap.put("email_id", orderModel.getOrderAssignCustomer().getCustomerEmail());
                        customerQueryMap.put("customer_id", orderModel.getOrderAssignCustomer().getCustomerId());
                        ApisController.shareOrder(this, customerQueryMap);
                    }
                }
                start(this, ACTION_SAVE_ORDER);
                break;
            }
            case ACTION_SAVE_ORDER: {
                // We need to store this order details in local db for reports...
                if (orderModel.isOrderSavedInDb()) {
                    ReportParent reportParent = new ReportParent();

                    reportParent.setTransactionId(orderModel.getOrderId());
                    reportParent.setSubtotalAmount(orderModel.orderAmountPaidModel.getAmountSubTotal());
                    reportParent.setDiscountAmount(orderModel.orderAmountPaidModel.getAmountDiscount());
                    reportParent.setTaxAmount(orderModel.orderAmountPaidModel.getAmountTax());
                    reportParent.setTotalAmount(orderModel.orderAmountPaidModel.getAmount());
                    reportParent.setCashAmount(orderModel.orderAmountPaidModel.getAmountPaidByCash());
                    reportParent.setCheckAmount(orderModel.orderAmountPaidModel.getAmountPaidByCheck());
                    reportParent.setCreditAmount(orderModel.orderAmountPaidModel.getAmountPaidByCredit());
                    reportParent.setCustom1Amount(orderModel.orderAmountPaidModel.getAmountPaidCustom1());
                    reportParent.setCustom2Amount(orderModel.orderAmountPaidModel.getAmountPaidCustom2());
                    reportParent.setGiftAmount(orderModel.orderAmountPaidModel.getAmountPaidByGift());
                    reportParent.setRewardAmount(orderModel.orderAmountPaidModel.getAmountPaidByRewards());

                    reportParent.setDescription(Constants.DEFAULT_DESCRIPTION);
                    reportParent.setPayoutType(Constants.DEFAULT_PAYOUT_NAME);
                    reportParent.setRefundStatus(Constants.REFUND_NO);
                    reportParent.setSaveState(orderModel.isOrderSavedOnServer() ? Constants.ORDER_SUCCESS : Constants.ORDER_FAIL);

                    reportParent.setTransTime(Helper.getCurrentDate(false));
                    PosReportTable posReportTable = new PosReportTable(this);

                    // For daily report
                    reportParent.setReportName(Constants.DAILY_REPORT);
                    posReportTable.insertData(reportParent);

                    // For shift report
                    reportParent.setReportName(Constants.SHIFT_REPORT);
                    posReportTable.insertData(reportParent);
                }

                // start receipt printing...
                start(this, ACTION_PRINT_ORDER);
                break;
            }

            case ACTION_SAVE_ORDER_MANUAL: {
                // We need to store this order details in local db for reports...
                if (orderModel.isOrderSavedInDb()) {
                    String transId = orderModel.getOrderId();

                    PosReportTable posReportTable = new PosReportTable(this);
                    boolean orderDeleted = posReportTable.deleteRecord(transId);
                    if (!orderDeleted) {
                        return;
                    }

                    ReportParent reportParent = new ReportParent();
                    reportParent.setTransactionId(orderModel.getOrderId());
                    reportParent.setSubtotalAmount(orderModel.orderAmountPaidModel.getAmountSubTotal());
                    reportParent.setDiscountAmount(orderModel.orderAmountPaidModel.getAmountDiscount());
                    reportParent.setTaxAmount(orderModel.orderAmountPaidModel.getAmountTax());
                    reportParent.setTotalAmount(orderModel.orderAmountPaidModel.getAmount());
                    reportParent.setCashAmount(orderModel.orderAmountPaidModel.getAmountPaidByCash());
                    reportParent.setCheckAmount(orderModel.orderAmountPaidModel.getAmountPaidByCheck());
                    reportParent.setCreditAmount(orderModel.orderAmountPaidModel.getAmountPaidByCredit());
                    reportParent.setCustom1Amount(orderModel.orderAmountPaidModel.getAmountPaidCustom1());
                    reportParent.setCustom2Amount(orderModel.orderAmountPaidModel.getAmountPaidCustom2());
                    reportParent.setGiftAmount(orderModel.orderAmountPaidModel.getAmountPaidByGift());
                    reportParent.setRewardAmount(orderModel.orderAmountPaidModel.getAmountPaidByRewards());

                    reportParent.setDescription(Constants.DEFAULT_DESCRIPTION);
                    reportParent.setPayoutType(Constants.DEFAULT_PAYOUT_NAME);
                    reportParent.setRefundStatus(Constants.REFUND_NO);
                    reportParent.setSaveState(Constants.MANUAL_ENTRY);

                    reportParent.setTransTime(Helper.getCurrentDate(false));

                    // For daily report
                    reportParent.setReportName(Constants.DAILY_REPORT);
                    posReportTable.insertData(reportParent);

                    // For shift report
                    reportParent.setReportName(Constants.SHIFT_REPORT);
                    posReportTable.insertData(reportParent);
                }
                break;
            }

            case ACTION_PRINT_ORDER: {
                break;
            }

            case ACTION_PRINT_SAMPLE: {
                break;
            }
        }
    }

    public static void start(Context pContext, String pActionName) {
        Intent intent = new Intent(pContext, BackgroundService.class);
        intent.setAction(pActionName);
        pContext.startService(intent);
    }

    public static void stop(Context pContext) {
        Intent intent = new Intent(pContext, BackgroundService.class);
        pContext.stopService(intent);
    }
}

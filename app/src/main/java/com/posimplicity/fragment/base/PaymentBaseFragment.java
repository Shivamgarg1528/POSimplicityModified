package com.posimplicity.fragment.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.CustomControls.ToastHelper;
import com.controller.ApisController;
import com.easylibs.http.EasyHttpResponse;
import com.easylibs.listener.EventListener;
import com.posimplicity.R;
import com.posimplicity.dialog.AlertHelper;
import com.posimplicity.dialog.TransactionAssignDialog;
import com.posimplicity.dialog.TransactionIdDialog;
import com.posimplicity.fragment.payment.CashFragment;
import com.posimplicity.fragment.payment.CheckFragment;
import com.posimplicity.interfaces.OnTransactionAssignListener;
import com.posimplicity.interfaces.OnTransactionIdCallback;
import com.posimplicity.model.local.AmountPaidModel;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.model.response.other.Validate;
import com.service.BackgroundService;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyStringFormat;
import com.utils.POSApp;

public abstract class PaymentBaseFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener, OnTransactionIdCallback {

    protected POSApp mPosApp = POSApp.getInstance();

    public abstract void startPaymentProcess();

    private void showTransactionAssignToCustomerDialog() {
        new TransactionAssignDialog(mBaseActivity, new OnTransactionAssignListener() {
            @Override
            public void onTransactionAssigned(CustomerParent.Customer pAssignedUser) {
                Toast.makeText(mBaseActivity, "Customer Assigned", Toast.LENGTH_SHORT).show();
                mPosApp.mOrderModel.setOrderAssignCustomer(pAssignedUser);
            }
        }).show();
    }

    private void showTransactionIdDialog(boolean pShowAdapter, OnTransactionIdCallback pOnTransactionIdCallback) {
        new TransactionIdDialog(mBaseActivity, pShowAdapter, pOnTransactionIdCallback).show();
    }

    private void showTransactionAssignToClerkDialog() {
        new TransactionAssignDialog(mBaseActivity, new OnTransactionAssignListener() {
            @Override
            public void onTransactionAssigned(CustomerParent.Customer pAssignedUser) {
                Toast.makeText(mBaseActivity, "Clerk Assigned", Toast.LENGTH_SHORT).show();
                mPosApp.mOrderModel.setOrderAssignClerk(pAssignedUser);
            }
        }).show();
    }

    protected void showChangeAmountDialog() {
        String pChangeAmtMessage = "Change Amount : $" + MyStringFormat.formatWith2DecimalPlaces(mPosApp.mOrderModel.orderAmountPaidModel.getAmountChange());
        AlertHelper.showAlertDialog(mBaseActivity, pChangeAmtMessage, mBaseActivity.getString(R.string.string_ok), null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startPaymentProcess();
            }
        });
    }

    public void showOverFlowDialog(View pAnchorView) {
        PopupMenu popupMenu = new PopupMenu(mBaseActivity, pAnchorView);
        popupMenu.inflate(R.menu.payment_popup_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.payment_option_popup_menu_assign_customer: {
                showTransactionAssignToCustomerDialog();
                return true;
            }
            case R.id.payment_option_popup_menu_assign_clerk: {
                showTransactionAssignToClerkDialog();
                return true;
            }
            case R.id.payment_option_popup_menu_comment: {
                return true;
            }
            case R.id.payment_option_popup_menu_split: {
                return true;
            }
            case R.id.payment_option_popup_menu_manual_save: {
                showTransactionIdDialog(false, this);
                return true;
            }
        }
        return false;
    }


    @Override
    public void onTransactionIdCallback(String pTransId) {

        mPosApp.mOrderModel.setOrderId(pTransId);
        float billAmt = mPosApp.mOrderModel.orderAmountPaidModel.getAmount();

        // Recycle Object First
        mPosApp.mOrderModel.orderAmountPaidModel = new AmountPaidModel();

        mPosApp.mOrderModel.orderAmountPaidModel.setAmount(billAmt);
        mPosApp.mOrderModel.orderAmountPaidModel.setAmountPaid(billAmt);
        mPosApp.mOrderModel.setOrderStatus(Constants.ORDER_STATUS_COMPLETE);

        if (PaymentBaseFragment.this instanceof CashFragment) {
            mPosApp.mOrderModel.setOrderPaymentMode(Constants.PAYMENT_MODE_CASH);
            mPosApp.mOrderModel.orderAmountPaidModel.setAmountPaidByCash(billAmt);
        } else if (PaymentBaseFragment.this instanceof CheckFragment) {
            mPosApp.mOrderModel.setOrderPaymentMode(Constants.PAYMENT_MODE_CHECK);
            mPosApp.mOrderModel.orderAmountPaidModel.setAmountPaidByCheck(billAmt);
        }

        mBaseActivity.showProgressDialog();
        ApisController.createOrder(mBaseActivity, mPosApp.mOrderModel, new EventListener() {
            @Override
            public void onEvent(int pEventCode, Object pEventData) {
                mBaseActivity.dismissProgressDialog();
                Validate validate = ((EasyHttpResponse<Validate>) pEventData).getData();
                if (Helper.verifyCreateOrder(mBaseActivity, validate, true)) {
                    ToastHelper.showApprovedToast(mBaseActivity);
                    AppSharedPrefs.getInstance(mBaseActivity).setOrderModel(mPosApp.mOrderModel);
                    LocalBroadcastManager.getInstance(mBaseActivity).sendBroadcast(new Intent(Constants.ACTION_CLEAR));
                    BackgroundService.start(mBaseActivity, BackgroundService.ACTION_SAVE_ORDER_MANUAL);
                    mBaseActivity.finish();
                } else {
                    float billAmt = mPosApp.mOrderModel.orderAmountPaidModel.getAmount();
                    mPosApp.mOrderModel.orderAmountPaidModel = new AmountPaidModel();
                    mPosApp.mOrderModel.orderAmountPaidModel.setAmount(billAmt);
                }
            }
        });
    }
}


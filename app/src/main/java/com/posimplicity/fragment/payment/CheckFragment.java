package com.posimplicity.fragment.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.CustomControls.ToastHelper;
import com.posimplicity.R;
import com.posimplicity.fragment.base.PaymentBaseFragment;
import com.service.BackgroundService;
import com.utils.AppSharedPrefs;
import com.utils.Constants;

public class CheckFragment extends PaymentBaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_check, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.fragment_payment_check_iv_pay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_check_iv_pay: {
                startPaymentProcess();
                break;
            }
        }
    }

    @Override
    public void onPageChanged(int pPosition) {

    }

    @Override
    public void startPaymentProcess() {
        // all due amount will become pay amount as we can't pay in parts
        float payAmt = mPosApp.mOrderModel.orderAmountPaidModel.getAmountDue();

        mPosApp.mOrderModel.orderAmountPaidModel.setAmountPaid(mPosApp.mOrderModel.orderAmountPaidModel.getAmountPaid() + payAmt);
        mPosApp.mOrderModel.orderAmountPaidModel.setAmountPaidByCheck(mPosApp.mOrderModel.orderAmountPaidModel.getAmountPaidByCheck() + payAmt);
        mPosApp.mOrderModel.orderAmountPaidModel.setAmountDue(0.00f);

        //Update total due amount value on HomeActivity
        LocalBroadcastManager.getInstance(mBaseActivity)
                .sendBroadcast(new Intent(Constants.ACTION_AMOUNT_PAID));

        mPosApp.mOrderModel.setOrderComment("");
        mPosApp.mOrderModel.setOrderPaymentMode(Constants.PAYMENT_MODE_CHECK);
        mPosApp.mOrderModel.setOrderStatus(Constants.ORDER_STATUS_COMPLETE);

        // saving order model data into preference for future use...
        AppSharedPrefs.getInstance(mBaseActivity).setOrderModel(mPosApp.mOrderModel);

        //Clear all data from home screen...
        LocalBroadcastManager.getInstance(mBaseActivity)
                .sendBroadcast(new Intent(Constants.ACTION_CLEAR));

        //Creating Order
        ToastHelper.showApprovedToast(mBaseActivity);
        BackgroundService.start(mBaseActivity, BackgroundService.ACTION_CREATE_ORDER);
        mBaseActivity.finish();
    }
}

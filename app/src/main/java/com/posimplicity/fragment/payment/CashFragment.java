package com.posimplicity.fragment.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.CustomControls.ToastHelper;
import com.posimplicity.fragment.base.PaymentBaseFragment;
import com.service.BackgroundService;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.Keypad;
import com.utils.MyStringFormat;
import com.posimplicity.R;

public class CashFragment extends PaymentBaseFragment implements Keypad.KeypadClick, View.OnClickListener {

    private Keypad mKeypad;

    //Views
    private TextView mTextViewEnteredAmt;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_cash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mKeypad = new Keypad(view, this, Keypad.keypadArrayExtra);
        mKeypad.onBind(0.0f);

        mTextViewEnteredAmt = (TextView) view.findViewById(R.id.fragment_payment_cash_tv_entered_amt);
        mTextViewEnteredAmt.setText(MyStringFormat.formatWith2DecimalPlaces(0.0f));

        view.findViewById(R.id.fragment_payment_cash_iv_pay).setOnClickListener(this);
    }

    @Override
    public void onKeypadClick(String pNewValue) {
        mTextViewEnteredAmt.setText(pNewValue);
    }

    @Override
    public void onPageChanged(int pPosition) {
    }

    @Override
    public void startPaymentProcess() {
        mGApp.mOrderModel.setOrderComment("");
        mGApp.mOrderModel.setOrderPaymentMode(Constants.PAYMENT_MODE_CASH);
        mGApp.mOrderModel.setOrderStatus(Constants.ORDER_STATUS_COMPLETE);

        // saving order model data into preference for future use...
        AppSharedPrefs.getInstance(mBaseActivity).setOrderModel(mGApp.mOrderModel);

        //Clear all data from home screen...
        LocalBroadcastManager.getInstance(mBaseActivity)
                .sendBroadcast(new Intent(Constants.ACTION_CLEAR));

        //Creating Order
        ToastHelper.showApprovedToast(mBaseActivity);
        BackgroundService.start(mBaseActivity, BackgroundService.ACTION_CREATE_ORDER);
        mBaseActivity.finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_cash_iv_pay: {
                float enteredAmt = Float.parseFloat(mTextViewEnteredAmt.getText().toString());
                float payAmt = 0.0f;
                if (enteredAmt >= 0) {
                    float dueAmt = mGApp.mOrderModel.orderAmountPaidModel.getAmountDue();
                    if (enteredAmt > dueAmt) { // we need to find change amount that will return to customer
                        payAmt = dueAmt;
                        mGApp.mOrderModel.orderAmountPaidModel.setAmountChange(enteredAmt - payAmt);
                    } else {
                        payAmt = enteredAmt;
                    }
                    mGApp.mOrderModel.orderAmountPaidModel.setAmountPaid(mGApp.mOrderModel.orderAmountPaidModel.getAmountPaid() + payAmt);
                    mGApp.mOrderModel.orderAmountPaidModel.setAmountPaidByCash(mGApp.mOrderModel.orderAmountPaidModel.getAmountPaidByCash() + payAmt);
                    mGApp.mOrderModel.orderAmountPaidModel.setAmountDue(dueAmt - payAmt);

                    // reset value for new
                    mKeypad.onBind(0.00f, false);
                    mTextViewEnteredAmt.setText(MyStringFormat.formatWith2DecimalPlaces(0.00f));

                    //Update total due amount value on HomeActivity
                    LocalBroadcastManager.getInstance(mBaseActivity).sendBroadcast(new Intent(Constants.ACTION_AMOUNT_PAID));

                    if (mGApp.mOrderModel.orderAmountPaidModel.getAmountDue() == 0) {
                        if (mGApp.mOrderModel.orderAmountPaidModel.getAmountChange() > 0) {
                            showChangeAmountDialog();
                            return;
                        }
                        startPaymentProcess();
                    }
                }
                break;
            }
        }
    }
}

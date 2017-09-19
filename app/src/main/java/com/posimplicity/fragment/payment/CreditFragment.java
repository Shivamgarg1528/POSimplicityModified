package com.posimplicity.fragment.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.CustomControls.ToastHelper;
import com.devmarvel.creditcardentry.library.CardValidCallback;
import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.easylibs.listener.EventListener;
import com.posimplicity.R;
import com.posimplicity.dialog.SwipeCardDialog;
import com.posimplicity.fragment.base.PaymentBaseFragment;
import com.posimplicity.gateway.BaseGateway;
import com.posimplicity.gateway.BridgeGateway;
import com.posimplicity.gateway.PropayGateway;
import com.posimplicity.model.local.CardInfoModel;
import com.posimplicity.model.local.Setting;
import com.service.BackgroundService;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyStringFormat;

import java.util.LinkedHashMap;

public class CreditFragment extends PaymentBaseFragment implements View.OnClickListener, CardValidCallback, EventListener {

    private int mUsedGatewayPosition = BaseGateway.GATEWAY_NONE;
    private boolean mCreditEncryptionEnable = false;

    private CreditCardForm mCreditCardForm;

    private Setting.AppSetting.Gateway mGatewaySettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_credit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View viewPay = view.findViewById(R.id.fragment_payment_credit_tv_pay);
        viewPay.setOnClickListener(this);

        View viewSwipe = view.findViewById(R.id.fragment_payment_credit_tv_swipe);
        viewSwipe.setOnClickListener(this);

        mCreditCardForm = (CreditCardForm) view.findViewById(R.id.fragment_payment_credit_cc_form);
        mCreditCardForm.setOnCardValidCallback(this);

        Setting.AppSetting appSetting = AppSharedPrefs.getInstance(mBaseActivity).getSetting().getAppSetting();
        mCreditEncryptionEnable = appSetting.getOtherSetting().getDetail().get(appSetting.getOtherSetting().getEncryptionForCC()).isEnable();
        mGatewaySettings = appSetting.getGateway();

        for (int index = 0; index < mGatewaySettings.getDetail().size(); index++) {
            if (mGatewaySettings.getDetail().get(index).isEnable()) {
                mUsedGatewayPosition = index;
                break;
            }
        }

        if (mUsedGatewayPosition == BaseGateway.GATEWAY_NONE) {
            mCreditCardForm.setVisibility(View.INVISIBLE);
            viewSwipe.setVisibility(View.INVISIBLE);
            viewPay.setVisibility(View.VISIBLE);
        } else if (mUsedGatewayPosition == BaseGateway.GATEWAY_DEJAVOO
                && selectedGatewayInfoAvailable(true)) {
            mCreditCardForm.setVisibility(View.INVISIBLE);
            viewSwipe.setVisibility(View.INVISIBLE);
            viewPay.setVisibility(View.VISIBLE);
        } else if (selectedGatewayInfoAvailable(true)) {
            mCreditCardForm.setVisibility(View.VISIBLE);
            viewSwipe.setVisibility(View.VISIBLE);
            viewPay.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_credit_tv_pay: {
                if (mUsedGatewayPosition == BaseGateway.GATEWAY_NONE) {
                } else if (mUsedGatewayPosition == BaseGateway.GATEWAY_DEJAVOO) {
                }
                break;
            }
            case R.id.fragment_payment_credit_tv_swipe: {
                new SwipeCardDialog(mBaseActivity, mCreditEncryptionEnable, this).show();
                break;
            }
        }
    }

    private void startGatewayProcess(CardInfoModel pCardInfoModel, boolean pCardManual) {
        float payAmt = mPosApp.mOrderModel.orderAmountPaidModel.getAmountDue();

        pCardInfoModel.setTransactionAmt(MyStringFormat.formatWith2DecimalPlaces(payAmt));
        pCardInfoModel.setEventListener(this);

        switch (mUsedGatewayPosition) {
            case BaseGateway.GATEWAY_TSYS: {
                if (pCardManual) {
                    pCardInfoModel.setTransType(BaseGateway.TRANSACTION_TSYS_KEYED);
                } else {
                    pCardInfoModel.setTransType(BaseGateway.TRANSACTION_PLUG_PAY_SWIPE);
                }
                break;
            }
            case BaseGateway.GATEWAY_PLUG_PAY: {
                if (pCardManual) {
                    pCardInfoModel.setTransType(BaseGateway.TRANSACTION_PLUG_PAY_KEYED);
                } else {
                    pCardInfoModel.setTransType(BaseGateway.TRANSACTION_PLUG_PAY_SWIPE);
                }
                break;
            }
            case BaseGateway.GATEWAY_BRIDGE_PAY: {
                pCardInfoModel.setTransType(BaseGateway.TRANSACTION_BRIDGE_KEYED);
                new BridgeGateway(mBaseActivity).startGatewayProcess(pCardInfoModel);
                break;
            }
            case BaseGateway.GATEWAY_PROPAY: {
                pCardInfoModel.setTransType(BaseGateway.TRANSACTION_PROPAY_KEYED);
                new PropayGateway(mBaseActivity).startGatewayProcess(pCardInfoModel);
                break;
            }
            case BaseGateway.GATEWAY_DEJAVOO: {
                break;
            }
        }
    }

    @Override
    public void startPaymentProcess() {
        // all due amount will become pay amount as we can't pay in parts
        float payAmt = mPosApp.mOrderModel.orderAmountPaidModel.getAmountDue();

        mPosApp.mOrderModel.orderAmountPaidModel.setAmountPaid(mPosApp.mOrderModel.orderAmountPaidModel.getAmountPaid() + payAmt);
        mPosApp.mOrderModel.orderAmountPaidModel.setAmountPaidByCredit(mPosApp.mOrderModel.orderAmountPaidModel.getAmountPaidByCredit() + payAmt);
        mPosApp.mOrderModel.orderAmountPaidModel.setAmountDue(0.00f);

        //Update total due amount value on HomeActivity
        LocalBroadcastManager.getInstance(mBaseActivity)
                .sendBroadcast(new Intent(Constants.ACTION_AMOUNT_PAID));

        mPosApp.mOrderModel.setOrderComment("");
        mPosApp.mOrderModel.setOrderPaymentMode(Constants.PAYMENT_MODE_CREDIT);
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

    private boolean selectedGatewayInfoAvailable(boolean pShownToast) {
        boolean preferenceSet = false;
        switch (mUsedGatewayPosition) {
            case BaseGateway.GATEWAY_TSYS: {
                Setting.AppSetting.Gateway.TsysPay tsysPay = mGatewaySettings.getTsysPay();
                if (!Helper.isBlank(tsysPay.getDeviceId())
                        && !Helper.isBlank(tsysPay.getMerchantId())
                        && !Helper.isBlank(tsysPay.getKey())
                        && !Helper.isBlank(tsysPay.getUserName())
                        && !Helper.isBlank(tsysPay.getPassword())) {
                    preferenceSet = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_PLUG_PAY: {
                Setting.AppSetting.Gateway.PlugNPay plugNPay = mGatewaySettings.getPlugNPay();
                if (!Helper.isBlank(plugNPay.getPlugNPayId())) {
                    preferenceSet = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_BRIDGE_PAY: {
                Setting.AppSetting.Gateway.BridgePay bridgePay = mGatewaySettings.getBridgePay();
                if (!Helper.isBlank(bridgePay.getUserName())
                        && !Helper.isBlank(bridgePay.getUserPassword())) {
                    preferenceSet = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_PROPAY: {
                Setting.AppSetting.Gateway.ProPay propay = mGatewaySettings.getProPay();
                if (!Helper.isBlank(propay.getPayerId())) {
                    preferenceSet = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_DEJAVOO: {
                break;
            }
        }
        if (pShownToast && !preferenceSet) {
            mBaseActivity.showToast("Please Provide Gateway Info Under Setting");
        }
        return preferenceSet;
    }

    @Override
    public void cardValid(CreditCard creditCard) {
        CardInfoModel cardInfoModel = new CardInfoModel();
        cardInfoModel.setCardNumber(creditCard.getCardNumber().replace(" ", ""));
        cardInfoModel.setCardExpYear(String.valueOf(creditCard.getExpYear()));
        cardInfoModel.setCardExpMonth(String.valueOf(creditCard.getExpMonth()));

        cardInfoModel.setCvv2Number(creditCard.getSecurityCode());
        cardInfoModel.setCardHolderName("");
        startGatewayProcess(cardInfoModel, true);
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
        switch (pEventCode) {
            case -1: {
                CardInfoModel cardInfoModel = (CardInfoModel) pEventData;
                startGatewayProcess(cardInfoModel, false);
                break;
            }
            case BaseGateway.TRANSACTION_TSYS_KEYED:
            case BaseGateway.TRANSACTION_TSYS_SWIPE:
            case BaseGateway.TRANSACTION_TSYS_SWIPE_ENCRYPTED:
            case BaseGateway.TRANSACTION_PROPAY_KEYED:
            case BaseGateway.TRANSACTION_BRIDGE_KEYED: {
                boolean isSuccess = (boolean) pEventData;
                if (isSuccess) {
                    startPaymentProcess();
                } else {
                    ToastHelper.showDeclineToast(mBaseActivity);
                }
                break;
            }
            case BaseGateway.TRANSACTION_PLUG_PAY_KEYED:
            case BaseGateway.TRANSACTION_PLUG_PAY_SWIPE:
            case BaseGateway.TRANSACTION_PLUG_PAY_SWIPE_ENCRYPTED: {
                String responseStr = (String) pEventData;
                LinkedHashMap<String, String> responseMap = Helper.parseQueryParams(responseStr);
                if (responseMap.containsKey("success") && "yes".equalsIgnoreCase(responseMap.get("success"))) {
                    String surchargeAmt = "";
                    if (responseMap.containsKey("surcharge")) {
                        surchargeAmt = responseMap.get("surcharge");
                    }
                } else {
                    ToastHelper.showDeclineToast(mBaseActivity);
                }
                break;
            }
        }
    }
}

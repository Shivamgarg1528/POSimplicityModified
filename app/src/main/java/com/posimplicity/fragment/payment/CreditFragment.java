package com.posimplicity.fragment.payment;

import android.app.AlertDialog;
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
import com.posimplicity.gateway.PropayGateway;
import com.posimplicity.model.local.CardInfoModel;
import com.posimplicity.model.local.Setting;
import com.posimplicity.model.response.gateway.BridgePayResponse;
import com.service.BackgroundService;
import com.utils.AppSharedPrefs;
import com.utils.CardHelper;
import com.utils.CheckCardInfo;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyStringFormat;

import java.util.LinkedHashMap;

public class CreditFragment extends PaymentBaseFragment implements View.OnClickListener, CardValidCallback, EventListener {

    private int mUsedGatewayPosition;
    private boolean mCreditEncryptionEnable = false;

    private CreditCardForm mCreditCardForm;
    private CreditCard mCreditCard;

    private Setting.AppSetting.Gateway mGatewaySettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_credit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fragment_payment_credit_tv_pay).setOnClickListener(this);

        View viewSwipe = view.findViewById(R.id.fragment_payment_credit_tv_swipe);
        viewSwipe.setOnClickListener(this);

        mCreditCardForm = (CreditCardForm) view.findViewById(R.id.fragment_payment_credit_cc_form);
        mCreditCardForm.setOnCardValidCallback(this);

        Setting.AppSetting appSetting = AppSharedPrefs.getInstance(mBaseActivity).getSetting().getAppSetting();
        mCreditEncryptionEnable = appSetting.getOtherSetting().getDetail().get(appSetting.getOtherSetting().getEncryptionForCC()).isEnable();
        mGatewaySettings = appSetting.getGateway();

        getUsedGatewayPosition();

        if (mUsedGatewayPosition == BaseGateway.GATEWAY_NONE) {
            mCreditCardForm.setVisibility(View.INVISIBLE);
            viewSwipe.setVisibility(View.INVISIBLE);
        } else if (selectedGatewayInfoAvailable(true)) {
            mCreditCardForm.setVisibility(View.VISIBLE);
            viewSwipe.setVisibility(View.VISIBLE);
        }
    }

    private void getUsedGatewayPosition() {
        mUsedGatewayPosition = -1;
        for (int index = 0; index < mGatewaySettings.getDetail().size(); index++) {
            if (mGatewaySettings.getDetail().get(index).isEnable()) {
                mUsedGatewayPosition = index;
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_credit_tv_pay: {
                if (mUsedGatewayPosition == BaseGateway.GATEWAY_NONE) {
                    mBaseActivity.showToast("Offline Payment");
                } else if (mCreditCard == null) {
                    mBaseActivity.showToast("Please Enter Card Information");
                    return;
                } else {
                    startGatewayProcess(false);
                    mCreditCardForm.clearForm();
                    mCreditCard = null;
                }
                break;
            }
            case R.id.fragment_payment_credit_tv_swipe: {
                new SwipeCardDialog(mBaseActivity).show(this);
                break;
            }
        }
    }

    private void startGatewayProcess(boolean pCardManual) {

        float payAmt = mGApp.mOrderModel.orderAmountPaidModel.getAmountDue();
        CardInfoModel cardInfoModel = new CardInfoModel();
        cardInfoModel.setTransactionAmt(MyStringFormat.formatWith2DecimalPlaces(payAmt));
        cardInfoModel.setCardHolderName("");
        if (pCardManual) {
            cardInfoModel.setCardNumber(mCreditCard.getCardNumber().replace(" ", ""));
            cardInfoModel.setCardExpDate(String.valueOf(mCreditCard.getExpMonth()));
            cardInfoModel.setCardExpYear(String.valueOf(mCreditCard.getExpYear()));
        } else {
        }

        cardInfoModel.setEventListener(this);
        switch (mUsedGatewayPosition) {
            case BaseGateway.GATEWAY_TSYS: {
                if (pCardManual) {
                    cardInfoModel.setTransType(BaseGateway.TRANSACTION_TSYS_KEYED);
                    cardInfoModel.setCvv2Number(mCreditCard.getSecurityCode());
                } else {
                    cardInfoModel.setTransType(BaseGateway.TRANSACTION_PLUG_PAY_SWIPE);
                    cardInfoModel.setCvv2Number(mCreditCard.getSecurityCode());
                }
                break;
            }
            case BaseGateway.GATEWAY_PLUG_PAY: {
                if (pCardManual) {
                    cardInfoModel.setTransType(BaseGateway.TRANSACTION_PLUG_PAY_KEYED);
                } else {
                    cardInfoModel.setTransType(BaseGateway.TRANSACTION_PLUG_PAY_SWIPE);
                }
                break;
            }
            case BaseGateway.GATEWAY_BRIDGE_PAY: {
                cardInfoModel.setTransType(BaseGateway.TRANSACTION_BRIDGE_KEYED);
                break;
            }
            case BaseGateway.GATEWAY_PROPAY: {
                cardInfoModel.setTransType(BaseGateway.TRANSACTION_PROPAY_KEYED);
                new PropayGateway(mBaseActivity).startGatewayProcess(cardInfoModel);
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
        float payAmt = mGApp.mOrderModel.orderAmountPaidModel.getAmountDue();

        mGApp.mOrderModel.orderAmountPaidModel.setAmountPaid(mGApp.mOrderModel.orderAmountPaidModel.getAmountPaid() + payAmt);
        mGApp.mOrderModel.orderAmountPaidModel.setAmountPaidByCredit(mGApp.mOrderModel.orderAmountPaidModel.getAmountPaidByCredit() + payAmt);
        mGApp.mOrderModel.orderAmountPaidModel.setAmountDue(0.00f);

        //Update total due amount value on HomeActivity
        LocalBroadcastManager.getInstance(mBaseActivity)
                .sendBroadcast(new Intent(Constants.ACTION_AMOUNT_PAID));

        mGApp.mOrderModel.setOrderComment("");
        mGApp.mOrderModel.setOrderPaymentMode(Constants.PAYMENT_MODE_CREDIT);
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

    private boolean selectedGatewayInfoAvailable(boolean pShownToast) {
        boolean checkingCondition = false;
        switch (mUsedGatewayPosition) {
            case BaseGateway.GATEWAY_TSYS: {
                Setting.AppSetting.Gateway.TsysPay tsysPay = mGatewaySettings.getTsysPay();
                if (!Helper.isBlank(tsysPay.getDeviceId()) && !Helper.isBlank(tsysPay.getMerchantId()) && !Helper.isBlank(tsysPay.getKey()) && !Helper.isBlank(tsysPay.getUserName()) && !Helper.isBlank(tsysPay.getPassword())) {
                    checkingCondition = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_PLUG_PAY: {
                Setting.AppSetting.Gateway.PlugNPay plugNPay = mGatewaySettings.getPlugNPay();
                if (!Helper.isBlank(plugNPay.getPlugNPayId())) {
                    checkingCondition = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_BRIDGE_PAY: {
                Setting.AppSetting.Gateway.BridgePay bridgePay = mGatewaySettings.getBridgePay();
                if (!Helper.isBlank(bridgePay.getUserName()) && !Helper.isBlank(bridgePay.getUserPassword())) {
                    checkingCondition = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_PROPAY: {
                Setting.AppSetting.Gateway.ProPay propay = mGatewaySettings.getProPay();
                if (!Helper.isBlank(propay.getPayerId())) {
                    checkingCondition = true;
                }
                break;
            }
            case BaseGateway.GATEWAY_DEJAVOO: {
                break;
            }
        }
        if (pShownToast && !checkingCondition) {
            mBaseActivity.showToast("Please Provide Gateway Info Under Setting");
        }
        return checkingCondition;
    }

    @Override
    public void cardValid(CreditCard creditCard) {
        this.mCreditCard = creditCard;
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
        switch (pEventCode) {
            case -1: {
                String magStripData = (String) pEventData;
                if (!mCreditEncryptionEnable) {
                    CardHelper cardHelper = new CardHelper(magStripData);
                    if (cardHelper.parseCardInfo()) {
                    } else {
                        new SwipeCardDialog(mBaseActivity).show(this);
                    }
                } else {

                }
                break;
            }
            case BaseGateway.TRANSACTION_BRIDGE_KEYED: {
                BridgePayResponse bridgePayResponse = (BridgePayResponse) pEventData;
                if (bridgePayResponse != null && bridgePayResponse.getResponse() != null && "Approved".equalsIgnoreCase(bridgePayResponse.getResponse().getRespMSG())) {
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_delete_successfully));
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
            case BaseGateway.TRANSACTION_TSYS_KEYED:
            case BaseGateway.TRANSACTION_TSYS_SWIPE:
            case BaseGateway.TRANSACTION_TSYS_SWIPE_ENCRYPTED: {
                boolean isSuccess = (boolean) pEventData;
                if (isSuccess) {
                } else {
                    ToastHelper.showDeclineToast(mBaseActivity);
                }
                break;
            }
            case BaseGateway.TRANSACTION_PROPAY_KEYED: {
                boolean isSuccess = (boolean) pEventData;
                if (isSuccess) {
                    startPaymentProcess();
                } else {
                    ToastHelper.showDeclineToast(mBaseActivity);
                }
                break;
            }
        }
    }
}

package com.posimplicity.fragment.setting.ccadmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easylibs.listener.EventListener;
import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.posimplicity.gateway.BaseGateway;
import com.posimplicity.gateway.PropayGateway;
import com.posimplicity.model.local.CardInfoModel;
import com.posimplicity.model.local.Setting;
import com.posimplicity.model.response.gateway.BridgePayResponse;
import com.posimplicity.model.response.gateway.PropayIdDeleteResponse;
import com.posimplicity.model.response.gateway.PropayIdResponse;
import com.utils.AppSharedPrefs;
import com.utils.Helper;

import java.util.LinkedHashMap;

public class PropayFragment extends BaseFragment implements View.OnClickListener, EventListener {

    private PropayGateway mPropayGateway;
    private Setting mSetting;
    private Setting.AppSetting.Gateway.ProPay mPropay;

    private View mViewCreatePropayPayerId;
    private View mViewDeletePropayPayerId;
    private TextView mTextViewPropayPayerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_propay_, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextViewPropayPayerId = (TextView) view.findViewById(R.id.fragment_propay_tv_propay_payer_id);

        mViewCreatePropayPayerId = view.findViewById(R.id.fragment_propay_tv_create_propay_payer);
        mViewCreatePropayPayerId.setOnClickListener(this);

        mViewDeletePropayPayerId = view.findViewById(R.id.fragment_propay_tv_delete_propay_payer);
        mViewDeletePropayPayerId.setOnClickListener(this);

        mSetting = AppSharedPrefs.getInstance(mBaseActivity).getSetting();
        mPropay = mSetting.getAppSetting().getGateway().getProPay();

        updatePropayUi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_propay_tv_create_propay_payer: {
                if (Helper.isConnected(mBaseActivity)) {
                    mBaseActivity.showProgressDialog();
                    CardInfoModel cardInfoModel = new CardInfoModel();
                    cardInfoModel.setEventListener(this);
                    cardInfoModel.setTransType(BaseGateway.TRANSACTION_PROPAY_CREATE);
                    mPropayGateway = new PropayGateway(mBaseActivity);
                    mPropayGateway.startGatewayProcess(cardInfoModel);
                } else {
                    mBaseActivity.showToast();
                }
                break;
            }

            case R.id.fragment_propay_tv_delete_propay_payer: {
                if (Helper.isConnected(mBaseActivity)) {
                    mBaseActivity.showProgressDialog();
                    CardInfoModel cardInfoModel = new CardInfoModel();
                    cardInfoModel.setTransactionAmt("10.00");
                    cardInfoModel.setCardNumber("4444444444444448");
                    cardInfoModel.setCardExpMonth("12");
                    cardInfoModel.setCardExpYear("18");
                    cardInfoModel.setCardHolderName("Garg");

                    cardInfoModel.setEventListener(this);
                    cardInfoModel.setTransType(BaseGateway.TRANSACTION_PROPAY_KEYED);

                    mPropayGateway = new PropayGateway(mBaseActivity);
                    mPropayGateway.startGatewayProcess(cardInfoModel);
                } else {
                    mBaseActivity.showToast();
                }
                break;
            }
        }
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
        mBaseActivity.dismissProgressDialog();
        switch (pEventCode) {
            case BaseGateway.TRANSACTION_PROPAY_CREATE: {
                PropayIdResponse propayIdResponse = (PropayIdResponse) pEventData;
                if (propayIdResponse != null && "SUCCESS".equalsIgnoreCase(propayIdResponse.getRequestResult().getResultValue())) {
                    mPropay.setPayerId(propayIdResponse.getExternalAccountID());
                    Helper.updateSettingPreference(mBaseActivity, null, mSetting, false);
                    updatePropayUi();
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_create_successfully));
                } else {
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_failed_to_create));
                }
                break;
            }
            case BaseGateway.TRANSACTION_PROPAY_DELETE: {
                PropayIdDeleteResponse propayIdDeleteResponse = (PropayIdDeleteResponse) pEventData;
                if (propayIdDeleteResponse != null && "SUCCESS".equalsIgnoreCase(propayIdDeleteResponse.getResultValue())) {
                    mPropay.setPayerId("");
                    Helper.updateSettingPreference(mBaseActivity, null, mSetting, false);
                    updatePropayUi();
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_delete_successfully));
                } else {
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_failed_to_delete));
                }
                break;
            }
            case BaseGateway.TRANSACTION_BRIDGE_KEYED: {
                BridgePayResponse bridgePayResponse = (BridgePayResponse) pEventData;
                if (bridgePayResponse != null && bridgePayResponse.getResponse() != null && "Approved".equalsIgnoreCase(bridgePayResponse.getResponse().getRespMSG())) {
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_delete_successfully));
                } else {
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_failed_to_delete));
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
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_failed_to_delete));
                }
                break;
            }
            case BaseGateway.TRANSACTION_TSYS_KEY_GENERATION:
            case BaseGateway.TRANSACTION_TSYS_KEY_UPGRADE:
            case BaseGateway.TRANSACTION_TSYS_KEYED:
            case BaseGateway.TRANSACTION_TSYS_SWIPE:
            case BaseGateway.TRANSACTION_TSYS_SWIPE_ENCRYPTED:
            case BaseGateway.TRANSACTION_TSYS_REFUND:
            case BaseGateway.TRANSACTION_TSYS_TIP_ADJUSTMENT: {
                boolean isSuccess = (boolean) pEventData;
                if (isSuccess) {
                } else {
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_failed_to_delete));
                }
                break;
            }
            case BaseGateway.TRANSACTION_PROPAY_KEYED: {
                boolean isSuccess = (boolean) pEventData;
                if (isSuccess) {
                } else {
                    mBaseActivity.showToast(getString(R.string.string_propay_payer_id_failed_to_delete));
                }
                break;
            }
        }
    }

    private void updatePropayUi() {
        if (Helper.isBlank(mPropay.getPayerId())) {
            mViewCreatePropayPayerId.setVisibility(View.VISIBLE);
            mViewDeletePropayPayerId.setVisibility(View.GONE);
        } else {
            mViewCreatePropayPayerId.setVisibility(View.GONE);
            mViewDeletePropayPayerId.setVisibility(View.VISIBLE);
        }

        String propayPayerId = Helper.isBlank(mPropay.getPayerId()) ? getString(R.string.string_propay_payer_id_not_available) : mPropay.getPayerId();
        mTextViewPropayPayerId.setText(getString(R.string.string_propay_payer_id_label) + " " + propayPayerId);
    }
}

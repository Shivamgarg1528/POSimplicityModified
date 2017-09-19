package com.posimplicity.gateway;

import com.posimplicity.activity.BaseActivity;
import com.posimplicity.model.local.CardInfoModel;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.Helper;

import java.util.HashMap;

public class PlugPayGateway extends BaseGateway {

    private final String PLUG_N_PAY_URL = "https://pay1.plugnpay.com/payment/pnpremote.cgi";
    private Setting.AppSetting.Gateway.PlugNPay mPlugNPaySettings;

    public PlugPayGateway(BaseActivity pBaseActivity) {
        super(pBaseActivity);
        mPlugNPaySettings = AppSharedPrefs.getInstance(pBaseActivity).getSetting().getAppSetting().getGateway().getPlugNPay();
    }

    @Override
    protected void sendPayment(CardInfoModel pCardInfoModel) {

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("publisher-name", mPlugNPaySettings.getPlugNPayId());
        queryMap.put("card-amount", pCardInfoModel.getTransactionAmt());
        queryMap.put("authtype", "authpostauth");
        queryMap.put("currency", "USD");
        queryMap.put("mode", "auth");

        switch (pCardInfoModel.getTransType()) {
            case TRANSACTION_PLUG_PAY_KEYED: {
                queryMap.put("card-exp", pCardInfoModel.getCardExpMonth().concat(pCardInfoModel.getCardExpYear()));
                queryMap.put("card-number", pCardInfoModel.getCardNumber());
                queryMap.put("card-name", pCardInfoModel.getCardHolderName());
                break;
            }
            case TRANSACTION_PLUG_PAY_SWIPE: {
                queryMap.put("magstripe", pCardInfoModel.getCardMagData());
                break;
            }
            case TRANSACTION_PLUG_PAY_SWIPE_ENCRYPTED: {
                queryMap.put("magensacc", pCardInfoModel.getCardMagData());
                queryMap.put("swipedevice", "KYB");
                break;
            }
        }
        String urlParameters = Helper.appendQueryParams(queryMap);
        jsonRequest(Constants.HttpMethod.HTTP_POST, PLUG_N_PAY_URL, urlParameters, null);
    }

    @Override
    protected void sendResultBack(CardInfoModel pCardInfoModel) {
        pCardInfoModel.getEventListener().onEvent(pCardInfoModel.getTransType(), pCardInfoModel.getResponse());
    }
}

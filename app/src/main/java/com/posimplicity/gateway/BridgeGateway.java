package com.posimplicity.gateway;

import com.easylibs.utils.JsonUtils;
import com.posimplicity.activity.BaseActivity;
import com.posimplicity.model.local.CardInfoModel;
import com.posimplicity.model.local.Setting;
import com.posimplicity.model.response.gateway.BridgePayResponse;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.Helper;

import java.util.HashMap;

public class BridgeGateway extends BaseGateway {

    private static final String BRIDGE_PAY_URL = "https://gateway.itstgate.com/SmartPayments/transact.asmx/ProcessCreditCard";
    private final Setting.AppSetting.Gateway.BridgePay mBridgePaySettings;

    public BridgeGateway(BaseActivity pBaseActivity) {
        super(pBaseActivity);
        mBridgePaySettings = AppSharedPrefs.getInstance(mBaseActivity).getSetting().getAppSetting().getGateway().getBridgePay();
    }

    @Override
    protected void sendPayment(CardInfoModel pCardInfoModel) {

        HashMap<String, String> queryMap = new HashMap<>();

        queryMap.put("UserName", mBridgePaySettings.getUserName());
        queryMap.put("Password", mBridgePaySettings.getUserPassword());

        queryMap.put("TransType", "Sale");

        queryMap.put("CardNum", pCardInfoModel.getCardNumber());
        queryMap.put("ExpDate", pCardInfoModel.getCardExpDate().concat(pCardInfoModel.getCardExpYear()));
        queryMap.put("MagData", pCardInfoModel.getCardMagData());
        queryMap.put("NameOnCard", pCardInfoModel.getCardHolderName());
        queryMap.put("Amount", pCardInfoModel.getTransactionAmt());

        queryMap.put("InvNum", "");
        queryMap.put("ExtData", "");
        queryMap.put("PNRef", "");
        queryMap.put("Zip", "");
        queryMap.put("Street", "");
        queryMap.put("CVNum", "");

        String urlParameters = Helper.appendQueryParams(queryMap);
        jsonRequest(Constants.HttpMethod.HTTP_POST, BRIDGE_PAY_URL, urlParameters, null);
    }

    @Override
    protected void sendResultBack(CardInfoModel pCardInfoModel) {
        String xmlToJson = Helper.convertXmlToJson(pCardInfoModel.getResponse());
        BridgePayResponse bridgePayResponse = JsonUtils.objectify(xmlToJson, BridgePayResponse.class);
        pCardInfoModel.getEventListener().onEvent(BaseGateway.TRANSACTION_BRIDGE_KEYED, bridgePayResponse);
    }
}

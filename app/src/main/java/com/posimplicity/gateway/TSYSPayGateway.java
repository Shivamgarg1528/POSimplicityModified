package com.posimplicity.gateway;

import com.posimplicity.activity.BaseActivity;
import com.posimplicity.model.local.CardInfoModel;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.Helper;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TSYSPayGateway extends BaseGateway {

    //private static final String URL = "https://stagegw.transnox.com/servlets/TransNox_API_Server";

    private static final String URL = "https://gateway.transit-pass.com/servlets/TransNox_API_Server";
    private static final String TSYS_DEVELOPER_ID = "002601G001";


    private Setting.AppSetting.Gateway.TsysPay mTsysPaySettings;

    public TSYSPayGateway(BaseActivity pBaseActivity) {
        super(pBaseActivity);
        mTsysPaySettings = AppSharedPrefs.getInstance(pBaseActivity).getSetting().getAppSetting().getGateway().getTsysPay();
    }

    @Override
    protected void sendPayment(CardInfoModel pCardInfoModel) {

        JSONObject outerJSON = new JSONObject();
        JSONObject innerJSON;
        Map<String, String> hashMap = new LinkedHashMap<>();

        try {
            switch (pCardInfoModel.getTransType()) {

                case TRANSACTION_TSYS_KEY_GENERATION:
                case TRANSACTION_TSYS_KEY_UPGRADE:


                    hashMap.put("mid", mTsysPaySettings.getMerchantId());
                    hashMap.put("userID", mTsysPaySettings.getUserName());
                    hashMap.put("password", mTsysPaySettings.getPassword());

                    if (!Helper.isBlank(mTsysPaySettings.getKey()))
                        hashMap.put("transactionKey", mTsysPaySettings.getKey());
                    hashMap.put("developerID", TSYS_DEVELOPER_ID);

                    innerJSON = new JSONObject(hashMap);
                    outerJSON.put("GenerateKey", innerJSON);
                    break;


                case TRANSACTION_TSYS_SWIPE: {

                    hashMap.put("deviceID", mTsysPaySettings.getDeviceId());
                    hashMap.put("transactionKey", mTsysPaySettings.getKey());
                    hashMap.put("cardDataSource", "SWIPE");
                    hashMap.put("transactionAmount", getFormattedAmount(pCardInfoModel.getTransactionAmt()));
                    hashMap.put("tip", getFormattedAmount(pCardInfoModel.getTipAmount()));
                    hashMap.put("track1Data", pCardInfoModel.getCardTrack1());
                    hashMap.put("track2Data", pCardInfoModel.getCardTrack2());
                    hashMap.put("developerID", TSYS_DEVELOPER_ID);

                    innerJSON = new JSONObject(hashMap);
                    outerJSON.put("Sale", innerJSON);

                    break;
                }
                case TRANSACTION_TSYS_KEYED: {

                    hashMap.put("deviceID", mTsysPaySettings.getDeviceId());
                    hashMap.put("transactionKey", mTsysPaySettings.getKey());
                    hashMap.put("cardDataSource", "MANUAL");
                    hashMap.put("transactionAmount", getFormattedAmount(pCardInfoModel.getTransactionAmt()));
                    hashMap.put("tip", getFormattedAmount(pCardInfoModel.getTipAmount()));
                    hashMap.put("cardNumber", pCardInfoModel.getCardNumber());
                    hashMap.put("expirationDate", pCardInfoModel.getCardExpMonth().concat(pCardInfoModel.getCardExpYear()));
                    hashMap.put("cvv2", pCardInfoModel.getCvv2Number());
                    hashMap.put("cardHolderName", pCardInfoModel.getCardHolderName());
                    hashMap.put("developerID", TSYS_DEVELOPER_ID);

                    innerJSON = new JSONObject(hashMap);
                    outerJSON.put("Sale", innerJSON);

                    break;
                }
                case TRANSACTION_TSYS_REFUND: {

                    hashMap.put("deviceID", mTsysPaySettings.getDeviceId());
                    hashMap.put("transactionKey", mTsysPaySettings.getKey());
                    hashMap.put("transactionAmount", getFormattedAmount(pCardInfoModel.getTransactionAmt()));
                    hashMap.put("tip", getFormattedAmount(pCardInfoModel.getTipAmount()));
                    hashMap.put("transactionID", "");
                    hashMap.put("developerID", TSYS_DEVELOPER_ID);

                    innerJSON = new JSONObject(hashMap);
                    outerJSON.put("Return", innerJSON);

                    break;
                }
                case TRANSACTION_TSYS_TIP_ADJUSTMENT: {

                    hashMap.put("deviceID", mTsysPaySettings.getDeviceId());
                    hashMap.put("transactionKey", mTsysPaySettings.getKey());
                    hashMap.put("tip", getFormattedAmount(pCardInfoModel.getTipAmount()));
                    hashMap.put("transactionID", "");
                    hashMap.put("developerID", TSYS_DEVELOPER_ID);

                    innerJSON = new JSONObject(hashMap);
                    outerJSON.put("TipAdjustment", innerJSON);

                    break;
                }
                case TRANSACTION_TSYS_SWIPE_ENCRYPTED: {

                    hashMap.put("deviceID", mTsysPaySettings.getDeviceId());
                    hashMap.put("transactionKey", mTsysPaySettings.getKey());
                    hashMap.put("cardDataSource", "SWIPE");
                    hashMap.put("transactionAmount", getFormattedAmount(pCardInfoModel.getTransactionAmt()));
                    hashMap.put("tip", getFormattedAmount(pCardInfoModel.getTipAmount()));
                    hashMap.put("track2Data", pCardInfoModel.getCardTrack2());
                    hashMap.put("encryptionType", "TDES");
                    hashMap.put("ksn", pCardInfoModel.getKsn());
                    hashMap.put("tokenRequired", "Y");
                    hashMap.put("developerID", TSYS_DEVELOPER_ID);

                    innerJSON = new JSONObject(hashMap);
                    outerJSON.put("Sale", innerJSON);

                    break;
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        jsonRequest(Constants.HttpMethod.HTTP_POST, URL, outerJSON.toString(), getHeaders());
    }

    private String getFormattedAmount(String pAmount) {
        if (Helper.isBlank(pAmount)) {
            return "0";
        } else {
            try {
                float floatVersion = Float.parseFloat(pAmount);
                floatVersion *= 100.0f;
                return (String.valueOf((int) floatVersion));
            } catch (Exception ex) {
                // Return empty so Gateway reject that transaction
                return "";
            }
        }
    }

    @Override
    protected void sendResultBack(CardInfoModel pCardInfoModel) {
        boolean isSuccess = parseResponse(pCardInfoModel);
        pCardInfoModel.getEventListener().onEvent(pCardInfoModel.getTransType(),isSuccess);
    }

    @Override
    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> hashProperty = super.getHeaders();
        hashProperty.put(HTTP.USER_AGENT, "infonox");
        hashProperty.put(HTTP.CONTENT_TYPE, "application/json");
        return hashProperty;
    }

    private boolean parseResponse(CardInfoModel pCardInfoModel) {
        if (Helper.isBlank(pCardInfoModel.getResponse())) {
            return false;
        }
        boolean isSuccess = false;
        try {
            JSONObject jsonObject = new JSONObject(pCardInfoModel.getResponse());
            Iterator<String> keys = jsonObject.keys();
            for (; keys.hasNext(); ) {
                String key = keys.next();
                JSONObject innerObject = jsonObject.getJSONObject(key);
                String status = innerObject.getString("status");
                if ("PASS".equalsIgnoreCase(status)) {
                    isSuccess = true;
                    switch (pCardInfoModel.getTransType()) {

                        case TRANSACTION_TSYS_KEY_GENERATION:
                        case TRANSACTION_TSYS_KEY_UPGRADE:
                            if (innerObject.has("transactionKey")) {
                                String transactionKey = innerObject.getString("transactionKey");
                                mTsysPaySettings.setKey(transactionKey);
                            }
                            break;

                        case TRANSACTION_TSYS_SWIPE:
                        case TRANSACTION_TSYS_KEYED: {
                            if (innerObject.has("transactionID")) {
                                String transactionID = innerObject.getString("transactionID");
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return isSuccess;
    }
}

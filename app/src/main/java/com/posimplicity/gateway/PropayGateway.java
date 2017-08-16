package com.posimplicity.gateway;

import android.util.Base64;

import com.easylibs.utils.JsonUtils;
import com.posimplicity.activity.BaseActivity;
import com.posimplicity.model.local.CardInfoModel;
import com.posimplicity.model.request.gateway.PropayIdRequest;
import com.posimplicity.model.response.gateway.PropayIdDeleteResponse;
import com.posimplicity.model.response.gateway.PropayIdResponse;
import com.posimplicity.model.response.gateway.PropayMethodId;
import com.posimplicity.model.response.gateway.PropayTransaction;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.CreditCardHelper;
import com.utils.Helper;
import com.utils.MyPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.PosInterfaces.PrefrenceKeyConst.PAYMENT_METHOD_ID_PROPAY;

public class PropayGateway extends BaseGateway {

    private static final String PROPAY_URL = "https://xmltestapi.propay.com/ProtectPay/Payers";

    public PropayGateway(BaseActivity pBaseActivity) {
        super(pBaseActivity);
    }

    @Override
    protected void sendPayment(CardInfoModel pCardInfoModel) {
        switch (pCardInfoModel.getTransType()) {

            case TRANSACTION_PROPAY_CREATE: {
                PropayIdRequest propayIdRequest = new PropayIdRequest();
                propayIdRequest.setName("POSimplicity");
                jsonRequest(Constants.HttpMethod.HTTP_PUT, PROPAY_URL, JsonUtils.jsonify(propayIdRequest), getHeaders());
                break;
            }
            case TRANSACTION_PROPAY_DELETE: {
                String payerId = AppSharedPrefs.getInstance(mBaseActivity).getSetting().getAppSetting().getGateway().getProPay().getPayerId();
                payerId = "/" + payerId + "/";
                jsonRequest(Constants.HttpMethod.HTTP_DELETE, PROPAY_URL + payerId, "", getHeaders());
                break;
            }
            case TRANSACTION_PROPAY_KEYED: {
                String payerId = AppSharedPrefs.getInstance(mBaseActivity).getSetting().getAppSetting().getGateway().getProPay().getPayerId();
                String ccType = "";
                CreditCardHelper cardHelper = new CreditCardHelper();
                boolean isCardOk = cardHelper.isCardOk(pCardInfoModel.getCardNumber());
                if (!isCardOk || Helper.isBlank(ccType = cardHelper.getCCType(pCardInfoModel.getCardNumber()))) {
                    mResponseStr = "";
                } else {
                    String payerUrl = "/" + payerId + "/PaymentMethods/";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("AccountNumber", pCardInfoModel.getCardNumber());
                        jsonObject.put("ExpirationDate", pCardInfoModel.getCardExpDate().concat(pCardInfoModel.getCardExpYear()));
                        jsonObject.put("AccountName", pCardInfoModel.getCardHolderName());
                        jsonObject.put("PaymentMethodType", ccType);
                        jsonObject.put("PayerAccountId", payerId);
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                    jsonRequest(Constants.HttpMethod.HTTP_PUT, PROPAY_URL + payerUrl, jsonObject.toString(), getHeaders());
                }
                if (!Helper.isBlank(mResponseStr)) {
                    PropayMethodId propayMethodId = JsonUtils.objectify(mResponseStr, PropayMethodId.class);
                    if (propayMethodId != null && propayMethodId.getRequestResult() != null && "SUCCESS".equalsIgnoreCase(propayMethodId.getRequestResult().getResultValue()) && !Helper.isBlank(propayMethodId.getPaymentMethodId())) {
                        String payerUrl = "/" + payerId + "/PaymentMethods/ProcessedTransactions";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("PayerAccountId", payerId);
                            jsonObject.put("paymentMethodID", propayMethodId.getPaymentMethodId());
                            jsonObject.put("Amount", pCardInfoModel.getTransactionAmt());
                            jsonObject.put("CurrencyCode", "USD");
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                        jsonRequest(Constants.HttpMethod.HTTP_PUT, PROPAY_URL + payerUrl, jsonObject.toString(), getHeaders());
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void sendResultBack(CardInfoModel pCardInfoModel) {
        switch (pCardInfoModel.getTransType()) {

            case TRANSACTION_PROPAY_CREATE: {
                PropayIdResponse response = JsonUtils.objectify(pCardInfoModel.getResponse(), PropayIdResponse.class);
                pCardInfoModel.getEventListener().onEvent(pCardInfoModel.getTransType(), response);
                break;
            }
            case TRANSACTION_PROPAY_DELETE: {
                PropayIdDeleteResponse response = JsonUtils.objectify(pCardInfoModel.getResponse(), PropayIdDeleteResponse.class);
                pCardInfoModel.getEventListener().onEvent(pCardInfoModel.getTransType(), response);
                break;
            }

            case TRANSACTION_PROPAY_KEYED: {
                boolean isSuccess = false;
                PropayTransaction propayTransaction = JsonUtils.objectify(pCardInfoModel.getResponse(), PropayTransaction.class);
                if (propayTransaction != null && propayTransaction.getRequestResult() != null && "SUCCESS".equalsIgnoreCase(propayTransaction.getRequestResult().getResultValue()) && propayTransaction.getTransaction() != null && "SUCCESS".equalsIgnoreCase(propayTransaction.getTransaction().getTransactionResult())) {
                    isSuccess = true;
                }
                pCardInfoModel.getEventListener().onEvent(pCardInfoModel.getTransType(), isSuccess);
                break;
            }
        }
    }

    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> propertyMap = new HashMap<>();
        propertyMap.put("Accept-Encoding", "deflate");
        propertyMap.put("Authorization", "Basic " + Base64.encodeToString(("5747881770773031:457ff74f-b004-4896-8b17-3db6755445b0").getBytes(), Base64.NO_WRAP));
        propertyMap.put("Content-Type", "application/json");
        propertyMap.put("Connection", "Keep-Alive");
        propertyMap.put("Host", "xmltestapi.propay.com");
        propertyMap.put("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
        return propertyMap;
    }
}

package com.posimplicity.gateway;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.posimplicity.BuildConfig;
import com.posimplicity.activity.BaseActivity;
import com.posimplicity.model.local.CardInfoModel;
import com.utils.Helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public abstract class BaseGateway extends AsyncTask<CardInfoModel, Void, CardInfoModel> {

    // Gateways
    public static final short TRANSACTION_PROPAY_CREATE = 0;
    public static final short TRANSACTION_PROPAY_DELETE = 1;
    public static final short TRANSACTION_PROPAY_KEYED = 2;

    public static final short TRANSACTION_BRIDGE_KEYED = 4;

    public static final short TRANSACTION_PLUG_PAY_KEYED = 5;
    public static final short TRANSACTION_PLUG_PAY_SWIPE = 6;
    public static final short TRANSACTION_PLUG_PAY_SWIPE_ENCRYPTED = 7;

    public static final short TRANSACTION_TSYS_KEY_GENERATION = 8;
    public static final short TRANSACTION_TSYS_KEY_UPGRADE = 9;
    public static final short TRANSACTION_TSYS_KEYED = 10;
    public static final short TRANSACTION_TSYS_SWIPE = 11;
    public static final short TRANSACTION_TSYS_SWIPE_ENCRYPTED = 12;
    public static final short TRANSACTION_TSYS_REFUND = 13;
    public static final short TRANSACTION_TSYS_TIP_ADJUSTMENT = 14;


    public static final int GATEWAY_NONE = -1;
    public static final int GATEWAY_TSYS = 0;
    public static final int GATEWAY_PLUG_PAY = 1;
    public static final int GATEWAY_BRIDGE_PAY = 2;
    public static final int GATEWAY_PROPAY = 3;
    public static final int GATEWAY_DEJAVOO = 4;
    public static final int GATEWAY_SETTING = 5;

    @SuppressLint("StaticFieldLeak")
    protected BaseActivity mBaseActivity;

    protected String mResponseStr = "";

    BaseGateway(BaseActivity pBaseActivity) {
        this.mBaseActivity = pBaseActivity;
    }

    protected abstract void sendPayment(CardInfoModel pCardInfoModel);

    protected abstract void sendResultBack(CardInfoModel pCardInfoModel);

    public final void startGatewayProcess(CardInfoModel pCardInfoModel) {
        executeOnExecutor(THREAD_POOL_EXECUTOR, pCardInfoModel);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mBaseActivity.showProgressDialog();
    }

    @Override
    protected CardInfoModel doInBackground(CardInfoModel... cardInfoModels) {
        sendPayment(cardInfoModels[0]);
        cardInfoModels[0].setResponse(mResponseStr);
        return cardInfoModels[0];
    }

    @Override
    protected void onPostExecute(CardInfoModel cardInfoModel) {
        super.onPostExecute(cardInfoModel);
        if (BuildConfig.DEBUG) {
            Log.d("BaseGateway", "Response-> [" + cardInfoModel.getResponse() + "]");
        }
        mBaseActivity.dismissProgressDialog();
        sendResultBack(cardInfoModel);
    }

    void jsonRequest(String pMethod, String pUrl, String pJSON, HashMap<String, String> pRequestPropertyMap) {
        try {
            if (BuildConfig.DEBUG) {
                Log.d("BaseGateway", "jsonRequest() called with: pMethod = [" + pMethod + "], pUrl = [" + pUrl + "], pJSON = [" + pJSON + "], pRequestPropertyMap = [" + pRequestPropertyMap + "]");
            }
            StringBuffer response = null;
            URL url = new URL(pUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(pMethod);
            connection.setDoOutput(true);
            if (pRequestPropertyMap != null) {
                Set<String> keys = pRequestPropertyMap.keySet();
                for (String key : keys) {
                    connection.setRequestProperty(key, pRequestPropertyMap.get(key));
                }
            }
            if (!Helper.isBlank(pJSON)) {
                OutputStream out = connection.getOutputStream();
                out.write(pJSON.getBytes());
                out.flush();
                out.close();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = getResponseString(connection);
            }
            mResponseStr = response.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            mResponseStr = "";
        }
    }

    @NonNull
    private StringBuffer getResponseString(HttpURLConnection pUrlConnection) throws Exception {
        StringBuffer apiResponse = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pUrlConnection.getInputStream()));
        String eachLine;
        while ((eachLine = bufferedReader.readLine()) != null) {
            apiResponse.append(eachLine);
        }
        bufferedReader.close();
        return apiResponse;
    }

    protected HashMap<String, String> getHeaders() {
        return new HashMap<>();
    }

}

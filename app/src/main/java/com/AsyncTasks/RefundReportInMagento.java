package com.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.Beans.TSYSResponseModel;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.database.local.SaveTransactionDetails;
import com.Fragments.MaintFragmentCCAdmin;
import com.posimplicity.gateway.TSYSGateway;
import com.posimplicity.gateway.TSYSGateway.OnCallBackForTSYS;
import com.PosInterfaces.PrefrenceKeyConst;
import com.RecieptPrints.GoForPrint;
import com.utils.CreateFormatOnMagentoCall;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.HomeActivity;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class RefundReportInMagento extends AsyncTask<Void, Integer, String> implements OnCancelListener, PrefrenceKeyConst {

    private String baseUrl;
    private HomeActivity instance;
    private String transactionId;
    private String response;
    private Context mContext;
    private ProgressDialog progressHUD;
    private float cash, credit, check, tipAmount, gift, rewards, custom1, custom2;
    private JSONObject jsonObject;
    private int printMode;

    public RefundReportInMagento(Context mContext, String transId, float cash, float credit, float check, float gift, float reward, float custom1, float custom2, float tipAmount, int printMode) {
        this.mContext = mContext;
        this.baseUrl = MyPreferences.getMyPreference(STORE_NAME, mContext);
        this.transactionId = transId;
        this.cash = cash;
        this.credit = credit;
        this.check = check;
        this.gift = gift;
        this.rewards = reward;
        this.custom1 = custom1;
        this.custom2 = custom2;
        this.tipAmount = tipAmount;
        this.printMode = printMode;
        this.instance = HomeActivity.localInstance;
    }

    @Override
    protected void onPreExecute() {
        progressHUD = ProgressDialog.show(mContext, "", "Processing...", true, false, this);
        jsonObject = new CreateFormatOnMagentoCall(instance.dataList).createJSONFormatOnRefundTime();
    }

    @Override
    protected String doInBackground(Void... params) {
        return excuteApiForResponse();
    }

    @Override
    protected void onPostExecute(String result) {
        progressHUD.dismiss();

        if (response.equalsIgnoreCase("No Internet")) {
            NoInternetDialog.noInternetDialogShown(mContext);
        } else if (response.equalsIgnoreCase("ExceptionOccur")) {
            ExceptionDialog.onExceptionOccur(mContext);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String response = jsonObject.getString("msg");
                if (response.equalsIgnoreCase("Refund item sucessfully")) {
                    Variables.cashAmount = cash;
                    Variables.checkAmount = check;
                    Variables.custom1Amount = custom1;
                    Variables.custom2Amount = custom2;
                    Variables.giftAmount = gift;
                    Variables.rewardsAmount = rewards;
                    instance.surchrgeAmountList.add(credit);
                    SaveTransactionDetails.saveTransactionInDataBase(mContext, -Variables.itemsAmount - Variables.totalDiscount, -Variables.taxAmount, -(Variables.totalBillAmount + Variables.taxAmount), -cash, -credit, -check, -gift, -rewards, ReportsTable.YES_REFUND, ReportsTable.SUCCESSFULL, -custom1, -custom2);

                    final TSYSGateway tsysGateway = new TSYSGateway(mContext, "" + credit, TSYSGateway.TSYS_REFUND, MyStringFormat.formatWith2DecimalPlaces(tipAmount));
                    tsysGateway.onInterfaceRegister(new OnCallBackForTSYS() {
                        @Override
                        public void onTSYSResponse(String responseDate, int requestedCodeReturn) {
                            TSYSResponseModel response = tsysGateway.paresTSYSResponse(responseDate);
                            ToastUtils.showOwnToast(mContext, response.getResponseMsg());
                            GoForPrint goForPrint = new GoForPrint(mContext, printMode, false);
                            goForPrint.onExectue();
                        }
                    });
                    if (Variables.refundByCC && MyPreferences.getLongPreference(GATEWAY_USED_POSITION, mContext) == MaintFragmentCCAdmin.TSYS_PAY_ID)
                        tsysGateway.doExection();
                    else if (Variables.refundByCC && MyPreferences.getLongPreference(GATEWAY_USED_POSITION, mContext) == MaintFragmentCCAdmin.DEJAVO_PAY_ID) {
                        Toast.makeText(mContext, "Please Use Function Tab To Refund This Transaction With Dejavoo Completely.", Toast.LENGTH_LONG).show();
                        GoForPrint goForPrint = new GoForPrint(mContext, printMode, false);
                        goForPrint.onExectue();
                    } else {
                        GoForPrint goForPrint = new GoForPrint(mContext, printMode, false);
                        goForPrint.onExectue();
                    }
                } else
                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String excuteApiForResponse() {

        HttpPost hPost = new HttpPost(baseUrl + "?tag=credit_memo");
        HttpClient hClient = new DefaultHttpClient();

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("customerEmail", Variables.billToName));
        nameValuePairs.add(new BasicNameValuePair("discount", "" + Variables.orderLevelDiscount));
        nameValuePairs.add(new BasicNameValuePair("transId", transactionId));
        nameValuePairs.add(new BasicNameValuePair("details", jsonObject.toString()));

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
            hPost.setEntity(urlEncodedFormEntity);
            System.out.println(baseUrl + "?tag=credit_memo&" + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs)));
            ResponseHandler<String> rHandler = new BasicResponseHandler();
            boolean isConnected = InternetConnectionDetector.isInternetAvailable(mContext);

            if (isConnected)
                response = hClient.execute(hPost, rHandler);
            else
                response = "No Internet";

            Log.v("RESPONSE :", response.trim());
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response = "ExceptionOccur";
            Log.v("RESPONSE :", response.trim());
            return response;
        }
    }

    @Override
    public void onCancel(DialogInterface arg0) {
        progressHUD.dismiss();
    }
}

package com.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.Beans.CheckOutParentModel;
import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.database.local.SaveTransactionDetails;
import com.posimplicity.database.local.StaffTable;
import com.PosInterfaces.PrefrenceKeyConst;
import com.RecieptPrints.PrintExtraReceipt;
import com.RecieptPrints.PrintReceiptCustomer;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.CreateFormatOnMagentoCall;
import com.utils.POSApp;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.utils.WebServiceCall;
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

public class CompleteReportInMagento extends AsyncTask<Void, Integer, String> implements PrefrenceKeyConst {

    private String baseUrl;
    private HomeActivity instance;
    private String transactionId;
    private String response = "ExceptionOccur";
    private Context mContext;
    private float custom1Amt, custom2Amt, check, total, discount, tax, netAmount, change, cash, rewardAmount, giftCard, cashAfterChg, rewAfterChange, giftAfterChg, creditAmt,orderLevelDiscount;
    private ArrayList<CheckOutParentModel> parentList;
    private ArrayList<Float> surchargeList;
    private String ordeStaus;
    private JSONObject jsonObject;
    private String paymentMode, shipToName, billToName, orderComment;
    private boolean savedInLocalDb;
    private POSApp POSApp;
    //private SetValuesForOrdersApi setValues;

    @SuppressWarnings("unchecked")
    public CompleteReportInMagento(Context mContext, String orderStatus, String paymentMode, boolean savedInLocalDb) {

        this.mContext = mContext;
        this.baseUrl = MyPreferences.getMyPreference(STORE_NAME, mContext);
        this.transactionId = MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext);
        this.instance = HomeActivity.localInstance;
        this.parentList = (ArrayList<CheckOutParentModel>) instance.dataList.clone();
        this.total = Variables.itemsAmount;
        this.discount = Variables.totalDiscount;
        this.tax = Variables.taxAmount;
        this.netAmount = Variables.totalBillAmount;
        this.change = Variables.changeAmt;
        this.cash = Variables.cashAmount;
        this.check = Variables.checkAmount;
        this.custom1Amt = Variables.custom1Amount;
        this.custom2Amt = Variables.custom2Amount;
        this.rewardAmount = Variables.rewardsAmount;
        this.giftCard = Variables.giftAmount;
        this.cashAfterChg = Variables.cashAfterChange;
        this.creditAmt = Variables.ccAmount;
        this.giftAfterChg = Variables.giftAmtAfterChange;
        this.rewAfterChange = Variables.rewardsAfterChange;
        this.orderLevelDiscount = Variables.orderLevelDiscount;
        this.surchargeList = (ArrayList<Float>) instance.surchrgeAmountList.clone();
        this.paymentMode = paymentMode;
        this.savedInLocalDb = savedInLocalDb;
        this.shipToName = Variables.shipToName;
        this.billToName = Variables.billToName;
        this.POSApp = POSApp.getInstance();
        this.ordeStaus = orderStatus;
        this.orderComment = Variables.orderComment;
        //this.setValues         = new SetValuesForOrdersApi(mContext);
    }

    @Override
    protected void onPreExecute() {
        jsonObject = new CreateFormatOnMagentoCall(instance.dataList).createJSONObjForRequest();
        /*setValues.setallRequestedValues();
        ordeStaus = setValues.getOrderStatus();*/
    }

    @Override
    protected String doInBackground(Void... params) {
        excuteApiForResponse();
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("RESPONSE :" + response.trim());
        onPostWork();
    }

    private void onPostWork() {


        if (response.equalsIgnoreCase("No Internet") || response.equalsIgnoreCase("ExceptionOccur") || response.isEmpty()) {
            Toast.makeText(mContext, "Unable to Record Last Order", Toast.LENGTH_SHORT).show();
            onSaveAndPrint(WebServiceCall.WEBSERVICE_CALL_NO_INTERENET);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("msg");
                if (message.equalsIgnoreCase("saved in magento database")) {
                    //if(message.equalsIgnoreCase("saved in magento databas")){
                    new NotificationApi(mContext, ordeStaus).execute();
                    ToastUtils.showOwnToast(mContext, "Order Saved SuccessFully");
                    onSaveAndPrint(WebServiceCall.WEBSERVICE_CALL_RESULT_VALID);
                } else {
                    ToastUtils.showOwnToast(mContext, message);
                    onSaveAndPrint(WebServiceCall.WEBSERVICE_CALL_NO_INTERENET);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onSaveAndPrint(WebServiceCall.WEBSERVICE_CALL_NO_INTERENET);
            }
        }

    }

    private void onSaveAndPrint(int statusCode) {

        switch (statusCode) {

            case WebServiceCall.WEBSERVICE_CALL_NO_INTERENET:
                if (savedInLocalDb)
                    SaveTransactionDetails.saveTransactionInDataBase(mContext, total - discount, tax, netAmount, cashAfterChg, creditAmt, check, giftAfterChg, rewAfterChange, ReportsTable.NO_REFUND, ReportsTable.FAILED, custom1Amt, custom2Amt);
                if (PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)) {
                    new UsbPR(mContext, new PrinterCallBack() {

                        @Override
                        public void onDisconnected() {
                            PrintSettings.showUsbNotAvailableToast(mContext);
                        }

                        @Override
                        public void onConnected(BasePR pPrinter) {
                            PrintExtraReceipt.onFailedToSave(mContext, pPrinter);
                            new PrintReceiptCustomer(mContext).onFailedToSaveReciepts(parentList, surchargeList, total, discount, tax, netAmount, change, cash, check, rewardAmount, giftCard, creditAmt, pPrinter);
                        }
                    }).onConnectionStart();
                }

                if (PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)) {
                    PrintExtraReceipt.onFailedToSave(mContext, POSApp.getPrinterBT());
                    new PrintReceiptCustomer(mContext).onFailedToSaveReciepts(parentList, surchargeList, total, discount, tax, netAmount, change, cash, check, rewardAmount, giftCard, creditAmt, POSApp.getPrinterBT());
                }

                break;

            case WebServiceCall.WEBSERVICE_CALL_RESULT_VALID:
                if (savedInLocalDb)
                    SaveTransactionDetails.saveTransactionInDataBase(mContext, total - discount, tax, netAmount, cashAfterChg, creditAmt, check, giftAfterChg, rewAfterChange, ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, custom1Amt, custom2Amt);

                break;

            default:
                break;
        }
    }

    public String excuteApiForResponse() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        if (MyPreferences.getBooleanPrefrences(CLERK_ORDER_ASSIGN, mContext)) {

            CustomerModel customerModel = new CustomerTable(mContext).getSingleInfoFromTableBasedOnGroupId();
            if (!customerModel.isCustomerNotValid()) {
                billToName = customerModel.getTelephoneNo() + "@gmail.com";
                nameValuePairs.add(new BasicNameValuePair("group_id", customerModel.getGroupId()));
            }
        }

        HttpPost hPost = new HttpPost(baseUrl + "?tag=save_orders_in_magento");
        HttpClient hClient = new DefaultHttpClient();

        nameValuePairs.add(new BasicNameValuePair("customerEmail", billToName));
        nameValuePairs.add(new BasicNameValuePair("discount", "" + orderLevelDiscount));
        nameValuePairs.add(new BasicNameValuePair("transId", transactionId));
        nameValuePairs.add(new BasicNameValuePair("paymode", paymentMode));
        nameValuePairs.add(new BasicNameValuePair("orderStatus", ordeStaus));
        nameValuePairs.add(new BasicNameValuePair("ship_to_name", shipToName));
        nameValuePairs.add(new BasicNameValuePair("fee", "0"));
        nameValuePairs.add(new BasicNameValuePair("order_comment", orderComment));
        nameValuePairs.add(new BasicNameValuePair("details", jsonObject.toString()));


        if (!MyPreferences.getBooleanPrefrences(ENCRYPTED_PAY_ENABLE, mContext)) {

            long gatewayType = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION, mContext);
            if (!(gatewayType == 1)) {
                nameValuePairs.add(new BasicNameValuePair("CCNumber", Variables.ccNumber));
                nameValuePairs.add(new BasicNameValuePair("Name", Variables.gateWayTrasId));
                nameValuePairs.add(new BasicNameValuePair("ExpiryYear", Variables.CcExpiryYear));
                nameValuePairs.add(new BasicNameValuePair("ExpiryDate", Variables.CcExpiryDate));
                nameValuePairs.add(new BasicNameValuePair("CardType", Variables.Cardtype));
            }
        }

        if (MyPreferences.getBooleanPrefrences(CLERK_REPORTING, mContext)) {
            nameValuePairs.add(new BasicNameValuePair("shipping_cost", "" + new StaffTable(mContext).getSumOfPayGradesOfLoginUsers()));
        }

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
            hPost.setEntity(urlEncodedFormEntity);
            ResponseHandler<String> rHandler = new BasicResponseHandler();
            System.out.println(baseUrl + "?tag=save_orders_in_magento&" + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs)));
            boolean isConnected = InternetConnectionDetector.isInternetAvailable(mContext);
            if (isConnected)
                response = hClient.execute(hPost, rHandler);
            else
                response = "No Internet";
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response = "ExceptionOccur";
            return response;
        }
    }
}


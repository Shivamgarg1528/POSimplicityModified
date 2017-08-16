package com.posimplicity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;

import com.posimplicity.database.POSDatabaseHandler;
import com.Dialogs.LoginPopUp;
import com.PosInterfaces.PrefrenceKeyConst;
import com.Services.ParseService;
import com.utils.Constants;
import com.utils.MyPreferences;
import com.utils.StartAndroidActivity;
import com.controller.ApisController;
import com.easylibs.http.EasyHttpResponse;
import com.easylibs.listener.EventListener;
import com.google.gson.JsonObject;
import com.posimplicity.model.response.api.CategoryParent;
import com.posimplicity.model.response.api.CustomerGroupParent;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.model.response.api.ProductParent;

import org.json.JSONArray;


public class SplashActivity extends BaseActivity implements EventListener {

    private final String MINT_ID = "b5f9f859";
    private POSDatabaseHandler dataBaseHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, false, this);
        setContentView(R.layout.activity_splash);

        //ApisController.getCategories(this, this);
        //ApisController.getProducts(this, this);
        //ApisController.getProductOptions(this, this);
        ApisController.getCustomers(this, this);
        ApisController.getCustomersGroup(this, this);

        long hours = MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.HOURS_OF_DAY, this);
        long mint = MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.MINUTES, this);

        if (hours < 0)
            MyPreferences.setLongPreferences(PrefrenceKeyConst.HOURS_OF_DAY, 3, this);

        if (mint < 0)
            MyPreferences.setLongPreferences(PrefrenceKeyConst.MINUTES, 0, this);


        //dataBaseHandler = new POSDatabaseHandler(mContext);
        //dataBaseHandler.openWritableDataBase();

        onInitViews();
        onListenerRegister();

        if (TextUtils.isEmpty(MyPreferences.getMyPreference(ANDROID_DEVICE_ID, mContext))) {
            String androidId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
            MyPreferences.setMyPreference(ANDROID_DEVICE_ID, androidId, mContext);
        }

        globalApp.onScreenSize();
        String baseUrl = MyPreferences.getMyPreference(STORE_NAME, mContext);
        String setupTime = MyPreferences.getMyPreference(SETUP_TIME, mContext);

        if (TextUtils.isEmpty(baseUrl))
            new LoginPopUp(mContext).onLoginDialog();
        else if (TextUtils.isEmpty(setupTime))
            startActivity(new Intent(mContext, SetupActivity.class));
        else {
            //StartAndroidActivity.onActivityStart(true, mContext, HomeActivity.class);
            StartAndroidActivity.onActivityStart(true, mContext, SyncOptionActivity.class);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataBaseHandler != null) {
            dataBaseHandler.close();
        }
    }


    @Override
    public void onInitViews() {
    }

    @Override
    public void onListenerRegister() {
    }

    @Override
    public void onDataRecieved(JSONArray arry) {
    }

    @Override
    public void onSocketStateChanged(int state) {
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
        switch (pEventCode) {

            case Constants.API_CATEGORY: {
                CategoryParent categoryList = ((EasyHttpResponse<CategoryParent>) pEventData).getData();
                if (categoryList != null && categoryList.getDataList() != null) {
                    ParseService.parseCategoryResponse(this, categoryList.getDataList());
                }
                break;
            }

            case Constants.API_PRODUCT: {
                ProductParent productList = ((EasyHttpResponse<ProductParent>) pEventData).getData();
                if (productList != null && productList.getDataList() != null) {
                    ParseService.parseProductResponse(this, productList.getDataList());
                }
                break;
            }

            case Constants.API_PRODUCT_OPTIONS: {
                JsonObject responseInJson = ((EasyHttpResponse<JsonObject>) pEventData).getData();
                if (responseInJson != null) {
                    ParseService.parsedProductOptionResponse(responseInJson.toString(), this);
                }
                break;
            }
            case Constants.API_CUSTOMER: {
                CustomerParent customerList = ((EasyHttpResponse<CustomerParent>) pEventData).getData();
                if (customerList != null && customerList.getCustomerList() != null) {
                    ParseService.parseCustomerResponse(this, customerList.getCustomerList());
                }
                break;
            }
            case Constants.API_CUSTOMER_GROUP: {
                CustomerGroupParent customerGroupList = ((EasyHttpResponse<CustomerGroupParent>) pEventData).getData();
                if (customerGroupList != null && customerGroupList.getGroupDetails() != null) {
                    ParseService.parseCustomerGroupResponse(this, customerGroupList.getGroupDetails());
                }
                break;
            }
        }
    }
}

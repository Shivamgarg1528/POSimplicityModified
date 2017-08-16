package com.posimplicity.fragment.start;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.posimplicity.dialog.AlertHelper;
import com.PosInterfaces.PrefrenceKeyConst;
import com.Services.ParseService;
import com.posimplicity.fragment.base.BaseFragment;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyPreferences;
import com.controller.ApisController;
import com.posimplicity.database.server.CategoryTable;
import com.posimplicity.database.server.CustomerGroupTable;
import com.posimplicity.database.server.CustomerTable;
import com.posimplicity.database.server.ProductOptionTable;
import com.posimplicity.database.server.ProductTable;
import com.easylibs.http.EasyHttpResponse;
import com.easylibs.listener.EventListener;
import com.google.gson.JsonObject;
import com.posimplicity.model.response.api.CategoryParent;
import com.posimplicity.model.response.api.CustomerGroupParent;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.model.response.api.ProductParent;
import com.posimplicity.R;

public class SetupFragment extends BaseFragment implements EventListener {

    private static final int TOTAL_API = 5;
    private int mTotalApiCount = 0;

    public SetupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean pAppSetup = MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_APP_SETUP, mBaseActivity);
        if (pAppSetup) {
            AlertHelper.getAlertDialog(mBaseActivity, "Do you want to sync the application ?", "Yes", "Skip", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (DialogInterface.BUTTON_POSITIVE == which) {
                        startSetupProcess();
                    } else {
                        mBaseActivity.replaceFragment(new OperatorFragment());
                    }
                }
            });
        } else {
            startSetupProcess();
        }
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {

        ++mTotalApiCount; // records that an api called

        switch (pEventCode) {

            case Constants.API_CATEGORY: {
                CategoryParent categoryList = ((EasyHttpResponse<CategoryParent>) pEventData).getData();
                if (categoryList != null && categoryList.getDataList() != null) {
                    ParseService.parseCategoryResponse(mBaseActivity, categoryList.getDataList());
                }
                break;
            }

            case Constants.API_PRODUCT: {
                ProductParent productList = ((EasyHttpResponse<ProductParent>) pEventData).getData();
                if (productList != null && productList.getDataList() != null) {
                    ParseService.parseProductResponse(mBaseActivity, productList.getDataList());
                }
                break;
            }

            case Constants.API_PRODUCT_OPTIONS: {
                JsonObject responseInJson = ((EasyHttpResponse<JsonObject>) pEventData).getData();
                if (responseInJson != null) {
                    ParseService.parsedProductOptionResponse(responseInJson.toString(), mBaseActivity);
                }
                break;
            }
            case Constants.API_CUSTOMER: {
                CustomerParent customerList = ((EasyHttpResponse<CustomerParent>) pEventData).getData();
                if (customerList != null && customerList.getCustomerList() != null) {
                    ParseService.parseCustomerResponse(mBaseActivity, customerList.getCustomerList());
                }
                break;
            }
            case Constants.API_CUSTOMER_GROUP: {
                CustomerGroupParent customerGroupList = ((EasyHttpResponse<CustomerGroupParent>) pEventData).getData();
                if (customerGroupList != null && customerGroupList.getGroupDetails() != null) {
                    ParseService.parseCustomerGroupResponse(mBaseActivity, customerGroupList.getGroupDetails());
                }
                break;
            }
        }
        if (mTotalApiCount == TOTAL_API) { // All Apis Called
            mBaseActivity.dismissProgressDialog();
            MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_APP_SETUP, true, mBaseActivity);
            mBaseActivity.replaceFragment(new OperatorFragment());
        }
    }

    /**
     * Clear all server tables before loading new data
     */
    private void clearServerDatabaseTables() {
        new CategoryTable(mBaseActivity).deleteAll();
        new CustomerGroupTable(mBaseActivity).deleteAll();
        new CustomerTable(mBaseActivity).deleteAll();
        new ProductOptionTable(mBaseActivity).deleteAll();
        new ProductTable(mBaseActivity).deleteAll();

        // Also Resting IS_APP_SETUP to false
        MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_APP_SETUP, false, mBaseActivity);
    }

    /**
     * start download from server
     */
    private void startSetupProcess() {

        if (!Helper.isConnected(mBaseActivity)) {
            AlertHelper.getNoInternetAlert(mBaseActivity, "Skip", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (DialogInterface.BUTTON_POSITIVE == which) {
                        startSetupProcess();
                    } else {
                        // No internet . close activity
                        mBaseActivity.finish();
                    }
                }
            });
            return;
        }
        // Internet Available....
        clearServerDatabaseTables();
        mBaseActivity.showProgressDialog();
        ApisController.getCategories(mBaseActivity, this);
        ApisController.getCustomers(mBaseActivity, this);
        ApisController.getCustomersGroup(mBaseActivity, this);
        ApisController.getProductOptions(mBaseActivity, this);
        ApisController.getProducts(mBaseActivity, this);
    }
}

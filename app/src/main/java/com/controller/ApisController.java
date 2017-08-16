package com.controller;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.PosInterfaces.PrefrenceKeyConst;
import com.easylibs.http.EasyHttp;
import com.easylibs.http.EasyHttpRequest;
import com.easylibs.listener.EventListener;
import com.easylibs.utils.EasyUtils;
import com.google.gson.JsonObject;
import com.posimplicity.model.local.OrderModel;
import com.posimplicity.model.response.api.CategoryParent;
import com.posimplicity.model.response.api.CustomerGroupParent;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.model.response.api.ProductParent;
import com.posimplicity.model.response.other.OrderResponse;
import com.posimplicity.model.response.other.Validate;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyPreferences;

import java.util.HashMap;

public class ApisController {

    private static final int NETWORK_CACHE_DISABLE = -1;
    private static final int SOCKET_TIMEOUT_MILLIS = 4 * (int) DateUtils.SECOND_IN_MILLIS;

    private static String getBaseUrl(Context pContext, String pStoreName) {
        return "http://" + (!EasyUtils.isBlank(pStoreName) ? pStoreName : MyPreferences.getMyPreference(PrefrenceKeyConst.STORE_NAME, pContext)) + ".posimplicity.biz/api/pos.php?";
    }

    public static void getCategories(Context pContext, EventListener pEventListener) {

        EasyHttpRequest<CategoryParent> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, null) + "tag=categories_list");
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_CATEGORY);
        request.setResponseType(CategoryParent.class);
        request.setEventListener(pEventListener);

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void getProducts(Context pContext, EventListener pEventListener) {

        EasyHttpRequest<ProductParent> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, null) + "tag=product_list");
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_PRODUCT);
        request.setResponseType(ProductParent.class);
        request.setEventListener(pEventListener);

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void getProductOptions(Context pContext, EventListener pEventListener) {

        EasyHttpRequest<JsonObject> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, null) + "tag=product_options_list");
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_PRODUCT_OPTIONS);
        request.setEventListener(pEventListener);
        request.setResponseType(JsonObject.class);

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void getCustomers(Context pContext, EventListener pEventListener) {

        EasyHttpRequest<CustomerParent> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, null) + "tag=customer_list");
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_CUSTOMER);
        request.setEventListener(pEventListener);
        request.setResponseType(CustomerParent.class);

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void getCustomersGroup(Context pContext, EventListener pEventListener) {

        EasyHttpRequest<CustomerGroupParent> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, null) + "tag=customer_group");
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_CUSTOMER_GROUP);
        request.setEventListener(pEventListener);
        request.setResponseType(CustomerGroupParent.class);

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void storeValidate(Context pContext, EventListener pEventListener, String pStoreName) {

        EasyHttpRequest<Validate> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, pStoreName) + "tag=store_exist");
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_VALIDATE_STORE);
        request.setEventListener(pEventListener);
        request.setResponseType(Validate.class);

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void adminValidate(Context pContext, EventListener pEventListener, String pUrl) {

        EasyHttpRequest<Validate> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, null) + "tag=login_details" + pUrl);
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_VALIDATE_ADMIN);
        request.setEventListener(pEventListener);
        request.setResponseType(Validate.class);

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static Validate createOrder(final Context pContext, OrderModel pOrderModel, EventListener pEventListener) {

        HashMap<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put("details", Helper.getCreateOrderDetails(pOrderModel));
        queryParamsMap.put("discount", String.valueOf(pOrderModel.getOrderDisPercentage()));
        queryParamsMap.put("transId", pOrderModel.getOrderId());
        queryParamsMap.put("paymode", pOrderModel.getOrderPaymentMode());
        queryParamsMap.put("orderStatus", pOrderModel.getOrderStatus());
        queryParamsMap.put("ship_to_name", "");
        queryParamsMap.put("fee", "0");
        queryParamsMap.put("order_comment", pOrderModel.getOrderComment() != null ? pOrderModel.getOrderComment() : "");
        queryParamsMap.put("customerEmail", pOrderModel.getOrderAssignCustomer() != null ? pOrderModel.getOrderAssignCustomer().getCustomerEmail() : "");

        if (pOrderModel.getOrderAssignClerk() != null) {
            queryParamsMap.put("customerEmail", pOrderModel.getOrderAssignClerk().getCustomerTelephone() + "@gmail.com");
            queryParamsMap.put("group_id", pOrderModel.getOrderAssignClerk().getCustomerGroupId());
        }
        queryParamsMap.put("CCNumber", "");
        queryParamsMap.put("Name", "");
        queryParamsMap.put("ExpiryYear", "");
        queryParamsMap.put("ExpiryDate", "");
        queryParamsMap.put("CardType", "");

        queryParamsMap.put("tag", "save_orders_in_magento");

        EasyHttpRequest<Validate> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(Helper.appendQueryParams(getBaseUrl(pContext, null), queryParamsMap));
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_CREATE_ORDER);
        request.setResponseType(Validate.class);
        if (pEventListener != null) {
            request.setEventListener(pEventListener);
            EasyHttp.getExecutor(pContext).executeAsync(request);
            return null;
        } else {
            return EasyHttp.getExecutor(pContext).executeSync(request).getData();
        }
    }

    public static void shareOrder(Context pContext, HashMap<String, String> pQueryParams) {
        pQueryParams.put("tag", "sales_post");

        EasyHttpRequest<String> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(Helper.appendQueryParams(getBaseUrl(pContext, null), pQueryParams));
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void pushNotification(Context pContext, HashMap<String, String> pQueryParams) {
        pQueryParams.put("tag", "send_notification");

        EasyHttpRequest<String> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(Helper.appendQueryParams(getBaseUrl(pContext, null), pQueryParams));
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        EasyHttp.getExecutor(pContext).executeAsync(request);
    }

    public static void getOrders(Context pContext, int pCaseValue, EventListener pEventListener) {

        EasyHttpRequest<OrderResponse> request = new EasyHttpRequest<>();
        request.setContext(pContext);
        request.setCacheTtl(NETWORK_CACHE_DISABLE);
        request.setSocketTimeOutMs(SOCKET_TIMEOUT_MILLIS);
        request.setUrl(getBaseUrl(pContext, null) + "tag=fetch_order_based_on_requirement&caseValue=" + pCaseValue);
        request.setHttpMethod(EasyHttpRequest.Method.GET);
        request.setEventCode(Constants.API_ORDERS);
        request.setEventListener(pEventListener);
        request.setResponseType(OrderResponse.class);
        Log.d("ApisController", request.getUrl());

        EasyHttp.getExecutor(pContext).executeAsync(request);
    }
}

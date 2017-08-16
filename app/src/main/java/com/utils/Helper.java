package com.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.JsonPakage.XML;
import com.PosInterfaces.PrefrenceKeyConst;
import com.adapter.recycler.SwitchAdapter;
import com.posimplicity.activity.BaseActivity;
import com.posimplicity.activity.StartingActivity;
import com.posimplicity.model.local.CheckoutParent;
import com.posimplicity.model.local.OrderModel;
import com.posimplicity.model.local.Setting;
import com.posimplicity.model.response.other.Validate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Helper {

    public static String convertXmlToJson(String pXmlString) {
        return XML.toJSONObject(pXmlString).toString();
    }

    public static void hideSoftKeyboard(Context pContext, View pView) {
        InputMethodManager imm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pView.getWindowToken(), 0);
    }

    public static void updateSettingPreference(Context pContext, SwitchAdapter pSwitchAdapter, Setting pSetting, boolean pShownText) {
        if (pSwitchAdapter != null) {
            pSwitchAdapter.notifyDataSetChanged();
        }
        AppSharedPrefs.getInstance(pContext).setSetting(pSetting);
        if (pShownText) {
            Toast.makeText(pContext, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getWebBaseUrl(Context pContext) {
        return "http://" + MyPreferences.getMyPreference(PrefrenceKeyConst.STORE_NAME, pContext) + ".posimplicity.biz/";
    }

    public static String removeAllNullString(String pString) {
        pString = pString.replace("null", "");
        pString = pString.replace("null,", "");
        pString = pString.replace(",null,", ",");
        pString = pString.replace(",null", "");
        pString = pString.replace("NULL,", "");
        pString = pString.replace("NULL", "");
        pString = pString.replace(",NULL,", ",");
        pString = pString.replace(",NULL", "");
        return pString;
    }

    public static boolean isBlank(String pText) {
        return pText == null || pText.trim().length() == 0;
    }

    public static LinkedHashMap<String, String> parseQueryParams(String pParams) {
        try {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
            String[] params = pParams.split("&");
            for (String pair : params) {
                int index = pair.indexOf("=");
                hashMap.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"), URLDecoder.decode(pair.substring(index + 1), "UTF-8"));
            }
            return hashMap;
        } catch (Exception ignored) {
            return new LinkedHashMap<>();
        }
    }

    public static String appendQueryParams(@NonNull HashMap<String, String> pParamsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : pParamsMap.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(pParamsMap.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (stringBuilder.length() > 0)
                stringBuilder.append("&");

            stringBuilder.append(key).append("=").append(value);
        }
        return stringBuilder.toString();
    }

    public static String appendQueryParams(String pUrl, HashMap<String, String> pParamsMap) {
        Uri.Builder builder = Uri.parse(pUrl).buildUpon();
        for (String key : pParamsMap.keySet()) {
            builder.appendQueryParameter(key, pParamsMap.get(key));
        }
        return builder.build().toString();
    }

    public static String getOrderId() {
        return "" + System.currentTimeMillis();
    }

    public static String getCurrentDate(boolean pTimeIncluded) {
        if (pTimeIncluded)
            return (String) DateFormat.format("yyyy/MM/dd hh:mm:ss", new Date().getTime());
        else
            return (String) DateFormat.format("yyyy/MM/dd", new Date().getTime());
    }


    private static NetworkInfo getNetworkInfo(Context pContext) {
        ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * @param pContext
     * @return true if device is connected to any active network, false otherwise
     */
    public static boolean isConnected(Context pContext) {
        NetworkInfo ni = getNetworkInfo(pContext);
        return ni != null && ni.isConnected();
    }

    public static int dpToPx(Context pContext, int pDimenInDp) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (pDimenInDp * displayMetrics.density);
    }

    public static ArrayAdapter getStringArrayAdapterInstance(Context pContext, int pLayoutId, int pViewId, List<String> pList) {
        return new ArrayAdapter<String>(pContext, pLayoutId, pViewId, pList) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };
    }

    public static ArrayAdapter getIntArrayAdapterInstance(Context pContext, int pLayoutId, int pViewId, List<Integer> pList) {
        return new ArrayAdapter<Integer>(pContext, pLayoutId, pViewId, pList) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };
    }

    public static boolean verifyCreateOrder(final Context pContext, Validate pValidate, boolean pShownToast) {
        String orderStatusMsg = null;
        if (pValidate != null && pValidate.getSuccess() == Constants.SUCCESS) {
            orderStatusMsg = Constants.MESSAGE_ORDER_PLACED_SUCCESSFULLY;
        } else {
            orderStatusMsg = pValidate == null ? "Unable to Record Last Order" : Helper.isBlank(pValidate.getMsg()) ? "Unable to Record Last Order" : pValidate.getMsg();
        }
        final String finalOrderStatusMsg = orderStatusMsg;
        if (pShownToast) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(pContext.getApplicationContext(), finalOrderStatusMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return Constants.MESSAGE_ORDER_PLACED_SUCCESSFULLY.equalsIgnoreCase(orderStatusMsg);
    }

    /**
     * @hide
     */
    public static String getCreateOrderDetails(OrderModel pOrderModel) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (CheckoutParent checkoutParent : pOrderModel.orderCheckoutItemsList) {
                JSONObject eachProduct = new JSONObject();

                eachProduct.put("product_id", checkoutParent.getProductId());
                eachProduct.put("product_qty", checkoutParent.getProductQty());
                eachProduct.put("product_price", checkoutParent.getProductAndOptionPrice());
                eachProduct.put("product_discount", checkoutParent.getProductAndOptionDiscount());

                JSONArray eachProductOptionArray = new JSONArray();
                List<CheckoutParent.ProductOptions> productOptionsList = checkoutParent.getProductOptions();

                for (CheckoutParent.ProductOptions productOptions : productOptionsList) {
                    JSONObject eachProductSubOption = new JSONObject();
                    StringBuilder subOptionIds = new StringBuilder();
                    List<CheckoutParent.ProductOptions.OptionSubOptions> subOptionsList = productOptions.getOptionSubOptions();
                    for (int index = 0; index < subOptionsList.size(); index++) {
                        subOptionIds.append(subOptionsList.get(index).getSubOptionId());
                        if (index < subOptionsList.size() - 1) {
                            subOptionIds.append(",");
                        }
                    }
                    eachProductSubOption.put("option_id", productOptions.getOptionId());
                    eachProductSubOption.put("sub_option_id", subOptionIds.toString());
                    eachProductOptionArray.put(eachProductSubOption);
                }

                eachProduct.put("productDetails", eachProductOptionArray);
                jsonArray.put(eachProduct);
            }
            jsonObject.put("Result", jsonArray);
        } catch (Exception ignored) {
        }
        return jsonObject.toString();
    }

    public static void startAppAgain(BaseActivity pBaseActivity) {
        Intent intent = new Intent(pBaseActivity, StartingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pBaseActivity.startActivity(intent);
    }

    public static String createSpaceString(int pSpaces) {
        return String.format("%" + pSpaces + "s", " ");
    }
}

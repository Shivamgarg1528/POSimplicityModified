package com.posimplicity.fragment.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.posimplicity.database.server.CustomerTable;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.BuildConfig;
import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.utils.Constants;
import com.utils.JSONObJValidator;

import org.json.JSONObject;

public class WebViewFragment extends BaseFragment {

    private String mUrl;
    private boolean mEnableJavaScript;

    public static WebViewFragment newInstance(String pUrl, boolean pEnableJavaScript) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_KEY_URL, pUrl);
        bundle.putBoolean(Constants.EXTRA_KEY_INTERFACE, pEnableJavaScript);
        WebViewFragment f = new WebViewFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(Constants.EXTRA_KEY_URL);
        mEnableJavaScript = getArguments().getBoolean(Constants.EXTRA_KEY_INTERFACE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        WebView mWebView = new WebView(mBaseActivity);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return mWebView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading Url:-> " + mUrl);
        }
        WebView webView = (WebView) view;
        webView.loadUrl(mUrl);
        if (mEnableJavaScript) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new PosAndroidJsInterface(mBaseActivity), "POS_ADD_CUSTOMER");
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mBaseActivity.showProgressDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBaseActivity.dismissProgressDialog();
            }
        });
    }

    private static class PosAndroidJsInterface {

        private Context mBaseActivity;

        PosAndroidJsInterface(Context mBaseActivity) {
            this.mBaseActivity = mBaseActivity;
        }

        @JavascriptInterface
        public void showErrorMsg() {
            Toast.makeText(mBaseActivity, R.string.string_failed_to_record_data, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void showCustomerInfo(String jsonData) {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String customerId = JSONObJValidator.stringTagValidate(jsonObject, "customer_id", "-1");
                String firstName = JSONObJValidator.stringTagValidate(jsonObject, "firstname", "");
                String lastName = JSONObJValidator.stringTagValidate(jsonObject, "lastname", "");
                String email = JSONObJValidator.stringTagValidate(jsonObject, "email", "");
                String telephone = JSONObJValidator.stringTagValidate(jsonObject, "telephone", "");
                String groupId = JSONObJValidator.stringTagValidate(jsonObject, "group_id", CustomerTable.DEFAULT_GROUP_ID);

                String street = JSONObJValidator.stringFromObj(jsonObject, "street", "NULL");
                String city = JSONObJValidator.stringTagValidate(jsonObject, "city", "NULL");
                String countryId = JSONObJValidator.stringTagValidate(jsonObject, "country_id", "NULL");
                String region = JSONObJValidator.stringTagValidate(jsonObject, "region", "NULL");
                String postcode = JSONObJValidator.stringTagValidate(jsonObject, "postcode", "NULL");

                CustomerParent.Customer customer = new CustomerParent.Customer();
                customer.setCustomerAddress(street + "," + city + "," + countryId + "," + region + "," + postcode);
                customer.setCustomerEmail(email);
                customer.setCustomerFirstName(firstName);
                customer.setCustomerLastName(lastName);
                customer.setCustomerGroupId(groupId);
                customer.setCustomerTelephone(telephone);
                customer.setCustomerId(customerId);
                new CustomerTable(mBaseActivity).insertData(customer);

                Toast.makeText(mBaseActivity, R.string.string_record_data_successfully, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

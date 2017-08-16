package com.posimplicity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.controller.ApisController;
import com.posimplicity.database.server.CustomerTable;
import com.easylibs.http.EasyHttpResponse;
import com.easylibs.listener.EventListener;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.model.response.other.OrderResponse;
import com.posimplicity.R;
import com.utils.Constants;
import com.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class PendingAndReprintActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, EventListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private List<OrderResponse.DetailsBean> mOrderList = new ArrayList<>(0);
    private List<String> mOrderIdList = new ArrayList<>(0);
    private List<CustomerParent.Customer> mCustomerList;
    private ListView mListView;
    private OrderResponse.DetailsBean mSelectedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_reprint);

        mCustomerList = new CustomerTable(this).getAllData();

        List<String> spinnerList = new ArrayList<>(0);
        spinnerList.add(getString(R.string.string_select_any_option));

        boolean mPendingActivity = getIntent().getBooleanExtra(Constants.EXTRA_KEY, false);
        if (mPendingActivity) {
            setupToolBar("Pending Orders", false);
            spinnerList.add("Pending Orders");
        } else {
            setupToolBar("Reprint Orders", false);
            spinnerList.add("Last 10 Orders");
            spinnerList.add("Last Order");
            spinnerList.add("Today's Orders");
        }
        ArrayAdapter arrayAdapter = Helper.getStringArrayAdapterInstance(this, R.layout.spinner_layout, android.R.id.text1, spinnerList);
        Spinner spinner = (Spinner) findViewById(R.id.activity_pending_reprint_spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        mListView = (ListView) findViewById(R.id.activity_pending_reprint_list_view);
        findViewById(R.id.activity_pending_reprint_tv_save).setOnClickListener(this);
    }

    @Override
    protected void onBackTapped() {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            String spinnerItem = (String) parent.getAdapter().getItem(position);
            int caseValue = -1;
            if (spinnerItem.equalsIgnoreCase("Pending Orders")) {
                caseValue = 4;
            } else {
                caseValue = position;
            }
            showProgressDialog();
            ApisController.getOrders(this, caseValue, this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
        dismissProgressDialog();
        OrderResponse orderResponse = ((EasyHttpResponse<OrderResponse>) pEventData).getData();
        if (orderResponse != null) {
            List<OrderResponse.DetailsBean> detailList = orderResponse.getDetailList();
            if (!detailList.isEmpty() && detailList.size() > 0) {
                mOrderList.clear();
                mOrderIdList.clear();
                for (OrderResponse.DetailsBean detailsBean : detailList) {
                    if (!Helper.isBlank(detailsBean.getOrderId())) {
                        String clerkId = detailsBean.getShippingId();
                        String clerkName = "";
                        if (!Helper.isBlank(clerkId)) {
                            for (CustomerParent.Customer customer : mCustomerList) {
                                if (clerkId.equalsIgnoreCase(customer.getCustomerId())) {
                                    clerkName = customer.getCustomerFirstName();
                                    break;
                                }
                            }
                        }
                        mOrderList.add(detailsBean);
                        mOrderIdList.add(detailsBean.getOrderId()
                                .concat("   ").concat(detailsBean.getStatus())
                                .concat("   ").concat(clerkName).toUpperCase());
                    }
                }
                mListView.setAdapter(Helper.getStringArrayAdapterInstance(this, android.R.layout.simple_list_item_single_choice, android.R.id.text1, mOrderIdList));
                mListView.setOnItemClickListener(this);
            } else {
                Toast.makeText(this, R.string.string_no_order_available, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.string_no_order_available, Toast.LENGTH_SHORT).show();
        }
        if (!mOrderIdList.isEmpty())
            findViewById(R.id.activity_pending_reprint_tv_save).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.activity_pending_reprint_tv_save).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedOrder = mOrderList.get(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_pending_reprint_tv_save: {
                if (mSelectedOrder == null) {
                    Toast.makeText(this, R.string.string_please_select_any_order_from_list, Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            }
        }
    }
}

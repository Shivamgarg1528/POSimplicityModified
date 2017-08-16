package com.posimplicity.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.posimplicity.database.server.CustomerTable;
import com.posimplicity.interfaces.OnTransactionAssignListener;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.R;
import com.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class TransactionAssignDialog extends BaseAlert implements View.OnClickListener, TextWatcher {

    private Context mContext;
    private OnTransactionAssignListener mTransactionAssignedListener;

    private TextView mTextViewName;
    private TextView mTextViewAddress;

    private List<CustomerParent.Customer> mCustomerParentList = new ArrayList<>(0);
    private int mIndex;

    public TransactionAssignDialog(@NonNull Context context, OnTransactionAssignListener pOnTransactionAssignedListener) {
        super(context);
        mContext = context;
        mTransactionAssignedListener = pOnTransactionAssignedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_assign_transaction_layout);

        mTextViewName = (TextView) findViewById(R.id.dialog_assign_transaction_layout_tv_name);
        mTextViewAddress = (TextView) findViewById(R.id.dialog_assign_transaction_layout_tv_address);

        ((EditText) findViewById(R.id.dialog_assign_transaction_layout_edt_mobile_no))
                .addTextChangedListener(this);

        findViewById(R.id.dialog_assign_transaction_layout_tv_assign).
                setOnClickListener(this);

        mCustomerParentList = new CustomerTable(mContext).getAllData();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_assign_transaction_layout_tv_assign: {
                mTransactionAssignedListener.onTransactionAssigned(mCustomerParentList.get(mIndex));
                dismiss();
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String mobileNo = s.toString();
        mIndex = getIndexOfCustomerBasedOnMobile(mobileNo);
        if (mIndex != -1) {
            findViewById(R.id.dialog_assign_transaction_layout_tv_assign)
                    .setVisibility(View.VISIBLE);
            CustomerParent.Customer customer = mCustomerParentList.get(mIndex);
            mTextViewAddress.setText(Helper.isBlank(customer.getCustomerAddress()) ? "" : Helper.removeAllNullString(customer.getCustomerAddress()));
            mTextViewName.setText(Helper.isBlank(customer.getFullName()) ? "" : Helper.removeAllNullString(customer.getFullName()));
        } else {
            findViewById(R.id.dialog_assign_transaction_layout_tv_assign)
                    .setVisibility(View.INVISIBLE);
            mTextViewAddress.setText("");
            mTextViewName.setText("");
        }
    }

    private int getIndexOfCustomerBasedOnMobile(String pMobileNo) {
        if (Helper.isBlank(pMobileNo)) {
            return -1;
        }
        for (int index = 0; index < mCustomerParentList.size(); index++) {
            CustomerParent.Customer customer = mCustomerParentList.get(index);
            if (customer != null
                    && !Helper.isBlank(customer.getCustomerTelephone())
                    && customer.getCustomerTelephone().startsWith(pMobileNo)) {
                return index;
            }
        }
        return -1;
    }
}

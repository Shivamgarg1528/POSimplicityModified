package com.posimplicity.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.posimplicity.interfaces.OnTransactionIdCallback;
import com.posimplicity.R;

public class TransactionIdDialog extends BaseAlert {

    private final boolean mShowAdapter;
    private OnTransactionIdCallback mOnTransactionIdCallback;
    private AutoCompleteTextView mAutoCompleteTextView;

    public TransactionIdDialog(Context context, boolean pShowAdapter, OnTransactionIdCallback pOnTransactionIdCallback) {
        super(context);
        this.mShowAdapter = pShowAdapter;
        this.mOnTransactionIdCallback = pOnTransactionIdCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_transaction_id_layout);

        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.dialog_transaction_id_layout_edt_trans_id);

        findViewById(R.id.dialog_transaction_id_layout_tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAutoCompleteTextView.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext().getApplicationContext(), "Please Enter Transaction Id First", Toast.LENGTH_SHORT).show();
                    return;
                }
                mOnTransactionIdCallback.onTransactionIdCallback(mAutoCompleteTextView.getText().toString());
                dismiss();
            }
        });

        if (null != getWindow().getDecorView()) {
            getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }
}

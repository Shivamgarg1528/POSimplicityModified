package com.posimplicity.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.utils.Keypad;
import com.utils.MyStringFormat;
import com.posimplicity.interfaces.OnDiscountApply;
import com.posimplicity.R;

public class DiscountDialog extends BaseAlert implements View.OnClickListener, Keypad.KeypadClick, DialogInterface.OnDismissListener {

    // Discount constants...
    public static final int DISCOUNT_DOLLAR = 0;
    public static final int DISCOUNT_PERCENTAGE = 1;

    @NonNull
    private final Context mContext;
    // Views
    private TextView mTextViewDiscount;

    // Values
    private OnDiscountApply mListener;
    private Keypad mKeypad;


    public DiscountDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void show(OnDiscountApply pListener) {
        this.mListener = pListener;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // keeping parent view for future reference and passing it to keypad class for calls
        View mParentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_discount_layout, null);
        setContentView(mParentView);

        // call this method after setContentView
        mKeypad = new Keypad(mParentView, this, Keypad.keypadArray);
        mKeypad.onBind(0.0f);

        mTextViewDiscount = (TextView) findViewById(R.id.dialog_discounts_layout_tv_discount);
        mTextViewDiscount.setText(MyStringFormat.formatWith2DecimalPlaces(0.0f));

        findViewById(R.id.dialog_discounts_layout_tv_apply_dollar_discount).setOnClickListener(this);
        findViewById(R.id.dialog_discounts_layout_tv_apply_percentage_discount).setOnClickListener(this);
        setOnDismissListener(this);

        ((TextView) findViewById(R.id.dialog_discounts_layout_tv_title))
                .setText("For $ discount please enter the discount as a whole number.\n For example enter 10.00 for $10.00\n\nFor % discount please enter the discount as a whole number.\n For example enter 10.00 for 10%");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_discounts_layout_tv_apply_dollar_discount: {
                mListener.onDiscountApply(DISCOUNT_DOLLAR, Float.parseFloat(mTextViewDiscount.getText().toString()), this);
                break;
            }
            case R.id.dialog_discounts_layout_tv_apply_percentage_discount: {
                mListener.onDiscountApply(DISCOUNT_PERCENTAGE, Float.parseFloat(mTextViewDiscount.getText().toString()), this);
                break;
            }
        }
    }

    @Override
    public void onKeypadClick(String pValue) {
        mTextViewDiscount.setText(pValue);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mKeypad.onUnbind();
    }
}

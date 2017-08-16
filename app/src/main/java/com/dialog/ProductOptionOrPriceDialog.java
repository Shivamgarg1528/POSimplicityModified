package com.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.utils.Keypad;
import com.utils.MyStringFormat;
import com.adapter.recycler.ProductOptionAdapter;
import com.posimplicity.interfaces.OnProductItemModify;
import com.posimplicity.model.local.CheckoutParent;
import com.posimplicity.R;

import java.util.List;

public class ProductOptionOrPriceDialog extends AlertDialog implements View.OnClickListener, Keypad.KeypadClick, DialogInterface.OnDismissListener {

    @NonNull
    private final Context mContext;

    private CheckoutParent mCheckoutParent;
    private OnProductItemModify mListener;
    private Keypad mKeypad;

    // Views
    private TextView mTextViewProductPrice;

    // Values
    private float mProductPrice;

    public ProductOptionOrPriceDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void show(CheckoutParent pCheckoutParent, OnProductItemModify pListener) {
        this.mCheckoutParent = pCheckoutParent;
        this.mListener = pListener;
        this.mProductPrice = pCheckoutParent.getProductPrice();
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // keeping parent view for future reference and passing it to keypad class for calls
        View mParentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_product_option_or_price_layout, null);
        setContentView(mParentView);

        // call this method after setContentView
        mKeypad = new Keypad(mParentView, this, Keypad.keypadArray);
        mKeypad.onBind(mProductPrice);

        mTextViewProductPrice = (TextView) findViewById(R.id.dialog_product_option_or_price_layout_tv_price);
        mTextViewProductPrice.setText(MyStringFormat.formatWith2DecimalPlaces(mProductPrice));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dialog_product_option_or_price_layout_rv_options);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            ProductOptionAdapter mAdapter = new ProductOptionAdapter(mContext, mCheckoutParent.getProductOptions());
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged(findViewById(R.id.dialog_product_option_or_price_layout_rv_empty_tv));
        }

        findViewById(R.id.dialog_product_option_or_price_layout_tv_add_or_modify).setOnClickListener(this);
        setOnDismissListener(this);

        // hide custom price views
        if (mProductPrice > 0) {
            findViewById(R.id.dialog_product_option_or_price_layout_ll_price_parent).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_product_option_or_price_layout_tv_add_or_modify: {
                // if any option is enable on server side
                // then we need to add that option compulsory because it is mandatory...
                if (!mCheckoutParent.getProductOptions().isEmpty()) {
                    iterateOptionAndSubOption(false);
                    iterateOptionAndSubOption(true);
                }
                if (mProductPrice <= 0) {
                    mCheckoutParent.setProductPrice(Float.parseFloat(mTextViewProductPrice.getText().toString()));
                }
                mListener.onItemModified(mCheckoutParent);
                dismiss();
                break;
            }
        }
    }

    private void iterateOptionAndSubOption(boolean pRemoveUnselectedOption) {
        float subOptionPrice = 0.0f;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mCheckoutParent.getProductId());

        for (int index = 0; index < mCheckoutParent.getProductOptions().size(); index++) {
            CheckoutParent.ProductOptions productOption = mCheckoutParent.getProductOptions().get(index);
            List<CheckoutParent.ProductOptions.OptionSubOptions> productSubOptionList = productOption.getOptionSubOptions();
            stringBuilder.append(productOption.getOptionId());
            boolean isAnyItemSelected = false;
            for (int index1 = 0; index1 < productSubOptionList.size(); index1++) {
                CheckoutParent.ProductOptions.OptionSubOptions productSubOption = productSubOptionList.get(index1);
                if (productSubOption.isSubOptionSelected()) {
                    isAnyItemSelected = true;
                    subOptionPrice += productSubOption.getSubOptionPrice();
                    stringBuilder.append(productSubOption.getSubOptionId());
                } else if (pRemoveUnselectedOption) {
                    productSubOptionList.remove(index1);
                    --index1;
                }
            }
            if (productOption.isOptionMandatory() && !isAnyItemSelected && !pRemoveUnselectedOption) {
                if (!productOption.isOptionSelected()) {
                    Toast.makeText(mContext, "Please Add " + productOption.getOptionName(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (pRemoveUnselectedOption) {
            mCheckoutParent.setProductOptionPrice(subOptionPrice);
            mCheckoutParent.setSelectedIds(stringBuilder.toString());
        }
    }

    @Override
    public void onKeypadClick(String pValue) {
        mTextViewProductPrice.setText(pValue);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mKeypad.onUnbind();
    }

}

package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.Beans.CheckOutParentModel;
import com.Beans.ProductModel;
import com.CalculationsTask.AmountCalculation;
import com.posimplicity.database.local.SecurityTable;
import com.Socket.ConvertStringOfJson;
import com.utils.InputCalculation;
import com.utils.MyStringFormat;
import com.utils.SecurityVerification;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.HomeActivity;
import com.posimplicity.R;

public class DiscountDialog extends BaseDialog {

    private TextView discDisplay, headerTextTV;
    private float discountAmt = 0.0f;
    private HomeActivity insActivity;
    private StringBuilder enteredStringBld = null;
    private ProductModel productModel;
    private CheckOutParentModel parent;
    private int currentVisualPopupType;
    private final int ADD_CHAR = 0;
    private final int REM_CHAR = 1;
    public static final int DIALOG_DISCOUNT_TRANSACTION_PERCENTAGE = 0;
    public static final int DIALOG_DISCOUNT_TRANSACTION_DOLLAR = 1;
    public static final int DIALOG_DISCOUNT_ITEMS_PERCENTAGE = 2;
    public static final int DIALOG_DISCOUNT_ITEMS_DOLLAR = 3;

    public DiscountDialog(Context context, int theme, int width, int height, boolean isOutSideTouch, boolean isCancelable, int layoutId) {
        super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
        enteredStringBld = new StringBuilder("000");
    }

    public void show(int dialogVersion, CheckOutParentModel parent) {

        this.currentVisualPopupType = dialogVersion;
        if (parent != null) {
            this.productModel = parent.getProduct();
            this.parent = parent;
        }

        if (Variables.startNewTrans)
            ToastUtils.showOwnToast(mContext, "Add Items First In Cart !!!");
        else {
            boolean isTrue = false;
            switch (currentVisualPopupType) {

                case DIALOG_DISCOUNT_TRANSACTION_PERCENTAGE:

                    if (!Variables.discountDollar)
                        isTrue = true;

                    break;

                case DIALOG_DISCOUNT_TRANSACTION_DOLLAR:

                    if (!Variables.discountPercentage)
                        isTrue = true;
                    break;

                case DIALOG_DISCOUNT_ITEMS_PERCENTAGE:

                    isTrue = true;

                    break;
                case DIALOG_DISCOUNT_ITEMS_DOLLAR:

                    isTrue = true;

                    break;

                default:
                    break;
            }

            if (!isTrue)
                ToastUtils.showOwnToast(mContext, "Not Applicable");
            else {
                switch (currentVisualPopupType) {

                    case DIALOG_DISCOUNT_TRANSACTION_PERCENTAGE:
                        new SecurityVerification(mContext, SecurityTable.Settings_Transaction_Percentage_Discount).discountFunctionChecking(this);
                        break;

                    case DIALOG_DISCOUNT_TRANSACTION_DOLLAR:
                        new SecurityVerification(mContext, SecurityTable.Settings_Transaction_Dollar_Discount).discountFunctionChecking(this);
                        break;

                    case DIALOG_DISCOUNT_ITEMS_PERCENTAGE:
                        new SecurityVerification(mContext, SecurityTable.Settings_Item_Percentage_Discount).discountFunctionChecking(this);
                        break;

                    case DIALOG_DISCOUNT_ITEMS_DOLLAR:
                        new SecurityVerification(mContext, SecurityTable.Settings_Item_Dollar_Discount).discountFunctionChecking(this);
                        break;

                    default:
                        break;
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        discDisplay = findViewByIdAndCast(R.id.Dailog_Discount_TV_Display);
        headerTextTV = findViewByIdAndCast(R.id.Dailog_Discount_TV_Header);
        insActivity = HomeActivity.localInstance;
        discDisplay.setText("0.00");

        if (!(currentVisualPopupType % 2 == 0))
            headerTextTV.setText("For $ discount please enter the discount as a whole number.\n For example enter $10.00");
        else
            headerTextTV.setText("For % discount please enter the discount as a whole number.\n For example enter 10.00 for 10%");
    }

    private void setTextOnView(String input, int keyPurpose, TextView view) {
        if (keyPurpose == ADD_CHAR)
            view.setText(InputCalculation.onKeyDown(enteredStringBld, input, false));
        else if (keyPurpose == REM_CHAR)
            view.setText(InputCalculation.onKeyUp(enteredStringBld));
        discountAmt = Float.parseFloat(discDisplay.getText().toString());
    }


    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.b1:
                setTextOnView(KEY_ONE, ADD_CHAR, discDisplay);
                break;

            case R.id.b2:
                setTextOnView(KEY_TWO, ADD_CHAR, discDisplay);
                break;

            case R.id.b3:
                setTextOnView(KEY_THREE, ADD_CHAR, discDisplay);
                break;

            case R.id.b4:
                setTextOnView(KEY_FOUR, ADD_CHAR, discDisplay);
                break;

            case R.id.b5:
                setTextOnView(KEY_FIVE, ADD_CHAR, discDisplay);
                break;

            case R.id.b6:
                setTextOnView(KEY_SIX, ADD_CHAR, discDisplay);
                break;

            case R.id.b7:
                setTextOnView(KEY_SEVEN, ADD_CHAR, discDisplay);
                break;

            case R.id.b8:
                setTextOnView(KEY_EIGHT, ADD_CHAR, discDisplay);
                break;

            case R.id.b9:
                setTextOnView(KEY_NINE, ADD_CHAR, discDisplay);
                break;

            case R.id.b0:
                setTextOnView(KEY_ZERO, ADD_CHAR, discDisplay);
                break;

            case R.id.b00:
                setTextOnView(KEY_DOUBLE_ZERO, ADD_CHAR, discDisplay);
                break;

            case R.id.bs:
                setTextOnView("", REM_CHAR, discDisplay);
                break;

            case R.id.ok_dia:

                if (discountAmt == 0.0)
                    ToastUtils.showOwnToast(mContext, "Discount Can't Be Zero");
                else {
                    switch (currentVisualPopupType) {

                        case DIALOG_DISCOUNT_TRANSACTION_PERCENTAGE:

                            if (discountAmt <= 100.0) {
                                Variables.discountInPercentage = discountAmt;
                                Variables.discountApplied = true;
                                Variables.discountPercentage = true;
                                Variables.discountDollar = false;
                                insActivity.calculateSubTotalEachTime();
                                dismiss();
                                new ConvertStringOfJson(mContext).onOrderDiscount("" + discountAmt, ConvertStringOfJson.ORDER_DISCOUNT_PER);
                            } else
                                ToastUtils.showOwnToast(mContext, "Discount can't be given more than 100%");
                            break;

                        case DIALOG_DISCOUNT_TRANSACTION_DOLLAR:

                            String itemstotalPrice = new AmountCalculation(mContext).getItemsAmt();
                            String discountonItems = new AmountCalculation(mContext).getItemsDiscountAmt();

                            if (discountAmt <= Float.parseFloat(itemstotalPrice) - Float.parseFloat(discountonItems)) {

                                Variables.discountInDollar = discountAmt;
                                Variables.discountApplied = true;
                                Variables.discountDollar = true;
                                Variables.discountPercentage = false;
                                insActivity.calculateSubTotalEachTime();
                                dismiss();
                                new ConvertStringOfJson(mContext).onOrderDiscount("" + discountAmt, ConvertStringOfJson.ORDER_DISCOUNT_DOLL);
                            } else
                                ToastUtils.showOwnToast(mContext, "Discount can't be given more than SubToatal Amount");
                            break;

                        case DIALOG_DISCOUNT_ITEMS_PERCENTAGE:
                            if (discountAmt <= 100.0) {

                                String productAmount = productModel.getProductCalAmount();
                                String discountConvertedAmt = String.valueOf((Float.parseFloat(productAmount) * discountAmt) / 100.0f);
                                productModel.setProductDisAmount(MyStringFormat.onStringFormat(discountConvertedAmt));
                                insActivity.myAdapter.notifyDataSetChanged();
                                insActivity.calculateSubTotalEachTime();
                                dismiss();
                                new ConvertStringOfJson(mContext).onProductModification(parent, ConvertStringOfJson.SOCKET_DISCOUNT);
                            } else
                                ToastUtils.showOwnToast(mContext, "Discount can't be given more than 100% of Item Amount");
                            break;

                        case DIALOG_DISCOUNT_ITEMS_DOLLAR:

                            String productAmount = productModel.getProductCalAmount();

                            if (discountAmt <= Float.parseFloat(productAmount)) {
                                productModel.setProductDisAmount(MyStringFormat.formatWith2DecimalPlaces(discountAmt));
                                insActivity.myAdapter.notifyDataSetChanged();
                                insActivity.calculateSubTotalEachTime();
                                dismiss();
                                new ConvertStringOfJson(mContext).onProductModification(parent, ConvertStringOfJson.SOCKET_DISCOUNT);
                            } else
                                ToastUtils.showOwnToast(mContext, "Discount can't be more then Item Amount");

                            break;

                        default:
                            break;
                    }
                }
                break;
        }
    }
}

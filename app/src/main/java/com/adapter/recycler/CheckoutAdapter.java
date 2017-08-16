package com.adapter.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.posimplicity.interfaces.OnDiscountApply;
import com.posimplicity.interfaces.OnItemUpdate;
import com.posimplicity.model.local.CheckoutParent;
import com.posimplicity.R;
import com.posimplicity.dialog.DiscountDialog;
import com.utils.Constants;
import com.utils.POSApp;
import com.utils.MyStringFormat;

import java.util.List;


public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CheckoutParent> mDataList;
    private OnItemUpdate mOnItemUpdate;

    public CheckoutAdapter(Context pContext, List<CheckoutParent> pDataList, OnItemUpdate pOnItemUpdate) {
        this.mContext = pContext;
        this.mDataList = pDataList;
        this.mOnItemUpdate = pOnItemUpdate;
    }

    @Override
    public CheckoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_adapter_row_layout, parent, false);
        return new CheckoutAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CheckoutAdapter.ViewHolder holder, int position) {
        CheckoutParent checkoutParent = mDataList.get(position);
        holder.mTextViewItemName.setText(checkoutParent.getProductName());
        holder.mTextViewItemQty.setText(String.valueOf(checkoutParent.getProductQty()));
        holder.mTextViewItemPrice.setText(MyStringFormat.formatWith2DecimalPlaces(checkoutParent.getFinalPrice()));
        holder.mViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                mOnItemUpdate.onUpdate(null);

                if (mDataList.isEmpty()) {
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.ACTION_CLEAR));
                }
            }
        });
        holder.mTextViewQtyIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutParent checkoutParent = mDataList.get(holder.getAdapterPosition());
                boolean success = checkoutParent.increaseQtyByOneIfPossible();
                if (success) {
                    notifyItemChanged(holder.getAdapterPosition());
                    mOnItemUpdate.onUpdate(null);
                } else
                    Toast.makeText(mContext, "Qty Can't Increase More", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mTextViewQtyDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutParent checkoutParent = mDataList.get(holder.getAdapterPosition());
                boolean success = checkoutParent.decreaseQtyByOneIfPossible();
                if (success) {
                    notifyItemChanged(holder.getAdapterPosition());
                    mOnItemUpdate.onUpdate(null);
                } else
                    Toast.makeText(mContext, "Qty Can't Decrease More", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckoutParent checkoutParent = mDataList.get(holder.getAdapterPosition());
                PopupMenu popupMenu = new PopupMenu(mContext, holder.mParentView);
                popupMenu.inflate(R.menu.checkout_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.popup_checkout_menu_apply_discount:
                                if (POSApp.getInstance().mOrderModel.orderAmountPaidModel.getAmountPaid() > 0) {
                                    Toast.makeText(mContext, R.string.string_you_can_not_apply_discount_now, Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                                new DiscountDialog(mContext).show(new OnDiscountApply() {
                                    @Override
                                    public void onDiscountApply(int pDiscountType, float pValue, AlertDialog pAlertDialog) {
                                        switch (pDiscountType) {
                                            case DiscountDialog.DISCOUNT_DOLLAR: {
                                                if (pValue > checkoutParent.getProductAndOptionPrice()) {
                                                    Toast.makeText(mContext, R.string.string_value_must_be_less_than_product_price, Toast.LENGTH_SHORT).show();
                                                } else if (pValue == 0) {
                                                    Toast.makeText(mContext, R.string.string_value_must_be_greater_than_0, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    checkoutParent.setProductDisApplied(true);
                                                    checkoutParent.setProductDisPercentage(0.0f);
                                                    checkoutParent.setProductDisDollar(pValue);
                                                    pAlertDialog.dismiss();
                                                    notifyItemChanged(holder.getAdapterPosition());
                                                    mOnItemUpdate.onUpdate(null);
                                                    Toast.makeText(mContext, R.string.string_discount_applied_successfully, Toast.LENGTH_SHORT).show();
                                                }
                                                break;
                                            }
                                            case DiscountDialog.DISCOUNT_PERCENTAGE: {
                                                if (pValue <= 0 || pValue > 100) {
                                                    Toast.makeText(mContext, R.string.string_value_must_be_greater_than_0_and_less_than_equals_to_100, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    checkoutParent.setProductDisApplied(true);
                                                    checkoutParent.setProductDisPercentage(pValue);
                                                    checkoutParent.setProductDisDollar(0.0f);
                                                    pAlertDialog.dismiss();
                                                    notifyItemChanged(holder.getAdapterPosition());
                                                    mOnItemUpdate.onUpdate(null);
                                                    Toast.makeText(mContext, R.string.string_discount_applied_successfully, Toast.LENGTH_SHORT).show();
                                                }
                                                break;
                                            }
                                        }
                                    }
                                });
                                return true;

                            case R.id.popup_checkout_menu_clear_discount: {

                                if (POSApp.getInstance().mOrderModel.orderAmountPaidModel.getAmountPaid() > 0) {
                                    Toast.makeText(mContext, R.string.string_you_can_not_clear_discount_now, Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                                if (checkoutParent.isProductDisApplied()) {
                                    checkoutParent.setProductDisPercentage(0.0f);
                                    checkoutParent.setProductDisDollar(0.0f);
                                    notifyItemChanged(holder.getAdapterPosition());
                                    mOnItemUpdate.onUpdate(null);
                                    Toast.makeText(mContext, R.string.string_discount_cleared_successfully, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, R.string.string_discount_not_applied_yet, Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View mParentView;
        private View mViewDelete;
        private TextView mTextViewItemName;
        private TextView mTextViewItemQty;
        private TextView mTextViewItemPrice;
        private TextView mTextViewQtyIncrease;
        private TextView mTextViewQtyDecrease;


        ViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mViewDelete = mParentView.findViewById(R.id.checkout_adapter_row_layout_iv_item_delete);
            mTextViewItemName = (TextView) mParentView.findViewById(R.id.checkout_adapter_row_layout_iv_item_name);
            mTextViewItemQty = (TextView) mParentView.findViewById(R.id.checkout_adapter_row_layout_iv_item_qty);
            mTextViewItemPrice = (TextView) mParentView.findViewById(R.id.checkout_adapter_row_layout_iv_item_price);
            mTextViewQtyIncrease = (TextView) mParentView.findViewById(R.id.checkout_adapter_row_layout_iv_item_qty_increase);
            mTextViewQtyDecrease = (TextView) mParentView.findViewById(R.id.checkout_adapter_row_layout_iv_item_qty_decrease);
        }
    }
}

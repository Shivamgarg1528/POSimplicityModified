package com.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.utils.MyStringFormat;
import com.posimplicity.model.local.CheckoutParent;
import com.posimplicity.R;

import java.util.List;

public class ProductSubOptionAdapter extends RecyclerView.Adapter<ProductSubOptionAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CheckoutParent.ProductOptions.OptionSubOptions> mDataList;

    public ProductSubOptionAdapter(Context pContext, List<CheckoutParent.ProductOptions.OptionSubOptions> pProductSubOptionsList) {
        this.mContext = pContext;
        this.mDataList = pProductSubOptionsList;
    }

    @Override
    public ProductSubOptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_sub_option_adapter_row_layout, parent, false);
        return new ProductSubOptionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductSubOptionAdapter.ViewHolder holder, int position) {
        CheckoutParent.ProductOptions.OptionSubOptions subOption = mDataList.get(position);
        holder.mTextViewSubOptionName.setText(subOption.getSubOptionName());
        holder.mTextViewSubOptionPrice.setText(MyStringFormat.formatWith2DecimalPlaces(subOption.getSubOptionPrice()));
        if (subOption.isSubOptionSelected()) {
            holder.mImageViewCheck.setImageResource(R.drawable.ic_check_circle);
        } else {
            holder.mImageViewCheck.setImageResource(R.drawable.ic_uncheck_radio);
        }
        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutParent.ProductOptions.OptionSubOptions productSubOptions = mDataList.get(holder.getAdapterPosition());
                productSubOptions.setIsSubOptionSelected(!productSubOptions.isSubOptionSelected());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mParentView;
        TextView mTextViewSubOptionName, mTextViewSubOptionPrice;
        ImageView mImageViewCheck;

        ViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mTextViewSubOptionName = (TextView) mParentView.findViewById(R.id.product_sub_option_adapter_row_layout_tv_sub_option_name);
            mTextViewSubOptionPrice = (TextView) mParentView.findViewById(R.id.product_sub_option_adapter_row_layout_tv_sub_option_price);
            mImageViewCheck = (ImageView) mParentView.findViewById(R.id.product_sub_option_adapter_row_layout_iv_arrow);
        }
    }
}

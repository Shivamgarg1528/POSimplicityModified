package com.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.posimplicity.model.local.CheckoutParent;
import com.posimplicity.R;

import java.util.List;

public class ProductOptionAdapter extends RecyclerView.Adapter<ProductOptionAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CheckoutParent.ProductOptions> mDataList;

    public ProductOptionAdapter(Context pContext, List<CheckoutParent.ProductOptions> pProductOptionsList) {
        this.mContext = pContext;
        this.mDataList = pProductOptionsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_option_adapter_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CheckoutParent.ProductOptions productOption = mDataList.get(position);
        holder.mTextViewOptionName.setText(productOption.getOptionName());
        if (productOption.isOptionSelected()) {
            holder.mRecyclerView.setVisibility(View.VISIBLE);
            holder.mImageViewArrow.setImageResource(R.drawable.ic_arrow_drop_up);
        } else {
            holder.mRecyclerView.setVisibility(View.GONE);
            holder.mImageViewArrow.setImageResource(R.drawable.ic_arrow_drop_down);
        }
        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutParent.ProductOptions productOptions = mDataList.get(holder.getAdapterPosition());
                productOptions.setOptionSelected(!productOptions.isOptionSelected());
                holder.mRecyclerView.setAdapter(new ProductSubOptionAdapter(mContext, productOptions.getOptionSubOptions()));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void notifyDataSetChanged(View pEmptyView) {
        if (mDataList.isEmpty())
            pEmptyView.setVisibility(View.VISIBLE);
        else
            pEmptyView.setVisibility(View.INVISIBLE);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mParentView;
        TextView mTextViewOptionName;
        ImageView mImageViewArrow;
        RecyclerView mRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mTextViewOptionName = (TextView) mParentView.findViewById(R.id.product_option_adapter_row_layout_tv_option_name);
            mImageViewArrow = (ImageView) mParentView.findViewById(R.id.product_option_adapter_row_layout_iv_arrow);

            mRecyclerView = (RecyclerView) mParentView.findViewById(R.id.product_option_adapter_row_layout_rv);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}

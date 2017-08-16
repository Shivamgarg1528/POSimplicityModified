package com.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.posimplicity.model.response.api.ProductParent;
import com.posimplicity.R;
import com.posimplicity.activity.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductParent.Product> dataList;

    public ProductAdapter(List<ProductParent.Product> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ProductParent.Product product = dataList.get(position);
        if (product != null) {
            holder.mTextViewProductName.setVisibility(View.VISIBLE);
            holder.mImageViewProductImage.setVisibility(View.VISIBLE);
            if (product.getProductImageShown() == 1) {
                Picasso
                        .with(holder.mImageViewProductImage.getContext())
                        .load(product.getProductImage())
                        .into(holder.mImageViewProductImage);
                holder.mTextViewProductName.setVisibility(View.GONE);
                holder.mImageViewProductImage.setVisibility(View.VISIBLE);
            } else {
                holder.mImageViewProductImage.setVisibility(View.GONE);
                holder.mTextViewProductName.setText(product.getProductName());
                holder.mTextViewProductName.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mTextViewProductName.setVisibility(View.GONE);
            holder.mImageViewProductImage.setVisibility(View.GONE);
        }
        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductParent.Product tappedProduct = dataList.get(holder.getAdapterPosition());
                if (tappedProduct != null) {
                    HomeActivity homeActivity = (HomeActivity) holder.mParentView.getContext();
                    homeActivity.productButtonTapped(tappedProduct);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View mParentView;
        private TextView mTextViewProductName;
        private ImageView mImageViewProductImage;

        ViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mTextViewProductName = (TextView) mParentView.findViewById(R.id.product_adapter_tv_product_name);
            mImageViewProductImage = (ImageView) mParentView.findViewById(R.id.product_adapter_iv_product_image);
        }
    }
}

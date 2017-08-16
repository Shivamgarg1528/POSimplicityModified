package com.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.posimplicity.model.response.api.CategoryParent;
import com.posimplicity.R;
import com.posimplicity.activity.HomeActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryParent.Category> dataList;

    public CategoryAdapter(List<CategoryParent.Category> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CategoryParent.Category category = dataList.get(position);
        holder.mTxtViewCategoryName.setText(category.getDeptName());
        holder.mTxtViewCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryParent.Category category = dataList.get(holder.getAdapterPosition());
                HomeActivity homeActivity = (HomeActivity) holder.mTxtViewCategoryName.getContext();
                homeActivity.refreshProductListBasedOnCategoryId(category.getDeptId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtViewCategoryName;

        ViewHolder(View itemView) {
            super(itemView);
            mTxtViewCategoryName = (TextView) itemView.findViewById(R.id.category_adapter_row_layout_tv_category_name);
        }
    }
}

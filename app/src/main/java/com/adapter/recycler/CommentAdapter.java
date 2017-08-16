package com.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.posimplicity.model.local.CommentModel;
import com.posimplicity.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CommentModel> mDataList;

    public CommentAdapter(Context pContext, List<CommentModel> pProductSubOptionsList) {
        this.mContext = pContext;
        this.mDataList = pProductSubOptionsList;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_adapter_row_layout, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, int position) {
        CommentModel subOption = mDataList.get(position);
        holder.mTextViewComment.setText(subOption.getCommentString());
        if (subOption.isCommentSelected()) {
            holder.mImageViewCheck.setImageResource(R.drawable.ic_check_circle);
        } else {
            holder.mImageViewCheck.setImageResource(R.drawable.ic_uncheck_radio);
        }
        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentModel productSubOptions = mDataList.get(holder.getAdapterPosition());
                productSubOptions.setCommentSelected(!productSubOptions.isCommentSelected());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mParentView;
        TextView mTextViewComment;
        ImageView mImageViewCheck;

        ViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mTextViewComment = (TextView) mParentView.findViewById(R.id.comment_adapter_row_layout_tv_sub_option_name);
            mImageViewCheck = (ImageView) mParentView.findViewById(R.id.comment_adapter_row_layout_iv_arrow);
        }
    }
}

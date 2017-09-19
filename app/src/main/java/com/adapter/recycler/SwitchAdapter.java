package com.adapter.recycler;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.posimplicity.R;
import com.posimplicity.dialog.AlertHelper;
import com.posimplicity.interfaces.OnSwitchOnOff;
import com.posimplicity.model.local.Detail;

import java.util.List;

public class SwitchAdapter extends RecyclerView.Adapter<SwitchAdapter.ViewHolder> {

    private List<Detail> mDataList;
    private OnSwitchOnOff mOnSwitchListener;
    private boolean mExcludeLastItem;

    public SwitchAdapter(List<Detail> pDataList, OnSwitchOnOff pOnSwitchListener, boolean pExcludeLastItem) {
        this.mDataList = pDataList;
        this.mOnSwitchListener = pOnSwitchListener;
        this.mExcludeLastItem = pExcludeLastItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.switch_adapter_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Detail detail = mDataList.get(position);
        if (detail.isEnable())
            holder.mImageViewState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_switch_enable, 0);
        else
            holder.mImageViewState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_switch_disable, 0);

        holder.mImageViewState.setText(detail.getName());
        holder.mImageViewState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Detail detail = mDataList.get(holder.getAdapterPosition());
                if (detail.isConfirmationPopup()) {
                    String disableOrEnable = detail.isEnable() ? "Disable" : "Enable";
                    String message = String.format("Are You Sure You Want To %s %s ?", disableOrEnable, detail.getName());
                    Context localContext = holder.mImageViewState.getContext();
                    AlertHelper.showAlertDialog(localContext, message, localContext.getString(R.string.string_yes), localContext.getString(R.string.string_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                mOnSwitchListener.onSwitchChange(holder.getAdapterPosition(), mDataList, detail, SwitchAdapter.this);
                            } else {
                                notifyDataSetChanged();
                            }
                        }
                    });
                } else {
                    mOnSwitchListener.onSwitchChange(holder.getAdapterPosition(), mDataList, detail, SwitchAdapter.this);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() > 0) {
            return mExcludeLastItem ? mDataList.size() - 1 : mDataList.size();
        }
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mImageViewState;

        ViewHolder(View v) {
            super(v);
            mImageViewState = (TextView) itemView.findViewById(R.id.switch_adapter_iv_state);
        }
    }
}

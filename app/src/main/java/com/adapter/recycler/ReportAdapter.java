package com.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Beans.ReportModel;
import com.posimplicity.interfaces.OnReportClick;
import com.posimplicity.R;
import com.posimplicity.fragment.report.ReportFragment;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<ReportModel> mDataList;
    private OnReportClick mOnReportClick;

    public ReportAdapter(List<ReportModel> pDataList, ReportFragment pOnReportClick) {
        this.mDataList = pDataList;
        this.mOnReportClick = pOnReportClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_adapter_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ReportModel reportModel = mDataList.get(position);
        holder.mTxtViewName.setText(reportModel.getName());
        holder.mTxtViewValue.setText(reportModel.getValue());

        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportModel reportModel = mDataList.get(holder.getAdapterPosition());
                mOnReportClick.onReportClick(reportModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtViewName;
        private TextView mTxtViewValue;
        private View mParentView;

        ViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mTxtViewName = (TextView) itemView.findViewById(R.id.report_adapter_row_layout_tv_name);
            mTxtViewValue = (TextView) itemView.findViewById(R.id.report_adapter_row_layout_tv_value);
        }
    }
}

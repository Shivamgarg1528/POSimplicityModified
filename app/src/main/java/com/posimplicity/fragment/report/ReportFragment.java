package com.posimplicity.fragment.report;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Beans.ReportModel;
import com.adapter.recycler.ReportAdapter;
import com.posimplicity.database.local.PosReportTable;
import com.posimplicity.interfaces.OnReportClick;
import com.posimplicity.R;
import com.posimplicity.activity.ChartActivity;
import com.posimplicity.dialog.AlertHelper;
import com.posimplicity.fragment.base.ReportBaseFragment;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyStringFormat;

import java.util.List;


public class ReportFragment extends ReportBaseFragment implements View.OnClickListener, OnReportClick {

    private String mReportName;
    private ReportAdapter mReportAdapter;

    public static ReportFragment newInstance(String pReportName) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_KEY, pReportName);

        ReportFragment reportFragment = new ReportFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportName = getArguments().getString(Constants.EXTRA_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUpdatedList(mReportName);

        view.findViewById(R.id.fragment_report_tv_clear).setOnClickListener(this);
        view.findViewById(R.id.fragment_report_tv_chart).setOnClickListener(this);
        view.findViewById(R.id.fragment_report_tv_print).setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_report_recycler_view);
        recyclerView.setHasFixedSize(true);
        mReportAdapter = new ReportAdapter(mDataList, this);
        recyclerView.setAdapter(mReportAdapter);
    }

    @Override
    public void onPageChanged(int pIndex) {
    }

    @Override
    public void onReportClick(ReportModel pReportParent) {

        List<String> payoutDetails = mPosReportTable.getListBasedOnColumnName(pReportParent.getName(), mReportName, Helper.getCurrentDate(false));
        List<String> descriptionDetails = mPosReportTable.getListBasedOnColumnName(PosReportTable.DESCRIPTION, mReportName, Helper.getCurrentDate(false));
        StringBuilder stringBuilder = new StringBuilder();
        if (!payoutDetails.isEmpty() && pReportParent.isClickable()) {
            for (int i = 0; i < payoutDetails.size(); i++) {

                stringBuilder.append(i + 1);
                stringBuilder.append("- ");
                stringBuilder.append(MyStringFormat.onStringFormat(payoutDetails.get(i)));
                stringBuilder.append(" : ");
                stringBuilder.append(descriptionDetails.get(i));

                if (i < payoutDetails.size() - 1) {
                    stringBuilder.append("\n");
                }
            }
            AlertHelper.getAlertDialog(mBaseActivity, stringBuilder.toString(), getString(R.string.string_ok), null, null);
        } else {
            Toast.makeText(mBaseActivity, String.format(getString(R.string.string_no_details_available_for), pReportParent.getName()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_report_tv_clear: {
                AlertHelper.getAlertDialog(mBaseActivity, String.format(getString(R.string.string_are_you_sure_you_want_to_clear) + " ?", mReportName), getString(R.string.string_yes), getString(R.string.string_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            boolean isRecordDeleted = mPosReportTable.deleteReport(mReportName);
                            if (isRecordDeleted) {
                                getUpdatedList(mReportName);
                                mReportAdapter.notifyDataSetChanged();
                            }
                            Toast.makeText(mBaseActivity, String.format(getString(R.string.string_report_cleared), mReportName), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }

            case R.id.fragment_report_tv_chart: {
                Intent intent = new Intent(mBaseActivity, ChartActivity.class);
                intent.putExtra(Constants.EXTRA_KEY, mReportParent);
                startActivity(intent);
                break;
            }

            case R.id.fragment_report_tv_print: {
                break;
            }
        }
    }
}

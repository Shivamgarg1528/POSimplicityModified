package com.posimplicity.fragment.function;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.adapter.recycler.CommentAdapter;
import com.posimplicity.database.local.CommentTable;
import com.posimplicity.database.local.PosReportTable;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.model.local.CommentModel;
import com.posimplicity.model.local.ReportParent;
import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.utils.Constants;
import com.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class PayoutFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String LOTTERY_PAY_OUT = "Lottery Pay Out";
    private static final String EXPENSE_PAY_OUT = "Expense Pay Out";
    private static final String SUPPLIES_PAY_OUT = "Supplies Pay Out";
    private static final String PRODUCT_LIST_PURCHASE = "ProductList Purchase";
    private static final String OTHER_PAY_OUT = "Other Pay Out";
    private static final String TIP_PAY_OUT = "Tip Pay Out";
    private static final String MANUAL_CASH_REFUND = "Manual Cash Refund";

    private List<String> mListPayout = new ArrayList<>(7);
    private List<CommentModel> mListCommentModel = new ArrayList<>(0);

    private EditText mEdtPayAmount;
    private EditText mEdtPayDes;

    private int mSelectedIndex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListPayout.add(LOTTERY_PAY_OUT);
        mListPayout.add(EXPENSE_PAY_OUT);
        mListPayout.add(SUPPLIES_PAY_OUT);
        mListPayout.add(OTHER_PAY_OUT);
        mListPayout.add(TIP_PAY_OUT);
        mListPayout.add(PRODUCT_LIST_PURCHASE);
        mListPayout.add(MANUAL_CASH_REFUND);

        mListCommentModel.addAll(new CommentTable(mBaseActivity).getAllData(CommentTable.COMMENT_PAYOUT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_function_payout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter arrayAdapter = Helper.getStringArrayAdapterInstance(mBaseActivity, R.layout.spinner_layout, android.R.id.text1, mListPayout);
        Spinner spinner = (Spinner) view.findViewById(R.id.fragment_function_payout_spinner_payouts_name);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_function_recycler_view);
        recyclerView.setAdapter(new CommentAdapter(mBaseActivity, mListCommentModel));

        if (recyclerView.getAdapter().getItemCount() <= 0)
            view.findViewById(R.id.fragment_function_payout_tv_recycler_title).setVisibility(View.GONE);

        view.findViewById(R.id.fragment_function_payout_tv_save).setOnClickListener(this);

        mEdtPayAmount = (EditText) view.findViewById(R.id.fragment_function_payout_edt_pay_amount);
        mEdtPayDes = (EditText) view.findViewById(R.id.fragment_function_payout_edt_pay_description);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedIndex = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_function_payout_tv_save: {
                if (Helper.isBlank(mEdtPayAmount.getText().toString())) {
                    Toast.makeText(mBaseActivity, R.string.string_please_enter_pay_amount_first, Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder payoutDescription = new StringBuilder();
                if (!mListCommentModel.isEmpty()) {
                    for (CommentModel commentModel : mListCommentModel) {
                        if (commentModel.isCommentSelected()) {
                            payoutDescription.append(commentModel.getCommentString())
                                    .append(",");
                        }
                    }
                }
                if (!Helper.isBlank(mEdtPayDes.getText().toString())) {
                    payoutDescription.append(mEdtPayDes.getText().toString());
                } else if (payoutDescription.length() == 0) {
                    payoutDescription.append(ReportsTable.DEFAULT_DESCRIPTION);
                } else
                    payoutDescription.deleteCharAt(payoutDescription.length() - 1);

                float payAmount = Float.parseFloat(mEdtPayAmount.getText().toString());
                String selectedPayout = mListPayout.get(mSelectedIndex);

                ReportParent reportParent = new ReportParent();
                reportParent.setRefundStatus(Constants.REFUND_NO);
                reportParent.setSaveState(Constants.ORDER_SUCCESS);
                reportParent.setDescription(payoutDescription.toString());
                reportParent.setTransTime(Helper.getCurrentDate(false));
                reportParent.setPayoutType(selectedPayout);

                switch (selectedPayout) {

                    case LOTTERY_PAY_OUT: {
                        reportParent.setLotteryAmount(payAmount);
                        break;
                    }
                    case EXPENSE_PAY_OUT: {
                        reportParent.setExpensesAmount(payAmount);
                        break;
                    }
                    case SUPPLIES_PAY_OUT: {
                        reportParent.setSuppliesAmount(payAmount);
                        break;
                    }
                    case PRODUCT_LIST_PURCHASE: {
                        reportParent.setProductListAmount(payAmount);
                        break;
                    }
                    case OTHER_PAY_OUT: {
                        reportParent.setOtherAmount(payAmount);
                        break;
                    }
                    case TIP_PAY_OUT: {
                        reportParent.setTipPayAmount(payAmount);
                        break;
                    }
                    case MANUAL_CASH_REFUND: {
                        reportParent.setManualCashRefund(payAmount);
                        break;
                    }
                }

                PosReportTable posReportTable = new PosReportTable(mBaseActivity);
                reportParent.setReportName(Constants.DAILY_REPORT);
                posReportTable.insertData(reportParent);

                reportParent.setReportName(Constants.SHIFT_REPORT);
                posReportTable.insertData(reportParent);

                Toast.makeText(mBaseActivity, R.string.string_payout_saved, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}

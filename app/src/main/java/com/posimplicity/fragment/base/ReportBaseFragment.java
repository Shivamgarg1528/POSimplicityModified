package com.posimplicity.fragment.base;

import com.Beans.ReportModel;
import com.posimplicity.database.local.PosReportTable;
import com.posimplicity.model.local.ReportParent;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.MyStringFormat;

import java.util.ArrayList;
import java.util.List;

public abstract class ReportBaseFragment extends BaseFragment {

    protected List<ReportModel> mDataList = new ArrayList<>(0);
    protected ReportParent mReportParent;
    protected PosReportTable mPosReportTable;

    public void getUpdatedList(String pReportName) {

        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }

        String transTime = Helper.getCurrentDate(false);
        mPosReportTable = new PosReportTable(mBaseActivity);
        mReportParent = mPosReportTable.getReportInfoFromName(transTime, pReportName);
        mReportParent.setTransactionId("");
        mReportParent.setManuallyRecOrders(mPosReportTable.getSumOfGivenSaveState(transTime, pReportName, Constants.MANUAL_ENTRY));
        mReportParent.setNoInternetOrders(mPosReportTable.getSumOfGivenSaveState(transTime, pReportName, Constants.ORDER_FAIL));

        mDataList.add(new ReportModel(PosReportTable.SUB_TOTAL_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getSubtotalAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.DISCOUNT_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getDiscountAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.TAX_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getTaxAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.TOTAL_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getTotalAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.CASH_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getCashAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.CREDIT_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getCreditAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.CHECK_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getCheckAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.CUSTOM_1_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getCustom1Amount()), false));
        mDataList.add(new ReportModel(PosReportTable.CUSTOM_2_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getCustom2Amount()), false));
        mDataList.add(new ReportModel(PosReportTable.GIFT_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getGiftAmount()), false));
        mDataList.add(new ReportModel(PosReportTable.REWARDS_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getRewardAmount()), false));

        mDataList.add(new ReportModel(PosReportTable.LOTTERY_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getLotteryAmount()), true));
        mDataList.add(new ReportModel(PosReportTable.EXPENSES_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getExpensesAmount()), true));
        mDataList.add(new ReportModel(PosReportTable.SUPPLIES_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getSuppliesAmount()), true));
        mDataList.add(new ReportModel(PosReportTable.PRODUCT_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getProductListAmount()), true));
        mDataList.add(new ReportModel(PosReportTable.OTHER_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getOtherAmount()), true));
        mDataList.add(new ReportModel(PosReportTable.TIP_PAY_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getTipPayAmount()), true));
        mDataList.add(new ReportModel(PosReportTable.MANUAL_CASH_AMOUNT, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getManualCashRefund()), true));

        mDataList.add(new ReportModel(Constants.MANUAL_ENTRY, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getManuallyRecOrders()), false));
        mDataList.add(new ReportModel(Constants.ORDER_FAIL, MyStringFormat.formatWith2DecimalPlaces(mReportParent.getNoInternetOrders()), false));
    }

    public abstract void onPageChanged(int pIndex);
}

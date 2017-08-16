package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.posimplicity.database.server.DbHelper;
import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.local.ReportParent;

import java.util.ArrayList;
import java.util.List;

public class PosReportTable extends BaseTable<ReportParent> {

    private static final String TABLE_NAME = "ReportTable";

    public static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String TRANS_ID = "TransactionId";
    public static final String SUB_TOTAL_AMOUNT = "SubTotalAmount";
    public static final String DISCOUNT_AMOUNT = "DiscountAmount";
    public static final String TAX_AMOUNT = "TaxAmount";
    public static final String TOTAL_AMOUNT = "TotalAmount";
    public static final String CASH_AMOUNT = "CashAmount";
    public static final String CREDIT_AMOUNT = "CreditAmount";
    public static final String CHECK_AMOUNT = "CheckAmount";
    public static final String CUSTOM_1_AMOUNT = "Custom1Amt";
    public static final String CUSTOM_2_AMOUNT = "Custom2Amt";
    public static final String GIFT_AMOUNT = "GiftAmount";
    public static final String REWARDS_AMOUNT = "RewardAmount";

    public static final String LOTTERY_AMOUNT = "LotteryAmount";
    public static final String EXPENSES_AMOUNT = "ExpensesAmount";
    public static final String SUPPLIES_AMOUNT = "SuppliesAmount";
    public static final String PRODUCT_AMOUNT = "ProductAmount";
    public static final String OTHER_AMOUNT = "OtherAmount";
    public static final String TIP_PAY_AMOUNT = "TipPayAmount";
    public static final String MANUAL_CASH_AMOUNT = "ManualCashRefund";

    private static final String TRANS_TIME = "TransTime";
    private static final String REFUND_STATUS = "RefundStatus";
    private static final String SAVE_STATE = "SaveState";

    public static final String DESCRIPTION = "Description";
    private static final String REPORT_NAME = "ReportName";
    private static final String PAYOUT_NAME = "PayoutName";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ( "
                + TRANS_ID + " TEXT, "
                + SUB_TOTAL_AMOUNT + " TEXT, "
                + DISCOUNT_AMOUNT + " TEXT, "
                + TAX_AMOUNT + " TEXT, "
                + TOTAL_AMOUNT + " TEXT, "
                + CASH_AMOUNT + " TEXT, "
                + CREDIT_AMOUNT + " TEXT, "
                + CHECK_AMOUNT + " TEXT, "
                + CUSTOM_1_AMOUNT + " TEXT, "
                + CUSTOM_2_AMOUNT + " TEXT, "
                + GIFT_AMOUNT + " TEXT, "
                + REWARDS_AMOUNT + " TEXT, "

                + LOTTERY_AMOUNT + " TEXT, "
                + EXPENSES_AMOUNT + " TEXT, "
                + SUPPLIES_AMOUNT + " TEXT, "
                + PRODUCT_AMOUNT + " TEXT, "
                + OTHER_AMOUNT + " TEXT, "
                + TIP_PAY_AMOUNT + " TEXT, "
                + MANUAL_CASH_AMOUNT + " TEXT, "

                + TRANS_TIME + " TEXT, "
                + REFUND_STATUS + " TEXT, "
                + SAVE_STATE + " TEXT, "

                + DESCRIPTION + " TEXT, "
                + REPORT_NAME + " TEXT, "
                + PAYOUT_NAME + " TEXT  )";
    }

    public PosReportTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(ReportParent pNewOrUpdatedModel, ReportParent pExistingModel) {
        ContentValues values = new ContentValues();

        values.put(TRANS_ID, pNewOrUpdatedModel.getTransactionId());
        values.put(SUB_TOTAL_AMOUNT, pNewOrUpdatedModel.getSubtotalAmount());
        values.put(DISCOUNT_AMOUNT, pNewOrUpdatedModel.getDiscountAmount());
        values.put(TAX_AMOUNT, pNewOrUpdatedModel.getTaxAmount());
        values.put(TOTAL_AMOUNT, pNewOrUpdatedModel.getTotalAmount());
        values.put(CASH_AMOUNT, pNewOrUpdatedModel.getCashAmount());
        values.put(CREDIT_AMOUNT, pNewOrUpdatedModel.getCreditAmount());
        values.put(CHECK_AMOUNT, pNewOrUpdatedModel.getCheckAmount());
        values.put(CUSTOM_1_AMOUNT, pNewOrUpdatedModel.getCustom1Amount());
        values.put(CUSTOM_2_AMOUNT, pNewOrUpdatedModel.getCustom2Amount());
        values.put(GIFT_AMOUNT, pNewOrUpdatedModel.getGiftAmount());
        values.put(REWARDS_AMOUNT, pNewOrUpdatedModel.getRewardAmount());

        values.put(LOTTERY_AMOUNT, pNewOrUpdatedModel.getLotteryAmount());
        values.put(EXPENSES_AMOUNT, pNewOrUpdatedModel.getExpensesAmount());
        values.put(SUPPLIES_AMOUNT, pNewOrUpdatedModel.getSuppliesAmount());
        values.put(PRODUCT_AMOUNT, pNewOrUpdatedModel.getProductListAmount());
        values.put(OTHER_AMOUNT, pNewOrUpdatedModel.getOtherAmount());
        values.put(TIP_PAY_AMOUNT, pNewOrUpdatedModel.getTipPayAmount());

        values.put(MANUAL_CASH_AMOUNT, pNewOrUpdatedModel.getManualCashRefund());

        values.put(TRANS_TIME, pNewOrUpdatedModel.getTransTime());
        values.put(REFUND_STATUS, pNewOrUpdatedModel.getRefundStatus());
        values.put(SAVE_STATE, pNewOrUpdatedModel.getSaveState());

        values.put(DESCRIPTION, pNewOrUpdatedModel.getDescription());
        values.put(REPORT_NAME, pNewOrUpdatedModel.getReportName());
        values.put(PAYOUT_NAME, pNewOrUpdatedModel.getPayoutType());

        return values;
    }

    @Override
    protected ArrayList<ReportParent> getAllData(String pSelection, String[] pSelectionArgs) {
        return new ArrayList<>(0);
    }

    public ReportParent getReportInfoFromName(String pTransTime, String pReportName) {
        ReportParent reportParent = new ReportParent();
        reportParent.setReportName(pReportName);
        String rawQuery = String.format("SELECT"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s ,"
                        + " SUM(%s) as %s , SUM(%s) as %s"
                        + " FROM %s WHERE " + REPORT_NAME + " = %s  AND " + TRANS_TIME + " = %s "
                , SUB_TOTAL_AMOUNT, SUB_TOTAL_AMOUNT
                , DISCOUNT_AMOUNT, DISCOUNT_AMOUNT
                , TAX_AMOUNT, TAX_AMOUNT
                , TOTAL_AMOUNT, TOTAL_AMOUNT
                , CASH_AMOUNT, CASH_AMOUNT
                , CREDIT_AMOUNT, CREDIT_AMOUNT
                , CHECK_AMOUNT, CHECK_AMOUNT
                , CUSTOM_1_AMOUNT, CUSTOM_1_AMOUNT
                , CUSTOM_2_AMOUNT, CUSTOM_2_AMOUNT
                , GIFT_AMOUNT, GIFT_AMOUNT
                , REWARDS_AMOUNT, REWARDS_AMOUNT
                , LOTTERY_AMOUNT, LOTTERY_AMOUNT
                , EXPENSES_AMOUNT, EXPENSES_AMOUNT
                , SUPPLIES_AMOUNT, SUPPLIES_AMOUNT
                , PRODUCT_AMOUNT, PRODUCT_AMOUNT
                , OTHER_AMOUNT, OTHER_AMOUNT
                , TIP_PAY_AMOUNT, TIP_PAY_AMOUNT
                , MANUAL_CASH_AMOUNT, MANUAL_CASH_AMOUNT
                , TABLE_NAME, "'" + pReportName + "'", "'" + pTransTime + "'");

        Cursor cursor = null;
        try {
            cursor = mWritableDatabase.rawQuery(rawQuery, null);

            if (cursor.getCount() <= 0) {
                return reportParent;
            }
            cursor.moveToFirst();

            reportParent.setSubtotalAmount(cursor.getFloat(cursor.getColumnIndex(SUB_TOTAL_AMOUNT)));
            reportParent.setDiscountAmount(cursor.getFloat(cursor.getColumnIndex(DISCOUNT_AMOUNT)));
            reportParent.setTaxAmount(cursor.getFloat(cursor.getColumnIndex(TAX_AMOUNT)));
            reportParent.setTotalAmount(cursor.getFloat(cursor.getColumnIndex(TOTAL_AMOUNT)));
            reportParent.setCashAmount(cursor.getFloat(cursor.getColumnIndex(CASH_AMOUNT)));
            reportParent.setCreditAmount(cursor.getFloat(cursor.getColumnIndex(CREDIT_AMOUNT)));
            reportParent.setCheckAmount(cursor.getFloat(cursor.getColumnIndex(CHECK_AMOUNT)));
            reportParent.setCustom1Amount(cursor.getFloat(cursor.getColumnIndex(CUSTOM_1_AMOUNT)));
            reportParent.setCustom2Amount(cursor.getFloat(cursor.getColumnIndex(CUSTOM_2_AMOUNT)));
            reportParent.setGiftAmount(cursor.getFloat(cursor.getColumnIndex(GIFT_AMOUNT)));
            reportParent.setRewardAmount(cursor.getFloat(cursor.getColumnIndex(REWARDS_AMOUNT)));

            reportParent.setLotteryAmount(cursor.getFloat(cursor.getColumnIndex(LOTTERY_AMOUNT)));
            reportParent.setExpensesAmount(cursor.getFloat(cursor.getColumnIndex(EXPENSES_AMOUNT)));
            reportParent.setSuppliesAmount(cursor.getFloat(cursor.getColumnIndex(SUPPLIES_AMOUNT)));
            reportParent.setProductListAmount(cursor.getFloat(cursor.getColumnIndex(PRODUCT_AMOUNT)));
            reportParent.setOtherAmount(cursor.getFloat(cursor.getColumnIndex(OTHER_AMOUNT)));
            reportParent.setTipPayAmount(cursor.getFloat(cursor.getColumnIndex(TIP_PAY_AMOUNT)));

            reportParent.setManualCashRefund(cursor.getFloat(cursor.getColumnIndex(MANUAL_CASH_AMOUNT)));

        } catch (Exception e) {
            Log.e(mTableName, "getReportInfoFromName", e);
        } finally {
            closeCursor(cursor);
        }
        return reportParent;
    }

    public boolean deleteRecord(String pTransId) {
        return mWritableDatabase.delete(TABLE_NAME, TRANS_ID + "=?", new String[]{pTransId}) > 0;
    }

    public boolean deleteReport(String pReportName) {
        return mWritableDatabase.delete(TABLE_NAME, REPORT_NAME + "=?", new String[]{pReportName}) > 0;
    }

    public float getSumOfGivenSaveState(String pTransTime, String pReportName, String pSaveState) {
        float sum = 0;
        String rawQuery = String.format("SELECT "
                        + "SUM(%s) as %s "
                        + "FROM %s WHERE " + REPORT_NAME + " = %s  AND " + TRANS_TIME + " = %s AND " + SAVE_STATE + " = %s "
                , TOTAL_AMOUNT, TOTAL_AMOUNT
                , TABLE_NAME
                , "'" + pReportName + "'"
                , "'" + pTransTime + "'"
                , "'" + pSaveState + "'");

        Cursor cursor = null;
        try {
            cursor = mWritableDatabase.rawQuery(rawQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                sum = cursor.getFloat(cursor.getColumnIndex(TOTAL_AMOUNT));
            }
        } catch (Exception e) {
            Log.e(mTableName, "getSumOfGivenSaveState", e);
        } finally {
            closeCursor(cursor);
        }
        return sum;
    }

    public List<String> getListBasedOnColumnName(String pColumnName, String pReportName, String pTransTime) {
        List<String> mDataList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mWritableDatabase.query(TABLE_NAME, new String[]{pColumnName}, REPORT_NAME + " =? AND " + TRANS_TIME + " =? ", new String[]{pReportName, pTransTime}, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String floatValue = cursor.getString(cursor.getColumnIndex(pColumnName));
                    if (!"0.0".equalsIgnoreCase(floatValue)) {
                        mDataList.add(floatValue);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return mDataList;
    }
}


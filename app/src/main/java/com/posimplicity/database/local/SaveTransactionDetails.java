package com.posimplicity.database.local;

import android.content.Context;

import com.Beans.ReportsModel;
import com.Beans.TransactionModel;
import com.utils.CurrentDate;
import com.utils.MyStringFormat;

public class SaveTransactionDetails {
	

	public static void saveTransactionInDataBase(Context mContext,float totalAmount, float taxAmount, float totalWithTaxAmount, float cashAmount, float creditAmount, float checkAmount, float giftAmount, float rewardAmount ,String refundStatus, String saveState,float custom1Amt,float custom2Amt){
		String transDate  = CurrentDate.returnCurrentDate();		

		ReportsModel dailyReportsModel = new ReportsModel(				
				MyStringFormat.formatWith2DecimalPlaces(totalAmount + taxAmount)                    , MyStringFormat.formatWith2DecimalPlaces(taxAmount)   ,
				MyStringFormat.formatWith2DecimalPlaces(totalWithTaxAmount)                         , MyStringFormat.formatWith2DecimalPlaces(excludeTaxAmt(cashAmount,taxAmount))  ,
				MyStringFormat.formatWith2DecimalPlaces(excludeTaxAmt(creditAmount,taxAmount))      , MyStringFormat.formatWith2DecimalPlaces(excludeTaxAmt(checkAmount,taxAmount)) ,
				MyStringFormat.formatWith2DecimalPlaces(excludeTaxAmt(giftAmount,taxAmount))        , MyStringFormat.formatWith2DecimalPlaces(excludeTaxAmt(rewardAmount,taxAmount)),
				transDate                                                           , refundStatus,
				saveState                                                           , ReportsTable.DEFAULT_VALUE, 
				ReportsTable.DEFAULT_VALUE                                          , ReportsTable.DEFAULT_VALUE, 
				ReportsTable.DEFAULT_VALUE                                          , ReportsTable.DEFAULT_VALUE,
				ReportsTable.DEFAULT_VALUE                                          , ReportsTable.DEFAULT_VALUE, 
				ReportsTable.DEFAULT_DESCRIPTION                                    , ReportsTable.DEFAULT_PAYOUT_NAME,
				MyStringFormat.formatWith2DecimalPlaces(excludeTaxAmt(custom1Amt,taxAmount)),MyStringFormat.formatWith2DecimalPlaces(excludeTaxAmt(custom2Amt,taxAmount)));

		new ReportsTable(mContext).addInfoInTable(dailyReportsModel,ReportsTable.DAILY_REPORT);
		new ReportsTable(mContext).addInfoInTable(dailyReportsModel,ReportsTable.SHIFT_REPORT);

	}

	private static float excludeTaxAmt(float payAmt, float taxAmt){
		/*if(payAmt > 0)
			return payAmt-taxAmt;
		else*/
			return payAmt;
	}

	public static void savePayoutsTransactionInDataBase(Context mContext,float lotteryAmt, float expensesAmt, float suppliesAmt, 
			float productAmt, float otherAmt, float tipPayAmt,float manualCashrefAmt, String refundStatus, String saveState,String description,String descrptionPayoutName){

		String transTime               = CurrentDate.returnCurrentDate();

		ReportsModel dailyReportsModel = new ReportsModel(
				ReportsTable.DEFAULT_VALUE                                    , ReportsTable.DEFAULT_VALUE, 
				ReportsTable.DEFAULT_VALUE                                    , ReportsTable.DEFAULT_VALUE, 
				ReportsTable.DEFAULT_VALUE                                    , ReportsTable.DEFAULT_VALUE, 
				ReportsTable.DEFAULT_VALUE                                    , ReportsTable.DEFAULT_VALUE,  
				transTime                                                     , refundStatus,
				saveState                                                     , MyStringFormat.formatWith2DecimalPlaces(lotteryAmt),
				MyStringFormat.formatWith2DecimalPlaces(expensesAmt)                          , MyStringFormat.formatWith2DecimalPlaces(suppliesAmt) ,
				MyStringFormat.formatWith2DecimalPlaces(productAmt)                           , MyStringFormat.formatWith2DecimalPlaces(otherAmt),
				MyStringFormat.formatWith2DecimalPlaces(tipPayAmt)                            , MyStringFormat.formatWith2DecimalPlaces(manualCashrefAmt),
				description                                                   , descrptionPayoutName, 
				ReportsTable.DEFAULT_VALUE                                    , ReportsTable.DEFAULT_VALUE);

		new ReportsTable(mContext).addInfoInTable(dailyReportsModel,ReportsTable.DAILY_REPORT);
		new ReportsTable(mContext).addInfoInTable(dailyReportsModel,ReportsTable.SHIFT_REPORT);
	}
	
	public static void saveTransactionWithId(String transactionId,String clerkId,Context mContext){
		TransactionModel transactionModel = new TransactionModel(clerkId, transactionId, CurrentDate.returnCurrentDate());
		new TransactionTable(mContext).addInfoInTable(transactionModel);
	}
}

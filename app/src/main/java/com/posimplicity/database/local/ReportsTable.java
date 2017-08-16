package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.ReportsModel;
import com.utils.MyStringFormat;
import com.posimplicity.database.POSDatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ReportsTable {

	/**
	 *   TABLE NAME....
	 */

	public static final String TABLE_NAME       = "ReportsTable";

	/**
	 *   SOME CONSTANT VALUE SUPPLIES AS VALUE FOR KEY
	 */

	public static final String DAILY_REPORT             = "DailyReport";
	public static final String SHIFT_REPORT             = "ShiftReport";
	public static final String NO_REFUND                = "No";
	public static final String YES_REFUND               = "Yes";
	public static final String FAILED                   = "Failed";
	public static final String SUCCESSFULL              = "Success";
	public static final String MANUALLY_ENTRY           = "ManuallEntry";
	public static final String NO_INTERNET_ORDERS       = "Orders Not Recorded In Backend";
	public static final String MANUALLY_RECORDED_ORDERS = "Manually Recorded Orders";
	public final static String DEFAULT_VALUE            = "0.00";
	public final static String DEFAULT_DESCRIPTION      = "Comment Not Available";
	public static final String DEFAULT_PAYOUT_NAME      = "Payout Name Not Available";
	public static final String TIP_AMOUNT               = "Tip Amount";


	/**
	 *   COLUMNS NAME IN TABLE 
	 */

	public static final String TOTAL_AMOUNT            = "TotalAmount";
	public static final String TAX_AMOUNT         	   = "TaxAmount";
	public static final String TOTAL_WITH_TAX_AMOUNT   = "TotalWithTaxAmount";	
	public static final String CASH_AMOUNT    		   = "CashAmount";
	public static final String CREDIT_AMOUNT           = "CreditAmount";
	public static final String CHECK_AMOUNT            = "CheckAmount";
	public static final String GIFT_AMOUNT        	   = "GiftAmount";	
	public static final String REWARDS_AMOUNT          = "RewardAmount";
	public static final String TRANS_TIME              = "TransTime";
	public static final String REFUND_STATUS           = "RefundStatus";	
	public static final String SAVE_STATE              = "SaveState";
	public static final String LOTTERY_AMOUNT          = "LotteryAmount";
	public static final String EXPENSES_AMOUNT         = "ExpensesAmount";	
	public static final String SUPPLIES_AMOUNT         = "SuppliesAmount";
	public static final String PRODUCT_AMOUNT          = "ProductAmount";
	public static final String OTHER_AMOUNT            = "OtherAmount";
	public static final String TIP_Pay_AMOUNT          = "TipPayAmount";
	public static final String MANUAL_CASH_REFUND      = "ManualCashRefund";
	public static final String DESCRIPTION             = "Description";
	public static final String REPORT_NAME             = "ReportName";
	public static final String PAYOUT_NAME             = "PayoutName";
	public static final String CUSTOM_1_AMOUNT    	   = "Custom1Amt";
	public static final String CUSTOM_2_AMOUNT    	   = "Custom2Amt";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public ReportsTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "
					+ REPORT_NAME       	+" TEXT, "
					+ TOTAL_AMOUNT       	+" TEXT, "
					+ TAX_AMOUNT      		+" TEXT, "
					+ TOTAL_WITH_TAX_AMOUNT +" TEXT, "
					+ CASH_AMOUNT    		+" TEXT, "
					+ CREDIT_AMOUNT      	+" TEXT, "
					+ CHECK_AMOUNT   		+" TEXT, "
					+ GIFT_AMOUNT   		+" TEXT, "
					+ REWARDS_AMOUNT  		+" TEXT, "
					+ TRANS_TIME       		+" TEXT, "
					+ REFUND_STATUS 		+" TEXT, "
					+ SAVE_STATE  			+" TEXT, "
					+ LOTTERY_AMOUNT      	+" TEXT, "
					+ EXPENSES_AMOUNT       +" TEXT, "
					+ SUPPLIES_AMOUNT      	+" TEXT, "
					+ PRODUCT_AMOUNT   		+" TEXT, "
					+ OTHER_AMOUNT     		+" TEXT, "
					+ TIP_Pay_AMOUNT        +" TEXT, "
					+ MANUAL_CASH_REFUND 	+" TEXT, "
					+ DESCRIPTION     		+" TEXT, "
					+ PAYOUT_NAME           +" TEXT, "
					+ CUSTOM_1_AMOUNT  		+" TEXT, "
					+ CUSTOM_2_AMOUNT  		+" TEXT  )";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addInfoInTable(ReportsModel reportsModel,String reportName) {
		try{
			reportsModel.setReportName(reportName);
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(TOTAL_AMOUNT,			reportsModel.getTotalAmount());
			values.put(TAX_AMOUNT,         		reportsModel.getTaxAmount());
			values.put(TOTAL_WITH_TAX_AMOUNT,   reportsModel.getTotalWithTaxAmount());
			values.put(CASH_AMOUNT,   			reportsModel.getCashAmount());			
			values.put(CREDIT_AMOUNT,   		reportsModel.getCreditAmount());
			values.put(CHECK_AMOUNT,   			reportsModel.getCheckAmount());
			values.put(GIFT_AMOUNT,   			reportsModel.getGiftAmount());
			values.put(REWARDS_AMOUNT,   		reportsModel.getRewardAmount());
			values.put(TRANS_TIME,   			reportsModel.getTransTime());			
			values.put(REFUND_STATUS,   		reportsModel.getRefundStatus());
			values.put(SAVE_STATE,   			reportsModel.getSaveState());
			values.put(LOTTERY_AMOUNT,   		reportsModel.getLotteryAmount());
			values.put(EXPENSES_AMOUNT,   		reportsModel.getExpensesAmount());
			values.put(SUPPLIES_AMOUNT,   		reportsModel.getSuppliesAmount());
			values.put(PRODUCT_AMOUNT,   		reportsModel.getProductAmount());
			values.put(OTHER_AMOUNT, 			reportsModel.getOtherAmount());
			values.put(TIP_Pay_AMOUNT,          reportsModel.getTipPayAmount());
			values.put(MANUAL_CASH_REFUND,   	reportsModel.getManualCashRefund());
			values.put(DESCRIPTION, 			reportsModel.getDescrption());
			values.put(REPORT_NAME, 			reportsModel.getReportName());
			values.put(PAYOUT_NAME,             reportsModel.getPayoutType());
			values.put(CUSTOM_1_AMOUNT, 	    reportsModel.getCustom1Amount());
			values.put(CUSTOM_2_AMOUNT,         reportsModel.getCustom2Amount());


			// Inserting Row
			posDataBase.insert(TABLE_NAME, null, values);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public String getSumOfTotalAmountBasedOnDynamicValues(String timeTrans, String reportName , String saveState){
		float total = 0 ;
		try {
			posDataBase           = posDbHandler.openReadableDataBase();
			String rawQuery       = String.format("SELECT SUM(%s) FROM %s WHERE "+ REPORT_NAME +" = %s AND "+TRANS_TIME+" = %s  AND "+SAVE_STATE+" = %s "
					,TOTAL_AMOUNT,TABLE_NAME ,"'"+reportName+"'","'"+timeTrans+"'","'"+saveState+"'");
			Cursor cursor         = posDataBase.rawQuery(rawQuery, null);
			if (posDbHandler.cusorIsFine(cursor)) {
				cursor.moveToFirst();
				if(cursor.getString(0) != null){
					total = Float.parseFloat(MyStringFormat.onStringFormat(cursor.getString(0)));
				}
			}
			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		return MyStringFormat.formatWith2DecimalPlaces(total);
	}


	public ReportsModel getReportModel(String timeTrans,String reportName){
		ReportsModel reportsModel = new ReportsModel();
		reportsModel.setReportName(reportName);
		String rawQuery = "";
		try {
			posDataBase           = posDbHandler.openReadableDataBase();

			if(timeTrans != null)
			{
				rawQuery = String.format("SELECT SUM(%s), SUM(%s), "
						+ "SUM(%s), SUM(%s), "
						+ "SUM(%s), SUM(%s),"
						+ "SUM(%s), SUM(%s),"
						+ "SUM(%s), SUM(%s),"
						+ "SUM(%s), SUM(%s),"
						+ "SUM(%s), SUM(%s),"
						+ "SUM(%s), SUM(%s),"
						+ "SUM(%s) "
						+ "FROM %s WHERE "+ REPORT_NAME +" = %s AND "+TRANS_TIME+" = %s "
						,TOTAL_AMOUNT          ,TAX_AMOUNT
						,TOTAL_WITH_TAX_AMOUNT ,CASH_AMOUNT
						,CREDIT_AMOUNT         ,CHECK_AMOUNT
						,GIFT_AMOUNT           ,REWARDS_AMOUNT
						,LOTTERY_AMOUNT        ,EXPENSES_AMOUNT
						,SUPPLIES_AMOUNT       ,PRODUCT_AMOUNT
						,OTHER_AMOUNT          ,TIP_Pay_AMOUNT
						,MANUAL_CASH_REFUND    ,CUSTOM_1_AMOUNT
						,CUSTOM_2_AMOUNT       ,TABLE_NAME
						,"'"+reportName+"'","'"+timeTrans+"'");

			}
			else
			{	  rawQuery = String.format("SELECT SUM(%s), SUM(%s), "
					+ "SUM(%s), SUM(%s), "
					+ "SUM(%s), SUM(%s),"
					+ "SUM(%s), SUM(%s),"
					+ "SUM(%s), SUM(%s),"
					+ "SUM(%s), SUM(%s),"
					+ "SUM(%s), SUM(%s),"
					+ "SUM(%s), SUM(%s),"
					+ "SUM(%s) "
					+ "FROM %s WHERE "+ REPORT_NAME +" = %s "
					,TOTAL_AMOUNT          ,TAX_AMOUNT
					,TOTAL_WITH_TAX_AMOUNT ,CASH_AMOUNT
					,CREDIT_AMOUNT         ,CHECK_AMOUNT
					,GIFT_AMOUNT           ,REWARDS_AMOUNT
					,LOTTERY_AMOUNT        ,EXPENSES_AMOUNT
					,SUPPLIES_AMOUNT       ,PRODUCT_AMOUNT
					,OTHER_AMOUNT          ,TIP_Pay_AMOUNT
					,MANUAL_CASH_REFUND    ,CUSTOM_1_AMOUNT
					,CUSTOM_2_AMOUNT       
					,TABLE_NAME,"'"+reportName+"'");
			}

			Cursor cursor               = posDataBase.rawQuery(rawQuery, null);
			if (cursor.getCount() > 0 && cursor.moveToFirst() ) {
				if(cursor.getString(0) != null){
					reportsModel.setTotalAmount(MyStringFormat.onStringFormat(cursor.getString(0)));
					reportsModel.setTaxAmount(MyStringFormat.onStringFormat(cursor.getString(1)));
					reportsModel.setTotalWithTaxAmount(MyStringFormat.onStringFormat(cursor.getString(2)));
					reportsModel.setCashAmount(MyStringFormat.onStringFormat(cursor.getString(3)));					
					reportsModel.setCreditAmount(MyStringFormat.onStringFormat(cursor.getString(4)));
					reportsModel.setCheckAmount(MyStringFormat.onStringFormat(cursor.getString(5)));
					reportsModel.setGiftAmount(MyStringFormat.onStringFormat(cursor.getString(6)));
					reportsModel.setRewardAmount(MyStringFormat.onStringFormat(cursor.getString(7)));				
					reportsModel.setLotteryAmount(MyStringFormat.onStringFormat(cursor.getString(8)));
					reportsModel.setExpensesAmount(MyStringFormat.onStringFormat(cursor.getString(9)));
					reportsModel.setSuppliesAmount(MyStringFormat.onStringFormat(cursor.getString(10)));
					reportsModel.setProductAmount(MyStringFormat.onStringFormat(cursor.getString(11)));
					reportsModel.setOtherAmount(MyStringFormat.onStringFormat(cursor.getString(12)));
					reportsModel.setTipPayAmount(MyStringFormat.onStringFormat(cursor.getString(13)));
					reportsModel.setManualCashRefund(MyStringFormat.onStringFormat(cursor.getString(14)));	
					reportsModel.setCustom1Amount(MyStringFormat.onStringFormat(cursor.getString(15)));
					reportsModel.setCustom2Amount(MyStringFormat.onStringFormat(cursor.getString(16)));
				}
			}
			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		return reportsModel;
	}

	public void deleteInfoFromTable(String reportName) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, REPORT_NAME + " =? ", new String []{ reportName });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

	public List<String> getListOfSomeInfoBasedOnDynamicValue(String columnName,String reportName, String time,String payoutName) {	

		List<String> payoutList         = new ArrayList<>();
		try{
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = null;

			if(time.isEmpty())	{
				//String rawQuery       = String.format("SELECT %s FROM %s WHERE "+ REPORT_NAME +" = %s AND "+PAYOUT_NAME+" = %s ",columnName,TABLE_NAME ,"'"+reportName+"'","'"+payoutName+"'");
				cursor = posDataBase.query(TABLE_NAME, new String [] { columnName }, REPORT_NAME + " =? AND "+ PAYOUT_NAME + " =? ",new String[]{reportName,payoutName}, null, null, null);
			}			
			else
				cursor = posDataBase.query(TABLE_NAME, new String [] { columnName }, REPORT_NAME + " =? AND "+ TRANS_TIME  +" =?  AND "+ PAYOUT_NAME + " =? ",new String[]{ reportName ,time,payoutName }, null, null, null);

			if (posDbHandler.cusorIsFine(cursor)) {
				while (cursor.moveToNext()){
					if(!(cursor.getString(0).equalsIgnoreCase("0.00")))
						payoutList.add(cursor.getString(0));
				}
			}
			cursor.close();
		}

		catch(Exception ex){ ex.printStackTrace(); }
		finally{ posDbHandler.closeDataBase(); }
		return payoutList;
	}
}

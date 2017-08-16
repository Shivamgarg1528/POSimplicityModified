package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.TransactionModel;
import com.utils.CurrentDate;
import com.posimplicity.database.POSDatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionTable {


	public static final String TABLE_NAME     = "TransactionTable";

	private final String TRANS_ID             = "TransId";
	private final String DATE                 = "Dates";
	private final String CLERK_ID             = "ClerkId";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public TransactionTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ CLERK_ID              +" TEXT, "
					+ TRANS_ID              +" TEXT, "
					+ DATE                  +" TEXT )";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addInfoInTable(TransactionModel transactionMode) {
		try {
			posDataBase                 = posDbHandler.openWritableDataBase();
			ContentValues values        = new ContentValues();
			values.put(CLERK_ID,			transactionMode.getClerkId());
			values.put(TRANS_ID,            transactionMode.getTransId());
			values.put(DATE,                 new SimpleDateFormat("yyyy/MM/dd").format(new Date()).toString().trim());
			posDataBase.insert(TABLE_NAME, null, values);

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

	public List<TransactionModel> getAllInfoFromTable(String clerkId,String time){

		List<TransactionModel> listOfAllData = new ArrayList<TransactionModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,DATE +"==", new String[]{time}, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					TransactionModel customerModel = new TransactionModel();
					customerModel.setClerkId(cursor.getString(0));
					customerModel.setTransId(cursor.getString(1));
					customerModel.setDate(cursor.getString(2));
					listOfAllData.add(customerModel);

				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return userInfo list
		return listOfAllData;
	}

	public List<String> getTransactions() {
		String time  = CurrentDate.returnCurrentDate();
		List<String> listOfAllData = new ArrayList<String>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.rawQuery("Select * from TransactionTable where Dates = '"+time+"'",null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					listOfAllData.add(cursor.getString(1));

				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return userInfo list
		
		return listOfAllData;
	}

}

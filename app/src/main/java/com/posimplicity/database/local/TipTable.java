package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.CustomerModel;
import com.utils.MyStringFormat;
import com.posimplicity.database.POSDatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TipTable {

	public static final String TABLE_NAME     = "TipTable";

	private final String CUSTOMER_ID          = "CustomerId";
	private final String FIRST_NAME           = "FirstName";
	private final String TIP_AMOUNT           = "TipAmount";
	private final String DATE                 = "Dates";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public TipTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ CUSTOMER_ID           +" TEXT PRIMARY KEY , "
					+ FIRST_NAME            +" TEXT, "
					+ TIP_AMOUNT            +" TEXT, "
					+ DATE                  +" TEXT )";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addInfoInTable(CustomerModel customerModel) {
		boolean isRecordExist = false;
		float lastTipAmt      = 0.0f;
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				do {
					if(cursor.getString(0).equalsIgnoreCase(customerModel.getCustomerId())){
						lastTipAmt  = cursor.getFloat(2);
						isRecordExist = true;
						break;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			if(isRecordExist){

				lastTipAmt += Float.parseFloat(customerModel.getTipAmount());
				customerModel.setTipAmount(""+lastTipAmt);

				ContentValues values  = new ContentValues();
				values.put(CUSTOMER_ID,			customerModel.getCustomerId());
				values.put(FIRST_NAME,          customerModel.getFirstName());
				values.put(TIP_AMOUNT,          customerModel.getTipAmount());
				values.put(DATE,                 new SimpleDateFormat("yyyy/MM/dd").format(new Date()).toString().trim());
				posDataBase.update(TABLE_NAME, values, CUSTOMER_ID +" =? ", new String[] { customerModel.getCustomerId() } );
			}
			else
			{
				ContentValues values  = new ContentValues();
				values.put(CUSTOMER_ID,			customerModel.getCustomerId());
				values.put(FIRST_NAME,          customerModel.getFirstName());
				values.put(TIP_AMOUNT,          customerModel.getTipAmount());
				posDataBase.insert(TABLE_NAME, null, values);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

	public List<CustomerModel> getAllInfoFromTable(){

		List<CustomerModel> listOfAllData = new ArrayList<CustomerModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					CustomerModel customerModel = new CustomerModel();
					customerModel.setCustomerId(cursor.getString(0));
					customerModel.setFirstName(cursor.getString(1));
					customerModel.setTipAmount(MyStringFormat.onStringFormat(cursor.getString(2)));
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

	public String getSumOfTips(String transTime){

		float total = 0 ;
		try {
			posDataBase           = posDbHandler.openReadableDataBase();
			String rawQuery = String.format("SELECT SUM(%s) FROM %s WHERE "+ DATE +" = %s "
					,TIP_AMOUNT,TABLE_NAME ,"'"+transTime+"'");
			Cursor cursor               = posDataBase.rawQuery(rawQuery, null);
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

	public String getSumOfTipsWithOutDate(){

		float total = 0 ;
		try {
			posDataBase           = posDbHandler.openReadableDataBase();
			String rawQuery = String.format("SELECT SUM(%s) FROM %s "
					,TIP_AMOUNT,TABLE_NAME );
			Cursor cursor               = posDataBase.rawQuery(rawQuery, null);
			if (cursor.getCount() > 0 && cursor.moveToFirst() ) {
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


	public void deleteInfoFromTable() {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, null ,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

}
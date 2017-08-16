package com.posimplicity.database.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.PaymentMode;
import com.posimplicity.database.POSDatabaseHandler;

public class PaymentModeTable {

	public static final String TABLE_NAME     = "PaymentModeTable";

	private final String MODE_ID              = "paymentModeId";
	private final String MODE_NAME            = "paymentModeName";
	private final String MODE_SORT_ID         = "paymentModeSortId";
	private final String MODE_STATUS          = "paymentModeStatus";
	private final String MODE_DELETABLE       = "paymentModeDeletable";
	private final String MODE_TYPE            = "paymentModeType";
	private final String MODE_DESC            = "paymentModeDes";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public PaymentModeTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{		    	
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" (        "	
					+ MODE_ID               +" INTEGER, "
					+ MODE_NAME             +" TEXT,    "
					+ MODE_SORT_ID          +" INTEGER, "
					+ MODE_STATUS           +" INTEGER, "
					+ MODE_DELETABLE        +" INTEGER, "
					+ MODE_TYPE             +" TEXT,    "
					+ MODE_DESC             +" TEXT )";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public boolean addInfoInTable(PaymentMode.PaymentModeBean paymentMode) {
		boolean recordInserted = false;
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(MODE_ID,          paymentMode.getPaymentModeId());
			values.put(MODE_NAME,        paymentMode.getPaymentModeName());
			values.put(MODE_SORT_ID,     paymentMode.getPaymentModeSortId());
			values.put(MODE_STATUS,      paymentMode.isPaymentModeStatus());
			values.put(MODE_DELETABLE,   paymentMode.isPaymentModeDeletable());
			values.put(MODE_TYPE,        paymentMode.getPaymentModeType());
			values.put(MODE_DESC,        paymentMode.getPaymentModeDes());

			// Inserting Row
			long id = posDataBase.insert(TABLE_NAME, null, values);

			if(id != -1)
				recordInserted = true;
		}
		catch(Exception ex){
			ex.printStackTrace();

		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
		return recordInserted;
	}

	public void updateInfoInTable(PaymentMode.PaymentModeBean paymentMode) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(MODE_ID,          paymentMode.getPaymentModeId());
			values.put(MODE_NAME,        paymentMode.getPaymentModeName());
			values.put(MODE_SORT_ID,     paymentMode.getPaymentModeSortId());
			values.put(MODE_STATUS,      paymentMode.isPaymentModeStatus());
			values.put(MODE_DELETABLE,   paymentMode.isPaymentModeDeletable());
			values.put(MODE_TYPE,        paymentMode.getPaymentModeType());
			values.put(MODE_DESC,        paymentMode.getPaymentModeDes());

			// Update Row
			posDataBase.update(TABLE_NAME, values, MODE_ID +" =? ", new String[] { String.valueOf(paymentMode.getPaymentModeId()) } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<PaymentMode.PaymentModeBean> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				PaymentMode.PaymentModeBean paymentMode = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(MODE_ID,          paymentMode.getPaymentModeId());
				values.put(MODE_NAME,        paymentMode.getPaymentModeName());
				values.put(MODE_SORT_ID,     paymentMode.getPaymentModeSortId());
				values.put(MODE_STATUS,      paymentMode.isPaymentModeStatus());
				values.put(MODE_DELETABLE,   paymentMode.isPaymentModeDeletable());
				values.put(MODE_TYPE,        paymentMode.getPaymentModeType());
				values.put(MODE_DESC,        paymentMode.getPaymentModeDes());

				// Update Row
				posDataBase.update(TABLE_NAME, values, MODE_ID +" =? ", new String[] { String.valueOf(paymentMode.getPaymentModeId()) } );
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<PaymentMode.PaymentModeBean> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				PaymentMode.PaymentModeBean paymentMode = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(MODE_ID,          paymentMode.getPaymentModeId());
				values.put(MODE_NAME,        paymentMode.getPaymentModeName());
				values.put(MODE_SORT_ID,     paymentMode.getPaymentModeSortId());
				values.put(MODE_STATUS,      paymentMode.isPaymentModeStatus());
				values.put(MODE_DELETABLE,   paymentMode.isPaymentModeDeletable());
				values.put(MODE_TYPE,        paymentMode.getPaymentModeType());
				values.put(MODE_DESC,        paymentMode.getPaymentModeDes());

				// Inserting Row
				posDataBase.insert(TABLE_NAME, null, values);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public List<PaymentMode.PaymentModeBean> getAllInfoFromTableDefalut(){
		List<PaymentMode.PaymentModeBean> listOfAllData = new ArrayList<PaymentMode.PaymentModeBean>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null,null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					PaymentMode.PaymentModeBean paymentMode = new PaymentMode.PaymentModeBean();					
					paymentMode.setPaymentModeId(cursor.getInt(0));
					paymentMode.setPaymentModeName(cursor.getString(1));
					paymentMode.setPaymentModeSortId(cursor.getInt(2));
					paymentMode.setPaymentModeStatus(1 == cursor.getInt(3));
					paymentMode.setPaymentModeDeletable(1 == cursor.getInt(4));
					paymentMode.setPaymentModeType(cursor.getString(5));
					paymentMode.setPaymentModeDes(cursor.getString(6));
					listOfAllData.add(paymentMode);

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

		Collections.sort(listOfAllData);
		System.out.println(Arrays.toString(listOfAllData.toArray()));
		return listOfAllData;
	}

	public int getLastId() {
		int id = 100;		
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			String qry            = "Select "+MODE_ID+" From "+TABLE_NAME + " Order by "+MODE_ID+" desc limit 1";
			System.out.println(qry);
			Cursor cursor         = posDataBase.rawQuery(qry, null);
			if (cursor != null && cursor.moveToFirst()) {
				id = (Integer.parseInt(cursor.getString(0)));
			}
			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
		return id;
	}

	public void clearTable() {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, null, null);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void deleteTender(int paymentModeId) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, MODE_ID +" =? ", new String[]{String.valueOf(paymentModeId)});
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}
}

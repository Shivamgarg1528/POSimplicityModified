package com.posimplicity.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.CustomerGroup;

public class CustomerGroupTable {

	public static final String TABLE_NAME         = "CustomerGroupTable";

	private final String GROUP_ID                 = "GroupId";
	private final String GROUP_NAME               = "GroupName";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public CustomerGroupTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ GROUP_ID              +" TEXT, "
					+ GROUP_NAME            +" TEXT, PRIMARY KEY ( "+ GROUP_ID + " ))";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public void addInfoListInTable(List<CustomerGroup> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				CustomerGroup customerGroup = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(GROUP_ID,			customerGroup.getCustomerGroupId());
				values.put(GROUP_NAME,          customerGroup.getCustomerGroupName());

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


	public List<CustomerGroup> getAllInfoFromTable(){
		List<CustomerGroup> listOfAllData = new ArrayList<CustomerGroup>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					CustomerGroup customerGroup = new CustomerGroup();
					customerGroup.setCustomerGroupId(cursor.getString(0));
					customerGroup.setCustomerGroupName(cursor.getString(1));
					listOfAllData.add(customerGroup);
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

		return listOfAllData;
	}

	public List<String> getNameOfCustomerGroup(){

		List<String> listOfAllData = new ArrayList<String>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

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

		return listOfAllData;

	}


	public void deleteInfoFromTable(String deptId) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, GROUP_ID + " =? ", new String []{ deptId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

	public void deleteListFromTable(String idList){


		try {
			posDataBase = posDbHandler.openWritableDataBase();

			List<String> ids = Arrays.asList(idList.split(","));

			for(int index = 0 ; index < ids.size() ; index++)
				posDataBase.delete(TABLE_NAME, GROUP_ID + " =? ", new String []{ ids.get(index) });

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}


	}
}

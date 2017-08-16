package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.POSAuthority;
import com.posimplicity.database.POSDatabaseHandler;

import java.util.ArrayList;
import java.util.List;


public class SecurityTable {

	public static final String TABLE_NAME       = "SecurityTable";

	public static final String SettingName      = "SettingName";
	public static final String Admin            = "Admin";
	public static final String Clerk            = "Clerk";	
	public static final String Manager          = "Manager";
	public static final String Supervisor       = "Supervisor";
	public static final String OVERRIDE         = "SettingOverride";


	////////// Settings Namesss

	public static final String Settings_Admin                                 = "Admin";
	public static final String Settings_DailyReport                           = "DailyReport";
	public static final String Settings_ShiftReport                           = "ShiftReport";
	public static final String Settings_TipReport                             = "TipReport";
	public static final String Settings_Transaction_Dollar_Discount           = "Transaction Dollar Discount";
	public static final String Settings_Transaction_Percentage_Discount       = "Transaction Percentage Discount";
	public static final String Settings_Item_Dollar_Discount                  = "Item Dollar Discount";
	public static final String Settings_Item_Percentage_Discount              = "Item Percentage Discount";
	public static final String Settings_Refund                                = "Refund";
	public static final String Settings_RePrint                               = "RePrint";	
	public static final String Settings_Drawer                                = "Drawer";


	public SQLiteDatabase posDataBase;
	public POSDatabaseHandler posDbHandler;

	public SecurityTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME           +" ( "					
					+ SettingName          +" TEXT, "
					+ Admin                +" TEXT, "
					+ Clerk    	           +" TEXT, "
					+ Manager              +" TEXT, "
					+ Supervisor           +" TEXT, "
					+ OVERRIDE             +" TEXT )";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addInfoInTable(POSAuthority posAuthority) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(Admin,    posAuthority.isAdminHaveRights());
			values.put(Clerk,            posAuthority.isClerkHaveRights());
			values.put(Manager,          posAuthority.isManagerHaveRights());
			values.put(Supervisor,       posAuthority.isSuperVisorHaveRights());
			values.put(SettingName,      posAuthority.getSettingName());
			values.put(OVERRIDE,         posAuthority.isSettingOverrideByManager());

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

	public void updateInfoInTable(POSAuthority posAuthority) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(Admin,    posAuthority.isAdminHaveRights());
			values.put(Clerk,            posAuthority.isClerkHaveRights());
			values.put(Manager,          posAuthority.isManagerHaveRights());
			values.put(Supervisor,       posAuthority.isSuperVisorHaveRights());
			values.put(SettingName,      posAuthority.getSettingName());
			values.put(OVERRIDE,         posAuthority.isSettingOverrideByManager());

			// Update Row
			posDataBase.update(TABLE_NAME, values, SettingName +" =? ", new String[] { posAuthority.getSettingName() } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<POSAuthority> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				POSAuthority posAuthority = listOfDetails.get(index);
				ContentValues values = new ContentValues();


				values.put(Admin,    posAuthority.isAdminHaveRights());
				values.put(Clerk,            posAuthority.isClerkHaveRights());
				values.put(Manager,          posAuthority.isManagerHaveRights());
				values.put(Supervisor,       posAuthority.isSuperVisorHaveRights());
				values.put(SettingName,      posAuthority.getSettingName());
				values.put(OVERRIDE,         posAuthority.isSettingOverrideByManager());

				// Update Row
				posDataBase.update(TABLE_NAME, values, SettingName +" =? ", new String[] { posAuthority.getSettingName() } );
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<POSAuthority> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				POSAuthority posAuthority = listOfDetails.get(index);
				ContentValues values = new ContentValues();


				values.put(Admin,            posAuthority.isAdminHaveRights());
				values.put(Clerk,            posAuthority.isClerkHaveRights());
				values.put(Manager,          posAuthority.isManagerHaveRights());
				values.put(Supervisor,       posAuthority.isSuperVisorHaveRights());
				values.put(SettingName,      posAuthority.getSettingName());
				values.put(OVERRIDE,         posAuthority.isSettingOverrideByManager());

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


	public List<POSAuthority> getAllInfoFromTable(){
		List<POSAuthority> listOfAllData = new ArrayList<POSAuthority>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					POSAuthority roleModel = new POSAuthority();					
					roleModel.setSettingName(cursor.getString(0));
					roleModel.setAdminHaveRights(1 == cursor.getInt(1));
					roleModel.setClerkHaveRights(1 == cursor.getInt(2));
					roleModel.setManagerHaveRights(1 == cursor.getInt(3));
					roleModel.setSuperVisorHaveRights(1 == cursor.getInt(4));
					roleModel.setSettingOverrideByManager(1 == cursor.getInt(5));

					listOfAllData.add(roleModel);

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

	public boolean isSettingOn(String settingName, String roleName) {
		boolean isSettingOn = false;
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, new String []{ roleName } ,SettingName + "=?", new String[]{settingName}, null, null, null);

			// looping through all rows and adding to list
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				isSettingOn = 1 == cursor.getInt(0);
			}

			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		return isSettingOn;
	}

	public POSAuthority getSingleObjBasedOnSettingNameAndRoleName(String settingName){
		POSAuthority roleModel = new POSAuthority();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,SettingName + "=?", new String[]{settingName}, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {					
					roleModel.setSettingName(cursor.getString(0));
					roleModel.setAdminHaveRights(1 == cursor.getInt(1));
					roleModel.setClerkHaveRights(1 == cursor.getInt(2));
					roleModel.setManagerHaveRights(1 == cursor.getInt(3));
					roleModel.setSuperVisorHaveRights(1 == cursor.getInt(4));
					roleModel.setSettingOverrideByManager(1 == cursor.getInt(5));

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
		return roleModel;
	}
}

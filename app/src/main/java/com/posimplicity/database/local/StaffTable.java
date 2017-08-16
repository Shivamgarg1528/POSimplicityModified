package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.StaffModel;
import com.utils.MyStringFormat;
import com.posimplicity.database.POSDatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class StaffTable {

	public static final String DEFAULT_GROUP_ID     = "-1"; 

	public static final String TABLE_NAME     = "StaffTable";

	private final String STAFF_ID             = "StaffId";
	private final String STAFF_NAME           = "StaffName";
	private final String PAYGRADE             = "StaffPayGrade";
	private final String LOGIN_STATUS         = "LoginStatus";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public StaffTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ STAFF_ID              +" INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ STAFF_NAME            +" TEXT, "
					+ PAYGRADE              +" TEXT, "
					+ LOGIN_STATUS          +" TEXT )";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getSumOfPayGradesOfLoginUsers(){

		float total = 0 ;
		try {
			posDataBase     = posDbHandler.openReadableDataBase();
			String rawQuery = String.format("SELECT SUM(%s) FROM %s WHERE "+ LOGIN_STATUS +" = %s ",PAYGRADE,TABLE_NAME ,"'"+1+"'");
			Cursor cursor   = posDataBase.rawQuery(rawQuery, null);
			if (posDbHandler.cusorIsFine(cursor)) {
				cursor.moveToFirst();
				if(cursor.getString(0) != null)
					total = Float.parseFloat(MyStringFormat.onStringFormat(cursor.getString(0)));
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

	public boolean addInfoInTable(StaffModel staffModel) {
		boolean recordInserted = false;
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();


			values.put(STAFF_NAME,          staffModel.getStaffName());
			values.put(PAYGRADE,            staffModel.getStaffPayGrade());
			values.put(LOGIN_STATUS,        staffModel.isStaffLogin());

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

	public void updateInfoInTable(StaffModel staffModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(STAFF_NAME,          staffModel.getStaffName());
			values.put(PAYGRADE,            staffModel.getStaffPayGrade());
			values.put(LOGIN_STATUS,        staffModel.isStaffLogin());

			// Update Row
			posDataBase.update(TABLE_NAME, values, STAFF_ID +" =? ", new String[] { staffModel.getStaffId() } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<StaffModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				StaffModel staffModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(STAFF_NAME,          staffModel.getStaffName());
				values.put(PAYGRADE,            staffModel.getStaffPayGrade());
				values.put(LOGIN_STATUS,        staffModel.isStaffLogin());
				posDataBase.update(TABLE_NAME, values, STAFF_ID +" =? ", new String[] { staffModel.getStaffId() } );

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<StaffModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				StaffModel staffModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(STAFF_NAME,          staffModel.getStaffName());
				values.put(PAYGRADE,            staffModel.getStaffPayGrade());
				values.put(LOGIN_STATUS,        staffModel.isStaffLogin());

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

	public boolean getStatusOfAllStaff(boolean status){
		boolean isStaffOk = false;
		StaffModel staffModel = new StaffModel();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {


					staffModel.setStaffId(cursor.getString(0));
					staffModel.setStaffName(cursor.getString(1));
					staffModel.setStaffPayGrade(cursor.getString(2));
					staffModel.setStaffLogin(cursor.getInt(3) == 0?false:true);

					if(staffModel.isStaffLogin() == status){
						isStaffOk = true;
						break;
					}

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
		return isStaffOk;
	}

	public List<StaffModel> getAllInfoFromTable(boolean status){
		List<StaffModel> listOfAllData = new ArrayList<StaffModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					StaffModel staffModel = new StaffModel();
					staffModel.setStaffId(cursor.getString(0));
					staffModel.setStaffName(cursor.getString(1));
					staffModel.setStaffPayGrade(cursor.getString(2));
					staffModel.setStaffLogin(cursor.getInt(3) == 0?false:true);

					if(staffModel.isStaffLogin() == status)
						listOfAllData.add(staffModel);

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

	public List<StaffModel> getAllInfoFromTableDefalut(){
		List<StaffModel> listOfAllData = new ArrayList<StaffModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StaffModel staffModel = new StaffModel();
					staffModel.setStaffId(cursor.getString(0));
					staffModel.setStaffName(cursor.getString(1));
					staffModel.setStaffPayGrade(cursor.getString(2));
					staffModel.setStaffLogin(cursor.getInt(3) == 0?false:true);
					listOfAllData.add(staffModel);

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


	public StaffModel getSingleInfoFromTableByCustomerId(String staffId){

		StaffModel staffModel = new StaffModel();
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, null, STAFF_ID + "=?",new String[] { staffId }, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {

				staffModel.setStaffId(cursor.getString(0));
				staffModel.setStaffName(cursor.getString(1));
				staffModel.setStaffPayGrade(cursor.getString(2));
				staffModel.setStaffLogin(cursor.getInt(3) == 0?false:true);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return staffModel;
	}

	public void deleteInfoFromTable(String staffId) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, STAFF_ID + " =? ", new String []{ staffId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}
}
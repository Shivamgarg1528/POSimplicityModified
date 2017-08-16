package com.posimplicity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.CategoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryTable {

	public static final String TABLE_NAME         = "CategoryTable";

	private final String DEPARTMENTAL_ID          = "DeptId";
	private final String DEPARTMENTAL_NAME        = "DeptName";
	private final String DEPARTMENTAL_STATUS      = "DeptStatus";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public CategoryTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ DEPARTMENTAL_ID       +" TEXT, "
					+ DEPARTMENTAL_NAME     +" TEXT, "
					+ DEPARTMENTAL_STATUS   +" TEXT, PRIMARY KEY ( "+ DEPARTMENTAL_ID + " ))";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addInfoInTable(CategoryModel categoryModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(DEPARTMENTAL_ID,			categoryModel.getDeptId());
			values.put(DEPARTMENTAL_NAME,       categoryModel.getDeptName());
			values.put(DEPARTMENTAL_STATUS,     categoryModel.getDepStatus());

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

	public void updateInfoInTable(CategoryModel categoryModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(DEPARTMENTAL_ID,			categoryModel.getDeptId());
			values.put(DEPARTMENTAL_NAME,       categoryModel.getDeptName());
			values.put(DEPARTMENTAL_STATUS,     categoryModel.getDepStatus());

			// Update Row
			posDataBase.update(TABLE_NAME, values, DEPARTMENTAL_ID +" =? ", new String[] { categoryModel.getDeptId() } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<CategoryModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				CategoryModel categoryModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(DEPARTMENTAL_ID,			categoryModel.getDeptId());
				values.put(DEPARTMENTAL_NAME,       categoryModel.getDeptName());
				values.put(DEPARTMENTAL_STATUS,     categoryModel.getDepStatus());

				// Update Row
				posDataBase.update(TABLE_NAME, values, DEPARTMENTAL_ID +" =? ", new String[] { categoryModel.getDeptId() } );

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<CategoryModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				CategoryModel categoryModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(DEPARTMENTAL_ID,			categoryModel.getDeptId());
				values.put(DEPARTMENTAL_NAME,       categoryModel.getDeptName());
				values.put(DEPARTMENTAL_STATUS,     categoryModel.getDepStatus());

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


	public List<CategoryModel> getAllInfoFromTable(){
		List<CategoryModel> listOfAllData = new ArrayList<CategoryModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					CategoryModel categoryModel = new CategoryModel();
					categoryModel.setDeptId(cursor.getString(0));
					categoryModel.setDeptName(cursor.getString(1));
					categoryModel.setDepStatus(cursor.getString(2));
					if (categoryModel.getDeptName().startsWith("@#@")) {
						continue;
					}
					else
						listOfAllData.add(categoryModel);
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
		Collections.sort(listOfAllData);
		
		return listOfAllData;
	}


	public CategoryModel getSingleInfoFromTableByDeptId(String deptId){

		CategoryModel categoryModel = new CategoryModel();
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, null, DEPARTMENTAL_ID + "=? AND "+DEPARTMENTAL_STATUS + " ?= " ,new String[] { deptId, "yes" }, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				categoryModel.setDeptId(cursor.getString(0));
				categoryModel.setDeptName(cursor.getString(1));
				categoryModel.setDepStatus(cursor.getString(2));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return categoryModel;
	}


	public void deleteInfoFromTable(String deptId) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, DEPARTMENTAL_ID + " =? ", new String []{ deptId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}
}

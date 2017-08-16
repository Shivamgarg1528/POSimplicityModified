package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.CommentModel;
import com.posimplicity.database.POSDatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class PayoutDescTable {

	public static final String TABLE_NAME     = "PayDescTable";

	private final String DESCRIP_ID           = "DescId";
	private final String DESCRIPTION___       = "DescString";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public PayoutDescTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ DESCRIP_ID            +" INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DESCRIPTION___        +" TEXT)";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public boolean addInfoInTable(CommentModel commentModel) {
		boolean recordInserted = false;
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();


			values.put(DESCRIPTION___,      commentModel.getCommentString());
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

	public void updateInfoInTable(CommentModel commentModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(DESCRIPTION___,      commentModel.getCommentString());// Update Row
			posDataBase.update(TABLE_NAME, values, DESCRIP_ID +" =? ", new String[] { ""+commentModel.getCommentId() } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<CommentModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				CommentModel commentModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(DESCRIPTION___,      commentModel.getCommentString());

				// Update Row
				posDataBase.update(TABLE_NAME, values, DESCRIP_ID +" =? ", new String[] { ""+commentModel.getCommentId() } );

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<CommentModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				CommentModel commentModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(DESCRIPTION___,      commentModel.getCommentString());

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


	public List<CommentModel> getAllInfoFromTable(boolean status){
		List<CommentModel> listOfAllData = new ArrayList<CommentModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					CommentModel commentModel = new CommentModel();
					commentModel.setCommentId(cursor.getInt(0));
					commentModel.setCommentString(cursor.getString(1));
					listOfAllData.add(commentModel);

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

	public List<CommentModel> getAllInfoFromTableDefalut(){
		List<CommentModel> listOfAllData = new ArrayList<CommentModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					CommentModel commentModel = new CommentModel();
					commentModel.setCommentId(cursor.getInt(0));
					commentModel.setCommentString(cursor.getString(1));
					listOfAllData.add(commentModel);

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

	public void deleteInfoFromTable(String commentId) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, DESCRIP_ID + " =? ", new String []{ commentId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}


}

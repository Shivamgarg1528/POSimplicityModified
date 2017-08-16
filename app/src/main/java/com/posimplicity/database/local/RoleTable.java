package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.RoleInfo;
import com.posimplicity.database.POSDatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class RoleTable {

	public static final String TABLE_NAME     = "RoleTable";

	private final String ROLE_NAME            = "RoleName";
	private final String ROLE_PASSWORD        = "RolePassword";
	private final String IS_ROLE_ACTIVE       = "IsRoleActive";
	private final String ROLE_ID              = "RoleId";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public RoleTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ ROLE_NAME             +" TEXT, "
					+ ROLE_PASSWORD         +" TEXT, "
					+ IS_ROLE_ACTIVE        +" TEXT, "
					+ ROLE_ID               +" TEXT )";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public boolean addInfoInTable(RoleInfo roleInfo) {
		boolean recordInserted = false;
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();


			values.put(ROLE_PASSWORD,       roleInfo.getRolePassword());
			values.put(IS_ROLE_ACTIVE,      roleInfo.isRoleActive());
			values.put(ROLE_NAME,           roleInfo.getRoleName());
			values.put(ROLE_ID,             roleInfo.getRoleId());

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

	public void updateInfoInTable(RoleInfo roleInfo) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(ROLE_PASSWORD,       roleInfo.getRolePassword());
			values.put(IS_ROLE_ACTIVE,      roleInfo.isRoleActive());
			values.put(ROLE_NAME,           roleInfo.getRoleName());
			values.put(ROLE_ID,             roleInfo.getRoleId());

			// Update Row
			posDataBase.update(TABLE_NAME, values, ROLE_NAME +" =? ", new String[] { roleInfo.getRoleName() } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<RoleInfo> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				RoleInfo roleInfo = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(ROLE_PASSWORD,       roleInfo.getRolePassword());
				values.put(IS_ROLE_ACTIVE,      roleInfo.isRoleActive());
				values.put(ROLE_NAME,           roleInfo.getRoleName());
				values.put(ROLE_ID,             roleInfo.getRoleId());

				// Update Row
				posDataBase.update(TABLE_NAME, values, ROLE_NAME +" =? ", new String[] { roleInfo.getRoleName() } );

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<RoleInfo> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				RoleInfo roleInfo = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(ROLE_PASSWORD,       roleInfo.getRolePassword());
				values.put(IS_ROLE_ACTIVE,      roleInfo.isRoleActive());
				values.put(ROLE_NAME,           roleInfo.getRoleName());
				values.put(ROLE_ID,             roleInfo.getRoleId());

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

	public List<RoleInfo> getAllInfoFromTableDefalut(boolean adminNeed){
		List<RoleInfo> listOfAllData = new ArrayList<RoleInfo>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					RoleInfo roleInfo = new RoleInfo();
					roleInfo.setRoleName(cursor.getString(0));
					roleInfo.setRolePassword(cursor.getString(1));
					roleInfo.setRoleActive(1 == cursor.getInt(2));
					roleInfo.setRoleId(cursor.getString(3));

					if(roleInfo.getRoleName().equals("Admin")){
						if(adminNeed)
							listOfAllData.add(roleInfo);
						else
							continue;
					}
					else
						listOfAllData.add(roleInfo);

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


	public RoleInfo getSingleInfoFromTableByRoleName(String roleName){

		RoleInfo roleInfo = new RoleInfo();
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, null, ROLE_NAME + "=?",new String[] { roleName }, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				roleInfo.setRoleName(cursor.getString(0));
				roleInfo.setRolePassword(cursor.getString(1));
				roleInfo.setRoleActive(1 == cursor.getInt(2));
				roleInfo.setRoleId(cursor.getString(3));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return roleInfo;
	}

	public void deleteInfoFromTable(String roleName) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, ROLE_NAME + " =? ", new String []{ roleName });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}
}
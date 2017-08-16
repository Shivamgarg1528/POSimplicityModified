package com.posimplicity.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.Beans.CustomerModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CustomerTable {

	public static final String DEFAULT_GROUP_ID = "1";
	public static final String TABLE_NAME       = "CustomerTable";

	private final String CUSTOMER_ID          = "CustomerId";
	private final String FIRST_NAME           = "FirstName";
	private final String LAST_NAME            = "LastName";
	private final String EMAIL_ADDDRESS       = "EmailAddress";
	private final String CUSTOMER_ADDRESS     = "CustomerAddress";
	private final String TELEPHONE_NO         = "TelePhoneNo";
	private final String GROUP_ID             = "GroupId";
	private final String CUSTOMER_LOGIN_STA   = "CustomerLoginStatus";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public CustomerTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ CUSTOMER_ID           +" TEXT, "
					+ FIRST_NAME            +" TEXT, "
					+ LAST_NAME             +" TEXT, "
					+ EMAIL_ADDDRESS        +" TEXT, "
					+ CUSTOMER_ADDRESS      +" TEXT, "
					+ TELEPHONE_NO          +" TEXT, "
					+ GROUP_ID              +" TEXT, "
					+ CUSTOMER_LOGIN_STA    +" TEXT, PRIMARY KEY ( "+ CUSTOMER_ID + " ))";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean addInfoInTable(CustomerModel customerModel) {
		boolean recordInserted = false;
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(CUSTOMER_ID,			customerModel.getCustomerId());
			values.put(FIRST_NAME,          customerModel.getFirstName());
			values.put(LAST_NAME,           customerModel.getLastName());
			values.put(EMAIL_ADDDRESS,      customerModel.getEmailAddress());
			values.put(CUSTOMER_ADDRESS,    customerModel.getPermanantAddress());
			values.put(TELEPHONE_NO,        customerModel.getTelephoneNo());
			values.put(GROUP_ID,            customerModel.getGroupId());
			values.put(CUSTOMER_LOGIN_STA,  customerModel.isCustomerLogin());

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

	public void updateInfoInTable(CustomerModel customerModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(CUSTOMER_ID,			customerModel.getCustomerId());
			values.put(FIRST_NAME,          customerModel.getFirstName());
			values.put(LAST_NAME,           customerModel.getLastName());
			values.put(EMAIL_ADDDRESS,      customerModel.getEmailAddress());
			values.put(CUSTOMER_ADDRESS,    customerModel.getPermanantAddress());
			values.put(TELEPHONE_NO,        customerModel.getTelephoneNo());
			values.put(GROUP_ID,            customerModel.getGroupId());
			values.put(CUSTOMER_LOGIN_STA,  customerModel.isCustomerLogin());

			// Update Row
			posDataBase.update(TABLE_NAME, values, CUSTOMER_ID +" =? ", new String[] { customerModel.getCustomerId() } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<CustomerModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				CustomerModel customerModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(CUSTOMER_ID,			customerModel.getCustomerId());
				values.put(FIRST_NAME,          customerModel.getFirstName());
				values.put(LAST_NAME,           customerModel.getLastName());
				values.put(EMAIL_ADDDRESS,      customerModel.getEmailAddress());
				values.put(CUSTOMER_ADDRESS,    customerModel.getPermanantAddress());
				values.put(TELEPHONE_NO,        customerModel.getTelephoneNo());
				values.put(GROUP_ID,            customerModel.getGroupId());
				values.put(CUSTOMER_LOGIN_STA,  customerModel.isCustomerLogin());

				// Update Row
				posDataBase.update(TABLE_NAME, values, CUSTOMER_ID +" =? ", new String[] { customerModel.getCustomerId() } );

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<CustomerModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				CustomerModel customerModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(CUSTOMER_ID,			customerModel.getCustomerId());
				values.put(FIRST_NAME,          customerModel.getFirstName());
				values.put(LAST_NAME,           customerModel.getLastName());
				values.put(EMAIL_ADDDRESS,      customerModel.getEmailAddress());
				values.put(CUSTOMER_ADDRESS,    customerModel.getPermanantAddress());
				values.put(TELEPHONE_NO,        customerModel.getTelephoneNo());
				values.put(GROUP_ID,            customerModel.getGroupId());
				values.put(CUSTOMER_LOGIN_STA,  customerModel.isCustomerLogin());

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
					customerModel.setLastName(cursor.getString(2));
					customerModel.setEmailAddress(cursor.getString(3));
					customerModel.setPermanantAddress(cursor.getString(4));
					customerModel.setTelephoneNo(cursor.getString(5));
					customerModel.setGroupId(cursor.getString(6));
					customerModel.setCustomerLogin(cursor.getInt(7) == 1);
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

	public CustomerModel getSingleInfoFromTableBasedOnGroupId(){
		CustomerModel customerModel = new CustomerModel();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,GROUP_ID + " !=? ", new String[]{ CustomerTable.DEFAULT_GROUP_ID }, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					customerModel.setCustomerId(cursor.getString(0));
					customerModel.setFirstName(cursor.getString(1));
					customerModel.setLastName(cursor.getString(2));
					customerModel.setEmailAddress(cursor.getString(3));
					customerModel.setPermanantAddress(cursor.getString(4));
					customerModel.setTelephoneNo(cursor.getString(5));
					customerModel.setGroupId(cursor.getString(6));
					customerModel.setCustomerLogin(cursor.getInt(7) == 1);

					if(customerModel.isCustomerLogin())
						break;

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
		return customerModel;
	}
	public List<CustomerModel> getInfoFromTableBasedOnGroupId(){
		List<CustomerModel> listOfAllData = new ArrayList<CustomerModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,GROUP_ID + " !=? ", new String[]{ CustomerTable.DEFAULT_GROUP_ID }, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					CustomerModel customerModel = new CustomerModel();
					customerModel.setCustomerId(cursor.getString(0));
					customerModel.setFirstName(cursor.getString(1));
					customerModel.setLastName(cursor.getString(2));
					customerModel.setEmailAddress(cursor.getString(3));
					customerModel.setPermanantAddress(cursor.getString(4));
					customerModel.setTelephoneNo(cursor.getString(5));
					customerModel.setGroupId(cursor.getString(6));
					customerModel.setCustomerLogin(cursor.getInt(7) == 1);
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

	public CustomerModel getInfoFromTableBasedOnLastRecord(){
		CustomerModel customerModel = new CustomerModel();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null,null, null, null, CUSTOMER_ID + " ASC");

			if (posDbHandler.cusorIsFine(cursor)) {
				cursor.moveToLast();
				customerModel.setCustomerId(cursor.getString(0));
				customerModel.setFirstName(cursor.getString(1));
				customerModel.setLastName(cursor.getString(2));
				customerModel.setEmailAddress(cursor.getString(3));
				customerModel.setPermanantAddress(cursor.getString(4));
				customerModel.setTelephoneNo(cursor.getString(5));
				customerModel.setGroupId(cursor.getString(6));
				customerModel.setCustomerLogin(cursor.getInt(7) == 1);
			}
			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		return customerModel;
	}

	public List<CustomerModel> getInfoFromTableBasedOnLoginStatus(boolean loginStatus){

		List<CustomerModel> listOfAllData = new ArrayList<CustomerModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null,null, null, null, null);
			if (posDbHandler.cusorIsFine(cursor)) {
				cursor.moveToFirst();
				do {
					CustomerModel customerModel = new CustomerModel();
					customerModel.setCustomerId(cursor.getString(0));
					customerModel.setFirstName(cursor.getString(1));
					customerModel.setLastName(cursor.getString(2));
					customerModel.setEmailAddress(cursor.getString(3));
					customerModel.setPermanantAddress(cursor.getString(4));
					customerModel.setTelephoneNo(cursor.getString(5));
					customerModel.setGroupId(cursor.getString(6));
					customerModel.setCustomerLogin(cursor.getInt(7) == 1);
					
					if((customerModel.isCustomerLogin() == loginStatus) && !customerModel.getGroupId().equalsIgnoreCase(CustomerTable.DEFAULT_GROUP_ID))
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


	public CustomerModel getSingleInfoFromTableByCustomerId(String customerId){

		CustomerModel customerModel = new CustomerModel();
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, null, CUSTOMER_ID + "=?",new String[] { customerId }, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				customerModel.setCustomerId(cursor.getString(0));
				customerModel.setFirstName(cursor.getString(1));
				customerModel.setLastName(cursor.getString(2));
				customerModel.setEmailAddress(cursor.getString(3));
				customerModel.setPermanantAddress(cursor.getString(4));
				customerModel.setTelephoneNo(cursor.getString(5));
				customerModel.setGroupId(cursor.getString(6));
				customerModel.setCustomerLogin(cursor.getInt(7) == 1);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return customerModel;
	}

	public CustomerModel getSingleInfoFromTableByPhoneNo(String phoneNo){
		CustomerModel customerModel = new CustomerModel();
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, null, TELEPHONE_NO + "=?",new String[] { phoneNo }, null, null, null, null);
			if (cursor != null  && cursor.moveToFirst()) {

				customerModel.setCustomerId(cursor.getString(0));
				customerModel.setFirstName(cursor.getString(1));
				customerModel.setLastName(cursor.getString(2));
				customerModel.setEmailAddress(cursor.getString(3));
				customerModel.setPermanantAddress(cursor.getString(4));
				customerModel.setTelephoneNo(cursor.getString(5));
				customerModel.setGroupId(cursor.getString(6));
				customerModel.setCustomerLogin(cursor.getInt(7) == 1);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return customerModel;
	}

	public void deleteInfoListFromTable(String customerIds) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			List<String> customerIdList  = Arrays.asList(customerIds.split(","));
			for (String customerId : customerIdList) 
				posDataBase.delete(TABLE_NAME, CUSTOMER_ID + " =? ", new String []{ customerId });

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

	public void deleteInfoFromTable(String customerId) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, CUSTOMER_ID + " =? ", new String []{ customerId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}
}
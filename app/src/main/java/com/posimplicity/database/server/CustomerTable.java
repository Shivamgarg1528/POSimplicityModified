package com.posimplicity.database.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.response.api.CustomerParent;

import java.util.ArrayList;

public class CustomerTable extends BaseTable<CustomerParent.Customer> {

    public static final String DEFAULT_GROUP_ID = "1";
    public static final String TABLE_NAME = "CustomerTable";

    static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String CUSTOMER_ID = "CustomerId";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String EMAIL_ADDDRESS = "EmailAddress";
    private static final String CUSTOMER_ADDRESS = "CustomerAddress";
    private static final String TELEPHONE_NO = "TelePhoneNo";
    private static final String GROUP_ID = "GroupId";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ( "
                + CUSTOMER_ID + " TEXT, "
                + FIRST_NAME + " TEXT, "
                + LAST_NAME + " TEXT, "
                + EMAIL_ADDDRESS + " TEXT, "
                + CUSTOMER_ADDRESS + " TEXT, "
                + TELEPHONE_NO + " TEXT, "
                + GROUP_ID + " TEXT)";
    }

    public CustomerTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(CustomerParent.Customer pNewOrUpdatedModel, CustomerParent.Customer pExistingModel) {
        ContentValues values = new ContentValues();

        values.put(CUSTOMER_ID, pNewOrUpdatedModel.getCustomerId());
        values.put(FIRST_NAME, pNewOrUpdatedModel.getCustomerFirstName());
        values.put(LAST_NAME, pNewOrUpdatedModel.getCustomerLastName());
        values.put(EMAIL_ADDDRESS, pNewOrUpdatedModel.getCustomerEmail());
        values.put(CUSTOMER_ADDRESS, pNewOrUpdatedModel.getCustomerAddress());
        values.put(TELEPHONE_NO, pNewOrUpdatedModel.getCustomerTelephone());
        values.put(GROUP_ID, pNewOrUpdatedModel.getCustomerGroupId());
        return values;
    }

    @Override
    protected ArrayList<CustomerParent.Customer> getAllData(String pSelection, String[] pSelectionArgs) {
        ArrayList<CustomerParent.Customer> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, pSelection, pSelectionArgs, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                CustomerParent.Customer customer = new CustomerParent.Customer();
                customer.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                customer.setCustomerFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
                customer.setCustomerLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
                customer.setCustomerEmail(cursor.getString(cursor.getColumnIndex(EMAIL_ADDDRESS)));
                customer.setCustomerAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                customer.setCustomerTelephone(cursor.getString(cursor.getColumnIndex(TELEPHONE_NO)));
                customer.setCustomerGroupId(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
                dataList.add(customer);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }
}

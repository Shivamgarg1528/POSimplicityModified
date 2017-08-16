package com.posimplicity.database.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.response.api.CustomerGroupParent;

import java.util.ArrayList;


public class CustomerGroupTable extends BaseTable<CustomerGroupParent.CustomerGroup> {

    public static final String TABLE_NAME = "CustomerGroupTable";

    static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String GROUP_ID = "GroupId";
    private static final String GROUP_NAME = "GroupName";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ( "
                + GROUP_ID + " TEXT, "
                + GROUP_NAME + " TEXT)";
    }

    public CustomerGroupTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(CustomerGroupParent.CustomerGroup pNewOrUpdatedModel, CustomerGroupParent.CustomerGroup pExistingModel) {
        ContentValues values = new ContentValues();
        values.put(GROUP_ID, pNewOrUpdatedModel.getCustomerGroupId());
        values.put(GROUP_NAME, pNewOrUpdatedModel.getCustomerGroupCode());
        return values;
    }

    @Override
    protected ArrayList<CustomerGroupParent.CustomerGroup> getAllData(String pSelection, String[] pSelectionArgs) {
        ArrayList<CustomerGroupParent.CustomerGroup> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, pSelection, pSelectionArgs, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                CustomerGroupParent.CustomerGroup customerGroup = new CustomerGroupParent.CustomerGroup();
                customerGroup.setCustomerGroupId(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
                customerGroup.setCustomerGroupCode(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
                dataList.add(customerGroup);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }
}

package com.posimplicity.database.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.response.api.CategoryParent;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryTable extends BaseTable<CategoryParent.Category> {

    private static final String TABLE_NAME = "CategoryTable";

    static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String DEPARTMENTAL_ID = "DeptId";
    private static final String DEPARTMENTAL_NAME = "DeptName";
    private static final String DEPARTMENTAL_STATUS = "DeptStatus";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ( "
                + DEPARTMENTAL_ID + " TEXT, "
                + DEPARTMENTAL_NAME + " TEXT, "
                + DEPARTMENTAL_STATUS + " TEXT)";
    }

    public CategoryTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(CategoryParent.Category pNewOrUpdatedModel, CategoryParent.Category pExistingModel) {
        ContentValues values = new ContentValues();
        values.put(DEPARTMENTAL_ID, pNewOrUpdatedModel.getDeptId());
        values.put(DEPARTMENTAL_NAME, pNewOrUpdatedModel.getDeptName());
        values.put(DEPARTMENTAL_STATUS, pNewOrUpdatedModel.getIsDeptActive());
        return values;
    }

    @Override
    protected ArrayList<CategoryParent.Category> getAllData(String pSelection, String[] pSelectionArgs) {
        ArrayList<CategoryParent.Category> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, pSelection, pSelectionArgs, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                CategoryParent.Category category = new CategoryParent.Category();
                category.setDeptId(cursor.getString(cursor.getColumnIndex(DEPARTMENTAL_ID)));
                category.setDeptName(cursor.getString(cursor.getColumnIndex(DEPARTMENTAL_NAME)));
                category.setIsDeptActive(cursor.getString(cursor.getColumnIndex(DEPARTMENTAL_STATUS)));
                dataList.add(category);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        // Sorting collection based on deptID
        Collections.sort(dataList);
        return dataList;
    }
}

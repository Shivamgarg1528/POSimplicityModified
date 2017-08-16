package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.posimplicity.database.server.DbHelper;
import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.local.RoleParent;

import java.util.ArrayList;

public class PosRoleTable extends BaseTable<RoleParent.RoleModel> {

    private static final String TABLE_NAME = "RoleTable";

    public static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String ROLE_NAME = "RoleName";
    private static final String ROLE_PASSWORD = "RolePassword";
    public  static final String IS_ROLE_ACTIVE = "IsRoleActive";
    private static final String ROLE_ID = "RoleId";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ( "
                + ROLE_NAME + " TEXT, "
                + ROLE_PASSWORD + " TEXT, "
                + IS_ROLE_ACTIVE + " TEXT, "
                + ROLE_ID + " TEXT )";
    }

    public PosRoleTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(RoleParent.RoleModel pNewOrUpdatedModel, RoleParent.RoleModel pExistingModel) {
        ContentValues values = new ContentValues();
        values.put(ROLE_PASSWORD, pNewOrUpdatedModel.getRolePassword());
        values.put(IS_ROLE_ACTIVE, pNewOrUpdatedModel.isRoleActive());
        values.put(ROLE_NAME, pNewOrUpdatedModel.getRoleName());
        values.put(ROLE_ID, pNewOrUpdatedModel.getRoleId());
        return values;
    }

    @Override
    public ArrayList<RoleParent.RoleModel> getAllData(String pSelection, String[] pSelectionArgs) {
        ArrayList<RoleParent.RoleModel> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, pSelection, pSelectionArgs, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                RoleParent.RoleModel roleModel = new RoleParent.RoleModel();
                roleModel.setRoleName(cursor.getString(cursor.getColumnIndex(ROLE_NAME)));
                roleModel.setRolePassword(cursor.getString(cursor.getColumnIndex(ROLE_PASSWORD)));
                roleModel.setRoleActive(1 == cursor.getInt(cursor.getColumnIndex(IS_ROLE_ACTIVE)));
                roleModel.setRoleId(cursor.getString(cursor.getColumnIndex(ROLE_ID)));
                dataList.add(roleModel);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }
}

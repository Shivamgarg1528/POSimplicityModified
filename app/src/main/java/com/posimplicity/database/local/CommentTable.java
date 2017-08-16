package com.posimplicity.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.posimplicity.database.server.DbHelper;
import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.local.CommentModel;

import java.util.ArrayList;

public class CommentTable extends BaseTable<CommentModel> {

    public static final String COMMENT_ORDER = "OrderComment";
    public static final String COMMENT_PAYOUT = "PayoutComment";

    public static final String TABLE_NAME = "CommentTable";
    public static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String COMMENT_STRING = "CommentString";
    private static final String COMMENT_TYPE = "CommentType";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + COMMENT_TYPE + " TEXT ,"
                + COMMENT_STRING + " TEXT)";
    }

    public CommentTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(CommentModel pNewOrUpdatedModel, CommentModel pExistingModel) {
        ContentValues values = new ContentValues();
        values.put(COMMENT_STRING, pNewOrUpdatedModel.getCommentString());
        values.put(COMMENT_TYPE, pNewOrUpdatedModel.getCommentType());
        return values;
    }

    @Override
    protected ArrayList<CommentModel> getAllData(String pSelection, String[] pSelectionArgs) {
        ArrayList<CommentModel> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, pSelection, pSelectionArgs, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                CommentModel commentModel = new CommentModel();
                commentModel.setCommentString(cursor.getString(cursor.getColumnIndex(COMMENT_STRING)));
                commentModel.setCommentType(cursor.getString(cursor.getColumnIndex(COMMENT_TYPE)));
                dataList.add(commentModel);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }

    public ArrayList<CommentModel> getAllData(String pCommentType) {
        ArrayList<CommentModel> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, CommentTable.COMMENT_TYPE + "=?", new String[]{pCommentType}, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                CommentModel commentModel = new CommentModel();
                commentModel.setCommentString(cursor.getString(cursor.getColumnIndex(COMMENT_STRING)));
                commentModel.setCommentType(cursor.getString(cursor.getColumnIndex(COMMENT_TYPE)));
                dataList.add(commentModel);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }
}

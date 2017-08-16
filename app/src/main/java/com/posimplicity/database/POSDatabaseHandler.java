package com.posimplicity.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.posimplicity.database.local.CommentTable;
import com.posimplicity.database.local.PaymentModeTable;
import com.posimplicity.database.local.PayoutDescTable;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.database.local.SecurityTable;
import com.posimplicity.database.local.StaffTable;
import com.posimplicity.database.local.TipTable;
import com.posimplicity.database.local.TransactionTable;


public class POSDatabaseHandler extends SQLiteOpenHelper {

    // DataBase Version and Database Name
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "POSimplicity.db";

    private SQLiteDatabase mSqLiteDatabase;

    public POSDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public synchronized SQLiteDatabase openWritableDataBase() throws SQLException {
        if (mSqLiteDatabase == null || !mSqLiteDatabase.isOpen())
            mSqLiteDatabase = getWritableDatabase();
        return mSqLiteDatabase;
    }

    public synchronized SQLiteDatabase openReadableDataBase() throws SQLException {
        if (mSqLiteDatabase == null || !mSqLiteDatabase.isOpen())
            mSqLiteDatabase = getReadableDatabase();
        return mSqLiteDatabase;
    }

    public synchronized boolean closeDataBase() throws SQLException {
        if (mSqLiteDatabase != null && mSqLiteDatabase.isOpen()) {
            mSqLiteDatabase.close();
            return true;
        }
        return false;
    }

    public synchronized boolean cusorIsFine(Cursor cursor) {
        return cursor != null && cursor.getCount() > 0;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            mSqLiteDatabase = db;
            //new PosCategoryTable(this).createSchemaOfTable();
           /* new CustomerTable(mContext).createSchemaOfTable(db);
            //new CategoryTable(mContext).createSchemaOfTable(db);
            new ProductOptionTable(mContext).createSchemaOfTable(db);
            new ProductTable(mContext).createSchemaOfTable(db);
            new ReportsTable(mContext).createSchemaOfTable(db);
            new SecurityTable(mContext).createSchemaOfTable(db);
            new StaffTable(mContext).createSchemaOfTable(db);
            new RoleTable(mContext).createSchemaOfTable(db);
            new CommentTable(mContext).createSchemaOfTable(db);
            new PayoutDescTable(mContext).createSchemaOfTable(db);
            new TipTable(mContext).createSchemaOfTable(db);
            new CustomerGroupTable(mContext).createSchemaOfTable(db);
            new TransactionTable(mContext).createSchemaOfTable(db);
            new PaymentModeTable(mContext).createSchemaOfTable(db);
            new OrderTable(mContext).createSchemaOfTable(db);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mSqLiteDatabase = db;
        dropTable(db, CustomerTable.TABLE_NAME);
        dropTable(db, CategoryTable.TABLE_NAME);
        dropTable(db, ProductOptionTable.TABLE_NAME);
        dropTable(db, ProductTable.TABLE_NAME);
        dropTable(db, ReportsTable.TABLE_NAME);
        dropTable(db, SecurityTable.TABLE_NAME);
        dropTable(db, StaffTable.TABLE_NAME);
        dropTable(db, StaffTable.TABLE_NAME);
        dropTable(db, CommentTable.TABLE_NAME);
        dropTable(db, TipTable.TABLE_NAME);
        dropTable(db, PayoutDescTable.TABLE_NAME);
        dropTable(db, CustomerGroupTable.TABLE_NAME);
        dropTable(db, TransactionTable.TABLE_NAME);
        dropTable(db, PaymentModeTable.TABLE_NAME);
        //dropTable(db, OrderTable.TABLE_NAME);

        onCreate(db);
    }

    private void dropTable(SQLiteDatabase db, String DATABASE_TABLE) {
        db.execSQL("DROP TABLE IF EXISTS '" + DATABASE_TABLE + "'");
    }

    public void deleteTableInfo(SQLiteDatabase db) {

        db.delete(CustomerTable.TABLE_NAME, "1", null);
        db.delete(CategoryTable.TABLE_NAME, "1", null);
        db.delete(ProductOptionTable.TABLE_NAME, "1", null);
        db.delete(ProductTable.TABLE_NAME, "1", null);
        db.delete(CustomerGroupTable.TABLE_NAME, "1", null);
    }

    public static POSDatabaseHandler getInstance(Context context) {
        return null;
    }
}


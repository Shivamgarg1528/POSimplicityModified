package com.posimplicity.database.server;

import android.database.sqlite.SQLiteDatabase;

import com.posimplicity.database.local.CommentTable;
import com.posimplicity.database.local.PosReportTable;
import com.posimplicity.database.local.PosRoleTable;
import com.easylibs.sqlite.EasySQLiteHelper;

public class DbHelper implements EasySQLiteHelper {

    private static DbHelper sInstance;

    /**
     * NO synchronization, as even singleton is not strictly required
     *
     * @return
     */
    public static DbHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DbHelper();
        }
        return sInstance;
    }

    /**
     * private constructor for singleton
     */
    private DbHelper() {
        //  nothing to do here
    }

    @Override
    public String getDbName() {
        return "POSimplicity.DB";
    }

    @Override
    public int getDbVersion() {
        return 1;
    }

    @Override
    public String[] getCreateTableQueries() {
        return new String[]{

                CategoryTable.SQL_CREATE_TABLE,
                ProductTable.SQL_CREATE_TABLE,
                ProductOptionTable.SQL_CREATE_TABLE,
                CustomerTable.SQL_CREATE_TABLE,
                CustomerGroupTable.SQL_CREATE_TABLE,

                PosRoleTable.SQL_CREATE_TABLE,
                PosReportTable.SQL_CREATE_TABLE,
                CommentTable.SQL_CREATE_TABLE
        };
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

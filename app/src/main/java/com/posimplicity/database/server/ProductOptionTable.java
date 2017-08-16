package com.posimplicity.database.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.local.CheckoutParent;
import com.posimplicity.model.response.api.ProductOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductOptionTable extends BaseTable<ProductOption> {

    private static final String TABLE_NAME = "ProductOptionTable";

    static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String PRODUCT_ID = "ProductId";
    private static final String OPTION_ID = "OptionId";
    private static final String OPTION_NAME = "OptionName";
    private static final String OPTION_SORT_ORDER = "OptionSortOrder";
    private static final String SUB_OPTION_ID = "SubOptionId";
    private static final String SUB_OPTION_NAME = "SubOptionName";
    private static final String SUB_OPTION_PRICE = "SubOptionPrice";
    private static final String SUB_OPTION_SORT_ORDER = "SubOptionSortOrder";
    private static final String OPTION_ENABLE = "OptionEnable";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ( "
                + PRODUCT_ID + " TEXT, "
                + OPTION_ID + " TEXT, "
                + OPTION_NAME + " TEXT, "
                + OPTION_SORT_ORDER + " TEXT, "
                + SUB_OPTION_ID + " TEXT, "
                + SUB_OPTION_NAME + " TEXT, "
                + SUB_OPTION_PRICE + " TEXT, "
                + SUB_OPTION_SORT_ORDER + " TEXT, "
                + OPTION_ENABLE + " TEXT )";
    }

    public ProductOptionTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(ProductOption pNewOrUpdatedModel, ProductOption pExistingModel) {

        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, pNewOrUpdatedModel.getProductId());
        values.put(OPTION_ID, pNewOrUpdatedModel.getOptionId());
        values.put(OPTION_NAME, pNewOrUpdatedModel.getOptionName());
        values.put(OPTION_SORT_ORDER, pNewOrUpdatedModel.getOptionSortOrder());
        values.put(SUB_OPTION_ID, pNewOrUpdatedModel.getSubOptionIds());
        values.put(SUB_OPTION_NAME, pNewOrUpdatedModel.getSubOptionNames());
        values.put(SUB_OPTION_PRICE, pNewOrUpdatedModel.getSubOptionPrices());
        values.put(SUB_OPTION_SORT_ORDER, pNewOrUpdatedModel.getSubOptionSortOrder());
        values.put(OPTION_ENABLE, pNewOrUpdatedModel.getOptionRequire());
        return values;
    }

    @Override
    protected ArrayList<ProductOption> getAllData(String pSelection, String[] pSelectionArgs) {
        ArrayList<ProductOption> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, pSelection, pSelectionArgs, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                ProductOption productModel = new ProductOption();
                productModel.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                productModel.setOptionId(cursor.getString(cursor.getColumnIndex(OPTION_ID)));
                productModel.setOptionName(cursor.getString(cursor.getColumnIndex(OPTION_NAME)));
                productModel.setOptionSortOrder(cursor.getString(cursor.getColumnIndex(OPTION_SORT_ORDER)));
                productModel.setSubOptionIds(cursor.getString(cursor.getColumnIndex(SUB_OPTION_ID)));
                productModel.setSubOptionNames(cursor.getString(cursor.getColumnIndex(SUB_OPTION_NAME)));
                productModel.setSubOptionPrices(cursor.getString(cursor.getColumnIndex(SUB_OPTION_PRICE)));
                productModel.setSubOptionSortOrder(cursor.getString(cursor.getColumnIndex(SUB_OPTION_SORT_ORDER)));
                productModel.setOptionRequire(cursor.getString(cursor.getColumnIndex(OPTION_ENABLE)));
                dataList.add(productModel);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }

    public List<CheckoutParent.ProductOptions> getProductOptions(String pProductId) {
        List<CheckoutParent.ProductOptions> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            cursor = mWritableDatabase.query(TABLE_NAME, null, PRODUCT_ID + " =? ", new String[]{pProductId}, null, null, null, null);
            if (cursor.getCount() <= 0) {
                return dataList;
            }
            while (cursor.moveToNext()) {

                CheckoutParent.ProductOptions productOptionsModel = new CheckoutParent.ProductOptions();
                productOptionsModel.setOptionId(cursor.getInt(cursor.getColumnIndex(OPTION_ID)));
                productOptionsModel.setOptionName(cursor.getString(cursor.getColumnIndex(OPTION_NAME)));
                productOptionsModel.setOptionSortOrder(cursor.getInt(cursor.getColumnIndex(OPTION_SORT_ORDER)));
                productOptionsModel.setOptionMandatory(cursor.getInt(cursor.getColumnIndex(OPTION_NAME)) == 1);

                List<CheckoutParent.ProductOptions.OptionSubOptions> subOptionsList = new ArrayList<>();

                List<String> subOptionIds = Arrays.asList(cursor.getString(cursor.getColumnIndex(SUB_OPTION_ID)).split(","));
                List<String> subOptionNames = Arrays.asList(cursor.getString(cursor.getColumnIndex(SUB_OPTION_NAME)).split(","));
                List<String> subOptionPrices = Arrays.asList(cursor.getString(cursor.getColumnIndex(SUB_OPTION_PRICE)).split(","));
                List<String> subOptionSortOrders = Arrays.asList(cursor.getString(cursor.getColumnIndex(SUB_OPTION_SORT_ORDER)).split(","));

                for (int index = 0; index < subOptionIds.size(); index++) {
                    String subOptionId = subOptionIds.get(index);
                    String subOptionName = subOptionNames.get(index);
                    String subOptionPrice = subOptionPrices.get(index);
                    String subOptionSortOrder = subOptionSortOrders.get(index);

                    CheckoutParent.ProductOptions.OptionSubOptions subOptionInstance = new CheckoutParent.ProductOptions.OptionSubOptions();
                    subOptionInstance.setSubOptionId(Integer.parseInt(subOptionId));
                    subOptionInstance.setSubOptionName(subOptionName);
                    subOptionInstance.setSubOptionPrice(Float.parseFloat(subOptionPrice));
                    subOptionInstance.setSubOptionSortOrder(Integer.parseInt(subOptionSortOrder));
                    subOptionsList.add(subOptionInstance);
                }
                productOptionsModel.setOptionSubOptions(subOptionsList);
                dataList.add(productOptionsModel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        Collections.sort(dataList);
        return dataList;
    }
}
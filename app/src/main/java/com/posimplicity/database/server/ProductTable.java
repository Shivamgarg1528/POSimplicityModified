package com.posimplicity.database.server;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.easylibs.sqlite.BaseTable;
import com.posimplicity.model.response.api.ProductParent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductTable extends BaseTable<ProductParent.Product> {

    private static final String TABLE_NAME = "ProductTable";

    static final String SQL_CREATE_TABLE;

    // Column Names
    private static final String PRODUCT_ID = "ProductId";
    private static final String PRODUCT_SKU = "ProductSku";
    private static final String PRODUCT_IMAGE_URL = "ProductImageUrl";
    private static final String PRODUCT_IMAGE_TEXT = "ProductImageText";
    private static final String PRODUCT_WEIGHT = "ProductWeight";
    private static final String PRODUCT_DESCRIPTION = "ProductDescription";
    private static final String PRODUCT_CREATED_AT = "ProductCreatedAt";
    private static final String PRODUCT_UPDATED_AT = "ProductUpdatedAt";
    private static final String PRODUCT_PRICE = "ProductPrice";
    private static final String PRODUCT_SPECIAL_PRICE = "ProductSpecialPrice";
    private static final String PRODUCT_TAX_CLASS_ID = "ProductTaxClassId";
    private static final String PRODUCT_CAT_ID = "ProductCategoryId";
    private static final String PRODUCT_POSITION = "ProductPosition";
    private static final String PRODUCT_NAME = "ProductName";
    private static final String PRODUCT_TAX_RATE = "ProductTaxRate";
    private static final String PRODUCT_IS_ACTIVE = "ProductIsActive";
    private static final String PRODUCT_IMAGE_SHOWN = "ProductImageShown";

    static {
        SQL_CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ( "
                + PRODUCT_ID + " TEXT, "
                + PRODUCT_SKU + " TEXT, "
                + PRODUCT_IMAGE_URL + " TEXT, "
                + PRODUCT_IMAGE_TEXT + " TEXT, "
                + PRODUCT_WEIGHT + " TEXT, "
                + PRODUCT_DESCRIPTION + " TEXT, "
                + PRODUCT_CREATED_AT + " TEXT, "
                + PRODUCT_UPDATED_AT + " TEXT, "
                + PRODUCT_PRICE + " TEXT, "
                + PRODUCT_SPECIAL_PRICE + " TEXT, "
                + PRODUCT_TAX_CLASS_ID + " TEXT, "
                + PRODUCT_CAT_ID + " TEXT, "
                + PRODUCT_POSITION + " TEXT, "
                + PRODUCT_NAME + " TEXT, "
                + PRODUCT_TAX_RATE + " TEXT, "
                + PRODUCT_IS_ACTIVE + " TEXT, "
                + PRODUCT_IMAGE_SHOWN + " TEXT)";
    }

    public ProductTable(Context pContext) {
        super(pContext, DbHelper.getInstance(), TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(ProductParent.Product pNewOrUpdatedModel, ProductParent.Product pExistingModel) {
        ContentValues values = new ContentValues();

        values.put(PRODUCT_ID, pNewOrUpdatedModel.getProductId());
        values.put(PRODUCT_SKU, pNewOrUpdatedModel.getProductSku());
        values.put(PRODUCT_IMAGE_URL, pNewOrUpdatedModel.getProductImage());
        values.put(PRODUCT_IMAGE_TEXT, pNewOrUpdatedModel.getProductImageText());
        values.put(PRODUCT_WEIGHT, pNewOrUpdatedModel.getProductWeight());
        values.put(PRODUCT_DESCRIPTION, pNewOrUpdatedModel.getProductDescription());
        values.put(PRODUCT_CREATED_AT, pNewOrUpdatedModel.getProductCreatedAt());
        values.put(PRODUCT_UPDATED_AT, pNewOrUpdatedModel.getProductUpdatedAt());
        values.put(PRODUCT_PRICE, pNewOrUpdatedModel.getProductPrice());
        values.put(PRODUCT_SPECIAL_PRICE, pNewOrUpdatedModel.getProductSpecialPrice());
        values.put(PRODUCT_TAX_CLASS_ID, pNewOrUpdatedModel.getProductTaxClassId());
        values.put(PRODUCT_CAT_ID, pNewOrUpdatedModel.getProductCatId());
        values.put(PRODUCT_POSITION, pNewOrUpdatedModel.getProductPosition());
        values.put(PRODUCT_NAME, pNewOrUpdatedModel.getProductName());
        values.put(PRODUCT_TAX_RATE, pNewOrUpdatedModel.getProductTaxRate());
        values.put(PRODUCT_IS_ACTIVE, pNewOrUpdatedModel.getProductIsActive());
        values.put(PRODUCT_IMAGE_SHOWN, pNewOrUpdatedModel.getProductImageShown());

        return values;
    }

    @Override
    protected ArrayList<ProductParent.Product> getAllData(String pSelection, String[] pSelectionArgs) {
        ArrayList<ProductParent.Product> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            // query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
            cursor = mWritableDatabase.query(mTableName, null, pSelection, pSelectionArgs, null, null, null);

            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                ProductParent.Product productModel = new ProductParent.Product();
                productModel.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                productModel.setProductSku(cursor.getString(cursor.getColumnIndex(PRODUCT_SKU)));
                productModel.setProductImage(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE_URL)));
                productModel.setProductImageText(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE_TEXT)));
                productModel.setProductWeight(cursor.getString(cursor.getColumnIndex(PRODUCT_WEIGHT)));
                productModel.setProductDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)));
                productModel.setProductCreatedAt(cursor.getString(cursor.getColumnIndex(PRODUCT_CREATED_AT)));
                productModel.setProductUpdatedAt(cursor.getString(cursor.getColumnIndex(PRODUCT_UPDATED_AT)));
                productModel.setProductPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)));
                productModel.setProductSpecialPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_SPECIAL_PRICE)));
                productModel.setProductTaxClassId(cursor.getString(cursor.getColumnIndex(PRODUCT_TAX_CLASS_ID)));
                productModel.setProductCatId(cursor.getString(cursor.getColumnIndex(PRODUCT_CAT_ID)));
                productModel.setProductPosition(cursor.getString(cursor.getColumnIndex(PRODUCT_POSITION)));
                productModel.setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                productModel.setProductTaxRate(cursor.getFloat(cursor.getColumnIndex(PRODUCT_TAX_RATE)));
                productModel.setProductIsActive(cursor.getString(cursor.getColumnIndex(PRODUCT_IS_ACTIVE)));
                productModel.setProductImageShown(cursor.getInt(cursor.getColumnIndex(PRODUCT_IMAGE_SHOWN)));
                dataList.add(productModel);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getAllData", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }

    public boolean isProductAssignedToGivenCategory(String pCatId) {
        Cursor cursor = null;
        try {
            String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE (','|| " + PRODUCT_CAT_ID + " || ',') LIKE '%," + pCatId + ",%'";
            cursor = mWritableDatabase.rawQuery(sqlQuery, null);
            if (cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e(mTableName, "isProductAssignedToGivenCategory", e);
        } finally {
            closeCursor(cursor);
        }
        return false;
    }

    public List<ProductParent.Product> getProductsAssignedToGivenCategory(String pCatId) {

        ArrayList<ProductParent.Product> dataList = new ArrayList<>(0);
        Cursor cursor = null;
        try {
            String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE (','|| " + PRODUCT_CAT_ID + " || ',') LIKE '%," + pCatId + ",%'";
            cursor = mWritableDatabase.rawQuery(sqlQuery, null);
            if (cursor.getCount() <= 0) {
                return dataList;
            }

            while (cursor.moveToNext()) {
                ProductParent.Product productModel = new ProductParent.Product();
                productModel.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                productModel.setProductSku(cursor.getString(cursor.getColumnIndex(PRODUCT_SKU)));
                productModel.setProductImage(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE_URL)));
                productModel.setProductImageText(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE_TEXT)));
                productModel.setProductWeight(cursor.getString(cursor.getColumnIndex(PRODUCT_WEIGHT)));
                productModel.setProductDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)));
                productModel.setProductCreatedAt(cursor.getString(cursor.getColumnIndex(PRODUCT_CREATED_AT)));
                productModel.setProductUpdatedAt(cursor.getString(cursor.getColumnIndex(PRODUCT_UPDATED_AT)));
                productModel.setProductPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)));
                productModel.setProductSpecialPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_SPECIAL_PRICE)));
                productModel.setProductTaxClassId(cursor.getString(cursor.getColumnIndex(PRODUCT_TAX_CLASS_ID)));
                productModel.setProductCatId(cursor.getString(cursor.getColumnIndex(PRODUCT_CAT_ID)));
                productModel.setProductPosition(cursor.getString(cursor.getColumnIndex(PRODUCT_POSITION)));
                productModel.setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                productModel.setProductTaxRate(cursor.getFloat(cursor.getColumnIndex(PRODUCT_TAX_RATE)));
                productModel.setProductIsActive(cursor.getString(cursor.getColumnIndex(PRODUCT_IS_ACTIVE)));
                productModel.setProductImageShown(cursor.getInt(cursor.getColumnIndex(PRODUCT_IMAGE_SHOWN)));
                dataList.add(productModel);
            }
        } catch (Exception e) {
            Log.e(mTableName, "getProductsAssignedToGivenCategory", e);
        } finally {
            closeCursor(cursor);
        }
        return dataList;
    }

    public ProductParent.Product getProduct(String pProductCase, boolean pProductSku) {

        ProductParent.Product productModel = null;
        Cursor cursor = null;
        try {
            String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + (pProductSku ? PRODUCT_SKU : PRODUCT_NAME) + " = '" + pProductCase + "'";
            Log.d("ProductTable", sqlQuery);

            cursor = mWritableDatabase.rawQuery(sqlQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                productModel = new ProductParent.Product();
                productModel.setProductId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                productModel.setProductSku(cursor.getString(cursor.getColumnIndex(PRODUCT_SKU)));
                productModel.setProductImage(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE_URL)));
                productModel.setProductImageText(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE_TEXT)));
                productModel.setProductWeight(cursor.getString(cursor.getColumnIndex(PRODUCT_WEIGHT)));
                productModel.setProductDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)));
                productModel.setProductCreatedAt(cursor.getString(cursor.getColumnIndex(PRODUCT_CREATED_AT)));
                productModel.setProductUpdatedAt(cursor.getString(cursor.getColumnIndex(PRODUCT_UPDATED_AT)));
                productModel.setProductPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)));
                productModel.setProductSpecialPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_SPECIAL_PRICE)));
                productModel.setProductTaxClassId(cursor.getString(cursor.getColumnIndex(PRODUCT_TAX_CLASS_ID)));
                productModel.setProductCatId(cursor.getString(cursor.getColumnIndex(PRODUCT_CAT_ID)));
                productModel.setProductPosition(cursor.getString(cursor.getColumnIndex(PRODUCT_POSITION)));
                productModel.setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                productModel.setProductTaxRate(cursor.getFloat(cursor.getColumnIndex(PRODUCT_TAX_RATE)));
                productModel.setProductIsActive(cursor.getString(cursor.getColumnIndex(PRODUCT_IS_ACTIVE)));
                productModel.setProductImageShown(cursor.getInt(cursor.getColumnIndex(PRODUCT_IMAGE_SHOWN)));
            }
        } catch (Exception e) {
            Log.e(mTableName, "getProduct", e);
        } finally {
            closeCursor(cursor);
        }
        return productModel;
    }

    public List<String> getProductNameList() {
        Set<String> productNameSet = new HashSet<>(0);
        Cursor cursor = null;
        try {
            String sqlQuery = "SELECT " + PRODUCT_NAME + " FROM " + TABLE_NAME;
            cursor = mWritableDatabase.rawQuery(sqlQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    productNameSet.add(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                }
            }
        } catch (Exception e) {
            Log.e(mTableName, "getProduct", e);
        } finally {
            closeCursor(cursor);
        }
        return new ArrayList<>(productNameSet);
    }
}

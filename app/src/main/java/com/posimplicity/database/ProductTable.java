package com.posimplicity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.ProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductTable {


	public static final String TABLE_NAME       = "ProductTable";

	private final String PRODUCT_ID          	= "ProductId";
	private final String PRODUCT_SKU         	= "ProductSku";
	private final String PRODUCT_IMAGE_URL   	= "ProductImageUrl";	
	private final String PRODUCT_IMAGE_TEXT     = "ProductImageText";
	private final String PRODUCT_WEIGHT         = "ProductWeight";
	private final String PRODUCT_DESCRIPTION    = "ProductDescription";
	private final String PRODUCT_CREATED_AT   	= "ProductCreatedAt";	
	private final String PRODUCT_UPDATED_AT     = "ProductUpdatedAt";
	private final String PRODUCT_PRICE          = "ProductPrice";
	private final String PRODUCT_SPECIAL_PRICE  = "ProductSpecialPrice";	
	private final String PRODUCT_TAX_CLASS_ID   = "ProductTaxClassId";
	private final String PRODUCT_CAT_ID         = "ProductCategoryId";
	private final String PRODUCT_POSITION       = "ProductPosition";	
	private final String PRODUCT_NAME           = "ProductName";
	private final String PRODUCT_TAX_RATE       = "ProductTaxRate";
	private final String PRODUCT_IS_ACTIVE      = "ProductIsActive";
	private final String PRODUCT_IMAGE_SHOWN    = "ProductImageShown";

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public ProductTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ PRODUCT_ID       		+" TEXT, "
					+ PRODUCT_SKU      		+" TEXT, "
					+ PRODUCT_IMAGE_URL     +" TEXT, "
					+ PRODUCT_IMAGE_TEXT    +" TEXT, "
					+ PRODUCT_WEIGHT      	+" TEXT, "
					+ PRODUCT_DESCRIPTION   +" TEXT, "
					+ PRODUCT_CREATED_AT    +" TEXT, "
					+ PRODUCT_UPDATED_AT    +" TEXT, "
					+ PRODUCT_PRICE       	+" TEXT, "
					+ PRODUCT_SPECIAL_PRICE +" TEXT, "
					+ PRODUCT_TAX_CLASS_ID  +" TEXT, "
					+ PRODUCT_CAT_ID      	+" TEXT, "
					+ PRODUCT_POSITION      +" TEXT, "
					+ PRODUCT_NAME       	+" TEXT, "
					+ PRODUCT_TAX_RATE   	+" TEXT, "
					+ PRODUCT_IS_ACTIVE     +" TEXT, "
					+ PRODUCT_IMAGE_SHOWN   +" TEXT, PRIMARY KEY ( "+ PRODUCT_ID + " , "+ PRODUCT_CAT_ID + " ))";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addInfoInTable(ProductModel productModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(PRODUCT_ID,				productModel.getProductId());
			values.put(PRODUCT_SKU,         	productModel.getProductSKU());
			values.put(PRODUCT_IMAGE_URL,   	productModel.getProductImageUrl());
			values.put(PRODUCT_IMAGE_TEXT,   	productModel.getProductImageText());			
			values.put(PRODUCT_WEIGHT,   		productModel.getProductWeight());
			values.put(PRODUCT_DESCRIPTION,   	productModel.getProductDescription());
			values.put(PRODUCT_CREATED_AT,   	productModel.getProductCreatedAt());
			values.put(PRODUCT_UPDATED_AT,   	productModel.getProductUpdatedAt());
			values.put(PRODUCT_PRICE,   		productModel.getProductPrice());			
			values.put(PRODUCT_SPECIAL_PRICE,   productModel.getProductSpecialPrice());
			values.put(PRODUCT_TAX_CLASS_ID,   	productModel.getProductTaxId());
			values.put(PRODUCT_CAT_ID,   		productModel.getProductCategoryId());
			values.put(PRODUCT_POSITION,   		productModel.getProductPosition());
			values.put(PRODUCT_NAME,   			productModel.getProductName());
			values.put(PRODUCT_TAX_RATE,   		productModel.getProductTaxRate());
			values.put(PRODUCT_IS_ACTIVE, 		productModel.getProductIsActive());
			values.put(PRODUCT_IMAGE_SHOWN,     productModel.isProductImageShown());

			// Inserting Row
			posDataBase.insert(TABLE_NAME, null, values);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoInTable(ProductModel productModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(PRODUCT_ID,				productModel.getProductId());
			values.put(PRODUCT_SKU,         	productModel.getProductSKU());
			values.put(PRODUCT_IMAGE_URL,   	productModel.getProductImageUrl());
			values.put(PRODUCT_IMAGE_TEXT,   	productModel.getProductImageText());			
			values.put(PRODUCT_WEIGHT,   		productModel.getProductWeight());
			values.put(PRODUCT_DESCRIPTION,   	productModel.getProductDescription());
			values.put(PRODUCT_CREATED_AT,   	productModel.getProductCreatedAt());
			values.put(PRODUCT_UPDATED_AT,   	productModel.getProductUpdatedAt());
			values.put(PRODUCT_PRICE,   		productModel.getProductPrice());			
			values.put(PRODUCT_SPECIAL_PRICE,   productModel.getProductSpecialPrice());
			values.put(PRODUCT_TAX_CLASS_ID,   	productModel.getProductTaxId());
			values.put(PRODUCT_CAT_ID,   		productModel.getProductCategoryId());
			values.put(PRODUCT_POSITION,   		productModel.getProductPosition());
			values.put(PRODUCT_NAME,   			productModel.getProductName());
			values.put(PRODUCT_TAX_RATE,   		productModel.getProductTaxRate());
			values.put(PRODUCT_IS_ACTIVE, 		productModel.getProductIsActive());
			values.put(PRODUCT_IMAGE_SHOWN,     productModel.isProductImageShown());

			// Update Row
			posDataBase.update(TABLE_NAME, values, PRODUCT_ID +" =? ", new String[] { productModel.getProductId() } );
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void updateInfoListInTable(List<ProductModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				ProductModel productModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(PRODUCT_ID,				productModel.getProductId());
				values.put(PRODUCT_SKU,         	productModel.getProductSKU());
				values.put(PRODUCT_IMAGE_URL,   	productModel.getProductImageUrl());
				values.put(PRODUCT_IMAGE_TEXT,   	productModel.getProductImageText());			
				values.put(PRODUCT_WEIGHT,   		productModel.getProductWeight());
				values.put(PRODUCT_DESCRIPTION,   	productModel.getProductDescription());
				values.put(PRODUCT_CREATED_AT,   	productModel.getProductCreatedAt());
				values.put(PRODUCT_UPDATED_AT,   	productModel.getProductUpdatedAt());
				values.put(PRODUCT_PRICE,   		productModel.getProductPrice());			
				values.put(PRODUCT_SPECIAL_PRICE,   productModel.getProductSpecialPrice());
				values.put(PRODUCT_TAX_CLASS_ID,   	productModel.getProductTaxId());
				values.put(PRODUCT_CAT_ID,   		productModel.getProductCategoryId());
				values.put(PRODUCT_POSITION,   		productModel.getProductPosition());
				values.put(PRODUCT_NAME,   			productModel.getProductName());
				values.put(PRODUCT_TAX_RATE,   		productModel.getProductTaxRate());
				values.put(PRODUCT_IS_ACTIVE, 		productModel.getProductIsActive());
				values.put(PRODUCT_IMAGE_SHOWN,     productModel.isProductImageShown());

				// Update Row
				posDataBase.update(TABLE_NAME, values, PRODUCT_ID +" =? ", new String[] { productModel.getProductId() } );

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public void addInfoListInTable(List<ProductModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				ProductModel productModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(PRODUCT_ID,				productModel.getProductId());
				values.put(PRODUCT_SKU,         	productModel.getProductSKU());
				values.put(PRODUCT_IMAGE_URL,   	productModel.getProductImageUrl());
				values.put(PRODUCT_IMAGE_TEXT,   	productModel.getProductImageText());			
				values.put(PRODUCT_WEIGHT,   		productModel.getProductWeight());
				values.put(PRODUCT_DESCRIPTION,   	productModel.getProductDescription());
				values.put(PRODUCT_CREATED_AT,   	productModel.getProductCreatedAt());
				values.put(PRODUCT_UPDATED_AT,   	productModel.getProductUpdatedAt());
				values.put(PRODUCT_PRICE,   		productModel.getProductPrice());			
				values.put(PRODUCT_SPECIAL_PRICE,   productModel.getProductSpecialPrice());
				values.put(PRODUCT_TAX_CLASS_ID,   	productModel.getProductTaxId());
				values.put(PRODUCT_CAT_ID,   		productModel.getProductCategoryId());
				values.put(PRODUCT_POSITION,   		productModel.getProductPosition());
				values.put(PRODUCT_NAME,   			productModel.getProductName());
				values.put(PRODUCT_TAX_RATE,   		productModel.getProductTaxRate());
				values.put(PRODUCT_IS_ACTIVE,       productModel.getProductIsActive());
				values.put(PRODUCT_IMAGE_SHOWN,     productModel.isProductImageShown());

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


	private void findPositionOfProduct(ProductModel productModel,String catId){

		String productCategoryId = productModel.getProductCategoryId();
		String productPosition   = productModel.getProductPosition();


		List<String> categoryIdList      = Arrays.asList(productCategoryId.split(","));
		List<String> positionIdList      = Arrays.asList(productPosition.split(","));
		if(categoryIdList.size() > 0 && positionIdList.size() > 0){
			int locationOfCatId     = categoryIdList.indexOf(catId);
			String productPositionV = positionIdList.get(locationOfCatId);
			productModel.setProductLocation(productPositionV);
		}
	}


	public List<ProductModel> getAllInfoFromTable(){
		List<ProductModel> listOfAllData = new ArrayList<ProductModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null ,null, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					ProductModel productModel = new ProductModel();

					productModel.setProductId(cursor.getString(0));
					productModel.setProductSKU(cursor.getString(1));
					productModel.setProductImageUrl(cursor.getString(2));
					productModel.setProductImageText(cursor.getString(3));					
					productModel.setProductWeight(cursor.getString(4));
					productModel.setProductDescription(cursor.getString(5));
					productModel.setProductCreatedAt(cursor.getString(6));
					productModel.setProductUpdatedAt(cursor.getString(7));
					productModel.setProductPrice(cursor.getString(8));
					productModel.setProductSpecialPrice(cursor.getString(9));
					productModel.setProductTaxId(cursor.getString(10));
					productModel.setProductCategoryId(cursor.getString(11));
					productModel.setProductPosition(cursor.getString(12));
					productModel.setProductName(cursor.getString(13));
					productModel.setProductTaxRate(cursor.getString(14));
					productModel.setProductIsActive(cursor.getString(15));
					productModel.setProductImageShown(cursor.getInt(16) == 1?true:false);
					productModel.setProductQty("1");
					productModel.setProductDisAmount("0.00");
					productModel.setProductOptionsPrice("0.00");

					listOfAllData.add(productModel);

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

	public List<ProductModel> getAllInfoFromTableBasedOnCategoryID(String categoryId){

		List<ProductModel> listOfAllData = new ArrayList<ProductModel>();
		try {

			posDataBase                 = posDbHandler.openReadableDataBase();
			String sqlQuery             = "SELECT * FROM "+TABLE_NAME+" WHERE (','|| "+PRODUCT_CAT_ID+" || ',') LIKE '%,"+categoryId+",%'";
			//System.out.println(sqlQuery);
			Cursor cursor               = posDataBase.rawQuery(sqlQuery,null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					ProductModel productModel = new ProductModel();

					productModel.setProductId(cursor.getString(0));
					productModel.setProductSKU(cursor.getString(1));
					productModel.setProductImageUrl(cursor.getString(2));
					productModel.setProductImageText(cursor.getString(3));					
					productModel.setProductWeight(cursor.getString(4));
					productModel.setProductDescription(cursor.getString(5));
					productModel.setProductCreatedAt(cursor.getString(6));
					productModel.setProductUpdatedAt(cursor.getString(7));
					productModel.setProductPrice(cursor.getString(8));
					productModel.setProductSpecialPrice(cursor.getString(9));
					productModel.setProductTaxId(cursor.getString(10));
					productModel.setProductCategoryId(cursor.getString(11));
					productModel.setProductPosition(cursor.getString(12));
					productModel.setProductName(cursor.getString(13));
					productModel.setProductTaxRate(cursor.getString(14));
					productModel.setProductIsActive(cursor.getString(15));
					productModel.setProductImageShown(cursor.getInt(16) == 1?true:false);
					productModel.setProductQty("1");
					productModel.setProductDisAmount("0.00");
					productModel.setProductOptionsPrice("0.00");
					
					findPositionOfProduct(productModel,categoryId);
					
					listOfAllData.add(productModel);

				} while (cursor.moveToNext());
			}
			cursor.close();
			Collections.sort(listOfAllData);
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



	public ProductModel getSingleInfoFromTableByProductId(String productId){

		ProductModel productModel = new ProductModel();
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, null, PRODUCT_ID + "=? " ,new String[] { productId }, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {

				productModel.setProductId(cursor.getString(0));
				productModel.setProductSKU(cursor.getString(1));
				productModel.setProductImageUrl(cursor.getString(2));
				productModel.setProductImageText(cursor.getString(3));					
				productModel.setProductWeight(cursor.getString(4));
				productModel.setProductDescription(cursor.getString(5));
				productModel.setProductCreatedAt(cursor.getString(6));
				productModel.setProductUpdatedAt(cursor.getString(7));
				productModel.setProductPrice(cursor.getString(8));
				productModel.setProductSpecialPrice(cursor.getString(9));
				productModel.setProductTaxId(cursor.getString(10));
				productModel.setProductCategoryId(cursor.getString(11));
				productModel.setProductPosition(cursor.getString(12));
				productModel.setProductName(cursor.getString(13));
				productModel.setProductTaxRate(cursor.getString(14));
				productModel.setProductIsActive(cursor.getString(15));
				productModel.setProductImageShown(cursor.getInt(16) == 1);
				productModel.setProductQty("1");
				productModel.setProductDisAmount("0.00");
				productModel.setProductOptionsPrice("0.00");
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return productModel;
	}


	public void deleteInfoFromTable(String productId) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			posDataBase.delete(TABLE_NAME, PRODUCT_ID + " =? ", new String []{ productId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

	public int getProdctIdFromTableByProductName(String serachProduct) {
		String productId = "0";
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, new String[]{ PRODUCT_ID },PRODUCT_NAME + "=? " ,new String[] { serachProduct }, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				productId = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return Integer.parseInt(productId);
	}

	public int getProdctIdFromTableByProductSku(String skuId) {
		String productId = "0";
		try {
			posDataBase    = posDbHandler.openReadableDataBase();
			Cursor cursor  = posDataBase.query(TABLE_NAME, new String[]{ PRODUCT_ID },PRODUCT_SKU + "=? " ,new String[] { skuId }, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				productId = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		// return activeRoleInfo object
		return Integer.parseInt(productId);
	}
}

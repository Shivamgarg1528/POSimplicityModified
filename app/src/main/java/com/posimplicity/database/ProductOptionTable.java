package com.posimplicity.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Beans.OptionModel;
import com.Beans.ProductOptionsModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.utils.MyStringFormat;

public class ProductOptionTable {



	public static final String TABLE_NAME       = "ProductOptionTable";

	private final String PRODUCT_ID          	= "ProductId";
	private final String OPTION_ID         		= "OptionId";
	private final String OPTION_NAME   			= "OptionName";	
	private final String OPTION_SORT_ORDER      = "OptionSortOrder";
	private final String SUB_OPTION_ID          = "SubOptionId";
	private final String SUB_OPTION_NAME    	= "SubOptionName";
	private final String SUB_OPTION_PRICE   	= "SubOptionPrice";	
	private final String SUB_OPTION_SORT_ORDER  = "SubOptionSortOrder";
	private final String OPTION_ENABLE          = "OptionEnable";	

	private SQLiteDatabase posDataBase;
	private POSDatabaseHandler posDbHandler;

	public ProductOptionTable(Context context) {
		posDbHandler  = POSDatabaseHandler.getInstance(context);
	}

	public void createSchemaOfTable(SQLiteDatabase db){
		try{
			String query = "CREATE TABLE "
					+ TABLE_NAME            +" ( "					
					+ PRODUCT_ID       		+" TEXT, "
					+ OPTION_ID      		+" TEXT, "
					+ OPTION_NAME     		+" TEXT, "
					+ OPTION_SORT_ORDER     +" TEXT, "
					+ SUB_OPTION_ID      	+" TEXT, "
					+ SUB_OPTION_NAME       +" TEXT, "
					+ SUB_OPTION_PRICE      +" TEXT, "
					+ SUB_OPTION_SORT_ORDER +" TEXT, "
					+ OPTION_ENABLE         +" TEXT, PRIMARY KEY ( "+ PRODUCT_ID + " , "+ OPTION_ID + " , " +SUB_OPTION_ID + " ))";

			Log.e(this.getClass().getName()+" :", "QUERY: -->>" + query);
			db.execSQL(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addInfoInTable(ProductOptionsModel productOptionsModel) {
		try{
			posDataBase           = posDbHandler.openWritableDataBase();
			ContentValues values  = new ContentValues();

			values.put(PRODUCT_ID,				productOptionsModel.getProductId());
			values.put(OPTION_ID,         		productOptionsModel.getOptionId());
			values.put(OPTION_NAME,   			productOptionsModel.getOptionName());
			values.put(OPTION_SORT_ORDER,   	productOptionsModel.getOptionSortOrder());			
			values.put(SUB_OPTION_ID,   		productOptionsModel.getSubOptionId());
			values.put(SUB_OPTION_NAME,   		productOptionsModel.getSubOptionName());
			values.put(SUB_OPTION_PRICE,   		productOptionsModel.getSubOptionPrice());
			values.put(SUB_OPTION_SORT_ORDER,   productOptionsModel.getSubOptionSortOrder());
			values.put(OPTION_ENABLE,   	    productOptionsModel.getOptionEnable());			

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


	public void addInfoListInTable(List<ProductOptionsModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				ProductOptionsModel productOptionsModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(PRODUCT_ID,				productOptionsModel.getProductId());
				values.put(OPTION_ID,         		productOptionsModel.getOptionId());
				values.put(OPTION_NAME,   			productOptionsModel.getOptionName());
				values.put(OPTION_SORT_ORDER,   	productOptionsModel.getOptionSortOrder());			
				values.put(SUB_OPTION_ID,   		productOptionsModel.getSubOptionId());
				values.put(SUB_OPTION_NAME,   		productOptionsModel.getSubOptionName());
				values.put(SUB_OPTION_PRICE,   		productOptionsModel.getSubOptionPrice());
				values.put(SUB_OPTION_SORT_ORDER,   productOptionsModel.getSubOptionSortOrder());
				values.put(OPTION_ENABLE,   	    productOptionsModel.getOptionEnable());				

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

	public void updateInfoListInTable(List<ProductOptionsModel> listOfDetails){
		try{
			posDataBase           = posDbHandler.openWritableDataBase();

			for(int index = listOfDetails.size()-1 ; index >= 0 ; index-- ){

				ProductOptionsModel productOptionsModel = listOfDetails.get(index);
				ContentValues values = new ContentValues();

				values.put(PRODUCT_ID,				productOptionsModel.getProductId());
				values.put(OPTION_ID,         		productOptionsModel.getOptionId());
				values.put(OPTION_NAME,   			productOptionsModel.getOptionName());
				values.put(OPTION_SORT_ORDER,   	productOptionsModel.getOptionSortOrder());			
				values.put(SUB_OPTION_ID,   		productOptionsModel.getSubOptionId());
				values.put(SUB_OPTION_NAME,   		productOptionsModel.getSubOptionName());
				values.put(SUB_OPTION_PRICE,   		productOptionsModel.getSubOptionPrice());
				values.put(SUB_OPTION_SORT_ORDER,   productOptionsModel.getSubOptionSortOrder());
				values.put(OPTION_ENABLE,   	    productOptionsModel.getOptionEnable());				

				// Update Row
				posDataBase.update(TABLE_NAME , values , PRODUCT_ID +" =? AND "+OPTION_ID +" =? " ,new String []{ productOptionsModel.getProductId(),productOptionsModel.getOptionId()});
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();// Closing database connection
		}
	}

	public int getPositionOfOptionModel(List<RelationalOptionModel> listOfAllData , String optionId){
		int positionOfModel = -1 ;

		for(int index = listOfAllData.size() - 1 ; index >= 0; index -- ){
			if(listOfAllData.get(index).getOptionModel().getOptionId().equalsIgnoreCase(optionId)){
				positionOfModel = index ;
				return positionOfModel;
			}
		}
		return positionOfModel;
	}


	public List<RelationalOptionModel> getAllInfoFromTable(String productId){

		List<RelationalOptionModel> listOfAllData = new ArrayList<RelationalOptionModel>();
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null, PRODUCT_ID+" =? ", new String[]{ productId }, null, null, null, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {

					ProductOptionsModel productOptionsModel = new ProductOptionsModel();

					productOptionsModel.setProductId(cursor.getString(0));
					productOptionsModel.setOptionId(cursor.getString(1));
					productOptionsModel.setOptionName(cursor.getString(2));
					productOptionsModel.setOptionSortOrder(cursor.getString(3));
					productOptionsModel.setSubOptionId(cursor.getString(4));
					productOptionsModel.setSubOptionName(cursor.getString(5));
					productOptionsModel.setSubOptionPrice(cursor.getString(6));
					productOptionsModel.setSubOptionSortOrder(cursor.getString(7));
					productOptionsModel.setOptionEnable(cursor.getString(8));

					boolean productRequired;
					if(productOptionsModel.getOptionEnable().equalsIgnoreCase("1"))
						productRequired = true;
					else
						productRequired = false;

					OptionModel optionModel    = new OptionModel(productOptionsModel.getProductId(), productOptionsModel.getOptionId(), productOptionsModel.getOptionName(), productOptionsModel.getOptionSortOrder(), productRequired);
					List<SubOptionModel> newSubOptionList = new ArrayList<>();

					List<String> subOptionIds  		 = Arrays.asList(productOptionsModel.getSubOptionId().split(","));
					List<String> subOptionNames      = Arrays.asList(productOptionsModel.getSubOptionName().split(","));
					List<String> subOptionPrices     = Arrays.asList(productOptionsModel.getSubOptionPrice().split(","));
					List<String> subOptionSortOrders = Arrays.asList(productOptionsModel.getSubOptionSortOrder().split(","));

					for(int index = 0 ;index < subOptionIds.size() ; index ++ ){
						String subOptionId  		= subOptionIds.get(index);
						String subOptionName        = subOptionNames.get(index);
						String subOptionPrice       = subOptionPrices.get(index);
						String subOptionSortOrder   = subOptionSortOrders.get(index);
						subOptionPrice              = MyStringFormat.onStringFormat(subOptionPrice);
						newSubOptionList.add(new SubOptionModel(subOptionId, subOptionName, subOptionPrice, subOptionSortOrder));
					}
					listOfAllData.add(new RelationalOptionModel(optionModel,newSubOptionList));

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
		Collections.sort(listOfAllData);
		return listOfAllData;
	}


	public void deleteInfoListFromTable(List<String> productIds) {
		try {
			posDataBase = posDbHandler.openWritableDataBase();
			for(int index = 0 ; index < productIds.size() ; index ++ ){
				posDataBase.delete(TABLE_NAME, PRODUCT_ID + " =? ", new String []{ productIds.get(index) });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
	}

	public RelationalOptionModel getRelationModelObj(String productId,String optionId, String subOptionsIdss) {	
		RelationalOptionModel  relationalOptionModel = null;
		try {
			posDataBase                 = posDbHandler.openReadableDataBase();
			Cursor cursor               = posDataBase.query(TABLE_NAME, null, PRODUCT_ID+" =? AND "+ OPTION_ID+" =? ", new String[]{ productId ,optionId }, null, null, null, null);
			boolean firstTimeOnly       = true;

			if (cursor != null && cursor.getCount() > 0 &&  cursor.moveToFirst()) {
				ProductOptionsModel productOptionsModel = new ProductOptionsModel();

				productOptionsModel.setProductId(cursor.getString(0));
				productOptionsModel.setOptionId(cursor.getString(1));
				productOptionsModel.setOptionName(cursor.getString(2));
				productOptionsModel.setOptionSortOrder(cursor.getString(3));
				productOptionsModel.setSubOptionId(cursor.getString(4));
				productOptionsModel.setSubOptionName(cursor.getString(5));
				productOptionsModel.setSubOptionPrice(cursor.getString(6));
				productOptionsModel.setSubOptionSortOrder(cursor.getString(7));
				productOptionsModel.setOptionEnable(cursor.getString(8));

				boolean productRequired;
				if(productOptionsModel.getOptionEnable().equalsIgnoreCase("1"))
					productRequired = true;
				else
					productRequired = false;
				OptionModel optionModel            = new OptionModel(productOptionsModel.getProductId(), productOptionsModel.getOptionId(), productOptionsModel.getOptionName(), productOptionsModel.getOptionSortOrder(), productRequired);

				List<String> subOptionIds  		   = Arrays.asList(productOptionsModel.getSubOptionId().split(","));
				List<String> subOptionNames        = Arrays.asList(productOptionsModel.getSubOptionName().split(","));
				List<String> subOptionPrices       = Arrays.asList(productOptionsModel.getSubOptionPrice().split(","));
				List<String> subOptionSortOrders   = Arrays.asList(productOptionsModel.getSubOptionSortOrder().split(","));

				List<String> requestedSubOptionIds = Arrays.asList(subOptionsIdss.split(","));

				for(int index = 0 ; index < requestedSubOptionIds.size() ; index ++ ){

					String requestedSubOptionId       = requestedSubOptionIds.get(index);
					int findedSubOptionIdLocation     = subOptionIds.indexOf(requestedSubOptionId);

					if(findedSubOptionIdLocation > -1){

						String findedOptionId         = subOptionIds.get(findedSubOptionIdLocation);							
						String findedOptionName       = subOptionNames.get(findedSubOptionIdLocation);
						String findedOptionPrice      = subOptionPrices.get(findedSubOptionIdLocation);
						String findedOptionSortOrder  = subOptionSortOrders.get(findedSubOptionIdLocation);

						if(firstTimeOnly){
							firstTimeOnly = false;
							List<SubOptionModel> newSubOptionList = new ArrayList<>();
							newSubOptionList.add(new SubOptionModel(findedOptionId,findedOptionName,findedOptionPrice,findedOptionSortOrder));
							relationalOptionModel        = new RelationalOptionModel(optionModel,newSubOptionList);
						}
						else
						{
							SubOptionModel subOptionModel = new SubOptionModel(findedOptionId,findedOptionName,findedOptionPrice,findedOptionSortOrder);
							relationalOptionModel.getListOfSubOptionModel().add(subOptionModel);
						}
					}
				}
			}

			cursor.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			posDbHandler.closeDataBase();
		}
		return relationalOptionModel;
	}
}

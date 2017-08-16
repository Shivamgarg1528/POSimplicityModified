package com.Services;

import android.content.Context;

import com.Beans.ProductModel;
import com.posimplicity.database.ProductTable;
import com.utils.ImageProcessing;
import com.utils.JSONObJValidator;
import com.utils.MyStringFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {

	public static void parseProductDataWhenAdd(String responseData, Context mContext) {

		try{
			JSONObject outerJSONObj     = new JSONObject(responseData);
			JSONArray itemsDetails      = outerJSONObj.getJSONArray("product_list");			
			int lengthOfArray           = itemsDetails.length();

			if (lengthOfArray  > 0) {

				List<ProductModel> listOfProductModel       =  new ArrayList<>();				

				for (int i = 0; i < lengthOfArray ; i++) {

					JSONObject jsonObject      = itemsDetails.getJSONObject(i);
					String productSKU          = JSONObJValidator.stringTagValidate(jsonObject, "sku",			 "");
					String productId           = JSONObJValidator.stringTagValidate(jsonObject, "product_id",	 "-1");
					String productName         = JSONObJValidator.stringTagValidate(jsonObject, "name", 		 "productName"+i);
					String productImageText    = JSONObJValidator.stringTagValidate(jsonObject, "image_text",    "");
					String productDescription  = JSONObJValidator.stringTagValidate(jsonObject, "description",   "");
					String productWeight       = JSONObJValidator.stringTagValidate(jsonObject, "weight",        "");
					String productCreatedAt    = JSONObJValidator.stringTagValidate(jsonObject, "created_at", 	 "");
					String productUpdatedAt    = JSONObJValidator.stringTagValidate(jsonObject, "updated_at",    "");
					String productPrice        = JSONObJValidator.stringTagValidate(jsonObject, "price",         "0.00");
					String productSpecialPrice = JSONObJValidator.stringTagValidate(jsonObject, "special_price", "0.00");
					String productTaxId        = JSONObJValidator.stringTagValidate(jsonObject, "tax_class_id",  "-1");
					String productTaxRate      = JSONObJValidator.stringTagValidate(jsonObject, "tax_rate",      "0.00");
					String productCategoryId   = JSONObJValidator.stringTagValidate(jsonObject, "cat_id",        "");
					String productLocation     = JSONObJValidator.stringTagValidate(jsonObject, "position",      "");
					String productImageUrl     = JSONObJValidator.stringTagValidate(jsonObject, "image",         "");
					String productIsActive     = JSONObJValidator.stringTagValidate(jsonObject, "is_active",     "2");
					int    productImageShown   = JSONObJValidator.intTagValidate(jsonObject   , "image_shown"   , 0);

					productTaxRate             = MyStringFormat.onTaxRateFormat((Float.parseFloat(productTaxRate))/100.0f);
					productPrice               = MyStringFormat.onStringFormat(productPrice);
					productSpecialPrice        = MyStringFormat.onStringFormat(productSpecialPrice);				

					if(productIsActive.equalsIgnoreCase("1")){
						listOfProductModel.add(new ProductModel(productId, productTaxId, productName, productDescription, productCreatedAt, productUpdatedAt, productSKU, productImageText, productImageUrl, productWeight, productSpecialPrice, productPrice, productTaxRate, productCategoryId, productLocation,productIsActive,productImageShown == 1 ?true:false));
						ImageProcessing.downLoadImageFromServerAndSavedInSdCard(productImageUrl,productId);
					}
				}
				if(listOfProductModel.size() > 0 )
					new ProductTable(mContext).addInfoListInTable(listOfProductModel);				
			}		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}	


	public static void parseProductDataWhenUpdateToUpdate(String responseData, Context mContext){
		try{
			JSONObject outerJSONObj     = new JSONObject(responseData);
			JSONArray itemsDetails      = outerJSONObj.getJSONArray("product_list");			
			int lengthOfArray           = itemsDetails.length();

			if (lengthOfArray  > 0) {

				List<ProductModel> listOfProductModel       =  new ArrayList<>();

				for (int i = 0; i < lengthOfArray ; i++) {

					JSONObject jsonObject      = itemsDetails.getJSONObject(i);
					String productSKU          = JSONObJValidator.stringTagValidate(jsonObject, "sku",			 "");
					String productId           = JSONObJValidator.stringTagValidate(jsonObject, "product_id",	 "-1");
					String productName         = JSONObJValidator.stringTagValidate(jsonObject, "name", 		 "productName"+i);
					String productImageText    = JSONObJValidator.stringTagValidate(jsonObject, "image_text",    "");
					String productDescription  = JSONObJValidator.stringTagValidate(jsonObject, "description",   "");
					String productWeight       = JSONObJValidator.stringTagValidate(jsonObject, "weight",        "");
					String productCreatedAt    = JSONObJValidator.stringTagValidate(jsonObject, "created_at", 	 "");
					String productUpdatedAt    = JSONObJValidator.stringTagValidate(jsonObject, "updated_at",    "");
					String productPrice        = JSONObJValidator.stringTagValidate(jsonObject, "price",         "0.00");
					String productSpecialPrice = JSONObJValidator.stringTagValidate(jsonObject, "special_price", "0.00");
					String productTaxId        = JSONObJValidator.stringTagValidate(jsonObject, "tax_class_id",  "-1");
					String productTaxRate      = JSONObJValidator.stringTagValidate(jsonObject, "tax_rate",      "0.00");
					String productCategoryId   = JSONObJValidator.stringTagValidate(jsonObject, "cat_id",        "");
					String productLocation     = JSONObJValidator.stringTagValidate(jsonObject, "position",      "");
					String productImageUrl     = JSONObJValidator.stringTagValidate(jsonObject, "image",         "");
					String productIsActive     = JSONObJValidator.stringTagValidate(jsonObject, "is_active",     "1");
					int    productImageShown   = JSONObJValidator.intTagValidate(jsonObject   , "image_shown"   , 0);

					productTaxRate             = MyStringFormat.onTaxRateFormat((Float.parseFloat(productTaxRate))/100.0f);

					productPrice        = MyStringFormat.onStringFormat(productPrice);
					productSpecialPrice = MyStringFormat.onStringFormat(productSpecialPrice);

					listOfProductModel.add(new ProductModel(productId, productTaxId, productName, productDescription, productCreatedAt, productUpdatedAt, productSKU, productImageText, productImageUrl, productWeight, productSpecialPrice, productPrice, productTaxRate, productCategoryId, productLocation,productIsActive,productImageShown == 1 ?true:false));
					ImageProcessing.downLoadImageFromServerAndSavedInSdCard(productImageUrl,productId);
				}				

				if(listOfProductModel.size() > 0 )
					new ProductTable(mContext).updateInfoListInTable(listOfProductModel);				
			}		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void parsedProductDataWhenUpdateToDelete(String responseData,Context mContext){		
		try {
			JSONObject jsonObj         = new JSONObject(responseData);
			JSONArray deleteProduct    = jsonObj.getJSONArray("deleteProduct");
			int lengthOfDeletedProduct = deleteProduct.length();
			if (lengthOfDeletedProduct > 0) {				
				for (int i = 0; i < lengthOfDeletedProduct; i++) {

					JSONObject jsonObject      = deleteProduct.getJSONObject(i);
					String productIds          = JSONObJValidator.stringTagValidate(jsonObject,  "product_id", "");
					if(!productIds.isEmpty()){
						List<String> productIdList = Arrays.asList(productIds.split(","));
						for (int j = 0; j < productIdList.size(); j++) {
							String productId = productIdList.get(j);
							new ProductTable(mContext).deleteInfoFromTable(productId);	
							ImageProcessing.deleteAnImageFromSdCard(ImageProcessing.FOLDER_NAME,productId);
						}
					}
				}
			}			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}	
}

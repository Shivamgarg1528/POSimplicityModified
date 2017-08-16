package com.Services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.Beans.ProductOptionsModel;
import com.posimplicity.database.ProductOptionTable;
import com.utils.JSONObJValidator;
import android.content.Context;

public class ProductOptionService {


	public static void parsedProductOptionDataWhenAdd(String responseData , Context mContext){

		try {			 
			List<ProductOptionsModel> prOptionsModels = new ArrayList<>();
			JSONObject outerJsonObj    = new JSONObject(responseData);
			JSONObject innerJsonObj    = outerJsonObj.getJSONObject("product_options_list");

			if( innerJsonObj != null && innerJsonObj.length() > 0){

				Iterator<String> keys = innerJsonObj.keys();
				while(keys.hasNext()){
					String productId               = (String) keys.next();
					JSONArray productOptionsArray  = innerJsonObj.getJSONArray(productId);

					for(int index = 0 ; index < productOptionsArray.length() ; index ++ ){

						JSONObject innerObj       = productOptionsArray.getJSONObject(index);

						String subOptionEnable    = JSONObJValidator.stringTagValidate (innerObj,    "option_require"   , "0");						
						String optionId           = JSONObJValidator.stringTagValidate (innerObj,    "option_id"        , "0");
						String optionSortOrder    = JSONObJValidator.stringTagValidate (innerObj,    "option_sort_order", "0");
						String optionName         = JSONObJValidator.stringTagValidate (innerObj,    "option_name", "");

						String subOptionName      = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_names"  , "");
						String subOptionId        = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_ids"   , "0");
						String subOptionSortOrder = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_sort_order" , "0");
						String subOptionPrice     = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_prices"  , "0.00");

						prOptionsModels.add(new ProductOptionsModel(productId, optionId, optionName, optionSortOrder, subOptionId, subOptionName, subOptionPrice, subOptionSortOrder, subOptionEnable));
					}	
				}

				if(prOptionsModels.size() > 0 )
					new ProductOptionTable(mContext).addInfoListInTable(prOptionsModels);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void parsedProductOptionDataWhenUpdate(String responseData , Context mContext){

		try{			 
			List<ProductOptionsModel> prOptionsModels = new ArrayList<>();
			JSONObject outerJsonObj    = new JSONObject(responseData);
			JSONObject innerJsonObj    = outerJsonObj.getJSONObject("product_options_list");

			if( innerJsonObj != null && innerJsonObj.length() > 0){

				Iterator<String> keys = innerJsonObj.keys();
				while(keys.hasNext()){
					String productId               = (String) keys.next();
					JSONArray productOptionsArray  = innerJsonObj.getJSONArray(productId);

					for(int index = 0 ; index < productOptionsArray.length() ; index ++ ){

						JSONObject innerObj       = productOptionsArray.getJSONObject(index);

						String subOptionEnable    = JSONObJValidator.stringTagValidate (innerObj,    "option_require"   , "0");						
						String optionId           = JSONObJValidator.stringTagValidate (innerObj,    "option_id"        , "0");
						String optionSortOrder    = JSONObJValidator.stringTagValidate (innerObj,    "option_sort_order", "0");
						String optionName         = JSONObJValidator.stringTagValidate (innerObj,    "option_name", "");

						String subOptionName      = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_names"  , "");
						String subOptionId        = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_ids"   , "0");
						String subOptionSortOrder = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_sort_order" , "0");
						String subOptionPrice     = JSONObJValidator.stringTagValidate (innerObj,    "sub_option_prices"  , "0.00");

						prOptionsModels.add(new ProductOptionsModel(productId, optionId, optionName, optionSortOrder, subOptionId, subOptionName, subOptionPrice, subOptionSortOrder, subOptionEnable));
					}	
				}

				if(prOptionsModels.size() > 0 )
					new ProductOptionTable(mContext).updateInfoListInTable(prOptionsModels);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

package com.Services;

import android.content.Context;

import com.utils.Constants;
import com.utils.Helper;
import com.utils.JSONObJValidator;
import com.posimplicity.database.local.PosRoleTable;
import com.posimplicity.database.server.CategoryTable;
import com.posimplicity.database.server.CustomerGroupTable;
import com.posimplicity.database.server.CustomerTable;
import com.posimplicity.database.server.ProductOptionTable;
import com.posimplicity.database.server.ProductTable;
import com.posimplicity.model.local.RoleParent;
import com.posimplicity.model.response.api.CategoryParent;
import com.posimplicity.model.response.api.CustomerGroupParent;
import com.posimplicity.model.response.api.CustomerParent;
import com.posimplicity.model.response.api.ProductOption;
import com.posimplicity.model.response.api.ProductParent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class ParseService {

    public static void parseCategoryResponse(Context pContext, List<CategoryParent.Category> pDataList) {
        CategoryTable posCategoryTable = new CategoryTable(pContext);
        for (int index = 0; index < pDataList.size(); index++) {
            CategoryParent.Category category = pDataList.get(index);
            if (!Helper.isBlank(category.getDeptName()) && !category.getDeptName().startsWith(Constants.NON_POS_CATEGORY) && Constants.ACTIVE.equalsIgnoreCase(category.getIsDeptActive())) {
                posCategoryTable.insertData(category);
            }
        }
    }

    public static void parseProductResponse(Context pContext, List<ProductParent.Product> pDataList) {
        ProductTable posProductTable = new ProductTable(pContext);
        for (int index = 0; index < pDataList.size(); index++) {
            ProductParent.Product product = pDataList.get(index);
            int posIdsLength = product.getProductPosition().split(",").length;
            int catIdsLength = product.getProductCatId().split(",").length;
            boolean isValid = catIdsLength > 0 && posIdsLength > 0 && catIdsLength == posIdsLength;
            if (isValid && Constants.ACTIVE.equalsIgnoreCase(product.getProductIsActive())) {
                posProductTable.insertData(product);
            }
        }
    }

    public static void parseCustomerResponse(Context pContext, List<CustomerParent.Customer> pDataList) {
        CustomerTable posCustomerTable = new CustomerTable(pContext);
        for (int index = 0; index < pDataList.size(); index++) {
            posCustomerTable.insertData(pDataList.get(index));
        }
    }

    public static void parseCustomerGroupResponse(Context pContext, List<CustomerGroupParent.CustomerGroup> pDataList) {
        CustomerGroupTable posCustomerGroupTable = new CustomerGroupTable(pContext);
        for (int index = 0; index < pDataList.size(); index++) {
            posCustomerGroupTable.insertData(pDataList.get(index));
        }
    }

    public static void parseRoleInfo(Context pContext, List<RoleParent.RoleModel> pDataList) {
        PosRoleTable posRoleTable = new PosRoleTable(pContext);
        for (int index = 0; index < pDataList.size(); index++) {
            posRoleTable.insertData(pDataList.get(index));
        }
    }

    public static void parsedProductOptionResponse(String pResponseData, Context mContext) {
        try {
            JSONObject outerJsonObj = new JSONObject(pResponseData);
            JSONObject innerJsonObj = outerJsonObj.getJSONObject("product_options_list");
            ProductOptionTable posProductOptionTable = new ProductOptionTable(mContext);

            if (innerJsonObj != null && innerJsonObj.length() > 0) {

                Iterator<String> keys = innerJsonObj.keys();
                while (keys.hasNext()) {
                    String productId = (String) keys.next();
                    JSONArray productOptionsArray = innerJsonObj.getJSONArray(productId);

                    for (int index = 0; index < productOptionsArray.length(); index++) {

                        JSONObject innerObj = productOptionsArray.getJSONObject(index);

                        String optionEnable = JSONObJValidator.stringTagValidate(innerObj, "option_require", "0");

                        String optionId = JSONObJValidator.stringTagValidate(innerObj, "option_id", "0");
                        String optionName = JSONObJValidator.stringTagValidate(innerObj, "option_name", "");
                        String optionSortOrder = JSONObJValidator.stringTagValidate(innerObj, "option_sort_order", "0");

                        String subOptionId = JSONObJValidator.stringTagValidate(innerObj, "sub_option_ids", "0");
                        String subOptionName = JSONObJValidator.stringTagValidate(innerObj, "sub_option_names", "");
                        String subOptionSortOrder = JSONObJValidator.stringTagValidate(innerObj, "sub_option_sort_order", "0");
                        String subOptionPrice = JSONObJValidator.stringTagValidate(innerObj, "sub_option_prices", "0.00");

                        ProductOption productOption = new ProductOption(productId, optionEnable, optionId, optionName, optionSortOrder, subOptionId, subOptionName, subOptionSortOrder, subOptionPrice);
                        posProductOptionTable.insertData(productOption);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

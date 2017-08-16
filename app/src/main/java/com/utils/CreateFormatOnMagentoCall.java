package com.utils;

import com.Beans.CheckOutParentModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CreateFormatOnMagentoCall {

    private List<CheckOutParentModel> dataList;

    public CreateFormatOnMagentoCall(List<CheckOutParentModel> dataList) {
        super();
        this.dataList = dataList;
    }

    public JSONObject obj() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray parentJson = new JSONArray();

            for (int index = dataList.size() - 1; index >= 0; index--) {
                CheckOutParentModel parent = dataList.get(index);
                ProductModel product = dataList.get(index).getProduct();
                List<RelationalOptionModel> childs = dataList.get(index).getChilds();

                if (!parent.getExtraArgument().isSurageApplicable()) {

                    JSONObject firstChild = new JSONObject();
                    firstChild.put("product_id", "" + product.getProductId());
                    firstChild.put("product_qty", product.getProductQty());
                    firstChild.put("product_price", "" + product.getProductPrice());
                    firstChild.put("product_discount", "" + product.getProductDisAmount());

                    JSONArray productDetailsArray = new JSONArray();
                    for (int value = childs.size() - 1; value >= 0; value--) {

                        JSONObject innerObj = new JSONObject();
                        RelationalOptionModel productOptionItems = childs.get(value);
                        String optionId = productOptionItems.getOptionModel().getOptionId();
                        String allSuboptionIdBasedOnOptionId = findAllSubOptionId(productOptionItems.getListOfSubOptionModel());
                        innerObj.put("option_id", "" + optionId);
                        innerObj.put("sub_option_id", "" + allSuboptionIdBasedOnOptionId);
                        productDetailsArray.put(innerObj);

                    }
                    firstChild.put("productDetails", productDetailsArray);
                    parentJson.put(firstChild);
                }
            }
            jsonObject.put("Result", parentJson);
            System.out.println(jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject createJSONObjForRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray parentJson = new JSONArray();

            for (int index = dataList.size() - 1; index >= 0; index--) {
                CheckOutParentModel parent = dataList.get(index);
                ProductModel product = dataList.get(index).getProduct();
                List<RelationalOptionModel> childs = dataList.get(index).getChilds();

                if (!parent.getExtraArgument().isSurageApplicable()) {

                    JSONObject firstChild = new JSONObject();
                    firstChild.put("product_id", "" + product.getProductId());
                    firstChild.put("product_qty", product.getProductQty());
                    firstChild.put("product_price", "" + product.getProductPrice());
                    firstChild.put("product_discount", "" + product.getProductDisAmount());

                    JSONArray productDetailsArray = new JSONArray();
                    for (int value = childs.size() - 1; value >= 0; value--) {

                        JSONObject innerObj = new JSONObject();
                        RelationalOptionModel productOptionItems = childs.get(value);
                        String optionId = productOptionItems.getOptionModel().getOptionId();
                        String allSuboptionIdBasedOnOptionId = findAllSubOptionId(productOptionItems.getListOfSubOptionModel());
                        innerObj.put("option_id", "" + optionId);
                        innerObj.put("sub_option_id", "" + allSuboptionIdBasedOnOptionId);
                        productDetailsArray.put(innerObj);

                    }
                    firstChild.put("productDetails", productDetailsArray);
                    parentJson.put(firstChild);
                }
            }
            jsonObject.put("Result", parentJson);
            System.out.println(jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject createJSONFormatOnRefundTime() {

        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray parentJson = new JSONArray();

            for (int index = dataList.size() - 1; index >= 0; index--) {
                CheckOutParentModel parent = dataList.get(index);
                ProductModel product = dataList.get(index).getProduct();
                if (!parent.getExtraArgument().isSurageApplicable()) {

                    JSONObject firstChild = new JSONObject();
                    firstChild.put("product_id", "" + product.getProductId());
                    firstChild.put("product_qty", product.getProductQty());
                    parentJson.put(firstChild);
                }
            }
            jsonObject.put("Result", parentJson);
            System.out.println(jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;


    }


    public String findAllSubOptionId(List<SubOptionModel> childs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int value = childs.size() - 1; value >= 0; value--) {
            SubOptionModel subOptionModel = childs.get(value);
            stringBuilder.append((stringBuilder.length() == 0) ? subOptionModel.getSubOptionId() : "," + subOptionModel.getSubOptionId());
        }
        return stringBuilder.toString();
    }
}

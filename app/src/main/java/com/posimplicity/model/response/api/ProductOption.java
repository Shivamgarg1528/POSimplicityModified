package com.posimplicity.model.response.api;

import com.easylibs.sqlite.IModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 17/6/17.
 */

public class ProductOption implements IModel {

    // local added
    private String productId;


    @SerializedName("option_require")
    private String optionRequire;
    @SerializedName("option_id")
    private String optionId;
    @SerializedName("option_name")
    private String optionName;
    @SerializedName("option_sort_order")
    private String optionSortOrder;
    @SerializedName("sub_option_ids")
    private String subOptionIds;
    @SerializedName("sub_option_names")
    private String subOptionNames;
    @SerializedName("sub_option_sort_order")
    private String subOptionSortOrder;
    @SerializedName("sub_option_prices")
    private String subOptionPrices;

    public ProductOption(String productId, String optionRequire, String optionId, String optionName, String optionSortOrder, String subOptionIds, String subOptionNames, String subOptionSortOrder, String subOptionPrices) {
        this.productId = productId;
        this.optionRequire = optionRequire;
        this.optionId = optionId;
        this.optionName = optionName;
        this.optionSortOrder = optionSortOrder;
        this.subOptionIds = subOptionIds;
        this.subOptionNames = subOptionNames;
        this.subOptionSortOrder = subOptionSortOrder;
        this.subOptionPrices = subOptionPrices;
    }

    public ProductOption() {
        /*
         Empty Constructor
         */
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionSortOrder() {
        return optionSortOrder;
    }

    public void setOptionSortOrder(String optionSortOrder) {
        this.optionSortOrder = optionSortOrder;
    }

    public String getOptionRequire() {
        return optionRequire;
    }

    public void setOptionRequire(String optionRequire) {
        this.optionRequire = optionRequire;
    }

    public String getSubOptionIds() {
        return subOptionIds;
    }

    public void setSubOptionIds(String subOptionIds) {
        this.subOptionIds = subOptionIds;
    }

    public String getSubOptionPrices() {
        return subOptionPrices;
    }

    public void setSubOptionPrices(String subOptionPrices) {
        this.subOptionPrices = subOptionPrices;
    }

    public String getSubOptionNames() {
        return subOptionNames;
    }

    public void setSubOptionNames(String subOptionNames) {
        this.subOptionNames = subOptionNames;
    }

    public String getSubOptionSortOrder() {
        return subOptionSortOrder;
    }

    public void setSubOptionSortOrder(String subOptionSortOrder) {
        this.subOptionSortOrder = subOptionSortOrder;
    }

    @Override
    public long getRowId() {
        return 0;
    }

    @Override
    public void setRowId(long pRowId) {

    }

    @Override
    public String toString() {
        return "ProductOption{" +
                "productId='" + productId + '\'' +
                ", optionRequire='" + optionRequire + '\'' +
                ", optionId='" + optionId + '\'' +
                ", optionName='" + optionName + '\'' +
                ", optionSortOrder='" + optionSortOrder + '\'' +
                ", subOptionIds='" + subOptionIds + '\'' +
                ", subOptionNames='" + subOptionNames + '\'' +
                ", subOptionSortOrder='" + subOptionSortOrder + '\'' +
                ", subOptionPrices='" + subOptionPrices + '\'' +
                '}';
    }
}

package com.posimplicity.model.response.api;

import com.easylibs.sqlite.IModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductParent {

    @SerializedName("product_list")
    private List<Product> dataList;

    public List<Product> getDataList() {
        return dataList;
    }

    public static class Product implements IModel {

        @SerializedName("image")
        private String productImage;
        @SerializedName("image_shown")
        private int productImageShown;
        @SerializedName("cat_id")
        private String productCatId;
        @SerializedName("sku")
        private String productSku;
        @SerializedName("product_id")
        private String productId;
        @SerializedName("name")
        private String productName;
        @SerializedName("description")
        private String productDescription;
        @SerializedName("short_description")
        private String productShortDescription;
        @SerializedName("weight")
        private String productWeight;
        @SerializedName("created_at")
        private String productCreatedAt;
        @SerializedName("updated_at")
        private String productUpdatedAt;
        @SerializedName("price")
        private String productPrice;
        @SerializedName("special_price")
        private String productSpecialPrice;
        @SerializedName("tax_class_id")
        private String productTaxClassId;
        @SerializedName("is_active")
        private String productIsActive;
        @SerializedName("image_text")
        private String productImageText;
        @SerializedName("tax_rate")
        private float productTaxRate;
        @SerializedName("position")
        private String productPosition;

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public int getProductImageShown() {
            return productImageShown;
        }

        public void setProductImageShown(int productImageShown) {
            this.productImageShown = productImageShown;
        }

        public String getProductCatId() {
            return productCatId;
        }

        public void setProductCatId(String productCatId) {
            this.productCatId = productCatId;
        }

        public String getProductSku() {
            return productSku;
        }

        public void setProductSku(String productSku) {
            this.productSku = productSku;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public String getProductShortDescription() {
            return productShortDescription;
        }

        public void setProductShortDescription(String productShortDescription) {
            this.productShortDescription = productShortDescription;
        }

        public String getProductWeight() {
            return productWeight;
        }

        public void setProductWeight(String productWeight) {
            this.productWeight = productWeight;
        }

        public String getProductCreatedAt() {
            return productCreatedAt;
        }

        public void setProductCreatedAt(String productCreatedAt) {
            this.productCreatedAt = productCreatedAt;
        }

        public String getProductUpdatedAt() {
            return productUpdatedAt;
        }

        public void setProductUpdatedAt(String productUpdatedAt) {
            this.productUpdatedAt = productUpdatedAt;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductSpecialPrice() {
            return productSpecialPrice;
        }

        public void setProductSpecialPrice(String productSpecialPrice) {
            this.productSpecialPrice = productSpecialPrice;
        }

        public String getProductTaxClassId() {
            return productTaxClassId;
        }

        public void setProductTaxClassId(String productTaxClassId) {
            this.productTaxClassId = productTaxClassId;
        }

        public String getProductIsActive() {
            return productIsActive;
        }

        public void setProductIsActive(String productIsActive) {
            this.productIsActive = productIsActive;
        }

        public String getProductImageText() {
            return productImageText;
        }

        public void setProductImageText(String productImageText) {
            this.productImageText = productImageText;
        }

        public float getProductTaxRate() {
            return productTaxRate;
        }

        public void setProductTaxRate(float productTaxRate) {
            this.productTaxRate = productTaxRate;
        }

        public String getProductPosition() {
            return productPosition;
        }

        public void setProductPosition(String productPosition) {
            this.productPosition = productPosition;
        }

        @Override
        public long getRowId() {
            return 0;
        }

        @Override
        public void setRowId(long pRowId) {

        }
    }
}

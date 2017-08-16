package com.Beans;

import com.utils.MyStringFormat;

public class ProductModel implements Comparable<ProductModel> {

    private String productId;
    private String productTaxId;
    private String productName;
    private String productDescription;
    private String productCreatedAt;
    private String productUpdatedAt;
    private String productSKU;
    private String productImageText;
    private String productImageUrl;
    private String productQty;
    private String productWeight;
    private String productSpecialPrice;
    private String productPrice;
    private String productTaxRate;
    private String productDisAmount;
    private String productOptionsPrice;
    private String productCategoryId;
    private String productPosition;
    private String productIsActive;
    private String productLocation = "0";
    private String productQtyOnPendingTime = "0";
    private boolean productImageShown = false;


    public ProductModel() {
    }

    public ProductModel(String productId, String productTaxId,
                        String productName, String productDescription,
                        String productCreatedAt, String productUpdatedAt,
                        String productSKU, String productImageText, String productImageUrl,
                        String productWeight, String productSpecialPrice,
                        String productPrice, String productTaxRate,
                        String productCategoryId, String productPosition, String productIsActive, boolean productImageShown) {
        super();
        this.productId = productId;
        this.productTaxId = productTaxId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCreatedAt = productCreatedAt;
        this.productUpdatedAt = productUpdatedAt;
        this.productSKU = productSKU;
        this.productImageText = productImageText;
        this.productImageUrl = productImageUrl;
        this.productWeight = productWeight;
        this.productSpecialPrice = productSpecialPrice;
        this.productPrice = productPrice;
        this.productTaxRate = productTaxRate;
        this.productCategoryId = productCategoryId;
        this.productPosition = productPosition;
        this.productIsActive = productIsActive;
        this.productImageShown = productImageShown;
    }

    public ProductModel(String productName, String productPrice) {
        this.productId = "-32767";
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = "1";
        this.productOptionsPrice = "0.00";
        this.productTaxRate = "0.00";
        this.productDisAmount = "0.00";
    }


    public boolean isProductImageShown() {
        return productImageShown;
    }


    public String getProductLocation() {
        return productLocation;
    }


    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }


    public void setProductImageShown(boolean productImageShown) {
        this.productImageShown = productImageShown;
    }


    public String getProductQtyOnPendingTime() {
        return productQtyOnPendingTime;
    }

    public void setProductQtyOnPendingTime(String productQtyOnPendingTime) {
        this.productQtyOnPendingTime = productQtyOnPendingTime;
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

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductPosition() {
        return productPosition;
    }


    public void setProductPosition(String productPosition) {
        this.productPosition = productPosition;
    }


    public String getProductCategoryId() {
        return productCategoryId;
    }


    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTaxId() {
        return productTaxId;
    }

    public void setProductTaxId(String productTaxId) {
        this.productTaxId = productTaxId;
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

    public String getProductSKU() {
        return productSKU;
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductSpecialPrice() {
        return productSpecialPrice;
    }

    public void setProductSpecialPrice(String productSpecialPrice) {
        this.productSpecialPrice = productSpecialPrice;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductTaxRate() {
        return productTaxRate;
    }

    public void setProductTaxRate(String productTaxRate) {
        this.productTaxRate = productTaxRate;
    }

    public String getProductCalAmount() {

        float totalCalculatePrice = Integer.parseInt(productQty) * (Float.parseFloat(productPrice) + Float.parseFloat(productOptionsPrice));
        String productCalAmount = MyStringFormat.formatWith2DecimalPlaces(totalCalculatePrice);
        return productCalAmount;

    }

    public String getProductAndOptionPrice() {

        float totalCalculatePrice = (Float.parseFloat(productPrice) + Float.parseFloat(productOptionsPrice));
        String productCalAmount = MyStringFormat.formatWith2DecimalPlaces(totalCalculatePrice);
        return productCalAmount;

    }

    public String getProductDisAmount() {
        // TODO : changes done
        float totalDisAmount = (Float.parseFloat(productDisAmount) * Float.parseFloat("1"));
        String productDisAmount = MyStringFormat.formatWith2DecimalPlaces(totalDisAmount);
        return productDisAmount;

    }

    public void setProductDisAmount(String productDisAmount) {
        this.productDisAmount = productDisAmount;
    }

    public String getProductOptionsPrice() {
        return productOptionsPrice;
    }

    public void setProductOptionsPrice(String productOptionsPrice) {
        this.productOptionsPrice = productOptionsPrice;
    }

    public void upgradeQtyByOne() {
        this.productQty = String.valueOf(Integer.parseInt(this.productQty) + 1);
    }

    @Override
    public int compareTo(ProductModel another) {
        return Integer.parseInt(productLocation) - Integer.parseInt(another.getProductLocation());
    }

    @Override
    public String toString() {
        return "ProductModel [productId=" + productId + ", productTaxId="
                + productTaxId + ", productName=" + productName
                + ", productDescription=" + productDescription
                + ", productCreatedAt=" + productCreatedAt
                + ", productUpdatedAt=" + productUpdatedAt + ", productSKU="
                + productSKU + ", productImageText=" + productImageText
                + ", productImageUrl=" + productImageUrl + ", productQty="
                + productQty + ", productWeight=" + productWeight
                + ", productSpecialPrice=" + productSpecialPrice
                + ", productPrice=" + productPrice + ", productTaxRate="
                + productTaxRate + ", productDisAmount=" + productDisAmount
                + ", productOptionsPrice=" + productOptionsPrice
                + ", productCategoryId=" + productCategoryId
                + ", productPosition=" + productPosition + ", productIsActive="
                + productIsActive + ", productLocation=" + productLocation
                + ", productQtyOnPendingTime=" + productQtyOnPendingTime
                + ", productImageShown=" + productImageShown + "]";
    }

}
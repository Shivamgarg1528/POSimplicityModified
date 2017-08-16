package com.Beans;


public class ProductToCategoryModel {

	private String productId;
	private String productCategoryId;
	private String productLocation;

	public ProductToCategoryModel() {}

	public ProductToCategoryModel(String productId, String productCategoryId,
			String productLocation) {
		super();
		this.productId = productId;
		this.productCategoryId = productCategoryId;
		this.productLocation = productLocation;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductLocation() {
		return productLocation;
	}

	public void setProductLocation(String productLocation) {
		this.productLocation = productLocation;
	}

	@Override
	public String toString() {
		return "ProductToCategoryModel [productId=" + productId
				+ ", productCategoryId=" + productCategoryId
				+ ", productLocation=" + productLocation + "]";
	}
	
}
	
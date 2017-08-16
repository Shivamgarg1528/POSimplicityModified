package com.Beans;

public class ProductOptionsModel {

	private String productId;
	private String optionId;
	private String optionName;
	private String optionSortOrder;
	private String subOptionId;
	private String subOptionName;
	private String subOptionPrice;
	private String subOptionSortOrder;
	private String optionEnable;
	
	public ProductOptionsModel(String productId, String optionId,
			String optionName, String optionSortOrder, String subOptionId,
			String subOptionName, String subOptionPrice,
			String subOptionSortOrder, String optionEnable) {
		super();
		this.productId = productId;
		this.optionId = optionId;
		this.optionName = optionName;
		this.optionSortOrder = optionSortOrder;
		this.subOptionId = subOptionId;
		this.subOptionName = subOptionName;
		this.subOptionPrice = subOptionPrice;
		this.subOptionSortOrder = subOptionSortOrder;
		this.optionEnable = optionEnable;
	}

	public ProductOptionsModel() {}

	@Override
	public String toString() {
		return "ProductOptionsModel [productId=" + productId + ", optionId="
				+ optionId + ", optionName=" + optionName
				+ ", optionSortOrder=" + optionSortOrder + ", subOptionId="
				+ subOptionId + ", subOptionName=" + subOptionName
				+ ", subOptionPrice=" + subOptionPrice
				+ ", subOptionSortOrder=" + subOptionSortOrder
				+ ", OptionEnable=" + optionEnable + "]";
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

	public String getSubOptionId() {
		return subOptionId;
	}

	public void setSubOptionId(String subOptionId) {
		this.subOptionId = subOptionId;
	}

	public String getSubOptionName() {
		return subOptionName;
	}

	public void setSubOptionName(String subOptionName) {
		this.subOptionName = subOptionName;
	}

	public String getSubOptionPrice() {
		return subOptionPrice;
	}

	public void setSubOptionPrice(String subOptionPrice) {
		this.subOptionPrice = subOptionPrice;
	}

	public String getSubOptionSortOrder() {
		return subOptionSortOrder;
	}

	public void setSubOptionSortOrder(String subOptionSortOrder) {
		this.subOptionSortOrder = subOptionSortOrder;
	}

	public String getOptionEnable() {
		return optionEnable;
	}

	public void setOptionEnable(String optionEnable) {
		this.optionEnable = optionEnable;
	}
	
}

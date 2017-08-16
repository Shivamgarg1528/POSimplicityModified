package com.Beans;

public class OptionModel implements Comparable<OptionModel>{
	
	private String  productId;
	private String  optionId;
	private String  optionName;
	private String  optionSortOrder;
	private boolean isEnable;
	
	public OptionModel(String productId, String optionId, String optionName,
			String optionSortOrder, boolean isEnable) {
		super();
		this.productId = productId;
		this.optionId = optionId;
		this.optionName = optionName;
		this.optionSortOrder = optionSortOrder;
		this.isEnable = isEnable;
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

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	@Override
	public int compareTo(OptionModel another) {		
		return Integer.parseInt(optionSortOrder) - Integer.parseInt(another.optionSortOrder);
	}	
}

package com.Beans;

public class SubOptionModel implements Comparable<SubOptionModel>{

	private String subOptionId;
	private String subOptionName;
	private String subOptionPrice;
	private String subOptionSortOrder;
	private boolean isSelected;



	public SubOptionModel(String subOptionId, String subOptionName,
			String subOptionPrice, String subOptionSortOrder) {
		super();
		this.subOptionId = subOptionId;
		this.subOptionName = subOptionName;
		this.subOptionPrice = subOptionPrice;
		this.subOptionSortOrder = subOptionSortOrder;
	}



	@Override
	public String toString() {
		return "SubOptionModel [subOptionId=" + subOptionId
				+ ", subOptionName=" + subOptionName + ", subOptionPrice="
				+ subOptionPrice + ", subOptionSortOrder=" + subOptionSortOrder
				+ "]";
	}



	public boolean isSelected() {
		return isSelected;
	}



	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
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



	@Override
	public int compareTo(SubOptionModel another) {
		return  Integer.parseInt(subOptionSortOrder) - Integer.parseInt(another.subOptionSortOrder);
	}
}

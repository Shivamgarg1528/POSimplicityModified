package com.Beans;

public class CCPaidInfoModel {
	
	private int index;
	private String titleName;
	private String titleValue;
	
	public CCPaidInfoModel(int index, String titleName, String titleValue) {
		super();
		this.index = index;
		this.titleName = titleName;
		this.titleValue = titleValue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getTitleValue() {
		return titleValue;
	}

	public void setTitleValue(String titleValue) {
		this.titleValue = titleValue;
	}
}

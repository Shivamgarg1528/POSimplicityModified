package com.Beans;

public class ReportModel {
	
	private String name;
	private String value;
	private boolean clickable;

	public ReportModel(String name, String value, boolean pClickable) {
		super();
		this.name = name;
		this.value = value;
		this.clickable = pClickable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
}

package com.Beans;

public class FunctionModel {
	
	private boolean isEnable;
	private String text;
	
	public FunctionModel(boolean isEnable, String text) {
		super();
		this.isEnable = isEnable;
		this.text     = text;
	}
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "FunctionClass [isEnable=" + isEnable + ", text=" + text + "]";
	}
	
	
	
}

package com.Beans;

public class NavDrawerItemModel {

	private String title;
	private int icon;	

	public NavDrawerItemModel(){
		
	}

	public NavDrawerItemModel(String title, int icon){
		this.title = title;
		this.icon = icon;
	}

	public String getTitle(){
		return this.title;
	}

	public int getIcon(){
		return this.icon;
	}


	public void setTitle(String title){
		this.title = title;
	}

	public void setIcon(int icon){
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "NavDrawerItem [title=" + title + ", icon=" + icon + "]";
	}
	
	
}

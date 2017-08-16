package com.Beans;

public class ExtraProductArgument {

	private boolean isSurageApplicable;
	private boolean isPendingItems;


	public boolean isSurageApplicable() {
		return isSurageApplicable;
	}
	public void setSurageApplicable(boolean isSurageApplicable) {
		this.isSurageApplicable = isSurageApplicable;
	}

	public boolean isPendingItems() {
		return isPendingItems;
	}
	public void setPendingItems(boolean isPendingItems) {
		this.isPendingItems = isPendingItems;
	}

	public ExtraProductArgument(boolean isSurageApplicable,
			boolean isPendingItems) {
		super();
		this.isSurageApplicable = isSurageApplicable;
		this.isPendingItems     = isPendingItems;
	}
}

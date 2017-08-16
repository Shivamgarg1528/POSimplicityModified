package com.Beans;

public class TransactionModel {
	
	private String clerkId;
	private String transId;
	private String date;
	
	public TransactionModel(String clerkId, String transId, String date) {
		super();
		this.clerkId = clerkId;
		this.transId = transId;
		this.date = date;
	}

	public TransactionModel() {
		// TODO Auto-generated constructor stub
	}

	public String getClerkId() {
		return clerkId;
	}

	public void setClerkId(String clerkId) {
		this.clerkId = clerkId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	

}

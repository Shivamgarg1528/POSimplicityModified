package com.Beans;

import java.util.ArrayList;

public class PendingOrderModel {

	private String transactionId;
	private String incrementId;
	private CustomerModel customerOrClerkBillToNameModel;
	private CustomerModel tableOrClerkShipToNameModel;
	private String orderStatus;
	private String orderDiscount;
	private ArrayList<CheckOutParentModel> productList;


	public PendingOrderModel(String transactionId, String incrementId,
			CustomerModel customerOrClerkBillToNameModel,
			CustomerModel tableOrClerkShipToNameModel, String orderStatus,
			String orderDiscount, ArrayList<CheckOutParentModel> productList) {
		super();
		this.transactionId = transactionId;
		this.incrementId = incrementId;
		this.customerOrClerkBillToNameModel = customerOrClerkBillToNameModel;
		this.tableOrClerkShipToNameModel = tableOrClerkShipToNameModel;
		this.orderStatus = orderStatus;
		this.orderDiscount = orderDiscount;
		this.productList = productList;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public String getIncrementId() {
		return incrementId;
	}


	public void setIncrementId(String incrementId) {
		this.incrementId = incrementId;
	}


	public CustomerModel getCustomerOrClerkBillToNameModel() {
		return customerOrClerkBillToNameModel;
	}


	public void setCustomerOrClerkBillToNameModel(
			CustomerModel customerOrClerkBillToNameModel) {
		this.customerOrClerkBillToNameModel = customerOrClerkBillToNameModel;
	}


	public CustomerModel getTableOrClerkShipToNameModel() {
		return tableOrClerkShipToNameModel;
	}


	public void setTableOrClerkShipToNameModel(
			CustomerModel tableOrClerkShipToNameModel) {
		this.tableOrClerkShipToNameModel = tableOrClerkShipToNameModel;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getOrderDiscount() {
		return orderDiscount;
	}


	public void setOrderDiscount(String orderDiscount) {
		this.orderDiscount = orderDiscount;
	}


	public ArrayList<CheckOutParentModel> getProductList() {
		return productList;
	}


	public void setProductList(ArrayList<CheckOutParentModel> productList) {
		this.productList = productList;
	}
}

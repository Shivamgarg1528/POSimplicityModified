package com.Beans;

public class CustomerGroup {
	
	private String customerGroupId;
	private String customerGroupName;
	
	
	public CustomerGroup(String customerGroupId, String customerGroupName) {
		super();
		this.customerGroupId = customerGroupId;
		this.customerGroupName = customerGroupName;
	}


	public CustomerGroup() {}


	public String getCustomerGroupId() {
		return customerGroupId;
	}


	public void setCustomerGroupId(String customerGroupId) {
		this.customerGroupId = customerGroupId;
	}


	public String getCustomerGroupName() {
		return customerGroupName;
	}


	public void setCustomerGroupName(String customerGroupName) {
		this.customerGroupName = customerGroupName;
	}


	@Override
	public String toString() {
		return "CustomerGroupParent [customerGroupId=" + customerGroupId
				+ ", customerGroupName=" + customerGroupName + "]";
	}
	
	
	
	
	
	
	

}

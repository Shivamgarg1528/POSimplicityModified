package com.Beans;

public class CustomerModel implements Comparable<CustomerModel>  {

	private String customerId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String telephoneNo;
	private String permanantAddress;
	private String tipAmount;
	private String groupId;
	private boolean isRowSelected;
	private boolean isCustomerLogin;

	public CustomerModel(String customerId, String firstName, String lastName,
			String emailAddress, String telephoneNo, String permanantAddress,
			String groupId, boolean isRowSelected, boolean isCustomerLogin) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.telephoneNo = telephoneNo;
		this.permanantAddress = permanantAddress;
		this.groupId = groupId;
		this.isRowSelected = isRowSelected;
		this.isCustomerLogin = isCustomerLogin;
	}

	public CustomerModel(String customerId, String firstName, String tipAmount) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.tipAmount = tipAmount;
	}
	
	
	
	public boolean isRowSelected() {
		return isRowSelected;
	}

	public void setRowSelected(boolean isRowSelected) {
		this.isRowSelected = isRowSelected;
	}

	public boolean isCustomerLogin() {
		return isCustomerLogin;
	}

	public void setCustomerLogin(boolean isCustomerLogin) {
		this.isCustomerLogin = isCustomerLogin;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public CustomerModel(){
		
		this.customerId       = "-1";
		this.firstName        = "Default";
		this.lastName         = "";
		this.emailAddress     = "";
		this.telephoneNo      = "0";
		this.groupId          = "0";
		this.permanantAddress = "";
	}
	
	public boolean isCustomerNotValid(){
		return customerId.equalsIgnoreCase("-1");
	}

	public String getTipAmount() {
		return tipAmount;
	}




	public void setTipAmount(String tipAmount) {
		this.tipAmount = tipAmount;
	}


	public String getFullName() {
		return firstName + lastName;
	}

	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public String getTelephoneNo() {
		return telephoneNo;
	}


	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}


	public String getPermanantAddress() {
		return permanantAddress;
	}


	public void setPermanantAddress(String permanantAddress) {
		this.permanantAddress = permanantAddress;
	}



	@Override
	public String toString() {
		return "CustomerParent [customerId=" + customerId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + ", telephoneNo=" + telephoneNo
				+ ", permanantAddress=" + permanantAddress + ", tipAmount="
				+ tipAmount + ", groupId=" + groupId + ", isRowSelected="
				+ isRowSelected + ", isCustomerLogin=" + isCustomerLogin + "]";
	}

	@Override
	public int compareTo(CustomerModel customer) {
		return Integer.parseInt(customerId) - Integer.parseInt(customer.getCustomerId());
	}
}

package com.Beans;

public class StaffModel implements Comparable<StaffModel>{

	private String staffId;
	private String staffName;
	private String staffPayGrade;
	private boolean isStaffLogin;
	private boolean isRowSelected;
	
	

	public StaffModel(String staffId, String staffName, String staffPayGrade,
			boolean isStaffLogin, boolean isRowSelected) {
		super();
		this.staffId = staffId;
		this.staffName = staffName;
		this.staffPayGrade = staffPayGrade;
		this.isStaffLogin = isStaffLogin;
		this.isRowSelected = isRowSelected;
	}



	public StaffModel() {}



	public String getStaffId() {
		return staffId;
	}



	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}



	public String getStaffName() {
		return staffName;
	}



	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}



	public String getStaffPayGrade() {
		return staffPayGrade;
	}



	public void setStaffPayGrade(String staffPayGrade) {
		this.staffPayGrade = staffPayGrade;
	}



	public boolean isStaffLogin() {
		return isStaffLogin;
	}



	public void setStaffLogin(boolean isStaffLogin) {
		this.isStaffLogin = isStaffLogin;
	}



	public boolean isRowSelected() {
		return isRowSelected;
	}



	public void setRowSelected(boolean isRowSelected) {
		this.isRowSelected = isRowSelected;
	}



	@Override
	public int compareTo(StaffModel rhs) {
		return Integer.parseInt(getStaffId())- Integer.parseInt(rhs.getStaffId());
	}	

}

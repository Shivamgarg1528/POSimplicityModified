package com.Beans;

public class SplitBillClass {
	
	private int rowId;
	private int paymentMode;
	private String rowName;
	private String splitBillPayAmount;
	private String totalBillAmount;
	private String splitBillAmount;
	private String splitBillPaidAmount;
	private String splitBillDueAmount;
	private String splitBillAmountText;
	private String splitBillPayAmountText;
	private boolean isBillPaid;
	private boolean isRowSelected;
	private boolean isPartOfBillPaid;
	private final String CUSTOMER = "Bill ";

	public SplitBillClass(int rowId, int paymentMode, int rowName,
			String splitBillPayAmount, String totalBillAmount,
			String splitBillAmount, String splitBillPaidAmount,
			String splitBillDueAmount, boolean isBillPaid, boolean isRowSelected, boolean isPartOfBillPaid) {
		super();
		this.rowId = rowId;
		this.paymentMode = paymentMode;
		this.splitBillPayAmount = splitBillPayAmount;
		this.totalBillAmount = totalBillAmount;
		this.splitBillAmount = splitBillAmount;
		this.splitBillPaidAmount = splitBillPaidAmount;
		this.splitBillDueAmount = splitBillDueAmount;
		this.isBillPaid = isBillPaid;
		this.isRowSelected = isRowSelected;
		this.splitBillAmountText = splitBillAmount;
		this.splitBillPayAmountText = splitBillPayAmount;
		this.isPartOfBillPaid = isPartOfBillPaid;
		setRowName(rowName);
	}
	
	
	public boolean isPartOfBillPaid() {
		return isPartOfBillPaid;
	}

	public void setPartOfBillPaid(boolean isPartOfBillPaid) {
		this.isPartOfBillPaid = isPartOfBillPaid;
	}

	public String getSplitBillAmountText() {
		return splitBillAmountText;
	}

	public void setSplitBillAmountText(String splitBillAmountText) {
		this.splitBillAmountText = splitBillAmountText;
	}

	public String getSplitBillPayAmountText() {
		return splitBillPayAmountText;
	}


	public void setSplitBillPayAmountText(String splitBillPayAmountText) {
		this.splitBillPayAmountText = splitBillPayAmountText;
	}



	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(int rowName) {
		this.rowName = CUSTOMER + rowName;
	}

	public String getSplitBillPayAmount() {
		return splitBillPayAmount;
	}

	public void setSplitBillPayAmount(String splitBillPayAmount) {
		this.splitBillPayAmount = splitBillPayAmount;
	}

	public String getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(String totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public String getSplitBillAmount() {
		return splitBillAmount;
	}

	public void setSplitBillAmount(String splitBillAmount) {
		this.splitBillAmount = splitBillAmount;
	}

	public String getSplitBillPaidAmount() {
		return splitBillPaidAmount;
	}

	public void setSplitBillPaidAmount(String splitBillPaidAmount) {
		this.splitBillPaidAmount = splitBillPaidAmount;
	}

	public String getSplitBillDueAmount() {
		return splitBillDueAmount;
	}

	public void setSplitBillDueAmount(String splitBillDueAmount) {
		this.splitBillDueAmount = splitBillDueAmount;
	}

	public boolean isBillPaid() {
		return isBillPaid;
	}

	public void setBillPaid(boolean isBillPaid) {
		this.isBillPaid = isBillPaid;
	}

	public boolean isRowSelected() {
		return isRowSelected;
	}

	public void setRowSelected(boolean isRowSelected) {
		this.isRowSelected = isRowSelected;
	}
	
	
	
	
}
package com.Beans;

public class ReportsModel {


	private String totalAmount;
	private String taxAmount;
	private String totalWithTaxAmount;
	private String cashAmount;
	private String creditAmount;
	private String checkAmount;
	private String custom1Amount;
	private String custom2Amount;
	private String giftAmount;
	private String rewardAmount;

	private String transTime;	
	private String refundStatus;
	private String saveState;
	private String lotteryAmount;
	private String expensesAmount;
	private String suppliesAmount;
	private String productAmount;
	private String otherAmount;
	private String tipPayAmount;
	private String manualCashRefund;
	private String descrption;
	private String reportName;
	private String tipAmount         = "0.00";
	private String noInternetOrders  = "0.00";
	private String manuallyRecOrders = "0.00";
	private String payoutType;



	public ReportsModel(String totalAmount, String taxAmount,
			String totalWithTaxAmount, String cashAmount, String creditAmount,
			String checkAmount, String giftAmount, String rewardAmount,
			String transTime, String refundStatus, String saveState,
			String lotteryAmount, String expensesAmount, String suppliesAmount,
			String productAmount, String otherAmount,String tipPayAmount, String manualCashRefund,
			String descrption, String payoutType,String custom1Amt,String custom2Amt) {
		super();
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.totalWithTaxAmount = totalWithTaxAmount;
		this.cashAmount = cashAmount;
		this.creditAmount = creditAmount;
		this.checkAmount = checkAmount;
		this.giftAmount = giftAmount;
		this.rewardAmount = rewardAmount;
		this.transTime = transTime;
		this.refundStatus = refundStatus;
		this.saveState = saveState;
		this.lotteryAmount = lotteryAmount;
		this.expensesAmount = expensesAmount;
		this.suppliesAmount = suppliesAmount;
		this.productAmount = productAmount;
		this.otherAmount = otherAmount;
		this.tipPayAmount = tipPayAmount;
		this.manualCashRefund = manualCashRefund;
		this.descrption = descrption;
		this.payoutType = payoutType;
		this.custom1Amount = custom1Amt;
		this.custom2Amount = custom2Amt;
	}

	public ReportsModel() {

		this.totalAmount 		= "0.00";
		this.taxAmount 	    	= "0.00";
		this.totalWithTaxAmount = "0.00";
		this.cashAmount			= "0.00";
		this.creditAmount 		= "0.00";
		this.checkAmount		= "0.00";
		this.giftAmount 		= "0.00";
		this.rewardAmount 		= "0.00";
		this.transTime 			= "0.00";
		this.refundStatus		= "0.00";
		this.saveState		    = "0.00";
		this.lotteryAmount 		= "0.00";
		this.expensesAmount 	= "0.00";
		this.suppliesAmount 	= "0.00";
		this.productAmount		= "0.00";
		this.otherAmount 		= "0.00";
		this.tipPayAmount       = "0.00";
		this.manualCashRefund 	= "0.00";
		this.tipAmount          = "0.00";
		this.descrption 		= "Not Provided";
		this.payoutType         = "";
		this.custom1Amount		= "0.00";
		this.custom2Amount		= "0.00";
	}


	public String getPayoutType() {
		return payoutType;
	}

	public void setPayoutType(String payoutType) {
		this.payoutType = payoutType;
	}

	public String getTipPayAmount() {
		return tipPayAmount;
	}

	public void setTipPayAmount(String tipPayAmount) {
		this.tipPayAmount = tipPayAmount;
	}

	public String getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(String tipAmount) {
		this.tipAmount = tipAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTotalWithTaxAmount() {
		return totalWithTaxAmount;
	}

	public void setTotalWithTaxAmount(String totalWithTaxAmount) {
		this.totalWithTaxAmount = totalWithTaxAmount;
	}

	public String getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(String cashAmount) {
		this.cashAmount = cashAmount;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(String checkAmount) {
		this.checkAmount = checkAmount;
	}

	public String getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(String giftAmount) {
		this.giftAmount = giftAmount;
	}

	public String getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(String rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getSaveState() {
		return saveState;
	}

	public void setSaveState(String saveState) {
		this.saveState = saveState;
	}

	public String getLotteryAmount() {
		return lotteryAmount;
	}

	public void setLotteryAmount(String lotteryAmount) {
		this.lotteryAmount = lotteryAmount;
	}

	public String getExpensesAmount() {
		return expensesAmount;
	}

	public void setExpensesAmount(String expensesAmount) {
		this.expensesAmount = expensesAmount;
	}

	public String getSuppliesAmount() {
		return suppliesAmount;
	}

	public void setSuppliesAmount(String suppliesAmount) {
		this.suppliesAmount = suppliesAmount;
	}

	public String getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(String productAmount) {
		this.productAmount = productAmount;
	}

	public String getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(String otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getManualCashRefund() {
		return manualCashRefund;
	}

	public void setManualCashRefund(String manualCashRefund) {
		this.manualCashRefund = manualCashRefund;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getNoInternetOrders() {
		return noInternetOrders;
	}

	public void setNoInternetOrders(String noInternetOrders) {
		this.noInternetOrders = noInternetOrders;
	}

	public String getManuallyRecOrders() {
		return manuallyRecOrders;
	}

	public void setManuallyRecOrders(String manuallyRecOrders) {
		this.manuallyRecOrders = manuallyRecOrders;
	}


	/**
	 * @return the custom1Amount
	 */
	public String getCustom1Amount() {
		return custom1Amount;
	}

	/**
	 * @param custom1Amount the custom1Amount to set
	 */
	public void setCustom1Amount(String custom1Amount) {
		this.custom1Amount = custom1Amount;
	}

	/**
	 * @return the custom2Amount
	 */
	public String getCustom2Amount() {
		return custom2Amount;
	}

	/**
	 * @param custom2Amount the custom2Amount to set
	 */
	public void setCustom2Amount(String custom2Amount) {
		this.custom2Amount = custom2Amount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportsModel [totalAmount=" + totalAmount + ", taxAmount="
				+ taxAmount + ", totalWithTaxAmount=" + totalWithTaxAmount
				+ ", cashAmount=" + cashAmount + ", creditAmount="
				+ creditAmount + ", checkAmount=" + checkAmount
				+ ", custom1Amount=" + custom1Amount + ", custom2Amount="
				+ custom2Amount + ", giftAmount=" + giftAmount
				+ ", rewardAmount=" + rewardAmount + ", transTime=" + transTime
				+ ", refundStatus=" + refundStatus + ", saveState=" + saveState
				+ ", lotteryAmount=" + lotteryAmount + ", expensesAmount="
				+ expensesAmount + ", suppliesAmount=" + suppliesAmount
				+ ", productAmount=" + productAmount + ", otherAmount="
				+ otherAmount + ", tipPayAmount=" + tipPayAmount
				+ ", manualCashRefund=" + manualCashRefund + ", descrption="
				+ descrption + ", reportName=" + reportName + ", tipAmount="
				+ tipAmount + ", noInternetOrders=" + noInternetOrders
				+ ", manuallyRecOrders=" + manuallyRecOrders + ", payoutType="
				+ payoutType + "]";
	}
}

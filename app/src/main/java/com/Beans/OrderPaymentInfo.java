package com.Beans;

public class OrderPaymentInfo {

	private String orderId,paymentModeId,paymentAmount,orderDate,reportName;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderPaymentInfo [orderId=" + orderId + ", paymentModeId="
				+ paymentModeId + ", paymentAmount=" + paymentAmount
				+ ", orderDate=" + orderDate + ", orderStatus=" + reportName
				+ "]";
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the paymentModeId
	 */
	public String getPaymentModeId() {
		return paymentModeId;
	}

	/**
	 * @param paymentModeId the paymentModeId to set
	 */
	public void setPaymentModeId(String paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	/**
	 * @return the paymentAmount
	 */
	public String getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param paymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the orderStatus
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setReportName(String orderStatus) {
		this.reportName = orderStatus;
	}
	
}

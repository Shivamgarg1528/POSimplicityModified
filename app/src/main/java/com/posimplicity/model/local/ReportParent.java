package com.posimplicity.model.local;

import com.easylibs.sqlite.IModel;

import java.io.Serializable;

public class ReportParent implements IModel, Serializable {

    private String transactionId;

    // Trans Amount
    private float subtotalAmount;
    private float discountAmount;
    private float taxAmount;
    private float totalAmount;
    private float cashAmount;
    private float creditAmount;
    private float checkAmount;
    private float custom1Amount;
    private float custom2Amount;
    private float giftAmount;
    private float rewardAmount;

    // Payouts Amt
    private float lotteryAmount;
    private float expensesAmount;
    private float suppliesAmount;
    private float productListAmount;
    private float otherAmount;
    private float tipPayAmount;
    private float manualCashRefund;

    // Tip Amount
    private float tipAmount;

    // No Internet Orders
    private float noInternetOrders;

    // Manually Recorded Orders
    private float manuallyRecOrders;

    // Trans Date
    private String transTime;
    private String refundStatus;
    private String saveState;

    private String description;
    private String reportName;
    private String payoutType;


    @Override
    public long getRowId() {
        return 0;
    }

    @Override
    public void setRowId(long pRowId) {

    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public float getSubtotalAmount() {
        return subtotalAmount;
    }

    public void setSubtotalAmount(float subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public float getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(float taxAmount) {
        this.taxAmount = taxAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(float cashAmount) {
        this.cashAmount = cashAmount;
    }

    public float getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(float creditAmount) {
        this.creditAmount = creditAmount;
    }

    public float getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(float checkAmount) {
        this.checkAmount = checkAmount;
    }

    public float getCustom1Amount() {
        return custom1Amount;
    }

    public void setCustom1Amount(float custom1Amount) {
        this.custom1Amount = custom1Amount;
    }

    public float getCustom2Amount() {
        return custom2Amount;
    }

    public void setCustom2Amount(float custom2Amount) {
        this.custom2Amount = custom2Amount;
    }

    public float getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(float giftAmount) {
        this.giftAmount = giftAmount;
    }

    public float getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(float rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public float getLotteryAmount() {
        return lotteryAmount;
    }

    public void setLotteryAmount(float lotteryAmount) {
        this.lotteryAmount = lotteryAmount;
    }

    public float getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(float expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    public float getSuppliesAmount() {
        return suppliesAmount;
    }

    public void setSuppliesAmount(float suppliesAmount) {
        this.suppliesAmount = suppliesAmount;
    }

    public float getProductListAmount() {
        return productListAmount;
    }

    public void setProductListAmount(float productListAmount) {
        this.productListAmount = productListAmount;
    }

    public float getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(float otherAmount) {
        this.otherAmount = otherAmount;
    }

    public float getTipPayAmount() {
        return tipPayAmount;
    }

    public void setTipPayAmount(float tipPayAmount) {
        this.tipPayAmount = tipPayAmount;
    }

    public float getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(float tipAmount) {
        this.tipAmount = tipAmount;
    }

    public float getNoInternetOrders() {
        return noInternetOrders;
    }

    public void setNoInternetOrders(float noInternetOrders) {
        this.noInternetOrders = noInternetOrders;
    }

    public float getManuallyRecOrders() {
        return manuallyRecOrders;
    }

    public void setManuallyRecOrders(float manuallyRecOrders) {
        this.manuallyRecOrders = manuallyRecOrders;
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

    public float getManualCashRefund() {
        return manualCashRefund;
    }

    public void setManualCashRefund(float manualCashRefund) {
        this.manualCashRefund = manualCashRefund;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getPayoutType() {
        return payoutType;
    }

    public void setPayoutType(String payoutType) {
        this.payoutType = payoutType;
    }
}

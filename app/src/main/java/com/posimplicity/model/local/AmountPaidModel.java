package com.posimplicity.model.local;


public class AmountPaidModel {

    // order amount info

    private float mAmountSubTotal;
    private float mAmountTax;


    // bill amount,paid amount,dueAmount
    private float mAmount;
    private float mAmountPaid;
    private float mAmountDue;

    private float mAmountPaidByCheck;
    private float mAmountPaidByCredit;

    // cash amount, change amount for cash,gift,tender
    private float mAmountPaidByCash;
    private float mAmountChange;

    //gift amount
    private float mAmountPaidByGift;

    // rewards amount
    private float mAmountPaidByRewards;

    // custom1 amount
    private float mAmountPaidCustom1;

    // custom2 amount
    private float mAmountPaidCustom2;

    // discountAmt
    private float mAmountDiscount;

    public float getAmount() {
        return mAmount;
    }

    public void setAmount(float mAmount) {
        this.mAmount = mAmount;
    }

    public float getAmountPaid() {
        return mAmountPaid;
    }

    public void setAmountPaid(float mAmountPaid) {
        this.mAmountPaid = mAmountPaid;
    }

    public float getAmountDue() {
        return mAmountDue;
    }

    public void setAmountDue(float mAmountDue) {
        this.mAmountDue = mAmountDue;
    }

    public float getAmountPaidByCheck() {
        return mAmountPaidByCheck;
    }

    public void setAmountPaidByCheck(float mAmountPaidByCheck) {
        this.mAmountPaidByCheck = mAmountPaidByCheck;
    }

    public float getAmountPaidByCredit() {
        return mAmountPaidByCredit;
    }

    public void setAmountPaidByCredit(float mAmountPaidByCredit) {
        this.mAmountPaidByCredit = mAmountPaidByCredit;
    }

    public float getAmountPaidByGift() {
        return mAmountPaidByGift;
    }

    public void setAmountPaidByGift(float mAmountPaidByGift) {
        this.mAmountPaidByGift = mAmountPaidByGift;
    }

    public float getAmountPaidByRewards() {
        return mAmountPaidByRewards;
    }

    public void setAmountPaidByRewards(float amountPaidByRewards) {
        this.mAmountPaidByRewards = amountPaidByRewards;
    }

    public float getAmountPaidByCash() {
        return mAmountPaidByCash;
    }

    public void setAmountPaidByCash(float mAmountPaidByCash) {
        this.mAmountPaidByCash = mAmountPaidByCash;
    }

    public float getAmountChange() {
        return mAmountChange;
    }

    public void setAmountChange(float mAmountPaidByCashChange) {
        this.mAmountChange = mAmountPaidByCashChange;
    }

    public float getAmountDiscount() {
        return mAmountDiscount;
    }

    public void setAmountDiscount(float mAmountDiscount) {
        this.mAmountDiscount = mAmountDiscount;
    }

    public float getAmountPaidCustom1() {
        return mAmountPaidCustom1;
    }

    public void setAmountPaidCustom1(float mAmountPaidCustom1) {
        this.mAmountPaidCustom1 = mAmountPaidCustom1;
    }

    public float getAmountPaidCustom2() {
        return mAmountPaidCustom2;
    }

    public void setAmountPaidCustom2(float mAmountPaidCustom2) {
        this.mAmountPaidCustom2 = mAmountPaidCustom2;
    }

    public float getAmountSubTotal() {
        return mAmountSubTotal;
    }

    public void setAmountSubTotal(float mAmountSubTotal) {
        this.mAmountSubTotal = mAmountSubTotal;
    }

    public float getAmountTax() {
        return mAmountTax;
    }

    public void setAmountTax(float mAmountTax) {
        this.mAmountTax = mAmountTax;
    }
}

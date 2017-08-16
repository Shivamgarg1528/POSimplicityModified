package com.posimplicity.model.local;

import com.posimplicity.model.response.api.CustomerParent;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {

    private String orderId;
    private String orderStatus;
    private String orderComment;
    private String orderPaymentMode;

    private float orderDisPercentage;
    private float orderDiscountDollar;

    private boolean orderDiscountApplied;

    // Means all order must be saved in database by default...
    private boolean orderSavedInDb = true;

    private boolean orderSavedOnServer;

    private CustomerParent.Customer orderAssignCustomer;
    private CustomerParent.Customer orderAssignClerk;

    public List<CheckoutParent> orderCheckoutItemsList = new ArrayList<>(0);

    public AmountPaidModel orderAmountPaidModel = new AmountPaidModel();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public String getOrderPaymentMode() {
        return orderPaymentMode;
    }

    public void setOrderPaymentMode(String orderPaymentMode) {
        this.orderPaymentMode = orderPaymentMode;
    }

    public float getOrderDisPercentage() {
        return orderDisPercentage;
    }

    public void setOrderDisPercentage(float orderDisPercentage) {
        this.orderDisPercentage = orderDisPercentage;
    }

    public float getOrderDiscountDollar() {
        return orderDiscountDollar;
    }

    public void setOrderDiscountDollar(float orderDiscountDollar) {
        this.orderDiscountDollar = orderDiscountDollar;
    }

    public boolean isOrderDiscountApplied() {
        return orderDiscountApplied;
    }

    public void setOrderDiscountApplied(boolean orderDiscountApplied) {
        this.orderDiscountApplied = orderDiscountApplied;
    }

    public boolean isOrderSavedInDb() {
        return orderSavedInDb;
    }

    public void setOrderSavedInDb(boolean orderSavedInDb) {
        this.orderSavedInDb = orderSavedInDb;
    }

    public boolean isOrderSavedOnServer() {
        return orderSavedOnServer;
    }

    public void setOrderSavedOnServer(boolean orderSavedOnServer) {
        this.orderSavedOnServer = orderSavedOnServer;
    }

    public CustomerParent.Customer getOrderAssignCustomer() {
        return orderAssignCustomer;
    }

    public void setOrderAssignCustomer(CustomerParent.Customer orderAssignCustomer) {
        this.orderAssignCustomer = orderAssignCustomer;
    }

    public CustomerParent.Customer getOrderAssignClerk() {
        return orderAssignClerk;
    }

    public void setOrderAssignClerk(CustomerParent.Customer orderAssignClerk) {
        this.orderAssignClerk = orderAssignClerk;
    }
}
package com.posimplicity.model.local;


import com.easylibs.listener.EventListener;

public class CardInfoModel {

    private String cardHolderName;
    private String cardNumber;
    private String cardExpDate;
    private String cardExpYear;
    private String cardTrack1;
    private String cardTrack2;
    private String cardMagData;
    private String cardSwipeData;
    private String cvv2Number;
    private String ksn;

    private String transactionAmt;
    private String tipAmount;

    private String response;

    private short transType;
    private EventListener eventListener;


    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpDate() {
        return cardExpDate;
    }

    public void setCardExpDate(String cardExpDate) {
        this.cardExpDate = cardExpDate;
    }

    public String getCardExpYear() {
        return cardExpYear;
    }

    public void setCardExpYear(String cardExpYear) {
        this.cardExpYear = cardExpYear;
    }

    public String getCardTrack1() {
        return cardTrack1;
    }

    public void setCardTrack1(String cardTrack1) {
        this.cardTrack1 = cardTrack1;
    }

    public String getCardTrack2() {
        return cardTrack2;
    }

    public void setCardTrack2(String cardTrack2) {
        this.cardTrack2 = cardTrack2;
    }

    public String getCardMagData() {
        return cardMagData;
    }

    public void setCardMagData(String cardMagData) {
        this.cardMagData = cardMagData;
    }

    public String getCardSwipeData() {
        return cardSwipeData;
    }

    public void setCardSwipeData(String cardSwipeData) {
        this.cardSwipeData = cardSwipeData;
    }

    public String getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(String transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public short getTransType() {
        return transType;
    }

    public void setTransType(short transType) {
        this.transType = transType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public String getCvv2Number() {
        return cvv2Number;
    }

    public void setCvv2Number(String cvv2Number) {
        this.cvv2Number = cvv2Number;
    }

    public String getKsn() {
        return ksn;
    }

    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    @Override
    public String toString() {
        return "CardInfoModel{" +
                "cardHolderName='" + cardHolderName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardExpDate='" + cardExpDate + '\'' +
                ", cardExpYear='" + cardExpYear + '\'' +
                ", cardTrack1='" + cardTrack1 + '\'' +
                ", cardTrack2='" + cardTrack2 + '\'' +
                ", cardMagData='" + cardMagData + '\'' +
                ", cardSwipeData='" + cardSwipeData + '\'' +
                ", cvv2Number='" + cvv2Number + '\'' +
                ", ksn='" + ksn + '\'' +
                ", transactionAmt='" + transactionAmt + '\'' +
                ", tipAmount='" + tipAmount + '\'' +
                ", response='" + response + '\'' +
                ", transType=" + transType +
                ", eventListener=" + eventListener +
                '}';
    }
}

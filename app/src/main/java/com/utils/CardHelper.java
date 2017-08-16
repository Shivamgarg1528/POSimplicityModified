package com.utils;

import java.util.Arrays;
import java.util.List;

public class CardHelper {

    private static final String REGEX_VISA = "^4[0-9]{12}(?:[0-9]{3})?$";
    private static final String REGEX_MASTER = "^5[1-5][0-9]{14}$";
    private static final String REGEX_AMEX = "^3[47][0-9]{13}$";
    private static final String REGEX_DINER = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
    private static final String REGEX_DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{12}$";
    private static final String REGEX_JCB = "^(?:2131|1800|35\\d{3})\\d{11}$";

    private String magStripeData;

    private String cardHolderName;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardNumber;
    private String ccTypeFullName;
    private String ccTypeShortName;
    private String trackData1;
    private String trackData2;

    public CardHelper(String magStripeData) {
        this.magStripeData = magStripeData;
    }

    public String getMagStripeData() {
        return magStripeData;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    public String getCardExpiryYear() {
        return cardExpiryYear;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCcTypeFullName() {
        return ccTypeFullName;
    }

    public String getCcTypeShortName() {
        return ccTypeShortName;
    }

    public String getTrackData1() {
        return trackData1;
    }

    public String getTrackData2() {
        return trackData2;
    }

    public boolean parseCardInfo() {
        List<String> cardInfoInList = Arrays.asList(magStripeData.split("\\^"));
        if (cardInfoInList != null && !cardInfoInList.isEmpty() && cardInfoInList.size() > 2) {
            String stringAfterSecondCarrot = cardInfoInList.get(2);
            if (stringAfterSecondCarrot.contains("?;")
                    && stringAfterSecondCarrot.contains("=")
                    && stringAfterSecondCarrot.substring(stringAfterSecondCarrot.indexOf("=")).contains("?")) {

                int indexOfFirstToken = stringAfterSecondCarrot.indexOf("?;");
                int indexOfSecondToken = stringAfterSecondCarrot.indexOf("=");

                cardNumber = stringAfterSecondCarrot.substring(indexOfFirstToken + 2, indexOfSecondToken);
                cardExpiryMonth = stringAfterSecondCarrot.substring(indexOfSecondToken + 3, indexOfSecondToken + 5);
                cardExpiryYear = stringAfterSecondCarrot.substring(indexOfSecondToken + 1, indexOfSecondToken + 3);
                trackData1 = magStripeData.substring(0, magStripeData.indexOf("?;") + 1);
                trackData2 = stringAfterSecondCarrot.substring(indexOfFirstToken + 1);
                cardHolderName = getCardHolderNameFromString(cardInfoInList.get(1));
                ccTypeFullName = getCCType(cardNumber);
                ccTypeShortName = getCCTypeShort(cardNumber);
                return true;
            } else
                return false;
        } else
            return false;
    }

    private String getCardHolderNameFromString(String pNameOnCardString) {
        if (pNameOnCardString.contains("\\/")) {
            List<String> nameArray = Arrays.asList(pNameOnCardString.split("/"));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < nameArray.size(); i++) {
                stringBuilder.append(nameArray.get(i));
                if (i != nameArray.size() - 1) {
                    stringBuilder.append(" ");
                }
            }
            return stringBuilder.toString();
        } else
            return pNameOnCardString;
    }

    private String getCCType(String pCardNumber) {
        pCardNumber = pCardNumber.replaceAll("\\D", "");
        return (pCardNumber.matches(REGEX_VISA) ? "Visa" : (pCardNumber.matches(REGEX_MASTER) ? "MasterCard" : (pCardNumber.matches(REGEX_AMEX) ? "AMEX" : (pCardNumber.matches(REGEX_DINER) ? "DinersClub" : (pCardNumber.matches(REGEX_DISCOVER) ? "Discover" : (pCardNumber.matches(REGEX_JCB) ? "JCB" : ""))))));
    }

    private String getCCTypeShort(String pCardNumber) {
        pCardNumber = pCardNumber.replaceAll("\\D", "");
        return (pCardNumber.matches(REGEX_VISA) ? "VI" : (pCardNumber.matches(REGEX_MASTER) ? "MC" : (pCardNumber.matches(REGEX_AMEX) ? "AE" : (pCardNumber.matches(REGEX_DINER) ? "OT" : (pCardNumber.matches(REGEX_DISCOVER) ? "DI" : (pCardNumber.matches(REGEX_JCB) ? "JCB" : ""))))));
    }
}


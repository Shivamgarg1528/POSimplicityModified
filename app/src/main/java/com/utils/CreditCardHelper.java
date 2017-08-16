package com.utils;

public class CreditCardHelper {

    private static final String REGEX_VISA = "^4[0-9]{12}(?:[0-9]{3})?$";
    private static final String REGEX_MASTER = "^5[1-5][0-9]{14}$";
    private static final String REGEX_AMEX = "^3[47][0-9]{13}$";
    private static final String REGEX_DINER = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
    private static final String REGEX_DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{12}$";
    private static final String REGEX_JCB = "^(?:2131|1800|35\\d{3})\\d{11}$";

    public boolean isCardOk(String pCardNumber) {
        try {
            pCardNumber = pCardNumber.replaceAll("\\D", "");
            char[] ccNumberArray = pCardNumber.toCharArray();
            int checkSum = 0;

            for (int i = ccNumberArray.length - 1; i >= 0; i--) {
                char ccDigit = ccNumberArray[i];
                if ((ccNumberArray.length - i) % 2 == 0) {
                    int doubleDigit = Character.getNumericValue(ccDigit) * 2;
                    checkSum += (doubleDigit % 9 == 0 && doubleDigit != 0) ? 9 : doubleDigit % 9;
                } else {
                    checkSum += Character.getNumericValue(ccDigit);
                }
            }
            return (checkSum != 0 && checkSum % 10 == 0);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    public String getCCType(String pCardNumber) {
        pCardNumber = pCardNumber.replaceAll("\\D", "");
        return (pCardNumber.matches(REGEX_VISA) ? "Visa" : (pCardNumber.matches(REGEX_MASTER) ? "MasterCard" : (pCardNumber.matches(REGEX_AMEX) ? "AMEX" : (pCardNumber.matches(REGEX_DINER) ? "DinersClub" : (pCardNumber.matches(REGEX_DISCOVER) ? "Discover" : (pCardNumber.matches(REGEX_JCB) ? "JCB" : ""))))));
    }

    public String getCCTypeShort(String pCardNumber) {
        pCardNumber = pCardNumber.replaceAll("\\D", "");
        return (pCardNumber.matches(REGEX_VISA) ? "VI" : (pCardNumber.matches(REGEX_MASTER) ? "MC" : (pCardNumber.matches(REGEX_AMEX) ? "AE" : (pCardNumber.matches(REGEX_DINER) ? "OT" : (pCardNumber.matches(REGEX_DISCOVER) ? "DI" : (pCardNumber.matches(REGEX_JCB) ? "JCB" : ""))))));
    }
}

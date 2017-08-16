package com.utils;

import java.util.Arrays;
import java.util.List;

import android.content.Context;

public class CheckCardInfo {

    private String cvv2Number;
    private String cardHolderName;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardNumber;
    private String allCardData;
    private String ccTypeFullForm;
    private String magData;
    private String ccTypeShotForm;
    private boolean isCardValid;
    private String trackData1;
    private String trackData2;
    public Context context;

    public CheckCardInfo(Context mContext, String allCardData) {
        super();
        this.context = mContext;
        this.allCardData = allCardData;
    }

    public CheckCardInfo() {
    }


    public String getCvv2Number() {
        return cvv2Number;
    }

    public void setCvv2Number(String cvv2Number) {
        this.cvv2Number = cvv2Number;
    }

    public String getTrackData1() {
        return trackData1;
    }

    public void setTrackData1(String trackData1) {
        this.trackData1 = trackData1;
    }

    public String getTrackData2() {
        return trackData2;
    }

    public void setTrackData2(String trackData2) {
        this.trackData2 = trackData2;
    }

    /**
     * @return the cardHolderName
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * @param cardHolderName the cardHolderName to set
     */
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    /**
     * @return the cardExpiryMonth
     */
    public String getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    /**
     * @param cardExpiryMonth the cardExpiryMonth to set
     */
    public void setCardExpiryMonth(String cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
    }


    /**
     * @return the cardExpiryYear
     */
    public String getCardExpiryYear() {
        return cardExpiryYear;
    }


    /**
     * @param cardExpiryYear the cardExpiryYear to set
     */
    public void setCardExpiryYear(String cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
    }


    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }


    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    /**
     * @return the allCardData
     */
    public String getAllCardData() {
        return allCardData;
    }


    /**
     * @param allCardData the allCardData to set
     */
    public void setAllCardData(String allCardData) {
        this.allCardData = allCardData;
    }


    /**
     * @return the ccTypeFullForm
     */
    public String getCcTypeFullForm() {
        return ccTypeFullForm;
    }


    /**
     * @param ccTypeFullForm the ccTypeFullForm to set
     */
    public void setCcTypeFullForm(String ccTypeFullForm) {
        this.ccTypeFullForm = ccTypeFullForm;
    }


    /**
     * @return the magData
     */
    public String getMagData() {
        return magData;
    }


    /**
     * @param magData the magData to set
     */
    public void setMagData(String magData) {
        this.magData = magData;
    }


    /**
     * @return the ccTypeShotForm
     */
    public String getCcTypeShotForm() {
        return ccTypeShotForm;
    }


    /**
     * @param ccTypeShotForm the ccTypeShotForm to set
     */
    public void setCcTypeShotForm(String ccTypeShotForm) {
        this.ccTypeShotForm = ccTypeShotForm;
    }


    /**
     * @param isCardValid the isCardValid to set
     */
    public void setCardValid(boolean isCardValid) {
        this.isCardValid = isCardValid;
    }


    public boolean validationOfCard() {


        if (allCardData == null || allCardData.isEmpty()) {   // Check whether  the card info is empty and variable must not equal to null
            isCardValid = false;
            return isCardValid;
        } else {
            isCardValid = isCardInfoContainsTwoCarrotSymbol();  // Check string contains two carrot symbol ("^")
            if (isCardValid) {
                List<String> cardInfoInList = Arrays.asList(allCardData.split("\\^"));
                extractNumberMonthYear(cardInfoInList);
                return isCardValid;
            } else
                return isCardValid;
        }
    }


    @Override
    public String toString() {
        return "CheckCardInfo [cardHolderName=" + cardHolderName
                + ", cardExpiryMonth=" + cardExpiryMonth + ", cardExpiryYear="
                + cardExpiryYear + ", cardNumber=" + cardNumber
                + ", allCardData=" + allCardData + ", ccTypeFullForm="
                + ccTypeFullForm + ", magData=" + magData + ", ccTypeShotForm="
                + ccTypeShotForm + ", isCardValid=" + isCardValid
                + ", trackData1=" + trackData1 + ", trackData2=" + trackData2
                + "]";
    }

    private void extractNameFromString(String nameOnCard) {

        if (nameOnCard.contains("\\/")) {
            List<String> NameSplit = Arrays.asList(nameOnCard.split("\\/"));
            StringBuilder commaSepValueBuilder = new StringBuilder();
            for (int i = 0; i < NameSplit.size(); i++) {
                commaSepValueBuilder.append(NameSplit.get(i));
                if (i != NameSplit.size() - 1) {
                    commaSepValueBuilder.append(" ");
                }
            }
            cardHolderName = commaSepValueBuilder.toString();
        } else
            cardHolderName = nameOnCard;
    }


    private void extractNumberMonthYear(List<String> cardInfoInList) {

        String stringAfterSecondCarrot = cardInfoInList.get(2);

        if ((stringAfterSecondCarrot.contains("?") && stringAfterSecondCarrot.contains(";"))
                && stringAfterSecondCarrot.contains("=")
                && stringAfterSecondCarrot.substring(stringAfterSecondCarrot.indexOf("=")).contains("?")) {

            int indexOfFrist = stringAfterSecondCarrot.indexOf("?;");
            int indexOfSecond = stringAfterSecondCarrot.indexOf("=");
            String orignalCardNumber = stringAfterSecondCarrot.substring(indexOfFrist + 2, indexOfSecond);
            String orignalExpMon = stringAfterSecondCarrot.substring(indexOfSecond + 3, indexOfSecond + 5);
            String orignalExpYear = stringAfterSecondCarrot.substring(indexOfSecond + 1, indexOfSecond + 3);
            String orignalMagData = stringAfterSecondCarrot.substring(indexOfFrist + 2);
            String orginalTrack2 = stringAfterSecondCarrot.substring(indexOfFrist + 1);

            if (orignalCardNumber.length() > 10 && orignalExpMon.length() == 2 && orignalExpYear.length() == 2) {
                cardNumber = orignalCardNumber;
                cardExpiryMonth = orignalExpMon;
                cardExpiryYear = orignalExpYear;
                magData = orignalMagData;
                trackData2 = orginalTrack2;
                ccTypeShotForm = getCCTypeShotLeter(cardNumber);
                ccTypeFullForm = getCCType(cardNumber);
                extractNameFromString(cardInfoInList.get(1));
                isCardValid = true;
                //isCardNumberIsValid(cardNumber);
            } else {
                isCardValid = false;
            }
        } else {
            isCardValid = false;
        }

    }

    public boolean isCardInfoContainsTwoCarrotSymbol() {

        String localValue = new String(allCardData);
        localValue = localValue.replace("^", "");
        if ((localValue.length() + 2) == allCardData.length()) {
            return true;
        }
        return false;
    }


    public String getCCType(String ccNumber) {

        String visaRegex = "^4[0-9]{12}(?:[0-9]{3})?$";
        String masterRegex = "^5[1-5][0-9]{14}$";
        String amexRegex = "^3[47][0-9]{13}$";
        String dinersClubrRegex = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        String discoverRegex = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String jcbRegex = "^(?:2131|1800|35\\d{3})\\d{11}$";
        //String commonRegex      = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";

        try {
            ccNumber = ccNumber.replaceAll("\\D", "");
            return (ccNumber.matches(visaRegex) ? "Visa" : ccNumber.matches(masterRegex) ? "MasterCard" : ccNumber.matches(amexRegex) ? "AMEX" : ccNumber.matches(dinersClubrRegex) ? "DinersClub" : ccNumber.matches(discoverRegex) ? "Discover" : ccNumber.matches(jcbRegex) ? "JCB" : "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Visa";
    }

    public String getCCTypeShotLeter(String ccNumber) {

        String visaRegex = "^4[0-9]{12}(?:[0-9]{3})?$";
        String masterRegex = "^5[1-5][0-9]{14}$";
        String amexRegex = "^3[47][0-9]{13}$";
        String dinersClubrRegex = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        String discoverRegex = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String jcbRegex = "^(?:2131|1800|35\\d{3})\\d{11}$";
        //String commonRegex      = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";

        try {
            ccNumber = ccNumber.replaceAll("\\D", "");
            return (ccNumber.matches(visaRegex) ? "VI" : ccNumber.matches(masterRegex) ? "MC" : ccNumber.matches(amexRegex) ? "AE" : ccNumber.matches(dinersClubrRegex) ? "OT" : ccNumber.matches(discoverRegex) ? "DI" : ccNumber.matches(jcbRegex) ? "JCB" : "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "VI";
    }

    public void isCardNumberIsValid(String ccNumber) {

        try {
            ccNumber = ccNumber.replaceAll("\\D", "");
            char[] ccNumberArry = ccNumber.toCharArray();

            int checkSum = 0;
            for (int i = ccNumberArry.length - 1; i >= 0; i--) {

                char ccDigit = ccNumberArry[i];

                if ((ccNumberArry.length - i) % 2 == 0) {
                    int doubleddDigit = Character.getNumericValue(ccDigit) * 2;
                    checkSum += (doubleddDigit % 9 == 0 && doubleddDigit != 0) ? 9 : doubleddDigit % 9;

                } else {
                    checkSum += Character.getNumericValue(ccDigit);
                }

            }
            isCardValid = (checkSum != 0 && checkSum % 10 == 0);

        } catch (Exception e) {
            isCardValid = false;
            e.printStackTrace();
        }
    }
}

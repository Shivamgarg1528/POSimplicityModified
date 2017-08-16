package com.utils;

public class InputCalculation {

    public static String onKeyDown(StringBuilder stringKey, String pressedKey, boolean specailCase) {   // add Character

        if (!specailCase) {
            if (stringKey.toString().contains(".")) {
                stringKey.deleteCharAt(stringKey.indexOf("."));
            }

            int enterValueLength = stringKey.length();
            if (enterValueLength <= 3 && String.valueOf(stringKey.charAt(0)).equalsIgnoreCase("0")) {
                stringKey.deleteCharAt(0);
            }
            stringKey.append(pressedKey);
        } else {
            stringKey.delete(0, stringKey.length());
            stringKey.append(pressedKey + "00");
        }
        stringKey.insert(stringKey.length() - 2, '.');
        return madeRightFormat(stringKey);
    }

    public static String onKeyDownWithOutDot(StringBuilder stringKey, String pressedKey) {   // add Character

        int enterValueLength = stringKey.length();
        if (enterValueLength <= 3 && String.valueOf(stringKey.charAt(0)).equalsIgnoreCase("0")) {

            if (Integer.parseInt(stringKey.toString()) < 10 && pressedKey.equalsIgnoreCase("00")) {
                stringKey.deleteCharAt(0);
                stringKey.deleteCharAt(0);
                stringKey.append(pressedKey);
                return stringKey.toString();
            } else if (Integer.parseInt(stringKey.toString()) >= 10 && pressedKey.equalsIgnoreCase("00")) {
                return stringKey.toString();
            } else {
                stringKey.deleteCharAt(0);
                stringKey.append(pressedKey);
                return stringKey.toString();
            }
        } else if (enterValueLength <= 3 && !String.valueOf(stringKey.charAt(0)).equalsIgnoreCase("0")) {
            return stringKey.toString();
        } else
            return stringKey.toString();
    }


    public static String onKeyUp(StringBuilder finalString) {  // remove Character

        if (finalString.toString().contains(".")) {
            finalString.deleteCharAt(finalString.indexOf("."));
        }

        int enterValueLength = finalString.length();
        if (enterValueLength <= 3 && String.valueOf(finalString.toString()).equalsIgnoreCase("000")) {
            finalString.insert(finalString.length() - 2, '.');
            return madeRightFormat(finalString);
        } else if (enterValueLength <= 3 && !String.valueOf(finalString.toString()).equalsIgnoreCase("000")) {
            finalString.deleteCharAt(finalString.length() - 1);
            finalString.insert(0, '0');
            finalString.insert(finalString.length() - 2, '.');
            return madeRightFormat(finalString);
        } else {
            finalString.deleteCharAt(finalString.length() - 1);
            finalString.insert(finalString.length() - 2, '.');
            return madeRightFormat(finalString);
        }
    }

    public static String onKeyUpWithOutDot(StringBuilder finalString) {  // remove Character

        int enterValueLength = finalString.length();
        if (enterValueLength <= 3 && String.valueOf(finalString.toString()).equalsIgnoreCase("000")) {
            return finalString.toString();
        } else if (enterValueLength <= 3 && !String.valueOf(finalString.toString()).equalsIgnoreCase("000")) {
            finalString.deleteCharAt(finalString.length() - 1);
            finalString.insert(0, '0');
            return finalString.toString();
        } else {
            finalString.deleteCharAt(finalString.length() - 1);
            return finalString.toString();
        }
    }

    public static String madeRightFormat(StringBuilder stringKey) {
        Float parseInput = Float.parseFloat(stringKey.toString());
        return MyStringFormat.formatWith2DecimalPlaces(parseInput);
    }
}

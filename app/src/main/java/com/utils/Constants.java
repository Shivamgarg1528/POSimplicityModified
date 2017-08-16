package com.utils;


public class Constants {

    // Asset file name
    public static final String ROLE_FILE = "com.posimplicity.biz.role.json";
    public static final String SETTING_FILE = "com.posimplicity.biz.setting.json";

    public static final int API_CATEGORY = 1;
    public static final int API_PRODUCT = 2;
    public static final int API_PRODUCT_OPTIONS = 3;
    public static final int API_CUSTOMER = 4;
    public static final int API_CUSTOMER_GROUP = 5;
    public static final int API_VALIDATE_STORE = 6;
    public static final int API_VALIDATE_ADMIN = 7;
    public static final int API_CREATE_ORDER = 8;
    public static final int API_ORDERS = 9;

    // Role ID's
    public static final String ROLE_ID_ADMIN = "0";

    // Icon Type
    public final static short ICON_NONE = 0;
    public final static short ICON_APP = 1;
    public final static short ICON_DRAWER = 2;
    public final static short ICON_BACK = 3;

    // Active state of any data from server
    public static final String ACTIVE = "1";

    // POSimplcity exclude all categories that start with '@#@' string.
    public static final String NON_POS_CATEGORY = "@#@";

    // Action Name when some amount has been paid by customer for updating due amount...
    public static final String ACTION_AMOUNT_PAID = "com.posimplicity.action.amount.paid";
    public static final String ACTION_CLEAR = "com.posimplicity.action.clear.all";

    // Printer Actions Wifi and Bluetooth
    public static final String ACTION_WIFI_CONNECTED = "com.posimplicity.action.wifi.connected";
    public static final String ACTION_WIFI_DISCONNECTED = "com.posimplicity.action.wifi.disconnected";
    public static final String ACTION_BLUETOOTH_CONNECTED = "com.posimplicity.action.bluetooth.connected";
    public static final String ACTION_BLUETOOTH_DISCONNECTED = "com.posimplicity.action.bluetooth.disconnected";

    // Payment Mode Name
    public static final String PAYMENT_MODE_CHECK = "checkmo";
    public static final String PAYMENT_MODE_CASH = "cashondelivery";
    public static final String PAYMENT_MODE_CREDIT = "ccsave";

    // Order Status Name
    public static final String ORDER_STATUS_COMPLETE = "complete";

    // Product Image Shown
    public static final int IMAGE_SHOWN = 1;
    public static final int SUCCESS = 1;
    // order message
    static final String MESSAGE_ORDER_PLACED_SUCCESSFULLY = "Order Placed Successfully";

    /*---Start---*/
    /* Default Values For Report Table */

    public static final String DAILY_REPORT = "DailyReport";
    public static final String SHIFT_REPORT = "ShiftReport";
    public static final String REFUND_NO = "No";
    public static final String REFUND_YES = "Yes";
    public static final String ORDER_FAIL = "Orders Not Recorded In Backend";
    public static final String ORDER_SUCCESS = "Success";
    public static final String MANUAL_ENTRY = "Manually Recorded Orders";
    public final static String DEFAULT_DESCRIPTION = "Comment Not Available";
    public static final String DEFAULT_PAYOUT_NAME = "Payout Name Not Available";

    public static final String TIP_AMOUNT = "Tip Amount";
    /*---End---*/

    // Extra Bundle Key...
    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_KEY_URL = "extraKeyUrl";
    public static final String EXTRA_KEY_INTERFACE = "extraKeyInterface";

    // Request Codes
    public static final int REQUEST_CODE_OPEN_BLUETOOTH = 101;
    public static final int REQUEST_CODE_SELECTED_BLUETOOTH_DEVICE = 102;

    public static class HttpMethod {
        public static final String HTTP_POST = "POST";
        public static final String HTTP_DELETE = "DELETE";
        public static final String HTTP_PUT = "PUT";
    }
}

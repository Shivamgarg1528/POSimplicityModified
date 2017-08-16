package com.PosInterfaces;

/**
 *   @author Shivam Garg
 *    This interface is used to Supply all the Prefernce Key To All The Activity For Reteriving the Prefernces Value from the Pref File.
 */

public interface PrefrenceKeyConst {

	/**
	 *    APPLICATION PREFERNCES KEY
	 * 
	 */

	public static final String STORE_NAME = "BaseUrl";
	public static final String SETUP_TIME              = "SetupTime";
	public static final String MAGENTO_URL             = "magentoUrl";
	public static final String REGISTRATION_URL        = "registrationUrl";
	public static final String ANDROID_DEVICE_ID       = "deviceID";


	/**
	 *    MERCHANT  PREFERNCES KEY
	 * 
	 */


	public static final String IS_MERCHANT_LOGIN       = "alreadyLogin";
	public static final String MERCHANT_NAME           = "merchantName";
	public static final String MERCHANT_PASSWORD       = "merchantPassword";

	public static final String REWARDS                 = "selectedRewards";		
	public static final String STORE                   = "storeName";

	//public static final String ENABLE_SCOKET           = "enableSocket";
	public static final String CLERK_TIME_ON_OFF_URL    = "clerkTimeOnOffUrl";

	public static final String NOBLE_ON_OFF             = "nobleOnOff";
	public static final String IS_CUSTOM_OPTION_PRINT_ON_OFF   = "customPrintOnOff";
	
	public static final String CLERK_TIME_ON_OFF        = "clerkTimeOnOff";
	public static final String CLERK_ORDER_ASSIGN      = "clerkOrderAssign";
	public static final String CLERK_REPORTING         = "clerkReporting";
	public static final String ORDERING_ENABLE         = "enableOrdering";
	public static final String DEVICE_ID               = "deviceID";
	public static final String OPTION_ITEM_EXPAND      = "close";
	public static final String TRANSACTION_ID          = "transId";
	public static final String TRANSACTION_ID_DEJAVOO  = "transIdDejavoo";
	public static final String WIFI_PRINT              = "wifiPrint";
	public static final String ENCRYPTED_PAY_ENABLE    = "encryptionEnable";
	public static final String QR_CODE_PRINTING        = "qrCodePrinting";
	public static final String BAR_CODE_PRINTING        = "barCodePrinting";

	public static final String FULL_PATH = "http://";	
	public static final String STATIC_TENDER_URL        = "functions/rewards/tendercard_rewards/";
	public static final String STATIC_POS_URL           = "functions/rewards/posimplicity_rewards/";
	public static final String PAYMENT_METHOD_ID_PROPAY = "paymentMethodId";


	public static final String  IS_SOCKET_NEEDED    = "isSocketNeeded";
	public static final String SOCKET_URL           = "http://www.posimplicity.com:8001";
	//public static final String SOCKET_URL         = "http://192.168.0.2:3000";	
	public static final String FIRST_TRANSACTION    = "firstTransaaction";
	public static final String DISABLEPRINT         = "disablePrint";

	public static final String SUB_URL              = ".posimplicity.biz/";
	//public static final String SUB_URL              = "/namita/magento/";

	public static final String IS_USB_ON_PS                 = "isUsbEnable";
	public static final String IS_DUPLICATE_RECIEPT_ON_PS   = "duplicateReceipts";
	public static final String IS_WIFI_ON_PS                = "wifiConnectionRequiredss";
	public static final String IS_TIP_ON_PS                 = "tipGiven";
	public static final String IS_BT_ON_PS                  = "btOnEnable";
	public static final String IS_PRINTER_SOUND_ON          = "printerSoundOnOff";
	public static final String IS_BT_CU_ON_PS               = "btOnEnableCustomer";
	public static final String IS_BT_KI_ON_PS               = "btOnEnableKitchen";
	public static final String IS_USB_CU_ON_PS               = "usbOnEnableCustomer";
	public static final String IS_USB_KI_ON_PS               = "usbOnEnableKitchen";
	public static final String IS_MARGIN_APPLIED            = "isMarginSet";
	public static final String WIFI_IP_ADDRESS              = "wifiIpAddress";
	
	public static final String TEXT1 = "text1";
	public static final String TEXT2 = "text2";
	public static final String TEXT3 = "text3";
	public static final String TEXT4 = "text4";

	public static final String SPACE1 = "space1";
	public static final String SPACE2 = "space2";
	public static final String SPACE3 = "space3";
	public static final String SPACE4 = "space4";
	public static final String DEVICE_CODE                  = "uniqueDeviceCode";
	public static final String MOST_RECENTLY_TRANSACTION_ID = "mostRecentlyTranId";



	/**
	 *    PAYMENT GATEWAY PREFERNCES KEY
	 * 
	 */

	public static final String GATEWAY_USED_POSITION        = "gatewayUsedPosition";

	/**
	 *    PLUG_N_PAY GATEWAY PREFERENCES KEY
	 */

	public static final String PLUG_PAY_ID             = "publisherName1";


	/**
	 *    NOBLE GATEWAY PREFERENCES KEY
	 */


	public static final String NOBLE_API_KEY_NAME        = "nobleApiKeyName";
	public static final String NOBLE_API_KEY             = "nobleApiKey";	
	public static final String NOBLE_NAME                = "nobleName";

	/**
	 *    BRIDGE_PAYMENT GATEWAY PREFERENCES KEY
	 */

	public static final String BRIDGE_GATEWAY_USERNAME = "userName1";
	public static final String BRIDGE_GATEWAY_PASSWORD = "password1";


	/**
	 *    PROPAY GATEWAY PREFERENCES KEY
	 */

	public static final String GENERATE_KEY         = "keyGenerated";
	public static final String PAYER_ID             = "payerId";


	/**
	 *    DEJAVOO GATEWAY PREFERENCES KEY
	 */


	public static final String DEJAVOO_IP_ADDRESS           = "dejavooIpAddress";
	public static final String DEJAVOO_AUTH_KEY             = "dejavoAuthKey";
	public static final String DEJAVO_OPTION                = "devjavooOption";
	public static final String ACNTLAST4                    = "AcntLast4";
	public static final String INV_NUM                      = "invNum";
	public static final String REMOTE_DEVICE_ADDRESS        = "remoteDeviceIp";
	public static final String IS_REMOTE_DEVICE_SECURE      = "isRemoteDeviceSecure";
	public static final String DEJAVOO_RESPONSE             = "dejavooResponse";


	/**
	 *    TSYS GATWAY PREFERENCES KEY
	 */

	public static final String TSYS_USER_NAME               = "tsysUserName";
	public static final String TSYS_PASSWORD                = "tsysPassword";
	public static final String TSYS_DEVICE_ID               = "tsysDeviceId";
	public static final String TSYS_MERCHA_ID               = "tsysMerchantId";
	public static final String TSYS_TRANSACTION_KEY         = "tsysGeneratedKey";
	public static final String TSYS_ORDER_TRANSACTION_ID    = "tsysTransactionid";


	/**
	 *    STORE TYPE ACTIVE PREFERENCES KEY
	 */

	public static final String POS_STORE_TYPE               = "posStoreType";



	/**
	 *    SECURITY SETTINGS ACTIVE PREFERENCES KEY
	 */
	public static final String SECURITY_LOGIN_USER_Id       = "securityLoginUserId";


	/**
	 *    DRAWER KEY
	 */

	public static final String DRAWER_CASH    = "drawerCash";
	public static final String DRAWER_CHECK   = "drawerCheck";
	public static final String DRAWER_CC      = "drawerCC";


	/**
	 *    RECEIPT KEY
	 */


	public static final String RECEIPT_CASH    = "receiptCash";
	public static final String RECEIPT_CHECK   = "receiptCheck";
	public static final String RECEIPT_CC      = "receiptCC";



	public static final String IS_RECEIPT_PROMPT_ON_PS = "isReceiptPrompt";

	public static final String SOCKET_KEY        = "socketKey";
	public static final String SOCKET_EMIT       = "socketEmitValue";
	
	public static final String TSYS_ENVIRNOMENT  = "tsysEnvirnoment"; 
	
	
	public static final String STAFF_ID_IN_LD    = "staffIdInLocalDatabase";
	public static final String DOMAIN_NAME       = "domainName";
	public static final String DOMAIN_ADDRESS    = "domanAddress";
	public static final String DEJAVO_PAYMENT_VIA_BLUETOOTH = "devjavooPaymentViaBluetooth";
	public static final String DEJAVO_PRMOPT_D_C = "devjavooPromptDebitCredit";
	public static final String DEJAVO_PAYMENT_TYPE = "devjavooPaymentType";
	
	public static final String CUSTOM_1            = "custom1";
	public static final String CUSTOM_2            = "custom2";
	
	// Tender Keys
	

	public static final String CASH_TENDER         = "cashTenderEnable";
	public static final String CHECK_TENDER        = "checkTenderEnable";
	public static final String CREDIT_TENDER       = "creditTenderEnable";
	public static final String TENDER_TENDER       = "tenderTenderEnable";
	public static final String UNRECORED_TENDER    = "unrecordedTenderEnable";
	public static final String CUSTOM_1_TENDER     = "custom1TenderEnable";
	public static final String CUSTOM_2_TENDER     = "custom2TenderEnable";
	public static final String BLUETOOTH_DEVICE_ADDRESS_PRINTING = "bluetoothDeviceAddressPrinting";
	public static final String HOURS_OF_DAY        = "hoursOfDay";
	public static final String MINUTES             = "minutes";

	/* New Constants*/
	String IS_APP_SETUP = "isAppSetup";
}

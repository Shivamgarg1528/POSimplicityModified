package com.utils;

import com.Beans.CustomerModel;

public class Variables {

	public static int uniqueTransId = 1;
	public static long transId          = 1;
	public static long transIdDejavoo   = 1;

	public static float discountInPercentage = 0.0f;
	public static float discountInDollar     = 0.0f;
	public static float totalBillAmount      = 0.0f;
	public static float totalDiscount        = 0.0f;
	public static float orderLevelDiscount = 0.0f;
	public static float itemsAmount          = 0.0f;
	public static float taxAmount            = 0.0f;	
	public static float changeAmt            = 0.0f;
	public static float rewardsAmount        = 0.0f;
	public static float rewardPointsValue    = 0.0f;
	public static float giftAmount           = 0.0f;
	public static float cashAmount           = 0.0f;	
	public static float checkAmount          = 0.0f;

	public static float ccAmount             = 0.0f;
	public static float custom2Amount        = 0.0f;
	public static float custom1Amount        = 0.0f;

	public static float dueCCAmount          = 0.0f;		
	public static float subChargeAmount      = 0.0f;
	public static float halfAmount           = 0.0f;

	public static float cashAfterChange      = 0.0f;
	public static float giftAmtAfterChange   = 0.0f;
	public static float rewardsAfterChange   = 0.0f;

	public static float fees                 = 0.0f;
	public static float dejavoPaymentAmt     = 0.0f;

	public static boolean startNewTrans      = true;	
	public static boolean discountPercentage = false;	
	public static boolean discountApplied    = false;
	public static boolean discountDollar     = false;
	public static boolean paymentByCC        = false;
	public static boolean isRetailWithTipAuthDone      = true;
	public static boolean isRestaurantWithTipSaleDone  = true;
	public static boolean isDejavooSuccess = false;
	public static boolean isPendingOrderItems = false;

	public static boolean isReprintActive     = false;
	public static boolean isReturnActive      = false; 
	public static boolean refundByCC          = false; 


	public static String ccNumber            = "";
	public static String CCHOlderName        = "";
	public static String CcExpiryDate        = "";
	public static String CcExpiryYear        = "";
	public static String Cardtype            = "";
	public static String orderStatus         = "pending";

	public static String gateWayTrasId       = "";
	public static String orderComment        = "";

	public static int customerId             = 0;
	public static String tableID             = "";
	public static String billToName          = "";
	public static String shipToName          = "";
	public static String customerName        = "";


	public static  CustomerModel customerOrClerkBillToNameModel;
	public static  CustomerModel tableOrClerkShipToNameModel;

	public static boolean forceCloseBluetooth;
}

package com.RecieptPrints;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.Beans.CheckOutParentModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.Fragments.DejavooFragment;
import com.Fragments.MaintFragmentCCAdmin;
import com.Fragments.MaintFragmentTender1;
import com.posimplicity.gateway.DejavooParseService;
import com.PosInterfaces.PrefrenceKeyConst;
import com.SetupPrinter.BasePR;
import com.posimplicity.HomeActivity;
import com.utils.CurrentDate;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.Variables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author shiva
 *
 */
/**
 * @author shiva
 *
 */
public class PrintReceiptCustomer implements PrefrenceKeyConst {

	private Context mContext;
	private HomeActivity instance;
	private final String DEFAULT_ADDRESS = "Enter Address ";
	private final String TOTAL_TEXT      = "Total:";
	private final String DISCOUNT_TEXT   = "Discount:";
	private final String TAX_TEXT        = "Tax:";
	private final String NET_AMOUNT_TEXT = "Net Amount:";
	private final String CHANGE_TEXT     = "Change:";
	private final String CASH_TEXT       = "Cash:";
	private final String CREDIT_TEXT     = "CreditCard:";
	private final String CC_TOTAL        = "CC Total:";
	private final String CREDIT_TEXT_SUR = "CC Surcharge:";
	private final String COMMENT         = "Comment:";
	private final String CHECK_TEXT      = "Check:";
	private final String REWARD_TEXT     = "Reward Amount:";
	private final String GIFT_TEXT       = "Gift Card:";


	public PrintReceiptCustomer(Context mContext)
	{
		this.mContext        = mContext;
		this.instance        = HomeActivity.localInstance;
	}

	/**
	 *  print receipt for customer 
	 * @param printCustomOptionAlso print custom option name if true, else don't print.
	 */

	public void onPrintRecieptCustomer(BasePR basePR , boolean printCustomOptionAlso)
	{		
		String text1 = MyPreferences.getMyPreference(TEXT1, mContext);
		String text2 = MyPreferences.getMyPreference(TEXT2, mContext);
		String text3 = MyPreferences.getMyPreference(TEXT3, mContext);
		String text4 = MyPreferences.getMyPreference(TEXT4, mContext);

		String formattedString ;
		basePR.playBuzzer();
		basePR.largeText();

		/**
		 *        Print Return As Text on Top Of Receipt if Return Run...
		 */

		if(Variables.isReturnActive){     
			basePR.underLine(true);
			basePR.printData(PrintSettings.onFormatHeaderAndFooter("Return"));
			basePR.printData(PrintSettings.NEW_LINE_CHAR);
			basePR.underLine(false);
		}

		if(Variables.isReprintActive){     
			basePR.underLine(true);
			basePR.printData(PrintSettings.onFormatHeaderAndFooter("RePrint"));
			basePR.printData(PrintSettings.NEW_LINE_CHAR);
			basePR.underLine(false);
		}

		basePR.printData(text1.isEmpty()?DEFAULT_ADDRESS:text1);
		basePR.printData(text2.isEmpty()?DEFAULT_ADDRESS:text2);
		basePR.printData(text3.isEmpty()?DEFAULT_ADDRESS:text3);
		basePR.printData(text4.isEmpty()?DEFAULT_ADDRESS+"\n":text4);


		basePR.smallText();

		formattedString = CurrentDate.returnCurrentDateWithTime();
		basePR.printData(formattedString);

		String transId  = MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext);
		basePR.printData("TransID  == "+transId);

		if(!Variables.tableID.isEmpty())
			basePR.printData("TableID  == "+Variables.tableID+ "\n");


		if(MyPreferences.getBooleanPrefrences(QR_CODE_PRINTING, mContext)){
			basePR.print2DQrCode(transId);
		}		

		if(MyPreferences.getBooleanPrefrences(BAR_CODE_PRINTING, mContext)){
			basePR.print1DBarCode(transId);
		}


		boolean needToPrintBarcodeForTsys = false;
		int storedOption                  =  (int) MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION, mContext);
		int selectedOption                =  (int) MyPreferences.getLongPreference(DEJAVO_OPTION, mContext);

		if(Variables.paymentByCC){
			switch (storedOption) {
			case MaintFragmentCCAdmin.DEJAVO_PAY_ID:

				switch (selectedOption) {

				case DejavooFragment.RETAIL_TIP:
				case DejavooFragment.RESTAURANT_TIP:
				case DejavooFragment.RETAIL:
				case DejavooFragment.RESTAURANT:

					try{

						DejavooParseService dejavooParseService = new DejavooParseService(MyPreferences.getMyPreference(PrefrenceKeyConst.DEJAVOO_RESPONSE, mContext));
						if(dejavooParseService.parseData(mContext)){
							transId          = dejavooParseService.dejavooResponse.getXmp().getResponse().getInvNum();
							basePR.printData("Inv-Num  == "+transId+"");

							formattedString  = dejavooParseService.dejavooResponse.getXmp().getResponse().getAuthCode();
							basePR.printData("AuthCode == "+formattedString);

							formattedString  = dejavooParseService.dejavooResponse.getXmp().getResponse().getPaymentType();
							basePR.printData("PaymentType == "+formattedString);

							Set<String> keyss = dejavooParseService.allParsedData.keySet();
							for (String string : keyss) {
								formattedString = "";
								formattedString = dejavooParseService.getKeyValue(string);
								List<String> keyssList = Arrays.asList(dejavooParseService.array);
								if(!TextUtils.isEmpty(formattedString) && keyssList.contains(string))
									basePR.printData(string+" == "+formattedString);
							}
							basePR.printData("\n");
						}
					}
					catch(Exception ex){
						ex.printStackTrace();
					}					
					break;
				}				
				break;

			case MaintFragmentCCAdmin.TSYS_PAY_ID:				
				formattedString  = Variables.gateWayTrasId;
				basePR.printData("TSYS TransId   == "+formattedString+"\n");
				needToPrintBarcodeForTsys = true;						
				break;

			default:
				basePR.printData("\n");
				break;
			}
		}

		for (int index = 0; index < instance.dataList.size(); index ++) {

			CheckOutParentModel   parent                    = instance.dataList.get(index);		
			ProductModel  product                      		= parent.getProduct();
			List<RelationalOptionModel> listOfchilds 	    = parent.getChilds();

			String  itemQty    = product.getProductQty();
			String productName = product.getProductName();
			float newPrice     = Float.parseFloat(product.getProductCalAmount());
			formattedString    = callFormatMethod(productName,itemQty,newPrice);

			basePR.printData(formattedString);

			if (!listOfchilds.isEmpty() && printCustomOptionAlso) {
				for(int count = 0; count < listOfchilds.size(); count ++){
					List<SubOptionModel> subOptionModels = listOfchilds.get(count).getListOfSubOptionModel();
					int sizeOfEachList    = subOptionModels.size();

					for(int index1 = 0 ; index1 < sizeOfEachList ;index1++){
						SubOptionModel subOptionModel = subOptionModels.get(index1);
						productName   = subOptionModel.getSubOptionName();
						basePR.printData(PrintSettings.onReformatName(productName));
					}
				}
			}			
		}

		basePR.printData("\n");

		formattedString = callFormatMethod(TOTAL_TEXT, "", Variables.itemsAmount);
		basePR.printData(formattedString);

		formattedString = callFormatMethod(DISCOUNT_TEXT, "", Variables.totalDiscount);
		basePR.printData(formattedString);

		formattedString = callFormatMethod(TAX_TEXT, "", Variables.taxAmount);
		basePR.printData(formattedString);

		// Changes Start
		formattedString = callFormatMethod(NET_AMOUNT_TEXT, "", Variables.totalBillAmount);
		basePR.printData(formattedString);
		// Changes End
		if(PrintSettings.isValueGreaterThanZero(Variables.changeAmt)){
			formattedString = callFormatMethod(CHANGE_TEXT, "", Variables.changeAmt);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(Variables.cashAmount)){
			formattedString = callFormatMethod(CASH_TEXT, "", Variables.cashAmount);
			basePR.printData(formattedString);
		}


		// Changes Start
		boolean addSum     = false;
		int sizeOfSrucList = instance.surchrgeAmountList.size();
		float totalSum     = 0;

		if(Variables.subChargeAmount > 0)
		{
			addSum          = true;
			formattedString = callFormatMethod(CREDIT_TEXT_SUR, "", Variables.subChargeAmount);
			basePR.printData(formattedString);
		}

		for (int i = 0; i < sizeOfSrucList; i++) {	
			float amount = instance.surchrgeAmountList.get(i);
			totalSum    += amount;

			if(sizeOfSrucList == 1){
				if(addSum){
					if(PrintSettings.isValueGreaterThanZero(amount+Variables.subChargeAmount)){
						formattedString = callFormatMethod(CC_TOTAL, "", amount+Variables.subChargeAmount);
						basePR.printData(formattedString);
					}
				}
				else{
					if(PrintSettings.isValueGreaterThanZero(amount)){
						formattedString = callFormatMethod(CREDIT_TEXT, "", amount);
						basePR.printData(formattedString);
					}
				}
			}
			else{
				if(PrintSettings.isValueGreaterThanZero(amount)){				
					formattedString = callFormatMethod(CREDIT_TEXT, "", amount);
					basePR.printData(formattedString);
				}
			}
		}

		if(sizeOfSrucList > 1 && addSum){
			if(PrintSettings.isValueGreaterThanZero(totalSum + Variables.subChargeAmount)){
				formattedString = callFormatMethod(CC_TOTAL, "", totalSum + Variables.subChargeAmount);
				basePR.printData(formattedString);
			}
		}

		// Changes End	

		if(PrintSettings.isValueGreaterThanZero(Variables.checkAmount)){
			formattedString = callFormatMethod(CHECK_TEXT, "", Variables.checkAmount);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(Variables.custom1Amount)){
			formattedString = callFormatMethod(MaintFragmentTender1.getCustom1Name(mContext)+" :", "", Variables.custom1Amount);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(Variables.custom2Amount)){
			formattedString = callFormatMethod(MaintFragmentTender1.getCustom2Name(mContext)+" :" , "", Variables.custom2Amount);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(Variables.rewardsAmount)){			
			formattedString = callFormatMethod(REWARD_TEXT, "", Variables.rewardsAmount);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(Variables.giftAmount)){
			formattedString = callFormatMethod(GIFT_TEXT, "", Variables.giftAmount);
			basePR.printData(formattedString);
		}

		basePR.printData("\n");

		if(MyPreferences.getBooleanPrefrences(IS_TIP_ON_PS, mContext) && Variables.paymentByCC && !Variables.isDejavooSuccess)   // Print tip line when payment done by CC
		{
			formattedString = "Tip Amount"+"                  _______________"+"\n";
			basePR.printData(formattedString);
			formattedString ="Total Amount"+"                _______________"+"\n";
			basePR.printData(formattedString);
			formattedString ="Signature"+" _________________________________"+"\n";
			basePR.printData(formattedString);
		}

		else if (!MyPreferences.getBooleanPrefrences(IS_TIP_ON_PS, mContext) && Variables.paymentByCC && !Variables.isDejavooSuccess) // Print Signature when  payment done by CC when tip is off
		{
			formattedString ="Signature"+" _________________________________"+"\n";
			basePR.printData(formattedString);
		}


		if (!Variables.orderComment.isEmpty()) {
			basePR.largeText();
			basePR.underLine(true);
			basePR.printData(COMMENT);
			basePR.underLine(false);
			basePR.smallText();
			basePR.printData(Variables.orderComment);
		}

		if(needToPrintBarcodeForTsys){
			if(MyPreferences.getBooleanPrefrences(QR_CODE_PRINTING, mContext)){
				basePR.print2DQrCode(Variables.gateWayTrasId);
			}		

			if(MyPreferences.getBooleanPrefrences(BAR_CODE_PRINTING, mContext)){
				basePR.print1DBarCode(Variables.gateWayTrasId);
			}
		}

		basePR.largeText();

		if (Variables.customerName.isEmpty()) 			
			formattedString = "---Thank You & Visit Again!---";
		else 	
			formattedString = "---"+Variables.customerName + ", Thank You & Visit Again!---";

		basePR.printData(PrintSettings.onFormatHeaderAndFooter(formattedString)+"\n\n");
		basePR.cutterCmd();
	}


	/**
	 * @author Shivam  this method will be used to print the receipt when payment is done in parts or by bill split.
	 * @param requestMode  requestMode Specify the PaymentMode like cash,credit,check
	 * @param paidAmount   payAmount for that transaction
	 */

	public void onEachBillPriniting(int requestMode, float paidAmount,BasePR basePR){

		String text1 = MyPreferences.getMyPreference(TEXT1, mContext);
		String text2 = MyPreferences.getMyPreference(TEXT2, mContext);
		String text3 = MyPreferences.getMyPreference(TEXT3, mContext);
		String text4 = MyPreferences.getMyPreference(TEXT4, mContext);

		String formattedString ;

		basePR.playBuzzer();
		basePR.largeText();

		if (text1.isEmpty())
			basePR.printData(DEFAULT_ADDRESS);
		else 
			basePR.printData(text1);

		if (text2.isEmpty()) 
			basePR.printData(DEFAULT_ADDRESS);
		else
			basePR.printData(text2);

		if (text3.isEmpty())
			basePR.printData(DEFAULT_ADDRESS);
		else
			basePR.printData(text3);

		if (text4.isEmpty())
			basePR.printData(DEFAULT_ADDRESS+"\n");
		else
			basePR.printData(text4);

		basePR.smallText();

		formattedString = (String) DateFormat.format("yyyy/MM/dd hh:mm:ss",new Date().getTime());
		basePR.printData(formattedString);

		String transId  = MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext);
		basePR.printData("TransID  == "+transId+"\n");

		if(MyPreferences.getBooleanPrefrences(QR_CODE_PRINTING, mContext)){
			basePR.print2DQrCode(transId);
			basePR.printData("\n");
		}

		if(MyPreferences.getBooleanPrefrences(BAR_CODE_PRINTING, mContext)){
			basePR.print1DBarCode(transId);
		}

		for (int index = 0; index < instance.dataList.size(); index ++) {

			CheckOutParentModel   parent                    = instance.dataList.get(index);		
			ProductModel  product                      		= parent.getProduct();
			List<RelationalOptionModel> listOfchilds 	    = parent.getChilds();

			String  itemQty    = product.getProductQty();
			String productName = product.getProductName();
			float newPrice     = Float.parseFloat(product.getProductCalAmount());
			formattedString    = callFormatMethod(productName,itemQty,newPrice);

			basePR.printData(formattedString);

			if (!listOfchilds.isEmpty()) {
				for(int count = 0; count < listOfchilds.size(); count ++){
					List<SubOptionModel> subOptionModels = listOfchilds.get(count).getListOfSubOptionModel();
					int sizeOfEachList    = subOptionModels.size();

					for(int index1 = 0 ; index1 < sizeOfEachList ;index1++){
						SubOptionModel subOptionModel = subOptionModels.get(index1);
						productName   = subOptionModel.getSubOptionName();
						basePR.printData(PrintSettings.onReformatName(productName));
					}
				}
			}			

		}

		basePR.printData("\n");

		formattedString = callFormatMethod(TOTAL_TEXT, "", Variables.itemsAmount);
		basePR.printData(formattedString);

		formattedString = callFormatMethod(DISCOUNT_TEXT, "", Variables.totalDiscount);
		basePR.printData(formattedString);

		formattedString = callFormatMethod(TAX_TEXT, "", Variables.taxAmount);
		basePR.printData(formattedString);


		formattedString = callFormatMethod(NET_AMOUNT_TEXT, "", Variables.totalBillAmount);
		basePR.printData(formattedString);

		switch (requestMode) {
		case 1:
			formattedString = callFormatMethod(CASH_TEXT, "", paidAmount);
			break;
		case 2:
			formattedString = callFormatMethod(CREDIT_TEXT, "",paidAmount);
			break;
		case 3:
			formattedString = callFormatMethod(CHECK_TEXT, "",paidAmount);
			break;


		default:
			break;
		}

		basePR.printData(formattedString);
		basePR.printData("\n");

		if(MyPreferences.getBooleanPrefrences(IS_TIP_ON_PS, mContext) && Variables.paymentByCC && !Variables.isDejavooSuccess)   // Print tip line when payment done by CC
		{
			formattedString = "Tip Amount"+"                  _______________"+"\n";
			basePR.printData(formattedString);
			formattedString ="Total Amount"+"                _______________"+"\n";
			basePR.printData(formattedString);
			formattedString ="Signature"+" _________________________________"+"\n";
			basePR.printData(formattedString);
		}
		else if (!MyPreferences.getBooleanPrefrences(IS_TIP_ON_PS, mContext) && Variables.paymentByCC && !Variables.isDejavooSuccess) // Print Signature when  payment done by CC when tip is off
		{
			formattedString ="Signature"+" _________________________________"+"\n";
			basePR.printData(formattedString);
		}

		basePR.largeText();

		if (Variables.customerName.isEmpty()) 			
			formattedString = "---Thank You & Visit Again!---";
		else 	
			formattedString = "---"+Variables.customerName + ", Thank You & Visit Again!---";

		basePR.printData(PrintSettings.onFormatHeaderAndFooter(formattedString)+"\n\n");
		basePR.cutterCmd();
	}


	private String callFormatMethod(String headerText,String qty,Object price) {
		return PrintSettings.onReformatName(headerText) + PrintSettings.onReformatQty(qty) + PrintSettings.onReformatPrice(MyStringFormat.formatWith2DecimalPlaces(price));
	}

	public void onFailedToSaveReciepts(ArrayList<CheckOutParentModel> parentList, ArrayList<Float> surchargeList, float total, float discount, float tax, float netAmount, float change, float cash, float check, float rewardAmount, float giftCard, float creditAmt,BasePR basePR) {		

		String text1 = MyPreferences.getMyPreference(TEXT1, mContext);
		String text2 = MyPreferences.getMyPreference(TEXT2, mContext);
		String text3 = MyPreferences.getMyPreference(TEXT3, mContext);
		String text4 = MyPreferences.getMyPreference(TEXT4, mContext);

		String formattedString ;

		basePR.playBuzzer();
		basePR.largeText();

		if (text1.isEmpty())
			basePR.printData(DEFAULT_ADDRESS);
		else 
			basePR.printData(text1);

		if (text2.isEmpty()) 
			basePR.printData(DEFAULT_ADDRESS);
		else
			basePR.printData(text2);

		if (text3.isEmpty())
			basePR.printData(DEFAULT_ADDRESS);
		else
			basePR.printData(text3);

		if (text4.isEmpty())
			basePR.printData(DEFAULT_ADDRESS+"\n");
		else
			basePR.printData(text4);

		basePR.smallText();

		formattedString = (String) DateFormat.format("yyyy/MM/dd hh:mm:ss",new Date().getTime());
		basePR.printData(formattedString);

		String transId  = MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext);
		basePR.printData("TransID  == "+transId+"\n");

		if(MyPreferences.getBooleanPrefrences(QR_CODE_PRINTING, mContext)){
			basePR.print2DQrCode(transId);
			basePR.printData("\n");
		}

		if(MyPreferences.getBooleanPrefrences(BAR_CODE_PRINTING, mContext)){
			basePR.print1DBarCode(transId);
		}


		for (int index = 0; index < parentList.size(); index ++) {

			CheckOutParentModel   parent                    = parentList.get(index);		
			ProductModel  product                      		= parent.getProduct();
			List<RelationalOptionModel> listOfchilds 	    = parent.getChilds();

			String  itemQty    = product.getProductQty();
			String productName = product.getProductName();
			float newPrice     = Float.parseFloat(product.getProductCalAmount());
			formattedString    = callFormatMethod(productName,itemQty,newPrice);

			basePR.printData(formattedString);

			if (!listOfchilds.isEmpty()) {
				for(int count = 0; count < listOfchilds.size(); count ++){
					List<SubOptionModel> subOptionModels = listOfchilds.get(count).getListOfSubOptionModel();
					int sizeOfEachList    = subOptionModels.size();

					for(int index1 = 0 ; index1 < sizeOfEachList ;index1++){
						SubOptionModel subOptionModel = subOptionModels.get(index1);
						productName   = subOptionModel.getSubOptionName();
						basePR.printData(PrintSettings.onReformatName(productName));
					}
				}
			}		
		}

		basePR.printData("\n");

		formattedString = callFormatMethod(TOTAL_TEXT, "", total);
		basePR.printData(formattedString);

		formattedString = callFormatMethod(DISCOUNT_TEXT, "", discount);
		basePR.printData(formattedString);

		formattedString = callFormatMethod(TAX_TEXT, "", tax);
		basePR.printData(formattedString);


		formattedString = callFormatMethod(NET_AMOUNT_TEXT, "", netAmount);
		basePR.printData(formattedString);

		if(PrintSettings.isValueGreaterThanZero(change)){
			formattedString = callFormatMethod(CHANGE_TEXT, "", change);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(cash)){
			formattedString = callFormatMethod(CASH_TEXT, "", cash);
			basePR.printData(formattedString);
		}

		// Changes Start
		boolean addSum     = false;
		int sizeOfSrucList = surchargeList.size();
		float totalSum     = 0;

		if(Variables.subChargeAmount > 0)
		{
			addSum          = true;
			formattedString = callFormatMethod(CREDIT_TEXT_SUR, "", Variables.subChargeAmount);
			basePR.printData(formattedString);
		}

		for (int i = 0; i < sizeOfSrucList; i++) {	
			float amount = surchargeList.get(i);
			totalSum    += amount;

			if(sizeOfSrucList == 1){
				if(addSum){
					formattedString = callFormatMethod(CC_TOTAL, "", amount+Variables.subChargeAmount);
					basePR.printData(formattedString);
				}
				else{
					formattedString = callFormatMethod(CREDIT_TEXT, "", amount);
					basePR.printData(formattedString);
				}
			}
			else{
				formattedString = callFormatMethod(CREDIT_TEXT, "", amount);
				basePR.printData(formattedString);
			}
		}

		if(sizeOfSrucList > 1 && addSum){
			formattedString = callFormatMethod(CC_TOTAL, "", totalSum + Variables.subChargeAmount);
			basePR.printData(formattedString);
		}

		// Changes End


		if(PrintSettings.isValueGreaterThanZero(check)){
			formattedString = callFormatMethod(CHECK_TEXT, "", check);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(rewardAmount)){

			formattedString = callFormatMethod(REWARD_TEXT, "", rewardAmount);
			basePR.printData(formattedString);
		}

		if(PrintSettings.isValueGreaterThanZero(giftCard)){
			formattedString = callFormatMethod(GIFT_TEXT, "", giftCard);
			basePR.printData(formattedString);
		}

		basePR.printData("\n");

		if(MyPreferences.getBooleanPrefrences(IS_TIP_ON_PS, mContext) && Variables.paymentByCC)   // Print tip line when payment done by CC
		{
			formattedString = "Tip Amount"+"                  _______________"+"\n";
			basePR.printData(formattedString);
			formattedString ="Total Amount"+"                _______________"+"\n";
			basePR.printData(formattedString);
			formattedString ="Signature"+" _________________________________"+"\n";
			basePR.printData(formattedString);
		}

		else if (!MyPreferences.getBooleanPrefrences(IS_TIP_ON_PS, mContext) && Variables.paymentByCC) // Print Signature when  payment done by CC when tip is off
		{
			formattedString ="Signature"+" _________________________________"+"\n";
			basePR.printData(formattedString);
		}

		if (!Variables.orderComment.isEmpty()) {
			basePR.largeText();
			basePR.printData(COMMENT);
			basePR.underLine(true);
			basePR.smallText();
			basePR.printData(Variables.orderComment);
		}

		basePR.largeText();

		if (Variables.customerName.isEmpty()) 			
			formattedString = "---Thank You & Visit Again!---";
		else 	
			formattedString = "---"+Variables.customerName + ", Thank You & Visit Again!---";

		basePR.printData(PrintSettings.onFormatHeaderAndFooter(formattedString)+"\n\n");
		basePR.cutterCmd();
	}
}

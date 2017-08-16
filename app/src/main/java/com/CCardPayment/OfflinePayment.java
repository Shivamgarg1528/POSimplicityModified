package com.CCardPayment;

import com.AsyncTasks.CompleteReportInMagento;
import com.AsyncTasks.ShareOrderWithCustomer;
import com.Fragments.CreditFragment;
import com.PosInterfaces.PrefrenceKeyConst;
import com.RecieptPrints.GoForPrint;
import com.RecieptPrints.PrintReceiptCustomer;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.POSApp;
import com.utils.MyPreferences;
import com.utils.Variables;
import com.posimplicity.HomeActivity;
import com.posimplicity.TenderActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class OfflinePayment implements PrefrenceKeyConst{

	private Context mContext;
	private HomeActivity instance;
	private CreditFragment creditFragment;
	POSApp POSApp;

	public OfflinePayment(Context mContext) {
		super();
		this.mContext          = mContext;
		this.instance          = HomeActivity.localInstance;
		this.creditFragment    = CreditFragment.localInstanceOFCCFragment;
		this.POSApp = POSApp.getInstance();
	}

	private void onOfflinePayment() {

		Variables.paymentByCC = true;
		instance.surchrgeAmountList.add(creditFragment.ccSubTotalAmt);

		Variables.ccAmount    += creditFragment.ccSubTotalAmt;
		Variables.halfAmount   = creditFragment.ccSubTotalAmt;

		//System.out.println("Pay Amount       =  "+creditFragment.ccSubTotalAmt);
		creditFragment.findCreditCardPaymentAmount();
		Variables.dueCCAmount  = creditFragment.ccSubTotalAmt;

		//System.out.println("Paid Amount       =  "+Variables.ccAmount);
		//System.out.println("Sur  Amount       =  "+Variables.subChargeAmount);
		//System.out.println("Due  Amount       =  "+Variables.dueCCAmount);

		if(Variables.dueCCAmount == 0.00 && instance.surchrgeAmountList.size() > 1) {	

			boolean letPrintDone = true;
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
				letPrintDone = false;

				new UsbPR(mContext, new PrinterCallBack() {

					@Override
					public void onDisconnected() {
						PrintSettings.showUsbNotAvailableToast(mContext);
						onHalfAmountPrinting();
					}

					@Override
					public void onConnected(BasePR pPrinter) {
						int numdersOfReciepts = CreditFragment.NUMBER_OF_RECEIPT;
						if(MyPreferences.getBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS, mContext))
							numdersOfReciepts++;
						for(int index = 0 ; index < numdersOfReciepts ; index ++){
							new PrintReceiptCustomer(mContext).onPrintRecieptCustomer(pPrinter, true);
						}
						onHalfAmountPrinting();
					}
				}).onConnectionStart();;
			}

			if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
				int numdersOfReciepts = CreditFragment.NUMBER_OF_RECEIPT;
				if(MyPreferences.getBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS, mContext))
					numdersOfReciepts++;
				for(int index = 0 ; index < numdersOfReciepts ; index ++){
					new PrintReceiptCustomer(mContext).onPrintRecieptCustomer(POSApp.getPrinterBT(), true);
				}
			}	

			if(letPrintDone){
				onHalfAmountPrinting();
			}			
		}

		if(Variables.dueCCAmount == 0.00) {		
			new CompleteReportInMagento(mContext,CreditFragment.ORDER_STATUS,CreditFragment.PAYMENT_MODE,true).execute();

			if(!Variables.billToName.isEmpty())				
				new ShareOrderWithCustomer(mContext).execute();

			GoForPrint goForPrint = new GoForPrint(mContext, 1,true);
			goForPrint.onExectue();
		}
		else {			
			boolean letPrintDone = true;
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
				letPrintDone = false;

				new UsbPR(mContext, new PrinterCallBack() {

					@Override
					public void onDisconnected() {
						onHalfAmountPrinting();
					}

					@Override
					public void onConnected(BasePR pPrinter) {
						int numdersOfReciepts = CreditFragment.NUMBER_OF_RECEIPT;
						if(MyPreferences.getBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS, mContext))
							numdersOfReciepts++;
						for(int index = 0 ; index < numdersOfReciepts ; index ++){
							new PrintReceiptCustomer(mContext).onPrintRecieptCustomer(pPrinter, true);
						}
						onHalfAmountPrinting();
					}
				}).onConnectionStart();
			}

			if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
				int numdersOfReciepts = CreditFragment.NUMBER_OF_RECEIPT;
				if(MyPreferences.getBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS, mContext))
					numdersOfReciepts++;
				for(int index = 0 ; index < numdersOfReciepts ; index ++){
					new PrintReceiptCustomer(mContext).onPrintRecieptCustomer(POSApp.getPrinterBT(), true);
				}
			}

			if(letPrintDone){
				onHalfAmountPrinting();
			}
		}
	}

	private void onHalfAmountPrinting() {

		if(!(Variables.dueCCAmount == 0.00)) {	
			mContext.startActivity(new Intent(mContext, TenderActivity.class));
			((Activity) mContext).finish();
			Variables.paymentByCC = false;
		}
	}

	public void onExecute()
	{
		onOfflinePayment();
	}
}

package com.RecieptPrints;

import android.app.Activity;
import android.content.Context;

import com.dialog.ChangeAmountDialog;
import com.AsyncTasks.CompleteReportInMagento;
import com.AsyncTasks.ShareOrderWithCustomer;
import com.adapter.SplitBillAdapter;
import com.CustomControls.ToastHelper;
import com.Dialogs.SplitBillRowsDialog;
import com.PosInterfaces.PrefrenceKeyConst;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.POSApp;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.HomeActivity;

public class EachBillPrint implements PrefrenceKeyConst{

	private Context mContext;
	private HomeActivity localInsatance;
	private final int CREDIT_FRAGMENT  = 2;
	private int numdersOfReciepts = 1;
	private SplitBillRowsDialog splitBillRowsDialog;
	float changeAmount;

	public EachBillPrint(Context mContext) {
		super();
		this.mContext            = mContext;
		this.localInsatance      = HomeActivity.localInstance;
		this.splitBillRowsDialog = SplitBillAdapter.splitBillRowsDialog;
	}


	public void onExectue() {

		SplitBillAdapter.localObj.setSplitBillPaidAmount(MyStringFormat.formatWith2DecimalPlaces(SplitBillAdapter.billPaidAmount + SplitBillAdapter.amountToPaid));
		SplitBillAdapter.localObj.setSplitBillPayAmount(MyStringFormat.formatWith2DecimalPlaces(SplitBillAdapter.billLeftToPay));
		SplitBillAdapter.localObj.setSplitBillPayAmountText(MyStringFormat.formatWith2DecimalPlaces(SplitBillAdapter.billLeftToPay));
		SplitBillAdapter.localObj.setPartOfBillPaid(true);

		if(SplitBillAdapter.billLeftToPay == 0){
			SplitBillAdapter.localObj.setBillPaid(true);
			ToastUtils.showOwnToast(mContext, "Bill Paid SuccessFully");			
		}
		else if(SplitBillAdapter.billLeftToPay < 0){
			changeAmount = SplitBillAdapter.amountToPaid - SplitBillAdapter.billDueAmount;
			SplitBillAdapter.localObj.setSplitBillPaidAmount(MyStringFormat.formatWith2DecimalPlaces(SplitBillAdapter.billPaidAmount + SplitBillAdapter.billDueAmount));
			SplitBillAdapter.localObj.setSplitBillPayAmount(MyStringFormat.formatWith2DecimalPlaces(0.0f));
			SplitBillAdapter.localObj.setSplitBillPayAmountText(MyStringFormat.formatWith2DecimalPlaces(0.0f));
			SplitBillAdapter.localObj.setBillPaid(true);
			ToastUtils.showOwnToast(mContext, "Bill Paid SuccessFully");
			SplitBillAdapter.amountToPaid = SplitBillAdapter.billDueAmount;			
		}
		updateVariables();

		SplitBillAdapter.adapter.notifyDataSetChanged();
		SplitBillAdapter.anyRowLeftForPayment();



		if(!SplitBillAdapter.anyRowLeftForPayment){

			new CompleteReportInMagento(mContext,splitBillRowsDialog.oRDER_STATUS,splitBillRowsDialog.pAYMENT_MODE,true).execute();

			if (!Variables.billToName.isEmpty()) {
				new ShareOrderWithCustomer(mContext).execute();
			}
		}

		if(SplitBillAdapter.paymentMode == CREDIT_FRAGMENT)
			numdersOfReciepts++;
		if(MyPreferences.getBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS, mContext))
			numdersOfReciepts++;

		boolean letPrintDone = true;
		if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){

			letPrintDone = false;
			new UsbPR(mContext, new PrinterCallBack() {

				@Override
				public void onDisconnected() {
					onPostExection();
				}

				@Override
				public void onConnected(BasePR pPrinter) {
					PrintReceiptCustomer pReciept = new PrintReceiptCustomer(mContext);
					for(int index = 0 ; index < numdersOfReciepts ; index ++){
						pReciept.onEachBillPriniting(SplitBillAdapter.paymentMode,SplitBillAdapter.amountToPaid, pPrinter);
					}
					PrintExtraReceipt.onOpenCashDrawer(pPrinter);
					onPostExection();
				}
			}).onConnectionStart();
		}
		if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
			PrintReceiptCustomer pReciept = new PrintReceiptCustomer(mContext);
			for(int index = 0 ; index < numdersOfReciepts ; index ++){
				pReciept.onEachBillPriniting(SplitBillAdapter.paymentMode,SplitBillAdapter.amountToPaid, POSApp.getInstance().getPrinterBT());
			}
			PrintExtraReceipt.onOpenCashDrawer(POSApp.getInstance().getPrinterBT());
		}

		if(letPrintDone)
			onPostExection();
	}


	private void updateVariables() {
		switch (SplitBillAdapter.paymentMode) {

		case 1:
			Variables.cashAfterChange  += SplitBillAdapter.amountToPaid; 
			break;			

		case 3:
			Variables.checkAmount      += SplitBillAdapter.amountToPaid;
			break;

		default:
			break;
		}
	}


	private void onPostExection(){

		if(changeAmount > 0)
			ChangeAmountDialog.showChangeAmountOnBillSplit(mContext, localInsatance, changeAmount,splitBillRowsDialog);
		else {
			if(!SplitBillAdapter.anyRowLeftForPayment){
				splitBillRowsDialog.dismiss();
				ToastHelper.showApprovedToast(mContext);
				localInsatance.resetAllData(mContext,0);
				((Activity) mContext).finish();
			}
		}
	}
}

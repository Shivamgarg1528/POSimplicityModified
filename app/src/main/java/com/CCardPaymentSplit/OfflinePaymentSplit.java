package com.CCardPaymentSplit;

import android.content.Context;
import com.adapter.SplitBillAdapter;
import com.RecieptPrints.EachBillPrint;
import com.utils.Variables;
import com.posimplicity.HomeActivity;

public class OfflinePaymentSplit {
	
	private Context mContext;
	private HomeActivity instance;


	public OfflinePaymentSplit(Context mContext) {
		super();
		this.mContext = mContext;
		this.instance  = HomeActivity.localInstance;
	}
	
	public void onExecute()
	{
		onOfflinePayment();
	}
	
	private void onOfflinePayment() {
		Variables.paymentByCC = true;
		instance.surchrgeAmountList.add(SplitBillAdapter.amountToPaid);
		Variables.ccAmount += SplitBillAdapter.amountToPaid;
		EachBillPrint eachBillPrint = new EachBillPrint(mContext);
		eachBillPrint.onExectue();
	}
}

package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.RecieptPrints.GoForPrint;
import com.posimplicity.R;

public class ReceiptPrintPrompt {

	private GoForPrint goForPrint;
	private Context    mcContext;
	int requestTender  = 0;


	public ReceiptPrintPrompt(GoForPrint goForPrint, Context mcContext, int requestTender) {
		super();
		this.goForPrint    = goForPrint;
		this.mcContext     = mcContext;
		this.requestTender = requestTender;
	}

	public void showReceiptPrintPrompt(){

		AlertDialog.Builder builder = new AlertDialog.Builder(mcContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("Receipt Printing ?");
		builder.setCancelable(false);

		switch (requestTender) {

		case GoForPrint.CASH_FRAGMENT:
		case GoForPrint.CHECQUE_FRAGMENT:			
		case GoForPrint.REWARDS_FRAGMENT:

			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					goForPrint.onPreExecute(false);
				}
			});

			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					goForPrint.onPreExecute(true);
				}
			});

			break;

		case GoForPrint.CREDIT_FRAGMENT:
			
			builder.setNegativeButton("No Receipt", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					goForPrint.onPreExecute(false);
				}
			});
			
			builder.setNeutralButton("One Receipt", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					goForPrint.onPreExecute(true);
				}
			});

			builder.setPositiveButton("Two Receipt", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					goForPrint.numdersOfReciepts ++;
					goForPrint.onPreExecute(true);
				}
			});

			break;

		default:
			break;
		}



		builder.show();		
		builder.create();		

	}	

}

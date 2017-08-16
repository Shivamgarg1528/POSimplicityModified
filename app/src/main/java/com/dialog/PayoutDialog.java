package com.dialog;

import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.Beans.CommentModel;
import com.posimplicity.database.local.PayoutDescTable;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.database.local.SaveTransactionDetails;
import com.RecieptPrints.PrintExtraReceipt;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.POSApp;
import com.utils.MyStringFormat;
import com.utils.ToastUtils;

public class PayoutDialog {

	private Context mContext;
	private String selectOption,payoutAmt,description;

	public PayoutDialog(Context mContext) {
		super();
		this.mContext        = mContext;
	}

	public String returnSelectedPayoutList(List<CommentModel> dataList){
		StringBuilder st = null;
		if(!dataList.isEmpty()){
			for(int index = 0; index < dataList.size() ; index ++){
				CommentModel commentModel = dataList.get(index);
				if(commentModel.isCommentSelected()){
					if(st == null){
						st = new StringBuilder();
						st.append(commentModel.getCommentString());
					}
					else
					{
						st.append(","+commentModel.getCommentString());	
					}						
				}
			}			
		}
		else 
			st = new StringBuilder("");
		return st.toString();
	}

	public void showEachPayoutClick(final String selectedOption,final int item) {
		this.selectOption = selectedOption;

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(selectedOption.toUpperCase(Locale.ENGLISH)+" :-");

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText inputAmount = new EditText(mContext);
		inputAmount.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
		inputAmount.setHint("Enter Payout Amount Here");

		final EditText inputComment = new EditText(mContext);
		inputComment.setInputType(InputType.TYPE_CLASS_TEXT);
		inputComment.setHint("Enter Custom Description Here");

		linearLayout.addView(inputAmount);
		linearLayout.addView(inputComment);

		builder.setView(linearLayout);

		final List<CommentModel> dataList = new PayoutDescTable(mContext).getAllInfoFromTableDefalut();
		if(!dataList.isEmpty()){

			final String[] items = new String[dataList.size()];

			for(int index = 0; index < dataList.size() ; index++)
				items[index] = dataList.get(index).getCommentString();

			builder.setMultiChoiceItems(items, null, new OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					dataList.get(which).setCommentSelected(isChecked);
				}
			});
		}

		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				String value      = inputAmount.getText().toString();
				String inputDesc  = inputComment.getText().toString().trim();
				String selected   = returnSelectedPayoutList(dataList);


				if(inputDesc.isEmpty() && selected.isEmpty())
					inputDesc = ReportsTable.DEFAULT_DESCRIPTION;
				else if(inputDesc.isEmpty() && !selected.isEmpty())
					inputDesc = selected;
				else if(!inputDesc.isEmpty() && selected.isEmpty()){} 				
				else
					inputDesc += ","+selected; 

				if(value.isEmpty()){
					ToastUtils.showOwnToast(mContext, "Enter Payout Amount Before Save");
					new PayoutDialog(mContext).showEachPayoutClick(selectedOption, item);
				}
				else{

					switch (item) {

					case 0:
						SaveTransactionDetails.savePayoutsTransactionInDataBase(mContext, Float.parseFloat(value), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, inputDesc,ReportsTable.LOTTERY_AMOUNT);
						break;

					case 1:
						SaveTransactionDetails.savePayoutsTransactionInDataBase(mContext, 0.0f, Float.parseFloat(value), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, inputDesc,ReportsTable.EXPENSES_AMOUNT);
						break;

					case 2:
						SaveTransactionDetails.savePayoutsTransactionInDataBase(mContext, 0.0f, 0.0f, Float.parseFloat(value), 0.0f, 0.0f, 0.0f, 0.0f, ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, inputDesc,ReportsTable.SUPPLIES_AMOUNT);
						break;

					case 3:
						SaveTransactionDetails.savePayoutsTransactionInDataBase(mContext, 0.0f, 0.0f, 0.0f,  Float.parseFloat(value), 0.0f, 0.0f,0.0f, ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, inputDesc,ReportsTable.PRODUCT_AMOUNT);
						break;

					case 4:
						SaveTransactionDetails.savePayoutsTransactionInDataBase(mContext, 0.0f, 0.0f, 0.0f, 0.0f,  Float.parseFloat(value), 0.0f, 0.0f,ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, inputDesc,ReportsTable.OTHER_AMOUNT);
						break;

					case 5:
						SaveTransactionDetails.savePayoutsTransactionInDataBase(mContext, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Float.parseFloat(value),0.0f, ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, inputDesc,ReportsTable.TIP_Pay_AMOUNT);
						break;

					case 6:
						SaveTransactionDetails.savePayoutsTransactionInDataBase(mContext, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Float.parseFloat(value), ReportsTable.NO_REFUND, ReportsTable.SUCCESSFULL, inputDesc,ReportsTable.MANUAL_CASH_REFUND);
						break;

					default:
						break;
					}

					payoutAmt   = MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(value));
					description = inputDesc;
					ToastUtils.showOwnToast(mContext, "SuccessFully Saved");

					if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
						new UsbPR(mContext, new PrinterCallBack() {

							@Override
							public void onDisconnected() {
								PrintSettings.showUsbNotAvailableToast(mContext);
							}

							@Override
							public void onConnected(BasePR pPrinter) {
								PrintExtraReceipt.onPrintPayoutReceipt(mContext, payoutAmt, selectOption, description, pPrinter);
							}
						}).onConnectionStart();
					}

					if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
						PrintExtraReceipt.onPrintPayoutReceipt(mContext, payoutAmt, selectOption, description, POSApp.getInstance().getPrinterBT());
					}					
				}
			}
		});

		builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}

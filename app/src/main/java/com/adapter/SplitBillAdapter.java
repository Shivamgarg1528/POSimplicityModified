package com.adapter;

import java.util.List;

import com.Beans.SplitBillClass;
import com.Dialogs.CCDialogForSplitBill;
import com.Dialogs.SplitBillRowsDialog;
import com.RecieptPrints.EachBillPrint;
import com.utils.CalculateWidthAndHeigth;
import com.utils.POSApp;
import com.utils.MyStringFormat;
import com.utils.ToastUtils;
import com.posimplicity.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SplitBillAdapter extends BaseAdapter {

	public static float amountToPaid;
	public static SplitBillRowsDialog splitBillRowsDialog;
	public static boolean anyRowLeftForPayment;
	public static int paymentMode;
	public static float billPaidAmount,enterAmount,billLeftToPay,billDueAmount;
	private static List<SplitBillClass> listOfSplittedRow;
	private static int listSize;

	public static SplitBillClass localObj;
	public static SplitBillAdapter adapter;

	private Context mContext;
	private LayoutInflater layoutInfaltor;
	private POSApp gApp;

	public SplitBillAdapter(Context mContext,List<SplitBillClass> listOfSplittedRow, SplitBillRowsDialog splitBillRowsDialog) {
		super();
		this.mContext                         = mContext;
		this.layoutInfaltor                   = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.gApp                             = POSApp.getInstance();
		SplitBillAdapter.splitBillRowsDialog  = splitBillRowsDialog;
		SplitBillAdapter.listOfSplittedRow    = listOfSplittedRow;
	}

	@Override
	public int getCount() {
		SplitBillAdapter.listSize  = listOfSplittedRow.size();
		return listSize;
	}

	@Override
	public SplitBillClass getItem(int position) {
		return listOfSplittedRow.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SplitHolder splitHolder;
		if(convertView == null){
			splitHolder = new SplitHolder();
			convertView = layoutInfaltor.inflate(R.layout.split_bill_row, null);
			splitHolder.rowNameTv            = (TextView) convertView.findViewById(R.id.Split_Bill_Adapter_Row_TV_Customer);
			splitHolder.splitBillEdt         = (EditText) convertView.findViewById(R.id.Split_Bill_Adapter_Row_EDT_billAmount);
			splitHolder.splitBillPayEdt      = (EditText) convertView.findViewById(R.id.Split_Bill_Adapter_Row_EDT_billPayAmount);
			splitHolder.splitBillPaidTv      = (TextView) convertView.findViewById(R.id.Split_Bill_Adapter_Row_TV_BillPaidAmount);
			splitHolder.splitBillDueTv       = (TextView) convertView.findViewById(R.id.Split_Bill_Adapter_Row_TV_BillDueAmount);
			splitHolder.paymentBtn           = (Button)   convertView.findViewById(R.id.Split_Bill_Adapter_Row_Do_Payment);
			splitHolder.billStatus           = (ImageView)convertView.findViewById(R.id.Split_Bill_Adapter_Row_IV_billStatus);
			splitHolder.billUpdateBtn        = (Button)   convertView.findViewById(R.id.Split_Bill_Adapter_Row_Btn_ApplyBillChanges);
			splitHolder.paymentModeSpinner   = (Spinner)  convertView.findViewById(R.id.Split_Bill_Adapter_Row_SP_PaymentMode);
			convertView.setTag(splitHolder);
		}
		else{
			splitHolder  = 	(SplitHolder) convertView.getTag();
		}

		splitHolder.referenceOfHolder = position;

		SplitBillClass localObj = getItem(splitHolder.referenceOfHolder);
		localObj.setSplitBillDueAmount(MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(localObj.getSplitBillAmount()) - Float.parseFloat(localObj.getSplitBillPaidAmount())));
		splitHolder.rowNameTv.setText(localObj.getRowName());
		splitHolder.splitBillEdt.setText(localObj.getSplitBillAmount());
		splitHolder.splitBillPayEdt.setText(localObj.getSplitBillPayAmount());
		splitHolder.splitBillPaidTv.setText(localObj.getSplitBillPaidAmount());
		splitHolder.splitBillDueTv.setText(localObj.getSplitBillDueAmount());

		if(localObj.isBillPaid()){
			splitHolder.splitBillPayEdt.setEnabled(false);
			splitHolder.splitBillEdt .setEnabled(false);
			splitHolder.paymentBtn.setEnabled(false);
			splitHolder.billUpdateBtn.setEnabled(false);
			splitHolder.billStatus.setImageResource(R.drawable.bill_paid);
			splitHolder.paymentBtn.setText("Closed");
			splitHolder.paymentModeSpinner.setEnabled(false);
		}
		else if(localObj.isPartOfBillPaid()){
			splitHolder.splitBillPayEdt.setEnabled(true);
			splitHolder.splitBillEdt .setEnabled(false);
			splitHolder.paymentBtn.setEnabled(true);
			splitHolder.billUpdateBtn.setEnabled(false);
			splitHolder.paymentModeSpinner.setEnabled(true);
			splitHolder.billStatus.setImageResource(R.drawable.bill_pending);
			splitHolder.paymentBtn.setText("Payment");
		}
		else
		{
			splitHolder.splitBillPayEdt.setEnabled(true);
			splitHolder.splitBillEdt .setEnabled(false);
			splitHolder.paymentBtn.setEnabled(true);
			splitHolder.billUpdateBtn.setEnabled(false);
			splitHolder.paymentModeSpinner.setEnabled(true);
			splitHolder.billStatus.setImageResource(R.drawable.bill_pending);
			splitHolder.paymentBtn.setText("Payment");
		}
		splitHolder.splitBillEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

			@Override
			public void afterTextChanged(Editable enterText) {
				SplitBillClass localObj  = getItem(splitHolder.referenceOfHolder);
				if(enterText.toString().length() > 0 && parseIntoFloat(enterText.toString()) < 0 ? false:true){
					localObj.setSplitBillAmount(MyStringFormat.formatWith2DecimalPlaces(parseIntoFloat(enterText.toString())));
				}else{
					localObj.setSplitBillAmount("0.00");
				}
			}
		});

		splitHolder.splitBillPayEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

			@Override
			public void afterTextChanged(Editable enterText) {
				SplitBillClass localObj  = getItem(splitHolder.referenceOfHolder);
				if(enterText.toString().length() > 0 && parseIntoFloat(enterText.toString()) < 0 ? false:true){
					localObj.setSplitBillPayAmount(MyStringFormat.formatWith2DecimalPlaces(parseIntoFloat(enterText.toString())));
				}else{
					localObj.setSplitBillPayAmount("0.00");
				}
			}
		});

		splitHolder.billUpdateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SplitBillClass localObj  = getItem(splitHolder.referenceOfHolder);
				onBillAmountUpdationTask(localObj,splitHolder.referenceOfHolder,Float.parseFloat(localObj.getSplitBillAmount()));
			}
		});

		splitHolder.paymentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SplitBillAdapter.localObj    = getItem(splitHolder.referenceOfHolder);
				SplitBillAdapter.enterAmount = Float.parseFloat(SplitBillAdapter.localObj.getSplitBillPayAmount());
				onBillPayAmountUdpationTask();
			}
		});

		splitHolder.paymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				SplitBillClass localObj  = getItem(splitHolder.referenceOfHolder);
				localObj.setPaymentMode(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		return convertView;
	}

	public Float parseIntoFloat(String string){
		try{
			return Float.parseFloat(string);
		}
		catch(Exception ex){
			return -1.0f;
		}
	}

	public void onBillPayAmountUdpationTask(){
		paymentMode = 0;
		EachBillPrint eachBillPrint = null;

		if(!localObj.isBillPaid()){
			if(localObj.getPaymentMode() == 0){
				ToastUtils.showOwnToast(mContext, "Please Select Any Payment Mode");
				localObj.setSplitBillPayAmount(localObj.getSplitBillPayAmountText());
				notifyDataSetChanged();
				return;
			}
			else
				paymentMode = localObj.getPaymentMode();

			if(enterAmount <= 0){
				ToastUtils.showOwnToast(mContext, "Please Provide Pay Amount");
				localObj.setSplitBillPayAmount(localObj.getSplitBillPayAmountText());
				notifyDataSetChanged();
			}
			else
			{
				billDueAmount  = Float.parseFloat(localObj.getSplitBillDueAmount());
				billLeftToPay  = billDueAmount - enterAmount;
				billPaidAmount = Float.parseFloat(localObj.getSplitBillPaidAmount());
				amountToPaid   = enterAmount;

				if(billLeftToPay < 0){
					if(paymentMode == 1){
						eachBillPrint = new EachBillPrint(mContext);
						eachBillPrint.onExectue();
					}
					else
					{
						ToastUtils.showOwnToast(mContext, "Pay Amount Must Be Equal Or Less Than Bill Amount");
						localObj.setSplitBillPaidAmount(localObj.getSplitBillPaidAmount());
						localObj.setSplitBillPayAmount(localObj.getSplitBillPayAmountText());
						notifyDataSetChanged();
					}
				}

				else if(billLeftToPay >= 0){

					switch (paymentMode) {

					case 1:
						eachBillPrint = new EachBillPrint(mContext);
						eachBillPrint.onExectue();
						break;

					case 2:
						int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() ,40);
						int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),60);

						CCDialogForSplitBill dailog = new CCDialogForSplitBill(mContext, R.style.myCoolDialog, width, height, false,false, R.layout.dialog_cc_split_bill_payment);
						dailog.show();

						break;

					case 3:
						eachBillPrint = new EachBillPrint(mContext);
						eachBillPrint.onExectue();
						break;

					default:
						break;
					}
				}
			}
		}
	}

	public static boolean anyRowLeftForPayment(){
		anyRowLeftForPayment = false;
		for(int index = 0; index < listSize; index++){
			if(listOfSplittedRow.get(index).isBillPaid())
				continue;
			else
				anyRowLeftForPayment = true;
		}
		return anyRowLeftForPayment;
	}	

	public void onBillAmountUpdationTask(SplitBillClass localObj,int position, float enterAmount){
		boolean isSelectedBtnIsLast = false;
		int listItemLeft    = 0;
		if(listSize == (position+1)){
			isSelectedBtnIsLast = true;
			listItemLeft = 0;
		}
		else{
			for(int index = position+ 1; index < listSize; index++){
				SplitBillClass localObj1 = listOfSplittedRow.get(index);
				if(!localObj1.isPartOfBillPaid())
					listItemLeft ++;
			}
			if(listItemLeft == 0)
				isSelectedBtnIsLast = true;
		}

		if(enterAmount <= 0){
			ToastUtils.showOwnToast(mContext, "Please Provide Bill Amount");
			localObj.setSplitBillAmount(localObj.getSplitBillAmountText());
		}
		else{
			float preSettedAmount = 0.0f; 
			float totalBillAmount = Float.parseFloat(localObj.getTotalBillAmount());
			for(int index = 0; index < position; index++){
				preSettedAmount += Float.parseFloat(listOfSplittedRow.get(index).getSplitBillAmount());
			}
			float remainingAmount = totalBillAmount - preSettedAmount;
			if(remainingAmount > 0){
				if(enterAmount <= remainingAmount) {
					float newRemaingAmount = 0.0f;

					if(isSelectedBtnIsLast){
						newRemaingAmount = Float.parseFloat(localObj.getSplitBillAmountText());
						localObj.setSplitBillAmount(MyStringFormat.formatWith2DecimalPlaces(newRemaingAmount));
						localObj.setSplitBillPayAmount(localObj.getSplitBillAmount());
						ToastUtils.showOwnToast(mContext, "Bill Amount Can't be Modified");
					}
					else{
						newRemaingAmount  = (remainingAmount - enterAmount)/listItemLeft;
						localObj.setSplitBillAmount(MyStringFormat.formatWith2DecimalPlaces(enterAmount));
						localObj.setSplitBillAmountText(MyStringFormat.formatWith2DecimalPlaces(enterAmount));
						localObj.setSplitBillPayAmount(localObj.getSplitBillAmount());
						localObj.setSplitBillPayAmountText(localObj.getSplitBillAmount());

						for(int index = position + 1 ; index < listSize ; index++){
							SplitBillClass changableObj = listOfSplittedRow.get(index);
							if(!changableObj.isPartOfBillPaid()){
								changableObj.setSplitBillAmount(MyStringFormat.formatWith2DecimalPlaces(newRemaingAmount));
								changableObj.setSplitBillAmountText(MyStringFormat.formatWith2DecimalPlaces(newRemaingAmount));
								changableObj.setSplitBillPayAmount(changableObj.getSplitBillAmount());
								changableObj.setSplitBillPayAmountText(changableObj.getSplitBillAmount());
							}
						}
						ToastUtils.showOwnToast(mContext, "Bill Amount Updated");

					}
				}
				else{
					ToastUtils.showOwnToast(mContext, "Bill Amount Cross the Total Due Amount");
					localObj.setSplitBillAmount(localObj.getSplitBillAmountText());
					localObj.setSplitBillPayAmount(localObj.getSplitBillAmount());
				}
			}
			else{
				ToastUtils.showOwnToast(mContext, "Bill Amount Exceed");
				localObj.setSplitBillAmount(localObj.getSplitBillAmountText());
				localObj.setSplitBillPayAmount(localObj.getSplitBillAmount());
			}
		}	
		notifyDataSetChanged();
	}

	public static class SplitHolder {

		TextView rowNameTv;
		EditText splitBillPayEdt;
		EditText splitBillEdt;
		TextView splitBillPaidTv;
		TextView splitBillDueTv;
		Button paymentBtn;
		ImageView billStatus;
		Button billUpdateBtn;
		Spinner paymentModeSpinner;
		int referenceOfHolder;
	}

	public void setCustomAdapters(SplitBillAdapter adapter) {
		SplitBillAdapter.adapter  = adapter;
	}
}

package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.Beans.CustomerModel;
import com.Beans.TSYSResponseModel;
import com.posimplicity.gateway.TSYSGateway;
import com.posimplicity.gateway.TSYSGateway.OnCallBackForTSYS;
import com.RecieptPrints.PrintExtraReceipt;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.database.CustomerTable;
import com.posimplicity.database.local.TipTable;
import com.posimplicity.database.local.TransactionTable;
import com.posimplicity.R;

import java.util.List;

public class TSYSTipAdjustment extends BaseDialog implements View.OnClickListener, TextWatcher,OnItemSelectedListener {

	private EditText tipAmountEdt,transIdEdt,clerkIdEdt;
	private View cancelView;
	private Button processNowBtn,clerkAssingbt;
	private String tipAmtStr;
	private TextView clerkNameTv;
	private String customerId = "";
	private CustomerModel clerkCustomer;
	private boolean clerkInfoExist;
	private Spinner spinner1;
	List<String> spinnerArray  ;


	public TSYSTipAdjustment(Context context, int theme, int width,
			int height, boolean isOutSideTouch, boolean isCancelable,
			int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cancelView    = findViewByIdAndCast(R.id.cancel_tip_adjusment);
		tipAmountEdt  = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Tip_Amount);		
		transIdEdt    = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Trans_Amount);	
		clerkIdEdt    = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_ClerkId);
		processNowBtn = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_BTN_ProcessNow);	
		clerkNameTv   = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_TV_ClerkName);
		clerkAssingbt = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_BTN_AssignClerk);
		spinner1      = findViewByIdAndCast(R.id.spinner1);
		cancelView.setOnClickListener(this);
		processNowBtn.setOnClickListener(this);	
		clerkAssingbt.setOnClickListener(this);
		clerkIdEdt.addTextChangedListener(this);

		spinnerArray                = new TransactionTable(mContext).getTransactions();

		ToastUtils.showOwnToast(mContext, spinnerArray.size() + "....");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, spinnerArray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		spinner1.setAdapter(spinnerArrayAdapter);	
		spinner1.setOnItemSelectedListener(this);
	}	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cancel_tip_adjusment:
			dismiss();
			break;

		case R.id.Dialog_Tip_Adjust_BTN_AssignClerk:
			String clerkId = clerkIdEdt.getText().toString().trim();

			if(clerkId.isEmpty())
			{
				clerkIdEdt.setError("Assign Tip To Clerk First");
				return;
			}

			if(clerkInfoExist){
				ToastUtils.showOwnToast(mContext, "Clerk Assigned Successfully");
			}
			else
				clerkIdEdt.setError("Enter Valid Clerk Id");			


			break;

		case R.id.Dialog_Tip_Adjust_BTN_ProcessNow:

			tipAmtStr          = tipAmountEdt.getText().toString();	
			String transId     = transIdEdt.getText().toString();			

			if(tipAmtStr.isEmpty()){
				tipAmountEdt.setError("Tip Can't be Blank");
				return;
			}

			if(transId.isEmpty()){
				transIdEdt.setError("Enter Transaction Id");
				return;
			}

			try{
				Float.parseFloat(tipAmtStr);				
			}
			catch(NumberFormatException nx){				
				tipAmountEdt.setError("Please Enter Valid Tip Amount");				
				return;
			}	

			Variables.gateWayTrasId = transId;

			final TSYSGateway tsysGateway = new TSYSGateway(mContext, tipAmtStr, TSYSGateway.TSYS_TIP_ADJUSTMENT);
			tsysGateway.onInterfaceRegister(new OnCallBackForTSYS() {						
				@Override
				public void onTSYSResponse(String responseDate, int requestedCodeReturn) {
					Variables.gateWayTrasId = "";
					TSYSResponseModel response        = tsysGateway.paresTSYSResponse(responseDate);
					ToastUtils.showOwnToast(mContext, response.getResponseMsg());
					if(response.isSuccess()){	

						if(!customerId.isEmpty()){

							clerkCustomer.setTipAmount(tipAmtStr);
							new TipTable(mContext).addInfoInTable(clerkCustomer);

							if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){

								new UsbPR(mContext, new PrinterCallBack() {

									@Override
									public void onDisconnected() {
										dismiss();
									}

									@Override
									public void onConnected(BasePR pPrinter) {
										PrintExtraReceipt.onPrintTipReceipt(clerkCustomer,transIdEdt.getText().toString(), pPrinter);
										dismiss();
									}
								}).onConnectionStart();
								return;
							}

							if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
								PrintExtraReceipt.onPrintTipReceipt(clerkCustomer,transIdEdt.getText().toString(),gApp.getPrinterBT());
							}
							dismiss();
						}
						else 
							dismiss();
					}
				}
			});
			tsysGateway.doExection();


			break;

		default:
			break;
		}
	}

	private  boolean isLong(String integerString){

		try{
			Long.parseLong(integerString);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private void setCustomerNameCorrespondingToMobileNo(final EditText mobileEditText) {

		String mobileNo = mobileEditText.getText().toString();

		if (isLong(mobileNo)) {
			CustomerTable customerTable  = new CustomerTable(mContext);			
			clerkCustomer                =  customerTable.getSingleInfoFromTableByPhoneNo(mobileNo);

			if(clerkCustomer.isCustomerNotValid())
				setClerkInformation(false,"","");				

			else 
				setClerkInformation(true,clerkCustomer.getCustomerId(),clerkCustomer.getFirstName());
		}
		else
			ToastUtils.showOwnToast(mContext, "Invalid Number");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	@Override
	public void afterTextChanged(Editable s) {
		if(s.length() > 0)
			setCustomerNameCorrespondingToMobileNo(clerkIdEdt);	
		else		
			setClerkInformation(false,"","");	
	}

	/**
	 * @author Shivam Garg 
	 * @param isClerkExist  true if, clerk information found from local database based on phone number otherwise false.
	 * @param clerkId       clerk generated Id (Identification Number also from local database)
	 * @param clerkName     name of the clerk in LD( local Database)
	 */

	private void setClerkInformation(boolean isClerkExist,String clerkId,String clerkName) {
		customerId     = clerkId;
		clerkInfoExist = isClerkExist;
		clerkNameTv.setText(clerkName);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,long arg3) {
		transIdEdt.setText(spinnerArray.get(pos));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {		
	}
}

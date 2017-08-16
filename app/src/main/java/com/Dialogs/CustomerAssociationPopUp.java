package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.posimplicity.R;
import com.utils.Variables;

public class CustomerAssociationPopUp extends BaseDialog implements View.OnClickListener {

	private EditText mobileEditText;
	private TextView customerNameTextView,customerInfoTextView;
	private CustomerModel customerInfo;
	private TextView headerTextTV;
	private Button assignOrderBtn,cancelBtn;
	
	public CustomerAssociationPopUp(Context context, int theme, int width,int height, boolean isOutSideTouch, boolean isCancelable,int layoutId) {

		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mobileEditText         = findViewByIdAndCast(R.id.cusMobbox);		
		customerNameTextView   = findViewByIdAndCast(R.id.custNameTextBox);	
		customerInfoTextView   = findViewByIdAndCast(R.id.customerInfo);
		assignOrderBtn         = findViewByIdAndCast(R.id.assignTranBtn);
		cancelBtn              = findViewByIdAndCast(R.id.cancelAssignDialog);
		headerTextTV           = findViewByIdAndCast(R.id.Dailog_Discount_TV_Header);
		
		assignOrderBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		

		mobileEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {}

			private void setCustomerNameCorrespondingToMobileNo(String mobileNo) {
				
				if(isInteger(mobileNo)){
					customerInfo =  new CustomerTable(mContext).getSingleInfoFromTableByPhoneNo(mobileNo);
					if(customerInfo == null){
						customerNameTextView.setText("CustomerParent Does Not Exist");
						customerInfoTextView.setText("");
						return ;
					}
					if (customerInfo.getCustomerId().isEmpty()) {
						customerNameTextView.setText("CustomerParent Does Not Exist");
						customerInfoTextView.setText("");
					}
					else {
						customerNameTextView.setText(customerInfo.getFullName());
						customerInfoTextView.setText(customerInfo.getPermanantAddress());
					}
				}
			}

			@Override
			public void afterTextChanged(Editable mobileNumber) {

				int length = mobileNumber.toString().length();
				if (length == 4 || length == 9 || length == 10) {
					setCustomerNameCorrespondingToMobileNo(mobileNumber.toString());
				}
				else{
					customerNameTextView.setText("");
					customerInfoTextView.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

		});


		headerTextTV.setText("Enter CustomerParent Mobile Number");
		assignOrderBtn.setText("ASSIGN TRANSACTION TO CUSTOMER");
	}

	@Override
	public <T> T findViewByIdAndCast(int id) {
		return super.findViewByIdAndCast(id);
	}


	private boolean isInteger(String integerString){
		try{
			Long.parseLong(integerString);
			return true;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return false;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.cancelAssignDialog:
			dismiss();

			break;
		case R.id.assignTranBtn:

			if(!customerNameTextView.getText().equals("")){
				if (!customerNameTextView.getText().equals("CustomerParent not Exist") && customerInfo != null ) {
					
					Variables.customerId      = Integer.parseInt(customerInfo.getCustomerId());
					Variables.billToName      = customerInfo.getEmailAddress();
					Variables.customerName    = customerInfo.getFirstName();
					
					dismiss();
				}
				else
					Toast.makeText(getContext(), "Not Assingened To Any CustomerParent", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getContext(), "Enter 10 digit's Number", Toast.LENGTH_SHORT).show();

			break;

		default:
			break;
		}
	}
}

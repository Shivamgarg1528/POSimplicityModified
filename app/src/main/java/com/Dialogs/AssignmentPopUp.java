package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.utils.Variables;
import com.posimplicity.R;

public class AssignmentPopUp extends BaseDialog implements View.OnClickListener {

	private EditText mobileEditText;
	private TextView customerNameTextView;
	private CustomerModel customerInfo;
	private TextView headerTextTV;
	private Button assignOrderBtn,cancelBtn;

	public AssignmentPopUp(Context context, int theme, int width,int height, boolean isOutSideTouch, boolean isCancelable,int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mobileEditText         = findViewByIdAndCast(R.id.cusMobbox);		
		customerNameTextView   = findViewByIdAndCast(R.id.custNameTextBox);	
		assignOrderBtn         = findViewByIdAndCast(R.id.assignTranBtn);
		cancelBtn              = findViewByIdAndCast(R.id.cancelAssignDialog);
		headerTextTV           = findViewByIdAndCast(R.id.Dailog_Discount_TV_Header);

		assignOrderBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

		InputFilter[] filters = new InputFilter[1];
		filters[0]            = new InputFilter.LengthFilter(4); //Filter to 4 characters
		mobileEditText .setFilters(filters);


		mobileEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {

			}
			private  boolean isInteger(String integerString){

				try{
					Integer.parseInt(integerString);
					return true;
				} catch (NumberFormatException nfe) {
					return false;
				}
			}

			private void setCustomerNameCorrespondingToMobileNo(final EditText mobileEditText) {

				String mobileNo = mobileEditText.getText().toString();

				if (isInteger(mobileNo)) {
					CustomerTable customerTable  = new CustomerTable(mContext);
					customerInfo                 =  customerTable.getSingleInfoFromTableByPhoneNo(mobileNo);
					if(customerInfo == null){
						customerNameTextView.setText("Invalid Information");
						return ;
					}

					if (customerInfo.getCustomerId().isEmpty())
						customerNameTextView.setText("Invalid Information");
					else
						customerNameTextView.setText(customerInfo.getFirstName());
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if(arg0.length() > 0)
					setCustomerNameCorrespondingToMobileNo(mobileEditText);
				else
					customerNameTextView.setText("");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

		});

	}

	@Override
	public <T> T findViewByIdAndCast(int id) {
		return super.findViewByIdAndCast(id);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.cancelAssignDialog:
			dismiss();

			break;
		case R.id.assignTranBtn:

			if(!customerNameTextView.getText().toString().isEmpty()){
				if (!customerNameTextView.getText().equals("Invalid Information") && customerInfo != null) {
					Variables.shipToName = customerInfo.getEmailAddress();
					Variables.tableID    = mobileEditText.getText().toString();
					dismiss();
				}
				else
					Toast.makeText(getContext(), "Not Assingened ", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getContext(), "Enter Info First", Toast.LENGTH_SHORT).show();

			break;

		default:
			break;
		}
	}

	public void show(int status) {
		show();

		switch (status) {

		case 0:
			headerTextTV.setText("Enter Clerk ID");
			assignOrderBtn.setText("ASSIGN TRANSACTION TO CLERK");
			break;

		case 1:
			headerTextTV.setText("Enter Table ID");
			assignOrderBtn.setText("ASSIGN TABLE");
			break;

		default:
			break;
		}

	}
}

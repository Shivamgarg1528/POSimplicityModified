package com.Dialogs;

import com.CCardPayment.ManualPayment;
import com.CCardPaymentSplit.ManualPaymentSplit;
import com.posimplicity.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CardInfoDialog extends BaseDialog implements View.OnClickListener{

	public EditText creditCardNumber,expMonthEditText, expYearEditText,nameOnCard,cvv2Number;
	private Button manualPayment,closeBtn;
	private int manualFromSplit;

	public CardInfoDialog(Context context, int theme, int width, int height,boolean isOutSideTouch, boolean isCancelable, int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	public void setData(int manualFromSplit){
		this.manualFromSplit = manualFromSplit;
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		creditCardNumber    = findViewByIdAndCast(R.id.DIALOG_CARD_INFO_EDT_CC_NUMBER); 
		expMonthEditText    = findViewByIdAndCast(R.id.DIALOG_CARD_INFO_EDT_MONTH);
		expYearEditText     = findViewByIdAndCast(R.id.DIALOG_CARD_INFO_EDT_YEAR);
		nameOnCard          = findViewByIdAndCast(R.id.DIALOG_CARD_INFO_EDT_NAME);
		cvv2Number          = findViewByIdAndCast(R.id.DIALOG_CARD_INFO_EDT_Cvv2);
		manualPayment       = findViewByIdAndCast(R.id.DIALOG_CARD_INFO_BTN_SUBMIT);
		closeBtn            = findViewByIdAndCast(R.id.DIALOG_CARD_INFO_BTN_CANCEL);

		manualPayment.setOnClickListener(this);	
		closeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.DIALOG_CARD_INFO_BTN_SUBMIT:
			if(manualFromSplit == 0)
				new ManualPayment(mContext,CardInfoDialog.this).onExectue();
			else if(manualFromSplit == 1)
				new ManualPaymentSplit(mContext, CardInfoDialog.this).onExectue();
			break;

		case R.id.DIALOG_CARD_INFO_BTN_CANCEL:
			dismiss();
			break;

		default:
			break;
		}
	}
}

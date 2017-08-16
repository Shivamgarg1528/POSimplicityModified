package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.utils.CalculateWidthAndHeigth;
import com.utils.ToastUtils;
import com.posimplicity.R;

public class SplitViewDialog extends BaseDialog implements View.OnClickListener{
	
	private NumberPicker selectSeatControl;
	private Button cancelBtn,doneBtn ;
	private int splitBills;
	private float subtotalAmt;
	private String pAYMENT_MODE; 
	private String oRDER_STATUS;
	
	public SplitViewDialog(Context context, int theme, int width,
			int height, boolean isOutSideTouch, boolean isCancelable,
			int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cancelBtn            = findViewByIdAndCast(R.id.cancelOnSeatPopup);
		doneBtn              = findViewByIdAndCast(R.id.doneOnSeatPopup);
		selectSeatControl    = findViewByIdAndCast(R.id.selectSeats);
		doneBtn  .setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		selectSeatControl.setMinValue(1);
		selectSeatControl.setMaxValue(100);
		selectSeatControl.setFocusable(true);
		selectSeatControl.setFocusableInTouchMode(true);
		selectSeatControl.setWrapSelectorWheel(false);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId())
		{
		case R.id.cancelOnSeatPopup: 
			dismiss();			
			break;
			
		case R.id.doneOnSeatPopup: 
			
			splitBills = selectSeatControl.getValue();
			if(splitBills < 1)
				ToastUtils.showOwnToast(mContext, "Bill Can't Be Splited");
			else
			{
				dismiss();
				ToastUtils.showOwnToast(mContext, "Splited SuccessFully");
				int width  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() , 80);
				int height = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 80);
				SplitBillRowsDialog splitBillRowsDialog = new SplitBillRowsDialog(mContext, R.style.myCoolDialog, width, height, false, false, R.layout.list_of_splitted_bill);
				splitBillRowsDialog.setData(splitBills,subtotalAmt,pAYMENT_MODE,oRDER_STATUS);
				splitBillRowsDialog.show();
			}			
			break; 
			
		default:
			break;
		}
	}

	public void setData(float subtotalAmt, String pAYMENT_MODE,String oRDER_STATUS) {
		this.subtotalAmt  = subtotalAmt;
		this.pAYMENT_MODE = pAYMENT_MODE;
		this.oRDER_STATUS = oRDER_STATUS;
	}
}

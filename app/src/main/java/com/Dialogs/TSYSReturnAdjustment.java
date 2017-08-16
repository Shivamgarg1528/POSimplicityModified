package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Beans.TSYSResponseModel;
import com.posimplicity.gateway.TSYSGateway;
import com.posimplicity.gateway.TSYSGateway.OnCallBackForTSYS;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.R;

public class TSYSReturnAdjustment extends BaseDialog implements View.OnClickListener {

	private EditText tipAmountEdt,transIdEdt,tipAmountEdt1;
	private View cancelView;
	private Button processNowBtn;
	private String transAmtStr,tipStr;


	public TSYSReturnAdjustment(Context context, int theme, int width,
			int height, boolean isOutSideTouch, boolean isCancelable,
			int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cancelView    = findViewByIdAndCast(R.id.cancel_tip_adjusment);
		tipAmountEdt  = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Tip_Amount);		
		tipAmountEdt1  = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Tip1_Amount);		
		transIdEdt    = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Trans_Amount);		
		processNowBtn = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_BTN_ProcessNow);		
		cancelView.setOnClickListener(this);
		processNowBtn.setOnClickListener(this);		
	}	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cancel_tip_adjusment:
			dismiss();
			break;

		case R.id.Dialog_Tip_Adjust_BTN_ProcessNow:

			transAmtStr          = tipAmountEdt.getText().toString();	
			tipStr             = tipAmountEdt1.getText().toString();
			String transId     = transIdEdt.getText().toString();

			if(tipStr.isEmpty())
				tipStr = "0.00";

			if(transAmtStr.isEmpty() || transId.isEmpty()){
				ToastUtils.showOwnToast(mContext, "You Need To Fill All Fields First");
				return;
			}

			try{
				Float.parseFloat(transAmtStr);
				Float.parseFloat(tipStr);
			}
			catch(NumberFormatException nx){				
				ToastUtils.showOwnToast(mContext, "Please Enter Valid Tip Amount");				
				return;
			}			
			Variables.gateWayTrasId = transId;

			final TSYSGateway tsysGateway = new TSYSGateway(mContext, transAmtStr, TSYSGateway.TSYS_REFUND, tipStr);
			tsysGateway.onInterfaceRegister(new OnCallBackForTSYS() {						
				@Override
				public void onTSYSResponse(String responseDate, int requestedCodeReturn) {
					Variables.gateWayTrasId = "";
					TSYSResponseModel response        = tsysGateway.paresTSYSResponse(responseDate);
					ToastUtils.showOwnToast(mContext, response.getResponseMsg());
					if(response.isSuccess()){
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
}

package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.posimplicity.service.DejavoDebitCreditOption;
import com.posimplicity.service.DejavoDebitCreditOption.YesNoCallBack;
import com.Fragments.MaintFragmentCCAdmin;
import com.posimplicity.gateway.DejavooGatewayTip;
import com.posimplicity.gateway.DejavooGatewayTipBT;
import com.posimplicity.gateway.DejavooGatewayTipBT.OnOk;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;

public class DejavooAdjustmentDialog extends BaseDialog implements View.OnClickListener, OnOk {


	public static final int TIP_ADJUSMENT = 0x00;
	public static final int RETURN_ADJUSTMENT = 0x01;

	private int optionForTipOrReturn = 0;
	private EditText tipAmountEdt,refNumEdt,ccNumberEdt,authKeyEdt,transAmtEdt;
	private TextView tipTv,refTv,ccNumbTv,authTv;
	private View cancelView;
	private Button processNowBtn;

	public DejavooAdjustmentDialog(Context context, int theme, int width,
			int height, boolean isOutSideTouch, boolean isCancelable,
			int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cancelView    = findViewByIdAndCast(R.id.cancel_tip_adjusment);
		tipAmountEdt  = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Tip_Amount);
		refNumEdt     = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Ref_Id);
		ccNumberEdt   = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_CC_Digit_4);
		transAmtEdt   = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_Trans_Amount);
		authKeyEdt    = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_EDT_AuthKey);
		processNowBtn = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_BTN_ProcessNow);
		tipTv         = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_TV_TipAmt_textView);
		refTv         = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_TV_Ref_Id_textView);
		ccNumbTv      = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_TV_4digit_textView);
		authTv        = findViewByIdAndCast(R.id.Dialog_Tip_Adjust_TV_AuthKey_textView);

		cancelView.setOnClickListener(this);
		processNowBtn.setOnClickListener(this);

		switch (optionForTipOrReturn) {

		case RETURN_ADJUSTMENT:
			tipTv.setVisibility(View.GONE);
			refTv.setVisibility(View.GONE);
			ccNumbTv.setVisibility(View.GONE);
			authTv.setVisibility(View.GONE);
			tipAmountEdt.setVisibility(View.GONE);
			refNumEdt.setVisibility(View.GONE);
			ccNumberEdt.setVisibility(View.GONE);
			authKeyEdt.setVisibility(View.GONE);
			((TextView)findViewByIdAndCast(R.id.title)).setText("Return Adjustment");
			break;

		default:
			break;
		}
	}

	public void sendData(int item) {
		optionForTipOrReturn = item;
	}

	@Override
	public void onClick(View v) {
		int toastvalue = 0;
		switch (v.getId()) {
		case R.id.cancel_tip_adjusment:
			dismiss();
			break;

		case R.id.Dialog_Tip_Adjust_BTN_ProcessNow:


			if(optionForTipOrReturn == RETURN_ADJUSTMENT){

				final String transAmt    = transAmtEdt.getText().toString();
				if(transAmt.isEmpty()){
					ToastUtils.showOwnToast(mContext, "You Need To Fill Transaction(Return) Amount");
					return;
				}
				try{
					Float.parseFloat(transAmt);
				}
				catch(NumberFormatException nx){
					ToastUtils.showOwnToast(mContext, "Please Enter Valid Transaction(Return) Amount");
					return;
				}

				final String ipAddress         = MyPreferences.getMyPreference(DEJAVOO_IP_ADDRESS, mContext);
				long gatewayUsedPos            = MyPreferences.getLongPreference(GATEWAY_USED_POSITION, mContext);

				if(gatewayUsedPos == MaintFragmentCCAdmin.DEJAVO_PAY_ID){
					final long selectedOption  = MyPreferences.getLongPreference(DEJAVO_OPTION, mContext);
					if(selectedOption >= 0){
						if(DejavoDebitCreditOption.isDejavoPromptEnable(mContext)){
							DejavoDebitCreditOption.showDejavoOptionListDialog(new YesNoCallBack() {

								@Override
								public void onYes(String string) {
									if(MyPreferences.getBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext)){
										DejavooGatewayTipBT dejavooGatewayTipBT = new DejavooGatewayTipBT(mContext, String.valueOf(optionForTipOrReturn), "0", "0", "0", "0", transAmt,DejavooAdjustmentDialog.this);
										dejavooGatewayTipBT.execute();
									}
									else{
										DejavooGatewayTip dejavooGatewayTip = new DejavooGatewayTip(mContext, ipAddress, optionForTipOrReturn, "0","0","0","0",transAmt,DejavooAdjustmentDialog.this);
										dejavooGatewayTip.execute();
									}
								}

								@Override
								public void onNo() {}

							}, mContext);
						}
						else{
							if(MyPreferences.getBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext)){
								DejavooGatewayTipBT dejavooGatewayTipBT = new DejavooGatewayTipBT(mContext, String.valueOf(optionForTipOrReturn), "0", "0", "0", "0", transAmt,DejavooAdjustmentDialog.this);
								dejavooGatewayTipBT.execute();
							}
							else{
								DejavooGatewayTip dejavooGatewayTip = new DejavooGatewayTip(mContext, ipAddress, optionForTipOrReturn, "0","0","0","0",transAmt,this);
								dejavooGatewayTip.execute();
							}
						}
					}
					else
						ToastUtils.showOwnToast(mContext, "Select Any Option For Dejavoo");
				}
				else
					ToastUtils.showOwnToast(mContext, "Enbale Dejavoo GateWay From App Admin");

			}
			else{

				String transAmt    = transAmtEdt.getText().toString();
				String tipAmtStr   = tipAmountEdt.getText().toString();
				String refNumStr   = refNumEdt.getText().toString();
				String ccNumbStr   = ccNumberEdt.getText().toString();
				String autKeyStr   = authKeyEdt.getText().toString();

				if(tipAmtStr.isEmpty() || refNumStr.isEmpty() || ccNumbStr.isEmpty() || autKeyStr.isEmpty() || transAmt.isEmpty()){
					ToastUtils.showOwnToast(mContext, "You Need To Fill All Fields First");
					return;
				}

				try{
					Float.parseFloat(tipAmtStr);
					toastvalue++;
					Float.parseFloat(transAmt);
				}
				catch(NumberFormatException nx){
					if(toastvalue == 0)
						ToastUtils.showOwnToast(mContext, "Please Enter Valid Tip Amount");
					else if(toastvalue == 1)
						ToastUtils.showOwnToast(mContext, "Please Enter Valid Trans Amount");
					return;
				}

				String ipAddress         = MyPreferences.getMyPreference(DEJAVOO_IP_ADDRESS, mContext);
				long gatewayUsedPos      = MyPreferences.getLongPreference(GATEWAY_USED_POSITION, mContext);

				if(gatewayUsedPos == MaintFragmentCCAdmin.DEJAVO_PAY_ID){
					long selectedOption  = MyPreferences.getLongPreference(DEJAVO_OPTION, mContext);
					if(selectedOption >= 0){
						if(MyPreferences.getBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext)){
							DejavooGatewayTipBT dejavooGatewayTipBT = new DejavooGatewayTipBT(mContext, String.valueOf(optionForTipOrReturn), autKeyStr, refNumStr, ccNumbStr, tipAmtStr, transAmt,DejavooAdjustmentDialog.this);
							dejavooGatewayTipBT.execute();
						}
						else{
							DejavooGatewayTip dejavooGatewayTip = new DejavooGatewayTip(mContext, ipAddress, optionForTipOrReturn, autKeyStr, refNumStr, ccNumbStr, tipAmtStr,transAmt,this);
							dejavooGatewayTip.execute();
						}
					}
					else
						ToastUtils.showOwnToast(mContext, "Select Any Option For Dejavoo");
				}
				else
					ToastUtils.showOwnToast(mContext, "Enbale Dejavoo GateWay From App Admin");


			}
			break;
		default:
			break;

		}
	}

	@Override
	public void onOk() {
		dismiss();
	}
}


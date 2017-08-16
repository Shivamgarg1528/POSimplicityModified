package com.Dialogs;

import com.dialog.ShowDecilineDialog;
import com.CCardPaymentSplit.CallSwipeGateWaySplit;
import com.CCardPaymentSplit.OfflinePaymentSplit;
import com.adapter.SplitBillAdapter;
import com.CustomControls.ToastHelper;
import com.Fragments.MaintFragmentCCAdmin;
import com.posimplicity.gateway.DejavoSplitGateway;
import com.posimplicity.gateway.DejavoSplitGatewayBt;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CalculateWidthAndHeigth;
import com.utils.CheckCardInfo;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CCDialogForSplitBill extends BaseDialog implements TextWatcher, View.OnClickListener {

	public EditText infoOnSwipeEditText,tipAmtEditText;
	public Button useManualTrans,offlineTransc,dejavoBtn;
	private Toast pleaseWaitToast;
	public CheckCardInfo ccCardInfo;
	public Handler handler;
	public View cancelView;
	private String cardInformation;

	public CCDialogForSplitBill(Context context, int theme, int width,int height, boolean isOutSideTouch, boolean isCancelable,int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler      = new Handler();
		useManualTrans      = findViewByIdAndCast(R.id.chooseManualTransaction);
		offlineTransc       = findViewByIdAndCast(R.id.chooseOfflineTransaction);
		dejavoBtn           = findViewByIdAndCast(R.id.dejavooo);
		infoOnSwipeEditText = findViewByIdAndCast(R.id.cardinfoedittext);  // EditText
		cancelView          = findViewByIdAndCast(R.id.btnCancelCC);
		tipAmtEditText      = findViewByIdAndCast(R.id.tipAmtEditText);

		customToast();
		setVisibility();
		showCcSwipeMessage();

		infoOnSwipeEditText.addTextChangedListener(this);
		useManualTrans.setOnClickListener(this);
		offlineTransc.setOnClickListener(this);
		dejavoBtn.setOnClickListener(this);
		cancelView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		try{
			switch (v.getId()) {

			case R.id.chooseManualTransaction:
				dismiss();
				int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(),  60);
				int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 60);
				CardInfoDialog cardInfoDialog = new CardInfoDialog(mContext, R.style.myCoolDialog, width, height, false, false, R.layout.dialog_card_info);
				cardInfoDialog.setData(1);
				cardInfoDialog.show();

				break;

			case R.id.chooseOfflineTransaction:
				dismiss();
				new OfflinePaymentSplit(mContext).onExecute();

				break;

			case R.id.btnCancelCC:
				dismiss();
				break;

			case R.id.dejavooo:

				String ipAddress         = MyPreferences.getMyPreference(DEJAVOO_IP_ADDRESS, mContext);
				long gatewayUsedPos      = MyPreferences.getLongPreference(GATEWAY_USED_POSITION, mContext);

				if(gatewayUsedPos == MaintFragmentCCAdmin.DEJAVO_PAY_ID){				
					long selectedOption  = MyPreferences.getLongPreference(DEJAVO_OPTION, mContext);
					if(selectedOption >= 0){

						float tipAmount = 0.0f;
						try{
							tipAmount = Float.parseFloat(tipAmtEditText.getText().toString());
						}
						catch(Exception ex){
							ex.printStackTrace();
							tipAmount = 0.0f;
							ToastUtils.showOwnToast(mContext, "Invalid Tip Amount");
							return;
						}
						if(!MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext))
							new DejavoSplitGateway(mContext, ipAddress, SplitBillAdapter.amountToPaid, tipAmount, selectedOption,this).execute();
						else
						{
							new DejavoSplitGatewayBt(mContext, "", SplitBillAdapter.amountToPaid, tipAmount, selectedOption,this).execute();

						}
					}
					else
						ToastUtils.showOwnToast(mContext, "Select Any Option For Dejavoo");
				}
				else
					ToastUtils.showOwnToast(mContext, "Enbale Dejavoo GateWay From App Admin");

				break;

			default:
				break;
			}

		}
		catch (Exception e) {
			ShowDecilineDialog.onSHowErroeMsg(mContext,e);
		}


	}

	public void onShowCustomToast(){
		pleaseWaitToast.show();
	}

	public void onHideCustomToast(){
		pleaseWaitToast.cancel();
	}

	private void customToast() {
		pleaseWaitToast = Toast.makeText(mContext, "Please Wait...",Toast.LENGTH_LONG);
		pleaseWaitToast.setGravity(Gravity.CENTER, 0, 0);
	}

	public  void showCcSwipeMessage() {
		long storedOption = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION, mContext);

		if(storedOption >= 0){
			if(!(storedOption == 5)){
				ToastHelper.showCCSwipeToast(mContext);
			}
		}
	}

	Runnable delayedAction = new Runnable() {

		@Override
		public void run() {

			pleaseWaitToast.cancel();
			dismiss();

			Boolean isInternetPresent = InternetConnectionDetector.isInternetAvailable(mContext);
			if (isInternetPresent) {
				cardInformation = infoOnSwipeEditText.getText().toString();
				if (MyPreferences.getBooleanPrefrences(ENCRYPTED_PAY_ENABLE,mContext)) {
					long gatewayUsed = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION,mContext);
					if (gatewayUsed == 0) {
						new CallSwipeGateWaySplit(mContext,ccCardInfo).execute();
					}
				}
				else {
					boolean correct = checkCcCredentials();
					if (correct) {
						new CallSwipeGateWaySplit(mContext,ccCardInfo).execute();
					}
					else {
						clearCardInfoFieldAndSetFocusAgain();
						ShowDecilineDialog.onWrongInput(mContext);
					}
				}
			} else
				Toast.makeText(mContext, "No InternetConnection",Toast.LENGTH_SHORT).show();
		}
	};

	public void clearCardInfoFieldAndSetFocusAgain(){

		infoOnSwipeEditText.requestFocus(R.id.cardinfoedittext);
		infoOnSwipeEditText.setText("");
	}

	protected boolean checkCcCredentials() {

		ccCardInfo = new CheckCardInfo(mContext, cardInformation);
		return ccCardInfo.validationOfCard();

	}
	private void setVisibility() {

		long storedOption = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION, mContext);
		if(storedOption >= 0){
			offlineTransc.setVisibility(View.GONE);
			if(storedOption == MaintFragmentCCAdmin.DEJAVO_PAY_ID){
				useManualTrans.setVisibility(View.GONE);
				dejavoBtn.setVisibility(View.VISIBLE);
			}
			else
			{
				useManualTrans.setVisibility(View.VISIBLE);
				dejavoBtn.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	@Override
	public void afterTextChanged(Editable s) {


		if (s.length() > 0) {
			if (s.length() == 1){
				pleaseWaitToast.show();
			}

			if (delayedAction != null)
				handler.removeCallbacks(delayedAction);

			handler.postDelayed(delayedAction, 2000);
		}	

	}
}

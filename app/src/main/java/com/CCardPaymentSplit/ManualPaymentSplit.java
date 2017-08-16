package com.CCardPaymentSplit;

import com.Dialogs.CardInfoDialog;
import com.Fragments.MaintFragmentCCAdmin;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CheckCardInfo;

import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.ToastUtils;

import android.content.Context;

public class ManualPaymentSplit implements PrefrenceKeyConst{

	private Context mContext;
	private CardInfoDialog ccInfoDialog;

	public ManualPaymentSplit(Context mContext, CardInfoDialog cardInfoDialog) {
		super();
		this.mContext      = mContext;
		this.ccInfoDialog  = cardInfoDialog;
	}

	public void onExectue(){
		onManualPayment();
	}

	private void onManualPayment() {

		Boolean isInternetPresent = InternetConnectionDetector.isInternetAvailable(mContext);
		if(isInternetPresent) {

			String creditCard = ccInfoDialog.creditCardNumber.getText().toString();
			String expmonth   = ccInfoDialog.expMonthEditText.getText().toString();
			String expyear    = ccInfoDialog.expYearEditText.getText().toString();
			String name       = ccInfoDialog.nameOnCard.getText().toString();
			String cvv2Number = ccInfoDialog.cvv2Number.getText().toString();
			boolean isCvv2NumberIsRequired = false;
			long gatewayTypeId = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION,mContext);

			if(gatewayTypeId == MaintFragmentCCAdmin.TSYS_PAY_ID) {
				isCvv2NumberIsRequired = true;
			}

			if(isCvv2NumberIsRequired && cvv2Number.isEmpty()){
				ToastUtils.showOwnToast(mContext, "Please Provide Card Info First");
				return;
			}

			try{
				if((creditCard.length() >= 14 && creditCard.length() < 17) && expmonth.length() == 2 && expyear.length() == 2 ){

					Long.parseLong(creditCard);
					Long.parseLong(expmonth);
					Long.parseLong(expyear);

					CheckCardInfo ccCardInfo = new CheckCardInfo();
					ccCardInfo.setCardNumber(creditCard);
					ccCardInfo.setCardExpiryMonth(expmonth);
					ccCardInfo.setCardExpiryYear(expyear);
					ccCardInfo.setCardHolderName(name);
					ccCardInfo.setCvv2Number(cvv2Number);
					ccInfoDialog.dismiss();
					new CallKeyedGateWaySplit(mContext, creditCard, expmonth, expyear, name, ccCardInfo,cvv2Number).execute();

				}
				else
					ToastUtils.showOwnToast(mContext, "Please Provide Card Info First");
			}
			catch(Exception ex){
				ToastUtils.showOwnToast(mContext, "Please Provide Valid Card Info");
				return ;
			}
		}
		else
			ToastUtils.showOwnToast(mContext, "No InternetConnection");
	}
}

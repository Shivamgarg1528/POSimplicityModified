package com.CCardPaymentSplit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.dialog.ShowDecilineDialog;
import com.Beans.TSYSResponseModel;
import com.adapter.SplitBillAdapter;
import com.CustomControls.ProgressHUD;
import com.posimplicity.gateway.BridgePayGateway;
import com.posimplicity.gateway.PlugNPayGateway;
import com.posimplicity.gateway.ProPayGateway;
import com.posimplicity.gateway.TSYSGateway;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CreditCardHelper;
import com.utils.CheckCardInfo;

import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.Variables;

public class CallKeyedGateWaySplit extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener,PrefrenceKeyConst{

	private Context mContext;
	private ProgressHUD progressHUD;
	private String cardNumber,cardExpMonth,cardExpYear,personName,cvv2Number;
	private float amounttoPaid;
	public CheckCardInfo ccCardInfo;
	private TSYSResponseModel responseModel;

	public CallKeyedGateWaySplit(Context mContext, String cardNumber,String cardExpMonth, String cardExpYear, String personName, CheckCardInfo ccCardInfo, String cvv2Number) {
		super();
		this.mContext = mContext;
		this.cardNumber = cardNumber;
		this.cardExpMonth = cardExpMonth;
		this.cardExpYear = cardExpYear;
		this.personName = personName;
		this.cvv2Number = cvv2Number;
		this.amounttoPaid = SplitBillAdapter.amountToPaid;
		this.ccCardInfo = ccCardInfo;
	}

	@Override
	protected void onPreExecute() {
		progressHUD = ProgressHUD.show(mContext, "Processing...", true, true, this);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		boolean result = onCallGateway();
		return result;
	}	

	@Override
	public void onCancel(DialogInterface dialog) {
		progressHUD.dismiss();
	}

	@Override
	protected void onPostExecute(Boolean result) {

		progressHUD.dismiss();
		if(responseModel != null){
			ToastUtils.showOwnToast(mContext, responseModel.getResponseMsg());
		}
		
		if (result) {
			Variables.ccNumber     = cardNumber;
			Variables.CCHOlderName = personName;
			Variables.CcExpiryDate = cardExpMonth;
			Variables.CcExpiryYear = cardExpYear;
			Variables.Cardtype     = new CreditCardHelper().getCCTypeShort(cardNumber);
			new OfflinePaymentSplit(mContext).onExecute();
		}
		else
			ShowDecilineDialog.onShow(mContext);

	}

	public boolean onCallGateway()
	{
		boolean successful = false;		
		long gatewayTypeId = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION,mContext);

		if(gatewayTypeId == 0) {

			String plugPayId         = MyPreferences.getMyPreference(PLUG_PAY_ID, 	mContext);
			PlugNPayGateway plugNpay = new PlugNPayGateway(mContext);
			successful               = plugNpay.plugNPayGatewayPayment(plugPayId,""+amounttoPaid,cardExpMonth+"/"+cardExpYear, cardNumber,personName);

		}
		else if(gatewayTypeId == 1)
		{
			String username = MyPreferences.getMyPreference(BRIDGE_GATEWAY_USERNAME,mContext);
			String password = MyPreferences.getMyPreference(BRIDGE_GATEWAY_PASSWORD,mContext);
			BridgePayGateway bridgePayGateway= new BridgePayGateway(mContext);
			successful     = bridgePayGateway.bridgeGatewayPayment(username, password,cardNumber,""+amounttoPaid,cardExpMonth,cardExpYear,personName, "");

		}
		else if(gatewayTypeId == 3){

			TSYSGateway tsysGateway  = new TSYSGateway(mContext, cardNumber, cardExpMonth+"/"+cardExpYear, personName, ""+amounttoPaid, TSYSGateway.TSYS_KEYED_SALES,cvv2Number,"0.00");
			if(tsysGateway.isTransactionKeyExist()){
				String response          = tsysGateway.requestDataOnTSYSServer();
				responseModel            = tsysGateway.paresTSYSResponse(response);
				successful               = responseModel.isSuccess();
			}
			else 
				successful = false;
		}
		else if(gatewayTypeId == 4){
			successful = new ProPayGateway(mContext).onKeyedTransaction(""+amounttoPaid, cardNumber, cardExpMonth+cardExpYear, personName);
		}
		else {
			successful = false;
		}
		return successful;
	}
}

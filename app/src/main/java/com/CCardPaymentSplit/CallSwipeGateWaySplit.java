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
import com.posimplicity.gateway.TSYSGateway;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CheckCardInfo;
import com.utils.MyPreferences;
import com.utils.ToastUtils;

public class CallSwipeGateWaySplit extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener ,PrefrenceKeyConst{

	private Context mContext;
	private ProgressHUD progressHUD;
	private String cardNumber,cardExpMonth,cardExpYear,personName,cardInformation,magData;
	protected CheckCardInfo ccObject;
	private float payAmount;
	private TSYSResponseModel responseModel;

	public CallSwipeGateWaySplit(Context mContext,CheckCardInfo ccObject) {
		super();
		this.mContext        = mContext;
		this.ccObject        = ccObject;
		this.cardNumber      = ccObject.getCardNumber();
		this.cardExpMonth    = ccObject.getCardExpiryMonth();
		this.cardExpYear     = ccObject.getCardExpiryYear();
		this.personName      = ccObject.getCardHolderName();
		this.cardInformation = ccObject.getAllCardData();
		this.magData         = ccObject.getMagData();
		this.payAmount       = SplitBillAdapter.amountToPaid;
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
			new OfflinePaymentSplit(mContext).onExecute();
		}
		else {
			ShowDecilineDialog.onShow(mContext);
		}
	}

	public boolean onCallGateway()
	{
		boolean successful = false;		
		long gatewayTypeId = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION,mContext);


		if(gatewayTypeId == 0) {
			PlugNPayGateway payGateway = new PlugNPayGateway(mContext);
			if(MyPreferences.getBooleanPrefrences(ENCRYPTED_PAY_ENABLE,mContext))
				successful = payGateway.onPlugNPayEncryptedData(cardInformation, ""+payAmount);
			else
				successful = payGateway.plugNPayCcSwipePayment(cardInformation, ""+payAmount);
		}
		else if(gatewayTypeId == 1)
		{
			String username = MyPreferences.getMyPreference(BRIDGE_GATEWAY_USERNAME,mContext);
			String password = MyPreferences.getMyPreference(BRIDGE_GATEWAY_PASSWORD,mContext);				
			BridgePayGateway bridgePayGateway = new BridgePayGateway(mContext);				
			successful = bridgePayGateway.bridgeGatewayPayment(username, password,cardNumber,""+payAmount,cardExpMonth,cardExpYear,personName,magData);
		}

		else if(gatewayTypeId == 3){

			TSYSGateway tsysGateway  = new TSYSGateway(mContext, ""+payAmount, cardInformation, TSYSGateway.TSYS_SWIPE_SALES);
			if(tsysGateway.isTransactionKeyExist()){
				String response          = tsysGateway.requestDataOnTSYSServer();
				responseModel            = tsysGateway.paresTSYSResponse(response);
				successful               = responseModel.isSuccess();
			}
			else 
				successful = false;
		}
		else{
			successful = false;
		}
		return successful;
	}
}

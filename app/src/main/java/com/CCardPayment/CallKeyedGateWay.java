package com.CCardPayment;

import java.util.Iterator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.dialog.ShowDecilineDialog;
import com.Beans.TSYSResponseModel;
import com.CustomControls.ProgressHUD;
import com.Fragments.CreditFragment;
import com.posimplicity.gateway.BridgePayGateway;
import com.posimplicity.gateway.PlugNPayGateway;
import com.posimplicity.gateway.ProPayGateway;
import com.posimplicity.gateway.TSYSGateway;
import com.JsonPakage.JSONObject;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CreditCardHelper;
import com.utils.CheckCardInfo;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.Variables;

public class CallKeyedGateWay extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener,PrefrenceKeyConst{

	private Context mContext;
	private ProgressHUD progressHUD;
	private String cardNumber,cardExpMonth,cardExpYear,personName,cvv2Num;
	private CreditFragment ccActivity;
	private float amounttoPaid,tipAmount;
	public CheckCardInfo ccCardInfo;
	private TSYSResponseModel responseModel;

	public CallKeyedGateWay(Context mContext) {
		super();
		this.mContext     = mContext;
		this.ccActivity   = CreditFragment.localInstanceOFCCFragment;
		this.ccCardInfo   = ccActivity.ccCardInfo;
		this.cardNumber   = ccCardInfo.getCardNumber();
		this.cardExpMonth = ccCardInfo.getCardExpiryMonth();
		this.cardExpYear  = ccCardInfo.getCardExpiryYear();
		this.personName   = ccCardInfo.getCardHolderName();
		this.cvv2Num      = ccCardInfo.getCvv2Number();
		this.amounttoPaid = ccActivity.ccSubTotalAmt;
		this.tipAmount    = ccActivity.tipAmount;
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
			new OfflinePayment(mContext).onExecute();
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

			TSYSGateway tsysGateway  = new TSYSGateway(mContext, cardNumber, cardExpMonth+"/"+cardExpYear, personName, ""+amounttoPaid, TSYSGateway.TSYS_KEYED_SALES,cvv2Num,""+tipAmount);
			if(tsysGateway.isTransactionKeyExist()){
				String response          = tsysGateway.requestDataOnTSYSServer();
				responseModel            = tsysGateway.paresTSYSResponse(response);
				successful               = responseModel.isSuccess();
				
				if(successful)
					getPartialAmount(responseModel.getTsysResponse());
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

	public void getPartialAmount(String response){
		try{
			JSONObject resultObj         = new JSONObject(response);
			Iterator<String>    keyss    = resultObj.keys();
			if(keyss.hasNext()){
				String string            = (String) keyss.next();
				JSONObject innJsonObject = resultObj.getJSONObject(string);
				String responseMsg       = JSONObJValidator.stringTagValidate(innJsonObject, "responseMessage", "");
				if(responseMsg.equalsIgnoreCase("Partially Approved")){
					ccActivity.ccSubTotalAmt = Float.parseFloat(JSONObJValidator.stringTagValidate(innJsonObject, "processedAmount", "0.00"))/100.0f;
				}			
			}
		}
		catch(Exception ex){ 
			ex.printStackTrace();			
		}		
	}
}

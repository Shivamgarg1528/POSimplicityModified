package com.CCardPayment;

import java.util.Iterator;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.dialog.ShowDecilineDialog;
import com.Beans.TSYSResponseModel;
import com.Fragments.CreditFragment;
import com.Fragments.MaintFragmentCCAdmin;
import com.posimplicity.gateway.BridgePayGateway;
import com.posimplicity.gateway.PlugNPayGateway;
import com.posimplicity.gateway.TSYSGateway;
import com.JsonPakage.JSONObject;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CheckCardInfo;
import com.utils.EncryptedCardInfo;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.Variables;

public class CallSwipeGateWay extends AsyncTask<Void, Integer, Boolean> implements PrefrenceKeyConst {

	private Context mContext;
	private ProgressDialog progressHUD;
	private String cardNumber,cardExpMonth,cardExpYear,personName,cardInformation,magData;
	private CreditFragment creditFragment;
	private float payAmount;
	protected CheckCardInfo ccObject;
	private TSYSResponseModel responseModel;
	private long gatewayUsedPosition = -1;
	private boolean encryptionEnable = false;

	public CallSwipeGateWay(Context mContext,CreditFragment creditFragment,CheckCardInfo ccObject) {
		super();

		this.mContext        = mContext;
		this.ccObject        = ccObject;
		this.cardNumber      = ccObject.getCardNumber();
		this.cardExpMonth    = ccObject.getCardExpiryMonth();
		this.cardExpYear     = ccObject.getCardExpiryYear();
		this.personName      = ccObject.getCardHolderName();
		this.cardInformation = ccObject.getAllCardData();
		this.magData         = ccObject.getMagData();
		this.creditFragment  = creditFragment;
		this.payAmount       = creditFragment.ccSubTotalAmt;
		this.gatewayUsedPosition = creditFragment.gatewayUsed;
		this.encryptionEnable    = creditFragment.encryptionEnabled;
	}

	public CallSwipeGateWay(Context mContext, String cardInformation,
			CreditFragment creditFragment) {
		super();
		this.mContext        = mContext;
		this.cardInformation = cardInformation;
		this.creditFragment  = creditFragment;
		this.payAmount       = creditFragment.ccSubTotalAmt;
		this.gatewayUsedPosition = creditFragment.gatewayUsed;
		this.encryptionEnable    = creditFragment.encryptionEnabled;
	}

	@Override
	protected void onPreExecute() {
		progressHUD = ProgressDialog.show(mContext,"", "Processing...", true, true);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		boolean result = onCallGateway();		
		return result;
	}	

	@Override
	protected void onPostExecute(Boolean result) {

		progressHUD.dismiss();		

		if(responseModel != null){
			ToastUtils.showOwnToast(mContext, responseModel.getResponseMsg());
		}

		if (result) {

			if(gatewayUsedPosition == MaintFragmentCCAdmin.DEJAVO_PAY_ID  && Variables.isDejavooSuccess)
				new OfflinePayment(mContext).onExecute();
			else if(gatewayUsedPosition != MaintFragmentCCAdmin.DEJAVO_PAY_ID  )
				new OfflinePayment(mContext).onExecute();

		}
		else {
			creditFragment.clearCardInfoFieldAndSetFocusAgain();
			ShowDecilineDialog.onShow(mContext);
		}
	}

	public boolean onCallGateway()
	{
		boolean successful = false;		

		if(gatewayUsedPosition == MaintFragmentCCAdmin.PLUG_N_PAY_ID) {

			PlugNPayGateway payGateway = new PlugNPayGateway(mContext);			
			if(encryptionEnable)
				successful = payGateway.onPlugNPayEncryptedData(cardInformation, ""+payAmount);
			else
				successful = payGateway.plugNPayCcSwipePayment(cardInformation, ""+payAmount);
		}

		else if(gatewayUsedPosition == MaintFragmentCCAdmin.BRIDGE_PAY_ID)
		{
			if(!encryptionEnable){
				String username = MyPreferences.getMyPreference(BRIDGE_GATEWAY_USERNAME,mContext);
				String password = MyPreferences.getMyPreference(BRIDGE_GATEWAY_PASSWORD,mContext);				
				BridgePayGateway bridgePayGateway = new BridgePayGateway(mContext);				
				successful = bridgePayGateway.bridgeGatewayPayment(username, password,cardNumber,""+payAmount,cardExpMonth,cardExpYear,personName,magData);

			}
		}
		else if(gatewayUsedPosition == MaintFragmentCCAdmin.TSYS_PAY_ID){
			if(encryptionEnable){
				
				EncryptedCardInfo encryptedCardInfo = new EncryptedCardInfo(mContext, cardInformation);
				boolean cardInfoValid               = encryptedCardInfo.parseData();
				if(cardInfoValid){
					TSYSGateway tsysGateway         = new TSYSGateway(mContext, ""+payAmount, encryptedCardInfo, TSYSGateway.TSYS_SWIPE_SALES_WITH_ENCRYPTION);

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
				else 
					successful = false;		
			}
			else{
				TSYSGateway tsysGateway  = new TSYSGateway(mContext, ""+payAmount, cardInformation, TSYSGateway.TSYS_SWIPE_SALES);
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
		}
		else{
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
					creditFragment.ccSubTotalAmt = Float.parseFloat(JSONObJValidator.stringTagValidate(innJsonObject, "processedAmount", "0.00"))/100.0f;
				}			
			}
		}
		catch(Exception ex){ 
			ex.printStackTrace();			
		}		
	}
}

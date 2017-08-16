package com.posimplicity.gateway;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.dialog.ShowDecilineDialog;
import com.posimplicity.service.BTDejavooService;
import com.CCardPayment.OfflinePayment;
import com.CustomControls.ProgressHUD;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.GenerateNewTransactionNo;
import com.utils.POSApp;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;

public class DejavoGatewayBT extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener ,PrefrenceKeyConst{

	private Context mContext;
	private StringBuilder sb;
	private String amountToPay;
	private String deviceId;
	private String transId;
	private ProgressHUD progressHUD;
	public String response = "";
	private int dejavoOption;

	public DejavoGatewayBT(Context mContext,float amountToPay, long selectedOption) {
		super();
		this.mContext        = mContext;
		this.deviceId        = MyPreferences.getMyPreference(DEVICE_ID, mContext);
		this.amountToPay     = MyStringFormat.formatWith2DecimalPlaces(amountToPay);
		this.transId         = GenerateNewTransactionNo.onNewTransactionNoForDejavoo(mContext);
		this.dejavoOption    = (int) selectedOption;
	} 

	@Override
	public void onCancel(DialogInterface dialog) {
		progressHUD.dismiss();
	}

	@Override
	protected void onPreExecute() {
		progressHUD = ProgressHUD.show(mContext, "Processing...", true, false, this);
	}


	@Override
	protected Boolean doInBackground(Void... params) {
		createXMLData(dejavoOption);
		executeApiWithXmlData();
		while (response.isEmpty()) {
			try{
				Thread.sleep(3000);
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		try{	
			progressHUD.dismiss();
			System.out.println("Response -> "+ response);

			if(!TextUtils.isEmpty(response) && !response.equalsIgnoreCase("Exception") && !response.equalsIgnoreCase("Service Unavailable")){

				DejavooParseService dejavooParseService = new DejavooParseService(response);
				if(dejavooParseService.parseData(mContext)){
					MyPreferences.setMyPreference(PrefrenceKeyConst.DEJAVOO_RESPONSE, "", mContext);
					MyPreferences.setMyPreference(PrefrenceKeyConst.DEJAVOO_RESPONSE, response, mContext);
					new OfflinePayment(mContext).onExecute();					
				}
				else
					ShowDecilineDialog.onShow(mContext);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			ShowDecilineDialog.onShow(mContext);
		}
	}

	private void createXMLData (int dejavoOption2){
		MyPreferences.setMyPreference(PrefrenceKeyConst.DEJAVOO_RESPONSE, "", mContext);
		sb = new StringBuilder();
		sb.append("<request>");
		sb.append("<PaymentType>"+MyPreferences.getMyPreference(PrefrenceKeyConst.DEJAVO_PAYMENT_TYPE, mContext)+"</PaymentType>");
		sb.append("<RegisterId>" +deviceId+"</RegisterId>");
		sb.append("<InvNum>"+transId+"</InvNum>");
		sb.append("<RefId>" +transId+"</RefId>");
		sb.append("<Amount>"+amountToPay+"</Amount>");
		sb.append("<TransType>"+"Sale"+"</TransType>");	
		sb.append("</request>");
		//sb.append("<CardData>"+"EntType=2,PAN=5454545454545454,Track1=,Track2=;5454545454545454=16041015432112345601?=,ExpDate=1220,AVS=,ZIP=12345,CVV=123,CardPresent=False"+"</CardData>");
	}

	private void executeApiWithXmlData (){
		try {
			final String request = "TerminalTransaction=" + sb.toString() + "\0";
			System.out.println("Request -> " + request);
			BTDejavooService.getData(new BTDejavooService.GetReceivedData() {
				@Override
				public void onData(String st) {
					response = st;
				}
			});
			POSApp.getInstance().bluetoothConnector.sendDatOverTerminal(request);
		}
		catch(Exception ex){
			ex.printStackTrace();
			response = "Exception";
		}
	}
}


package com.posimplicity.gateway;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.dialog.ShowDecilineDialog;
import com.posimplicity.service.BTDejavooService;
import com.CustomControls.ProgressHUD;
import com.CustomControls.ToastHelper;
import com.Dialogs.DejavooAdjustmentDialog;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.GenerateNewTransactionNo;
import com.utils.POSApp;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class DejavooGatewayTipBT extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener ,PrefrenceKeyConst{

	private Context mContext;
	private StringBuilder sb;
	private String tipAmount;
	private String deviceId;
	private String transId;
	private String authkey;
	private String acntLast4;
	private ProgressHUD progressHUD;
	public String response = "";
	private XMLParser parser; 
	private int dejavoTipOption;
	private String amountToPaid;
	static final String RESPONSE   = "response"; // parent node
	static final String MESSAGE    = "Message";
	static final String AUTH_CODE  = "AuthCode";
	static final String EXT_DATA   = "ExtData";
	POSApp posapp = POSApp.getInstance();
	OnOk onOk;
	public interface OnOk{
		public void onOk();
	}

	public DejavooGatewayTipBT(Context mContext, String selectedOption,String authkey,String invNum,String acntLast4,String tipAmount, String transAmt,OnOk onOk) {
		super();
		this.mContext        = mContext;
		this.deviceId        = MyPreferences.getMyPreference(DEVICE_ID, mContext);
		this.tipAmount       = MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(tipAmount));
		this.transId         = invNum;
		this.dejavoTipOption = Integer.parseInt(selectedOption);
		this.acntLast4       = acntLast4;
		this.authkey         = authkey;
		this.amountToPaid    = MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(transAmt));
		if(dejavoTipOption == DejavooAdjustmentDialog.RETURN_ADJUSTMENT){
			this.transId     = GenerateNewTransactionNo.onNewTransactionNoForDejavoo(mContext);
		}
		this.onOk            = onOk;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		progressHUD.dismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressHUD = ProgressHUD.show(mContext, "Processing...", true, false, this);
	}


	@Override
	protected Boolean doInBackground(Void... params) {
		createXMLData(dejavoTipOption);
		executeApiWithXmlData();
		while (response.isEmpty()) {
			try{
				Thread.sleep(3000);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		progressHUD.dismiss();
		System.out.println("Response -> "+ response);

		if(!response.equalsIgnoreCase("Exception") && !response.equalsIgnoreCase("Service Unavailable")){

			ArrayList<HashMap<String, String>> menuItems1 = new ArrayList<HashMap<String, String>>();
			parser = new XMLParser();
			Document doc = parser.getDomElement(response); // getting DOM element

			NodeList nl = doc.getElementsByTagName(RESPONSE);
			for (int i = 0; i < nl.getLength(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				map.put(MESSAGE, parser.getValue(e, MESSAGE));
				map.put(AUTH_CODE, parser.getValue(e, AUTH_CODE));
				map.put(EXT_DATA, parser.getValue(e, EXT_DATA));
				menuItems1.add(map);
			}

			if(menuItems1.size() > 0 && (menuItems1.get(0).get(MESSAGE).equalsIgnoreCase("Success") || (menuItems1.get(0).get(MESSAGE).equalsIgnoreCase("Approved")))){
				ToastHelper.showApprovedToast(mContext);
				onOk.onOk();
			}
			else{
				ShowDecilineDialog.onShow(mContext);
			}
		}
		else{
			ShowDecilineDialog.onShow(mContext);
		}
	}

	private void createXMLData (int caseValue) {

		sb = new StringBuilder();
		sb.append("<request>");
		sb.append("<PaymentType>"+MyPreferences.getMyPreference(PrefrenceKeyConst.DEJAVO_PAYMENT_TYPE, mContext)+"</PaymentType>");
		sb.append("<RegisterId>"+deviceId+"</RegisterId>");
		sb.append("<InvNum>"+transId+"</InvNum>");
		sb.append("<RefId>" +transId+"</RefId>");
		sb.append("<Amount>"+amountToPaid+"</Amount>");

		if (caseValue == DejavooAdjustmentDialog.TIP_ADJUSMENT) {
			sb.append("<Tip>" + tipAmount + "</Tip>");
			sb.append("<TransType>" + "TipAdjust" + "</TransType>");
			sb.append("<AcntLast4>" + acntLast4 + "</AcntLast4>");
			sb.append("<AuthCode>" + authkey + "</AuthCode>");
		}
		else{
			sb.append("<TransType>"+"Return"+"</TransType>");	
		}
		sb.append("</request>");		
	}

	private void executeApiWithXmlData (){
		try {
			String request = "TerminalTransaction="+sb.toString()+"\0";
			System.out.println("Request -> "+request);
			BTDejavooService.getData(new BTDejavooService.GetReceivedData() {
				@Override
				public void onData(String st) {
					response = st;
				}
			});
			posapp.getInstance().bluetoothConnector.sendDatOverTerminal(request);
		}
		catch(Exception ex){
			ex.printStackTrace();
			response = "Exception";
		}
	}
}

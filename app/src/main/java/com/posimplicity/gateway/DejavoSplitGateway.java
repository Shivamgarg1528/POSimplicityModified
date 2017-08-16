package com.posimplicity.gateway;

import java.net.URLEncoder;

import com.dialog.ShowDecilineDialog;
import com.CustomControls.ProgressHUD;
import com.CustomControls.ToastHelper;
import com.Dialogs.CCDialogForSplitBill;
import com.PosInterfaces.PrefrenceKeyConst;
import com.RecieptPrints.EachBillPrint;
import com.utils.GenerateNewTransactionNo;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.Variables;
import com.utils.XMLParser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;

public class DejavoSplitGateway  extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener ,PrefrenceKeyConst{

	private Context mContext;
	private String iPAddress;
	private StringBuilder sb;
	private String amountToPay;
	private String registerId;
	private String transId;
	private String authkey;
	private ProgressHUD progressHUD;
	private String response;
	private XMLParser parser; 
	private String tipAmont;
	private int dejavoOption;
	static final String RESPONSE   = "response"; // parent node
	static final String MESSAGE    = "Message";
	static final String AUTH_CODE  = "AuthCode";
	static final String EXT_DATA   = "ExtData";
	private CCDialogForSplitBill ccDialogForSplitBill;


	public DejavoSplitGateway(Context mContext, String iPAddress,float amountToPay, float tipAmount, long selectedOption, CCDialogForSplitBill ccDialogForSplitBill) {
		super();
		this.mContext        = mContext;
		this.iPAddress       = iPAddress;
		this.registerId      = MyPreferences.getMyPreference(DEVICE_ID, mContext);
		this.authkey         = MyPreferences.getMyPreference(DEJAVOO_AUTH_KEY, mContext);
		this.amountToPay     = MyStringFormat.formatWith2DecimalPlaces(amountToPay);
		this.transId         = GenerateNewTransactionNo.onNewTransactionNoForDejavoo(mContext);
		this.tipAmont        = MyStringFormat.formatWith2DecimalPlaces(tipAmount);
		this.dejavoOption    = (int) selectedOption;
		this.ccDialogForSplitBill = ccDialogForSplitBill;
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

		createXMLData(dejavoOption);
		executeApiWithXmlData();

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

					switch (dejavoOption) {
					case 0:
						Variables.isDejavooSuccess = true;
						break;

					case 1:
						if(Variables.isRetailWithTipAuthDone){
							MyPreferences.setMyPreference(DEJAVOO_AUTH_KEY,dejavooParseService.dejavooResponse.getXmp().getResponse().getAuthCode(), mContext);
							Variables.isRetailWithTipAuthDone = false; 
							ccDialogForSplitBill.tipAmtEditText.setEnabled(true);
							ccDialogForSplitBill.dejavoBtn.setTextColor(Color.parseColor("#000000"));
						}
						else
							Variables.isDejavooSuccess = true;
						break;

					case 2:
						Variables.isDejavooSuccess = true;

						break;
					case 3:
						if(Variables.isRestaurantWithTipSaleDone){

							MyPreferences.setMyPreference(DEJAVOO_AUTH_KEY,dejavooParseService.dejavooResponse.getXmp().getResponse().getAuthCode(), mContext);
							Variables.isRestaurantWithTipSaleDone = false;
							ccDialogForSplitBill.tipAmtEditText.setEnabled(true); 
							ccDialogForSplitBill.dejavoBtn.setTextColor(Color.parseColor("#000000"));
						}
						else
						{
							Variables.isDejavooSuccess = true;
						}

					default:
						break;
					}
					ToastHelper.showApprovedToast(mContext);

					if(Variables.isDejavooSuccess){
						ccDialogForSplitBill.dismiss();

						EachBillPrint eachBillPrint = new EachBillPrint(mContext);
						eachBillPrint.onExectue();
					}
				}
			}
			else
				ShowDecilineDialog.onShow(mContext);
		}
		catch(Exception ec){
			ec.printStackTrace();
		}
	}

	private void createXMLData (int dejavoOption2){

		sb = new StringBuilder();

		sb.append("<request>");
		sb.append("<PaymentType>"+MyPreferences.getMyPreference(PrefrenceKeyConst.DEJAVO_PAYMENT_TYPE, mContext)+"</PaymentType>");
		sb.append("<RegisterId>"+registerId+"</RegisterId>");
		sb.append("<InvNum>"+transId+"</InvNum>");
		sb.append("<RefId>" +transId+"</RefId>");
		sb.append("<Amount>"+amountToPay+"</Amount>");

		//sb.append("<CardData>"+"EntType=2,PAN=5454545454545454,Track1=,Track2=;5454545454545454=16041015432112345601?=,ExpDate=1214,AVS=,ZIP=12345,CVV=123,CardPresent=False"+"</CardData>");

		switch (dejavoOption2) {

		case 0:   //Retails
			sb.append("<TransType>"+"Sale"+"</TransType>");	
			break;

		case 1:  //Retails with Tip

			if(Variables.isRetailWithTipAuthDone)
				sb.append("<TransType>"+"Auth"+"</TransType>");
			else{
				sb.append("<TransType>"+"Capture"+"</TransType>");
				sb.append("<AuthCode>"  +authkey+"</AuthCode>");
				sb.append("<Tip>"+tipAmont+"</Tip>");
			}

			break;

		case 2:  // Restaurant 
			sb.append("<TransType>"+"Sale"+"</TransType>");
			break;

		case 3: // Restaurant With Tip
			if(Variables.isRestaurantWithTipSaleDone)
				sb.append("<TransType>"+"Sale"+"</TransType>");
			else{
				sb.append("<TransType>"+"TipAdjust"+"</TransType>");
				sb.append("<AuthCode>"  +authkey  +"</AuthCode>");
				sb.append("<Tip>"+tipAmont+"</Tip>");
				sb.append("<AcntLast4>"+MyPreferences.getMyPreference(ACNTLAST4, mContext)+"</AcntLast4>");
			}


		default:
			break;
		}

		sb.append("</request>");
	}

	private void executeApiWithXmlData (){
		try {
			System.out.println("Request -> "+"http://"+ iPAddress +"/cgi.html?TerminalTransaction="+sb.toString());
			String theXml  = URLEncoder.encode(sb.toString(), "UTF-8");
			parser         = new XMLParser();
			response       = parser.getXmlFromUrl("http://"+ iPAddress +"/cgi.html?TerminalTransaction="+theXml); // getting XML
		}
		catch(Exception ex){
			ex.printStackTrace();
			response = "Exception";
		}
	}
}

package com.posimplicity.gateway;

import java.net.URLEncoder;
import com.dialog.ShowDecilineDialog;
import com.CCardPayment.OfflinePayment;
import com.CustomControls.ProgressHUD;
import com.Fragments.CreditFragment;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.GenerateNewTransactionNo;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.XMLParser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.text.TextUtils;

public class DejavoGateway extends AsyncTask<Void, Integer, Boolean> implements OnCancelListener ,PrefrenceKeyConst{

	private Context mContext;
	private String iPAddress;
	private StringBuilder sb;
	private String amountToPay;
	private String registerId;
	private String transId;
	private ProgressHUD progressHUD;
	public String response;
	private XMLParser parser; 
	private int dejavoOption;
	public DejavoGateway(Context mContext, String iPAddress,float amountToPay, long selectedOption,CreditFragment creditFragment) {
		super();
		this.mContext        = mContext;
		this.iPAddress       = iPAddress;
		this.registerId      = MyPreferences.getMyPreference(DEVICE_ID, mContext);
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

		case 1:  //Retails With Tip
			sb.append("<TransType>"+"Sale"+"</TransType>");

			break;

		case 2:  // Restaurant 
			sb.append("<TransType>"+"Sale"+"</TransType>");
			break;

		case 3:// Restaurant With Tip
			sb.append("<TransType>"+"Sale"+"</TransType>");
			break;

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

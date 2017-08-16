package com.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.AsyncTasks.CompleteReportInMagento;
import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.PosInterfaces.AddCustomerInterface;
import com.PosInterfaces.MyWebClientClass;
import com.RecieptPrints.KitchenReceipt;
import com.RecieptPrints.PrintReceiptCustomer;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.HideSoftKeyBoardFromScreen;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.R;

import org.json.JSONObject;

public class BarFragment  extends BaseFragment implements OnClickListener, TextWatcher{

	private Button   saveOrderBtn,assignBtn,saveAndPrintWithWifi,saveAndPrintWithUSB;
	private TextView customerNameTv,dismissTV;
	private ProgressBar progressBarr;
	private WebView  webView;
	private EditText editText;
	private String editTextString;
	private CustomerModel customerInfo;
	private MyWebClientClass weClient;
	private String url ;
	public final String interfaceName = "POS_ADD_CUSTOMER";
	public static final String PAYMENT_MODE = "checkmo";
	public static final String ORDER_STATUS = Variables.orderStatus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView              = inflater.inflate(R.layout.fragment_bar_orders, null);
		webView               = findViewIdAndCast(R.id.Fragment_Bar_WebView_Customer_Add);
		customerNameTv        = findViewIdAndCast(R.id.Fragment_Bar_TextView_Customer_Name);
		saveOrderBtn          = findViewIdAndCast(R.id.Fragment_Bar_Btn_Save_Order);
		assignBtn             = findViewIdAndCast(R.id.Fragment_Bar_Btn_Assign);
		editText              = findViewIdAndCast(R.id.Fragment_Bar_EditText_Customer_MobileNo);
		dismissTV             = findViewIdAndCast(R.id.Fragment_Bar_TV_Cancel);
		saveAndPrintWithWifi  = findViewIdAndCast(R.id.Fragment_Bar_Order_Btn_Save_And_Print_With_Wifi);
		saveAndPrintWithUSB   = findViewIdAndCast(R.id.Fragment_Bar_Order_Btn_Save_And_Print_With_USB);

		progressBarr          = new ProgressBar(mContext);
		return rootView; 
	}	



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		url      = FULL_PATH + MyPreferences.getMyPreference(STORE, mContext) + SUB_URL +"functions/addbar/customerbar.htm";
		weClient = new MyWebClientClass(progressBarr, webView);
		webView.addJavascriptInterface(listener,interfaceName);
		weClient.loadRequestedUrl(url);
		progressBarr.setVisibility(View.GONE);
		saveOrderBtn.setOnClickListener(this);
		assignBtn.setOnClickListener(this);
		dismissTV.setOnClickListener(this);
		editText.addTextChangedListener(this);
		saveAndPrintWithWifi.setOnClickListener(this);
		saveAndPrintWithUSB.setOnClickListener(this);

		if(Variables.tableOrClerkShipToNameModel != null && !Variables.tableOrClerkShipToNameModel.isCustomerNotValid()){
			customerNameTv.setText(Variables.tableOrClerkShipToNameModel.getFirstName());
			Variables.shipToName = Variables.tableOrClerkShipToNameModel.getEmailAddress();
			Variables.tableID    = Variables.tableOrClerkShipToNameModel.getTelephoneNo();
			editText.setText(Variables.tableID);
		}

		if(PrintSettings.canPrintWifiSlip(mContext)){
			saveAndPrintWithWifi.setVisibility(View.VISIBLE);
		}

		if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
			saveAndPrintWithUSB.setVisibility(View.VISIBLE);
		}

		if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
			saveAndPrintWithUSB.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		String text;
		HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(mContext, editText);

		switch (v.getId()) {

		case R.id.Fragment_Bar_Order_Btn_Save_And_Print_With_Wifi:
			text = "Submit To "+ ((Button)v).getText().toString()+"!";
			okTOGoSimpleUSBWifiBluetoothPrinting(mContext, text,2);
			break;

		case R.id.Fragment_Bar_Order_Btn_Save_And_Print_With_USB:
			text = "Submit To "+ ((Button)v).getText().toString()+"!";
			okTOGoSimpleUSBWifiBluetoothPrinting(mContext, text,1);
			break;

		case R.id.Fragment_Bar_Btn_Save_Order:
			text = "Submit To "+ ((Button)v).getText().toString()+"!";
			okTOGoSimpleUSBWifiBluetoothPrinting(mContext, text,0);			

			break;

		case R.id.Fragment_Bar_Btn_Assign:

			editTextString = editText.getText().toString();

			if(editTextString.isEmpty()){
				editText.setError("Enter Mobile Number First");
				return;
			}
			if(customerNameTv.getText().toString().isEmpty() || customerNameTv.getText().toString().equalsIgnoreCase("Default"))
				editText.setError("Enter Valid Mobile Number First");
			else{
				Variables.shipToName = customerInfo.getEmailAddress();
				Variables.tableID    = editTextString;
				ToastUtils.showOwnToast(mContext, "CustomerParent Assigned Successfully");
			}

			break;

		case R.id.Fragment_Bar_TV_Cancel:
			((Activity) mContext).finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	@Override
	public void afterTextChanged(Editable s) {
		editTextString = s.toString();
		if(s.length() > 0){
			setCustomerNameCorrespondingToMobileNo();
		}
		else{
			customerNameTv.setText("");
		}
	}

	private void setCustomerNameCorrespondingToMobileNo() {		
		customerInfo                 =  new CustomerTable(mContext).getSingleInfoFromTableByPhoneNo(editTextString);
		customerNameTv.setText(customerInfo.getFirstName());			
	}	


	private AddCustomerInterface listener = new AddCustomerInterface() {

		@JavascriptInterface
		@Override
		public void showErrorMsg() {
			Toast.makeText(mContext, "Error To Record Data", Toast.LENGTH_SHORT).show();
		}

		@JavascriptInterface
		@Override
		public void showCustomerInfo(String jsonData) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				String customerId     = JSONObJValidator.stringTagValidate(jsonObject,    "customer_id", "-1");
				String firstName      = JSONObJValidator.stringTagValidate(jsonObject, "firstname", "");
				String lastName       = JSONObJValidator.stringTagValidate(jsonObject, "lastname", "");
				String email          = JSONObJValidator.stringTagValidate(jsonObject, "email", "");
				final String telephhone     = JSONObJValidator.stringTagValidate(jsonObject, "telephone", "");
				String groupId    = JSONObJValidator.stringTagValidate(jsonObject, "group_id", CustomerTable.DEFAULT_GROUP_ID);

				String street         = JSONObJValidator.stringFromObj    (jsonObject, "street",     "NULL");
				String city           = JSONObJValidator.stringTagValidate(jsonObject, "city",       "NULL");
				String countryId      = JSONObJValidator.stringTagValidate(jsonObject, "country_id", "NULL");
				String region         = JSONObJValidator.stringTagValidate(jsonObject, "region",     "NULL");
				String postcode       = JSONObJValidator.stringTagValidate(jsonObject, "postcode",   "NULL");
				StringBuilder address    = new StringBuilder();
				address.append(street).append(",").append(city).append(",").append(countryId).append(",").append(region).append(",").append(postcode);
				CustomerModel customer = new CustomerModel(customerId, firstName, lastName, email, telephhone, address.toString(),groupId,false,false);
				boolean insertSuccessFully = new CustomerTable(mContext).addInfoInTable(customer);	

				if(insertSuccessFully){
					ToastUtils.showOwnToast(mContext, "Record Data SuccessFully");
					((Activity)mContext).runOnUiThread(new Runnable() {					
						@Override
						public void run() {
							editText.setText(telephhone);						
						}
					});
				}
				else
					ToastUtils.showOwnToast(mContext, "Error To Insert Last Record In Local Db");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void okTOGoSimpleUSBWifiBluetoothPrinting(final Context mContext,String msg ,final int status) {


		if(Variables.tableID.isEmpty()){
			editText.setError("Please Assign CustomerParent First");
			return;
		}
		else{

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.app_icon);
			builder.setMessage(msg);
			builder.setCancelable(false);

			builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new CompleteReportInMagento(mContext,ORDER_STATUS,PAYMENT_MODE,false).execute();
					switch (status) {

					case 0:
						onCallAfterPrintRequest();
						break;

					case 1:						
						if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
							new UsbPR(mContext, new PrinterCallBack() {

								@Override
								public void onDisconnected() {
									onCallAfterPrintRequest();
								}

								@Override
								public void onConnected(BasePR pPrinter) {
									PrintReceiptCustomer printRecieptUSB = new PrintReceiptCustomer(mContext);
									printRecieptUSB.onPrintRecieptCustomer(pPrinter, true);
									onCallAfterPrintRequest();
								}

							}).onConnectionStart();
							return;
						}						
						if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
							PrintReceiptCustomer printRecieptUSB = new PrintReceiptCustomer(mContext);
							printRecieptUSB.onPrintRecieptCustomer(gApp.getPrinterBT(), true);
						}
						onCallAfterPrintRequest();
						break;

					case 2:

						if(PrintSettings.isAbleToPrintKitchenReceiptThroughUsb(mContext)){
							new UsbPR(mContext, new PrinterCallBack() {

								@Override
								public void onDisconnected() {
									onCallAfterPrintRequest();									
								}

								@Override
								public void onConnected(BasePR pPrinter) {
									KitchenReceipt.onPrintKitchenReciept(mContext, localInsatnceOfHome, pPrinter);
									onCallAfterPrintRequest();									
								}
							});
							return;
						}

		
						if(PrintSettings.isAbleToPrintKitchenReceiptThroughBluetooth(mContext)){
							KitchenReceipt.onPrintKitchenReciept(mContext, localInsatnceOfHome, gApp.getPrinterBT());
						}

						if(PrintSettings.canPrintWifiSlip(mContext)){
							KitchenReceipt.onPrintKitchenReciept(mContext, localInsatnceOfHome, gApp.getPrinterWF());
						}
						onCallAfterPrintRequest();					

						break;

					default:
						break;
					}					
				}
			});		

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {	
					dialog.dismiss();
				}
			});

			builder.show();
			builder.create();
		}
	}

	protected void onCallAfterPrintRequest() {
		localInsatnceOfHome.resetAllData(mContext,0);
		((Activity) mContext).finish();
	}
}


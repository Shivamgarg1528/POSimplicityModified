package com.Fragments;

import java.util.ArrayList;
import java.util.List;

import com.AlertDialogWithOptions.DrawerOpenOptionsDialog;
import com.dialog.BlueToothNotSupported;
import com.utils.BluetoothHelper;
import com.posimplicity.service.ServiceUtils;
import com.PosInterfaces.PrefrenceKeyConst;
import com.RecieptPrints.PrintExtraReceipt;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.DeviceListActivity;
import com.posimplicity.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MaintFragmentPrinterSetting extends BaseFragment implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

	public static final int REQUEST_CONNECT_DEVICE    = 1;
	public static final int REQUEST_ENABLE_BLUETOOTH  = 2;
	public static final int REQUEST_ENABLE_BLUETOOTH_DEVICE  = 3;

	public static final int USB_PRINTING_CASE         = 0;
	public static final int BL_PRINTING_CASE          = 1;
	public static final int WIFI_PRINTING_CASE        = 2;
	public static final int TIP_CASE                  = 3;
	public static final int DUPLICATE_RECEIPT_CASE    = 4;
	public static final int PRINTER_SOUND             = 5;
	public static final int RECEIPT_PROMPT_CASE       = 6;	
	private List<String> settingsList                 = new ArrayList<>();
	private String [] listItems                       = {"Usb Printing ","Bluetooth Printing ","Wifi Printing ","Tip ","Duplicate Receipt","Printer Sound","Receipt Prompt"};

	private EditText editText1, editText2, editText3, editText4;
	private Spinner spinner1, spinner2, spinner3, spinner4;
	private Button saveBtn, printBtn,resetBtn,drawerBtn;
	private String text1, text2, text3, text4;
	private int space1, space2, space3, space4;
	private ListView settingsListView;
	private ArrayAdapter<String> adapter;
	private boolean isAnyPrintOptionEnable;
	private RadioGroup radioGrpCustomer,radioGrpKitchen;

	public MaintFragmentPrinterSetting() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadArrayList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView         = inflater.inflate(R.layout.fragment_printer_settings,container, false);
		editText1        = findViewByIdAndCast(R.id.Fragment_Printer_Settings_EDT_FRIST);
		editText2        = findViewByIdAndCast(R.id.Fragment_Printer_Settings_EDT_SECOND);
		editText3        = findViewByIdAndCast(R.id.Fragment_Printer_Settings_EDT_THIRD);
		editText4        = findViewByIdAndCast(R.id.Fragment_Printer_Settings_EDT_FOUR);
		settingsListView = findViewByIdAndCast(R.id.Fragment_Printer_Settings_ListView_Settings_Items);
		spinner1         = findViewByIdAndCast(R.id.Fragment_Printer_Settings_SP_FRIST);
		spinner2         = findViewByIdAndCast(R.id.Fragment_Printer_Settings_SP_SECOND);
		spinner3         = findViewByIdAndCast(R.id.Fragment_Printer_Settings_SP_THIRD);
		spinner4         = findViewByIdAndCast(R.id.Fragment_Printer_Settings_SP_FOUR);
		saveBtn          = findViewByIdAndCast(R.id.Fragment_Printer_Settings_BUTTON_SAVE);
		printBtn         = findViewByIdAndCast(R.id.Fragment_Printer_Settings_BUTTON_TEST_PRINT);
		resetBtn         = findViewByIdAndCast(R.id.Fragment_Printer_Settings_BUTTON_RESET);
		drawerBtn        = findViewByIdAndCast(R.id.Fragment_Printer_Settings_Button_Drawer);
		radioGrpCustomer = findViewByIdAndCast(R.id.Fragment_Printer_Settings_RG_Customer);
		radioGrpKitchen  = findViewByIdAndCast(R.id.Fragment_Printer_Settings_RG_Kitchen);
		return rootView;
	}

	private void loadArrayList(){

		String connectedIpAddress = MyPreferences.getMyPreference(WIFI_IP_ADDRESS, mContext);
		if(connectedIpAddress.isEmpty())
			connectedIpAddress    = "No IP Address Available";	


		String connectedBTAddress = MyPreferences.getMyPreference(BLUETOOTH_DEVICE_ADDRESS_PRINTING, mContext);
		if(connectedBTAddress.isEmpty())
			connectedBTAddress    = "No Bluetooth Device Selected";

		settingsList.clear();
		settingsList.add(listItems[USB_PRINTING_CASE]);
		settingsList.add(listItems[BL_PRINTING_CASE].concat("( ").concat(connectedBTAddress).concat(" )"));
		settingsList.add(listItems[WIFI_PRINTING_CASE].concat("( ").concat(connectedIpAddress).concat(" )"));
		settingsList.add(listItems[TIP_CASE]);
		settingsList.add(listItems[DUPLICATE_RECEIPT_CASE]);
		settingsList.add(listItems[PRINTER_SOUND]);
		settingsList.add(listItems[RECEIPT_PROMPT_CASE]);
	}

	private void newAdapter(){
		adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_checked, android.R.id.text1, settingsList){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view =  super.getView(position, convertView, parent);
				((CheckedTextView)view.findViewById(android.R.id.text1)).setTextColor(Color.parseColor("#ffffff"));
				return view;
			}
		};
		settingsListView.setAdapter(adapter);	
		adapter.notifyDataSetChanged();
	}

	private void enableDisableOption(){
		settingsListView.setItemChecked(USB_PRINTING_CASE         , MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_USB_ON_PS, mContext));
		settingsListView.setItemChecked(BL_PRINTING_CASE          , MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_BT_ON_PS, mContext));
		settingsListView.setItemChecked(WIFI_PRINTING_CASE        , MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_WIFI_ON_PS, mContext));
		settingsListView.setItemChecked(TIP_CASE                  , MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_TIP_ON_PS, mContext));
		settingsListView.setItemChecked(DUPLICATE_RECEIPT_CASE    , MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_DUPLICATE_RECIEPT_ON_PS, mContext));
		settingsListView.setItemChecked(PRINTER_SOUND             , MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_PRINTER_SOUND_ON, mContext));
		settingsListView.setItemChecked(RECEIPT_PROMPT_CASE       , MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_RECEIPT_PROMPT_ON_PS, mContext));	
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);	
		settingsListView.setOnItemLongClickListener(this);
		settingsListView.setOnItemClickListener(this);

		boolean customerUsbEnable = MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_USB_CU_ON_PS , mContext);
		if(customerUsbEnable)
			radioGrpCustomer.check(R.id.Fragment_Printer_Settings_RB_Customer_USB);
		else
			radioGrpCustomer.check(R.id.Fragment_Printer_Settings_RB_Customer_BT);


		boolean kitchenUsbEnable  = MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_USB_KI_ON_PS , mContext);
		if(kitchenUsbEnable)
			radioGrpKitchen.check(R.id.Fragment_Printer_Settings_RB_Kitchen_USB);
		else
			radioGrpKitchen.check(R.id.Fragment_Printer_Settings_RB_Kitchen_BT);	


		newAdapter();
		enableDisableOption();

		editText1.setText(MyPreferences.getMyPreference(TEXT1, mContext));
		editText2.setText(MyPreferences.getMyPreference(TEXT2, mContext));
		editText3.setText(MyPreferences.getMyPreference(TEXT3, mContext));
		editText4.setText(MyPreferences.getMyPreference(TEXT4, mContext));

		List<Integer> counter = new ArrayList<Integer>();
		for (int i = 1; i <= 25; i++) {
			counter.add(i);
		}

		ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(mContext,android.R.layout.simple_spinner_item, counter);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner1.setAdapter(dataAdapter);
		spinner2.setAdapter(dataAdapter);
		spinner3.setAdapter(dataAdapter);
		spinner4.setAdapter(dataAdapter);	

		saveBtn.setOnClickListener(this);
		printBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		drawerBtn.setOnClickListener(this);

		if (MyPreferences.getBooleanPrefrences(IS_MARGIN_APPLIED, mContext) ){

			spinner1.setSelection((int) (MyPreferences.getLongPreference(SPACE1, mContext) - 1));
			spinner2.setSelection((int) (MyPreferences.getLongPreference(SPACE2, mContext) - 1));
			spinner3.setSelection((int) (MyPreferences.getLongPreference(SPACE3, mContext) - 1));
			spinner4.setSelection((int) (MyPreferences.getLongPreference(SPACE4, mContext) - 1));

		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.Fragment_Printer_Settings_Button_Drawer:
			new DrawerOpenOptionsDialog(mContext).showDrawerOpenOptionDialog();
			break;

		case R.id.Fragment_Printer_Settings_BUTTON_RESET:
			continueToReset(mContext);
			break;

		case R.id.Fragment_Printer_Settings_BUTTON_SAVE:
			settingTextAlignment();
			ToastUtils.showOwnToast(mContext, "Successfully Saved");
			break;

		case R.id.Fragment_Printer_Settings_BUTTON_TEST_PRINT:
			settingTextAlignment();

			String st = String.format("Bluetooth Checked = %s , Kitchen Receipt BT = %s , BT Printer Connected = %s", MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_BT_ON_PS, mContext),MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.IS_BT_KI_ON_PS, mContext),PrintSettings.isBTPrintAvilable(mContext));
			ToastUtils.showOwnToastLong(mContext, st);
			
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
				isAnyPrintOptionEnable = true;
				new UsbPR(mContext, new PrinterCallBack() {

					@Override
					public void onDisconnected() {
					}

					@Override
					public void onConnected(BasePR pPrinter) {
						PrintExtraReceipt.printSample(pPrinter);
					}
				}).onConnectionStart();
			}

			if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
				isAnyPrintOptionEnable = true;
				PrintExtraReceipt.printSample(gApp.getPrinterBT());
			}

			if(PrintSettings.canPrintWifiSlip(mContext)){
				isAnyPrintOptionEnable = true;
				PrintExtraReceipt.printSample(gApp.getPrinterWF());
			}

			if(!isAnyPrintOptionEnable)
				ToastUtils.showOwnToast(mContext, "Please Enable Any Priniting Option First ");

			break;

		default:
			break;
		}
	}

	private void settingTextAlignment() {

		text1  = editText1.getText().toString();
		space1 = Integer.parseInt(spinner1.getSelectedItem().toString());

		text2  = editText2.getText().toString();
		space2 = Integer.parseInt(spinner2.getSelectedItem().toString());

		text3  = editText3.getText().toString();
		space3 = Integer.parseInt(spinner3.getSelectedItem().toString());

		text4  = editText4.getText().toString();
		space4 = Integer.parseInt(spinner4.getSelectedItem().toString());

		MyPreferences.setLongPreferences(SPACE1, space1, mContext);
		MyPreferences.setLongPreferences(SPACE2, space2, mContext);
		MyPreferences.setLongPreferences(SPACE3, space3, mContext);
		MyPreferences.setLongPreferences(SPACE4, space4, mContext);

		String value1 = String.format("%" + space1 + "s", " ");
		String value2 = String.format("%" + space2 + "s", " ");
		String value3 = String.format("%" + space3 + "s", " ");
		String value4 = String.format("%" + space4 + "s", " ");

		if(!text1.isEmpty())
			text1 = value1.concat(text1);

		if(!text2.isEmpty())
			text2 = value2.concat(text2);

		if(!text3.isEmpty())
			text3 = value3.concat(text3);

		if(!text4.isEmpty())
			text4 = value4.concat(text4);

		MyPreferences.setMyPreference(TEXT1, text1, mContext);
		MyPreferences.setMyPreference(TEXT2, text2, mContext);
		MyPreferences.setMyPreference(TEXT3, text3, mContext);
		MyPreferences.setMyPreference(TEXT4, text4, mContext);
		MyPreferences.setBooleanPrefrences(IS_MARGIN_APPLIED,true, mContext);
		isAnyPrintOptionEnable = false;

		boolean customerUsbEnable = radioGrpCustomer.getCheckedRadioButtonId() == R.id.Fragment_Printer_Settings_RB_Customer_USB;
		boolean kitchenUsbEnable  = radioGrpKitchen .getCheckedRadioButtonId() == R.id.Fragment_Printer_Settings_RB_Kitchen_USB;

		if(customerUsbEnable){
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_USB_CU_ON_PS, true  , mContext);
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_BT_CU_ON_PS , false , mContext);
		}
		else{
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_USB_CU_ON_PS, false  , mContext);
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_BT_CU_ON_PS , true , mContext);
		}

		if(kitchenUsbEnable){
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_USB_KI_ON_PS, true  , mContext);
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_BT_KI_ON_PS , false , mContext);
		}
		else{
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_USB_KI_ON_PS, false  , mContext);
			MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.IS_BT_KI_ON_PS , true , mContext);
		}

	}

	@SuppressWarnings("unchecked")
	public <T> T findViewByIdAndCast(int id) {
		return (T) rootView.findViewById(id);
	}

	public void continueToReset(final Context mContext) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("Continue  to Reset All Printer Settings!");
		builder.setCancelable(false);
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				MyPreferences.setBooleanPrefrences(DRAWER_CASH                 , true, mContext);
				MyPreferences.setBooleanPrefrences(DRAWER_CC                   , false, mContext);
				MyPreferences.setBooleanPrefrences(DRAWER_CHECK                , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_MARGIN_APPLIED           , false, mContext);

				MyPreferences.setBooleanPrefrences(IS_RECEIPT_PROMPT_ON_PS        , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_TIP_ON_PS                   , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS     , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_USB_ON_PS                   , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_WIFI_ON_PS                  , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_BT_ON_PS                    , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_BT_CU_ON_PS                 , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_BT_KI_ON_PS                 , true , mContext);
				MyPreferences.setBooleanPrefrences(IS_USB_CU_ON_PS                , true , mContext);
				MyPreferences.setBooleanPrefrences(IS_USB_KI_ON_PS                , false, mContext);
				MyPreferences.setBooleanPrefrences(IS_PRINTER_SOUND_ON            , false, mContext);
				MyPreferences.setMyPreference(WIFI_IP_ADDRESS                     , ""   , mContext);
				MyPreferences.setMyPreference(BLUETOOTH_DEVICE_ADDRESS_PRINTING   , ""   , mContext);

				radioGrpCustomer.check(R.id.Fragment_Printer_Settings_RB_Customer_USB);
				radioGrpKitchen .check(R.id.Fragment_Printer_Settings_RB_Kitchen_BT);

				// Disable BT And Its Running Service
				BluetoothHelper.closeBluetoothSocketManually();
				ServiceUtils.operateBTService(mContext, false);
				ServiceUtils.operateWFService(mContext, false);

				MyPreferences.setLongPreferences(SPACE1, 1, mContext);
				MyPreferences.setLongPreferences(SPACE2, 1, mContext);
				MyPreferences.setLongPreferences(SPACE3, 1, mContext);
				MyPreferences.setLongPreferences(SPACE4, 1, mContext);

				MyPreferences.setMyPreference(TEXT1, "", mContext);
				MyPreferences.setMyPreference(TEXT2, "", mContext);
				MyPreferences.setMyPreference(TEXT3, "", mContext);
				MyPreferences.setMyPreference(TEXT4, "", mContext);

				editText1.setText("");
				editText2.setText("");
				editText3.setText("");
				editText4.setText("");

				spinner1.setSelection(0);
				spinner2.setSelection(0);
				spinner3.setSelection(0);
				spinner4.setSelection(0);

				ToastUtils.showOwnToast(mContext, "Successfully Reseted");
				loadAndRefereshList();
			}
		});		
		builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {	
				dialog.dismiss();
			}
		});

		AlertDialog alertDialog = builder.create();	
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case  REQUEST_CONNECT_DEVICE:

			if (resultCode == Activity.RESULT_OK) { 
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				MyPreferences.setMyPreference(PrefrenceKeyConst.BLUETOOTH_DEVICE_ADDRESS_PRINTING, address, mContext);
				MyPreferences.setBooleanPrefrences(IS_BT_ON_PS, false, mContext);
				loadAndRefereshList();
			}

			break;

		case REQUEST_ENABLE_BLUETOOTH:
			if(resultCode == Activity.RESULT_OK){
				ServiceUtils.operateBTService(mContext, true);
			}
			else{
				ServiceUtils.operateBTService(mContext, false);
				MyPreferences.setBooleanPrefrences(IS_BT_ON_PS, false, mContext);
				loadAndRefereshList();	
			}
			break;

		case REQUEST_ENABLE_BLUETOOTH_DEVICE:

			if(resultCode == Activity.RESULT_OK)
				BluetoothHelper.findAndSelectAnyDevice(this, mContext,0);

			break;
		}
	}

	public void showWifiConnectionDialogFirst(final Context mContext){

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("Please Provide IP Address First");

		final EditText defaultIp = new EditText(mContext);
		defaultIp.setHint("Enter IP Address , Default IP is 192.168.0.70");
		defaultIp.setHintTextColor(Color.parseColor("#a9a9a9"));
		defaultIp.setTextColor(Color.BLACK);
		defaultIp.setGravity(Gravity.CENTER);
		defaultIp.setImeOptions(EditorInfo.IME_ACTION_DONE);
		defaultIp.setSingleLine(true);
		builder.setView(defaultIp);
		builder.setCancelable(false);

		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String wifiAddress = "192.168.0.70";
				if (!defaultIp.getText().toString().isEmpty())
					wifiAddress    = defaultIp.getText().toString().trim();
				MyPreferences.setMyPreference(WIFI_IP_ADDRESS, wifiAddress, mContext);
				MyPreferences.setBooleanPrefrences(IS_WIFI_ON_PS, false, mContext);
				loadAndRefereshList();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}			
		});
		builder.show();		
		AlertDialog al = builder.create();
		al.setCanceledOnTouchOutside(false);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		CheckedTextView chView = (CheckedTextView) arg1;
		boolean ch             = chView.isChecked();
		switch (position) {

		case USB_PRINTING_CASE :
			MyPreferences.setBooleanPrefrences(IS_USB_ON_PS, ch, mContext);
			break;

		case BL_PRINTING_CASE:
			String connectedBlAddress = MyPreferences.getMyPreference(BLUETOOTH_DEVICE_ADDRESS_PRINTING, mContext);

			if(connectedBlAddress.isEmpty()){
				chView.setChecked(false);
				ToastUtils.showOwnToast(mContext,"Use Long Tap To Select Bluetooth Device");
			}

			MyPreferences.setBooleanPrefrences(IS_BT_ON_PS, chView.isChecked(), mContext);
			if(chView.isChecked()){
				if(!BluetoothHelper.isBluetoothOpen())
					BluetoothHelper.openBluetootSocket(this, mContext,MaintFragmentPrinterSetting.REQUEST_ENABLE_BLUETOOTH,0);
				else
					ServiceUtils.operateBTService(mContext, true);
			}
			else
				ServiceUtils.operateBTService(mContext, false);
			break;

		case WIFI_PRINTING_CASE:			
			String connectedIpAddress = MyPreferences.getMyPreference(WIFI_IP_ADDRESS, mContext);
			if(connectedIpAddress.isEmpty()){
				chView.setChecked(false);
				ToastUtils.showOwnToast(mContext,"Use Long Tap To Enter Ip Address For Printer");
			}
			MyPreferences.setBooleanPrefrences(IS_WIFI_ON_PS, chView.isChecked(), mContext);
			ServiceUtils.operateWFService(mContext, chView.isChecked());
			break;

		case TIP_CASE:
			MyPreferences.setBooleanPrefrences(IS_TIP_ON_PS ,ch, mContext);
			break;

		case DUPLICATE_RECEIPT_CASE:
			MyPreferences.setBooleanPrefrences(IS_DUPLICATE_RECIEPT_ON_PS ,ch, mContext);
			break;

		case PRINTER_SOUND:
			MyPreferences.setBooleanPrefrences(IS_PRINTER_SOUND_ON ,ch, mContext);
			break;

		case RECEIPT_PROMPT_CASE:
			MyPreferences.setBooleanPrefrences(IS_RECEIPT_PROMPT_ON_PS ,ch, mContext);
			break;

		default:
			break;
		}
		loadAndRefereshList();
	}

	public void loadAndRefereshList(){
		loadArrayList();
		adapter.notifyDataSetChanged();
		enableDisableOption();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
		switch (position) {

		case BL_PRINTING_CASE:

			if(!BluetoothHelper.isBluetoothAvailable()){
				BlueToothNotSupported.onBlueTooth(mContext);
				return true;
			}

			if(!BluetoothHelper.isBluetoothOpen())
				BluetoothHelper.openBluetootSocket(MaintFragmentPrinterSetting.this, mContext,MaintFragmentPrinterSetting.REQUEST_ENABLE_BLUETOOTH_DEVICE,0);
			else
				BluetoothHelper.findAndSelectAnyDevice(MaintFragmentPrinterSetting.this,mContext,0);

			return true;

		case WIFI_PRINTING_CASE:
			showWifiConnectionDialogFirst(mContext);
			return true;

		default:
			ToastUtils.showOwnToast(mContext, "Don't Use Long Tap , Please Try With Soft Tap");
			return true;
		}
	}
}

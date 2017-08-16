package com.Fragments;

import com.dialog.ResetStore;
import com.Dialogs.DomainNameDialog;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.posimplicity.R;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class MaintFragmentOtherSetting  extends BaseFragment implements OnClickListener, OnCheckedChangeListener, OnItemClickListener{

	private Button saveBtn,resetBtn,resetAutoStart,startAuto;
	private Switch collapseSwitch,enableSocketSwitch,enableEnGatSwitch,resetStoreSwitch,qrCodeSwitch,barCodeSwitch,clerkSwitch,orderAssignSwitch,enableTimeOnOff,enableCustomPrint;
	private ListView listView;
	private String [] LIST_ITEMS        = { "Retails","Restaurant","Bar","Quick Serve Restaurant With Kitchen Tablet","Pizza Delivery"};
	public static final int RETAILS_    = 0x00;
	public static final int RESTAURANT_ = 0x01;
	public static final int BAR_        = 0x02;
	public static final int QUICK_      = 0x03;
	public static final int PIZZA_      = 0x04;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		rootView              = inflater.inflate(R.layout.fragment_other_settings, container, false);
		collapseSwitch        = findViewIdAndCast(R.id.Fragment_Other_Settings_SWITCH_Collapse_Items);
		enableSocketSwitch    = findViewIdAndCast(R.id.Fragment_Other_Settings_SWITCH_Enable_Tablet);
		enableEnGatSwitch     = findViewIdAndCast(R.id.Fragment_Other_Settings_SWITCH_Encrypted_Gateway);
		resetStoreSwitch      = findViewIdAndCast(R.id.Fragment_Other_Settings_ResetStore);
		qrCodeSwitch          = findViewIdAndCast(R.id.Fragment_Other_Settings_QrCode);
		barCodeSwitch         = findViewIdAndCast(R.id.Fragment_Other_Settings_BarCode);
		clerkSwitch           = findViewIdAndCast(R.id.Fragment_Other_Settings_ClerkReporting);
		enableTimeOnOff       = findViewIdAndCast(R.id.Fragment_Other_Settings_ClerkTimeOnOff);
		orderAssignSwitch     = findViewIdAndCast(R.id.Fragment_Other_Settings_ClerkOrder);
		enableCustomPrint     = findViewIdAndCast(R.id.Fragment_Other_Settings_Supress_Custom_Printing);
		saveBtn               = findViewIdAndCast(R.id.Fragment_Other_Settings_BUTTON_SAVE);	
		resetBtn              = findViewIdAndCast(R.id.Fragment_Other_Settings_BUTTON_RESET);	
		startAuto             = findViewIdAndCast(R.id.Fragment_Other_Settings_BUTTON_AutoStart);
		resetAutoStart        = findViewIdAndCast(R.id.Fragment_Other_Settings_BUTTON_Reset_AutoStart);
		listView              = findViewIdAndCast(R.id.Fragment_Other_Settings_ListView_Store_Mode);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);

		startAuto.setText("Auto Start("+MyStringFormat.onIntFormat((int)MyPreferences.getLongPreference(PrefrenceKeyConst.HOURS_OF_DAY, mContext))+":"+MyStringFormat.onIntFormat((int)MyPreferences.getLongPreference(PrefrenceKeyConst.MINUTES, mContext))+")");
		
		enableCustomPrint.setChecked(MyPreferences.getBooleanPrefrences(IS_CUSTOM_OPTION_PRINT_ON_OFF, mContext));		
		enableCustomPrint.setOnCheckedChangeListener(this);

		enableTimeOnOff.setChecked(MyPreferences.getBooleanPrefrences(CLERK_TIME_ON_OFF, mContext));
		enableTimeOnOff.setOnCheckedChangeListener(this);

		orderAssignSwitch.setChecked(MyPreferences.getBooleanPrefrences(CLERK_ORDER_ASSIGN, mContext));
		orderAssignSwitch.setOnCheckedChangeListener(this);

		clerkSwitch.setChecked(MyPreferences.getBooleanPrefrences(CLERK_REPORTING, mContext));
		clerkSwitch.setOnCheckedChangeListener(this);

		collapseSwitch.setChecked(MyPreferences.getBooleanPrefrences(OPTION_ITEM_EXPAND, mContext));
		collapseSwitch.setOnCheckedChangeListener(this);

		enableSocketSwitch.setChecked(MyPreferences.getBooleanPrefrences(IS_SOCKET_NEEDED, mContext));
		enableSocketSwitch.setOnCheckedChangeListener(this);

		enableEnGatSwitch.setChecked(MyPreferences.getBooleanPrefrences(ENCRYPTED_PAY_ENABLE, mContext));
		enableEnGatSwitch.setOnCheckedChangeListener(this);

		resetStoreSwitch.setOnCheckedChangeListener(this);

		qrCodeSwitch.setChecked(MyPreferences.getBooleanPrefrences(QR_CODE_PRINTING, mContext));
		qrCodeSwitch.setOnCheckedChangeListener(this);

		barCodeSwitch.setChecked(MyPreferences.getBooleanPrefrences(BAR_CODE_PRINTING, mContext));
		barCodeSwitch.setOnCheckedChangeListener(this);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_checked, android.R.id.text1, LIST_ITEMS){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view     = super.getView(position, convertView, parent);
				TextView text = (TextView) view.findViewById(android.R.id.text1);
				text.setTextColor(Color.WHITE);
				return view;
			}			
		};

		long position  = MyPreferences.getLongPreference(POS_STORE_TYPE, mContext);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);		
		listView.setItemChecked((int)position, true);

		saveBtn.setOnClickListener (this);
		resetBtn.setOnClickListener(this);	
		startAuto.setOnClickListener(this);
		resetAutoStart.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.Fragment_Other_Settings_BUTTON_AutoStart:
			
			int hours = (int)MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.HOURS_OF_DAY, mContext);
			int mint  = (int)MyPreferences.getLongPreferenceWithDiffDefValue(PrefrenceKeyConst.MINUTES, mContext);

			TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					MyPreferences.setLongPreferences(PrefrenceKeyConst.HOURS_OF_DAY,hourOfDay, mContext);					
					MyPreferences.setLongPreferences(PrefrenceKeyConst.MINUTES,minute, mContext);
					startAuto.setText("Auto Start("+MyStringFormat.onIntFormat(hourOfDay)+":"+MyStringFormat.onIntFormat(minute)+")");
				}
			},hours, mint, true);
			timePickerDialog.show();

			break;

		case R.id.Fragment_Other_Settings_BUTTON_Reset_AutoStart:
			startAuto.setText("Auto Start(03:00)");
			MyPreferences.setLongPreferences(PrefrenceKeyConst.HOURS_OF_DAY,3, mContext);					
			MyPreferences.setLongPreferences(PrefrenceKeyConst.MINUTES,0, mContext);			
			break;

		case R.id.Fragment_Other_Settings_BUTTON_SAVE:
			Toast.makeText(mContext, "Successfully Saved", Toast.LENGTH_SHORT).show();
			break;

		case R.id.Fragment_Other_Settings_BUTTON_RESET:

			MyPreferences.setBooleanPrefrences(OPTION_ITEM_EXPAND  , false,  mContext);
			MyPreferences.setBooleanPrefrences(IS_SOCKET_NEEDED    , false,  mContext);
			MyPreferences.setBooleanPrefrences(ENCRYPTED_PAY_ENABLE, false,  mContext);
			MyPreferences.setLongPreferences(POS_STORE_TYPE        , 0, mContext);
			MyPreferences.setBooleanPrefrences(BAR_CODE_PRINTING, false, mContext);
			MyPreferences.setBooleanPrefrences(QR_CODE_PRINTING, false, mContext);
			MyPreferences.setBooleanPrefrences(CLERK_REPORTING   , false, mContext);
			MyPreferences.setBooleanPrefrences(CLERK_ORDER_ASSIGN, false, mContext);
			MyPreferences.setBooleanPrefrences(CLERK_TIME_ON_OFF , false, mContext);
			MyPreferences.setBooleanPrefrences(IS_CUSTOM_OPTION_PRINT_ON_OFF, false, mContext);

			collapseSwitch.setChecked(false);
			enableSocketSwitch.setChecked(false);
			enableEnGatSwitch.setChecked(false);
			barCodeSwitch.setChecked(false);
			qrCodeSwitch.setChecked(false);
			clerkSwitch.setChecked(false);
			orderAssignSwitch.setChecked(false);
			enableTimeOnOff.setChecked(false);
			enableCustomPrint.setChecked(false);
			listView.setItemChecked(0, true);

			if(gApp.getSocketIo() != null)
				gApp.getSocketIo().disconnetSocket();

			startAuto.setText("Auto Start(03:00)");
			MyPreferences.setLongPreferences(PrefrenceKeyConst.HOURS_OF_DAY,3, mContext);					
			MyPreferences.setLongPreferences(PrefrenceKeyConst.MINUTES,0, mContext);

			Toast.makeText(mContext, "Successfully Reseted", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {


		case R.id.Fragment_Other_Settings_Supress_Custom_Printing:
			MyPreferences.setBooleanPrefrences(IS_CUSTOM_OPTION_PRINT_ON_OFF, isChecked, mContext);
			break;

		case R.id.Fragment_Other_Settings_ClerkOrder:
			MyPreferences.setBooleanPrefrences(CLERK_ORDER_ASSIGN, isChecked, mContext);
			break;

		case R.id.Fragment_Other_Settings_ClerkReporting:
			MyPreferences.setBooleanPrefrences(CLERK_REPORTING, isChecked, mContext);
			break;

		case R.id.Fragment_Other_Settings_SWITCH_Collapse_Items:
			MyPreferences.setBooleanPrefrences(OPTION_ITEM_EXPAND, isChecked,  mContext);
			break;

		case R.id.Fragment_Other_Settings_SWITCH_Enable_Tablet:
			MyPreferences.setBooleanPrefrences(IS_SOCKET_NEEDED, isChecked,  mContext);
			if(!isChecked && gApp.getSocketIo() != null )
			{
				gApp.getSocketIo().disconnetSocket();
			}
			break;

		case R.id.Fragment_Other_Settings_SWITCH_Encrypted_Gateway:
			MyPreferences.setBooleanPrefrences(ENCRYPTED_PAY_ENABLE, isChecked,  mContext);
			break;

		case R.id.Fragment_Other_Settings_ResetStore:
			if(isChecked)
				ResetStore.onResetApp(mContext, resetStoreSwitch);
			break;

		case R.id.Fragment_Other_Settings_QrCode:
			MyPreferences.setBooleanPrefrences(QR_CODE_PRINTING, isChecked,  mContext);
			break;

		case R.id.Fragment_Other_Settings_BarCode:
			MyPreferences.setBooleanPrefrences(BAR_CODE_PRINTING, isChecked, mContext);
			break;
		case R.id.Fragment_Other_Settings_ClerkTimeOnOff:
			if(isChecked)
				new DomainNameDialog(activity).onSetDoaminName(enableTimeOnOff);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		MyPreferences.setLongPreferences(POS_STORE_TYPE, position, mContext);
	}
}

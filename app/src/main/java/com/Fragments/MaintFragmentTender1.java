package com.Fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;

public class MaintFragmentTender1 extends BaseFragment implements OnClickListener, OnItemLongClickListener {

	private Button saveBtn,resetBtn,addBtn;
	private ListView paymentModeListView;
	private List<String> paymentModeList = new ArrayList<>();
	private ArrayAdapter<String> adapter;

	public MaintFragmentTender1(){}	

	public static String getCustom1Name(Context mContext){
		return MyPreferences.getMyPreference(PrefrenceKeyConst.CUSTOM_1, mContext,"Custom Tender 1");
	}
	
	public static String getCustom2Name(Context mContext){
		return MyPreferences.getMyPreference(PrefrenceKeyConst.CUSTOM_2, mContext,"Custom Tender 2");
	}
	
	public static void setCustom1Name(Context mContext){
		MyPreferences.setMyPreference(PrefrenceKeyConst.CUSTOM_1, "Custom Tender 1", mContext);
	}
	
	public static void setCustom2Name(Context mContext){
		MyPreferences.setMyPreference(PrefrenceKeyConst.CUSTOM_2, "Custom Tender 2", mContext);
	}

	private void loadArrayList(){
		paymentModeList.clear();
		paymentModeList.add("Credit");
		paymentModeList.add("Cash");
		paymentModeList.add("Check");
		paymentModeList.add("Rewards");
		paymentModeList.add("Unrecorded");
		paymentModeList.add(getCustom1Name(mContext));
		paymentModeList.add(getCustom2Name(mContext));	
	}

	private void newAdapter(){
		adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_checked, android.R.id.text1, paymentModeList){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view =  super.getView(position, convertView, parent);
				((CheckedTextView)view.findViewById(android.R.id.text1)).setTextColor(Color.parseColor("#ffffff"));
				return view;
			}
		};
		paymentModeListView.setAdapter(adapter);	
		adapter.notifyDataSetChanged();
	}

	private void enableDisableOption(){
		paymentModeListView.setItemChecked(0, MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.CREDIT_TENDER, mContext));
		paymentModeListView.setItemChecked(1, MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.CASH_TENDER, mContext));
		paymentModeListView.setItemChecked(2, MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.CHECK_TENDER, mContext));
		paymentModeListView.setItemChecked(3, MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.TENDER_TENDER, mContext));
		paymentModeListView.setItemChecked(4, MyPreferences.getBooleanPreferencesWithDefalutTrue(PrefrenceKeyConst.UNRECORED_TENDER, mContext));
		paymentModeListView.setItemChecked(5, MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.CUSTOM_1_TENDER, mContext));
		paymentModeListView.setItemChecked(6, MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.CUSTOM_2_TENDER, mContext));
	}

	private void resetAllBooleanValue(){

		setCustom1Name(mContext);
		setCustom2Name(mContext);
		MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CREDIT_TENDER   ,true , mContext);
		MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CASH_TENDER     ,true , mContext);
		MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CHECK_TENDER    ,true , mContext);
		MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.TENDER_TENDER   ,true , mContext);
		MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.UNRECORED_TENDER,true , mContext);
		MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CUSTOM_1_TENDER ,false, mContext);
		MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CUSTOM_2_TENDER ,false, mContext);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadArrayList();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView            = inflater.inflate(R.layout.fragment_tender_setting, container, false);		
		saveBtn             = findViewIdAndCast(R.id.Fragment_Tender_Settings_BUTTON_SAVE);	
		resetBtn            = findViewIdAndCast(R.id.Fragment_Tender_Settings_BUTTON_RESET);
		addBtn              = findViewIdAndCast(R.id.Fragment_Tender_Settings_BUTTON_ADD);
		paymentModeListView = findViewIdAndCast(R.id.Fragment_Tender_List_View);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		saveBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		paymentModeListView.setOnItemLongClickListener(this);
		newAdapter();
		enableDisableOption();
	}

	public void onChangeTenderName(final int position){

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Enter New Name Of Tender!").setTitle(mContext.getString(R.string.app_name));

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		linearLayout.addView(editText);
		builder.setCancelable(false);
		builder.setView(linearLayout);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(editText.getText().toString().isEmpty()){
					ToastUtils.showOwnToast(mContext, "Tender Name Can't Be Empty.");
					onChangeTenderName(position);
					return;
				}

				if(position == 5)
					MyPreferences.setMyPreference(PrefrenceKeyConst.CUSTOM_1, editText.getText().toString(), mContext);
				else if (position == 6)
					MyPreferences.setMyPreference(PrefrenceKeyConst.CUSTOM_2, editText.getText().toString(), mContext);

				loadArrayList();

				adapter.notifyDataSetChanged();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {}

		});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.Fragment_Tender_Settings_BUTTON_ADD:
			//onAddNewTender(null);
			break;

		case R.id.Fragment_Tender_Settings_BUTTON_SAVE:	

			SparseBooleanArray array = paymentModeListView.getCheckedItemPositions();
			for(int index = 0 ; index < array.size() ; index++){
				boolean enableOrNot = array.get(index);

				switch (index) {

				case 0:
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CREDIT_TENDER   ,enableOrNot , mContext);
					break;

				case 1:
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CASH_TENDER     ,enableOrNot , mContext);
					break;

				case 2:
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CHECK_TENDER    ,enableOrNot , mContext);
					break;

				case 3:
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.TENDER_TENDER    ,enableOrNot , mContext);
					break;

					
				case 4:
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.UNRECORED_TENDER ,enableOrNot , mContext);
					break;

				
				case 5:
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CUSTOM_1_TENDER ,enableOrNot, mContext);
					break;

				case 6:
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CUSTOM_2_TENDER ,enableOrNot, mContext);
					break;

				default:
					break;
				}
			}
			ToastUtils.showOwnToast(mContext, "All Changes Saved Successfully");
			break;

		case R.id.Fragment_Tender_Settings_BUTTON_RESET:

			AlertDialog alertDialog = new AlertDialog.Builder(mContext)
			.setIcon(R.drawable.app_icon)
			.setTitle(R.string.app_name)
			.setMessage("Continue, To Reset All Saved Tender Settings")
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {}
			})
			.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					resetAllBooleanValue();
					loadArrayList();
					enableDisableOption();
					adapter.notifyDataSetChanged();
				}
			}).create();
			alertDialog.show();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long lo) {
		if(position == 5 || position == 6){
			onChangeTenderName(position);
		}
		return true;
	}
}

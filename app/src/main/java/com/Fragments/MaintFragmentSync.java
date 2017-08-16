package com.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dialog.NoInternetDialog;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.utils.InternetConnectionDetector;
import com.utils.ToastUtils;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;
import com.posimplicity.R;

import com.posimplicity.SyncActivity;

public class MaintFragmentSync extends BaseFragment implements OnClickListener,WebCallBackListener,WebServiceCallObjectIds{

	private Button fullAppSync,selectedItemsSync;
	private String[] apiSNeedToCall = new String []{ "Products" ,"Categories","Customers","Products Custom Options"};
	private int selectedPosition = -1;
	private ArrayAdapter<String> arrayAdapter;
	private ListView apiModeList;
	private int clickedPosition = -1;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	

		rootView      		= inflater.inflate(R.layout.fragment_sync_settings, container, false);
		fullAppSync   		= findViewIdAndCast(R.id.Fragment_Sync_Settings_BT_Full_App);	
		selectedItemsSync   = findViewIdAndCast(R.id.Fragment_Sync_Settings_BT_SelectedItems);
		apiModeList         = findViewIdAndCast(R.id.Fragment_Sync_Settings_ListView_Items);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		fullAppSync.setOnClickListener(this);
		selectedItemsSync.setOnClickListener(this);
		arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_checked, android.R.id.text1, apiSNeedToCall);

		//apiModeList.setAdapter(arrayAdapter);
		
	}
	
	public int  anyItemSelectedFromListView(){
		int  isSelected = -1;
		SparseBooleanArray checked = apiModeList.getCheckedItemPositions();

		for (int i = 0; i < arrayAdapter.getCount(); i++) {
			if (checked.get(i)) {
				isSelected = i;
				break;
			}
		}
		return isSelected;    
	}


	@Override
	public void onClick(View v) {
		Boolean isInternetPresent = InternetConnectionDetector.isInternetAvailable(mContext);

		switch (v.getId()) {

		case R.id.Fragment_Sync_Settings_BT_Full_App:
			clickedPosition = 0;
			if(isInternetPresent) {	
				onSyncStart(mContext, "Continue , To Sync Full APP");						
			}
			else
				NoInternetDialog.noInternetDialogShown(mContext);

			break;

		case R.id.Fragment_Sync_Settings_BT_SelectedItems:
			clickedPosition  = 1;

			if(isInternetPresent) {					
				selectedPosition    = anyItemSelectedFromListView();
				if(selectedPosition < 0){
					ToastUtils.showOwnToast(mContext, "Please Select Any Item From List");
					return ;
				}
				onSyncStart(mContext, "Continue , To Sync Selected Items");		
			}
			else
				NoInternetDialog.noInternetDialogShown(mContext);

			break;

		default:
			break;
		}
	}

	public  void onSyncStart(final Context mContext, String dialogMsg){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage(dialogMsg);
		builder.setCancelable(false);
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (clickedPosition) {
				case 0:
					
					Intent intent = new Intent(mContext, SyncActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);	
					
					break;
				case 1:

					break;

				default:
					break;
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();		
		builder.create();		
	}

	@Override
	public void onCallBack(WebServiceCall webServiceCall, String responseData,
			int responseCode) {
		// TODO Auto-generated method stub
		
	}
}

package com.posimplicity.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.ArrayAdapter;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.FixedStorage;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;

public class DejavoDebitCreditOption {

 public interface YesNoCallBack
 {
	 void onYes(String string);
	 void onNo();
 } 
	public static boolean isDejavoPromptEnable(Context mContext){
		
		if(!MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PRMOPT_D_C, mContext))
			MyPreferences.setMyPreference(PrefrenceKeyConst.DEJAVO_PAYMENT_TYPE, "Credit", mContext);
		
		return MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PRMOPT_D_C, mContext);
	}
	
	public static void showDejavoOptionListDialog(final YesNoCallBack yesNoCallBack, final Context context){

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice,FixedStorage.LIST_ITMES_DEJAVOO);

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);    
		builderSingle.setTitle("Select Dejavo Payment Type Option :-");
		builderSingle.setIcon(R.drawable.app_icon);
		builderSingle.setCancelable(false);
		builderSingle.setAdapter(arrayAdapter,  new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {				
				MyPreferences.setMyPreference(PrefrenceKeyConst.DEJAVO_PAYMENT_TYPE, FixedStorage.LIST_ITMES_DEJAVOO[which],context);
				yesNoCallBack.onYes(FixedStorage.LIST_ITMES_DEJAVOO[which]);				
			}
		});
		
		builderSingle.setNegativeButton("Cancel", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ToastUtils.showOwnToast(context, "Please Select Any Payment Type For Proceed.");
				yesNoCallBack.onNo();
			}
		});
		AlertDialog alertDialog = builderSingle.show();	
		alertDialog.setCanceledOnTouchOutside(false);
	}
}

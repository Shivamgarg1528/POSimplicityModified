package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import com.posimplicity.R;
import com.posimplicity.SyncActivity;

public class NoInternetDialog {

	public static void noInternetDialogShown(Context _mContext){
		AlertDialog.Builder builder = new AlertDialog.Builder(_mContext);
		builder.setIcon(R.drawable.app_icon).setMessage(R.string.String_No_Interent_Available).setTitle(R.string.app_name).setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		AlertDialog al = builder.create();
		al.show();
	}
	
	public static void noInternetDialogShownWhenSyncStart(final Context _mContext){
		AlertDialog.Builder builder = new AlertDialog.Builder(_mContext);
		builder.setIcon(R.drawable.app_icon)
		.setMessage(R.string.String_No_Interent_Available)
		.setTitle  (R.string.app_name)
		.setCancelable(false)
		.setPositiveButton(R.string.String_Retry, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = new Intent(_mContext, SyncActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				_mContext.startActivity(intent);
			}
		})
		.setNegativeButton(R.string.String_Cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});

		AlertDialog al = builder.create();
		al.show();
	}	
}

package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.posimplicity.HomeActivity;
import com.posimplicity.R;

public class ClearAllHomeScreenDataDialog {

	public static void onClearHomeScreenDataShown(final Context mContext,final HomeActivity instance)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("Do you realy want to delete all the Items from the list?");
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				instance.resetAllData(mContext,1);
			}
		});		
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {	
				dialog.dismiss();
			}
		});	
		builder.show();
		AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
	}

}

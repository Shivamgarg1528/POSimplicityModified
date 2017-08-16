package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.posimplicity.R;

public class InvalidUserDialog {

	public static void onInvalidUser(Context mContext){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.app_name)
		.setIcon(R.drawable.app_icon)
		.setMessage("Please Try Again. If You Are Unable to Log In Your Account May Be Suspended. Please call 800-239-8794 Ext 1 for Sales.")  
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface alertDialog, int id) {
				alertDialog.dismiss();
			}
		});	

		builder.show();
		builder.create();
	}
}

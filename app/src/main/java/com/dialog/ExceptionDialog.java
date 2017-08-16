package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.posimplicity.R;

public class ExceptionDialog {
	public static void onExceptionOccur(Context _mContext){
		AlertDialog.Builder builder = new AlertDialog.Builder(_mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Please Try Again Later").setTitle(R.string.app_name).setPositiveButton("Ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		AlertDialog al = builder.create();
		al.show();
	}
}

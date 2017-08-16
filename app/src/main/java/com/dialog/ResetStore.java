package com.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.widget.Switch;

import com.posimplicity.database.POSDatabaseHandler;
import com.utils.MyPreferences;

import com.utils.ToastUtils;
import com.posimplicity.R;
import com.posimplicity.SplashActivity;

public class ResetStore {

	public static void onResetApp(final Context _mContext, final Switch switch1){
		AlertDialog.Builder builder = new AlertDialog.Builder(_mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Do You Want To Reset The Store ?").setTitle(R.string.app_name).setPositiveButton("Conitinue", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				_mContext.deleteDatabase(POSDatabaseHandler.DATABASE_NAME);
				MyPreferences.resetAllPreferences(_mContext);
				ToastUtils.showOwnToast(_mContext, "Reset Store SuccessFully");
				Intent intent = new Intent(_mContext,SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				_mContext.startActivity(intent);
				((Activity) _mContext).finish();

			}
		});
		builder.setNegativeButton("Cancel",  new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch1.setChecked(false);
			}
		});

		AlertDialog al = builder.create();
		al.show();
	}
}

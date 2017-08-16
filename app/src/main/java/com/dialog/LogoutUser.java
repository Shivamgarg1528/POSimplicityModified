package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.StartAndroidActivity;
import com.utils.ToastUtils;
import com.posimplicity.OperatorActivity;
import com.posimplicity.R;

public class LogoutUser implements PrefrenceKeyConst{	

	private Context mContext;


	public LogoutUser(Context mContext) {
		super();
		this.mContext = mContext;
	}


	public void onUserLogout(){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("Continue, To Logout");
		builder.setCancelable(false);

		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ToastUtils.showOwnToast(mContext, "Logout Successfully");
				StartAndroidActivity.onActivityStart(true, mContext, OperatorActivity.class);
			}
		});		

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {	
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = builder.create();		
		alertDialog.show();
	}

}

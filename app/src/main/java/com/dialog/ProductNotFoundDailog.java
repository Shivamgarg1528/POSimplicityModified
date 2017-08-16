package com.dialog;


import com.posimplicity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ProductNotFoundDailog {	
	private Context mContext;

	public ProductNotFoundDailog(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void showProductNotFoundDailog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
		alert.setIcon(R.drawable.app_icon);
		alert.setTitle("ProductList Not Existed");
		alert.setMessage("There is no ProductList for provided Information");
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {					
				dialog.dismiss();
			}
		});
		alert.show();
	}

}

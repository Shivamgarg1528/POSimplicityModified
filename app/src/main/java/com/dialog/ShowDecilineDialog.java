package com.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.posimplicity.R;

public class ShowDecilineDialog {

	public static void onShow(final Context mContext)
	{
		Toast ImageToast = new Toast(mContext);
        LinearLayout toastLayout = new LinearLayout(mContext);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView image = new ImageView(mContext);
        image.setImageResource(R.drawable.carddecline);
        toastLayout.addView(image);
        ImageToast.setGravity(Gravity.CENTER, 0, 0);
        ImageToast.setView(toastLayout);
        ImageToast.setDuration(Toast.LENGTH_SHORT);
        ImageToast.show();
	
	}

	public static void onWrongInput(final Context mContext){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.app_name);
		builder.setMessage("Please Swipe Again !!! ");
		builder.setIcon(R.drawable.app_icon);
		builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {						
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}); 
		AlertDialog alert = builder.create();
		alert.show();
		alert.setCanceledOnTouchOutside(false);
	}

	public static void onSHowErroeMsg(Context mContext, Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Error FOund");
		builder.setMessage(errors.toString());
		builder.setIcon(R.drawable.app_icon);
		builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {						
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}); 
		AlertDialog alert = builder.create();
		alert.show();
		alert.setCanceledOnTouchOutside(false);
		
	}
}

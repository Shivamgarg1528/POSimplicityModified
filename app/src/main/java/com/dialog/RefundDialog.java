package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.AsyncTasks.RefundReportInMagento;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.utils.Variables;
import com.posimplicity.R;

public class RefundDialog implements PrefrenceKeyConst{

	public static void showRefundDialog(final Context mContext,final float cash, final float credit, final float check, final float gift, final float rewards, final float custom1Amt, final float custom2Amt,String msg,final int returnTypeCode)
	{
		final String transId = MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("OK to Refund these Items with "+msg+"?");
		builder.setCancelable(false);
		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		editText.setHint("Enter Tip Amount");
		editText.setHintTextColor(Color.DKGRAY);
		editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		linearLayout.addView(editText);
		builder.setView(linearLayout);

		builder.setPositiveButton("Continue With Tip", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				float tipAmount  = 0.0f;
				try{
					tipAmount = Float.parseFloat(editText.getText().toString());
				}
				catch(Exception ex){
					ex.printStackTrace(); 
				}				
				if(credit > 0)
					Variables.refundByCC = true;
				new RefundReportInMagento(mContext,transId,cash,credit,check,gift,rewards,custom1Amt,custom2Amt,tipAmount,returnTypeCode).execute();
			}
		});	

		builder.setNeutralButton("Skip Tip", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {	
				dialog.dismiss();
				if(credit > 0)
					Variables.refundByCC = true;
				new RefundReportInMagento(mContext,transId,cash,credit,check,gift,rewards,custom1Amt,custom2Amt,0.0f,returnTypeCode).execute();
			}
		});	

		builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {	
				dialog.dismiss();}
		});	
		builder.show();
		AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
	}
}

package com.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.PosInterfaces.PrefrenceKeyConst;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;

public class DomainNameDialog implements WebServiceCallObjectIds,PrefrenceKeyConst{

	private AlertDialog alertDialog = null;
	private Context mContext;
	private String storeName;
	private Switch enableTimeOnOff;

	public DomainNameDialog(Context mContext) {
		this.mContext       = mContext;
	}

	public void onSetDoaminName(final Switch enableTimeOnOff){
		this.enableTimeOnOff = enableTimeOnOff;
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Please provide the url / hostname").setTitle(mContext.getString(R.string.app_name));

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		editText.setHint("Enter Text...");
		editText.setText(MyPreferences.getMyPreference(PrefrenceKeyConst.DOMAIN_NAME, mContext));

		linearLayout.addView(editText);
		builder.setCancelable(false);
		builder.setView(linearLayout);
		builder.setPositiveButton("Save", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				storeName                          = editText.getText().toString();
				if(!storeName.isEmpty()) {
					MyPreferences.setMyPreference(PrefrenceKeyConst.DOMAIN_NAME, storeName,mContext);	
					MyPreferences.setMyPreference(PrefrenceKeyConst.CLERK_TIME_ON_OFF_URL, "http://".concat(storeName).concat(".timeclockwizard.com"), mContext);
					MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.CLERK_TIME_ON_OFF, true, mContext);
					enableTimeOnOff.setChecked(true);
				}
				else				
					invalidStoreMethod();				
			}
		});

		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				if(MyPreferences.getMyPreference(PrefrenceKeyConst.DOMAIN_NAME,mContext).isEmpty()){
					enableTimeOnOff.setChecked(false);
				}
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	private void invalidStoreMethod() {
		alertDialog.dismiss();					
		ToastUtils.showOwnToast(mContext, "Please Enter Valid Host Name");
		new DomainNameDialog(mContext).onSetDoaminName(enableTimeOnOff);
	}
}
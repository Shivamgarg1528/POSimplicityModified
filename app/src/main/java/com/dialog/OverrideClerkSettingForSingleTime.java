package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.Beans.POSAuthority;
import com.Beans.RoleInfo;
import com.posimplicity.database.local.RoleTable;
import com.posimplicity.database.local.SecurityTable;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.ToastUtils;
import com.posimplicity.R;

public class OverrideClerkSettingForSingleTime  implements PrefrenceKeyConst {

	private AlertDialog alertDialog = null;
	private Context mContext;
	private String password;
	private RoleInfo managerRole = null;
	private POSAuthority posAutho;

	public OverrideClerkSettingForSingleTime(Context mContext, POSAuthority posAutho) {
		this.mContext              = mContext;	
		this.managerRole           = new RoleTable(mContext).getSingleInfoFromTableByRoleName(SecurityTable.Manager);
		this.posAutho              = posAutho;
	}

	public void overrideClerkSettings(){

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage(String.format("Enter Manager Password To Unlock '%s' Settings For Single Use",posAutho.getSettingName())).setTitle(mContext.getString(R.string.app_name) + "Login ID");

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setHint("Enter Manager Password");
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

		linearLayout.addView(editText);

		builder.setCancelable(false);
		builder.setView(linearLayout);
		builder.setPositiveButton("Verify", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				password    = editText.getText().toString();
				alertDialog.dismiss();

				if(password.isEmpty()){
					ToastUtils.showOwnToast(mContext, "Please Enter Manager's Password .");
					new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
					return;
				}

				else{
					if(managerRole.isRoleActive() && managerRole.getRolePassword().equalsIgnoreCase(password)){
						executeAppropraiteTask();
					}
					else{
						ToastUtils.showOwnToast(mContext, "Password Match Failed . Please Check The Password !!!");
						new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
						return;
					}
				}
			}
		});

		builder.setNegativeButton("Leave", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	private void executeAppropraiteTask() {
		posAutho.setSettingOverrideByManager(true);
		new SecurityTable(mContext).updateInfoInTable(posAutho);
		ToastUtils.showOwnToast(mContext, String.format("'%s' Unlocked Successfully",posAutho.getSettingName()));

	}
}
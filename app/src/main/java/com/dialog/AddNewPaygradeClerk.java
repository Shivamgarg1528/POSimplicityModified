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
import com.Beans.StaffModel;
import com.posimplicity.database.local.StaffTable;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.ToastUtils;
import com.posimplicity.AddClerkPaygradeActivity;
import com.posimplicity.R;

public class AddNewPaygradeClerk implements PrefrenceKeyConst {

	private AlertDialog alertDialog = null;
	private Context mContext;
	private String staffName;
	private String staffPayGrade;
	private AddClerkPaygradeActivity activity;

	public AddNewPaygradeClerk(Context mContext, AddClerkPaygradeActivity addClerkScreen) {
		this.mContext       = mContext;
		this.activity       = addClerkScreen;
	}

	public void onAddNewEmployee(){
		
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Enter New Employee Info").setTitle(mContext.getString(R.string.app_name));

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setHint("Enter Employee Name");
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		final EditText editText1 = new EditText(mContext);
		editText1.setSingleLine(true); 
		editText1.setTextColor(Color.BLACK);
		editText1.setHint("Enter Employee PayGrade Amount");		
		editText1.setGravity(Gravity.CENTER);
		editText1.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		editText1.setTypeface(tf);
		editText1.setImeOptions(EditorInfo.IME_ACTION_DONE);

		
		linearLayout.addView(editText);
		linearLayout.addView(editText1);
		

		builder.setCancelable(false);
		builder.setView(linearLayout);

		builder.setPositiveButton("Save", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				staffName                          = editText.getText().toString();
				staffPayGrade                      = editText1.getText().toString();
				alertDialog.dismiss();

				if(staffName.isEmpty() && staffPayGrade.isEmpty()){
					ToastUtils.showOwnToast(mContext, "Please Enter StaffName And PayGrade Amount");
					new AddNewPaygradeClerk(mContext,activity).onAddNewEmployee();
				}
				else{
					new StaffTable(mContext).addInfoInTable(new StaffModel("0", staffName, staffPayGrade, false, false));
					activity.dataList.clear();
					activity.dataList.addAll(new StaffTable(mContext).getAllInfoFromTableDefalut());
					activity.mAdapter.notifyDataSetChanged();
					ToastUtils.showOwnToast(mContext, "Record Saved SuccessFully");	
				}
			}
		});

		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}
}
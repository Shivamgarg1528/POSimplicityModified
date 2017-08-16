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
import com.Beans.CommentModel;
import com.posimplicity.database.local.PayoutDescTable;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.ToastUtils;
import com.posimplicity.AddDescriptionActivity;
import com.posimplicity.R;

public class AddNewPayoutDesc implements PrefrenceKeyConst {

	private AlertDialog alertDialog = null;
	private Context mContext;
	private String orderComment;
	private AddDescriptionActivity activity;

	public AddNewPayoutDesc(Context mContext, AddDescriptionActivity addClerkScreen) {
		this.mContext       = mContext;
		this.activity       = addClerkScreen;	
	}

	public void onAddNewPayoutDesc(){

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Enter New Description").setTitle(mContext.getString(R.string.app_name));

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setHint("Enter Payout Description Here");
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		linearLayout.addView(editText);
		

		builder.setCancelable(false);
		builder.setView(linearLayout);

		builder.setPositiveButton("Save", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				orderComment                       = editText.getText().toString();
				alertDialog.dismiss();

				if(orderComment.isEmpty()){
					ToastUtils.showOwnToast(mContext, "Please Enter Payout Description Before Save.");
					new AddNewPayoutDesc(mContext,activity).onAddNewPayoutDesc();
				}
				else{
					new PayoutDescTable(mContext).addInfoInTable(new CommentModel(orderComment,false));
					activity.dataList.clear();
					activity.dataList.addAll(new PayoutDescTable(mContext).getAllInfoFromTableDefalut());
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
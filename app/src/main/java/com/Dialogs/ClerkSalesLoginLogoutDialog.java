package com.Dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.dialog.ClerkLoginLogoutTimeDialog;
import com.Beans.CustomerModel;
import com.adapter.ClerkInfoOrderAdapter;
import com.posimplicity.database.CustomerTable;
import com.utils.StartAndroidActivity;
import com.utils.ToastUtils;
import com.posimplicity.R;
import com.posimplicity.TenderActivity;

public class ClerkSalesLoginLogoutDialog extends BaseDialog implements View.OnClickListener {

	private ListView listView;
	private ClerkInfoOrderAdapter clerkInfoAdapter;
	private List<CustomerModel> dataList;
	private List<CustomerModel> newUpdatedList;
	private Button submitBtn;
	private ImageButton cancelBtn;
	private TextView dialogName;
	private int caseValue = 0;
	
	public ClerkSalesLoginLogoutDialog(Context context, int theme, int width,int height, boolean isOutSideTouch, boolean isCancelable,int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dataList          = new ArrayList<>();
		newUpdatedList    = new ArrayList<>();

		listView          = findViewByIdAndCast(R.id.Dialog_Clerk_ListView_Name); 
		submitBtn         = findViewByIdAndCast(R.id.Dialog_Clerk_BTN_Submit);
		cancelBtn         = findViewByIdAndCast(R.id.Dialog_Clerk_ImgBtn_Cancel);
		dialogName        = findViewByIdAndCast(R.id.Dialog_Clerk_TextView_Name);

		clerkInfoAdapter  = new ClerkInfoOrderAdapter(mContext, dataList);
		listView.setAdapter(clerkInfoAdapter);

		submitBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

	}

	private boolean checkUpdatedList(){

		newUpdatedList.clear();
		for(int index = dataList.size() - 1 ; index >= 0 ; index --){
			if(dataList.get(index).isRowSelected())
				newUpdatedList.add(dataList.get(index));
		}
		return newUpdatedList.size() > 0;
	}

	public void show(int login) {
		show();
		caseValue = login;
		switch (login) {

		case ClerkLoginLogoutTimeDialog.LOGIN:
			dialogName.setText("Select Any Available User To Login");
			dataList.clear();
			dataList.addAll(new CustomerTable(mContext).getInfoFromTableBasedOnLoginStatus(false));
			Collections.sort(dataList);
			break;

		case ClerkLoginLogoutTimeDialog.LOGOUT:
			dialogName.setText("Select Any Available User To Logout");
			dataList.clear();
			dataList.addAll(new CustomerTable(mContext).getInfoFromTableBasedOnLoginStatus(true));
			Collections.sort(dataList);
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.Dialog_Clerk_BTN_Submit:
			if(checkUpdatedList()){
				new CustomerTable(mContext).updateInfoListInTable(newUpdatedList);
				ToastUtils.showOwnToast(mContext, "Updated SuccessFully");
				dismiss();
				
				if(caseValue == ClerkLoginLogoutTimeDialog.LOGIN)
					StartAndroidActivity.onActivityStart(false, mContext, TenderActivity.class);
			}
			else
				ToastUtils.showOwnToast(mContext, "You didn't Make Any Choice Yet");

			break;

		case R.id.Dialog_Clerk_ImgBtn_Cancel:
			dismiss();

			break;

		default:
			break;
		}

	}
}


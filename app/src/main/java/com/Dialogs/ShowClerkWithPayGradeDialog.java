package com.Dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dialog.ClerkLoginLogoutTimeDialog;
import com.Beans.StaffModel;
import com.adapter.ClerkInfoAdapter;
import com.posimplicity.database.local.StaffTable;
import com.PosInterfaces.MyWebClientClass;
import com.utils.ToastUtils;
import com.posimplicity.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ShowClerkWithPayGradeDialog extends BaseDialog implements View.OnClickListener {

	private ListView listView;
	private ClerkInfoAdapter clerkInfoAdapter;
	private List<StaffModel> dataList;
	private List<StaffModel> newUpdatedList;
	private Button submitBtn;
	private ImageButton cancelBtn;
	private TextView dialogName;
	private MyWebClientClass webClientClass;
	private String url;
	public ShowClerkWithPayGradeDialog(Context context, int theme, int width,int height, boolean isOutSideTouch, boolean isCancelable,int layoutId) {
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

		clerkInfoAdapter  = new ClerkInfoAdapter(mContext, dataList);
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

	public void show(int login, MyWebClientClass webClientClass, String string) {
		this.webClientClass = webClientClass;
		this.url            = string;
		switch (login) {

		case ClerkLoginLogoutTimeDialog.LOGIN:
			dialogName.setText("List of All Available User Who Can Login");
			dataList.clear();
			dataList.addAll(new StaffTable(mContext).getAllInfoFromTable(false));
			Collections.sort(dataList);
			break;

		case ClerkLoginLogoutTimeDialog.LOGOUT:
			dialogName.setText("List of All Available User Who Can Logout");
			dataList.clear();
			dataList.addAll(new StaffTable(mContext).getAllInfoFromTable(true));
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
				new StaffTable(mContext).updateInfoListInTable(newUpdatedList);
				ToastUtils.showOwnToast(mContext, "Updated SuccessFully");
				ToastUtils.showOwnToast(mContext, "Total Paygrade Amount for Login Users :"+new StaffTable(mContext).getSumOfPayGradesOfLoginUsers());
				webClientClass.loadRequestedUrl(url);
				dismiss();
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

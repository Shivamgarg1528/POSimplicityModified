package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.Beans.CommentModel;
import com.adapter.CommentAdapter;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowCommentDailog extends BaseDialog implements View.OnClickListener {

	public ShowCommentDailog(Context context, int theme, int width, int height,boolean isOutSideTouch, boolean isCancelable, int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	private ListView listView;
	private CommentAdapter commentInfoAdapter;
	private List<CommentModel> dataList;
	private Button submitBtn;
	private ImageButton cancelBtn;
	private TextView dialogName;
	private int positonOfSelectedComment;
	private EditText customCommentEdt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dataList          = new ArrayList<>();
		listView          = findViewByIdAndCast(R.id.Dialog_Clerk_ListView_Name); 
		submitBtn         = findViewByIdAndCast(R.id.Dialog_Clerk_BTN_Submit);
		cancelBtn         = findViewByIdAndCast(R.id.Dialog_Clerk_ImgBtn_Cancel);
		dialogName        = findViewByIdAndCast(R.id.Dialog_Clerk_TextView_Name);
		customCommentEdt  = findViewByIdAndCast(R.id.DIALOG_Clerk_EditText_Custom_Comment);

		dialogName.setText("List Of All Available Comments");
		dataList.clear();
		//dataList.addAll(new CommentTable(mContext).getAllInfoFromTable(true));
		Collections.sort(dataList);		

		commentInfoAdapter  = new CommentAdapter(mContext, dataList);
		listView.setAdapter(commentInfoAdapter);

		submitBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

	}

	private boolean checkUpdatedList(){

		int count = 0;
		for(int index = dataList.size() - 1 ; index >= 0 ; index --){
			if(dataList.get(index).isCommentSelected()){
				count ++;
				positonOfSelectedComment = index;
			}
		}
		return count == 1;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.Dialog_Clerk_BTN_Submit:

			boolean isAnyItemSelected    = checkUpdatedList();
			boolean isCustomCommentExist = false;
			String  comment              = "";

			if(!customCommentEdt.getText().toString().isEmpty()){
				isCustomCommentExist = true;
				comment              = customCommentEdt.getText().toString();
			}

			if(isAnyItemSelected && isCustomCommentExist){
				ToastUtils.showOwnToast(mContext, "You Selected The Order's Comment And Custom Comment");
				Variables.orderComment = dataList.get(positonOfSelectedComment).getCommentString() + " . " + comment + "\n";
				dismiss();			
			}

			else if(isCustomCommentExist){
				ToastUtils.showOwnToast(mContext, "You Selected The Custom Comment");
				Variables.orderComment = comment + "\n";
				dismiss();			
			}

			else if(isAnyItemSelected){
				ToastUtils.showOwnToast(mContext, "You Selected The Order's Comment");
				Variables.orderComment = dataList.get(positonOfSelectedComment).getCommentString() + "\n";
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

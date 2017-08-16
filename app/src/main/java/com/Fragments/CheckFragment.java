package com.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.AsyncTasks.CompleteReportInMagento;
import com.AsyncTasks.ShareOrderWithCustomer;
import com.Dialogs.AssignmentPopUp;
import com.Dialogs.CustomerAssociationPopUp;
import com.Dialogs.ShowCommentDailog;
import com.RecieptPrints.GoForPrint;
import com.utils.CalculateWidthAndHeigth;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.Variables;
import com.dialog.ShowSplitPopUpOnConditions;
import com.posimplicity.R;


public class CheckFragment extends BaseFragment implements OnClickListener {

	private TextView checkedAmountTextview;
	private float checkAmount = 0.0f;
	private Button checkBtn,customerBtn,clerkBtn,splitBtn,commentBtn;
	private ImageButton cancelButton;
	private final String PAYMENT_MODE = "checkmo";
	private String ORDER_STATUS = "complete";
	private final int CLERK_ASSIGN_DIALOG = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(MyPreferences.getLongPreference(POS_STORE_TYPE, mContext) == MaintFragmentOtherSetting.QUICK_ )
			ORDER_STATUS = "pending";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView              = inflater.inflate(R.layout.fragment_checkpayment, null);
		checkedAmountTextview = findViewIdAndCast(R.id.subtotalAmount);
		cancelButton          = findViewIdAndCast(R.id.btn_cancel_cheque);
		checkBtn              = findViewIdAndCast(R.id.btn_cheque);

		customerBtn           = findViewIdAndCast(R.id.btn_customer);
		clerkBtn              = findViewIdAndCast(R.id.assingClerk);
		splitBtn              = findViewIdAndCast(R.id.check_split);
		commentBtn            = findViewIdAndCast(R.id.commentBtnCheckPayment);
		return rootView;
	}

	public void onChequeAmountUpdate()
	{
		checkAmount  = Variables.totalBillAmount - Variables.cashAmount - Variables.giftAmount - Variables.ccAmount - Variables.rewardsAmount;		
		String value = MyStringFormat.formatWith2DecimalPlaces(checkAmount);
		checkedAmountTextview.setText(value);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);		

		cancelButton.setOnClickListener(this);
		checkBtn.setOnClickListener(this);
		customerBtn.setOnClickListener(this);
		clerkBtn.setOnClickListener(this);
		splitBtn.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		onChequeAmountUpdate();

	}

	private void doPayment() {
		
		if(!localInsatnceOfHome.isShopingCartEmpty()) {
			Variables.checkAmount = checkAmount;
			new CompleteReportInMagento(mContext,ORDER_STATUS,PAYMENT_MODE,true).execute();

			if(!Variables.billToName.isEmpty())
				new ShareOrderWithCustomer(mContext).execute();

			GoForPrint goForPrint = new GoForPrint(mContext, GoForPrint.CHECQUE_FRAGMENT,true);
			goForPrint.onExectue();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.check_split:
			int width  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() , 35);
			int height = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 45);
			new ShowSplitPopUpOnConditions(mContext,width,height,checkAmount,PAYMENT_MODE,ORDER_STATUS).letShow();
			break;

		case R.id.btn_cancel_cheque:
			((Activity) mContext).finish();
			break;

		case R.id.btn_cheque:
			doPayment();
			break;

		case R.id.btn_customer:

			int width1   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() ,70);
			int height1  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),80);
			new CustomerAssociationPopUp(mContext, R.style.myCoolDialog, width1, height1, false, true, R.layout.dialog_assign_trans).show();
			break;

		case R.id.assingClerk:

			int width2   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() ,70);
			int height2  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),80);
			new AssignmentPopUp(mContext, R.style.myCoolDialog, width2, height2, false, true, R.layout.dialog_assign_trans).show(CLERK_ASSIGN_DIALOG);

			break;

		case R.id.commentBtnCheckPayment:

			int width3   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() ,60);
			int height3  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),70);
			new ShowCommentDailog(mContext, R.style.myCoolDialog, width3, height3, false, true, R.layout.dialog_comment_for_order).show();


			break;

		default:
			break;
		}
	}}

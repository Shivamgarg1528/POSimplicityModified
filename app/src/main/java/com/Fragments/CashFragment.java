package com.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dialog.ShowDecilineDialog;
import com.dialog.ShowSplitPopUpOnConditions;
import com.AsyncTasks.CompleteReportInMagento;
import com.AsyncTasks.ShareOrderWithCustomer;
import com.Dialogs.AssignmentPopUp;
import com.Dialogs.CustomerAssociationPopUp;
import com.Dialogs.ShowCommentDailog;
import com.RecieptPrints.GoForPrint;
import com.utils.CalculateWidthAndHeigth;
import com.utils.FixedStorage;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.TenderViewTextSet;
import com.utils.Variables;
import com.posimplicity.R;

public class CashFragment extends BaseFragment {

	private TextView enterAmtTV,subAmtTv;
	private float enteredAmt,subtotalAmt;
	private StringBuilder operationalStrBul = null;
	private static final String PAYMENT_MODE     = "cashondelivery";
	private static final int CLERK_ASSIGN_DIALOG = 0;	
	private String ORDER_STATUS  = "complete";

	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		operationalStrBul  = new StringBuilder("000");
		if(MyPreferences.getLongPreference(POS_STORE_TYPE, mContext) == MaintFragmentOtherSetting.QUICK_ )
			ORDER_STATUS = "pending";
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_cashpayment, null);		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		subAmtTv         = findViewIdAndCast(R.id.subtotalAmtTextview);
		enterAmtTV       = findViewIdAndCast(R.id.enteredValueTextview);
		subtotalAmt      = Variables.totalBillAmount - Variables.cashAmount - Variables.giftAmount - Variables.ccAmount - Variables.rewardsAmount;		

		subAmtTv.setText(MyStringFormat.formatWith2DecimalPlaces(subtotalAmt));
		enterAmtTV.setText("0.00");
		
	}	

	private void cashPaymentProcess() {

		subtotalAmt      = Float.parseFloat(MyStringFormat.formatWith2DecimalPlaces(subtotalAmt));
		enteredAmt       = TenderViewTextSet.getAmountFromTextView(enterAmtTV);

		//System.out.println("SubTotalAmt ->   "+ subtotalAmt);
		//System.out.println("EnterAmt    ->   "+ enteredAmt);

		if (!localInsatnceOfHome.isShopingCartEmpty()) {

			if (subtotalAmt > enteredAmt) {
				float leftAmoutToPay  = subtotalAmt - enteredAmt;
				Variables.cashAmount += enteredAmt;	
				subAmtTv.setText(MyStringFormat.formatWith2DecimalPlaces(leftAmoutToPay));
				subAmtTv.setTextColor(Color.RED);
				subtotalAmt = leftAmoutToPay;
				enterAmtTV.setText("0.00");
				enteredAmt = 0.0f;
				Variables.cashAfterChange = Variables.cashAmount;
			}

			else if (subtotalAmt <= enteredAmt) {

				Variables.changeAmt   = enteredAmt - subtotalAmt;
				Variables.cashAmount += enteredAmt;	
				Variables.cashAfterChange = Variables.cashAmount - Variables.changeAmt;	

				new CompleteReportInMagento(mContext,ORDER_STATUS,PAYMENT_MODE,true).execute();

				if (!Variables.billToName.isEmpty()) {
					new ShareOrderWithCustomer(mContext).execute();
				}

				GoForPrint goForPrint = new GoForPrint(mContext, 0,true);
				goForPrint.onExectue();
			}
			operationalStrBul  = new StringBuilder("000");  // Resetting the Value 
		}
	}


	public void onCashFragmentClick(View v) {
		try{
			switch (v.getId()) {

			case R.id.commentsBtn:


				int width3   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() ,60);
				int height3  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),70);
				new ShowCommentDailog(mContext, R.style.myCoolDialog, width3, height3, false, true, R.layout.dialog_comment_for_order).show();


				break;

			case R.id.customerDialog:

				int width1   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() ,70);
				int height1  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),80);
				new CustomerAssociationPopUp(mContext, R.style.myCoolDialog, width1, height1, false, true, R.layout.dialog_assign_trans).show();


				break;

			case R.id.assingClerk:

				int width2   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() ,70);
				int height2  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),80);
				new AssignmentPopUp(mContext, R.style.myCoolDialog, width2, height2, false, true, R.layout.dialog_assign_trans).show(CLERK_ASSIGN_DIALOG);

				break;

			case R.id.splitbillBtn:

				int width  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth() , 35);
				int height = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 45);

				new ShowSplitPopUpOnConditions(mContext,width,height,subtotalAmt,PAYMENT_MODE,ORDER_STATUS).letShow();

				break;	

			case R.id.number1:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.ONE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number2:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.TWO_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number3:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.THREE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number4:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.FOUR_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number5:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.FIVE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number6:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.SIX_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number7:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.SEVEN_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number8:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.EIGHT_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number9:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.NINE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number0:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.ZERO_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number00:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.DOUBLE_ZERO_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.backspaceButton:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,"",FixedStorage.REM_CHAR,false);
				break;

			case R.id.btn_$5:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.FIVE_NEW,FixedStorage.ADD_CHAR,true);
				break;

			case R.id.btn_$10:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.TEN_,FixedStorage.ADD_CHAR,true);
				break;

			case R.id.btn_$20:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.TWENTY_,FixedStorage.ADD_CHAR,true);
				break;

			case R.id.btn_$50:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.FIFITY_,FixedStorage.ADD_CHAR,true);
				break;

			case R.id.btn_$100:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.HUNDRED_,FixedStorage.ADD_CHAR,true);
				break;

			case R.id.btn_$500:
				TenderViewTextSet.setTextOnView(enterAmtTV,operationalStrBul,FixedStorage.FIVE_HUNDRED_,FixedStorage.ADD_CHAR,true);
				break;

			case R.id.btn_cash:
				cashPaymentProcess();
				break;

			case R.id.btn_cancelTender:
				((Activity) mContext).finish();
				break;
			default:
				break;
			}

		}
		catch (Exception e) {
			ShowDecilineDialog.onSHowErroeMsg(mContext,e);
		}
	}
}

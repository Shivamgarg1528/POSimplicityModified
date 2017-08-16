package com.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.AsyncTasks.CompleteReportInMagento;
import com.AsyncTasks.ShareOrderWithCustomer;
import com.PosInterfaces.MyWebClientClass;
import com.RecieptPrints.GoForPrint;
import com.utils.FixedStorage;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.TenderViewTextSet;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.dialog.ShowDecilineDialog;
import com.posimplicity.R;

public class TenderCardFragment extends BaseFragment {

	private Button inquryBtn;
	private ImageButton closeBtn;
	private LinearLayout mainLinearLayout;
	private WebView webView;
	private ProgressBar progressBar;
	private TextView valueEnteredTextview;
	private TextView subtotalTextview,totalAmtTextview;
	private float enteredAmt = 0.0f;	
	private float subtotalAmt= 0.0f;
	private final String PAYMENT_MODE = "pay";
	private String ORDER_STATUS       = "complete";
	private StringBuilder operationalStrBul = null;
	private int selectedRewards = -1;
	private static final int FLAG_REWARDS = 100;
	private static final int FLAG_GIFT    = 200;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		operationalStrBul  = new StringBuilder("000");
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView             = inflater.inflate(R.layout.fragment_giftc_card, null);
		closeBtn             = findViewIdAndCast(R.id.Fragment_Gift_Card_Tender_Btn_Close);
		mainLinearLayout     = findViewIdAndCast(R.id.Fragment_Gift_Card_LL_Main);
		webView              = findViewIdAndCast(R.id.webview);	
		progressBar          = findViewIdAndCast(R.id.progressBar1);
		subtotalTextview     = findViewIdAndCast(R.id.subtotalAmtTextview);
		totalAmtTextview     = findViewIdAndCast(R.id.totalAmtTextview);
		valueEnteredTextview = findViewIdAndCast(R.id.enteredValueTextview);
		inquryBtn            = findViewIdAndCast(R.id.balanceInquiry);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		subtotalAmt = Variables.totalBillAmount - Variables.cashAmount - Variables.giftAmount - Variables.ccAmount - Variables.rewardsAmount - Variables.taxAmount;
		subtotalTextview.setText(MyStringFormat.formatWith2DecimalPlaces(subtotalAmt));
		totalAmtTextview.setText(MyStringFormat.formatWith2DecimalPlaces(Variables.totalBillAmount));
		totalAmtTextview.setVisibility(View.VISIBLE);
		valueEnteredTextview.setText("0.00");

		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) mContext).finish();
			}
		});		

	}

	public void onCashFragmentClick(View v) {
		try{
			switch (v.getId()) {	
			case R.id.number1:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.ONE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number2:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.TWO_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number3:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.THREE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number4:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.FOUR_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number5:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.FIVE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number6:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.SIX_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number7:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.SEVEN_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number8:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.EIGHT_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number9:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.NINE_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number0:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.ZERO_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.number00:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,FixedStorage.DOUBLE_ZERO_,FixedStorage.ADD_CHAR,false);
				break;

			case R.id.backspaceButton:
				TenderViewTextSet.setTextOnView(valueEnteredTextview,operationalStrBul,"",FixedStorage.REM_CHAR,false);
				break;


			case R.id.btn_cash:
				cashPaymentProcess();
				break;

			case R.id.Fragment_Gift_Card_Tender_Btn_Close:
				((Activity) mContext).finish();
				break;

			case R.id.balanceInquiry:

				String url  = FULL_PATH  + MyPreferences.getMyPreference(STORE, mContext);

				url        += SUB_URL    + STATIC_TENDER_URL + "tgiftbalance.htm";
				MyWebClientClass myWebClientClass = new MyWebClientClass(progressBar, webView);
				myWebClientClass.loadRequestedUrl(url);

				break;
			default:
				break;
			}

		}
		catch (Exception e) {
			ShowDecilineDialog.onSHowErroeMsg(mContext,e);
		}
	}

	private void cashPaymentProcess() {

		subtotalAmt      = Float.parseFloat(MyStringFormat.formatWith2DecimalPlaces(subtotalAmt));
		enteredAmt       = TenderViewTextSet.getAmountFromTextView(valueEnteredTextview);

		System.out.println("SubTotalAmt ->   "+ subtotalAmt);
		System.out.println("EnterAmt    ->   "+ enteredAmt);

		if (subtotalAmt > 0) {

			if (enteredAmt <= 0) 
				ToastUtils.showOwnToast(mContext, "Pay Amount Must Be Greater Than 0.00");

			else if (subtotalAmt > enteredAmt) {				

				float leftAmoutToPay  = subtotalAmt - enteredAmt;

				if(selectedRewards == FLAG_GIFT ){
					Variables.giftAmount += enteredAmt;	
					Variables.giftAmtAfterChange = Variables.giftAmount;
				}
				else {
					Variables.rewardsAmount += enteredAmt;
					Variables.rewardsAfterChange = Variables.rewardsAmount;
				}

				subtotalTextview.setText(MyStringFormat.formatWith2DecimalPlaces(leftAmoutToPay));
				subtotalTextview.setTextColor(Color.RED);
				subtotalAmt = leftAmoutToPay;
				valueEnteredTextview.setText("0.00");
				enteredAmt = 0.0f;				
			}

			else if (subtotalAmt <= enteredAmt) {

				Variables.changeAmt   = enteredAmt - subtotalAmt;
				if(selectedRewards == FLAG_GIFT ){
					Variables.giftAmount += enteredAmt;	
					Variables.giftAmtAfterChange = Variables.giftAmount - Variables.changeAmt;	
				}
				else
				{
					Variables.rewardsAmount += enteredAmt;	
					Variables.rewardsAfterChange = Variables.rewardsAmount - Variables.changeAmt;	
				}

				new CompleteReportInMagento(mContext,ORDER_STATUS,PAYMENT_MODE,true).execute();

				if (!Variables.billToName.isEmpty()) {
					new ShareOrderWithCustomer(mContext).execute();
				}

				GoForPrint goForPrint = new GoForPrint(mContext, 3,true);
				goForPrint.onExectue();
			}
			operationalStrBul  = new StringBuilder("000");  // Resetting the Value 
		}
		else
			ToastUtils.showOwnToast(mContext, "Subtotal Amount Must Be Greater Than 0.00");
	}

	public void showTenderFragmentOptions()
	{
		int savedRewards  = (int) MyPreferences.getLongPreferenceWithDiffDefValue(REWARDS, mContext);

		if(savedRewards < 0)
			ToastUtils.showOwnToast(mContext, "Upgrade Needed - Contact POSimplicity at 800-239-8794");
		else if(savedRewards == MaintFragmentCurrentRewards.POS_CARD_REWRDS){
			inquryBtn.setVisibility(View.INVISIBLE);
			mainLinearLayout.setVisibility(View.VISIBLE);
			String url  = FULL_PATH  + MyPreferences.getMyPreference(STORE, mContext);
			url        += SUB_URL    + STATIC_POS_URL + "redeempospoints.htm";
			MyWebClientClass myWebClientClass = new MyWebClientClass(progressBar, webView);
			myWebClientClass.loadRequestedUrl(url);
			selectedRewards = FLAG_REWARDS;
		}
		else if(savedRewards == MaintFragmentCurrentRewards.TENDER_CARD_REWRDS){

			final CharSequence[] items = {"Gift Card  ", "Rewards"};

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Select TenderCard:-");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.dismiss();
					switch (item) {
					case 0:
						mainLinearLayout.setVisibility(View.VISIBLE);
						String url  = FULL_PATH  + MyPreferences.getMyPreference(STORE, mContext);
						url        += SUB_URL    + STATIC_TENDER_URL + "tgiftredeem.htm";
						MyWebClientClass myWebClientClass = new MyWebClientClass(progressBar, webView);
						myWebClientClass.loadRequestedUrl(url);
						selectedRewards = FLAG_GIFT;
						break;

					case 1:

						mainLinearLayout.setVisibility(View.VISIBLE);
						String url1  = FULL_PATH  + MyPreferences.getMyPreference(STORE, mContext);
						url1        += SUB_URL    + STATIC_TENDER_URL + "trewardsredeem.htm";
						MyWebClientClass myWebClientClass1 = new MyWebClientClass(progressBar, webView);
						myWebClientClass1.loadRequestedUrl(url1);
						selectedRewards = FLAG_REWARDS;

						break;

					default:
						break;
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
}

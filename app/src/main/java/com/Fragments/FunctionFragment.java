package com.Fragments;

import org.json.JSONObject;
import com.dialog.ClerkLoginLogoutTimeDialog;
import com.dialog.PayoutDialog;
import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.posimplicity.database.local.SecurityTable;
import com.Dialogs.TSYSReturnAdjustment;
import com.Dialogs.TSYSTipAdjustment;
import com.Dialogs.DejavooAdjustmentDialog;
import com.PosInterfaces.AddCustomerInterface;
import com.PosInterfaces.MyWebClientClass;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CalculateWidthAndHeigth;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.SecurityVerification;
import com.utils.ToastUtils;
import com.posimplicity.FunctionDrawerActivity;
import com.posimplicity.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class FunctionFragment extends BaseFragment implements AddCustomerInterface, OnClickListener{

	private WebView webView;
	private ProgressBar progressBar;
	private MyWebClientClass webClientClass;
	public final String interfaceName = "POS_ADD_CUSTOMER";
	private String storeName ;
	private int position = 0;
	public DejavooAdjustmentDialog tipAdjusment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null){
			position = bundle.getInt(FunctionDrawerActivity.PAGE_POSITION);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView         = inflater.inflate(R.layout.activity_functions, container, false);
		//webView          = findViewIdAndCast(R.id.Fragment_Function_WebView_webview);
		//progressBar      = findViewIdAndCast(R.id.Fragment_Function_ProgressBar_Loading);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		storeName = MyPreferences.getMyPreference(STORE, mContext);
		showView(position);
	}

	public void showView(int position){
		webClientClass = new MyWebClientClass(progressBar, webView);
		webView.addJavascriptInterface(this,interfaceName);
		webView.clearHistory();
		webView.clearCache(true);


		switch (position) {

		case FunctionDrawerActivity.FUNCTION_ADD_CUSTOMER:
			webClientClass.loadRequestedUrl(FULL_PATH + storeName + SUB_URL +"functions/addcustomer/addcustomer.htm");
			break;

		case FunctionDrawerActivity.FUNCTION_TIME_CLOCK:

			if(MyPreferences.getBooleanPrefrences(CLERK_REPORTING, mContext))
				new ClerkLoginLogoutTimeDialog(mContext).onClerkLoginLogoutTimeDialog(webClientClass,storeName);
			else if(MyPreferences.getBooleanPrefrences(CLERK_TIME_ON_OFF, mContext))
				//webClientClass.loadRequestedUrl(FULL_PATH + storeName + SUB_URL +"functions/timeclock/gettimeclock.htm");
				webClientClass.loadRequestedUrl(MyPreferences.getMyPreference(PrefrenceKeyConst.CLERK_TIME_ON_OFF_URL, mContext));
			else
				ToastUtils.showOwnToast(mContext, "Please Enable Clerk Reporting / Clerk Time Option From Other Settings");
			break;

		case FunctionDrawerActivity.FUNCTION_TENDER_CARD:
			int tenderPos = (int) MyPreferences.getLongPreferenceWithDiffDefValue(REWARDS, mContext);

			if(tenderPos == MaintFragmentCurrentRewards.TENDER_CARD_REWRDS)
				showTenderOptionDialog();
			else
				ToastUtils.showOwnToast(mContext, "You Need To Enable Tender First");
			break;

		case FunctionDrawerActivity.FUNCTION_DRAWER:
			new SecurityVerification(mContext,SecurityTable.Settings_Drawer).drawerFunctionChecking(mContext);
			break;

		case FunctionDrawerActivity.FUNCTION_PAYOUTS:
			showPayoutOptionDialog();
			break;

		case FunctionDrawerActivity.FUNCTION_ADD_TABLE:
			webClientClass.loadRequestedUrl(FULL_PATH + storeName + SUB_URL +"functions/addtable/addtable.htm");

			break;

		case FunctionDrawerActivity.FUNCTION_ADD_CLERK:
			webClientClass.loadRequestedUrl(FULL_PATH + storeName + SUB_URL +"functions/addclerk/clerktable.htm");
			break;

		case FunctionDrawerActivity.FUNCTION_DEJAVOO_TIP:
			showTipOptionDailog();
			break;

		case FunctionDrawerActivity.FUNCTION_DEJAVOO_RETURN:
			int width11   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 60);
			int height11  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),60);
			tipAdjusment = new DejavooAdjustmentDialog(activity, R.style.myCoolDialog, width11, height11, false, true, R.layout.tip_adjusment_layout);
			tipAdjusment.sendData(DejavooAdjustmentDialog.RETURN_ADJUSTMENT);
			tipAdjusment.show();
			break;

		case FunctionDrawerActivity.FUNCTION_TSYS_TIP:
			int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 50);
			int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),50);
			TSYSTipAdjustment tipAdjusment = new TSYSTipAdjustment(mContext, R.style.myCoolDialog, width, height, false, true, R.layout.dialog_tsys_tip_adjustment);
			tipAdjusment.show();
			break;

		case FunctionDrawerActivity.FUNCTION_TSYS_RETURN:

			int width1   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 50);
			int height1  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),50);
			TSYSReturnAdjustment returnAdjusment = new TSYSReturnAdjustment(mContext, R.style.myCoolDialog, width1, height1, false, true, R.layout.dialog_tsys_return_adjustment);
			returnAdjusment.show();

		default:
			break;
		}
	}

	@JavascriptInterface
	@Override
	public void showErrorMsg() {
		Toast.makeText(mContext, "Error To Record Data", Toast.LENGTH_SHORT).show();
	}

	@JavascriptInterface
	@Override
	public void showCustomerInfo(String jsonData) {
		System.out.println(jsonData);

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			String customerId    = JSONObJValidator.stringTagValidate(jsonObject,    "customer_id", "-1");
			String firstName  = JSONObJValidator.stringTagValidate(jsonObject, "firstname", "");
			String lastName   = JSONObJValidator.stringTagValidate(jsonObject, "lastname", "");
			String email      = JSONObJValidator.stringTagValidate(jsonObject, "email", "");
			String telephhone = JSONObJValidator.stringTagValidate(jsonObject, "telephone", "");
			String groupId    = JSONObJValidator.stringTagValidate(jsonObject, "group_id", CustomerTable.DEFAULT_GROUP_ID);

			String street    = JSONObJValidator.stringFromObj    (jsonObject, "street",     "NULL");
			String city      = JSONObJValidator.stringTagValidate(jsonObject, "city",       "NULL");
			String countryId = JSONObJValidator.stringTagValidate(jsonObject, "country_id", "NULL");
			String region    = JSONObJValidator.stringTagValidate(jsonObject, "region",     "NULL");
			String postcode  = JSONObJValidator.stringTagValidate(jsonObject, "postcode",   "NULL");
			StringBuilder address    = new StringBuilder();
			address.append(street).append(",").append(city).append(",").append(countryId).append(",").append(region).append(",").append(postcode);
			CustomerModel customer = new CustomerModel(customerId, firstName, lastName, email, telephhone, address.toString(),groupId,false,false);
			new CustomerTable(mContext).addInfoInTable(customer);
			Toast.makeText(mContext, "Record Data SuccessFully", Toast.LENGTH_SHORT).show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	public void showTenderOptionDialog(){

		final CharSequence[] items = {"Issue Rewards Card","Issue Gift Card "};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Select TenderCard :-");
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				dialog.dismiss();
				webView.setVisibility(View.VISIBLE);
				String url = FULL_PATH + storeName + SUB_URL + STATIC_TENDER_URL;
				switch (item) {

				case 0:
					System.out.println(url + "trewardsissue.htm");
					webClientClass.loadRequestedUrl(url+"trewardsissue.htm");
					break;

				case 1:
					System.out.println(url + "tgiftissue.htm");
					webClientClass.loadRequestedUrl(url+"tgiftissue.htm");
					break;

				default:
					break;
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showTipOptionDailog(){

		final CharSequence[] items = { "Tip For Retials","Tip For Restaurant" };

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Select Any Tip Adjusment :-");
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				dialog.dismiss();
				int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 60);
				int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(),60);

				tipAdjusment = new DejavooAdjustmentDialog(activity, R.style.myCoolDialog, width, height, false, true, R.layout.tip_adjusment_layout);
				tipAdjusment.sendData(DejavooAdjustmentDialog.TIP_ADJUSMENT);
				tipAdjusment.show();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showPayoutOptionDialog(){


		final CharSequence[] items = {"Lottery Pay Out", "Expense Pay Out", "Supplies Pay Out", "ProductList Purchase", "Other Pay Out","Tip Pay Out","Manual Cash Refund"};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Select Any Payout :-");
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				new PayoutDialog(mContext).showEachPayoutClick(items[item].toString(),item);
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}
}

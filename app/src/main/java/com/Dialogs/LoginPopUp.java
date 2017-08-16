package com.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.PosInterfaces.PrefrenceKeyConst;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.Services.ParseJSONResponse;
import com.utils.ImageProcessing;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;
import com.posimplicity.R;
import com.posimplicity.SetupActivity;

public class LoginPopUp implements WebServiceCallObjectIds,PrefrenceKeyConst,WebCallBackListener{

	private AlertDialog alertDialog = null;
	private Context mContext;
	private String newRequestedUrl;
	private String storeName;

	public LoginPopUp(Context mContext) {
		this.mContext       = mContext;
	}

	public void onLoginDialog(){

		//Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Enter Account Login Name Provided After You Registered!").setTitle(mContext.getString(R.string.app_name) + " Login ID");

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		//editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

		linearLayout.addView(editText);

		TextView sampleTv = new TextView(mContext);
		sampleTv.setText("To A View Demo Installation Please Enter 'DEMO' In The Above Field");
		sampleTv.setGravity(Gravity.CENTER);
		//sampleTv.setTypeface(tf);

		TextView clickHereTv = new TextView(mContext);
		String text = "<font navigation_item_selector=#0033ff>ClickHere </font> <font navigation_item_selector=#000000>To Register For A New Account.</font>";
		clickHereTv.setText(Html.fromHtml(text));
		clickHereTv.setGravity(Gravity.CENTER);
//		clickHereTv.setTypeface(tf);
		clickHereTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean isInternetPresent = InternetConnectionDetector.isInternetAvailable(mContext);
				if(isInternetPresent){
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.posimplicity.com/CreateAccount"));	
					mContext.startActivity(intent);
					alertDialog.dismiss();
				} else	
				{
					NoInternetDialog.noInternetDialogShown(mContext);
				}
			}
		});

		TextView line = new TextView(mContext);
		line.setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.textAppearance_mdpi_01_sp));
		line.setBackgroundColor(Color.BLACK);

		TextView noteTv = new TextView(mContext);
		noteTv.setText("If The Keyboard Does Not Appear Go To "+'"'+"Settings / Apps / Language & Input "+'"'+"and Tap on The Word "+'"'+"Default"+'"'+" and Turn Off the Physical Keyboard");
		noteTv.setGravity(Gravity.CENTER);
		noteTv.setTextColor(Color.RED);
//		noteTv.setTypeface(tf);

		linearLayout.addView(clickHereTv);
		linearLayout.addView(sampleTv);
		linearLayout.addView(line);
		linearLayout.addView(noteTv);

		builder.setCancelable(false);
		builder.setView(linearLayout);
		builder.setPositiveButton("Continue", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				storeName                          = editText.getText().toString();
				if(!storeName.isEmpty()) {
					newRequestedUrl                = "http://"+storeName+SUB_URL+"api/pos.php";
					String requetsedUrl            = newRequestedUrl + "?tag=store_exist";					
					WebServiceCall webServiceCall  = new WebServiceCall(requetsedUrl, "Store Validating...", OBJECT_ID_1, "StoreValidate :", mContext, null, LoginPopUp.this, true, false, false);
					webServiceCall.execute();							
				}
				else				
					invalidStoreMethod();				
			}
		});

		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();			
				((Activity) mContext).finish();
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	@Override
	public void onCallBack(WebServiceCall webServiceCall, String responseData,int responseCode) {

		alertDialog.dismiss();
		webServiceCall.onDismissProgDialog();

		switch (responseCode) {

		case WebServiceCall.WEBSERVICE_CALL_NO_INTERENET:
			NoInternetDialog.noInternetDialogShown(mContext);
			break;

		case WebServiceCall.WEBSERVICE_CALL_EXCEPTION:

			switch (webServiceCall.getWebServiceId()) {
			case OBJECT_ID_1:
				ToastUtils.showOwnToast(mContext, "Please Enter Valid Store Name");
				new LoginPopUp(mContext).onLoginDialog();
				break;

			default:
				ExceptionDialog.onExceptionOccur(mContext);
				break;
			}
			break;

		case WebServiceCall.WEBSERVICE_CALL_RESULT_VALID:

			switch (webServiceCall.getWebServiceId()) {

			case OBJECT_ID_1:		
				boolean isvalidStore = ParseJSONResponse.isStoreValid(responseData, mContext);
				if(isvalidStore){
					MyPreferences.setMyPreference(STORE_NAME, newRequestedUrl, mContext);
					String requetsedUrl       = newRequestedUrl + "?tag=get_server_time";
					WebServiceCall serverTimeWebObj  = new WebServiceCall(requetsedUrl, "Setting Up Store...", OBJECT_ID_2, "ServerTime :", mContext, null, LoginPopUp.this, true, false, false);
					serverTimeWebObj.execute();
				}					
				else
					invalidStoreMethod();
				break;

			case OBJECT_ID_2:
				ParseJSONResponse.storeServerTime(responseData, mContext);
				MyPreferences.setMyPreference(STORE, storeName, mContext);
				ImageProcessing.deleteImagesAndFolderFromSdCard(ImageProcessing.FOLDER_NAME);
				ImageProcessing.deleteImagesAndFolderFromSdCard(ImageProcessing.BAR_CODE_IMAGE);				
				mContext.startActivity(new Intent(mContext,SetupActivity.class));
				((Activity) mContext).finish();
				break;


			default:
				break;
			}

		default:
			break;			
		}
	}

	private void invalidStoreMethod() {
		alertDialog.dismiss();					
		ToastUtils.showOwnToast(mContext, "Please Enter Valid Store Name");
		new LoginPopUp(mContext).onLoginDialog();
	}
}
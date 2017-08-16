package com.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.Beans.TSYSResponseModel;
import com.posimplicity.gateway.TSYSGateway;
import com.posimplicity.gateway.TSYSGateway.OnCallBackForTSYS;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;

public class TSYSFragment extends BaseFragment implements OnCallBackForTSYS, OnClickListener {

	public Button generateKeyButton,updatekeyButton;
	private TSYSGateway tsys;
	public  String transactionKey;
	public EditText userNameEditText,passwordEditText,deviceIdEditText,merchantIdEditText;
	private String userNameString , passwordString ,deviceIdString,merchantIdString;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

		rootView         	= inflater.inflate(R.layout.fragment_tsys, container, false);
		generateKeyButton  	= findViewIdAndCast(R.id.Fragment_TSYS_Btn_GenerateKey);		
		updatekeyButton     = findViewIdAndCast(R.id.Fragment_TSYS_Btn_Updatekey);
		userNameEditText    = findViewIdAndCast(R.id.Fragment_TSYS_Edt_UserName);
		passwordEditText    = findViewIdAndCast(R.id.Fragment_TSYS_Edt_Password);
		deviceIdEditText    = findViewIdAndCast(R.id.Fragment_TSYS_Edt_DeviceId);
		merchantIdEditText  = findViewIdAndCast(R.id.Fragment_TSYS_Edt_Merchant_Id);
		transactionKey 		= MyPreferences.getMyPreference(TSYS_TRANSACTION_KEY, mContext);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);

		generateKeyButton.setOnClickListener(this);
		updatekeyButton.setOnClickListener(this);


		if(!transactionKey.isEmpty())
			generateKeyButton.setEnabled(false);
		else
			updatekeyButton.setEnabled(false);

		userNameEditText.setText(MyPreferences.getMyPreference(TSYS_USER_NAME, mContext));
		passwordEditText.setText(MyPreferences.getMyPreference(TSYS_PASSWORD, mContext));
		deviceIdEditText.setText(MyPreferences.getMyPreference(TSYS_DEVICE_ID, mContext));
		merchantIdEditText.setText(MyPreferences.getMyPreference(TSYS_MERCHA_ID, mContext));
		

	}	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.Fragment_TSYS_Btn_GenerateKey:
			onKeyPressed(TSYSGateway.TSYS_KEY_GENERATION);
			break;

		case R.id.Fragment_TSYS_Btn_Updatekey:
			onKeyPressed(TSYSGateway.TSYS_KEY_UPGRADTION);
			break;

		default:
			break;
		}
	}

	public void onKeyPressed(int requestCode){

		userNameString    = userNameEditText.getText().toString();
		passwordString    = passwordEditText.getText().toString();
		deviceIdString    = deviceIdEditText.getText().toString();
		merchantIdString  = merchantIdEditText.getText().toString();
		

		if(userNameString.isEmpty() || passwordString.isEmpty() || deviceIdString.isEmpty() || merchantIdString.isEmpty()){
			ToastUtils.showOwnToast(mContext, "Provide All Required Info For TSYS First");
			return;
		}

		MyPreferences.setMyPreference(TSYS_USER_NAME, userNameString,   mContext);
		MyPreferences.setMyPreference(TSYS_PASSWORD , passwordString,   mContext);
		MyPreferences.setMyPreference(TSYS_DEVICE_ID, deviceIdString,   mContext);
		MyPreferences.setMyPreference(TSYS_MERCHA_ID, merchantIdString, mContext);
		
		tsys = new TSYSGateway(mContext, "", requestCode);
		tsys.onInterfaceRegister(this);
		tsys.doExection();
	}

	@Override
	public void onTSYSResponse(String responseDate, int requestedCodeReturn) {
		if(tsys != null ){
			TSYSResponseModel response        = tsys.paresTSYSResponse(responseDate);
			ToastUtils.showOwnToast(mContext, response.getResponseMsg());
			if(response.isSuccess()){
				transactionKey = MyPreferences.getMyPreference(TSYS_TRANSACTION_KEY, mContext);
				generateKeyButton.setEnabled(false);
				updatekeyButton.setEnabled(true);
			}			
		}
	}

}


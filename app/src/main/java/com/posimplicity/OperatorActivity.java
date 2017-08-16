package com.posimplicity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dialog.ExceptionDialog;
import com.dialog.InvalidUserDialog;
import com.dialog.NoInternetDialog;
import com.Beans.RoleInfo;
import com.Services.UserValidationServices;
import com.utils.MyPreferences;
import com.utils.StartAndroidActivity;
import com.utils.ToastUtils;
import com.utils.VerifyFieldNotEmpty;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;
import com.posimplicity.database.local.RoleTable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperatorActivity extends BaseActivity implements OnClickListener,  WebCallBackListener, OnItemSelectedListener {

	private TextView loginBtn,loginAs;	
	private Spinner spinner ;
	private EditText roleNameEdt,rolePasswordEdt;
	private LinearLayout roleNameLl,rolePasswordLl;
	private String userName,pwd;
	private Animation showViewAnimation,showHideAnimation;
	private boolean isViewVisibile = true;
	private List<RoleInfo> dataList;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_operator_login);
		showViewAnimation = AnimationUtils.makeInAnimation(mContext, true);
		showHideAnimation = AnimationUtils.makeOutAnimation(mContext, true);
		dataList          = new ArrayList<>();
		dataList.clear();

		// Reseting all Security info First

		MyPreferences.setMyPreference(SECURITY_LOGIN_USER_Id, "Admin", mContext);

		onInitViews();
		onListenerRegister();
		ArrayAdapter<String> spinnerAdapter  = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_activated_1, android.R.id.text1){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view     = super.getView(position, convertView, parent);
				TextView text = (TextView) view.findViewById(android.R.id.text1);
				text.setTextColor(Color.WHITE);
				return view;
			}
		};

		dataList.addAll(new RoleTable(mContext).getAllInfoFromTableDefalut(true));
		Collections.sort(dataList);

		for(int index = 0 ; index < dataList.size() ; index++){
			RoleInfo roleInfo = dataList.get(index);
			if(roleInfo.isRoleActive()){
				spinnerAdapter.add(roleInfo.getRoleName());
			}
		}

		spinner.setAdapter(spinnerAdapter);
		loginAs.setText("Login As "+spinner.getSelectedItem());
	}

	@Override
	public void onInitViews() {
		loginBtn         = findViewByIdAndCast(R.id.login_button);
		roleNameLl       = findViewByIdAndCast(R.id.Activity_Operator_LL_Username);
		rolePasswordLl   = findViewByIdAndCast(R.id.Activity_Operator_LL_Password);
		loginAs          = findViewByIdAndCast(R.id.Activity_Operator_Textview_LoginAs);
		roleNameEdt      = findViewByIdAndCast(R.id.Activity_Operator_Edt_Username);
		rolePasswordEdt  = findViewByIdAndCast(R.id.Activity_Operator_Edt_Password);	
		spinner          = findViewByIdAndCast(R.id.Operator_Activity_Spinner_Roles);
	}

	@Override
	public void onListenerRegister() {
		loginBtn.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button:		
			MyPreferences.setMyPreference(SECURITY_LOGIN_USER_Id, ""+spinner.getSelectedItem(), mContext);
			pwd      = rolePasswordEdt.getText().toString().trim();
			if(spinner.getSelectedItem().equals("Admin")){
				userName = roleNameEdt.getText().toString().trim();
				if(!VerifyFieldNotEmpty.isFieldEmpty(mContext, roleNameEdt, "Enter Name First")){
					if(!VerifyFieldNotEmpty.isFieldEmpty(mContext, rolePasswordEdt, "Enter Password First")){
						String requetsedUrl = MyPreferences.getMyPreference(STORE_NAME, mContext);
						ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
						postParameters.add(new BasicNameValuePair("tag", "login_details"));
						postParameters.add(new BasicNameValuePair("name", userName));
						postParameters.add(new BasicNameValuePair("password", pwd));
						postParameters.add(new BasicNameValuePair("role", "Administrators"));

						WebServiceCall webServiceCall = new WebServiceCall(requetsedUrl+"?", "Validating...", OBJECT_ID_1, " Merchant API :- ", mContext, postParameters,OperatorActivity.this, true, false, true);
						webServiceCall.execute();
					}
				}				
			}
			else
			{
				if(!VerifyFieldNotEmpty.isFieldEmpty(mContext, rolePasswordEdt, "Enter Password First")){

					RoleInfo roleInfo = dataList.get(spinner.getSelectedItemPosition()); 
					if(roleInfo.isRoleOk()){
						if(roleInfo.getRolePassword().equals(pwd)){
							ToastUtils.showOwnToast(mContext, "Success");
							StartAndroidActivity.onActivityStart(true, mContext, HomeActivity.class);						
						}
						else
							rolePasswordEdt.setError("Password Match Failed , Please Contact To Admin");
					}
					else
						rolePasswordEdt.setError("Contact To Admin To Set Password");				
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onCallBack(WebServiceCall webServiceCall, String responseData,int responseCode) {

		webServiceCall.onDismissProgDialog();

		switch (responseCode) {

		case WebServiceCall.WEBSERVICE_CALL_EXCEPTION:
			ExceptionDialog.onExceptionOccur(mContext);
			break;

		case WebServiceCall.WEBSERVICE_CALL_NO_INTERENET:	
			NoInternetDialog.noInternetDialogShown(mContext);
			break;

		case WebServiceCall.WEBSERVICE_CALL_RESULT_VALID:	

			switch (webServiceCall.getWebServiceId()) {

			case OBJECT_ID_1:

				boolean isUserVerified = UserValidationServices.iSUserValid(responseData, mContext);
				if(isUserVerified){
					ToastUtils.showOwnToast(mContext, "Success");
					StartAndroidActivity.onActivityStart(true, mContext, HomeActivity.class);
				}
				else
					InvalidUserDialog.onInvalidUser(mContext);
				break;

			default:
				break;
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {

		for(int index = 0 ; index < dataList.size() ; index++){
			RoleInfo roleInfo = dataList.get(index);
			if(position == index){
				roleInfo.setRoleActive(true);
			}
			else 
				roleInfo.setRoleActive(false);
		}
		loginAs.setText("Login As "+(String) parent.getItemAtPosition(position));
		if(spinner.getSelectedItem().equals("Admin"))
		{
			roleNameLl.setAnimation(showViewAnimation);
			showViewAnimation.start();		
			roleNameLl.setVisibility(View.VISIBLE);
			rolePasswordLl.setVisibility(View.VISIBLE);
			isViewVisibile = true;
		}
		else{
			if(isViewVisibile){
				roleNameLl.setAnimation(showHideAnimation);
				showHideAnimation.start();
				roleNameLl.setVisibility(View.INVISIBLE);
				rolePasswordLl.setVisibility(View.VISIBLE);
				isViewVisibile = false;
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}

}
package com.posimplicity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.Beans.POSAuthority;
import com.Beans.PaymentMode;
import com.Beans.RoleInfo;
import com.posimplicity.database.local.PaymentModeTable;
import com.posimplicity.database.local.RoleTable;
import com.posimplicity.database.local.SecurityTable;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.Services.CustomerGroupService;
import com.Services.CustomerService;
import com.Services.LastOrderIdServices;
import com.Services.ProductOptionService;
import com.Services.ProductService;
import com.utils.InternetConnectionDetector;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.ReadFileFromAsset;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;
import com.utils.WebServiceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends BaseActivity implements WebCallBackListener,WebServiceCallObjectIds {

	private static final int SIZE_OF_WEB_OBJ = 6;

	private ProgressBar syncProgressView;
	private TextView    syncTextView;
	private String baseUrl,deviceId;
	private ArrayList<NameValuePair> postParameters;
	private WebServiceCall[] webServiceObjArray;
	private ArrayList<String>  requestedUrlList;
	private int progressCount = 0;
	private boolean firstDialogAppear = true;

	public static  final String[]  ROLES_NAME = {"Admin","Clerk","Manager","Superviosr"}; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_setup);
		onInitViews();
		onListenerRegister();
		onUrlListCreation();


		boolean isNetPresent = InternetConnectionDetector.isInternetAvailable(mContext);
		if(isNetPresent)
			startSetup();
		else
			NoInternetDialog.noInternetDialogShownWhenSyncStart(mContext);
	}

	private void onAuthorityInfoLoad() {
		try{
			List<POSAuthority> dataList = new ArrayList<>();
			String securityData         = ReadFileFromAsset.readAsString(mContext, "AuthorityInfo.txt");
			JSONObject jsonObj          = new JSONObject(securityData);
			JSONArray jsonArray         = jsonObj.getJSONArray("Details");
			int sizeOfArray             = jsonArray.length();
			if(sizeOfArray > 0 ){				
				for(int index = 0 ; index < sizeOfArray ; index++){
					JSONObject innerJsonObj          = jsonArray.getJSONObject(index);
					String settingName               = JSONObJValidator.stringTagValidate(innerJsonObj, "settingName", "Not Provided");
					boolean isAdminHaveRights        = 1 == JSONObJValidator.intTagValidate(innerJsonObj, "adminEnable", 0);
					boolean isClerkHaveRights        = 1 == JSONObJValidator.intTagValidate(innerJsonObj, "clerkEnable", 0);
					boolean isManagerHaveRights      = 1 == JSONObJValidator.intTagValidate(innerJsonObj, "managerEnable", 0);
					boolean isSuperVisorHaveRights   = 1 == JSONObJValidator.intTagValidate(innerJsonObj, "supervisorEnable", 0);
					POSAuthority posAuthority        = new POSAuthority(settingName, isAdminHaveRights, isClerkHaveRights, isManagerHaveRights, isSuperVisorHaveRights,false);
					dataList.add(posAuthority);
				}
				new SecurityTable(mContext).addInfoListInTable(dataList);			
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}	
	}

	private void onRoleInfoLoad() {
		try{
			List<RoleInfo> dataList     = new ArrayList<>();
			String securityData         = ReadFileFromAsset.readAsString(mContext, "RoleInfo.txt");
			JSONObject jsonObj          = new JSONObject(securityData);
			JSONArray jsonArray         = jsonObj.getJSONArray("Details");
			int sizeOfArray             = jsonArray.length();
			if(sizeOfArray > 0 ){				
				for(int index = 0 ; index < sizeOfArray ; index++){

					JSONObject innerJsonObj       = jsonArray.getJSONObject(index);
					String roleName               = JSONObJValidator.stringTagValidate(innerJsonObj, "roleName", "");
					String rolePassword           = JSONObJValidator.stringTagValidate(innerJsonObj, "rolePassword", "");
					String roleId                 = JSONObJValidator.stringTagValidate(innerJsonObj, "roleId", "-1");
					boolean isRoleActive          = 1 == JSONObJValidator.intTagValidate(innerJsonObj, "roleActive", 0);

					RoleInfo posAuthority         = new RoleInfo(roleName, rolePassword, isRoleActive,roleId);
					dataList.add(posAuthority);
				}
				new RoleTable(mContext).addInfoListInTable(dataList);			
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}	
	}

	private void onPaymentModeLoad() {
		try{
			String paymentData          = ReadFileFromAsset.readAsString(mContext, "PaymentModeListFile.txt");
			Gson gson                   = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			PaymentMode paymentMode     = gson.fromJson(paymentData, PaymentMode.class);
			new PaymentModeTable(mContext).addInfoListInTable(paymentMode.getPaymentMode());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}	
	}


	@Override
	public void onInitViews() {

		syncProgressView        = findViewByIdAndCast(R.id.Activity_Setup_ProgressBar_Setuping_Percentage);
		syncTextView            = findViewByIdAndCast(R.id.Activity_Setup_TextView_Percentage);

		baseUrl                 = MyPreferences.getMyPreference(STORE_NAME, mContext);
		deviceId                = MyPreferences.getMyPreference(ANDROID_DEVICE_ID, mContext);

		postParameters          = new ArrayList<>();
		requestedUrlList        = new ArrayList<>();
		webServiceObjArray      = new WebServiceCall[SIZE_OF_WEB_OBJ];
	}

	@Override
	public void onListenerRegister() {}

	private void onUrlListCreation() {

		requestedUrlList.clear();
		requestedUrlList.add(baseUrl	+"?tag=customer_list");
		requestedUrlList.add(baseUrl	+"?tag=categories_list" );
		requestedUrlList.add(baseUrl	+"?tag=product_options_list");
		requestedUrlList.add(baseUrl	+"?tag=product_list" );
		requestedUrlList.add(baseUrl	+"?tag=last_order_id&device_id="+deviceId);
		requestedUrlList.add(baseUrl	+"?tag=customer_group" );
	}

	private void startSetup(){

		onAuthorityInfoLoad();
		onRoleInfoLoad();
		onPaymentModeLoad();

		for(int index = 0; index <  SIZE_OF_WEB_OBJ; index ++){
			createWebSerObj(index, requestedUrlList.get(index), " No = "+index);
			startSetupTask(webServiceObjArray[index]);
		}
	}


	private void createWebSerObj(int index,String requestedUrl, String apiName){
		webServiceObjArray[index] = new WebServiceCall(requestedUrl, "", OBJECT_ID_ARRAY[index],apiName, mContext, postParameters, this, false, false, false);
	}

	private void startSetupTask(WebServiceCall webServiceCall){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			webServiceCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			webServiceCall.execute();
	}


	@Override
	public void onCallBack(WebServiceCall webServiceCall, String responseData,int responseCode) {


		webServiceCall.onDismissProgDialog();

		switch (responseCode) {

		case WebServiceCall.WEBSERVICE_CALL_NO_INTERENET:	

			if(webServiceCall.isNoInterent() && firstDialogAppear){
				NoInternetDialog.noInternetDialogShown(mContext);
				firstDialogAppear = false;
			}

			break;

		case WebServiceCall.WEBSERVICE_CALL_EXCEPTION:	

			if(webServiceCall.isExceptionOcuur() && firstDialogAppear){
				ExceptionDialog.onExceptionOccur(mContext);
				firstDialogAppear = false;
			}

			break;

		case WebServiceCall.WEBSERVICE_CALL_RESULT_VALID:

			progressCount += 100 / SIZE_OF_WEB_OBJ;

			switch (webServiceCall.getWebServiceId()) {

			case OBJECT_ID_1:		
				CustomerService.parseCustomerDataWhenAdd(responseData, mContext);
				break;

			case OBJECT_ID_2:
				//ParseService.parseCategoryResponse(responseData, mContext);
				break;

			case OBJECT_ID_3:
				ProductOptionService.parsedProductOptionDataWhenAdd(responseData, mContext);
				break;

			case OBJECT_ID_4:
				ProductService.parseProductDataWhenAdd(responseData, mContext);	
				break;

			case OBJECT_ID_5:	
				LastOrderIdServices.getLastId(responseData,mContext,deviceId);
				break;

			case OBJECT_ID_6:
				CustomerGroupService.parseCustomerGroupDataWhenAdd(responseData,mContext);
				break;

			default:
				break;
			}			

			webServiceCall.setTaskCompleted(true);

			break;

		default:
			break;
		}
		syncProgressView.setProgress(progressCount);
		syncTextView.setText("Setting Up ( "+progressCount+"% )...");

		Runnable runnable = new Runnable() {			
			@Override
			public void run() {
				Intent intent = new Intent(mContext, OperatorActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				((Activity) mContext).finish();
			}
		};

		if(!WebServiceUtils.isAnyAsyncTaskLeft(SIZE_OF_WEB_OBJ,webServiceObjArray)){
			syncProgressView.setProgress(100);
			syncTextView.setText("Setup Completed SuccessFully");	
			new Handler().postDelayed(runnable, 1000);
		}
	}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}
}

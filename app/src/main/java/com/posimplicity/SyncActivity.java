package com.posimplicity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.posimplicity.database.POSDatabaseHandler;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.Services.CustomerGroupService;
import com.Services.CustomerService;
import com.Services.LastOrderIdServices;
import com.Services.ProductOptionService;
import com.Services.ProductService;
import com.utils.ImageProcessing;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;
import com.utils.WebServiceUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SyncActivity extends BaseActivity implements WebCallBackListener,WebServiceCallObjectIds{

	private static final int SIZE_OF_WEB_OBJ = 6;

	private ProgressBar syncProgressView;
	private TextView    syncTextView;
	private String baseUrl,deviceId;
	private ArrayList<NameValuePair> postParameters;
	private WebServiceCall[] webServiceObjArray;
	private ArrayList<String>  requestedUrlList;
	private int progressCount = 0;
	private boolean firstDialogAppear = true;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_temp_sync);
		onInitViews();
		onListenerRegister();
		onUrlListCreation();
		boolean isNetPresent = InternetConnectionDetector.isInternetAvailable(mContext);
		if(isNetPresent)
			startSetup();
		else
			NoInternetDialog.noInternetDialogShownWhenSyncStart(mContext);
	}

	@Override
	public void onInitViews() {

		syncProgressView        = findViewByIdAndCast(R.id.Activity_Sync_ProgressBar_Syncing_Percentage);
		syncTextView            = findViewByIdAndCast(R.id.Activity_Sync_TextView_Percentage);

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
		deleteAllDataFromTables();
		ImageProcessing.deleteImagesAndFolderFromSdCard(ImageProcessing.FOLDER_NAME);
		for(int index = 0; index <  SIZE_OF_WEB_OBJ; index ++){
			createWebSerObj(index, requestedUrlList.get(index), " No = "+index);
			startSetupTask(webServiceObjArray[index]);
		}
	}

	private void deleteAllDataFromTables() {
		
		POSDatabaseHandler posDatabaseHandler = POSDatabaseHandler.getInstance(mContext);
		posDatabaseHandler.deleteTableInfo(posDatabaseHandler.getWritableDatabase());
		posDatabaseHandler.close();
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
		syncTextView.setText("Syncing ( "+progressCount+"% )...");

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
			syncTextView.setText("Syncing Completed SuccessFully");	
			new Handler().postDelayed(runnable, 1000);
		}
	}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}
}

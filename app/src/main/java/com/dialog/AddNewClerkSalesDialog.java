package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.Beans.CustomerGroup;
import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerGroupTable;
import com.posimplicity.database.CustomerTable;
import com.PosInterfaces.PrefrenceKeyConst;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.Services.CustomerService;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;
import com.posimplicity.AddClerkSalesActivity;
import com.posimplicity.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class AddNewClerkSalesDialog implements PrefrenceKeyConst, OnItemSelectedListener ,WebServiceCallObjectIds ,WebCallBackListener {

	private AlertDialog mAlertDialog = null;
	private Context mContext;
	private String staffName;
	private AddClerkSalesActivity activity;
	private Spinner sp;
	private List<CustomerGroup> listOfGroup;
	private String staffGroupId =  "-1";


	public AddNewClerkSalesDialog(Context mContext, AddClerkSalesActivity addClerkScreen) {
		this.mContext       = mContext;
		this.activity       = addClerkScreen;
	}

	public void onAddNewClerkSales(){

		listOfGroup = new CustomerGroupTable(mContext).getAllInfoFromTable();
		listOfGroup.add(0,new CustomerGroup("-1", "None"));

		List<String> s                    = new CustomerGroupTable(mContext).getNameOfCustomerGroup();
		s.add(0, "None");

		ArrayAdapter<String> adp          = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, s);
		sp = new Spinner(mContext);
		sp.setAdapter(adp);
		sp.setOnItemSelectedListener(this);

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Enter New Clerk Info").setTitle(mContext.getString(R.string.app_name));

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setHint("Enter Clerk Name");
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		final EditText editText2 = new EditText(mContext);
		editText2.setSingleLine(true); 
		editText2.setTextColor(Color.BLACK);
		editText2.setHint("Enter Employee Password");		
		editText2.setGravity(Gravity.CENTER);
		editText2.setInputType(InputType.TYPE_CLASS_TEXT);
		editText2.setTypeface(tf);
		editText2.setImeOptions(EditorInfo.IME_ACTION_DONE);

		linearLayout.addView(editText);
		linearLayout.addView(editText2);
		linearLayout.addView(sp);

		builder.setCancelable(false);
		builder.setView(linearLayout);

		builder.setPositiveButton("Save", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				staffName                          = editText.getText().toString();
				editText2.getText().toString();
				mAlertDialog.dismiss();

				if(staffName.isEmpty()){
					ToastUtils.showOwnToast(mContext, "Please Enter Clerk Name");
					new AddNewClerkSalesDialog(mContext,activity).onAddNewClerkSales();
				}
				else {
					Long phoneNo = new Long(MyPreferences.getLongPreference(STAFF_ID_IN_LD, mContext));

					CustomerModel customerModel   = new CustomerTable(mContext).getInfoFromTableBasedOnLastRecord();
					if(!customerModel.isCustomerNotValid()){
						phoneNo              = Long.valueOf(customerModel.getCustomerId());							
					}
					phoneNo +=400;					
					MyPreferences.setLongPreferences(STAFF_ID_IN_LD, phoneNo, mContext);

					StringBuilder requetsedUrl    = new StringBuilder(MyPreferences.getMyPreference(STORE_NAME, mContext));
					
					ArrayList<NameValuePair> postParams = new ArrayList<>();
					postParams.add(new BasicNameValuePair("tag"      , "create_customer"));
					postParams.add(new BasicNameValuePair("id"       , ""+phoneNo));
					postParams.add(new BasicNameValuePair("firstname", staffName));
					postParams.add(new BasicNameValuePair("group_id" , staffGroupId));

					WebServiceCall webServiceCall = new WebServiceCall(requetsedUrl.toString(), "Adding Employee ...", OBJECT_ID_1, "Add New Employee :-> ", mContext, postParams, AddNewClerkSalesDialog.this, true, false, true);
					webServiceCall.execute();
				}
			}
		});

		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mAlertDialog.dismiss();
			}
		});

		mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		staffGroupId = listOfGroup.get(position).getCustomerGroupId();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

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

				boolean isRecordInserted = CustomerService.parseCustomerDataWhenAddSales(responseData, mContext);				
				if(isRecordInserted){
					activity.dataList.clear();
					activity.dataList.addAll(new CustomerTable(mContext).getInfoFromTableBasedOnGroupId());
					activity.mAdapter.notifyDataSetChanged();
					ToastUtils.showOwnToast(mContext, "Record Saved SuccessFully");	
				}
				else
				{
					ToastUtils.showOwnToast(mContext, "Failed To Save Last Record");
					new AddNewClerkSalesDialog(mContext, activity).onAddNewClerkSales();
				}

				break;

			default:
				break;
			}

			break;

		default:
			break;
		}
	}
}
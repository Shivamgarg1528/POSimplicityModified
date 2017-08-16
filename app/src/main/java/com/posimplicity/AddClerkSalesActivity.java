package com.posimplicity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.dialog.AddNewClerkSalesDialog;
import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;

public class AddClerkSalesActivity  extends BaseActivity implements OnItemClickListener, WebCallBackListener {

	private ListView listview;
	public List<CustomerModel> dataList;
	public List<CustomerModel> newUpdatedList;
	public MainAdapter mAdapter;
	private StringBuilder staffIdsStrBld;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);


		dataList           = new ArrayList<>();
		newUpdatedList     = new ArrayList<>();

		dataList.clear();
		dataList.addAll(new CustomerTable(mContext).getInfoFromTableBasedOnGroupId());

		Collections.sort(dataList);
		listview        = new ListView(this);

		mAdapter = new MainAdapter(dataList);
		listview.setAdapter(mAdapter);
		listview.setFadingEdgeLength(0);		
		setContentView(listview);
		listview.setOnItemClickListener(this);
	}


	private boolean checkUpdatedList(){

		newUpdatedList.clear();
		for(int index = dataList.size() - 1 ; index >= 0 ; index --){
			if(dataList.get(index).isRowSelected())
				newUpdatedList.add(dataList.get(index));
		}
		return newUpdatedList.size() > 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_clerk_screen, menu);
		return super.onCreateOptionsMenu(menu);		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.AddNewItem:			
			new AddNewClerkSalesDialog(mContext, this).onAddNewClerkSales();
			break;

		case R.id.RemoveItem:

			staffIdsStrBld     = new StringBuilder();

			if(dataList.isEmpty()){
				ToastUtils.showOwnToast(mContext, "No Employee Record Exists");
				return false;			
			}

			if(checkUpdatedList()){

				for(int index = 0 ; index < newUpdatedList.size() ; index ++){
					CustomerModel staffModel = newUpdatedList.get(index);
					if(staffIdsStrBld.length() < 1){
						staffIdsStrBld.append(staffModel.getCustomerId());
					}
					else
					{
						staffIdsStrBld.append(","+staffModel.getCustomerId());
					}
				}	

				StringBuilder requetsedUrl  = new StringBuilder(MyPreferences.getMyPreference(STORE_NAME, mContext));
				requetsedUrl.append("?tag=delete_customer");
				requetsedUrl.append("&id="+staffIdsStrBld.toString());

				WebServiceCall webServiceCall = new WebServiceCall(requetsedUrl.toString(), "Employee Deleting...", OBJECT_ID_1, "Delete Employee :-> ", mContext, null, AddClerkSalesActivity.this, true, false, false);
				webServiceCall.execute();				
			}
			else
				ToastUtils.showOwnToast(mContext, "Select Any Employee Before Delete");

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MainAdapter extends ArrayAdapter<CustomerModel> {

		private LayoutInflater mInflater;
		private List<CustomerModel> dataList;

		private class Holder {
			public CheckedTextView textview;
		}

		public MainAdapter(List<CustomerModel> dataList) {
			super(mContext, 0, dataList);
			this.mInflater  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.dataList   = dataList;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			final CustomerModel staffModel = this.dataList.get(position);
			final Holder holder;
			if (convertView == null) {
				convertView        = mInflater.inflate(android.R.layout.simple_list_item_checked, null);
				holder             = new Holder();
				holder.textview    = (CheckedTextView) convertView.findViewById(android.R.id.text1);
				holder.textview.setTextColor(0xFFFFFFFF);
				convertView.setBackgroundColor(0xFF202020);				
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			holder.textview.setText(staffModel.getFirstName());
			holder.textview.setChecked(staffModel.isRowSelected());
			return convertView;
		}
	}
	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}

	@Override
	public void onInitViews() {}

	@Override
	public void onListenerRegister() {}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		dataList.get(position).setRowSelected(!dataList.get(position).isRowSelected());	
		mAdapter.notifyDataSetChanged();
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

				try{
					StringBuilder faliedCustomer = new StringBuilder();
					staffIdsStrBld = new StringBuilder();
					JSONObject level_1_JsonObj    = new JSONObject(responseData);
					JSONArray  level_1_JsonArr    = level_1_JsonObj.getJSONArray("details");
					int length                    = level_1_JsonArr.length();

					if(length > 0){
						for(int index = 0 ; index < length ; index ++){
							JSONObject level_2_JsonObj  = level_1_JsonArr.getJSONObject(index);
							String cutomerId            = JSONObJValidator.stringTagValidate(level_2_JsonObj, "customer_id", "-1");
							boolean status              = JSONObJValidator.stringTagValidate(level_2_JsonObj, "status", "failed").equalsIgnoreCase("success");
							if(status){
								if(staffIdsStrBld.length() < 1)
									staffIdsStrBld.append(cutomerId);
								else
									staffIdsStrBld.append(","+cutomerId);								
							}
							else{
								if(faliedCustomer.length() < 1)
									faliedCustomer.append(cutomerId);
								else
									faliedCustomer.append(","+cutomerId);
							}
						}

						if(faliedCustomer.length() > 0)
							ToastUtils.showOwnToast(mContext, "Falied To Delete Following Employee  "+faliedCustomer);
						
						new CustomerTable(mContext).deleteInfoListFromTable(staffIdsStrBld.toString());
						if(newUpdatedList.size() == 1)
							ToastUtils.showOwnToast(mContext, "Records Deleted Successfully");
						else
							ToastUtils.showOwnToast(mContext, "Record Deleted Successfully");

						dataList.clear();
						dataList.addAll(new CustomerTable(mContext).getInfoFromTableBasedOnGroupId());
						mAdapter.notifyDataSetChanged();

					}
					else				
						ToastUtils.showOwnToast(mContext, "Falied To Delete CustomerParent");
				}
				catch(Exception ex){
					ex.printStackTrace();
					ToastUtils.showOwnToast(mContext, "Falied To Delete CustomerParent");
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

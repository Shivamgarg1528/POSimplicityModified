package com.posimplicity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.AlertDialogWithOptions.FetchOrderOptionDialog;
import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.Beans.CheckOutParentModel;
import com.Beans.CustomerModel;
import com.Beans.ExtraProductArgument;
import com.Beans.PendingOrderModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.adapter.PendingDetailsAdapter;
import com.adapter.PendingOrderAdapter;
import com.posimplicity.database.CustomerTable;
import com.posimplicity.database.ProductOptionTable;
import com.posimplicity.database.ProductTable;
import com.utils.CurrentDate;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.StartAndroidActivity;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ReprintActivity extends BaseActivity implements OnClickListener, OnItemClickListener, WebCallBackListener, OnItemSelectedListener {

	private List<PendingOrderModel> listOfAllOrder;
	private HomeActivity instance;
	private Button pullBackBtn;
	private ListView orderIdListView;
	public  PendingOrderAdapter orderIdsAdapter;
	public  PendingDetailsAdapter orderDetailsAdapter;
	public  ExpandableListView orderDeatilesListview;
	private TextView noOrderExistTv;
	private LinearLayout mainLinearLayout;
	private List<String> listOfClerk;
	private Spinner spinner;
	boolean anExceptionOccur;
	private int selectedPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_reprint);
		onInitViews();
		onListenerRegister();
		new FetchOrderOptionDialog(mContext,this).showFetchOptionsDialog();
	}

	public void showView(int index){	
		StringBuilder requetsedUrl = new StringBuilder(MyPreferences.getMyPreference(STORE_NAME, mContext));
		requetsedUrl.append("?tag=fetch_order_based_on_requirement");
		requetsedUrl.append("&caseValue=" + index);		
		listOfAllOrder.clear();
		orderIdsAdapter.notifyDataSetChanged();
		WebServiceCall webServiceCall = new WebServiceCall(requetsedUrl.toString(), "Please Wait...", OBJECT_ID_1, "Order Id :", mContext, null, this, true, false, false);
		
		switch (index) {
		
		case 0:
		case 1:	
		case 2:	
			webServiceCall.execute();
			break;
			
		/*case 2:
			
			int width1   = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(), 50);
			int height1  = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(),50);
			
			SelectCustomDate selectCustomDate = new SelectCustomDate(mContext, R.style.myCoolDialog, width1, height1, false, false, R.layout.custom_date_dialog);
			selectCustomDate.show(webServiceCall);
			
			break;*/

		default:
			break;
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.reprint, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.Refersh) {
			Variables.tableOrClerkShipToNameModel  = Variables.customerOrClerkBillToNameModel =  null;
			StartAndroidActivity.onActivityStart(true, mContext, ReprintActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}

	@Override
	public void onInitViews() {

		instance                  =  HomeActivity.localInstance;

		orderIdListView           = findViewByIdAndCast(R.id.Activity_Pending_ListView_Order_Id);
		orderDeatilesListview     = findViewByIdAndCast(R.id.Activity_Pending_ExpandableList_View_Order_Info);
		pullBackBtn               = findViewByIdAndCast(R.id.Activity_Pending_Btn_Pull_Order);
		noOrderExistTv            = findViewByIdAndCast(R.id.Activity_Pending_Btn_TV_No_Order);
		mainLinearLayout          = findViewByIdAndCast(R.id.Activity_Pending_AllData_LL);
		spinner                   = findViewByIdAndCast(R.id.Activity_Pending_Spinner_Sort_Order);

		listOfAllOrder            =  new ArrayList<>();
		listOfClerk               =  new ArrayList<>();

		orderIdsAdapter           =  new PendingOrderAdapter(mContext, listOfAllOrder);		
		orderIdListView.setAdapter(orderIdsAdapter);		

	}

	@Override
	public void onListenerRegister() {		
		pullBackBtn.setOnClickListener(this);
		orderIdListView.setOnItemClickListener(this);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.Activity_Pending_Btn_Pull_Order:
			
			PendingOrderModel pendingOrderModel = listOfAllOrder.get(selectedPosition);
			
			
			
			instance.resetAllData(mContext,1);
			Variables.isPendingOrderItems = true;
			Variables.startNewTrans       = false;
			Variables.orderStatus         = pendingOrderModel.getOrderStatus();
			Variables.tableOrClerkShipToNameModel    = pendingOrderModel.getTableOrClerkShipToNameModel();
			Variables.customerOrClerkBillToNameModel = pendingOrderModel.getCustomerOrClerkBillToNameModel();
			instance.dataList.addAll(pendingOrderModel.getProductList());
			instance.myAdapter.notifyDataSetChanged();

			if(Float.parseFloat(pendingOrderModel.getOrderDiscount()) > 0.0){

				Variables.discountApplied   = true;
				Variables.discountDollar    = true;
				Variables.discountInDollar  = Float.parseFloat(pendingOrderModel.getOrderDiscount());
			}

			MyPreferences.setMyPreference(MOST_RECENTLY_TRANSACTION_ID, pendingOrderModel.getTransactionId(), mContext);
			instance.calculateSubTotalEachTime();
			instance.trasIdTv.setText(MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext));
			String timeAndDate = CurrentDate.returnCurrentDateWithTime();
			instance.dateTimeTv.setText(timeAndDate);
			
			Variables.isReprintActive = true;
			instance.subtotalBtn.setText("Reprint");

			finish();

			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		displayView(position);
	}

	public float findOptionsPrice(ArrayList<RelationalOptionModel> childs){

		float priceOfOptions = 0.0f;
		for(int index = 0; index < childs.size(); index++){
			List<SubOptionModel> listOfSubOption = childs.get(index).getListOfSubOptionModel();
			for(int value = listOfSubOption.size() -1 ; value >= 0; value --){
				priceOfOptions += Float.parseFloat(listOfSubOption.get(value).getSubOptionPrice());
			}
		}
		return priceOfOptions;
	}

	@Override
	public void onCallBack(WebServiceCall webServiceCall, String responseData,int responseCode) {

		webServiceCall.onDismissProgDialog();

		switch (responseCode) {

		case WebServiceCall.WEBSERVICE_CALL_NO_INTERENET:	
			NoInternetDialog.noInternetDialogShown(mContext);
			break;

		case WebServiceCall.WEBSERVICE_CALL_EXCEPTION:
			ExceptionDialog.onExceptionOccur(mContext);
			break;

		case WebServiceCall.WEBSERVICE_CALL_RESULT_VALID:

			switch (webServiceCall.getWebServiceId()) {

			case OBJECT_ID_1:	

				try{
					listOfClerk.clear();
					JSONObject level_1_Obj       =  new JSONObject(responseData);
					JSONArray  level_1_array     =  level_1_Obj.getJSONArray("Details");
					int  lengthOfArray           =  level_1_array.length();

					if(lengthOfArray < 1){
						noOrderExistTv.setVisibility(View.VISIBLE);
						mainLinearLayout.setVisibility(View.GONE);
					}
					else
					{
						noOrderExistTv.setVisibility(View.GONE);
						mainLinearLayout.setVisibility(View.VISIBLE);

						for(int index = 0 ; index < lengthOfArray; index ++ ){

							JSONObject level_2_Obj = level_1_array.getJSONObject(index);

							String transactionId        = JSONObJValidator.stringTagValidateOne(level_2_Obj, "order_id", "0");
							String incrementId          = JSONObJValidator.stringTagValidateOne(level_2_Obj, "increment_id", "0");
							String shipIdAsTelephone    = JSONObJValidator.stringTagValidateOne(level_2_Obj, "shipping_id", "0");
							String billIdAsTelephone    = JSONObJValidator.stringTagValidateOne(level_2_Obj, "billing_id" , "0");
							String orderStatus          = JSONObJValidator.stringTagValidateOne(level_2_Obj, "status", "pending");
							String orderDiscount        = JSONObJValidator.stringTagValidateOne(level_2_Obj, "discount_amount", "0.00");


							CustomerTable customerTabel = new CustomerTable(mContext);
							CustomerModel customerOrClerkBillToNameModel  = customerTabel.getSingleInfoFromTableByPhoneNo(billIdAsTelephone);
							CustomerModel tableOrClerkShipToNameModel     = customerTabel.getSingleInfoFromTableByPhoneNo(shipIdAsTelephone);

							JSONArray level_2_array    = level_2_Obj.getJSONArray("Order_details");
							float disocuntAmnt         = 0.0f;

							ArrayList<CheckOutParentModel> listOfProducts = new ArrayList<>();
							if (level_2_array.length() > 0 ) {
								for (int position = 0; position < level_2_array.length(); position++) {

									JSONObject level_3_Obj          = level_2_array.getJSONObject(position);							
									String productId                = JSONObJValidator.stringTagValidateOne(level_3_Obj,"product_id","0");
									String productQty               = JSONObJValidator.stringTagValidateOne(level_3_Obj,"qty_ordered","0");
									String prodductprc              = JSONObJValidator.stringTagValidateOne(level_3_Obj,"price","0");
									String productDis               = JSONObJValidator.stringTagValidateOne(level_3_Obj,"product_discount","0.00");
									disocuntAmnt                   += Float.parseFloat(productDis);
									JSONArray level_3_array         = level_3_Obj.getJSONArray("option_details");

									ProductModel productModel = new ProductTable(mContext).getSingleInfoFromTableByProductId(productId);

									productModel.setProductQty(String.valueOf((int)Float.parseFloat(productQty)));
									productModel.setProductPrice(prodductprc);
									productModel.setProductDisAmount(productDis);
									productModel.setProductQtyOnPendingTime(String.valueOf((int)Float.parseFloat(productQty)));

									ArrayList<RelationalOptionModel> relationalOptionModels = new ArrayList<>();

									if(level_3_array.length() > 0){

										for(int counter = 0; counter < level_3_array.length(); counter++){

											JSONObject innerMostObj = level_3_array.getJSONObject(counter);
											String optionId         = innerMostObj.getString("option_id");
											String subOptionsIds    = innerMostObj.getString("option_value");
											RelationalOptionModel returnedObj = new ProductOptionTable(mContext).getRelationModelObj(productId,optionId,subOptionsIds); 
											relationalOptionModels.add(returnedObj);
										}
									}

									float sumOfOptions     = findOptionsPrice(relationalOptionModels);
									productModel.setProductOptionsPrice(MyStringFormat.formatWith2DecimalPlaces(sumOfOptions));
									productModel.setProductPrice(MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(prodductprc) - sumOfOptions));
									listOfProducts.add(new CheckOutParentModel(productModel, relationalOptionModels, new ExtraProductArgument(false,true)));
								}
							}
							orderDiscount = MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(orderDiscount) - disocuntAmnt);
							listOfAllOrder.add(new PendingOrderModel(transactionId, incrementId,customerOrClerkBillToNameModel,tableOrClerkShipToNameModel, orderStatus, orderDiscount,listOfProducts));

							if(!listOfClerk.contains(customerOrClerkBillToNameModel.getFirstName()))
								listOfClerk.add(customerOrClerkBillToNameModel.getFirstName());
						}
						orderIdsAdapter.notifyDataSetChanged();

						ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, listOfClerk);
						dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spinner.setAdapter(dataAdapter);

						displayView(0);
					}
				}
				catch(Exception exception){
					exception.printStackTrace();
					ToastUtils.showOwnToast(mContext, "Error To Parse Response Data");
					anExceptionOccur = true;
				}
				break;
			default:
				break;
			}

		}
	}

	private void shortListBasedOnName(String name){
		if(!listOfAllOrder.isEmpty()){
			List<PendingOrderModel> refernceList = new ArrayList<>();
			for (int index = 0; index < listOfAllOrder.size();index ++ ){
				PendingOrderModel pendingOrderModel = listOfAllOrder.get(index);
				if(pendingOrderModel.getCustomerOrClerkBillToNameModel().getFirstName().equalsIgnoreCase(name))
					refernceList.add(0,pendingOrderModel);
				else
					refernceList.add(pendingOrderModel);
			}
			listOfAllOrder.clear();
			listOfAllOrder.addAll(refernceList);
			orderIdsAdapter.notifyDataSetChanged();
			displayView(0);
		}
	}

	private void displayView(int position) {

		if(!anExceptionOccur){
			selectedPosition = position;
			if(!listOfAllOrder.isEmpty()){
				PendingOrderModel pendingOrderModel = listOfAllOrder.get(position);
				if(!pendingOrderModel.getProductList().isEmpty()){
					orderDetailsAdapter       =  new PendingDetailsAdapter(mContext, pendingOrderModel.getProductList(), orderDeatilesListview);
					orderDeatilesListview.setAdapter(orderDetailsAdapter);
					pullBackBtn.setVisibility(View.VISIBLE);
					orderIdListView.setItemChecked(selectedPosition, true);
				}
				else{
					pullBackBtn.setVisibility(View.INVISIBLE);
					ToastUtils.showOwnToast(mContext, "Order Details Not Exist");
				}
			}
		}
		else
			ToastUtils.showOwnToast(mContext,"Please Refersh List Again");
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		shortListBasedOnName(""+parent.getSelectedItem());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

}

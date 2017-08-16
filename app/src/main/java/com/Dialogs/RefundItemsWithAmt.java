package com.Dialogs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dialog.ExceptionDialog;
import com.dialog.NoInternetDialog;
import com.Beans.CheckOutParentModel;
import com.Beans.ExtraProductArgument;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.posimplicity.database.ProductOptionTable;
import com.posimplicity.database.ProductTable;
import com.utils.CurrentDate;
import com.utils.HideSoftKeyBoardFromScreen;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.utils.WebCallBackListener;
import com.utils.WebServiceCall;
import com.posimplicity.HomeActivity;
import com.posimplicity.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RefundItemsWithAmt extends BaseDialog implements WebCallBackListener, View.OnClickListener {

	public static final int RETURN_  = 0x00;
	public static final int REPRINT_ = 0x01;	
	private ImageButton cancelButton;
	private Button refundButton;	
	private EditText transIdForRefund;
	private String transIdString;
	private HomeActivity instance;
	private float discount = 0.0f;
	private int requestCode = 0;

	public RefundItemsWithAmt(Context context, int theme, int width,int height, boolean isOutSideTouch, boolean isCancelable,int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
		instance = HomeActivity.localInstance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cancelButton     = findViewByIdAndCast(R.id.btn_cancelRefund);
		refundButton     = findViewByIdAndCast(R.id.refund);
		transIdForRefund = findViewByIdAndCast(R.id.transIdForRefund);

		cancelButton.setOnClickListener(this);
		refundButton.setOnClickListener(this);

	}

	public void onGetDetailsOfAnOrder(){

		instance.resetAllData(mContext,1);

		String requetsedUrl            = MyPreferences.getMyPreference(STORE_NAME, mContext).concat("?tag=complete_Order_Detailes&").concat("unique_no="+transIdString);
		WebServiceCall webServiceCall  = new WebServiceCall(requetsedUrl, "Order Details...", OBJECT_ID_1, "Order Details :", mContext, null, this, true, false, false);
		webServiceCall.execute();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancelRefund:
			HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(mContext, transIdForRefund);			
			dismiss();
			break;

		case R.id.refund:

			HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(mContext, transIdForRefund);
			transIdString = transIdForRefund.getText().toString();

			if(transIdString.isEmpty())
				transIdForRefund.setError("Please Enter TransactionId First");
			else{
				dismiss();
				onGetDetailsOfAnOrder();
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
					JSONObject jsonObject = new JSONObject(responseData);
					JSONArray jsonArray   = jsonObject.getJSONArray("Order_details");
					float disocuntAmnt = 0.0f;
					if (jsonArray.length() > 0 ) {
						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject jsonObj  = jsonArray.getJSONObject(i);							
							String productId    = jsonObj.getString("product_id");
							String productQty   = jsonObj.getString("qty_ordered");
							String prodcutPrce  = jsonObj.getString("price");
							String productDis   = JSONObJValidator.stringTagValidate(jsonObj, "product_discount", "0,00");
							disocuntAmnt       += Float.parseFloat(productDis);
							JSONArray inArr     = jsonObj.getJSONArray("option_details");

							ProductModel productModel = new ProductTable(mContext).getSingleInfoFromTableByProductId(productId);
							productModel.setProductQty(String.valueOf((int)Float.parseFloat(productQty)));
							productModel.setProductPrice(prodcutPrce);
							productModel.setProductDisAmount(productDis);
							ArrayList<RelationalOptionModel> relationalOptionModels = new ArrayList<>();

							if(inArr.length() > 0){

								for(int index = 0; index < inArr.length(); index++){

									JSONObject innerMostObj = inArr.getJSONObject(index);
									String optionId         = innerMostObj.getString("option_id");
									String subOptionsIds    = innerMostObj.getString("option_value");
									RelationalOptionModel returnedObj = new ProductOptionTable(mContext).getRelationModelObj(productId,optionId,subOptionsIds); 
									relationalOptionModels.add(returnedObj);
								}
							}

							float sumOfOptions     = findOptionsPrice(relationalOptionModels);
							productModel.setProductOptionsPrice(MyStringFormat.formatWith2DecimalPlaces(sumOfOptions));
							productModel.setProductPrice(MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(prodcutPrce) - sumOfOptions));
							instance.dataList.add(new CheckOutParentModel(productModel, relationalOptionModels, new ExtraProductArgument(false,true)));
						}
					}
					String discountAmount   = JSONObJValidator.stringTagValidate(jsonObject, "discount_amount", "0.00");
					discount                = Float.parseFloat(discountAmount);
					discount               -= disocuntAmnt;
					Variables.gateWayTrasId = JSONObJValidator.stringTagValidate(jsonObject, "gateway_transaction_id", "xxxx");

					if(Variables.gateWayTrasId.equalsIgnoreCase("xxxx"))
						Variables.gateWayTrasId = "";

					if(jsonArray.length() > 0 ){
						instance.myAdapter.notifyDataSetChanged();
						discount = Float.parseFloat(discountAmount);
						Variables.startNewTrans = false;

						switch (requestCode) {
						
						case RefundItemsWithAmt.REPRINT_:
							
							Variables.isReprintActive = true;
							instance.subtotalBtn.setText("Reprint");
							
							break;
							
						case RefundItemsWithAmt.RETURN_:
							Variables.isReturnActive  = true;
							instance.subtotalBtn.setText("Refund");
							break;

						default:
							break;
						}

						if(discount > 0.0){

							Variables.discountApplied   = true;
							Variables.discountDollar    = true;
							Variables.discountInDollar  = discount;
						}

						MyPreferences.setMyPreference(MOST_RECENTLY_TRANSACTION_ID, transIdString, mContext);
						instance.calculateSubTotalEachTime();
						instance.trasIdTv.setText(MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext));
						String timeAndDate = CurrentDate.returnCurrentDateWithTime();
						instance.dateTimeTv.setText(timeAndDate);
						ToastUtils.showOwnToast(mContext, "Order Details SuccessFully Fetched ");
					}
					else
					{ ToastUtils.showOwnToast(mContext, "Please Check Order Id"); 
					}
				}
				catch(Exception exception){
					exception.printStackTrace();
					ToastUtils.showOwnToast(mContext, "Please Check Order Id");
				}	
				break;
			default:
				break;
			}
		}
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

	public void show(int requestCode) {
		this.requestCode = requestCode;
		show();
	}
}

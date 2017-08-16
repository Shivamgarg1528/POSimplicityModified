package com.Socket;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Beans.CheckOutParentModel;
import com.Beans.ExtraProductArgument;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.posimplicity.database.ProductOptionTable;
import com.posimplicity.database.ProductTable;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.GenerateNewTransactionNo;
import com.utils.JSONObJValidator;
import com.utils.MyPreferences;
import com.utils.Variables;
import com.posimplicity.HomeActivity;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

public class UpdateUserInterface implements PrefrenceKeyConst{

	private Context mContext;
	private String responseData;
	private HomeActivity instance;
	private String channel,storeName,appId,currentStoreName;
	private JSONObject jsonObject;

	public UpdateUserInterface(Context mContext) {
		this.mContext          = mContext;
		this.instance          = HomeActivity.localInstance;
		this.currentStoreName  = MyPreferences.getMyPreference(STORE, mContext);
	}

	public void onUiUpdate(JSONArray jsonArray){
		try{
			responseData    = jsonArray.getString(0);
			Log.v("SocketData: -> ", responseData);
			jsonObject      = new JSONObject(responseData);
			boolean goAhead = validData(jsonObject);
			if(goAhead){
				channel = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.CHANNEL, "");
				if(!channel.isEmpty()){
					if(channel.equalsIgnoreCase(ConvertStringOfJson.ADD_PRODUCT) || channel.equalsIgnoreCase(ConvertStringOfJson.INCRE_QTY) || channel.equalsIgnoreCase(ConvertStringOfJson.DISCOUNT_ITEM))
						productAdd();
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private boolean validData(JSONObject jsonObject){

		storeName    = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.STORE, storeName);
		appId        = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.APP_ID_KEY, ConvertStringOfJson.APP_ID_VALUE);
		if(!appId.equalsIgnoreCase(ConvertStringOfJson.APP_ID_VALUE) && storeName.equalsIgnoreCase(currentStoreName)){
			return true;
		}
		else
			return false;
	}

	private void productAdd() throws Exception {
		
		if(Variables.startNewTrans){
			try{
				instance.trasIdTv.setText(GenerateNewTransactionNo.onNewTransactionNumber(mContext));
				String timeAndDate = (String) DateFormat.format("yyyy/MM/dd hh:mm:ss",new Date().getTime());
				instance.dateTimeTv.setText(timeAndDate);
				Variables.startNewTrans = false;
				new ConvertStringOfJson(mContext).onDateAndTransactionNo(timeAndDate,instance.trasIdTv.getText().toString());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		String productId        = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.PRODUCT_ID , "0"); 
		String productQty       = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.PRODUCT_QTY, "1"); 
		String productPrice     = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.PRODUCT_Pr , "0.00"); 
		String productOptionpr  = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.CHILD_Pr   , "0.00"); 
		String productDis       = JSONObJValidator.stringTagValidate(jsonObject, ConvertStringOfJson.DISCOUNT, "0.00");
		JSONArray jsonArray     = jsonObject.getJSONArray(ConvertStringOfJson.OPTION_DET);
		int lengthOfArray       = jsonArray.length();
		ArrayList<RelationalOptionModel> optionList = new ArrayList<RelationalOptionModel>();

		if(lengthOfArray > 0){
			for(int index = 0 ;index < lengthOfArray;index++){
				JSONObject innerObj    = jsonArray.getJSONObject(index);
				String optionId        = innerObj.getString(ConvertStringOfJson.OPTION_ID);
				String subOptionId     = innerObj.getString(ConvertStringOfJson.Sub_OPTION_ID);
				optionList.add(new ProductOptionTable(mContext).getRelationModelObj(productId, optionId, subOptionId));
			}
		}

		ProductModel clikedProduct             = new ProductTable(mContext).getSingleInfoFromTableByProductId(""+productId);
		CheckOutParentModel parent             = new CheckOutParentModel(clikedProduct, optionList , new ExtraProductArgument(false,false));
		boolean isProductExistInList           = instance.dataList.contains(parent);
		if (isProductExistInList) {
			int location = instance.dataList.indexOf(parent);
			instance.dataList.remove(location);
			instance.dataList.add(location,parent);
		} 
		else 
			instance.dataList.add(0, parent);

		clikedProduct.setProductQty(productQty);
		clikedProduct.setProductPrice(productPrice);
		clikedProduct.setProductOptionsPrice(productOptionpr);	
		clikedProduct.setProductDisAmount(productDis);

		instance.myAdapter.notifyDataSetChanged();
		instance.calculateSubTotalEachTime();
	}
}


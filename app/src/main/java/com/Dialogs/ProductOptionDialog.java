package com.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Beans.CheckOutParentModel;
import com.Beans.ExtraProductArgument;
import com.Beans.OptionModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.Socket.ConvertStringOfJson;
import com.utils.FixedStorage;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.TenderViewTextSet;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.adapter.ProductOptionListAdapter;
import com.posimplicity.HomeActivity;
import com.posimplicity.R;

import java.util.ArrayList;
import java.util.List;

public class ProductOptionDialog extends BaseDialog implements OnChildClickListener {

	private List<RelationalOptionModel> parentList;
	private ExpandableListView expandableListView;
	private ProductOptionListAdapter myExpandableAdapter;
	private ProductModel selectedProduct;
	private HomeActivity activityInstance;	
	private List<OptionModel> enabledParentList;
	private boolean showSelectedToast;
	private float priceOfOptions = 0.0f;
	private TextView noOptionTV;
	private TextView itemPrice;
	private StringBuilder enteredStringBld;	
	private LinearLayout includeLayout;


	public ProductOptionDialog(Context context, int theme, int width,int height, boolean isOutSideTouch, boolean isCancelable,int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
		enteredStringBld = new StringBuilder("000");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activityInstance    = HomeActivity.localInstance;
		parentList          = activityInstance.optionList;
		selectedProduct     = activityInstance.selectedProduct;
		expandableListView  = findViewByIdAndCast(R.id.options_list);	
		noOptionTV          = findViewByIdAndCast(R.id.Dialog_Product_Option_Tv_POption);
		itemPrice           = findViewByIdAndCast(R.id.Dialog_Product_Option_Tv_Price);
		includeLayout       = findViewByIdAndCast(R.id.Dialog_Product_Option_LL_KeyPadOfButtons);
		myExpandableAdapter = new ProductOptionListAdapter(mContext, parentList);
		enabledParentList   = new ArrayList<>();
		expandableListView.setAdapter(myExpandableAdapter);


		if(!MyPreferences.getBooleanPrefrences(OPTION_ITEM_EXPAND, mContext)){			
			for(int index=0;index<parentList.size();index++)
				expandableListView.expandGroup(index);
		}

		if(parentList.size() <= 0){
			noOptionTV.setVisibility(View.VISIBLE);
			noOptionTV.setSelected(true);
		}

		if(Float.parseFloat(selectedProduct.getProductPrice()) <= 0)
			includeLayout.setVisibility(View.VISIBLE);

		expandableListView.setOnChildClickListener(this);
		itemPrice.setText(selectedProduct.getProductPrice());
	}

	public float findOptionsPrice(ArrayList<RelationalOptionModel> childs){

		for(int index = 0; index < childs.size(); index++){
			List<SubOptionModel> listOfSubOption = childs.get(index).getListOfSubOptionModel();
			for(int value = listOfSubOption.size() -1 ; value >= 0; value --){
				priceOfOptions += Float.parseFloat(listOfSubOption.get(value).getSubOptionPrice());
			}
		}
		return priceOfOptions;
	}

	public ArrayList<RelationalOptionModel> onCreateList()
	{
		showSelectedToast = false;
		ArrayList<RelationalOptionModel> childs = new ArrayList<RelationalOptionModel>();

		for(int index = 0; index < parentList.size(); index++) {			

			RelationalOptionModel relationalOptionModel  		= parentList.get(index);
			OptionModel parentOption          	 				= relationalOptionModel.getOptionModel();
			List<SubOptionModel > childOptions  				= relationalOptionModel.getListOfSubOptionModel();

			boolean anyChildSelected                = false;

			if(parentOption.isEnable()){
				List<SubOptionModel> listOfSubOptionModel = new ArrayList<>();				
				for(int value = 0; value < childOptions.size(); value++) {
					SubOptionModel option = childOptions.get(value);
					if(option.isSelected()) {
						listOfSubOptionModel.add(option);
						anyChildSelected = true;
					}
				}
				if(!anyChildSelected){
					showSelectedToast = true;
					Toast.makeText(mContext, "Please Select Any Option For "+parentOption.getOptionName(), Toast.LENGTH_SHORT).show();
					break;
				}
				else
					childs.add(new RelationalOptionModel(parentOption, listOfSubOptionModel));
			}
			else{
				List<SubOptionModel> listOfSubOptionModel = new ArrayList<>();
				for(int value=0; value<childOptions.size(); value++) {
					SubOptionModel option = childOptions.get(value);
					if(option.isSelected()) {
						listOfSubOptionModel.add(option);
					}
				}
				childs.add(new RelationalOptionModel(parentOption, listOfSubOptionModel));
			}
		}
		return childs;
	}


	@Override
	public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {	
		SubOptionModel childOption = myExpandableAdapter.getChild(groupPosition, childPosition);
		childOption.setSelected(!childOption.isSelected());
		myExpandableAdapter.notifyDataSetChanged();
		return true;
	}

	public void onClick(View view){

		switch (view.getId()) {

		case R.id.button1:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.ONE_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button2:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.TWO_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button3:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.THREE_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button4:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.FOUR_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button5:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.FIVE_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button6:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.SIX_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button7:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.SEVEN_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button8:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.EIGHT_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button9:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.NINE_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button0:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.ZERO_,FixedStorage.ADD_CHAR,false);
			break;

		case R.id.button00:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,FixedStorage.DOUBLE_ZERO_,FixedStorage.ADD_CHAR,false);
			break;
		case R.id.backSpace:
			TenderViewTextSet.setTextOnView(itemPrice,enteredStringBld,"",FixedStorage.REM_CHAR,false);
			break;

		case R.id.canceloptionPopUp:
			dismiss();
			break;

		case R.id.addItemsOptions:


			float customPrice = 0.0f;
			String priceEditTextString = enteredStringBld.toString();
			try{
				if(Float.parseFloat(selectedProduct.getProductPrice()) <= 0){
					customPrice = Float.parseFloat(priceEditTextString);
					selectedProduct.setProductPrice(MyStringFormat.formatWith2DecimalPlaces(customPrice));
				}
			}
			catch(NumberFormatException nb){
				nb.printStackTrace();
				ToastUtils.showOwnToast(mContext, "Enter Valid ProductList Amount");
				return;
			}

			enabledParentList.clear();

			ArrayList<RelationalOptionModel> childs = onCreateList();

			if(!showSelectedToast){
				priceOfOptions = findOptionsPrice(childs);

				CheckOutParentModel parent   = new CheckOutParentModel(selectedProduct, childs,new ExtraProductArgument(false,Variables.isPendingOrderItems?true:false));
				boolean isProductExistInList = activityInstance.dataList.contains(parent);

				if (isProductExistInList) {

					ProductModel existingProduct = activityInstance.dataList.get(activityInstance.dataList.indexOf(parent)).getProduct();
					existingProduct.upgradeQtyByOne();
					existingProduct.setProductOptionsPrice(MyStringFormat.formatWith2DecimalPlaces(priceOfOptions));
					new ConvertStringOfJson(mContext).onProductModification(activityInstance.dataList.get(activityInstance.dataList.indexOf(parent)), ConvertStringOfJson.SOCKET_ADD_PRODUCT);
					if(Variables.isPendingOrderItems)
						activityInstance.dataList.get(activityInstance.dataList.indexOf(parent)).getExtraArgument().setPendingItems(true);
				} 
				else {
					selectedProduct.setProductOptionsPrice(MyStringFormat.formatWith2DecimalPlaces(priceOfOptions));
					activityInstance.dataList.add(0, parent);
					new ConvertStringOfJson(mContext).onProductModification(parent, ConvertStringOfJson.SOCKET_ADD_PRODUCT);
				}
				activityInstance.myAdapter.notifyDataSetChanged();
				activityInstance.calculateSubTotalEachTime();

				dismiss();
			}

			break;

		default:
			break;
		}

	}
}

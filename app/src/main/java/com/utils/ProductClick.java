package com.utils;

import android.content.Context;

import com.Beans.CheckOutParentModel;
import com.Beans.ExtraProductArgument;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Dialogs.ProductOptionDialog;
import com.Socket.ConvertStringOfJson;
import com.posimplicity.database.ProductOptionTable;
import com.posimplicity.database.ProductTable;
import com.posimplicity.HomeActivity;
import com.posimplicity.R;

import java.util.ArrayList;

public class ProductClick {	

	private Context mContext;
	private HomeActivity instance;
	private POSApp globalApp;

	public ProductClick(Context mContext) {	

		this.mContext  = mContext;
		this.instance  = (HomeActivity)mContext;
		this.globalApp = POSApp.getInstance();
	}	


	public void onClick(int productId)
	{
		try {

			ProductModel clikedProduct = new ProductTable(mContext).getSingleInfoFromTableByProductId(""+productId);

			instance.selectedProduct   = clikedProduct;
			instance.optionList        = new ProductOptionTable(mContext).getAllInfoFromTable(""+productId);

			if (instance.optionList.isEmpty() && !clikedProduct.getProductPrice().equalsIgnoreCase("0.00")) {	

				CheckOutParentModel parent   = new CheckOutParentModel(clikedProduct, new ArrayList<RelationalOptionModel>() , new ExtraProductArgument(false,Variables.isPendingOrderItems?true:false));
				boolean isProductExistInList = instance.dataList.contains(parent);

				if (isProductExistInList) {

					ProductModel existingProduct  = instance.dataList.get(instance.dataList.indexOf(parent)).getProduct();
					existingProduct.upgradeQtyByOne();
					new ConvertStringOfJson(mContext).onProductModification(instance.dataList.get(instance.dataList.indexOf(parent)),ConvertStringOfJson.SOCKET_ADD_PRODUCT);

					if(Variables.isPendingOrderItems)
						instance.dataList.get(instance.dataList.indexOf(parent)).getExtraArgument().setPendingItems(true);
				} 
				else {
					instance.dataList.add(0, parent);
					new ConvertStringOfJson(mContext).onProductModification(parent,ConvertStringOfJson.SOCKET_ADD_PRODUCT);
				}

				instance.myAdapter.notifyDataSetChanged();
				instance.calculateSubTotalEachTime();


			} else if (clikedProduct.getProductPrice().equalsIgnoreCase("0.00") || !instance.optionList.isEmpty()) {

				int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(), 45);
				int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(),90);
				instance.productOptionDialog = new ProductOptionDialog(mContext, R.style.myCoolDialog, width, height,true, true, R.layout.dialog_product_option_item);
				instance.productOptionDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

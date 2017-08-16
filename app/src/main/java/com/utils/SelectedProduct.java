package com.utils;

import com.posimplicity.database.ProductTable;
import android.content.Context;

public class SelectedProduct {

	private Context mContext;	
	private int productId;

	public SelectedProduct(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void getProductInformation(String skuId) {
		productId = new ProductTable(mContext).getProdctIdFromTableByProductSku(skuId);	

		if (productId == 0) {
		//	new ProductNotFoundDailog(mContext).showProductNotFoundDailog();
		ToastUtils.showOwnToast(mContext, "No ProductList Found For Sku "+skuId);
		}
		else {		
			new ProductClick(mContext).onClick(productId);
		}	
	}	
}

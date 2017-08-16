package com.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.posimplicity.HomeActivity;

import java.util.ArrayList;

public class AdapterForSearch { 
	private	Context mContext;
	private HomeActivity instance;


	public AdapterForSearch(Context mContext) {
		super();
		this.mContext = mContext;
		this.instance = (HomeActivity) mContext;
	}

	public void onSearchMethod() {
		
		ArrayList<String> productNames = new ArrayList<String>();		
		
		for(int index = 0; index < instance.allProductList.size(); index++){
			productNames.add(instance.allProductList.get(index).getProductName());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_expandable_list_item_1, productNames);		
		instance.searchProductByName.setAdapter(adapter);
		instance.searchProductByName.setOnItemClickListener(instance);
	}
}

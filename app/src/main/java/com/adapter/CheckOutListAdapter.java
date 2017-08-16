package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.Beans.CheckOutParentModel;
import com.Beans.OptionModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.Socket.ConvertStringOfJson;
import com.posimplicity.HomeActivity;
import com.posimplicity.R;

import java.util.List;

public class CheckOutListAdapter extends BaseExpandableListAdapter {	
	private Context mContext;	
	private List<CheckOutParentModel> productList;
	private ExpandableListView itemListControl;
	HomeActivity instance;

	public CheckOutListAdapter(Context mContext, List<CheckOutParentModel> productList,ExpandableListView itemListControl) {
		super();
		this.mContext        = mContext;
		this.productList     = productList;
		this.itemListControl = itemListControl;
		this.instance        = (HomeActivity) mContext;
	}	

	@Override
	public RelationalOptionModel getChild(int groupPosition, int childPosition) {
		return productList.get(groupPosition).getChilds().get(childPosition);
	}


	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {		
		ChildHolder childHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.child_item_in_expandable_list, null);
			childHolder = new ChildHolder();
			childHolder.textView=(TextView)convertView.findViewById(R.id.childItem);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		RelationalOptionModel relationalOptionModel = getChild(groupPosition, childPosition);
		OptionModel optionModel                     = relationalOptionModel.getOptionModel();
		List<SubOptionModel>   subOptionModels      = relationalOptionModel.getListOfSubOptionModel();

		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder suboptStr     = new StringBuilder();
		stringBuilder.append(optionModel.getOptionName()).append(" ( ");
		for(int index = subOptionModels.size() - 1 ; index >= 0 ; index --){
			suboptStr.append((suboptStr.length() == 0)?subOptionModels.get(index).getSubOptionName():","+subOptionModels.get(index).getSubOptionName());
		}
		stringBuilder.append(suboptStr);
		stringBuilder.append(" )");

		childHolder.textView.setText(stringBuilder.toString());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size=0;
		if(productList.get(groupPosition).getChilds()!=null)
			size = productList.get(groupPosition).getChilds().size();
		return size;
	}

	@Override
	public CheckOutParentModel getGroup(int groupPosition) {
		return productList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return  productList.size();	
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {    

		ParentHolder parentHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.selected_product_row, null);
			parentHolder = new ParentHolder();
			parentHolder.itemName        =(TextView)convertView.findViewById(R.id.headline);
			parentHolder.itemQunatity    =(TextView)convertView.findViewById(R.id.qty);
			parentHolder.itemPrice       =(TextView)convertView.findViewById(R.id.price);
			parentHolder.deleteButton    =(Button)convertView.findViewById(R.id.removeitem);
			parentHolder.discountAmount  =(TextView)convertView.findViewById(R.id.discountAmount);
			convertView.setTag(parentHolder);
		} else {
			parentHolder = (ParentHolder) convertView.getTag();
		}

		final ProductModel parentItem = getGroup(groupPosition).getProduct();
		parentHolder.itemName.setText(parentItem.getProductName());		
		parentHolder.itemQunatity.setText(parentItem.getProductQty());		
		parentHolder.itemPrice.setText(parentItem.getProductCalAmount());		
		parentHolder.discountAmount.setText(parentItem.getProductDisAmount());

		parentHolder.deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				CheckOutParentModel parentObj = getGroup(groupPosition);
				new ConvertStringOfJson(mContext).onProductModification(parentObj, ConvertStringOfJson.SOCKET_DEL_PRODUCT);
				removeDataFromList(parentObj);
				if(productList.isEmpty()){
					instance.resetAllData(mContext,1);
				}
				else
					instance.calculateSubTotalEachTime();
			}
		});

		itemListControl.expandGroup(groupPosition);
		return convertView;
	}

	protected void removeDataFromList(CheckOutParentModel parentObj) {		
		productList.remove(parentObj);		
		notifyDataSetChanged();
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

	class ChildHolder {		
		TextView textView;
	}
	class ParentHolder {

		TextView itemName,itemPrice,itemQunatity,discountAmount;
		Button deleteButton;
	}
}

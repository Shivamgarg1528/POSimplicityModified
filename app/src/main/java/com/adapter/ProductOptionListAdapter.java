package com.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.Beans.OptionModel;

import com.posimplicity.R;

public class ProductOptionListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<RelationalOptionModel> dataList;	

	public ProductOptionListAdapter(Context mContext,List<RelationalOptionModel> dataList) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
	}

	public SubOptionModel getChild(int groupPosition, int childPosition) {
		return dataList.get(groupPosition).getListOfSubOptionModel().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		final ChildHolder childHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.child_item, null);
			childHolder = new ChildHolder();
			childHolder.checkBox = (ImageView) convertView.findViewById(R.id.itemoptionCheckbox);
			childHolder.name     = (TextView)  convertView.findViewById(R.id.optionValue);
			childHolder.price    = (TextView)  convertView.findViewById(R.id.childprice);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		SubOptionModel subOptionModel = getChild(groupPosition, childPosition);
		
		childHolder.name.setText(subOptionModel.getSubOptionName());
		childHolder.price.setText("$ "+subOptionModel.getSubOptionPrice());

		if(subOptionModel.isSelected()) {
			childHolder.checkBox.setImageResource(R.drawable.checkbox_selected);
		} else {
			childHolder.checkBox.setImageResource(R.drawable.checkbox_normal);
		}

		return convertView;
	}


	public int getChildrenCount(int groupPosition) {		
		return dataList.get(groupPosition).getListOfSubOptionModel().size();
	}

	public OptionModel getGroup(int groupPosition) {
		return dataList.get(groupPosition).getOptionModel();
	}

	public int getGroupCount() {
		return dataList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,	View convertView, ViewGroup parent) {
		GroupHolder groupHolder;		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item, parent, false); 
			groupHolder       = new GroupHolder();
			groupHolder.title = (TextView) convertView.findViewById(R.id.OptionName);
			convertView.setTag(groupHolder);
		}
		else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		OptionModel parentOption = getGroup(groupPosition);
		((TextView) convertView.findViewById(R.id.OptionName)).setText(parentOption.getOptionName());		
		return convertView;
	}
	class GroupHolder {
		ImageView img;
		TextView title;
	}
	class ChildHolder {
		ImageView checkBox;
		TextView name;
		TextView price;
	}

	public boolean hasStableIds() {
		return true;
	}
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}

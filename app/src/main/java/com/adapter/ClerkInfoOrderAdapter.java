package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.Beans.CustomerModel;

import java.util.List;

public class ClerkInfoOrderAdapter extends BaseAdapter {

	private List<CustomerModel> dataList;
	private LayoutInflater lInflater;

	public ClerkInfoOrderAdapter(Context mContext, List<CustomerModel> dataList) {
		super();
		this.dataList  = dataList;
		this.lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public CustomerModel getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ReportHolder holder;
		if(convertView == null){
			holder = new ReportHolder();
			convertView         = lInflater.inflate(android.R.layout.simple_list_item_checked, null);
			holder.nameOfStaff  = (CheckedTextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(holder);
			convertView.setBackgroundColor(Color.GRAY);
		}
		else
			holder  = 	(ReportHolder) convertView.getTag();

		CustomerModel localObj = getItem(position);
		holder.nameOfStaff.setText(localObj.getFirstName());
		holder.nameOfStaff.setChecked(localObj.isRowSelected());

		convertView.setOnClickListener(new View.OnClickListener() {	

			@Override
			public void onClick(View v) {

				CustomerModel localObj = getItem(position);
				localObj.setRowSelected(!localObj.isRowSelected());
				localObj.setCustomerLogin(!localObj.isCustomerLogin());

				for(int index = 0 ;index < dataList.size() ; index ++){

					if(index == position)
						continue;
					else
					{
						CustomerModel resetedLocalObj = dataList.get(index);
						resetedLocalObj.setRowSelected(false);
						resetedLocalObj.setCustomerLogin(false);
					}
				}
				notifyDataSetChanged();				
			}
		});

		return convertView;
	}

	public class ReportHolder{
		CheckedTextView nameOfStaff;
	}

}

package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.Beans.StaffModel;

import java.util.List;

public class ClerkInfoAdapter extends BaseAdapter{

	private List<StaffModel> dataList;
	private LayoutInflater lInflater;

	public ClerkInfoAdapter(Context mContext, List<StaffModel> dataList) {
		super();
		this.dataList  = dataList;
		this.lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public StaffModel getItem(int position) {
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

		StaffModel localObj = getItem(position);
		holder.nameOfStaff.setText(localObj.getStaffName());
		holder.nameOfStaff.setChecked(localObj.isRowSelected());
		
		convertView.setOnClickListener(new View.OnClickListener() {			

			@Override
			public void onClick(View v) {
				StaffModel localObj = getItem(position);
				localObj.setRowSelected(!localObj.isRowSelected());
				localObj.setStaffLogin(!localObj.isStaffLogin());
				notifyDataSetChanged();				
			}
		});
		return convertView;
	}

	public class ReportHolder{
		CheckedTextView nameOfStaff;
	}

}

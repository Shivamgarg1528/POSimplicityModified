package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Beans.CCPaidInfoModel;
import com.posimplicity.R;

import java.util.List;

public class CCPaidAdapter  extends BaseAdapter {

	private List<CCPaidInfoModel> dataList;
	private LayoutInflater lInflater;


	public CCPaidAdapter(Context mContext, List<CCPaidInfoModel> dataList) {
		super();
		this.dataList  = dataList;
		this.lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public CCPaidInfoModel getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReportHolder holder;
		if(convertView == null){
			holder = new ReportHolder();
			convertView = lInflater.inflate(R.layout.cc_paid_cc_layout, null);
			holder.fieldName  = (TextView) convertView.findViewById(R.id.fieldName);
			holder.fieldValue = (TextView) convertView.findViewById(R.id.fieldValue);
			convertView.setTag(holder);
		}
		else
			holder  = 	(ReportHolder) convertView.getTag();
		CCPaidInfoModel localObj = getItem(position);
		
		holder.fieldName.setText(localObj.getTitleName());
		holder.fieldValue.setText(localObj.getTitleValue());

		return convertView;
	}

	public class ReportHolder{
		TextView fieldName,fieldValue;
	}

}

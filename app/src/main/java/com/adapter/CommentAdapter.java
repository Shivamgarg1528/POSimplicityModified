package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.Beans.CommentModel;

import java.util.List;

public class CommentAdapter extends BaseAdapter{

	private List<CommentModel> dataList;
	private LayoutInflater lInflater;
	

	public CommentAdapter(Context mContext, List<CommentModel> dataList) {
		super();
		this.dataList  = dataList;
		this.lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public CommentModel getItem(int position) {
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

		CommentModel localObj = getItem(position);
		holder.nameOfStaff.setText(localObj.getCommentString());
		holder.nameOfStaff.setChecked(localObj.isCommentSelected());
		
		convertView.setOnClickListener(new View.OnClickListener() {			

			@Override
			public void onClick(View v) {
				
				for(int index = 0; index < dataList.size() ; index ++)
					getItem(index).setCommentSelected(false);
				
				CommentModel localObj = getItem(position);
				localObj.setCommentSelected(!localObj.isCommentSelected());
				notifyDataSetChanged();				
			}
		});
		
		return convertView;
	}

	public class ReportHolder{
		CheckedTextView nameOfStaff;
	}

}


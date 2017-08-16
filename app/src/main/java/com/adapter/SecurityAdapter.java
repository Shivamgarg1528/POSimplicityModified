package com.adapter;

import java.util.List;

import com.Beans.POSAuthority;
import com.posimplicity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class SecurityAdapter extends BaseAdapter {

	private List<POSAuthority> dataList;
	private LayoutInflater layoutInflator;

	public SecurityAdapter(Context mContext, List<POSAuthority> dataList) {
		this.dataList        = dataList;
		this.layoutInflator  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public POSAuthority getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SecurityHolder holder = null;
		if(convertView == null){
			holder                         = new SecurityHolder();
			convertView                    = layoutInflator.inflate(R.layout.row_item_security_listview, null);
			holder.settingName             = (TextView)convertView.findViewById(R.id.settingName);
			holder.clerkCheckBox           = (CheckedTextView)convertView.findViewById(R.id.clerkBox);
			holder.managerCheckBox         = (CheckedTextView)convertView.findViewById(R.id.managerBox);
			holder.superVisorChechBox      = (CheckedTextView)convertView.findViewById(R.id.supervisorBox);
			convertView.setTag(holder);
		}
		else
			holder = (SecurityHolder)convertView.getTag();

		POSAuthority posAuthority          = getItem(position);
		holder.settingName.setText(posAuthority.getSettingName());
		holder.clerkCheckBox.setChecked(posAuthority.isClerkHaveRights());
		holder.managerCheckBox.setChecked(posAuthority.isManagerHaveRights());
		holder.superVisorChechBox.setChecked(posAuthority.isSuperVisorHaveRights());


		holder.clerkCheckBox.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				POSAuthority posAuthority          = getItem(position);
				posAuthority.setClerkHaveRights(!posAuthority.isClerkHaveRights());
				notifyDataSetChanged();
			}
		});


		holder.managerCheckBox.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				POSAuthority posAuthority          = getItem(position);
				posAuthority.setManagerHaveRights(!posAuthority.isManagerHaveRights());
				notifyDataSetChanged();
			}
		});

		holder.superVisorChechBox.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				POSAuthority posAuthority          = getItem(position);
				posAuthority.setSuperVisorHaveRights(!posAuthority.isSuperVisorHaveRights());
				notifyDataSetChanged();

			}
		});

		return convertView;
	}

	class SecurityHolder{
		CheckedTextView clerkCheckBox,managerCheckBox,superVisorChechBox;
		TextView settingName;
	}
}

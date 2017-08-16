package com.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.Beans.RoleInfo;
import com.posimplicity.R;

import java.util.List;

public class PasswordAdapter extends BaseAdapter {

	private List<RoleInfo> dataList;
	private LayoutInflater layoutInflator;

	public PasswordAdapter(Context mContext, List<RoleInfo> dataList) {
		this.dataList        = dataList;
		this.layoutInflator  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public RoleInfo getItem(int position) {
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
			convertView                    = layoutInflator.inflate(R.layout.row_for_password_listview, null);
			holder.roleNameTv              = (TextView)convertView.findViewById(R.id.Activity_Security_TextView_PassWord_RoleName);
			holder.passwordEdt             = (EditText)convertView.findViewById(R.id.Activity_Security_EditText_PassWord_RoleName);
			convertView.setTag(holder);
		}
		else
			holder = (SecurityHolder)convertView.getTag();

		RoleInfo roleInfo          = getItem(position);
		holder.roleNameTv.setText(roleInfo.getRoleName());
		holder.passwordEdt.setText(roleInfo.getRolePassword());		

		holder.passwordEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				RoleInfo roleInfo          = getItem(position);
				if(s.length() <= 0 ){
					roleInfo.setRolePassword("");
					roleInfo.setRoleActive(false);
				}
				else{
					roleInfo.setRolePassword(s.toString());
					roleInfo.setRoleActive(true);
				}
			}
		});

		return convertView;
	}

	class SecurityHolder{
		TextView roleNameTv;
		EditText passwordEdt;
	}
}

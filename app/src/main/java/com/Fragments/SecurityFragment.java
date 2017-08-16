package com.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Beans.POSAuthority;
import com.Beans.RoleInfo;
import com.adapter.PasswordAdapter;
import com.adapter.SecurityAdapter;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.database.local.RoleTable;
import com.posimplicity.database.local.SecurityTable;
import com.posimplicity.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecurityFragment extends BaseFragment implements OnClickListener, OnItemClickListener {

	private Button saveBtn,resetBtn;
	private ListView expanListView,passwordListView;
	private LinearLayout frameLayout;
	private TextView    noAuthorityTv;
	private SecurityAdapter securityAdapter;
	private PasswordAdapter passwordAdapter;
	private List<POSAuthority> dataList;
	private List<RoleInfo>     roleDataList;

	public SecurityFragment(){}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dataList        = new ArrayList<>();
		roleDataList    = new ArrayList<>();

		dataList.clear();
		roleDataList.clear();

		dataList.addAll(new SecurityTable(mContext).getAllInfoFromTable());
		roleDataList.addAll(new RoleTable(mContext).getAllInfoFromTableDefalut(false));		
		Collections.sort(roleDataList);

		securityAdapter = new SecurityAdapter(mContext,dataList);
		passwordAdapter = new PasswordAdapter(mContext, roleDataList);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView         = inflater.inflate(R.layout.activity_security, container, false);		
		saveBtn          = findViewIdAndCast(R.id.Activity_Security_Settings_BUTTON_SAVE);	
		resetBtn         = findViewIdAndCast(R.id.Activity_Security_Settings_BUTTON_RESET);	
		expanListView    = findViewIdAndCast(R.id.Activity_Security_Settings_ListView_Items);
		passwordListView = findViewIdAndCast(R.id.Activity_Security_Settings_ListView_Password);
		noAuthorityTv    = findViewIdAndCast(R.id.Activity_Security_TextView_No_Authority);
		frameLayout      = findViewIdAndCast(R.id.Activity_Security_Frame_Authority);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		expanListView.setAdapter(securityAdapter);
		expanListView.setOnItemClickListener(this);

		passwordListView.setAdapter(passwordAdapter);

		RoleInfo roleInfo          = new RoleTable(mContext).getSingleInfoFromTableByRoleName(MyPreferences.getMyPreference(SECURITY_LOGIN_USER_Id, mContext));

		if(!roleInfo.getRoleName().equals(SecurityTable.Admin))
			noAuthorityTv.setVisibility(View.VISIBLE);			
		else		
			frameLayout.setVisibility(View.VISIBLE);


		saveBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.Activity_Security_Settings_BUTTON_SAVE:	
			updateDb();
			ToastUtils.showOwnToast(mContext, "Updation Saved SuccessFully");
			break;

		case R.id.Activity_Security_Settings_BUTTON_RESET:
			onResetAllSecurity();		
			break;

		default:
			break;
		}

	}

	private void updateDb() {
		new SecurityTable(mContext).updateInfoListInTable(dataList);
		new RoleTable(mContext).updateInfoListInTable(roleDataList);
	}	

	public  void onResetAllSecurity(){

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Continue,To Reset All Security Locks").setTitle(R.string.app_name).setPositiveButton("Conitinue", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {


				/////////////////////////////
				//  RESET ALL SECURITY INFORMATION
				////////////////////////////

				for(int index = 0 ; index < dataList.size();index ++){
					POSAuthority securityModel = dataList.get(index);
					securityModel.setClerkHaveRights(false);
					securityModel.setManagerHaveRights(false);
					securityModel.setSuperVisorHaveRights(false);
				}
				new SecurityTable(mContext).updateInfoListInTable(dataList);
				securityAdapter.notifyDataSetChanged();


				/////////////////////////////
				//  RESET ALL PASSWORD INFORMATION
				////////////////////////////

				for(int index = 0 ; index < roleDataList.size();index ++){
					RoleInfo roleInfo = roleDataList.get(index);
					roleInfo.setRolePassword("");
					roleInfo.setRoleActive(false);
				}

				new RoleTable(mContext).updateInfoListInTable(roleDataList);
				passwordAdapter.notifyDataSetChanged();
				ToastUtils.showOwnToast(mContext, "Reset Locks SuccessFully");				
			}
		});

		builder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog al = builder.create();
		al.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		ToastUtils.showOwnToast(mContext, dataList.get(position).getSettingName());
	}
}

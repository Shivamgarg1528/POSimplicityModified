package com.Fragments;

import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.POSApp;
import com.posimplicity.HomeActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class BaseFragment extends Fragment implements  PrefrenceKeyConst {

	protected Context mContext;
	protected POSApp gApp;
	protected HomeActivity localInsatnceOfHome;
	protected View rootView;
	protected Activity activity;

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.mContext             = activity;
		this.activity             = activity;
		this.gApp                 = POSApp.getInstance();
		this.localInsatnceOfHome  = HomeActivity.localInstance;
	}

	@SuppressWarnings("unchecked")
	public <T> T findViewIdAndCast(int id) {
		return (T) rootView.findViewById(id);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(this instanceof CreditFragment)
			activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}

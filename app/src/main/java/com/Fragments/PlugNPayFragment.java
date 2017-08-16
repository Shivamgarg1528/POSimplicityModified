package com.Fragments;

import com.utils.MyPreferences;
import com.posimplicity.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class PlugNPayFragment extends BaseFragment  {
	
	public  EditText plugNPayFragmentEditTextId; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView                     = inflater.inflate(R.layout.fragment_plug_pay, container, false);
		plugNPayFragmentEditTextId   = findViewIdAndCast(R.id.Fragment_Plug_N_Pay_ET_Plug_Pay_Id);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		plugNPayFragmentEditTextId.setText(MyPreferences.getMyPreference(PLUG_PAY_ID, mContext));
	}
}

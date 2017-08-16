package com.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.utils.MyPreferences;
import com.posimplicity.R;

public class NobleFragment extends BaseFragment {

	public EditText nobleGatewayKeyName,nobleGatewayActName,nobleGatewayKey;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView             = inflater.inflate(R.layout.fragment_noble, container, false);
		nobleGatewayActName  = findViewIdAndCast(R.id.Fragment_Noble_Gateway_ET_Noble_Gateway_AccountName);
		nobleGatewayKeyName  = findViewIdAndCast(R.id.Fragment_Noble_Gateway_ET_Noble_Gateway_KEY_Name);
		nobleGatewayKey      = findViewIdAndCast(R.id.Fragment_Noble_Gateway_ET_Noble_Gateway_Key);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);		

		if(!MyPreferences.getMyPreference(NOBLE_NAME, mContext).isEmpty()){
			nobleGatewayActName.setText(MyPreferences.getMyPreference(NOBLE_NAME, mContext));	
			nobleGatewayKeyName.setText(MyPreferences.getMyPreference(NOBLE_API_KEY_NAME, mContext));
			nobleGatewayKey.setText(MyPreferences.getMyPreference(NOBLE_API_KEY, mContext));
		}
	}
}

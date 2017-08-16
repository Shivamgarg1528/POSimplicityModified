package com.Fragments;

import com.posimplicity.gateway.ProPayGateway;
import com.posimplicity.gateway.ProPayGateway.OnCallBacks;
import com.JsonPakage.JSONObject;
import com.utils.MyPreferences;
import com.posimplicity.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ProPayFragment extends BaseFragment  {

	public Button createPayer;
	public Button deletePayer;
	public String payerId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView 		= inflater.inflate(R.layout.fragment_propay, container, false);
		createPayer 	= findViewIdAndCast(R.id.createPayer);		
		deletePayer  	= findViewIdAndCast(R.id.paymentMethod);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);

		payerId        	= MyPreferences.getMyPreference(PAYER_ID, mContext);		
		createPayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ProPayGateway(getActivity(),new OnCallBacks() {

					@Override
					public void onSuccessfullCreation(String key) {
						if(key.isEmpty()){
							Toast.makeText(getActivity(), "Please Create Payer Id Again", Toast.LENGTH_SHORT).show();
						}
						else{
							try{
								JSONObject jsonObject = new JSONObject(key);
								JSONObject innerJsonObj = jsonObject.getJSONObject("RequestResult");
								String resultValue = innerJsonObj.getString("ResultValue");
								if(resultValue.equalsIgnoreCase("ORDER_SUCCESS")){
									payerId = jsonObject.getString("ExternalAccountID");
									createPayer.setEnabled(false);
									deletePayer.setEnabled(true);
									MyPreferences.setMyPreference(PAYER_ID, payerId, getActivity());
								}
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}).onCreatePayer();
			}
		});

		deletePayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ProPayGateway(getActivity(),new OnCallBacks() {
					@Override
					public void onSuccessfullCreation(String key) {
						if(key.isEmpty()){
							Toast.makeText(getActivity(), "Please Delete Payer Id Again", Toast.LENGTH_SHORT).show();
						}
						else{
							try{
								JSONObject jsonObject = new JSONObject(key);
								String  resultValue = jsonObject.getString("ResultValue");
								if(resultValue.equalsIgnoreCase("ORDER_SUCCESS")){
									payerId = "";
									MyPreferences.setMyPreference(PAYER_ID, payerId, getActivity());
									createPayer.setEnabled(true);
									deletePayer.setEnabled(false);
								}
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}).onPayerIdDelete();
			}
		});

		if(MyPreferences.getMyPreference(PAYER_ID, getActivity()).isEmpty()){
			createPayer.setEnabled(true);
			deletePayer.setEnabled(false);
		}
		else{
			createPayer.setEnabled(false);
			deletePayer.setEnabled(true);
		}
	}
}

package com.Fragments;

import com.utils.MyPreferences;
import com.posimplicity.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationFragment extends BaseFragment {
		
	private Button saveBtn;
	private EditText editBox;	
	private String url;

	public RegistrationFragment(){}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_registration, container, false);		
		saveBtn   = (Button)   rootView.findViewById(R.id.goBtn);
		editBox = (EditText) rootView.findViewById(R.id.urlOfMagentoAdmin);	
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);		
		url = MyPreferences.getMyPreference(REGISTRATION_URL,mContext);
		editBox.setText(url);		
		saveBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				executeUrl();
			}


		});		
	}

	private void executeUrl() { 

		if(TextUtils.isEmpty(url))
		{ 
			String enteredUrl = editBox.getText().toString().trim();
			if(TextUtils.isEmpty(enteredUrl))
				Toast.makeText(mContext, "Please Enter Url First!!!", Toast.LENGTH_SHORT).show();
			else{
				if (!enteredUrl.startsWith("http://") && !enteredUrl.startsWith("https://"))
					enteredUrl = "https://" + enteredUrl;
				MyPreferences.setMyPreference(REGISTRATION_URL, enteredUrl, mContext);
				Toast.makeText(mContext, "SuccessFully Saved", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			String enteredUrl = editBox.getText().toString().trim();
			if(enteredUrl.equalsIgnoreCase(url)){
				MyPreferences.setMyPreference(REGISTRATION_URL, url, mContext);	
				Toast.makeText(mContext, "SuccessFully Saved", Toast.LENGTH_SHORT).show();
			}		
			else if(enteredUrl.isEmpty()){
				Toast.makeText(mContext, "Url Can't be null",Toast.LENGTH_SHORT).show();
			}
			else
			{
				url = enteredUrl;
				MyPreferences.setMyPreference(REGISTRATION_URL, url, mContext);				
				Toast.makeText(mContext, "SuccessFully Saved", Toast.LENGTH_SHORT).show();
			}
		}		

	}
}

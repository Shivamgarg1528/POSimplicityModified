package com.posimplicity;

import org.json.JSONArray;

import com.utils.StartAndroidActivity;

import android.os.Bundle;
import android.view.View;

public class SyncOptionActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_sync_option);
	}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}

	@Override
	public void onInitViews() {}

	@Override
	public void onListenerRegister() {}

	public void loginSync(View view){
		switch (view.getId()) {

		case R.id.LoginWithOutSync:
			StartAndroidActivity.onActivityStart(true, mContext, OperatorActivity.class);	
			break;

		case R.id.LoginWithSync:
			StartAndroidActivity.onActivityStart(true, mContext, SyncActivity.class);	
			break;

		default:
			break;
		}
	}
}

package com.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.utils.POSApp;

public abstract class TenderBaseFragment extends Fragment {

	protected  Context mContext;
	protected POSApp gloableAppInstance;
	protected  View rootView;
	protected  Activity activity;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		activity           = (Activity) context;
		mContext           = context;
		gloableAppInstance = POSApp.getInstance();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@SuppressWarnings("unchecked")
	public <T> T findViewIdAndCast(int id) {
		return (T) rootView.findViewById(id);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}

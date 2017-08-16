package com.Fragments;

import com.posimplicity.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MaintFragmentAbout extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView    = inflater.inflate(R.layout.fragment_about, container,false);
		return rootView;
	}
}

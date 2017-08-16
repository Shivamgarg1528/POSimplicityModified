package com.Fragments;

import com.PosInterfaces.MyWebClientClass;
import com.utils.MyPreferences;
import com.posimplicity.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class MaintFragmentAdmin extends BaseFragment {

	private WebView webView;
	private ProgressBar progressBar;
	private MyWebClientClass myWebClient;
    ImageButton imageBtn;

	public MaintFragmentAdmin(){}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView         = inflater.inflate(R.layout.fragment_magento_admin, container, false);	
		webView          = findViewIdAndCast(R.id.Fragment_Mangento_WebView_webview);
		progressBar      = findViewIdAndCast(R.id.Fragment_Mangento_ProgressBar_Loading);
		imageBtn         = findViewIdAndCast(R.id.Fragment_Mangento_ImageBtn_Cancel);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);	
		myWebClient = new MyWebClientClass(progressBar, webView);
		String url  = FULL_PATH + MyPreferences.getMyPreference(STORE,mContext) + SUB_URL +"admin";
		myWebClient.loadRequestedUrl(url);		
	}	
}

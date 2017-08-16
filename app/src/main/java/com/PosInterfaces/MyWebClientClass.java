package com.PosInterfaces;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyWebClientClass extends WebViewClient {

	private ProgressBar progessBar;
	private String requestedUrl;
	private WebView controlView;

	public MyWebClientClass(ProgressBar progessBar,WebView controlView) {
		super();
		this.progessBar         = progessBar;
		this.controlView        = controlView;
		this.controlView.setWebViewClient(this);		
		WebSettings webSettings = controlView.getSettings(); 
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setBuiltInZoomControls(true);		
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);		

	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		progessBar.setVisibility(View.INVISIBLE);
	}

	public void loadRequestedUrl(String requestedUrl) {
		System.out.println("Page Url : - >  "+requestedUrl);
		this.requestedUrl = requestedUrl;
		this.progessBar.setVisibility(View.VISIBLE);

		if(controlView != null)
			controlView.loadUrl(this.requestedUrl);
	}
}

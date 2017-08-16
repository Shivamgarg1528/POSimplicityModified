package com.Fragments;

import java.util.ArrayList;
import com.Beans.NavDrawerItemModel;
import com.adapter.NavDrawerListAdapter;
import com.posimplicity.R;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MaintFragmentSupport extends BaseFragment {
	private ListView listView;
	private WebView wb;
	private ProgressBar pBar;
	private ArrayList<NavDrawerItemModel> dataList;
	private TypedArray navMenuIcons;

	public MaintFragmentSupport(){
	}

	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		dataList = new ArrayList<NavDrawerItemModel>();
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_support_new, container, false);
		listView       = (ListView)rootView.findViewById(R.id.Fragment_CCAdmin_ListView_Gateways);
		wb             = (WebView)rootView.findViewById(R.id.supportWV);
		pBar           = (ProgressBar)rootView.findViewById(R.id.supportPB);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		dataList.add(new NavDrawerItemModel("Support",               navMenuIcons.getResourceId(0, -1)));
		dataList.add(new NavDrawerItemModel("User Guide",            navMenuIcons.getResourceId(0, -1)));
		dataList.add(new NavDrawerItemModel("Knowledge Base",        navMenuIcons.getResourceId(0, -1)));
		dataList.add(new NavDrawerItemModel("Get CC Process",        navMenuIcons.getResourceId(0, -1)));
		
		navMenuIcons.recycle();
		NavDrawerListAdapter listAdapter = new NavDrawerListAdapter(mContext, dataList);
		listView.setAdapter(listAdapter);		
		listView.setOnItemClickListener(new MyOnItemClick());	
		WebSettings webSettings = wb.getSettings(); 
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setBuiltInZoomControls(true);		
		wb.setWebViewClient(new MyWebClient());	
		wb.setVisibility(View.VISIBLE);		
		displayView(0);

	}

	private class MyOnItemClick implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {		
			displayView(position);
		}
	}
	private void displayView(int position) {

		switch (position) {
		case 0:
			wb.loadUrl("http://support.posimplicity.com");			
			break;
		case 1 :
			wb.loadUrl("http://posimplicity.net/userguide/userguide.htm");
			break;
		case 2:
			wb.loadUrl("http://support.posimplicity.com");
			break;
		case 3 :
			wb.loadUrl("http://posimplicity.net/mif");
		default:
			break;
		}
		listView.setItemChecked(position, true);
		listView.setSelection(position);	
	}
	
	public class MyWebClient extends WebViewClient {
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
			pBar.setVisibility(View.GONE);
		}
	}
}


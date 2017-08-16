package com.PosInterfaces;

import android.webkit.JavascriptInterface;

public interface AddCustomerInterface {

	@JavascriptInterface
	public void showCustomerInfo(String jsonData);

	@JavascriptInterface
	public void showErrorMsg();

}

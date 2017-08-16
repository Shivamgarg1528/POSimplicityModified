package com.Dialogs;

import com.PosInterfaces.KeyboardKey;
import com.PosInterfaces.PrefrenceKeyConst;
import com.PosInterfaces.WebServiceCallObjectIds;
import com.utils.POSApp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public abstract class BaseDialog extends Dialog implements WebServiceCallObjectIds,PrefrenceKeyConst,KeyboardKey {

	private View rootView;
	private LayoutInflater layoutInflater;
	protected Context mContext;
	protected POSApp gApp;


	public BaseDialog(Context context, int theme , int width ,int height , boolean isOutSideTouch , boolean isCancelable, int layoutId) {
		super(context, theme);
		mContext       = context;
		gApp           = POSApp.getInstance();
		layoutInflater = LayoutInflater.from(context);
		rootView       = layoutInflater.inflate(layoutId, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(isOutSideTouch);
		setContentView(rootView);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		getWindow().setLayout(width, height);
		setCancelable(isCancelable);
	}

	@SuppressWarnings("unchecked")
	public <T> T findViewByIdAndCast(int id) {
		return (T) rootView.findViewById(id);
	}
}

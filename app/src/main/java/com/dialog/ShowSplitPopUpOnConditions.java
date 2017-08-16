package com.dialog;

import com.Dialogs.SplitViewDialog;
import com.posimplicity.R;

import android.content.Context;

public class ShowSplitPopUpOnConditions {	
	
	private Context mContext;
	private int width;
	private int height;
	private float subtotalAmt;
	private String pAYMENT_MODE; 
	private String oRDER_STATUS;	

	public ShowSplitPopUpOnConditions(Context mContext, int width, int height,float subtotalAmt, String pAYMENT_MODE,String oRDER_STATUS) {
		super();
		this.mContext = mContext;
		this.width = width;
		this.height = height;
		this.subtotalAmt = subtotalAmt;
		this.pAYMENT_MODE = pAYMENT_MODE;
		this.oRDER_STATUS = oRDER_STATUS;
	}

	public  void letShow(){
		
		SplitViewDialog seatSelectionDialog = new SplitViewDialog(mContext, R.style.myCoolDialog, width, height, true, true, R.layout.pop_on_table_selection);
		seatSelectionDialog.setData(subtotalAmt,pAYMENT_MODE,oRDER_STATUS);
		seatSelectionDialog.show();
	}
}

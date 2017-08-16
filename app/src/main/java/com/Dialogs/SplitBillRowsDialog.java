package com.Dialogs;

import java.util.ArrayList;
import java.util.List;

import com.Beans.SplitBillClass;
import com.adapter.SplitBillAdapter;
import com.utils.MyStringFormat;
import com.posimplicity.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SplitBillRowsDialog extends BaseDialog{

	private int noOfBills;
	private float amountToBeSplit;
	private List<SplitBillClass> listOfSplittedRow;
	private float eachBillAmount;
	private ListView billsListView;
	private SplitBillAdapter adapter;
	private View cancelDialog;
	public String pAYMENT_MODE; 
	public String oRDER_STATUS;

	public SplitBillRowsDialog(Context context, int theme, int width,
			int height, boolean isOutSideTouch, boolean isCancelable,
			int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		billsListView     = findViewByIdAndCast(R.id.listView1);
		cancelDialog      = findViewByIdAndCast(R.id.btnCancelCC);
		listOfSplittedRow = new ArrayList<>();
		adapter           = new SplitBillAdapter(mContext, listOfSplittedRow,this);
		adapter.setCustomAdapters(adapter);
		billsListView.setAdapter(adapter);

		for(int index = 0 ; index < noOfBills ; index++){
			listOfSplittedRow.add(new SplitBillClass(index, 0, index + 1, MyStringFormat.formatWith2DecimalPlaces(eachBillAmount), MyStringFormat.formatWith2DecimalPlaces(amountToBeSplit), MyStringFormat.formatWith2DecimalPlaces(eachBillAmount), "0.00", MyStringFormat.formatWith2DecimalPlaces(eachBillAmount), false, false,false));
		}

		adapter.notifyDataSetChanged();
		cancelDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void setData(int noOfBills, float amountToBeSplit, String pAYMENT_MODE, String oRDER_STATUS) {
		this.noOfBills       =  noOfBills;
		this.amountToBeSplit =  amountToBeSplit;
		this.eachBillAmount  = amountToBeSplit / noOfBills;
		this.pAYMENT_MODE    = pAYMENT_MODE;
		this.oRDER_STATUS    = oRDER_STATUS;
	}
}

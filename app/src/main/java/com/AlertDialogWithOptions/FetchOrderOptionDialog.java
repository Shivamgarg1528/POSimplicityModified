package com.AlertDialogWithOptions;

import com.posimplicity.ReprintActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

public class FetchOrderOptionDialog {

	private Context mContext;
	private final String[] LIST_ITMES = {"Last Transaction","Last 10 Orders","Today's Orders"};
	private ReprintActivity reprintActivity;
	private int index = -1;

	public FetchOrderOptionDialog(Context mContext, ReprintActivity reprintActivity) {
		super();
		this.mContext = mContext;
		this.reprintActivity = reprintActivity;
	}

	public void showFetchOptionsDialog() {

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice,LIST_ITMES);

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);    
		builderSingle.setTitle("Select One Option To Fetch Orders:-");
		builderSingle.setAdapter(arrayAdapter,  new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				index = which;
				reprintActivity.showView(index);
				
			}
		});

		/*builderSingle.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if(index == -1)
					new FetchOrderOptionDialog(mContext, reprintActivity).showFetchOptionsDialog();
				else
					
				dialog.dismiss();
			}
		});

		builderSingle.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});*/

		AlertDialog alert = builderSingle.create();
		alert.show();

	}
}

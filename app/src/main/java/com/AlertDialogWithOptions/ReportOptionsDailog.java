package com.AlertDialogWithOptions;

import com.posimplicity.database.local.SecurityTable;
import com.utils.SecurityVerification;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

public class ReportOptionsDailog {

	private Context mContext;
	private final String[] LIST_ITMES = {"Shift Report","Daily Report","Tip Report"};

	public ReportOptionsDailog(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void showReportOptionsDialog() {

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice,LIST_ITMES);

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);    
		builderSingle.setTitle("Select One Report:-");
		builderSingle.setAdapter(arrayAdapter,  new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {

				case 0:
					new SecurityVerification(mContext,SecurityTable.Settings_ShiftReport).shiftReportFunctionChecking();
					break;

				case 1:
					new SecurityVerification(mContext,SecurityTable.Settings_DailyReport).dailyReportFunctionChecking();
					break;
					
				case 2:
					new SecurityVerification(mContext,SecurityTable.Settings_TipReport).tipReportFunctionChecking();
					break;

				default:
					break;
				} 
			}
		});
		builderSingle.show();

	}
}

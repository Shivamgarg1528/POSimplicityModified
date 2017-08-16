package com.dialog;

import com.posimplicity.database.local.StaffTable;
import com.Dialogs.ShowClerkWithPayGradeDialog;
import com.PosInterfaces.MyWebClientClass;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CalculateWidthAndHeigth;
import com.utils.POSApp;
import com.posimplicity.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ClerkLoginLogoutTimeDialog implements PrefrenceKeyConst {

	private Context mContext;
	public static final int LOGIN     = 0x000;
	public static final int LOGOUT    = 0x001;
	
	public ClerkLoginLogoutTimeDialog(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void onClerkLoginLogoutTimeDialog(final MyWebClientClass webClientClass,final String storeName){

		final int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(POSApp.getInstance().getDeviceWidth(), 60);
		final int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(POSApp.getInstance().getDeviceHeight(),70);


		boolean anyLoginLeft  = new StaffTable(mContext).getStatusOfAllStaff(false);
		boolean anyLogoutLeft = new StaffTable(mContext).getStatusOfAllStaff(true);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("Select Any Option...");

		if(anyLoginLeft){

			builder.setPositiveButton("Login User", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ShowClerkWithPayGradeDialog dailog = new ShowClerkWithPayGradeDialog(mContext, R.style.myCoolDialog, width, height, false, false, R.layout.dialog_show_clerk_login_logout);
					dailog.show();

					if(dailog.isShowing())
						dailog.show(LOGIN,webClientClass,FULL_PATH + storeName + SUB_URL +"functions/timeclock/gettimeclock.htm");					
				}
			});
		}

		if(anyLogoutLeft){

			builder.setNegativeButton("Logout User", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					ShowClerkWithPayGradeDialog dailog = new ShowClerkWithPayGradeDialog(mContext, R.style.myCoolDialog, width, height, false, false, R.layout.dialog_show_clerk_login_logout);
					dailog.show();

					if(dailog.isShowing())
						dailog.show(LOGOUT,webClientClass,FULL_PATH + storeName + SUB_URL +"functions/timeclock/gettimeclock.htm");		
				}
			});
		}

		builder.show();		
		builder.create();

	}

}

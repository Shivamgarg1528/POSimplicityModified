package com.AlertDialogWithOptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.FixedStorage;
import com.utils.MyPreferences;
import com.posimplicity.R;

public class DrawerOpenOptionsDialog implements PrefrenceKeyConst{

	private Context context;
	
	public DrawerOpenOptionsDialog(Context context) {
		super();
		this.context = context;
	}

	public void showDrawerOpenOptionDialog() {

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);    
		builderSingle.setTitle("Select Drawer Options :- ");
		builderSingle.setIcon(R.drawable.app_icon);

		boolean cashDrawer   = MyPreferences.getBooleanPreferencesWithDefalutTrue(DRAWER_CASH, context);
		boolean checkDrawer  = MyPreferences.getBooleanPrefrences(DRAWER_CHECK, context);
		boolean ccDrawer     = MyPreferences.getBooleanPrefrences(DRAWER_CC, context);

		builderSingle.setMultiChoiceItems(FixedStorage.LIST_ITMES, new boolean []{cashDrawer,ccDrawer,checkDrawer}, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				switch (which) {
				case 0:
					MyPreferences.setBooleanPrefrences(DRAWER_CASH, isChecked, context);
					break;
				case 1:
					MyPreferences.setBooleanPrefrences(DRAWER_CC, isChecked, context);

					break;
				case 2:
					MyPreferences.setBooleanPrefrences(DRAWER_CHECK, isChecked, context);

					break;

				default:
					break;
				}
			}
		});
		builderSingle.show();
	}
}

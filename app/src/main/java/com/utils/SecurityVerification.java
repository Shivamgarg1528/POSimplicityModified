package com.utils;

import com.dialog.OverrideClerkSettingForSingleTime;
import com.Beans.POSAuthority;
import com.posimplicity.database.local.SecurityTable;
import com.Dialogs.DiscountDialog;
import com.Dialogs.RefundItemsWithAmt;
import com.PosInterfaces.PrefrenceKeyConst;
import com.RecieptPrints.PrintExtraReceipt;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.posimplicity.DailyReportAsChild;
import com.posimplicity.MaintenanceActivity;
import com.posimplicity.ShiftReportActivity;
import com.posimplicity.TipReportActivity;

import android.content.Context;

public class SecurityVerification implements PrefrenceKeyConst { 

	private Context mContext;
	private String   roleName;
	private POSAuthority  posAutho;
	private String settingName;

	public SecurityVerification(Context mContext) {
		super();
		this.mContext          = mContext;
	}

	public SecurityVerification(Context mContext,String settingName) {
		super();
		this.mContext          = mContext;
		this.roleName          = MyPreferences.getMyPreference(SECURITY_LOGIN_USER_Id, this.mContext);
		this.posAutho          = new SecurityTable(mContext).getSingleObjBasedOnSettingNameAndRoleName(settingName);
		this.settingName       = settingName;
	}

	private boolean isSettingOn(){
		return new SecurityTable(mContext).isSettingOn(settingName,roleName);
	}

	private void showDisableSettingToast(){
		ToastUtils.showOwnToast(mContext, String.format("Contact Super Admin To Unlock %s Functionality", settingName));
	}

	private boolean roleIsAdmin(){
		if(roleName.equalsIgnoreCase(SecurityTable.Admin))
			return true;
		else
			return false;
	}

	private boolean roleIsClerk(){
		if(roleName.equalsIgnoreCase(SecurityTable.Clerk))
			return true;
		else
			return false;
	}

	public  void drawerFunctionChecking(Context mContext){
		if(roleIsAdmin() || isSettingOn() || posAutho.isSettingOverrideByManager()){
			
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
				new UsbPR(mContext, new PrinterCallBack() {
					
					@Override
					public void onDisconnected() {}
					
					@Override
					public void onConnected(BasePR pPrinter) {
						PrintExtraReceipt.onOpenCashDrawer(pPrinter);
					}
				}).onConnectionStart();
			}
			
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){
				PrintExtraReceipt.onOpenCashDrawer(POSApp.getInstance().getPrinterBT());
			}
			
		}
		else if(!roleIsClerk())
			showDisableSettingToast();
		else
			new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
	}

	public void adminFunctionChecking(){

		if(roleIsAdmin() || isSettingOn() || posAutho.isSettingOverrideByManager())
			StartAndroidActivity.onActivityStart(false, mContext, MaintenanceActivity.class);
		else if(!roleIsClerk())
			showDisableSettingToast();
		else
			new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
	}

	public  void shiftReportFunctionChecking(){

		if(roleIsAdmin() || isSettingOn() || posAutho.isSettingOverrideByManager())
			StartAndroidActivity.onActivityStart(false, mContext, ShiftReportActivity.class);
		else if(!roleIsClerk())
			showDisableSettingToast();
		else
			new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
	}

	public  void dailyReportFunctionChecking(){

		if(roleIsAdmin() || isSettingOn() || posAutho.isSettingOverrideByManager())
			StartAndroidActivity.onActivityStart(false, mContext, DailyReportAsChild.class);
		else if(!roleIsClerk())
			showDisableSettingToast();
		else
			new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
	}

	public  void tipReportFunctionChecking(){

		if(roleIsAdmin() || isSettingOn() || posAutho.isSettingOverrideByManager())
			StartAndroidActivity.onActivityStart(false, mContext, TipReportActivity.class);
		else if(!roleIsClerk())
			showDisableSettingToast();
		else
			new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
	}



	public void refundFunctionChecking(RefundItemsWithAmt dilog, int requestCode){

		if(roleIsAdmin() || isSettingOn() || posAutho.isSettingOverrideByManager()){
			dilog.show(requestCode);
		}
		else if(!roleIsClerk())
			showDisableSettingToast();
		else
			new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
	}


	public void discountFunctionChecking(DiscountDialog discountDialog) {

		if(roleIsAdmin() || isSettingOn() || posAutho.isSettingOverrideByManager())
			discountDialog.show();
		else if(!roleIsClerk())
			showDisableSettingToast();
		else
			new OverrideClerkSettingForSingleTime(mContext,posAutho).overrideClerkSettings();
	}
}

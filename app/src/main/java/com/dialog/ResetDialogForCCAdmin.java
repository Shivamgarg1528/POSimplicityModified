package com.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.utils.BluetoothHelper;
import com.posimplicity.service.ServiceUtils;
import com.Fragments.BaseFragment;
import com.Fragments.BridgePaymentFragment;
import com.Fragments.CCAdminSettingsFragment;
import com.Fragments.DejavooFragment;
import com.Fragments.MaintFragmentCCAdmin;
import com.Fragments.NobleFragment;
import com.Fragments.PlugNPayFragment;
import com.Fragments.ProPayFragment;
import com.Fragments.TSYSFragment;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.R;

public class ResetDialogForCCAdmin implements PrefrenceKeyConst{

	public static void showResetDialog(final Context mContext ,final int requestedClick,final BaseFragment visibleFragment){

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("Continue,To Reset All Settings!!!");
		builder.setCancelable(false);

		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (requestedClick) {

				case MaintFragmentCCAdmin.PLUG_N_PAY_ID:	

					PlugNPayFragment reference =  (PlugNPayFragment)visibleFragment;
					reference.plugNPayFragmentEditTextId.setText("");
					MyPreferences.setMyPreference(PLUG_PAY_ID, "", mContext);

					break;

				case MaintFragmentCCAdmin.BRIDGE_PAY_ID:

					BridgePaymentFragment brPaymentFragment = (BridgePaymentFragment) visibleFragment;
					brPaymentFragment.bridgeGatewayUserName.setText("");
					brPaymentFragment.bridgeGatewayPassword.setText("");
					MyPreferences.setMyPreference(BRIDGE_GATEWAY_USERNAME,"" ,mContext);
					MyPreferences.setMyPreference(BRIDGE_GATEWAY_PASSWORD,"" ,mContext);

					break;

				case MaintFragmentCCAdmin.NOBLES_PAY_ID:

					NobleFragment nobleFragment = (NobleFragment)visibleFragment;					
					nobleFragment.nobleGatewayActName.setText("");					
					nobleFragment.nobleGatewayKeyName.setText("");
					nobleFragment.nobleGatewayKey.setText("");
					MyPreferences.setMyPreference(NOBLE_NAME,"" ,mContext);					
					MyPreferences.setMyPreference(NOBLE_API_KEY,"" ,mContext);
					MyPreferences.setMyPreference(NOBLE_API_KEY_NAME,"" ,mContext);
					
					break;

				case MaintFragmentCCAdmin.TSYS_PAY_ID:

					TSYSFragment tsysFragment = (TSYSFragment)visibleFragment;
					tsysFragment.transactionKey = "";
					tsysFragment.userNameEditText.setText("");
					tsysFragment.passwordEditText.setText("");
					tsysFragment.deviceIdEditText.setText("");
					tsysFragment.merchantIdEditText.setText("");
					tsysFragment.generateKeyButton.setEnabled(true);
					tsysFragment.updatekeyButton.setEnabled(false);
					MyPreferences.setMyPreference(TSYS_DEVICE_ID, "", mContext);
					MyPreferences.setMyPreference(TSYS_MERCHA_ID, "", mContext);
					MyPreferences.setMyPreference(TSYS_ORDER_TRANSACTION_ID, "", mContext);
					MyPreferences.setMyPreference(TSYS_PASSWORD, "", mContext);
					MyPreferences.setMyPreference(TSYS_TRANSACTION_KEY, "", mContext);
					MyPreferences.setMyPreference(TSYS_USER_NAME, "", mContext);
					
					break;



				case MaintFragmentCCAdmin.PROPAY_PAY_ID:

					ProPayFragment proPayFragment = (ProPayFragment) visibleFragment;
					proPayFragment.createPayer.setEnabled(true);
					proPayFragment.deletePayer.setEnabled(false);
					proPayFragment.payerId       = "";
					MyPreferences.setMyPreference(PAYER_ID, "", mContext);

					break;

				case MaintFragmentCCAdmin.DEJAVO_PAY_ID:
					
					DejavooFragment  dejavooFragment = (DejavooFragment)visibleFragment;
					dejavooFragment.editTextIpAddress.setText("");
					dejavooFragment.mSwitchDejavoo.setChecked(false);
					dejavooFragment.mSwitchPromptDC.setChecked(false);
					dejavooFragment.mSwitchState.setChecked(false);
					
					int position = (int)MyPreferences.getLongPreferenceWithDiffDefValue(DEJAVO_OPTION, mContext);
					
					if(position >= 0)
						dejavooFragment.listView.setItemChecked(position, false);
					
					MyPreferences.setMyPreference(DEJAVOO_IP_ADDRESS, "", mContext);
					MyPreferences.setLongPreferences(DEJAVO_OPTION  , -1, mContext);
					MyPreferences.setBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, false,mContext);
					MyPreferences.setBooleanPrefrences(DEJAVO_PRMOPT_D_C, false,mContext);
					MyPreferences.setMyPreference(PrefrenceKeyConst.REMOTE_DEVICE_ADDRESS, "", mContext);
					BluetoothHelper.closeBluetoothSocketManually();
					dejavooFragment.setMacAddressOnTextView();
					Variables.forceCloseBluetooth = false;
					ServiceUtils.operateBTDejavooService(mContext, false);

					break;
					
				case MaintFragmentCCAdmin.SETTINGS:

					CCAdminSettingsFragment ccAdminSettingFragment = (CCAdminSettingsFragment)visibleFragment;
					int gatewayPosition             = ccAdminSettingFragment.anyItemSelectedFromListView();

					if(gatewayPosition >= 0){
						ccAdminSettingFragment.listView.setItemChecked(gatewayPosition, false);						
					}
					MyPreferences.setLongPreferences(GATEWAY_USED_POSITION, -1, mContext);
					
					break;

				default:
					break;
				}
				ToastUtils.showOwnToast(mContext, "Reseted Successfully");
			}			
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});

		builder.show();		
		builder.create();		
	}
}

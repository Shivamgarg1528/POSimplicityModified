package com.Fragments;

import java.util.ArrayList;

import com.dialog.ResetDialogForCCAdmin;
import com.Beans.NavDrawerItemModel;
import com.adapter.NavDrawerListAdapter;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;

import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MaintFragmentCCAdmin extends BaseFragment implements OnItemClickListener, OnClickListener {


    public static final String PLUG_N_PAY = "PlugNPay";
    public static final String BRIDGE_PAY = "BridgePay";
    public static final String NOBLE_ = "Noble";
    public static final String TSYS_ = "TSYS";
    public static final String PROPAY_ = "ProPay";
    public static final String DEJAVOO = "Dejavoo";
    public static final String CC_SETTING = "Settings";
    public static final String GATEWAY = " GateWay";

    public static final int PLUG_N_PAY_ID = 0;
    public static final int BRIDGE_PAY_ID = 1;
    public static final int TSYS_PAY_ID = 2;
    public static final int PROPAY_PAY_ID = 3;
    public static final int DEJAVO_PAY_ID = 4;
    public static final int NOBLES_PAY_ID = 6;
    public static final int SETTINGS = 5;

    private ListView listView;
    private TextView gateWayName;
    private Button saveBtn, resetBtn;
    private ArrayList<NavDrawerItemModel> dataList;
    private TypedArray navMenuIcons;
    private BaseFragment visibleFragment;
    private int visiblePosition = -1;

    public MaintFragmentCCAdmin() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<NavDrawerItemModel>();
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_creditcard, container, false);
        listView = findViewIdAndCast(R.id.Fragment_CCAdmin_ListView_Gateways);
        gateWayName = findViewIdAndCast(R.id.Fragment_CCAdmin_TextView_Gateways_Name);
        saveBtn = findViewIdAndCast(R.id.SaveCCADMIN);
        resetBtn = findViewIdAndCast(R.id.RESET_CC_ADMIN);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataList.add(new NavDrawerItemModel(PLUG_N_PAY, navMenuIcons.getResourceId(5, -1)));
        dataList.add(new NavDrawerItemModel(BRIDGE_PAY, navMenuIcons.getResourceId(5, -1)));
        dataList.add(new NavDrawerItemModel(TSYS_, navMenuIcons.getResourceId(5, -1)));
        dataList.add(new NavDrawerItemModel(PROPAY_, navMenuIcons.getResourceId(5, -1)));
        dataList.add(new NavDrawerItemModel(DEJAVOO, navMenuIcons.getResourceId(5, -1)));
        //dataList.add(new NavDrawerItemModel(NOBLE_    ,        navMenuIcons.getResourceId(5, -1)));
        dataList.add(new NavDrawerItemModel(CC_SETTING, navMenuIcons.getResourceId(6, -1)));

        navMenuIcons.recycle();

        NavDrawerListAdapter listAdapter = new NavDrawerListAdapter(mContext, dataList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        saveBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

        long storedPositon = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION, mContext);
        displayView(storedPositon >= 0 ? (int) storedPositon : 2);

    }

    private void displayView(int position) {
        visiblePosition = position;

        switch (position) {

            case PLUG_N_PAY_ID:
                visibleFragment = new PlugNPayFragment();
                gateWayName.setText(PLUG_N_PAY + GATEWAY);
                break;

            case BRIDGE_PAY_ID:
                visibleFragment = new BridgePaymentFragment();
                gateWayName.setText(BRIDGE_PAY + GATEWAY);
                break;

            case NOBLES_PAY_ID:
                visibleFragment = new NobleFragment();
                gateWayName.setText(NOBLE_ + GATEWAY);
                break;


            case TSYS_PAY_ID:
                visibleFragment = new TSYSFragment();
                gateWayName.setText(TSYS_ + GATEWAY);
                break;

            case PROPAY_PAY_ID:
                visibleFragment = new ProPayFragment();
                gateWayName.setText(PROPAY_ + GATEWAY);
                break;


            case DEJAVO_PAY_ID:
                visibleFragment = new DejavooFragment();
                gateWayName.setText(DEJAVOO + GATEWAY);
                break;

            case SETTINGS:
                visibleFragment = new CCAdminSettingsFragment();
                gateWayName.setText(CC_SETTING);

            default:
                break;

        }

        if (visibleFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.child_frame_container, visibleFragment).commit();
            listView.setItemChecked(position, true);
            listView.setSelection(position);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        displayView(position);
    }

    public void showToastWhenInfoIncompete(String msg) {
        String toastMsg = "Please Provide Info For " + msg + " Before Save";
        ToastUtils.showOwnToast(mContext, toastMsg);
    }

    public void showToastWhenInfoCompeteySave(String msg) {
        String toastMsg = "Saved Provide Info For " + msg;
        ToastUtils.showOwnToast(mContext, toastMsg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.SaveCCADMIN:

                switch (visiblePosition) {

                    case PLUG_N_PAY_ID:

                        PlugNPayFragment plugNPayFragment = (PlugNPayFragment) visibleFragment;
                        String plugNPayId = plugNPayFragment.plugNPayFragmentEditTextId.getText().toString().trim();

                        if (plugNPayId.isEmpty()) {
                            showToastWhenInfoIncompete(PLUG_N_PAY);
                            return;
                        }

                        MyPreferences.setMyPreference(PLUG_PAY_ID, plugNPayId, mContext);
                        showToastWhenInfoCompeteySave(PLUG_N_PAY);

                        break;

                    case BRIDGE_PAY_ID:

                        BridgePaymentFragment brPaymentFragment = (BridgePaymentFragment) visibleFragment;
                        String bridgeGatewayUserName = brPaymentFragment.bridgeGatewayUserName.getText().toString().trim();
                        String bridgeGatewayPassword = brPaymentFragment.bridgeGatewayPassword.getText().toString().trim();

                        if (bridgeGatewayUserName.isEmpty() || bridgeGatewayPassword.isEmpty()) {
                            showToastWhenInfoIncompete(BRIDGE_PAY);
                            return;
                        }

                        MyPreferences.setMyPreference(BRIDGE_GATEWAY_USERNAME, bridgeGatewayUserName, mContext);
                        MyPreferences.setMyPreference(BRIDGE_GATEWAY_PASSWORD, bridgeGatewayPassword, mContext);
                        showToastWhenInfoCompeteySave(BRIDGE_PAY);

                        break;

                    case NOBLES_PAY_ID:

                        NobleFragment nobleFragment = (NobleFragment) visibleFragment;
                        String nobleActName = nobleFragment.nobleGatewayActName.getText().toString().trim();
                        String nobleKeyName = nobleFragment.nobleGatewayKeyName.getText().toString().trim();
                        String nobleKey = nobleFragment.nobleGatewayKey.getText().toString().trim();

                        if (nobleActName.isEmpty() || nobleKeyName.isEmpty() || nobleKey.isEmpty()) {
                            showToastWhenInfoIncompete(NOBLE_);
                            return;
                        }

                        MyPreferences.setMyPreference(NOBLE_NAME, nobleActName, mContext);
                        MyPreferences.setMyPreference(NOBLE_API_KEY_NAME, nobleKeyName, mContext);
                        MyPreferences.setMyPreference(NOBLE_API_KEY, nobleKey, mContext);
                        showToastWhenInfoCompeteySave(NOBLE_);

                        break;

                    case TSYS_PAY_ID:

                        TSYSFragment tsysFragment = (TSYSFragment) visibleFragment;

                        if (tsysFragment.transactionKey.isEmpty()) {
                            ToastUtils.showOwnToast(mContext, "Please Generate Key For " + TSYS_);
                            return;
                        }
                        //new TSYSEnvirnoment(mContext).showTSYSEnvirnomentDialog(this);
                        showToastWhenInfoCompeteySave(TSYS_);

                        break;

                    case PROPAY_PAY_ID:

                        ProPayFragment proPayFragment = (ProPayFragment) visibleFragment;

                        if (proPayFragment.payerId.isEmpty()) {
                            ToastUtils.showOwnToast(mContext, "Please Generate Payer ID For " + PROPAY_);
                            return;
                        }
                        showToastWhenInfoCompeteySave(PROPAY_);
                        break;

                    case DEJAVO_PAY_ID:

                        DejavooFragment dejavoFragment = (DejavooFragment) visibleFragment;
                        String dejavoIpAddress = dejavoFragment.editTextIpAddress.getText().toString().trim();
                        int optionPosition = dejavoFragment.anyItemSelectedFromListView();
                        boolean paymentViaBlue = MyPreferences.getBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext);

                        if (!paymentViaBlue) {
                            if (dejavoIpAddress.isEmpty() || optionPosition < 0) {
                                showToastWhenInfoIncompete(DEJAVOO);
                                return;
                            }
                        } else {
                            if (optionPosition < 0) {
                                showToastWhenInfoIncompete(DEJAVOO);
                                return;
                            }
                        }
                        MyPreferences.setMyPreference(DEJAVOO_IP_ADDRESS, dejavoIpAddress, mContext);
                        MyPreferences.setLongPreferences(DEJAVO_OPTION, optionPosition, mContext);

                        showToastWhenInfoCompeteySave(DEJAVOO);
                        break;

                    case SETTINGS:

                        CCAdminSettingsFragment ccAdminSettingFragment = (CCAdminSettingsFragment) visibleFragment;
                        int gatewayPosition = ccAdminSettingFragment.anyItemSelectedFromListView();

                        if (gatewayPosition < 0) {
                            ToastUtils.showOwnToast(mContext, "Please Select Any Gateway For CC Payment");
                            return;
                        }

                        boolean breakCommandExecute = false;
                        switch (gatewayPosition) {

                            case PLUG_N_PAY_ID:

                                String plugId = MyPreferences.getMyPreference(PLUG_PAY_ID, mContext);
                                if (plugId.isEmpty()) {
                                    showToastWhenInfoIncompete(PLUG_N_PAY);
                                    breakCommandExecute = true;
                                }

                                break;

                            case BRIDGE_PAY_ID:

                                String bridgeUserName = MyPreferences.getMyPreference(BRIDGE_GATEWAY_USERNAME, mContext);
                                String bridgePassword = MyPreferences.getMyPreference(BRIDGE_GATEWAY_PASSWORD, mContext);

                                if (bridgeUserName.isEmpty() || bridgePassword.isEmpty()) {
                                    showToastWhenInfoIncompete(BRIDGE_PAY);
                                    breakCommandExecute = true;
                                }

                                break;

                            case NOBLES_PAY_ID:

                                String nobleActNamess = MyPreferences.getMyPreference(NOBLE_NAME, mContext);
                                String nobleKeyNamess = MyPreferences.getMyPreference(NOBLE_API_KEY_NAME, mContext);
                                String nobleKeyss = MyPreferences.getMyPreference(NOBLE_API_KEY, mContext);

                                if (nobleActNamess.isEmpty() || nobleKeyNamess.isEmpty() || nobleKeyss.isEmpty()) {
                                    showToastWhenInfoIncompete(NOBLE_);
                                    breakCommandExecute = true;
                                }

                                break;
                            case TSYS_PAY_ID:

                                String transactionkey = MyPreferences.getMyPreference(TSYS_TRANSACTION_KEY, mContext);

                                if (transactionkey.isEmpty()) {
                                    showToastWhenInfoIncompete(TSYS_);
                                    breakCommandExecute = true;
                                }


                                break;
                            case PROPAY_PAY_ID:

                                String proPayId = MyPreferences.getMyPreference(PAYER_ID, mContext);
                                if (proPayId.isEmpty()) {
                                    showToastWhenInfoCompeteySave(PROPAY_);
                                    breakCommandExecute = true;
                                }

                                break;
                            case DEJAVO_PAY_ID:

                                String dejavoIpAddresss = MyPreferences.getMyPreference(DEJAVOO_IP_ADDRESS, mContext);
                                long optionPositions = MyPreferences.getLongPreferenceWithDiffDefValue(DEJAVO_OPTION, mContext);
                                boolean paymentViaBlue1 = MyPreferences.getBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext);

                                if (!paymentViaBlue1) {
                                    if (dejavoIpAddresss.isEmpty() || optionPositions < 0) {
                                        showToastWhenInfoIncompete(DEJAVOO);
                                        breakCommandExecute = true;
                                    }
                                } else {
                                    if (optionPositions < 0) {
                                        showToastWhenInfoIncompete(DEJAVOO);
                                        breakCommandExecute = true;
                                    }
                                }
                                break;

                            default:
                                break;
                        }

                        if (!breakCommandExecute) {
                            MyPreferences.setLongPreferences(GATEWAY_USED_POSITION, gatewayPosition, mContext);
                            showToastWhenInfoCompeteySave(CC_SETTING);
                        } else
                            listView.setItemChecked(gatewayPosition, false);

                        break;

                    default:
                        break;
                }

                break;

            case R.id.RESET_CC_ADMIN:

                ResetDialogForCCAdmin.showResetDialog(mContext, visiblePosition, visibleFragment);

                break;

            default:
                break;
        }
    }
}

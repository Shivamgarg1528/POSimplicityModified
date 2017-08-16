package com.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dialog.BlueToothNotSupported;
import com.posimplicity.service.BTDejavooService;
import com.utils.BluetoothHelper;
import com.posimplicity.service.ServiceUtils;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.DeviceListActivity;
import com.posimplicity.R;

public class DejavooFragment extends BaseFragment {

    public EditText editTextIpAddress;
    public ListView listView;
    public CheckedTextView mSwitchDejavoo, mSwitchPromptDC, mSwitchState;
    public int selectedPosition = -1;
    public static final int RETAIL = 0;
    public static final int RETAIL_TIP = 1;
    public static final int RESTAURANT = 2;
    public static final int RESTAURANT_TIP = 3;
    public String defaultText = "Dejavoo Bluetooth (No Device Selected)";
    public String selectedAddress = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dejavo, container, false);
        editTextIpAddress = findViewIdAndCast(R.id.Fragment_Dejavo_Edt_Ip_Address);
        listView = findViewIdAndCast(R.id.Fragment_Dejavo_ListView_Items);
        mSwitchDejavoo = findViewIdAndCast(R.id.Fragment_Dejavoo_Switch_Payment_Via_Bluetooth);
        mSwitchPromptDC = findViewIdAndCast(R.id.Fragment_Dejavoo_Switch_Prompt_Debit_Credit_Option);
        mSwitchState = findViewIdAndCast(R.id.Fragment_Dejavoo_Switch_State);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case MaintFragmentPrinterSetting.REQUEST_CONNECT_DEVICE:

                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    MyPreferences.setMyPreference(PrefrenceKeyConst.REMOTE_DEVICE_ADDRESS, address, mContext);
                    mSwitchDejavoo.setText("Dejavoo Bluetooth (" + address + ")");
                }

                break;

            case MaintFragmentPrinterSetting.REQUEST_ENABLE_BLUETOOTH_DEVICE:

                if (resultCode == Activity.RESULT_OK)
                    BluetoothHelper.findAndSelectAnyDevice(this, mContext, 1);

                break;
        }
    }

    public void setMacAddressOnTextView() {
        selectedAddress = MyPreferences.getMyPreference(PrefrenceKeyConst.REMOTE_DEVICE_ADDRESS, mContext);
        if (TextUtils.isEmpty(selectedAddress)) {
            selectedAddress = defaultText;
        } else {
            String newString = "Dejavoo Bluetooth (" + selectedAddress + ")";
            selectedAddress = newString;
        }
        mSwitchDejavoo.setText(selectedAddress);
        editTextIpAddress.setText(MyPreferences.getMyPreference(DEJAVOO_IP_ADDRESS, mContext));
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gApp.setVisibleFragment(this);

        mSwitchState.setChecked(BTDejavooService.isBtConnectionAvailable());
        setMacAddressOnTextView();
        mSwitchDejavoo.setChecked(MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext));
        mSwitchDejavoo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String address = MyPreferences.getMyPreference(REMOTE_DEVICE_ADDRESS, mContext);
                if (!TextUtils.isEmpty(address)) {
                    mSwitchDejavoo.setChecked(!mSwitchDejavoo.isChecked());
                    MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PAYMENT_VIA_BLUETOOTH, mSwitchDejavoo.isChecked(), mContext);
                    ServiceUtils.operateBTDejavooService(mContext, mSwitchDejavoo.isChecked());
                } else {
                    mSwitchDejavoo.setChecked(false);
                    ToastUtils.showOwnToast(mContext, "Use Long Tap To Select Bluetooth Device");
                    return;
                }
            }
        });

        mSwitchDejavoo.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (!BluetoothHelper.isBluetoothAvailable()) {
                    BlueToothNotSupported.onBlueTooth(mContext);
                    return true;
                }

                if (!BluetoothHelper.isBluetoothOpen())
                    BluetoothHelper.openBluetootSocket(DejavooFragment.this, mContext, MaintFragmentPrinterSetting.REQUEST_ENABLE_BLUETOOTH_DEVICE, 1);
                else
                    BluetoothHelper.findAndSelectAnyDevice(DejavooFragment.this, mContext, 1);

                return false;
            }
        });


        mSwitchPromptDC.setChecked(MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PRMOPT_D_C, mContext));
        mSwitchPromptDC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSwitchPromptDC.setChecked(!mSwitchPromptDC.isChecked());
                MyPreferences.setBooleanPrefrences(PrefrenceKeyConst.DEJAVO_PRMOPT_D_C, mSwitchPromptDC.isChecked(), mContext);
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_checked) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };
        arrayAdapter.add("Retail");
        arrayAdapter.add("Retail With Tip");
        arrayAdapter.add("Restaurant");
        arrayAdapter.add("Restaurant With Tip");
        listView.setAdapter(arrayAdapter);

        int position = (int) MyPreferences.getLongPreferenceWithDiffDefValue(DEJAVO_OPTION, mContext);

        if (position >= 0)
            listView.setItemChecked(position, true);
    }

    public int anyItemSelectedFromListView() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                selectedPosition = i;
                break;
            }
        }
        return selectedPosition;
    }
}

package com.posimplicity.fragment.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.RecieptPrints.PrintExtraReceipt;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.adapter.recycler.SwitchAdapter;
import com.posimplicity.BuildConfig;
import com.posimplicity.DeviceListActivity;
import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.posimplicity.interfaces.OnSwitchOnOff;
import com.posimplicity.model.local.Detail;
import com.posimplicity.model.local.Setting;
import com.posimplicity.service.BTService;
import com.posimplicity.service.printing.WifiService;
import com.service.BackgroundService;
import com.utils.AppSharedPrefs;
import com.utils.BluetoothHelper;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.POSApp;
import com.utils.PrinterHelper;

import java.util.ArrayList;
import java.util.List;

public class PrinterFragment extends BaseFragment implements OnSwitchOnOff, TextWatcher, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Setting mSetting;
    private Setting.AppSetting.Printer mPrinterSettings;

    private EditText mEditTxtText1;
    private EditText mEditTxtText2;
    private EditText mEditTxtText3;
    private EditText mEditTxtText4;


    // Extra Fields For Code Optimisation
    private EditText mEditTextWifiAddress;
    private int mItemIndex;
    private Detail mDetail;
    private SwitchAdapter mSwitchAdapter;

    //BroadcastManger to receive bluetooth and wifi state.
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
            }
            if (!Helper.isBlank(intent.getAction())) {
                switch (intent.getAction()) {
                    case Constants.ACTION_WIFI_CONNECTED:
                    case Constants.ACTION_BLUETOOTH_CONNECTED:
                        if (mDetail != null && (mItemIndex == mPrinterSettings.getBluetoothPrinting() || mItemIndex == mPrinterSettings.getWifiPrinting())) {
                            mDetail.setEnable(true);
                            Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, false);
                            mBaseActivity.dismissProgressDialog();
                        }
                        break;

                    case Constants.ACTION_WIFI_DISCONNECTED:
                    case Constants.ACTION_BLUETOOTH_DISCONNECTED:
                        if (mDetail != null && (mItemIndex == mPrinterSettings.getBluetoothPrinting() || mItemIndex == mPrinterSettings.getWifiPrinting())) {
                            mDetail.setEnable(false);
                            Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, false);
                            mBaseActivity.dismissProgressDialog();
                        }
                        break;
                }
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_WIFI_CONNECTED);
        intentFilter.addAction(Constants.ACTION_WIFI_DISCONNECTED);
        intentFilter.addAction(Constants.ACTION_BLUETOOTH_CONNECTED);
        intentFilter.addAction(Constants.ACTION_BLUETOOTH_DISCONNECTED);
        LocalBroadcastManager.getInstance(mBaseActivity).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_printer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fragment_printer_tv_drawer_settings).setOnClickListener(this);
        view.findViewById(R.id.fragment_printer_tv_test_print).setOnClickListener(this);

        mSetting = AppSharedPrefs.getInstance(mBaseActivity).getSetting();
        mPrinterSettings = mSetting.getAppSetting().getPrinter();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_printer_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new SwitchAdapter(mPrinterSettings.getDetail(), this, false));

        RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.fragment_printer_rv_customer_receipt_option);
        recyclerView1.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(new SwitchAdapter(mPrinterSettings.getCustomerReceiptArray(), new OnSwitchOnOff() {
            @Override
            public void onSwitchChange(int pItemIndex, List<Detail> pDataList, Detail pDetail, SwitchAdapter switchAdapter) {
                for (Detail detail : pDataList) {
                    if (!detail.getName().equalsIgnoreCase(pDetail.getName()))
                        detail.setEnable(false);
                }
                pDetail.setEnable(!pDetail.isEnable());
                Helper.updateSettingPreference(mBaseActivity, switchAdapter, mSetting, true);
            }
        }, false));

        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.fragment_printer_rv_kitchen_receipt_option);
        recyclerView2.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(new SwitchAdapter(mPrinterSettings.getKitchenReceiptArray(), new OnSwitchOnOff() {
            @Override
            public void onSwitchChange(int pItemIndex, List<Detail> pDataList, Detail pDetail, SwitchAdapter switchAdapter) {
                for (Detail detail : pDataList) {
                    if (!detail.getName().equalsIgnoreCase(pDetail.getName()))
                        detail.setEnable(false);
                }
                pDetail.setEnable(!pDetail.isEnable());
                Helper.updateSettingPreference(mBaseActivity, switchAdapter, mSetting, true);
            }
        }, false));

        // EditTexts
        mEditTxtText1 = ((EditText) view.findViewById(R.id.fragment_printer_edt_text1));
        mEditTxtText1.addTextChangedListener(this);
        mEditTxtText1.setText(mPrinterSettings.getText1());

        mEditTxtText2 = ((EditText) view.findViewById(R.id.fragment_printer_edt_text2));
        mEditTxtText2.addTextChangedListener(this);
        mEditTxtText2.setText(mPrinterSettings.getText2());

        mEditTxtText3 = ((EditText) view.findViewById(R.id.fragment_printer_edt_text3));
        mEditTxtText3.addTextChangedListener(this);
        mEditTxtText3.setText(mPrinterSettings.getText3());

        mEditTxtText4 = ((EditText) view.findViewById(R.id.fragment_printer_edt_text4));
        mEditTxtText4.addTextChangedListener(this);
        mEditTxtText4.setText(mPrinterSettings.getText4());

        // Spinner
        List<Integer> counter = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            counter.add(i);
        }

        ArrayAdapter dataAdapter = Helper.getIntArrayAdapterInstance(mBaseActivity, R.layout.spinner_layout, android.R.id.text1, counter);

        Spinner spinner1 = ((Spinner) view.findViewById(R.id.fragment_printer_sp_space1));
        spinner1.setOnItemSelectedListener(this);
        spinner1.setAdapter(dataAdapter);
        spinner1.setSelection(mPrinterSettings.getSpace1());

        Spinner spinner2 = ((Spinner) view.findViewById(R.id.fragment_printer_sp_space2));
        spinner2.setOnItemSelectedListener(this);
        spinner2.setAdapter(dataAdapter);
        spinner2.setSelection(mPrinterSettings.getSpace2());

        Spinner spinner3 = ((Spinner) view.findViewById(R.id.fragment_printer_sp_space3));
        spinner3.setOnItemSelectedListener(this);
        spinner3.setAdapter(dataAdapter);
        spinner3.setSelection(mPrinterSettings.getSpace3());

        Spinner spinner4 = ((Spinner) view.findViewById(R.id.fragment_printer_sp_space4));
        spinner4.setOnItemSelectedListener(this);
        spinner4.setAdapter(dataAdapter);
        spinner4.setSelection(mPrinterSettings.getSpace4());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case Constants.REQUEST_CODE_OPEN_BLUETOOTH: {
                    BluetoothHelper.findAndSelectDevice(this, mBaseActivity);
                    break;
                }

                case Constants.REQUEST_CODE_SELECTED_BLUETOOTH_DEVICE: {
                    String macAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    mSetting.getAppSetting().getPrinter().setBluetoothAddress(macAddress);
                    mDetail.setEnable(true);
                    Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, false);
                    mBaseActivity.showProgressDialog("Connecting With Printer...");
                    BTService.startService(mBaseActivity);
                    break;
                }

            }
        } else if (mDetail != null) {
            mDetail.setEnable(false);
            Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_printer_tv_drawer_settings: {
                showDrawerAlert();
                break;
            }
            case R.id.fragment_printer_tv_test_print: {
                BackgroundService.start(mBaseActivity, BackgroundService.ACTION_PRINT_SAMPLE);
                boolean anyModeConnected = false;

                // Checking USB-Printing is true/false ?
                if (PrinterHelper.isCustomerReceiptUsbOk()) {
                    new UsbPR(mBaseActivity, new PrinterCallBack() {
                        @Override
                        public void onConnected(BasePR pPrinter) {
                            PrintExtraReceipt.printSample(pPrinter);
                        }

                        @Override
                        public void onDisconnected() {
                            PrinterHelper.showConnectionNotAvailable(mBaseActivity);
                        }
                    }).onConnectionStart();
                    return;
                }

                // Checking BT-Printing is true/false ?
                if (PrinterHelper.isCustomerReceiptBTOk()) {
                    anyModeConnected = true;
                    PrintExtraReceipt.printSample(POSApp.getInstance().getPrinterBT());
                }

                // Checking WF-Printing is true/false ?
                if (PrinterHelper.isWifiPrinterConnected()) {
                    anyModeConnected = true;
                    PrintExtraReceipt.printSample(POSApp.getInstance().getPrinterWF());
                }

                if (!anyModeConnected) {
                    PrinterHelper.showConnectionNotAvailable(mBaseActivity);
                }

                break;
            }
        }
    }

    @Override
    public void onSwitchChange(int pItemIndex, List<Detail> mDataList, Detail pDetail, SwitchAdapter pSwitchAdapter) {

        this.mItemIndex = pItemIndex;
        this.mDetail = pDetail;
        this.mSwitchAdapter = pSwitchAdapter;

        if (pItemIndex == mPrinterSettings.getBluetoothPrinting()) {
            if (!mDetail.isEnable()) { // Means we want connection
                if (!BluetoothHelper.isBluetoothAvailable()) {
                    Toast.makeText(mBaseActivity, R.string.string_bluetooth_not_supported_by_device, Toast.LENGTH_SHORT).show();
                    Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, false);
                    return;
                }
                if (!BluetoothHelper.isBluetoothOpen()) {
                    BluetoothHelper.openBluetooth(this, mBaseActivity);
                } else {
                    BluetoothHelper.findAndSelectDevice(this, mBaseActivity);
                }
                return;
            } else {
                mDetail.setEnable(false);
                Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, true);
                BTService.stopService();
                BluetoothHelper.closeBluetoothSocketManually();
                return;
            }
        } else if (pItemIndex == mPrinterSettings.getWifiPrinting()) {
            if (!mDetail.isEnable()) { // Means we want connection
                showWifiAddressAlert(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Helper.hideSoftKeyboard(mBaseActivity, mEditTextWifiAddress);
                        if (DialogInterface.BUTTON_POSITIVE == which) {
                            String wifiAddress = "192.168.0.70";
                            if (!mEditTextWifiAddress.getText().toString().isEmpty()) {
                                wifiAddress = mEditTextWifiAddress.getText().toString();
                            }
                            mPrinterSettings.setWifiAddress(wifiAddress);
                            mDetail.setEnable(!mDetail.isEnable());
                            Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, false);
                            mBaseActivity.showProgressDialog("Connecting With Printer...");
                            WifiService.startService(mBaseActivity);
                            return;
                        } // When user tap on cancel button
                        Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, false);
                    }
                });
                return;
            } else {
                mDetail.setEnable(false);
                Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, true);
                WifiService.stopService();
                return;
            }
        } else {
            mDetail.setEnable(!mDetail.isEnable());
        }
        Helper.updateSettingPreference(mBaseActivity, mSwitchAdapter, mSetting, true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == mEditTxtText1.getEditableText()) {
            mPrinterSettings.setText1(s.toString());
        } else if (s == mEditTxtText2.getEditableText()) {
            mPrinterSettings.setText2(s.toString());
        } else if (s == mEditTxtText3.getEditableText()) {
            mPrinterSettings.setText3(s.toString());
        } else if (s == mEditTxtText4.getEditableText()) {
            mPrinterSettings.setText4(s.toString());
        }
        Helper.updateSettingPreference(mBaseActivity, null, mSetting, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.fragment_printer_sp_space1) {
            mPrinterSettings.setSpace1(position);
        } else if (parent.getId() == R.id.fragment_printer_sp_space2) {
            mPrinterSettings.setSpace2(position);
        } else if (parent.getId() == R.id.fragment_printer_sp_space3) {
            mPrinterSettings.setSpace3(position);
        } else if (parent.getId() == R.id.fragment_printer_sp_space4) {
            mPrinterSettings.setSpace4(position);
        }
        Helper.updateSettingPreference(mBaseActivity, null, mSetting, false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Call this method when user want to use wifi printer
     *
     * @param pListener
     */
    private void showWifiAddressAlert(DialogInterface.OnClickListener pListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.app_icon);
        builder.setMessage(String.format(getString(R.string.string_saved_ip_is_message), mPrinterSettings.getWifiAddress()));

        mEditTextWifiAddress = new EditText(mBaseActivity);
        mEditTextWifiAddress.setText(mPrinterSettings.getWifiAddress());
        mEditTextWifiAddress.setHint(R.string.string_enter_ip_address);
        mEditTextWifiAddress.setHintTextColor(Color.parseColor("#a9a9a9"));
        mEditTextWifiAddress.setTextColor(Color.BLACK);
        mEditTextWifiAddress.setGravity(Gravity.CENTER);
        mEditTextWifiAddress.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mEditTextWifiAddress.setSingleLine(true);

        builder.setView(mEditTextWifiAddress);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.string_continue), pListener);
        builder.setNegativeButton(R.string.string_cancel, pListener);
        builder.show();
    }

    /**
     * Call this method when user want to change drawer alert
     */
    private void showDrawerAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
        builder.setTitle(Html.fromHtml("<font color='#FFFFFF'>" + mBaseActivity.getString(R.string.string_enable_drawer_for_different_payment_mode) + "</font>"));
        builder.setIcon(R.drawable.app_icon);

        RecyclerView recyclerView = new RecyclerView(mBaseActivity);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 120));
        recyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setPadding(0, 0, 0, 20);
        recyclerView.setAdapter(new SwitchAdapter(mSetting.getAppSetting().getDrawer().getDetail(), new OnSwitchOnOff() {
            @Override
            public void onSwitchChange(int pItemIndex, List<Detail> mDataList, Detail detail, SwitchAdapter switchAdapter) {
                detail.setEnable(!detail.isEnable());
                switchAdapter.notifyDataSetChanged();
                Helper.updateSettingPreference(mBaseActivity, switchAdapter, mSetting, true);
            }
        }, false));

        builder.setView(recyclerView);
        AlertDialog al = builder.create();
        al.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        al.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(mBaseActivity).unregisterReceiver(mBroadcastReceiver);
    }
}

package com.posimplicity.fragment.setting.ccadmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;
import com.utils.Helper;

public class BridgepayFragment extends BaseFragment implements TextWatcher {

    public EditText mEditTxtName, mEditTxtPass;
    private Setting mSetting;
    private Setting.AppSetting.Gateway.BridgePay mBridgePaySetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bridgepay, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSetting = AppSharedPrefs.getInstance(mBaseActivity).getSetting();
        mBridgePaySetting = mSetting.getAppSetting().getGateway().getBridgePay();

        mEditTxtName = (EditText) view.findViewById(R.id.fragment_bridge_pay_edt_user_name);
        mEditTxtName.setText(mBridgePaySetting.getUserName());
        mEditTxtName.addTextChangedListener(this);

        mEditTxtPass = (EditText) view.findViewById(R.id.fragment_bridge_pay_edt_pass);
        mEditTxtPass.setText(mBridgePaySetting.getUserPassword());
        mEditTxtPass.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == mEditTxtName.getEditableText()) {
            mBridgePaySetting.setUserName(s.toString());
        } else if (s == mEditTxtPass.getEditableText()) {
            mBridgePaySetting.setUserPassword(s.toString());
        }
        Helper.updateSettingPreference(mBaseActivity, null, mSetting, false);
    }
}

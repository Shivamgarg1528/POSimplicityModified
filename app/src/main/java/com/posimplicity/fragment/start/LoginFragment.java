package com.posimplicity.fragment.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.PosInterfaces.PrefrenceKeyConst;
import com.Services.ParseService;
import com.posimplicity.model.local.Setting;
import com.posimplicity.fragment.base.BaseFragment;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.EasyFileUtils;
import com.utils.Helper;
import com.utils.MyPreferences;
import com.controller.ApisController;
import com.easylibs.http.EasyHttpResponse;
import com.easylibs.listener.EventListener;
import com.easylibs.utils.JsonUtils;
import com.posimplicity.model.local.RoleParent;
import com.posimplicity.model.response.other.Validate;
import com.posimplicity.R;

public class LoginFragment extends BaseFragment implements View.OnClickListener, TextWatcher, EventListener {

    private EditText mEdtStoreName;
    private TextView mTxtLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String storeName = MyPreferences.getMyPreference(PrefrenceKeyConst.STORE_NAME, mBaseActivity);
        if (!Helper.isBlank(storeName)) {
            // Means We did not setup app yet.. Need to launch LoginFragment
            mBaseActivity.replaceFragment(new SetupFragment());
            return;
        }
        view.findViewById(R.id.fragment_login_txt_register).setOnClickListener(this);

        mTxtLogin = (TextView) view.findViewById(R.id.fragment_login_txt_login);
        mTxtLogin.setOnClickListener(this);

        mEdtStoreName = (EditText) view.findViewById(R.id.fragment_login_edt_store_name);
        mEdtStoreName.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fragment_login_txt_login: {
                if (!Helper.isConnected(mBaseActivity)) {
                    mBaseActivity.showToast();
                } else {
                    String storeName = mEdtStoreName.getText().toString().trim();
                    mBaseActivity.showProgressDialog();
                    ApisController.storeValidate(mBaseActivity, this, storeName);
                }
                break;
            }

            case R.id.fragment_login_txt_register: {
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().trim().length() > 0) {
            mTxtLogin.setVisibility(View.VISIBLE);
        } else {
            mTxtLogin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
        if (pEventCode == Constants.API_VALIDATE_STORE) {
            Validate storeValidate = ((EasyHttpResponse<Validate>) pEventData).getData();
            if (storeValidate == null || Constants.SUCCESS != storeValidate.getSuccess()) {
                mBaseActivity.dismissProgressDialog();
                mBaseActivity.showToast("Invalid Store");
            } else {
                mBaseActivity.showToast("Valid Store");

                // Saving Store Name In Preference
                MyPreferences.setMyPreference(PrefrenceKeyConst.STORE_NAME, mEdtStoreName.getText().toString().trim(), mBaseActivity);

                // Adding data in local tables , reading from assets
                //1.Role Info
                //2.Saved Setting from asset to preference

                //1
                String roleJsonStr = EasyFileUtils.readAssetFile(mBaseActivity, Constants.ROLE_FILE);
                RoleParent roleParent = JsonUtils.objectify(roleJsonStr, RoleParent.class);
                if (roleParent != null && roleParent.getDetails() != null) {
                    ParseService.parseRoleInfo(mBaseActivity, roleParent.getDetails());
                }

                //2
                String settingJsonStr = EasyFileUtils.readAssetFile(mBaseActivity,Constants.SETTING_FILE);
                Setting setting = JsonUtils.objectify(settingJsonStr,Setting.class);
                AppSharedPrefs.getInstance(mBaseActivity).setSetting(setting);

                mBaseActivity.dismissProgressDialog();
                mBaseActivity.replaceFragment(new SetupFragment());
            }
        }
    }
}

package com.posimplicity.fragment.start;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.posimplicity.dialog.AlertHelper;
import com.posimplicity.fragment.base.BaseFragment;
import com.utils.Constants;
import com.utils.Helper;
import com.controller.ApisController;
import com.posimplicity.database.local.PosRoleTable;
import com.easylibs.http.EasyHttpResponse;
import com.easylibs.listener.EventListener;
import com.easylibs.utils.EasyUtils;
import com.posimplicity.model.local.RoleParent;
import com.posimplicity.model.response.other.Validate;
import com.posimplicity.R;
import com.posimplicity.activity.HomeActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperatorFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, EventListener {

    // views
    private ArrayList<RoleParent.RoleModel> mDataList;
    private EditText mEdtName;
    private EditText mEdtPwd;

    // data
    private RoleParent.RoleModel mRoleModel;

    public OperatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_operator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting app icon on toolbar
        mBaseActivity.setupToolBar("Login", false);

        mEdtName = (EditText) view.findViewById(R.id.fragment_operator_edt_name);
        mEdtPwd = (EditText) view.findViewById(R.id.fragment_operator_edt_pwd);

        view.findViewById(R.id.fragment_operator_txt_login).setOnClickListener(this);

        mDataList = new PosRoleTable(mBaseActivity).getAllData(PosRoleTable.IS_ROLE_ACTIVE + "=?", new String[]{"1"});
        Collections.sort(mDataList);

        List<String> mListRoleName = new ArrayList<>(mDataList.size());
        for (int index = 0; index < mDataList.size(); index++) {
            mListRoleName.add(mDataList.get(index).getRoleName());
        }

        ArrayAdapter arrayAdapter = Helper.getStringArrayAdapterInstance(mBaseActivity, R.layout.spinner_layout, android.R.id.text1, mListRoleName);

        Spinner spinner = (Spinner) view.findViewById(R.id.fragment_operator_spinner_role);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mRoleModel = mDataList.get(position);

        if (Constants.ROLE_ID_ADMIN.equalsIgnoreCase(mRoleModel.getRoleId())) {
            if (mEdtName.getVisibility() != View.VISIBLE) {
                mEdtName.setVisibility(View.VISIBLE);
                mEdtName.requestFocus();
            }
        } else {
            mEdtName.setVisibility(View.GONE);
            mEdtPwd.requestFocus();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (!Helper.isConnected(mBaseActivity)) {
            mBaseActivity.showToast(getString(R.string.string_please_check_internet_connection));
            return;
        }

        if (mEdtName.getText().toString().length() <= 0
                || mEdtPwd.getText().toString().length() <= 0
                || mRoleModel == null) {
            mBaseActivity.showToast(getString(R.string.string_please_provide_all_info));
            return;
        }

        // Internet Available
        if (Constants.ROLE_ID_ADMIN.equalsIgnoreCase(mRoleModel.getRoleId())) { // Call Admin validate api
            String pUrl = "";
            pUrl += "&name=" + mEdtName.getText().toString().trim();
            pUrl += "&password=" + mEdtPwd.getText().toString().trim();
            pUrl += "&role=Administrators";
            mBaseActivity.showProgressDialog();
            ApisController.adminValidate(mBaseActivity, this, pUrl);
        } else { // validate role locally...
            if (EasyUtils.isBlank(mRoleModel.getRolePassword())) {
                mBaseActivity.showToast(getString(R.string.string_contact_app_admin_for_better_support));
                return;
            }
            String mPasswordStr = mEdtPwd.getText().toString().trim();
            if (mPasswordStr.equalsIgnoreCase(mRoleModel.getRolePassword()))
                openHomeActivity();
            else {
                AlertHelper.getAlertDialog(mBaseActivity, getString(R.string.string_account_may_be_suspended), getString(R.string.string_ok), null, null);
            }
        }
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
        mBaseActivity.dismissProgressDialog();
        if (pEventCode == Constants.API_VALIDATE_ADMIN) {
            Validate adminValidate = ((EasyHttpResponse<Validate>) pEventData).getData();
            if (adminValidate == null || Constants.SUCCESS != adminValidate.getSuccess()) {
                AlertHelper.getAlertDialog(mBaseActivity, getString(R.string.string_account_may_be_suspended), getString(R.string.string_ok), null, null);
            } else
                openHomeActivity();
        }
    }

    private void openHomeActivity() {
        mBaseActivity.showToast(getString(R.string.string_login_success));
        mBaseActivity.finish();
        startActivity(new Intent(mBaseActivity, HomeActivity.class));
    }
}

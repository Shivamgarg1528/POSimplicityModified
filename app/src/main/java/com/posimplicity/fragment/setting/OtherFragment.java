package com.posimplicity.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.adapter.recycler.SwitchAdapter;
import com.posimplicity.database.server.DbHelper;
import com.posimplicity.fragment.base.SwitchBaseFragment;
import com.posimplicity.interfaces.OnSwitchOnOff;
import com.posimplicity.model.local.Detail;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;
import com.utils.Helper;

import java.util.List;

public class OtherFragment extends SwitchBaseFragment implements OnSwitchOnOff {

    private Setting.AppSetting.OtherSetting mOtherSetting;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOtherSetting = mSetting.getAppSetting().getOtherSetting();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(new SwitchAdapter(mOtherSetting.getDetail(), this, false));
    }

    @Override
    public void onSwitchChange(final int pItemIndex, final List<Detail> pDataList, final Detail pDetail, final SwitchAdapter pSwitchAdapter) {
        if (pItemIndex == mOtherSetting.getResetCurrentStore()) {
            mBaseActivity.deleteDatabase(DbHelper.getInstance().getDbName());
            AppSharedPrefs.getInstance(mBaseActivity).clear();
            Helper.startAppAgain(mBaseActivity);
        } else if (pItemIndex == mOtherSetting.getSyncApplication()) {
            mBaseActivity.deleteDatabase(DbHelper.getInstance().getDbName());
            AppSharedPrefs.getInstance(mBaseActivity).clear();
            Helper.startAppAgain(mBaseActivity);
        } else if (pItemIndex == mOtherSetting.getTimeClock()) {

        } else {
            pDetail.setEnable(!pDetail.isEnable());
        }
        Helper.updateSettingPreference(mBaseActivity, pSwitchAdapter, mSetting, true);
    }
}

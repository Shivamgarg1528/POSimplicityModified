package com.posimplicity.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.adapter.recycler.SwitchAdapter;
import com.posimplicity.fragment.base.SwitchBaseFragment;
import com.posimplicity.interfaces.OnSwitchOnOff;
import com.posimplicity.model.local.Detail;
import com.posimplicity.model.local.Setting;
import com.utils.Helper;

import java.util.List;

public class AppLocationFragment extends SwitchBaseFragment implements OnSwitchOnOff {

    private Setting.AppSetting.AppLocation mAppLocationSetting;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppLocationSetting = mSetting.getAppSetting().getAppLocation();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(new SwitchAdapter(mAppLocationSetting.getDetail(), this, false));
    }

    @Override
    public void onSwitchChange(int pItemIndex, List<Detail> pDataList, Detail pDetail, SwitchAdapter pSwitchAdapter) {
        if (mAppLocationSetting.isSingleChoiceMode()) {
            for (Detail detail : pDataList) {
                if (!detail.getName().equalsIgnoreCase(pDetail.getName()))
                    detail.setEnable(false);
            }
        }
        pDetail.setEnable(!pDetail.isEnable());
        Helper.updateSettingPreference(mBaseActivity, pSwitchAdapter, mSetting, true);
    }
}

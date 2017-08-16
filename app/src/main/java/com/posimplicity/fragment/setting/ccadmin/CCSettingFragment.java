package com.posimplicity.fragment.setting.ccadmin;

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

public class CCSettingFragment extends SwitchBaseFragment implements OnSwitchOnOff {

    private Setting.AppSetting.Gateway mGatewaySettings;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGatewaySettings = mSetting.getAppSetting().getGateway();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(new SwitchAdapter(mGatewaySettings.getDetail(), this, true));
    }

    @Override
    public void onSwitchChange(final int pItemIndex, final List<Detail> pDataList, final Detail pDetail, final SwitchAdapter pSwitchAdapter) {
        if (mGatewaySettings.isSingleChoiceMode()) {
            for (Detail detail : pDataList) {
                if (!detail.getName().equalsIgnoreCase(pDetail.getName()))
                    detail.setEnable(false);
            }
        }
        pDetail.setEnable(!pDetail.isEnable());
        Helper.updateSettingPreference(mBaseActivity, pSwitchAdapter, mSetting, true);
    }
}

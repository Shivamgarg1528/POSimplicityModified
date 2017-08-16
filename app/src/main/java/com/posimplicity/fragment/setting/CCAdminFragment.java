package com.posimplicity.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easylibs.listener.EventListener;
import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.posimplicity.fragment.setting.ccadmin.BridgepayFragment;
import com.posimplicity.fragment.setting.ccadmin.CCSettingFragment;
import com.posimplicity.fragment.setting.ccadmin.PropayFragment;
import com.posimplicity.gateway.BaseGateway;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class CCAdminFragment extends BaseFragment implements EventListener {

    protected Setting.AppSetting.Gateway mGatewaySettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ccadmin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> listTabName = new ArrayList<>();

        mGatewaySettings = AppSharedPrefs.getInstance(mBaseActivity).getSetting().getAppSetting().getGateway();
        for (int index = 0; index < mGatewaySettings.getDetail().size(); index++) {
            listTabName.add(mGatewaySettings.getDetail().get(index).getName());
        }

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.fragment_ccadmin_viewpager);
        mViewPager.setOffscreenPageLimit(listTabName.size() - 1);
        mViewPager.setAdapter(new CCAdminFragment.SimpleAdapter(getChildFragmentManager(), listTabName));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.fragment_ccadmin_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onEvent(int pEventCode, Object pEventData) {
    }

    private class SimpleAdapter extends FragmentStatePagerAdapter {

        private final List<String> mListTabName;

        SimpleAdapter(FragmentManager pFragmentManager, List<String> pListTabName) {
            super(pFragmentManager);
            this.mListTabName = pListTabName;
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment baseFragment = null;
            switch (position) {
                case BaseGateway.GATEWAY_TSYS: {
                    baseFragment = new PropayFragment();
                    break;
                }
                case BaseGateway.GATEWAY_PLUG_PAY: {
                    baseFragment = new BridgepayFragment();
                    break;
                }
                case BaseGateway.GATEWAY_BRIDGE_PAY: {
                    baseFragment = new PropayFragment();
                    break;
                }
                case BaseGateway.GATEWAY_PROPAY: {
                    baseFragment = new PropayFragment();
                    break;
                }
                case BaseGateway.GATEWAY_DEJAVOO: {
                    baseFragment = new PropayFragment();
                    break;
                }
                case BaseGateway.GATEWAY_SETTING: {
                    baseFragment = new CCSettingFragment();
                    break;
                }
            }
            return baseFragment;
        }

        @Override
        public int getCount() {
            return mListTabName.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTabName.get(position);
        }
    }
}

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

import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.posimplicity.fragment.function.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

public class SupportFragment extends BaseFragment {

    private static final short CONSTANT_SUPPORT = 0;
    private static final short CONSTANT_USER_GUIDE = 1;
    private static final short CONSTANT_KNOWLEDGE_BASE = 2;
    private static final short CONSTANT_CC_PROCESS = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> listTabName = new ArrayList<>();
        listTabName.add(getString(R.string.string_support));
        listTabName.add(getString(R.string.string_user_guide));
        listTabName.add(getString(R.string.string_knowledge_base));
        listTabName.add(getString(R.string.string_get_cc_process));

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.fragment_support_viewpager);
        mViewPager.setOffscreenPageLimit(listTabName.size() - 1);
        mViewPager.setAdapter(new SimpleAdapter(getChildFragmentManager(), listTabName));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.fragment_support_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private static class SimpleAdapter extends FragmentStatePagerAdapter {

        private final List<String> mListTabName;

        SimpleAdapter(FragmentManager pFragmentManager, List<String> pListTabName) {
            super(pFragmentManager);
            this.mListTabName = pListTabName;
        }

        @Override
        public Fragment getItem(int position) {
            String webUrl = "";
            switch ((short) position) {
                case CONSTANT_SUPPORT: {
                    webUrl = "http://support.posimplicity.com";
                    break;
                }
                case CONSTANT_USER_GUIDE: {
                    webUrl = "http://posimplicity.net/userguide/userguide.htm";
                    break;
                }
                case CONSTANT_KNOWLEDGE_BASE: {
                    webUrl = "http://support.posimplicity.com";
                    break;
                }
                case CONSTANT_CC_PROCESS: {
                    webUrl = "http://posimplicity.net/mif";
                    break;
                }
            }
            return WebViewFragment.newInstance(webUrl, false);
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

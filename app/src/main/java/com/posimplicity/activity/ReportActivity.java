package com.posimplicity.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.posimplicity.R;
import com.posimplicity.fragment.base.PaymentBaseFragment;
import com.posimplicity.fragment.base.ReportBaseFragment;
import com.posimplicity.fragment.report.ReportFragment;
import com.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setupToolBar(getString(R.string.string_reports), false);

        List<String> listTabName = new ArrayList<>();
        listTabName.add(getString(R.string.string_daily_report));
        listTabName.add(getString(R.string.string_shift_report));

        List<ReportBaseFragment> listOfFragments = new ArrayList<>();
        listOfFragments.add(ReportFragment.newInstance(Constants.DAILY_REPORT));
        listOfFragments.add(ReportFragment.newInstance(Constants.SHIFT_REPORT));

        mViewPager = (ViewPager) findViewById(R.id.activity_report_viewpager);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(listOfFragments.size() - 1);
        mViewPager.setAdapter(new SimpleAdapter(this, listTabName, listOfFragments));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_report_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_payment_iv_overflow: {
                PaymentBaseFragment visibleFragment = (PaymentBaseFragment) ((FragmentStatePagerAdapter) mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem());
                visibleFragment.showOverFlowDialog(v);
                break;
            }
        }
    }

    @Override
    protected void onBackTapped() {
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        ReportBaseFragment visibleFragment = (ReportBaseFragment) ((FragmentStatePagerAdapter) mViewPager.getAdapter()).getItem(position);
        visibleFragment.onPageChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private static class SimpleAdapter extends FragmentStatePagerAdapter {

        private final List<String> mListTabName;
        private final List<ReportBaseFragment> mListOfPaymentFragments;
        private boolean isFirstFinish = true;

        SimpleAdapter(BaseActivity activity, List<String> pListTabName, List<ReportBaseFragment> pListOfFragments) {
            super(activity.getSupportFragmentManager());
            this.mListTabName = pListTabName;
            this.mListOfPaymentFragments = pListOfFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mListOfPaymentFragments.get(position);
        }

        @Override
        public int getCount() {
            return mListOfPaymentFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTabName.get(position);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            if (isFirstFinish) {
                isFirstFinish = false;
                if (mListOfPaymentFragments.size() > 0) {
                    mListOfPaymentFragments.get(0).onPageChanged(0);
                }
            }
        }
    }
}

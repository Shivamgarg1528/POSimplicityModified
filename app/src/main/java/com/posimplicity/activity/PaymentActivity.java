package com.posimplicity.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.posimplicity.fragment.payment.CreditFragment;
import com.utils.Helper;
import com.posimplicity.R;
import com.posimplicity.fragment.payment.CashFragment;
import com.posimplicity.fragment.payment.CheckFragment;
import com.posimplicity.fragment.base.PaymentBaseFragment;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;
    private ImageView mImageViewOverFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = Helper.dpToPx(this, 600); //  fixed width   61%
        params.height = Helper.dpToPx(this, 470); // fixed height  85%
        params.gravity = Gravity.CENTER_VERTICAL;
        params.x = Helper.dpToPx(this, 120);
        params.y = Helper.dpToPx(this, 8);
        getWindow().setAttributes(params);

        setContentView(R.layout.activity_payment);

        List<String> listTabName = new ArrayList<>();
        listTabName.add("Credit");
        listTabName.add("Cash");
        listTabName.add("Check");

        List<PaymentBaseFragment> listOfPaymentFragments = new ArrayList<>();
        listOfPaymentFragments.add(new CreditFragment());
        listOfPaymentFragments.add(new CashFragment());
        listOfPaymentFragments.add(new CheckFragment());

        mImageViewOverFlow = (ImageView) findViewById(R.id.activity_payment_iv_overflow);
        mImageViewOverFlow.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.activity_payment_viewpager);
        mViewPager.addOnPageChangeListener(this);
        /*mViewPager.setOffscreenPageLimit(listOfPaymentFragments.size() - 1);*/
        mViewPager.setAdapter(new PaymentAdapter(this, listTabName, listOfPaymentFragments));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_payment_tab_layout);
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

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        PaymentBaseFragment visibleFragment = (PaymentBaseFragment) ((FragmentStatePagerAdapter) mViewPager.getAdapter()).getItem(position);
        if (visibleFragment instanceof CashFragment
                || visibleFragment instanceof CheckFragment
                || visibleFragment instanceof CreditFragment) {
            if (mImageViewOverFlow.getVisibility() != View.VISIBLE)
                mImageViewOverFlow.setVisibility(View.VISIBLE);
        } else {
            if (mImageViewOverFlow.getVisibility() != View.INVISIBLE)
                mImageViewOverFlow.setVisibility(View.INVISIBLE);
        }
        visibleFragment.onPageChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private static class PaymentAdapter extends FragmentStatePagerAdapter {

        private final List<String> mListTabName;
        private final List<PaymentBaseFragment> mListOfPaymentFragments;
        private boolean isFirstFinish = true;

        PaymentAdapter(BaseActivity activity, List<String> pListTabName, List<PaymentBaseFragment> pListOfPaymentFragments) {
            super(activity.getSupportFragmentManager());
            this.mListTabName = pListTabName;
            this.mListOfPaymentFragments = pListOfPaymentFragments;
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

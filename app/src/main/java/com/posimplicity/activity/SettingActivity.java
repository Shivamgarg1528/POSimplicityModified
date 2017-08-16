package com.posimplicity.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.posimplicity.fragment.function.PayoutFragment;
import com.posimplicity.fragment.function.WebViewFragment;
import com.posimplicity.fragment.setting.AboutFragment;
import com.posimplicity.fragment.setting.AppLocationFragment;
import com.posimplicity.fragment.setting.CCAdminFragment;
import com.posimplicity.fragment.setting.OtherFragment;
import com.posimplicity.fragment.setting.PrinterFragment;
import com.posimplicity.fragment.setting.RewardFragment;
import com.posimplicity.fragment.setting.SupportFragment;
import com.posimplicity.service.BTService;
import com.posimplicity.service.printing.WifiService;
import com.utils.Constants;
import com.utils.Helper;

public class SettingActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private int mSelectedItemId = R.id.activity_setting_drawer_item_cc_admin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (savedInstanceState != null) {
            mSelectedItemId = savedInstanceState.getInt(Constants.EXTRA_KEY);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_setting_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_setting_navigation);
        navigationView.setNavigationItemSelectedListener(this);
        MenuItem selectedItem = navigationView.getMenu().findItem(mSelectedItemId);
        selectedItem.setChecked(true);
        setupToolBar(selectedItem.getTitle().toString(), true);
        onNavigationItemSelect(selectedItem.getItemId());

        WifiService.startService(this);
        BTService.startService(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.EXTRA_KEY, mSelectedItemId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        item.setChecked(true);
        setToolTitle(item.getTitle().toString());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                onNavigationItemSelect(item.getItemId());
            }
        }, 500);
        return true;
    }

    @Override
    protected void onBackTapped() {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    /**
     * When Drawer item selected from list
     *
     * @param pItemId drawer item id
     */
    private void onNavigationItemSelect(int pItemId) {
        mSelectedItemId = pItemId;
        BaseFragment mFragment = null;
        String urlWebPage = Helper.getWebBaseUrl(this);

        switch (pItemId) {
            case R.id.activity_setting_drawer_item_printer: {
                mFragment = new PrinterFragment();
                break;
            }
            case R.id.activity_setting_drawer_item_cc_admin: {
                mFragment = new CCAdminFragment();
                break;
            }
            case R.id.activity_setting_drawer_item_support: {
                mFragment = new SupportFragment();
                break;
            }
            case R.id.activity_setting_drawer_item_rewards: {
                mFragment = new RewardFragment();
                break;
            }
            case R.id.activity_setting_drawer_item_security: {
                break;
            }
            case R.id.activity_setting_drawer_item_others: {
                mFragment = new OtherFragment();
                break;
            }
            case R.id.activity_setting_drawer_item_portal: {
                urlWebPage += "admin";
                mFragment = WebViewFragment.newInstance(urlWebPage, true);
                break;
            }
            case R.id.activity_setting_drawer_item_app_location: {
                mFragment = new AppLocationFragment();
                break;
            }
            case R.id.activity_setting_drawer_item_about: {
                mFragment = new AboutFragment();
                break;
            }
            case R.id.activity_setting_drawer_item_exit: {
                finish();
                break;
            }
        }
        if (mFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_setting_container, mFragment).commit();
        }
    }
}

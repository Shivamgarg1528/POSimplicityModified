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
import com.utils.Constants;
import com.utils.Helper;

public class FunctionActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private int mSelectedItemId = R.id.activity_function_drawer_item_add_customer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_function_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_function_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        String firstTitle = navigationView.getMenu().findItem(mSelectedItemId).getTitle().toString();
        setupToolBar(firstTitle, true);

        if (savedInstanceState != null) {
            mSelectedItemId = savedInstanceState.getInt(Constants.EXTRA_KEY);
        }
        onNavigationItemSelect(mSelectedItemId);
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

        BaseFragment mFragment = null;
        String urlWebPage = Helper.getWebBaseUrl(this);

        switch (pItemId) {
            case R.id.activity_function_drawer_item_add_customer: {
                urlWebPage += "functions/addcustomer/addcustomer.htm";
                mFragment = WebViewFragment.newInstance(urlWebPage, true);
                break;
            }
            case R.id.activity_function_drawer_item_add_table: {
                urlWebPage += "functions/addtable/addtable.htm";
                mFragment = WebViewFragment.newInstance(urlWebPage, true);
                break;
            }
            case R.id.activity_function_drawer_item_add_clerk: {
                urlWebPage += "functions/addclerk/clerktable.htm";
                mFragment = WebViewFragment.newInstance(urlWebPage, true);
                break;
            }
            case R.id.activity_function_drawer_item_time_clock: {
                break;
            }
            case R.id.activity_function_drawer_item_drawer: {
                break;
            }
            case R.id.activity_function_drawer_item_payouts: {
                mFragment = new PayoutFragment();
                break;
            }
            case R.id.activity_function_drawer_item_reward: {
                urlWebPage += "functions/rewards/tendercard_rewards/trewardsissue.htm";
                mFragment = WebViewFragment.newInstance(urlWebPage, true);
                break;
            }
            case R.id.activity_function_drawer_item_gift: {
                urlWebPage += "functions/rewards/tendercard_rewards/tgiftissue.htm";
                mFragment = WebViewFragment.newInstance(urlWebPage, true);
                break;
            }
            case R.id.activity_function_drawer_item_dejavoo_tip_adjustment: {
                break;
            }
            case R.id.activity_function_drawer_item_dejavoo_return_adjustment: {
                break;
            }
            case R.id.activity_function_drawer_item_tsys_tip_adjustment: {
                break;
            }
            case R.id.activity_function_drawer_item_tsys_return_adjustment: {
                break;
            }
            case R.id.activity_function_drawer_item_exit: {
                finish();
                break;
            }
        }
        if (mFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_function_container, mFragment).commit();
        }
    }
}

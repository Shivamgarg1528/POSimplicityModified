package com.posimplicity.activity;


import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.posimplicity.fragment.base.BaseFragment;
import com.utils.POSApp;
import com.utils.Helper;
import com.posimplicity.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected POSApp mGApp = POSApp.getInstance();
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;

    protected abstract void onBackTapped();

    /**
     * @param pTitle
     */
    protected void setToolTitle(String pTitle) {
        if (!Helper.isBlank(pTitle) && mToolbar != null) {
            setTitle(pTitle);
        }
    }

    /**
     * @param pTitle
     * @param pToolbarDrawer
     */
    public void setupToolBar(String pTitle, boolean pToolbarDrawer) {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (pToolbarDrawer) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_icon);
            }
            setToolTitle(pTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void showProgressDialog(String... pMessage) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(pMessage.length <= 0 ? "Please Wait..." : pMessage[0]);
        mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mProgressDialog.show();
    }

    public final void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void replaceFragment(BaseFragment pBaseFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_start_container, pBaseFragment, BaseFragment.TAG).commit();
    }

    public void showToast() {
        Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show();
    }

    public void showToast(String pToastMsg) {
        Toast.makeText(this, pToastMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackTapped();
        }
        return true;
    }
}

package com.posimplicity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.easylibs.utils.JsonUtils;
import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;
import com.posimplicity.fragment.start.SplashFragment;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;
import com.utils.Constants;
import com.utils.EasyFileUtils;

public class StartingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /*String settingJsonStr = EasyFileUtils.readAssetFile(this, Constants.SETTING_FILE);
        Setting setting = JsonUtils.objectify(settingJsonStr,Setting.class);
        AppSharedPrefs.getInstance(this).setSetting(setting);
        */AppSharedPrefs.getInstance(this);
        startActivity(new Intent(this, HomeActivity.class));
        //replaceFragment(new SplashFragment());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(BaseFragment.TAG);
        if (fragment != null) {
            if (fragment instanceof SplashFragment) {
                SplashFragment splashFragment = (SplashFragment) fragment;
                splashFragment.getHandler().removeCallbacks(splashFragment);
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onBackTapped() {
    }
}

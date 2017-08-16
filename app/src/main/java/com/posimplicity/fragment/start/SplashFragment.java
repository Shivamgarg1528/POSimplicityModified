package com.posimplicity.fragment.start;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.posimplicity.R;
import com.posimplicity.fragment.base.BaseFragment;

public class SplashFragment extends BaseFragment implements Runnable {

    private Handler mHandler;

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHandler();
        mHandler.postDelayed(this, 1500);
    }

    @Override
    public void run() {
        mBaseActivity.replaceFragment(new LoginFragment());
    }
}

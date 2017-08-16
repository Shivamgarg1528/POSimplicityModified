package com.posimplicity.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adapter.recycler.SwitchAdapter;
import com.posimplicity.R;
import com.posimplicity.model.local.Setting;
import com.utils.AppSharedPrefs;

public class SwitchBaseFragment extends BaseFragment {

    protected Setting mSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView mRecyclerView = new RecyclerView(mBaseActivity);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setBackgroundResource(R.color.colorPrimary);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity));
        return mRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSetting = AppSharedPrefs.getInstance(mBaseActivity).getSetting();
    }
}

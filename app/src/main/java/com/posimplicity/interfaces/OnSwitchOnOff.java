package com.posimplicity.interfaces;

import com.adapter.recycler.SwitchAdapter;
import com.posimplicity.model.local.Detail;

import java.util.List;

public interface OnSwitchOnOff {
    void onSwitchChange(int pItemIndex, List<Detail> mDataList, Detail detail, SwitchAdapter switchAdapter);
}

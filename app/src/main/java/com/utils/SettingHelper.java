package com.utils;

import com.posimplicity.model.local.Detail;

import java.util.List;

class SettingHelper {

    private static Detail getSettingDetail(List<Detail> pList, int pIndex) {
        return pList.get(pIndex);
    }

    static boolean isSettingEnable(List<Detail> pList, int pIndex) {
        return getSettingDetail(pList, pIndex).isEnable();
    }
}

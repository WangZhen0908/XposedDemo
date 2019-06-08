package com.moon.xposed.hook.location;

import android.telephony.NeighboringCellInfo;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

public class TelephonyManager_getNeighboringCellInfo extends XC_MethodHook {

    private static final String TAG = "TelephonyManager_getNeighboringCellInfo";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        // 暂时注释掉 start
        List<NeighboringCellInfo> cellInfos = new ArrayList<NeighboringCellInfo>();
        cellInfos.add(new NeighboringCellInfo());
        param.setResult(new ArrayList<NeighboringCellInfo>());
    }


}
package com.moon.xposed.hook.location;

import android.location.LocationManager;

import de.robv.android.xposed.XC_MethodHook;

public class LocationManager_getBestProvider extends XC_MethodHook {

    private static final String TAG = "LocationManager_getBestProvider";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        param.setResult(LocationManager.GPS_PROVIDER);
    }

}
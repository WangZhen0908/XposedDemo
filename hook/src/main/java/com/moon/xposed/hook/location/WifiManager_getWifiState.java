package com.moon.xposed.hook.location;

import android.net.wifi.WifiManager;

import de.robv.android.xposed.XC_MethodHook;

public class WifiManager_getWifiState extends XC_MethodHook {

    private static final String TAG = "WifiManager_getWifiState";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
//        param.setResult(WifiManager.WIFI_STATE_ENABLED);
        param.setResult(WifiManager.WIFI_STATE_DISABLED);
    }
}
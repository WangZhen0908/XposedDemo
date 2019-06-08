package com.moon.xposed.hook.location;

import android.telephony.gsm.GsmCellLocation;

import com.moon.xposed.hook.HookLocation;

import de.robv.android.xposed.XC_MethodHook;

/**
 * {@link android.telephony.TelephonyManager#getCellLocation()}
 *
 * @author Administrator
 */
public class TelephonyManager_getCellLocation extends XC_MethodHook {

    private static final String TAG = "TelephonyManager_getCellLocation";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        try {
            GsmCellLocation localGsmCellLocation = new GsmCellLocation();
            localGsmCellLocation.setLacAndCid(HookLocation.lac, HookLocation.cid);
            param.setResult(localGsmCellLocation);
        } catch (Exception e) {
        }
    }

}
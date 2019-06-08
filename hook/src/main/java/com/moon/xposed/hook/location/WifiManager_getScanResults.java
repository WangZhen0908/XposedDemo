package com.moon.xposed.hook.location;

import de.robv.android.xposed.XC_MethodHook;

public class WifiManager_getScanResults extends XC_MethodHook {

	private static final String TAG = "WifiManager_getScanResults";

	@Override
	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		super.beforeHookedMethod(param);
	}
	
	@Override
	protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		super.afterHookedMethod(param);
		param.setResult(null);
	}
	
}
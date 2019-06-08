package com.moon.xposed.hook.location;

import android.location.LocationManager;

import de.robv.android.xposed.XC_MethodHook;

public class LocationManager_isProviderEnabled extends XC_MethodHook {

	private static final String TAG = "LocationManager_isProviderEnabled";
	
	@Override
	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		super.beforeHookedMethod(param);
	}
	
	@Override
	protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		super.afterHookedMethod(param);
		if(LocationManager.NETWORK_PROVIDER.equals(param.args[0]) || LocationManager.GPS_PROVIDER.equals(param.args[0]) || LocationManager.PASSIVE_PROVIDER.equals(param.args[0])){
			param.setResult(true);
		}else{
			param.setResult(false);
		}
	}

}

package com.moon.xposed.hook.location;

import android.location.LocationManager;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;

public class LocationManager_getProviders extends XC_MethodHook {

	private static final String TAG = "LocationManager_getProviders";
	
	@Override
	protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		super.beforeHookedMethod(param);
	}
	
	@Override
	protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		super.afterHookedMethod(param);
		ArrayList<String> providers = new ArrayList<String>();
		providers.add(LocationManager.GPS_PROVIDER);
		providers.add(LocationManager.NETWORK_PROVIDER);
		param.setResult(providers);
	}

	
}
package com.moon.xposed.hook;

import android.content.pm.ApplicationInfo;

import com.moon.xposed.hookbase.HookUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (lpparam.appInfo == null) {
            return;
        }

        if ((lpparam.appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            //不hook系统APP
            return;
        }

        HookUtils.init(lpparam.classLoader);
        HookUtils.hook(new ToastHook());

    }
}
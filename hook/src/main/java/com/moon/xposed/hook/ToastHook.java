package com.moon.xposed.hook;

import android.content.Context;
import android.widget.Toast;

import com.moon.xposed.hookbase.BaseMethodHook;
import com.moon.xposed.hookbase.HookMethod;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;


public class ToastHook extends BaseMethodHook {

    @Override
    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        String text = (String) param.args[1];
        text = "被Hook " + text;
        param.args[1] = text;
    }

    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
    }

    @Override
    public List<HookMethod> getHookMethods() {
        List<HookMethod> methods = new ArrayList<>();
        methods.add(new HookMethod(Toast.class.getName(), "makeText", Context.class, CharSequence.class, int.class));
        return methods;
    }
}
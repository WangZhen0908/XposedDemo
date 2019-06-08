package com.moon.xposed.hookbase;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

/**
 * author WangZhen
 * created 2019/3/2 14:09
 */
public abstract class BaseMethodHook extends XC_MethodHook {

    /**
     * 获取需要进行hook的方法列表
     *
     * @return 需要进行hook的方法列表
     */
    public abstract List<HookMethod> getHookMethods();

}

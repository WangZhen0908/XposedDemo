package com.moon.xposed.hookbase;

import java.util.List;

import de.robv.android.xposed.XposedHelpers;

/**
 * author WangZhen
 * created 2019/3/2 14:08
 */
public class HookUtils {

    private static ClassLoader sClassLoader;

    public static void init(ClassLoader classLoader) {
        sClassLoader = classLoader;
    }

    public static void hook(BaseMethodHook hook) {
        try {
            //如果找不到类或方法会报异常
            List<HookMethod> methods = hook.getHookMethods();
            if (methods != null && !methods.isEmpty()) {
                for (HookMethod method : methods) {
                    hook(sClassLoader, hook, method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hook(ClassLoader classLoader, BaseMethodHook hook, HookMethod method) {
        int paramsSize = 0;
        if (method.getParamsTypes() != null && method.getParamsTypes().length > 0) {
            paramsSize = method.getParamsTypes().length;
        }
        Object[] parameterTypesAndCallback = new Object[paramsSize + 1];
        if (paramsSize > 0) {
            System.arraycopy(method.getParamsTypes(), 0, parameterTypesAndCallback, 0, paramsSize);
        }
        parameterTypesAndCallback[paramsSize] = hook;
        try {
            XposedHelpers.findAndHookMethod(method.getClassName(), classLoader, method.getMethodName(), parameterTypesAndCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

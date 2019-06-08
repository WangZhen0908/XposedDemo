package com.moon.xposed.hook.location;

import android.location.GpsStatus;


import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;

public class LocationManager_getGpsStatus extends XC_MethodHook {

    private static final String TAG = "LocationManager_getGpsStatus";

    @Override
    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }


    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        // 暂时注释 start
        GpsStatus localGpsStatus = (GpsStatus) param.getResult();
        if (localGpsStatus == null) {
            return;
        }

        Method[] methods = GpsStatus.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().equals("setStatus")) {
                method.setAccessible(true);
                if (method.getParameterTypes().length > 1) {

                    int svCount = 17;
                    int[] prns = new int[]{1, 7, 8, 9, 11, 16, 23, 26, 27,
                            30, 77, 78, 79, 80, 82, 83, 88};
                    ;
                    float[] snrs = new float[]{19.0F, 12.0F, 13.0F, 15.0F,
                            12.0F, 21.0F, 11.0F, 10.0F, 24.0F, 9.0F, 8.0F,
                            17.0F, 14.0F, 12.0F, 12.0F, 10.0F, 10.0F};

                    float[] elevations = new float[]{23.0F, 41.0F, 64.0F,
                            34.0F, 56.0F, 21.0F, 29.0F, 3.0F, 36.0F, 12.0F,
                            6.0F, 49.0F, 52.0F, 5.0F, 54.0F, 1.0F, 14.0F};

                    float[] azimuths = new float[]{138.0F, 325.0F, 0.0F,
                            252.0F, 185.0F, 68.0F, 209.0F, 88.0F, 31.0F,
                            321.0F, 128.0F, 91.0F, 10.0F, 326.0F, 220.0F,
                            211.0F, 27.0F};

                    int ephemerisMask = 31;
                    int almanacMask = 31;
                    int usedInFixMask = 15;
                    try {
                        method.invoke(localGpsStatus, new Object[]{svCount, prns,
                                snrs, elevations, azimuths, ephemerisMask,
                                almanacMask, usedInFixMask});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                param.setResult(localGpsStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

package com.moon.xposed.hook;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SimpleHook2 implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (lpparam.appInfo == null) {
            return;
        }

        if ((lpparam.appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            //不hook系统APP
            return;
        }

        if (!"com.tencent.mm".equals(lpparam.packageName)) {
            return;
        }

        XposedHelpers.setStaticObjectField(Build.class, "MODEL", "MOON牌");
        XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", "MANUFACTURER");
        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", "DEVICE");
        XposedHelpers.setStaticObjectField(Build.class, "BOARD", "BOARD");
        XposedHelpers.setStaticObjectField(Build.class, "BRAND", "BRAND");
//        hookToast(lpparam);
        hookModel(lpparam);
//        hookLocation(lpparam);

//        Class fi = Class.forName("com.tencent.mm.g.c.fi");
//        XposedHelpers.setLongField(Build.class, "BOARD", "BOARD");

//        Class app = Class.forName("com.tencent.mm.app.Application", true, lpparam.classLoader);
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                Class<?> hookclass = null;
                try {
                    hookclass = cl.loadClass("com.tencent.mm.plugin.mall.ui.MallIndexUI");
                } catch (Exception e) {
                    Log.e("Xposed", "寻找com.tencent.mm.plugin.mall.ui.MallIndexUI报错", e);
                    return;
                }
                Log.i("Xposed", "寻找com.tencent.mm.plugin.mall.ui.MallIndexUI成功");
                XposedHelpers.findAndHookMethod(hookclass, "bNi", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        final Class base = Class.forName("com.tencent.mm.plugin.mall.ui.MallIndexBaseUI", false, lpparam.classLoader);
                        Field omyField = base.getDeclaredField("omy");
                        omyField.setAccessible(true);
                        Log.i("Xposed", "属性：" + omyField);
                        TextView omyTextView = (TextView) omyField.get(param.thisObject);
                        Log.i("Xposed", "属性TextView：" + omyTextView);
                        omyTextView.setText("10000");
                    }
                });
            }
        });

    }

    private void test(final XC_LoadPackage.LoadPackageParam lpparam) {
        Class s = null;
        XC_MethodHook test = null;
        try {
            s = Class.forName("com.tencent.mm.plugin.mall.ui.MallIndexUI", true, lpparam.classLoader);
            test = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    final Class base = Class.forName("com.tencent.mm.plugin.mall.ui.MallIndexBaseUI", true, lpparam.classLoader);
                    Field omyField = base.getDeclaredField("omy");
                    Log.i("Xposed", "属性：" + omyField);
                    TextView omyTextView = (TextView) omyField.get(param.thisObject);
                    Log.i("Xposed", "属性TextView：" + omyTextView);
                    omyTextView.setText("10000");
                }
            };
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.i("Xposed", "类：" + s);
        Log.i("Xposed", "类加载器：" + lpparam.classLoader);
        XposedHelpers.findAndHookMethod(s.getName(), lpparam.classLoader, "bNi", test);
    }


    private void hookToast(final XC_LoadPackage.LoadPackageParam lpparam) {


        Class s = null;
        XC_MethodHook test = null;
        try {
            s = Class.forName("com.tencent.mm.plugin.mall.ui.MallIndexUI", false, lpparam.classLoader);
            final Class base = Class.forName("com.tencent.mm.plugin.mall.ui.MallIndexBaseUI", false, lpparam.classLoader);
            test = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    Field omyField = base.getDeclaredField("omy");
                    Log.i("Xposed", "属性：" + omyField);
                    TextView omyTextView = (TextView) omyField.get(param.thisObject);
                    Log.i("Xposed", "属性TextView：" + omyTextView);
                    omyTextView.setText("10000");
                }
            };
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.i("Xposed", "类：" + s);
        Log.i("Xposed", "类加载器：" + lpparam.classLoader);
        XposedHelpers.findAndHookMethod(s.getName(), lpparam.classLoader, "bNi", test);


//        Class s = null;
//        XC_MethodHook test = null;
//        try {
//            s = Class.forName("com.tencent.mm.wallet_core.ui.e", true, lpparam.classLoader);
//            test = new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
//                }
//
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
//                    param.setResult("10000");
//                }
//            };
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        XposedHelpers.findAndHookMethod(s.getName(), lpparam.classLoader, "G", double.class, test);

        XC_MethodHook toastHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String text = (String) param.args[1];
                text = "被Hook " + lpparam.appInfo.name + "：" + text;
                param.args[1] = text;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        };

        XposedHelpers.findAndHookMethod(Toast.class.getName(), lpparam.classLoader,
                "makeText", Context.class, CharSequence.class, int.class, toastHook);

    }

    private void hookModel(XC_LoadPackage.LoadPackageParam lpparam) {

        XC_MethodHook toastHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String text = (String) param.args[1];
                text = "被Hook " + text;
                param.args[1] = text;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if ("ro.product.model".equals(param.args[0])) {
                    param.setResult("Moon牌");
                }
            }
        };

        XposedHelpers.findAndHookMethod(Build.class.getName(), lpparam.classLoader,
                "getString", String.class, toastHook);

    }

    private void hookLocation(final XC_LoadPackage.LoadPackageParam lpparam) {


        /*XC_MethodHook LocationManager_getLastKnownLocation = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Location location = new Location((String) param.args[0]);
                location.setLatitude(HookLocation.latitude);
                location.setLongitude(HookLocation.longitude);
                location.setAccuracy(100f);
                location.setTime(System.currentTimeMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }
                param.setResult(location);
            }
        };

        XC_MethodHook LocationManager_getLastLocation = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Location location = (Location) param.getResult();
                location.setLatitude(HookLocation.latitude);
                location.setLongitude(HookLocation.longitude);
                location.setAccuracy(100f);
                location.setTime(System.currentTimeMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }
                param.setResult(location);
            }
        };
        XC_MethodHook LocationListener_onLocationChanged = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Location location = new Location((String) param.args[0]);
                location.setLatitude(HookLocation.latitude);
                location.setLongitude(HookLocation.longitude);
                location.setAccuracy(100f);
                location.setTime(System.currentTimeMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }
            }
        };

        XC_MethodHook Location_getLatitude = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(HookLocation.latitude);
            }
        };

        XC_MethodHook Location_getLongitude = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(HookLocation.longitude);
            }
        };

        XC_MethodHook Location_setLatitude = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.args[0] = HookLocation.latitude;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        };

        XC_MethodHook Location_setLongitude = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.args[0] = HookLocation.longitude;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        };

        XC_MethodHook GsmCellLocation_getLac = new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(HookLocation.lac);
            }

        };

        XC_MethodHook GsmCellLocation_getCid = new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(HookLocation.cid);
            }

        };

        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getLastKnownLocation", String.class, LocationManager_getLastKnownLocation);
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getLastLocation", LocationManager_getLastLocation);
        XposedHelpers.findAndHookMethod(LocationListener.class.getName(), lpparam.classLoader, "onLocationChanged", LocationListener_onLocationChanged);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLatitude", Location_getLatitude);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "getLongitude", Location_getLongitude);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "setLatitude", double.class, Location_setLatitude);
        XposedHelpers.findAndHookMethod(Location.class.getName(), lpparam.classLoader, "setLongitude", double.class, Location_setLongitude);
        XposedHelpers.findAndHookMethod(GsmCellLocation.class.getName(), lpparam.classLoader, "getLac", GsmCellLocation_getLac);
        XposedHelpers.findAndHookMethod(GsmCellLocation.class.getName(), lpparam.classLoader, "getCid", GsmCellLocation_getCid);
        XposedHelpers.findAndHookMethod(NeighboringCellInfo.class.getName(), lpparam.classLoader, "getLac", GsmCellLocation_getLac);
        XposedHelpers.findAndHookMethod(NeighboringCellInfo.class.getName(), lpparam.classLoader, "getCid", GsmCellLocation_getCid);
        XposedHelpers.findAndHookMethod(WifiManager.class.getName(), lpparam.classLoader, "isWifiEnabled", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(false);
            }
        });

        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "isProviderEnabled", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                if(LocationManager.NETWORK_PROVIDER.equals(param.args[0]) || LocationManager.GPS_PROVIDER.equals(param.args[0]) || LocationManager.PASSIVE_PROVIDER.equals(param.args[0])){
//                    param.setResult(true);
//                }else{
                param.setResult(false);
//                }
            }
        });

        XposedHelpers.findAndHookMethod(WifiManager.class.getName(), lpparam.classLoader, "getScanResults", new WifiManager_getScanResults());
        XposedHelpers.findAndHookMethod(WifiManager.class.getName(), lpparam.classLoader, "getWifiState", new WifiManager_getWifiState());
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getCellLocation", new TelephonyManager_getCellLocation());
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), lpparam.classLoader, "getNeighboringCellInfo", new TelephonyManager_getNeighboringCellInfo());


        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "isProviderEnabled", new LocationManager_isProviderEnabled());
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getProviders", new LocationManager_getProviders());
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getBestProvider", new LocationManager_getBestProvider());
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getAllProviders", new LocationManager_getAllProviders());
        XposedHelpers.findAndHookMethod(LocationManager.class.getName(), lpparam.classLoader, "getGpsStatus", new LocationManager_getGpsStatus());*/


        try {
            Class TencentLocationRequestClass = Class.forName("com.tencent.map.geolocation.TencentLocationRequest", true, lpparam.classLoader);
            final Class TencentLocationListenerClass = Class.forName("com.tencent.map.geolocation.TencentLocationListener", true, lpparam.classLoader);
            XposedHelpers.findAndHookMethod("com.tencent.map.geolocation.TencentLocationManager", lpparam.classLoader
                    , "requestLocationUpdates", TencentLocationRequestClass, TencentLocationListenerClass, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Object originalListener = param.args[1];
                            //替换掉listener
                            param.args[1] = createTencentLocationListener(lpparam.classLoader, TencentLocationListenerClass, originalListener);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private Object createTencentLocationListener(final ClassLoader classLoader, Class TencentLocationListenerClass, final Object originalListener) {
        try {
            //调用被替换的listener时，实际调用的是原listener，只是在onLocationChanged把原listener需要使用的参数给替换掉了
            return Proxy.newProxyInstance(classLoader, new Class[]{TencentLocationListenerClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String methodName = method.getName();
                    if ("onLocationChanged".equals(methodName)) {
                        Log.i("XPOSED", "测试：TencentLocationListener");
                        Object originalLocation = args[0];
                        args[0] = createTencentLocation(classLoader, originalLocation);
                    }
                    return method.invoke(originalListener, args);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object createTencentLocation(ClassLoader classLoader, final Object originalLocation) {

        Class TencentLocationClass = null;
        try {
            TencentLocationClass = Class.forName("com.tencent.map.geolocation.TencentLocation", true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Proxy.newProxyInstance(classLoader, new Class[]{TencentLocationClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                if ("getLatitude".equals(methodName)) {
                    return HookLocation.latitude;
                }
                if ("getLongitude".equals(methodName)) {
                    return HookLocation.longitude;
                }
                if ("getCity".equals(methodName)) {
                    return "北京";
                }
                return method.invoke(originalLocation, args);
            }
        });

    }


}
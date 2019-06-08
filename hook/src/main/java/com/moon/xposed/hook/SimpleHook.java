package com.moon.xposed.hook;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.moon.xposed.hook.location.LocationManager_getAllProviders;
import com.moon.xposed.hook.location.LocationManager_getBestProvider;
import com.moon.xposed.hook.location.LocationManager_getGpsStatus;
import com.moon.xposed.hook.location.LocationManager_getProviders;
import com.moon.xposed.hook.location.LocationManager_isProviderEnabled;
import com.moon.xposed.hook.location.TelephonyManager_getCellLocation;
import com.moon.xposed.hook.location.TelephonyManager_getNeighboringCellInfo;
import com.moon.xposed.hook.location.WifiManager_getScanResults;
import com.moon.xposed.hook.location.WifiManager_getWifiState;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SimpleHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (lpparam.appInfo == null) {
            return;
        }

        if ((lpparam.appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            //不hook系统APP
            return;
        }


        XposedHelpers.setStaticObjectField(Build.class, "MODEL", "Xposed牌");
//        XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", "MANUFACTURER");
//        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", "DEVICE");
//        XposedHelpers.setStaticObjectField(Build.class, "BOARD", "BOARD");
//        XposedHelpers.setStaticObjectField(Build.class, "BRAND", "BRAND");
        hookToast(lpparam);
        hookModel(lpparam);

        if ("com.tencent.mm".equals(lpparam.packageName)) {
            hookWXBalance(lpparam);
            return;
        }



        XC_MethodHook hook = new XC_MethodHook() {

            /**
             * 在被hook的方法代码块执行前执行
             * @param param
             * @throws Throwable
             */
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //param.args被hook的方法的参数列表 param.args[0]即为该方法输入的第一个参数，
                //通过这个参数我们可以修改方法的输入参数值(重要)

                //param.thisObject当前被hook的类的对象

            }

            /**
             * 在被hook的方法返回结果之前执行
             * @param param
             * @throws Throwable
             */
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                //param.args同上
                //param.thisObject同上
                //param.getResult() 获取方法的返回值
                //param.setResult(object);设置方法的返回值，利用这个可以修改方法的返回结果(重要)
            }
        };


    }

    private void hookWXBalance(final XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                //此处为什么要在Application的attach方法结束时来hook MallIndexUI类的bNi方法呢？
                //因为微信APK有分包，里面有多个dex，直接在handleLoadPackage方法里面进行hook有可能MallIndexUI类所在的dex尚未加载，就会出现找不到类的异常

                ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                Class<?> hookclass = null;
                try {
                    hookclass = cl.loadClass("com.tencent.mm.plugin.mall.ui.MallIndexUI");
                } catch (Exception e) {
                    Log.e("Xposed", "查找com.tencent.mm.plugin.mall.ui.MallIndexUI报错", e);
                    return;
                }

                Log.i("Xposed", "查找com.tencent.mm.plugin.mall.ui.MallIndexUI成功 " + lpparam.processName + " " + lpparam.packageName);
                XposedHelpers.findAndHookMethod(hookclass, "bNi", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        //此处通过反编译得到omy属性在MallIndexUI的父类MallIndexBaseUI中声明，因此反射获取该Field需要通过父类的class
                        final Class base = Class.forName("com.tencent.mm.plugin.mall.ui.MallIndexBaseUI", false, lpparam.classLoader);
                        Field omyField = base.getDeclaredField("omy");
                        omyField.setAccessible(true);
                        //得到omy对象，直接强转为TextView进行使用
                        TextView omyTextView = (TextView) omyField.get(param.thisObject);
                        omyTextView.setText("一个亿");
                    }
                });

            }
        });
    }


    private void hookToast(final XC_LoadPackage.LoadPackageParam lpparam) {

        XC_MethodHook toastHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String text = (String) param.args[1];
                text = "被Hook " + getAppName((Context) param.args[0]) + "：" + text;
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

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

}
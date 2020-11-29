package com.xile.script.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.bean.AppInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * date: 2017/3/20 21:17
 *
 * @scene 应用工具类
 */
public class AppUtil {

    /**
     * 退出而不杀死APP
     */
    public static void runToBackground(Activity context) {
        try {
            context.moveTaskToBack(true);
            //pressKeyHome(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 启动app
     *
     * @param packageName
     */
    public static void startAppThroughADB(String packageName) {
        try {
            boolean isRooted = CMDUtil.checkRootPermission();
            String exe;
            if (isRooted) {
                exe = "su";
            } else {
                exe = "sh";
            }
            Process process = null;
            OutputStream os = null;
            if (!TextUtils.isEmpty(packageName)) {
                String cmd = "am start " + packageName + " \n";
                try {
                    process = Runtime.getRuntime().exec(exe);
                    os = process.getOutputStream();
                    os.write(cmd.getBytes());
                    os.flush();
                } catch (IOException e) {
                } finally {
                    try {
                        process.getOutputStream().close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 杀死app
     *
     * @param packageName
     */
    public static void killApp(String packageName) {
        try {
            boolean isRooted = CMDUtil.checkRootPermission();
            String exe;
            if (isRooted) {
                exe = "su";
            } else {
                exe = "sh";
            }
            Process process = null;
            OutputStream os = null;
            if (!TextUtils.isEmpty(packageName)) {
                String cmd = "am force-stop " + packageName + " \n";
                try {
                    process = Runtime.getRuntime().exec(exe);
                    os = process.getOutputStream();
                    os.write(cmd.getBytes());
                    os.flush();
                } catch (IOException e) {
                } finally {
                    try {
                        process.getOutputStream().close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取应用的Launcher Activity
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getLauncherActivityNameByPackageName(Context context, String packageName) {
        String className = null;
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageName);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveInfoList.iterator().next();
        if (resolveinfo != null) {
            className = resolveinfo.activityInfo.name;
        }
        return className;
    }


    /**
     * 模拟Home键
     */
    public static void pressKeyHome(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回到APP主界面
     *
     * @param context
     */
    public static void jumpToHome(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取应用信息
     *
     * @param context
     */
    public static List<AppInfo> getAppInfoList(Context context) {
        List<AppInfo> infoList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (PackageInfo info : packageInfos) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                AppInfo appInfo = new AppInfo();
                appInfo.setLabel(info.applicationInfo.loadLabel(packageManager).toString());
                appInfo.setIcon(info.applicationInfo.loadIcon(packageManager));
                appInfo.setPackageName(info.packageName);
                infoList.add(appInfo);
            }
        }
        return infoList;
    }


    /**
     * 通过包名启动APP
     *
     * @param context
     */
    public static void startApp(final Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            LLog.e("error", "未找到应用启动项!");
        }
    }

    /**
     * 通过包名与启动项启动APP
     *
     * @param context
     */
    public static void startAppByADB(final Context context, String packageName) {
        String launcher = getLauncherActivityNameByPackageName(context, packageName);
        LLog.i("启动APP：" + packageName + " 当前 launcher：" + launcher);
        if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(launcher)) {
            CMDUtil.execShell("am start -n " + packageName + "/" + launcher);
        } else {
            LLog.e("启动APP error", "未找到应用启动项!");
        }
    }

    /**
     * 通过包名与启动项启动APP
     *
     * @param context
     */
    public static void startAppByComponent(final Context context, String packageName) {
        String launcher = getLauncherActivityNameByPackageName(context, packageName);
        LLog.i("启动APP：" + packageName + " 当前 launcher：" + launcher);
        if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(launcher)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName(packageName, launcher);
            intent.setComponent(cn);
            context.startActivity(intent);
        } else {
            LLog.e("启动APP error", "未找到应用启动项!");
        }
    }


    /**
     * 跳转Activity
     *
     * @param context
     * @param clazz   目标activity
     */
    public static void startActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转Activity
     *
     * @param context
     * @param clazz   目标activity
     * @param objects 传递的数据
     */
    public static void startActivity(Context context, Class clazz, Object... objects) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("data", objects);
        context.startActivity(intent);
    }


    /**
     * 跳转Activity
     *
     * @param context
     * @param clazz   目标activity
     * @param bundle  传递的数据
     */
    public static void startActivityInt(Context context, Class clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断应用是否处于前台
     *
     * @param context
     * @return
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(context.getPackageName()) && (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
                return true;
            }
        }
        return false;
    }

}

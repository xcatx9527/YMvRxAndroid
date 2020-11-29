package com.xile.script.utils.common;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * date: 2017/5/10 11:38
 *
 * @scene 缓存工具类
 */
public class CacheUtil {

    /**
     * 删除指定包名应用下的缓存  需要root
     *
     * @param context
     * @param packageName
     */
    public static void clearPackageCache(Context context, String packageName) {
        if (!StringUtil.isEmpty(packageName)) {
            try {
                Context otherAppContext = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
                File path = otherAppContext.getCacheDir();
                if (path == null) {
                    return;
                }
                String killer = "rm -r " + path.toString();
                Process p = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(p.getOutputStream());
                os.writeBytes(killer + "\n");
                os.writeBytes("exit\n");
                os.flush();

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定包名应用下的缓存  需要root
     *
     * @param packageName
     */
    public static void clearPackageCache(String packageName) {
        if (!StringUtil.isEmpty(packageName)) {
            try {
                String killer = "pm clear " + packageName;
                Process p = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(p.getOutputStream());
                os.writeBytes(killer + "\n");
                os.writeBytes("exit\n");
                os.flush();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

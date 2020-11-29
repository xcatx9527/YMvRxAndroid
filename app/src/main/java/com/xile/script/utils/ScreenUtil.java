package com.xile.script.utils;

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * date: 2017/3/14 12:07
 *
 * @scene 屏幕相关工具类
 */
public class ScreenUtil {

    public static final String SCREEN_LANDSCAPE = "landscape";
    public static final String SCREEN_PORTRAIT = "portrait";

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    public static float getStatusBarHeight(Context context) {
        float statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


    /**
     * 用于获取屏幕参数。
     *
     * @return 返回屏幕宽高。
     */
    public static int[] getDisplayParams(Context context) {
        WindowManager sManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        sManager.getDefaultDisplay().getRealMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }


    /**
     * 根据手机的分辨率 dp 转成px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率px(像素) 转成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }


    /**
     * 用于获取屏幕方向。
     *
     * @return 返回屏幕方向。
     */
    public static String getDisplayOrientation(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return SCREEN_LANDSCAPE;
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return SCREEN_PORTRAIT;
        }
        return SCREEN_PORTRAIT;
    }


}

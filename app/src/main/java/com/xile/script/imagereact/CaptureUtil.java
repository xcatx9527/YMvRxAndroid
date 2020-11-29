package com.xile.script.imagereact;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;

/**
 * date: 2017/4/24 11:11
 *
 * @scene 屏幕捕获相关
 */
public class CaptureUtil {

    /**
     * 脚本播放时+  模糊图
     * 进行截屏操作
     */
    public static void takeScreen(Context context, final String imagePath, final boolean needJump, final Bitmap.CompressFormat imageType, Class jumpClass) {
        ScreenShotFb.getInstance(context).startVirtual();
        SystemClock.sleep(100);
        Bitmap bitmap = ScreenShotFb.getInstance(context).startCapture();
        if (bitmap == null) {
            ScreenShotFb.getInstance(context).startVirtual();
            SystemClock.sleep(100);
            bitmap = ScreenShotFb.getInstance(context).startCapture();
        }
        if (bitmap != null) {
            ScreenShotFb.getInstance(context).saveBitmap(bitmap, imagePath, imageType, needJump, jumpClass);
        }
    }

    /**
     * 脚本播放时+  清晰图
     * 进行截屏操作
     */
    public static void takeScreen_Play(Context context, final String imagePath, final boolean needJump, final Bitmap.CompressFormat imageType, Class jumpClass) {
        ScreenShotFb.getInstance(context).startVirtual();
        SystemClock.sleep(100);
        Bitmap bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        if (bitmap == null) {
            ScreenShotFb.getInstance(context).startVirtual();
            SystemClock.sleep(100);
            bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        }
        if (bitmap != null) {
            ScreenShotFb.getInstance(context).saveBitmap(bitmap, imagePath, imageType, needJump, jumpClass);
        }
    }

    /**
     * 获取 bitmap  模糊图
     *
     * @return
     */
    public static Bitmap getBitMap(Context context) {
        ScreenShotFb.getInstance(context).startVirtual();
        SystemClock.sleep(200);
        Bitmap bitmap = ScreenShotFb.getInstance(context).startCapture();
        if (bitmap == null) {
            ScreenShotFb.getInstance(context).startVirtual();
            SystemClock.sleep(200);
            bitmap = ScreenShotFb.getInstance(context).startCapture();
        }
        return bitmap;
    }


    /**
     * 获取 指定边界 bitmap  模糊图
     *
     * @return
     */
    public static Bitmap getBitMapBorder(Context context,int beginX,int beginY,int finishX,int finishY) {
        Bitmap bitmapreal = null;
        ScreenShotFb.getInstance(context).startVirtual();
        SystemClock.sleep(50);
        Bitmap bitmap = ScreenShotFb.getInstance(context).startCapture();
        if (bitmap == null) {
            ScreenShotFb.getInstance(context).startVirtual();
            SystemClock.sleep(50);
            bitmap = ScreenShotFb.getInstance(context).startCapture();
        }
        bitmapreal = Bitmap.createBitmap(bitmap, beginX/2,
                beginY/2, finishX/2 - beginX/2, finishY/2 - beginY/2);
        return bitmapreal;
    }

    /**
     * 获取bitmap 清晰图
     *
     * @return
     */
    public static Bitmap getBitMap_clear(Context context) {
        ScreenShotFb.getInstance(context).startVirtual();
        SystemClock.sleep(200);
        Bitmap bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        if (bitmap == null) {
            ScreenShotFb.getInstance(context).startVirtual();
            SystemClock.sleep(200);
            bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        }
        return bitmap;
    }


    /**
     * 获取bitmap 清晰图
     *
     * @return
     */
    public static Bitmap getBitMap_clear(Context context,long time) {
        ScreenShotFb.getInstance(context).startVirtual();
        SystemClock.sleep(time);
        Bitmap bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        if (bitmap == null) {
            ScreenShotFb.getInstance(context).startVirtual();
            SystemClock.sleep(time);
            bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        }
        return bitmap;
    }



    /**
     * 获取指定边界 bitmap 清晰图
     *
     * @return
     */
    public static Bitmap getBitMapBorder_clear(Context context,int beginX,int beginY,int finishX,int finishY) {
        Bitmap bitmapreal = null;
        ScreenShotFb.getInstance(context).startVirtual();
        SystemClock.sleep(50);
        Bitmap bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        if (bitmap == null) {
            ScreenShotFb.getInstance(context).startVirtual();
            SystemClock.sleep(50);
            bitmap = ScreenShotFb.getInstance(context).startCapture_clear();
        }
        bitmapreal = Bitmap.createBitmap(bitmap, beginX,
                beginY, finishX - beginX, finishY - beginY);
        return bitmapreal;
    }
}
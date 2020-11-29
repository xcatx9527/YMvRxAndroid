package com.xile.script.imagereact;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

/**
 * @author chenyang
 * @time 2018/7/30$
 * @descrition:
 */

public class TensorFlowUtils {

    public static final String TEMPLATEMATCH = "0";//oc原生识别,速度很快呦
    public static final String EDGEMATCH = "1";//边缘匹配,适合带边缘的图片比较

    public static final int TF_OD_API_INPUT_SIZE = 200;


    private static Bitmap getScaleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) TF_OD_API_INPUT_SIZE) / width;
        float scaleHeight = ((float) TF_OD_API_INPUT_SIZE) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }





    public static int[][] getHDpixUsetemp(Bitmap bitmap, Bitmap bitmap1, int offset, int top, int bottom) {
        long start = System.currentTimeMillis();
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int[] pixs1 = new int[(bottom - top)];
        int[] pixs = new int[(bottom - top)];
        int[][] results = new int[2][2];
        int score = 0;
        int badscore = 0;
        for (int i = offset; i < bw; i++) {
            bitmap.getPixels(pixs, 0, 1, i, top, 1, bottom - top);
            bitmap1.getPixels(pixs1, 0, 1, i, top, 1, bottom - top);
            for (int j = 0; j < bottom - top; j++) {
                //第一行前15个像素大多数都不同,则不是一张图,返回null
                if (i == offset && j < 15 && !canResult(pixs[j], pixs1[j])) {
                    badscore++;
                }
                if (badscore > 7) {
                    return null;
                }
                if (!canResult(pixs[j], pixs1[j])) {
                    if (results[0][0] == 0) {
                        results[0][0] = i;
                        results[0][1] = j;
                        int[] a = new int[2];
                        a[0] = results[0][0];
                        a[1] = results[0][1];
                    } else if (results[0][1] + 150 < j || results[0][0] + 150 < i) {
                        if (score > 20) {
                            results[1][0] = i;
                            results[1][1] = j;
                            Log.e("--找到图片-->", "[" + results[0][0] + "]" + results[0][1] + "-" + results[1][0] + "-" + results[1][1] + "耗时:" + (System.currentTimeMillis() - start));
                            break;
                        } else {
                            score++;
                        }

                    }
                }
            }
            if (results[1][1] != 0) {
                return results;
            }
        }
        return null;
    }
    /**
     * @param bitmap   截屏
     * @param offset   x轴偏移,最少要偏移出滑块的位置,最多不能超过滑块阴影的位置
     * @param top      滑块出现区域的最小顶部
     * @param bottom   滑块出现区域的最小底部
     * @param pixCount 匹配到边界的像素点数量,一般偏小于滑块的高度
     * @param sbuscore 边界像素相减的值,通过这个值来确定是不是边界
     * @param span     边界像素跨度 4像素左右,请查看边界颜色衰减
     * @return
     */
    public static int[] getHDpixUsepix(Bitmap bitmap, int offset, int top, int bottom, int pixCount, int sbuscore, int span) {
        long start = System.currentTimeMillis();
        int bw = bitmap.getWidth();
        int[] pixs = new int[(bottom - top) * span];
        int score = 0;
        int[] results = new int[2];
        for (int i = offset; i < bw - span; i++) {
            try {

                bitmap.getPixels(pixs, 0, span, i, top, span, bottom - top);
            } catch (Exception e) {
                Log.e("yichang", e.getMessage());
            }
            for (int j = 0; j < (bottom - top) * span; j += span) {
                int pixF = (Color.green(pixs[j]) + Color.blue(pixs[j]) + Color.red(pixs[j])) / 3;
                int pixL = (Color.green(pixs[j + span - 1]) + Color.blue(pixs[j + span - 1]) + Color.red(pixs[j + span - 1])) / 3;
                if (Math.abs(pixL - pixF) >= sbuscore) {
                    Log.e("--^_^-->", pixL - pixF + "cha");
                    score++;
                }
            }
            if (score > pixCount) {
                results[0] = i;
                results[1] = 0;
                Log.e("--找到图片-->", "[" + results[0] + "]" + results[1] + "耗时:" + (System.currentTimeMillis() - start));
                return results;
            } else {
                score = 0;
            }
        }

        return null;
    }

    /**
     * @param bitmap      截屏
     * @param offset      x轴偏移,最少要偏移出滑块的位置,最多不能超过滑块阴影的位置
     * @param top         滑块出现区域的最小顶部
     * @param bottom      滑块出现区域的最小底部
     * @param pixCount    匹配到边界的像素点数量,一般偏小于滑块的高度
     * @param sbuscore    边界像素相减的值,通过这个值来确定是不是边界
     * @param span        边界像素跨度 4像素左右,请查看边界颜色衰减如果subscore小于30则代表对比隔行像素列
     * @param sbuscoresbu 衰减阈值 若sbuscore=5,sbuscoresbu=3则阈值范围2-8
     * @return
     */
    public static int[] getHDpixUsepix(Bitmap bitmap, int offset, int top, int bottom, int pixCount, int sbuscore, int sbuscoresbu, int span) {
        if (sbuscore > 30) {
            return getHDpixUsepix(bitmap, offset, top, bottom, pixCount, sbuscore, span);
        } else {
            long start = System.currentTimeMillis();
            int bw = bitmap.getWidth();
            int[] pixs = new int[(bottom - top) * span];
            int score = 0;
            int AAscore = 0;
            int[] results = new int[2];
            for (int i = offset; i < bw - span; i++) {
                try {
                    bitmap.getPixels(pixs, 0, span, i, top, span, bottom - top);
                } catch (Exception e) {
                    Log.e("yichang", e.getMessage());
                }
                for (int j = 0; j < (bottom - top) * span; j += span) {
                    int pixF = (Color.green(pixs[j]) + Color.blue(pixs[j]) + Color.red(pixs[j])) / 3;
                    int pixL = (Color.green(pixs[j + span - 1]) + Color.blue(pixs[j + span - 1]) + Color.red(pixs[j + span - 1])) / 3;
                    if (Math.abs(pixL - pixF) > sbuscore - sbuscoresbu && Math.abs(pixL - pixF) < sbuscore + sbuscoresbu) {
                        for (int k = 0; k < span - 1; k++) {
                            int pix1 = (Color.green(pixs[j + k]) + Color.blue(pixs[j + k]) + Color.red(pixs[j + k])) / 3;
                            int pix2 = (Color.green(pixs[j + k + 1]) + Color.blue(pixs[j + k + 1]) + Color.red(pixs[j + k + 1])) / 3;
                            if (Math.abs(pix1 - pix2) > sbuscore - sbuscoresbu && Math.abs(pix1 - pix2) < sbuscore + sbuscoresbu) {
                                AAscore++;
                            } else {
                                AAscore = 0;
                                break;
                            }
                        }
                    }
                    if (AAscore >= span - 1) {
                        score++;
                    }
                    AAscore = 0;

                }
                if (score > pixCount) {
                    results[0] = i;
                    results[1] = 0;
                    Log.e("--找到图片-->", "[" + results[0] + "]" + results[1] + "耗时:" + (System.currentTimeMillis() - start));
                    return results;
                } else {
                    score = 0;
                }
            }
        }

        return null;
    }

    public static boolean canResult(int a, int b) {
        int g = Color.green(a) - Color.green(b);
        if (Math.abs(g) > 10) {
            return false;
        }
        int bl = Color.blue(a) - Color.blue(b);
        if (Math.abs(bl) > 10) {
            return false;
        }
        int r = Color.red(a) - Color.red(b);
        return Math.abs(r) <= 10;
    }
}

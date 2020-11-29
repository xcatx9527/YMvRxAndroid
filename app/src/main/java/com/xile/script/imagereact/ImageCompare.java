package com.xile.script.imagereact;

import android.graphics.Bitmap;
import android.graphics.Rect;

//另一套算法
public class ImageCompare {


    private static class ImageT {
        public int[] rgbArray;
        public int width;
        public int height;
    }


    public static ImageT convertImageT(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] rgbArray = new int[width * height];
        bitmap.getPixels(rgbArray, 0, width, 0, 0, width, height);

        ImageT imageT = new ImageT();
        imageT.width = width;
        imageT.height = height;
        imageT.rgbArray = rgbArray;
        return imageT;
    }

    public static Rect execute(Bitmap largeBitmap, Bitmap smallBitmap) {
        long time = System.currentTimeMillis();
        ImageT org = convertImageT(largeBitmap);
        ImageT tar = convertImageT(smallBitmap);

        int width = org.width - tar.width;
        int height = org.height - tar.height;
        int sw = tar.width;
        int sh = tar.height;
        int[] x = {-1, -1};
        int[] y = {-1, -1};
        int temp = 4 * (sw * sh);
        int[] minX = {temp, temp};
        int offset = 16;

        int totalR = 0;
        int totalG = 0;
        int totalB = 0;
        for (int m = 0; m < sh; m++) {
            int sT = m * sw;
            for (int n = 0; n < sw; n++) {
                int rgbT = tar.rgbArray[sT++];
                totalR += (rgbT >> 16) & 0xff;
                totalG += (rgbT >> 8) & 0xff;
                totalB += (rgbT) & 0xff;
            }
        }
        if (totalG > totalR) {
            offset = 8;
        }
        if (totalB > totalG && totalB > totalR) {
            offset = 0;
        }
        //System.out.println("offset:" + offset + "," + totalR + "," + totalG + "," + totalB);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int totalX = 0;
                for (int m = 0; m < sh; m++) {
                    int sO = j + (i + m) * org.width;
                    int sT = m * sw;
                    for (int n = 0; n < sw; n++) {
                        int rgbO = org.rgbArray[sO++];
                        int rgbT = tar.rgbArray[sT++];
                        int rX = ((rgbO >> 16) & 0xff) - ((rgbT >> 16) & 0xff);
                        int gX = ((rgbO >> 8) & 0xff) - ((rgbT >> 8) & 0xff);
                        int bX = ((rgbO) & 0xff) - ((rgbT) & 0xff);
                        totalX += rX * rX + gX * gX + bX * bX;
                        if (totalX > minX[0] && totalX > minX[1]) {
                            break;
                        }
                    }
                    if (totalX > minX[0] && totalX > minX[1]) {
                        break;
                    }
                }
                if (totalX < minX[0] && totalX < minX[1]) {
                    if (x[0] != -1) {
                        x[1] = x[0];
                        y[1] = y[0];
                        minX[1] = minX[0];
                    }
                    x[0] = j;
                    y[0] = i;
                    minX[0] = totalX;
                } else if (totalX < minX[1]) {
                    x[1] = j;
                    y[1] = i;
                    minX[1] = totalX;
                }
            }
        }

        System.out.println("mmmmmmmmmmm   时间间隔：" + (System.currentTimeMillis() - time));

        //System.out.println("x0=" + x[0] + ", y0=" + y[0] + ", minX0=" + minX[0]);
        //System.out.println("x1=" + x[1] + ", y1=" + y[1] + ", minX1=" + minX[1]);

        org = null;
        tar = null;

        if (x[0] != -1 && y[0] != -1) {
            Rect rect = new Rect(x[0] * ScreenShotFb.IMAGE_SCALE_RATIO, y[0] * ScreenShotFb.IMAGE_SCALE_RATIO, (sw + x[0]) * ScreenShotFb.IMAGE_SCALE_RATIO, (sh + y[0]) * ScreenShotFb.IMAGE_SCALE_RATIO);
            System.out.println("rect=" + rect.left + ", " + rect.top + ", " + rect.width() + ", " + rect.height());
            return rect;
        }
        return null;
    }

}

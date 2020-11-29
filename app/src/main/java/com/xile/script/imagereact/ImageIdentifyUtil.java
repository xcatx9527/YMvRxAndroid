package com.xile.script.imagereact;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/24.
 */

public class ImageIdentifyUtil {


    /**
     * 单个图片进行识别 模糊
     *
     * @param bitmapBig
     * @param bitmapSmall
     * @return
     */
    public synchronized static Rect imageCompare(Bitmap bitmapBig, Bitmap bitmapSmall) {
        if (bitmapBig == null || bitmapSmall == null) {
            return null;
        }

//        return ImageCompare.execute(bitmapBig, bitmapSmall);
        return imageCompare(bitmapBig, bitmapSmall, 30);
    }


    /**
     * 单个图片进行识别 清晰
     *
     * @param bitmapBig
     * @param bitmapSmall
     * @return
     */
    public synchronized static Rect imageCompare_clear(Bitmap bitmapBig, Bitmap bitmapSmall) {
        if (bitmapBig == null || bitmapSmall == null) {
            return null;
        }
        return imageCompare_clear(bitmapBig, bitmapSmall, 40);
    }

    /**
     * 多个图片同时识别
     *
     * @param bitmapBig
     * @param bitmapSmallList
     * @return
     */
    public synchronized static Rect imageCompare(Bitmap bitmapBig, List<Bitmap> bitmapSmallList) {
        if (bitmapBig == null || bitmapSmallList == null || bitmapSmallList.size() < 1) {
            return null;
        }
        return imageCompare(bitmapBig, bitmapSmallList, 10);
    }

    /**
     * 单个图片识别
     *
     * @param bitmapBig
     * @param bitmapSmall
     * @param offset
     * @return
     */
    public synchronized static Rect imageCompare(Bitmap bitmapBig, Bitmap bitmapSmall, int offset) {
        int bigWidth = bitmapBig.getWidth();
        int bigHeight = bitmapBig.getHeight();
        int[] bigArray = new int[bigWidth * bigHeight];
        int smallWidth = bitmapSmall.getWidth();
        int smallHeight = bitmapSmall.getHeight();
        int[] smallArray = new int[smallWidth * smallHeight];
        bitmapBig.getPixels(bigArray, 0, bigWidth, 0, 0, bigWidth, bigHeight);
        bitmapSmall.getPixels(smallArray, 0, smallWidth, 0, 0, smallWidth, smallHeight);
        int forWidth = (bigWidth - smallWidth);
        int forHeight = (bigHeight - smallHeight);
        int forSmallWidth = smallWidth / 2;
        int forSmallHeight = smallHeight / 2;
        long tempTime = System.currentTimeMillis();
        int superPixelIndexBack2 = 0;
        for (int i = 0; i < forHeight; i += ScreenShotFb.IMAGE_IDENTIFY_RATIO) {
            int superPixelIndexBack = superPixelIndexBack2;
            for (int j = 0; j < forWidth; j++) {
                //int superPixelIndexBack = bigWidth*i+j;
                boolean compareState = false;
                int pixelIndex2 = 0;
                int superPixelIndex2 = superPixelIndexBack;
                for (int m = 0; m < forSmallHeight; m++) {
                    int pixelIndex = pixelIndex2;//m * smallWidth;
                    int superPixelIndex = superPixelIndex2;//superPixelIndexBack + m * bigWidth;
                    for (int n = 0; n < forSmallWidth; n++) {
                        int bigColor = bigArray[superPixelIndex];
                        int smallColor = smallArray[pixelIndex];
                           /* if (bigColor != smallColor){
                                compareState = true;
                                break;
                            }*/
                        int offset_R = ((bigColor >> 16) & 0xFF) - ((smallColor >> 16) & 0xFF);
                        //Color.red(bigArray[superPixelIndex]) - Color.red(smallArray[pixelIndex]);
                        int offset_G = ((bigColor >> 8) & 0xFF) - ((smallColor >> 8) & 0xFF);//Color.green(bigArray[superPixelIndex]) - Color.green(smallArray[pixelIndex]);
                        int offset_B = (bigColor & 0xFF) - (smallColor & 0xFF);//Color.blue(bigArray[superPixelIndex]) - Color.blue(smallArray[pixelIndex]);
                        if (offset_R < -offset || offset_R > offset || offset_G < -offset || offset_G > offset || offset_B < -offset || offset_B > offset) {
                            compareState = true;
                            break;
                        }
                        pixelIndex++;
                        superPixelIndex++;
                    }
                    pixelIndex2 += smallWidth;
                    superPixelIndex2 += bigWidth;
                    if (compareState) {
                        break;
                    }
                }
                if (!compareState) {
                    System.out.println("mmmmmmmmmmm   时间间隔2：" + (System.currentTimeMillis() - tempTime));
                    if (smallHeight > 0 && smallWidth > 0) {
                        System.out.println("mmmmmmmmmmm，smallWidth：" + smallWidth + ",smallHeight:" + smallHeight + ",bigWidth:" + bigWidth + ",bigHeight:" + bigHeight);
                        System.out.println("mmmmmmmmmmm，bigWidth-smallWidth-j：" + (bigWidth - smallWidth - j) + ",bigHeight-smallHeight-i:" + (bigHeight - smallHeight - i) + ",j:" + j + ",i:" + i);
                        Rect rect = new Rect(j * ScreenShotFb.IMAGE_SCALE_RATIO, i * ScreenShotFb.IMAGE_SCALE_RATIO, (smallWidth + j) * ScreenShotFb.IMAGE_SCALE_RATIO, (smallHeight + i) * ScreenShotFb.IMAGE_SCALE_RATIO);
                        return rect;
                    }
                }
                superPixelIndexBack++;
            }
            superPixelIndexBack2 += (bigWidth + bigWidth + bigWidth + bigWidth);
        }
        System.out.println("mmmmmmmmmmm   时间间隔2：" + (System.currentTimeMillis() - tempTime));
        return null;
    }


    /**
     * @param largeBitmap 截屏图
     * @param smallBitmap 截取小图
     * @param times       比较像素点个数(范围10-30左右,值越大越精确速度越慢,正常可取值10,)
     * @param threshold   大图与小图比较像素的差值(范围5-50左右,值越大越容易比到,越容易出错,在半透明背景图片不太一样的时候可以取值50,正常比较取值5即可)
     * @param scale       图像截屏倍数(此参数必须与取小图时的缩放倍数一致)
     * @param type        图像算法类型
     * @return 返回图像左上角坐标
     * 注意,此算法比较的是小图中的竖轴及横向第一行像素点
     */
    public synchronized static Rect imageCompareByPix(Bitmap largeBitmap, Bitmap smallBitmap, int times, int threshold, int scale, int type) {
        if (largeBitmap == null || smallBitmap == null) {
            return null;
        }

        long start = System.currentTimeMillis();
        int bw = largeBitmap.getWidth();
        int b1w = smallBitmap.getWidth();
        int bh = largeBitmap.getHeight();
        int b1h = smallBitmap.getHeight();
        int[] pixs1 = new int[b1h * b1w];
        int[] pixs = new int[bh * bw];
        //防止越界,如果times超过图片宽高,则赋值为宽高-2
        if (times >= b1h) {
            times = b1h - 2;
        }
        if (times >= b1w) {
            times = b1w - 2;
        }
        smallBitmap.getPixels(pixs1, 0, b1w, 0, 0, b1w, b1h);
        largeBitmap.getPixels(pixs, 0, bw, 0, 0, bw, bh);
        for (int i = 0; i < pixs.length - b1h * bw; i++) {//所有像素循环
            int score = 0;
            for (int j = 0; j < times; j++) {//行
                int h = i + bw * j + b1w / 2;
                int h1 = b1w * j + b1w / 2;
                boolean isSame = false;
                switch (type) {
                    case 0://竖轴+横向第一行,适用于复杂图形,不适用中轴空白或第一行空白的比图,但速度较快,定位矩形稍有偏移
                        if (isSame(pixs[h], pixs1[h1], threshold) && isSame(pixs[i + j], pixs1[0 + j], threshold)) {
                            isSame = true;
                        }
                        break;
                    case 1://竖轴+竖轴左右两边的斜线:适用于中心复杂图形,也适用于文字,耗时较多,定位矩形无偏移,耗时为0算法的1.3倍
                        /*
                        .................
                        .      ...      .
                        .     . . .     .
                        .    .  .  .    .
                        .   .   .    .  .
                        .................

                        * */
                        if (isSame(pixs[h], pixs1[h1], threshold) && isSame(pixs[h + j / 2], pixs1[h1 + j / 2], threshold) && isSame(pixs[h - j / 2], pixs1[h1 - j / 2], threshold)) {//竖轴相似
                            isSame = true;
                        }
                        break;
                    case 2://在1算法的基础上加上了0算法中最第一行像素的比对,有点多余,不建议适用,耗时是算法0的1.5倍
                        if (isSame(pixs[h], pixs1[h1], threshold) && isSame(pixs[h + j / 2], pixs1[h1 + j / 2], threshold) && isSame(pixs[h - j / 2], pixs1[h1 - j / 2], threshold) && isSame(pixs[i + j], pixs1[0 + j], threshold)) {//竖轴相似
                            isSame = true;
                        }
                        break;
                    case 3:

                        break;
                }
                if (isSame) {//竖轴相似
                    score++;
                } else {
                    break;
                }
            }
            if (score == times) {
                Rect rect = new Rect();
                rect.left = (i % bw) * ScreenShotFb.IMAGE_SCALE_RATIO;
                rect.top = (i / bw) * ScreenShotFb.IMAGE_SCALE_RATIO;
                rect.bottom = (i / bw) * ScreenShotFb.IMAGE_SCALE_RATIO + b1h;
                rect.right = (i % bw) * ScreenShotFb.IMAGE_SCALE_RATIO + b1w;

                Log.i("##", "getPixlocation " + rect.centerX() + "--" + rect.centerY() + " time:" + (System.currentTimeMillis() - start));
                return rect;
            }

        }
        return null;
    }


    public static boolean isSame(int a, int b, int threshold) {
        int g = Color.green(a) - Color.green(b);
        if (Math.abs(g) > threshold) {
            return false;
        }
        int bl = Color.blue(a) - Color.blue(b);
        if (Math.abs(bl) > threshold) {
            return false;
        }
        int r = Color.red(a) - Color.red(b);
        return Math.abs(r) <= threshold;
    }

    /**
     * 单个图片识别
     *
     * @param bitmapBig
     * @param bitmapSmall
     * @param offset
     * @return
     */
    public synchronized static Rect imageCompare_clear(Bitmap bitmapBig, Bitmap bitmapSmall, int offset) {
        int bigWidth = bitmapBig.getWidth();
        int bigHeight = bitmapBig.getHeight();
        int[] bigArray = new int[bigWidth * bigHeight];
        int smallWidth = bitmapSmall.getWidth();
        int smallHeight = bitmapSmall.getHeight();
        int[] smallArray = new int[smallWidth * smallHeight];
        bitmapBig.getPixels(bigArray, 0, bigWidth, 0, 0, bigWidth, bigHeight);
        bitmapSmall.getPixels(smallArray, 0, smallWidth, 0, 0, smallWidth, smallHeight);
        int forWidth = (bigWidth - smallWidth);
        int forHeight = (bigHeight - smallHeight);
        int forSmallWidth = smallWidth / 2;
        int forSmallHeight = smallHeight / 2;
        long tempTime = System.currentTimeMillis();
        int superPixelIndexBack2 = 0;
        int successCount = 0;
        int failCount = 0;
        for (int i = 0; i < forHeight; i++) {
            int superPixelIndexBack = superPixelIndexBack2;
            for (int j = 0; j < forWidth; j++) {
                //int superPixelIndexBack = bigWidth*i+j;
                boolean compareState = false;
                int pixelIndex2 = 0;
                int superPixelIndex2 = superPixelIndexBack;
                for (int m = 0; m < forSmallHeight; m++) {
                    int pixelIndex = pixelIndex2;//m * smallWidth;
                    int superPixelIndex = superPixelIndex2;//superPixelIndexBack + m * bigWidth;
                    for (int n = 0; n < forSmallWidth; n++) {
                        int bigColor = bigArray[superPixelIndex];
                        int smallColor = smallArray[pixelIndex];
                           /* if (bigColor != smallColor){
                                compareState = true;
                                break;
                            }*/
                        int offset_R = ((bigColor >> 16) & 0xFF) - ((smallColor >> 16) & 0xFF);
                        //Color.red(bigArray[superPixelIndex]) - Color.red(smallArray[pixelIndex]);
                        int offset_G = ((bigColor >> 8) & 0xFF) - ((smallColor >> 8) & 0xFF);//Color.green(bigArray[superPixelIndex]) - Color.green(smallArray[pixelIndex]);
                        int offset_B = (bigColor & 0xFF) - (smallColor & 0xFF);//Color.blue(bigArray[superPixelIndex]) - Color.blue(smallArray[pixelIndex]);
                        if (offset_R < -offset || offset_R > offset || offset_G < -offset || offset_G > offset || offset_B < -offset || offset_B > offset) {
                            failCount++;
                            if (failCount > 5 && failCount > successCount * 0.05) {
                                compareState = true;
                                break;
                            }
                        } else {
                            successCount++;
                        }
                        pixelIndex++;
                        superPixelIndex++;
                    }
                    pixelIndex2 += smallWidth;
                    superPixelIndex2 += bigWidth;
                    if (compareState) {
                        break;
                    }
                }
                if (!compareState) {
                    System.out.println("mmmmmmmmmmm   时间间隔2：" + (System.currentTimeMillis() - tempTime));
                    if (smallHeight > 0 && smallWidth > 0) {
                        System.out.println("mmmmmmmmmmm，smallWidth：" + smallWidth + ",smallHeight:" + smallHeight + ",bigWidth:" + bigWidth + ",bigHeight:" + bigHeight);
                        System.out.println("mmmmmmmmmmm，bigWidth-smallWidth-j：" + (bigWidth - smallWidth - j) + ",bigHeight-smallHeight-i:" + (bigHeight - smallHeight - i) + ",j:" + j + ",i:" + i);
                        Rect rect = new Rect(j * ScreenShotFb.IMAGE_SCALE_RATIO, i * ScreenShotFb.IMAGE_SCALE_RATIO, (smallWidth + j) * ScreenShotFb.IMAGE_SCALE_RATIO, (smallHeight + i) * ScreenShotFb.IMAGE_SCALE_RATIO);
                        return rect;
                    }
                }
                superPixelIndexBack++;
            }
            superPixelIndexBack2 += bigWidth;
            //superPixelIndexBack2 += (bigWidth + bigWidth + bigWidth + bigWidth);
        }
        System.out.println("mmmmmmmmmmm   时间间隔2：" + (System.currentTimeMillis() - tempTime));
        return null;
    }

    /**
     * 同时校验多张图
     *
     * @param bitmapBig
     * @param bitmapSmallList
     * @param offset
     * @return
     */
    public synchronized static Rect imageCompare(Bitmap bitmapBig, List<Bitmap> bitmapSmallList, int offset) {
        long tempTime = System.currentTimeMillis();
        int bigWidth = bitmapBig.getWidth();
        int bigHeight = bitmapBig.getHeight();
        int[] bigArray = new int[bigWidth * bigHeight];
        bitmapBig.getPixels(bigArray, 0, bigWidth, 0, 0, bigWidth, bigHeight);
        for (Bitmap bitmapSmall : bitmapSmallList) {
            int smallWidth = bitmapSmall.getWidth();
            int smallHeight = bitmapSmall.getHeight();
            int[] smallArray = new int[smallWidth * smallHeight];
            bitmapSmall.getPixels(smallArray, 0, smallWidth, 0, 0, smallWidth, smallHeight);
            int forWidth = (bigWidth - smallWidth);
            int forHeight = (bigHeight - smallHeight);
            int forSmallWidth = smallWidth / 2;
            int forSmallHeight = smallHeight / 2;
            int superPixelIndexBack2 = 0;
            for (int i = 0; i < forHeight; i += ScreenShotFb.IMAGE_IDENTIFY_RATIO) {
                int superPixelIndexBack = superPixelIndexBack2;
                for (int j = 0; j < forWidth; j++) {
                    //int superPixelIndexBack = bigWidth*i+j;
                    boolean compareState = false;
                    int pixelIndex2 = 0;
                    int superPixelIndex2 = superPixelIndexBack;
                    for (int m = 0; m < forSmallHeight; m += ScreenShotFb.IMAGE_IDENTIFY_RATIO) {
                        int pixelIndex = pixelIndex2;//m * smallWidth;
                        int superPixelIndex = superPixelIndex2;//superPixelIndexBack + m * bigWidth;
                        for (int n = 0; n < forSmallWidth; n += ScreenShotFb.IMAGE_IDENTIFY_RATIO) {
                            int bigColor = bigArray[superPixelIndex];
                            int smallColor = smallArray[pixelIndex];
                           /* if (bigColor != smallColor){
                                compareState = true;
                                break;
                            }*/
                            int offset_R = ((bigColor >> 16) & 0xFF) - ((smallColor >> 16) & 0xFF);
                            //Color.red(bigArray[superPixelIndex]) - Color.red(smallArray[pixelIndex]);
                            int offset_G = ((bigColor >> 8) & 0xFF) - ((smallColor >> 8) & 0xFF);//Color.green(bigArray[superPixelIndex]) - Color.green(smallArray[pixelIndex]);
                            int offset_B = (bigColor & 0xFF) - (smallColor & 0xFF);//Color.blue(bigArray[superPixelIndex]) - Color.blue(smallArray[pixelIndex]);
                            if (offset_R < -offset || offset_R > offset
                                    || offset_G < -offset || offset_G > offset
                                    || offset_B < -offset || offset_B > offset) {
                                compareState = true;
                                break;
                            }
                            pixelIndex += ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                            superPixelIndex += ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                        }
                        pixelIndex2 += (smallWidth + smallWidth + smallWidth + smallWidth);
                        superPixelIndex2 += (bigWidth + bigWidth + bigWidth + bigWidth);
                        if (compareState) {
                            break;
                        }
                    }
                    if (!compareState) {
                        System.out.println("mmmmmmmmmmm   时间间隔2：" + (System.currentTimeMillis() - tempTime));
                        if (smallHeight > 0 && smallWidth > 0) {
                            System.out.println("mmmmmmmmmmm，smallWidth：" + smallWidth);
                            Rect rect = new Rect(j * ScreenShotFb.IMAGE_SCALE_RATIO, i * ScreenShotFb.IMAGE_SCALE_RATIO, (smallWidth + j) * ScreenShotFb.IMAGE_SCALE_RATIO, (smallHeight + i) * ScreenShotFb.IMAGE_SCALE_RATIO);
                            return rect;
                        }
                    }
                    superPixelIndexBack++;
                }
                superPixelIndexBack2 += (bigWidth + bigWidth + bigWidth + bigWidth);
            }
        }
        System.out.println("mmmmmmmmmmm   时间间隔2：" + (System.currentTimeMillis() - tempTime));
        return null;
    }

    public synchronized static Rect imageCompareTwo(Bitmap bitmapBig, List<Bitmap> bitmapSmallList, int offset) {
        return null;
    }


}

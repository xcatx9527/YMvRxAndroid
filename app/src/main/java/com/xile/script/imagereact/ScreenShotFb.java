package com.xile.script.imagereact;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * created by zxf
 */
public class ScreenShotFb {

    public static final int IMAGE_IDENTIFY_RATIO = 4;//图像识别跳跃行数
    public static final int IMAGE_SCALE_RATIO = 2; //图片保存缩放比
    private static final String LOG_TAG = "------->";
    private final String TAG = ScreenShotFb.class.getName();
    private MediaProjection mMediaProjection = null;
    private VirtualDisplay mVirtualDisplay = null;
    private WindowManager mWindowManager = null;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private ImageReader mImageReader = null;
    private DisplayMetrics metrics = null;
    private int mScreenDensity = 0;
    private Bitmap bitmap = null;
    private static Context mContext;
    private int result;  //截图相关
    private Intent intent;
    private MediaProjectionManager mMediaProjectionManager;  //截图相关

    private int displyCount = 0;

    private ScreenShotFb() {

    }

    public static ScreenShotFb getInstance(Context context) {
        mContext = context;
        return SingleF.instance;
    }

    //在第一次被引用时被加载
    static class SingleF {
        private static ScreenShotFb instance = new ScreenShotFb();
    }

    public int getResult() {
        return result;
    }

    public Intent getIntent() {
        return intent;
    }

    public MediaProjectionManager getMediaProjectionManager() {
        return mMediaProjectionManager;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setIntent(Intent intent1) {
        this.intent = intent1;
    }

    public void setMediaProjectionManager(MediaProjectionManager mMediaProjectionManager) {
        this.mMediaProjectionManager = mMediaProjectionManager;
    }


    public void createVirtualEnvironment() {
        if (mImageReader == null) {
            mMediaProjectionManager = (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            metrics = new DisplayMetrics();
            Display display = mWindowManager.getDefaultDisplay();
            display.getRealMetrics(metrics);
            if (Build.VERSION.SDK_INT >= 23) {
                if (ScreenConfigBcReceiver.getScreenOrientation(mContext) == Configuration.ORIENTATION_PORTRAIT) {
                    windowWidth = display.getMode().getPhysicalWidth();
                    windowHeight = display.getMode().getPhysicalHeight();
                } else if (ScreenConfigBcReceiver.getScreenOrientation(mContext) == Configuration.ORIENTATION_LANDSCAPE) {
                    windowWidth = display.getMode().getPhysicalHeight();
                    windowHeight = display.getMode().getPhysicalWidth();
                }
            } else {
                windowWidth = metrics.widthPixels;
                windowHeight = metrics.heightPixels;
                /*int visualKeyHeight = VisualKeyUtil.getNavigationBarHeight(mContext);
                windowHeight += visualKeyHeight;*/
            }
            mScreenDensity = metrics.densityDpi;
            mImageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 2); //ImageFormat.RGB_565
        }
    }

    public void startVirtual() {
        if (ScreenConfigBcReceiver.screenHasChanged) {
            ScreenConfigBcReceiver.screenHasChanged = false;
            closeObject();
            return;
        }

        createVirtualEnvironment();
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    public void setUpMediaProjection() {
        if (mMediaProjectionManager != null) {
            mMediaProjection = mMediaProjectionManager.getMediaProjection(result, intent);
        }
        Log.i(TAG, "mMediaProjection defined");
    }

    private void virtualDisplay() {
        if (mVirtualDisplay == null && mMediaProjection != null && mImageReader != null) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    windowWidth, windowHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
            Log.i(TAG, "virtual displayed");
        }
    }

    /**
     * 截图操作
     *
     * @return
     */
    public synchronized Bitmap startCapture() {
//        displyCount++;
        bitmap = null;
        if (mImageReader != null) {
            Image image = mImageReader.acquireLatestImage();
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();

                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;

                int realWidth = width + rowPadding / pixelStride;
                byte[] mBytesTmp = new byte[width * height / (IMAGE_SCALE_RATIO * IMAGE_SCALE_RATIO) * 4 + 4];
                for (int i = 0; i < height; i += IMAGE_SCALE_RATIO) {
                    int offset_p = i / IMAGE_SCALE_RATIO * width / IMAGE_SCALE_RATIO * 4;
                    for (int j = 0; j < width * 4; j += IMAGE_SCALE_RATIO * 4) {
                        mBytesTmp[offset_p + j / IMAGE_SCALE_RATIO] = buffer.get(i * realWidth * 4 + j);
                        mBytesTmp[offset_p + j / IMAGE_SCALE_RATIO + 1] = buffer.get(i * realWidth * 4 + j + 1);
                        mBytesTmp[offset_p + j / IMAGE_SCALE_RATIO + 2] = buffer.get(i * realWidth * 4 + j + 2);
                        mBytesTmp[offset_p + j / IMAGE_SCALE_RATIO + 3] = buffer.get(i * realWidth * 4 + j + 3);
                    }
                }
                ByteBuffer mBufferTmp = ByteBuffer.wrap(mBytesTmp);
                bitmap = Bitmap.createBitmap(width / IMAGE_SCALE_RATIO, height / IMAGE_SCALE_RATIO, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(mBufferTmp);
                if (image != null) {
                    image.close();
                    image = null;
                }
                if (mBufferTmp != null) {
                    mBufferTmp = null;
                }
                if (buffer != null) {
                    buffer = null;
                }
//                if (displyCount > 50){
//                    displyCount = 0;
//                if (mImageReader != null) {
//                    mImageReader = null;
//                }
//                tearDownMediaProjection();
                stopVirtual();
//                }

            } else {
                return null;
            }
            Log.i(TAG, "image data captured");
        }
        if (bitmap == null){
            if (mImageReader != null) {
                mImageReader = null;
            }
            tearDownMediaProjection();
            stopVirtual();
        }

        return bitmap;
    }

    /**
     * 截图操作
     *
     * @return
     */
    public synchronized Bitmap startCapture(int scale) {
        bitmap = null;
        if (mImageReader != null) {
            Image image = mImageReader.acquireLatestImage();
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();

                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;

                int realWidth = width + rowPadding / pixelStride;
                byte[] mBytesTmp = new byte[width * height / (scale * scale) * 4 + 4];
                for (int i = 0; i < height; i += scale) {
                    int offset_p = i / scale * width / scale * 4;
                    for (int j = 0; j < width * 4; j += scale * 4) {
                        mBytesTmp[offset_p + j / scale] = buffer.get(i * realWidth * 4 + j);
                        mBytesTmp[offset_p + j / scale + 1] = buffer.get(i * realWidth * 4 + j + 1);
                        mBytesTmp[offset_p + j / scale + 2] = buffer.get(i * realWidth * 4 + j + 2);
                        mBytesTmp[offset_p + j / scale + 3] = buffer.get(i * realWidth * 4 + j + 3);
                    }
                }
                ByteBuffer mBufferTmp = ByteBuffer.wrap(mBytesTmp);
                bitmap = Bitmap.createBitmap(width / scale, height / scale, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(mBufferTmp);
                if (image != null) {
                    image.close();
                    image = null;
                }
                if (mBufferTmp != null) {
                    mBufferTmp = null;
                }
                if (buffer != null) {
                    buffer = null;
                }
                stopVirtual();
            } else {
                return null;
            }
            Log.i(TAG, "image data captured");
        }
        return bitmap;
    }

    /**
     * 截图操作
     *
     * @return
     */
    public synchronized Bitmap startCapture_clear() {
        Bitmap bitmap = null;
        if (mImageReader != null) {
            Image image = mImageReader.acquireLatestImage();
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
                bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);


                if (image != null) {
                    image.close();
                    image = null;
                }
//                if (displyCount > 50){
//                    displyCount = 0;
//                    if (mImageReader != null) {
//                        mImageReader = null;
//                    }
//                tearDownMediaProjection();
                stopVirtual();
//                }

            } else {
                return null;
            }
            Log.i(TAG, "image data captured");
        }
        if (bitmap == null){
            if (mImageReader != null) {
                mImageReader = null;
            }
            tearDownMediaProjection();
            stopVirtual();
        }
        return bitmap;
    }

    /**
     * 截图保存
     *
     * @param bitmap
     * @param imagePath
     * @param needJump  是否需要跳转
     * @return
     */
    public boolean saveBitmap(Bitmap bitmap, String imagePath, Bitmap.CompressFormat imageType, boolean needJump, Class<?> clazz) {
        Log.i(TAG, "saveBitmap-------------------screen image start");
        if (bitmap != null) {
            saveBitmap(bitmap, imagePath, imageType);
            if (needJump) {
                Intent intent = new Intent(mContext, clazz);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        Log.i(TAG, "mMediaProjection undefined");
    }

    /**
     * 虚拟展示的停止
     */
    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
        Log.i(TAG, "virtual display stopped");
    }

    public void closeObject() {
        if (mImageReader != null) {
            mImageReader = null;
        }
        tearDownMediaProjection();
        stopVirtual();
        System.gc();

    }

    /**
     * @param scale 放缩
     * @param x     裁剪x
     * @param y
     * @param endx
     * @param endy
     * @return
     */
    public Bitmap startCaptureX(float scale, int x, int y, int endx, int endy) {
        Bitmap bitmap = null;
        Image image = null;
        if (mImageReader != null) {
            try {

                image = mImageReader.acquireLatestImage();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
            if (image != null) {
                int width = endx - x;
                int height = endy - y;
                if (endx < 0) {
                    width = image.getWidth();
                }
                if (endy < 0) {
                    height = image.getHeight();
                }
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * image.getWidth();
                bitmap = Bitmap.createBitmap(image.getWidth() + rowPadding / pixelStride, image.getHeight(), Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, x, y, width, height, matrix, true);
                if (image != null) {
                    image.close();
                    image = null;
                }
            } else {
                Log.e(LOG_TAG, "截图失败");
                return null;
            }
            Log.i(LOG_TAG, "截图成功");
        }
        return bitmap;
    }

    public Bitmap startCaptureX() {
        return startCaptureX(0.5f, 0, 0, -1, -1);
    }


    /**
     * 图片保存
     *
     * @param bitmap    //图片资源
     * @param imagePath //图片路径
     * @param imageType //图片类型
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String imagePath, Bitmap.CompressFormat imageType) {

        if (bitmap != null) {
            FileOutputStream out = null;
            try {
                File fileImage = new File(imagePath);
                File parent = new File(fileImage.getParent());
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                fileImage.createNewFile();
                out = new FileOutputStream(fileImage);
                if (out != null) {//
                    bitmap.compress(imageType, 100, out);
                    out.flush();
                    out.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


}

package com.xile.script.utils.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xile.script.config.Constants;
import com.xile.script.bean.PopupInfo;
import com.xile.script.utils.script.PopupUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/26.
 */

public class ImageUtil {

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
            FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_TEMP);
            FileHelper.makeRootDirectory(Constants.SCRIPT_PLAY_CAPTURE_TEMP);
            FileHelper.makeRootDirectory(Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH);
            FileHelper.makeRootDirectory(Constants.SCRIPT_TEMP_ALERT_CAPTURE_PATH);
            FileHelper.makeRootDirectory(Constants.SCRIPT_TEMP_ALERT_CLEAR_CAPTURE_PATH);
            FileOutputStream out = null;
            try {
                File fileImage = new File(imagePath);
                FileHelper.makeRootDirectory(fileImage.getParent());
                fileImage.createNewFile();
                out = new FileOutputStream(fileImage);
                if (out != null) {//
                    bitmap.compress(imageType, 40, out);
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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * 本地多张图片转换为Bitmap集合
     *
     * @param imgParentPath 存放图片的文件夹路径
     * @param needPicName   是否需要图片的名字(根据此flag获取bitmap集合或PopupInfo集合)
     * @return BitmapList
     */
    public static Vector<?> convertImages2BitmapList(String imgParentPath, boolean needPicName) {
        Vector<Object> objects = new Vector<>();
        if ((new File(imgParentPath)).isDirectory()) {
            File[] files = (new File(imgParentPath)).listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (PopupUtil.getAlertNameList().contains(files[i].getName()) || PopupUtil.getPlatformNameList().contains(files[i].getName())
                            || PopupUtil.getPopupNameList().contains(files[i].getName()) || PopupUtil.getCompareNameList().contains(files[i].getName())
                            || PopupUtil.getSampleNameList().contains(files[i].getName()) || PopupUtil.getModuleNameList().contains(files[i].getName())) {
                        Bitmap bitmap = convertImage2Bitmap(files[i].getAbsolutePath());
                        if (bitmap != null) {
                            if (needPicName) {
                                objects.add(new PopupInfo(files[i].getName(), bitmap, null));
                            } else {
                                objects.add(bitmap);
                            }
                        }
                    }
                }
            }
        }
        return objects;
    }


    /**
     * 本地图片转换为Bitmap
     *
     * @param imgPath 图片路径
     * @return Bitmap
     */
    public static Bitmap convertImage2Bitmap(String imgPath) {
        Bitmap bitmap = null;
        if (!StringUtil.isEmpty(imgPath) && new File(imgPath).exists()) {
            bitmap = BitmapFactory.decodeFile(imgPath);
        }
        return bitmap;
    }


}

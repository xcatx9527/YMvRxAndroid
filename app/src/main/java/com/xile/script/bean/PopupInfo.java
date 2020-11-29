package com.xile.script.bean;

import android.graphics.Bitmap;

/**
 * date: 2017/5/4 10:58
 *
 * @scene 弹窗实体类
 */
public class PopupInfo {
    private String name;    //弹窗图片名字
    private Bitmap bitmap;  //弹窗图片
    private String type;    //弹窗类型

    public PopupInfo() {
    }

    public PopupInfo(String name, Bitmap bitmap, String type) {
        this.name = name;
        this.bitmap = bitmap;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PopupInfo{" +
                "name='" + name + '\'' +
                ", bitmap=" + bitmap +
                ", type='" + type + '\'' +
                '}';
    }
}

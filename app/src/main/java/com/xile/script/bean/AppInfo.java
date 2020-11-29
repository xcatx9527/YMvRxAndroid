package com.xile.script.bean;

import android.graphics.drawable.Drawable;

/**
 * date: 2017/4/5 20:07
 *
 * @description  应用信息
 */
public class AppInfo {
    private String packageName; //包名
    private String label;       //名称
    private Drawable icon;      //图标

    public AppInfo() {
    }

    public AppInfo(String packageName, String label, Drawable icon) {
        this.packageName = packageName;
        this.label = label;
        this.icon = icon;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "packageName='" + packageName + '\'' +
                ", label='" + label + '\'' +
                ", icon=" + icon +
                '}';
    }
}

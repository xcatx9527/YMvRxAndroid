package com.xile.script.bean;

import android.graphics.drawable.Drawable;

/**
 * date: 2017/3/28 18:09
 */
public class FeatureInfo {
    private Drawable icon;  //图标
    private String function;  //功能名称

    public FeatureInfo() {
    }

    public FeatureInfo(Drawable icon, String function) {
        this.icon = icon;
        this.function = function;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "FeatureInfo{" +
                "icon=" + icon +
                ", function=" + function +
                '}';
    }
}

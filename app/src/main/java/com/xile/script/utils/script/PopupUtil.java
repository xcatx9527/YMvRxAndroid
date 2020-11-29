package com.xile.script.utils.script;

import com.xile.script.bean.PopupInfo;

import java.util.Vector;

/**
 * date: 2017/5/4 12:37
 *
 * @scene 弹窗处理工具类
 */
public class PopupUtil {
    /**
     * 老虎平台图片识别相关的
     */
    private static Vector<PopupInfo> popupImgsList = new Vector<>();  //待识别的弹框图像集合
    private static Vector<String> alertNameList = new Vector<>();     //报警图片名称集合
    private static Vector<String> platformNameList = new Vector<>();  //平台登录图片名称集合
    private static Vector<String> popupNameList = new Vector<>();     //弹窗图片名称集合
    private static Vector<String> compareNameList = new Vector<>();   //比较类型图片名称集合
    private static Vector<String> sampleNameList = new Vector<>();    //样本类型名称集合
    private static Vector<String> moduleNameList = new Vector<>();    //模板类型图片名称集合

    public static Vector<PopupInfo> getPopupImgsList() {
        return popupImgsList;
    }

    public static void setPopupImgsList(Vector<PopupInfo> popupImgs) {
        PopupUtil.popupImgsList = popupImgs;
    }

    public static Vector<String> getAlertNameList() {
        return alertNameList;
    }

    public static void setAlertNameList(Vector<String> alertNameList) {
        PopupUtil.alertNameList = alertNameList;
    }

    public static Vector<String> getPlatformNameList() {
        return platformNameList;
    }

    public static void setPlatformNameList(Vector<String> platformNameList) {
        PopupUtil.platformNameList = platformNameList;
    }

    public static Vector<String> getPopupNameList() {
        return popupNameList;
    }

    public static void setPopupNameList(Vector<String> popupNameList) {
        PopupUtil.popupNameList = popupNameList;
    }

    public static Vector<String> getCompareNameList() {
        return compareNameList;
    }

    public static void setCompareNameList(Vector<String> compareNameList) {
        PopupUtil.compareNameList = compareNameList;
    }

    public static Vector<String> getSampleNameList() {
        return sampleNameList;
    }

    public static void setSampleNameList(Vector<String> sampleNameList) {
        PopupUtil.sampleNameList = sampleNameList;
    }

    public static Vector<String> getModuleNameList() {
        return moduleNameList;
    }

    public static void setModuleNameList(Vector<String> moduleNameList) {
        PopupUtil.moduleNameList = moduleNameList;
    }

    /**
     * 数据清空
     */
    public static void clear() {
        popupImgsList.clear();
        alertNameList.clear();
        platformNameList.clear();
        compareNameList.clear();
        popupNameList.clear();
        sampleNameList.clear();
        moduleNameList.clear();
    }


}

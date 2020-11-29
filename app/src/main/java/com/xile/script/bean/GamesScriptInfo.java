package com.xile.script.bean;

import java.util.List;

/**
 * date: 2017/5/22 18:23
 *
 * @scene 手游脚本信息
 */
public class GamesScriptInfo {


    /**
     * pics : {"accountPics":["http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/3.png","http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/1.png"],"alertPics":["http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/3.png","http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/1.png","http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/身份.png"],"popupWindowPics":["http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/3.png","http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/1.png","http://sy.xunbaotianxing.com/IBS/SY/ROBOT_SHELL_TEMP/40108/身份.png"]}
     * shell :
     */

    private PicsBean pics;//图片链接
    private String shell;//脚本链接

    public GamesScriptInfo() {
    }

    public GamesScriptInfo(PicsBean pics, String shell) {
        this.pics = pics;
        this.shell = shell;
    }

    public PicsBean getPics() {
        return pics;
    }

    public void setPics(PicsBean pics) {
        this.pics = pics;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public static class PicsBean {
        private List<String> accountPics;
        private List<String> alertPics;
        private List<String> popupWindowPics;
        private List<String> comparePics;

        public List<String> getAccountPics() {
            return accountPics;
        }

        public void setAccountPics(List<String> accountPics) {
            this.accountPics = accountPics;
        }

        public List<String> getAlertPics() {
            return alertPics;
        }

        public void setAlertPics(List<String> alertPics) {
            this.alertPics = alertPics;
        }

        public List<String> getPopupWindowPics() {
            return popupWindowPics;
        }

        public void setPopupWindowPics(List<String> popupWindowPics) {
            this.popupWindowPics = popupWindowPics;
        }

        public List<String> getComparePics() {
            return comparePics;
        }

        public void setComparePics(List<String> comparePics) {
            this.comparePics = comparePics;
        }

        @Override
        public String toString() {
            return "PicsBean{" +
                    "accountPics=" + accountPics +
                    ", alertPics=" + alertPics +
                    ", popupWindowPics=" + popupWindowPics +
                    ", comparePics=" + comparePics +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "GamesScriptInfo{" +
                "pics=" + pics +
                ", shell='" + shell + '\'' +
                '}';
    }
}

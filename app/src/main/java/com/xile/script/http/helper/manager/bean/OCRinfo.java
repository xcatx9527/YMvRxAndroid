package com.xile.script.http.helper.manager.bean;

import java.util.List;

/**
 * date: 2019/4/1 16:10
 */
public class OCRinfo {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 100-200区
         * pointX : 50
         * pointY : 100
         */

        private String name;
        private int pointX;
        private int pointY;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPointX() {
            return pointX;
        }

        public void setPointX(int pointX) {
            this.pointX = pointX;
        }

        public int getPointY() {
            return pointY;
        }

        public void setPointY(int pointY) {
            this.pointY = pointY;
        }
    }
}

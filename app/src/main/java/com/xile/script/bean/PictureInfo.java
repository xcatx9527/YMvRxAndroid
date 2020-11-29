package com.xile.script.bean;

import java.util.List;

/**
 * date: 2017/8/14 19:07
 *
 * @scene 图片库实体类
 */
public class PictureInfo {
    private List<String> imgUrls;//图片下载地址
    private int imgNum;//顺序时当前第几张图片

    public PictureInfo() {
    }

    public PictureInfo(List<String> imgUrls, int imgNum) {
        this.imgUrls = imgUrls;
        this.imgNum = imgNum;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getImgNum() {
        return imgNum;
    }

    public void setImgNum(int imgNum) {
        this.imgNum = imgNum;
    }

    @Override
    public String toString() {
        return "PictureInfo{" +
                "imgUrls=" + imgUrls +
                ", imgNum=" + imgNum +
                '}';
    }
}

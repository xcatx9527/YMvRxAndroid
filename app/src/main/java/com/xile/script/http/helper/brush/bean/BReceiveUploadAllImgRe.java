package com.xile.script.http.helper.brush.bean;

import java.util.List;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 收到所有图片上传完毕回执
 */
public class BReceiveUploadAllImgRe extends BBaseSessionObject {
    private String retCode;
    private String retDesc;
    private List<String> errorImages;

    public BReceiveUploadAllImgRe() {
    }

    public BReceiveUploadAllImgRe(String retCode, String retDesc, List<String> errorImages, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.errorImages = errorImages;
        this.timestamp = timestamp;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public List<String> getErrorImages() {
        return errorImages;
    }

    public void setErrorImages(List<String> errorImages) {
        this.errorImages = errorImages;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveUploadImgRe{" +
                "timestamp=" + timestamp +
                ", retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", errorImages='" + errorImages + '\'' +
                '}';
    }
}

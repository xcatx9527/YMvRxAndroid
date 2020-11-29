package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 收到上传一张图片回执
 */
public class BReceiveUploadImgRe extends BBaseSessionObject{
    private String retCode;
    private String retDesc;
    private String imageName;

    public BReceiveUploadImgRe() {
    }

    public BReceiveUploadImgRe(String retCode, String retDesc, String imageName, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.imageName = imageName;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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
                ", imageName='" + imageName + '\'' +
                '}';
    }
}

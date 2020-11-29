package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 收到请求上传图片回执
 */
public class BReceiveRequestUploadImgRe extends BBaseSessionObject{
    private String retCode;
    private String retDesc;

    public BReceiveRequestUploadImgRe() {
    }

    public BReceiveRequestUploadImgRe(String retCode, String retDesc, String errorType, String errorDesc, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
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


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveAlertRe{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/1 16:27
 *
 * @scene 上传验证码截图响应
 */
public class BReceiveVerifyCodeUploadRe extends BBaseSessionObject {
    private String retCode;
    private String retDesc;
    private String verifyNumber;

    public BReceiveVerifyCodeUploadRe() {
    }

    public BReceiveVerifyCodeUploadRe(String retCode, String retDesc, String verifyNumber, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.verifyNumber = verifyNumber;
        this.timestamp = timestamp;
    }

    public String getRetcode() {
        return retCode;
    }

    public void setRetcode(String retcode) {
        this.retCode = retcode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public String getVerifyNumber() {
        return verifyNumber;
    }

    public void setVerifyNumber(String verifyNumber) {
        this.verifyNumber = verifyNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveVerifyCodeUploadRe{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", verifyNumber='" + verifyNumber + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

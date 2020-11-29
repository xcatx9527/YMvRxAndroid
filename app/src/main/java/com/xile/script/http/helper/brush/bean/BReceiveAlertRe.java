package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 收到警报回执
 */
public class BReceiveAlertRe extends BBaseSessionObject{
    private String retCode;
    private String retDesc;
    private String errorType;
    private String errorDesc;

    public BReceiveAlertRe() {
    }

    public BReceiveAlertRe(String retCode, String retDesc, String errorType, String errorDesc, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.errorType = errorType;
        this.errorDesc = errorDesc;
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

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
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
                ", errorType='" + errorType + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

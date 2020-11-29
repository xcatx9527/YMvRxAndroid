package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 19:42
 *
 * @scene 获取手机短信回执
 */
public class BReceiveMessage extends BBaseSessionObject{
    private String retCode;
    private String retDesc;
    private String message;

    public BReceiveMessage() {
    }

    public BReceiveMessage(String retCode, String retDesc, String message, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveMessage{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

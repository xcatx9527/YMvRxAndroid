package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 14:04
 *
 * @scene 发送手机重启回执
 */
public class BReceivePhoneRebootRe extends BBaseSessionObject {
    private String retCode;
    private String retDesc;

    public BReceivePhoneRebootRe() {
    }

    public BReceivePhoneRebootRe(String retCode, String retDesc, long timestamp) {
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
        return "BReceivePhoneNumRe{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
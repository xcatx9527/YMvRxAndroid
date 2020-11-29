package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 14:04
 *
 * @scene 获取手机号回执
 */
public class BReceivePhoneNumRe extends BBaseSessionObject{
    private String retCode;
    private String retDesc;
    private String phone;

    public BReceivePhoneNumRe() {
    }

    public BReceivePhoneNumRe(String retCode, String retDesc, String phone, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
                ", phone='" + phone + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

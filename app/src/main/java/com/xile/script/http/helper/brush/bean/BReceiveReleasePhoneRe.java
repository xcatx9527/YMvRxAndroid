package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/22 15:32
 *
 * @scene 服务器释放手机号回执
 */
public class BReceiveReleasePhoneRe extends BBaseSessionObject{
    private String retCode;
    private String retDesc;

    public BReceiveReleasePhoneRe() {
    }

    public BReceiveReleasePhoneRe(String retCode, String retDesc, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.timestamp = timestamp;
    }

    public String getRetCode() {
        return retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveReleasePhoneRe{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 20:19
 *
 * @scene 请求获取图片验证码
 */
public class BRequestGetVerifyCode extends BBaseSessionObject{
    private String name;
    private String type;
    private String verifyNumber;

    public BRequestGetVerifyCode() {
    }

    public BRequestGetVerifyCode(String name, String type, String verifyNumber, long timestamp) {
        this.name = name;
        this.type = type;
        this.verifyNumber = verifyNumber;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return "BRequestGetVerifyCode{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", verifyNumber='" + verifyNumber + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

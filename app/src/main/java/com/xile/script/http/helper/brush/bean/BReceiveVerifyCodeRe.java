package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/25 12:01
 *
 * @scene 手机端获取图片验证码回执
 */
public class BReceiveVerifyCodeRe extends BBaseSessionObject{
    private String retCode;
    private String retDesc;
    private String verify;

    public BReceiveVerifyCodeRe() {
    }

    public BReceiveVerifyCodeRe(String retCode, String retDesc, String verify, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.verify = verify;
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

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveVerifyCodeRe{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", verify='" + verify + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

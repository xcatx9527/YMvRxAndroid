package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 19:42
 *
 * @scene 获取身份证信息回执
 */
public class BReceiveIdCardRe extends BBaseSessionObject {
    private String retCode;
    private String retDesc;
    private String realName;
    private String idCardInfo;
    private String gender;

    public BReceiveIdCardRe() {
    }

    public BReceiveIdCardRe(String retCode, String retDesc, String realName, String idCardInfo, String gender, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.realName = realName;
        this.idCardInfo = idCardInfo;
        this.timestamp = timestamp;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardInfo() {
        return idCardInfo;
    }

    public void setIdCardInfo(String idCardInfo) {
        this.idCardInfo = idCardInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveIdCardRe{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", realName='" + realName + '\'' +
                ", idCardInfo='" + idCardInfo + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

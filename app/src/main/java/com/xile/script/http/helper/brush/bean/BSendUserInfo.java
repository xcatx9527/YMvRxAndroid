package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 17:48
 *
 * @scene 更新用户信息
 */
public class BSendUserInfo extends BBaseSessionObject {
    private String name;
    private String type;
    private String userId;
    private String account;
    private String passWord;
    private String phoneName;
    private String realName;
    private String idCardInfo;
    private String phoneNumber;

    public BSendUserInfo() {
    }

    public BSendUserInfo(String name, String type, String userId, String account, String passWord, String phoneName, String realName, String idCardInfo, String phoneNumber, long timestamp) {
        this.name = name;
        this.type = type;
        this.userId = userId;
        this.account = account;
        this.passWord = passWord;
        this.phoneName = phoneName;
        this.realName = realName;
        this.idCardInfo = idCardInfo;
        this.phoneNumber = phoneNumber;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendUserInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", account='" + account + '\'' +
                ", passWord='" + passWord + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", realName='" + realName + '\'' +
                ", idCardInfo='" + idCardInfo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

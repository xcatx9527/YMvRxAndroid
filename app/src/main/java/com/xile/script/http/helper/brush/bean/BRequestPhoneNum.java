package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 21:24
 *
 * @scene 发起获取手机号码请求
 */
public class BRequestPhoneNum extends BBaseSessionObject {
    private String userId;
    private String name;
    private String type;
    private String channel;
    private String itemId;
    private String phoneType;
    private String notPrefix;
    private String conditionId;
    private String repeat;
    private String mobile;

    public BRequestPhoneNum() {
    }

    public BRequestPhoneNum(String userId, String name, String type, String channel, String itemId, String phoneType, String notPrefix, String mobile, String conditionId, String repeat, long timestamp) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.channel = channel;
        this.itemId = itemId;
        this.phoneType = phoneType;
        this.notPrefix = notPrefix;
        this.mobile = mobile;
        this.conditionId = conditionId;
        this.repeat = repeat;
        this.timestamp = timestamp;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNotPrefix() {
        return notPrefix;
    }

    public void setNotPrefix(String notPrefix) {
        this.notPrefix = notPrefix;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BRequestPhoneNum{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", channel='" + channel + '\'' +
                ", itemId='" + itemId + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ", notPrefix='" + notPrefix + '\'' +
                ", phoneNumber='" + mobile + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", repeat='" + repeat + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

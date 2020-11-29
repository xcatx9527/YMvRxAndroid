package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/22 15:29
 *
 * @scene 释放手机号
 */
public class BSendReleasePhoneNum extends BBaseSessionObject{
    private String name;
    private String type;
    private String channel;
    private String phoneList;
    private String phone;
    private String itemId;

    public BSendReleasePhoneNum() {
    }

    public BSendReleasePhoneNum(String name, String type, String channel, String phoneList, String phone, long timestamp,String itemId) {
        this.name = name;
        this.type = type;
        this.channel = channel;
        this.phoneList = phoneList;
        this.phone = phone;
        this.timestamp = timestamp;
        this.itemId = itemId;
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

    public String getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(String phoneList) {
        this.phoneList = phoneList;
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

    public String getItmeId() {
        return itemId;
    }

    public void setItmeId(String itemId) {
        this.itemId = itemId;
    }


    @Override
    public String toString() {
        return "BSendReleasePhoneNum{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", channel='" + channel + '\'' +
                ", phoneList='" + phoneList + '\'' +
                ", phone='" + phone + '\'' +
                ", timestamp=" + timestamp +'\'' +
                ", itemId=" + itemId +
                '}';
    }
}

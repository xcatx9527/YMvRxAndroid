package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 21:24
 *
 * @scene 发起获取手机短信请求
 */
public class BRequestMessage extends BBaseSessionObject{
    private String name;
    private String type;
    private String channel;
    private String phone;
    private String itemId;
    private String conditionId;

    public BRequestMessage() {
    }

    public BRequestMessage(String name, String type, String channel, String phone, String itemId, String conditionId, long timestamp) {
        this.name = name;
        this.type = type;
        this.channel = channel;
        this.phone = phone;
        this.itemId = itemId;
        this.conditionId = conditionId;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BRequestMessage{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", channel='" + channel + '\'' +
                ", phone='" + phone + '\'' +
                ", itemId='" + itemId + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

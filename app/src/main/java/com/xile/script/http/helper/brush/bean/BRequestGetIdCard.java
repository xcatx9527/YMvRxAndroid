package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 20:19
 *
 * @scene 请求获取身份证信息
 */
public class BRequestGetIdCard extends BBaseSessionObject{
    private String name;
    private String type;
    private String conditionId;
    private String gender;

    public BRequestGetIdCard() {
    }

    public BRequestGetIdCard(String name, String type, String conditionId,String gender, long timestamp) {
        this.name = name;
        this.type = type;
        this.conditionId = conditionId;
        this.gender = gender;
        this.timestamp = timestamp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
        return "BRequestGetIdCard{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

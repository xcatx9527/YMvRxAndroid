package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 20:19
 *
 * @scene 请求获取远程协助指令
 */
public class BRequestGetAssistanceCmd extends BBaseSessionObject{
    private String name;
    private String type;
    private String id;

    public BRequestGetAssistanceCmd() {
    }

    public BRequestGetAssistanceCmd(String name, String type, String id, long timestamp) {
        this.name = name;
        this.type = type;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BRequestGetAssistanceCmd{" +
                "timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

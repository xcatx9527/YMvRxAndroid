package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 10:26
 *
 * @scene 模块执行结果
 */
public class BSendModuleResult extends BBaseSessionObject {
    private String name;
    private String type;
    private String userId;
    private String module;

    public BSendModuleResult() {
    }

    public BSendModuleResult(String name, String type, String userId, String module, long timestamp) {
        this.name = name;
        this.type = type;
        this.userId = userId;
        this.module = module;
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendModuleResult{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", module='" + module + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

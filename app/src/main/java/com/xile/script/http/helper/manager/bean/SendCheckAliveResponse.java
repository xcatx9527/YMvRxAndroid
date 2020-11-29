package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 回应检测心跳
 */
public class SendCheckAliveResponse {
    private String name;
    private String type;
    private String robotName;

    public SendCheckAliveResponse() {
    }

    public SendCheckAliveResponse(String name, String type, String robotName) {
        this.name = name;
        this.type = type;
        this.robotName = robotName;
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

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    @Override
    public String toString() {
        return "SendCheckAliveResponse{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", robotName='" + robotName + '\'' +
                '}';
    }
}

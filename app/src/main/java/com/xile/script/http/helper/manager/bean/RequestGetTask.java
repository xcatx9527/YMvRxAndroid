package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 15:01
 *
 * @scene 请求获取订单任务
 */
public class RequestGetTask extends BaseSessionObject {
    private String name;
    private String type;
    private String systemType;
    private String robotName;

    public RequestGetTask() {
    }

    public RequestGetTask(String name, String type, String systemType, String robotName, String timeStamp) {
        this.name = name;
        this.type = type;
        this.systemType = systemType;
        this.robotName = robotName;
        this.timeStamp = timeStamp;
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

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "RequestGetTask{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", systemType='" + systemType + '\'' +
                ", robotName='" + robotName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

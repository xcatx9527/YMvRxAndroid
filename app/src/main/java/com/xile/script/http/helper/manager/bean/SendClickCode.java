package com.xile.script.http.helper.manager.bean;

/**
 * date: 2019/4/1 15:01
 *
 * @scene 客户端告诉服务器点击了获取验证码
 */
public class SendClickCode extends BaseSessionObject {
    private String name;
    private String type;
    private String systemType;
    private String taskType;
    private String taskData;
    private String robotName;

    public SendClickCode() {
    }

    public SendClickCode(String name, String type, String systemType, String taskType, String taskData, String robotName, String timeStamp) {
        this.name = name;
        this.type = type;
        this.systemType = systemType;
        this.taskType = taskType;
        this.taskData = taskData;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskData() {
        return taskData;
    }

    public void setTaskData(String taskData) {
        this.taskData = taskData;
    }


    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    @Override
    public String toString() {
        return "RequestGetOcrResult{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", systemType='" + systemType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

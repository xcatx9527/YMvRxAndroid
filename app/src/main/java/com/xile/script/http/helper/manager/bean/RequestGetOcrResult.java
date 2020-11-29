package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 15:01
 *
 * @scene 客户端询问服务器某个任务的结果
 * <!-- 1(识别大区服) 2(识别小区服) 3(识别角色等级) 4(获取手机验证码) -->
 */
public class RequestGetOcrResult extends BaseSessionObject {
    private String name;
    private String type;
    private String systemType;
    private String taskType;
    private String taskData;
    private String askType;
    private String robotName;

    public RequestGetOcrResult() {
    }

    public RequestGetOcrResult(String name, String type, String systemType, String taskType, String taskData, String askType, String robotName, String timeStamp) {
        this.name = name;
        this.type = type;
        this.systemType = systemType;
        this.taskType = taskType;
        this.taskData = taskData;
        this.askType = askType;
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

    public String getAskType() {
        return askType;
    }

    public void setAskType(String askType) {
        this.askType = askType;
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
                ", askType='" + askType + '\'' +
                ", robotName='" + robotName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

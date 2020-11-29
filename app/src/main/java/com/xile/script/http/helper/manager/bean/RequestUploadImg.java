package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:10
 *
 * @scene 请求上传图片
 */
public class RequestUploadImg extends BaseSessionObject {
    private String name;
    private String type;
    private String systemType;
    private String taskType;
    private String taskData;
    private String robotName;
    private String imageNames;

    public RequestUploadImg() {
    }

    public RequestUploadImg(String name, String type, String systemType, String taskType, String taskData, String robotName, String imageNames, String timeStamp) {
        this.name = name;
        this.type = type;
        this.systemType = systemType;
        this.taskType = taskType;
        this.taskData = taskData;
        this.robotName = robotName;
        this.imageNames = imageNames;
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

    public String getImageNames() {
        return imageNames;
    }

    public void setImageNames(String imageNames) {
        this.imageNames = imageNames;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "RequestUploadImg{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", systemType='" + systemType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                ", imageNames='" + imageNames + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

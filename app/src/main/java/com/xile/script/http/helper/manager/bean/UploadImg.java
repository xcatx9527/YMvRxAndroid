package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:27
 *
 * @scene 上传图片
 */
public class UploadImg extends BaseSessionObject {
    private String name;
    private String type;
    private String systemType;
    private String taskType;
    private String taskData;
    private String robotName;
    private String imageName;
    private String imageData;

    public UploadImg() {
    }

    public UploadImg(String name, String type, String systemType, String taskType, String taskData, String robotName, String timeStamp, String imageName, String imageData) {
        this.name = name;
        this.type = type;
        this.systemType = systemType;
        this.taskType = taskType;
        this.taskData = taskData;
        this.robotName = robotName;
        this.timeStamp = timeStamp;
        this.imageName = imageName;
        this.imageData = imageData;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "UploadImg{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", systemType='" + systemType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageData='" + imageData + '\'' +
                '}';
    }
}

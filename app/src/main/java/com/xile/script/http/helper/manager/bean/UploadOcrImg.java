package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:10
 *
 * @scene 客户端发送一张带类型的图片(区服识别 / 角色识别等)
 */
public class UploadOcrImg extends BaseSessionObject {
    private String name;
    private String type;
    private String systemType;
    private String taskType;
    private String taskData;
    private String robotName;
    private String imageName;
    private String imgType;
    private String imageData;

    public UploadOcrImg() {
    }

    public UploadOcrImg(String name, String type, String systemType, String taskType, String taskData, String robotName, String imageName, String imgType, String imageData, String timeStamp) {
        this.name = name;
        this.type = type;
        this.systemType = systemType;
        this.taskType = taskType;
        this.taskData = taskData;
        this.robotName = robotName;
        this.imageName = imageName;
        this.imgType = imgType;
        this.imageData = imageData;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }


    @Override
    public String toString() {
        return "UploadOcrImg{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", systemType='" + systemType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imgType='" + imgType + '\'' +
                ", imageData='" + imageData + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

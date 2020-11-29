package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:59
 *
 * @scene  发送截图状态响应
 */
public class SendUploadAllFinishedResponse extends BaseSessionObject {
    private String retcode;
    private String systemType;
    private String taskType;
    private String taskData;
    private String robotName;
    private String errorImages;

    public SendUploadAllFinishedResponse() {
    }

    public SendUploadAllFinishedResponse(String retcode, String systemType, String taskType, String taskData, String robotName, String errorImages, String timeStamp) {
        this.retcode = retcode;
        this.systemType = systemType;
        this.taskType = taskType;
        this.taskData = taskData;
        this.robotName = robotName;
        this.errorImages = errorImages;
        this.timeStamp = timeStamp;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
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

    public String getErrorImages() {
        return errorImages;
    }

    public void setErrorImages(String errorImages) {
        this.errorImages = errorImages;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "SendUploadAllFinishedResponse{" +
                "retcode='" + retcode + '\'' +
                ", systemType='" + systemType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                ", errorImages='" + errorImages + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

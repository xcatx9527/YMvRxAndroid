package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:27
 *
 * @scene 上传图片响应
 */
public class UploadImgResponse extends BaseSessionObject {
    private String retcode;
    private String taskType;
    private String taskData;
    private String imageName;

    public UploadImgResponse() {
    }

    public UploadImgResponse(String retcode, String taskType, String taskData, String imageName, String timeStamp) {
        this.retcode = retcode;
        this.taskType = taskType;
        this.taskData = taskData;
        this.imageName = imageName;
        this.timeStamp = timeStamp;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "UploadImgResponse{" +
                "retcode='" + retcode + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", imageName='" + imageName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

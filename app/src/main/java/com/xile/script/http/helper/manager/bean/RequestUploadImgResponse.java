package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:42
 *
 * @scene 请求上传图片响应
 */
public class RequestUploadImgResponse extends BaseSessionObject {
    private String retcode;
    private String taskType;
    private String taskData;

    public RequestUploadImgResponse() {
    }

    public RequestUploadImgResponse(String retcode, String taskType, String taskData, String timeStamp) {
        this.retcode = retcode;
        this.taskType = taskType;
        this.taskData = taskData;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "RequestUploadImgResponse{" +
                "retcode='" + retcode + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

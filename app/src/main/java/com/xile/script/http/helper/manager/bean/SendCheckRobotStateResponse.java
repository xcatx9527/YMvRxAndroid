package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 18:05
 *
 * @scene 应答检测机器人状态消息
 */
public class SendCheckRobotStateResponse {
    private String name;
    private String type;
    private String retcode;
    private String taskType;
    private String taskData;
    private String errorType;
    private String errorDesc;

    public SendCheckRobotStateResponse() {
    }

    public SendCheckRobotStateResponse(String name, String type, String retcode, String taskType, String taskData, String errorType, String errorDesc) {
        this.name = name;
        this.type = type;
        this.retcode = retcode;
        this.taskType = taskType;
        this.taskData = taskData;
        this.errorType = errorType;
        this.errorDesc = errorDesc;
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

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    @Override
    public String toString() {
        return "SendCheckRobotStateResponse{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", retcode='" + retcode + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", errorType='" + errorType + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                '}';
    }
}

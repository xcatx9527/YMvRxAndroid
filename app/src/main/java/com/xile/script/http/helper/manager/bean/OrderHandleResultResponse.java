package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:59
 *
 * @scene  发送截图结果响应
 */
public class OrderHandleResultResponse extends BaseSessionObject {
    private String retcode;
    private String systemType;
    private String taskType;
    private String taskData;
    private String robotName;
    private String errorType;
    private String errorDesc;

    public OrderHandleResultResponse() {
    }

    public OrderHandleResultResponse(String retcode, String systemType, String taskType, String taskData, String robotName, String errorType, String errorDesc, String timeStamp) {
        this.retcode = retcode;
        this.systemType = systemType;
        this.taskType = taskType;
        this.taskData = taskData;
        this.robotName = robotName;
        this.errorType = errorType;
        this.errorDesc = errorDesc;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "OrderHandleResultResponse{" +
                "retcode='" + retcode + '\'' +
                ", systemType='" + systemType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                ", errorType='" + errorType + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

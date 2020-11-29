package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:42
 *
 * @scene 服务器应答点击获取验证码
 */
public class SendClickCodeResponse extends BaseSessionObject {
    private String retcode;
    private String taskType;
    private String taskData;
    private String systemType;
    private String robotName;

    public SendClickCodeResponse() {
    }

    public SendClickCodeResponse(String retcode, String taskType, String taskData, String systemType, String robotName) {
        this.retcode = retcode;
        this.taskType = taskType;
        this.taskData = taskData;
        this.systemType = systemType;
        this.robotName = robotName;
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

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    @Override
    public String toString() {
        return "SendClickCodeResponse{" +
                "retcode='" + retcode + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", systemType='" + systemType + '\'' +
                ", robotName='" + robotName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

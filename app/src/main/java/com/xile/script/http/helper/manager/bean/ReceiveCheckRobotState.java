package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 18:08
 *
 * @scene 接收到询问机器人状态消息
 */
public class ReceiveCheckRobotState {
    private String systemType;
    private String taskType;
    private String taskData;
    private String robotName;

    public ReceiveCheckRobotState() {
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

    @Override
    public String toString() {
        return "ReceiveCheckRobotState{" +
                "systemType='" + systemType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                '}';
    }
}

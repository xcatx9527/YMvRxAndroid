package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:59
 *
 * @scene  发送截图状态
 */
public class SendUploadAllFinished extends BaseSessionObject {
    private String name;
    private String type;
    private String taskType;
    private String taskData;
    private String robotName;

    public SendUploadAllFinished() {
    }

    public SendUploadAllFinished(String name, String type, String taskType, String taskData, String robotName, String timeStamp) {
        this.name = name;
        this.type = type;
        this.taskType = taskType;
        this.taskData = taskData;
        this.robotName = robotName;
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

    @Override
    public String toString() {
        return "SendUploadAllFinished{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", robotName='" + robotName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

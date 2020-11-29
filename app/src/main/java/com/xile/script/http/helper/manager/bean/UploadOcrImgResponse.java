package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:42
 *
 * @scene 服务器端应答发送一张带类型图片成功与否(区服识别 / 角色识别等)
 */
public class UploadOcrImgResponse extends BaseSessionObject {
    private String retcode;
    private String taskType;
    private String taskData;
    private String imageName;

    public UploadOcrImgResponse() {
    }

    public UploadOcrImgResponse(String retcode, String taskType, String taskData, String imageName, String timeStamp) {
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

    @Override
    public String toString() {
        return "UploadOcrImgResponse{" +
                "retcode='" + retcode + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", imageName='" + imageName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

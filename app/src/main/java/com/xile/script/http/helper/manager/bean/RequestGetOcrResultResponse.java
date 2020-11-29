package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/1 16:42
 *
 * @scene 服务器应答某个任务的结果
 */
public class RequestGetOcrResultResponse extends BaseSessionObject {
    private String retcode;
    private String taskType;
    private String askType;
    private String ocrResult;

    public RequestGetOcrResultResponse() {
    }

    public RequestGetOcrResultResponse(String retcode, String taskType, String askType, String ocrResult, String timeStamp) {
        this.retcode = retcode;
        this.taskType = taskType;
        this.askType = askType;
        this.ocrResult = ocrResult;
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

    public String getAskType() {
        return askType;
    }

    public void setAskType(String askType) {
        this.askType = askType;
    }

    public String getOcrResult() {
        return ocrResult;
    }

    public void setOcrResult(String ocrResult) {
        this.ocrResult = ocrResult;
    }


    @Override
    public String toString() {
        return "RequestGetOcrResultResponse{" +
                "retcode='" + retcode + '\'' +
                ", taskType='" + taskType + '\'' +
                ", askType='" + askType + '\'' +
                ", ocrResult='" + ocrResult + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

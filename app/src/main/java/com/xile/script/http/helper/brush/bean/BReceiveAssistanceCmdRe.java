package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/1 16:27
 *
 * @scene 获取远程协助指令响应
 */
public class BReceiveAssistanceCmdRe extends BBaseSessionObject {
    private String retCode;
    private String retDesc;
    private String id;
    private String cmdContent;

    public BReceiveAssistanceCmdRe() {
    }

    public BReceiveAssistanceCmdRe(String retCode, String retDesc, String id, String cmdContent, long timestamp) {
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.retDesc = retDesc;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCmdContent() {
        return cmdContent;
    }

    public void setCmdContent(String cmdContent) {
        this.cmdContent = cmdContent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveAssistanceCmdRe{" +
                "timestamp=" + timestamp +
                ", retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", id='" + id + '\'' +
                ", cmdContent='" + cmdContent + '\'' +
                '}';
    }
}

package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/1 16:27
 *
 * @scene 上传远程协助截图响应
 */
public class BReceiveRemoteAssistanceImgRe extends BBaseSessionObject {
    private String retCode;
    private String retDesc;
    private String id;

    public BReceiveRemoteAssistanceImgRe() {
    }

    public BReceiveRemoteAssistanceImgRe(String retCode, String retDesc, String id, long timestamp) {
        this.retCode = retCode;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveRemoteAssistanceImgRe{" +
                "timestamp=" + timestamp +
                ", retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

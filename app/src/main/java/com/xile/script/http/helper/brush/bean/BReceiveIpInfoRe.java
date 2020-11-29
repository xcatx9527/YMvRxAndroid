package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 19:42
 *
 * @scene 更新IP信息回执
 */
public class BReceiveIpInfoRe extends BBaseSessionObject{
    private int ipFlag;
    private String retCode;
    private String retDesc;

    public BReceiveIpInfoRe() {
    }

    public BReceiveIpInfoRe(int ipFlag,String retCode, String retDesc, long timestamp) {
        this.ipFlag = ipFlag;
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.timestamp = timestamp;
    }

    public int getIpFlag() {
        return ipFlag;
    }

    public void setIpFlag(int ipFlag) {
        this.ipFlag = ipFlag;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveIpInfoRe{" +
                "ipFlag=" + ipFlag +
                ", retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 19:39
 *
 * @scene 更新IP信息
 */
public class BSendIpInfo extends BBaseSessionObject {
    private String name;
    private String type;
    private int ipFlag;
    private String orderId;
    private String remoteIp;

    public BSendIpInfo() {
    }

    public BSendIpInfo(String name, String type, int ipFlag, String orderId, String remoteIp, long timestamp) {
        this.name = name;
        this.type = type;
        this.ipFlag = ipFlag;
        this.orderId = orderId;
        this.remoteIp = remoteIp;
        this.timestamp = timestamp;
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

    public int getIpFlag() {
        return ipFlag;
    }

    public void setIpFlag(int ipFlag) {
        this.ipFlag = ipFlag;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendIpInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ipFlag=" + ipFlag +
                ", orderId='" + orderId + '\'' +
                ", remoteIp='" + remoteIp + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/20 20:40
 *
 * @scene 订单处理结果回执
 */
public class BReceiveOrderResultRe extends BBaseSessionObject{
    private String name;
    private String type;
    private String retCode;
    private String retDesc;
    private int orderStatus;

    public BReceiveOrderResultRe() {
    }

    public BReceiveOrderResultRe(String name, String type, String retCode, String retDesc, int orderStatus, long timestamp) {
        this.name = name;
        this.type = type;
        this.retCode = retCode;
        this.retDesc = retDesc;
        this.orderStatus = orderStatus;
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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BReceiveOrderResultRe{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", orderStatus=" + orderStatus +
                ", timestamp=" + timestamp +
                '}';
    }
}

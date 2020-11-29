package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 11:13
 *
 * @scene 充值提交工号信息
 */
public class BSendJobNumber extends BBaseSessionObject {
    private String name;
    private String type;
    private String phoneName;
    private String orderId;
    private String jobNumber;

    public BSendJobNumber() {
    }

    public BSendJobNumber(String name, String type, String phoneName, String orderId, String jobNumber, long timestamp) {
        this.name = name;
        this.type = type;
        this.phoneName = phoneName;
        this.orderId = orderId;
        this.jobNumber = jobNumber;
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

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Override
    public String toString() {
        return "BSendJobNumber{" +
                "timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                '}';
    }
}

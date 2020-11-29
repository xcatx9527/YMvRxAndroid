package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/20 19:41
 *
 * @scene 发送订单处理结果
 */
public class BSendOrderResult extends BBaseSessionObject {
    private String name;
    private String type;
    private String orderId;
    private String conditionId;
    private String phoneName;
    private String orderStatus;
    private String imageName;
    private String imageData;
    private String errorDesc;

    public BSendOrderResult() {
    }

    public BSendOrderResult(String name, String type, String orderId, String conditionId, String phoneName, String orderStatus, String imageName, String imageData, long timestamp, String errorDesc) {
        this.name = name;
        this.type = type;
        this.orderId = orderId;
        this.conditionId = conditionId;
        this.phoneName = phoneName;
        this.orderStatus = orderStatus;
        this.imageName = imageName;
        this.imageData = imageData;
        this.timestamp = timestamp;
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendOrderResult{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", orderId='" + orderId + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageData='" + imageData + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

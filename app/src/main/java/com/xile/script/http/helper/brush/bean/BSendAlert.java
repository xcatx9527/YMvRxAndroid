package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/22 15:29
 *
 * @scene 客户端发送警报
 */
public class BSendAlert extends BBaseSessionObject {
    private String name;
    private String type;
    private String errorType;
    private String errorDesc;
    private String imageName;
    private String imageData;
    private String phoneName;
    private String orderId;

    public BSendAlert() {
    }

    public BSendAlert(String name, String type, String errorType, String errorDesc, String imageName, String imageData, String phoneName, String orderId, long timestamp) {
        this.name = name;
        this.type = type;
        this.errorType = errorType;
        this.errorDesc = errorDesc;
        this.imageName = imageName;
        this.imageData = imageData;
        this.phoneName = phoneName;
        this.orderId = orderId;
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

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendAlert{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", errorType='" + errorType + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageData='" + imageData + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

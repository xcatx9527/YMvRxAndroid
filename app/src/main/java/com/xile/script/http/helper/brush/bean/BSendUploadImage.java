package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/22 15:29
 *
 * @scene 客户端上传一张图片
 */
public class BSendUploadImage extends BBaseSessionObject {
    private String name;
    private String type;
    private String taskType;
    private String phoneName;
    private String conditionId;
    private String userId;
    private String orderId;
    private String imageName;
    private String imageData;

    public BSendUploadImage() {
    }

    public BSendUploadImage(String name, String type, String taskType, String phoneName, String conditionId, String userId, String orderId, String imageName, String imageData, long timestamp) {
        this.name = name;
        this.type = type;
        this.taskType = taskType;
        this.phoneName = phoneName;
        this.conditionId = conditionId;
        this.userId = userId;
        this.orderId = orderId;
        this.imageName = imageName;
        this.imageData = imageData;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
        return "BSendUploadImage{" +
                "timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", taskType='" + taskType + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageData='" + imageData + '\'' +
                '}';
    }
}

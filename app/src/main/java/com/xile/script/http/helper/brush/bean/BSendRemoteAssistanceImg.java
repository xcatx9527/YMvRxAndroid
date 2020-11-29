package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/25 11:50
 *
 * @scene 客户端上传一张远程协助截图
 */
public class BSendRemoteAssistanceImg extends BBaseSessionObject {
    private String name;
    private String type;
    private String conditionId;
    private String gameOS;
    private String deviceName;
    private String imageData;

    public BSendRemoteAssistanceImg() {
    }

    public BSendRemoteAssistanceImg(String name, String type, String conditionId, String gameOS, String deviceName, String imageData, long timestamp) {
        this.name = name;
        this.type = type;
        this.conditionId = conditionId;
        this.gameOS = gameOS;
        this.deviceName = deviceName;
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

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getGameOS() {
        return gameOS;
    }

    public void setGameOS(String gameOS) {
        this.gameOS = gameOS;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
        return "BSendRemoteAssistanceImg{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", gameOS='" + gameOS + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", imageData='" + imageData + '\'' +
                '}';
    }
}

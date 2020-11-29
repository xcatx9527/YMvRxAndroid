package com.xile.script.http.helper.brush.bean;

import java.util.List;

/**
 * date: 2017/11/25 11:50
 *
 * @scene 客户端上传一张验证码截图
 */
public class BSendVerifyCodeImg extends BBaseSessionObject {
    private String name;
    private String type;
    private String conditionId;
    private int verifyType;
    private String imageName;
    private String imageData;
    private List imageNameList;
    private List imageDataList;

    public BSendVerifyCodeImg() {
    }

    public BSendVerifyCodeImg(String name, String type, String conditionId, int verifyType, String imageName, String imageData, List imageNameList, List imageDataList,long timestamp) {
        this.name = name;
        this.type = type;
        this.conditionId = conditionId;
        this.verifyType = verifyType;
        this.imageName = imageName;
        this.imageData = imageData;
        this.imageNameList = imageNameList;
        this.imageDataList = imageDataList;
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

    public int getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(int verifyType) {
        this.verifyType = verifyType;
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

    public List getImageNameList() {
        return imageNameList;
    }

    public void setImageNameList(List imageNameList) {
        this.imageNameList = imageNameList;
    }

    public List getImageDataList() {
        return imageDataList;
    }

    public void setImageDataList(List imageDataList) {
        this.imageDataList = imageDataList;
    }

    @Override
    public String toString() {
        return "BSendVerifyCodeImg{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", verifyType=" + verifyType +
                ", imageName='" + imageName + '\'' +
                ", imageData='" + imageData + '\'' +
                ", imageNameList=" + imageNameList +
                ", imageDataList=" + imageDataList +
                ", timestamp=" + timestamp +
                '}';
    }
}

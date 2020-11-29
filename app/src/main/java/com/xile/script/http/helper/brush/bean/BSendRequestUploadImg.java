package com.xile.script.http.helper.brush.bean;

import java.util.List;

/**
 * date: 2017/7/1 16:10
 *
 * @scene 请求上传图片
 */
public class BSendRequestUploadImg extends BBaseSessionObject {
    private String name;
    private String type;
    private String phoneName;
    private List<String> imageNames;

    public BSendRequestUploadImg() {
    }

    public BSendRequestUploadImg(String name, String type, String phoneName, List<String> imageNames, long timestamp) {
        this.name = name;
        this.type = type;
        this.phoneName = phoneName;
        this.imageNames = imageNames;
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

    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "RequestUploadImg{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", imageNames='" + imageNames + '\'' +
                ", timeStamp='" + timestamp + '\'' +
                '}';
    }
}

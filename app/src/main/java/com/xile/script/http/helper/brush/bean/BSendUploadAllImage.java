package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/22 15:29
 *
 * @scene 客户端发送所有图片上传完毕
 */
public class BSendUploadAllImage extends BBaseSessionObject {
    private String name;
    private String type;
    private String phoneName;

    public BSendUploadAllImage() {
    }

    public BSendUploadAllImage(String name, String type, String phoneName, long timestamp) {
        this.name = name;
        this.type = type;
        this.phoneName = phoneName;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendUploadAllImage{" +
                "timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", phoneName='" + phoneName + '\'' +
                '}';
    }
}

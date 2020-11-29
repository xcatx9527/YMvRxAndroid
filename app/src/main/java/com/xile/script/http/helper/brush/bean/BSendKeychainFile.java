package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/25 11:50
 *
 * @scene 客户端上传Keychain文件
 */
public class BSendKeychainFile extends BBaseSessionObject{
    private String name;
    private String type;
    private String keychainName;
    private String keychainData;
    private String userId;

    public BSendKeychainFile() {
    }

    public BSendKeychainFile(String name, String type, String keychainName, String keychainData, String userId, long timestamp) {
        this.name = name;
        this.type = type;
        this.keychainName = keychainName;
        this.keychainData = keychainData;
        this.userId = userId;
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

    public String getKeychainName() {
        return keychainName;
    }

    public void setKeychainName(String keychainName) {
        this.keychainName = keychainName;
    }

    public String getKeychainData() {
        return keychainData;
    }

    public void setKeychainData(String keychainData) {
        this.keychainData = keychainData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendKeychainFile{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", keychainName='" + keychainName + '\'' +
                ", keychainData='" + keychainData + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

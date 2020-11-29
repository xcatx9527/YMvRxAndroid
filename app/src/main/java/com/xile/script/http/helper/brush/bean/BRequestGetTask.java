package com.xile.script.http.helper.brush.bean;

import org.json.JSONArray;

/**

 * date: 2017/7/1 15:01
 *
 * @scene 请求获取优化订单任务
 */
public class BRequestGetTask extends BBaseSessionObject {
    private String name;
    private String type;
    private String gameOS;
    private String phoneName;
    private JSONArray fileList;
    private JSONArray appList;

    public BRequestGetTask() {
    }

    public BRequestGetTask(String name, String type, String gameOS, String phoneName, JSONArray fileList, JSONArray appList, long timestamp) {
        this.name = name;
        this.type = type;
        this.gameOS = gameOS;
        this.phoneName = phoneName;
        this.fileList = fileList;
        this.appList = appList;
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

    public String getGameOS() {
        return gameOS;
    }

    public void setGameOS(String gameOS) {
        this.gameOS = gameOS;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public JSONArray getFileList() {
        return fileList;
    }

    public void setFileList(JSONArray fileList) {
        this.fileList = fileList;
    }

    public JSONArray getAppList() {
        return appList;
    }

    public void setAppList(JSONArray appList) {
        this.appList = appList;
    }

    @Override
    public String toString() {
        return "BRequestGetTask{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", gameOS='" + gameOS + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", fileList=" + fileList +
                ", appList=" + appList +
                ", timestamp=" + timestamp +
                '}';
    }
}

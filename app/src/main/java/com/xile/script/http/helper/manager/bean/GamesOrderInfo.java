package com.xile.script.http.helper.manager.bean;

import java.io.Serializable;

/**
 * @scene 手游订单实体类
 */

public class GamesOrderInfo extends BaseSessionObject implements Serializable{
    private String retcode;
    private String aid;
    private String gameName;// 游戏名字
    private String gmPassword;// 账号
    private String gmUserName;// 密码
    private String roleLevel;// 角色等级
    private String zoneName;// 小区名字
    private String QName;//  大区名字
    private String zoneNum;//  小区区号
    private String taskType;//  任务类型:订单截图或者寄售截图类型
    private String taskData;//  两种,ordernum或者commonid
    private String channelId;//  脚本渠道ID


    public GamesOrderInfo() {
    }

    public GamesOrderInfo(String retcode, String aid, String gameName, String gmPassword, String gmUserName, String roleLevel, String zoneName, String QName, String zoneNum, String taskType, String taskData, String channelId) {
        this.retcode = retcode;
        this.aid = aid;
        this.gameName = gameName;
        this.gmPassword = gmPassword;
        this.gmUserName = gmUserName;
        this.roleLevel = roleLevel;
        this.zoneName = zoneName;
        this.QName = QName;
        this.zoneNum = zoneNum;
        this.taskType = taskType;
        this.taskData = taskData;
        this.channelId = channelId;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGmPassword() {
        return gmPassword;
    }

    public void setGmPassword(String gmPassword) {
        this.gmPassword = gmPassword;
    }

    public String getGmUserName() {
        return gmUserName;
    }

    public void setGmUserName(String gmUserName) {
        this.gmUserName = gmUserName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getQName() {
        return QName;
    }

    public void setQName(String QName) {
        this.QName = QName;
    }

    public String getZoneNum() {
        return zoneNum;
    }

    public void setZoneNum(String zoneNum) {
        this.zoneNum = zoneNum;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskData() {
        return taskData;
    }

    public void setTaskData(String taskData) {
        this.taskData = taskData;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "GamesOrderInfo{" +
                "retcode='" + retcode + '\'' +
                ", aid='" + aid + '\'' +
                ", gameName='" + gameName + '\'' +
                ", gmPassword='" + gmPassword + '\'' +
                ", gmUserName='" + gmUserName + '\'' +
                ", roleLevel='" + roleLevel + '\'' +
                ", zoneName='" + zoneName + '\'' +
                ", QName='" + QName + '\'' +
                ", zoneNum='" + zoneNum + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskData='" + taskData + '\'' +
                ", channelId='" + channelId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}


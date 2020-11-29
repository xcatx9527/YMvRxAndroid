package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/11/21 11:13
 *
 * @scene 更新设备信息
 */
public class BSendDeviceInfo extends BBaseSessionObject{
    private String name;
    private String type;
    private String userId;
    private String deviceInfo;
    private String loginIpInfo;
    private String serverId;
    private String serverName;
    private String roleName;
    private String roleLevel;
    private String conditionId;
    private String phoneName;

    public BSendDeviceInfo() {
    }

    public BSendDeviceInfo(String name, String type, String userId, String deviceInfo, String loginIpInfo, String serverId, String serverName, String roleName, String roleLevel,String conditionId,String phoneName, long timestamp) {
        this.name = name;
        this.type = type;
        this.userId = userId;
        this.deviceInfo = deviceInfo;
        this.loginIpInfo = loginIpInfo;
        this.serverId = serverId;
        this.serverName = serverName;
        this.roleName = roleName;
        this.roleLevel = roleLevel;
        this.conditionId = conditionId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getLoginIpInfo() {
        return loginIpInfo;
    }

    public void setLoginIpInfo(String loginIpInfo) {
        this.loginIpInfo = loginIpInfo;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BSendDeviceInfo{" +
                "timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", loginIpInfo='" + loginIpInfo + '\'' +
                ", serverId='" + serverId + '\'' +
                ", serverName='" + serverName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleLevel='" + roleLevel + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", phoneName='" + phoneName + '\'' +
                '}';
    }
}

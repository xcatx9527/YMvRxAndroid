package com.xile.script.http.helper.brush.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * date: 2017/5/11 16:00
 *
 * @scene 优化订单
 */
public class BrushOrderInfo extends BBaseSessionObject implements Serializable {


    /**
     * accountImagePathList : []    //图片类型  platform
     * alertImagePathList : []      //图片类型  alert
     * compareImagePathList : []    //图片类型  ifdef
     * durationTime : 活跃时长
     * gameId : 5
     * gameName : 灰烬大陆官服
     * gameOS : Android
     * gamePackageName :   //应用包名
     * gameProvider : 欢动
     * linkScript :脚本链接
     * orderId : 100031
     * orderStatus : 1
     * orderType : 0注册 1登录
     * popUpWindowsImagePathList : []   //图片类型  popup
     * userModel : {"GSOrderList":[{"$ref":"$"}],"lastStartTime":1494487914537,"userId":"110037","validFlag":true}  //游戏用户信息
     * userid : 110037
     */

    private String retCode;//返回码
    private String retDesc;//返回描述
    private String channelType;//脚本类型
    private String conditionId;//项目ID
    private long durationTime;//脚本时长
    private String gameId;//游戏ID
    private String gameName;//游戏名字
    private String gameNameSpell;//游戏名字全拼
    private String gameOS;//游戏平台
    private String gamePackageName;//游戏包名
    private String gameProvider;//游戏厂商
    private String linkScript;//脚本链接
    private String certificate;//证书链接
    private String orderId;//订单号
    private String vpnAccount;//vpn账号
    private String vpnPasswd;//vpn密码
    private String commentary;//单条评论内容
    private long orderStatus;//订单状态
    private long orderType;//订单类型
    private UserModelBean userModel;//游戏信息
    private String userId;//用户ID
    private String desc;//订单备注
    private int encrypt;//脚本加密
    private String hostIp;//VPN服务器地址
    private String secretKey;//VPN预共享密钥
    private String templateThreshold;//阈值
    private int twice;//充值订单是否执行两次以上
    private List<String> keychain; //文件
    private List<String> commentPathList;//评论话库脚本集合
    private List<String> stateScriptList;//Excel脚本集合
    private List<String> commentImagePathList;//论坛发图链接集合
    private List<String> accountImagePathList;//平台图片集合
    private List<String> alertImagePathList;//警报图片集合
    private List<String> compareImagePathList;//比较图片集合
    private List<String> popUpWindowsImagePathList;//弹窗图片集合
    private List<String> sampleImagePathList;//样本集合
    private List<String> payImagePathList;//Excel模板图片集合
    private List<AppEntity> appList;//应用更新集合
    private List<String> templateList;//Excel模板集合
    private Map<String, String> vpnList;//VPN列表
    private int zyzx;
    private int duli;


    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public long getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameNameSpell() {
        return gameNameSpell;
    }

    public void setGameNameSpell(String gameNameSpell) {
        this.gameNameSpell = gameNameSpell;
    }

    public String getGameOS() {
        return gameOS;
    }

    public void setGameOS(String gameOS) {
        this.gameOS = gameOS;
    }

    public String getGamePackageName() {
        return gamePackageName;
    }

    public void setGamePackageName(String gamePackageName) {
        this.gamePackageName = gamePackageName;
    }

    public String getGameProvider() {
        return gameProvider;
    }

    public void setGameProvider(String gameProvider) {
        this.gameProvider = gameProvider;
    }

    public String getLinkScript() {
        return linkScript;
    }

    public void setLinkScript(String linkScript) {
        this.linkScript = linkScript;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String linkCert) {
        this.certificate = linkCert;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVpnAccount() {
        return vpnAccount;
    }

    public void setVpnAccount(String vpnAccount) {
        this.vpnAccount = vpnAccount;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getVpnPasswd() {
        return vpnPasswd;
    }

    public void setVpnPasswd(String vpnPasswd) {
        this.vpnPasswd = vpnPasswd;
    }

    public long getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(long orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getOrderType() {
        return orderType;
    }

    public void setOrderType(long orderType) {
        this.orderType = orderType;
    }

    public UserModelBean getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModelBean userModel) {
        this.userModel = userModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTemplateThreshold() {
        return templateThreshold;
    }

    public void setTemplateThreshold(String templateThreshold) {
        this.templateThreshold = templateThreshold;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTwice() {
        return twice;
    }

    public void setTwice(int twice) {
        this.twice = twice;
    }

    public List<String> getKeychain() {
        return keychain;
    }

    public void setKeychain(List<String> keychain) {
        this.keychain = keychain;
    }

    public List<String> getCommentPathList() {
        return commentPathList;
    }

    public void setCommentPathList(List<String> commentPathList) {
        this.commentPathList = commentPathList;
    }

    public List<String> getStateScriptList() {
        return stateScriptList;
    }

    public void setStateScriptList(List<String> stateScriptList) {
        this.stateScriptList = stateScriptList;
    }

    public List<String> getCommentImagePathList() {
        return commentImagePathList;
    }

    public void setCommentImagePathList(List<String> commentImagePathList) {
        this.commentImagePathList = commentImagePathList;
    }

    public List<String> getAccountImagePathList() {
        return accountImagePathList;
    }

    public void setAccountImagePathList(List<String> accountImagePathList) {
        this.accountImagePathList = accountImagePathList;
    }

    public List<String> getAlertImagePathList() {
        return alertImagePathList;
    }

    public void setAlertImagePathList(List<String> alertImagePathList) {
        this.alertImagePathList = alertImagePathList;
    }

    public List<String> getCompareImagePathList() {
        return compareImagePathList;
    }

    public void setCompareImagePathList(List<String> compareImagePathList) {
        this.compareImagePathList = compareImagePathList;
    }

    public List<String> getPopUpWindowsImagePathList() {
        return popUpWindowsImagePathList;
    }

    public void setPopUpWindowsImagePathList(List<String> popUpWindowsImagePathList) {
        this.popUpWindowsImagePathList = popUpWindowsImagePathList;
    }

    public List<String> getSampleImagePathList() {
        return sampleImagePathList;
    }

    public void setSampleImagePathList(List<String> sampleImagePathList) {
        this.sampleImagePathList = sampleImagePathList;
    }

    public List<String> getPayImagePathList() {
        return payImagePathList;
    }

    public void setPayImagePathList(List<String> payImagePathList) {
        this.payImagePathList = payImagePathList;
    }

    public List<AppEntity> getAppInfoList() {
        return appList;
    }

    public void setAppInfoList(List<AppEntity> appInfoList) {
        this.appList = appInfoList;
    }

    public List<String> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<String> templateList) {
        this.templateList = templateList;
    }

    public Map<String, String> getVpnList() {
        return vpnList;
    }

    public void setVpnList(Map<String, String> vpnList) {
        this.vpnList = vpnList;
    }

    public int getZyzx() {
        return zyzx;
    }

    public void setZyzx(int zyzx) {
        this.zyzx = zyzx;
    }

    public int getDuli() {
        return duli;
    }

    public void setDuli(int duli) {
        this.duli = duli;
    }

    public static class UserModelBean {
        // 用户ID 因为不同产品用户名可能相同，调度中心需要定义一个userid来区分用户
        private String userId;
        // 设备信息
        private String deviceInfo;
        // 登录使用的信息
        private String loginIpInfo;
        // 用户登录账号
        private String account;
        // 用户登录密码
        private String passWord;
        // 用户实名姓名
        private String realName;
        // 用户实名身份证信息
        private String idCardInfo;
        // 用户关联的游戏ID
        private String gameId;
        // 用户角色所在区服ID
        private String serverId;
        // 用户角色所在区服名称
        private String serverName;
        // 用户角色名称
        private String roleName;
        // 用户角色等级
        private String roleLevel;
        //手机号
        private String phoneNumber;


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdCardInfo() {
            return idCardInfo;
        }

        public void setIdCardInfo(String idCardInfo) {
            this.idCardInfo = idCardInfo;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
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

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return "UserModelBean{" +
                    "userId='" + userId + '\'' +
                    ", deviceInfo='" + deviceInfo + '\'' +
                    ", loginIpInfo='" + loginIpInfo + '\'' +
                    ", account='" + account + '\'' +
                    ", passWord='" + passWord + '\'' +
                    ", realName='" + realName + '\'' +
                    ", idCardInfo='" + idCardInfo + '\'' +
                    ", gameId='" + gameId + '\'' +
                    ", serverId='" + serverId + '\'' +
                    ", serverName='" + serverName + '\'' +
                    ", roleName='" + roleName + '\'' +
                    ", roleLevel='" + roleLevel + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }

    public static class AppEntity {
        /**
         * link : http://118.145.5.141:4080/shell/gameApk/script/gameapkfile.apk
         * name : script
         */

        private String link;
        private String name;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "AppInfo{" +
                    "link='" + link + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BrushOrderInfo{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", channelType='" + channelType + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", durationTime=" + durationTime +
                ", gameId='" + gameId + '\'' +
                ", gameName='" + gameName + '\'' +
                ", gameNameSpell='" + gameNameSpell + '\'' +
                ", gameOS='" + gameOS + '\'' +
                ", gamePackageName='" + gamePackageName + '\'' +
                ", gameProvider='" + gameProvider + '\'' +
                ", linkScript='" + linkScript + '\'' +
                ", certificate='" + certificate + '\'' +
                ", orderId='" + orderId + '\'' +
                ", vpnAccount='" + vpnAccount + '\'' +
                ", vpnPasswd='" + vpnPasswd + '\'' +
                ", commentary='" + commentary + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderType=" + orderType +
                ", userModel=" + userModel +
                ", userId='" + userId + '\'' +
                ", desc='" + desc + '\'' +
                ", encrypt=" + encrypt +
                ", hostIp='" + hostIp + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", templateThreshold='" + templateThreshold + '\'' +
                ", twice=" + twice +
                ", keychain=" + keychain +
                ", commentPathList=" + commentPathList +
                ", stateScriptList=" + stateScriptList +
                ", commentImagePathList=" + commentImagePathList +
                ", accountImagePathList=" + accountImagePathList +
                ", alertImagePathList=" + alertImagePathList +
                ", compareImagePathList=" + compareImagePathList +
                ", popUpWindowsImagePathList=" + popUpWindowsImagePathList +
                ", sampleImagePathList=" + sampleImagePathList +
                ", payImagePathList=" + payImagePathList +
                ", appList=" + appList +
                ", templateList=" + templateList +
                ", vpnList=" + vpnList +
                ", timestamp=" + timestamp +
                '}';
    }
}

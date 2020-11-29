package com.xile.script.http.common;

/**
 * date: 2017/5/9 16:48
 */
public class HttpConstants {

    //优化平台
    //public static final String ORDER_CENTER_CONTROL_HOST = "http://192.168.21.128:8080/scriptManager_dev";
    //public static final String ORDER_CENTER_CONTROL_HOST = "http://xlyqq.xilexuan.com/scriptmanager";


//    public static final String ORDER_CENTER_CONTROL_HOST = "http://118.144.88.217:4080/scriptmanager";
    public static final String ORDER_CENTER_CONTROL_HOST_UP_LOAD_FILE_KEYCHAIN = "http://118.144.88.217:4080/scriptmanager";
    public static final String ORDER_CENTER_CONTROL_HOST = "http://39.106.93.192:4080/scriptmanager";




    public static String getCenterControlOrderUrl = ORDER_CENTER_CONTROL_HOST + "/supplyLinkedScript.do?";//请求优化平台订单
    public static String updateCenterControlUserUrl = ORDER_CENTER_CONTROL_HOST + "/userRegister.do?";//更新优化平台用户信息
    public static String updateCenterControlDeviceInfoUrl = ORDER_CENTER_CONTROL_HOST + "/userRegisterInfo.do?";//更新优化平台设备信息
    public static String responseIpInfoUrl = ORDER_CENTER_CONTROL_HOST + "/updateRemoteIp.do?";//返回IP信息
    public static String responseCenterControlResultUrl = ORDER_CENTER_CONTROL_HOST + "/replyLinkedScript.do?";//响应优化平台订单执行结果
    public static String uploadCenterControlPicAndShellUrl = ORDER_CENTER_CONTROL_HOST + "/robotShellUpload.do?";//上传优化平台截图和游戏脚本
    public static String responseModelCenterControlResultUrl = ORDER_CENTER_CONTROL_HOST + "/updateUserModule.do?";//上传模块
    public static final String UPLOAD_ALERT_PIC = ORDER_CENTER_CONTROL_HOST + "/verifyCode.do?method=alarm";//警报传图
//    public static final String UPLOAD_FILE_HOST = ORDER_CENTER_CONTROL_HOST + "/keychainUpload.do?";//上传keychain
    public static final String UPLOAD_FILE_HOST = ORDER_CENTER_CONTROL_HOST_UP_LOAD_FILE_KEYCHAIN + "/keychainUpload.do?";//上传keychain
    public static final String SEND_HTTP_MESSAGE = ORDER_CENTER_CONTROL_HOST + "/messageHandler.do?";//HTTP协议
    public static final String UPDATA_PACKAGE = ORDER_CENTER_CONTROL_HOST + "/gameApk.do?";//包更新
    public static final String GET_IP = ORDER_CENTER_CONTROL_HOST + "/vpnTest.do?method=getVpn&teamId=android_script";//获取IP
    public static final String UPLOADPICTOTENSORFLOW = "http://devcloud.xilexuan.com/tensorflow/detect/czjy/";
    public static final String UPLOADPICTOTENSORFLOW_TAPTAP = "http://devcloud.xilexuan.com/tensorflow/verificationCode";
    public static final String UPLOADPICTONORMAL = "http://devcloud.xilexuan.com/tensorflow/TouTiaoGuangGao";
    public static final String UPLOADTRAFFICDATA = ORDER_CENTER_CONTROL_HOST + "/orderFlow.do"; //上传流量数据

    //管理后台
    public static final String ORDER_MANAGER_HOST = "http://sy.xunbao178.com/robot.gsp";//服务器的URL
    public static String getManagerScriptUrl = ORDER_MANAGER_HOST + "?method=getRobotShellUrls";//请求管理后台脚本
    public static String uploadManagerPicAndShellUrl = "http://sy.xunbaotianxing.com/robotShellUpload";//上传管理后台截图和游戏脚本

    public static final String ORDER_CHECK_IP = "http://39.106.93.192:4080/scriptmanager/checkip.do";
}

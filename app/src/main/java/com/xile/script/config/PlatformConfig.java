package com.xile.script.config;

import com.xile.script.utils.common.StringUtil;

import script.tools.config.ScriptConstants;

/**
 * date: 2017/5/10 10:47
 *
 * @scene 游戏平台配置常量
 */
public class PlatformConfig {
    public static final String IMG_TYPE_1 = "1";//图片类型  alert
    public static final String IMG_TYPE_2 = "2";//图片类型  platform
    public static final String IMG_TYPE_3 = "3";//图片类型  popup
    public static final String IMG_TYPE_4 = "4";//图片类型  compare
    public static final String IMG_TYPE_5 = "5";//图片类型  sample
    public static final String IMG_TYPE_6 = "6";//图片类型  module
    public static final String CURRENT_OWN = "current_own";//当前存贮的所有者
    public static final String CURRENT_ADMIN = "current_admin";//当前临时存储的用户名
    public static final String CURRENT_CHECKNETLIMIT = "current_netlimit";//是否检查网络
    public static final String CURRENT_PASSWORD = "current_password";//当前临时存储的密码
    public static final String CURRENT_NICKNAME = "current_nickname";//当前临时存储的用户昵称
    public static final String CURRENT_COMMENTARY = "current_commentary";//当前临时存储的评论内容
    public static final String CURRENT_PHONE_NUMBER = "current_phone_number";//当前临时存储的手机号
    public static final String CURRENT_NAME = "current_name";//当前临时存储的姓名
    public static final String CURRENT_ID_CARD = "current_id_card";//当前临时存储的身份证
    public static final String CURRENT_GENDER = "current_id_gender";//当前临时存储的性别
    public static final String CURRENT_LARGE_AREA_NUMBER = "current_large_area_number";//当前临时存储的大区服
    public static final String CURRENT_SMALL_AREA_NUMBER = "current_small_area_number";//当前临时存储的小区服
    public static final String CURRENT_MANAGER_RESULT_TYPE = "current_manager_result_type";//当前游戏交易订单执行结果类型
    public static final String CURRENT_BRUSH_RESULT_TYPE = "current_brush_result_type";//当前优化订单执行结果类型
    public static final String CURRENT_BRUSH_RESULT_ERRORDESC = "current_brush_result_desc";//当前优化订单执行结果错误描述
    public static final String CURRENT_ROLE_LEVEL = "current_role_level";//当前临时存储的角色等级
    public static final String CURRENT_DEVICE_INFO = "current_device_info";//当前设备信息
    public static final String CURRENT_IP_INFO = "current_ip_info";//当前IP信息
    public static final String CURRENT_REMOTE_IP_ADDRESS = "current_remote_ip_address";//当前远程IP地址
    public static final String NEED_KILL_APP = "needKillApp";//是否需要杀死APP
    public static final String ISFREE = "isFree";//当前执行脚本是否空闲
    public static final String NEED_CHECK_ACTIVE = "needCheckActive";//是否需要检测游戏活跃时长
    public static final String NEED_KILL_PACKAGENAME = "needKillPackageName";//需要杀死的APP包名
    public static final String ACTIVE_TIME = "activeTime";//游戏活跃时长
    public static final String ORDER_EXEC_SUCCESS = "order_exec_success";//订单执行是否成功
    public static final String COMPARE_ALL = "compareAll";//是否比较所有图片  当执行客服订单时根据类型比较，提高速度  本地播放脚本时全比
    public static final String PHONE_NAME = "phone_name";//手机标识
    public static final String VPN_USERNAME = "vpnUserName";//当前VPN用户名
    public static final String VPN_PASSWORD = "vpnPassWord";//当前VPN密码
    public static final String CURRENT_VPN_TYPE = "current_vpn_type";//当前VPN类型
    public static final String CURRENT_COMMENT_FILE_NAME = "current_comment_file_name";//当前词库名称
    public static final String CURRENT_NEED_REBOOT = "current_need_reboot";//当前执行订单是否需要重启手机
    public static final String CURRENT_BOOLEAN_RECHARGE = "current_boolean_recharge";//当前手机是否为充值手机
    public static final String CURRENT_ADB_CMD = "current_adb_cmd";//当前执行指令是否采用adb
    public static final String CURRENT_SCREEN_ON = "current_screen_on";//是否保持屏幕常亮
    public static final String LAST_REBOOT_TIME = "last_reboot_time";//上次重启手机的时间
    public static final String LAST_RECEIVE_BROADCAST_TIME = "last_receive_broadcast_time";//上次接收到广播的时间
    public static final String CURRENT_ALERT_PIC_PATH = "current_alert_pic_path";//当前报警截图路径
    public static final String CURRENT_ORDER_CAPTURE_TYPE = "current_order_capture_type";//当前订单截图类型
    public static final String PHONE_BRAND = "phone_brand";//手机品牌
    public static final String ASHES_PACKAGE_NAME = "com.hjsz.hd";//灰烬使者包名
    public static final String TENCENTAPP_PACKAGE_NAME = "com.tencent.android.qqdownloader";//腾讯应用宝包名
    public static final String ZHUXIAN_PACKAGE_NAME = "com.wanmei.zhuxian.laohu";//诛仙包名
    public static final String CURRENT_OCR_TYPE = "current_ocr_type";//当前ocr识别类型


    /**
     * 根据包名获取对应平台
     *
     * @param packageName
     * @return
     */
    public static String getPlatform(String packageName) {
        if (!StringUtil.isEmpty(packageName)) {
            switch (packageName) {
                case ZHUXIAN_PACKAGE_NAME:
                    return ScriptConstants.PLATFORM_MANAGER;
                case ASHES_PACKAGE_NAME:
                    return ScriptConstants.PLATFORM_BRUSH;
                default:
                    break;
            }
        }
        return "";
    }

}

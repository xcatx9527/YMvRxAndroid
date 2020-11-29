package com.xile.script.http.helper.brush.mina;

/**
 * date: 2017/11/20 19:31
 */
public class BOrderStateConfig {
    public static final String ORDER_SUCCESS = "2";//订单处理完成
    public static final String ORDER_FAIL = "3";//订单处理失败
    public static final String ORDER_VPN_FAIL = "4";//订单处理失败(VPN切换超时)
    public static final String ORDER_FILE_LOST_FAIL = "6";//订单处理失败(订单文件缺失)
    public static final String ORDER_FAIL_PAUSE = "8";//订单处理失败(注册成功但未有后续操作，暂停)
    public static final String ORDER_GAME_DOWNLOAD_FAIL = "9";//订单处理失败(安装前出错，下载失败)
    public static final String ORDER_REGISTER_FAIL = "10";//订单处理失败(注册成功前出错，注册流程失败)
    public static final String ORDER_CREATE_ROLE_FAIL = "11";//订单处理失败(注册成功后出错，角色创建失败)
    public static final String ORDER_WRONG_ACCOUNT_PWD_FAIL = "12";//订单处理失败(账号密码错误)
    public static final String ORDER_ACCOUNT_EXCEPTION_FAIL = "13";//订单处理失败(账号异常)
    public static final String ORDER_CHANGE_DEVICE_FAIL = "14";//订单处理失败(设备切换失败)
    public static final String ORDER_REGISTER_EXCEPTION = "15";//订单处理失败(注册异常)
    public static final String ORDER_LOGIN_FAIL = "16";//订单处理失败(登录失败)
}

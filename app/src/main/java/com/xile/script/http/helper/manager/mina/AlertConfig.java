package com.xile.script.http.helper.manager.mina;


/**
 * date: 2017/5/23 10:42
 *
 * @scene 警报接口枚举类型
 */
public class AlertConfig {
    public static final String ALERT_AND_DELAY_REDO_GAME_UPDATE = "ALERT_AND_DELAY_REDO_GAME_UPDATE";//游戏维护更新
    public static final String ALERT_AND_ABOLISH_PASSWORD_ERROR = "ALERT_AND_ABOLISH_PASSWORD_ERROR";//账号密码错误
    public static final String ALERT_AND_ABOLISH_FORBIDEN_USER = "ALERT_AND_ABOLISH_FORBIDEN_USER"; //账号被封禁
    public static final String ALERT_AND_ABOLISH_SEVERWRITE_ERROR = "ALERT_AND_ABOLISH_SEVERWRITE_ERROR";//区服填写错误或角色为空
    public static final String ALERT_AND_REDO_SCRIPT_ERROR = "ALERT_AND_REDO_SCRIPT_ERROR";//脚本错误重新执行 报警3
    public static final String ALERT_AND_REDO_DOWNLOAD_ERROR = "ALERT_AND_REDO_DOWNLOAD_ERROR";//脚本下载错误重新执行
    public static final String ALERT_AND_REDO_SEVERID_ERROR = "ALERT_AND_REDO_SEVERID_ERROR";//大区服ID未配置，默认-1
    public static final String ALERT_AND_RECONFIRM_MORE_ROLE = "ALERT_AND_RECONFIRM_MORE_ROLE";//有多个符合的角色等级 （（两个或两个以上）的角色等级大于服务器返给的等级）
    public static final String ALERT_AND_ABOLISH_NONE_ROLE = "ALERT_AND_ABOLISH_NONE_ROLE";//没有符合的角色等级（游戏所有的角色 都小于服务器返给的等级）
    public static final String ALERT_AND_PAUSE_KEFU_DO = "ALERT_AND_PAUSE_KEFU_DO";//人工协助
    public static final String ALERT_AND_REDO_KEFU_ERROR = "ALERT_AND_REDO_KEFU_ERROR";//客服协助时误操作
    public static final String ALERT_AND_DELAY_REDO_WAIT_ENTER = "ALERT_AND_DELAY_REDO_WAIT_ENTER";//排队状态，等待进入游戏
    public static final String ALERT_AND_DELAY_REDO_AUTHCODE_ERROR = "ALERT_AND_DELAY_REDO_AUTHCODE_ERROR";//短信验证码获取失败
    public static final String ALERT_AND_REDO_ORC_SERVERID_ERROR = "ALERT_AND_REDO_ORC_SERVERID_ERROR";//区服识别失败 本地脚本重新执行
    public static final String ALERT_AND_REDO_ORC_ROLELEVEL_ERROR = "ALERT_AND_REDO_ORC_ROLELEVEL_ERROR";//角色等级识别失败

    public static final String ALERT_AND_PAUSE_WAITE_OUTTIME = "ALERT_AND_PAUSE_WAITE_OUTTIME";//脚本执行超时
    public static final String ALERT_AND_PAUSE_EXECUTE_OUTTIME = "ALERT_AND_PAUSE_EXECUTE_OUTTIME";//单号等待超时


    public static String getAlertDesc(String alerType) {
        switch (alerType) {
            case ALERT_AND_DELAY_REDO_GAME_UPDATE:
                return "游戏维护更新";

            case ALERT_AND_ABOLISH_PASSWORD_ERROR:
                return "账号密码错误";

            case ALERT_AND_ABOLISH_FORBIDEN_USER:
                return "账号被封禁";

            case ALERT_AND_ABOLISH_SEVERWRITE_ERROR:
                return "区服填写错误或角色为空";

            case ALERT_AND_REDO_SEVERID_ERROR:
                return "大区服ID未配置";

            case ALERT_AND_RECONFIRM_MORE_ROLE:
                return "有多个符合的角色等级";

            case ALERT_AND_REDO_SCRIPT_ERROR:
                return "脚本错误重新执行";

            case ALERT_AND_REDO_DOWNLOAD_ERROR:
                return "脚本下载错误重新执行";

            case ALERT_AND_ABOLISH_NONE_ROLE:
                return "没有符合的角色等级";

            case ALERT_AND_PAUSE_KEFU_DO:
                return "人工协助";

            case ALERT_AND_REDO_KEFU_ERROR:
                return "客服协助时误操作";

            case ALERT_AND_DELAY_REDO_WAIT_ENTER:
                return "排队状态，等待进入游戏";

            case ALERT_AND_REDO_ORC_SERVERID_ERROR:
                return "区服识别失败";

            case ALERT_AND_REDO_ORC_ROLELEVEL_ERROR:
                return "角色等级识别失败";

            case ALERT_AND_DELAY_REDO_AUTHCODE_ERROR:
                return "短信验证码获取失败";

            default:
                break;
        }
        return "";
    }


}

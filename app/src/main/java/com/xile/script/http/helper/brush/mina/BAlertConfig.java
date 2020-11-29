package com.xile.script.http.helper.brush.mina;

/**

 * date: 2017/7/4 10:35
 *
 * @scene 优化平台警报类型
 */
public class BAlertConfig {

    public static final String B_ALERT_AND_REDO_SCRIPT_ERROR = "B_ALERT_AND_REDO_SCRIPT_ERROR";//脚本错误重新执行
    public static final String B_ALERT_AND_PAUSE_KEFU_DO = "B_ALERT_AND_PAUSE_KEFU_DO";//人工协助


    public static String getAlertDesc(String alerType) {
        switch (alerType) {

            case B_ALERT_AND_REDO_SCRIPT_ERROR:
                return "脚本错误重新执行";

            case B_ALERT_AND_PAUSE_KEFU_DO:
                return "人工协助";

            default:
                break;
        }
        return "";
    }


}

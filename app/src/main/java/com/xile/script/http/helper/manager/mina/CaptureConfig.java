package com.xile.script.http.helper.manager.mina;

/**
 * date: 2017/7/4 10:35
 *
 * @scene 机器人状态枚举类型
 */
public class CaptureConfig {
    public static final String ROBOT_IS_FREE = "ROBOT_IS_FREE";//空闲状态
    public static final String ROBOT_IS_CAPTURING = "ROBOT_IS_CAPTURING";//正在截图
    public static final String ROBOT_IS_UPLOADING_IMAGES = "ROBOT_IS_UPLOADING_IMAGES";//正在上传
    public static final String ROBOT_IS_WAITING_ASSISTANCE = "ROBOT_IS_WAITING_ASSISTANCE";//等待人工协助

    public static String getRobotStateDesc(String robotType) {
        switch (robotType) {
            case ROBOT_IS_FREE:
                return "空闲状态";

            case ROBOT_IS_CAPTURING:
                return "正在截图";

            case ROBOT_IS_UPLOADING_IMAGES:
                return "正在上传";

            case ROBOT_IS_WAITING_ASSISTANCE:
                return "等待人工协助";

            default:
                break;
        }
        return "";
    }

}

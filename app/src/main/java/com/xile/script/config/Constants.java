package com.xile.script.config;


import android.os.Environment;

import com.xile.script.base.ScriptApplication;
import com.xile.script.http.helper.brush.bean.BrushOrderInfo;
import com.xile.script.http.helper.manager.bean.GamesOrderInfo;
import com.yzy.example.BuildConfig;

/**
 * date: 2017/3/1 11:17
 *
 * @scene 常量值
 */

public class Constants {

    // <-状态值->

    public static RecordEnum RECORD_STATE = RecordEnum.STOP_RECORD;//录制状态
    public static PlayEnum PLAY_STATE = PlayEnum.STOP_PLAY;//播放状态
    public static CollectEnum COLLECT_STATE = CollectEnum.COLLECT_PHOTO;//采集状态
    public static ExecEnum EXEC_STATE = ExecEnum.EXEC_STOP;//执行状态

    public static boolean needPauseRecord = false; //是否需要暂停
    public static boolean needSave = false;  //是否需要保存
    public static boolean execServerScript = false;  //是否处于执行服务器脚本
    public static String currentPlatform;//当前游戏平台
    public static String currentRobotState;//当前机器人状态
    public static long requestOrderTime;//请求订单一刻的当前时间
    public static long getOrderTime;//获取到订单一刻的当前时间
    public static long orderSuccessTime;//订单处理成功时的当前时间
    public static BrushOrderInfo sBrushOrderInfo;//全局当前优化平台订单信息
    public static GamesOrderInfo sGamesOrderInfo;//全局当前管理后台订单信息
    public static long loopCount = 0;//指令执行循环次数
    public static int failRepeatCount = 0;//返回IP/设备/用户 信息以及上传文件失败次数
    public static int failGetVerifyCodeCount = 0;//获取校验码失败次数

    public static int VERIFY_TYPE = -1;//验证码状态值    0表示文字验证码图片，1表示滑动验证码图片，2表示手势验证码，3表示点击验证码

    public static int ACCOUNT_MIN = -1;//账号最小位数
    public static int ACCOUNT_MAX = -1;//账号最大位数

    public static String gender = "";
    // <-常SD卡存储->


    public static final String DUMP_PATH = Environment.getExternalStorageDirectory() + "/ui.xml";
    public static final String DEVICE_PATH = Environment.getExternalStorageDirectory() + "/LocalInfo/DeviceInfo.txt";

    public static final String MAC_PATH = Environment.getExternalStorageDirectory() + "/LocalInfo/MacAddressInfo.txt";
    public static final String CID_PATH = Environment.getExternalStorageDirectory() + "/LocalInfo/cid.txt";
    public static final String CORE_PATH = Environment.getExternalStorageDirectory() + "/LocalInfo/CoreVersion.txt";
    public static final String ARP_PATH = Environment.getExternalStorageDirectory() + "/LocalInfo/arp.txt";
    public static final String MAC_PREFS_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/mac.xml";
    public static final String CID_PREFS_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/cid.xml";
    public static final String CORE_PREFS_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/core.xml";
    public static final String ARP_PREFS_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/shared_prefs/arp.xml";
    public static final String PHONE_NAME_PATH = Environment.getExternalStorageDirectory() + "/LocalInfo/PhoneName.txt";
    public static final String LOCAL_HOOK_API = Environment.getExternalStorageDirectory() + "/LocalInfo/changeAPI.txt";
    public static final String LOCAL_HOOK_FINGER = Environment.getExternalStorageDirectory() + "/LocalInfo/changeFinger.txt";
    public static final String INSTALL_APP_PATH = Environment.getExternalStorageDirectory() + "/Android/data/" + BuildConfig.APPLICATION_ID + "/cache/app";
    public static final String RECORD_FILE_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/record/";
    public static final String SCRIPT_FOLDER_ROOT_PATH = ScriptApplication.app.getExternalFilesDir("XileScript").getAbsolutePath();
    public static final String SCRIPT_FOLDER_GAME_DATA_PATH = SCRIPT_FOLDER_ROOT_PATH + "/GameData/";
    public static final String SCRIPT_FOLDER_GAME_KEYCHAIN_PATH = SCRIPT_FOLDER_GAME_DATA_PATH + "keychain/";
    public static final String SCRIPT_FOLDER_GAME_APK_PATH = SCRIPT_FOLDER_GAME_DATA_PATH + "apk/";
    public static final String SCRIPT_FOLDER_GAME_RESOURCE_PATH = SCRIPT_FOLDER_GAME_DATA_PATH + "resource/";
    public static final String SCRIPT_FOLDER_GAME_APK_MD5_PATH = SCRIPT_FOLDER_GAME_DATA_PATH + "XMD5.cfg";
    public static final String SCRIPT_FOLDER_GAME_KEYCHAIN_UPLOAD_PATH = SCRIPT_FOLDER_GAME_DATA_PATH + "upload/";
    public static final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    public static final String SCRIPT_FOLDER_PATH = SCRIPT_FOLDER_ROOT_PATH + "/script/";

    public static final String CERT_FOLDER_PATH = SCRIPT_FOLDER_ROOT_PATH + "/cert/";
    public static final String SCRIPT_FOLDER_TEMP = SCRIPT_FOLDER_ROOT_PATH + "/temp";//临时文件夹
    public static final String SCRIPT_PLAY_CAPTURE_TEMP = SCRIPT_FOLDER_ROOT_PATH + "/capture";//脚本播放的进行截图取图操作
    public static final String SCRIPT_AREA_PATH = SCRIPT_FOLDER_ROOT_PATH + "/area";//脚本采集区服坐标点文件夹
    public static final String SCRIPT_ROLE_PATH = SCRIPT_FOLDER_ROOT_PATH + "/role";//脚本采集角色坐标点文件夹
    public static final String SCRIPT_APPLICATION_DATA_PATH = SCRIPT_FOLDER_GAME_DATA_PATH + "/data";//缓存的应用数据
    public static final String SCRIPT_FOLDER_ROOT_POP_IMG_PATH = SCRIPT_FOLDER_ROOT_PATH + "/popup";//从服务器下载的图片文件夹根目录
    public static final String SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH = SCRIPT_FOLDER_ROOT_PATH + "/local_popup";//本地图片文件夹根目录
    public static final String SCRIPT_FOLDER_POP_LOCAL_IMG_PATH = SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH + "/popup";//本地弹框图片文件夹
    public static final String SCRIPT_FOLDER_ALERT_LOCAL_IMG_PATH = SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH + "/alert";//本地警告图片文件夹
    public static final String SCRIPT_FOLDER_PLATFORM_LOCAL_IMG_PATH = SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH + "/platform";//本地平台图片文件夹
    public static final String SCRIPT_FOLDER_COMPARE_LOCAL_IMG_PATH = SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH + "/compare";//本地比较图片文件夹
    public static final String SCRIPT_FOLDER_SAMPLE_LOCAL_IMG_PATH = SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH + "/sample";//本地样本文件夹
    public static final String SCRIPT_FOLDER_MODULE_LOCAL_IMG_PATH = SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH + "/module";//本地模板图片文件夹
    public static final String SCRIPT_TAKE_SMALL_PHOTO_PATH = SCRIPT_FOLDER_ROOT_PATH + "/thumb";//截取小图
    public static final String SCRIPT_EXCEL_PATH = SCRIPT_FOLDER_ROOT_PATH + "/excel";//Excel脚本
    public static final String SCRIPT_TEMP_FILE_DOWNLOAD_PATH = DCIM_PATH + "/Camera";//下载缓存
    public static final String SCRIPT_TEMP_ALERT_CAPTURE_PATH = SCRIPT_FOLDER_TEMP + "/tempAlertCapture";//报警截图
    public static final String SCRIPT_TEMP_ALERT_CLEAR_CAPTURE_PATH = SCRIPT_FOLDER_TEMP + "/tempClearAlertCapture";//报警清晰截图
    public static final String SCRIPT_FOLDER_COMMENT_PATH = SCRIPT_FOLDER_ROOT_PATH + "/comment/";//词库
    public static final String SCRIPT_FOLDER_LOCAL_COMMENT_PATH = SCRIPT_FOLDER_ROOT_PATH + "/local_comment/";//本地词库
    public static final String LOG_PATH = SCRIPT_FOLDER_ROOT_PATH + "/local_comment/" + "log.txt";//错误日志
    public static final String WEIXIN_PATH = Environment.getExternalStorageDirectory() + "/Android/" + "微信消息.txt";//微信日志
    public static final String SCRIPT_MATCH_PATH = SCRIPT_FOLDER_ROOT_PATH + "/matchTmp";//Excel模板匹配路径


    public static final String SCRIPT_FOLDER_TEMP_PATH = SCRIPT_FOLDER_TEMP + "/tempScript.txt";//临时执行脚本
    public static final String BRUSH_ORDER_FOLDER_TEMP_PATH = SCRIPT_FOLDER_TEMP + "/tempBrushOrder.txt";//临时缓存订单
    public static final String MANAGER_ORDER_FOLDER_TEMP_PATH = SCRIPT_FOLDER_TEMP + "/tempManagerOrder.txt";//临时缓存订单
    public static final String BRUSH_ORDER_ID_TEMP_PATH = SCRIPT_FOLDER_TEMP + "/tempUserId.txt";//临时缓存用户ID
    public static final String GAMES_ORDER_LOG_PATH = Environment.getExternalStorageDirectory() + "/GamesOrderLog.txt";//临时缓存Log
    public static final String SCRIPT_FOLDER_TEMP_PNG_PATH = SCRIPT_FOLDER_TEMP + "/tempScript.jpg";//临时录制截图的图片路径
    public static final String SCRIPT_FOLDER_TAKE_TEMP_PNG_PATH = SCRIPT_FOLDER_TEMP + "/tempCapture.png";//临时取图的图片路径
    public static final String SCRIPT_FOLDER_TEMP_AREA_UPLOAD_PATH = SCRIPT_FOLDER_TEMP + "/tempAreaOcrUpload.png";//临时区服上传的图片路径
    public static final String SCRIPT_FOLDER_TEMP_ROLE_UPLOAD_PATH = SCRIPT_FOLDER_TEMP + "/tempRoleOcrUpload.png";//临时角色上传的图片路径
    public static final String FLOAT_LOCATION_VIEW_DATA_AREA_PATH_RECT = SCRIPT_FOLDER_TEMP + "/tempAreaLocationRect.txt";//区服的上一次点的位置
    public static final String FLOAT_LOCATION_VIEW_DATA_ROLE_PATH_RECT = SCRIPT_FOLDER_TEMP + "/tempRoleLocationRect.txt";// 角色的上一次点的位置
    public static final String FLOAT_LOCATION_VIEW_DATA_AREA_PATH_POINT = SCRIPT_FOLDER_TEMP + "/tempAreaLocationPoint.txt";//区服的上一次点的位置
    public static final String FLOAT_LOCATION_VIEW_DATA_ROLE_PATH_POINT = SCRIPT_FOLDER_TEMP + "/tempRoleLocationPoint.txt";// 角色的上一次点的位置


}

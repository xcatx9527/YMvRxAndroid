package com.xile.script.config;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import script.tools.config.DeviceConfig;

/**
 * date: 2017/5/2 19:14
 *
 * @scene 游戏配置
 */
public class GameConfig {
    private static ConcurrentHashMap<String, Object> areaAndRoleConfigs;


    public static synchronized ConcurrentHashMap getAreaAndRoleConfigs() {
        if (areaAndRoleConfigs == null) {
            areaAndRoleConfigs = new ConcurrentHashMap();
        }
        return areaAndRoleConfigs;
    }

    public static final String DATA_CHOOSE = "数据选";
    public static final String WHITE_LIST = "白名单";
    public static final String INTERVAL = "间隔";
    public static final String RGB = "RGB";
    public static final String SPLIT = "-";
    public static final String CONFIG = "配置";
    public static final String ACCOUNT_RULE = "账号注册规则";
    public static final String ACCOUNT_DIGIT_RULE = "账号注册位数规则";

    public static final String DEVICE_TYPE = "设备机型";
    public static final String DEVICE_RANDOM_PARAM = "设备参数随机产生";
    public static final String ACCOUNT_RULE_RANDOM = "随意注册";
    public static final String ACCOUNT_RULE_PHONE = "手机注册";

    public static final String AREA_ORDER = "区服顺序";
    public static final String LARGE_AREA_SMALL_FRAME = "数据选大区服小框";
    public static final String LARGE_AREA_LARGE_FRAME = "数据选大区服大框";
    public static final String LARGE_AREA_TEXT_FRAME = "数据选大区服文字框";
    public static final String SMALL_AREA_SMALL_FRAME = "数据选小区服小框";
    public static final String SMALL_AREA_LARGE_FRAME = "数据选小区服大框";
    public static final String TINNY_AREA_TEXT_FRAME = "数据选小区服文字框";
    public static final String ROLE_FRAME = "数据选角色框";
    public static final String LARGE_AREA_WHITE_LIST = "大区白名单";
    public static final String TINNY_AREA_WHITE_LIST = "小区白名单";
    public static final String LEVEL_WHITE_LIST = "等级白名单";
    public static final String LARGE_AREA_INTERVAL_COUNT = "大区间隔区数";
    public static final String LARGE_AREA_INTERVAL_MARK = "大区间隔符";
    public static final String LARGE_AREA_TEXT_ERROR_MARK = "大区文字识别纠错字符";
    public static final String SMALL_AREA_TEXT_ERROR_MARK = "小区文字识别纠错字符";
    public static final String LARGE_AREA_DIFFRENT_VALUE = "大区可识别差值";
    public static final String SMALL_AREA_DIFFRENT_VALUE = "小区可识别差值";
    public static final String LARGE_AREA_SHOT = "大区截图区域";
    public static final String SMALL_AREA_SHOT = "小区截图区域";
    public static final String ROLE_SHOT = "角色截图区域";
    public static final String ROLE_CORRECT_WORD = "角色文字识别纠错字符";
    public static final String LARGE_AREA_SWIPE_COMMAND = "大区滑动指令";
    public static final String LARGE_AREA_RGB = "大区RGB";
    public static final String TINNY_AREA_RGB = "小区RGB";
    public static final String LARGE_AREA_TAG_SCREENSHOT = "大区标识截图区域";
    public static final String SMALL_AREA_TAG_SCREENSHOT = "小区标识截图区域";
    public static final String LEVEL_RGB = "等级RGB";
    public static final String PHONE_MAIL = "手机邮箱";
    public static final String ORDER_MIN_TIME = "订单最小时长";
    public static final String ALERT_QUIT = "老虎平台报警退出";
    public static final String AREA_TEXT_TYPE_INT = "数字类型";
    public static final String AREA_TEXT_TYPE_STRING = "文字类型";
    public static final String FINISH_DELAY_STRING = "脚本结束延长时间";
    public static final String LARGE_AREA_DIRECT = "大区方向";
    public static final String SMALL_AREA_DIRECT = "小区方向";
    public static final String VERTICAL_DIRECT = "竖直方向";
    public static final String HORIZONTAL_DIRECT = "水平方向";
    public static final String AREA_TEXT_TO_REMOVE = "区服文字去除符";
    public static final String CUT_PHONE_VERIFY_CODE = "截取手机验证码";
    public static final String PHONE_VERIFY_CODE_PROJECT_ID = "手机验证码项目id";
    public static final String PHONE_VERIFY_CODE_OPERATOR = "手机验证码运营商";
    public static final String PHONE_VERIFY_CODE_NOTPREFIX = "手机验证码不要号段";
    public static final String PHONE_VERIFY_CODE_INFO_FIRST = "手机验证码信息第一条";
    public static final String PHONE_VERIFY_CODE_INFO_SECOND = "手机验证码信息第二条";
    public static final String PHONE_VERIFY_CODE_INFO_THIRD = "手机验证码信息第三条";
    public static final String PHONE_NUMBER_SOURCE = "手机号来源";
    //自定义键盘按键START
    public static final String KEYBOARD = "键盘";
    public static final String KEYBOARD_COMPARE_PIC = "自定义键盘比较图片";
    //自定义键盘按键END
    public static final String OWNER_POKER_AREA = "已方牌面区域";
    public static final String ABAVE_POKER_AREA = "上家出牌区域";
    public static final String BELOW_POKER_AREA = "下家出牌区域";
    public static final String DRAW_BUTTON_COORDINATE = "出牌按钮坐标";
    public static final String VPN_CITY_LISTS = "VPN城市列表";
    public static final String BOOLEAN_EXEXUTE_ORDER_REBOOT = "执行订单是否重启";
    public static final String BOOLEAN_RECHANGE_PHONE = "是否为充值手机";
    public static final String BOOLEAN_ADB_TOUCH = "ADB触摸事件";
    public static final String BOOLEAN_SCREEN_ON = "是否保持屏幕常亮";
    public static final String BOOLEAN_CHANGE_API_LEVEL = "修改Api Level";
    public static final String BOOLEAN_CHANGE_FINGER = "修改指纹信息";
    public static final String ACCESSIBILITY_POP_ID = "无障碍弹窗ID";
    public static final String ACCESSIBILITY_POP_TEXT = "无障碍弹窗文本";
    public static final String NEED_KILL_APP_NAME = "需要杀死的APP";


    public static final String GAME_AREA_ORDER = "game_area_order";    //区顺序
    public static final String GAME_LARGE_AREA_SMALL_FRAME = "game_large_area_small_frame";    //大区服小框
    public static final String GAME_LARGE_AREA_LARGE_FRAME = "game_large_area_large_frame";    //大区服大框
    public static final String GAME_LARGE_AREA_TEXT_FRAME = "game_large_area_text_frame";      //大区服文字框
    public static final String GAME_SMALL_AREA_SMALL_FRAME = "game_small_area_small_frame";    //小区服小框
    public static final String GAME_SMALL_AREA_LARGE_FRAME = "game_small_area_large_frame";    //小区服大框
    public static final String GAME_TINNY_AREA_TEXT_FRAME_1 = "game_tinny_area_text_frame_1";  //小区服文字框1
    public static final String GAME_TINNY_AREA_TEXT_FRAME_2 = "game_tinny_area_text_frame_2";  //小区服文字框2
    public static final String GAME_TINNY_AREA_TEXT_FRAME_3 = "game_tinny_area_text_frame_3";  //小区服文字框3
    public static final String GAME_TINNY_AREA_TEXT_FRAME_4 = "game_tinny_area_text_frame_4";  //小区服文字框4
    public static final String GAME_TINNY_AREA_TEXT_FRAME_5 = "game_tinny_area_text_frame_5";  //小区服文字框5
    public static final String GAME_TINNY_AREA_TEXT_FRAME_6 = "game_tinny_area_text_frame_6";  //小区服文字框6
    public static final String GAME_TINNY_AREA_TEXT_FRAME_7 = "game_tinny_area_text_frame_7";  //小区服文字框7
    public static final String GAME_TINNY_AREA_TEXT_FRAME_8 = "game_tinny_area_text_frame_8";  //小区服文字框8
    public static final String GAME_TINNY_AREA_TEXT_FRAME_9 = "game_tinny_area_text_frame_9";  //小区服文字框9
    public static final String GAME_TINNY_AREA_TEXT_FRAME_10 = "game_tinny_area_text_frame_10";//小区服文字框10
    public static final String GAME_ROLE_FRAME_1 = "game_role_frame_1";  //角色框1
    public static final String GAME_ROLE_FRAME_2 = "game_role_frame_2";  //角色框2
    public static final String GAME_ROLE_FRAME_3 = "game_role_frame_3";  //角色框3
    public static final String GAME_ROLE_FRAME_4 = "game_role_frame_4";  //角色框4
    public static final String GAME_ROLE_FRAME_5 = "game_role_frame_5";  //角色框5
    public static final String GAME_LARGE_AREA_WHITE_LIST = "game_large_area_white_list";  //大区白名单
    public static final String GAME_TINNY_AREA_WHITE_LIST = "game_tinny_area_white_list";  //小区白名单
    public static final String GAME_LEVEL_WHITE_LIST = "game_level_white_list";  //等级白名单
    public static final String GAME_LARGE_AREA_INTERVAL_COUNT = "game_large_area_interval_count";  //大区间隔区数
    public static final String GAME_LARGE_AREA_TEXT_ERROR_MARK = "game_large_area_text_error_mark";  //大区文字识别纠错字符
    public static final String GAME_SMALL_AREA_TEXT_ERROR_MARK = "game_small_area_text_error_mark";  //大区文字识别纠错字符
    public static final String GAME_LARGE_AREA_DIFFRENT_VALUE = "game_large_area_diffrent_value";  //大区可识别差值
    public static final String GAME_SMALL_AREA_DIFFRENT_VALUE = "game_small_area_diffrent_value";  //小区可识别差值
    public static final String GAME_LARGE_AREA_SHOT = "game_large_area_shot";  //大区截图区域
    public static final String GAME_SMALL_AREA_SHOT = "game_small_area_shot";  //小区截图区域
    public static final String GAME_ROLE_SHOT = "game_role_shot";  //角色截图区域
    public static final String GAME_INTERVAL_MARK = "game_interval_mark";  //间隔符
    public static final String GAME_LARGE_AREA_SWIPE_COMMAND = "game_large_area_swipe_command";  //大区滑动指令
    public static final String GAME_ROLE_CORRECT_WORD = "game_role_correct_word";  //角色纠错字符
    public static final String GAME_LARGE_AREA_RGB = "game_large_area_rgb";  //大区RGB
    public static final String GAME_TINNY_AREA_RGB = "game_tinny_area_rgb";  //小区RGB
    public static final String GAME_LEVEL_RGB = "game_level_rgb";  //等级RGB
    public static final String GAME_PHONE_MAIL = "game_phone_mail";  //手机邮箱
    public static final String GAME_ORDER_MIN_TIME = "game_order_min_time";  //订单最小时长
    public static final String GAME_ALERT_QUIT = "game_alert_quit";  //报警退出点击
    public static final String GAME_FINISH_DELAY = "game_finish_delay";  //脚本结束延长时间
    public static final String GAME_LARGE_AREA_TAG_SCREENSHOT = "game_large_area_tag_screenshot";  //大区标识截图区域
    public static final String GAME_SMALL_AREA_TAG_SCREENSHOT = "game_small_area_tag_screenshot";  //小区标识截图区域
    public static final String GAME_LARGE_AREA_DIRECT = "game_large_area_direct";  //大区方向
    public static final String GAME_SMALL_AREA_DIRECT = "game_small_area_direct";  //小区方向
    public static final String GAME_AREA_TEXT_TO_REMOVE = "game_area_text_to_remove";  //区服文字去除服
    public static final String GAME_KEYBOARD_1 = "game_keyboard_1";  //键盘1
    public static final String GAME_KEYBOARD_2 = "game_keyboard_2";  //键盘2
    public static final String GAME_KEYBOARD_3 = "game_keyboard_3";  //键盘3
    public static final String GAME_KEYBOARD_4 = "game_keyboard_4";  //键盘4
    public static final String GAME_KEYBOARD_5 = "game_keyboard_5";  //键盘5
    public static final String GAME_KEYBOARD_6 = "game_keyboard_6";  //键盘6
    public static final String GAME_KEYBOARD_7 = "game_keyboard_7";  //键盘7
    public static final String GAME_KEYBOARD_8 = "game_keyboard_8";  //键盘8
    public static final String GAME_KEYBOARD_9 = "game_keyboard_9";  //键盘9
    public static final String GAME_KEYBOARD_0 = "game_keyboard_0";  //键盘0
    public static final String GAME_KEYBOARD_BACK = "game_keyboard_back";  //键盘退格
    public static final String GAME_KEYBOARD_SURE = "game_keyboard_sure";  //键盘确定
    public static final String GAME_KEYBOARD_COMPARE_PIC = "game_keyboard_compare_pic";  //自定义键盘比较图片
    public static final String GAME_CUT_PHONE_VERIFY_CODE = "game_cut_phone_verify_code";  //截取手机号验证码
    public static final String GAME_PHONE_VERIFY_CODE_PROJECT_ID = "game_phone_verify_code_project_id";  //手机验证码项目id
    public static final String GAME_PHONE_VERIFY_CODE_OPERATOR = "game_phone_verify_code_operator";  //手机验证码运营商
    public static final String GAME_PHONE_VERIFY_CODE_NOTPREFIX = "game_phone_verify_code_notprefix";  //手机验证码不要号段
    public static final String GAME_PHONE_VERIFY_CODE_INFO_FIRST = "game_phone_verify_code_info_first";  //手机验证码信息第一条
    public static final String GAME_PHONE_VERIFY_CODE_INFO_SECOND = "game_phone_verify_code_info_second";  //手机验证码信息第二条
    public static final String GAME_PHONE_VERIFY_CODE_INFO_THIRD = "game_phone_verify_code_info_third";  //手机验证码信息第三条
    public static final String GAME_PHONE_NUMBER_SOURCE = "game_phone_number_source";  //手机号来源

    public static final String GAME_OWNER_POKER_AREA = "game_owner_poker_area";
    public static final String GAME_ABAVE_POKER_AREA = "game_abave_poker_area";
    public static final String GAME_BELOW_POKER_AREA = "game_below_poker_area";
    public static final String GAME_DRAW_BUTTON_COORDINATE = "game_draw_button_coordinate";


    public static String ASHES_ACCOUNT_RULE;  //账号配置规则
    public static String ASHES_PASSWORD_RULE; //密码配置规则
    public static Vector<String> registerPercentList = new Vector<>();  //账号注册配置百分比合集

    /**
     * 清空配置数据集
     */
    public static void clear() {
        GameConfig.registerPercentList.clear();
        DeviceConfig.deviceConfigMap.clear();
        DeviceConfig.deviceRandomParam = true;
    }



    //==================================统计时间相关========================

    public static String COUNT_CONFIG_START = "统计配置开始";
    public static String COUNT_CONFIG_END = "统计配置结束";

    public static String COUNT_CHANGEIP_START = "统计切换IP开始";
    public static String COUNT_CHANGEIP_END = "统计切换IP结束";

    public static String COUNT_DELETE_FILE_START = "统计删除文件开始";
    public static String COUNT_DELETE_FILE_END = "统计删除文件结束";

    public static String COUNT_RE_BACK_IP_START = "统计返回IP开始";
    public static String COUNT_RE_BACK_IP_END = "统计返回IP结束";

    public static String COUNT_KILL_UNINSTALL_START = "统计杀死卸载开始";
    public static String COUNT_KILL_UNINSTALL_END = "统计杀死卸载结束";

    public static String COUNT_INSTALL_START = "统计安装开始";
    public static String COUNT_INSTALL_END = "统计安装结束";

    public static String COUNT_CHECK_CODE_START = "统计校验码开始";
    public static String COUNT_CHECK_CODE_END = "统计校验码结束";

    public static String COUNT_PHONE_NUM_START = "统计手机号开始";
    public static String COUNT_PHONE_NUM_END = "统计手机号结束";

    public static String COUNT_VERIFY_CODE_START = "统计手机验证码开始";
    public static String COUNT_VERIFY_CODE_END = "统计手机验证码结束";

    public static String COUNT_CUSTOMIZE_START = "统计自定义开始";
    public static String COUNT_CUSTOMIZE_END = "统计自定义结束";


    public static String COUNT_CONFIG_START_KEY = "configStart";
    public static String COUNT_CONFIG_END_KEY = "configEnd";

    public static String COUNT_CHANGEIP_START_KEY = "changeIpStart";
    public static String COUNT_CHANGEIP_END_KEY = "changeIpEnd";

    public static String COUNT_DELETE_FILE_START_KEY = "deleteFileStart";
    public static String COUNT_DELETE_FILE_END_KEY = "deleteFileEnd";

    public static String COUNT_RE_BACK_IP_START_KEY = "reBackIpStart";
    public static String COUNT_RE_BACK_IP_END_KEY = "reBackIpEnd";

    public static String COUNT_KILL_UNINSTALL_START_KEY = "killUninstallStart";
    public static String COUNT_KILL_UNINSTALL_END_KEY = "killUninstallEnd";

    public static String COUNT_INSTALL_START_KEY = "installStart";
    public static String COUNT_INSTALL_END_KEY = "installEnd";

    public static String COUNT_CHECK_CODE_START_KEY = "checkCodeStart";
    public static String COUNT_CHECK_CODE_END_KEY = "checkCodeEnd";

    public static String COUNT_PHONE_NUM_START_KEY = "phoneNumStart";
    public static String COUNT_PHONE_NUM_END_KEY = "phoneNumEnd";

    public static String COUNT_VERIFY_CODE_START_KEY = "verifyCodeStart";
    public static String COUNT_VERIFY_CODE_END_KEY = "verifyCodeEnd";

    public static String COUNT_CUSTOMIZE_START_KEY = "customizeStart";
    public static String COUNT_CUSTOMIZE_END_KEY = "customizeEnd";

    public static String COUNT_DOWNLOADFILE_KEY = "downloadFileTime";
    public static String COUNT_COMPAREPIC_KEY = "comparePicTime";


}

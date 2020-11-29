package com.xile.script.utils.script;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.BaseFloatView;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.*;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.InstructUtil;
import com.xile.script.utils.common.SpUtil;
import script.tools.EventUtil;
import script.tools.config.ScriptConstants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * date: 2017/5/13 16:06
 */

public class SocketUtil {
    private static String ipAddress = "127.0.0.1";
    private static int client_port = 1112;
    private static final int server_port = 1111;
    public static volatile ServerSocket server = null;
    private static Socket socket = null;
    public static boolean flag = false;
    private static PrintWriter pw;
    private static boolean isRooted;  //是否ROOT
    private static long delayTime = 0;  //睡眠时间
    public static boolean needDelay = false;  //是否需要延长播放时间
    public static int SOCKET_TOTAL_MSG_TYPE = 2;  //Handler消息类型总数
    public static final int SOCKET_MSG_INIT = 0;  //初始化
    public static final int SOCKET_MSG_EXEC = 1;  //执行脚本

    public static List<String> scriptList = new ArrayList<>();  //执行的脚本集合
    public static List<String> tempScriptList = new ArrayList<>();
    public static boolean recordScript = false;  //判断是否保存当前指令
    public static Map<String, Integer> execModuleCount = new ConcurrentHashMap<>();
    public static List<String> accessiblityPopList_ID = new ArrayList<>();
    public static List<String> accessiblityPopList_TEXT = new ArrayList<>();
    public static List<String> need_kill_APP_list = new ArrayList<>();


    private static int totalLoopCount;      //循环次数
    public static int currentExecNum;       //当前执行的脚本条数
    private static int currentLoopNum;      //当前执行的循环次数
    public static int currentTryLoadingNum; //当前检测载入界面的次数


    private static Handler execHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SOCKET_MSG_INIT:  //初始化
                    ScriptApplication.getService().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                closeSocket();
                                if (socket == null || !socket.isConnected()) {
                                    socket = new Socket(ipAddress, client_port);
                                }
                                if (pw == null) {
                                    pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                closeSocket();
                            }
                            sendEmptyMessage(SOCKET_MSG_EXEC);
                        }
                    });
                    break;

                case SOCKET_MSG_EXEC:  //执行脚本
                    ScriptApplication.getService().execute(new Runnable() {
                        @Override
                        public void run() {
                            execScript();
                        }
                    });
                    break;

            }
        }
    };


    /**
     * 初始化指令
     *
     * @param strList 指令集合
     * @param count   循环次数
     */
    public synchronized static void initInstruct(final List<String> strList, final int count) {
        isRooted = CMDUtil.checkRootPermission();
        EventUtil.closeEvent();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_2000);
        EventUtil.initEventPermission();
        EventUtil.initEvent();
        scriptList.clear();
        execModuleCount.clear();
        accessiblityPopList_ID.clear();
        accessiblityPopList_TEXT.clear();
        need_kill_APP_list.clear();

        scriptList.addAll(strList);
        totalLoopCount = count;
        currentExecNum = 0;
        currentLoopNum = 1;
        currentTryLoadingNum = 1;
        if (isRooted) {
            execHandler.sendEmptyMessage(SOCKET_MSG_EXEC);
        } else {
            execHandler.sendEmptyMessage(SOCKET_MSG_INIT);
        }
    }


    /**
     * 执行脚本
     */
    private static synchronized void execScript() {
        try {
            if (Constants.PLAY_STATE == PlayEnum.START_PLAY) {//播放状态
                if (currentLoopNum <= totalLoopCount) {
                    if (currentExecNum >= scriptList.size()) {
                        currentLoopNum += 1;
                        currentExecNum = 0;
                        execHandler.sendEmptyMessage(SOCKET_MSG_EXEC);
                    } else {
                        if (SpUtil.getKeyBoolean(PlatformConfig.NEED_CHECK_ACTIVE, false)) {//是否需要检测游戏活跃时长
                            if (!needDelay) {
                                if (((System.currentTimeMillis() - Constants.getOrderTime)) > ((SpUtil.getKeyLong(PlatformConfig.ACTIVE_TIME, 15)) * 60 * 1000)) {
                                    needDelay = true;
                                    Constants.getOrderTime = System.currentTimeMillis();
                                }
                            } else {
                                ScriptUtil.dealWithDelayLimit();
                            }
                        }
                        String instructStr = scriptList.get(currentExecNum);
                        currentExecNum += 1;
                        delayTime = 100;
                        if (!TextUtils.isEmpty(instructStr)) {//指令执行
                            execInstruct(instructStr, true);
                        } else {
                            setSleep(0, true);
                        }
                    }
                } else {
                    SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, true);
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();
                }
            } else {
                if (RecordFloatView.bigFloatState != RecordFloatView.EXEC) {//非执行 正常播放
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 指令的执行
     * 1
     *
     * @param instructStr
     */
    private static void execInstruct(String instructStr, boolean continueExec) {
        if (recordScript) {
            tempScriptList.add(instructStr);
        }
        instructStr = InstructUtil.script2Cmd(instructStr);
        instructStr = instructStr.replace("\uFEFF", "").trim();

        LLog.e("当前执行的指令:" + instructStr);
        if (instructStr.startsWith(ScriptConstants.ECHO_CMD)) {          //解释说明，不执行
        } else if (instructStr.startsWith(GameConfig.CONFIG)) {
            ScriptUtil.dealWithGameConfig(instructStr);                 //脚本配置
        } else if (instructStr.startsWith(ScriptConstants.START_APP_SCRIPT)) {
            ScriptUtil.dealWithStartApp(instructStr);                   //启动APP
        } else if (instructStr.startsWith(ScriptConstants.KILL_APP_SCRIPT)) {
            ScriptUtil.dealWithKillApp(instructStr);                    //杀死APP
        } else if (instructStr.startsWith(ScriptConstants.INSTALLSLIENT_APP_SCRIPT)) {
            ScriptUtil.dealWithInstallApp(instructStr);                 //安装APP
            return;
        } else if (instructStr.startsWith(ScriptConstants.CHECK_APP_SCRIPT)) {
            ScriptUtil.dealWithCheckApp(instructStr);                   //检测APP
            return;
        } else if (instructStr.startsWith(ScriptConstants.INSTALLSLIENT_APK_SCRIPT)) {
            ScriptUtil.dealWithInstallApk(instructStr);                 //安装APK
        } else if (instructStr.startsWith(ScriptConstants.UNINSTALLSLIENT_APP_SCRIPT)) {
            ScriptUtil.dealWithUninstallApp(instructStr);               //卸载APP
        } else if (instructStr.startsWith(ScriptConstants.CAPTURE_SCRIPT)) {
            ScriptUtil.dealWithCapture(instructStr);                    //截屏操作
        }  else if (instructStr.startsWith(ScriptConstants.TAP_PICTURE_SCRIPT)) {
            if (instructStr.split(ScriptConstants.SPLIT).length == 4) {
                ScriptUtil.dealWithTapPictureSingleMatch(instructStr);//点图片 (单模板匹配)
            } else if (instructStr.split(ScriptConstants.SPLIT).length == 6) {
                ScriptUtil.dealWithPixTapPicture(instructStr);         //点图片 (多重算法)
            } else {
                ScriptUtil.dealWithTapPicture(instructStr);             //点图片
            }
        } else if (instructStr.startsWith(ScriptConstants.TAP_PICTURE_VECTOR_SCRIPT)) {
            if (instructStr.split(ScriptConstants.SPLIT).length == 6) {
                ScriptUtil.dealWithTapPicVectorSingleMatch(instructStr);//点击图片偏移量 (单模板匹配)
            } else if (instructStr.split(ScriptConstants.SPLIT).length == 8) {
                ScriptUtil.dealWithPixTapPictureVector(instructStr);   //点击图片偏移量 (多重算法)
            } else {
                ScriptUtil.dealWithTapPictureVector(instructStr);      //点击图片偏移量
            }
        }  else if (instructStr.startsWith(ScriptConstants.RANDOM_USERNAME_SCRIPT)) {
            ScriptUtil.dealWithRandom(instructStr);                    //生成随机用户名
            return;
        } else if (instructStr.startsWith(ScriptConstants.USERNAME_SCRIPT)) {
            ScriptUtil.dealWithCreatUserName();                       //生成用户名
            return;
        } else if (instructStr.startsWith(ScriptConstants.RANDOM_PASSWORD_SCRIPT)) {
            ScriptUtil.dealWithRandom(instructStr);                    //生成随机密码
            return;
        } else if (instructStr.startsWith(ScriptConstants.SPECIFY_TYPE_PASSWORD_SCRIPT)) {
            ScriptUtil.dealWithTypePassword(instructStr);             //生成指定格式密码
            return;
        } else if (instructStr.startsWith(ScriptConstants.RANDOM_NICKNAME_SCRIPT)) {
            ScriptUtil.dealWithRandom(instructStr);                    //生成随机昵称
            return;
        } else if (instructStr.startsWith(ScriptConstants.EXECUTE_PRE_SCRIPT)) {
            ScriptUtil.dealWithPreCmd(instructStr);                    //执行上几句
        } else if (instructStr.startsWith(ScriptConstants.IF_DEF_CMD)) {
            if (instructStr.contains(ScriptConstants.SINGLE_MATCH)) {
                ScriptUtil.dealWithDefinedSingleMatchCmd(instructStr);//定义 DEFIND (单模板匹配)
            } else if (instructStr.split(ScriptConstants.SPLIT).length == 4) {
                ScriptUtil.dealWithCvDefinedCmd(instructStr);          //定义 DEFIND (模板匹配)
            } else if (instructStr.split(ScriptConstants.SPLIT).length == 6) {
                ScriptUtil.dealWithPixDefinedCmd(instructStr);         //定义 DEFIND (多重算法)
            } else {
                ScriptUtil.dealWithDefinedCmd(instructStr);            //定义 DEFIND
            }
        } else if (instructStr.startsWith(ScriptConstants.IF_NO_DEF_CMD)) {
            if (instructStr.split(ScriptConstants.SPLIT).length == 6) {
                ScriptUtil.dealWithPixNoDefinedCmd(instructStr);       //定义 NODEFIND (多重算法)
            } else if (instructStr.contains(ScriptConstants.SINGLE_MATCH)) {
                ScriptUtil.dealWithNoDefinedSingleMatch(instructStr); //定义 NODEFIND (单模板匹配)
            } else {
                ScriptUtil.dealWithNoDefinedCmd(instructStr);          //定义 NODEFIND
            }
        } else if (instructStr.startsWith(ScriptConstants.KEYVALUE_SCRIPT)) {
            ScriptUtil.dealWithKeyValue(instructStr);                  //键值
        } else if (instructStr.startsWith(ScriptConstants.CHECK_STICK_SCRIPT)) {
            ScriptUtil.dealWithCheckStick(instructStr);               //校验卡屏状态
            return;
        } else if (instructStr.startsWith(ScriptConstants.CALL_POLICE)) {
            ScriptUtil.dealWithAlert(instructStr);                      //报警
            return;
        } else if (instructStr.startsWith(ScriptConstants.CHANGE_DEVICE_INFO_SCRIPT)) {
            ScriptUtil.dealWithChangeDeviceCheck();                   //修改设备信息
        } else if (instructStr.startsWith(ScriptConstants.CHANGE_OWN_DEVICE_INFO_SCRIPT)) {
            ScriptUtil.dealWithGetOwnDeviceInfo();                   //生成本机设备信息
        } else if (instructStr.startsWith(ScriptConstants.CHANGE_IP_SCRIPT)) {
            ScriptUtil.dealWithChangeIp(instructStr);                  //切换IP
        } else if (instructStr.startsWith(ScriptConstants.CHECK_VPN_STATUS_SCRIPT)) {
            ScriptUtil.dealWithCheckVpnStatus();                      //检验VPN连接状态
            return;
        } else if (instructStr.startsWith(ScriptConstants.RETURN_USER_INFO_SCRIPT)) {
            ScriptUtil.dealWithReturnUserInfo();                     //返回用户信息
            return;
        } else if (instructStr.startsWith(ScriptConstants.RETURN_DEVICE_INFO_SCRIPT)) {
            ScriptUtil.dealWithReturnDeviceInfo();                   //返回设备信息
            return;
        } else if (instructStr.startsWith(ScriptConstants.RETURN_IP_INFO_SCRIPT)) {
            ScriptUtil.dealWithReturnIpInfo(0);                //返回IP信息
            return;
        } else if (instructStr.startsWith(ScriptConstants.GET_PHONE_NUMBER_SCRIPT)) {
            ScriptUtil.dealWithGetPhoneNumber(instructStr);           //获取手机号
            return;
        } else if (instructStr.startsWith(ScriptConstants.GET_PHONE_NUMBER_ADMIN_SCRIPT)) {
            ScriptUtil.dealWithGetPhoneNumberAdmin(instructStr);      //获取手机号账号
            return;
        } else if (instructStr.startsWith(ScriptConstants.GET_PHONE_NUMBER_DEFINED)) {
            ScriptUtil.dealWithGetPhoneNumberAdmin(instructStr);      //获取指定手机号
            return;
        } else if (instructStr.contains(ScriptConstants.GET_PHONE_CODE_SCRIPT)) {
            ScriptUtil.dealWithGetPhoneCode(instructStr);              //获取验证码
            return;
        } else if (instructStr.contains(ScriptConstants.GET_PHONE_CODE_PASSWORD_SCRIPT)) {
            ScriptUtil.dealWithGetPhoneCodePassWord(instructStr);     //获取验证码密码
            return;
        } else if (instructStr.startsWith(ScriptConstants.RELEASE_PHONE_NUMBER_SCRIPT)) {
            ScriptUtil.dealWithRelesePhoneNumber();                   //释放手机号
        } else if (instructStr.startsWith(ScriptConstants.INSERT_PHONE_NUMBER_SCRIPT)) {
            ScriptUtil.dealWithInsertPhoneNumber();                   //插入手机号
        } else if (instructStr.contains(ScriptConstants.INSERT_KEYBOARD_PHONE_NUMBER_SCRIPT)) {
            ScriptUtil.dealWithInsertKeyBordPhoneNumber();           //插入自定义键盘手机号
        } else if (instructStr.contains(ScriptConstants.INSERT_KEYBOARD_VER_CODE_SCRIPT)) {
            ScriptUtil.dealWithInsertKeyBordVerCode();                //插入自定义键盘验证码
        } else if (instructStr.startsWith(ScriptConstants.INSERT_PHONE_CODE_SCRIPT)) {
            ScriptUtil.dealWithInsertPhoneCode(instructStr);          //插入手机验证码
        } else if (instructStr.startsWith(ScriptConstants.LOOP_COUNT_SCRIPT)) {
            ScriptUtil.dealWithLoopCount(instructStr);                 //处理循环次数
        } else if (instructStr.startsWith(ScriptConstants.TAP_IMMEDIATELY_SCRIPT)) {
            ScriptUtil.dealWithTapImmediately(instructStr);           //处理直接点击
        } else if (instructStr.startsWith(ScriptConstants.TAP_AREA_SCRIPT)) {
            ScriptUtil.dealWithTapArea(instructStr);                   //点击区域
        } else if (instructStr.startsWith(ScriptConstants.TEXT_CMD)) {
            ScriptUtil.dealWithNormalText(instructStr);                //输入普通文本
        } else if (instructStr.startsWith(ScriptConstants.INPUT_CMD)) {
            ScriptUtil.dealWithChineseText(instructStr);               //输入中文文本
        } else if (instructStr.startsWith(ScriptConstants.INPUT_SCRIPT_SPLIT)) {
            ScriptUtil.dealWithInputWithhao(instructStr);               //带号输入
        } else if (instructStr.startsWith(ScriptConstants.MOVE_FLOAT_VIEW_SCRIPT)) {
            ScriptUtil.dealWithFloatView(instructStr);                 //移动悬浮按钮
        } else if (instructStr.startsWith(ScriptConstants.ASSISTANCE_SCRIPT)) {
            ScriptUtil.dealWithAssistant(instructStr);                 //人工协助
            return;
        } else if (instructStr.startsWith(ScriptConstants.REMOTE_ASSISTANCE_SCRIPT)) {
            ScriptUtil.dealWithRemoteAssistant();                     //远程协助
            return;
        } else if (instructStr.startsWith(ScriptConstants.STATUSBAR_SCRIPT)) {
            ScriptUtil.dealWithStatusBar(instructStr);                 //状态栏处理
        } else if (instructStr.startsWith(ScriptConstants.SLEEP_CMD)) {
            delayTime = ScriptUtil.dealWithSleep(instructStr);          //睡眠
        } else if (instructStr.startsWith(ScriptConstants.RANDOM_CLICK_CMD)) {
            ScriptUtil.dealWithRandomTap(instructStr);                 //处理随机点击
        } else if (instructStr.startsWith(ScriptConstants.RANDOM_SWIPE_CMD)) {
            ScriptUtil.dealWithRandomSwipe(instructStr);               //处理随机滑动
        } else if (instructStr.startsWith(ScriptConstants.MODULE_START_EXECUTION)) {
            ScriptUtil.dealWithModuleStart(instructStr);               //模块开始执行
        } else if (instructStr.startsWith(ScriptConstants.MODULE_STOP_EXECUTION)) {
            ScriptUtil.dealWithModuleStop(instructStr);                //模块结束执行
            return;
        } else if (instructStr.startsWith(ScriptConstants.DELETE_FILE_SCRIPT)) {
            ScriptUtil.dealWithDeleteFile(instructStr);                //删除文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.COPY_FILE_SCRIPT)) {
            ScriptUtil.dealWithCoPyFile(instructStr);                  //复制文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.UPLOAD_FILE_SCRIPT)) {
            ScriptUtil.dealWithUploadFile(instructStr);                //上传文件
        } else if (instructStr.startsWith(ScriptConstants.GET_VERIFYCODE_SCRIPT)) {
            ScriptUtil.dealWithGetVerifyCode();                       //获取校验码
            return;
        }else if (instructStr.startsWith(ScriptConstants.CAPTURE_VERIFY_CODE_PIC_SCRIPT)) {
            ScriptUtil.dealWithCaptureVerifyCodePic(instructStr);    //截取校验码图片
        } else if (instructStr.startsWith(ScriptConstants.UPLOAD_VERIFYCODE_PIC_SCRIPT)) {
            ScriptUtil.dealWithUploadVerifyCodePic(instructStr);      //上传校验码图片
            return;
        } else if (instructStr.startsWith(ScriptConstants.UPLOAD_COORDINATION_VERIFYCODE_PIC_SCRIPT)) {
            ScriptUtil.dealWithUploadVerifyCodePicWithCoordination(instructStr);//上传坐标校验码图片
            return;
        } else if (instructStr.startsWith(ScriptConstants.COPY_FILE_SCRIPT_NEED_ROOT)) {
            ScriptUtil.dealWithCoPyFileNeedRoot(instructStr);        //ROOT方式复制文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.MAKE_DIRECTORY_SCRIPT)) {
            ScriptUtil.dealWithMakeDirectoryNeedRoot(instructStr);  //ROOT方式创建文件夹
            return;
        } else if (instructStr.startsWith(ScriptConstants.GIVE_FILE_RW_PERMISION_SCRIPT)) {
            ScriptUtil.dealWithGiveFileRWPermision(instructStr);    //ROOT方式赋予文件读写权限
            return;
        } else if (instructStr.startsWith(ScriptConstants.CREATE_EMAIL_SCRIPT)) {
            ScriptUtil.dealWithCreateEmail(instructStr);             //创建邮箱账号
            return;
        } else if (instructStr.startsWith(ScriptConstants.ANDROID_OS_CMD_SCRIPT)) {
            ScriptUtil.dealWithAndroidCmd(instructStr);               //安卓CMD指令
        } else if (instructStr.startsWith(ScriptConstants.GET_ID_CARD_INFO_SCRIPT)) {
            ScriptUtil.dealWithGetIdCardInfo(instructStr);                      //获取身份证信息
            return;
        } else if (instructStr.startsWith(ScriptConstants.INSERT_ID_CARD_NAME_SCRIPT)) {
            ScriptUtil.dealWithInsertIdCardName();                   //插入身份证姓名
            return;
        } else if (instructStr.startsWith(ScriptConstants.INSERT_ID_CARD_NUMBER_SCRIPT)) {
            ScriptUtil.dealWithInsertIdCardNumber();                 //插入身份证号码
            return;
        } else if (instructStr.startsWith(ScriptConstants.DIRECT_REQUEST_LINK_SCRIPT)) {
            ScriptUtil.dealWithDirectRequest(instructStr);            //直接请求链接
            return;
        } else if (instructStr.startsWith(ScriptConstants.BROWSER_OPEN_LINK_SCRIPT)) {
            ScriptUtil.dealWithBrowserOpen(instructStr);              //浏览器打开链接
            return;
        } else if (instructStr.startsWith(ScriptConstants.PROXY_REQUEST_LINK_SCRIPT)) {
            ScriptUtil.dealWithProxyLink(instructStr);                //代理请求链接
        } else if (instructStr.startsWith(ScriptConstants.MD5_COMPARE_SCRIPT)) {
            ScriptUtil.dealWithMD5CompareFile(instructStr);           //检测文件是否存在
            return;
        } else if (instructStr.startsWith(ScriptConstants.ORDER_CLICK_SCRIPT)) {
            ScriptUtil.dealWithOrderClick(instructStr);               //顺序点击
        } else if (instructStr.startsWith(ScriptConstants.RETURN_ORDER_RESULT)) {
            ScriptUtil.dealWithReturnOrderResult(instructStr);       //返回订单结果
            return;
        } else if (instructStr.startsWith(ScriptConstants.COPY_APPLICATION_FILE_SCRIPT)) {
            ScriptUtil.dealWithCoPyApplicationFile(instructStr);    //复制应用文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.RESTORE_APPLICATION_FILE_SCRIPT)) {
            ScriptUtil.dealWithRestoreApplicationFile(instructStr); //还原应用文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.GET_OWN_SCRIPT)) {
            ScriptUtil.dealWithGetOwn(instructStr);                   //获取所有者
            return;
        } else if (instructStr.startsWith(ScriptConstants.MODIFY_OWN_SCRIPT)) {
            ScriptUtil.dealWithModifyOwn(instructStr);                //更改所有者
            return;
        } else if (instructStr.startsWith(ScriptConstants.UPLOAD_CAPTURE_SCRIPT)) {
            ScriptUtil.dealWithUploadCaptureImg(instructStr);        //上传截图
            return;
        } else if (instructStr.startsWith(ScriptConstants.DO_ZIP_SCRIPT)) {
            ScriptUtil.dealWithDoZip(instructStr);                    //压缩文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.DO_UNZIP_SCRIPT)) {
            ScriptUtil.dealWithDoUnZip(instructStr);                  //解压缩文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.ZIP_DO_ZIP_SCRIPT)) {
            ScriptUtil.dealWithZipDoZip(instructStr);                 //ZIP压缩文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.ZIP_DO_UNZIP_SCRIPT)) {
            ScriptUtil.dealWithZipDoUnZip(instructStr);              //ZIP解压缩文件
            return;
        } else if (instructStr.startsWith(ScriptConstants.DELETE_CONTANCLIST_SCRIPT)) {
            ScriptUtil.dealWithDeleteContactList();                  //删除通讯录
        } else if (instructStr.startsWith(ScriptConstants.CREATE_CONTANCLIST_SCRIPT)) {
            ScriptUtil.dealWithCreateContactList();                  //生成通讯录
        } else if (instructStr.startsWith(ScriptConstants.UPLOADTENSORFLOW_SCRIPT)) {
            ScriptUtil.dealWithUploadTensorflow(instructStr);        //上传模板素材
            return;
        } else if (instructStr.startsWith(ScriptConstants.BATCH_DELETE_FILE_SCRIPT)) {
            ScriptUtil.dealWithBatchDeleteFile(instructStr);         //批量删除
        } else if (instructStr.startsWith(ScriptConstants.CLEAN_CACHE_FILE_SCRIPT)) {
            ScriptUtil.dealWithCleanCacheFile();                     //清除缓存文件
        } else if (instructStr.startsWith(ScriptConstants.GIVE_APPLICATION_FLOAT_PERMISSION_SCRIPT)) {
            ScriptUtil.dealWithApplicationPermission(instructStr);   //跳转应用授权页面
        } else if (instructStr.startsWith(ScriptConstants.GIVE_SYSTEM_SETTING_SCRIPT)) {
            ScriptUtil.dealWithSystemSetting();   //跳转系统设置页面
        } else if (instructStr.startsWith(ScriptConstants.HUADONG_ELEMENT_MATCH_SCRIPT)) {
            ScriptUtil.dealWithHuaDongCmdNew(instructStr);            //滑动验证码识别
            return;
        } else if (instructStr.startsWith(ScriptConstants.BLINKPOINT__SCRIPT)) {
            ScriptUtil.dealWithBlinkPoint(instructStr);            //闪点验证码识别
            return;
        } else if (instructStr.startsWith(ScriptConstants.FILL_CLIPBOARD_SCRIPT)) {
            ScriptUtil.dealWithFillClipboard(instructStr);            //填充粘贴板
        } else if (instructStr.startsWith(ScriptConstants.CHECK_IP_ADDRESS_SCRIPT)) {
            ScriptUtil.dealWithCheckIpAddress();                      //校验IP地址
            return;
        } else if (instructStr.startsWith(ScriptConstants.CLOSE_VPN_SCRIPT)) {
            ScriptUtil.dealWithVpnClose();                            //断开VPN
        } else if (instructStr.startsWith(ScriptConstants.TESSERACT_SCRIPT)) {
            ScriptUtil.dealWithTesseract(instructStr);                 //数据截图
            return;
        } else if (instructStr.startsWith(ScriptConstants.WEIXIN_SCRIPT)) {
            ScriptUtil.dealWithSendMessage(instructStr);              //发送微信消息
            return;
        } else if (instructStr.startsWith(ScriptConstants.HIDE_FLOAT_VIEW_SCRIPT)) {
            ScriptUtil.dealWithHideFloatView();                      //隐藏悬浮球
        } else if (instructStr.startsWith(ScriptConstants.SHOW_FLOAT_VIEW_SCRIPT)) {
            ScriptUtil.dealWithShowFloatView();                      //显示悬浮球
        } else if (instructStr.startsWith(ScriptConstants.STATIC_TIME_SCRIPT)) {
            ScriptUtil.dealWithStatistic(instructStr);                      //统计
        } else if (instructStr.startsWith(ScriptConstants.CLICK_TEXT)) {
            ScriptUtil.dealWithClickText(instructStr);                      //点击文字
        } else {//点击，按键,滑动,文本
            sendInstruct(instructStr, continueExec);
        }
        setSleep(delayTime, continueExec);
    }


    /**
     * 设置睡眠时间
     */
    private static void setSleep(long time, boolean continueExec) {
        if (continueExec) {
            sendMessage(SOCKET_MSG_EXEC, time);
        } else {
            SystemClock.sleep(time);
        }
    }


    /**
     * 设置睡眠时间  供外部类调用
     */
    public static void setDelay(long time) {
        delayTime = time;
    }

    /**
     * 继续播放脚本  供外部类调用
     */
    public static void continueExec() {
        sendMessage(SocketUtil.SOCKET_MSG_EXEC, 1000);
    }

    /**
     * 发送消息  供外部类调用
     *
     * @param msgType   消息类型
     * @param delayTime 延迟时间
     */
    public static void sendMessage(int msgType, long delayTime) {
        if (execHandler != null) {
            execHandler.sendEmptyMessageDelayed(msgType, delayTime);
        }
    }

    /**
     * 清空消息队列  供外部类调用
     */
    public static void clearMessage() {
        if (execHandler != null) {
            for (int i = 0; i < SOCKET_TOTAL_MSG_TYPE; i++) {
                execHandler.removeMessages(i);
            }
        }
    }

    /**
     * 执行指令  供外部类调用
     *
     * @param instructStr
     */
    public static void execFromOut(String instructStr) {
        execInstruct(instructStr, false);
    }


    /**
     * 执行指令  供外部类调用
     *
     * @param instructStr
     */
    public static void execFromOut(String instructStr, boolean needCheck) {
        execInstruct(instructStr, needCheck);
    }

    /**
     * 接收指令
     */
    public static void receiveInstruct() {
        ScriptApplication.getService().execute(() -> {
            try {
                server = new ServerSocket(server_port);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Socket client = null;
            while (flag) {
                try {
                    client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String str = in.readLine();
                    Log.e("server", "receiveInstruct:" + str);
                    CommandUtil.commandConversion(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 发送指令  脚本APP唯一发送指令出口
     *
     * @param command         指令
     * @param needCheckScreen 是否需要检测界面弹窗
     */
    public static void sendInstruct(String command, boolean needCheckScreen) {
        if (needCheckScreen) {
            if (ScriptUtil.getPictureDetected()) {//检测到弹窗
                SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
                currentExecNum--;
            } else {
                flushData(command);
            }
        } else {
            flushData(command);
        }
    }


    /**
     * 发送数据流
     *
     * @param command 指令
     */
    public static void flushData(String command) {
        if (command.contains(ScriptConstants.TAP_CMD)) {
            //ScriptUtil.checkInTrap(command);  //检测卡屏
        }
        if (isRooted) {
            if (command.startsWith(ScriptConstants.TAP_CMD + ScriptConstants.SPLIT) || command.startsWith(ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT)
                    || command.startsWith(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT) || command.startsWith(ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT)) {
                showGesture(command); //展示手势轨迹
                if (TextUtils.isEmpty(SpUtil.getKeyString(PlatformConfig.PHONE_NAME, ""))) {
                    RecordFloatView.updateMessage("手机标识有误，请查看!");
                    return;
                }
                if (SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "").startsWith("zong") || SpUtil.getKeyBoolean(PlatformConfig.CURRENT_ADB_CMD, false)) {
                    command = InstructUtil.script2SuCmd(command);
                    CMDUtil.execShell(command);
                } else {
                    EventUtil.sendEvent(command);
                }
            } else {
                command = InstructUtil.script2SuCmd(command);
                CMDUtil.execShell(command);
            }
        } else {
            pw.println(command + ScriptConstants.SPLIT);
            pw.flush();
        }
    }


    /**
     * 发送消息  展示手势轨迹
     *
     * @param command 指令
     */
    public static void showGesture(String command) {
        Message message = Message.obtain();
        message.obj = command;
        message.what = 2;
        BaseFloatView.sFloatHandler.sendMessage(message);
    }


    /**
     * 关闭socket
     */
    private static void closeSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (pw != null) {
                pw.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
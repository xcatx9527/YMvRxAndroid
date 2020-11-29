package com.xile.script.http.helper.manager.mina;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.ExecEnum;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.http.common.HttpConstants;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.manager.bean.*;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.ScreenUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.game.InsertAreaRoleUtil;
import com.xile.script.utils.script.ScriptUtil;
import script.tools.config.ScriptConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.xile.script.base.ui.view.floatview.RecordFloatView.updateMessage;
import static com.xile.script.config.Constants.sGamesOrderInfo;

/**
 * date: 2017/5/9 18:11
 *
 * @scene 手游交易订单处理
 */
public class NioTask extends Handler {
    public static final int MSG_TYPE_GET_ORDER = 1;//获取订单
    public static final int MSG_TYPE_REQUEST_UPLOAD_IMG = 2;//客户端发送一个准备上传图片的请求
    public static final int MSG_TYPE_CALL_ALERT = 3;//客户端发送一个报警请求
    public static final int MSG_TYPE_UPLOAD_IMG = 4;//客户端发送一个上传游戏截图
    public static final int MSG_TYPE_ALL_UPLOAD_FINISH = 5;//客户端发送一个全部截图上传成功消息
    public static final int MSG_TYPE_ROBOT_STATUS_RE = 6;//客户端应答一个检测状态消息给服务器端
    public static final int MSG_TYPE_ASSISTANCE_TIME_CHECK = 7;//人工协助时长检测
    public static final int MSG_TYPE_MINA_RESET = 8;//Mina重新初始化
    public static final int MSG_TYPE_RESEND_MSG = 9; //如果未收到服务器响应  重发消息
    public static final int MSG_TYPE_ROBOT_ALIVE_RE = 10; //客户端应答一个心跳消息给服务器端
    public static final int MSG_TYPE_DEAL_WITH_ORDER = 11; //处理订单
    public static final int MSG_TYPE_UPLOAD_SINGLE_IMG = 12; //客户端上传单张图片(区服识别/角色识别等)
    public static final int MSG_TYPE_CLICK_CODE = 13; //客户端点击获取验证码
    public static final int MSG_TYPE_REQUEST_SERVER_RESULT = 14; //客户端请求服务器结果(区服识别结果/角色识别结果/手机验证码获取)

    public static final int NIO_TOTAL_MSG_TYPE = 14;

    private static NioTask instance;
    public List<File> sUploadImgsList = new ArrayList<>();//需要上传的图片
    public int currentUploadNum = 0;//当前上传的图片序号
    public boolean isUploadErrorImages = false;//是否是传缺失的图片
    public BaseSessionObject currentSendObject;//当前要发送的 message Object
    public String currentMsgType;//当前要发送的 message type
    public long currentTimeStamp = 0;//收到一个re时得到的时间戳
    public boolean isMsgResponsed = true;//消息是否得到应答

    private NioTask() {
    }

    public static synchronized NioTask getInstance() {
        if (instance == null) {
            instance = new NioTask(Looper.getMainLooper());
        }
        return instance;
    }

    public void reset() {
        currentUploadNum = 0;
        isUploadErrorImages = false;
        currentSendObject = null;
        currentMsgType = null;
        currentTimeStamp = 0;
        if (sUploadImgsList != null) {
            sUploadImgsList.clear();
        }
    }

    public static void release() {
        instance = null;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START && Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            switch (msg.what) {
                case MSG_TYPE_GET_ORDER:  //获取订单
//                    LLog.d("轮询服务器订单中...");
                    getOrder();
                    Message message = Message.obtain();
                    message.what = MSG_TYPE_GET_ORDER;
                    message.obj = System.currentTimeMillis();
                    sendMessageDelayed(message, 30 * 1000);
                    break;

                case MSG_TYPE_REQUEST_UPLOAD_IMG:  //客户端发送一个准备上传图片的请求
                    if (sGamesOrderInfo != null) {
                        String names = FileHelper.getUploadImgNames(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + sGamesOrderInfo.getTaskData());
                        if (TextUtils.isEmpty(names)) {
                            //报警
                            sendMessage(MSG_TYPE_CALL_ALERT, 1000);
                            return;
                        }
                        RequestUploadImg requestUploadImg = new RequestUploadImg(NioHelper.SEND_IMAGE_LIST, NioHelper.TASK_TYPE_8003, NioHelper.SYSTEM_TYPE,
                                sGamesOrderInfo.getTaskType(), sGamesOrderInfo.getTaskData(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), names, String.valueOf(System.currentTimeMillis()));
                        NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_8003, requestUploadImg);
                    } else {
                        updateMessage("内存中订单为空,发送准备上传图片的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_CALL_ALERT:  //客户端发送一个报警请求
                    if (sGamesOrderInfo != null) {
                        String alertType = SpUtil.getKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR);
                        if (TextUtils.isEmpty(alertType)) {
                            alertType = AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR;
                        }
                        String alertDesc = AlertConfig.getAlertDesc(alertType);
                        File file = FileHelper.getUploadAlertImg(SpUtil.getKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, ""));
                        OrderHandleResult orderHandleResult = null;
                        if (file != null) {
                            String fileName = file.getName();
                            String fileData = FileHelper.getUploadFile(file);
                            orderHandleResult = new OrderHandleResult(NioHelper.SEND_ERROR, NioHelper.TASK_TYPE_8007, NioHelper.SYSTEM_TYPE,
                                    sGamesOrderInfo.getTaskType(), sGamesOrderInfo.getTaskData(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"),
                                    alertType, alertDesc, String.valueOf(System.currentTimeMillis()), fileName, fileData);
                        } else {
                            orderHandleResult = new OrderHandleResult(NioHelper.SEND_ERROR, NioHelper.TASK_TYPE_8007, NioHelper.SYSTEM_TYPE,
                                    sGamesOrderInfo.getTaskType(), sGamesOrderInfo.getTaskData(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"),
                                    alertType, alertDesc, String.valueOf(System.currentTimeMillis()), "", "");
                        }

                        NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_8007, orderHandleResult);
                    } else {
                        updateMessage("内存中订单为空,上传报警截图失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_UPLOAD_IMG:  //客户端发送一个上传游戏截图
                    if (sGamesOrderInfo != null) {
                        if (currentUploadNum == 0 && !isUploadErrorImages) {
                            sUploadImgsList = FileHelper.getFiles(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + sGamesOrderInfo.getTaskData());
                            if (sUploadImgsList == null || sUploadImgsList.size() == 0) {
                                //报警
                                LLog.e("订单对应的截图为空!");
                                updateMessage("订单对应的截图为空!");
                                sendMessage(MSG_TYPE_CALL_ALERT, 1000);
                                return;
                            }
                            LLog.e("开始上传图片!");
                            updateMessage("开始上传图片!");
                        }
                        if (currentUploadNum < sUploadImgsList.size()) {
                            Constants.currentRobotState = CaptureConfig.ROBOT_IS_UPLOADING_IMAGES;
                            String fileData = FileHelper.getUploadFile(sUploadImgsList.get(currentUploadNum));
                            UploadImg uploadImg = new UploadImg(NioHelper.SEND_IMAGE, NioHelper.TASK_TYPE_8005, NioHelper.SYSTEM_TYPE, sGamesOrderInfo.getTaskType(),
                                    sGamesOrderInfo.getTaskData(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), String.valueOf(System.currentTimeMillis()),
                                    sUploadImgsList.get(currentUploadNum).getName(), fileData);

                            NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_8005, uploadImg);
                        } else {
                            //全部图片上传完毕
                            LLog.e("全部图片上传完毕! 图片数量:" + sUploadImgsList.size());
                            updateMessage("全部图片上传完毕! 图片数量:" + sUploadImgsList.size());
                            currentUploadNum = 0;
                            sUploadImgsList.clear();
                            sendMessage(MSG_TYPE_ALL_UPLOAD_FINISH, 1000);
                        }
                    } else {
                        updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_ALL_UPLOAD_FINISH:  //客户端发送一个全部截图上传成功消息
                    if (sGamesOrderInfo != null) {
                        SendUploadAllFinished sendUploadAllFinished = new SendUploadAllFinished(NioHelper.SUCC_IMAGE_LIST, NioHelper.TASK_TYPE_8009,
                                sGamesOrderInfo.getTaskType(), sGamesOrderInfo.getTaskData(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"),
                                String.valueOf(System.currentTimeMillis()));
                        NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_8009, sendUploadAllFinished);
                    } else {
                        updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_ROBOT_STATUS_RE:  //客户端应答一个检测状态消息给服务器端
                    if (sGamesOrderInfo != null) {
                        SendCheckRobotStateResponse sendCheckRobotState = new SendCheckRobotStateResponse(NioHelper.SEND_WAITING_RE, NioHelper.TASK_TYPE_6002, "0",
                                sGamesOrderInfo.getTaskType(), sGamesOrderInfo.getTaskData(), Constants.currentRobotState,
                                CaptureConfig.getRobotStateDesc(Constants.currentRobotState));
                        NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_6002, sendCheckRobotState);
                    } else {
                        updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_UPLOAD_SINGLE_IMG:  //上传单张图片(区服识别/角色识别等)
                    String reqType = SpUtil.getKeyString(PlatformConfig.CURRENT_OCR_TYPE, null);
                    if (sGamesOrderInfo != null) {
                        String filePath = null;
                        int screenWidth = ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[0];
                        int screenHeight = ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1];
                        if (InsertAreaRoleUtil.getInstance().UPLOAD_LARGE_AREA.equals(reqType) || InsertAreaRoleUtil.getInstance().UPLOAD_SMALL_AREA.equals(reqType)) {
                            filePath = Constants.SCRIPT_FOLDER_TEMP_AREA_UPLOAD_PATH.replace(".png", "") + "_" + screenWidth + "x" + screenHeight + ".png";
                        } else if (InsertAreaRoleUtil.getInstance().UPLOAD_ROLE.equals(reqType)) {
                            filePath = Constants.SCRIPT_FOLDER_TEMP_ROLE_UPLOAD_PATH.replace(".png", "") + "_" + screenWidth + "x" + screenHeight + ".png";
                        }
                        File file = new File(filePath);
                        if (!file.exists()) {
                            LLog.e("上传单张图片文件不存在:" + filePath);
                            BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR, "上传单张图片文件不存在_NULL");
                            return;
                        }
                        String fileData = FileHelper.getUploadFile(file);
                        UploadOcrImg uploadOcrImg = new UploadOcrImg(NioHelper.SEND_IMAGE_OCR_SINGLE, NioHelper.TASK_TYPE_8011, NioHelper.SYSTEM_TYPE, sGamesOrderInfo.getTaskType(),
                                sGamesOrderInfo.getTaskData(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), file.getName(), reqType, fileData, String.valueOf(System.currentTimeMillis()));
                        NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_8011, uploadOcrImg);
                    } else {
                        updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_CLICK_CODE:  //客户端点击获取验证码
                    if (sGamesOrderInfo != null) {
                        SendClickCode sendClickCode = new SendClickCode(NioHelper.SEND_CLICK_CODE, NioHelper.TASK_TYPE_8013, NioHelper.SYSTEM_TYPE, sGamesOrderInfo.getTaskType(),
                                sGamesOrderInfo.getTaskData(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), String.valueOf(System.currentTimeMillis()));
                        NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_8013, sendClickCode);
                    } else {
                        updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_REQUEST_SERVER_RESULT:  //请求服务器结果(区服识别结果/角色识别结果/手机验证码获取)
                    String askType = SpUtil.getKeyString(PlatformConfig.CURRENT_OCR_TYPE, null);
                    if (sGamesOrderInfo != null) {
                        RequestGetOcrResult requestGetOcrResult = new RequestGetOcrResult(NioHelper.SEND_REQUEST_RESULT, NioHelper.TASK_TYPE_6003, NioHelper.SYSTEM_TYPE, sGamesOrderInfo.getTaskType(),
                                sGamesOrderInfo.getTaskData(), askType, SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), String.valueOf(System.currentTimeMillis()));
                        NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_6003, requestGetOcrResult);
                    } else {
                        updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case MSG_TYPE_ASSISTANCE_TIME_CHECK:  //人工协助时长检测
                    if (Constants.PLAY_STATE == PlayEnum.PAUSE_PLAY) {
                        if (System.currentTimeMillis() - ScriptUtil.assistantTime > (35 * 60 * 1000)) {
                            BaseOrderHelper.resetData();
                        } else {
                            sendMessage(MSG_TYPE_ASSISTANCE_TIME_CHECK, 1 * 60 * 1000);
                        }
                    }
                    break;

                case MSG_TYPE_MINA_RESET:  //Mina重新初始化
                    //NioHelper.getInstance().init();
                    NioConnection.needReconnect = true;
                    break;

                case MSG_TYPE_RESEND_MSG:  //如果未收到服务器响应  重发消息
                    if (!TextUtils.isEmpty(currentMsgType) && currentSendObject != null && !TextUtils.isEmpty(currentSendObject.getTimeStamp())) {
                        if (!isMsgResponsed && (System.currentTimeMillis() - Long.parseLong(currentSendObject.getTimeStamp())) > 1 * 60 * 1000) {
                            if (currentTimeStamp < Long.parseLong(currentSendObject.getTimeStamp())) {
                                NioHelper.getInstance().sendMessage(currentMsgType, currentSendObject);
                            }
                        }
                    }
                    sendMessage(MSG_TYPE_RESEND_MSG, 30 * 1000);
                    break;

                case MSG_TYPE_ROBOT_ALIVE_RE:  //客户端应答一个心跳消息给服务器端
                    SendCheckAliveResponse sendCheckAliveResponse = new SendCheckAliveResponse(NioHelper.NEW_KEEP_ALIVE_RE, NioHelper.TASK_TYPE_1002, SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"));
                    NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_1002, sendCheckAliveResponse);
                    break;

                case MSG_TYPE_DEAL_WITH_ORDER:
                    if (msg.obj != null) {
                        if (msg.obj instanceof GamesOrderInfo) {
                            GamesOrderInfo orderInfo = (GamesOrderInfo) msg.obj;
                            dealWithOrder(orderInfo);
                        }
                    }
                    break;

                default:
                    break;
            }

        }
    }


    public NioTask(Looper mainLooper) {
        super(mainLooper);
        //NioHelper.getInstance().init();
        new NioConnection().start();
        Constants.currentRobotState = CaptureConfig.ROBOT_IS_FREE;
        SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
        Message message = Message.obtain();
        message.what = MSG_TYPE_GET_ORDER;
        sendMessageDelayed(message, 5000);
        sendEmptyMessageDelayed(MSG_TYPE_RESEND_MSG, 5000);
    }


    /**
     * 请求订单
     */

    public void getOrder() {
        if (SpUtil.getKeyBoolean(PlatformConfig.ISFREE, true)) {//处于空闲状态
            LLog.d("处于空闲中...");
            RequestGetTask getTaskBean = new RequestGetTask(NioHelper.GET_TASK, NioHelper.TASK_TYPE_8001, NioHelper.SYSTEM_TYPE, SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), String.valueOf(System.currentTimeMillis()));
            NioHelper.getInstance().sendMessage(NioHelper.TASK_TYPE_8001, getTaskBean);
        }
    }


    /**
     * 处理订单
     *
     * @param gamesOrderInfo
     */
    public void dealWithOrder(GamesOrderInfo gamesOrderInfo) {
        try {
            updateMessage("开始处理订单!");
            sGamesOrderInfo = gamesOrderInfo;
            currentUploadNum = 0;
            isUploadErrorImages = false;
            sUploadImgsList.clear();
            Constants.currentRobotState = CaptureConfig.ROBOT_IS_CAPTURING;
            FileUtil.saveFile(ScriptApplication.getGson().toJson(sGamesOrderInfo), Constants.MANAGER_ORDER_FOLDER_TEMP_PATH);
            //SerializableUtil.toSaveLog(Constants.GAMES_ORDER_LOG_PATH, ScriptApplication.getGson().toJson(sGamesOrderInfo));
            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, false);//置为非空闲
            SpUtil.putKeyBoolean(PlatformConfig.NEED_CHECK_ACTIVE, false);//需要检阅游戏时长
            SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);//订单处理状态先置为失败
            SpUtil.putKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, "");//滞空报警图片
            SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, true);

            if (!StringUtil.isEmpty(gamesOrderInfo.getGmUserName()) && !StringUtil.isEmpty(gamesOrderInfo.getGmPassword()) && !StringUtil.isEmpty(gamesOrderInfo.getAid())) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, gamesOrderInfo.getGmUserName());
                SpUtil.putKeyString(PlatformConfig.CURRENT_PASSWORD, gamesOrderInfo.getGmPassword());
            } else {
                updateMessage("从服务器获取到的用户名或密码或aid为空!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_ABOLISH_PASSWORD_ERROR);
                sendMessage(MSG_TYPE_CALL_ALERT, 1000);
                return;
            }
            if (TextUtils.isEmpty(gamesOrderInfo.getZoneNum()) || "-1".equals(gamesOrderInfo.getZoneNum()) || TextUtils.isEmpty(gamesOrderInfo.getQName())) {
                updateMessage("大区服ID未配置!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_REDO_SEVERID_ERROR);
                sendMessage(MSG_TYPE_CALL_ALERT, 1000);
                return;
            }
            if (TextUtils.isEmpty(gamesOrderInfo.getRoleLevel())) {
                updateMessage("角色等级为空!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_ABOLISH_SEVERWRITE_ERROR);
                sendMessage(MSG_TYPE_CALL_ALERT, 1000);
                return;
            }
            SpUtil.putKeyString(PlatformConfig.CURRENT_LARGE_AREA_NUMBER, String.valueOf(gamesOrderInfo.getQName()));
            SpUtil.putKeyString(PlatformConfig.CURRENT_SMALL_AREA_NUMBER, String.valueOf(gamesOrderInfo.getZoneNum()));
            SpUtil.putKeyString(PlatformConfig.CURRENT_ROLE_LEVEL, String.valueOf(gamesOrderInfo.getRoleLevel()));

            GamesOrderHelper.getInstance().getScript(HttpConstants.getManagerScriptUrl, gamesOrderInfo.getAid(), gamesOrderInfo.getChannelId());
        } catch (Exception e) {
            updateMessage("处理订单抛异常了!");
            BaseOrderHelper.resetData();
        }
    }


    /**
     * 更新消息状态
     *
     * @param msgType   消息类型
     * @param delayTime 延迟时间
     */
    public void sendMessage(int msgType, long delayTime) {
        if (instance != null) {
            instance.sendEmptyMessageDelayed(msgType, delayTime);
        }
    }

    /**
     * 更新消息状态
     *
     * @param msgType   消息类型
     * @param object    消息内容
     * @param delayTime 延迟时间
     */
    public void sendMessage(int msgType, Object object, long delayTime) {
        if (instance != null) {
            Message msg = Message.obtain();
            msg.what = msgType;
            msg.obj = object;
            instance.sendMessageDelayed(msg, delayTime);
        }
    }


}



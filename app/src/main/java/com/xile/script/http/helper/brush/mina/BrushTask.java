package com.xile.script.http.helper.brush.mina;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.PictureInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.ExecEnum;
import com.xile.script.config.GameConfig;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.brush.bean.BBaseSessionObject;
import com.xile.script.http.helper.brush.bean.BRequestGetAssistanceCmd;
import com.xile.script.http.helper.brush.bean.BRequestGetIdCard;
import com.xile.script.http.helper.brush.bean.BRequestGetTask;
import com.xile.script.http.helper.brush.bean.BRequestGetVerifyCode;
import com.xile.script.http.helper.brush.bean.BRequestMessage;
import com.xile.script.http.helper.brush.bean.BRequestPhoneNum;
import com.xile.script.http.helper.brush.bean.BSendAlert;
import com.xile.script.http.helper.brush.bean.BSendCheckAliveRe;
import com.xile.script.http.helper.brush.bean.BSendDeviceInfo;
import com.xile.script.http.helper.brush.bean.BSendIpInfo;
import com.xile.script.http.helper.brush.bean.BSendJobNumber;
import com.xile.script.http.helper.brush.bean.BSendKeychainFile;
import com.xile.script.http.helper.brush.bean.BSendModuleResult;
import com.xile.script.http.helper.brush.bean.BSendOrderResult;
import com.xile.script.http.helper.brush.bean.BSendPhoneReboot;
import com.xile.script.http.helper.brush.bean.BSendReleasePhoneNum;
import com.xile.script.http.helper.brush.bean.BSendRemoteAssistanceImg;
import com.xile.script.http.helper.brush.bean.BSendRequestUploadImg;
import com.xile.script.http.helper.brush.bean.BSendUploadAllImage;
import com.xile.script.http.helper.brush.bean.BSendUploadImage;
import com.xile.script.http.helper.brush.bean.BSendUserInfo;
import com.xile.script.http.helper.brush.bean.BSendVerifyCodeImg;
import com.xile.script.http.helper.brush.bean.BrushOrderInfo;
import com.xile.script.http.helper.other.MobileRegisterHelper;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.TimeUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.MD5Util;
import com.xile.script.utils.common.SerializableUtil;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.ScriptUtil;
import com.xile.script.utils.script.SocketUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import script.tools.config.ScriptConstants;

/**
 * date: 2017/5/9 18:11
 *
 * @scene 游戏优化订单处理
 */
public class BrushTask extends Handler {
    public static final int B_MSG_TYPE_GET_ORDER = 1;//获取订单
    public static final int B_MSG_TYPE_DEAL_WITH_ORDER = 2; //处理订单
    public static final int B_MSG_TYPE_ORDER_RESULT = 3; //客户端发送订单执行结果
    public static final int B_MSG_TYPE_UPDATE_MODULE = 4;//客户端发送更新模块信息
    public static final int B_MSG_TYPE_UPDATE_DEVICE_INFO = 5;//客户端发送更新设备信息
    public static final int B_MSG_TYPE_UPDATE_USER_INFO = 6;//客户端发送更新用户信息
    public static final int B_MSG_TYPE_UPDATE_IP_INFO = 7;//客户端发送更新IP信息
    public static final int B_MSG_TYPE_GET_ID_CARD = 8;//客户端发送获取身份证信息
    public static final int B_MSG_TYPE_GET_PHONE_NUMBER = 9;//客户端发送获取手机号信息
    public static final int B_MSG_TYPE_GET_MESSAGE = 10;//客户端发送获取短信
    public static final int B_MSG_TYPE_RELEASE_PHONE = 11;//客户端发送释放手机号
    public static final int B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE = 12;//客户端上传一个验证码截图
    public static final int B_MSG_TYPE_GET_VERY_CODE = 13;//客户端发送获取校验码
    public static final int B_MSG_TYPE_CALL_ALERT = 14;//客户端发送警报
    public static final int B_MSG_TYPE_UPLOAD_KEYCHAIN = 15;//客户端发送Keychain
    public static final int B_MSG_TYPE_ROBOT_ALIVE_RE = 16;//客户端应答机器人心跳消息
    public static final int B_MSG_TYPE_REMOTE_ASSISTANCE = 17;//客户端上传截图发送远程协助
    public static final int B_MSG_TYPE_GET_REMOTE_ASSISTANCE_CMD = 18;//客户端发送获取远程协助指令
    public static final int B_MSG_TYPE_REQUEST_UPLOAD_IMAGE = 19;//客户端发送请求上传图片
    public static final int B_MSG_TYPE_UPLOAD_ONE_IMAGE = 20;//客户端上传一张图片
    public static final int B_MSG_TYPE_ALL_IMAGE_UPLOADED = 21;//客户端发送所有图片上传成功
    public static final int B_MSG_TYPE_PHONE_REBOOT = 22;//客户端发送手机重启
    public static final int B_MSG_TYPE_COMMIT_JOB_NUMBER = 23;//客户端提交充值工号信息
    public static final int B_MSG_TYPE_DELIVER_PYTHON = 24; //传递python脚本
    public static final int B_MSG_TYPE_RESEND_MSG = 25; //如果未收到服务器响应  重发消息
    public static final int B_MSG_TYPE_MINA_RESET = 26;//Mina重新初始化
    public static final int B_MSG_TYPE_FILE_SYNC_CHECK = 27;//文件同步状态检测
    public static final int B_MSG_TYPE_TOOLS_UPDATE_CHECK = 28;//工具更新状态检测

    public static final int BRUSH_TOTAL_MSG_TYPE = 28;

    private static BrushTask instance;
    public List<File> b_sUploadImgsList = new ArrayList<>();//需要上传的图片
    public int b_currentUploadNum = 0;//当前上传的图片序号
    public boolean b_isUploadErrorImages = false;//是否是传缺失的图片
    public BBaseSessionObject b_currentSendObject;//当前要发送的 message Object
    public String b_currentMsgType;//当前要发送的 message type
    public long b_currentTimeStamp = 0;//收到一个re时得到的时间戳
    public boolean b_isMsgResponsed = true;//消息是否得到应答

    private Long isFreeTime = 0L;

    public static Map<String, Long> timeCountMap = new ConcurrentHashMap<>();
    public static List<Long> downLoadFileTime = new ArrayList<>(); //下载文件时间
    public static List<Long> comparePicTime = new ArrayList<>(); //上传文件时间


    private BrushTask() {

    }

    public static synchronized BrushTask getInstance() {
        if (instance == null) {
            instance = new BrushTask(Looper.getMainLooper());
        }
        return instance;
    }

    public void reset() {
        b_currentUploadNum = 0;
        b_isUploadErrorImages = false;
        b_currentSendObject = null;
        b_currentMsgType = null;
        b_currentTimeStamp = 0;
        if (b_sUploadImgsList != null) {
            b_sUploadImgsList.clear();
        }
    }

    public static void release() {
        instance = null;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START && Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
            switch (msg.what) {
                case B_MSG_TYPE_GET_ORDER:  //获取订单
//                    LLog.d("轮询服务器订单中...");
                    getOrder();
                    Constants.requestOrderTime = System.currentTimeMillis();
                    Message message = Message.obtain();
                    message.what = B_MSG_TYPE_GET_ORDER;
                    message.obj = System.currentTimeMillis();
                    sendMessageDelayed(message, 1000);
                    break;

                case B_MSG_TYPE_DEAL_WITH_ORDER:  //处理订单
                    if (msg.obj != null) {
                        if (msg.obj instanceof BrushOrderInfo) {
                            BrushOrderInfo orderInfo = (BrushOrderInfo) msg.obj;
                            dealWithOrder(orderInfo);
                        }
                    }
                    break;

                case B_MSG_TYPE_ORDER_RESULT:  //发送订单执行结果
                    if (Constants.sBrushOrderInfo != null) {
                        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                        String orderResult = SpUtil.getKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL);
                        if (BOrderStateConfig.ORDER_SUCCESS.equals(orderResult)) {
                            Constants.orderSuccessTime = System.currentTimeMillis();
                        }
                        String fileName = "";
                        String fileData = null;
                        List<File> upList = FileHelper.getFiles(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId());
                        if (upList != null && upList.size() > 0) {
                            fileName = upList.get(0).getName();
                            fileData = FileHelper.getUploadFile(upList.get(0));
                        } else {
                            LLog.e("订单对应的截图为空!");
                            RecordFloatView.updateMessage("订单对应的截图为空!");
                        }
                        BSendOrderResult bSendOrderResult = new BSendOrderResult(BrushHelper.B_RESPONSE_ORDER_RESULT,
                                BrushHelper.B_TASK_TYPE_9003, Constants.sBrushOrderInfo.getOrderId(),
                                Constants.sBrushOrderInfo.getConditionId(),
                                SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"),
                                orderResult, fileName, fileData, System.currentTimeMillis(), SpUtil.getKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "未知错误"));
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9003, bSendOrderResult);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,发送订单执行结果的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_UPDATE_MODULE:  //更新模块
                    if (msg.obj != null) {
                        if (Constants.sBrushOrderInfo != null) {
                            BSendModuleResult sendModuleResult = new BSendModuleResult(BrushHelper.B_UPDATE_MODULE_INFO, BrushHelper.B_TASK_TYPE_9005,
                                    Constants.sBrushOrderInfo.getUserId(), (String) msg.obj, System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9005, sendModuleResult);
                        } else {
                            RecordFloatView.updateMessage("内存中订单为空,发送更新模块的请求失败!");
                            BaseOrderHelper.resetData();
                        }
                    }
                    break;

                case B_MSG_TYPE_UPDATE_DEVICE_INFO:  //更新设备信息
                    if (Constants.sBrushOrderInfo != null) {
                        BSendDeviceInfo sendDeviceInfo = new BSendDeviceInfo(BrushHelper.B_UPDATE_DEVICE_INFO, BrushHelper.B_TASK_TYPE_9007, Constants.sBrushOrderInfo.getUserId(),
                                SpUtil.getKeyString(PlatformConfig.CURRENT_DEVICE_INFO, ""), SpUtil.getKeyString(PlatformConfig.CURRENT_IP_INFO, ""),
                                null, null, null, null, Constants.sBrushOrderInfo.getConditionId(), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9007, sendDeviceInfo);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,发送更新设备信息的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_UPDATE_USER_INFO:  //更新用户信息
                    if (Constants.sBrushOrderInfo != null) {
                        BSendUserInfo sendUserInfo = new BSendUserInfo(BrushHelper.B_UPDATE_USER_INFO, BrushHelper.B_TASK_TYPE_9009, Constants.sBrushOrderInfo.getUserId(),
                                SpUtil.getKeyString(PlatformConfig.CURRENT_ADMIN, ""), SpUtil.getKeyString(PlatformConfig.CURRENT_PASSWORD, ""),
                                SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), SpUtil.getKeyString(PlatformConfig.CURRENT_NAME, ""),
                                SpUtil.getKeyString(PlatformConfig.CURRENT_ID_CARD, ""), SpUtil.getKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, ""),
                                System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9009, sendUserInfo);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,发送更新用户信息的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_UPDATE_IP_INFO:  //更新IP信息
                    if (Constants.sBrushOrderInfo != null) {
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            String ip = bundle.getString("ip");
                            int ipFlag = bundle.getInt("ipFlag");
                            BSendIpInfo sendIpInfo = new BSendIpInfo(BrushHelper.B_UPDATE_IP_INFO, BrushHelper.B_TASK_TYPE_9011, ipFlag, Constants.sBrushOrderInfo.getOrderId(), ip, System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9011, sendIpInfo);
                        } else {
                            RecordFloatView.updateMessage("Hnadler中传递的ip与ipFlag为空,订单处理失败!");
                            BrushOrderHelper.getInstance().orderDealFailure("Hnadler中传递的ip与ipFlag为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                            return;
                        }
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,发送更新IP信息的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_GET_ID_CARD:  //获取身份证信息
                    if (Constants.sBrushOrderInfo != null) {
                        String gender = "男";
                        if (Constants.gender.equals("男") || Constants.gender.equals("女")) {
                            gender = Constants.gender;
                        } else {
                            gender = "";
                        }
                        BRequestGetIdCard bRequestGetIdCard = new BRequestGetIdCard(BrushHelper.B_GET_ID_CARD, BrushHelper.B_TASK_TYPE_9013, Constants.sBrushOrderInfo.getConditionId(), gender, System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9013, bRequestGetIdCard);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,发送获取身份证信息的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_GET_PHONE_NUMBER:  //获取手机号
                    if (Constants.sBrushOrderInfo != null) {
                        BRequestPhoneNum bRequestPhoneNum = new BRequestPhoneNum(Constants.sBrushOrderInfo.getUserId(), BrushHelper.B_GET_PHONE_NUM, BrushHelper.B_TASK_TYPE_9015, MobileRegisterHelper.getInstance().phone_number_source,
                                MobileRegisterHelper.getInstance().ItemId, MobileRegisterHelper.getInstance().PhoneType, MobileRegisterHelper.getInstance().notPrefix,
                                MobileRegisterHelper.getInstance().phone, Constants.sBrushOrderInfo.getConditionId(), "0", System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9015, bRequestPhoneNum);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,发送获取手机号的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_GET_MESSAGE:  //获取短信
                    if (Constants.sBrushOrderInfo != null) {
                        if (MobileRegisterHelper.getInstance().phone == null) {
                            FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "获取短信 手机号为空! : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");

                            ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_FAILED);
                            return;
                        }
                        BRequestMessage bRequestMessage = new BRequestMessage(BrushHelper.B_GET_MESSAGE, BrushHelper.B_TASK_TYPE_9017, MobileRegisterHelper.getInstance().phone_number_source,
                                MobileRegisterHelper.getInstance().phone, MobileRegisterHelper.getInstance().ItemId, Constants.sBrushOrderInfo.getConditionId(), System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9017, bRequestMessage);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,发送获取短信的请求失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_RELEASE_PHONE:  //释放手机号码
                    BSendReleasePhoneNum bSendReleasePhoneNum = new BSendReleasePhoneNum(BrushHelper.B_RELAESE_PHONE, BrushHelper.B_TASK_TYPE_9019, MobileRegisterHelper.getInstance().phone_number_source
                            , MobileRegisterHelper.getInstance().phone, MobileRegisterHelper.getInstance().phone, System.currentTimeMillis(), MobileRegisterHelper.getInstance().ItemId);
                    BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9019, bSendReleasePhoneNum);
                    break;

                case B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE:  //客户端上传一个验证码截图
                    if (Constants.sBrushOrderInfo != null) {
                        if (b_currentUploadNum == 0 && !b_isUploadErrorImages) {
                            b_sUploadImgsList = FileHelper.getFiles(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId());
                            Collections.sort(b_sUploadImgsList);
                            if (b_sUploadImgsList == null || b_sUploadImgsList.size() == 0) {
                                //订单处理失败
                                LLog.e("订单对应的截图为空!");
                                RecordFloatView.updateMessage("订单对应的截图为空!");
                                BrushOrderHelper.getInstance().orderDealFailure("订单对应的截图为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                                return;
                            }
                            LLog.e("开始上传图片!");
                            RecordFloatView.updateMessage("开始上传图片!");
                        }
                        if (b_sUploadImgsList.size() == 1 && b_currentUploadNum == 0) {
                            String fileData = FileHelper.getUploadFile(b_sUploadImgsList.get(b_currentUploadNum));
                            BSendVerifyCodeImg bSendVerifyCodeImg = new BSendVerifyCodeImg(BrushHelper.B_UPLOAD_VERIFY_CODE_CAPTURE, BrushHelper.B_TASK_TYPE_9031,
                                    Constants.sBrushOrderInfo.getConditionId(), Constants.VERIFY_TYPE, b_sUploadImgsList.get(b_currentUploadNum).getName(), fileData, null, null, System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9031, bSendVerifyCodeImg);
                        } else if (b_sUploadImgsList.size() > 1 && b_currentUploadNum == 0) {
                            List<String> imageNameList = FileHelper.getFileNames(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId());
                            Collections.sort(imageNameList);
                            List<String> imageDataList = new ArrayList<>();
                            for (File file : b_sUploadImgsList) {
                                imageDataList.add(FileHelper.getUploadFile(file));
                            }
                            BSendVerifyCodeImg bSendVerifyCodeImg = new BSendVerifyCodeImg(BrushHelper.B_UPLOAD_VERIFY_CODE_CAPTURE, BrushHelper.B_TASK_TYPE_9031,
                                    Constants.sBrushOrderInfo.getConditionId(), Constants.VERIFY_TYPE, null, null, imageNameList, imageDataList, System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9031, bSendVerifyCodeImg);
                        } else {
                            //全部图片上传完毕
                            LLog.e("全部图片上传完毕! 图片数量:" + b_sUploadImgsList.size());
                            RecordFloatView.updateMessage("全部图片上传完毕! 图片数量:" + b_sUploadImgsList.size());
                            FileHelper.deleteFolderFile(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId());
                            b_currentUploadNum = 0;
                            b_sUploadImgsList.clear();
                            SocketUtil.continueExec();
                        }
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_GET_VERY_CODE:  //校验码
                    BRequestGetVerifyCode bRequestGetVerifyCode = new BRequestGetVerifyCode(BrushHelper.B_GET_VERIFY_CODE, BrushHelper.B_TASK_TYPE_9027,
                            MobileRegisterHelper.getInstance().verifyNumber, System.currentTimeMillis());
                    BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9027, bRequestGetVerifyCode);
                    break;

                case B_MSG_TYPE_CALL_ALERT:  //客户端发送一个报警请求
                    if (Constants.sBrushOrderInfo != null) {
                        String alertType = SpUtil.getKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BAlertConfig.B_ALERT_AND_REDO_SCRIPT_ERROR);
                        if (TextUtils.isEmpty(alertType)) {
                            alertType = BAlertConfig.B_ALERT_AND_REDO_SCRIPT_ERROR;
                        }
                        String alertDesc = BAlertConfig.getAlertDesc(alertType);
                        File file = FileHelper.getUploadAlertImg(SpUtil.getKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, ""));
                        BSendAlert bSendAlert = null;
                        if (file != null) {
                            String fileName = file.getName();
                            String fileData = FileHelper.getUploadFile(file);
                            bSendAlert = new BSendAlert(BrushHelper.B_CALL_ALERT, BrushHelper.B_TASK_TYPE_9033, alertType, alertDesc, fileName, fileData,
                                    SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), Constants.sBrushOrderInfo.getOrderId(), System.currentTimeMillis());
                        } else {
                            bSendAlert = new BSendAlert(BrushHelper.B_CALL_ALERT, BrushHelper.B_TASK_TYPE_9033, alertType, alertDesc, "", "",
                                    SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), Constants.sBrushOrderInfo.getOrderId(), System.currentTimeMillis());
                        }
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9033, bSendAlert);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,上传报警截图失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_UPLOAD_KEYCHAIN:  //客户端发送一个Keychain
                    if (msg.obj != null) {
                        String keychainFilePath = (String) msg.obj;
                        String keychainData = FileHelper.getUploadFile(new File(keychainFilePath));
                        if (Constants.sBrushOrderInfo != null) {
                            BSendKeychainFile bSendKeychainFile = new BSendKeychainFile(BrushHelper.B_UPLOAD_KEYCHAIN, BrushHelper.B_TASK_TYPE_9035, new File(keychainFilePath).getName(),
                                    keychainData, Constants.sBrushOrderInfo.getUserId(), System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9035, bSendKeychainFile);
                        } else {
                            RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                            BaseOrderHelper.resetData();
                        }
                    }
                    break;

                case B_MSG_TYPE_REMOTE_ASSISTANCE:  //发送远程协助
                    if (Constants.sBrushOrderInfo != null) {
                        List<File> mgsList = FileHelper.getFiles(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId());
                        if (mgsList == null || mgsList.size() == 0) {
                            //订单处理失败
                            LLog.e("订单对应的远程协助截图为空!");
                            RecordFloatView.updateMessage("订单对应的远程协助截图为空!");
                            BrushOrderHelper.getInstance().orderDealFailure("订单对应的远程协助截图为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                            return;
                        }
                        LLog.e("开始上传远程协助图片!");
                        RecordFloatView.updateMessage("开始上传远程协助图片!");
                        String fileData = FileHelper.getUploadFile(mgsList.get(0));
                        BSendRemoteAssistanceImg bSendRemoteAssistanceImg = new BSendRemoteAssistanceImg(BrushHelper.B_REMOTE_ASSISTANCE, BrushHelper.B_TASK_TYPE_9039,
                                Constants.sBrushOrderInfo.getConditionId(), BrushHelper.B_SYSTEM_TYPE, SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), fileData, System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9039, bSendRemoteAssistanceImg);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_GET_REMOTE_ASSISTANCE_CMD:  //获取远程协助指令
                    if (msg.obj != null) {
                        String imgId = (String) msg.obj;
                        if (Constants.sBrushOrderInfo != null) {
                            BRequestGetAssistanceCmd bRequestGetAssistanceCmd = new BRequestGetAssistanceCmd(BrushHelper.B_GET_REMOTE_ASSISTANCE_CMD, BrushHelper.B_TASK_TYPE_9041,
                                    imgId, System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9041, bRequestGetAssistanceCmd);
                        } else {
                            RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                            BaseOrderHelper.resetData();
                        }
                    }
                    break;

                case B_MSG_TYPE_REQUEST_UPLOAD_IMAGE:  //客户端发送一个准备上传图片的请求
                    if (Constants.sBrushOrderInfo != null) {
                        List<String> names = FileHelper.getFileNames(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId());
                        if (names == null || names.size() == 0) {
                            //订单处理失败
                            LLog.e("订单对应的截屏图片为空!");
                            RecordFloatView.updateMessage("订单对应的截屏图片为空!");
                            BrushOrderHelper.getInstance().orderDealFailure("订单对应的截屏图片为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                            return;
                        }
                        BSendRequestUploadImg bSendRequestUploadImg = new BSendRequestUploadImg(BrushHelper.B_REQUEST_UPLOAD_IMAGE, BrushHelper.B_TASK_TYPE_9043,
                                SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), names, System.currentTimeMillis());

                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9043, bSendRequestUploadImg);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_UPLOAD_ONE_IMAGE:  //客户端上传一张图片
                    if (Constants.sBrushOrderInfo != null) {
                        if (b_currentUploadNum == 0 && !b_isUploadErrorImages) {
                            b_sUploadImgsList = FileHelper.getFiles(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId());
                            if (b_sUploadImgsList == null || b_sUploadImgsList.size() == 0) {
                                //订单处理失败
                                LLog.e("订单对应的截图为空!");
                                RecordFloatView.updateMessage("订单对应的截图为空!");
                                BrushOrderHelper.getInstance().orderDealFailure("订单对应的截图为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                                return;
                            }
                            LLog.e("开始上传图片!");
                            RecordFloatView.updateMessage("开始上传图片!");
                        }
                        if (b_currentUploadNum < b_sUploadImgsList.size()) {
                            String fileName = b_sUploadImgsList.get(b_currentUploadNum).getName();
                            String fileData = FileHelper.getUploadFile(b_sUploadImgsList.get(b_currentUploadNum));
                            BSendUploadImage bSendUploadImage = new BSendUploadImage(BrushHelper.B_UPLOAD_ONE_IMAGE, BrushHelper.B_TASK_TYPE_9045,
                                    SpUtil.getKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, ""), SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"),
                                    Constants.sBrushOrderInfo.getConditionId(), Constants.sBrushOrderInfo.getUserId(), Constants.sBrushOrderInfo.getOrderId(), fileName, fileData, System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9045, bSendUploadImage);
                        } else {
                            //全部图片上传完毕
                            LLog.e("全部图片上传完毕! 图片数量:" + b_sUploadImgsList.size());
                            RecordFloatView.updateMessage("全部图片上传完毕! 图片数量:" + b_sUploadImgsList.size());
                            b_currentUploadNum = 0;
                            b_sUploadImgsList.clear();
                            if (BOrderCaptureConfig.ORDER_CAPTURE_TYPE_RECHARGE.equals(SpUtil.getKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_GAME))) {
                                BrushOrderHelper.getInstance().rechargePicUploaded = true;
                            }
                            sendMessage(B_MSG_TYPE_ALL_IMAGE_UPLOADED, 1000);
                        }
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_ALL_IMAGE_UPLOADED:  //客户端发送一个全部截图上传成功消息
                    if (Constants.sBrushOrderInfo != null) {
                        BSendUploadAllImage bSendUploadAllImage = new BSendUploadAllImage(BrushHelper.B_ALL_IMAGE_UPLOAD_FINISH, BrushHelper.B_TASK_TYPE_9047,
                                SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9047, bSendUploadAllImage);
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_PHONE_REBOOT:  //客户端发送一个手机重启的消息
                    BSendPhoneReboot bSendPhoneReboot = new BSendPhoneReboot(BrushHelper.B_PHONE_REBOOT, BrushHelper.B_TASK_TYPE_9049,
                            SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), System.currentTimeMillis());
                    BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9049, bSendPhoneReboot);
                    break;

                case B_MSG_TYPE_COMMIT_JOB_NUMBER:  //客户端提交充值工号信息
                    if (msg.obj != null) {
                        String jobNumber = (String) msg.obj;
                        if (Constants.sBrushOrderInfo != null) {
                            BSendJobNumber bSendJobNumber = new BSendJobNumber(BrushHelper.B_COMMIT_JOB_NUMBER, BrushHelper.B_TASK_TYPE_9055,
                                    SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), Constants.sBrushOrderInfo.getOrderId(),
                                    jobNumber, System.currentTimeMillis());
                            BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9055, bSendJobNumber);
                        } else {
                            RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                            BaseOrderHelper.resetData();
                        }
                    }
                    break;

                case B_MSG_TYPE_DELIVER_PYTHON:
                    if (Constants.sBrushOrderInfo != null) {
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            String pythonScript = bundle.getString("data");
                            BrushOrderHelper.getInstance().deliverPythonScript(Constants.sBrushOrderInfo.getUserId(), pythonScript);
                        } else {
                            RecordFloatView.updateMessage("Hnadler中传递的Python脚本为空,订单处理失败!");
                            BrushOrderHelper.getInstance().orderDealFailure("Hnadler中传递的Python脚本为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                        }
                    } else {
                        RecordFloatView.updateMessage("内存中订单为空,订单处理失败!");
                        BaseOrderHelper.resetData();
                    }
                    break;

                case B_MSG_TYPE_ROBOT_ALIVE_RE:  //客户端应答一个心跳消息给服务器端
                    BSendCheckAliveRe sendCheckAliveResponse = new BSendCheckAliveRe(BrushHelper.B_KEEP_ALIVE_RE, BrushHelper.B_TASK_TYPE_1004, SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"));
                    BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_1004, sendCheckAliveResponse);
                    break;

                case B_MSG_TYPE_RESEND_MSG:  //如果未收到服务器响应  重发消息
                    if (!TextUtils.isEmpty(b_currentMsgType) && b_currentSendObject != null) {
                        if (!b_isMsgResponsed && (System.currentTimeMillis() - b_currentSendObject.getTimeStamp()) > 15 * 1000) {
                            if (b_currentTimeStamp < b_currentSendObject.getTimeStamp()) {
                                BrushHelper.getInstance().sendMessage(b_currentMsgType, b_currentSendObject);
                            }
                        }
                    }
                    sendMessage(B_MSG_TYPE_RESEND_MSG, 10 * 1000);
                    break;

                case B_MSG_TYPE_MINA_RESET:  //Mina重新初始化
                    //BrushHelper.getInstance().init();
                    BNioConnection.b_needReconnect = true;
                    break;

                case B_MSG_TYPE_FILE_SYNC_CHECK:  //文件同步状态检测
                    if (msg.obj != null) {
                        boolean sync_status = (boolean) msg.obj;
                        if (sync_status) {
                            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
                        } else {
                        }
                    }
                    break;

                case B_MSG_TYPE_TOOLS_UPDATE_CHECK:  //工具更新状态检测
                    if (msg.obj != null) {
                        boolean update_status = (boolean) msg.obj;
                        if (update_status) {
                            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
                        } else {

                        }
                    }
                    break;

                default:
                    break;
            }
        }

    }


    private BrushTask(Looper mainLooper) {
        super(mainLooper);
        //BrushHelper.getInstance().init();
        //new BNioConnection().start();
        SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
        Message message = Message.obtain();
        message.what = B_MSG_TYPE_GET_ORDER;
        sendMessageDelayed(message, 5000);
        sendEmptyMessageDelayed(B_MSG_TYPE_RESEND_MSG, 5000);
    }


    /**
     * 请求订单
     */
    public void getOrder() {
        if (SpUtil.getKeyBoolean(PlatformConfig.ISFREE, true)) {//处于空闲状态
            LLog.d("处于空闲中...");
            isFreeTime = System.currentTimeMillis();
            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, false);//置为非空闲
            ScriptApplication.getService().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        LLog.d("正在获取本地资源MD5值...");
                        List<File> localFileList = new ArrayList<>();
                        localFileList.addAll(FileHelper.getFiles(Constants.SCRIPT_FOLDER_GAME_APK_PATH));
                        localFileList.addAll(FileHelper.getFiles(Constants.SCRIPT_FOLDER_GAME_RESOURCE_PATH));
                        HashMap<String, String> md5Map = (HashMap<String, String>) SerializableUtil.fromSerialize(Constants.SCRIPT_FOLDER_GAME_APK_MD5_PATH);
                        if (md5Map == null) {
                            md5Map = new HashMap<>();
                        }
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < localFileList.size(); i++) {
                            if (!localFileList.get(i).getName().endsWith("apk") && !localFileList.get(i).getName().endsWith("zip")) {
                                continue;
                            }
                            JSONObject object = new JSONObject();
                            object.put("name", localFileList.get(i).getName());
                            String md5Value = md5Map.get(localFileList.get(i).getName());
                            if (!TextUtils.isEmpty(md5Value)) {
                                object.put("md5", md5Value);
                            } else {
                                md5Value = MD5Util.getFileMD5(localFileList.get(i));
                                object.put("md5", md5Value);
                                md5Map.put(localFileList.get(i).getName(), md5Value);
                            }
                            jsonArray.put(object);
                        }
                        SerializableUtil.toSerialize(Constants.SCRIPT_FOLDER_GAME_APK_MD5_PATH, md5Map);


                        JSONArray appArray = new JSONArray();
                        PackageManager packageManager = ScriptApplication.getInstance().getPackageManager();
                        PackageInfo scriptInfo = packageManager.getPackageInfo(ScriptApplication.getInstance().getPackageName(), 0);
                        PackageInfo deviceHookInfo = packageManager.getPackageInfo("com.example.devicehook", 0);
                        PackageInfo fileSyncInfo = packageManager.getPackageInfo("com.filesync.manual", 0);
                        JSONObject scriptObj = new JSONObject();
                        JSONObject deviceHookObj = new JSONObject();
                        JSONObject fileSyncObj = new JSONObject();
                        scriptObj.put("name", "script");
                        scriptObj.put("version", scriptInfo.versionName);
                        deviceHookObj.put("name", "deviceHook");
                        deviceHookObj.put("version", deviceHookInfo.versionName);
                        fileSyncObj.put("name", "file");
                        fileSyncObj.put("version", fileSyncInfo.versionName);
                        appArray.put(scriptObj);
                        appArray.put(deviceHookObj);
                        appArray.put(fileSyncObj);

                        BRequestGetTask getTaskBean = new BRequestGetTask(BrushHelper.B_GET_TASK, BrushHelper.B_TASK_TYPE_9001, BrushHelper.B_SYSTEM_TYPE, SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow"), jsonArray, appArray, System.currentTimeMillis());
                        BrushHelper.getInstance().sendMessage(BrushHelper.B_TASK_TYPE_9001, getTaskBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {  //非空闲转态
            String[] time = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_ORDER_MIN_TIME);
            long orderTime = System.currentTimeMillis() - isFreeTime;
            if (time == null || time.length == 0) {
                if (orderTime > 30 * 60 * 1000) {
                    LLog.d("卡住重新执行订单...");
                    isFreeTime = System.currentTimeMillis();
                    if (Constants.sGamesOrderInfo != null || Constants.sBrushOrderInfo != null) {
                        Constants.EXEC_STATE = ExecEnum.EXEC_START;
                        Constants.PLAY_STATE = PlayEnum.START_PLAY;
                        sendEmptyMessageDelayed(BrushTask.B_MSG_TYPE_GET_ORDER, 5000);
                        sendMessageObject(BrushTask.B_MSG_TYPE_DEAL_WITH_ORDER, Constants.sBrushOrderInfo, 1000);
                    }
                }
            }
        }
    }


    /**
     * 处理订单
     *
     * @param brushOrderInfo
     */
    public void dealWithOrder(BrushOrderInfo brushOrderInfo) {
        try {

            timeCountMap.clear();
            downLoadFileTime.clear();
            comparePicTime.clear();

            RecordFloatView.updateMessage("开始处理订单!");
            Constants.sBrushOrderInfo = brushOrderInfo;
            b_currentUploadNum = 0;
            b_isUploadErrorImages = false;
            b_sUploadImgsList.clear();
            FileUtil.saveFile(ScriptApplication.getGson().toJson(brushOrderInfo), Constants.BRUSH_ORDER_FOLDER_TEMP_PATH);
            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, false);//置为非空闲
            if (SpUtil.getKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, false)) {
                if (BrushOrderHelper.checkNeedReboot(brushOrderInfo)) {
                    return;
                }
            }
            SpUtil.putKeyBoolean(PlatformConfig.NEED_CHECK_ACTIVE, false);//需要检阅游戏时长
            SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);//订单处理状态先置为失败
            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL);//重置当前优化订单执行结果类型
            SpUtil.putKeyLong(PlatformConfig.ACTIVE_TIME, brushOrderInfo.getDurationTime());//游戏活跃时长
            SpUtil.getKeyString(PlatformConfig.CURRENT_IP_INFO, "");//置空存储的IP城市
            SpUtil.putKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, "");//置空报警图片
            SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, true);
            Constants.getOrderTime = System.currentTimeMillis();
            if (!StringUtil.isEmpty(brushOrderInfo.getUserModel().getAccount()) && !StringUtil.isEmpty(brushOrderInfo.getUserModel().getPassWord())) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, brushOrderInfo.getUserModel().getAccount());
                SpUtil.putKeyString(PlatformConfig.CURRENT_PASSWORD, brushOrderInfo.getUserModel().getPassWord());
            } else {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, "");
                SpUtil.putKeyString(PlatformConfig.CURRENT_PASSWORD, "");
                //RecordFloatView.updateMessage("从服务器获取到的用户名或密码为空!");
            }

            if (!StringUtil.isEmpty(brushOrderInfo.getCommentary())) {
                //存储评论的内容
                SpUtil.putKeyString(PlatformConfig.CURRENT_COMMENTARY, brushOrderInfo.getCommentary());
            } else {
                SpUtil.putKeyString(PlatformConfig.CURRENT_COMMENTARY, "");
            }

            if (!StringUtil.isEmpty(brushOrderInfo.getUserModel().getRealName()) && !StringUtil.isEmpty(brushOrderInfo.getUserModel().getIdCardInfo())) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_NAME, brushOrderInfo.getUserModel().getRealName());
                SpUtil.putKeyString(PlatformConfig.CURRENT_ID_CARD, brushOrderInfo.getUserModel().getIdCardInfo());
            } else {
                //RecordFloatView.updateMessage("从服务器获取到的身份证和或姓名为空!");
            }

            if (!StringUtil.isEmpty(brushOrderInfo.getUserModel().getPhoneNumber())) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, brushOrderInfo.getUserModel().getPhoneNumber());
            } else {
                //RecordFloatView.updateMessage("从服务器获取到的手机号为空!");
            }

            BrushOrderHelper.getInstance().rechargePicUploaded = BOrderTypeConfig.ORDER_TYPE_PAY != Constants.sBrushOrderInfo.getOrderType() && BOrderTypeConfig.ORDER_TYPE_AUTO_PAY != Constants.sBrushOrderInfo.getOrderType();

            //删除旧Keychain
            FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_PATH);
            if (brushOrderInfo.getKeychain() != null && brushOrderInfo.getKeychain().size() > 0) {
                for (String keychainUrl : brushOrderInfo.getKeychain()) {
                    BrushOrderHelper.getInstance().downloadFile(keychainUrl);
                }
            } else {
                //RecordFloatView.updateMessage("从服务器获取到的keychain为空!");
            }

            BrushOrderHelper.getInstance().dealWithImgs(brushOrderInfo);

            if (brushOrderInfo.getCommentPathList() != null && brushOrderInfo.getCommentPathList().size() > 0) {
                BrushOrderHelper.getInstance().TvComments = new ArrayList<>();
                for (String commentUrl : brushOrderInfo.getCommentPathList()) {
                    BrushOrderHelper.getInstance().downloadComment(commentUrl);
                }
            }
            if (brushOrderInfo.getStateScriptList() != null && brushOrderInfo.getStateScriptList().size() > 0) {
                for (String excelUrl : brushOrderInfo.getStateScriptList()) {
                    BrushOrderHelper.getInstance().downloadExcel(excelUrl);
                }
            }
            if (brushOrderInfo.getTemplateList() != null && brushOrderInfo.getTemplateList().size() > 0) {
                File script = new File(Constants.SCRIPT_MATCH_PATH);
                if (script == null) {
                    script.mkdir();
                } else {
                    FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_MATCH_PATH);
                }
                for (String templateUrl : brushOrderInfo.getTemplateList()) {
                    BrushOrderHelper.getInstance().downloadMatchTmp(templateUrl);
                }
            }
            if (brushOrderInfo.getCommentImagePathList() != null && brushOrderInfo.getCommentImagePathList().size() > 0) {
                BrushOrderHelper.getInstance().commentImgUrls = new PictureInfo(new ArrayList<>(), 0);
                for (String imgUrl : brushOrderInfo.getCommentImagePathList()) {
                    BrushOrderHelper.getInstance().commentImgUrls.getImgUrls().add(imgUrl);
                }
                BrushOrderHelper.getInstance().commentImgUrls.setImgNum(0);
            }

            if (!StringUtil.isEmpty(brushOrderInfo.getLinkScript())) {
                BrushOrderHelper.getInstance().downloadScript(brushOrderInfo.getLinkScript());
            } else {
                RecordFloatView.updateMessage("脚本链接为空!");
                BrushOrderHelper.getInstance().orderDealFailure("脚本链接为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            }

        } catch (Exception e) {
            RecordFloatView.updateMessage("抛异常了!");
            BrushOrderHelper.getInstance().orderDealFailure("抛异常了!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
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
    public void sendMessageObject(int msgType, Object object, long delayTime) {
        if (instance != null) {
            Message msg = Message.obtain();
            msg.what = msgType;
            msg.obj = object;
            instance.sendMessageDelayed(msg, delayTime);
        }
    }

    /**
     * 更新消息状态
     *
     * @param msgType   消息类型
     * @param object    消息内容
     * @param delayTime 延迟时间
     */
    public void sendMessageBundle(int msgType, String object, long delayTime) {
        if (instance != null) {
            Message msg = Message.obtain();
            msg.what = msgType;
            Bundle bundle = new Bundle();
            bundle.putString("data", object);
            msg.setData(bundle);
            instance.sendMessageDelayed(msg, delayTime);
        }
    }

    /**
     * 更新消息状态
     *
     * @param msgType   消息类型
     * @param bundle    消息内容
     * @param delayTime 延迟时间
     */
    public void sendMessageBundle(int msgType, Bundle bundle, long delayTime) {
        if (instance != null) {
            Message msg = Message.obtain();
            msg.what = msgType;
            msg.setData(bundle);
            instance.sendMessageDelayed(msg, delayTime);
        }
    }

}



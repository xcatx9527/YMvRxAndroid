package com.xile.script.http.helper.brush.mina;

import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.GameConfig;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.brush.bean.*;
import com.xile.script.http.helper.other.MobileRegisterHelper;
import com.xile.script.service.AndServerService;
import com.xile.script.utils.*;
import com.xile.script.utils.appupdate.DownloadService;
import com.xile.script.utils.appupdate.SystemUpdate;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.script.ScriptUtil;
import com.xile.script.utils.script.SocketUtil;
import org.apache.mina.core.future.ConnectFuture;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import script.tools.config.ScriptConstants;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * date: 2017/6/30 21:26
 *
 * @scene 优化订单工具类
 */

public class BrushHelper {

    //协议
    public static final String B_TASK_TYPE_9001 = "9001";
    public static final String B_TASK_TYPE_9002 = "9002";
    public static final String B_TASK_TYPE_9003 = "9003";
    public static final String B_TASK_TYPE_9004 = "9004";
    public static final String B_TASK_TYPE_9005 = "9005";
    public static final String B_TASK_TYPE_9006 = "9006";
    public static final String B_TASK_TYPE_9007 = "9007";
    public static final String B_TASK_TYPE_9008 = "9008";
    public static final String B_TASK_TYPE_9009 = "9009";
    public static final String B_TASK_TYPE_9010 = "9010";
    public static final String B_TASK_TYPE_9011 = "9011";
    public static final String B_TASK_TYPE_9012 = "9012";
    public static final String B_TASK_TYPE_9013 = "9013";
    public static final String B_TASK_TYPE_9014 = "9014";
    public static final String B_TASK_TYPE_9015 = "9015";
    public static final String B_TASK_TYPE_9016 = "9016";
    public static final String B_TASK_TYPE_9017 = "9017";
    public static final String B_TASK_TYPE_9018 = "9018";
    public static final String B_TASK_TYPE_9019 = "9019";
    public static final String B_TASK_TYPE_9020 = "9020";
    public static final String B_TASK_TYPE_9021 = "9021";
    public static final String B_TASK_TYPE_9022 = "9022";
    public static final String B_TASK_TYPE_9023 = "9023";
    public static final String B_TASK_TYPE_9024 = "9024";
    public static final String B_TASK_TYPE_9025 = "9025";
    public static final String B_TASK_TYPE_9026 = "9026";
    public static final String B_TASK_TYPE_9027 = "9027";
    public static final String B_TASK_TYPE_9028 = "9028";
    public static final String B_TASK_TYPE_9029 = "9029";
    public static final String B_TASK_TYPE_9030 = "9030";
    public static final String B_TASK_TYPE_9031 = "9031";
    public static final String B_TASK_TYPE_9032 = "9032";
    public static final String B_TASK_TYPE_9033 = "9033";
    public static final String B_TASK_TYPE_9034 = "9034";
    public static final String B_TASK_TYPE_9035 = "9035";
    public static final String B_TASK_TYPE_9036 = "9036";
    public static final String B_TASK_TYPE_9037 = "9037";
    public static final String B_TASK_TYPE_9038 = "9038";
    public static final String B_TASK_TYPE_9039 = "9039";
    public static final String B_TASK_TYPE_9040 = "9040";
    public static final String B_TASK_TYPE_9041 = "9041";
    public static final String B_TASK_TYPE_9042 = "9042";
    public static final String B_TASK_TYPE_9043 = "9043";
    public static final String B_TASK_TYPE_9044 = "9044";
    public static final String B_TASK_TYPE_9045 = "9045";
    public static final String B_TASK_TYPE_9046 = "9046";
    public static final String B_TASK_TYPE_9047 = "9047";
    public static final String B_TASK_TYPE_9048 = "9048";
    public static final String B_TASK_TYPE_9049 = "9049";
    public static final String B_TASK_TYPE_9050 = "9050";
    public static final String B_TASK_TYPE_9055 = "9055";
    public static final String B_TASK_TYPE_9056 = "9056";
    public static final String B_TASK_TYPE_1003 = "1003";
    public static final String B_TASK_TYPE_1004 = "1004";

    public static final String B_GET_TASK = "SupplyScriptNotice";
    public static final String B_GET_TASK_RE = "SupplyScriptNotice_Re";
    public static final String B_RESPONSE_ORDER_RESULT = "ReplyScriptNotice";
    public static final String B_RESPONSE_ORDER_RESULT_RE = "ReplyScriptNotice_Re";
    public static final String B_UPDATE_MODULE_INFO = "UserModuleNotice";
    public static final String B_UPDATE_MODULE_INFO_RE = "UserModuleNotice_Re";
    public static final String B_UPDATE_DEVICE_INFO = "UserOtherInfoNotice";
    public static final String B_UPDATE_DEVICE_INFO_RE = "UserOtherInfoNotice_Re";
    public static final String B_UPDATE_USER_INFO = "UserAccountInfoNotice";
    public static final String B_UPDATE_USER_INFO_RE = "UserAccountInfoNotice_Re";
    public static final String B_UPDATE_IP_INFO = "RemoteIpNotice";
    public static final String B_UPDATE_IP_INFO_RE = "RemoteIpNotice_Re";
    public static final String B_GET_ID_CARD = "BindRealNameNotice";
    public static final String B_GET_ID_CARD_RE = "BindRealNameNotice_Re";
    public static final String B_GET_PHONE_NUM = "GetPhoneNotice";
    public static final String B_GET_PHONE_NUM_RE = "GetPhoneNotice_Re";
    public static final String B_GET_MESSAGE = "GetMessageNotice";
    public static final String B_GET_MESSAGE_RE = "GetMessageNotice_Re";
    public static final String B_RELAESE_PHONE = "ReleasePhoneNotice";
    public static final String B_RELAESE_PHONE_RE = "ReleasePhoneNotice_Re";
    public static final String B_SEND_PHONE_NUM = "SendPhoneNotice";
    public static final String B_SEND_PHONE_NUM_RE = "SendPhoneNotice_Re";
    public static final String B_SEND_MESSAGE = "SendMessageNotice";
    public static final String B_SEND_MESSAGE_RE = "SendMessageNotice_Re";
    public static final String B_GET_RELEASE_PHONE = "GetReleasePhoneNotice";
    public static final String B_GET_RELEASE_PHONE_RE = "GetReleasePhoneNotice_Re";
    public static final String B_GET_VERIFY_CODE = "GetVerifyNotice";
    public static final String B_GET_VERIFY_CODE_RE = "GetVerifyNotice_Re";
    public static final String B_GET_APPOINT_PHONE_NUM = "GetSmsNotice";
    public static final String B_GET_APPOINT_PHONE_NUM_RE = "GetSmsNotice_Re";
    public static final String B_UPLOAD_VERIFY_CODE_CAPTURE = "VerifyImgNotice";
    public static final String B_UPLOAD_VERIFY_CODE_CAPTURE_RE = "VerifyImgNotice_Re";
    public static final String B_CALL_ALERT = "AlarmImgNotice";
    public static final String B_CALL_ALERT_RE = "AlarmImgNotice_Re";
    public static final String B_UPLOAD_KEYCHAIN = "KeychainUploadNotice";
    public static final String B_UPLOAD_KEYCHAIN_RE = "KeychainUploadNotice_Re";
    public static final String B_REMOTE_ASSISTANCE = "ImageOrderUploadNotice";
    public static final String B_REMOTE_ASSISTANCE_RE = "ImageOrderUploadNotice_Re";
    public static final String B_GET_REMOTE_ASSISTANCE_CMD = "GetImageOrderContentNotice";
    public static final String B_GET_REMOTE_ASSISTANCE_CMD_RE = "GetImageOrderContentNotice_Re";
    public static final String B_REQUEST_UPLOAD_IMAGE = "SendImageList";
    public static final String B_REQUEST_UPLOAD_IMAGE_RE = "SendImageList_Re";
    public static final String B_UPLOAD_ONE_IMAGE = "SendImage";
    public static final String B_UPLOAD_ONE_IMAGE_RE = "SendImage_Re";
    public static final String B_ALL_IMAGE_UPLOAD_FINISH = "SuccImageList";
    public static final String B_ALL_IMAGE_UPLOAD_FINISH_RE = "SuccImageList_Re";
    public static final String B_PHONE_REBOOT = "DeviceRestartNotice";
    public static final String B_PHONE_REBOOT_RE = "DeviceRestartNotice_Re";
    public static final String B_COMMIT_JOB_NUMBER = "OrderOperaterNotice";
    public static final String B_COMMIT_JOB_NUMBER_RE = "OrderOperaterNotice_Re";
    public static final String B_KEEP_ALIVE = "KeepAlive";
    public static final String B_KEEP_ALIVE_RE = "KeepAlive_Re";


    public final String brushServerIp = "118.144.88.217";
    public final int brushServerPort = 8045;  //端口
    public static final String B_SYSTEM_TYPE = "Android";
    private static BrushHelper sInstance;
    public BIMSocketClient mBIMSocketClient;
    public ConnectFuture mBConnectFuture;

    private static ConcurrentHashMap bNioGetMsgMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap bNioSendMsgMap = new ConcurrentHashMap<>();

    static {
        bNioSendMsgMap.put(B_TASK_TYPE_9001, "sendMsgForGetTask");
        bNioSendMsgMap.put(B_TASK_TYPE_9003, "sendMsgForOrderResult");
        bNioSendMsgMap.put(B_TASK_TYPE_9005, "sendMsgForModuleResult");
        bNioSendMsgMap.put(B_TASK_TYPE_9007, "sendMsgForDeviceInfo");
        bNioSendMsgMap.put(B_TASK_TYPE_9009, "sendMsgForUserInfo");
        bNioSendMsgMap.put(B_TASK_TYPE_9011, "sendMsgForIpInfo");
        bNioSendMsgMap.put(B_TASK_TYPE_9013, "sendMsgToGetIdCard");
        bNioSendMsgMap.put(B_TASK_TYPE_9015, "sendMsgToGetPhoneNumber");
        bNioSendMsgMap.put(B_TASK_TYPE_9017, "sendMsgToGetMessage");
        bNioSendMsgMap.put(B_TASK_TYPE_9019, "sendMsgToReleasePhone");
        bNioSendMsgMap.put(B_TASK_TYPE_9021, "sendMsgToSendPhoneNumber");
        bNioSendMsgMap.put(B_TASK_TYPE_9023, "sendMsgToSendMessage");
        bNioSendMsgMap.put(B_TASK_TYPE_9025, "sendMsgToGetReleasePhoneNumber");
        bNioSendMsgMap.put(B_TASK_TYPE_9027, "sendMsgToGetVerifyCode");
        bNioSendMsgMap.put(B_TASK_TYPE_9029, "sendMsgToGetAppointMessage");
        bNioSendMsgMap.put(B_TASK_TYPE_9031, "sendMsgToUploadVerifyCapture");
        bNioSendMsgMap.put(B_TASK_TYPE_9033, "sendMsgToCallAlert");
        bNioSendMsgMap.put(B_TASK_TYPE_9035, "sendMsgToUploadKeychain");
        bNioSendMsgMap.put(B_TASK_TYPE_9037, "sendMsgToSyncVoice");
        bNioSendMsgMap.put(B_TASK_TYPE_9039, "sendMsgToRequestRemoteAssistance");
        bNioSendMsgMap.put(B_TASK_TYPE_9041, "sendMsgToGetAssistanceCmd");
        bNioSendMsgMap.put(B_TASK_TYPE_9043, "sendMsgToRequestUploadImage");
        bNioSendMsgMap.put(B_TASK_TYPE_9045, "sendMsgToUploadOneImage");
        bNioSendMsgMap.put(B_TASK_TYPE_9047, "sendMsgToRequestAllImgUploaded");
        bNioSendMsgMap.put(B_TASK_TYPE_9049, "sendMsgToRebootPhone");
        bNioSendMsgMap.put(B_TASK_TYPE_9055, "sendMsgToCommitJobNumber");
        bNioSendMsgMap.put(B_TASK_TYPE_1004, "sendMsgForCheckAlive");

        bNioGetMsgMap.put(B_TASK_TYPE_9002, "receiveMsgForGetTaskRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9004, "receiveMsgForOrderResultRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9006, "receiveMsgForModuleResultRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9008, "receiveMsgForDeviceInfoRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9010, "receiveMsgForUserInfoRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9012, "receiveMsgForIpInfoRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9014, "receiveMsgToGetIdCardRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9016, "receiveMsgToGetPhoneNumberRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9018, "receiveMsgToGetMessageRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9020, "receiveMsgToReleasePhoneRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9022, "receiveMsgToSendPhoneNumberRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9024, "receiveMsgToSendMessageRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9026, "receiveMsgToGetReleasePhoneNumberRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9028, "receiveMsgToGetVerifyCodeRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9030, "receiveMsgToGetAppointMessageRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9032, "receiveMsgToUploadVerifyCaptureRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9034, "receiveMsgToCallAlertRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9036, "receiveMsgToUploadKeychainRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9038, "receiveMsgToSyncVoiceRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9040, "receiveMsgToRequestRemoteAssistanceRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9042, "receiveMsgToGetAssistanceCmdRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9044, "receiveMsgToRequestUploadImageRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9046, "receiveMsgToUploadOneImageRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9048, "receiveMsgToRequestAllImgUploadedRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9050, "receiveMsgToRebootPhoneRe");
        bNioGetMsgMap.put(B_TASK_TYPE_9056, "receiveMsgToCommitJobNumberRe");
        bNioGetMsgMap.put(B_TASK_TYPE_1003, "receiveMsgForCheckAliveRe");
    }


    private BrushHelper() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static BrushHelper getInstance() {
        if (sInstance == null) {
            synchronized (BrushHelper.class) {
                if (sInstance == null) {
                    sInstance = new BrushHelper();
                }
            }
        }
        return sInstance;
    }


    /**
     * 初始化
     */
    public void init() {
        ScriptApplication.getService().execute(new Runnable() {
            @Override
            public void run() {
                LLog.e("优化 - 初始化Mina");
                if (mBIMSocketClient == null) {
                    mBIMSocketClient = new BIMSocketClient(brushServerIp, brushServerPort);
                    LLog.e("优化 - 重新初始化Mina imSocketClient为空！");
                }
                if (mBIMSocketClient != null) {
                    if (mBConnectFuture == null) {
                        LLog.e("优化 - 重新初始化Mina connectFuture为空！");
                        mBConnectFuture = mBIMSocketClient.socketConnect();
                    }
                    if (mBConnectFuture != null) {
                        if (!mBConnectFuture.isConnected() || !mBConnectFuture.getSession().isConnected()) {
                            LLog.e("优化 - Mina未连接成功,将继续重连!");
                            mBConnectFuture = mBIMSocketClient.socketConnect();
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_MINA_RESET, 10000);
                        }
                    }
                }
            }
        });
    }

    /**
     * 发送消息
     *
     * @param msgType 消息类型
     * @param object  请求实体类
     */
    public void sendMessage(String msgType, Object object) {
        try {
            if (!TextUtils.isEmpty(msgType) && object != null) {
                LLog.i("##  send  ##  " + "msgType:" + msgType + " object:" + object.toString());
                String methodName = (String) bNioSendMsgMap.get(msgType);
                if (!TextUtils.isEmpty(methodName)) {
                    this.getClass().getMethod(methodName, String.class, Object.class).invoke(this, msgType, object);
                } else {
                    LLog.e("发送消息方法名为空!");
                    RecordFloatView.updateMessage("发送消息方法名为空!");
                }
            } else {
                LLog.e("发送消息时出现NULL!" + "msgType:" + " object" + object);
                RecordFloatView.updateMessage("发送消息时出现NULL!" + "msgType:" + " object" + object);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收消息
     *
     * @param msgType 消息类型
     * @param object  response
     */
    public void getMessage(String msgType, Object object) {
        try {
            if (!TextUtils.isEmpty(msgType) && object != null) {
                String methodName = (String) bNioGetMsgMap.get(msgType);
                if (!TextUtils.isEmpty(methodName)) {
                    this.getClass().getMethod(methodName, Object.class).invoke(this, object);
                } else {
                    LLog.e("接收消息方法名为空!");
                    RecordFloatView.updateMessage("接收消息方法名为空!");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    /**
     * 客户端请求一个订单任务
     *
     * @param msgType 消息类型 9001
     * @param object  BRequestGetTask
     */
    public void sendMsgForGetTask(String msgType, Object object) {
        BRequestGetTask getTaskBean = null;
        if (object instanceof BRequestGetTask) {
            getTaskBean = (BRequestGetTask) object;
        } else {
            LLog.e("instanceof BRequestGetTask Error !");
            RecordFloatView.updateMessage("instanceof BRequestGetTask Error !");
            return;
        }
        if (getTaskBean != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", getTaskBean.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("gameOS", getTaskBean.getGameOS());
                data.put("phoneName", getTaskBean.getPhoneName());
                data.put("fileList", getTaskBean.getFileList());
                data.put("appList", getTaskBean.getAppList());
                data.put("timestamp", getTaskBean.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送请求订单成功!", false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送请求订单异常!");
                RecordFloatView.updateMessage("发送请求订单异常!");
            }
        }
    }


    /**
     * 服务器应答一个订单任务
     *
     * @param object
     */
    long mCurrentTime = 0;

    public void receiveMsgForGetTaskRe(Object object) {
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForGetTaskRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForGetTaskRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BrushOrderInfo brushOrderInfo = ScriptApplication.getGson().fromJson(data, BrushOrderInfo.class);
            if (brushOrderInfo != null) {
                if (!TextUtils.isEmpty(brushOrderInfo.getRetCode())) {
                    if (!"0".equals(brushOrderInfo.getRetCode())) {
                        if ("3".equals(brushOrderInfo.getRetCode())) {
                            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, false);
                            LLog.e("获取订单失败，开始自动更新资源文件 -->" + brushOrderInfo.getRetDesc());
                            RecordFloatView.updateMessage("获取订单失败，开始自动更新资源文件 -->" + brushOrderInfo.getRetDesc());
                            AppUtil.startAppByADB(ScriptApplication.getInstance(), "com.filesync.manual");
                            SystemClock.sleep(3000);
                        } else if ("5".equals(brushOrderInfo.getRetCode())) {
                            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, false);
                            LLog.e("获取订单失败，开始自动更新软件工具 -->" + brushOrderInfo.getRetDesc());
                            RecordFloatView.updateMessage("获取订单失败，开始自动更新软件工具 -->" + brushOrderInfo.getRetDesc());
                            List<BrushOrderInfo.AppEntity> appList = brushOrderInfo.getAppInfoList();
                            if (appList == null || appList.size() == 0) {
                                LLog.e("更新失败,应用更新列表为空!");
                                RecordFloatView.updateMessage("更新失败,应用更新列表为空!");
                                SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
                                return;
                            }
                            SortListUtil.sort(appList, "name", SortListUtil.ASC);
                            DownloadService.getInstance().setNum(0);
                            DownloadService.getInstance().setMaxNum(appList.size());
                            for (BrushOrderInfo.AppEntity appInfo : appList) {
                                LLog.i("开始更新文件 --> " + appInfo.getName());
                                RecordFloatView.updateMessage("开始更新文件 --> " + appInfo.getName());
                                SystemUpdate.showNotification(ScriptApplication.getInstance(), null, appInfo.getLink(), false, appInfo.getName(), true);
                            }
                        } else {
                            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
                            LLog.e("获取订单失败:" + brushOrderInfo.getRetDesc());
                            if (System.currentTimeMillis() - mCurrentTime > 10 * 1000) {
                                mCurrentTime = System.currentTimeMillis();
                                RecordFloatView.updateMessage("获取订单失败:" + brushOrderInfo.getRetDesc());
                            }
                        }
                        return;
                    }
                    LLog.e("获取到优化订单!");
                    RecordFloatView.updateMessage("获取到优化订单!");
                    long timeStamp = brushOrderInfo.getTimestamp();
                    if (timeStamp > BrushTask.getInstance().b_currentTimeStamp) {
                        BrushTask.getInstance().b_currentTimeStamp = timeStamp;
                        BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_DEAL_WITH_ORDER, brushOrderInfo, 1000);
                    } else {
                        BaseOrderHelper.resetData();
                        LLog.e("服务器的时间戳有问题？ 本地时间:" + BrushTask.getInstance().b_currentTimeStamp + "    服务器时间:" + timeStamp);
                    }
                } else {
                    LLog.e("获取订单retCode为空！");
                    RecordFloatView.updateMessage("获取订单retCode为空!");
                    BaseOrderHelper.resetData();
                }
            } else {
                LLog.e("订单实体类为空！");
                RecordFloatView.updateMessage("订单实体类为空!");
                BaseOrderHelper.resetData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("获取订单时出现异常！");
            RecordFloatView.updateMessage("获取订单时出现异常!");
            BaseOrderHelper.resetData();
        }
    }


    /**
     * 客户端响应一个订单结果
     *
     * @param msgType 消息类型 9003
     * @param object  BSendOrderResult
     */
    public void sendMsgForOrderResult(String msgType, Object object) {

        String timeString = getTimeString();
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendOrderResult bSendOrderResult = null;
        if (object instanceof BSendOrderResult) {
            bSendOrderResult = (BSendOrderResult) object;
        } else {
            LLog.e("instanceof BSendOrderResult Error !");
            RecordFloatView.updateMessage("instanceof BSendOrderResult Error !");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", bSendOrderResult.getName());
            jsonObject.put("type", msgType);
            JSONObject data = new JSONObject();
            data.put("orderId", bSendOrderResult.getOrderId());
            data.put("conditionId", bSendOrderResult.getConditionId());
            data.put("phoneName", bSendOrderResult.getPhoneName());
            data.put("orderStatus", bSendOrderResult.getOrderStatus());
            data.put("imageName", bSendOrderResult.getImageName());
            data.put("imageData", bSendOrderResult.getImageData());
            data.put("timestamp", bSendOrderResult.getTimestamp());
            data.put("errorDesc", bSendOrderResult.getErrorDesc());
            data.put("orderTimeRecord", timeString);
            jsonObject.put("data", data.toString());
            b_nioPackageAndSend(jsonObject, "发送订单执行结果成功!", true);
        } catch (JSONException e) {
            e.printStackTrace();
            LLog.e("发送订单执行结果异常!");
            RecordFloatView.updateMessage("发送订单执行结果异常!");
        }
    }

    /**
     * 统计时间
     *
     * @return
     */
    private String getTimeString() {
        String timeStr = "";
        try {
            long downloadFileTime = 0L;
            long comparePicTime = 0L;
            if (BrushTask.downLoadFileTime.size() > 0) {
                for (int i = 0; i < BrushTask.downLoadFileTime.size(); i++) {
                    downloadFileTime += BrushTask.downLoadFileTime.get(i);
                }
            }
            if (BrushTask.comparePicTime.size() > 0) {
                for (int i = 0; i < BrushTask.comparePicTime.size(); i++) {
                    comparePicTime += BrushTask.comparePicTime.get(i);
                }
            }
            JSONObject jsonObject = new JSONObject();
            Map<String, Long> timeCountMap = BrushTask.timeCountMap;
            timeCountMap.put(GameConfig.COUNT_DOWNLOADFILE_KEY, downloadFileTime);
            timeCountMap.put(GameConfig.COUNT_COMPAREPIC_KEY, comparePicTime);
            for (Map.Entry<String, Long> next : timeCountMap.entrySet()) {
                String key = next.getKey();
                Long value = next.getValue();
                System.out.println(key + ":" + value);
                jsonObject.put(key, value);
            }
            LLog.e("jsonObject -------- " + jsonObject.toString());
            timeStr = jsonObject.toString();
            return timeStr;
        } catch (JSONException | ConcurrentModificationException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    /**
     * 服务器应答一个订单执行结果
     *
     * @param object
     */
    public void receiveMsgForOrderResultRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9003.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForOrderResultRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForOrderResultRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveOrderResultRe bReceiveOrderResultRe = ScriptApplication.getGson().fromJson(data, BReceiveOrderResultRe.class);
            if (bReceiveOrderResultRe != null) {
                if (!TextUtils.isEmpty(bReceiveOrderResultRe.getRetCode())) {
                    if (!"0".equals(bReceiveOrderResultRe.getRetCode())) {
                        LLog.e("服务器给客户端应答订单执行结果 retCode=" + bReceiveOrderResultRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答订单执行结果 retCode=" + bReceiveOrderResultRe.getRetCode());
                        //再次发送订单执行结果
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 10000);
                    } else {
                        if (bReceiveOrderResultRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_ORDER_RESULT);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveOrderResultRe.getTimestamp();
                            if (BOrderStateConfig.ORDER_FAIL_PAUSE.equals(SpUtil.getKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL))) {
                                return;
                            }
                            if (BrushOrderHelper.getInstance().script_app.equals(Constants.sBrushOrderInfo.getChannelType())) {  //常规APP脚本
                                BrushOrderHelper.getInstance().closeVPN();
                            } else if (BrushOrderHelper.getInstance().script_python.equals(Constants.sBrushOrderInfo.getChannelType())) {  //python脚本
                                ScriptApplication.getInstance().stopService(new Intent(ScriptApplication.getInstance(), AndServerService.class));
                            }
                            LLog.e("订单处理成功，开始获取下一订单!");
                            RecordFloatView.updateMessage("订单处理成功，开始获取下一订单!");
                            FileHelper.deleteFolderFile(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId());
                            Constants.sBrushOrderInfo = null;
                            FileHelper.deleteFile(Constants.BRUSH_ORDER_FOLDER_TEMP_PATH);
                            if (!BOrderStateConfig.ORDER_CHANGE_DEVICE_FAIL.equals(SpUtil.getKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL))) {
                                BaseOrderHelper.resetData();//重置状态值
                            }
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答订单执行结果 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答订单执行结果 retCode为空!");
                    //再次发送订单执行结果
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 10000);
                }
            } else {
                LLog.e("bReceiveOrderResultRe 为空！");
                RecordFloatView.updateMessage("bReceiveOrderResultRe 为空!");
                //再次发送订单执行结果
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答订单执行结果时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答订单执行结果时出现异常!");
            //再次发送订单执行结果
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 10000);
        }
    }


    /**
     * 客户端响应一个模块执行结果
     *
     * @param msgType 消息类型 9005
     * @param object  BSendModuleResult
     */
    public void sendMsgForModuleResult(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendModuleResult bSendModuleResult = null;
        if (object instanceof BSendModuleResult) {
            bSendModuleResult = (BSendModuleResult) object;
        } else {
            LLog.e("instanceof BSendModuleResult Error !");
            RecordFloatView.updateMessage("instanceof BSendModuleResult Error !");
            return;
        }
        if (bSendModuleResult != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendModuleResult.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("userId", bSendModuleResult.getUserId());
                data.put("module", bSendModuleResult.getModule());
                data.put("timestamp", bSendModuleResult.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送模块执行结果成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送模块执行结果异常!");
                RecordFloatView.updateMessage("发送模块执行结果异常!");
            }
        }
    }

    /**
     * 服务器应答一个模块执行结果
     *
     * @param object
     */
    public void receiveMsgForModuleResultRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9005.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentSendObject = null;
            BrushTask.getInstance().b_currentMsgType = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForOrderResultRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForOrderResultRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveModuleResultRe bReceiveModuleResultRe = ScriptApplication.getGson().fromJson(data, BReceiveModuleResultRe.class);
            if (bReceiveModuleResultRe != null) {
                if (!TextUtils.isEmpty(bReceiveModuleResultRe.getRetCode())) {
                    if (!"0".equals(bReceiveModuleResultRe.getRetCode())) {
                        LLog.e("服务器给客户端应答模块执行结果 retCode=" + bReceiveModuleResultRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答模块执行结果 retCode=" + bReceiveModuleResultRe.getRetCode());
                        //再次发送模块执行结果
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_MODULE, 10000);
                    } else {
                        if (bReceiveModuleResultRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_UPDATE_MODULE);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveModuleResultRe.getTimestamp();
                            SocketUtil.continueExec();
                            LLog.e("调取模块执行结果接口成功!");
                            RecordFloatView.updateMessage("调取模块执行结果接口成功!");
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答模块执行结果 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答模块执行结果 retCode为空!");
                    //再次发送模块执行结果
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_MODULE, 10000);
                }
            } else {
                LLog.e("bReceiveModuleResultRe 为空！");
                RecordFloatView.updateMessage("bReceiveModuleResultRe 为空!");
                //再次发送模块执行结果
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_MODULE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答模块执行结果时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答模块执行结果时出现异常!");
            //再次发送模块执行结果
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_MODULE, 10000);
        }
    }


    /**
     * 客户端响应一个更新设备信息
     *
     * @param msgType 消息类型 9007
     * @param object  BSendDeviceInfo
     */
    public void sendMsgForDeviceInfo(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendDeviceInfo bSendDeviceInfo = null;
        if (object instanceof BSendDeviceInfo) {
            bSendDeviceInfo = (BSendDeviceInfo) object;
        } else {
            LLog.e("instanceof BSendDeviceInfo Error !");
            RecordFloatView.updateMessage("instanceof BSendDeviceInfo Error !");
            return;
        }
        if (bSendDeviceInfo != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendDeviceInfo.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("userId", bSendDeviceInfo.getUserId());
                data.put("deviceInfo", bSendDeviceInfo.getDeviceInfo());
                data.put("loginIpInfo", bSendDeviceInfo.getLoginIpInfo());
                data.put("conditionId", bSendDeviceInfo.getConditionId());
                data.put("phoneName", bSendDeviceInfo.getPhoneName());
                data.put("timestamp", bSendDeviceInfo.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送更新设备信息成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送更新设备信息异常!");
                RecordFloatView.updateMessage("发送更新设备信息异常!");
            }
        }
    }

    /**
     * 服务器应答一个更新设备信息结果
     *
     * @param object
     */
    public void receiveMsgForDeviceInfoRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9007.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForDeviceInfoRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForDeviceInfoRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveDeviceInfoRe bReceiveDeviceInfoRe = ScriptApplication.getGson().fromJson(data, BReceiveDeviceInfoRe.class);
            if (bReceiveDeviceInfoRe != null) {
                if (!TextUtils.isEmpty(bReceiveDeviceInfoRe.getRetCode())) {
                    if (!"0".equals(bReceiveDeviceInfoRe.getRetCode())) {
                        LLog.e("服务器给客户端应答更新设备信息结果 retCode=" + bReceiveDeviceInfoRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答更新设备信息结果 retCode=" + bReceiveDeviceInfoRe.getRetCode());
                        //再次发送更新设备信息
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_DEVICE_INFO, 10000);
                    } else {

                        if (bReceiveDeviceInfoRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_UPDATE_DEVICE_INFO);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveDeviceInfoRe.getTimestamp();
                            LLog.e("调取更新设备信息接口成功!");
                            RecordFloatView.updateMessage("调取更新设备信息接口成功!");
                            SocketUtil.continueExec();//只有更新成功才继续播放脚本
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答更新设备信息结果 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答更新设备信息结果 retCode为空!");
                    //再次发送更新设备信息
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_DEVICE_INFO, 10000);
                }
            } else {
                LLog.e("bReceiveDeviceInfoRe 为空！");
                RecordFloatView.updateMessage("bReceiveDeviceInfoRe 为空!");
                //再次发送更新设备信息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_DEVICE_INFO, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答更新设备信息结果时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答更新设备信息结果时出现异常!");
            //再次发送更新设备信息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_DEVICE_INFO, 10000);
        }
    }


    /**
     * 客户端响应一个更新用户信息
     *
     * @param msgType 消息类型 9009
     * @param object  BSendUserInfo
     */
    public void sendMsgForUserInfo(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendUserInfo bSendUserInfo = null;
        if (object instanceof BSendUserInfo) {
            bSendUserInfo = (BSendUserInfo) object;
        } else {
            LLog.e("instanceof BSendDeviceInfo Error !");
            RecordFloatView.updateMessage("instanceof BSendDeviceInfo Error !");
            return;
        }
        if (bSendUserInfo != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendUserInfo.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("userId", bSendUserInfo.getUserId());
                data.put("account", bSendUserInfo.getAccount());
                data.put("passWord", bSendUserInfo.getPassWord());
                data.put("phoneName", bSendUserInfo.getPhoneName());
                data.put("realName", bSendUserInfo.getRealName());
                data.put("idCardInfo", bSendUserInfo.getIdCardInfo());
                data.put("phoneNumber", bSendUserInfo.getPhoneNumber());
                data.put("timestamp", bSendUserInfo.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送更新用户信息成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送更新用户信息异常!");
                RecordFloatView.updateMessage("发送更新用户信息异常!");
            }
        }
    }

    /**
     * 服务器应答一个更新用户信息结果
     *
     * @param object
     */
    public void receiveMsgForUserInfoRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9009.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForUserInfoRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForUserInfoRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveUserInfoRe bReceiveUserInfoRe = ScriptApplication.getGson().fromJson(data, BReceiveUserInfoRe.class);
            if (bReceiveUserInfoRe != null) {
                if (!TextUtils.isEmpty(bReceiveUserInfoRe.getRetCode())) {
                    if (!"0".equals(bReceiveUserInfoRe.getRetCode())) {
                        LLog.e("服务器给客户端应答更新用户信息结果 retCode=" + bReceiveUserInfoRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答更新用户信息结果 retCode=" + bReceiveUserInfoRe.getRetCode());
                        //再次发送更新用户信息
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_USER_INFO, 10000);
                    } else {
                        if (bReceiveUserInfoRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_UPDATE_USER_INFO);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveUserInfoRe.getTimestamp();
                            LLog.e("调取更新用户信息接口成功!");
                            RecordFloatView.updateMessage("调取更新用户信息接口成功!");
                            SocketUtil.continueExec();//只有更新成功才继续播放脚本
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答更新用户信息结果 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答更新用户信息结果 retCode为空!");
                    //再次发送更新用户信息
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_USER_INFO, 10000);
                }
            } else {
                LLog.e("bReceiveUserInfoRe 为空！");
                RecordFloatView.updateMessage("bReceiveUserInfoRe 为空!");
                //再次发送更新用户信息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_USER_INFO, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答更新用户信息结果时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答更新用户信息结果时出现异常!");
            //再次发送更新用户信息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_USER_INFO, 10000);
        }
    }

    /**
     * 客户端响应一个更新IP信息
     *
     * @param msgType 消息类型 9011
     * @param object  BSendIpInfo
     */
    public void sendMsgForIpInfo(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendIpInfo bSendIpInfo = null;
        if (object instanceof BSendIpInfo) {
            bSendIpInfo = (BSendIpInfo) object;
        } else {
            LLog.e("instanceof BSendIpInfo Error !");
            RecordFloatView.updateMessage("instanceof BSendIpInfo Error !");
            return;
        }
        if (bSendIpInfo != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendIpInfo.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("orderId", bSendIpInfo.getOrderId());
                data.put("remoteIp", bSendIpInfo.getRemoteIp());
                data.put("ipFlag", bSendIpInfo.getIpFlag());
                data.put("timestamp", bSendIpInfo.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送更新IP信息成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送更新IP信息异常!");
                RecordFloatView.updateMessage("发送更新IP信息异常!");
            }
        }
    }

    /**
     * 服务器应答一个更新IP信息结果
     *
     * @param object
     */
    public void receiveMsgForIpInfoRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9011.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForIpInfoRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForIpInfoRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveIpInfoRe bReceiveIpInfoRe = ScriptApplication.getGson().fromJson(data, BReceiveIpInfoRe.class);
            if (bReceiveIpInfoRe != null) {
                if (!TextUtils.isEmpty(bReceiveIpInfoRe.getRetCode())) {
                    if (!"0".equals(bReceiveIpInfoRe.getRetCode())) {

                        LLog.e("服务器给客户端应答更新IP信息结果 retCode=" + bReceiveIpInfoRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答更新IP信息结果 retCode=" + bReceiveIpInfoRe.getRetCode());
                        //再次发送更新IP信息
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_IP_INFO, 10000);

                    } else {
                        if (bReceiveIpInfoRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_UPDATE_IP_INFO);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveIpInfoRe.getTimestamp();
                            LLog.e("调取更新IP接口成功!");
                            RecordFloatView.updateMessage("调取更新IP接口成功!");
                            if (bReceiveIpInfoRe.getIpFlag() == 0) {//第一次返回IP信息
                                SocketUtil.continueExec();//只有更新成功才继续播放脚本
                            } else if (bReceiveIpInfoRe.getIpFlag() == 1) {//第二次返回IP信息 订单结束
                                //返回订单处理成功与否
                                ScriptApplication.getService().execute(() -> {
                                    if (SpUtil.getKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false)) {
                                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_SUCCESS);
                                    } else {
                                        FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "服务器应答一个更新IP信息结果失败 : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_FAILED);
                                    }
                                });
                            }
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答更新IP信息结果 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答更新IP信息结果 retCode为空!");
                    //再次发送更新IP信息
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_IP_INFO, 10000);
                }
            } else {
                LLog.e("bReceiveIpInfoRe 为空！");
                RecordFloatView.updateMessage("bReceiveIpInfoRe 为空!");
                //再次发送更新IP信息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_IP_INFO, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答更新IP信息结果时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答更新IP信息结果时出现异常!");
            //再次发送更新IP信息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_IP_INFO, 10000);
        }
    }


    /**
     * 客户端发起一个获取身份证信息
     *
     * @param msgType 消息类型 9013
     * @param object  BRequestGetIdCard
     */
    public void sendMsgToGetIdCard(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BRequestGetIdCard bRequestGetIdCard = null;
        if (object instanceof BRequestGetIdCard) {
            bRequestGetIdCard = (BRequestGetIdCard) object;
        } else {
            LLog.e("instanceof BRequestGetIdCard Error !");
            RecordFloatView.updateMessage("instanceof BRequestGetIdCard Error !");
            return;
        }
        if (bRequestGetIdCard != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bRequestGetIdCard.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("conditionId", bRequestGetIdCard.getConditionId());
                data.put("timestamp", bRequestGetIdCard.getTimestamp());
                data.put("gender", bRequestGetIdCard.getGender());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送获取身份证信息成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送获取身份证信息异常!");
                RecordFloatView.updateMessage("发送获取身份证信息异常!");
            }
        }
    }

    /**
     * 服务器应答一个获取身份证信息结果
     *
     * @param object
     */
    public void receiveMsgToGetIdCardRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9013.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToGetIdCardRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToGetIdCardRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveIdCardRe bReceiveIdCardRe = ScriptApplication.getGson().fromJson(data, BReceiveIdCardRe.class);
            if (bReceiveIdCardRe != null) {
                if (!TextUtils.isEmpty(bReceiveIdCardRe.getRetCode())) {
                    if (!"0".equals(bReceiveIdCardRe.getRetCode())) {
                        LLog.e("服务器给客户端应答获取身份证信息 retCode=" + bReceiveIdCardRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答获取身份证信息 retCode=" + bReceiveIdCardRe.getRetCode());
                        //再次发送获取身份证信息
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_ID_CARD, 10000);
                    } else {
                        if (bReceiveIdCardRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_GET_ID_CARD);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveIdCardRe.getTimestamp();
                            MobileRegisterHelper.getInstance().dealWithIdCardRe(bReceiveIdCardRe.getRealName(), bReceiveIdCardRe.getIdCardInfo(), bReceiveIdCardRe.getGender());
                            LLog.e("调取获取身份证信息接口成功!");
                            RecordFloatView.updateMessage("调取获取身份证信息接口成功!");
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答获取身份证信息 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答获取身份证信息 retCode为空!");
                    //再次发送获取身份证信息
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_ID_CARD, 10000);
                }
            } else {
                LLog.e("bReceiveIdCardRe 为空！");
                RecordFloatView.updateMessage("bReceiveIdCardRe 为空!");
                //再次发送获取身份证信息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_ID_CARD, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答获取身份证信息时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答取身份证信息时出现异常!");
            //再次发送获取身份证信息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_ID_CARD, 10000);
        }
    }

    /**
     * 客户端发起一个获取手机号码
     *
     * @param msgType 消息类型 9015
     * @param object  BRequestPhoneNum
     */
    public void sendMsgToGetPhoneNumber(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BRequestPhoneNum bRequestPhoneNum = null;
        if (object instanceof BRequestPhoneNum) {
            bRequestPhoneNum = (BRequestPhoneNum) object;
        } else {
            LLog.e("instanceof BRequestPhoneNum Error !");
            RecordFloatView.updateMessage("instanceof BRequestPhoneNum Error !");
            return;
        }
        if (bRequestPhoneNum != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bRequestPhoneNum.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("userId", bRequestPhoneNum.getUserId());
                data.put("channel", bRequestPhoneNum.getChannel());
                data.put("itemId", bRequestPhoneNum.getItemId());
                data.put("phoneType", bRequestPhoneNum.getPhoneType());
                data.put("notPrefix", bRequestPhoneNum.getNotPrefix());
                data.put("mobile", bRequestPhoneNum.getMobile());
                data.put("conditionId", bRequestPhoneNum.getConditionId());
                data.put("repeat", bRequestPhoneNum.getRepeat());
                data.put("timestamp", bRequestPhoneNum.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送获取手机号码成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送获取手机号码异常!");
                RecordFloatView.updateMessage("发送获取手机号码异常!");
            }
        }
    }

    /**
     * 服务器应答一个获取手机号码结果
     *
     * @param object
     */
    public void receiveMsgToGetPhoneNumberRe(Object object) {

        MobileRegisterHelper.getInstance().getPhoneCount++;
        if (MobileRegisterHelper.getInstance().getPhoneCount > 20) {
            MobileRegisterHelper.getInstance().getPhoneCount = 0;
            BrushOrderHelper.getInstance().orderDealFailure("重新获取手机号超过20次!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            return;
        }
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9015.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToGetPhoneNumberRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToGetPhoneNumberRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceivePhoneNumRe bReceivePhoneNumRe = ScriptApplication.getGson().fromJson(data, BReceivePhoneNumRe.class);
            if (bReceivePhoneNumRe != null) {
                if (!TextUtils.isEmpty(bReceivePhoneNumRe.getRetCode())) {
                    if (!"0".equals(bReceivePhoneNumRe.getRetCode())) {
                        LLog.e("服务器给客户端应答获取手机号码 retCode=" + bReceivePhoneNumRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答获取手机号码 retCode=" + bReceivePhoneNumRe.getRetCode());
                        //再次发送获取手机号码
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_PHONE_NUMBER, 10000);
                    } else {
                        if (bReceivePhoneNumRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_GET_PHONE_NUMBER);
                            BrushTask.getInstance().b_currentTimeStamp = bReceivePhoneNumRe.getTimestamp();
                            MobileRegisterHelper.getInstance().dealWithPhoneNumberRe(bReceivePhoneNumRe.getPhone());
                            LLog.e("调取获取手机号码接口成功! " + bReceivePhoneNumRe.getPhone());
                            RecordFloatView.updateMessage("调取获取手机号码接口成功! " + bReceivePhoneNumRe.getPhone());
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答获取手机号码 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答获取手机号码 retCode为空!");
                    //再次发送获取手机号码
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_PHONE_NUMBER, 10000);
                }
            } else {
                LLog.e("bReceivePhoneNumRe 为空！");
                RecordFloatView.updateMessage("bReceivePhoneNumRe 为空!");
                //再次发送获取手机号码
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_PHONE_NUMBER, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答获取手机号码时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答获取手机号码时出现异常!");
            //再次发送获取手机号码
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_PHONE_NUMBER, 10000);
        }
    }

    /**
     * 客户端发起一个获取手机短信
     *
     * @param msgType 消息类型 9017
     * @param object  BRequestMessage
     */
    public void sendMsgToGetMessage(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BRequestMessage bRequestMessage = null;
        if (object instanceof BRequestMessage) {
            bRequestMessage = (BRequestMessage) object;
        } else {
            LLog.e("instanceof BRequestMessage Error !");
            RecordFloatView.updateMessage("instanceof BRequestMessage Error !");
            return;
        }
        if (bRequestMessage != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bRequestMessage.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("channel", bRequestMessage.getChannel());
                data.put("phone", bRequestMessage.getPhone());
                data.put("itemId", bRequestMessage.getItemId());
                data.put("conditionId", bRequestMessage.getConditionId());
                data.put("timestamp", bRequestMessage.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送获取手机短信成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送获取手机短信异常!");
                RecordFloatView.updateMessage("发送获取手机短信异常!");
            }
        }
    }

    /**
     * 服务器应答一个获取手机短信结果
     *
     * @param object
     */
    public void receiveMsgToGetMessageRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9017.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToGetMessageRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToGetMessageRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveMessage bReceiveMessage = ScriptApplication.getGson().fromJson(data, BReceiveMessage.class);
            if (bReceiveMessage != null) {
                if (!TextUtils.isEmpty(bReceiveMessage.getRetCode())) {
                    if (!"0".equals(bReceiveMessage.getRetCode())) {
                        LLog.e("服务器给客户端应答获取手机短信 retCode=" + bReceiveMessage.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答获获取手机短信 retCode=" + bReceiveMessage.getRetCode());
                        //再次发送获取手机短信
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_MESSAGE, 10000);
                    } else {
                        if (bReceiveMessage.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_GET_MESSAGE);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveMessage.getTimestamp();
                            String message = bReceiveMessage.getMessage();
                            MobileRegisterHelper.getInstance().dealWithMessageRe(message);
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答获取手机短信 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答获取手机短信 retCode为空!");
                    //再次发送获取手机短信
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_MESSAGE, 10000);
                }
            } else {
                LLog.e("bReceiveMessage 为空！");
                RecordFloatView.updateMessage("bReceiveMessage 为空!");
                //再次发送获取手机短信
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_MESSAGE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答获取手机短信时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答获取手机短信时出现异常!");
            //再次发送获取手机短信
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_MESSAGE, 10000);
        }
    }

    /**
     * 客户端发起一个释放手机号码
     *
     * @param msgType 消息类型 9019
     * @param object  BRequestGetIdCard
     */
    public void sendMsgToReleasePhone(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendReleasePhoneNum bSendReleasePhoneNum = null;
        if (object instanceof BSendReleasePhoneNum) {
            bSendReleasePhoneNum = (BSendReleasePhoneNum) object;
        } else {
            LLog.e("instanceof BSendReleasePhoneNum Error !");
            RecordFloatView.updateMessage("instanceof BSendReleasePhoneNum Error !");
            return;
        }
        if (bSendReleasePhoneNum != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendReleasePhoneNum.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("itemId", bSendReleasePhoneNum.getItmeId());
                data.put("channel", bSendReleasePhoneNum.getChannel());
                data.put("phone", bSendReleasePhoneNum.getPhone());
                data.put("timestamp", bSendReleasePhoneNum.getTimestamp());
                jsonObject.put("data", data.toString());
                b_nioPackageAndSend(jsonObject, "发送释放手机号码成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送释放手机号码异常!");
                RecordFloatView.updateMessage("发送释放手机号码异常!");
            }
        }
    }

    /**
     * 服务器应答一个释放手机号码
     *
     * @param object
     */
    public void receiveMsgToReleasePhoneRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9019.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToReleasePhoneRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToReleasePhoneRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveReleasePhoneRe bReceiveReleasePhoneRe = ScriptApplication.getGson().fromJson(data, BReceiveReleasePhoneRe.class);
            if (bReceiveReleasePhoneRe != null) {
                if (!TextUtils.isEmpty(bReceiveReleasePhoneRe.getRetCode())) {
                    if (!"0".equals(bReceiveReleasePhoneRe.getRetCode())) {
                        LLog.e("服务器给客户端应答释放手机号码 retCode=" + bReceiveReleasePhoneRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答释放手机号码 retCode=" + bReceiveReleasePhoneRe.getRetCode());
                        //再次发送释放手机号码
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_RELEASE_PHONE, 10000);
                    } else {
                        if (bReceiveReleasePhoneRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_RELEASE_PHONE);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveReleasePhoneRe.getTimestamp();
                            LLog.e("调取释放手机号码成功!");
                            RecordFloatView.updateMessage("调取释放手机号码成功!");
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答释放手机号码 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答释放手机号码 retCode为空!");
                    //再次发送释放手机号码
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_RELEASE_PHONE, 10000);
                }
            } else {
                LLog.e("bReceiveReleasePhoneRe 为空！");
                RecordFloatView.updateMessage("bReceiveReleasePhoneRe 为空!");
                //再次发送释放手机号码
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_RELEASE_PHONE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答释放手机号码时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答释放手机号码时出现异常!");
            //再次发送释放手机号码
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_RELEASE_PHONE, 10000);
        }
    }


    /**
     * 客户端发送一张验证码截图
     *
     * @param msgType 消息类型 9031
     * @param object  UploadImg
     */
    public void sendMsgToUploadVerifyCapture(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendVerifyCodeImg bSendVerifyCodeImg = null;
        if (object instanceof BSendVerifyCodeImg) {
            bSendVerifyCodeImg = (BSendVerifyCodeImg) object;
        } else {
            LLog.e("instanceof BSendVerifyCodeImg Error !");
            RecordFloatView.updateMessage("instanceof BSendVerifyCodeImg Error !");
            return;
        }
        if (bSendVerifyCodeImg != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendVerifyCodeImg.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("conditionId", bSendVerifyCodeImg.getConditionId());
                data.put("verifyType", bSendVerifyCodeImg.getVerifyType());
                data.put("imageName", bSendVerifyCodeImg.getImageName());
                data.put("imageData", bSendVerifyCodeImg.getImageData());
                JSONArray arrayNameList = null;
                if (bSendVerifyCodeImg.getImageNameList() != null) {
                    arrayNameList = new JSONArray(bSendVerifyCodeImg.getImageNameList().toArray());
                }
                JSONArray arrayDataList = null;
                if (bSendVerifyCodeImg.getImageDataList() != null) {
                    arrayDataList = new JSONArray(bSendVerifyCodeImg.getImageDataList().toArray());
                }
                data.put("imageNameList", arrayNameList);
                data.put("imageDataList", arrayDataList);
                data.put("timeStamp", bSendVerifyCodeImg.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "上传验证码截图成功! 图片名称:" + bSendVerifyCodeImg.getImageName(), false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始上传验证码截图异常!");
                RecordFloatView.updateMessage("开始上传验证码截图异常!");
            }
        }
    }


    /**
     * 服务器端应答发送验证码图片成功与否
     *
     * @param object
     */
    public void receiveMsgToUploadVerifyCaptureRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9031.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForUploadImgRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForUploadImgRe Error !");
            //再次发起上传图片
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答上传图片成功与否data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答上传图片成功与否data为空 response:" + response.toString());
                //再次上传当前图片
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 10000);
                return;
            }
            BReceiveVerifyCodeUploadRe bReceiveVerifyCodeUploadRe = ScriptApplication.getGson().fromJson(data, BReceiveVerifyCodeUploadRe.class);
            if (bReceiveVerifyCodeUploadRe != null) {
                if (!TextUtils.isEmpty(bReceiveVerifyCodeUploadRe.getRetcode())) {
                    String retCode = bReceiveVerifyCodeUploadRe.getRetcode();
                    if (!"0".equals(retCode)) {
                        LLog.e("服务端应答上传图片成功与否retCode=" + retCode);
                        RecordFloatView.updateMessage("服务端应答上传图片成功与否retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器端应答发送一张图片成功与否 任务不存在!");
                            RecordFloatView.updateMessage("服务器端应答发送一张图片成功与否 任务不存在!");
                            BaseOrderHelper.resetData();
                        } else if ("2".equals(retCode)) {
                            LLog.e("发起图片上传图片名称为空!");
                            RecordFloatView.updateMessage("发起图片上传图片名称为空!");
                            //再次上传当前图片
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 10000);
                        }
                    } else {
                        if (bReceiveVerifyCodeUploadRe.getTimeStamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveVerifyCodeUploadRe.getTimeStamp();
                            MobileRegisterHelper.getInstance().dealWithUploadVerifyCodeRe(bReceiveVerifyCodeUploadRe.getVerifyNumber());
                            BrushTask.getInstance().b_currentUploadNum++;
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 100);
                        }
                    }
                } else {
                    LLog.e("服务端应答上传图片成功与否retCode为空!");
                    RecordFloatView.updateMessage("服务端应答上传图片成功与否retCode为空!");
                    //再次上传当前图片
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 10000);
                }
            } else {
                LLog.e("bReceiveVerifyCodeUploadRe 为空!");
                RecordFloatView.updateMessage("bReceiveVerifyCodeUploadRe 为空!");
                //再次上传当前图片
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务端应答上传图片的请求时出现异常!");
            RecordFloatView.updateMessage("服务端应答上传图片的请求时出现异常!");
            //再次上传当前图片
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 10000);
        }
    }

    /**
     * 客户端发起一个获取图片验证码
     *
     * @param msgType 消息类型 9027
     * @param object  BRequestGetIdCard
     */
    public void sendMsgToGetVerifyCode(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BRequestGetVerifyCode bRequestGetVerifyCode = null;
        if (object instanceof BRequestGetVerifyCode) {
            bRequestGetVerifyCode = (BRequestGetVerifyCode) object;
        } else {
            LLog.e("instanceof BRequestGetVerifyCode Error !");
            RecordFloatView.updateMessage("instanceof BRequestGetVerifyCode Error !");
            return;
        }
        if (bRequestGetVerifyCode != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bRequestGetVerifyCode.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("verifyNumber", bRequestGetVerifyCode.getVerifyNumber());
                data.put("timestamp", bRequestGetVerifyCode.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送获取图片验证码成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送获取图片验证码异常!");
                RecordFloatView.updateMessage("发送获取图片验证码异常!");
            }
        }
    }

    /**
     * 服务器应答一个获取图片验证码
     *
     * @param object
     */
    public void receiveMsgToGetVerifyCodeRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9027.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        Constants.failGetVerifyCodeCount++;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToGetVerifyCodeRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToGetVerifyCodeRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveVerifyCodeRe bReceiveVerifyCodeRe = ScriptApplication.getGson().fromJson(data, BReceiveVerifyCodeRe.class);
            if (bReceiveVerifyCodeRe != null) {
                if (!TextUtils.isEmpty(bReceiveVerifyCodeRe.getRetCode())) {
                    if ((!"0".equals(bReceiveVerifyCodeRe.getRetCode()))) {
                        if (Constants.failGetVerifyCodeCount > 15 || ("-1".equals(bReceiveVerifyCodeRe.getRetCode()))) {
                            Constants.failGetVerifyCodeCount = 0;
                            FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "服务器应答一个获取图片验证码超过15次 : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");


                            ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_FAILED);
                            return;
                        }
                        LLog.e("服务器给客户端应答获取图片验证码 retCode=" + bReceiveVerifyCodeRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答获取图片验证码 retCode=" + bReceiveVerifyCodeRe.getRetCode());
                        //再次发送获取图片验证码
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_VERY_CODE, 10000);
                    } else if ("0".equals(bReceiveVerifyCodeRe.getRetCode())) {
                        Constants.failGetVerifyCodeCount = 0;
                        if (bReceiveVerifyCodeRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_GET_VERY_CODE);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveVerifyCodeRe.getTimestamp();
                            MobileRegisterHelper.getInstance().dealWithGetVerifyCodeRe(bReceiveVerifyCodeRe.getVerify());
                            LLog.e("调取获取图片验证码成功!");
                            RecordFloatView.updateMessage("调取获取图片验证码成功!");
                        }
                    }
                } else {
                    if (Constants.failGetVerifyCodeCount > 15) {
                        Constants.failGetVerifyCodeCount = 0;
                        FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "服务器应答一个获取图片验证码超过15次1 : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");

                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_FAILED);
                        return;
                    }
                    LLog.e("服务器给客户端应答获取图片验证码 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答获取图片验证码 retCode为空!");
                    //再次发送获取图片验证码
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_VERY_CODE, 10000);
                }
            } else {
                if (Constants.failGetVerifyCodeCount > 15) {
                    Constants.failGetVerifyCodeCount = 0;
                    FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "服务器应答一个获取图片验证码超过15次2 : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");

                    ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_FAILED);
                    return;
                }
                LLog.e("bReceiveVerifyCodeRe 为空！");
                RecordFloatView.updateMessage("bReceiveVerifyCodeRe 为空!");
                //再次发送获取图片验证码
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_VERY_CODE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答获取图片验证码时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答获取图片验证码时出现异常!");
            //再次发送获取图片验证码
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_VERY_CODE, 10000);
        }
    }


    /**
     * 客户端发送警报
     *
     * @param msgType 消息类型 9033
     * @param object  BSendAlert
     */
    public void sendMsgToCallAlert(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendAlert bSendAlert = null;
        if (object instanceof BSendAlert) {
            bSendAlert = (BSendAlert) object;
        } else {
            LLog.e("instanceof BSendAlert Error !");
            RecordFloatView.updateMessage("instanceof BSendAlert Error !");
            return;
        }
        if (bSendAlert != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendAlert.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("errorType", bSendAlert.getErrorType());
                data.put("errorDesc", bSendAlert.getErrorDesc());
                data.put("phoneName", bSendAlert.getPhoneName());
                data.put("orderId", bSendAlert.getOrderId());
                data.put("timestamp", bSendAlert.getTimestamp());

                if (!TextUtils.isEmpty(bSendAlert.getImageName())) {
                    data.put("imageName", bSendAlert.getImageName());
                }
                if (!TextUtils.isEmpty(bSendAlert.getImageData())) {
                    data.put("imageData", bSendAlert.getImageData());
                }
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送警报成功!", true);
            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("客户端开始发送警报报告时出现异常!");
            }
        }
    }


    /**
     * 服务器端应答警报报告
     *
     * @param object
     */
    public void receiveMsgToCallAlertRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9033.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToCallAlertRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToCallAlertRe Error !");
            //再次发起警报报告
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_CALL_ALERT, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务器端应答警报报告data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务器端应答警报报告data为空 response:" + response.toString());
                //再次发起警报报告
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_CALL_ALERT, 10000);
                return;
            }
            BReceiveAlertRe bReceiveAlertRe = ScriptApplication.getGson().fromJson(data, BReceiveAlertRe.class);
            if (bReceiveAlertRe != null) {
                if (!TextUtils.isEmpty(bReceiveAlertRe.getRetCode())) {
                    String retCode = bReceiveAlertRe.getRetCode();
                    if (!"0".equals(retCode)) {
                        LLog.e("服务器端应答警报报告retCode=" + retCode);
                        RecordFloatView.updateMessage("服务器端应答警报报告retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器端应答警报报告 任务不存在!");
                            RecordFloatView.updateMessage("服务器端应答警报报告 任务不存在!");
                            BaseOrderHelper.resetData();
                        } else if ("2".equals(retCode)) {
                            LLog.e("服务器端应答警报报告 警报类型不存在!");
                            RecordFloatView.updateMessage("服务器端应答警报报告 警报类型不存在!");
                            //再次发起警报报告
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_CALL_ALERT, 10000);
                        }
                    } else {
                        long timeStamp = bReceiveAlertRe.getTimestamp();
                        if (timeStamp > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_CALL_ALERT);
                            BrushTask.getInstance().b_currentTimeStamp = timeStamp;
                            String alertType = bReceiveAlertRe.getErrorType();
                            LLog.e("报警成功:" + alertType);
                            RecordFloatView.updateMessage("报警成功:" + alertType);
//                            if (BAlertConfig.B_ALERT_AND_REDO_SCRIPT_ERROR.equals(alertType)) {
//                                LLog.e("即将重新执行本地订单!");
//                                RecordFloatView.updateMessage("即将重新执行本地订单!");
//                                BrushTask.sendMessage(BrushTask.B_MSG_TYPE_DEAL_WITH_ORDER, Constants.sBrushOrderInfo, 1 * 60 * 1000);
//                                return;
//                            }
                            if (!BAlertConfig.B_ALERT_AND_PAUSE_KEFU_DO.equals(alertType)) {
                                FileUtil.saveFile("", Constants.BRUSH_ORDER_FOLDER_TEMP_PATH);
                                Constants.sBrushOrderInfo = null;
                                BaseOrderHelper.resetData();//重置状态值
                            }
                        }
                    }
                }
            } else {
                LLog.e("bReceiveAlertRe 为空!");
                RecordFloatView.updateMessage("bReceiveAlertRe 为空!");
                //再次发起警报报告
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_CALL_ALERT, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器端应答警报报告时出现异常!");
            RecordFloatView.updateMessage("服务器端应答警报报告时出现异常!");
            //再次发起警报报告
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_CALL_ALERT, 10000);
        }
    }


    /**
     * 客户端发起一个上传keychain文件
     *
     * @param msgType 消息类型 9035
     * @param object  BSendKeychainFile
     */
    public void sendMsgToUploadKeychain(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendKeychainFile bSendKeychainFile = null;
        if (object instanceof BSendKeychainFile) {
            bSendKeychainFile = (BSendKeychainFile) object;
        } else {
            LLog.e("instanceof BSendKeychainFile Error !");
            RecordFloatView.updateMessage("instanceof BSendKeychainFile Error !");
            return;
        }
        if (bSendKeychainFile != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendKeychainFile.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("keychainName", bSendKeychainFile.getKeychainName());
                data.put("keychainData", bSendKeychainFile.getKeychainData());
                data.put("userId", bSendKeychainFile.getUserId());
                data.put("timestamp", bSendKeychainFile.getTimestamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送上传keychain文件成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送上传keychain文件异常!");
                RecordFloatView.updateMessage("发送上传keychain文件异常!");
            }
        }
    }

    /**
     * 服务器应答一个上传keychain文件
     *
     * @param object
     */
    public void receiveMsgToUploadKeychainRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9035.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToUploadKeychainRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToUploadKeychainRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveKeychainFileRe bReceiveKeychainFileRe = ScriptApplication.getGson().fromJson(data, BReceiveKeychainFileRe.class);
            if (bReceiveKeychainFileRe != null) {
                if (!TextUtils.isEmpty(bReceiveKeychainFileRe.getRetCode())) {
                    if (!"0".equals(bReceiveKeychainFileRe.getRetCode())) {
                        LLog.e("服务器给客户端应答上传keychain文件 retCode=" + bReceiveKeychainFileRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答上传keychain文件 retCode=" + bReceiveKeychainFileRe.getRetCode());
                        //再次发送上传keychain文件
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_KEYCHAIN, 10000);
                    } else {
                        if (bReceiveKeychainFileRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_UPLOAD_KEYCHAIN);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveKeychainFileRe.getTimestamp();
                            SocketUtil.continueExec();
                            LLog.e("调取上传keychain文件成功!");
                            RecordFloatView.updateMessage("调取上传keychain文件成功!");
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答上传keychain文件 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答上传keychain文件 retCode为空!");
                    //再次发送上传keychain文件
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_KEYCHAIN, 10000);
                }
            } else {
                LLog.e("bReceiveKeychainFileRe 为空！");
                RecordFloatView.updateMessage("bReceiveKeychainFileRe 为空!");
                //再次发送上传keychain文件
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_KEYCHAIN, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答上传keychain文件时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答上传keychain文件时出现异常!");
            //再次发送上传keychain文件
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_KEYCHAIN, 10000);
        }
    }

    /**
     * 客户端发送远程协助截图
     *
     * @param msgType 消息类型 9039
     * @param object  BSendRemoteAssistanceImg
     */
    public void sendMsgToRequestRemoteAssistance(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendRemoteAssistanceImg bSendRemoteAssistanceImg = null;
        if (object instanceof BSendRemoteAssistanceImg) {
            bSendRemoteAssistanceImg = (BSendRemoteAssistanceImg) object;
        } else {
            LLog.e("instanceof BSendRemoteAssistanceImg Error !");
            RecordFloatView.updateMessage("instanceof BSendRemoteAssistanceImg Error !");
            return;
        }
        if (bSendRemoteAssistanceImg != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendRemoteAssistanceImg.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("imageData", bSendRemoteAssistanceImg.getImageData());
                data.put("conditionId", bSendRemoteAssistanceImg.getConditionId());
                data.put("gameOS", bSendRemoteAssistanceImg.getGameOS());
                data.put("deviceName", bSendRemoteAssistanceImg.getDeviceName());
                data.put("timeStamp", bSendRemoteAssistanceImg.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送远程协助截图成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送远程协助截图异常!");
                RecordFloatView.updateMessage("发送远程协助截图异常!");
            }
        }
    }

    /**
     * 服务器应答远程协助截图
     *
     * @param object
     */
    public void receiveMsgToRequestRemoteAssistanceRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9039.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToRequestRemoteAssistanceRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToRequestRemoteAssistanceRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveRemoteAssistanceImgRe bReceiveRemoteAssistanceImgRe = ScriptApplication.getGson().fromJson(data, BReceiveRemoteAssistanceImgRe.class);
            if (bReceiveRemoteAssistanceImgRe != null) {
                if (!TextUtils.isEmpty(bReceiveRemoteAssistanceImgRe.getRetCode())) {
                    if (!"0".equals(bReceiveRemoteAssistanceImgRe.getRetCode())) {
                        LLog.e("服务器给客户端应答远程协助 retCode=" + bReceiveRemoteAssistanceImgRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答远程协助 retCode=" + bReceiveRemoteAssistanceImgRe.getRetCode());
                        //再次发送远程协助截图
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE, 10000);
                    } else {
                        if (bReceiveRemoteAssistanceImgRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveRemoteAssistanceImgRe.getTimestamp();
                            LLog.e("调取远程协助成功!");
                            RecordFloatView.updateMessage("调取远程协助成功!");
                            FileHelper.deleteFolderFile(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId());
                            String imgId = bReceiveRemoteAssistanceImgRe.getId();
                            BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_GET_REMOTE_ASSISTANCE_CMD, imgId, 1000);
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答远程协助 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答远程协助 retCode为空!");
                    //再次发送远程协助截图
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE, 10000);
                }
            } else {
                LLog.e("bReceiveRemoteAssistanceImgRe 为空！");
                RecordFloatView.updateMessage("bReceiveRemoteAssistanceImgRe 为空!");
                //再次发送远程协助截图
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答远程协助截图时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答远程协助截图时出现异常!");
            //再次发送远程协助截图
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE, 10000);
        }
    }

    /**
     * 客户端发送获取远程协助指令
     *
     * @param msgType 消息类型 9041
     * @param object  BRequestGetAssistanceCmd
     */
    public void sendMsgToGetAssistanceCmd(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BRequestGetAssistanceCmd bRequestGetAssistanceCmd = null;
        if (object instanceof BRequestGetAssistanceCmd) {
            bRequestGetAssistanceCmd = (BRequestGetAssistanceCmd) object;
        } else {
            LLog.e("instanceof BRequestGetAssistanceCmd Error !");
            RecordFloatView.updateMessage("instanceof BRequestGetAssistanceCmd Error !");
            return;
        }
        if (bRequestGetAssistanceCmd != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bRequestGetAssistanceCmd.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("id", bRequestGetAssistanceCmd.getId());
                data.put("timestamp", bRequestGetAssistanceCmd.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送获取远程协助指令成功!", false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送获取远程协助指令异常!");
                RecordFloatView.updateMessage("发送获取远程协助指令异常!");
            }
        }
    }

    /**
     * 服务器应答获取远程协助指令
     *
     * @param object
     */
    public void receiveMsgToGetAssistanceCmdRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9041.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToRequestRemoteAssistanceRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToRequestRemoteAssistanceRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("data为空 response:" + response.toString());
                RecordFloatView.updateMessage("data为空 response:" + response.toString());
                return;
            }
            BReceiveAssistanceCmdRe bReceiveAssistanceCmdRe = ScriptApplication.getGson().fromJson(data, BReceiveAssistanceCmdRe.class);
            if (bReceiveAssistanceCmdRe != null) {
                if (!TextUtils.isEmpty(bReceiveAssistanceCmdRe.getRetCode())) {
                    if (!"0".equals(bReceiveAssistanceCmdRe.getRetCode())) {
                        LLog.e("服务器给客户端应答获取远程协助指令 retCode=" + bReceiveAssistanceCmdRe.getRetCode());
                        RecordFloatView.updateMessage("服务器给客户端应答获取远程协助指令 retCode=" + bReceiveAssistanceCmdRe.getRetCode());
                        //再次发送获取远程协助指令
                        BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_GET_REMOTE_ASSISTANCE_CMD, bReceiveAssistanceCmdRe.getId(), 3000);
                    } else {
                        if (bReceiveAssistanceCmdRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_GET_REMOTE_ASSISTANCE_CMD);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveAssistanceCmdRe.getTimestamp();
                            LLog.e("调取获取远程协助指令成功!");
                            RecordFloatView.updateMessage("调取获取远程协助指令成功!");
                            String cmd = bReceiveAssistanceCmdRe.getCmdContent();
                            String instruct = InstructUtil.script2Cmd(cmd);
                            SocketUtil.sendInstruct(instruct, false);
                            SystemClock.sleep(1000);
                            SocketUtil.continueExec();
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答获取远程协助指令 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答获取远程协助指令 retCode为空!");
                    //再次发送获取远程协助指令
                    BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_GET_REMOTE_ASSISTANCE_CMD, bReceiveAssistanceCmdRe.getId(), 10000);
                }
            } else {
                LLog.e("bReceiveAssistanceCmdRe 为空！");
                RecordFloatView.updateMessage("bReceiveAssistanceCmdRe 为空!");
                //再次发送获取远程协助
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端应答获取远程协助指令时出现异常！");
            RecordFloatView.updateMessage("服务器给客户端应答获取远程协助指令时出现异常!");
            //再次发送获取远程协助
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE, 10000);
        }
    }

    /**
     * 客户端发送一个准备上传图片的请求
     *
     * @param msgType 消息类型 9043
     * @param object  BSendRequestUploadImg
     */

    public void sendMsgToRequestUploadImage(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendRequestUploadImg bSendRequestUploadImg = null;
        if (object instanceof BSendRequestUploadImg) {
            bSendRequestUploadImg = (BSendRequestUploadImg) object;
        } else {
            LLog.e("instanceof BSendRequestUploadImg Error !");
            RecordFloatView.updateMessage("instanceof BSendRequestUploadImg Error !");
            return;
        }
        if (bSendRequestUploadImg != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendRequestUploadImg.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("phoneName", bSendRequestUploadImg.getPhoneName());
                JSONArray jsonArray = null;
                if (bSendRequestUploadImg.getImageNames() != null) {
                    jsonArray = new JSONArray(bSendRequestUploadImg.getImageNames().toArray());
                }
                data.put("imageNames", jsonArray);
                data.put("timeStamp", bSendRequestUploadImg.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送请求上传图片成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送请求上传图片异常!");
                RecordFloatView.updateMessage("发送请求上传图片异常!");
            }
        }
    }


    /**
     * 服务端应答一个准备上传图片的请求
     *
     * @param object
     */
    public void receiveMsgToRequestUploadImageRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9043.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToRequestUploadImageRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToRequestUploadImageRe Error !");
            //再次发起上传图片
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答上传图片的请求data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答上传图片的请求data为空 response:" + response.toString());
                //再次发起上传图片
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 10000);
                return;
            }
            BReceiveRequestUploadImgRe bReceiveRequestUploadImgRe = ScriptApplication.getGson().fromJson(data, BReceiveRequestUploadImgRe.class);
            if (bReceiveRequestUploadImgRe != null) {
                if (!TextUtils.isEmpty(bReceiveRequestUploadImgRe.getRetCode())) {
                    String retCode = bReceiveRequestUploadImgRe.getRetCode();
                    if (!"0".equals(retCode)) {
                        LLog.e("服务端应答上传图片的请求retCode=" + retCode);
                        RecordFloatView.updateMessage("服务端应答上传图片的请求retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务端应答一个准备上传图片的请求 任务不存在!");
                            RecordFloatView.updateMessage("服务端应答一个准备上传图片的请求 任务不存在!");
                            BaseOrderHelper.resetData();
                            return;
                        } else if ("2".equals(retCode)) {
                            LLog.e("发起图片上传请求imageNames为空!");
                            RecordFloatView.updateMessage("发起图片上传请求imageNames为空!");
                            //再次发起上传图片
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 10000);
                        }
                    } else {
                        if (bReceiveRequestUploadImgRe.getTimeStamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveRequestUploadImgRe.getTimeStamp();
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE);
                            //上传图片
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 1000);
                        }
                    }
                } else {
                    LLog.e("服务端应答上传图片retCode为空!");
                    RecordFloatView.updateMessage("服务端应答上传图片retCode为空!");
                    //再次发起上传图片
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 10000);
                }
            } else {
                LLog.e("bReceiveRequestUploadImgRe 为空!");
                RecordFloatView.updateMessage("bReceiveRequestUploadImgRe 为空!");
                //再次发起上传图片
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务端应答上传图片的请求时出现异常!");
            RecordFloatView.updateMessage("服务端应答上传图片的请求时出现异常!");
            //再次发起上传图片
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 10000);
        }

    }

    /**
     * 客户端发送一张图片
     *
     * @param msgType 消息类型 9045
     * @param object  BSendUploadImage
     */
    public void sendMsgToUploadOneImage(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendUploadImage bSendUploadImage = null;
        if (object instanceof BSendUploadImage) {
            bSendUploadImage = (BSendUploadImage) object;
        } else {
            LLog.e("instanceof BSendUploadImage Error !");
            RecordFloatView.updateMessage("instanceof BSendUploadImage Error !");
            //再次发起上传图片
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 10000);
            return;
        }
        if (bSendUploadImage != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendUploadImage.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("taskType", bSendUploadImage.getTaskType());
                data.put("phoneName", bSendUploadImage.getPhoneName());
                data.put("conditionId", bSendUploadImage.getConditionId());
                data.put("userId", bSendUploadImage.getUserId());
                data.put("orderId", bSendUploadImage.getOrderId());
                data.put("imageName", bSendUploadImage.getImageName());
                data.put("imageData", bSendUploadImage.getImageData());
                data.put("timestamp", bSendUploadImage.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发起上传图片成功!", false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始上传图片异常!");
                RecordFloatView.updateMessage("开始上传图片异常!");
                //再次发起上传一张图片
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 10000);
            }
        }
    }


    /**
     * 服务器端应答发送一张图片成功与否
     *
     * @param object
     */
    public void receiveMsgToUploadOneImageRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9045.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToUploadOneImageRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToUploadOneImageRe Error !");
            //再次发起上传一张图片
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答上传图片成功与否data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答上传图片成功与否data为空 response:" + response.toString());
                //再次上传当前图片
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 10000);
                return;
            }
            BReceiveUploadImgRe bReceiveUploadImgRe = ScriptApplication.getGson().fromJson(data, BReceiveUploadImgRe.class);
            if (bReceiveUploadImgRe != null) {
                if (!TextUtils.isEmpty(bReceiveUploadImgRe.getRetCode())) {
                    String retCode = bReceiveUploadImgRe.getRetCode();
                    if (!"0".equals(retCode)) {
                        LLog.e("服务端应答上传图片成功与否retCode=" + retCode);
                        RecordFloatView.updateMessage("服务端应答上传图片成功与否retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器端应答发送一张图片成功与否 任务不存在!");
                            RecordFloatView.updateMessage("服务器端应答发送一张图片成功与否 任务不存在!");
                            BaseOrderHelper.resetData();
                            return;
                        } else if ("2".equals(retCode)) {
                            LLog.e("发起图片上传图片名称为空!");
                            RecordFloatView.updateMessage("发起图片上传图片名称为空!");
                            //再次上传当前图片
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 10000);
                        }
                    } else {
                        if (bReceiveUploadImgRe.getTimeStamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveUploadImgRe.getTimeStamp();
                            BrushTask.getInstance().b_currentUploadNum++;
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 100);
                        }
                    }
                } else {
                    LLog.e("服务端应答上传图片成功与否retCode为空!");
                    RecordFloatView.updateMessage("服务端应答上传图片成功与否retCode为空!");
                    //再次上传当前图片
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 10000);
                }
            } else {
                LLog.e("bReceiveUploadImgRe 为空!");
                RecordFloatView.updateMessage("bReceiveUploadImgRe 为空!");
                //再次上传当前图片
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务端应答上传图片的请求时出现异常!");
            RecordFloatView.updateMessage("服务端应答上传图片的请求时出现异常!");
            //再次上传当前图片
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 10000);
        }
    }

    /**
     * 客户端给服务器发送全部截图上传成功消息
     *
     * @param msgType 消息类型 9047
     * @param object  BSendUploadAllImage
     */
    public void sendMsgToRequestAllImgUploaded(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendUploadAllImage bSendUploadAllImage = null;
        if (object instanceof BSendUploadAllImage) {
            bSendUploadAllImage = (BSendUploadAllImage) object;
        } else {
            LLog.e("instanceof BSendUploadAllImage Error !");
            RecordFloatView.updateMessage("instanceof BSendUploadAllImage Error !");
            return;
        }
        if (bSendUploadAllImage != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendUploadAllImage.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("phoneName", bSendUploadAllImage.getPhoneName());
                data.put("timeStamp", bSendUploadAllImage.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送全部截图上传成功消息-成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送全部截图上传成功消息时出现异常!");
                RecordFloatView.updateMessage("发送全部截图上传成功消息时出现异常!");
            }
        }
    }


    /**
     * 服务器给客户端发送截图全部上传成功消息的应答
     *
     * @param object
     */
    public void receiveMsgToRequestAllImgUploadedRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9047.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToRequestAllImgUploadedRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToRequestAllImgUploadedRe Error !");
            //再次发起全部截图上传成功消息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ALL_IMAGE_UPLOADED, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答上传图片成功与否data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答上传图片成功与否data为空 response:" + response.toString());
                //再次发起全部截图上传成功消息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ALL_IMAGE_UPLOADED, 10000);
                return;
            }
            BReceiveUploadAllImgRe bReceiveUploadAllImgRe = ScriptApplication.getGson().fromJson(data, BReceiveUploadAllImgRe.class);
            if (bReceiveUploadAllImgRe != null) {
                if (!TextUtils.isEmpty(bReceiveUploadAllImgRe.getRetCode())) {
                    String retCode = bReceiveUploadAllImgRe.getRetCode();
                    if (!"0".equals(retCode)) {
                        LLog.e("服务器给客户端发送截图全部上传成功消息的应答 retCode=" + retCode);
                        RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答 retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器给客户端发送截图全部上传成功消息的应答 任务不存在!");
                            RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答 任务不存在!");
                            BaseOrderHelper.resetData();
                            return;
                        } else if ("2".equals(retCode)) {
                            List<String> errorImages = bReceiveUploadAllImgRe.getErrorImages();
                            LLog.e("图片缺失: " + errorImages);
                            RecordFloatView.updateMessage("图片缺失: " + errorImages);
                            if (errorImages == null || errorImages.size() == 0) {
                                BrushTask.getInstance().b_isUploadErrorImages = true;
                                for (String name : errorImages) {
                                    if (!TextUtils.isEmpty(name)) {
                                        File file = new File(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId() + "/" + name);
                                        if (file.exists()) {
                                            BrushTask.getInstance().b_sUploadImgsList.add(file);
                                        } else {
                                            LLog.e("图片不存在: " + name);
                                            RecordFloatView.updateMessage("图片不存在: " + name);
                                        }
                                    }
                                }
                                //再次发送上传图片
                                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_ONE_IMAGE, 3000);
                            } else {
                                //再次发起全部截图上传成功消息
                                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ALL_IMAGE_UPLOADED, 10000);
                            }
                        }
                    } else {
                        if (bReceiveUploadAllImgRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_ALL_IMAGE_UPLOADED);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveUploadAllImgRe.getTimestamp();
                            LLog.e("全部图片上传成功!");
                            RecordFloatView.updateMessage("全部图片上传成功!");
                            FileHelper.deleteFolderFile(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId());
                            if (Constants.PLAY_STATE == PlayEnum.START_PLAY) {
                                SocketUtil.continueExec();
                            }
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答截图全部上传成功 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答截图全部上传成功 retCode为空!");
                    //再次发起全部截图上传成功消息
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ALL_IMAGE_UPLOADED, 10000);
                }
            } else {
                LLog.e("bReceiveUploadAllImgRe 为空!");
                RecordFloatView.updateMessage("bReceiveUploadAllImgRe 为空!");
                //再次发起全部截图上传成功消息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ALL_IMAGE_UPLOADED, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端发送截图全部上传成功消息的应答时出现异常!");
            RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答时出现异常!");
            //再次发起全部截图上传成功消息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ALL_IMAGE_UPLOADED, 10000);
        }
    }

    /**
     * 客户端给服务器发送即将重启手机消息
     *
     * @param msgType 消息类型 9049
     * @param object  BSendPhoneReboot
     */
    public void sendMsgToRebootPhone(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendPhoneReboot bSendPhoneReboot = null;
        if (object instanceof BSendPhoneReboot) {
            bSendPhoneReboot = (BSendPhoneReboot) object;
        } else {
            LLog.e("instanceof BSendPhoneReboot Error !");
            RecordFloatView.updateMessage("instanceof BSendPhoneReboot Error !");
            return;
        }
        if (bSendPhoneReboot != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendPhoneReboot.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("phoneName", bSendPhoneReboot.getPhoneName());
                data.put("timeStamp", bSendPhoneReboot.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送即将重启手机消息-成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送即将重启手机消息时出现异常!");
                RecordFloatView.updateMessage("发送即将重启手机消息时出现异常!");
            }
        }
    }


    /**
     * 服务器给客户端发送即将重启手机消息的应答
     *
     * @param object
     */
    public void receiveMsgToRebootPhoneRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9049.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToRebootPhoneRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToRebootPhoneRe Error !");
            //再次发起即将重启手机消息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_PHONE_REBOOT, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答即将重启手机消息data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答即将重启手机消息data为空 response:" + response.toString());
                //再次发起即将重启手机消息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_PHONE_REBOOT, 10000);
                return;
            }
            BReceivePhoneRebootRe bReceivePhoneRebootRe = ScriptApplication.getGson().fromJson(data, BReceivePhoneRebootRe.class);
            if (bReceivePhoneRebootRe != null) {
                if (!TextUtils.isEmpty(bReceivePhoneRebootRe.getRetCode())) {
                    String retCode = bReceivePhoneRebootRe.getRetCode();
                    if (!"0".equals(retCode)) {
                        LLog.e("服务器给客户端发送即将重启手机消息的应答 retCode=" + retCode);
                        RecordFloatView.updateMessage("服务器给客户端发送即将重启手机消息的应答 retCode=" + retCode);
                        //再次发起即将重启手机消息
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_PHONE_REBOOT, 10000);
                    } else {
                        if (bReceivePhoneRebootRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_PHONE_REBOOT);
                            BrushTask.getInstance().b_currentTimeStamp = bReceivePhoneRebootRe.getTimestamp();
                            LLog.e("收到重启消息回执成功!");
                            RecordFloatView.updateMessage("收到重启消息回执成功!");
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答即将重启手机消息 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答即将重启手机消息 retCode为空!");
                    //再次发起即将重启手机消息
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_PHONE_REBOOT, 10000);
                }
            } else {
                LLog.e("bReceivePhoneRebootRe 为空!");
                RecordFloatView.updateMessage("bReceivePhoneRebootRe 为空!");
                //再次发起即将重启手机消息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_PHONE_REBOOT, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端发送截图全部上传成功消息的应答时出现异常!");
            RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答时出现异常!");
            //再次发起即将重启手机消息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_PHONE_REBOOT, 10000);
        }
    }

    /**
     * 客户端给服务器发送工号信息
     *
     * @param msgType 消息类型 9055
     * @param object  BSendJobNumber
     */
    public void sendMsgToCommitJobNumber(String msgType, Object object) {
        BrushTask.getInstance().b_currentMsgType = msgType;
        BrushTask.getInstance().b_currentSendObject = (BBaseSessionObject) object;
        BSendJobNumber bSendJobNumber = null;
        if (object instanceof BSendJobNumber) {
            bSendJobNumber = (BSendJobNumber) object;
        } else {
            LLog.e("instanceof BSendJobNumber Error !");
            RecordFloatView.updateMessage("instanceof BSendJobNumber Error !");
            return;
        }
        if (bSendJobNumber != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendJobNumber.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("phoneName", bSendJobNumber.getPhoneName());
                data.put("orderId", bSendJobNumber.getOrderId());
                data.put("jobNumber", bSendJobNumber.getJobNumber());
                data.put("timeStamp", bSendJobNumber.getTimeStamp());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送工号信息-成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送工号信息消息时出现异常!");
                RecordFloatView.updateMessage("发送工号信息消息时出现异常!");
            }
        }
    }


    /**
     * 服务器给客户端发送工号信息的应答
     *
     * @param object
     */
    public void receiveMsgToCommitJobNumberRe(Object object) {
        BrushTask.getInstance().b_isMsgResponsed = true;
        if (B_TASK_TYPE_9055.equals(BrushTask.getInstance().b_currentMsgType)) {
            BrushTask.getInstance().b_currentMsgType = null;
            BrushTask.getInstance().b_currentSendObject = null;
        }
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgToCommitJobNumberRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgToCommitJobNumberRe Error !");
            //再次发起发送工号信息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答工号信息消息data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答工号信息消息data为空 response:" + response.toString());
                //再次发起发送工号信息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER, 10000);
                return;
            }
            BReceiveJobNumberRe bReceiveJobNumberRe = ScriptApplication.getGson().fromJson(data, BReceiveJobNumberRe.class);
            if (bReceiveJobNumberRe != null) {
                if (!TextUtils.isEmpty(bReceiveJobNumberRe.getRetCode())) {
                    String retCode = bReceiveJobNumberRe.getRetCode();
                    if (!"0".equals(retCode)) {
                        LLog.e("服务器给客户端发送工号信息消息的应答 retCode=" + retCode + " retDesc=" + bReceiveJobNumberRe.getRetDesc());
                        RecordFloatView.updateMessage("服务器给客户端发送工号信息消息的应答 retCode=" + retCode + " retDesc=" + bReceiveJobNumberRe.getRetDesc());
                        //再次发起发送工号信息
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER, 10000);
                    } else {
                        if (bReceiveJobNumberRe.getTimestamp() > BrushTask.getInstance().b_currentTimeStamp) {
                            BrushTask.getInstance().removeMessages(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER);
                            BrushTask.getInstance().b_currentTimeStamp = bReceiveJobNumberRe.getTimestamp();
                            LLog.e("收到工号信息回执成功!");
                            RecordFloatView.updateMessage("收到工号信息回执成功!");
                            if (ScriptUtil.sPromptDialog != null && ScriptUtil.sPromptDialog.isShowing()) {
                                ScriptUtil.sPromptDialog.dismiss();
                            }
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答工号信息消息 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答工号信息消息 retCode为空!");
                    //再次发起发送工号信息
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER, 10000);
                }
            } else {
                LLog.e("bReceiveJobNumberRe 为空!");
                RecordFloatView.updateMessage("bReceiveJobNumberRe 为空!");
                //再次发起发送工号信息
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端发送工号信息消息的应答时出现异常!");
            RecordFloatView.updateMessage("服务器给客户端发送工号信息消息的应答时出现异常!");
            //再次发起发送工号信息
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER, 10000);
        }
    }


    /**
     * 服务器发送一个心跳消息给客户端
     *
     * @param object
     */
    public void receiveMsgForCheckAliveRe(Object object) {
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForCheckAliveRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForCheckAliveRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务器发送一个心跳消息给客户端 data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务器发送一个心跳消息给客户端 data为空 response:" + response.toString());
                return;
            }
            BReceiveCheckAlive bReceiveCheckAlive = ScriptApplication.getGson().fromJson(data, BReceiveCheckAlive.class);
            if (bReceiveCheckAlive != null) {
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ROBOT_ALIVE_RE, 1000);
            } else {
                LLog.e("bReceiveCheckAlive 为空!");
                RecordFloatView.updateMessage("bReceiveCheckAlive 为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器发送一个心跳消息给客户端时出现异常!");
            RecordFloatView.updateMessage("服务器发送一个心跳消息给客户端时出现异常!");
        }
    }


    /**
     * 客户端应答一个心跳消息给服务器端
     *
     * @param msgType 消息类型 6002
     * @param object  SendCheckRobotStateResponse
     */
    public void sendMsgForCheckAlive(String msgType, Object object) {
        BSendCheckAliveRe bSendCheckAliveRe = null;
        if (object instanceof BSendCheckAliveRe) {
            bSendCheckAliveRe = (BSendCheckAliveRe) object;
        } else {
            LLog.e("instanceof BSendCheckAliveRe Error !");
            RecordFloatView.updateMessage("instanceof BSendCheckAliveRe Error !");
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ROBOT_ALIVE_RE, 10000);
            return;
        }
        if (bSendCheckAliveRe != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", bSendCheckAliveRe.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("phoneName", bSendCheckAliveRe.getPhoneName());
                jsonObject.put("data", data.toString());

                b_nioPackageAndSend(jsonObject, "发送应答心跳成功!", false);
            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始发送应答心跳时出现异常!");
                RecordFloatView.updateMessage("开始发送应答心跳时出现异常!");
                //再次应答一个心跳消息给服务器端
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ROBOT_ALIVE_RE, 10000);
            }
        }
    }


    /**
     * 打包并发送消息
     *
     * @param jsonObject 消息内容
     * @param successMsg 消息提示
     * @param needToast  是否需要弹提示
     */
    public void b_nioPackageAndSend(JSONObject jsonObject, String successMsg, boolean needToast) {

        //走协议
        /*if (jsonObject != null) {
            String type = jsonObject.optString("type");
            if (mBConnectFuture != null) {
                if (mBConnectFuture.isConnected() && mBConnectFuture.getSession().isConnected()) {
                    WriteFuture future = mBConnectFuture.getSession().write(jsonObject.toString());
                    future.awaitUninterruptibly();
                    if (future.isWritten()) {
                        if (!TextUtils.isEmpty(successMsg)) {
                            LLog.i(successMsg);
                            if (needToast) {
                                RecordFloatView.updateMessage(successMsg);
                            }
                        }
                        if (!B_TASK_TYPE_9001.equals(type) && !B_TASK_TYPE_1004.equals(type)) {
                            BrushTask.b_isMsgResponsed = false;
                        }
                    } else {
                        LLog.e("优化 - 发送消息失败!");
                        RecordFloatView.updateMessage("优化 - 发送消息失败!");
                        if (!B_TASK_TYPE_9001.equals(type) && !B_TASK_TYPE_1004.equals(type)) {
                            BrushTask.b_isMsgResponsed = false;
                        }
                    }
                } else {
                    LLog.e("优化 - 未成功连接远程会话,发送失败!");
                    RecordFloatView.updateMessage("优化 - 未成功连接远程会话,发送失败!");
                    if (!B_TASK_TYPE_9001.equals(type) && !B_TASK_TYPE_1004.equals(type)) {
                        BrushTask.b_isMsgResponsed = false;
                    }
                }
            } else {
                BrushTask.sendMessage(BrushTask.B_MSG_TYPE_MINA_RESET, 50BrushTask.getInstance().sendMessage      }*/

        //走伪协议(HTTP请求)
        try {
            if (jsonObject != null) {
                String type = jsonObject.optString("type");
                if (!TextUtils.isEmpty(successMsg)) {
                    LLog.i(successMsg);
                    if (needToast) {
                        RecordFloatView.updateMessage(successMsg);
                    }
                }
                BrushOrderHelper.getInstance().sendHttpMessage(type, jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

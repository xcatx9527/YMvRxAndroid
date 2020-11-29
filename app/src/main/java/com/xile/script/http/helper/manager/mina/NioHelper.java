package com.xile.script.http.helper.manager.mina;

import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.manager.bean.*;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.common.*;
import com.xile.script.utils.game.InsertAreaRoleUtil;
import com.xile.script.utils.script.ScriptUtil;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * date: 2017/6/30 21:26
 *
 * @scene Nio工具类
 */

public class NioHelper {
    //协议
    public static final String TASK_TYPE_8001 = "8001";
    public static final String TASK_TYPE_8002 = "8002";
    public static final String TASK_TYPE_8003 = "8003";
    public static final String TASK_TYPE_8004 = "8004";
    public static final String TASK_TYPE_8005 = "8005";
    public static final String TASK_TYPE_8006 = "8006";
    public static final String TASK_TYPE_8007 = "8007";
    public static final String TASK_TYPE_8008 = "8008";
    public static final String TASK_TYPE_8009 = "8009";
    public static final String TASK_TYPE_8010 = "8010";
    public static final String TASK_TYPE_8011 = "8011";
    public static final String TASK_TYPE_8012 = "8012";
    public static final String TASK_TYPE_8013 = "8013";
    public static final String TASK_TYPE_8014 = "8014";
    public static final String TASK_TYPE_6001 = "6001";
    public static final String TASK_TYPE_6002 = "6002";
    public static final String TASK_TYPE_6003 = "6003";
    public static final String TASK_TYPE_6004 = "6004";
    public static final String TASK_TYPE_1001 = "1001";
    public static final String TASK_TYPE_1002 = "1002";

    public static final String GET_TASK = "GetTask";
    public static final String GET_TASK_RE = "GetTask_Re";
    public static final String SEND_IMAGE_LIST = "SendImageList";
    public static final String SEND_IMAGE_LIST_RE = "SendImageList_Re";
    public static final String SEND_IMAGE = "SendImage";
    public static final String SEND_IMAGE_RE = "SendImage_Re";
    public static final String SEND_ERROR = "SendError";
    public static final String SEND_ERROR_RE = "SendError_Re";
    public static final String SUCC_IMAGE_LIST = "succImageList";
    public static final String SUCC_IMAGE_LIST_RE = "succImageList_Re";
    public static final String SEND_IMAGE_OCR_SINGLE = "OCRImageType";
    public static final String SEND_IMAGE_OCR_SINGLE_RE = "OCRImageType_Re";
    public static final String SEND_WAITING = "SendWaiting";
    public static final String SEND_WAITING_RE = "SendWaiting_Re";
    public static final String SEND_REQUEST_RESULT = "AskOCR";
    public static final String SEND_REQUEST_RESULT_RE = "AskOCR_Re";
    public static final String SEND_CLICK_CODE = "AfterClickSendAuthCode";
    public static final String SEND_CLICK_CODE_RE = "AfterClickSendAuthCode_Re";
    public static final String NEW_KEEP_ALIVE = "NewKeepAlive";
    public static final String NEW_KEEP_ALIVE_RE = "NewKeepAlive_Re";

    //public final String nioServerIp = "10.4.72.152";  //IP
    public final String nioServerIp = "221.228.210.227";  //IP
    public final int nioServerPort = 8099;  //端口
    public static final String SYSTEM_TYPE = "android";
    private static NioHelper sInstance;
    public IMSocketClient imSocketClient;
    public ConnectFuture connectFuture;

    private static ConcurrentHashMap nioGetMsgMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap nioSendMsgMap = new ConcurrentHashMap<>();

    static {
        nioSendMsgMap.put(TASK_TYPE_8001, "sendMsgForGetTask");
        nioSendMsgMap.put(TASK_TYPE_8003, "sendMsgForRequestUploadImg");
        nioSendMsgMap.put(TASK_TYPE_8005, "sendMsgForUploadImg");
        nioSendMsgMap.put(TASK_TYPE_8007, "sendMsgForCaptureResult");
        nioSendMsgMap.put(TASK_TYPE_8009, "sendMsgForCaptureState");
        nioSendMsgMap.put(TASK_TYPE_8011, "sendMsgForUploadOcrImg");
        nioSendMsgMap.put(TASK_TYPE_8013, "sendMsgForCodeClick");
        nioSendMsgMap.put(TASK_TYPE_6002, "sendMsgForCheckRobotState");
        nioSendMsgMap.put(TASK_TYPE_6003, "sendMsgForRequestOcrResult");
        nioSendMsgMap.put(TASK_TYPE_1002, "sendMsgForCheckAlive");

        nioGetMsgMap.put(TASK_TYPE_8002, "receiveMsgForGetTaskRe");
        nioGetMsgMap.put(TASK_TYPE_8004, "receiveMsgForRequestUploadImgRe");
        nioGetMsgMap.put(TASK_TYPE_8006, "receiveMsgForUploadImgRe");
        nioGetMsgMap.put(TASK_TYPE_8008, "receiveMsgForCaptureResultRe");
        nioGetMsgMap.put(TASK_TYPE_8010, "receiveMsgForCaptureStateRe");
        nioGetMsgMap.put(TASK_TYPE_8012, "receiveMsgForUploadOcrImgRe");
        nioGetMsgMap.put(TASK_TYPE_8014, "receiveMsgForCodeClickRe");
        nioGetMsgMap.put(TASK_TYPE_6001, "receiveMsgForCheckRobotStateRe");
        nioGetMsgMap.put(TASK_TYPE_6004, "receiveMsgForRequestOcrResultRe");
        nioGetMsgMap.put(TASK_TYPE_1001, "receiveMsgForCheckAliveRe");
    }


    private NioHelper() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static NioHelper getInstance() {
        if (sInstance == null) {
            synchronized (NioHelper.class) {
                if (sInstance == null) {
                    sInstance = new NioHelper();
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
                LLog.e("初始化Mina");
                if (imSocketClient == null) {
                    imSocketClient = new IMSocketClient(nioServerIp, nioServerPort);
                    LLog.e("重新初始化Mina imSocketClient为空！");
                }
                if (imSocketClient != null) {
                    if (connectFuture == null) {
                        LLog.e("重新初始化Mina connectFuture为空！");
                        connectFuture = imSocketClient.socketConnect();
                    }
                    if (connectFuture != null) {
                        if (!connectFuture.isConnected() || !connectFuture.getSession().isConnected()) {
                            LLog.e("Mina未连接成功,将继续重连!");
                            connectFuture = imSocketClient.socketConnect();
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_MINA_RESET, 10000);
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
                String methodName = (String) nioSendMsgMap.get(msgType);
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
                String methodName = (String) nioGetMsgMap.get(msgType);
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
     * 客户端请求一个截图任务
     *
     * @param msgType 消息类型 8001
     * @param object  RequestGetTask
     */
    public void sendMsgForGetTask(String msgType, Object object) {
        RequestGetTask getTaskBean = null;
        if (object instanceof RequestGetTask) {
            getTaskBean = (RequestGetTask) object;
        } else {
            LLog.e("instanceof RequestGetTask Error !");
            RecordFloatView.updateMessage("instanceof RequestGetTask Error !");
            return;
        }
        if (getTaskBean != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", getTaskBean.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("systemType", DESUtil.encrypt(getTaskBean.getSystemType()));
                data.put("robotName", DESUtil.encrypt(getTaskBean.getRobotName()));
                data.put("timeStamp", DESUtil.encrypt(getTaskBean.getTimeStamp()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "发送请求订单成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送请求订单异常!");
                RecordFloatView.updateMessage("发送请求订单异常!");
            }
        }
    }


    /**
     * 服务器应答一个截图任务
     * -1：服务出错
     * 0：获取订单成功
     * 1：taskType为空
     * 2：taskData不能为空
     * 3：taskType类型不匹配
     * 4：该机器人无法处理该游戏截图任务
     * 5：该截图任务还没到执行时间
     * 6：获取卖家老虎账号失败
     * 7：当前所有GM账号都被占用
     * 8：绑定老虎账号失败
     * 21,服务器出错，订单正被其它机器处理
     * 22.当前没有订单
     * 23.robotName为空
     *
     * @param object
     */
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
            LLog.d("获取到订单..." + data);
            GamesOrderInfo gamesOrderInfo = ScriptApplication.getGson().fromJson(data, GamesOrderInfo.class);
            if (gamesOrderInfo != null) {
                LLog.d("加密的gamesOrderInfo:" + gamesOrderInfo.toString());
                if (!TextUtils.isEmpty(gamesOrderInfo.getRetcode())) {
                    String retCode = DESUtil.decrypt(gamesOrderInfo.getRetcode());
                    if (!"0".equals(retCode)) {
                        String failCode = GamesOrderHelper.getInstance().dealWithFailCode(retCode);
                        LLog.e("获取订单失败:" + failCode);
                        RecordFloatView.updateMessage("获取订单失败:" + failCode);
                        return;
                    }
                    LLog.e("获取到订单!");
                    RecordFloatView.updateMessage("获取到订单!");
                    gamesOrderInfo.setRetcode(DESUtil.decrypt(gamesOrderInfo.getRetcode()));
                    gamesOrderInfo.setAid(DESUtil.decrypt(gamesOrderInfo.getAid()));
                    gamesOrderInfo.setGameName(DESUtil.decrypt(gamesOrderInfo.getGameName()));
                    gamesOrderInfo.setQName(DESUtil.decrypt(gamesOrderInfo.getQName()));
                    gamesOrderInfo.setZoneName(DESUtil.decrypt(gamesOrderInfo.getZoneName()));
                    gamesOrderInfo.setZoneNum(DESUtil.decrypt(gamesOrderInfo.getZoneNum()));
                    gamesOrderInfo.setTaskType(DESUtil.decrypt(gamesOrderInfo.getTaskType()));
                    gamesOrderInfo.setTaskData(DESUtil.decrypt(gamesOrderInfo.getTaskData()));
                    gamesOrderInfo.setRoleLevel(DESUtil.decrypt(gamesOrderInfo.getRoleLevel()));
                    gamesOrderInfo.setGmUserName(DESUtil.decrypt(gamesOrderInfo.getGmUserName()));
                    gamesOrderInfo.setGmPassword(DESUtil.decrypt(gamesOrderInfo.getGmPassword()));
                    gamesOrderInfo.setChannelId(DESUtil.decrypt(gamesOrderInfo.getChannelId()));
                    gamesOrderInfo.setTimeStamp(DESUtil.decrypt(gamesOrderInfo.getTimeStamp()));
                    LLog.d("解密后的gamesOrderInfo:" + gamesOrderInfo.toString());

                    if (SpUtil.getKeyBoolean(PlatformConfig.ISFREE, true)) {//处于空闲状态
                        String timeStamp = gamesOrderInfo.getTimeStamp();
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_DEAL_WITH_ORDER, gamesOrderInfo, 1000);
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    } else {
                        LLog.e("当前为非空闲状态！");
                        RecordFloatView.updateMessage("当前为非空闲状态!");
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
     * 客户端发送一个准备上传图片的请求
     *
     * @param msgType 消息类型 8003
     * @param object  BRequestUploadImg
     */

    public void sendMsgForRequestUploadImg(String msgType, Object object) {
        NioTask.getInstance().currentMsgType = msgType;
        NioTask.getInstance().currentSendObject = (BaseSessionObject) object;
        RequestUploadImg requestUploadImg = null;
        if (object instanceof RequestUploadImg) {
            requestUploadImg = (RequestUploadImg) object;
        } else {
            LLog.e("instanceof RequestUploadImg Error !");
            RecordFloatView.updateMessage("instanceof RequestUploadImg Error !");
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 10000);
            return;
        }
        if (requestUploadImg != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", requestUploadImg.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("systemType", DESUtil.encrypt(requestUploadImg.getSystemType()));
                data.put("taskType", DESUtil.encrypt(requestUploadImg.getTaskType()));
                data.put("taskData", DESUtil.encrypt(requestUploadImg.getTaskData()));
                data.put("robotName", DESUtil.encrypt(requestUploadImg.getRobotName()));
                data.put("imageNames", DESUtil.encrypt(requestUploadImg.getImageNames()));
                data.put("timeStamp", DESUtil.encrypt(requestUploadImg.getTimeStamp()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "发送请求上传图片成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送请求上传图片异常!");
                RecordFloatView.updateMessage("发送请求上传图片异常!");
                //再次发起上传图片的请求
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 10000);
            }
        }
    }


    /**
     * 服务端应答一个准备上传图片的请求
     *
     * @param object
     */
    public void receiveMsgForRequestUploadImgRe(Object object) {
        NioTask.getInstance().isMsgResponsed = true;
        NioTask.getInstance().currentMsgType = null;
        NioTask.getInstance().currentSendObject = null;
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForRequestUploadImgRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForRequestUploadImgRe Error !");
            //再次发起上传图片的请求
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答上传图片的请求data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答上传图片的请求data为空 response:" + response.toString());
                //再次发起上传图片的请求
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 10000);
                return;
            }
            RequestUploadImgResponse requestUploadImgResponse = ScriptApplication.getGson().fromJson(data, RequestUploadImgResponse.class);
            if (requestUploadImgResponse != null) {
                if (!TextUtils.isEmpty(requestUploadImgResponse.getRetcode())) {
                    String retCode = DESUtil.decrypt(requestUploadImgResponse.getRetcode());
                    if (!"0".equals(retCode)) {
                        LLog.e("服务端应答上传图片的请求retCode=" + retCode);
                        RecordFloatView.updateMessage("服务端应答上传图片的请求retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务端应答一个准备上传图片的请求 任务不存在!");
                            RecordFloatView.updateMessage("服务端应答一个准备上传图片的请求 任务不存在!");
                            BaseOrderHelper.resetData();
                        } else if ("2".equals(retCode)) {
                            LLog.e("发起图片上传请求imageNames为空!");
                            RecordFloatView.updateMessage("发起图片上传请求imageNames为空!");
                            //再次发起上传图片的请求
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 10000);
                        }
                    } else {
                        //上传图片
                        String timeStamp = DESUtil.decrypt(requestUploadImgResponse.getTimeStamp());
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 1000);
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    }
                }
            } else {
                LLog.e("requestUploadImgResponse 为空!");
                RecordFloatView.updateMessage("requestUploadImgResponse 为空!");
                //再次发起上传图片的请求
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务端应答上传图片的请求时出现异常!");
            RecordFloatView.updateMessage("服务端应答上传图片的请求时出现异常!");
            //再次发起上传图片的请求
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 10000);
        }
    }

    /**
     * 客户端发送一张图片
     *
     * @param msgType 消息类型 8005
     * @param object  UploadImg
     */
    public void sendMsgForUploadImg(String msgType, Object object) {
        NioTask.getInstance().currentMsgType = msgType;
        NioTask.getInstance().currentSendObject = (BaseSessionObject) object;
        UploadImg uploadImg = null;
        if (object instanceof UploadImg) {
            uploadImg = (UploadImg) object;
        } else {
            LLog.e("instanceof UploadImg Error !");
            RecordFloatView.updateMessage("instanceof UploadImg Error !");
            //再次发起上传图片
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
            return;
        }
        if (uploadImg != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", uploadImg.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("systemType", DESUtil.encrypt(uploadImg.getSystemType()));
                data.put("taskType", DESUtil.encrypt(uploadImg.getTaskType()));
                data.put("taskData", DESUtil.encrypt(uploadImg.getTaskData()));
                data.put("robotName", DESUtil.encrypt(uploadImg.getRobotName()));
                data.put("imageName", DESUtil.encrypt(uploadImg.getImageName()));
                data.put("imageData", uploadImg.getImageData());
                data.put("timeStamp", DESUtil.encrypt(uploadImg.getTimeStamp()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "上传图片成功! 图片名称:" + uploadImg.getImageName(), false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始上传图片异常!");
                RecordFloatView.updateMessage("开始上传图片异常!");
                //再次发起上传图片
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
            }
        }
    }


    /**
     * 服务器端应答发送一张图片成功与否
     *
     * @param object
     */
    public void receiveMsgForUploadImgRe(Object object) {
        NioTask.getInstance().isMsgResponsed = true;
        NioTask.getInstance().currentMsgType = null;
        NioTask.getInstance().currentSendObject = null;
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForUploadImgRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForUploadImgRe Error !");
            //再次发起上传图片
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答上传图片成功与否data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答上传图片成功与否data为空 response:" + response.toString());
                //再次上传当前图片
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
                return;
            }
            UploadImgResponse uploadImgResponse = ScriptApplication.getGson().fromJson(data, UploadImgResponse.class);
            if (uploadImgResponse != null) {
                if (!TextUtils.isEmpty(uploadImgResponse.getRetcode())) {
                    String retCode = DESUtil.decrypt(uploadImgResponse.getRetcode());
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
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
                        } else if ("-1".equals(retCode)) {
                            LLog.e("发起上传带类型图片 图片内容为空!");
                            RecordFloatView.updateMessage("发起上传带类型图片 图片内容为空!");
                            //再次上传当前图片
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
                        } else if ("-2".equals(retCode)) {
                            LLog.e("发起上传带类型图片 服务器写文件异常!");
                            RecordFloatView.updateMessage("发起上传带类型图片 服务器写文件异常!");
                            //再次上传当前图片
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
                        }
                    } else {
                        String timeStamp = DESUtil.decrypt(uploadImgResponse.getTimeStamp());
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    NioTask.getInstance().currentUploadNum++;
                                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 100);
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    }
                } else {
                    LLog.e("服务端应答上传图片成功与否retCode为空!");
                    RecordFloatView.updateMessage("服务端应答上传图片成功与否retCode为空!");
                    //再次上传当前图片
                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
                }
            } else {
                LLog.e("uploadImgResponse 为空!");
                RecordFloatView.updateMessage("uploadImgResponse 为空!");
                //再次上传当前图片
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务端应答上传图片的请求时出现异常!");
            RecordFloatView.updateMessage("服务端应答上传图片的请求时出现异常!");
            //再次上传当前图片
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 10000);
        }
    }


    /**
     * 客户端发送一张带类型的图片(区服识别/角色识别等)
     *
     * @param msgType 消息类型 8011
     * @param object  UploadOcrImg
     */
    public void sendMsgForUploadOcrImg(String msgType, Object object) {
        NioTask.getInstance().currentMsgType = msgType;
        NioTask.getInstance().currentSendObject = (BaseSessionObject) object;
        UploadOcrImg uploadOcrImg = null;
        if (object instanceof UploadOcrImg) {
            uploadOcrImg = (UploadOcrImg) object;
        } else {
            LLog.e("instanceof UploadOcrImg Error !");
            RecordFloatView.updateMessage("instanceof UploadOcrImg Error !");
            //再次发起上传图片
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
            return;
        }
        if (uploadOcrImg != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", uploadOcrImg.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("systemType", DESUtil.encrypt(uploadOcrImg.getSystemType()));
                data.put("taskType", DESUtil.encrypt(uploadOcrImg.getTaskType()));
                data.put("taskData", DESUtil.encrypt(uploadOcrImg.getTaskData()));
                data.put("robotName", DESUtil.encrypt(uploadOcrImg.getRobotName()));
                data.put("imageName", DESUtil.encrypt(uploadOcrImg.getImageName()));
                data.put("imgType", DESUtil.encrypt(uploadOcrImg.getImgType()));
                data.put("imageData", uploadOcrImg.getImageData());
                data.put("timeStamp", DESUtil.encrypt(uploadOcrImg.getTimeStamp()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "上传图片成功! 图片名称:" + uploadOcrImg.getImageName(), false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始上传单张图片异常!");
                RecordFloatView.updateMessage("开始上传单张图片异常!");
                //再次发起上传图片
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
            }
        }
    }


    /**
     * 服务器端应答发送一张带类型图片成功与否(区服识别/角色识别等)
     *
     * @param object
     */
    public void receiveMsgForUploadOcrImgRe(Object object) {
        NioTask.getInstance().isMsgResponsed = true;
        NioTask.getInstance().currentMsgType = null;
        NioTask.getInstance().currentSendObject = null;
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForUploadOcrImgRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForUploadOcrImgRe Error !");
            //再次发起上传图片
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务端应答上传带类型图片成功与否data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务端应答上传带类型图片成功与否data为空 response:" + response.toString());
                //再次上传当前图片
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
                return;
            }
            UploadOcrImgResponse uploadOcrImgResponse = ScriptApplication.getGson().fromJson(data, UploadOcrImgResponse.class);
            if (uploadOcrImgResponse != null) {
                if (!TextUtils.isEmpty(uploadOcrImgResponse.getRetcode())) {
                    String retCode = DESUtil.decrypt(uploadOcrImgResponse.getRetcode());
                    if (!"0".equals(retCode)) {
                        LLog.e("服务端应答上传带类型图片成功与否retCode=" + retCode);
                        RecordFloatView.updateMessage("服务端应答上传带类型图片成功与否retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器端应答发送一张带类型图片成功与否 任务不存在!");
                            RecordFloatView.updateMessage("服务器端应答发送一张带类型图片成功与否 任务不存在!");
                            BaseOrderHelper.resetData();
                        } else if ("2".equals(retCode)) {
                            LLog.e("发起图片上传带类型图片名称为空!");
                            RecordFloatView.updateMessage("发起图片上传带类型图片名称为空!");
                            //再次上传当前图片
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
                        } else if ("-1".equals(retCode)) {
                            LLog.e("发起上传带类型图片 图片内容为空!");
                            RecordFloatView.updateMessage("发起上传带类型图片 图片内容为空!");
                            //再次上传当前图片
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
                        } else if ("-2".equals(retCode)) {
                            LLog.e("发起上传带类型图片 服务器写文件异常!");
                            RecordFloatView.updateMessage("发起上传带类型图片 服务器写文件异常!");
                            //再次上传当前图片
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
                        }
                    } else {
                        String timeStamp = DESUtil.decrypt(uploadOcrImgResponse.getTimeStamp());
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 1000);
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    }
                } else {
                    LLog.e("服务端应答上传带类型图片成功与否retCode为空!");
                    RecordFloatView.updateMessage("服务端应答上传带类型图片成功与否retCode为空!");
                    //再次上传当前图片
                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
                }
            } else {
                LLog.e("uploadOcrImgResponse 为空!");
                RecordFloatView.updateMessage("uploadOcrImgResponse 为空!");
                //再次上传当前图片
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务端应答上传带类型图片的请求时出现异常!");
            RecordFloatView.updateMessage("服务端应答上传带类型图片的请求时出现异常!");
            //再次上传当前图片
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, 10000);
        }
    }

    /**
     * 客户端点击获取验证码
     *
     * @param msgType 消息类型 8013
     * @param object  SendClickCode
     */
    public void sendMsgForCodeClick(String msgType, Object object) {
        NioTask.getInstance().currentMsgType = msgType;
        NioTask.getInstance().currentSendObject = (BaseSessionObject) object;
        SendClickCode sendClickCode = null;
        if (object instanceof SendClickCode) {
            sendClickCode = (SendClickCode) object;
        } else {
            LLog.e("instanceof SendClickCode Error !");
            RecordFloatView.updateMessage("instanceof SendClickCode Error !");
            //再次发起点击获取验证码
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
            return;
        }
        if (sendClickCode != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", sendClickCode.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("systemType", DESUtil.encrypt(sendClickCode.getSystemType()));
                data.put("taskType", DESUtil.encrypt(sendClickCode.getTaskType()));
                data.put("taskData", DESUtil.encrypt(sendClickCode.getTaskData()));
                data.put("robotName", DESUtil.encrypt(sendClickCode.getRobotName()));
                data.put("timeStamp", DESUtil.encrypt(sendClickCode.getTimeStamp()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "点击获取验证码成功!", false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("点击获取验证码异常!");
                RecordFloatView.updateMessage("点击获取验证码异常!");
                //再次发起点击获取验证码
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
            }
        }
    }


    /**
     * 服务器应答客户端点击获取验证码
     *
     * @param object
     */
    public void receiveMsgForCodeClickRe(Object object) {
        NioTask.getInstance().isMsgResponsed = true;
        NioTask.getInstance().currentMsgType = null;
        NioTask.getInstance().currentSendObject = null;
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForCodeClickRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForCodeClickRe Error !");
            //再次发起点击获取验证码
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务器应答客户端点击获取验证码成功与否data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务器应答客户端点击获取验证码成功与否data为空 response:" + response.toString());
                //再次发起点击获取验证码
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
                return;
            }
            SendClickCodeResponse sendClickCodeResponse = ScriptApplication.getGson().fromJson(data, SendClickCodeResponse.class);
            if (sendClickCodeResponse != null) {
                if (!TextUtils.isEmpty(sendClickCodeResponse.getRetcode())) {
                    String retCode = DESUtil.decrypt(sendClickCodeResponse.getRetcode());
                    if (!"0".equals(retCode)) {
                        LLog.e("服务器应答客户端点击获取验证码成功与否retCode=" + retCode);
                        RecordFloatView.updateMessage("服务器应答客户端点击获取验证码成功与否retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器应答客户端点击获取验证码成功与否 任务不存在!");
                            RecordFloatView.updateMessage("服务器应答客户端点击获取验证码成功与否 任务不存在!");
                            BaseOrderHelper.resetData();
                        } else {
                            //再次发起点击获取验证码
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
                        }
                    } else {
                        String timeStamp = DESUtil.decrypt(sendClickCodeResponse.getTimeStamp());
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    InsertAreaRoleUtil.getInstance().dealWithOcrInit(InsertAreaRoleUtil.getInstance().REQUEST_CODE);
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    }
                } else {
                    LLog.e("服务器应答客户端点击获取验证码成功与否retCode为空!");
                    RecordFloatView.updateMessage("服务器应答客户端点击获取验证码成功与否retCode为空!");
                    //再次发起点击获取验证码
                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
                }
            } else {
                LLog.e("sendClickCodeResponse 为空!");
                RecordFloatView.updateMessage("sendClickCodeResponse 为空!");
                //再次发起点击获取验证码
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器应答客户端点击获取验证码结果的请求时出现异常!");
            RecordFloatView.updateMessage("服务器应答客户端点击获取验证码结果的请求时出现异常!");
            //再次发起点击获取验证码
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, 10000);
        }
    }


    /**
     * 客户端发送请求服务器结果(区服识别结果/角色识别结果/手机验证码获取)
     *
     * @param msgType 消息类型 6003
     * @param object  RequestGetOcrResult
     */
    public void sendMsgForRequestOcrResult(String msgType, Object object) {
        NioTask.getInstance().currentMsgType = msgType;
        NioTask.getInstance().currentSendObject = (BaseSessionObject) object;
        RequestGetOcrResult requestGetOcrResult = null;
        if (object instanceof RequestGetOcrResult) {
            requestGetOcrResult = (RequestGetOcrResult) object;
        } else {
            LLog.e("instanceof RequestGetOcrResult Error !");
            RecordFloatView.updateMessage("instanceof RequestGetOcrResult Error !");
            //再次发起请求服务器结果
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            return;
        }
        if (requestGetOcrResult != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", requestGetOcrResult.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("systemType", DESUtil.encrypt(requestGetOcrResult.getSystemType()));
                data.put("taskType", DESUtil.encrypt(requestGetOcrResult.getTaskType()));
                data.put("taskData", DESUtil.encrypt(requestGetOcrResult.getTaskData()));
                data.put("robotName", DESUtil.encrypt(requestGetOcrResult.getRobotName()));
                data.put("askType", DESUtil.encrypt(requestGetOcrResult.getAskType()));
                data.put("timeStamp", DESUtil.encrypt(requestGetOcrResult.getTimeStamp()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "发送请求服务器OCR结果成功!", false);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("发送请求服务器OCR结果异常!");
                RecordFloatView.updateMessage("发送请求服务器OCR结果异常!");
                //再次发起请求服务器结果
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            }
        }
    }


    /**
     * 服务器应答某个任务的结果(区服识别/角色识别/短信获取等)
     *
     * @param object
     */
    public void receiveMsgForRequestOcrResultRe(Object object) {
        NioTask.getInstance().isMsgResponsed = true;
        NioTask.getInstance().currentMsgType = null;
        NioTask.getInstance().currentSendObject = null;
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForRequestOcrResultRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForRequestOcrResultRe Error !");
            //再次发起请求服务器结果
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务器应答OCR结果成功与否data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务器应答OCR结果成功与否data为空 response:" + response.toString());
                //再次发起请求服务器结果
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                return;
            }
            RequestGetOcrResultResponse requestGetOcrResultResponse = ScriptApplication.getGson().fromJson(data, RequestGetOcrResultResponse.class);
            if (requestGetOcrResultResponse != null) {
                if (!TextUtils.isEmpty(requestGetOcrResultResponse.getRetcode())) {
                    String retCode = DESUtil.decrypt(requestGetOcrResultResponse.getRetcode());
                    if (!"0".equals(retCode)) {
                        LLog.e("服务器应答OCR结果成功与否retCode=" + retCode);
                        RecordFloatView.updateMessage("服务器应答OCR结果成功与否retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器应答OCR结果成功与否 任务不存在!");
                            RecordFloatView.updateMessage("服务器应答OCR结果成功与否 任务不存在!");
                            BaseOrderHelper.resetData();
                        } else if ("2".equals(retCode)) {
                            LLog.e("服务器应答暂无结果!");
                            RecordFloatView.updateMessage("服务器应答暂无结果!");
                            //再次发起请求服务器结果
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                        } else if ("3".equals(retCode)) {
                            LLog.e("服务器应答OCR结果失败!");
                            RecordFloatView.updateMessage("服务器应答OCR结果失败!");
                            String askType = DESUtil.decrypt(requestGetOcrResultResponse.getAskType());
                            if (TextUtils.isEmpty(askType)) {
                                LLog.e("服务器应答OCR结果失败时 askType 为空！");
                                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                                return;
                            }
                            if (InsertAreaRoleUtil.getInstance().UPLOAD_LARGE_AREA.equals(askType) || InsertAreaRoleUtil.getInstance().UPLOAD_SMALL_AREA.equals(askType)) {
                                BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_REDO_ORC_SERVERID_ERROR, "区服识别失败_NULL");
                            } else if (InsertAreaRoleUtil.getInstance().UPLOAD_ROLE.equals(askType)) {
                                BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_REDO_ORC_ROLELEVEL_ERROR, "角色识别失败_NULL");
                            } else if (InsertAreaRoleUtil.getInstance().REQUEST_CODE.equals(askType)) {
                                BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_DELAY_REDO_AUTHCODE_ERROR, "短信验证码获取失败_NULL");
                            }
                        }
                    } else {
                        String timeStamp = DESUtil.decrypt(requestGetOcrResultResponse.getTimeStamp());
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    String askType = DESUtil.decrypt(requestGetOcrResultResponse.getAskType());
                                    if (TextUtils.isEmpty(askType)) {
                                        LLog.e("服务器应答OCR结果成功时 askType 为空！");
                                        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                                        return;
                                    }
                                    String ocrResult = DESUtil.decrypt(requestGetOcrResultResponse.getOcrResult());
                                    LLog.i("ocrResult:" + ocrResult);
                                    if (TextUtils.isEmpty(ocrResult)) {
                                        LLog.e("服务器应答OCR结果成功时 ocrResult 为空！");
                                        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                                        return;
                                    }
                                    ScriptApplication.getService().execute(() -> {
                                        InsertAreaRoleUtil.getInstance().dealWithAOcr(ocrResult, askType);
                                    });
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    }
                } else {
                    LLog.e("服务器应答OCR结果成功与否retCode为空!");
                    RecordFloatView.updateMessage("服务器应答OCR结果成功与否retCode为空!");
                    //再次发起请求服务器结果
                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                }
            } else {
                LLog.e("requestGetOcrResultResponse 为空!");
                RecordFloatView.updateMessage("requestGetOcrResultResponse 为空!");
                //再次发起请求服务器结果
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器应答OCR结果的请求时出现异常!");
            RecordFloatView.updateMessage("服务器应答OCR结果的请求时出现异常!");
            //再次发起请求服务器结果
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
        }
    }


    /**
     * 客户端发送警报
     *
     * @param msgType 消息类型 8007
     * @param object  OrderHandleResult
     */
    public void sendMsgForCaptureResult(String msgType, Object object) {
        NioTask.getInstance().currentMsgType = msgType;
        NioTask.getInstance().currentSendObject = (BaseSessionObject) object;
        OrderHandleResult orderHandleResult = null;
        if (object instanceof OrderHandleResult) {
            orderHandleResult = (OrderHandleResult) object;
        } else {
            LLog.e("instanceof OrderHandleResult Error !");
            RecordFloatView.updateMessage("instanceof OrderHandleResult Error !");
            //再次发起警报报告
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 10000);
            return;
        }
        if (orderHandleResult != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", orderHandleResult.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("systemType", DESUtil.encrypt(orderHandleResult.getSystemType()));
                data.put("taskType", DESUtil.encrypt(orderHandleResult.getTaskType()));
                data.put("taskData", DESUtil.encrypt(orderHandleResult.getTaskData()));
                data.put("robotName", DESUtil.encrypt(orderHandleResult.getRobotName()));
                data.put("errorType", DESUtil.encrypt(orderHandleResult.getErrorType()));
                data.put("errorDesc", DESUtil.encrypt(orderHandleResult.getErrorDesc()));
                data.put("timeStamp", DESUtil.encrypt(orderHandleResult.getTimeStamp()));
                if (!TextUtils.isEmpty(orderHandleResult.getImageName())) {
                    data.put("imageName", DESUtil.encrypt(orderHandleResult.getImageName()));
                }
                if (!TextUtils.isEmpty(orderHandleResult.getImageData())) {
                    data.put("imageData", orderHandleResult.getImageData());
                }
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "发送警报成功!", true);
            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("客户端开始发送警报报告时出现异常!");
                RecordFloatView.updateMessage("客户端开始发送警报报告时出现异常!");
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 10000);
            }
        }
    }


    /**
     * 服务器端应答警报报告
     *
     * @param object
     */
    public void receiveMsgForCaptureResultRe(Object object) {
        NioTask.getInstance().isMsgResponsed = true;
        NioTask.getInstance().currentMsgType = null;
        NioTask.getInstance().currentSendObject = null;
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForCaptureResultRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForCaptureResultRe Error !");
            //再次发起警报报告
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务器端应答警报报告data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务器端应答警报报告data为空 response:" + response.toString());
                //再次发起警报报告
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 10000);
                return;
            }
            OrderHandleResultResponse orderHandleResultResponse = ScriptApplication.getGson().fromJson(data, OrderHandleResultResponse.class);
            if (orderHandleResultResponse != null) {
                if (!TextUtils.isEmpty(orderHandleResultResponse.getRetcode())) {
                    String retCode = DESUtil.decrypt(orderHandleResultResponse.getRetcode());
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
                            //再次发送警报
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 10000);
                        }
                    } else {
                        String timeStamp = DESUtil.decrypt(orderHandleResultResponse.getTimeStamp());
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    String alertType = DESUtil.decrypt(orderHandleResultResponse.getErrorType());
                                    LLog.e("报警成功:" + alertType);
                                    RecordFloatView.updateMessage("报警成功:" + alertType);
                                    SerializableUtil.toSaveLog(Constants.GAMES_ORDER_LOG_PATH, "订单号:" + Constants.sGamesOrderInfo.getTaskData() + "  警报类型:" + SpUtil.getKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR));
                                    if (AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR.equals(alertType) || AlertConfig.ALERT_AND_REDO_DOWNLOAD_ERROR.equals(alertType)
                                            || AlertConfig.ALERT_AND_REDO_KEFU_ERROR.equals(alertType) || AlertConfig.ALERT_AND_REDO_ORC_SERVERID_ERROR.equals(alertType)) {
                                        LLog.e("即将重新执行本地订单!");
                                        RecordFloatView.updateMessage("即将重新执行本地订单!");
                                        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_DEAL_WITH_ORDER, Constants.sGamesOrderInfo, 60 * 1000);
                                        return;
                                    }
                                    if (!AlertConfig.ALERT_AND_PAUSE_KEFU_DO.equals(alertType)) {
                                        FileUtil.saveFile("", Constants.MANAGER_ORDER_FOLDER_TEMP_PATH);
                                        Constants.sGamesOrderInfo = null;
                                        BaseOrderHelper.resetData();//重置状态值
                                    }
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    }
                    ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
                }
            } else {
                LLog.e("orderHandleResultResponse 为空!");
                RecordFloatView.updateMessage("orderHandleResultResponse 为空!");
                ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
                //再次发送警报
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器端应答警报报告时出现异常!");
            RecordFloatView.updateMessage("服务器端应答警报报告时出现异常!");
            ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
            //再次发送警报
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 10000);
        }
    }


    /**
     * 客户端给服务器发送全部截图上传成功消息
     *
     * @param msgType 消息类型 8009
     * @param object  SendUploadAllFinished
     */
    public void sendMsgForCaptureState(String msgType, Object object) {
        NioTask.getInstance().currentMsgType = msgType;
        NioTask.getInstance().currentSendObject = (BaseSessionObject) object;
        SendUploadAllFinished sendUploadAllFinished = null;
        if (object instanceof SendUploadAllFinished) {
            sendUploadAllFinished = (SendUploadAllFinished) object;
        } else {
            LLog.e("instanceof SendUploadAllFinished Error !");
            RecordFloatView.updateMessage("instanceof SendUploadAllFinished Error !");
            //再次给服务器发送全部截图上传成功消息
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
            return;
        }
        if (sendUploadAllFinished != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", sendUploadAllFinished.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("taskType", DESUtil.encrypt(sendUploadAllFinished.getTaskType()));
                data.put("taskData", DESUtil.encrypt(sendUploadAllFinished.getTaskData()));
                data.put("robotName", DESUtil.encrypt(sendUploadAllFinished.getRobotName()));
                data.put("timeStamp", DESUtil.encrypt(sendUploadAllFinished.getTimeStamp()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "发送全部截图上传成功消息-成功!", true);

            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始发送全部截图上传成功消息异常!");
                RecordFloatView.updateMessage("开始发送全部截图上传成功消息异常!");
                //再次发送全部截图上传成功消息
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
            }
        }
    }


    /**
     * 服务器给客户端发送截图全部上传成功消息的应答
     *
     * @param object
     */
    public void receiveMsgForCaptureStateRe(Object object) {
        NioTask.getInstance().isMsgResponsed = true;
        NioTask.getInstance().currentMsgType = null;
        NioTask.getInstance().currentSendObject = null;
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForCaptureStateRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForCaptureStateRe Error !");
            //再次给服务器发送全部截图上传成功消息
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务器给客户端发送截图全部上传成功消息的应答 data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答 data为空 response:" + response.toString());
                //再次发送全部截图上传成功消息
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
                return;
            }
            SendUploadAllFinishedResponse sendUploadAllFinishedResponse = ScriptApplication.getGson().fromJson(data, SendUploadAllFinishedResponse.class);
            if (sendUploadAllFinishedResponse != null) {
                if (!TextUtils.isEmpty(sendUploadAllFinishedResponse.getRetcode())) {
                    String retCode = DESUtil.decrypt(sendUploadAllFinishedResponse.getRetcode());
                    if (!"0".equals(retCode)) {
                        LLog.e("服务器给客户端发送截图全部上传成功消息的应答 retCode=" + retCode);
                        RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答 retCode=" + retCode);
                        if ("1".equals(retCode)) {
                            LLog.e("服务器给客户端发送截图全部上传成功消息的应答 任务不存在!");
                            RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答 任务不存在!");
                            BaseOrderHelper.resetData();
                        } else if ("2".equals(retCode)) {
                            String imgNames = DESUtil.decrypt(sendUploadAllFinishedResponse.getErrorImages());
                            LLog.e("图片缺失: " + imgNames);
                            RecordFloatView.updateMessage("图片缺失: " + imgNames);
                            if (!TextUtils.isEmpty(imgNames)) {
                                NioTask.getInstance().isUploadErrorImages = true;
                                if (imgNames.contains(",")) {
                                    String[] names = imgNames.split(",");
                                    for (String name : names) {
                                        if (!TextUtils.isEmpty(name)) {
                                            File file = new File(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sGamesOrderInfo.getTaskData() + "/" + name);
                                            if (file.exists()) {
                                                NioTask.getInstance().sUploadImgsList.add(file);
                                            } else {
                                                LLog.e("图片不存在: " + name);
                                                RecordFloatView.updateMessage("图片不存在: " + name);
                                            }
                                        }
                                    }
                                } else {
                                    File file = new File(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sGamesOrderInfo.getTaskData() + "/" + imgNames);
                                    if (file.exists()) {
                                        NioTask.getInstance().sUploadImgsList.add(file);
                                    } else {
                                        LLog.e("图片不存在: " + imgNames);
                                        RecordFloatView.updateMessage("图片不存在: " + imgNames);
                                    }
                                }
                                ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
                                //再次发送上传图片
                                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_IMG, 3000);
                            } else {
                                ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
                                //再次发送全部截图上传成功消息
                                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
                            }
                        }
                    } else {
                        String timeStamp = DESUtil.decrypt(sendUploadAllFinishedResponse.getTimeStamp());
                        if (!TextUtils.isEmpty(timeStamp)) {
                            if (StringUtil.isInteger(timeStamp)) {
                                if (Long.parseLong(timeStamp) > NioTask.getInstance().currentTimeStamp) {
                                    NioTask.getInstance().currentTimeStamp = Long.parseLong(timeStamp);
                                    LLog.e("订单处理成功，开始获取下一订单!");
                                    RecordFloatView.updateMessage("订单处理成功，开始获取下一订单!");
                                    FileHelper.deleteFolderFile(Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sGamesOrderInfo.getTaskData());
                                    Constants.sGamesOrderInfo = null;
                                    FileUtil.saveFile("", Constants.MANAGER_ORDER_FOLDER_TEMP_PATH);
                                    BaseOrderHelper.resetData();//重置状态值
                                    ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
                                }
                            } else {
                                LLog.e("服务器返回的timeStamp非纯数字！");
                            }
                        } else {
                            LLog.e("服务器返回的timeStamp为空！");
                        }
                    }
                } else {
                    LLog.e("服务器给客户端应答截图全部上传成功 retCode为空!");
                    RecordFloatView.updateMessage("服务器给客户端应答截图全部上传成功 retCode为空!");
                    ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
                    //再次发送全部截图上传成功消息
                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
                }
            } else {
                LLog.e("sendUploadAllFinishedResponse 为空!");
                RecordFloatView.updateMessage("sendUploadAllFinishedResponse 为空!");
                ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
                //再次发送全部截图上传成功消息
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器给客户端发送截图全部上传成功消息的应答时出现异常!");
            RecordFloatView.updateMessage("服务器给客户端发送截图全部上传成功消息的应答时出现异常!");
            ScriptUtil.dealWithFloatView("移动悬浮按钮#0#0");
            //再次发送全部截图上传成功消息
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ALL_UPLOAD_FINISH, 10000);
        }
    }


    /**
     * 服务器发送一个询问状态消息给客户端
     *
     * @param object
     */
    public void receiveMsgForCheckRobotStateRe(Object object) {
        JSONObject response = null;
        if (object instanceof JSONObject) {
            response = (JSONObject) object;
        } else {
            LLog.e("instanceof JSONObject receiveMsgForCheckRobotStateRe Error !");
            RecordFloatView.updateMessage("instanceof JSONObject receiveMsgForCheckRobotStateRe Error !");
            return;
        }
        try {
            String data = response.optString("data");
            if (TextUtils.isEmpty(data)) {
                LLog.e("服务器发送一个询问状态消息给客户端 data为空 response:" + response.toString());
                RecordFloatView.updateMessage("服务器发送一个询问状态消息给客户端 data为空 response:" + response.toString());
                return;
            }
            ReceiveCheckRobotState receiveCheckRobotState = ScriptApplication.getGson().fromJson(data, ReceiveCheckRobotState.class);
            if (receiveCheckRobotState != null) {
                if (!TextUtils.isEmpty(receiveCheckRobotState.getRobotName())) {
                    String robotName = DESUtil.decrypt(receiveCheckRobotState.getRobotName());
                    if (!SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow").equals(robotName)) {
                        LLog.e("服务器询问机器人状态消息时phoneName不匹配" + " phoneName:" + robotName);
                        RecordFloatView.updateMessage("服务器询问机器人状态消息时phoneName不匹配" + " phoneName:" + robotName);
                    } else {
                        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ROBOT_STATUS_RE, 1000);
                    }
                }
            } else {
                LLog.e("receiveCheckRobotState 为空!");
                RecordFloatView.updateMessage("receiveCheckRobotState 为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("服务器发送一个询问状态消息给客户端时出现异常!");
            RecordFloatView.updateMessage("服务器发送一个询问状态消息给客户端时出现异常!");
        }
    }


    /**
     * 客户端应答一个检测状态消息给服务器端
     *
     * @param msgType 消息类型 6002
     * @param object  SendCheckRobotStateResponse
     */
    public void sendMsgForCheckRobotState(String msgType, Object object) {
        SendCheckRobotStateResponse sendCheckRobotStateResponse = null;
        if (object instanceof SendCheckRobotStateResponse) {
            sendCheckRobotStateResponse = (SendCheckRobotStateResponse) object;
        } else {
            LLog.e("instanceof SendCheckRobotStateResponse Error !");
            RecordFloatView.updateMessage("instanceof SendCheckRobotStateResponse Error !");
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ROBOT_STATUS_RE, 10000);
            return;
        }
        if (sendCheckRobotStateResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", sendCheckRobotStateResponse.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("retcode", DESUtil.encrypt(sendCheckRobotStateResponse.getRetcode()));
                data.put("taskType", DESUtil.encrypt(sendCheckRobotStateResponse.getTaskType()));
                data.put("taskData", DESUtil.encrypt(sendCheckRobotStateResponse.getTaskData()));
                data.put("errorType", DESUtil.encrypt(sendCheckRobotStateResponse.getErrorType()));
                data.put("errorDesc", DESUtil.encrypt(sendCheckRobotStateResponse.getErrorDesc()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "发送机器人当前状态成功!", false);
            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始发送机器人当前状态时出现异常!");
                RecordFloatView.updateMessage("开始发送机器人当前状态时出现异常!");
                //再次发送机器人当前状态
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ROBOT_STATUS_RE, 10000);
            }
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
            ReceiveCheckAlive receiveCheckAlive = ScriptApplication.getGson().fromJson(data, ReceiveCheckAlive.class);
            if (receiveCheckAlive != null) {
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ROBOT_ALIVE_RE, 1000);
            } else {
                LLog.e("receiveCheckAlive 为空!");
                RecordFloatView.updateMessage("receiveCheckAlive 为空!");
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
        SendCheckAliveResponse sendCheckAliveResponse = null;
        if (object instanceof SendCheckAliveResponse) {
            sendCheckAliveResponse = (SendCheckAliveResponse) object;
        } else {
            LLog.e("instanceof SendCheckAliveRe Error !");
            RecordFloatView.updateMessage("instanceof SendCheckAliveRe Error !");
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ROBOT_ALIVE_RE, 10000);
            return;
        }
        if (sendCheckAliveResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", sendCheckAliveResponse.getName());
                jsonObject.put("type", msgType);
                JSONObject data = new JSONObject();
                data.put("robotName", DESUtil.encrypt(sendCheckAliveResponse.getRobotName()));
                jsonObject.put("data", data.toString());

                nioPackageAndSend(jsonObject, "发送应答心跳成功!", false);
            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("开始发送应答心跳时出现异常!");
                RecordFloatView.updateMessage("开始发送应答心跳时出现异常!");
                //再次应答一个心跳消息给服务器端
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ROBOT_ALIVE_RE, 10000);
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
    public void nioPackageAndSend(JSONObject jsonObject, String successMsg, boolean needToast) {
        ScriptApplication.getService().execute(() -> {
            if (jsonObject != null) {
                String type = jsonObject.optString("type");
                if (connectFuture != null) {
                    if (connectFuture.isConnected() && connectFuture.getSession().isConnected()) {
                        WriteFuture future = connectFuture.getSession().write(jsonObject.toString());
                        future.awaitUninterruptibly();
                        if (future.isWritten()) {
                            if (!TextUtils.isEmpty(successMsg)) {
                                LLog.i(successMsg);
                                if (needToast) {
                                    RecordFloatView.updateMessage(successMsg);
                                }
                            }
                            if (!TASK_TYPE_8001.equals(type) && !TASK_TYPE_6002.equals(type) && !TASK_TYPE_1002.equals(type)) {
                                NioTask.getInstance().isMsgResponsed = false;
                            }
                        } else {
                            LLog.e("发送失败!");
                            RecordFloatView.updateMessage("发送失败!");
                            if (!TASK_TYPE_8001.equals(type) && !TASK_TYPE_6002.equals(type) && !TASK_TYPE_1002.equals(type)) {
                                NioTask.getInstance().isMsgResponsed = false;
                            }
                        }
                    } else {
                        LLog.e("未成功连接远程会话!");
                        RecordFloatView.updateMessage("未成功连接远程会话!");
                        if (!TASK_TYPE_8001.equals(type) && !TASK_TYPE_6002.equals(type) && !TASK_TYPE_1002.equals(type)) {
                            NioTask.getInstance().isMsgResponsed = false;
                        }
                    }
                } else {
                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_MINA_RESET, 5000);
                }
            }
        });
    }


}

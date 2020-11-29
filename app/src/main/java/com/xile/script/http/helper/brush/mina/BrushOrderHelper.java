package com.xile.script.http.helper.brush.mina;

import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.CommentInfo;
import com.xile.script.bean.DeviceInfo;
import com.xile.script.bean.PictureInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.config.SleepConfig;
import com.xile.script.http.common.*;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.brush.bean.BrushOrderInfo;
import com.xile.script.service.AndServerService;
import com.xile.script.service.UploadKeychainService;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.TimeUtil;
import com.xile.script.utils.common.*;
import com.xile.script.utils.script.*;
import com.xile.script.utils.traffic.StatisticTrafficManager;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import script.tools.config.KeyCode;
import script.tools.config.ScriptConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * date: 2017/5/9 18:11
 *
 * @scene 优化订单工具类
 */


public class BrushOrderHelper extends BaseOrderHelper {
    private volatile static BrushOrderHelper instance;
    public List<CommentInfo> TvComments;//评论话库
    public PictureInfo commentImgUrls;//图片库
    public boolean rechargePicUploaded;//是否上传过充值截图
    public final String script_app = "app";
    public final String script_python = "python";
    public boolean vpnConnected = false;

    private BrushOrderHelper() {


    }

    public static synchronized BrushOrderHelper getInstance() {
        if (instance == null) {
            instance = new BrushOrderHelper();
        }
        return instance;
    }

    /**
     * 下载脚本
     *
     * @param url
     */
    public void downloadScript(String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                long time = System.currentTimeMillis();
                url = url.replace("\\", "/");
                String name = url.substring(url.lastIndexOf("/") + 1);
                File script = new File(Constants.SCRIPT_FOLDER_PATH + name);
                LLog.i(Constants.SCRIPT_FOLDER_PATH + name);
                HttpRequest.download(url, script, new FileDownloadCallback() {
                    //开始下载
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    //下载进度
                    @Override
                    public void onProgress(int progress, long networkSpeed) {
                        super.onProgress(progress, networkSpeed);
                    }

                    //下载失败
                    @Override
                    public void onFailure() {
                        super.onFailure();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("脚本下载失败!");
                        RecordFloatView.updateMessage("脚本下载失败!");
                        orderDealFailure("脚本下载失败!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                    }

                    //下载完成（下载成功）
                    @Override
                    public void onDone() {
                        super.onDone();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("脚本下载成功!");
                        RecordFloatView.updateMessage("脚本下载成功!");
                        //开始执行脚本
                        List<String> scripts = null;
                        if (Constants.sBrushOrderInfo != null && Constants.sBrushOrderInfo.getEncrypt() == 0) {
                            scripts = TxtUtils.getTxtList(Constants.SCRIPT_FOLDER_PATH + name);
                        } else if (Constants.sBrushOrderInfo != null && Constants.sBrushOrderInfo.getEncrypt() == 1) {
                            String encrytpScript = FileHelper.getFileString(Constants.SCRIPT_FOLDER_PATH + name);
                            String decriptScript = DESUtil.decrypt(encrytpScript);
                            if (TextUtils.isEmpty(decriptScript)) {
                                //订单处理失败
                                LLog.e("脚本内容为空111!");
                                FileHelper.copyFile(Constants.SCRIPT_FOLDER_PATH + name, Constants.SCRIPT_FOLDER_LOCAL_COMMENT_PATH + "111_" + name);
                                RecordFloatView.updateMessage("脚本内容为空!");
                                orderDealFailure("脚本内容为空111!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                                return;
                            }
                            String[] instructs = decriptScript.split("\n");
                            if (instructs.length > 0) {
                                scripts = new ArrayList<>();
                                for (String instruct : instructs) {
                                    scripts.add(instruct.trim());
                                }
                            }
                        }


                        if (scripts != null && scripts.size() > 0) {
                            if (script_app.equals(Constants.sBrushOrderInfo.getChannelType())) {  //常规APP脚本
                                ExecuteUtil.execServerScript(1, scripts);
                            } else if (script_python.equals(Constants.sBrushOrderInfo.getChannelType())) {  //python脚本
                                String pythonScript = FileHelper.getFileString(Constants.SCRIPT_FOLDER_PATH + name);
                                ScriptApplication.getInstance().startService(new Intent(ScriptApplication.getInstance(), AndServerService.class));
                                BrushTask.getInstance().sendMessageBundle(BrushTask.B_MSG_TYPE_DELIVER_PYTHON, pythonScript, 10000);

                            }
                        } else {
                            //订单处理失败
                            LLog.e("脚本内容为空222!");
                            FileHelper.copyFile(Constants.SCRIPT_FOLDER_PATH + name, Constants.SCRIPT_FOLDER_LOCAL_COMMENT_PATH + "222_" + name);
                            RecordFloatView.updateMessage("脚本内容为空!");
//                            orderDealFailure("脚本内容为空222!   encrypt == "+Constants.sBrushOrderInfo.getEncrypt(), "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                            orderDealFailure("脚本内容为空222!   encrypt == ", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                        }
                    }
                });
            } else {
                //订单处理失败
                LLog.e("脚本链接为空!");
                RecordFloatView.updateMessage("脚本链接为空!");
                orderDealFailure("脚本链接为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("处理脚本异常!");
            RecordFloatView.updateMessage("处理脚本异常!");
            orderDealFailure("处理脚本异常!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");

        }
    }


    /**
     * 下载评论话库
     *
     * @param url
     */
    public void downloadComment(String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                long time = System.currentTimeMillis();
                url = url.replace("\\", "/");
                String name = url.substring(url.lastIndexOf("/") + 1);
                SpUtil.putKeyString(PlatformConfig.CURRENT_COMMENT_FILE_NAME, name);
                File script = new File(Constants.SCRIPT_FOLDER_COMMENT_PATH + name);
                LLog.i(Constants.SCRIPT_FOLDER_COMMENT_PATH + name);
                HttpRequest.download(url, script, new FileDownloadCallback() {
                    //开始下载
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    //下载进度
                    @Override
                    public void onProgress(int progress, long networkSpeed) {
                        super.onProgress(progress, networkSpeed);
                    }

                    //下载失败
                    @Override
                    public void onFailure() {
                        super.onFailure();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("评论话库下载失败!");
                        RecordFloatView.updateMessage("评论话库下载失败!");
                        orderDealFailure("评论话库下载失败!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                    }

                    //下载完成（下载成功）
                    @Override
                    public void onDone() {
                        super.onDone();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("评论话库下载成功!");
                        RecordFloatView.updateMessage("评论话库下载成功!");
                        List<String> comments = FileHelper.readFile(Constants.SCRIPT_FOLDER_COMMENT_PATH + name);
                        if (comments != null && comments.size() > 0) {
                            CommentInfo commentInfo = new CommentInfo(name, comments, 0);
                            BrushOrderHelper.getInstance().TvComments.add(commentInfo);
                        } else {
                            //订单处理失败
                            LLog.e("评论话库内容为空!");
                            RecordFloatView.updateMessage("评论话库内容为空!");
                            //orderDealFailure();
                        }
                    }
                });
            } else {
                LLog.e("评论话库链接为空!");
                RecordFloatView.updateMessage("评论话库链接为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("处理评论话库异常!");
            RecordFloatView.updateMessage("处理评论话库异常!");
            orderDealFailure("处理评论话库异常!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }

    /**
     * 下载Excel脚本
     *
     * @param url
     */
    public void downloadExcel(String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                long time = System.currentTimeMillis();
                url = url.replace("\\", "/");
                String name = url.substring(url.lastIndexOf("/") + 1);
                File script = new File(Constants.SCRIPT_EXCEL_PATH, name);
                LLog.i(Constants.SCRIPT_EXCEL_PATH + name);
                HttpRequest.download(url, script, new FileDownloadCallback() {
                    //开始下载
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    //下载进度
                    @Override
                    public void onProgress(int progress, long networkSpeed) {
                        super.onProgress(progress, networkSpeed);
                    }

                    //下载失败
                    @Override
                    public void onFailure() {
                        super.onFailure();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("充值脚本下载失败!");
                        RecordFloatView.updateMessage("充值脚本下载失败!");
                        orderDealFailure("充值脚本下载失败!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                    }

                    //下载完成（下载成功）
                    @Override
                    public void onDone() {
                        super.onDone();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("充值脚本下载成功!");
                        //RecordFloatView.updateMessage("充值脚本下载成功!");
                    }
                });
            } else {
                LLog.e("充值脚本链接为空!");
                RecordFloatView.updateMessage("充值脚本链接为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("充值脚本异常!");
            RecordFloatView.updateMessage("充值脚本异常!");
            orderDealFailure("充值脚本异常!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }

    /**
     * 下载txt模板
     *
     * @param url
     */
    public void downloadMatchTmp(String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                long time = System.currentTimeMillis();
                url = url.replace("\\", "/");
                String name = url.substring(url.lastIndexOf("/") + 1);
                File script = new File(Constants.SCRIPT_MATCH_PATH, name);
                LLog.i(Constants.SCRIPT_EXCEL_PATH + name);
                HttpRequest.download(url, script, new FileDownloadCallback() {
                    //开始下载
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    //下载进度
                    @Override
                    public void onProgress(int progress, long networkSpeed) {
                        super.onProgress(progress, networkSpeed);
                    }

                    //下载失败
                    @Override
                    public void onFailure() {
                        super.onFailure();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("模板下载失败!");
                        RecordFloatView.updateMessage("模板下载失败!");
                        orderDealFailure("模板下载失败!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                    }

                    //下载完成（下载成功）
                    @Override
                    public void onDone() {
                        super.onDone();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("模板下载成功!");
                        //RecordFloatView.updateMessage("模板下载成功!");
                    }
                });
            } else {
                LLog.e("模板链接为空!");
                RecordFloatView.updateMessage("模板链接为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("下载模板异常!");
            RecordFloatView.updateMessage("下载模板异常!");
            orderDealFailure("下载模板异常!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }


    /**
     * 图片处理
     *
     * @param brushOrderInfo
     */
    public void dealWithImgs(BrushOrderInfo brushOrderInfo) {
        PopupUtil.clear();
        if (brushOrderInfo.getAccountImagePathList() != null && brushOrderInfo.getAccountImagePathList().size() > 0) {//platform
            PopupUtil.getPlatformNameList().addAll(getImageName(brushOrderInfo.getAccountImagePathList()));
        }
        if (brushOrderInfo.getAlertImagePathList() != null && brushOrderInfo.getAlertImagePathList().size() > 0) {//alert
            PopupUtil.getAlertNameList().addAll(getImageName(brushOrderInfo.getAlertImagePathList()));
        }
        if (brushOrderInfo.getPopUpWindowsImagePathList() != null && brushOrderInfo.getPopUpWindowsImagePathList().size() > 0) {//popup
            PopupUtil.getPopupNameList().addAll(getImageName(brushOrderInfo.getPopUpWindowsImagePathList()));
        }
        if (brushOrderInfo.getCompareImagePathList() != null && brushOrderInfo.getCompareImagePathList().size() > 0) {//compare
            PopupUtil.getCompareNameList().addAll(getImageName(brushOrderInfo.getCompareImagePathList()));
        }
        if (brushOrderInfo.getSampleImagePathList() != null && brushOrderInfo.getSampleImagePathList().size() > 0) {//sample
            PopupUtil.getSampleNameList().addAll(getImageName(brushOrderInfo.getSampleImagePathList()));
        }
        if (brushOrderInfo.getPayImagePathList() != null && brushOrderInfo.getPayImagePathList().size() > 0) {//module
            PopupUtil.getModuleNameList().addAll(getImageName(brushOrderInfo.getPayImagePathList()));
        }
    }


    /**
     * 检测是否需要重启手机
     *
     * @param brushOrderInfo
     * @return
     */
    public static boolean checkNeedReboot(final BrushOrderInfo brushOrderInfo) {
        final String tempUserId = FileUtil.readFile(Constants.BRUSH_ORDER_ID_TEMP_PATH);
        if (!brushOrderInfo.getUserId().equals(tempUserId)) {
//            final PopupDialog dialog = new PopupDialog(ScriptApplication.getInstance(), 1, false);
//            dialog.setTitleText(ScriptApplication.getInstance().getString(R.string.dialog_reboot_phone));
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
//            dialog.setOnPositiveListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
            ScriptApplication.getService().execute(new Runnable() {
                @Override
                public void run() {
//                            dialog.dismiss();
                    String imei_1 = DeviceUtil.getIMEI(ScriptApplication.getInstance());
                    ScriptUtil.dealWithChangeDeviceInfo();
                    SystemClock.sleep(3000);
                    String imei_2 = DeviceUtil.getIMEI(ScriptApplication.getInstance());
                    LLog.i("修改前imei_1==========: " + imei_1);
                    LLog.i("修改后imei_2==========: " + imei_2);
                    if (TextUtils.isEmpty(imei_1) || TextUtils.isEmpty(imei_2) || imei_1.equals(imei_2)) {
                        LLog.e("修改imei失败，返回订单信息失败");
                        RecordFloatView.updateMessage("修改imei失败，即将重启！！！");
                        FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "修改imei失败 : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.CHANGE_DEVICE_INFO_SCRIPT);
                        SystemClock.sleep(3000);
                        dealWithXposedReboot();
                        return;
                    }
                    CMDUtil.execShell("chmod 777 /system");
                    SystemClock.sleep(1000);
                    CMDUtil.execShell("chmod 777 /system/build.prop");
                    SystemClock.sleep(1000);
                    CMDUtil.execShell("mount -o rw,remount -t rootfs /system");
                    SystemClock.sleep(1000);

                    String hardwareInfo = FileUtil.readFile("/system/build.prop");
                    String deviceStr = SpUtil.getKeyString(PlatformConfig.CURRENT_DEVICE_INFO, "");//取当前设备信息
                    DeviceInfo deviceInfo = ScriptApplication.getGson().fromJson(deviceStr, DeviceInfo.class);
                    //写build.prop
                    String buildId = getHardwareInfo(hardwareInfo, "ro.build.id=");
                    String displayId = getHardwareInfo(hardwareInfo, "ro.build.display.id=");
                    String brand = getHardwareInfo(hardwareInfo, "ro.product.brand=");
                    String model = getHardwareInfo(hardwareInfo, "ro.product.model=");
                    String device = getHardwareInfo(hardwareInfo, "ro.product.device=");
                    String name = getHardwareInfo(hardwareInfo, "ro.product.name=");
                    String product = getHardwareInfo(hardwareInfo, "ro.build.product=");
                    String manufacturer = getHardwareInfo(hardwareInfo, "ro.product.manufacturer=");
                    String description = getHardwareInfo(hardwareInfo, "ro.build.description=");
                    String fingerprint = getHardwareInfo(hardwareInfo, "ro.build.fingerprint=");
                    String randomId = RandomUtil.getRandomNum(5, 9) + "." + RandomUtil.getRandomNum(0, 9) + "." + RandomUtil.getRandomNum(0, 9);
                    hardwareInfo = hardwareInfo.replace("ro.build.id=" + buildId, "ro.build.id=" + deviceInfo.getDisplay()).replace("ro.build.display.id=" + displayId, "ro.build.display.id=" + deviceInfo.getDisplay())
                            .replace("ro.product.brand=" + brand, "ro.product.brand=" + deviceInfo.getBrand()).replace("ro.product.model=" + model, "ro.product.model=" + deviceInfo.getModel())
                            .replace("ro.product.device=" + device, "ro.product.device=" + deviceInfo.getBrand()).replace("ro.product.name=" + name, "ro.product.name=" + deviceInfo.getBrand())
                            .replace("ro.build.product=" + product, "ro.build.product=" + deviceInfo.getBrand())
                            .replace("ro.product.manufacturer=" + manufacturer, "ro.product.manufacturer=" + deviceInfo.getManufacturer())
                            .replace("ro.build.description=" + description, "ro.build.description=" + deviceInfo.getBrand() + "-user " + deviceInfo.getRelease() + " " + deviceInfo.getDisplay() + " " + randomId + " release-keys")
                            .replace("ro.build.fingerprint=" + fingerprint, "ro.build.fingerprint=" + deviceInfo.getBrand() + "/" + deviceInfo.getDevice() + "/" + deviceInfo.getDevice() + ":" + deviceInfo.getRelease() + "/" + deviceInfo.getDisplay() + "/" + randomId + ":user/release-keys");
                    FileUtil.saveFile(hardwareInfo, "/sdcard/build.prop");
                    //一定要还原权限 否则手机启动不了
                    CMDUtil.execShell("cp /sdcard/build.prop /system/build.prop");
                    SystemClock.sleep(1000);
                    CMDUtil.execShell("chmod 0644 /system/build.prop");
                    //写tempOrderId
                    FileUtil.saveFile(brushOrderInfo.getUserId(), Constants.BRUSH_ORDER_ID_TEMP_PATH);
                    SystemClock.sleep(1000);
                    CMDUtil.execShell("reboot");
//                        }
//                    });
                }
            });
//            dialog.show();
            return true;
        }
        return false;
    }

    /**
     * 替换手机硬件信息
     *
     * @param scrStr
     * @param property
     * @return
     */
    public static String getHardwareInfo(String scrStr, String property) {
        String descStr = scrStr.substring(scrStr.indexOf(property) + property.length());
        descStr = descStr.substring(0, descStr.indexOf("\n"));
        return descStr;
    }

    /**
     * 重启xposed  单次操作
     */
    public static void dealWithXposedReboot() {
        SystemClock.sleep(2000);
        AppUtil.startAppByADB(ScriptApplication.getInstance(), "de.robv.android.xposed.installer");
        SystemClock.sleep(5000);
        CMDUtil.execShell("input swipe 10 600 800 600 1500");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input tap 540 290");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input tap 968 317");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input tap 960 540");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input swipe 10 600 800 600 1500");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input tap 540 140");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input tap 1017 138");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input tap 740 145");
        SystemClock.sleep(2000);
        CMDUtil.execShell("input tap 835 1095");
        SystemClock.sleep(5000);
        CMDUtil.execShell("reboot");
    }


    /**
     * 传递Python脚本
     *
     * @param orderId
     * @param script
     */
    public void deliverPythonScript(final String orderId, final String script) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LLog.d("传递Python脚本 -- \norderId:" + orderId + "\nscript:\\n" + script);
                    if (TextUtils.isEmpty(orderId) || TextUtils.isEmpty(script)) {
                        BrushOrderHelper.getInstance().orderDealFailure("传递Python脚本id为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                    } else {
                        OkHttpClient client = new OkHttpClient();
                        MediaType mediaType = MediaType.parse("application/json");
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("orderid", orderId);
                        jsonObject.put("script", script);
                        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                        Request request = new Request.Builder()
                                .url("http://localhost:8001/douyuTV")
                                .post(body)
                                .addHeader("content-type", "application/json")
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        int code = (int) jsonObject.opt("code");
                                        String data = (String) jsonObject.opt("data");
                                        if (code == 200) {
                                            LLog.e("调取传递Python脚本接口成功!");
                                            RecordFloatView.updateMessage("调取传递Python脚本接口成功!");
                                        } else {
                                            LLog.e("调取传递Python脚本接口失败code=" + code + ",data=" + data);
                                            RecordFloatView.updateMessage("调取传递Python脚本接口失败code=" + code + ",data=" + data);
                                            BrushTask.getInstance().sendMessageBundle(BrushTask.B_MSG_TYPE_DELIVER_PYTHON, script, 10000);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LLog.e("调取传递Python脚本接口异常!");
                                        RecordFloatView.updateMessage("调取传递Python脚本接口异常!");
                                        BrushTask.getInstance().sendMessageBundle(BrushTask.B_MSG_TYPE_DELIVER_PYTHON, script, 10000);
                                    }
                                } else {
                                    LLog.e("调取传递Python脚本接口response为空!");
                                    RecordFloatView.updateMessage("调取传递Python脚本接口response为空!");
                                    BrushTask.getInstance().sendMessageBundle(BrushTask.B_MSG_TYPE_DELIVER_PYTHON, script, 10000);
                                }
                            }

                            @Override
                            public void onFailure(Call call, IOException e) {
                                LLog.e("调取传递Python脚本接口失败!");
                                RecordFloatView.updateMessage("调取传递Python脚本接口失败!");
                                BrushTask.getInstance().sendMessageBundle(BrushTask.B_MSG_TYPE_DELIVER_PYTHON, script, 10000);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LLog.e("调取传递Python脚本接口异常!");
                    RecordFloatView.updateMessage("调取传递Python脚本接口异常!");
                    BrushTask.getInstance().sendMessageBundle(BrushTask.B_MSG_TYPE_DELIVER_PYTHON, script, 10000);
                }

            }
        }).start();
    }

    /**
     * 发送HTTP协议
     *
     * @param message
     */
    public void sendHttpMessage(final String type, Object message) {
        try {
            final HashMap parms = new HashMap();
            parms.put("message", message.toString());
            PostHttpRequest.getInstance().post(HttpConstants.SEND_HTTP_MESSAGE, parms, new StringCallback() {
                @Override
                public void onResponse(String response, int id) {
                    super.onResponse(response, id);
                    try {
                        if (!TextUtils.isEmpty(response)) {
                            LLog.e("优化 - 接收到消息 message:" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String type = jsonObject.optString("type");
                            if (!TextUtils.isEmpty(type)) {
                                LLog.e("优化 - 接收到消息 type:" + type);
                                BrushHelper.getInstance().getMessage(type, jsonObject);
                            } else {
                                LLog.e("优化 - 接收到消息 type为NULL！");
                                RecordFloatView.updateMessage("接收消息 message type为NULL！");
                            }
                        } else {
                            LLog.e("调取HTTP协议response为空!");
                            RecordFloatView.updateMessage("调取HTTP协议response为空!");
                            if (!BrushHelper.B_TASK_TYPE_9001.equals(type) && !BrushHelper.B_TASK_TYPE_1004.equals(type)) {
                                BrushTask.getInstance().b_isMsgResponsed = false;
                            }
                            if (BrushHelper.B_TASK_TYPE_9001.equals(type)) {
                                SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LLog.e("调取HTTP协议时出现异常!");
                        if (!BrushHelper.B_TASK_TYPE_9001.equals(type) && !BrushHelper.B_TASK_TYPE_1004.equals(type)) {
                            BrushTask.getInstance().b_isMsgResponsed = false;
                        }
                        if (BrushHelper.B_TASK_TYPE_9001.equals(type)) {
                            SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
                        }
                    }
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    super.onError(call, e, id);
                    LLog.e("调取发送HTTP协议接口失败!  " + e);
                    RecordFloatView.updateMessage("调取发送HTTP协议接口失败! " + e);
                    Constants.failRepeatCount++;
                    BrushTask.getInstance().b_isMsgResponsed = true;
                    if ((BrushHelper.B_TASK_TYPE_9007.equals(type)) && Constants.failRepeatCount > 15) {//返回设备信息
                        Constants.failRepeatCount = 0;
                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_GAME_DOWNLOAD_FAIL);
                        return;
                    } else if (BrushHelper.B_TASK_TYPE_9011.equals(type) && Constants.failRepeatCount > 15) {//返回IP信息
                        Constants.failRepeatCount = 0;
                        try {
                            JSONObject data = new JSONObject(((JSONObject) message).optString("data"));
                            int ipFlag = data.optInt("ipFlag");
                            if (ipFlag == 0) {//第一次返回IP信息
                                ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_GAME_DOWNLOAD_FAIL);
                            } else if (ipFlag == 1) {//第二次返回IP信息 订单结束
                                //返回订单处理成功与否
                                if (SpUtil.getKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false)) {
                                    ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_SUCCESS);
                                } else {
                                    FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "第二次返回IP信息 订单结束 : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");

                                    ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_FAILED);
                                }
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        return;
                    } else if (BrushHelper.B_TASK_TYPE_9015.equals(type) && Constants.failRepeatCount > 30) {//获取手机号
                        Constants.failRepeatCount = 0;
                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_REGISTER_FAIL);
                        return;
                    } else if ((BrushHelper.B_TASK_TYPE_9009.equals(type)) && Constants.failRepeatCount > 15) {//返回用户信息
                        Constants.failRepeatCount = 0;
                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_CREATE_ROLE_FAIL);
                        return;
                    } else if ((BrushHelper.B_TASK_TYPE_9027.equals(type)) && Constants.failRepeatCount > 30) {//获取校验码
                        Constants.failRepeatCount = 0;
                        FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "发送HTTP协议 B_TASK_TYPE_9027! : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");

                        ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.ORDER_FAILED);
                        return;
                    }
                    if (BrushHelper.B_TASK_TYPE_9001.equals(type)) {
                        SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
                    }
                    if (!BrushHelper.B_TASK_TYPE_9001.equals(type) && !BrushHelper.B_TASK_TYPE_1004.equals(type)) {
                        BrushTask.getInstance().b_isMsgResponsed = false;
                    }
                    if (BrushHelper.B_TASK_TYPE_9003.equals(type)) {
                        BrushOrderHelper.getInstance().closeVPN();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("调取发送HTTP协议接口异常!");
            RecordFloatView.updateMessage("调取发送HTTP协议接口异常!");
            if (!BrushHelper.B_TASK_TYPE_9001.equals(type) && !BrushHelper.B_TASK_TYPE_1004.equals(type)) {
                BrushTask.getInstance().b_isMsgResponsed = false;
            }
            if (BrushHelper.B_TASK_TYPE_9001.equals(type)) {
                SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
            }
        }
    }

    /**
     * 上传Keychain
     *
     * @param fileName
     * @param userId
     * @param message
     */
    public void uploadKeychain(final String fileName, String userId, Object message) {
        try {
            LLog.i("开始上传keychain 当前文件: " + fileName);
            RequestParams requestParams = new RequestParams();
            File file = new File(fileName.replace(".cfg", ""));
            requestParams.addFormDataPart("file", file);
            new FileHttpRequest().post(HttpConstants.UPLOAD_FILE_HOST + "&userId=" + userId, requestParams, new FileCallBack() {
                @Override
                protected void onSuccess(Object response) {
                    try {
                        if (response != null) {
                            LLog.e("上传Keychain - 接收到消息 message:" + response);
                            JSONObject jsonObject = new JSONObject(response.toString());
                            int code = jsonObject.optInt("code");
                            if (code == 200) {
                                UploadKeychainService.isUploading = false;
                                LLog.e("调取上传keychain文件成功!");
                                RecordFloatView.updateMessage("调取上传keychain文件成功!");
                                FileHelper.deleteFile(fileName);
                                String keychain = fileName.replace(".cfg", "");
                                FileHelper.deleteFile(keychain);
                            } else {
                                UploadKeychainService.isUploading = false;
                                LLog.e("上传Keychain - 接收到消息 code:" + code);
                            }
                        } else {
                            UploadKeychainService.isUploading = false;
                            LLog.e("调取上传Keychain response为空!");
                            RecordFloatView.updateMessage("调取上传Keychain response为空!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        UploadKeychainService.isUploading = false;
                        LLog.e("调取uploadKeychain HTTP协议时出现异常!");
                    }
                }

                @Override
                public void onFailure(int errorCode, String msg) {
                    UploadKeychainService.isUploading = false;
                    LLog.e("调取uploadKeychain HTTP协议时失败! errorCode:" + errorCode + " msg:" + msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            UploadKeychainService.isUploading = false;
        }


        //协议方式
    /*    try {
            LLog.i("开始上传keychain 当前文件: " + fileName);
            final HashMap parms = new HashMap();
            parms.put("message", message.toString());
            PostHttpRequest.getInstance().post(HttpConstants.SEND_HTTP_MESSAGE, parms, new StringCallback() {
                @Override
                public void onResponse(String response, int id) {
                    super.onResponse(response, id);
                    try {
                        if (!TextUtils.isEmpty(response)) {
                            LLog.e("优化 - 接收到消息 message:" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String type = jsonObject.optString("type");
                            if (!TextUtils.isEmpty(type)) {
                                LLog.e("优化 - 接收到消息 type:" + type);

                                String data = jsonObject.optString("data");
                                if (TextUtils.isEmpty(data)) {
                                    UploadKeychainService.isUploading = false;
                                    LLog.e("data为空 response:" + response.toString());
                                    RecordFloatView.updateMessage("data为空 response:" + response.toString());
                                    return;
                                }
                                BReceiveKeychainFileRe bReceiveKeychainFileRe = ScriptApplication.getGson().fromJson(data, BReceiveKeychainFileRe.class);
                                if (bReceiveKeychainFileRe != null) {
                                    if (!TextUtils.isEmpty(bReceiveKeychainFileRe.getRetCode())) {
                                        if (!"0".equals(bReceiveKeychainFileRe.getRetCode())) {
                                            UploadKeychainService.isUploading = false;
                                            LLog.e("服务器给客户端应答上传keychain文件 retCode=" + bReceiveKeychainFileRe.getRetCode());
                                            RecordFloatView.updateMessage("服务器给客户端应答上传keychain文件 retCode=" + bReceiveKeychainFileRe.getRetCode());
                                        } else {
                                            UploadKeychainService.isUploading = false;
                                            LLog.e("调取上传keychain文件成功!");
                                            RecordFloatView.updateMessage("调取上传keychain文件成功!");
                                            FileHelper.deleteFile(fileName);
                                            String keychain = fileName.replace(".cfg", "");
                                            FileHelper.deleteFile(keychain);
                                        }
                                    } else {
                                        UploadKeychainService.isUploading = false;
                                        LLog.e("服务器给客户端应答上传keychain文件 retCode为空!");
                                        RecordFloatView.updateMessage("服务器给客户端应答上传keychain文件 retCode为空!");
                                    }
                                } else {
                                    UploadKeychainService.isUploading = false;
                                    LLog.e("bReceiveKeychainFileRe 为空！");
                                    RecordFloatView.updateMessage("bReceiveKeychainFileRe 为空!");
                                }
                            } else {
                                LLog.e("优化 - 接收到消息 type为NULL！");
                                RecordFloatView.updateMessage("接收消息 message type为NULL！");
                            }
                        } else {
                            UploadKeychainService.isUploading = false;
                            LLog.e("调取HTTP协议response为空!");
                            RecordFloatView.updateMessage("调取HTTP协议response为空!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        UploadKeychainService.isUploading = false;
                        LLog.e("调取uploadKeychain HTTP协议时出现异常!");
                    }
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    super.onError(call, e, id);
                    UploadKeychainService.isUploading = false;
                    LLog.e("调取上传Keychain接口失败!  " + e);
                    RecordFloatView.updateMessage("调取上传Keychain接口失败! " + e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            UploadKeychainService.isUploading = false;
            LLog.e("调取上传Keychain接口异常!");
            RecordFloatView.updateMessage("调取上传Keychain接口异常!");
        }*/
    }


    /**
     * 关闭VPN
     */
    public static void closeVPN() {
        BrushOrderHelper.getInstance().vpnConnected = false;
        if ("芝麻VPN".equals(SpUtil.getKeyString(PlatformConfig.CURRENT_VPN_TYPE, ""))) {
            ZimaVpnUtil.getInstance().closeVpn();
        } else if ("无极VPN".equals(SpUtil.getKeyString(PlatformConfig.CURRENT_VPN_TYPE, ""))) {
            WujiVpnUtil.closeVPN();
        }

        StatisticTrafficManager.getInstance().stoptTimeData();
        StatisticTrafficManager.getInstance().uploadTrafficDate();
    }


    @Override
    public void orderDealFailure(String... args) {
        ScriptApplication.getService().execute(() -> {
            try {
                Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                ScriptUtil.dealWithCapture("截屏#游戏_1");
                SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_GAME);
                ScriptUtil.dealWithKeyValue(ScriptConstants.KEYVALUE_SCRIPT + ScriptConstants.SPLIT + KeyCode.KEYCODE_HOME);
                SystemClock.sleep(SleepConfig.SLEEP_TIME_2000);
                AppUtil.killApp(SpUtil.getKeyString(PlatformConfig.NEED_KILL_PACKAGENAME, ""));
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL);
                if (args != null && args.length > 0) {
                    if (args.length == 1) {
                        FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误 : \n" + args[0] + ": \n\n");
                    } else if (args.length == 2) {
                        FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误 : \n" + args[0] + " :  " + args[1] + ": \n\n");
                    }
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, args[0]);
                }
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void orderDealFailure(String orderType) {
        ScriptApplication.getService().execute(() -> {
            Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
            ScriptUtil.dealWithKeyValue(ScriptConstants.KEYVALUE_SCRIPT + ScriptConstants.SPLIT + KeyCode.KEYCODE_HOME);
            SystemClock.sleep(SleepConfig.SLEEP_TIME_2000);
            AppUtil.killApp(SpUtil.getKeyString(PlatformConfig.NEED_KILL_PACKAGENAME, ""));

            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL);
            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, orderType);
//            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, orderType);
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
        });
    }
}

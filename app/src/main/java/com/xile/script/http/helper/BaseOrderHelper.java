package com.xile.script.http.helper;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.cutimage.CutImageActivity;
import com.xile.script.base.ui.view.floatview.BaseFloatView;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.CommentInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.http.helper.manager.mina.CaptureConfig;
import com.xile.script.http.helper.manager.mina.NioTask;
import com.xile.script.imagereact.CaptureUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.script.ExecuteUtil;
import com.xile.script.utils.script.PlatformUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import script.tools.config.ScriptConstants;


/**
 * date: 2017/5/22 19:00
 *
 * @scene 订单处理工具类
 */
public abstract class BaseOrderHelper {


    /**
     * 截取图片名字
     *
     * @param urls
     * @return
     */
    public List<String> getImageName(List<String> urls) {
        List<String> names = new ArrayList<>();
        if (urls != null && urls.size() > 0) {
            try {
                for (int i = 0; i < urls.size(); i++) {
                    String url = urls.get(i);
                    url = url.replace("\\", "/");
                    String name = url.substring(url.lastIndexOf("/") + 1);
                    url = url.replace(" ", "");
                    name = name.replace(" ", "");
                    names.add(name);
                    downloadImgs(url, Constants.SCRIPT_FOLDER_ROOT_POP_IMG_PATH + "/" + name);//下载图片
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return names;
    }


    /**
     * 下载图片
     *
     * @param url
     * @param saveName
     */
    public void downloadImgs(final String url, String saveName) {
        try {
            long time = System.currentTimeMillis();
            LLog.e(saveName);
            HttpRequest.download(url, new File(saveName), new FileDownloadCallback() {
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
                    LLog.e("图片下载失败!图片下载地址：" + url);
                    RecordFloatView.updateMessage("图片下载失败!");
                }

                //下载完成（下载成功）
                @Override
                public void onDone() {
                    super.onDone();
                    BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                    LLog.e("图片下载成功!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("图片下载异常!图片下载地址：" + url);
            RecordFloatView.updateMessage("图片下载异常!");
        }
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
                        orderDealFailure("脚本下载失败!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                    }

                    //下载完成（下载成功）
                    @Override
                    public void onDone() {
                        super.onDone();
                        LLog.e("脚本下载成功!");
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        RecordFloatView.updateMessage("脚本下载成功!");
                        //开始执行脚本
                        SystemClock.sleep(2000);
                        List<String> scripts = FileHelper.readFile(Constants.SCRIPT_FOLDER_PATH + name);
                        if (scripts != null && scripts.size() > 0) {
                            ExecuteUtil.execServerScript(1, scripts);
                        } else {
                            //订单处理失败
                            LLog.e("脚本内容为空333!");
                            FileHelper.copyFile(Constants.SCRIPT_FOLDER_PATH + name,Constants.SCRIPT_FOLDER_LOCAL_COMMENT_PATH+"333_"+name);
                            RecordFloatView.updateMessage("脚本内容为空!");
                            orderDealFailure("脚本内容为空333!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                        }
                    }
                });
            } else {
                //订单处理失败
                LLog.e("脚本链接为空!");
                RecordFloatView.updateMessage("脚本链接为空!");
                orderDealFailure("脚本链接为空!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("处理脚本异常!");
            RecordFloatView.updateMessage("处理脚本异常!");
            orderDealFailure("处理脚本异常!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");

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
                url = url.replace("\\", "/");
                    String name = url.substring(url.lastIndexOf("/") + 1);
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
                        LLog.e("评论话库下载失败!");
                        RecordFloatView.updateMessage("评论话库下载失败!");
                        orderDealFailure("评论话库下载失败!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                    }

                    //下载完成（下载成功）
                    @Override
                    public void onDone() {
                        super.onDone();
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
            orderDealFailure("处理评论话库异常!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
        }
    }

    /**
     * 下载文件 - keychain
     *
     * @param url
     */
    public void downloadFile(String url) {
        try {
            long time = System.currentTimeMillis();
            if (!TextUtils.isEmpty(url)) {
                url = url.replace("\\", "/");
                    String name = url.substring(url.lastIndexOf("/") + 1);
                File file = new File(Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_PATH + name);
                LLog.i(Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_PATH + name);
                HttpRequest.download(url, file, new FileDownloadCallback() {
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
                        LLog.e("keychain 文件下载失败!");
                        RecordFloatView.updateMessage("keychain 文件下载失败!");
                        orderDealFailure("keychain 文件下载失败!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                    }

                    //下载完成（下载成功）
                    @Override
                    public void onDone() {
                        super.onDone();
                        BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                        LLog.e("keychain 文件下载成功!");
                        RecordFloatView.updateMessage("keychain 文件下载成功!");
                    }
                });
            } else {
                //订单处理失败
                BrushTask.downLoadFileTime.add(System.currentTimeMillis() - time);
                LLog.e("keychain链接为空!");
                RecordFloatView.updateMessage("keychain链接为空!");
                orderDealFailure("keychain链接为空!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("处理keychain下载异常!");
            RecordFloatView.updateMessage("处理keychain下载异常!");
            orderDealFailure("处理keychain下载异常!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");

        }
    }


    public abstract void orderDealFailure(String... args);

    public abstract void orderDealFailure(String orderType);


    /**
     * 警报
     *
     * @param alertType 警报类型
     */

    public static void callAlertAndFinish(String alertType, String alertPicName) {
        //退出账号
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
            SystemClock.sleep(350);
            String path = Constants.SCRIPT_TEMP_ALERT_CAPTURE_PATH + "/" + Constants.sGamesOrderInfo.getTaskData() + "_" + SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow")
                    + "_" + alertType + "_" + alertPicName + "_" + System.currentTimeMillis() + ".png";
            CaptureUtil.takeScreen(ScriptApplication.getInstance(), path, false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
            //本地存全清图 方便纠正
            /*String local_path = Constants.SCRIPT_TEMP_ALERT_CLEAR_CAPTURE_PATH + "/" + "全清_" + SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow")
                    + "_" + alertType + "_" + alertPicName + "_" + System.currentTimeMillis() + ".png";
            CaptureUtil.takeScreen_Play(ScriptApplication.getInstance(), local_path, false, Bitmap.CompressFormat.PNG, CutImageActivity.class);*/
            SpUtil.putKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, path);
            BaseFloatView.sFloatHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).show(0, 0);
                }
            }, 100);
            PlatformUtil.quitOut();
        }
        SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);
        SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, true);
        SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, alertType);
        RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();//终止脚本
    }

    /**
     * 警报并暂停 适用于人工协助
     *
     * @param alertType 警报类型
     */

    public static void callAlertAndPause(String alertType, boolean caputre) {
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            if (caputre) {
                RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
                SystemClock.sleep(350);
                String path = Constants.SCRIPT_TEMP_ALERT_CAPTURE_PATH + "/" + Constants.sGamesOrderInfo.getTaskData() + "_" + SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow")
                        + "_" + alertType + "_人工协助_" + System.currentTimeMillis() + ".jpg";
                CaptureUtil.takeScreen(ScriptApplication.getInstance(), path, false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
                SpUtil.putKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, path);
                BaseFloatView.sFloatHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecordFloatView.getInstance(ScriptApplication.getInstance()).show(0, 0);
                    }
                }, 100);
            }
            Constants.currentRobotState = CaptureConfig.ROBOT_IS_WAITING_ASSISTANCE;
            SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, alertType);
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 1000);
        } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
            if (caputre) {
                RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
                SystemClock.sleep(350);
                String path = Constants.SCRIPT_TEMP_ALERT_CAPTURE_PATH + "/" + Constants.sBrushOrderInfo.getUserId() + "_" + SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow")
                        + "_人工协助_" + System.currentTimeMillis() + ".jpg";
                CaptureUtil.takeScreen(ScriptApplication.getInstance(), path, false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
                SpUtil.putKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, path);
                BaseFloatView.sFloatHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecordFloatView.getInstance(ScriptApplication.getInstance()).show(0, 0);
                    }
                }, 100);
            }
            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, alertType);
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_CALL_ALERT, 1000);
        }
    }


    /**
     * 重置一些状态值
     */
    public static void resetData() {
        SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, false);
        SpUtil.putKeyBoolean(PlatformConfig.ISFREE, true);
        SpUtil.putKeyBoolean(PlatformConfig.NEED_CHECK_ACTIVE, false);
        SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);
        SpUtil.getKeyString(PlatformConfig.NEED_KILL_PACKAGENAME, "");
        SpUtil.putKeyLong(PlatformConfig.ACTIVE_TIME, 0);
        SpUtil.putKeyString(PlatformConfig.CURRENT_ALERT_PIC_PATH, "");
        SpUtil.putKeyString(PlatformConfig.CURRENT_NAME, "");
        SpUtil.putKeyString(PlatformConfig.CURRENT_ID_CARD, "");
        SpUtil.putKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, "");
        SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, "");
        Constants.currentRobotState = CaptureConfig.ROBOT_IS_FREE;
        Constants.sGamesOrderInfo = null;
        Constants.sBrushOrderInfo = null;
        if (ScriptConstants.PLATFORM_MANAGER.equals(Constants.currentPlatform)) {
            NioTask.getInstance().reset();
        } else if (ScriptConstants.PLATFORM_BRUSH.equals(Constants.currentPlatform)) {
            BrushTask.getInstance().reset();
        }
    }


}

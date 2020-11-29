package com.xile.script.utils.game;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;

import com.chenyang.lloglib.LLog;
import com.google.gson.JsonSyntaxException;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.GameConfig;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.SleepConfig;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.manager.bean.OCRinfo;
import com.xile.script.http.helper.manager.mina.AlertConfig;
import com.xile.script.http.helper.manager.mina.NioTask;
import com.xile.script.imagereact.CaptureUtil;
import com.xile.script.imagereact.ScreenShotFb;
import com.xile.script.utils.InstructUtil;
import com.xile.script.utils.ScreenUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.ReorganizeUitl;
import com.xile.script.utils.script.SocketUtil;

import java.util.ArrayList;
import java.util.List;

import script.tools.config.ScriptConstants;

/**
 * date: 2019/3/31 18:56
 */
public class InsertAreaRoleUtil {
    private static InsertAreaRoleUtil sInsertAreaRoleUtil;
    public final String UPLOAD_LARGE_AREA = "1"; //上传大区图片
    public final String UPLOAD_SMALL_AREA = "2"; //上传小区图片
    public final String UPLOAD_ROLE = "3"; //上传角色图片
    public final String REQUEST_CODE = "4"; // 请求手机验证码
    public String lastArea;
    public int checkCount;
    public String verifyCode;

    public static synchronized InsertAreaRoleUtil getInstance() {
        if (sInsertAreaRoleUtil == null) {
            sInsertAreaRoleUtil = new InsertAreaRoleUtil();
        }
        return sInsertAreaRoleUtil;
    }

    public void reset() {
        checkCount = 0;
        lastArea = "";
    }

    /**
     * 区服/角色界面截图传给服务器
     */
    public void dealWithOcrInit(String reqType) {
        //删除缓存图片
        FileHelper.deleteFile(Constants.SCRIPT_FOLDER_TEMP_AREA_UPLOAD_PATH);
        FileHelper.deleteFile(Constants.SCRIPT_FOLDER_TEMP_ROLE_UPLOAD_PATH);
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            if (UPLOAD_LARGE_AREA.equals(reqType)) {
                //大区截图区域
                String game_large_area_shot = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_LARGE_AREA_SHOT);
                dealWithShot(game_large_area_shot, Constants.SCRIPT_FOLDER_TEMP_AREA_UPLOAD_PATH);
                SpUtil.putKeyString(PlatformConfig.CURRENT_OCR_TYPE, UPLOAD_LARGE_AREA);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, SleepConfig.SLEEP_TIME_2000);
            } else if (UPLOAD_SMALL_AREA.equals(reqType)) {
                //小区截图区域
                String game_small_area_shot = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_SMALL_AREA_SHOT);
                dealWithShot(game_small_area_shot, Constants.SCRIPT_FOLDER_TEMP_AREA_UPLOAD_PATH);
                SpUtil.putKeyString(PlatformConfig.CURRENT_OCR_TYPE, UPLOAD_SMALL_AREA);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, SleepConfig.SLEEP_TIME_2000);
            } else if (UPLOAD_ROLE.equals(reqType)) {
                //角色截图区域
                String game_role_shot = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_ROLE_SHOT);
                dealWithShot(game_role_shot, Constants.SCRIPT_FOLDER_TEMP_ROLE_UPLOAD_PATH);
                SpUtil.putKeyString(PlatformConfig.CURRENT_OCR_TYPE, UPLOAD_ROLE);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_UPLOAD_SINGLE_IMG, SleepConfig.SLEEP_TIME_2000);
            } else if (REQUEST_CODE.equals(reqType)) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_OCR_TYPE, REQUEST_CODE);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, SleepConfig.SLEEP_TIME_2000);
            }
        }
    }

    /**
     * 截图区域处理
     *
     * @param shotStr
     */
    private void dealWithShot(String shotStr, String savePath) {
        try {
            int screenWidth = ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[0];
            int screenHeight = ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1];
            String path = savePath.replace(".png", "") + "_" + screenWidth + "x" + screenHeight + ".png";
            String[] datas = shotStr.split(ScriptConstants.CMD_SPLIT);
            int beginX = Integer.parseInt(datas[0]);
            int beginY = Integer.parseInt(datas[1]);
            int endX = Integer.parseInt(datas[2]);
            int endY = Integer.parseInt(datas[3]);
            Bitmap bitmap = CaptureUtil.getBitMapBorder_clear(ScriptApplication.getInstance(), beginX, beginY, endX, endY);
            if (bitmap != null) {
                ScreenShotFb.getInstance(ScriptApplication.getInstance()).saveBitmap(bitmap, path, Bitmap.CompressFormat.PNG, false, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("请检查脚本中区服截图区域配置与角色截图区域配置是否有误!!!");
            RecordFloatView.updateMessage("请检查脚本中区服截图区域配置与角色截图区域配置是否有误!!!");
        }
    }


    /**
     * 截图区域坐标还原
     *
     * @param shotStr
     * @param shotX
     * @param shotY
     */
    private String dealWithShotRestore(String shotStr, int shotX, int shotY) {
        try {
            String[] datas = shotStr.split(ScriptConstants.CMD_SPLIT);
            int beginX = Integer.parseInt(datas[0]);
            int beginY = Integer.parseInt(datas[1]);
            String tapString = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + (shotX + beginX) + ScriptConstants.SPLIT + (beginY + shotY);
            return tapString;
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("请检查脚本中区服截图区域配置与角色截图区域配置是否有误!!!");
            RecordFloatView.updateMessage("请检查脚本中区服截图区域配置与角色截图区域配置是否有误!!!");
        }
        return "";
    }

    /**
     * 处理OCR结果
     *
     * @param ocrResult
     * @param askType
     */
    public void dealWithAOcr(String ocrResult, String askType) {
        if (!REQUEST_CODE.equals(askType)) {  //大区/小区/角色 识别
            try {
                OCRinfo ocRinfo = ScriptApplication.getGson().fromJson(ocrResult, OCRinfo.class);
                if (ocRinfo == null) {
                    LLog.e("处理OCR识别OCRinfo为空！");
                    RecordFloatView.updateMessage("处理OCR识别OCRinfo为空！");
                    NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                    return;
                }
                List<OCRinfo.DataBean> data = ocRinfo.getData();
                if (data == null || data.size() == 0) {
                    LLog.e("处理OCR识别OCRinfo.data为空！");
                    RecordFloatView.updateMessage("处理OCR识别OCRinfo.data为空！");
                    if (UPLOAD_LARGE_AREA.equals(askType) || UPLOAD_SMALL_AREA.equals(askType)) {
                        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
                        return;
                    }
                }
                if (UPLOAD_LARGE_AREA.equals(askType)) {
                    dealWithLargeArea(data);
                } else if (UPLOAD_SMALL_AREA.equals(askType)) {
                    dealWithSmallArea(data);
                } else if (UPLOAD_ROLE.equals(askType)) {
                    dealWithRole(data);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                LLog.e("处理区服识别异常 ocrResult:" + ocrResult);
                RecordFloatView.updateMessage("处理区服识别异常 ocrResult:" + ocrResult);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            }
        } else {  //获取短信验证码
            dealWithVerifyCode(ocrResult);
        }
    }

    /**
     * 处理大区服识别
     *
     * @param data
     */
    private void dealWithLargeArea(List<OCRinfo.DataBean> data) {
        ScriptApplication.getService().execute(() -> {
            try {
                String largeAreaFromServer = SpUtil.getKeyString(PlatformConfig.CURRENT_LARGE_AREA_NUMBER, "");//服务器给的大区服
                LLog.e("当前要识别的大区服:" + largeAreaFromServer);
                String area_swipe_cmd = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_LARGE_AREA_SWIPE_COMMAND);//大区滑动指令
                String tempArea = "";
                for (OCRinfo.DataBean dataBean : data) {
                    if (dataBean != null) {
                        String areaName = dataBean.getName();
                        tempArea = areaName;
                        if (!TextUtils.isEmpty(areaName)) {
                            if (areaName.replace("区", "").equals(largeAreaFromServer.replace("区", ""))) {
                                LLog.d("识别到了指定的大区服:" + areaName);
                                RecordFloatView.updateMessage("识别到了指定的大区服:" + areaName);
                                String game_large_area_shot = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_LARGE_AREA_SHOT);
                                String tapString = dealWithShotRestore(game_large_area_shot, dataBean.getPointX(), dataBean.getPointY());
                                if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏坐标转竖屏坐标重组指令
                                    tapString = ReorganizeUitl.landscapeToPortrait(tapString);
                                }
                                SocketUtil.sendInstruct(tapString, false);
                                SystemClock.sleep(SleepConfig.SLEEP_TIME_5000);
                                //识别小区服
                                dealWithOcrInit(UPLOAD_SMALL_AREA);
                                return;
                            }
                        } else {
                            LLog.e("OCR识别大区服出现空，报警");
                            RecordFloatView.updateMessage("OCR识别大区服出现空，报警!");
                            BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_REDO_ORC_SERVERID_ERROR, "OCR识别大区服出现空_NULL");
                            return;
                        }
                    }
                }
                if (tempArea.equals(lastArea)) {
                    checkCount++;
                } else {
                    lastArea = tempArea;
                }
                if (checkCount > 3) { //滑到最底端三次校验卡屏
                    reset();
                    LLog.e("OCR识别区服失败，检测到卡屏!");
                    RecordFloatView.updateMessage("OCR识别区服失败，检测到卡屏!");
                    BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_REDO_ORC_SERVERID_ERROR, "OCR识别区服失败，检测到卡屏_NULL");
                    return;
                }
                LLog.e("当前未识别到指定大区,执行滑动动作！");
                if (TextUtils.isEmpty(area_swipe_cmd)) {
                    LLog.e("请检查脚本是否配置了大区滑动指令！！！");
                    return;
                }
                area_swipe_cmd = InstructUtil.script2Cmd(area_swipe_cmd);
                SocketUtil.sendInstruct(area_swipe_cmd, false);
                SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
                dealWithOcrInit(UPLOAD_LARGE_AREA);
            } catch (Exception e) {
                e.printStackTrace();
                LLog.e("处理区服识别异常 e:" + e);
                RecordFloatView.updateMessage("处理区服识别异常 e:" + e);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            }
        });
    }


    /**
     * 处理小区服识别
     *
     * @param data
     */
    private void dealWithSmallArea(List<OCRinfo.DataBean> data) {
        ScriptApplication.getService().execute(() -> {
            try {
                String smallAreaFromServer = SpUtil.getKeyString(PlatformConfig.CURRENT_SMALL_AREA_NUMBER, "");//服务器给的小区服
                LLog.e("当前要识别的小区服:" + smallAreaFromServer);
                for (OCRinfo.DataBean dataBean : data) {
                    String smallAreaName = dataBean.getName();
                    if (!TextUtils.isEmpty(smallAreaName)) {
                        if (smallAreaName.replace("区", "").equals(smallAreaFromServer.replace("区", "").replace("互通", ""))) {
                            LLog.d("识别到了指定的小区服:" + smallAreaName);
                            RecordFloatView.updateMessage("识别到了指定的小区服:" + smallAreaName);
                            String game_small_area_shot = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_SMALL_AREA_SHOT);
                            String tapString = dealWithShotRestore(game_small_area_shot, dataBean.getPointX(), dataBean.getPointY());
                            if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏坐标转竖屏坐标重组指令
                                tapString = ReorganizeUitl.landscapeToPortrait(tapString);
                            }
                            SocketUtil.sendInstruct(tapString, false);
                            SystemClock.sleep(SleepConfig.SLEEP_TIME_2000);
                            SocketUtil.continueExec();
                            return;
                        }
                    }
                }
                LLog.e("未识别到指定小区服,报警!");
                RecordFloatView.updateMessage("未识别到指定小区服,报警!");
                BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_REDO_ORC_SERVERID_ERROR, "未识别到指定小区服_NULL");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                LLog.e("处理小区服识别异常 e:" + e);
                RecordFloatView.updateMessage("处理小区服识别异常 e:" + e);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            }
        });
    }

    /**
     * 处理角色识别
     *
     * @param data
     */
    private void dealWithRole(List<OCRinfo.DataBean> data) {
        ScriptApplication.getService().execute(() -> {
            try {
                boolean isNoRole = true;  //默认没有角色
                int realLevel = 0;  //初始化真实等级
                int serverLevel = Integer.parseInt(SpUtil.getKeyString(PlatformConfig.CURRENT_ROLE_LEVEL, ""));//设定角色等级
                LLog.e("当前要识别的角色等级:" + serverLevel);
                List<String> instructs = new ArrayList<>();//检测符合角色等级的有几个(>=)
                for (OCRinfo.DataBean dataBean : data) {
                    if (dataBean != null) {
                        int tempLevel = -1;
                        String roleName = dataBean.getName();
                        if (!StringUtil.isEmpty(roleName)) {
                            isNoRole = false;  //有角色
                            try {
                                tempLevel = Integer.parseInt(StringUtil.getNumber(roleName));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (tempLevel >= serverLevel) {  //符合要求的条目  大于等于服务器等级
                            realLevel = tempLevel;
                            String game_role_shot = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_ROLE_SHOT);
                            String tapRoleStr = dealWithShotRestore(game_role_shot, dataBean.getPointX(), dataBean.getPointY());
                            instructs.add(tapRoleStr);
                        }
                    }
                }
                if (instructs.size() >= 2) {  //有多个符合的角色等级 （（两个或两个以上）的角色等级大于服务器返给的等级）
                    BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_RECONFIRM_MORE_ROLE, "多角色_NULL");
                    return;
                } else if (instructs.size() == 1) {
                    LLog.i("识别到符合的角色等级:" + realLevel + "  服务器等级:" + serverLevel);
                    RecordFloatView.updateMessage("识别到符合的角色等级:" + realLevel + "  服务器等级:" + serverLevel);
                    String tapRoleStr = instructs.get(0);
                    if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏坐标转竖屏坐标重组指令
                        tapRoleStr = ReorganizeUitl.landscapeToPortrait(tapRoleStr);
                    }
                    SocketUtil.sendInstruct(tapRoleStr, false);
                    SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
                    SocketUtil.continueExec();
                } else {  //没有符合的角色等级（游戏所有的角色 都小于服务器返给的等级）
                    if (isNoRole) {
                        BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_ABOLISH_SEVERWRITE_ERROR, "没有角色_NULL");
                    } else {
                        BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_ABOLISH_NONE_ROLE, "无符合等级的角色_NULL");
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LLog.e("处理角色识别异常 e:" + e);
                RecordFloatView.updateMessage("处理角色识别异常 e:" + e);
                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_REQUEST_SERVER_RESULT, 10000);
            }
        });
    }
 
   /**
     * 处理短信验证码
     *
     * @param ocrResult
     */
    private void dealWithVerifyCode(String ocrResult) {
        LLog.e("当前手机验证码:" + ocrResult);
        RecordFloatView.updateMessage("当前手机验证码:" + ocrResult);
        verifyCode = ocrResult;
        SocketUtil.continueExec();
    }

}

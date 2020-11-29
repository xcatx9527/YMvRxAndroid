package com.xile.script.utils.script;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.algorithm.templatematch.Util;
import com.algorithm.templatematch.bean.SlidingPoint;
import com.algorithm.templatematch.bean.TmpMatchResult;
import com.alibaba.fastjson.JSON;
import com.chenyang.lloglib.LLog;
import com.google.gson.JsonSyntaxException;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.promptdialog.PopupDialog;
import com.xile.script.base.ui.promptdialog.PromptDialog;
import com.xile.script.base.ui.view.cutimage.CutImageActivity;
import com.xile.script.base.ui.view.floatview.BaseFloatView;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.AppInfo;
import com.xile.script.bean.CommentInfo;
import com.xile.script.bean.ContantInfo;
import com.xile.script.bean.DeviceInfo;
import com.xile.script.bean.PopupInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.ExecEnum;
import com.xile.script.config.GameConfig;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.config.SleepConfig;
import com.xile.script.http.common.GetHttpRequest;
import com.xile.script.http.common.HttpConstants;
import com.xile.script.http.common.JsonCallback;
import com.xile.script.http.common.StringCallback;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.brush.bean.BSendKeychainFile;
import com.xile.script.http.helper.brush.bean.BrushOrderInfo;
import com.xile.script.http.helper.brush.mina.BAlertConfig;
import com.xile.script.http.helper.brush.mina.BOrderCaptureConfig;
import com.xile.script.http.helper.brush.mina.BOrderStateConfig;
import com.xile.script.http.helper.brush.mina.BOrderTypeConfig;
import com.xile.script.http.helper.brush.mina.BrushHelper;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.http.helper.manager.mina.AlertConfig;
import com.xile.script.http.helper.manager.mina.NioTask;
import com.xile.script.http.helper.other.MobileRegisterHelper;
import com.xile.script.http.helper.other.UploadFileHelper;
import com.xile.script.imagereact.CaptureUtil;
import com.xile.script.imagereact.ImageIdentifyUtil;
import com.xile.script.imagereact.ScreenShotFb;
import com.xile.script.imagereact.TensorFlowUtils;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.BitmapUtil;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.InstructUtil;
import com.xile.script.utils.SortListUtil;
import com.xile.script.utils.TimeUtil;
import com.xile.script.utils.captureX.GetPurplesTask;
import com.xile.script.utils.common.AccountUtil;
import com.xile.script.utils.common.ApkOperateManager;
import com.xile.script.utils.common.CacheUtil;
import com.xile.script.utils.common.DeviceUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.ImageUtil;
import com.xile.script.utils.common.MD5Util;
import com.xile.script.utils.common.RandomUtil;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.common.ZipUtil;
import com.xile.script.utils.common.ZipUtils;
import com.xile.script.utils.game.InsertAreaRoleUtil;
import com.yzy.example.BuildConfig;
import com.yzy.example.R;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Call;
import script.tools.config.DeviceConfig;
import script.tools.config.KeyCode;
import script.tools.config.ScriptConstants;

import static script.tools.config.LogConfig.LOG_TAG;


/**
 * @scene 脚本工具类
 */

public class ScriptUtil {

    /**
     * 游戏配置处理
     *
     * @param instructStr
     */
    public static void dealWithGameConfig(String instructStr) {
        try {
            if (!StringUtil.isEmpty(instructStr)) {
                String[] strs = instructStr.split(ScriptConstants.SPLIT);
                if (strs.length > 1) {
                    if (instructStr.contains(GameConfig.AREA_ORDER)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_AREA_ORDER, strs[2]);
                    } else if (instructStr.contains(GameConfig.PHONE_MAIL)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_PHONE_MAIL, strs[2]);
                    } else if (instructStr.contains(GameConfig.ORDER_MIN_TIME)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_ORDER_MIN_TIME);
                    } else if (instructStr.contains(GameConfig.PHONE_NUMBER_SOURCE)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_PHONE_NUMBER_SOURCE, strs[2]);
                    } else if (instructStr.contains(GameConfig.PHONE_VERIFY_CODE_PROJECT_ID)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_PHONE_VERIFY_CODE_PROJECT_ID);
                    } else if (instructStr.contains(GameConfig.PHONE_VERIFY_CODE_OPERATOR)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_PHONE_VERIFY_CODE_OPERATOR);
                    } else if (instructStr.contains(GameConfig.PHONE_VERIFY_CODE_NOTPREFIX)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_PHONE_VERIFY_CODE_NOTPREFIX);
                    } else if (instructStr.contains(GameConfig.PHONE_VERIFY_CODE_INFO_FIRST)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_PHONE_VERIFY_CODE_INFO_FIRST);
                    } else if (instructStr.contains(GameConfig.PHONE_VERIFY_CODE_INFO_SECOND)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_PHONE_VERIFY_CODE_INFO_SECOND);
                    } else if (instructStr.contains(GameConfig.PHONE_VERIFY_CODE_INFO_THIRD)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_PHONE_VERIFY_CODE_INFO_THIRD);
                    } else if (instructStr.contains(GameConfig.CUT_PHONE_VERIFY_CODE)) { //截取手机号验证码
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_CUT_PHONE_VERIFY_CODE);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_DIRECT)) {  //大区方向
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LARGE_AREA_DIRECT, strs[2]);
                    } else if (instructStr.contains(GameConfig.SMALL_AREA_DIRECT)) {  //小区方向
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_SMALL_AREA_DIRECT, strs[2]);
                    } else if (instructStr.contains(GameConfig.AREA_TEXT_TO_REMOVE)) {  //小区方向
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_AREA_TEXT_TO_REMOVE, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_SWIPE_COMMAND)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LARGE_AREA_SWIPE_COMMAND, instructStr.substring(instructStr.indexOf(ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT)));
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_SMALL_FRAME)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_LARGE_AREA_SMALL_FRAME);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_LARGE_FRAME)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_LARGE_AREA_LARGE_FRAME);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_TEXT_FRAME)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_LARGE_AREA_TEXT_FRAME);
                    } else if (instructStr.contains(GameConfig.SMALL_AREA_SMALL_FRAME)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_SMALL_AREA_SMALL_FRAME);
                    } else if (instructStr.contains(GameConfig.SMALL_AREA_LARGE_FRAME)) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_SMALL_AREA_LARGE_FRAME);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "1")
                            && !instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "10")
                            && !instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "11")
                            && !instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "12")
                            && !instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "13")
                            && !instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "14")
                            && !instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "15")
                            && !instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "16")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_1);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "2")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_2);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "3")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_3);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "4")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_4);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "5")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_5);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "6")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_6);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "7")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_7);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "8")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_8);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "9")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_9);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_TEXT_FRAME + "10")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_TINNY_AREA_TEXT_FRAME_10);
                    } else if (instructStr.contains(GameConfig.ROLE_FRAME + "1")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_ROLE_FRAME_1);
                    } else if (instructStr.contains(GameConfig.ROLE_FRAME + "2")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_ROLE_FRAME_2);
                    } else if (instructStr.contains(GameConfig.ROLE_FRAME + "3")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_ROLE_FRAME_3);
                    } else if (instructStr.contains(GameConfig.ROLE_FRAME + "4")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_ROLE_FRAME_4);
                    } else if (instructStr.contains(GameConfig.ROLE_FRAME + "5")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_ROLE_FRAME_5);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "1")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_1);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "2")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_2);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "3")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_3);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "4")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_4);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "5")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_5);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "6")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_6);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "7")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_7);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "8")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_8);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "9")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_9);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "0")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_0);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "退格")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_BACK);
                    } else if (instructStr.contains(GameConfig.KEYBOARD + "确定")) {
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_KEYBOARD_SURE);
                    } else if (instructStr.contains(GameConfig.KEYBOARD_COMPARE_PIC)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_KEYBOARD_COMPARE_PIC, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_WHITE_LIST)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LARGE_AREA_WHITE_LIST, strs[2]);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_WHITE_LIST)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_TINNY_AREA_WHITE_LIST, strs[2]);
                    } else if (instructStr.contains(GameConfig.LEVEL_WHITE_LIST)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LEVEL_WHITE_LIST, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_INTERVAL_COUNT)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LARGE_AREA_INTERVAL_COUNT, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_TEXT_ERROR_MARK)) { //大区文字识别纠错字符
                        splitData(instructStr, ScriptConstants.SPLIT, GameConfig.GAME_LARGE_AREA_TEXT_ERROR_MARK);
                    } else if (instructStr.contains(GameConfig.SMALL_AREA_TEXT_ERROR_MARK)) { //小区文字识别纠错字符
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_SMALL_AREA_TEXT_ERROR_MARK, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_DIFFRENT_VALUE)) { //大区可识别差值
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LARGE_AREA_DIFFRENT_VALUE, strs[2]);
                    } else if (instructStr.contains(GameConfig.SMALL_AREA_DIFFRENT_VALUE)) { //小区可识别差值
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_SMALL_AREA_DIFFRENT_VALUE, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_SHOT)) { //大区截图区域
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LARGE_AREA_SHOT, strs[2]);
                    } else if (instructStr.contains(GameConfig.SMALL_AREA_SHOT)) { //小区截图区域
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_SMALL_AREA_SHOT, strs[2]);
                    } else if (instructStr.contains(GameConfig.ROLE_SHOT)) { //角色截图区域
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_ROLE_SHOT, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_INTERVAL_MARK)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_INTERVAL_MARK, strs[2]);
                    } else if (instructStr.contains(GameConfig.ROLE_CORRECT_WORD)) {//角色文字识别纠错字符
                        splitData(instructStr, ScriptConstants.SPLIT, GameConfig.GAME_ROLE_CORRECT_WORD);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_RGB)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LARGE_AREA_RGB, strs[2]);
                    } else if (instructStr.contains(GameConfig.TINNY_AREA_RGB)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_TINNY_AREA_RGB, strs[2]);
                    } else if (instructStr.contains(GameConfig.LEVEL_RGB)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_LEVEL_RGB, strs[2]);
                    } else if (instructStr.contains(GameConfig.LARGE_AREA_TAG_SCREENSHOT)) {  //大区标识截图区域
                        splitData(strs[2], ScriptConstants.CMD_SPLIT, GameConfig.GAME_LARGE_AREA_TAG_SCREENSHOT);
                    } else if (instructStr.contains(GameConfig.SMALL_AREA_TAG_SCREENSHOT)) {  //小区标识截图区域
                        splitData(strs[2], ScriptConstants.CMD_SPLIT, GameConfig.GAME_SMALL_AREA_TAG_SCREENSHOT);
                    } else if (instructStr.contains(GameConfig.ALERT_QUIT)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_ALERT_QUIT, strs[2]);
                    } else if (instructStr.contains(GameConfig.FINISH_DELAY_STRING)) {
                        GameConfig.getAreaAndRoleConfigs().put(GameConfig.GAME_FINISH_DELAY, strs[2]);
                    } else if (instructStr.contains(GameConfig.ACCOUNT_RULE)) {//账号注册规则配置
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length >= 4) {
                            initPercent(datas[2], Integer.parseInt(datas[3]));
                        }
                    } else if (instructStr.contains(GameConfig.ACCOUNT_DIGIT_RULE)) {//配置#账号注册位数规则#6#15
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length >= 4) {
                            Constants.ACCOUNT_MIN = Integer.parseInt(datas[2]);
                            Constants.ACCOUNT_MAX = Integer.parseInt(datas[3]);
                        }
                    } else if (instructStr.contains(GameConfig.DEVICE_TYPE)) {//设备机型配置
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length >= 4) {
                            DeviceConfig.deviceConfigMap.put(datas[2], Integer.parseInt(datas[3]));
                        }
                    } else if (instructStr.contains(GameConfig.DEVICE_RANDOM_PARAM)) {//设备参数随机产生
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length >= 3) {
                            DeviceConfig.deviceRandomParam = "是".equals(datas[2]);
                        }
                    } else if (instructStr.contains(GameConfig.OWNER_POKER_AREA)) {  //己方牌面区域
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_OWNER_POKER_AREA);
                    } else if (instructStr.contains(GameConfig.ABAVE_POKER_AREA)) {  //上家出牌区域
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_ABAVE_POKER_AREA);
                    } else if (instructStr.contains(GameConfig.BELOW_POKER_AREA)) {     //下架出牌区域
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_BELOW_POKER_AREA);
                    } else if (instructStr.contains(GameConfig.DRAW_BUTTON_COORDINATE)) { //出牌按钮坐标
                        splitData(strs[2], GameConfig.SPLIT, GameConfig.GAME_DRAW_BUTTON_COORDINATE);
                    } else if (instructStr.contains(GameConfig.VPN_CITY_LISTS)) { // VPN城市列表
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        LLog.e("vpn城市列表======： " + ZimaVpnUtil.getInstance().sCities.toString());
                        if (datas.length >= 3) {
                            String VPNCityStr = datas[2];   //南京-徐州-合肥-德阳-镇江-抚州
                            String[] cityData = VPNCityStr.split(ScriptConstants.PNG_SPACE);
                            ZimaVpnUtil.getInstance().sCities.clear();
                            ZimaVpnUtil.getInstance().initCity();
                            ConcurrentHashMap<String, String> newCities = new ConcurrentHashMap<>();
                            String[] city = new String[cityData.length];
                            String[] cityCode = new String[cityData.length];
                            for (int i = 0; i < cityData.length; i++) {
                                city[i] = cityData[i];
                                cityCode[i] = ZimaVpnUtil.getInstance().sCities.get(cityData[i]);
                                newCities.put(cityData[i], cityCode[i]);
                            }
                            ZimaVpnUtil.getInstance().sCities = newCities;
                            ZimaVpnUtil.getInstance().setCity(city, cityCode);
                            LLog.e("新vpn城市列表-------------------------------------------： ");
                            LLog.e("新vpn城市列表======city： " + Arrays.toString(ZimaVpnUtil.getInstance().sCity));
                            LLog.e("新vpn城市列表======cityCode： " + Arrays.toString(ZimaVpnUtil.getInstance().sCityCode));
                            LLog.e("新vpn城市列表======sCities： " + ZimaVpnUtil.getInstance().sCities.toString());
                        }
                    } else if (instructStr.contains(GameConfig.BOOLEAN_EXEXUTE_ORDER_REBOOT)) { // 执行订单是否重启
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            SpUtil.putKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, "是".equals(datas[2]));
                        }
                    } else if (instructStr.contains(GameConfig.BOOLEAN_RECHANGE_PHONE)) { // 是否为充值手机
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            SpUtil.putKeyBoolean(PlatformConfig.CURRENT_BOOLEAN_RECHARGE, "是".equals(datas[2]));
                        }
                    } else if (instructStr.contains(GameConfig.BOOLEAN_ADB_TOUCH)) { // ADB触摸事件
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            SpUtil.putKeyBoolean(PlatformConfig.CURRENT_ADB_CMD, "是".equals(datas[2]));
                        }
                    } else if (instructStr.contains(GameConfig.BOOLEAN_SCREEN_ON)) { // 是否保持屏幕常亮
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            SpUtil.putKeyBoolean(PlatformConfig.CURRENT_SCREEN_ON, "是".equals(datas[2]));
                        }
                    } else if (instructStr.contains(GameConfig.BOOLEAN_CHANGE_API_LEVEL)) { // 是否修改Api Level
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            FileUtil.saveFile("是".equals(datas[2]) ? "true" : "false", Constants.LOCAL_HOOK_API);
                        }
                    } else if (instructStr.contains(GameConfig.BOOLEAN_CHANGE_FINGER)) { // 是否修改指纹信息
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            FileUtil.saveFile("是".equals(datas[2]) ? "true" : "false", Constants.LOCAL_HOOK_FINGER);
                        }
                    } else if (instructStr.contains(GameConfig.ACCESSIBILITY_POP_ID)) { // 无障碍弹窗ID
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            String[] strScriptList = datas[2].split("\\|\\|");
                            if (strScriptList.length > 0) {
                                SocketUtil.accessiblityPopList_ID.addAll(Arrays.asList(strScriptList));
                            }
                        }
                    } else if (instructStr.contains(GameConfig.ACCESSIBILITY_POP_TEXT)) { // 无障碍弹窗文本
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            String[] strScriptList = datas[2].split("\\|\\|");
                            if (strScriptList.length > 0) {
                                SocketUtil.accessiblityPopList_TEXT.addAll(Arrays.asList(strScriptList));
                            }
                        }
                    } else if (instructStr.contains(GameConfig.NEED_KILL_APP_NAME)) { // 无障碍弹窗文本
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length == 3) {
                            String[] strScriptList = datas[2].split("\\|\\|");
                            if (strScriptList.length > 0) {
                                SocketUtil.need_kill_APP_list.addAll(Arrays.asList(strScriptList));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化注册百分比
     *
     * @param rule
     * @param count
     */
    public static void initPercent(String rule, int count) {
        for (int i = 0; i < count; i++) {
            GameConfig.registerPercentList.add(rule);
        }
    }


    /**
     * 拆分并保存游戏配置
     *
     * @param str   待拆分的字符串
     * @param split 分隔符
     * @param key   存储的key
     */
    public static void splitData(String str, String split, String key) {
        if (!StringUtil.isEmpty(str)) {
            String[] datas = str.split(split);
            if (datas.length > 0) {
                GameConfig.getAreaAndRoleConfigs().put(key, datas);
            }
        }
    }


    /**
     * 应用启动处理
     * 启动APP 诛仙(com.wanmei.zhuxian.laohu)
     *
     * @param instructStr
     */
    public static void dealWithStartApp(String instructStr) {
        try {
            String packageName = instructStr.substring(instructStr.indexOf("(") + "(".length(), instructStr.lastIndexOf(")"));
            if (!StringUtil.isEmpty(packageName)) {
                AppUtil.killApp(packageName);
                if (RecordFloatView.bigFloatState == RecordFloatView.EXEC) {//执行客服脚本状态
                    if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {//优化平台
                        SpUtil.putKeyBoolean(PlatformConfig.NEED_CHECK_ACTIVE, true);
                        Constants.getOrderTime = System.currentTimeMillis();
                    }
                    SpUtil.putKeyString(PlatformConfig.NEED_KILL_PACKAGENAME, packageName);
                    SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, true);
                }
                SystemClock.sleep(SleepConfig.SLEEP_TIME_5000);
                AppUtil.startAppByComponent(ScriptApplication.getInstance(), packageName);
            } else {
                if (RecordFloatView.bigFloatState == RecordFloatView.EXEC) {
                    SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);
                    SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR);
                }
                SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, true);
                RecordFloatView.updateMessage("APP包名有误!");
                RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();  //APP包名有误 直接终止脚本
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 应用杀死处理
     * 杀死APP 诛仙(com.wanmei.zhuxian.laohu)
     *
     * @param instructStr
     */
    public static void dealWithKillApp(String instructStr) {
        try {
            String packageName = instructStr.substring(instructStr.indexOf("(") + "(".length(), instructStr.lastIndexOf(")"));
            if (!StringUtil.isEmpty(packageName)) {
                AppUtil.killApp(packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 应用安装app处理
     * 安装APP 诛仙(诛仙.apk)
     *
     * @param instructStr
     */
    public static void dealWithInstallApp(String instructStr) {
        try {
            String APKName = instructStr.substring(instructStr.indexOf("(") + "(".length(), instructStr.lastIndexOf(")"));
            if (!StringUtil.isEmpty(APKName) && new File(Constants.SCRIPT_FOLDER_GAME_APK_PATH + APKName).exists()) {
                ApkOperateManager.installSlient(Constants.SCRIPT_FOLDER_GAME_APK_PATH + APKName);
            } else {
                RecordFloatView.updateMessage("Apk文件不存在!");
                LLog.e("Apk文件不存在!");
                RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();  //APP包名有误 直接终止脚本
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 检测APP处理
     * 检测APP#映客.apk#com.melive.ingke
     *
     * @param instructStr
     */
    public static void dealWithCheckApp(String instructStr) {
        try {
            String[] split = instructStr.split(ScriptConstants.SPLIT);
            if (split != null && split.length > 2) {
                String apkName = split[1];
                String appPackName = split[2];
                List<AppInfo> appInfoList = AppUtil.getAppInfoList(ScriptApplication.getInstance());
                ArrayList<String> packNameList = new ArrayList<>();
                for (int i = 0; i < appInfoList.size(); i++) {
                    AppInfo appInfo = appInfoList.get(i);
                    String packageName = appInfo.getPackageName();
                    packNameList.add(packageName);
                }

                if (packNameList.contains(appPackName)) {
                    //存在指定的应用
                    SocketUtil.continueExec();
                } else if (new File(Constants.SCRIPT_FOLDER_GAME_APK_PATH + apkName).exists()) {
                    LLog.e("检测APP------需要安装APP :" + apkName);
                    ApkOperateManager.installSlient(Constants.SCRIPT_FOLDER_GAME_APK_PATH + apkName);
                } else {
                    RecordFloatView.updateMessage("检测APP------指定的应用未安装并且Apk文件不存在!");
                    LLog.e("检测APP------指定的应用未安装并且Apk文件不存在!");
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();  //APP包名有误 直接终止脚本
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 应用安装apk处理
     * 安装APP 诛仙(诛仙.apk)
     *
     * @param instructStr
     */
    public static void dealWithInstallApk(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas.length >= 2) {
                String fileName = datas[1];
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                ScriptApplication.getInstance().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 应用卸载app处理
     * 卸载APP 诛仙(com.wanmei.zhuxian.laohu)
     *
     * @param instructStr
     */
    public static void dealWithUninstallApp(String instructStr) {
        try {
            String packageName = instructStr.substring(instructStr.indexOf("(") + "(".length(), instructStr.lastIndexOf(")"));
            if (!StringUtil.isEmpty(packageName)) {
                ApkOperateManager.uninstallSlient(packageName);
                CMDUtil.execShell("rm -rf /data/app/" + packageName + "-*");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 截图处理
     * 截屏#桌面背景
     *
     * @param instructStr
     */
    public static void dealWithCapture(String instructStr) {
        try {
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length == 2) {
                RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
                SystemClock.sleep(300);
                if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                    CaptureUtil.takeScreen_Play(ScriptApplication.getInstance(), Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sGamesOrderInfo.getTaskData() + "/"
                            + pics[1] + ".jpg", false, Bitmap.CompressFormat.JPEG, CutImageActivity.class);
                    LLog.i(LOG_TAG, "当前截图:" + pics[1] + ".jpg");
                } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                    SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_ROLE);
                    CaptureUtil.takeScreen(ScriptApplication.getInstance(), Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId() + "/"
                            + Constants.sBrushOrderInfo.getUserId() + "_" + System.currentTimeMillis() + ".jpg", false, Bitmap.CompressFormat.JPEG, CutImageActivity.class);
                    LLog.i(LOG_TAG, "当前截图:" + Constants.sBrushOrderInfo.getUserId() + "_" + System.currentTimeMillis() + ".jpg");
                } else {
                    CaptureUtil.takeScreen(ScriptApplication.getInstance(), Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + +System.currentTimeMillis() + ".jpg", false, Bitmap.CompressFormat.JPEG, CutImageActivity.class);
                }
                BaseFloatView.sFloatHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecordFloatView.getInstance(ScriptApplication.getInstance()).show(xInView, yInView);
                    }
                }, 100);
            } else if (pics.length == 3 && "高清".equals(pics[2])) {
                RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
                SystemClock.sleep(300);
                if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                    CaptureUtil.takeScreen_Play(ScriptApplication.getInstance(), Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sGamesOrderInfo.getTaskData() + "/"
                            + pics[1] + ".png", false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
                    LLog.i(LOG_TAG, "当前截图:" + pics[1] + ".png");
                } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                    SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_ROLE);
                    CaptureUtil.takeScreen_Play(ScriptApplication.getInstance(), Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getUserId() + "/"
                            + Constants.sBrushOrderInfo.getUserId() + "_" + System.currentTimeMillis() + ".png", false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
                    LLog.i(LOG_TAG, "当前截图:" + Constants.sBrushOrderInfo.getUserId() + "_" + System.currentTimeMillis() + ".png");
                } else {
                    CaptureUtil.takeScreen_Play(ScriptApplication.getInstance(), Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + System.currentTimeMillis() + ".png", false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
                }
                BaseFloatView.sFloatHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecordFloatView.getInstance(ScriptApplication.getInstance()).show(xInView, yInView);
                    }
                }, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 页面加载完毕处理
     *
     * @param instructStr
     */
    public static void dealWithLoadComplete(String instructStr) {
        if (PlatformUtil.loadingFinished()) { //页面载入成功

        } else {
            checkInLimit();
        }
    }


    /**
     * 检测页面加载限制次数
     */
    private static void checkInLimit() {
        if (SocketUtil.currentTryLoadingNum <= 18) {
            LLog.e(LOG_TAG, "当前识别游戏载入次数:" + SocketUtil.currentTryLoadingNum);
            SocketUtil.setDelay(SleepConfig.SLEEP_TIME_5000);
            SocketUtil.currentTryLoadingNum += 1;
            SocketUtil.currentExecNum -= 1;
        } else {
            RecordFloatView.updateMessage(ScriptApplication.getInstance().getResources().getString(R.string.text_alert_connot_loading_game));
            SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);
            SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, true);
            SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR);
            RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();//终止脚本
        }
    }


    /**
     * 随机处理
     * 生成随机用户名/生成随机昵称/生成随机密码
     *
     * @param instructStr
     */
    public static void dealWithRandom(String instructStr) {
        Vector<String> list = GameConfig.registerPercentList;
        if (list != null && list.size() > 0) {
            GameConfig.ASHES_ACCOUNT_RULE = list.get((int) (Math.random() * (list.size() - 1)));
            GameConfig.ASHES_PASSWORD_RULE = list.get((int) (Math.random() * (list.size() - 1)));
        }
        String registerAccount;
        String registerPassword;
        if (!StringUtil.isEmpty(GameConfig.ASHES_ACCOUNT_RULE) && !StringUtil.isEmpty(GameConfig.ASHES_PASSWORD_RULE)) {
            registerAccount = AccountUtil.getRandomName(GameConfig.ASHES_ACCOUNT_RULE);
            registerPassword = AccountUtil.getRandomName(GameConfig.ASHES_PASSWORD_RULE);
            GameConfig.ASHES_ACCOUNT_RULE = null;
            GameConfig.ASHES_PASSWORD_RULE = null;
        } else {
            registerAccount = AccountUtil.getRandomName();
            registerPassword = AccountUtil.getRandomName("?&&$$$$");
        }
        if (instructStr.contains(ScriptConstants.RANDOM_USERNAME_SCRIPT)) {
            LLog.i("当前注册的用户名:" + registerAccount);
            SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, registerAccount);
        } else if (instructStr.contains(ScriptConstants.RANDOM_NICKNAME_SCRIPT)) {
            LLog.i("当前随机的用户昵称:" + registerAccount);
            SpUtil.putKeyString(PlatformConfig.CURRENT_NICKNAME, registerAccount);
        } else if (instructStr.contains(ScriptConstants.RANDOM_PASSWORD_SCRIPT)) {
            LLog.i("当前随机的用户密码:" + registerPassword);
            SpUtil.putKeyString(PlatformConfig.CURRENT_PASSWORD, registerPassword);
        }
        SocketUtil.continueExec();//继续播放脚本
    }


    /**
     * 生成用户名
     */
    public static void dealWithCreatUserName() {
        int minDigit = Constants.ACCOUNT_MIN;
        int maxDigit = Constants.ACCOUNT_MIN;
        String accountName = "";
        int i = 0;
        if (minDigit != -1 && maxDigit != -1) {
            int length = 0;
            do {
                if (i <= 20) {
                    String accountRule = AccountUtil.getRandamAccountRule();
                    LLog.e("-----------------------    " + (i++) + " : " + accountRule);
                    accountName = AccountUtil.getRandomName(accountRule);
                    char[] chars = accountName.toCharArray();
                    length = chars.length;
                } else {
                    accountName = "";
                    break;
                }
            } while (length >= minDigit && length <= maxDigit);
        }

        if (!TextUtils.isEmpty(accountName)) {
            LLog.e("当前注册的用户名:" + accountName);
            SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, accountName);
            SocketUtil.continueExec();//继续播放脚本
        } else {
            LLog.i("注册的用户名失败:");
            BrushOrderHelper.getInstance().orderDealFailure("注册的用户名失败!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }

    /**
     * 生成指定格式密码
     * 生成指定格式密码#**((&&$$
     *
     * @param instructStr
     */
    public static void dealWithTypePassword(String instructStr) {
        String[] strs = instructStr.split(ScriptConstants.SPLIT);
        if (strs.length > 1) {
            String typePassword = AccountUtil.getRandomName(strs[1]);
            LLog.e("当前注册的密码:" + typePassword);
            SpUtil.putKeyString(PlatformConfig.CURRENT_PASSWORD, typePassword);
            SocketUtil.continueExec();//继续播放脚本
        }
    }


    /**
     * 返回用户信息处理
     */
    public static void dealWithReturnUserInfo() {
        //返回游戏注册信息
        Constants.failRepeatCount = 0;
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_USER_INFO, 1000);
    }

    /**
     * 返回设备信息处理
     */
    public static void dealWithReturnDeviceInfo() {
        //返回游戏注册信息
        Constants.failRepeatCount = 0;
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPDATE_DEVICE_INFO, 1000);
    }


    /**
     * 执行上几句处理
     * 执行上几句处理#10
     *
     * @param instructStr
     */
    public static void dealWithPreCmd(String instructStr) {
        LLog.e("当前剩余循环次数:" + Constants.loopCount);
        if (Constants.loopCount > 0) {
            int num = Integer.parseInt(instructStr.split(ScriptConstants.SPLIT)[1]);
            SocketUtil.currentExecNum -= (num + 1);
            Constants.loopCount--;
        }
    }


    /**
     * 修改设备信息检测
     */
    public static void dealWithChangeDeviceCheck() {
        if (!SpUtil.getKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, false)) {
            HookUtil.getDeviceInfo(ScriptApplication.getInstance().getPackageName(), DeviceConfig.deviceConfigMap, DeviceConfig.deviceRandomParam, ScriptApplication.getInstance());
            SystemClock.sleep(3000);
        }
        String imei_1 = DeviceUtil.getIMEI(ScriptApplication.getInstance());
        ScriptUtil.dealWithChangeDeviceInfo();
        SystemClock.sleep(3000);
        String imei_2 = DeviceUtil.getIMEI(ScriptApplication.getInstance());
        LLog.i("修改前imei_1==========: " + imei_1);
        LLog.i("修改后imei_2==========: " + imei_2);
        if (!SpUtil.getKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, false)) {
            if (TextUtils.isEmpty(imei_1) || TextUtils.isEmpty(imei_2) || imei_1.equals(imei_2)) {
                LLog.e("修改imei失败，返回订单信息失败");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "切换设备信息失败 : \n" + "  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                ScriptUtil.dealWithReturnOrderResult(ScriptConstants.RETURN_ORDER_RESULT + ScriptConstants.SPLIT + ScriptConstants.CHANGE_DEVICE_INFO_SCRIPT);
                SystemClock.sleep(3000);
                ScriptApplication.getService().execute(() -> BrushOrderHelper.dealWithXposedReboot());
                return;
            }
        }
    }


    /**
     * 生成本机设备信息
     */
    public static void dealWithGetOwnDeviceInfo() {
        String deviceInfo = DeviceUtil.printCustomDeviceInfo(ScriptApplication.getInstance());
        SpUtil.putKeyString(PlatformConfig.CURRENT_DEVICE_INFO, deviceInfo);//存当前设备信息

    }


    /**
     * 修改设备信息处理
     */
    public static void dealWithChangeDeviceInfo() {
        if (Constants.sBrushOrderInfo != null) {
            CacheUtil.clearPackageCache(Constants.sBrushOrderInfo.getGamePackageName());//清游戏缓存
        }
        if (Constants.sBrushOrderInfo != null && Constants.sBrushOrderInfo.getUserModel() != null) {
            BrushOrderInfo.UserModelBean userModelBean = Constants.sBrushOrderInfo.getUserModel();
            if (userModelBean != null) {
                String deviceStr = userModelBean.getDeviceInfo();
                if (!StringUtil.isEmpty(deviceStr)) {
                    try {
                        DeviceInfo deviceInfo = ScriptApplication.getGson().fromJson(deviceStr, DeviceInfo.class);
                        LLog.e("登录需要设置的设备信息:" + deviceInfo.toString());
                        SpUtil.putKeyString(PlatformConfig.CURRENT_DEVICE_INFO, deviceStr);//存当前设备信息
                        FileUtil.saveFile(ScriptApplication.getGson().toJson(deviceInfo), Constants.DEVICE_PATH);//设置当前设备信息
                        FileUtil.saveFile(deviceInfo.getMacAddress(), Constants.MAC_PATH);//设置当前MAC信息
                        FileUtil.saveFile(deviceInfo.getCid(), Constants.CID_PATH);//设置当前SD卡序列号信息
                        FileUtil.saveFile(deviceInfo.getCoreVersion(), Constants.CORE_PATH);//设置当前内核信息
                        FileUtil.saveFile(deviceInfo.getArp(), Constants.ARP_PATH);//设置当前ARP信息
                        SpUtil.putDeviceInfo(BuildConfig.APPLICATION_ID, ScriptApplication.getGson().toJson(deviceInfo));//设置当前设备信息
                        CMDUtil.execCommand(new String[]{"echo \"" + deviceInfo.getMacAddress() + "\" > " + Constants.MAC_PREFS_PATH}, false, false);
                        CMDUtil.execShell("chmod 755 " + Constants.MAC_PREFS_PATH);//设置当前MAC信息
                        CMDUtil.execCommand(new String[]{"echo \"" + deviceInfo.getCid() + "\" > " + Constants.CID_PREFS_PATH}, false, false);
                        CMDUtil.execShell("chmod 755 " + Constants.CID_PREFS_PATH);//设置当前SD卡序列号信息
                        CMDUtil.execCommand(new String[]{"echo \"" + deviceInfo.getCoreVersion() + "\" > " + Constants.CORE_PREFS_PATH}, false, false);
                        CMDUtil.execShell("chmod 755 " + Constants.CORE_PREFS_PATH);//设置当前内核信息
                        CMDUtil.execCommand(new String[]{"echo \"" + deviceInfo.getArp() + "\" > " + Constants.ARP_PREFS_PATH}, false, false);
                        CMDUtil.execShell("chmod 755 " + Constants.ARP_PREFS_PATH);//设置当前ARP信息
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    createDeviceInfo();
                }
            } else {
                createDeviceInfo();
            }
        } else {
            createDeviceInfo();
        }
    }


    /**
     * 创建设备信息
     */
    public static void createDeviceInfo() {
        try {
            String tempUserId = FileUtil.readFile(Constants.BRUSH_ORDER_ID_TEMP_PATH);
            if (RecordFloatView.bigFloatState == RecordFloatView.EXEC) {
                if (Constants.sBrushOrderInfo != null && !TextUtils.isEmpty(Constants.sBrushOrderInfo.getUserId()) && !Constants.sBrushOrderInfo.getUserId().equals(tempUserId)) {
                    HookUtil.getDeviceInfo(ScriptApplication.getInstance().getPackageName(), DeviceConfig.deviceConfigMap, DeviceConfig.deviceRandomParam, ScriptApplication.getInstance());
                    SystemClock.sleep(3000);
                    String deviceInfo = DeviceUtil.getDeviceInfo(ScriptApplication.getInstance());
                    SpUtil.putKeyString(PlatformConfig.CURRENT_DEVICE_INFO, deviceInfo);//存当前设备信息
                }
            } else if (RecordFloatView.bigFloatState == RecordFloatView.PLAY) {
                HookUtil.getDeviceInfo(ScriptApplication.getInstance().getPackageName(), DeviceConfig.deviceConfigMap, DeviceConfig.deviceRandomParam, ScriptApplication.getInstance());
                SystemClock.sleep(3000);
                String deviceInfo = DeviceUtil.getDeviceInfo(ScriptApplication.getInstance());
                SpUtil.putKeyString(PlatformConfig.CURRENT_DEVICE_INFO, deviceInfo);//存当前设备信息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 切换IP处理
     */
    public static void dealWithChangeIp(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        //获取检查网络延时参数
        if (datas.length >= 3) {
            SpUtil.putKeyInt(PlatformConfig.CURRENT_CHECKNETLIMIT, Integer.parseInt(datas[2]));
        }
        if (datas.length >= 2) {
            if (!TextUtils.isEmpty(datas[1]) && datas[1].contains("本地断网")) {
                BrushOrderHelper.getInstance().vpnConnected = true;
            } else if (!TextUtils.isEmpty(datas[1]) && datas[1].contains("芝麻VPN")) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_VPN_TYPE, "芝麻VPN");
                ZimaVpnUtil.getInstance().linkVpn();
            } else if (!TextUtils.isEmpty(datas[1]) && datas[1].contains("无极VPN")) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_VPN_TYPE, "无极VPN");
                WujiVpnUtil.dealWithVPN();
            } else {
                LLog.e("VPN参数有误，请更正后重试！");
                RecordFloatView.updateMessage("VPN参数有误，请更正后重试！");
                if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
                    BrushOrderHelper.getInstance().orderDealFailure("VPN参数有误!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                }
            }
        } else {
            BrushOrderHelper.getInstance().orderDealFailure("切换VPN指令有误!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            LLog.e("切换VPN指令有误，请更正后重试！");
            RecordFloatView.updateMessage("切换VPN指令有误，请更正后重试！");
        }
    }


    /**
     * 检验VPN连接状态处理
     */
    public static void dealWithCheckVpnStatus() {
        LLog.e("开始 检验VPN连接状态处理");
        ScriptApplication.getService().execute(new Runnable() {
            @Override
            public void run() {
                while (Constants.PLAY_STATE == PlayEnum.START_PLAY) {
                    if (!BrushOrderHelper.getInstance().vpnConnected) {
                        LLog.e("检验VPN连接状态中===========>");
                        SystemClock.sleep(5000);
                    } else {
                        LLog.e("vpn连接成功 =======> 继续执行脚本..");


                        SocketUtil.continueExec();

                        break;
                    }
                }
            }
        });
    }

    /**
     * 返回IP信息处理
     * ifconfig.me/ip
     * http://ip.taobao.com/service/getIpInfo2.php?ip=myip
     */
    public static void dealWithReturnIpInfo(int ipFlag) {
//        CMDUtil.CommandResult commandResult = CMDUtil.execCommand(new String[]{"curl ifconfig.me/ip"}, true, true);
//        String ip = "";
//        if (commandResult != null && !TextUtils.isEmpty(commandResult.successMsg)) {
//            try {
//                ip = commandResult.successMsg.split("\n")[0];
//                LLog.e("当前代理IP: " + ip + "  ipFlag:" + ipFlag);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        String id = ip.split("%")[0];
//        LLog.e("当前代理IP --- id: " + id );
//        if (TextUtils.isEmpty(id) || id.length()<10) {
//            LLog.e("返回IP失败: 订单失败");
//            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "返回IP失败");
//            BrushOrderHelper.getInstance().orderDealFailure("返回IP失败");
//        } else {
        checkIp("ip", ipFlag);
//        }

    }


    public static void checkIp(String ip, int ipFlag) {
        //LLog.i("流量 message    :  " + jsonObject.toString());
        GetHttpRequest getHttpRequest = new GetHttpRequest();
        Map<String, String> map = new HashMap<>();
//        map.put("remoteIp", ip);
        map.put("orderId", Constants.sBrushOrderInfo.getOrderId());
        LLog.e("要检测的ip:" + ip);
        getHttpRequest.get(HttpConstants.ORDER_CHECK_IP, map, new JsonCallback() {
            @Override
            public void onResponse(JSONObject response, int id) {
                super.onResponse(response, id);
                int code = (int) response.opt("code");
                LLog.e("检测ip状态:" + code);
                if (code == 0) {
                    LLog.e("ip不重复 返回IP信息到后台:");
                    Constants.failRepeatCount = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("ip", "");
                    bundle.putInt("ipFlag", ipFlag);
                    BrushTask.getInstance().sendMessageBundle(BrushTask.B_MSG_TYPE_UPDATE_IP_INFO, bundle, 1000);
                } else { //ip重复
                    String vpnType = SpUtil.getKeyString(PlatformConfig.CURRENT_VPN_TYPE, "haha");
                    BrushOrderHelper.closeVPN();
                    LLog.e("ip重复 重新执行脚本:");
                    SocketUtil.currentExecNum = 1;
                    SocketUtil.continueExec();
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);

            }
        });

    }


    /**
     * 获取手机号处理
     */
    public static void dealWithGetPhoneNumber(String instructStr) {
        Constants.failRepeatCount = 0;
        MobileRegisterHelper.getInstance().contentPhoneType = "";
        MobileRegisterHelper.getInstance().requestPhone(instructStr);
    }

    /**
     * 获取手机号账号
     */
    public static void dealWithGetPhoneNumberAdmin(String instructStr) {
        Constants.failRepeatCount = 0;
        MobileRegisterHelper.getInstance().contentPhoneType = "";
        MobileRegisterHelper.getInstance().requestPhone(instructStr);
    }

    /**
     * 释放手机号处理
     */
    public static void dealWithRelesePhoneNumber() {
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_RELEASE_PHONE, 1000);
    }

    /**
     * 插入手机号处理
     */
    public static void dealWithInsertPhoneNumber() {
        SocketUtil.sendInstruct(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + SpUtil.getKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, null), false);
        SocketUtil.setDelay(1000);
    }

    /**
     * 插入自定义键盘手机号处理
     */
    public static void dealWithInsertKeyBordPhoneNumber() {
        if (!TextUtils.isEmpty(MobileRegisterHelper.getInstance().phone)) {
            char[] chars = MobileRegisterHelper.getInstance().phone.toCharArray();
            int length = chars.length;
            for (int i = 0; i < length; i++) {
                if (String.valueOf(chars[i]).equals("1")) {
                    String[] number1 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_1);
                    creatPointAction(number1);
                } else if (String.valueOf(chars[i]).equals("2")) {
                    String[] number2 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_2);
                    creatPointAction(number2);
                } else if (String.valueOf(chars[i]).equals("3")) {
                    String[] number3 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_3);
                    creatPointAction(number3);
                } else if (String.valueOf(chars[i]).equals("4")) {
                    String[] number4 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_4);
                    creatPointAction(number4);
                } else if (String.valueOf(chars[i]).equals("5")) {
                    String[] number5 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_5);
                    creatPointAction(number5);
                } else if (String.valueOf(chars[i]).equals("6")) {
                    String[] number6 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_6);
                    creatPointAction(number6);
                } else if (String.valueOf(chars[i]).equals("7")) {
                    String[] number7 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_7);
                    creatPointAction(number7);
                } else if (String.valueOf(chars[i]).equals("8")) {
                    String[] number8 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_8);
                    creatPointAction(number8);
                } else if (String.valueOf(chars[i]).equals("9")) {
                    String[] number9 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_9);
                    creatPointAction(number9);
                } else if (String.valueOf(chars[i]).equals("0")) {
                    String[] number0 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_0);
                    creatPointAction(number0);
                }
            }
        }
    }

    /**
     * 插入自定义键盘验证码处理
     */
    public static void dealWithInsertKeyBordVerCode() {
        if (!MobileRegisterHelper.getInstance().verifyCode.isEmpty()) {
            char[] chars = MobileRegisterHelper.getInstance().verifyCode.toCharArray();
            int length = chars.length;
            for (int i = 0; i < length; i++) {
                if (String.valueOf(chars[i]).equals("1")) {
                    String[] number1 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_1);
                    creatPointAction(number1);
                } else if (String.valueOf(chars[i]).equals("2")) {
                    String[] number2 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_2);
                    creatPointAction(number2);
                } else if (String.valueOf(chars[i]).equals("3")) {
                    String[] number3 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_3);
                    creatPointAction(number3);
                } else if (String.valueOf(chars[i]).equals("4")) {
                    String[] number4 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_4);
                    creatPointAction(number4);
                } else if (String.valueOf(chars[i]).equals("5")) {
                    String[] number5 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_5);
                    creatPointAction(number5);
                } else if (String.valueOf(chars[i]).equals("6")) {
                    String[] number6 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_6);
                    creatPointAction(number6);
                } else if (String.valueOf(chars[i]).equals("7")) {
                    String[] number7 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_7);
                    creatPointAction(number7);
                } else if (String.valueOf(chars[i]).equals("8")) {
                    String[] number8 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_8);
                    creatPointAction(number8);
                } else if (String.valueOf(chars[i]).equals("9")) {
                    String[] number9 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_9);
                    creatPointAction(number9);
                } else if (String.valueOf(chars[i]).equals("0")) {
                    String[] number0 = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_KEYBOARD_0);
                    creatPointAction(number0);
                }
            }
        }
    }

    /**
     * 创建自定义键盘点的动作
     *
     * @param number
     */
    public static void creatPointAction(String[] number) {
        LLog.e("键盘的点=========" + number.toString());
        SocketUtil.sendInstruct(ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + ((number.length > 0) ? (number[0] + ScriptConstants.SPLIT + number[1]) : (0 + ScriptConstants.SPLIT + 0)), false);
        SystemClock.sleep(500);
    }


    /**
     * 获取验证码处理
     */
    public static void dealWithGetPhoneCode(String instructStr) {
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CLICK_CODE, SleepConfig.SLEEP_TIME_2000);
        } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
            MobileRegisterHelper.getInstance().verifyCode = null;
            MobileRegisterHelper.getInstance().passwordPhoneCode = "";
            MobileRegisterHelper.getInstance().getPhoneCodeMessage(instructStr);
        }
    }

    /**
     * 获取验证码密码处理
     */
    public static void dealWithGetPhoneCodePassWord(String instructStr) {
        MobileRegisterHelper.getInstance().verifyCode = null;
        MobileRegisterHelper.getInstance().passwordPhoneCode = "";
        MobileRegisterHelper.getInstance().getPhoneCodeMessage(instructStr);
    }

    /**
     * 插入手机验证码处理
     * 插入手机验证码
     * 插入手机验证码#1
     */
    public static void dealWithInsertPhoneCode(String instructStr) {
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            String inputStr = ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + InsertAreaRoleUtil.getInstance().verifyCode;
            SocketUtil.sendInstruct(inputStr, false);
        } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
            if (!instructStr.contains(ScriptConstants.SPLIT)) {
                SocketUtil.sendInstruct(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + MobileRegisterHelper.getInstance().verifyCode.trim(), false);
            } else {
                try {
                    int num = Integer.parseInt(instructStr.split(ScriptConstants.SPLIT)[1]);
                    String oneVerifyCode = MobileRegisterHelper.getInstance().verifyCode.trim().substring(num - 1, num);
                    SocketUtil.sendInstruct(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + oneVerifyCode, false);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 循环次数处理
     * 循环次数#10
     * 点击#100#100
     * 执行上几句#1
     */
    public static void dealWithLoopCount(String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            try {
                String[] datas = instructStr.split(ScriptConstants.SPLIT);
                if (datas.length == 2) {
                    Constants.loopCount = Long.parseLong(datas[1]);
                    LLog.i("当前循环次数:" + Constants.loopCount);
                } else if (datas.length == 3) {
                    Constants.loopCount = Integer.parseInt(datas[1]) + (long) ((Integer.parseInt(datas[2]) - Integer.parseInt(datas[1]) + 1) * Math.random());
                    LLog.i("当前随机的循环次数:" + Constants.loopCount);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 直接点击处理
     * 直接点击#100#100
     */
    public static void dealWithTapImmediately(final String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            try {
                String[] datas = instructStr.split(ScriptConstants.SPLIT);
                if (datas.length >= 3) {
                    String cmd = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + datas[1] + ScriptConstants.SPLIT + datas[2];
                    SocketUtil.sendInstruct(cmd, false);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 点击区域处理
     * 点击区域#100#100#50#50  随机点击(100,100) X坐标+-50  Y坐标+-50
     */
    public static void dealWithTapArea(final String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            try {
                String[] datas = instructStr.split(ScriptConstants.SPLIT);
                if (datas.length >= 5) {
                    String cmd = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + (Integer.parseInt(datas[1]) + RandomUtil.getRandomSpace(Integer.parseInt(datas[3])))
                            + ScriptConstants.SPLIT + (Integer.parseInt(datas[2]) + RandomUtil.getRandomSpace(Integer.parseInt(datas[4])));
                    LLog.i("随机点击区域: " + cmd);
                    SocketUtil.sendInstruct(cmd, true);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 键值事件处理
     * 键值#67 表示删除回退
     */
    public static void dealWithKeyValue(final String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            try {
                String[] datas = instructStr.split(ScriptConstants.SPLIT);
                if (datas.length >= 2) {
                    String cmd = ScriptConstants.INPUT_CMD + " " + ScriptConstants.KEYEVENT_CMD + " " + datas[1];
                    CMDUtil.execShell(cmd);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 普通文本处理
     * 文本#5465asd!!
     */
    public static void dealWithNormalText(String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            try {
                String[] data = instructStr.split(ScriptConstants.SPLIT);
                if (data.length >= 1) {
                    if (ScriptConstants.ADMIN.equals(data[1])) {
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_ADMIN, ""));
                    } else if (ScriptConstants.PASSWORD.equals(data[1])) {
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_PASSWORD, ""));
                    } else if (ScriptConstants.PHONE_NUMBER.equals(data[1])) {
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, ""));
                    } else if (ScriptConstants.NAME.equals(data[1])) {
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_NAME, ""));
                    } else if (ScriptConstants.ID_CARD.equals(data[1])) {
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_ID_CARD, ""));
                    } else if (ScriptConstants.NICKNAME.equals(data[1])) {
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_NICKNAME, ""));
                    } else if (ScriptConstants.COMMENT.equals(data[1])) {
                        if (data.length < 4) {
                            return;
                        }
                        instructStr = ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + " ";
                        if (BrushOrderHelper.getInstance().TvComments == null || BrushOrderHelper.getInstance().TvComments.size() == 0) {
                            RecordFloatView.updateMessage("话库中取到的内容为空!");
                        } else {
                            for (CommentInfo commentInfo : BrushOrderHelper.getInstance().TvComments) {
                                if ((data[2] + ".txt").equals(commentInfo.getName())) {
                                    if ("顺序".equals(data[3])) {
                                        if (commentInfo.getCommentNum() >= commentInfo.getComments().size()) {
                                            commentInfo.setCommentNum(0);
                                        }
                                        instructStr = ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + commentInfo.getComments().get(commentInfo.getCommentNum());
                                        int newNum = commentInfo.getCommentNum() + 1;
                                        commentInfo.setCommentNum(newNum);
                                    } else if ("乱序".equals(data[3])) {
                                        instructStr = ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + commentInfo.getComments().get(new Random().nextInt(commentInfo.getComments().size()));
                                    }
                                }
                            }
                        }
                    }
                    SocketUtil.sendInstruct(instructStr, false);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 中文文本处理
     * 输入#我是字符串^_^!
     */
    public static void dealWithChineseText(String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            try {
                String[] data = instructStr.split(ScriptConstants.SPLIT);
                if (data.length >= 1) {
                    if (ScriptConstants.ADMIN.equals(data[1])) {
                        LLog.i("当前用户名：" + SpUtil.getKeyString(PlatformConfig.CURRENT_ADMIN, ""));
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_ADMIN, ""));
                    } else if (ScriptConstants.PASSWORD.equals(data[1])) {
                        LLog.i("当前密码：" + SpUtil.getKeyString(PlatformConfig.CURRENT_PASSWORD, ""));
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_PASSWORD, ""));
                    } else if (ScriptConstants.PHONE_NUMBER.equals(data[1])) {
                        LLog.i("当前手机号：" + SpUtil.getKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, ""));
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, ""));
                    } else if (ScriptConstants.NAME.equals(data[1])) {
                        LLog.i("当前身份证姓名：" + SpUtil.getKeyString(PlatformConfig.CURRENT_NAME, ""));
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_NAME, ""));
                    } else if (ScriptConstants.ID_CARD.equals(data[1])) {
                        LLog.i("当前身份证号码：" + SpUtil.getKeyString(PlatformConfig.CURRENT_ID_CARD, ""));
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_ID_CARD, ""));
                    } else if (ScriptConstants.NICKNAME.equals(data[1])) {
                        LLog.i("当前昵称：" + SpUtil.getKeyString(PlatformConfig.CURRENT_NICKNAME, ""));
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_NICKNAME, ""));
                    } else if (ScriptConstants.COMMENTARY.equals(data[1])) {
                        LLog.i("当前评论内容：" + SpUtil.getKeyString(PlatformConfig.CURRENT_COMMENTARY, ""));
                        instructStr = instructStr.replace(data[1], SpUtil.getKeyString(PlatformConfig.CURRENT_COMMENTARY, ""));
                    } else if (ScriptConstants.COMMENT.equals(data[1])) {
                        if (data.length < 4) {
                            return;
                        }
                        instructStr = ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + " ";
                        if (BrushOrderHelper.getInstance().TvComments == null || BrushOrderHelper.getInstance().TvComments.size() == 0) {
                            RecordFloatView.updateMessage("话库中取到的内容为空!");
                        } else {
                            for (CommentInfo commentInfo : BrushOrderHelper.getInstance().TvComments) {
                                if ((data[2] + ".txt").equals(commentInfo.getName())) {
                                    if ("顺序".equals(data[3])) {
                                        if (commentInfo.getCommentNum() >= commentInfo.getComments().size()) {
                                            commentInfo.setCommentNum(0);
                                        }
                                        instructStr = ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + commentInfo.getComments().get(commentInfo.getCommentNum());
                                        int newNum = commentInfo.getCommentNum() + 1;
                                        commentInfo.setCommentNum(newNum);
                                    } else if ("乱序".equals(data[3])) {
                                        instructStr = ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + commentInfo.getComments().get(new Random().nextInt(commentInfo.getComments().size()));
                                    }
                                }
                            }
                        }
                    }
                    SocketUtil.sendInstruct(instructStr, false);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    public static void dealWithInputWithhao(String instructStr) {
        String content = instructStr.substring(instructStr.indexOf("(") + "(".length(), instructStr.lastIndexOf(")"));
        String str = "am broadcast -a ADB_INPUT_TEXT --es msg " + "'" + content + "'";
        CMDUtil.execShell(str);
    }

    /**
     * 移动悬浮按钮处理
     * 移动悬浮按钮#0#1080
     */
    public static int xInView = 0;
    public static int yInView = 0;

    public static void dealWithFloatView(String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            try {
                String[] datas = instructStr.split(ScriptConstants.SPLIT);
                if (datas.length >= 3) {
                    PlatformUtil.initRecordView(Integer.valueOf(datas[1]), Integer.valueOf(datas[2]));
                    xInView = Integer.valueOf(datas[1]);
                    yInView = Integer.valueOf(datas[2]);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 人工协助处理
     * 人工协助
     * 人工协助#无截图
     */
    public static long assistantTime = 0;

    public static PromptDialog sPromptDialog;

    public static void dealWithAssistant(String instructStr) {
        try {
            RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayPause();
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.execServerScript) {
                if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                    if (datas.length >= 2 && "无截图".equals(datas[1])) {
                        BaseOrderHelper.callAlertAndPause(AlertConfig.ALERT_AND_PAUSE_KEFU_DO, false);
                    } else {
                        BaseOrderHelper.callAlertAndPause(AlertConfig.ALERT_AND_PAUSE_KEFU_DO, true);
                    }
                } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                    if (datas.length >= 2 && "无截图".equals(datas[1])) {
                        BaseOrderHelper.callAlertAndPause(BAlertConfig.B_ALERT_AND_PAUSE_KEFU_DO, false);
                    } else {
                        BaseOrderHelper.callAlertAndPause(BAlertConfig.B_ALERT_AND_PAUSE_KEFU_DO, true);
                    }
                    SocketUtil.sendInstruct("ime#com.sohu.inputmethod.sogou.xiaomi/.SogouIME", false);
                    if (Constants.sBrushOrderInfo != null && (BOrderTypeConfig.ORDER_TYPE_PAY == Constants.sBrushOrderInfo.getOrderType()
                            || BOrderTypeConfig.ORDER_TYPE_AUTO_PAY == Constants.sBrushOrderInfo.getOrderType()
                            || BOrderTypeConfig.ORDER_TYPE_UPGRADE == Constants.sBrushOrderInfo.getOrderType())) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sPromptDialog = new PromptDialog(ScriptApplication.getInstance());
                                sPromptDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
                                String title = "";
                                if (BOrderTypeConfig.ORDER_TYPE_PAY == Constants.sBrushOrderInfo.getOrderType() || BOrderTypeConfig.ORDER_TYPE_AUTO_PAY == Constants.sBrushOrderInfo.getOrderType()) {
                                    title = "充值人员验证<br>备注:<font color=\"#FF0000\">" + Constants.sBrushOrderInfo.getDesc() + "</font>";
                                    if (Constants.sBrushOrderInfo.getTwice() == 0) {
                                        sPromptDialog.setPositiveBtnBackBround(Color.parseColor("#008000"));
                                        sPromptDialog.setNegativeBtnBackBround(Color.parseColor("#008000"));
                                    } else {
                                        sPromptDialog.setPositiveBtnBackBround(Color.parseColor("#ffcc0000"));
                                        sPromptDialog.setNegativeBtnBackBround(Color.parseColor("#ffcc0000"));
                                    }
                                } else if (BOrderTypeConfig.ORDER_TYPE_UPGRADE == Constants.sBrushOrderInfo.getOrderType()) {
                                    title = "升级人员验证<br>备注:<font color=\"#FF0000\">" + Constants.sBrushOrderInfo.getDesc() + "</font>";
                                    sPromptDialog.setPositiveBtnBackBround(Color.parseColor("#979797"));
                                    sPromptDialog.setNegativeBtnBackBround(Color.parseColor("#979797"));
                                }
                                sPromptDialog.setTitleText(Html.fromHtml(title));
                                sPromptDialog.setNegativeBtnText("跳单");
                                sPromptDialog.setCancelable(false);
                                sPromptDialog.setOnPositiveListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String jobNumber = ((EditText) sPromptDialog.getEditText()).getText().toString().trim();
                                        if (TextUtils.isEmpty(jobNumber)) {
                                            LLog.e("工号为空,请重新输入!");
                                            RecordFloatView.updateMessage("工号为空,请重新输入!");
                                            return;
                                        }
                                        BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_COMMIT_JOB_NUMBER, jobNumber, 1000);
                                    }
                                });
                                sPromptDialog.setOnNegativeListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final PopupDialog popupDialog = new PopupDialog(ScriptApplication.getInstance(), 2, false);
                                        popupDialog.setTitle("确定要跳过此订单吗？");
                                        popupDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
                                        popupDialog.setOnPositiveListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popupDialog.dismiss();
                                                sPromptDialog.dismiss();
                                                Constants.PLAY_STATE = PlayEnum.START_PLAY;
                                                new Handler(Looper.getMainLooper()).post(() -> {
                                                    RecordFloatView.getInstance(ScriptApplication.getInstance()).initState();
                                                });
                                                LLog.e("即将开始获取下一订单!");
                                                RecordFloatView.updateMessage("即将开始获取下一订单!");
                                                Constants.sBrushOrderInfo = null;
                                                FileUtil.saveFile("", Constants.BRUSH_ORDER_FOLDER_TEMP_PATH);
                                                BaseOrderHelper.resetData();//重置状态值
                                            }
                                        });
                                        popupDialog.setOnNegativeListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popupDialog.dismiss();
                                            }
                                        });
                                        popupDialog.show();
                                    }
                                });
                                sPromptDialog.show();
                            }
                        }, 100);
                    }
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //assistantTime = System.currentTimeMillis();
        //dealWithCheckAssistant();
    }


    /**
     * 远程协助处理
     */
    public static void dealWithRemoteAssistant() {
        if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.execServerScript) {
            if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {

            } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                String path = Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId() + "/" + SpUtil.getKeyString(PlatformConfig.PHONE_NAME, "unKnow")
                        + "_远程协助_" + System.currentTimeMillis() + ".jpg";
                CaptureUtil.takeScreen_Play(ScriptApplication.getInstance(), path, false, Bitmap.CompressFormat.JPEG, CutImageActivity.class);
                Bitmap bitmap = BitmapUtil.compressImage(path);
                ImageUtil.saveBitmap(bitmap, path, Bitmap.CompressFormat.JPEG);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REMOTE_ASSISTANCE, 1000);
            }
        }
    }


    /**
     * 人工协助时长检测
     */

    public static void dealWithCheckAssistant() {
        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_ASSISTANCE_TIME_CHECK, 5000);
    }


    /**
     * 状态栏处理
     * 状态栏#隐藏状态栏
     * 状态栏#恢复状态栏
     */

    public static void dealWithStatusBar(String instructStr) {
        if (!StringUtil.isEmpty(instructStr)) {
            if (instructStr.contains(ScriptConstants.HIDE_STATUSBAR_SCRIPT)) {  //隐藏状态栏
                CMDUtil.execShell("settings put global policy_control immersive.status=*");
            } else if (instructStr.contains(ScriptConstants.OPEN_STATUSBAR_SCRIPT)) {  //恢复状态栏
                CMDUtil.execShell("settings put global policy_control null");
            }
        }
    }

    /**
     * 脚本延长时间处理
     */

    private static long tempDelayTime = 0;

    public static void dealWithDelayLimit() {
        int limitTime;
        if (StringUtil.isEmpty((String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_FINISH_DELAY))) {
            limitTime = 0;
        } else {
            limitTime = Integer.parseInt((String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_FINISH_DELAY));
        }
        LLog.i("脚本结束延长时间:" + limitTime);
        if ((System.currentTimeMillis() - Constants.getOrderTime) <= limitTime * 60 * 1000) {
            if (System.currentTimeMillis() - tempDelayTime > 10 * 1000) {
                tempDelayTime = System.currentTimeMillis();
                if (PlatformUtil.loadingFinished()) {
                    SocketUtil.needDelay = false;
                    if (BOrderTypeConfig.ORDER_TYPE_PAY != Constants.sBrushOrderInfo.getOrderType()) {
                        SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, true);
                    } else {
                        SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);
                    }
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();
                    return;
                }
            }
        } else {
            LLog.i("已超过脚本延长时间,结束播放!");
            RecordFloatView.updateMessage("已超过脚本延长时间,结束播放!");
            SocketUtil.needDelay = false;
            if (BOrderTypeConfig.ORDER_TYPE_PAY != Constants.sBrushOrderInfo.getOrderType()) {
                SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, true);
            } else {
                SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);
            }
            RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();
            return;
        }
    }


    /**
     * 定义Defined的指令处理
     * IFDEF#断网自动连接
     * 点图片#断网自动连接
     * ENDIF
     */
    public static void dealWithDefinedCmd(String instructStr) {
        getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        long time = System.currentTimeMillis();
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        if (pics.length >= 1) {
            ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
            SystemClock.sleep(50);
            Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
            if (largeBitmap == null) {
                largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            }
            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }
            LLog.e("------------------------------------" + largeBitmap);
            if (largeBitmap != null) {
                List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                    for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                        if (info != null && PlatformConfig.IMG_TYPE_4.equals(info.getType()) && info.getName().contains(pics[1])) {
                            accordInfoList.add(info);
                        }
                    }
                }
                if (accordInfoList.size() == 0) {
                    LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                }
                Rect rect = null;
                for (PopupInfo infoTemp : accordInfoList) {
                    rect = ImageIdentifyUtil.imageCompare(largeBitmap, infoTemp.getBitmap());
                    if (rect != null) {
                        LLog.d("Def 识别到图了 " + infoTemp.getName());
                        break;
                    }
                }
                if (rect != null) {
                    SocketUtil.setDelay(SleepConfig.SLEEP_TIME_1000);
                } else {
                    LLog.d("Def 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    int num = getEndNum(SocketUtil.currentExecNum);
                    SocketUtil.currentExecNum += (num + 1);
                }
                if (largeBitmap != null) {
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
        }
        BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
    }

    /**
     * 定义Defined的指令处理
     * IFDEF#图片名称#比较像素点个数#像素误差offset#图像截屏倍数#算法类型 (例:IFDEF#xx图片#10#5#2#1)
     * ENDIF
     */
    public static void dealWithPixDefinedCmd(String instructStr) {
        getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        long time = System.currentTimeMillis();
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        if (pics.length >= 6) {
            ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
            SystemClock.sleep(50);
            Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
            if (largeBitmap == null) {
                largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            }
            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }
            if (largeBitmap != null) {
                List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                    for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                        if (info != null && PlatformConfig.IMG_TYPE_4.equals(info.getType()) && info.getName().contains(pics[1])) {
                            accordInfoList.add(info);
                        }
                    }
                }
                if (accordInfoList.size() == 0) {
                    LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                }
                Rect rect = null;
                for (PopupInfo infoTemp : accordInfoList) {
                    rect = ImageIdentifyUtil.imageCompareByPix(largeBitmap, infoTemp.getBitmap(), Integer.parseInt(pics[2]), Integer.parseInt(pics[3]), Integer.parseInt(pics[4]), Integer.parseInt(pics[5]));
                    if (rect != null) {
                        LLog.d("Def 识别到图了 " + infoTemp.getName());
                        break;
                    }
                }
                if (rect != null) {
                    SocketUtil.setDelay(SleepConfig.SLEEP_TIME_1000);
                } else {
                    LLog.d("Def 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    int num = getEndNum(SocketUtil.currentExecNum);
                    SocketUtil.currentExecNum += (num + 1);
                }
                if (largeBitmap != null) {
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
        }
        BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
    }

    /**
     * 定义Defined的指令处理(单模板算法)
     * IFDEF#断网自动连接#单模板#匹配个数#分数阈值
     * IFDEF#名称#单模板#1#0.8
     * <p>
     * 点图片#断网自动连接
     * ENDIF
     */
    public static void dealWithDefinedSingleMatchCmd(String instructStr) {
        getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        long time = System.currentTimeMillis();
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        if (pics.length >= 5) {
            ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
            SystemClock.sleep(50);
            Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
            if (largeBitmap == null) {
                largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            }
            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }
            LLog.e("------------------------------------" + largeBitmap);
            if (largeBitmap != null) {
                List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                    for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                        if (info != null && PlatformConfig.IMG_TYPE_4.equals(info.getType()) && info.getName().contains(pics[1])) {
                            accordInfoList.add(info);
                        }
                    }
                }
                if (accordInfoList.size() == 0) {
                    LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                }
                TmpMatchResult[] rectRts = null;
                TmpMatchResult rect = null;
                for (PopupInfo infoTemp : accordInfoList) {
                    rectRts = new Util().getSingleResult(largeBitmap, infoTemp.getBitmap(), infoTemp.getName(), Integer.parseInt(StringUtil.getNumbers(pics[3])), Double.parseDouble(pics[4]));
                    if (rectRts != null && rectRts.length > 0) {
                        rect = rectRts[0];
                        LLog.d("Def 识别到图了 " + infoTemp.getName() + " 坐标:" + rect.nCentX * ScreenShotFb.IMAGE_SCALE_RATIO + "," + rect.nCentY * ScreenShotFb.IMAGE_SCALE_RATIO);
                        break;
                    }
                }
                if (rectRts != null && rectRts.length > 0) {
                    SocketUtil.setDelay(SleepConfig.SLEEP_TIME_1000);
                } else {
                    LLog.d("Def 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    int num = getEndNum(SocketUtil.currentExecNum);
                    SocketUtil.currentExecNum += (num + 1);
                }
                if (largeBitmap != null) {
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
        }
        BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
    }


    /**
     * 定义Defined的指令处理
     * IFDEF#名称#0.8#点击
     * ENDIF
     * /**单模板
     * ---------------------------
     * 循环次数#40
     * IFDEF#测试#0.6#点击
     * 睡眠#2000
     * ENDIF
     * 执行上几句#3
     * ----------------------------
     * IFDEF#名称#阈值(0-1)
     */
    public static void dealWithCvDefinedCmd(String instructStr) {
        getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        long time = System.currentTimeMillis();
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        if (pics.length >= 4) {
            ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
            SystemClock.sleep(50);
            Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
            if (largeBitmap == null) {
                largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            }
            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }
            LLog.e("------------------------------------" + largeBitmap);
            if (largeBitmap != null) {
                List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                    for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                        if (info != null && PlatformConfig.IMG_TYPE_4.equals(info.getType()) && info.getName().contains(pics[1])) {
                            accordInfoList.add(info);
                        }
                    }
                }
                if (accordInfoList.size() == 0) {
                    LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                }
                TmpMatchResult[] result = null;
                Util util = new Util();
                for (PopupInfo infoTemp : accordInfoList) {
                    result = util.getSingleResult(largeBitmap, infoTemp.getBitmap(), infoTemp.getName(), 1, Double.parseDouble(pics[2]));
                    if (result != null) {
                        LLog.d("Def 识别到图了 " + infoTemp.getName());
                        break;
                    }
                }
                if (result != null && result.length > 0) {
                    SocketUtil.setDelay(SleepConfig.SLEEP_TIME_1000);
                } else {
                    LLog.d("Def 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    int num = getEndNum(SocketUtil.currentExecNum);
                    SocketUtil.currentExecNum += (num + 1);
                }
                if (largeBitmap != null) {
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
        }
        BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
    }


    /**
     * 此方法目前只适用于竖屏状态
     * 滑动验证码识别#b#84#345#456#574#85#60#55
     * 滑动验证码识别#拖动条x#拖动条y#凹凸滑块像素offset(凹凸滑块右边界的x值)#topy(矩形框上边界y值)#bottomy(矩形框下边界y值)#偏移x(取凹凸滑块宽度的一半,可调整)#是否旋转
     */
    public static void dealWithHuaDongCmd(String instructStr) {
        getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        if (pics.length == 8) {
            ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
            SystemClock.sleep(50);
            Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture_clear();
            if (largeBitmap == null) {
                largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            }
            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }
            if (largeBitmap != null) {
                List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                if (PopupUtil.getCompareNameList() != null && PopupUtil.getCompareNameList().size() > 0) {  //弹窗图片
                    for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                        if (info != null && PlatformConfig.IMG_TYPE_4.equals(info.getType()) && info.getName().contains("hd")) {
                            accordInfoList.add(info);
                        }
                    }
                }
                if (accordInfoList.size() <= 0) {
                    LLog.e("CvDef 未找到需要比对的图 ");
                } else {
                    int[][] rect = new int[2][];
                    for (int i = 0; i < accordInfoList.size(); i++) {
                        rect = TensorFlowUtils.getHDpixUsetemp(largeBitmap, accordInfoList.get(i).getBitmap(), Integer.parseInt(pics[3]), Integer.parseInt(pics[4]), Integer.parseInt(pics[5]));
                        if (rect != null) {
                            break;
                        }
                    }
                    if (rect != null) {
                        int time = (int) (Math.random() * 1000) + 500;
                        String cmd = "长按#" + pics[1] + "#" + pics[2] + "#" + "1000" + "#" + "滑动#" + pics[1] + "#" + pics[2] + "#" + (rect[0][0] + Integer.parseInt(pics[6])) + "#" + (rect[0][1] + pics[4]) + "#" + time;
                        String instruct = InstructUtil.script2Cmd(cmd);
                        SocketUtil.sendInstruct(instruct, false);
                        LLog.e("滑动匹配 识别到: " + "---坐标:" + cmd);
                        SocketUtil.continueExec();//继续播放脚本
                    } else {
                        LLog.e("滑动匹配失败,未识别到: " + pics[1]);
                        SocketUtil.continueExec();//继续播放脚本
                    }
                }
            }
            if (largeBitmap != null) {
                largeBitmap.recycle();
                largeBitmap = null;
            }
        }
    }

    /**
     * 边界黑白明显的尽量使用1,如果滑块阴影较为明显,可以用2
     * 1.滑动验证码识别#200#1244#340#630#1130#90#120#0#3#30#0//如果边界像素差值大于30时说明使用单列像素对比,差值为120,此时跨度阈值无用,像素跨度为3表示第一列与第三列对比
     * 2.滑动验证码识别#200#1244#340#630#1130#90#5#5#4#30#0//如果边界像素差值小于30时说明使用渐变对比,差值为5,阈值为5,则每列像素渐变差范围为0-10,像素跨度为4则比较四列像素递减值
     * 2.滑动验证码识别#200#1244#340#200#640#60#70#0#4#30#0//如果边界像素差值小于30时说明使用渐变对比,差值为5,阈值为5,则每列像素渐变差范围为0-10,像素跨度为4则比较四列像素递减值
     * 滑动验证码识别#拖动条x#拖动条y#滑块位置offset#topy#bottomy#像素比较数量#边界像素差值#跨度阈值#滑块边界对比像素跨度#滑动偏移#方向(横向1)
     */
    public static void dealWithHuaDong(String instructStr) {
        getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
        SystemClock.sleep(50);
        Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture_clear();
        if (largeBitmap == null) {
            largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
        }

        if (largeBitmap != null) {

            int[] rect = TensorFlowUtils.getHDpixUsepix(largeBitmap, Integer.parseInt(pics[3]), Integer.parseInt(pics[4]), Integer.parseInt(pics[5]), Integer.parseInt(pics[6]), Integer.parseInt(pics[7]), Integer.parseInt(pics[8]), Integer.parseInt(pics[9]));
            if (rect != null) {
                int time = (int) (Math.random() * 1000) + 800;
                String cmd = "滑动#" + pics[1] + "#" + pics[2] + "#" + (rect[0] + Integer.parseInt(pics[10])) + "#" + (pics[2]) + "#" + time;
                String instruct = InstructUtil.script2Cmd(cmd);

                if ("1".equals(pics[11])) {
                    instruct = ReorganizeUitl.landscapeToPortrait(instruct);
                }
                SocketUtil.sendInstruct(instruct, false);
                SocketUtil.continueExec();//继续播放脚本
            } else {
                LLog.e("滑动匹配失败,未识别到: " + pics[1]);
                SocketUtil.continueExec();//继续播放脚本
            }
        } else {
            SocketUtil.continueExec();//继续播放脚本

        }

    }

    /**
     * @param instructStr 闪点验证码#0.5#130#500#950#1600#180#120#70#18#100#8
     */
    public static void dealWithBlinkPoint(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length < 12) {
            datas = new String[]{"0.5", "130", "500", "950", "1600", "180", "120", "70", "18", "100", "8"};
        }
        new GetPurplesTask(arrayList -> {
            if (arrayList.size() >= 3) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < arrayList.size() - 1; i++) {
                    String cmd = "";
                    cmd = "长按#" + arrayList.get(i).centerX() + "#" + arrayList.get(i).centerY() + "#1000#滑动#" + arrayList.get(i).centerX() + "#" + arrayList.get(i).centerY() + "#" + arrayList.get(i + 1).centerX() + "#" + arrayList.get(i + 1).centerY() + "#500#";
                    stringBuilder.append(cmd);
                }
                stringBuilder.substring(0, stringBuilder.length() - 2);
                Log.e(LOG_TAG, stringBuilder.toString());
                String command = InstructUtil.script2Cmd(stringBuilder.toString());
                SocketUtil.sendInstruct(command, false);
                Log.e(LOG_TAG, (arrayList.get(0).centerX()) + "--" + (arrayList.get(0).centerY()));
            } else {
                Log.e(LOG_TAG, "识别失败");
            }

            SocketUtil.continueExec();
        }).execute(datas);
    }


    /**
     * /**
     * <p>
     * 输入图像
     * 起始点坐标
     * 查找区域左上角坐标
     * 查找区域右下角坐标
     * 滑动块的宽度
     * 滑动块的高度
     * 滑动块的弧长度
     * 图像旋转标志    0竖屏 1横屏
     * 终点位置
     * (横屏识别时坐标全部需要横屏转竖屏)
     * <p>
     * 查左上 -------------------           .......  查左上----------
     * |                        |           . 拖  .  |             |
     * |  ....                  |           . 动  .  |             |
     * |   ; .凹凸滑块           |          . 条  .  |             |
     * |  ....                  |           .......  |             |
     * |                        |                    |             |
     * ---------------------- 查右下                 |             |
     * ...........                                    --------- 查右下
     * .  拖动条  .
     * ...........
     * <p>
     * <p> 注意:所有的坐标点都得转换成竖屏坐标
     * <p>
     * 滑动验证码识别#拖动条x#拖动条y#查区左上角x值#查区左上角y角#查区右下角x值#查区右下角y值#凹凸滑块宽度#凹凸滑块高度#凹凸滑块弧缺口直径#是否旋转#滑动时间#变速/匀速
     * qq竖向验证码示例:滑动验证码识别#222#1035#50#385#1030#940#130#130#40#0#1500#匀速#500
     * qq横向验证码示例:滑动验证码识别#185#700#252#585#725#1425#107#107#30#1#1500#匀速#500
     * <p>
     * 滑动验证码识别#228#1300#167#794#909#1214#105#105#38#0#1500#匀速
     */
    public static void dealWithHuaDongCmdNew(String instructStr) {

        boolean needRotate = false;
        try {
            SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 13) {
                ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
                SystemClock.sleep(300);
                Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture_clear();
                if (largeBitmap == null) {
                    if (pics.length == 14) {
                        long time = Long.parseLong(pics[13]);
                        largeBitmap = CaptureUtil.getBitMap_clear(ScriptApplication.getInstance(), time);
                    } else {
                        largeBitmap = CaptureUtil.getBitMap_clear(ScriptApplication.getInstance());
                    }
                }

                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    needRotate = true;
                }

                if (largeBitmap != null) {
                    Log.e("--^_^-->", "dealWithHuaDongCmdNew  " + largeBitmap);
                    Util util = new Util();
                    boolean type = "0".equals(pics[10]);
                    SlidingPoint res = util.getSildingPosition(largeBitmap, new SlidingPoint(Integer.parseInt(pics[1]), Integer.parseInt(pics[2])), new SlidingPoint(Integer.parseInt(pics[3]), Integer.parseInt(pics[4])),
                            new SlidingPoint(Integer.parseInt(pics[5]), Integer.parseInt(pics[6])), Integer.parseInt(pics[7]), Integer.parseInt(pics[8]), Integer.parseInt(pics[9]), type);
                    Log.e("--^_^-->", "res ==============  " + res);
                    if (res != null) {
                        int time = Integer.parseInt(StringUtil.getNumber(pics[11]));
                        String cmd = null;
                        Log.e("--^_^-->", "dealWithHuaDongCmdNew  " + res.x + "  " + res.y);
                        if ("匀速".equals(pics[12])) {
                            time = time / 2;
                            cmd = "滑动#" + pics[1] + "#" + pics[2] + "#" + res.x + "#" + res.y + "#" + time;
                        } else if ("变速".equals(pics[12])) {
                            int x12 = (Integer.parseInt(StringUtil.getNumber(pics[1])) + (res.x - Integer.parseInt(StringUtil.getNumber(pics[1]))) / 3);
                            int x13 = (Integer.parseInt(StringUtil.getNumber(pics[1])) + (res.x - Integer.parseInt(StringUtil.getNumber(pics[1]))) / 3 * 2);
                            String long_click_1 = ScriptConstants.LONG_CLICK_CMD
                                    + ScriptConstants.SPLIT + pics[1]
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + "100";

                            String swipe_1 = ScriptConstants.SWIPE_CMD
                                    + ScriptConstants.SPLIT + pics[1]
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + x12
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + "100";

                            String long_click_2 = ScriptConstants.LONG_CLICK_CMD
                                    + ScriptConstants.SPLIT + x12
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + "500";

                            String swipe_2 = ScriptConstants.SWIPE_CMD
                                    + ScriptConstants.SPLIT + x12
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + x13
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + time;

                            String long_click_3 = ScriptConstants.LONG_CLICK_CMD
                                    + ScriptConstants.SPLIT + x13
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + "500";

                            String swipe_3 = ScriptConstants.SWIPE_CMD
                                    + ScriptConstants.SPLIT + x13
                                    + ScriptConstants.SPLIT + pics[2]
                                    + ScriptConstants.SPLIT + res.x
                                    + ScriptConstants.SPLIT + res.y
                                    + ScriptConstants.SPLIT + time;

                            cmd = long_click_1 + ScriptConstants.SPLIT + swipe_1 + ScriptConstants.SPLIT + long_click_2 + ScriptConstants.SPLIT
                                    + swipe_2 + ScriptConstants.SPLIT + long_click_3 + ScriptConstants.SPLIT + swipe_3;
                        }


                        String instruct = InstructUtil.script2Cmd(cmd);
                        if (needRotate) {
                            instruct = ReorganizeUitl.landscapeToPortrait(InstructUtil.script2Cmd(cmd));
                        }
                        SocketUtil.sendInstruct(instruct, false);
                        LLog.e("滑动匹配 识别到: " + "---坐标:" + cmd);
                        SocketUtil.continueExec();//继续播放脚本
                    } else {
                        LLog.e("滑动匹配失败,未识别到: " + pics[1]);
                        SocketUtil.continueExec();//继续播放脚本
                    }
                } else {
                    LLog.e("截图失败，重新截图" + pics[1]);
                    SocketUtil.continueExec();//继续播放脚本
                }
            }
            if (pics.length == 8) {
                dealWithHuaDongCmd(instructStr);
            }
            if (pics.length == 12) {
                dealWithHuaDong(instructStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 定义NoDefined的指令处理
     * IFNODEF#诛仙游戏图标
     * ENDIF
     */
    public static void dealWithNoDefinedCmd(String instructStr) {

        getPictureDetected();

        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        long time = System.currentTimeMillis();
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        if (pics.length >= 1) {
            Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }
            LLog.e("------------------------------------" + largeBitmap);
            if (largeBitmap != null) {
                List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                    for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                        if (info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && info.getName().contains(pics[1])) {
                            accordInfoList.add(info);
                        }
                    }
                }
                if (accordInfoList.size() == 0) {
                    LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                }
                Rect rect = null;
                for (PopupInfo infoTemp : accordInfoList) {
                    rect = ImageIdentifyUtil.imageCompare(largeBitmap, infoTemp.getBitmap());
                    if (rect != null) {
                        LLog.d("Def 识别到图了 " + infoTemp.getName());
                        break;
                    }
                }
                if (rect != null) {
                    int num = getEndNum(SocketUtil.currentExecNum);
                    SocketUtil.currentExecNum += (num + 1);
                } else {
                    LLog.d("NoDef 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    SocketUtil.setDelay(SleepConfig.SLEEP_TIME_1000);
                }
                if (largeBitmap != null) {
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
        }
        BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
    }


    /**
     * 定义NoDefined的指令处理
     * IFNODEF#图片名称#比较像素点个数#像素误差offset#图像截屏倍数#算法类型 (例:IFNODEF#xx图片#10#5#2#1)
     * ENDIF
     */
    public static void dealWithPixNoDefinedCmd(String instructStr) {
        try {
            getPictureDetected();
            SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
            long time = System.currentTimeMillis();
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 6) {
                Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                }
                LLog.e("------------------------------------" + largeBitmap);
                if (largeBitmap != null) {
                    List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                    if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                            if (info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && info.getName().contains(pics[1])) {
                                accordInfoList.add(info);
                            }
                        }
                    }
                    if (accordInfoList.size() == 0) {
                        LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                    }
                    Rect rect = null;
                    for (PopupInfo infoTemp : accordInfoList) {
                        rect = ImageIdentifyUtil.imageCompareByPix(largeBitmap, infoTemp.getBitmap(), Integer.parseInt(pics[2]), Integer.parseInt(pics[3]), Integer.parseInt(pics[4]), Integer.parseInt(pics[5]));
                        if (rect != null) {
                            LLog.d("Def 识别到图了 " + infoTemp.getName());
                            break;
                        }
                    }
                    if (rect != null) {
                        int num = getEndNum(SocketUtil.currentExecNum);
                        SocketUtil.currentExecNum += (num + 1);
                    } else {
                        LLog.d("NoDef 未识别到图 " + pics[1] + ScriptConstants.PNG);
                        SocketUtil.setDelay(SleepConfig.SLEEP_TIME_1000);
                    }
                    if (largeBitmap != null) {
                        largeBitmap.recycle();
                        largeBitmap = null;
                    }
                }
            }
            BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * 定义NoDefined的指令处理(单模板算法)
     * IFNODEF#诛仙游戏图标#单模板#匹配个数#分数阈值
     * IFNODEF#名称#单模板#1#0.8
     * ENDIF
     */
    public static void dealWithNoDefinedSingleMatch(String instructStr) {
        getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        long time = System.currentTimeMillis();
        String[] pics = instructStr.split(ScriptConstants.SPLIT);
        if (pics.length >= 5) {
            Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }
            LLog.e("------------------------------------" + largeBitmap);
            if (largeBitmap != null) {
                List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                    for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                        if (info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && info.getName().contains(pics[1])) {
                            accordInfoList.add(info);
                        }
                    }
                }
                if (accordInfoList.size() == 0) {
                    LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                }
                TmpMatchResult[] rectRts = null;
                TmpMatchResult rect = null;
                for (PopupInfo infoTemp : accordInfoList) {
                    rectRts = new Util().getSingleResult(largeBitmap, infoTemp.getBitmap(), infoTemp.getName(), Integer.parseInt(StringUtil.getNumbers(pics[3])), Double.parseDouble(pics[4]));
                    if (rectRts != null && rectRts.length > 0) {
                        rect = rectRts[0];
                        LLog.d("Def 识别到图了 " + infoTemp.getName() + " 坐标:" + rect.nCentX * ScreenShotFb.IMAGE_SCALE_RATIO + "," + rect.nCentY * ScreenShotFb.IMAGE_SCALE_RATIO);
                        break;
                    }
                }
                if (rectRts != null && rectRts.length > 0) {
                    int num = getEndNum(SocketUtil.currentExecNum);
                    SocketUtil.currentExecNum += (num + 1);
                } else {
                    LLog.d("NoDef 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    SocketUtil.setDelay(SleepConfig.SLEEP_TIME_1000);
                }
                if (largeBitmap != null) {
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
        }
        BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
    }


    /**
     * 警报处理
     * 报警#B_ALERT_AND_PAUSE_KEFU_DO
     */
    public static void dealWithAlert(String instructStr) {
        if (Constants.execServerScript) {
            if (!StringUtil.isEmpty(instructStr)) {
                if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                    try {
                        String[] datas = instructStr.split(ScriptConstants.SPLIT);
                        if (datas.length >= 2) {
                            SpUtil.putKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false);
                            SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, datas[1]);
                            SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, true);
                            RecordFloatView.updateMessage("报警:" + datas[1]);
                            BaseOrderHelper.callAlertAndFinish(datas[1], "脚本执行未识别到指定界面_NULL");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {

                }
            }
        } else {
            RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayFinished();
        }
    }


    /**
     * 获取从IFDEF到ENDIF之间有几个指令
     *
     * @param currentNum
     * @return
     */
    public static int getEndNum(int currentNum) {
        int defNum = 0;
        int num = 0;
        for (int i = SocketUtil.currentExecNum; i < SocketUtil.scriptList.size(); i++) {
            if (SocketUtil.scriptList.get(i).contains(ScriptConstants.IF_DEF_CMD) || SocketUtil.scriptList.get(i).contains(ScriptConstants.IF_NO_DEF_CMD)) {
                ++defNum;
            }
            if (ScriptConstants.ENDIF_CMD.equals(SocketUtil.scriptList.get(i))) {
                if (defNum == 0) {
                    return i - currentNum;
                }
                --defNum;
            }
        }
        return num;
    }


    /**
     * 初始化弹框图片
     *
     * @param imgPath 需要加载的图片路径
     */
    public static void initPopupImgs(String imgPath) {
        PopupUtil.getPopupImgsList().addAll((Vector<PopupInfo>) ImageUtil.convertImages2BitmapList(imgPath, true));
        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
            if (PopupUtil.getAlertNameList().contains(info.getName())) {
                info.setType(PlatformConfig.IMG_TYPE_1);
            } else if (PopupUtil.getPlatformNameList().contains(info.getName())) {
                info.setType(PlatformConfig.IMG_TYPE_2);
            } else if (PopupUtil.getPopupNameList().contains(info.getName())) {
                info.setType(PlatformConfig.IMG_TYPE_3);
            } else if (PopupUtil.getCompareNameList().contains(info.getName())) {
                info.setType(PlatformConfig.IMG_TYPE_4);
            } else if (PopupUtil.getSampleNameList().contains(info.getName())) {
                info.setType(PlatformConfig.IMG_TYPE_5);
            } else if (PopupUtil.getModuleNameList().contains(info.getName())) {
                info.setType(PlatformConfig.IMG_TYPE_6);
            }
        }
    }


    /**
     * 检测界面弹窗并关闭
     *
     * @return 是否有弹窗
     */
    public static boolean getPictureDetected() {
        if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
            LLog.d("检测弹窗开始----------------->");
            long time1 = System.currentTimeMillis();
            ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
            SystemClock.sleep(50);
            Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
//        Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCaptureX();

            if (largeBitmap == null) {
                LLog.d("检测弹窗2次截图----------------->");
                ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
                SystemClock.sleep(200);
//            largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCaptureX();
                largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
            }

            if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
            }

            if (largeBitmap != null) {

                Vector<PopupInfo> tmpPops = reSortPic(PopupUtil.getPopupImgsList());
                LLog.d("ScriptUtil.tmpPops():" + tmpPops.toString());
                for (PopupInfo info : tmpPops) {
                    time1 = System.currentTimeMillis();
                    if ((info != null && (PlatformConfig.IMG_TYPE_1.equals(info.getType()) || PlatformConfig.IMG_TYPE_3.equals(info.getType())))) {
                        Rect rect = ImageIdentifyUtil.imageCompare(largeBitmap, info.getBitmap());
                        if (rect != null) {
                            if (PlatformConfig.IMG_TYPE_1.equals(info.getType())) {
                                if (Constants.execServerScript) {
                                    LLog.e("检测到警报图片！");
                                    RecordFloatView.updateMessage("检测到警报图片:" + info.getName());
                                    if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                                        if (info.getName().contains("更新")) {
                                            BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_DELAY_REDO_GAME_UPDATE, info.getName().replace(".png", "") + "_" + "坐标_" + rect.centerX() + "_" + rect.centerY());
                                        } else if (info.getName().contains("封禁")) {
                                            BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_ABOLISH_FORBIDEN_USER, info.getName().replace(".png", "") + "_" + "坐标_" + rect.centerX() + "_" + rect.centerY());
                                        }
                                    } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                                        if (info.getName().contains("人工协助")) {
                                            dealWithAssistant("人工协助");
                                        }
                                    }
                                }
                            } else if (PlatformConfig.IMG_TYPE_3.equals(info.getType())) {
                                //弹窗四种类型
                                //活动.png
                                //活动-点-100_100.png
                                //活动-区-100_100_200_200.png
                                //活动-区-100_100_200_200-点-100_100.png
                                LLog.e("检测到弹窗图片！" + "图片名字:" + info.getName() + " 坐标:" + rect.centerX() + "," + rect.centerY());
                                try {
                                    if (!StringUtil.isEmpty(info.getName())) {
                                        String command = "";
                                        String[] strs = info.getName().split(ScriptConstants.PNG_SPACE);
                                        if (info.getName().contains("点") && !info.getName().contains("区")) {
                                            if (strs.length > 2) {
                                                String pointStr = strs[strs.length - 1].substring(0, strs[strs.length - 1].lastIndexOf(ScriptConstants.PNG));
                                                String[] points = pointStr.split(ScriptConstants.PNG_SPLIT);
                                                if (points.length >= 1) {
                                                    command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + points[0] + ScriptConstants.SPLIT + points[1];
                                                }
                                            }
                                        } else if (info.getName().contains("区") && !info.getName().contains("点")) {
                                            if (strs.length > 2) {
                                                String pointStr = strs[strs.length - 1].substring(0, strs[strs.length - 1].lastIndexOf(ScriptConstants.PNG));
                                                String[] points = pointStr.split(ScriptConstants.PNG_SPLIT);
                                                if (points.length >= 4) {
                                                    command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + RandomUtil.getRandomNum(Integer.parseInt(points[0]), Integer.parseInt(points[2]))
                                                            + ScriptConstants.SPLIT + +RandomUtil.getRandomNum(Integer.parseInt(points[1]), Integer.parseInt(points[3]));
                                                }
                                            }
                                        } else if (info.getName().contains("点") && info.getName().contains("区")) {
                                            if (strs.length >= 5) {
                                                String areaStr = strs[2];
                                                String pointStr = strs[strs.length - 1].substring(0, strs[strs.length - 1].lastIndexOf(ScriptConstants.PNG));
                                                String[] areas = areaStr.split(ScriptConstants.PNG_SPLIT);
                                                String[] points = pointStr.split(ScriptConstants.PNG_SPLIT);
                                                if (rect.centerX() >= Integer.parseInt(areas[0]) && rect.centerX() <= Integer.parseInt(areas[2])
                                                        && rect.centerY() >= Integer.parseInt(areas[1]) && rect.centerY() <= Integer.parseInt(areas[3])) {
                                                    command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + points[0] + ScriptConstants.SPLIT + points[1];
                                                }
                                            }
                                        } else {  //需要点击关闭的图片
                                            command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + rect.centerX() + ScriptConstants.SPLIT + rect.centerY();
                                        }
                                        if (!StringUtil.isEmpty(command)) {
                                            /*if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                                                command = ReorganizeUitl.portraitToLandscape(command);
                                            }*/
                                            SocketUtil.sendInstruct(command, false);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            BrushTask.comparePicTime.add(System.currentTimeMillis() - time1);
                            return true;
                        }
                    }
                }

                largeBitmap.recycle();
                largeBitmap = null;
                BrushTask.comparePicTime.add(System.currentTimeMillis() - time1);
            } else {
                LLog.e("弹窗截图失败--------------->");
            }
        } else {
            LLog.d("PopupUtil.getPopupImgsList()为空");
        }
        return false;
    }


    /**
     * 处理点击图片的指令
     * 点图片#弹窗
     *
     * @param instructStr
     */
    public static void dealWithTapPicture(String instructStr) {
        try {
            long time = System.currentTimeMillis();
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 1) {
                Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                }
                if (largeBitmap != null) {
                    List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                    if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                            if ((info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && (info.getName()).contains(pics[1]))) {
                                accordInfoList.add(info);
                            }
                        }
                    }
                    if (accordInfoList.size() == 0) {
                        LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                    }
                    Rect rect = null;
                    for (PopupInfo infoTemp : accordInfoList) {
                        rect = ImageIdentifyUtil.imageCompare(largeBitmap, infoTemp.getBitmap());
                        if (rect != null) {
                            LLog.e("点图片", "识别到图了 " + infoTemp.getName());
                            LLog.e("检测到要点击的图片！" + "图片名字:" + infoTemp.getName() + " 坐标:" + rect.centerX() + "," + rect.centerY());
                            break;
                        }
                    }
                    if (rect != null) {
                        //需要点击关闭的图片
                        String command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + rect.centerX() + ScriptConstants.SPLIT + rect.centerY();
                        /*if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                            command = ReorganizeUitl.portraitToLandscape(command);
                        }*/
                        SocketUtil.sendInstruct(command, false);
                    } else {
                        LLog.e("点图片 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    }
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
            BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理点击图片的指令
     * 点图片#弹窗#比较像素点个数#像素误差offset#图片截屏倍数#算法类型
     * 例:点图片#弹窗#10#5#2#1
     *
     * @param instructStr
     */
    public static void dealWithPixTapPicture(String instructStr) {
        try {
            long time = System.currentTimeMillis();
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 6) {
                Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                }
                if (largeBitmap != null) {
                    List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                    if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                            if ((info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && (info.getName()).contains(pics[1]))) {
                                accordInfoList.add(info);
                            }
                        }
                    }
                    if (accordInfoList.size() == 0) {
                        LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                    }
                    Rect rect = null;
                    for (PopupInfo infoTemp : accordInfoList) {
                        rect = ImageIdentifyUtil.imageCompareByPix(largeBitmap, infoTemp.getBitmap(), Integer.parseInt(pics[2]), Integer.parseInt(pics[3]), Integer.parseInt(pics[4]), Integer.parseInt(pics[5]));
                        if (rect != null) {
                            LLog.e("点图片", "识别到图了 " + infoTemp.getName());
                            LLog.e("检测到要点击的图片！" + "图片名字:" + infoTemp.getName() + " 坐标:" + rect.centerX() + "," + rect.centerY());
                            break;
                        }
                    }
                    if (rect != null) {
                        //需要点击关闭的图片
                        String command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + rect.centerX() + ScriptConstants.SPLIT + rect.centerY();
                        /*if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                            command = ReorganizeUitl.portraitToLandscape(command);
                        }*/
                        SocketUtil.sendInstruct(command, false);
                    } else {
                        LLog.e("点图片 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    }
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
            BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 处理点击图片的指令(单模板匹配)
     * 点图片#弹窗#匹配个数#分数阈值
     * 点图片#弹窗#1#0.6
     *
     * @param instructStr
     */
    public static void dealWithTapPictureSingleMatch(String instructStr) {
        try {
            long time = System.currentTimeMillis();
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 4) {
                Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                }
                if (largeBitmap != null) {
                    List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                    if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                            if ((info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && (info.getName()).contains(pics[1]))) {
                                accordInfoList.add(info);
                            }
                        }
                    }
                    if (accordInfoList.size() == 0) {
                        LLog.e("Def 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                    }
                    TmpMatchResult[] rectRts = null;
                    TmpMatchResult rect = null;
                    for (PopupInfo infoTemp : accordInfoList) {
                        rectRts = new Util().getSingleResult(largeBitmap, infoTemp.getBitmap(), infoTemp.getName(), Integer.parseInt(StringUtil.getNumbers(pics[2])), Double.parseDouble(pics[3]));
                        if (rectRts != null && rectRts.length > 0) {
                            rect = rectRts[0];
                            LLog.e("点图片", "识别到图了 " + infoTemp.getName());
                            LLog.e("检测到要点击的图片！" + "图片名字:" + infoTemp.getName() + " 坐标:" + rect.nCentX * ScreenShotFb.IMAGE_SCALE_RATIO + "," + rect.nCentY * ScreenShotFb.IMAGE_SCALE_RATIO);
                            break;
                        }
                    }
                    if (rectRts != null && rectRts.length > 0) {
                        //需要点击关闭的图片
                        String command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + rect.nCentX * ScreenShotFb.IMAGE_SCALE_RATIO + ScriptConstants.SPLIT + rect.nCentY * ScreenShotFb.IMAGE_SCALE_RATIO;
                        /*if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                            command = ReorganizeUitl.portraitToLandscape(command);
                        }*/
                        SocketUtil.sendInstruct(command, false);
                    } else {
                        LLog.e("点图片 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    }
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
            BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理点击图片偏移量
     * 点击图片偏移量#图片名字#偏移X#偏移Y
     * 例:点击图片偏移量#每日任务#-100#-100
     *
     * @param instructStr
     */
    public static void dealWithTapPictureVector(String instructStr) {
        try {
            long time = System.currentTimeMillis();
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 4) {
                Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                }
                if (largeBitmap != null) {
                    List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                    if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                            if ((info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && (info.getName()).contains(pics[1]))) {
                                accordInfoList.add(info);
                            }
                        }
                    }
                    if (accordInfoList.size() == 0) {
                        LLog.e("点击图片偏移量 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                    }
                    Rect rect = null;
                    for (PopupInfo infoTemp : accordInfoList) {
                        rect = ImageIdentifyUtil.imageCompare(largeBitmap, infoTemp.getBitmap());
                        if (rect != null) {
                            LLog.e("点击图片偏移量", "识别到图了 " + infoTemp.getName() + " 坐标:" + rect.centerX() + "," + rect.centerY());
                            break;
                        }
                    }
                    if (rect != null) {
                        //需要点击的点
                        String command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + (rect.centerX() + Integer.parseInt(pics[2])) + ScriptConstants.SPLIT + (rect.centerY() + Integer.parseInt(pics[3]));
                            /*if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                                command = ReorganizeUitl.portraitToLandscape(command);
                            }*/
                        SocketUtil.sendInstruct(command, false);
                    } else {
                        LLog.e("点击图片偏移量 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    }
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
            BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理点击图片偏移量
     * 点击图片偏移量#图片名字#偏移X#偏移Y#比较像素点个数#像素误差offset#图片截屏倍数#算法类型
     * 例:点击图片偏移量#每日任务#-100#-100#10#5#2#1
     *
     * @param instructStr
     */
    public static void dealWithPixTapPictureVector(String instructStr) {
        try {
            long time = System.currentTimeMillis();
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 8) {
                Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                }
                if (largeBitmap != null) {
                    List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                    if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                            if ((info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && (info.getName()).contains(pics[1]))) {
                                accordInfoList.add(info);
                            }
                        }
                    }
                    if (accordInfoList.size() == 0) {
                        LLog.e("点击图片偏移量 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                    }
                    Rect rect = null;
                    for (PopupInfo infoTemp : accordInfoList) {
                        rect = ImageIdentifyUtil.imageCompareByPix(largeBitmap, infoTemp.getBitmap(), Integer.parseInt(pics[4]), Integer.parseInt(pics[5]), Integer.parseInt(pics[6]), Integer.parseInt(pics[7]));
                        if (rect != null) {
                            LLog.e("点击图片偏移量", "识别到图了 " + infoTemp.getName() + " 坐标:" + rect.centerX() + "," + rect.centerY());
                            break;
                        }
                    }
                    if (rect != null) {
                        //需要点击的点
                        String command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + (rect.centerX() + Integer.parseInt(pics[2])) + ScriptConstants.SPLIT + (rect.centerY() + Integer.parseInt(pics[3]));
                            /*if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                                command = ReorganizeUitl.portraitToLandscape(command);
                            }*/
                        SocketUtil.sendInstruct(command, false);
                    } else {
                        LLog.e("点图片 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    }
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
            BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理点击图片偏移量(单模板匹配)
     * 点击图片偏移量#图片名字#偏移X#偏移Y#匹配个数#分数阈值
     * 例:点击图片偏移量#每日任务#-100#-100#1#0.6
     *
     * @param instructStr
     */
    public static void dealWithTapPicVectorSingleMatch(String instructStr) {
        try {
            long time = System.currentTimeMillis();
            String[] pics = instructStr.split(ScriptConstants.SPLIT);
            if (pics.length >= 6) {
                Bitmap largeBitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
                if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                    largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                }
                if (largeBitmap != null) {
                    List<PopupInfo> accordInfoList = new ArrayList<>();//符合条件的图片
                    if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //弹窗图片
                        for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                            if ((info != null && (PlatformConfig.IMG_TYPE_4.equals(info.getType())) && (info.getName()).contains(pics[1]))) {
                                accordInfoList.add(info);
                            }
                        }
                    }
                    if (accordInfoList.size() == 0) {
                        LLog.e("点击图片偏移量 未找到需要比对的图 " + pics[1] + ScriptConstants.PNG);
                    }
                    TmpMatchResult[] rectRts = null;
                    TmpMatchResult rect = null;
                    for (PopupInfo infoTemp : accordInfoList) {
                        rectRts = new Util().getSingleResult(largeBitmap, infoTemp.getBitmap(), infoTemp.getName(), Integer.parseInt(StringUtil.getNumbers(pics[4])), Double.parseDouble(pics[5]));
                        if (rectRts != null && rectRts.length > 0) {
                            rect = rectRts[0];
                            LLog.e("点击图片偏移量", "识别到图了 " + infoTemp.getName() + " 坐标:" + rect.nCentX * ScreenShotFb.IMAGE_SCALE_RATIO + "," + rect.nCentY * ScreenShotFb.IMAGE_SCALE_RATIO);
                            break;
                        }
                    }
                    if (rectRts != null && rectRts.length > 0) {
                        //需要点击的点
                        String command = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + (rect.nCentX * ScreenShotFb.IMAGE_SCALE_RATIO + Integer.parseInt(pics[2])) + ScriptConstants.SPLIT + (rect.nCentY * ScreenShotFb.IMAGE_SCALE_RATIO + Integer.parseInt(pics[3]));
                            /*if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                                command = ReorganizeUitl.portraitToLandscape(command);
                            }*/
                        SocketUtil.sendInstruct(command, false);
                    } else {
                        LLog.e("点图片 未识别到图 " + pics[1] + ScriptConstants.PNG);
                    }
                    largeBitmap.recycle();
                    largeBitmap = null;
                }
            }
            BrushTask.comparePicTime.add(System.currentTimeMillis() - time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 重新排序图片
     *
     * @param imgs 原始图片集合
     * @return 排序后图片集合
     */
    static int num = 0;

    public static Vector<PopupInfo> reSortPic(Vector<PopupInfo> imgs) {
        Vector<PopupInfo> resetImgs = new Vector<>();
        Vector<PopupInfo> frontImgs = new Vector<>();
        Vector<PopupInfo> centerImgs = new Vector<>();
        Vector<PopupInfo> backImgs = new Vector<>();
        Vector<PopupInfo> alertImgs = new Vector<>();
        if (imgs != null && imgs.size() > 0) {
            for (PopupInfo info : imgs) {
                if (PlatformConfig.IMG_TYPE_1.equals(info.getType())) {
                    alertImgs.add(info);
                } else if (info.getName().startsWith("AA1") && !PlatformConfig.IMG_TYPE_1.equals(info.getType())) {
                    frontImgs.add(info);
                } else if (info.getName().startsWith("AA2") && !PlatformConfig.IMG_TYPE_1.equals(info.getType())) {
                    centerImgs.add(info);
                } else {
                    backImgs.add(info);
                }
            }
            SortListUtil.shuffle(alertImgs);
            SortListUtil.shuffle(frontImgs);
            //SortListUtil.sort(centerImgs, "name", SortListUtil.ASC);
            SortListUtil.shuffle(centerImgs);
            SortListUtil.shuffle(backImgs);

            resetImgs.addAll(alertImgs);
            resetImgs.addAll(frontImgs);
            resetImgs.addAll(centerImgs);
            resetImgs.addAll(backImgs);
        }
        num++;
        num %= 3;
        if (num == 0) {
            if (frontImgs.size() > 0) {
                frontImgs.addAll(0, alertImgs);
                return frontImgs;
            }
        } else if (num == 1) {
            if (centerImgs.size() > 0) {
                centerImgs.addAll(0, alertImgs);
                return centerImgs;
            }
        } else if (num == 2) {
            if (backImgs.size() > 0) {
                backImgs.addAll(0, alertImgs);
                return backImgs;
            }
        }
        return resetImgs;
    }


    /**
     * 检测是否卡屏
     *
     * @param instrustStr 点击指令
     */
    public static Bitmap currentTapBitmap; //每次点击点生成一个bitmap
    public static Bitmap currentCenterBitmap; //每次点击点生成一个中心bitmap
    public static int tapLimitCount1 = 0; //点击累计次数 超过3次报警
    public static int tapLimitCount2 = 0; //点击累计次数 超过20次报警

    public static void dealWithCheckInTrap(String instrustStr) {
        try {
            if (!StringUtil.isEmpty(instrustStr)) {
                String[] datas = instrustStr.split(ScriptConstants.SPLIT);
                if (datas.length >= 3) {
                    int x1, y1, x2, y2;
                    x1 = (Integer.parseInt(datas[1]) - 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    y1 = (Integer.parseInt(datas[2]) - 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    x2 = (Integer.parseInt(datas[1]) + 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    y2 = (Integer.parseInt(datas[2]) + 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    if (x1 <= 0) {
                        x1 = 0;
                    }
                    if (y1 <= 0) {
                        y1 = 0;
                    }
                    if (x2 >= (1920 / 3)) {
                        x2 = 1920 / 3;
                    }
                    if (y2 >= (1080 / 3)) {
                        y2 = 1080 / 3;
                    }
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
                    SystemClock.sleep(50);
                    Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
                    if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                        largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                    }
                    if (largeBitmap != null) {
                        Bitmap tapBitmap = null;
                        if (x1 + Math.abs(x2 - x1) < largeBitmap.getWidth() && y1 + Math.abs(y2 - y1) < largeBitmap.getHeight()) {
                            tapBitmap = Bitmap.createBitmap(largeBitmap, x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
                        }
                        Bitmap centerBitmap = Bitmap.createBitmap(largeBitmap, 910 / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO,
                                490 / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO, 100 / ScreenShotFb.IMAGE_SCALE_RATIO, 100 / ScreenShotFb.IMAGE_SCALE_RATIO);
                        Rect tapRect = ImageIdentifyUtil.imageCompare(largeBitmap, currentTapBitmap);
                        Rect centerRect = ImageIdentifyUtil.imageCompare(largeBitmap, currentCenterBitmap);
                        if (tapRect != null && centerRect != null) {
                            tapLimitCount1++;
                        } else if (tapRect != null || centerRect != null) {
                            tapLimitCount2++;
                        } else {
                            tapLimitCount1 = 0;
                            tapLimitCount2 = 0;
                        }
                        currentTapBitmap = tapBitmap;
                        currentCenterBitmap = centerBitmap;
                        if (tapLimitCount1 >= 10 || tapLimitCount2 >= 20) {
                            //报警
                            RecordFloatView.updateMessage("检测到卡屏!--" + "Count1:" + tapLimitCount1 + ",Count2:" + tapLimitCount2);
                            tapLimitCount1 = 0;
                            tapLimitCount2 = 0;
                            BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_REDO_SCRIPT_ERROR, "卡屏_NULL");
                            return;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * 睡眠处理
     * 睡眠#1000#20000 (随机睡眠1~20秒)
     *
     * @param instructStr
     */
    public static long dealWithSleep(String instructStr) {
        try {
            String[] timeStr = instructStr.split(ScriptConstants.SPLIT);
            if (timeStr.length == 2) {
                return Long.parseLong(timeStr[1]);
            } else if (timeStr.length == 3) {
                long time = Long.parseLong(timeStr[1]) + (long) ((Long.parseLong(timeStr[2]) - Long.parseLong(timeStr[1])) * Math.random());
                LLog.i("当前随机的睡眠时间:" + time);
                return time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SleepConfig.SLEEP_TIME_1000;
    }


    /**
     * 处理随机点击指令
     *
     * @param instructStr
     * @example 随机点击#点击#870#180||点击#700#180||点击#530#180||点击#360#180
     * @example 随机点击#**词库**#点击库#乱序(顺序)
     */
    public static void dealWithRandomTap(String instructStr) {
        if (instructStr.contains("||")) {
            instructStr = instructStr.replace("\uFEFF", "");
            instructStr = instructStr.replace(ScriptConstants.RANDOM_CLICK_CMD + ScriptConstants.SPLIT, "");
            instructStr = instructStr.replace(ScriptConstants.TAP_SCRIPT, ScriptConstants.TAP_CMD);
            String[] strScriptList = instructStr.split("\\|\\|");
            Random random = new Random();
            String tapString = strScriptList[random.nextInt(strScriptList.length)];
       /* if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //竖屏坐标转横屏坐标重组指令
            tapString = ReorganizeUitl.portraitToLandscape(tapString);
        }*/
            LLog.d("随机点击:" + tapString);
            SocketUtil.sendInstruct(tapString, true);
        } else if (instructStr.contains("**词库**")) {
            if (BrushOrderHelper.getInstance().TvComments == null || BrushOrderHelper.getInstance().TvComments.size() == 0) {
                RecordFloatView.updateMessage("话库中取到的内容为空!");
            } else {
                String[] data = instructStr.split(ScriptConstants.SPLIT);
                if (data.length < 4) {
                    return;
                }
                for (CommentInfo commentInfo : BrushOrderHelper.getInstance().TvComments) {
                    if ((data[2] + ".txt").equals(commentInfo.getName())) {
                        if ("顺序".equals(data[3])) {
                            if (commentInfo.getCommentNum() >= commentInfo.getComments().size()) {
                                commentInfo.setCommentNum(0);
                            }
                            instructStr = commentInfo.getComments().get(commentInfo.getCommentNum()).replace(ScriptConstants.TAP_SCRIPT, ScriptConstants.TAP_CMD);
                            int newNum = commentInfo.getCommentNum() + 1;
                            commentInfo.setCommentNum(newNum);
                        } else if ("乱序".equals(data[3])) {
                            instructStr = commentInfo.getComments().get(new Random().nextInt(commentInfo.getComments().size())).replace(ScriptConstants.TAP_SCRIPT, ScriptConstants.TAP_CMD);
                        }
                    }
                }
                SocketUtil.sendInstruct(instructStr, false);

            }
        }
    }

    /**
     * 处理随机滑动指令
     *
     * @param instructStr
     * @example 随机滑动#**词库**#滑动库#乱序(顺序)
     */
    public static void dealWithRandomSwipe(String instructStr) {
        if (BrushOrderHelper.getInstance().TvComments == null || BrushOrderHelper.getInstance().TvComments.size() == 0) {
            RecordFloatView.updateMessage("话库中取到的内容为空!");
        } else {
            String[] data = instructStr.split(ScriptConstants.SPLIT);
            if (data.length < 4) {
                return;
            }
            for (CommentInfo commentInfo : BrushOrderHelper.getInstance().TvComments) {
                if ((data[2] + ".txt").equals(commentInfo.getName())) {
                    if ("顺序".equals(data[3])) {
                        if (commentInfo.getCommentNum() >= commentInfo.getComments().size()) {
                            commentInfo.setCommentNum(0);
                        }
                        instructStr = commentInfo.getComments().get(commentInfo.getCommentNum()).replace(ScriptConstants.SWIPE_SCRIPT, ScriptConstants.SWIPE_CMD);
                        int newNum = commentInfo.getCommentNum() + 1;
                        commentInfo.setCommentNum(newNum);
                    } else if ("乱序".equals(data[3])) {
                        instructStr = commentInfo.getComments().get(new Random().nextInt(commentInfo.getComments().size())).replace(ScriptConstants.SWIPE_SCRIPT, ScriptConstants.SWIPE_CMD);
                    }
                }
            }
            SocketUtil.sendInstruct(instructStr, false);

        }
    }


    /**
     * 处理模块开始执行流程
     *
     * @param instructStr
     */
    public static void dealWithModuleStart(String instructStr) {
        LLog.d("instructStr:" + instructStr);
        SocketUtil.tempScriptList.clear();
        SocketUtil.recordScript = true;//进行记录该模块的指令
        SocketUtil.tempScriptList.add(instructStr);
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length >= 2) {
            LLog.d("截取#后面的:" + datas[1]);
            if (SocketUtil.execModuleCount.containsKey(datas[1])) {
                int count = SocketUtil.execModuleCount.get(datas[1]);
                if (count > 0) {
                    SocketUtil.execModuleCount.put(datas[1], count);
                }
            } else {
                SocketUtil.execModuleCount.put(datas[1], 1);

            }

        }
    }


    /**
     * 处理模块结束执行流程
     *
     * @param instructStr
     */
    public static void dealWithModuleStop(String instructStr) {
        SocketUtil.recordScript = false;//取消记录该模块的指令
        SocketUtil.tempScriptList.clear();
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length >= 2) {
            if (SocketUtil.execModuleCount.containsKey(datas[1])) {
                SocketUtil.execModuleCount.remove(datas[1]);
                BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_UPDATE_MODULE, datas[1], 1000);
            }
        }
    }

    /**
     * 删除指定名称的文件
     * 删除文件#/storage/emulated/0/shared_prefs
     *
     * @param instructStr
     */
    public static void dealWithDeleteFile(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length >= 2) {
            if ("/storage/emulated/0".equals(datas[1]) || "/storage/emulated/0/".equals(datas[1])) {
                return;
            }
            FileHelper.deleteFolderFile(datas[1]);
            LLog.i("文件删除完成！");
            SocketUtil.continueExec();//继续播放脚本
        }
    }

    /**
     * 拷贝指定名称的文件到指定的路径
     * 复制文件#/data/data/com.xx/test.txt#/storage/emulated/0/test.txt
     *
     * @param instructStr
     */
    public static void dealWithCoPyFile(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length >= 2) {
            String src = datas[1];  //数据源文件路径
            String des = datas[2];  //目标文件路径
            LLog.e("src===" + src);
            LLog.e("des===" + des);
            if (FileUtil.isFileExist(src)) {
                FileHelper.copyFileDirectory(src, des);
                LLog.e("文件拷贝完成");
                RecordFloatView.updateMessage("文件拷贝完成:");
                SocketUtil.continueExec();
            } else {
                //报警 订单所需文件缺失
                LLog.e("报警：订单所需文件缺失");
                RecordFloatView.updateMessage("报警：订单所需文件缺失！");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FILE_LOST_FAIL);
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "订单所需文件缺失");
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
            }
        }
    }


    /**
     * 获取所有者(/data/data/包名及子文件夹)
     * 获取所有者#/data/data/#com.reader.firebird
     * /data/data/----------->fatherDir--------自己指定
     * 包名及子文件夹----------->sonfile--------自己指定
     *
     * @param string
     */
    public static void dealWithGetOwn(String string) {
        try {
            String[] split = string.split(ScriptConstants.SPLIT);
            if (split != null && split.length > 2) {
                String fatherDir = split[1];  //父文件
                String sonfile = split[2];    //子文件--->要获取的所有者的文件
                String cmd = "ls -l " + fatherDir + " | grep " + sonfile;
                CMDUtil.CommandResult commandResult = CMDUtil.execCommand(new String[]{cmd}, true, true);
                LLog.e("所有者全部信息=============" + commandResult);
                String successMsg = commandResult.successMsg;
                if (successMsg != null) {
                    while (successMsg.contains("  ")) {
                        successMsg = successMsg.replace("  ", " ");
                    }
                }
                LLog.e("格式化后所有者信息=============" + successMsg);
                String[] ownString = successMsg.split(" ");
                String ownStreamStr = ownString[2];
                LLog.e("所有者=============" + ownStreamStr);
                if (TextUtils.isEmpty(ownStreamStr)) {
                    LLog.e("获取所有者失败 -> ownStreamStr为空!");
                    RecordFloatView.updateMessage("获取所有者失败 -> ownStreamStr为空!");
                    return;
                } else {
                    SpUtil.putKeyString(PlatformConfig.CURRENT_OWN, ownStreamStr);
                    LLog.e("获取所有者成功 继续执行脚本");
                    RecordFloatView.updateMessage("获取所有者者成功 继续执行脚本");
                    SocketUtil.continueExec();
                }
            } else {
                LLog.e("获取所有者 -> 指令出错，请核对指令是否正确!");
                RecordFloatView.updateMessage("获取所有者 -> 指令出错，请核对指令是否正确!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更改所有者(/data/data/包名及子文件夹)
     * 更改所有者#/data/data/#com.reader.firebird
     * /data/data/----------->fatherDir--------自己指定
     * 包名及子文件夹----------->sonfile--------自己指定
     */
    public static void dealWithModifyOwn(String string) {
        try {
            String[] split = string.split(ScriptConstants.SPLIT);
            if (split != null && split.length > 2) {
                String fatherDir = split[1];  //父文件
                String sonfile = split[2];    //子文件--->要获取的所有者的文件

                String ownString = SpUtil.getKeyString(PlatformConfig.CURRENT_OWN, "");
                if (TextUtils.isEmpty(ownString)) {
                    LLog.e("更改所有者 -> ownString为空!");
                    RecordFloatView.updateMessage("获取所有者失败 -> ownStreamStr为空!");
                    return;
                } else {
                    String cmd = "chown -R " + ownString + ":" + ownString + " " + fatherDir + sonfile;
                    boolean b = CMDUtil.execShellWaitFor(cmd);
                    if (b) {
                        LLog.e("更改所有者成功 继续执行脚本");
                        RecordFloatView.updateMessage("更改所有者成功 继续执行脚本");
                        SocketUtil.continueExec();
                    } else {
                        LLog.e("更改所有者失败!");
                        RecordFloatView.updateMessage("更改所有者失败!");
                        return;
                    }
                }
            } else {
                LLog.e("更改所有者 -> 指令出错，请核对指令是否正确!");
                RecordFloatView.updateMessage("更改所有者 -> 指令出错，请核对指令是否正确!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ROOT方式复制应用文件(data/data/包名 --> /XileScript/data/包名)
     * 复制应用文件#com.tencent.mm
     */
    public static void dealWithCoPyApplicationFile(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas.length > 1) {
                if (Constants.execServerScript) {
                    if (Constants.sBrushOrderInfo == null || TextUtils.isEmpty(Constants.sBrushOrderInfo.getUserId()) || TextUtils.isEmpty(Constants.sBrushOrderInfo.getGameNameSpell())) {
                        LLog.e("内存中订单或用户ID或游戏名字为空!");
                        RecordFloatView.updateMessage("内存中订单或用户ID或游戏名字为空!");
                        BrushOrderHelper.getInstance().orderDealFailure("内存中订单或用户ID或游戏名字为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                        return;
                    }
                    String packageName = datas[1];
                    if (TextUtils.isEmpty(packageName)) {
                        LLog.e("ROOT方式复制应用文件 -> 未找到应用包名!");
                        RecordFloatView.updateMessage("ROOT方式复制应用文件 -> 未找到应用包名!");
                        BrushOrderHelper.getInstance().orderDealFailure("ROOT方式复制应用文件 -> 未找到应用包名!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                        return;
                    }
                    List<String> userIds = FileHelper.getFileNames(Constants.SCRIPT_APPLICATION_DATA_PATH + "/" + Constants.sBrushOrderInfo.getGameNameSpell());
                    if (userIds == null || userIds.size() == 0 || !userIds.contains(Constants.sBrushOrderInfo.getUserId())) {
                        LLog.e("未发现应用文件,正在进行拷贝操作...");
                        RecordFloatView.updateMessage("未发现应用文件,正在进行拷贝操作...");
                        FileHelper.makeRootDirectory(Constants.SCRIPT_APPLICATION_DATA_PATH + "/" + Constants.sBrushOrderInfo.getGameNameSpell() + "/" + Constants.sBrushOrderInfo.getUserId() + "/" + packageName);
                        String script = "cp -fr `find /data/data/" + packageName + " -maxdepth 1 -path \"/data/data/" + packageName + "/lib\" -prune -o -print | sed 1d` " + Constants.SCRIPT_APPLICATION_DATA_PATH + "/" + Constants.sBrushOrderInfo.getGameNameSpell() + "/" + Constants.sBrushOrderInfo.getUserId() + "/" + packageName;
                        LLog.i("指令:" + script);
                        if (CMDUtil.execShellWaitFor(script)) {
                            SocketUtil.continueExec();
                        } else {
                            LLog.e("execute cmd failure ! " + script);
                            RecordFloatView.updateMessage("execute cmd failure ! " + script);
                        }
                    } else {
                        LLog.e("已存在应用文件,无需进行拷贝操作...");
                        RecordFloatView.updateMessage("已存在应用文件,无需进行拷贝操作...");
                        SocketUtil.continueExec();
                    }
                } else {
                    LLog.e("非执行服务器脚本 已跳过该指令!");
                    RecordFloatView.updateMessage("非执行服务器脚本 已跳过该指令!");
                    SocketUtil.continueExec();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ROOT方式还原应用文件(/XileScript/data/包名 --> data/data/包名)
     * 还原应用文件#com.tencent.mm
     */
    public static void dealWithRestoreApplicationFile(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas.length > 1) {
                if (Constants.execServerScript) {
                    if (Constants.sBrushOrderInfo == null || TextUtils.isEmpty(Constants.sBrushOrderInfo.getUserId()) || TextUtils.isEmpty(Constants.sBrushOrderInfo.getGameNameSpell())) {
                        LLog.e("内存中订单或用户ID或游戏名字为空!");
                        RecordFloatView.updateMessage("内存中订单或用户ID或游戏名字为空!");
                        BrushOrderHelper.getInstance().orderDealFailure("内存中订单或用户ID或游戏名字为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                        return;
                    }
                    String packageName = datas[1];
                    if (TextUtils.isEmpty(packageName)) {
                        LLog.e("ROOT方式还原应用文件 -> 未找到应用包名!");
                        RecordFloatView.updateMessage("ROOT方式还原应用文件 -> 未找到应用包名!");
                        BrushOrderHelper.getInstance().orderDealFailure("ROOT方式还原应用文件 -> 未找到应用包名!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                        return;
                    }
                    List<String> userIds = FileHelper.getFileNames(Constants.SCRIPT_APPLICATION_DATA_PATH + "/" + Constants.sBrushOrderInfo.getGameNameSpell());
                    if (userIds == null || userIds.size() == 0 || !userIds.contains(Constants.sBrushOrderInfo.getUserId())) {
                        LLog.e("未发现需要还原的应用文件 -> 跳过!");
                        RecordFloatView.updateMessage("未发现需要还原的应用文件 -> 跳过!");
                        SocketUtil.continueExec();
                    } else {
                        LLog.e("发现需要还原的应用文件,进行拷贝操作...");
                        RecordFloatView.updateMessage("发现需要还原的应用文件,进行拷贝操作...");
                        String script = "ROOT方式复制文件#" + Constants.SCRIPT_APPLICATION_DATA_PATH + "/" + Constants.sBrushOrderInfo.getGameNameSpell() + "/" + Constants.sBrushOrderInfo.getUserId() + "/" + packageName + "#/data/data/";
                        LLog.i("指令:" + script);
                        dealWithCoPyFileNeedRoot(script);
                        String permissionScript = "赋予文件读写权限#" + "/data/data/" + packageName;
                        LLog.i("指令:" + permissionScript);
                        SystemClock.sleep(1000);
                        dealWithGiveFileRWPermision(permissionScript);
                    }
                } else {
                    LLog.e("非执行服务器脚本 已跳过该指令!");
                    RecordFloatView.updateMessage("非执行服务器脚本 已跳过该指令!");
                    SocketUtil.continueExec();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ROOT方式拷贝指定名称的文件或文件夹到指定的路径
     * ROOT方式复制文件#/data/data/com.reader.firebird/shared_prefs#/storage/emulated/0
     *
     * @param instructStr
     */
    public static void dealWithCoPyFileNeedRoot(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length >= 2) {
            String src = datas[1];  //数据源文件路径
            String des = datas[2];  //目标文件路径
            LLog.e("src===" + src);
            LLog.e("des===" + des);
            String cmd = "cp  -R -f " + src + " " + des;  ///data/data/com.UCMobile/shared_prefs#/storage/emulated/0/shared_prefs
            try {
                CMDUtil.upgradeRootPermission(src);
                if (FileUtil.isFileExistRoot(src)) {
                    if (CMDUtil.execShellWaitFor(cmd)) {
                        SocketUtil.continueExec();
                    } else {
                        LLog.e("execute copy cmd failure !");
                        RecordFloatView.updateMessage("execute cmd failure !");
                        SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FILE_LOST_FAIL);
                        SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "execute copy cmd failure");
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                    }
                } else {
                    //报警 订单所需文件缺失
                    LLog.e("报警：订单所需文件缺失");
                    RecordFloatView.updateMessage("报警：订单所需文件缺失！");
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FILE_LOST_FAIL);
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "订单所需文件缺失");
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                    Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * ROOT方式创建文件夹
     * ROOT方式创建文件夹#/data/data/com.xx/aa
     *
     * @param instructStr
     */
    public static void dealWithMakeDirectoryNeedRoot(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length >= 2) {
            String des_dir = datas[1];  //目标文件夹路径
            String cmd = "mkdir -p " + des_dir;
            try {
                if (CMDUtil.execShellWaitFor(cmd)) {
                    SocketUtil.continueExec();
                } else {
                    LLog.e("execute cmd failure !");
                    RecordFloatView.updateMessage("execute cmd failure !");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * ROOT方式赋予文件读写权限
     * 赋予文件读写权限#/data/data/com.reader.firebird/shared_prefs
     *
     * @param instructStr
     */
    public static void dealWithGiveFileRWPermision(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas.length >= 2) {
            String des_dir = datas[1];  //目标文件夹路径
            try {
                if (CMDUtil.upgradeRootPermission(des_dir)) {
                    SocketUtil.continueExec();
                } else {
                    LLog.e("execute permission cmd failure !");
                    RecordFloatView.updateMessage("execute cmd failure !");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 上传文件
     * 上传文件#/storage/emulated/0/shared_prefs.tar
     *
     * @param instructStr
     */
    public static void dealWithUploadFile(String instructStr) {
        if (!TextUtils.isEmpty(instructStr)) {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas.length >= 2) {
                File file = new File(datas[1]);
                if (file.exists() && Constants.sBrushOrderInfo != null) {
                    //BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_UPLOAD_KEYCHAIN, file.getAbsolutePath(), 1000); //暂时废弃 另起线程上传
                    String src = datas[1];  //数据源文件路径
                    FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_UPLOAD_PATH + Constants.sBrushOrderInfo.getUserId());
                    String des = Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_UPLOAD_PATH + Constants.sBrushOrderInfo.getUserId() + File.separator + file.getName();  //目标文件路径
                    String desConfig = Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_UPLOAD_PATH + Constants.sBrushOrderInfo.getUserId() + File.separator + file.getName() + ".cfg";  //目标配置文件路径
                    String keychainData = FileHelper.getUploadFile(file);
                    BSendKeychainFile bSendKeychainFile = new BSendKeychainFile(BrushHelper.B_UPLOAD_KEYCHAIN, BrushHelper.B_TASK_TYPE_9035, file.getName(),
                            keychainData, Constants.sBrushOrderInfo.getUserId(), System.currentTimeMillis());
                    LLog.i("src===" + src);
                    LLog.i("des===" + des);
                    FileHelper.copyFileDirectory(src, des);
                    LLog.i("文件拷贝完成");
                    FileHelper.saveFile(ScriptApplication.getGson().toJson(bSendKeychainFile), desConfig);
                } else {
                    LLog.e("要上传的文件不存在!");
                    RecordFloatView.updateMessage("要上传的文件不存在!");
                    //报警 订单所需文件缺失
                    LLog.e("报警：订单所需文件缺失");
                    RecordFloatView.updateMessage("报警：订单所需文件缺失！");
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FILE_LOST_FAIL);
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "订单所需文件缺失");
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                    Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                }
            }
        }
    }

    /**
     * 截取校验码图片
     */
    public static void dealWithCaptureVerifyCodePic(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas != null && datas.length >= 2) {
                String path = Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId() + "/" + "vercode_" + datas[1] + ".png";
                CaptureUtil.takeScreen(ScriptApplication.getInstance(), path, false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传自定义验证码图片
     * 上传校验码图片#0/1    0表示文字验证码图片，1表示滑动验证码图片,2表示手势验证码,3表示点击验证码
     */
    public static void dealWithUploadVerifyCodePic(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas != null && datas.length == 2) {
                int verifyType = Integer.parseInt(datas[1]); //0表示文字验证码图片，1表示滑动验证码图片
                Constants.VERIFY_TYPE = verifyType;
                String path = Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId() + "/" + "vercode_" + MobileRegisterHelper.getInstance().phone + ".png";
                CaptureUtil.takeScreen(ScriptApplication.getInstance(), path, false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 1000);
            } else if (datas != null && datas.length > 2 && "高清".equals(datas[2])) {
                int verifyType = Integer.parseInt(datas[1]); //0表示文字验证码图片，1表示滑动验证码图片
                Constants.VERIFY_TYPE = verifyType;
                String path = Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId() + "/" + "vercode_" + MobileRegisterHelper.getInstance().phone + ".png";
                CaptureUtil.takeScreen_Play(ScriptApplication.getInstance(), path, false, Bitmap.CompressFormat.PNG, CutImageActivity.class);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 1000);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传截图
     * 上传截图#角色截图
     * 上传截图#游戏截图
     * 上传截图#充值截图
     * 上传截图#等级截图
     * 上传截图#区服截图
     */
    public static void dealWithUploadCaptureImg(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas != null && datas.length >= 2) {
            if ("角色截图".equals(datas[1])) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_ROLE);
            } else if ("游戏截图".equals(datas[1])) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_GAME);
            } else if ("充值截图".equals(datas[1])) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_RECHARGE);
            } else if ("等级截图".equals(datas[1])) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_LEVEL);
            } else if ("区服截图".equals(datas[1])) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_AREA);
            } else {
                LLog.e("截图类型有误，请检查脚本！");
                RecordFloatView.updateMessage("截图类型有误，请检查脚本！");
                return;
            }
            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_REQUEST_UPLOAD_IMAGE, 1000);
        }
    }

    /**
     * 压缩文件
     * 压缩文件#/storage/emulated/0/databases
     */
    public static void dealWithDoZip(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas != null && datas.length == 2) {
                String zipScript = "tar -cvf " + datas[1] + ".tar " + datas[1];
                if (CMDUtil.execShellWaitFor(zipScript)) {
                    SocketUtil.continueExec();
                } else {
                    LLog.e("执行压缩指令失败1");
                    BrushOrderHelper.getInstance().orderDealFailure("执行压缩指令失败1");
                    FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误222 : \n" + "执行压缩指令失败0 " + ": \n\n");
                }
            } else if (datas != null && datas.length == 3) {
                String zipScript = "tar -cvf " + datas[2] + " " + datas[1];
                if (CMDUtil.execShellWaitFor(zipScript)) {
                    SocketUtil.continueExec();
                } else {
                    LLog.e("执行压缩指令失败");
                    BrushOrderHelper.getInstance().orderDealFailure("执行压缩指令失败2");
                    FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误222 : \n" + "执行压缩指令失败1 " + ": \n\n");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            LLog.e("执行压缩指令异常 e:" + e);
            BrushOrderHelper.getInstance().orderDealFailure("执行压缩指令异常 e");
            FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误222 : \n" + "执行压缩指令异常 " + ": \n\n");
        }
    }

    /**
     * 解压缩文件
     * 解压缩文件#/storage/emulated/0/shared_prefs.tar
     */
    public static void dealWithDoUnZip(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas != null && datas.length >= 2) {
                String zipScript = "tar -xvf " + datas[1];
                if (CMDUtil.execShellWaitFor(zipScript)) {
                    SocketUtil.continueExec();
                } else {
                    LLog.e("执行解压缩指令失败");
                    BrushOrderHelper.getInstance().orderDealFailure("执行解压缩指令失败!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            LLog.e("执行解压缩指令失败 e:" + e);
            BrushOrderHelper.getInstance().orderDealFailure("执行解压缩指令失败 e!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }


    /**
     * ZIP压缩文件 ZipUtils
     * ZIP压缩文件#/storage/emulated/0/databases#/storage/emulated/0/databases.zip
     */
    public static void dealWithZipDoZip(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas != null && datas.length == 3) {
                ZipUtil.toZip(datas[1], datas[2], true);
                SocketUtil.continueExec();
            } else {
                LLog.e("执行ZIP压缩指令错误");
                LLog.e("订单处理失败!");
                BrushOrderHelper.getInstance().orderDealFailure("执行ZIP压缩指令错误");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误222 : \n" + "执行ZIP压缩指令错误 " + ": \n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("执行ZIP压缩指令异常 :" + e);
            BrushOrderHelper.getInstance().orderDealFailure("执行ZIP压缩指令异常");
            FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误222 : \n" + "执行ZIP压缩指令异常 " + ": \n\n");
        }
    }


    /**
     * ZIP解压缩文件
     * ZIP解压缩文件#/storage/emulated/0/databases.zip#/storage/emulated/0
     */
    public static void dealWithZipDoUnZip(String instructStr) {
        try {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas != null && datas.length == 3) {
                ZipUtils.UnZipFolder(datas[1], datas[2]);
                SocketUtil.continueExec();
            } else {
                LLog.e("执行ZIP压缩指令失败");
                BrushOrderHelper.getInstance().orderDealFailure("执行ZIP压缩指令失败!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("执行ZIP压缩指令异常: " + e);
            BrushOrderHelper.getInstance().orderDealFailure("执行ZIP压缩指令失败 e!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }


    /**
     * 获取校验码
     */
    public static void dealWithGetVerifyCode() {
        Constants.failRepeatCount = 0;
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_VERY_CODE, 1000);
    }


    /**
     * 创建邮箱账号
     * 创建邮箱账号#@163.com#*****
     *
     * @param instructStr
     */
    public static void dealWithCreateEmail(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas != null && datas.length >= 3) {
            String emailPlatform = datas[1];  //邮箱平台
            String emailNameType = datas[2];  //邮箱名称产生的格式
            String emailHeadName = AccountUtil.getRandomName(emailNameType); //邮箱的名称
            String emailName = emailHeadName + emailPlatform;  //生成完整的邮箱
            LLog.e("邮箱为：" + emailName);
            SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, emailName); //保存成用户名
            SocketUtil.continueExec();
        }
    }

    /**
     * android 系统cmd指令
     * 安卓CMD指令#find /storage/emulated/0 -name "*.acid" | xargs rm -rf
     *
     * @param instructStr
     */
    public static void dealWithAndroidCmd(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas != null && datas.length >= 2) {
            String cmdContent = datas[1];
            CMDUtil.execShell(cmdContent);
        }
    }


    /**
     * 获取身份证信息
     */
    public static void dealWithGetIdCardInfo(String instructStr) {
        if (instructStr.contains(ScriptConstants.SPLIT)) {
            String[] datas = instructStr.split(ScriptConstants.SPLIT);
            if (datas != null && datas.length >= 2) {
                Constants.gender = datas[1];
            }
        }
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_ID_CARD, 1000);
    }

    /**
     * 插入身份证姓名
     */
    public static void dealWithInsertIdCardName() {
        String name = SpUtil.getKeyString(PlatformConfig.CURRENT_NAME, "");
        LLog.e("当前身份证姓名：" + name);
        if (!TextUtils.isEmpty(name)) {
            SocketUtil.sendInstruct(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + name, false);
            SocketUtil.continueExec();
        } else {
            BrushOrderHelper.getInstance().orderDealFailure("当前身份证姓名 为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }


    /**
     * 插入身份证号码
     */
    public static void dealWithInsertIdCardNumber() {
        String number = SpUtil.getKeyString(PlatformConfig.CURRENT_ID_CARD, "");
        LLog.e("当前身份证号码：" + number);
        if (!TextUtils.isEmpty(number)) {
            SocketUtil.sendInstruct(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + number, false);
            SocketUtil.continueExec();//执行下一条指令
        } else {
            BrushOrderHelper.getInstance().orderDealFailure("当前身份证号码 为空!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }


    /**
     * 直接请求链接处理
     * 直接请求链接#http://www.baidu.com
     *
     * @param instructStr
     */
    public static void dealWithDirectRequest(String instructStr) {
        LLog.e("直接请求链接======instructStr====" + instructStr);
        String[] split = instructStr.split(ScriptConstants.SPLIT);
        if (split != null && split.length > 1) {
            String requestLink = split[1];
            GetHttpRequest request = new GetHttpRequest();
            request.getString(requestLink, null, new StringCallback() {
                @Override
                public void onResponse(String response, int id) {
                    LLog.e("直接请求链接======onResponse====" + response);
                }


                @Override
                public void onError(Call call, Exception e, int id) {
                    LLog.e("直接请求链接=====onError=====" + e);
                }
            });
        }
    }

    /**
     * 浏览器打开链接处理
     * 浏览器打开链接#http://www.baidu.com
     *
     * @param instructStr
     */
    public static void dealWithBrowserOpen(String instructStr) {
        String[] split = instructStr.split(ScriptConstants.SPLIT);
        if (split != null && split.length > 1) {
            String requestLink = split[1];
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(requestLink));
            ScriptApplication.getInstance().startActivity(intent);
            SocketUtil.continueExec();
        }
    }

    /**
     * 代理请求链接
     * 代理请求链接#http://www.baidu.com#GET#Android
     *
     * @param instructStr
     */
    public static void dealWithProxyLink(String instructStr) {
        String[] datas = instructStr.split(ScriptConstants.SPLIT);
        if (datas != null && datas.length >= 4) {
            ConnectUtil.function(datas[1], datas[2], datas[3]);
        }
    }

    /**
     * md5校验文件处理
     * MD5校验#/storage/emulated/0/11.txt#2dfgd5e5ere5rgheh5er22e5g555
     *
     * @param instructStr
     */
    public static void dealWithMD5CompareFile(String instructStr) {
        String[] split = instructStr.split(ScriptConstants.SPLIT);
        if (split != null && split.length > 2) {
            String filePath = split[1];
            String MD5toCompareStr = split[2];
            File file = new File(filePath);
            if (file.exists()) {
                String fileMd53 = MD5Util.getFileMd53(file);
                LLog.e("fileMd53===========" + fileMd53);
                LLog.e("MD5toCompareStr===========" + MD5toCompareStr);
                if (fileMd53.equals(MD5toCompareStr)) {
                    LLog.e("文件下载成功===========");
                    SocketUtil.currentExecNum += 3;
                    SocketUtil.continueExec();
                } else {
                    LLog.e("文件正在下载===========");
                    //SystemClock.sleep(10000);
                    SocketUtil.sendMessage(SocketUtil.SOCKET_MSG_EXEC, 30000);
                }
            } else { //每隔一分钟比较一次
                //SystemClock.sleep(10000);
                SocketUtil.sendMessage(SocketUtil.SOCKET_MSG_EXEC, 30000);
            }
        }
    }


    /**
     * 处理顺序点击
     * 顺序点击#**词库**#点击库
     *
     * @param instructStr
     */
    public static void dealWithOrderClick(String instructStr) {
        String[] data = instructStr.split(ScriptConstants.SPLIT);
        if (data.length >= 3) {
            if (ScriptConstants.COMMENT.equals(data[1])) {
                String instruct = null;
                if (BrushOrderHelper.getInstance().TvComments == null || BrushOrderHelper.getInstance().TvComments.size() == 0) {
                    RecordFloatView.updateMessage("话库中取到的内容为空!");
                } else {
                    for (CommentInfo commentInfo : BrushOrderHelper.getInstance().TvComments) {
                        if ((data[2] + ".txt").equals(commentInfo.getName())) {
                            if (commentInfo.getCommentNum() >= commentInfo.getComments().size()) {
                                commentInfo.setCommentNum(0);
                            }
                            instruct = commentInfo.getComments().get(commentInfo.getCommentNum()).replace(ScriptConstants.TAP_SCRIPT, ScriptConstants.TAP_CMD);
                            LLog.i("当前顺序点击指令: " + instruct);
                            int newNum = commentInfo.getCommentNum() + 1;
                            commentInfo.setCommentNum(newNum);
                        }
                    }
                    if (instruct != null) {
                        SocketUtil.sendInstruct(instruct, false);
                    }
                }
            }
        }
    }

    /**
     * 检测是否卡屏
     *
     * @param instrustStr 点击指令
     */
    public static boolean checkInTrap(String instrustStr) {
        try {
            if (!StringUtil.isEmpty(instrustStr)) {
                String[] datas = instrustStr.split(ScriptConstants.SPLIT);
                if (datas.length >= 3) {
                    int x1, y1, x2, y2;
                    x1 = (Integer.parseInt(datas[1]) - 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    y1 = (Integer.parseInt(datas[2]) - 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    x2 = (Integer.parseInt(datas[1]) + 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    y2 = (Integer.parseInt(datas[2]) + 50) / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO;
                    if (x1 <= 0) {
                        x1 = 0;
                    }
                    if (y1 <= 0) {
                        y1 = 0;
                    }
                    if (x2 >= (1920 / ScreenShotFb.IMAGE_SCALE_RATIO)) {
                        x2 = 1920 / ScreenShotFb.IMAGE_SCALE_RATIO;
                    }
                    if (y2 >= (1080 / ScreenShotFb.IMAGE_SCALE_RATIO)) {
                        y2 = 1080 / ScreenShotFb.IMAGE_SCALE_RATIO;
                    }
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
                    SystemClock.sleep(50);
                    Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
                    if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
                        largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
                    }
                    if (largeBitmap != null) {
                        Bitmap tapBitmap = null;
                        if (x1 + Math.abs(x2 - x1) < largeBitmap.getWidth() && y1 + Math.abs(y2 - y1) < largeBitmap.getHeight()) {
                            tapBitmap = Bitmap.createBitmap(largeBitmap, x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
                        }
                        Bitmap centerBitmap = Bitmap.createBitmap(largeBitmap, 910 / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO,
                                490 / ScreenShotFb.IMAGE_SCALE_RATIO / ScreenShotFb.IMAGE_IDENTIFY_RATIO * ScreenShotFb.IMAGE_IDENTIFY_RATIO, 100 / ScreenShotFb.IMAGE_SCALE_RATIO, 100 / ScreenShotFb.IMAGE_SCALE_RATIO);
                        Rect tapRect = ImageIdentifyUtil.imageCompare(largeBitmap, currentTapBitmap);
                        Rect centerRect = ImageIdentifyUtil.imageCompare(largeBitmap, currentCenterBitmap);
                        if (tapRect != null && centerRect != null) {
                            tapLimitCount1++;
                        } else if (tapRect != null || centerRect != null) {
                            tapLimitCount2++;
                        } else {
                            tapLimitCount1 = 0;
                            tapLimitCount2 = 0;
                        }
                        currentTapBitmap = tapBitmap;
                        currentCenterBitmap = centerBitmap;
                        if (tapLimitCount1 >= 10 || tapLimitCount2 >= 20) {
                            //报警
                            RecordFloatView.updateMessage("检测到卡屏!--" + "Count1:" + tapLimitCount1 + ",Count2:" + tapLimitCount2);
                            tapLimitCount1 = 0;
                            tapLimitCount2 = 0;
                            return true;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 处理返回订单是否成功或失败，（多为主动处理）
     * 返回订单结果#成功
     *
     * @param instructStr
     */
    public static void dealWithReturnOrderResult(String instructStr) {
        if (SocketUtil.need_kill_APP_list != null && SocketUtil.need_kill_APP_list.size() > 0) {
            for (String name : SocketUtil.need_kill_APP_list) {
                if (!StringUtil.isEmpty(name)) {
                    SocketUtil.execFromOut("键值#3", false);
                    SystemClock.sleep(500);
                    SocketUtil.execFromOut("杀死APP 无极(org.wuji)", false);
                    SystemClock.sleep(500);
                }
            }
        }

        String[] data = instructStr.split(ScriptConstants.SPLIT);
        if (data.length >= 2) {
            Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
            ScriptUtil.dealWithCapture("截屏#游戏_1");
            SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_GAME);
            if (ScriptConstants.ORDER_SUCCESS.equals(data[1])) {
                LLog.e("订单处理成功!");
                RecordFloatView.updateMessage("订单处理成功!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_SUCCESS);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_FAILED.equals(data[1])) {
                LLog.e("订单处理失败!");
                RecordFloatView.updateMessage("订单处理失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "脚本指令为 显示 失败");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.CHANGE_DEVICE_INFO_SCRIPT.equals(data[1])) {
                LLog.e("切换设备信息失败!");
                RecordFloatView.updateMessage("切换设备信息失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "切换设备信息失败");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_CHANGE_DEVICE_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_FAILED_PAUSE.equals(data[1])) {
                LLog.e("订单处理失败,暂停!");
                RecordFloatView.updateMessage("订单处理失败,暂停!");
                RecordFloatView.getInstance(ScriptApplication.getInstance()).setPlayPause();
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL_PAUSE);
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "订单处理失败,暂停!");
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_GAME_DOWNLOAD_FAIL.equals(data[1])) {
                LLog.e("订单处理失败,安装前失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "安装前失败");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误 : \n" + "安装前失败!  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                RecordFloatView.updateMessage("订单处理失败,安装前失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_GAME_DOWNLOAD_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_REGISTER_FAIL.equals(data[1])) {
                LLog.e("订单处理失败,注册失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "注册失败");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误 : \n" + "注册失败!  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                RecordFloatView.updateMessage("订单处理失败,注册失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_REGISTER_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_LOGIN_FAIL.equals(data[1])) {
                LLog.e("订单处理失败,登录失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "登录失败");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误 : \n" + "登录失败!  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                RecordFloatView.updateMessage("订单处理失败,登录失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_LOGIN_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_CREATE_ROLE_FAIL.equals(data[1])) {
                LLog.e("订单处理失败,注册成功后失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "注册成功后失败");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误 : \n" + "注册成功后失败!  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                RecordFloatView.updateMessage("订单处理失败,注册成功后失败!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_CREATE_ROLE_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_WRONG_ACCOUNT_PWD_FAIL.equals(data[1])) {
                LLog.e("订单处理失败,账号密码错误!");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "返回订单失败错误 : \n" + "账号密码错误!  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "账号密码错误");
                RecordFloatView.updateMessage("订单处理失败,账号密码错误!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_WRONG_ACCOUNT_PWD_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_ACCOUNT_EXCEPTION_FAIL.equals(data[1])) {
                LLog.e("订单处理失败,账号异常!");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "账号异常 : \n" + "账号密码错误!  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "账号异常");
                RecordFloatView.updateMessage("订单处理失败,账号异常!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_ACCOUNT_EXCEPTION_FAIL);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            } else if (ScriptConstants.ORDER_REGISTER_EXCEPTION_FAIL.equals(data[1])) {
                LLog.e("订单处理失败,注册异常!");
                FileHelper.addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + "注册异常 : \n" + "账号密码错误!  执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "注册异常");
                RecordFloatView.updateMessage("订单处理失败,注册异常!");
                SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_REGISTER_EXCEPTION);
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 3000);
            }
            ScriptUtil.dealWithKeyValue(ScriptConstants.KEYVALUE_SCRIPT + ScriptConstants.SPLIT + KeyCode.KEYCODE_HOME);
            SystemClock.sleep(SleepConfig.SLEEP_TIME_2000);
            AppUtil.killApp(SpUtil.getKeyString(PlatformConfig.NEED_KILL_PACKAGENAME, ""));
        }
    }


    /**
     * 卸载手机中的一些应用
     */
    public static void dealWithUninstallOtherApp() {
        try {
            List<AppInfo> appInfoList = AppUtil.getAppInfoList(ScriptApplication.getInstance());
            for (AppInfo info : appInfoList) {
                if (!"com.example.devicehook".equals(info.getPackageName()) && !"com.speedsoftware.rootexplorer".equals(info.getPackageName())
                        && !"de.robv.android.xposed.installer".equals(info.getPackageName()) && !"com.tencent.mm".equals(info.getPackageName())
                        && !"com.miui.systemlogcache".equals(info.getPackageName()) && !"com.p2p.controller".equals(info.getPackageName())
                        && !"com.zjns.xposedinstaller".equals(info.getPackageName()) && !"com.tencent.mobileqq".equals(info.getPackageName())
                        && !"com.oupeng.mini.android".equals(info.getPackageName()) && !"org.wuji".equals(info.getPackageName())
                        && !"com.example.devicemodify".equals(info.getPackageName()) && !"com.mengdie.proxy".equals(info.getPackageName())
                        && !"com.xile.script".equals(info.getPackageName()) && !"com.baidu.input_mi".equals(info.getPackageName())
                        && !"com.eg.android.AlipayGphone".equals(info.getPackageName()) && !"com.tencent.android.qqdownloader".equals(info.getPackageName())
                        && !"com.wanmei.zhuxian.laohu".equals(info.getPackageName()) && !"tianma.com.tianma".equals(info.getPackageName())
                        && !"in.zhaoj.shadowsocksr".equals(info.getPackageName()) && !"com.tencent.android.qqdownloader".equals(info.getPackageName())
                        && !"com.mrbimc.selinux".equals(info.getPackageName()) && !"com.wandoujia.phoenix2".equals(info.getPackageName())
                        && !"com.xiaomi.shop".equals(info.getPackageName()) && !"com.doubleopen.wxskzs".equals(info.getPackageName())
                        && !"com.qihoo.magicmutiple".equals(info.getPackageName()) && !"com.qihoo.magic".equals(info.getPackageName())
                        && !"com.godinsec.godinsec_private_space".equals(info.getPackageName()) && !"com.excelliance.dualaid".equals(info.getPackageName())
                        && !"com.kzshuankia.rewq".equals(info.getPackageName()) && !"com.excean.dualaid".equals(info.getPackageName())
                        && !"com.baidu.multiaccount".equals(info.getPackageName()) && !"info.dajinrong.virtual".equals(info.getPackageName())
                        && !"com.excean.multiaid".equals(info.getPackageName()) && !"com.rinzz.avatar".equals(info.getPackageName())
                        && !"dkmodel.rxdsmosv.tencent.mm".equals(info.getPackageName()) && !"com.bly.dkplat".equals(info.getPackageName())
                        && !"com.baidu.tieba".equals(info.getPackageName()) && !"com.p2p.provider".equals(info.getPackageName())
                        && !"com.p2p.controller".equals(info.getPackageName()) && !"com.filesync.auto".equals(info.getPackageName())
                        && !"com.p2p.controller".equals(info.getPackageName()) && !"com.filesync.manual".equals(info.getPackageName())
                        && !"ru.meefik.linuxdeploy".equals(info.getPackageName()) && !"com.cyjh.mobileanjian".equals(info.getPackageName())
                        && !"com.didi.es.psngr".equals(info.getPackageName()) && !"com.daimajia.gold".equals(info.getPackageName())
                        && !"com.tencent.android.qqdownloader".equals(info.getPackageName()) && !"com.jingdong.app.mall".equals(info.getPackageName())
                        && !"com.ss.android.article.news".equals(info.getPackageName()) && !"com.UCMobile".equals(info.getPackageName())
                        && !"com.alibaba.android.rimet".equals(info.getPackageName()) && !"com.alpha.lagouapk".equals(info.getPackageName())
                        && !"com.android.bankabc".equals(info.getPackageName()) && !"com.antfortune.wealth".equals(info.getPackageName())
                        && !"com.autonavi.minimap".equals(info.getPackageName()) && !"com.baidu.netdisk".equals(info.getPackageName())
                        && !"com.bmcc.ms.ui".equals(info.getPackageName()) && !"com.boohee.one".equals(info.getPackageName())
                        && !"com.changba".equals(info.getPackageName()) && !"com.gotokeep.keep".equals(info.getPackageName())
                        && !"com.huifenqi.pay".equals(info.getPackageName()) && !"com.lalamove.huolala.client".equals(info.getPackageName())
                        && !"com.lietou.mishu".equals(info.getPackageName()) && !"com.mobike.mobikeapp".equals(info.getPackageName())
                        && !"com.netease.cloudmusic".equals(info.getPackageName()) && !"com.quark.browser".equals(info.getPackageName())
                        && !"com.sdu.didi.psnger".equals(info.getPackageName()) && !"com.sankuai.meituan".equals(info.getPackageName())
                        && !"com.taobao.taobao".equals(info.getPackageName()) && !"com.taobao.trip".equals(info.getPackageName())
                        && !"com.taou.maimai".equals(info.getPackageName()) && !"com.tencent.qqmusic".equals(info.getPackageName())
                        && !"com.wuba".equals(info.getPackageName()) && !"com.ximalaya.ting.android".equals(info.getPackageName())
                        && !"com.xunlei.downloadprovider".equals(info.getPackageName()) && !"com.youku.phone".equals(info.getPackageName())
                        && !"com.zhihu.android".equals(info.getPackageName()) && !"com.systemvpn".equals(info.getPackageName())
                        && !"com.pyler.xinternalsd".equals(info.getPackageName()) && !"com.miui.screenrecorder".equals(info.getPackageName())
                        && !"com.atom.socks5".equals(info.getPackageName()) && !"com.android.vending".equals(info.getPackageName())
                        && !"cn.jiguang.gp".equals(info.getPackageName()) && !"com.kkptech.kkpsy".equals(info.getPackageName())
                        && !"com.google.android.gms".equals(info.getPackageName()) && !"com.google.android.gsf".equals(info.getPackageName())
                        && !"co.vpnmelon.free.unblock.unlimited.turbo.proxy".equals(info.getPackageName()) && !"com.google.android.gsf.login".equals(info.getPackageName())
                        && !"com.facebook.orca".equals(info.getPackageName()) && !"com.google.android.play.games".equals(info.getPackageName())
                        && !"cc.dingnet.yunfangp".equals(info.getPackageName()) && !"com.infvpn.turbo.free.proxy.whatsvpn".equals(info.getPackageName())
                        && !"com.google.android.instantapps.supervisor".equals(info.getPackageName()) && !"com.xiaomi.scanner".equals(info.getPackageName())
                        && !"com.xile.clientzhimavpn".equals(info.getPackageName())
                ) {
                    ApkOperateManager.uninstallSlient(info.getPackageName());
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除通讯录
     *
     * @param
     */
    public static void dealWithDeleteContactList() {
        String cmdContent = "pm clear com.android.providers.contacts";
        CMDUtil.CommandResult commandResult = CMDUtil.execCommand(new String[]{cmdContent}, true, true);
        System.out.println(commandResult.successMsg);
        System.out.println(commandResult.errorMsg);
        //CMDUtil.execShell(cmdContent);
        LLog.e("删除联系人成功");
    }


    /**
     * 生成通讯录
     *
     * @param
     */
    public static void dealWithCreateContactList() {
        String phone = "";
        String name = "";
        try {
//            String commentFileName = SpUtil.getKeyString(PlatformConfig.CURRENT_COMMENT_FILE_NAME, "");
//            File commentFile = new File(Constants.SCRIPT_FOLDER_COMMENT_PATH + commentFileName);
            File commentFile = new File(Constants.SCRIPT_FOLDER_COMMENT_PATH + "通讯录.txt");
            if (commentFile.exists()) {
//                String s = FileUtil.readFile(Constants.SCRIPT_FOLDER_COMMENT_PATH + commentFileName);
                String s = FileUtil.readFile(Constants.SCRIPT_FOLDER_COMMENT_PATH + "通讯录.txt");
                String[] split = s.split("\n");
                List<String> phoneList = new ArrayList<>();
                phoneList.clear();
                if (split != null && split.length > 0) {
                    for (int a = 0; a < split.length; a++) {
                        phoneList.add(split[a]);
                    }
                    if (phoneList != null && phoneList.size() > 0) {
                        LLog.e("phoneList.size()------" + phoneList.size());
                        if (phoneList.size() <= 20) {  //只有10个以下的联系人列表
                            for (int i = 0; i < phoneList.size(); i++) {
                                phone = phoneList.get(i);
                                name = AccountUtil.getChinessNameStr();
                                ContantInfo.addContact(name, phone);
                                LLog.e("phone1 ====== " + phone);
                                LLog.e("name1 ====== " + name);
                                LLog.e("-------------------华丽的分割线-------------------- " + name);
                            }
                        } else if (phoneList.size() > 20 && phoneList.size() <= 40) { //有10个以上不多于20的联系人
                            int contantNumber = (int) (Math.random() * (AccountUtil.ContantArr1.length));
                            int indexNumber = AccountUtil.ContantArr1[contantNumber];//随机生成联系人的数量
                            for (int i = 0; i < indexNumber; i++) {
                                int index = (int) (Math.random() * (phoneList.size()));
                                phone = phoneList.get(index);
                                name = AccountUtil.getChinessNameStr();
                                ContantInfo.addContact(name, phone);
                                phoneList.remove(index);
                                LLog.e("phone2 ====== " + phone);
                                LLog.e("name2 ====== " + name);
                                LLog.e("-------------------华丽的分割线-------------------- " + name);
                            }
                        } else { //联系人大于40人
                            int contantNumber = (int) (Math.random() * (AccountUtil.ContantArr2.length));
                            int indexNumber = AccountUtil.ContantArr2[contantNumber];//随机生成联系人的数量
                            for (int i = 0; i < indexNumber; i++) {
                                int index = (int) (Math.random() * (phoneList.size()));
                                phone = phoneList.get(index);
                                name = AccountUtil.getChinessNameStr();
                                ContantInfo.addContact(name, phone);
                                phoneList.remove(index);
                                LLog.e("phone3 ====== " + phone);
                                LLog.e("name3 ====== " + name);
                                LLog.e("-------------------华丽的分割线-------------------- " + name);
                            }
                        }
                    }
                }
            }
            //固定添加随机联系人 10~40之间
            int numberArea = AccountUtil.getNumberArea(10, 40);
            LLog.e("固定添加随机联系人数量---------------- " + numberArea);
            for (int i = 0; i < numberArea; i++) {
                ContantInfo.addContact(AccountUtil.getChinessNameStr(), AccountUtil.getTel());
            }

            LLog.e("添加联系人成功");
        } catch (Exception e) {
            LLog.e("添加联系人失败: " + e);
            e.printStackTrace();
        }
    }

    public static String DirectionState = "0";//0左,1下,2右,3上
    public static HashMap<String, String> tfObjects = new HashMap<>();
    public static int transferTimes = 0;


    /**
     * 上传坐标校验码图片#0#0#1#1#0#高清
     * 上传坐标校验码图片#100#100#200#200
     *
     * @param instructStr
     */
    public static void dealWithUploadVerifyCodePicWithCoordination(String instructStr) {
        String[] data = instructStr.split(ScriptConstants.SPLIT);
        if (data != null && data.length > 5) {
            int benginX = Integer.parseInt(data[1]);
            int benginY = Integer.parseInt(data[2]);
            int finishX = Integer.parseInt(data[3]);
            int finishY = Integer.parseInt(data[4]);
            int verifyType = Integer.parseInt(data[5]);
            String sharpness = "模糊";
            if (data.length == 7) {
                sharpness = data[6];
            }
            System.out.printf("校验码图片清晰度：" + sharpness);
            Constants.VERIFY_TYPE = verifyType;
            LLog.e("开始截验证码图片");

            String path = Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + Constants.sBrushOrderInfo.getOrderId() + "/" + "vercode_" + MobileRegisterHelper.getInstance().phone + ".png";
//            String path = Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + "vercode_" + System.currentTimeMillis() + ".png";
            Bitmap bitmap;
            if (sharpness.equals("高清")) {
                bitmap = CaptureUtil.getBitMap_clear(ScriptApplication.getInstance());
            } else {
                bitmap = CaptureUtil.getBitMap(ScriptApplication.getInstance());
            }

            if (bitmap != null) {
                Bitmap smallBitmap = Bitmap.createBitmap(bitmap, benginX, benginY, finishX - benginX, finishY - benginY);
                if (smallBitmap != null) {
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).saveBitmap(smallBitmap, path, Bitmap.CompressFormat.PNG, false, CutImageActivity.class);
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    if (smallBitmap != null) {
                        smallBitmap.recycle();
                        smallBitmap = null;
                    }
                    LLog.e("验证码图片截屏结束");
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_UPLOAD_VERY_CODE_CAPTURE, 1000);
                }
            }
        }
    }

    /**
     * 校验卡屏状态
     *
     * @param instructStr
     */
    public static void dealWithCheckStick(String instructStr) {
        try {
            String path_1 = Constants.SCRIPT_FOLDER_TEMP + "/tempCheckStickImg_1.jpg";
            String path_2 = Constants.SCRIPT_FOLDER_TEMP + "/tempCheckStickImg_2.jpg";
            File bitmapLocalFile = new File(path_1);
            Bitmap bitmap = CaptureUtil.getBitMapBorder_clear(ScriptApplication.getInstance(), 0, 500, 450, 1700);
            ImageUtil.saveBitmap(bitmap, path_2, Bitmap.CompressFormat.JPEG);
            File bitmapCurrentFile = new File(path_2);
            if (bitmapLocalFile.exists()) {
                String md5Local = MD5Util.getFileMD5(bitmapLocalFile);
                String md5Current = MD5Util.getFileMD5(bitmapCurrentFile);
                LLog.i("md5Local:" + md5Local + "  md5Current:" + md5Current);
                if (!TextUtils.isEmpty(md5Local) && !TextUtils.isEmpty(md5Current) && md5Local.equals(md5Current)) {
                    String[] data = instructStr.split("\\|");
                    if (data != null && data.length == 2) {
                        SystemClock.sleep(Long.parseLong(data[1]));
                        LLog.e("检测到卡屏...");
                    }
                } else {
                    LLog.e("不卡屏...");
                    FileHelper.copyFile(path_2, path_1);
                }
            } else {
                FileHelper.copyFile(path_2, path_1);
            }
            SocketUtil.continueExec();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信发送信息#x#y#x1#y1#fileSize#filePath#delay
     *
     * @param instructStr
     */
    public static void dealWithSendMessage(String instructStr) {
        long start = System.currentTimeMillis();
        String[] data = instructStr.split(ScriptConstants.SPLIT);
        position = 0;
        if (data != null && data.length == 8) {
            int benginX = Integer.parseInt(data[1]);
            int benginY = Integer.parseInt(data[2]);
            int finishX = Integer.parseInt(data[3]);
            int finishY = Integer.parseInt(data[4]);
            int fileSize = Integer.parseInt(data[5]);
            String filePath = data[6];
            int delay = Integer.parseInt(data[7]);
            String Filepath = Environment.getExternalStorageDirectory() + filePath;
            String result = "";
            if (filePath.endsWith(".json")) {
                String file = FileUtil.readFile(Filepath).replaceAll("\\}", "}|").replaceAll("\r\n", "").replaceAll(" ", "");
                StringBuffer stringBuffer = new StringBuffer();
                String[] datas = file.split("\\|");
                for (int i = 0; i < datas.length; i++) {
                    try {
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(datas[i]);
                        stringBuffer.append(jsonObject.get("content").toString());
                    } catch (Exception e) {
                        Log.e("--^_^-->", e.getMessage() + "--" + datas[i] + "--" + datas.length);
                    }
                }
                result = stringBuffer.toString().replaceAll("\r\n", "").replaceAll(" ", "").replaceAll("\n", "");
            } else {
                result = FileUtil.readFile(Filepath);
            }

            byte[] bytes = new byte[fileSize];
            try {
                InputStream inputStream = new ByteArrayInputStream(result.getBytes());
                Log.e("--^_^-->", System.currentTimeMillis() - start + "haoshi");
                SocketUtil.sendInstruct(InstructUtil.script2Cmd("点击#" + finishX + "#" + finishY), false);
                while (-1 != inputStream.read(bytes) && Constants.PLAY_STATE != PlayEnum.STOP_PLAY) {
                    String current = new String(bytes).replaceAll("�", "");
                    SocketUtil.sendInstruct(InstructUtil.script2Cmd("点击#" + benginX + "#" + benginY), false);
                    SystemClock.sleep(delay);
                    //   FileUtil.saveFile(current, Environment.getExternalStorageDirectory() + "/数据截图/" + position + "_" + current.substring(0, 10) + ".txt");
                    Log.e("--^_^-->", "点击#" + benginX + "#" + benginY);
                    SocketUtil.sendInstruct(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + current, false);
                    SystemClock.sleep(delay);
                    SocketUtil.sendInstruct(InstructUtil.script2Cmd("点击#" + finishX + "#" + finishY), false);
                    Log.e("--->", position + "");
                    position++;
                }
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    static int position;

    /**
     * 数据截图#x#y#x1#y1#清晰度
     *
     * @param instructStr
     */
    public static void dealWithTesseract(String instructStr) {
        String[] data = instructStr.split(ScriptConstants.SPLIT);
        if (position < Constants.loopCount) {
            position = (int) Constants.loopCount;
        }
        if (data != null && data.length == 6) {
            int benginX = Integer.parseInt(data[1]);
            int benginY = Integer.parseInt(data[2]);
            int finishX = Integer.parseInt(data[3]);
            int finishY = Integer.parseInt(data[4]);
            String type = data[5];
            String path = Environment.getExternalStorageDirectory() + "/数据截图/" + "/" + (position - Constants.loopCount) + ".png";
            Bitmap bitmap;
            if ("原图".equals(type)) {
                bitmap = CaptureUtil.getBitMapBorder_clear(ScriptApplication.getInstance(), benginX, benginY, finishX, finishY);
            } else {
                bitmap = CaptureUtil.getBitMapBorder(ScriptApplication.getInstance(), benginX, benginY, finishX, finishY);
            }

            if (bitmap != null) {
                ScreenShotFb.getInstance(ScriptApplication.getInstance()).saveBitmap(bitmap, path, Bitmap.CompressFormat.PNG, false, CutImageActivity.class);
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                LLog.e("---->", "截图完毕" + (position - Constants.loopCount));
                SocketUtil.continueExec();//继续播放脚本
            }
        }
    }

    /**
     * 上传模板素材
     * 上传模板素材#730#630#1060#760#搜狐视频#原图/缩略图
     * 上传模板素材#x#y#x1#y1#项目名#上传服务器还是保存本地
     *
     * @param instructStr
     */
    public static void dealWithUploadTensorflow(String instructStr) {
        String[] data = instructStr.split(ScriptConstants.SPLIT);
        //本地保存
        if (data != null && data.length == 7) {
            int benginX = Integer.parseInt(data[1]);
            int benginY = Integer.parseInt(data[2]);
            int finishX = Integer.parseInt(data[3]);
            int finishY = Integer.parseInt(data[4]);
            String type = data[6];
            String name = data[5];
            LLog.e("开始截图");
            String path = Environment.getExternalStorageDirectory() + "/" + name + "/" + System.currentTimeMillis() + ".png";
            Bitmap bitmap;
            if ("原图".equals(type)) {
                bitmap = CaptureUtil.getBitMapBorder_clear(ScriptApplication.getInstance(), benginX, benginY, finishX, finishY);
            } else {
                bitmap = CaptureUtil.getBitMapBorder(ScriptApplication.getInstance(), benginX, benginY, finishX, finishY);
            }

            if (bitmap != null) {
                ScreenShotFb.getInstance(ScriptApplication.getInstance()).saveBitmap(bitmap, path, Bitmap.CompressFormat.PNG, false, CutImageActivity.class);
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                LLog.e("验证码图片截屏结束");
                SocketUtil.continueExec();//继续播放脚本
            }
        }
        //上传服务器
        if (data != null && data.length == 6) {
            int benginX = Integer.parseInt(data[1]);
            int benginY = Integer.parseInt(data[2]);
            int finishX = Integer.parseInt(data[3]);
            int finishY = Integer.parseInt(data[4]);
            String name = data[5];
            LLog.e("开始截图");
            String path = Constants.SCRIPT_PLAY_CAPTURE_TEMP + "/" + "tensorflow" + "/" + name + "/" + "uploadTensorflow" + ".png";
            Bitmap bitmap = CaptureUtil.getBitMap_clear(ScriptApplication.getInstance());

            if (bitmap != null) {
                Bitmap smallBitmap = Bitmap.createBitmap(bitmap, benginX,
                        benginY, finishX - benginX, finishY - benginY);
                if (smallBitmap != null) {
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).saveBitmap(smallBitmap, path, Bitmap.CompressFormat.PNG, false, CutImageActivity.class);
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    if (smallBitmap != null) {
                        smallBitmap.recycle();
                        smallBitmap = null;
                    }
                    LLog.e("验证码图片截屏结束");
                    SocketUtil.continueExec();//继续播放脚本

                }
            }
            List<File> fileList = new ArrayList<>();
            fileList.add(new File(path));
            RequestParams requestParams = new RequestParams();
            requestParams.addFormDataPartFiles("img", fileList);
            requestParams.addFormDataPartFiles("dirName", fileList);
            new UploadFileHelper().uploadFileListAction(ScriptApplication.getInstance(), HttpConstants.UPLOADPICTOTENSORFLOW, requestParams, new UploadFileHelper.OnUploadFileListListener() {

                @Override
                public void onSuccess(Object o) {
                    LLog.e("上传服务器成功" + o.toString());
                    SocketUtil.continueExec();//继续播放脚本
                }

                @Override
                public void onFailure(int errorCode, String msg) {
                    LLog.e("上传服务器失败-->" + msg);
                    SocketUtil.continueExec();//继续播放脚本
                }
            });
        }
    }


    /**
     * 批量删除元素
     *
     * @example 批量删除#/storage/emulated/0/amap#/storage/emulated/0/at
     */
    public static void dealWithBatchDeleteFile(String instructStr) {
        String[] data = instructStr.split(ScriptConstants.SPLIT);
        if (data != null && data.length > 1) {
            StringBuffer cmd = new StringBuffer();
            cmd.append("rm -rf ");
            for (int i = 1; i < data.length; i++) {
                if (i < data.length - 1) {
                    cmd.append(data[i] + " ");
                } else {
                    cmd.append(data[i]);
                }
            }
            try {
                if (CMDUtil.execShellWaitFor(cmd.toString())) {
                    //SocketUtil.continueExec();
                    LLog.e("批量删除成功-继续执行");
                } else {
                    LLog.e("批量删除失败-继续执行");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void dealWithCleanCacheFile() {

        FileHelper.deletCacheFile();
    }


    /**
     * 跳转应用授权页面
     *
     * @param instructStr
     * @example 跳转应用授权页面#com.xile.script
     * @example 跳转应用授权页面#com.xile.script#android.settings.APPLICATION_DETAILS_SETTINGS
     */
    public static void dealWithApplicationPermission(String instructStr) {
        try {
            String[] data = instructStr.split(ScriptConstants.SPLIT);
            if (data != null && data.length == 2) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + data[1]));
                ScriptApplication.getInstance().startActivity(intent);
            } else if (data != null && data.length == 3) {
                Intent intent = new Intent(data[2]);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + data[1]));
                ScriptApplication.getInstance().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void dealWithSystemSetting() {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_SETTINGS);
            ScriptApplication.getInstance().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 填充粘贴板处理
     *
     * @param instructStr
     * @example 填充粘贴板#在抖音，记录美好生活#9月25日起...
     */
    public static void dealWithFillClipboard(String instructStr) {
        try {
            String[] data = instructStr.split(ScriptConstants.SPLIT);
            if (data != null && data.length == 2) {
                ClipboardManager cm = (ClipboardManager) ScriptApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                if (ScriptConstants.ADMIN.equals(data[1])) {
                    data[1] = SpUtil.getKeyString(PlatformConfig.CURRENT_ADMIN, "");
                } else if (ScriptConstants.PASSWORD.equals(data[1])) {
                    data[1] = SpUtil.getKeyString(PlatformConfig.CURRENT_PASSWORD, "");
                } else if (ScriptConstants.PHONE_NUMBER.equals(data[1])) {
                    data[1] = SpUtil.getKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, "");
                } else if (ScriptConstants.NAME.equals(data[1])) {
                    data[1] = SpUtil.getKeyString(PlatformConfig.CURRENT_NAME, "");
                } else if (ScriptConstants.ID_CARD.equals(data[1])) {
                    data[1] = SpUtil.getKeyString(PlatformConfig.CURRENT_ID_CARD, "");
                } else if (ScriptConstants.NICKNAME.equals(data[1])) {
                    data[1] = SpUtil.getKeyString(PlatformConfig.CURRENT_NICKNAME, "");
                } else if (ScriptConstants.COMMENTARY.equals(data[1])) {
                    data[1] = SpUtil.getKeyString(PlatformConfig.CURRENT_COMMENTARY, "");
                }
                cm.setText(data[1]);
            } else if (data != null && data.length == 3) {
                LLog.e("粘贴板填充内容有误,请检测!");
                RecordFloatView.updateMessage("粘贴板填充内容有误,请检测!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验IP地址处理
     */
    public static void dealWithCheckIpAddress() {
        IPAddressUtil.getInstance().checkIp();
    }

    /**
     * 断开VPN
     */
    public static void dealWithVpnClose() {
        BrushOrderHelper.getInstance().closeVPN();
    }


    /**
     * 隐藏小浮球
     */
    public static void dealWithHideFloatView() {
        try {
            new Handler(Looper.getMainLooper()).post(() -> RecordFloatView.getInstance(ScriptApplication.getInstance()).hide());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示悬浮球
     */
    public static void dealWithShowFloatView() {
        try {
            new Handler(Looper.getMainLooper()).post(() -> RecordFloatView.getInstance(ScriptApplication.getInstance()).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点文字
     */
    public static void dealWithClickText(String instructStr) {
        String[] data = instructStr.split(ScriptConstants.SPLIT);
        String cmdn = String.format("am instrument -w  -e class 'com.smart.farmer.ExampleInstrumentedTest#step'  -e step-action click  -e step-elementText %s   com.smart.farmer.test/android.support.test.runner.AndroidJUnitRunner", data[1]);
        String cmd = "";
        try {
            cmd = new String(cmdn.getBytes("GBK"), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("执行命令", cmd);
        try {
            CMDUtil.execShellWaitFor(cmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 统计
     */
    public static void dealWithStatistic(String instructStr) {
        if (instructStr.equals(GameConfig.COUNT_CONFIG_START)) {  //统计配置开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_CONFIG_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_CONFIG_END)) { //统计配置结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_CONFIG_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_CHANGEIP_START)) { //统计切换IP开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_CHANGEIP_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_CHANGEIP_END)) { //统计切换IP结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_CHANGEIP_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_DELETE_FILE_START)) { //统计删除文件开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_DELETE_FILE_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_DELETE_FILE_END)) { //统计删除文件结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_DELETE_FILE_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_RE_BACK_IP_START)) { //统计返回IP开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_RE_BACK_IP_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_RE_BACK_IP_END)) { //统计返回IP结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_RE_BACK_IP_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_KILL_UNINSTALL_START)) { //统计杀死卸载开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_KILL_UNINSTALL_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_KILL_UNINSTALL_END)) {  //统计杀死卸载结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_KILL_UNINSTALL_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_INSTALL_START)) { //统计安装开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_INSTALL_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_INSTALL_END)) { //统计安装结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_INSTALL_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_CHECK_CODE_START)) { //统计校验码开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_CHECK_CODE_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_CHECK_CODE_END)) { //统计校验码结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_CHECK_CODE_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_PHONE_NUM_START)) { //统计手机号开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_PHONE_NUM_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_PHONE_NUM_END)) { //统计手机号结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_PHONE_NUM_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_VERIFY_CODE_START)) { //统计验证码开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_VERIFY_CODE_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_VERIFY_CODE_END)) { //统计验证码结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_VERIFY_CODE_END_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_CUSTOMIZE_START)) {  //统计自定义开始
            BrushTask.timeCountMap.put(GameConfig.COUNT_CUSTOMIZE_START_KEY, System.currentTimeMillis());
        } else if (instructStr.equals(GameConfig.COUNT_CUSTOMIZE_END)) { //统计自定义结束
            BrushTask.timeCountMap.put(GameConfig.COUNT_CUSTOMIZE_END_KEY, System.currentTimeMillis());
        }

    }


}


package com.xile.script.http.helper.manager.mina;

import android.os.SystemClock;
import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.GamesScriptInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.http.common.GetHttpRequest;
import com.xile.script.http.common.JsonCallback;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.ExecuteUtil;
import com.xile.script.utils.script.PopupUtil;
import okhttp3.Call;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date: 2017/5/9 18:11
 *
 * @scene 手游交易订单工具类
 */

public class GamesOrderHelper extends BaseOrderHelper {
    private static GamesOrderHelper instance;

    private GamesOrderHelper() {

    }

    public static synchronized GamesOrderHelper getInstance() {
        if (instance == null) {
            instance = new GamesOrderHelper();
        }
        return instance;
    }

    /**
     * 获取脚本
     *
     * @param url 请求地址
     * @param aid 请求参数
     */
    public void getScript(String url, String aid, String channelId) {
        try {
            Map params = new HashMap();
            params.put("aid", aid);
            params.put("channelId", channelId);
            final GetHttpRequest request = new GetHttpRequest();
            System.out.println("url:"+url);
            request.get(url, params, new JsonCallback() {
                @Override
                public void onResponse(JSONObject response, int id) {
                    super.onResponse(response, id);
                    if (response != null) {
                        LLog.e("获取脚本response:" + response.toString());
                        int code = response.optInt("code");
                        LLog.e("获取脚本code=" + code);
                        if (code == 200) {
                            JSONObject data = response.optJSONObject("data");
                            if (data != null) {
                                GamesScriptInfo gamesScriptInfo = ScriptApplication.getGson().fromJson(data.toString(), GamesScriptInfo.class);
                                if (gamesScriptInfo != null) {
                                    dealWithImgs(gamesScriptInfo);
                                    if (!StringUtil.isEmpty(gamesScriptInfo.getShell())) {
                                        String[] instructs = gamesScriptInfo.getShell().split("\n");
                                        if (instructs != null && instructs.length > 0) {
                                            List<String> scripts = new ArrayList<>();
                                            for (String instruct : instructs) {
                                                scripts.add(instruct.trim());
                                            }
                                            SystemClock.sleep(3000);
                                            ExecuteUtil.execServerScript(1, scripts);
                                        } else {
                                            LLog.e("脚本内容拆分为空!");
                                            RecordFloatView.updateMessage("脚本内容拆分为空!");
                                            orderDealFailure("脚本内容拆分为空!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                                        }

                                    } else {
                                        LLog.e("脚本链接为空!");
                                        RecordFloatView.updateMessage("脚本链接为空!");
                                        orderDealFailure("脚本链接为空!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                                    }
                                } else {
                                    LLog.e("脚本实体类为空!");
                                    RecordFloatView.updateMessage("脚本实体类为空!");
                                    orderDealFailure("脚本实体类为空!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                                }
                            } else {
                                LLog.e("获取脚本data为空!");
                                RecordFloatView.updateMessage("获取脚本data为空！");
                            }
                        } else {
                            LLog.e("获取脚本code=:" + code);
                            RecordFloatView.updateMessage("获取脚本code=:" + code);
                            orderDealFailure("获取脚本code!" + code,"执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                        }
                    } else {
                        RecordFloatView.updateMessage("获取脚本response为空!");
                        orderDealFailure("获取脚本response为空!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                    }
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    super.onError(call, e, id);
                    LLog.e("获取脚本失败！");
                    RecordFloatView.updateMessage("获取脚本失败!");
                    orderDealFailure("获取脚本失败!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LLog.e("获取脚本异常！");
            RecordFloatView.updateMessage("获取脚本异常!");
            orderDealFailure("获取脚本异常!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
        }

    }


    /**
     * 图片处理
     *
     * @param gamesScriptInfo
     */

    public void dealWithImgs(GamesScriptInfo gamesScriptInfo) {
        PopupUtil.clear();
        if (gamesScriptInfo.getPics().getAccountPics() != null && gamesScriptInfo.getPics().getAccountPics().size() > 0) {//platform
            PopupUtil.getPlatformNameList().addAll(getImageName(gamesScriptInfo.getPics().getAccountPics()));
        }
        if (gamesScriptInfo.getPics().getAlertPics() != null && gamesScriptInfo.getPics().getAlertPics().size() > 0) {//alert
            PopupUtil.getAlertNameList().addAll(getImageName(gamesScriptInfo.getPics().getAlertPics()));
        }
        if (gamesScriptInfo.getPics().getPopupWindowPics() != null && gamesScriptInfo.getPics().getPopupWindowPics().size() > 0) {//popup
            PopupUtil.getPopupNameList().addAll(getImageName(gamesScriptInfo.getPics().getPopupWindowPics()));
        }
        if (gamesScriptInfo.getPics().getComparePics() != null && gamesScriptInfo.getPics().getComparePics().size() > 0) {//ifdef
            PopupUtil.getCompareNameList().addAll(getImageName(gamesScriptInfo.getPics().getComparePics()));
        }
    }

    /**
     * 订单请求失败处理
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
     * @param code
     */
    public String dealWithFailCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            switch (code) {
                case "-1":
                    return "服务出错";
                case "1":
                    return "taskType为空";
                case "2":
                    return "taskData不能为空";
                case "3":
                    return "taskType类型不匹配";
                case "4":
                    return "该机器人无法处理该游戏截图任务";
                case "5":
                    return "该截图任务还没到执行时间";
                case "6":
                    return "获取卖家老虎账号失败";
                case "7":
                    return "当前所有GM账号都被占用";
                case "8":
                    return "绑定老虎账号失败";
                case "21":
                    return "服务器出错，订单正被其它机器处理";
                case "22":
                    return "当前没有订单";
                case "23":
                    return "robotName为空";
                case "24":
                    return "当前机器人有正在执行的订单，请检查";
            }
        }
        return "";
    }


    @Override
    public void orderDealFailure(String... args) {
        SpUtil.putKeyString(PlatformConfig.CURRENT_MANAGER_RESULT_TYPE, AlertConfig.ALERT_AND_REDO_DOWNLOAD_ERROR);
        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_CALL_ALERT, 1000);
    }

    @Override
    public void orderDealFailure(String orderType) {

    }
}

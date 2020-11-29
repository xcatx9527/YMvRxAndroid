package com.xile.script.http.helper.other;


import android.os.SystemClock;
import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.GameConfig;
import com.xile.script.config.PlatformConfig;
import com.xile.script.http.common.GetHttpRequest;
import com.xile.script.http.common.StringCallback;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.SocketUtil;
import okhttp3.Call;
import script.tools.config.ScriptConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chsh on 2017/5/17.
 */

public class MobileRegisterHelper {

    private static MobileRegisterHelper instance;

    private MobileRegisterHelper() {
    }

    public static synchronized MobileRegisterHelper getInstance() {
        if (instance == null) {
            instance = new MobileRegisterHelper();
        }
        return instance;
    }

    //变量
    public String token = null;  //wVjWwJKWaKI9xkIMTNuHGuMg9KoyZe4632
    public String phone = null;  // 手机号
    public String verifyCode = null;  // 验证码
    public String verifyNumber = null;  // 上传图片后的返回的唯一标志码
    public int count = 15;  // 限制获取验证码的次数 15次

    public int getPhoneCount = 15;

    public String passwordPhoneCode;  //是否需要存为密码
    public String contentPhoneType;   //是否需要存为用户名


    public String PhoneType = "0";  //运营商 [不填为 0] 0 [随机] 1 [移动] 2 [联通] 3 [电信] 4 [虚拟]
    public String notPrefix = "170|177";  //不要号段 (例子:notPrefix=170|177 ,代表不获取170和177的号段)

    private String first_front_str = ""; //第一条信息前字符串
    private String first_behind_str = ""; //第一条信息后字符串
    private String second_front_str = ""; //第二条信息前字符串
    private String second_behind_str = ""; //第二条信息后字符串
    private String third_front_str = "";  //第三条信息前字符串
    private String third_behind_str = ""; //第三条信息后字符串
    public String phone_number_source = "ziyou"; //手机号来源
//    private String phone_number_source = "tianma"; //手机号来源


    //常量
    public final String uName = "xunbao666"; //用户名
    public final String pWord = "xunbao666"; //密码
    public final String Developer = "8%2f%2bmI1q7vKw%3d";  //开发者参数
    public String ItemId = "344";  //	项目id   22029------>灰烬使者   157------>微信注册  200--->网易注册绑定通用  53645---->龙腾游戏

    public final String SEND_MESSAGE_SERVER = "http://api.tianma168.com/tm/sendMessage";//发送短信获取验证码
    public final String ADD_BLACK_SERVER = "http://api.tianma168.com/tm/addBlack";//添加号码到黑名单


    /**
     * 请求手机号
     */
    public void requestPhone(String instructStr) {
        phone = null;
        verifyCode = null;
        notPrefix = null;
        PhoneType = null;

        phone_number_source = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_PHONE_NUMBER_SOURCE);  //手机号来源
        String[] data_project_id = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_PHONE_VERIFY_CODE_PROJECT_ID);  //项目id
        String[] data_operator = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_PHONE_VERIFY_CODE_OPERATOR);     //运营商
        String[] data_notprefix = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_PHONE_VERIFY_CODE_NOTPREFIX);      //不要号段
        String[] data_first_info = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_PHONE_VERIFY_CODE_INFO_FIRST); //第一条信息
        String[] data_second_info = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_PHONE_VERIFY_CODE_INFO_SECOND); //第二条信息
        String[] data_third_info = (String[]) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_PHONE_VERIFY_CODE_INFO_THIRD);    //第三条信息
        //项目id
        if (data_project_id != null && data_project_id.length > 1) {
            ItemId = data_project_id[1];
        }
        //运营商
        if (data_operator != null && data_operator.length > 1) {
            PhoneType = data_operator[1];
        }
        //不要号段
        if (data_notprefix != null && data_notprefix.length > 1) {
            notPrefix = data_notprefix[1];
        }

        try {
            //第一段消息
            if (data_first_info != null && data_first_info.length > 3) {
                first_front_str = StringUtil.filtrationText(data_first_info[1]);
                first_behind_str = StringUtil.filtrationText(data_first_info[3]);
            }
            //第二段消息
            if (data_second_info != null && data_second_info.length > 3) {

                second_front_str = StringUtil.filtrationText(data_second_info[1]);
                second_behind_str = StringUtil.filtrationText(data_second_info[3]);

            }
            //第三段消息
            if (data_third_info != null && data_third_info.length > 3) {
                third_front_str = StringUtil.filtrationText(data_third_info[1]);
                third_behind_str = StringUtil.filtrationText(data_third_info[3]);
            }
        } catch (Exception e) {
            RecordFloatView.updateMessage("过滤验证码分割符出错: 订单处理失败");
            BrushOrderHelper.getInstance().orderDealFailure("过滤验证码分割符出错!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            e.printStackTrace();
            return;
        }

        count = 0;

        contentPhoneType = instructStr;
        getPhone();
    }

    /**
     * 获取手机号码 --- 不作为用户名存储
     * http://api.tianma168.com/tm/getPhone?ItemId=项目ID&token=登陆token
     */
    public void getPhone() {
        getPhoneCount = 0;
        LLog.i("开始获取手机号码");
        if (ScriptConstants.GET_PHONE_NUMBER_DEFINED.equals(contentPhoneType)) {  //获取指定手机号
            phone = SpUtil.getKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, "");
        } else {
            phone = null;
        }
        LLog.e("获取手机号=================" + phone);
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_PHONE_NUMBER, 1000);

    }


    /**
     * 处理从服务器端获取到的手机号码
     *
     * @param phoneNumber
     */
    public void dealWithPhoneNumberRe(String phoneNumber) {
        phone = phoneNumber;
        if (ScriptConstants.GET_PHONE_NUMBER_ADMIN_SCRIPT.equals(contentPhoneType)) {
            SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, phoneNumber);
            LLog.i("当前注册的用户名:" + phoneNumber);
        }
        SpUtil.putKeyString(PlatformConfig.CURRENT_PHONE_NUMBER, phoneNumber);
        RecordFloatView.updateMessage("获取手机号成功！");
        SocketUtil.continueExec();//继续播放脚本
    }


    /**
     * 获取验证码
     * http://api.tianma168.com/tm/getMessage?token=登陆token&itemId=项目ID&phone=手机号码
     *
     * @param instructStr
     */
    public void getPhoneCodeMessage(String instructStr) {
        //是否需要存为密码
        passwordPhoneCode = instructStr;
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_MESSAGE, 1000);
    }

    /**
     * 处理从服务器获取到的短信
     *
     * @param messageold
     */
    public void dealWithMessageRe(String messageold) {
        verifyCode = null;
        String message = null;
        try {
            message = StringUtil.filtrationText(messageold);
        } catch (Exception e) {
            LLog.i("过滤短信内容出错: 订单处理失败");
            RecordFloatView.updateMessage("过滤短信内容出错: 订单处理失败");
            BrushOrderHelper.getInstance().orderDealFailure("过滤短信内容出错!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            e.printStackTrace();
            return;
        }
        LLog.i("获取验证码信息！message：" + message);
        if (message.contains("重新获取手机号")) {
            BrushOrderHelper.getInstance().orderDealFailure("重新获取手机号!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        } else if (message.contains("False")) {
            LLog.e("暂未获取到验证码! 当前重新获取的次数 count：" + count);
            RecordFloatView.updateMessage("暂未获取到验证码! 当前重新获取的次数 count：" + count);
            count++;
            if (count < 15) {  //重新获取验证码
                BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_GET_MESSAGE, 8000);
            } else {  //订单处理失败
                BrushOrderHelper.getInstance().orderDealFailure("重新获取验证码超过15次!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            }
        } else {  //对获取到验证码信息做处理
            if (!TextUtils.isEmpty(first_front_str) && !TextUtils.isEmpty(first_behind_str) && (message.contains(first_front_str) || message.contains(first_behind_str))) {
                if (first_front_str.equals("NOTHING") && !first_behind_str.equals("NOTHING")) {
                    verifyCode = message.split(first_behind_str)[0];
                } else if (!first_front_str.equals("NOTHING") && first_behind_str.equals("NOTHING")) {
                    verifyCode = message.split(first_front_str)[1];
                } else if (first_front_str.equals("NOTHING") && first_behind_str.equals("NOTHING")) {
                    verifyCode = message.trim();
                } else {
                    LLog.e("message:" + message);
                    LLog.e("前字符串:" + first_front_str + "    后字符串:" + first_behind_str);
                    String firstSplit = message.split(first_front_str)[1];
                    LLog.e("第一条 第一次拆分后结果:" + firstSplit);
                    String secondSplit = firstSplit.split(first_behind_str)[0];
                    LLog.e("第一条 第二次拆分后结果:" + secondSplit);
                    verifyCode = secondSplit;
                }
                LLog.e("First ---> verifyCode:" + verifyCode);
                setVerifyCode(verifyCode);
                return;
            }

            if (!TextUtils.isEmpty(second_front_str) && !TextUtils.isEmpty(second_behind_str) && (message.contains(second_front_str) || message.contains(second_behind_str))) {
                if (second_front_str.equals("NOTHING") && !second_behind_str.equals("NOTHING")) {
                    verifyCode = message.split(second_behind_str)[0];
                } else if (!second_front_str.equals("NOTHING") && second_behind_str.equals("NOTHING")) {
                    verifyCode = message.split(second_front_str)[1];
                } else if (second_front_str.equals("NOTHING") && second_behind_str.equals("NOTHING")) {
                    verifyCode = message;
                } else {
                    LLog.e("message:" + message);
                    LLog.e("前字符串:" + second_front_str + "    后字符串:" + second_behind_str);
                    String firstSplit = message.split(second_front_str)[1];
                    LLog.e("第二条 第一次拆分后结果:" + firstSplit);
                    String secondSplit = firstSplit.split(second_behind_str)[0];
                    LLog.e("第二条 第二次拆分后结果:" + secondSplit);
                    verifyCode = secondSplit;
                }
                LLog.i("Second ---> verifyCode:" + verifyCode);
                setVerifyCode(verifyCode);
                return;
            }

            if (!TextUtils.isEmpty(third_front_str) && !TextUtils.isEmpty(third_behind_str) && (message.contains(third_front_str) || message.contains(third_front_str))) {
                if (third_front_str.equals("NOTHING") && !third_behind_str.equals("NOTHING")) {
                    verifyCode = message.split(third_behind_str)[0];
                } else if (!third_front_str.equals("NOTHING") && third_behind_str.equals("NOTHING")) {
                    verifyCode = message.split(third_front_str)[1];
                } else if (third_front_str.equals("NOTHING") && third_behind_str.equals("NOTHING")) {
                    verifyCode = message;
                } else {
                    LLog.e("message:" + message);
                    LLog.e("前字符串:" + third_front_str + "    后字符串:" + third_behind_str);
                    String firstSplit = message.split(second_front_str)[1];
                    LLog.e("第三条 第一次拆分后结果:" + firstSplit);
                    String secondSplit = firstSplit.split(third_behind_str)[0];
                    LLog.e("第三条 第二次拆分后结果:" + secondSplit);
                    verifyCode = secondSplit;

                }
                LLog.i("Third ---> verifyCode:" + verifyCode);
                setVerifyCode(verifyCode);
                return;
            }

            if (ScriptConstants.GET_PHONE_CODE_PASSWORD_SCRIPT.equals(passwordPhoneCode)) {
                SpUtil.putKeyString(PlatformConfig.CURRENT_PASSWORD, verifyCode);  //存为密码
                LLog.i("当前密码:" + verifyCode);
            }

        }
    }


    public void setVerifyCode(String code) {
        if (code != null) {
            LLog.i("当前验证码:" + code);
            RecordFloatView.updateMessage("获取验证码成功，验证码为:" + code);
            SocketUtil.continueExec();//继续播放脚本
        } else {
            RecordFloatView.updateMessage("获取验证码为null: 订单处理失败");
            BrushOrderHelper.getInstance().orderDealFailure("获取验证码为null!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
        }
    }

    /**
     * 处理服务器返回的身份证信息
     *
     * @param realName
     * @param idCardInfo
     */
    public void dealWithIdCardRe(String realName, String idCardInfo, String gender) {
        SpUtil.putKeyString(PlatformConfig.CURRENT_NAME, realName);
        SpUtil.putKeyString(PlatformConfig.CURRENT_ID_CARD, idCardInfo);
        SpUtil.putKeyString(PlatformConfig.CURRENT_GENDER, gender);
        SocketUtil.continueExec();//继续播放脚本
    }

    /**
     * 处理服务器返回的图片标识
     *
     * @param verifyNumber
     */
    public void dealWithUploadVerifyCodeRe(String verifyNumber) {
        this.verifyNumber = verifyNumber;
    }

    /**
     * 处理服务器返回的图片验证码
     *
     * @param verifyCode
     */
    public void dealWithGetVerifyCodeRe(String verifyCode) {
        LLog.e("校验码 verifyCode:     " + verifyCode);
        LLog.e("校验码 类型:     " + Constants.VERIFY_TYPE);
        if (!TextUtils.isEmpty(verifyCode)) {
            if (Constants.VERIFY_TYPE == 0) {  //图片验证码
                SocketUtil.sendInstruct(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT + verifyCode, false);  //输入图片验证码到验证码输入框
                SocketUtil.continueExec();//继续播放脚本
            } else if (Constants.VERIFY_TYPE == 1) { //滑动验证码
                try {
                    String[] split = (verifyCode.trim().replaceAll(" ", "")).split(ScriptConstants.CMD_SPLIT);
                    if (split != null && split.length >= 4) {
                        int x_start = Integer.parseInt(split[0]) * 2;
                        int y_start = Integer.parseInt(split[1]) * 2;
                        int x_end = Integer.parseInt(split[2]) * 2;
                        int y_end = Integer.parseInt(split[3]) * 2;
                        String instrctstr = ScriptConstants.LONG_CLICK_CMD
                                + ScriptConstants.SPLIT + x_start
                                + ScriptConstants.SPLIT + y_start
                                + ScriptConstants.SPLIT + "2000"
                                + ScriptConstants.SPLIT + ScriptConstants.SWIPE_CMD
                                + ScriptConstants.SPLIT + x_start
                                + ScriptConstants.SPLIT + y_start
                                + ScriptConstants.SPLIT + x_end
                                + ScriptConstants.SPLIT + y_end
                                + ScriptConstants.SPLIT + "2000";
                        LLog.e("instrctstr:     " + instrctstr);
                        SocketUtil.sendInstruct(instrctstr, false);  //输入图片验证码到验证码输入框
                        SocketUtil.continueExec();//继续播放脚本
                    } else {
                        RecordFloatView.updateMessage("订单处理失败!");
                        BrushOrderHelper.getInstance().orderDealFailure("处理服务器返回的图片验证码：滑动验证码!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                    }
                } catch (Exception e) {
                    LLog.e("e:     " + e);
                    RecordFloatView.updateMessage("订单处理失败!");
                    BrushOrderHelper.getInstance().orderDealFailure("处理服务器返回的图片异常：滑动验证码!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                }
            } else if (Constants.VERIFY_TYPE == 2) {  //手势验证码
                String instrctstr = verifyCode.trim().replaceAll(" ", "").replaceAll("长按", "longclick").replaceAll("滑动", "swipe");
                String[] split = (instrctstr.trim()).split(ScriptConstants.SPLIT);
                LLog.e("手势校验码 split.length:     " + split.length);
                if (split != null && split.length >= 10) {
                    LLog.e("手势校验码 verify:     " + instrctstr);
                    SocketUtil.sendInstruct(instrctstr, false);  //输入图片验证码到验证码输入框
                    SocketUtil.continueExec();//继续播放脚本
                } else {
                    RecordFloatView.updateMessage("订单处理失败!");
                    BrushOrderHelper.getInstance().orderDealFailure("处理服务器返回的图片验证码：手势验证码!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                }
            } else if (Constants.VERIFY_TYPE == 3) { //图片位置点击验证码
                String[] split = verifyCode.trim().replaceAll(" ", "").split(ScriptConstants.SPLIT);
                if (split != null && split.length > 0) {
                    for (int i = 0; i < split.length; i++) {
                        String[] point = split[i].split(ScriptConstants.CMD_SPLIT);
                        if (point != null && point.length > 1) {
                            SocketUtil.sendInstruct(ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + point[0] + ScriptConstants.SPLIT + point[1], false);
                            SystemClock.sleep(2000);
                        } else {
                            RecordFloatView.updateMessage("订单处理失败!");
                            BrushOrderHelper.getInstance().orderDealFailure("处理服务器返回的图片验证码：图片位置点击验证码0!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                        }
                    }
                    SocketUtil.continueExec();//继续播放脚本
                } else {
                    RecordFloatView.updateMessage("订单处理失败!");
                    BrushOrderHelper.getInstance().orderDealFailure("处理服务器返回的图片验证码：图片位置点击验证码1!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                }
            } else {
                RecordFloatView.updateMessage("订单处理失败!");
                BrushOrderHelper.getInstance().orderDealFailure("处理服务器返回的图片验证码：其他!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
            }
        }
    }

    /**
     * 添加号码到黑名单
     * <p>
     * http://api.tianma168.com/tm/addBlack?token=登陆token&phoneList=itemId-phone
     * 不同项目同时加黑号码列表格式: itemId-phone,phone,phone;itemId-phone,phone; 其中phone为号码，itemId为项目ID, 同时加黑多个号码时请注意后面的分号需要加上
     */
    public void addBlack() {
        Map params = new HashMap();
        params.put("token", token);
        params.put("phoneList", ItemId + "-" + phone);

        GetHttpRequest request = new GetHttpRequest();
        request.getString(ADD_BLACK_SERVER, params, new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }
        });
    }


    /**
     * 发送短信-------没用
     * <p>
     * 正确返回：Ok,注意：返回OK只是代表提交发送命令成功，至于短信是否发送成功需要通过调用 “获取短信”的接口来检查发送状态
     * http://api.tianma168.com/tm/sendMessage?token=登陆token&Phone=手机号&ItemId=项目ID&Msg=短信内容
     */
    public void sendMessage() {
        Map params = new HashMap();
        params.put("ItemId", ItemId);
        params.put("Phone", phone);
        params.put("Msg", "1111111");

        final GetHttpRequest request = new GetHttpRequest();
        request.getString(SEND_MESSAGE_SERVER, params, new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }
        });
    }


}
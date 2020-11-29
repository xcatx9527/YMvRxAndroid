package com.xile.script.utils.script;

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
import com.xile.script.http.helper.brush.mina.BOrderStateConfig;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.utils.common.SpUtil;

import static org.wuji.wujivpninterface.*;

/**
 * date: 2017/11/1 19:40
 *
 * @descript 无极VPN 只有 改变 与关闭 没有登陆
 */
public class WujiVpnUtil {
    public static final int VPN_STATE_2 = 2;//改变 vpn
    public static int VPN_STATE = VPN_STATE_2;

    public static WujiHandle wujiVpnHandler = null;

    static class WujiHandle extends Handler {

        public WujiHandle(Looper looper) {
            super(looper);
            String result = init(ScriptApplication.getInstance());
            if (!TextUtils.isEmpty(result)) {
                showInitResult(result);
            }
            LLog.e("init code ==" + result);
            LLog.e("init code ==" + result);
            try {
                String closeResult = closeip(ScriptApplication.getInstance());
                LLog.e("关闭无极 code ==" + closeResult);
            } catch (Exception e) {
                LLog.e("关闭无极 code ==" + e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (Constants.PLAY_STATE == PlayEnum.START_PLAY) {
                switch (msg.what) {
                    case 1:
                        if (VPN_STATE == VPN_STATE_2) {//进行登录获取ip信息操作
                            //进行VPN切换  与登陆一个流程
                            String userName = null;
                            String passWord = null;

                            final int zyzx = Constants.sBrushOrderInfo.getZyzx();//专业或尊享   0代表专业 1代表尊享
                            final int duli = Constants.sBrushOrderInfo.getDuli();//是否使用独享 0代表不使用 1代表使用 如果选择1则xyzx不起作用

                            if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
                                if (Constants.sBrushOrderInfo != null) {
                                    if (System.currentTimeMillis() - Constants.getOrderTime > 3 * 60 * 1000) {  //VPN切三分钟超时
                                        wujiVpnHandler.removeMessages(1);
                                        SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_VPN_FAIL);
                                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                                        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                                        return;
                                    }
                                    userName = Constants.sBrushOrderInfo.getVpnAccount();
                                    passWord = Constants.sBrushOrderInfo.getVpnPasswd();
                                } else {
                                    LLog.e("内存中订单为空,无法使用VPN");
                                    RecordFloatView.updateMessage("内存中订单为空,无法使用VPN！");
                                }
                            } else {
                                userName = SpUtil.getKeyString(PlatformConfig.VPN_USERNAME, "");
                                passWord = SpUtil.getKeyString(PlatformConfig.VPN_PASSWORD, "");
                            }
                            LLog.e("当前无极VPN 用户名:" + userName);
                            LLog.e("当前无极VPN 密码:" + passWord);
                            LLog.e("当前无极VPN zyzx:" + zyzx);
                            LLog.e("当前无极VPN duli:" + duli);
                            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                                BrushOrderHelper.getInstance().orderDealFailure("当前无极VPN 异常!", "执行时间 ：" + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s");
                                return;
                            }
                            final String finalUserName = userName;
                            final String finalPassWord = passWord;
                            ScriptApplication.getService().execute(() -> {
                                LLog.e("当前无极VPN 用户名:" + finalUserName);
                                LLog.e("当前无极VPN 密码:" + finalPassWord);
                                String changeResult = changeip(ScriptApplication.getInstance(), finalUserName, finalPassWord, zyzx, duli);
                                showChangeResult(changeResult);
                            });

                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }


    /**
     * 设置VPN
     */
    public static void dealWithVPN() {
        BrushOrderHelper.getInstance().vpnConnected = false;
        if (wujiVpnHandler == null) {
            wujiVpnHandler = new WujiHandle(Looper.getMainLooper());
        }
        VPN_STATE = VPN_STATE_2;
        //进行关闭 无极vpn 操作
        wujiVpnHandler.sendEmptyMessageDelayed(1, 1000);
    }

    /**
     * 关闭VPN
     */
    public static void closeVPN() {
        try {
            String closeResult = closeip(ScriptApplication.getInstance());
            showCloseResult(closeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * VPN 初始化回执
     *
     * @param code
     */
    public static void showInitResult(String code) {
        try {
            code = code.split(":")[0];
            switch (code) {
                case "0":
                    LLog.i("无极VPN初始化成功!");
                    RecordFloatView.updateMessage("无极VPN初始化成功!");
                    break;
                case "1":
                    LLog.e("没有root权限!");
                    RecordFloatView.updateMessage("没有root权限!");
                    break;
                case "2":
                    LLog.e("赋予安装文件最高权限失败!");
                    RecordFloatView.updateMessage("赋予安装文件最高权限失败!");
                    break;
                case "3":
                    LLog.e("内核正在运行，不能重复初始化!");
                    RecordFloatView.updateMessage("内核正在运行，不能重复初始化!");
                    break;
                case "5":
                    LLog.e("执行清除命令失败!");
                    RecordFloatView.updateMessage("执行清除命令失败!");
                    break;
                case "6":
                    LLog.e("获取系统组件失败!");
                    RecordFloatView.updateMessage("获取系统组件失败!");
                    break;
                case "7":
                    LLog.e("获取系统路径失败!");
                    RecordFloatView.updateMessage("获取系统路径失败!");
                    break;
                case "8":
                    LLog.e("获取包名失败!");
                    RecordFloatView.updateMessage("获取包名失败!");
                    break;
                case "9":
                    LLog.e("获取系统参数失败!");
                    RecordFloatView.updateMessage("获取系统参数失败!");
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * VPN 切换回执
     */
    public static void showChangeResult(String code) {
        try {
            LLog.e("切换 code ===== " + code);
            code = code.split(":")[0];
            switch (code) {
                case "0":
                    LLog.i("无极VPN 切换成功!");
                    RecordFloatView.updateMessage("无极VPN 切换成功!");
                    SpUtil.putKeyString(PlatformConfig.CURRENT_IP_INFO, "");//存当前IP所在的城市
                    BrushOrderHelper.getInstance().vpnConnected = true;
                    SocketUtil.continueExec();//只有VPN连接成功才继续播放脚本
                    break;
                case "1":
                    LLog.e("无极VPN切换 用户名密码 数据 找不到!");
                    RecordFloatView.updateMessage("无极VPN切换 用户名密码 数据 找不到!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "2":
                    LLog.e("无极VPN切换 正处理上一次的切换 IP 操作!");
                    RecordFloatView.updateMessage("无极VPN切换 正处理上一次的切换 IP 操作!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "3":
                    LLog.e("无极VPN切换 代表两次操作间隔不能小于 10 秒!");
                    RecordFloatView.updateMessage("无极VPN切换 代表两次操作间隔不能小于 10 秒!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "4":
                    LLog.e("无极VPN切换 获取系统数据失败!");
                    RecordFloatView.updateMessage("无极VPN切换 获取系统数据失败!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "5":
                    LLog.e("无极VPN切换 返回 ip 已经在运行当中， 在不关闭 IP 的情况下， 不能再申请!");
                    RecordFloatView.updateMessage("无极VPN切换 返回 ip 已经在运行当中， 在不关闭 IP 的情况下， 不能再申请!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "6":
                    LLog.e("无极VPN切换 操作失败!");
                    RecordFloatView.updateMessage("无极VPN切换 操作失败!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "7":
                    LLog.e("无极VPN切换 丢失文件!");
                    RecordFloatView.updateMessage("无极VPN切换 丢失文件!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "8":
                    LLog.e("无极VPN切换 启动IP失败!");
                    RecordFloatView.updateMessage("无极VPN切换 启动IP失败!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "9":
                    LLog.e("无极VPN切换 服务器没数据返回!");
                    RecordFloatView.updateMessage("无极VPN切换 服务器没数据返回!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
                case "10":
                    LLog.e("无极VPN切换 服务器返回的错误消息!");
                    RecordFloatView.updateMessage("无极VPN切换 服务器返回的错误消息!");
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;

                default:
                    wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            wujiVpnHandler.sendEmptyMessageDelayed(1, 15000);
        }
    }

    /**
     * VPN 关闭回执
     */
    public static void showCloseResult(String code) {
        try {
            code = code.split(":")[0];
            switch (code) {
                case "0":
                    LLog.i("无极VPN 关闭成功!");
                    RecordFloatView.updateMessage("无极VPN 关闭成功!");
                    break;
                case "1":
                    LLog.e("无极VPN 获取包名失败!");
                    RecordFloatView.updateMessage("无极VPN 获取包名失败!");
                    break;
                case "2":
                    LLog.e("无极VPN 获取执行命令失败!");
                    RecordFloatView.updateMessage("无极VPN 获取执行命令失败!");
                    break;
                case "3":
                    LLog.e("无极VPN 获取系统值失败!");
                    RecordFloatView.updateMessage("无极VPN 获取系统值失败!");
                    break;

                default:
                    LLog.e("无极VPN 关闭失败!");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

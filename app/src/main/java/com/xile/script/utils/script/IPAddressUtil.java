package com.xile.script.utils.script;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.config.SleepConfig;
import com.xile.script.http.helper.brush.mina.BOrderStateConfig;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.common.SpUtil;

/**
 * @descript IP校验
 */

public class IPAddressUtil {
    private static IPAddressUtil instance;
    public MyHandle mHandler = null;
    private int count = 0;

    public static IPAddressUtil getInstance() {
        if (instance == null) {
            instance = new IPAddressUtil();
        }
        return instance;
    }

    public void sendMsg(int type, long delayTime) {
        if (mHandler == null) {
            mHandler = new MyHandle(Looper.getMainLooper());
        }
        Message msg = mHandler.obtainMessage();
        msg.what = type;
        mHandler.sendMessageDelayed(msg, delayTime);
    }

    public void checkIp() {
        count = 0;
        sendMsg(1, 1000);
    }

    class MyHandle extends Handler {

        public MyHandle(Looper looper) {
            super(looper);
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (Constants.PLAY_STATE == PlayEnum.START_PLAY) {
                switch (msg.what) {
                    case 1:
                        if (count > 30) {
                            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_VPN_FAIL);
                            SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "IP校验大于30次");
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                            Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                            return;
                        }
                        ScriptApplication.getService().execute(new Runnable() {
                            @Override
                            public void run() {
                                CMDUtil.CommandResult commandResult = CMDUtil.execCommand(new String[]{"curl ip.cn"}, true, true);
                                LLog.i("当前IP校验: " + commandResult == null ? "" : commandResult.toString());
                                if (commandResult != null && !TextUtils.isEmpty(commandResult.successMsg) && commandResult.successMsg.contains("来自") && !commandResult.successMsg.contains("北京")) {
                                    SocketUtil.continueExec();
                                } else {
                                    count++;
                                    sendMsg(1, SleepConfig.SLEEP_TIME_2000);
                                }
                            }
                        });
                    default:

                        break;
                }
            }
        }
    }


}

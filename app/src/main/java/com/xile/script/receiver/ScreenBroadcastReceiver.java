package com.xile.script.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.chenyang.lloglib.LLog;
import com.xile.script.config.PlatformConfig;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.common.SpUtil;

/**
 * 检测屏幕状态
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String strAction = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(strAction)) {
            if (SpUtil.getKeyBoolean(PlatformConfig.CURRENT_SCREEN_ON, true)) {
                keepScreenOn();
            }
        } else if (Intent.ACTION_SCREEN_ON.equals(strAction)) {
        } else if (Intent.ACTION_USER_PRESENT.equals(strAction)) {
        } else {
            //nothing
        }
    }

    public static synchronized void keepScreenOn() {
        try {
            //屏幕锁屏
            LLog.e("屏幕状态", "ACTION_SCREEN_OFF触发");
            //屏幕锁屏
            SystemClock.sleep(2000);
            CMDUtil.execShell("input keyevent 26");
            SystemClock.sleep(1500);
            CMDUtil.execShell("input swipe 540 1000 540 400 300");
            SystemClock.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

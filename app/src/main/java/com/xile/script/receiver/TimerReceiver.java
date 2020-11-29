package com.xile.script.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.script.ScriptUtil;

/**
 * date: 2018/2/23 14:41
 *
 * @scene 检测重启机器的定时广播
 */
public class TimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            ScriptApplication.getService().execute(() -> {
                LLog.i("接收到检测重启机器的定时广播...");
                SpUtil.putKeyLong(PlatformConfig.LAST_RECEIVE_BROADCAST_TIME, System.currentTimeMillis());
                long lastRebootTime = SpUtil.getKeyLong(PlatformConfig.LAST_REBOOT_TIME, 0);
                boolean needReboot = false;
                long sdFreeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
                if (sdFreeSpace / 1024 / 1024 / 1024 <= 1) {
                    needReboot = true;
                }

                if ((System.currentTimeMillis() - lastRebootTime > 60 * 60 * 1000 && (System.currentTimeMillis() - Constants.requestOrderTime > 30 * 60 * 1000 || System.currentTimeMillis() - Constants.orderSuccessTime > 2 * 60 * 60 * 1000)) || needReboot) {
                    SpUtil.putKeyLong(PlatformConfig.LAST_REBOOT_TIME, System.currentTimeMillis());
                    LLog.i("符合机器重启条件，即将重启...");
                    if (!SpUtil.getKeyBoolean(PlatformConfig.CURRENT_BOOLEAN_RECHARGE, false)) {
                        FileHelper.deletCacheFile();
                        FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_FOLDER_PATH);
                        FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_MATCH_PATH);
                        FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_FOLDER_ROOT_POP_IMG_PATH);
                        ScriptUtil.dealWithUninstallOtherApp();
                        SystemClock.sleep(3000);
                        CMDUtil.execShell("reboot");
                    }
                }
            });
        }
    }
}

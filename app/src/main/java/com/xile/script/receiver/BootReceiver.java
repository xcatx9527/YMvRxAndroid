package com.xile.script.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;
import com.chenyang.lloglib.LLog;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.service.CheckRebootService;
import com.xile.script.utils.CMDUtil;
import script.tools.config.ScriptConstants;

/**
 * date: 2018/2/23 14:41
 *
 * @scene 开机重启广播
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            try {
                LLog.e("开机启动了");
                Toast.makeText(context, "开机啦", Toast.LENGTH_LONG).show();
                LLog.e("自动解锁");
                CMDUtil.execShell("input keyevent 26");
                SystemClock.sleep(2000);
                CMDUtil.execShell("input keyevent 26");
                SystemClock.sleep(2000);
                CMDUtil.execShell("input swipe 540 1000 540 400 300");
                SystemClock.sleep(1000);
                context.startService(new Intent(context, CheckRebootService.class));
                Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.xile.script");
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appIntent.putExtra("fromBoot", true);
                appIntent.putExtra("bootType", ScriptConstants.PLATFORM_BRUSH);
                //appIntent.putExtra("bootType", ScriptConstants.PLATFORM_MANAGER);
                context.startActivity(appIntent);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_PHONE_REBOOT, 1000);
                    }
                }, 15000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

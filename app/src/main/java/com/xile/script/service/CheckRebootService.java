package com.xile.script.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.receiver.ScreenBroadcastReceiver;
import com.xile.script.receiver.TimerReceiver;
import com.xile.script.utils.appupdate.DownloadService;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.MD5Util;
import com.xile.script.utils.common.SerializableUtil;
import com.xile.script.utils.common.SpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * date: 2018/2/23 14:41
 *
 * @scene 检测重启机器的服务
 */
public class CheckRebootService extends Service {
    public static final String ACTION_SCREEN_CHECK = "action_screen_check";

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("CheckRebootService.onCreate");
        initAlarm(AlarmReceiver.class, ACTION_SCREEN_CHECK);
        initMd5Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("CheckRebootService.onStartCommand");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        this.registerReceiver(new TimerReceiver(), filter);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("CheckRebootService.onDestroy");
    }

    /**
     * 初始化定时器
     *
     * @param action
     */
    public static void initAlarm(Class<? extends BroadcastReceiver> receiverClass, String action) {
        AlarmManager alarmMgr = (AlarmManager) ScriptApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ScriptApplication.getInstance(), receiverClass);
        intent.setAction(action);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(ScriptApplication.getInstance(), 0, intent, 0);
        alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10 * 1000, alarmIntent);
    }

    /**
     * 本地资源文件MD5定时更新
     */
    private void initMd5Timer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<File> localFileList = new ArrayList<>();
                    localFileList.addAll(FileHelper.getFiles(Constants.SCRIPT_FOLDER_GAME_APK_PATH));
                    localFileList.addAll(FileHelper.getFiles(Constants.SCRIPT_FOLDER_GAME_RESOURCE_PATH));
                    HashMap<String, String> md5Map = new HashMap<>();
                    for (int i = 0; i < localFileList.size(); i++) {
                        if (!localFileList.get(i).getName().endsWith("apk") && !localFileList.get(i).getName().endsWith("zip")) {
                            continue;
                        }
                        String md5Value = MD5Util.getFileMD5(localFileList.get(i));
                        md5Map.put(localFileList.get(i).getName(), md5Value);
                    }
                    SerializableUtil.toSerialize(Constants.SCRIPT_FOLDER_GAME_APK_MD5_PATH, md5Map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 60000, 60000);
    }


    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (ACTION_SCREEN_CHECK.equals(intent.getAction())) {
                    PowerManager powerManager = (PowerManager) ScriptApplication.getInstance().getSystemService(Context.POWER_SERVICE);
                    boolean ifOpen = powerManager.isScreenOn();
                    if (!ifOpen && SpUtil.getKeyBoolean(PlatformConfig.CURRENT_SCREEN_ON, true)) {
                        //屏幕解锁
                        ScreenBroadcastReceiver.keepScreenOn();
                    }
                    long last_receive_broadcast_time = SpUtil.getKeyLong(PlatformConfig.LAST_RECEIVE_BROADCAST_TIME, 0);
                    //LLog.i("last_receive_broadcast_time:" + TimeUtil.formatTimeStamp(last_receive_broadcast_time));
                    if (System.currentTimeMillis() - last_receive_broadcast_time > 30 * 60 * 1000) {
                        SpUtil.putKeyLong(PlatformConfig.LAST_RECEIVE_BROADCAST_TIME, System.currentTimeMillis());
                        LLog.i("超过30分钟未收到定时广播，即将重启...");
                        if (!SpUtil.getKeyBoolean(PlatformConfig.CURRENT_BOOLEAN_RECHARGE, false)) {
                            DownloadService.dealWithXpose();
                        }
                    }
                    initAlarm(AlarmReceiver.class, ACTION_SCREEN_CHECK);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

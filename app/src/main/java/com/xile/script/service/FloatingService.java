package com.xile.script.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.BaseFloatView;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.imagereact.ScreenConfigBcReceiver;
import com.xile.script.receiver.ScreenBroadcastReceiver;
import com.yzy.example.R;
import com.yzy.example.component.MainActivity;

/**
 * date: 2017/3/13 16:06
 */


public class FloatingService extends Service {
    private RecordFloatView recordFloatView;
    private ScreenConfigBcReceiver screenConfigBcReceiver;
    private ScreenBroadcastReceiver screenBroadcastReceiver = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        screenConfigBcReceiver = new ScreenConfigBcReceiver();
        //注册广播接收,注意：要监听这个系统广播就必须用这种方式来注册，不能再xml中注册，否则不能生效
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        registerReceiver(screenConfigBcReceiver, filter);
        //registerScreenBroadcastReceiver();
        LLog.d("FloatingService is created !");
        String CHANNEL_ID = "com.example.recyclerviewtest.N1";
        String CHANNEL_NAME = "SCRIPT";
        NotificationChannel notificationChannel = null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Notification notification = null;
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification.Builder builder = new Notification.Builder(this);
        //String contentText = "<font color=\"#00BFFF\"><big><strong>☛ I'm running</strong></big></font>";
        String contentText = "嘿...我告诉你一个走路不被绷带绊倒的办法 ➟";
        builder.setContentTitle(getResources().getString(R.string.app_name)).setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(Html.fromHtml(contentText))
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setVibrate(new long[]{300, 400, 500}).setContentIntent(pi);
        if (Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        }
        startForeground(0x111, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LLog.d("FloatingService is started !");
        recordFloatView = RecordFloatView.getInstance(getApplicationContext());
        recordFloatView.setDuration(BaseFloatView.LENGTH_ALWAYS);
        recordFloatView.initState();
        recordFloatView.show();
        //VolumeWatcher.getInstance().registerReceive(ScriptApplication.getInstance());
        return START_NOT_STICKY;
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
        LLog.d("FloatingService is destroyed !");
        if (recordFloatView != null && recordFloatView.isShow()) {
            recordFloatView.hide();
            recordFloatView.reset();
            recordFloatView = null;
        }

        //注销广播
        if (screenConfigBcReceiver != null) {
            try {
                unregisterReceiver(screenConfigBcReceiver);
                //unregisterReceiver(screenBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册检测屏幕状态的广播
     */
    private void registerScreenBroadcastReceiver() {
        screenBroadcastReceiver = new ScreenBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);//当屏幕锁屏的时候触发
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);//当屏幕解锁的时候触发
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);//当用户重新唤醒手持设备时触发
        this.registerReceiver(screenBroadcastReceiver, intentFilter);
    }
}

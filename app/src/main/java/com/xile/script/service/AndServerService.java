package com.xile.script.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.handler.AndOrderResultHandler;
import com.xile.script.handler.AndUpdateModuleHandler;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.AssetsWebsite;

/**
 * date: 2017/12/8 15:20
 */
public class AndServerService extends Service {
    private Server mServer;
    private AssetManager mAssetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mAssetManager = getAssets();
        AndServer andServer = new AndServer.Build()
                .port(7070)
                .timeout(15 * 1000)
                .registerHandler("updateModule", new AndUpdateModuleHandler())
                .registerHandler("orderResult", new AndOrderResultHandler())
                .website(new AssetsWebsite(mAssetManager, ""))
                .build();
        mServer = andServer.createServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mServer != null) {
            if (!mServer.isRunning()) {
                mServer.start();
                LLog.i("AndServer 开始运行!");
                RecordFloatView.updateMessage("AndServer 开始运行!");
            } else {
                LLog.i("AndServer 已经在运行!");
                RecordFloatView.updateMessage("AndServer 已经在运行!");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("AndServerService.onDestroy");
        if (mServer != null) {
            if (mServer.isRunning()) {
                mServer.stop();
            }
        }
    }
}

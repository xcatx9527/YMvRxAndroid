package com.xile.script.imagereact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

/**
 * Created by chsh on 2017/5/15.
 * 监听手机屏幕方向改变的广播
 */

public class ScreenConfigBcReceiver extends BroadcastReceiver {
    public static boolean screenHasChanged = false;//屏幕方向是否改变

    @Override
    public void onReceive(Context context, Intent intent) {
        screenHasChanged = true;
    }

    public static int getScreenOrientation(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        return ori;
    }
}

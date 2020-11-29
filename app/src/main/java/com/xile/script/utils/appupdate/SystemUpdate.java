package com.xile.script.utils.appupdate;

import android.content.Context;
import android.content.Intent;

/**
 * Created by chsh on 2018/1/2.
 */

public class SystemUpdate {

    public SystemUpdate() {

    }

    /**
     * 采用notification
     *
     * @param mContext
     * @param content
     * @param apkUrl
     * @param showTitle
     */
    public static void showNotification(Context mContext, String content, String apkUrl, boolean showTitle, String type, boolean autoUpdate) {
        Intent myIntent = new Intent(mContext, DownloadService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
        myIntent.putExtra(Constants.APK_DOWNLOAD_TYPE, type);
        myIntent.putExtra(Constants.APK_DOWNLOAD_AUTO, autoUpdate);
        if (showTitle) {
            myIntent.putExtra(Constants.APK_DOWNLOAD_TITLE, content);
        }
        mContext.startService(myIntent);
    }
}

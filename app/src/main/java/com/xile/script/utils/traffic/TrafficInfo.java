package com.xile.script.utils.traffic;


import android.content.Context;
import android.net.TrafficStats;

import java.math.BigDecimal;
import java.util.Timer;

/**
 * 应用的流量信息
 */
public class TrafficInfo {
    private static final int UNSUPPORTED = -1;
    private static final String LOG_TAG = "test";
    private static TrafficInfo instance;
    private int uid=0;
    private long preRxBytes = 0;
    private Timer mTimer = null;

    /** 更新频率（每几秒更新一次,至少1秒） */
    private final int UPDATE_FREQUENCY = 1;
    private int times = 1;
    private Context mContext;

    public TrafficInfo(Context mContext, int uid) {
        this.uid = uid;
        this.mContext=mContext;
    }

    public TrafficInfo(Context mContext) {
        this.mContext=mContext;
    }


    /**
     * 获取当前下载流量总和
     *
     * @return
     */
    public static long getNetworkRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    /**
     * 获取当前上传流量总和
     *
     * @return
     */
    public static long getNetworkTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    /**
     * 获取当前网速
     *
     * @return
     */
    public double getNetSpeed() {
        long curRxBytes = getNetworkRxBytes();
        if (preRxBytes == 0)
            preRxBytes = curRxBytes;
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        //int kb = (int) Math.floor(bytes / 1024 + 0.5);
        double kb = (double)bytes / (double)1024;
        BigDecimal bd = new BigDecimal(kb);

        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }



    public void stopCalculateNetSpeed() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }



}

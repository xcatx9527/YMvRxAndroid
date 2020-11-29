package com.xile.script.utils.traffic;


import com.chenyang.lloglib.LLog;
import com.xile.script.config.Constants;
import com.xile.script.http.common.HttpConstants;
import com.xile.script.http.common.PostHttpRequest;
import com.xile.script.http.common.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

public class StatisticTrafficManager {

    public static String DATE_TO_STRING_DETAIAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private long beginFlowData;
    private long currentFlowData;
    private long produceFlowData;
    private String printSize;
    private Timer timer = new Timer();
    private TimerTask task;

    private ArrayList<String> trafficUint = new ArrayList<>();

    private long startTime;
    private long currentTime;
    private long finishTime;
    private String unittime = "0s";
    private long countTime = 60000;

    private StatisticTrafficManager() {

    }

    public static StatisticTrafficManager getInstance() {
        return SingleF.instance;
    }

    static class SingleF {
        private static StatisticTrafficManager instance = new StatisticTrafficManager();
    }


    public void startTimeData(int time) {
        stoptTimeData();
        LLog.e("开始统计流量    :  ");
        beginFlowData = TrafficInfo.getNetworkRxBytes() + TrafficInfo.getNetworkTxBytes();
        unittime = "" + (time / 1000) + "s";
        trafficUint.clear();
        startTime = currentTime = System.currentTimeMillis();
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    currentFlowData = TrafficInfo.getNetworkRxBytes() + TrafficInfo.getNetworkTxBytes();
                    produceFlowData = currentFlowData - beginFlowData - (time / 1000) * 220;
                    if (produceFlowData < 0) {
                        produceFlowData = 0;
                    }
                    beginFlowData = currentFlowData;
                    printSize = getPrintSize(produceFlowData);
                    //LLog.i("单位时间流量大小    :  " + printSize);
                    trafficUint.add(printSize);
                    long upLoadTime = System.currentTimeMillis() - currentTime;
                    if (upLoadTime >= countTime) {
                        currentTime = System.currentTimeMillis();
                        uploadTrafficDate();
                    }
                }
            };
        }
        timer.schedule(task, time, time);
    }


    public void stoptTimeData() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }


    public void uploadTrafficDate() {
        finishTime = System.currentTimeMillis();
        String startTimeStr = DateUtils.getDateToString(startTime, DATE_TO_STRING_DETAIAL_PATTERN);  //开始时间
        String finishTimeStr = DateUtils.getDateToString(finishTime, DATE_TO_STRING_DETAIAL_PATTERN); //结束时间
        String orderId = "";
        if (Constants.sBrushOrderInfo != null && timer != null) {
            orderId = Constants.sBrushOrderInfo.getOrderId();
        } else {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < trafficUint.size(); i++) {
                jsonArray.put(trafficUint.get(i));
            }
            jsonObject.put("orderId", orderId);
            jsonObject.put("startTime", startTimeStr);
            jsonObject.put("endTime", finishTimeStr);
            jsonObject.put("interval", unittime);
            jsonObject.put("flowData", jsonArray);


            final HashMap parms = new HashMap();
            parms.put("message", jsonObject.toString());

            //LLog.i("流量 message    :  " + jsonObject.toString());
            PostHttpRequest.getInstance().post(HttpConstants.UPLOADTRAFFICDATA, parms, new StringCallback() {
                @Override
                public void onResponse(String response, int id) {
                    super.onResponse(response, id);
                    //script.tools.LLog.e("接收上传流量 message - :" + response.toString());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);

                        int code = jsonObject.optInt("code");
                        if (code == 0) {
                           // LLog.i("上传流量 message    成功:  " + jsonObject.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    super.onError(call, e, id);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
            return size + "KB";
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推


//        else {
//            size = size / 1024;
//        }
//        if (size < 1024) {
//            //因为如果以MB为单位的话，要保留最后1位小数，
//            //因此，把此数乘以100之后再取余
//            size = size * 100;
//            return String.valueOf((size / 100)) + "."
//                    + String.valueOf((size % 100)) + "MB";
//        } else {
//            //否则如果要以GB为单位的，先除于1024再作同样的处理
//            size = size * 100 / 1024;
//            return String.valueOf((size / 100)) + "."
//                    + String.valueOf((size % 100)) + "GB";
//        }
    }


}

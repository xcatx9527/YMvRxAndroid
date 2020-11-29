package script.tools;

import android.text.TextUtils;

import com.chenyang.lloglib.LLog;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.InstructUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import script.tools.config.DeviceConfig;
import script.tools.config.ScriptConstants;

/**
 * date: 2017/2/21 15:04
 *
 * @scene sendevent事件工具类
 */
public class EventUtil {
    private static final int EVENT_ABS = 0;
    private static final int EVENT_KEY = 1;

    static {
        try {
            System.loadLibrary("event");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化设备驱动节点权限
     */
    public static void initEventPermission() {
        CMDUtil.execShell("setenforce Permissive");
        CMDUtil.execShell("mount -o rw,remount -t rootfs /system");
        CMDUtil.execShell("chmod 777 /system");
        CMDUtil.execShell("chmod 777 /system/bin");
        CMDUtil.execShell("chmod 777 /system/bin/input");
        CMDUtil.execShell("chmod 777 /dev");
        CMDUtil.execShell("chmod 777 /dev/input");
        File inputDir = new File("/dev/input");
        if (inputDir.exists() && inputDir.isDirectory()) {
            File[] events = inputDir.listFiles();
            for (int i = 0; i < events.length; i++) {
                CMDUtil.execShell("chmod 777 /dev/input/event" + i);
            }
        }
    }


    /**
     * 初始化event设备驱动键值
     *
     * @return string
     */
    public static native void initEvent();


    /**
     * 处理指令事件
     *
     * @param event
     * @param eventType
     */
    private static native void inputEvent(String event, int eventType);


    /**
     * 关闭event设备驱动
     *
     * @return string
     */
    public static native void closeEvent();


    /**
     * 发送事件 sendevent指令发送唯一出口(注意:只接收 ‘点击 滑动 按键 长按’ 事件)
     *
     * @param cmd 指令
     */
    public static synchronized void sendEvent(final String cmd) {
        String instruct = InstructUtil.script2Cmd(cmd);
        List<String> events = script2Event(instruct);
        flushData(events);
    }


    /**
     * 脚本转Event
     *
     * @param script
     * @return
     */
    private static List<String> script2Event(String script) {
        List<String> events = new ArrayList<String>();
        try {
            if (!TextUtils.isEmpty(script)) {
                String[] datas = script.split(ScriptConstants.SPLIT);
                String deviceConfig = FileUtil.readFile(DeviceConfig.DEVICE_PATH);
                if (TextUtils.isEmpty(deviceConfig)) {
                    deviceConfig = DeviceConfig.XIAOMI + ScriptConstants.SPLIT + DeviceConfig.Level_6;
                    FileUtil.saveFile(deviceConfig, DeviceConfig.DEVICE_PATH);
                }
                deviceConfig = deviceConfig.replace("﹟", ScriptConstants.SPLIT);
                if (!deviceConfig.contains(ScriptConstants.SPLIT)) {
                    LLog.e("手机品牌配置有误!");
                    return null;
                }
                String[] phoneType = deviceConfig.split(ScriptConstants.SPLIT);
                if (phoneType.length != 2) {
                    LLog.e("手机品牌配置有误!");
                    return null;
                }
                String deviceBrand = phoneType[0]; //手机品牌
                String systemLevel = phoneType[1]; //系统级别

                switch (deviceBrand) {
                    case DeviceConfig.XIAOMI:
                        if (script.startsWith(ScriptConstants.TAP_CMD) && datas.length >= 3) {//点击
                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_start(events, datas[0]);
                            }
                            if (DeviceConfig.Level_6.equals(systemLevel) || DeviceConfig.Level_7_0.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#3#48#1");
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                            }
                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_end(events, datas[0]);
                            }
                        } else if (script.startsWith(ScriptConstants.SWIPE_CMD) && !script.contains(ScriptConstants.LONG_CLICK_CMD) && datas.length >= 6) {//滑动
                            int spaceNum = Integer.parseInt(datas[5]) / 10;
                            LLog.e("spaceNum:" + spaceNum);
                            float startX = Float.parseFloat(datas[1]);
                            float startY = Float.parseFloat(datas[2]);
                            float endX = Float.parseFloat(datas[3]);
                            float endY = Float.parseFloat(datas[4]);
                            float xSpace = (endX - startX) / spaceNum;
                            float ySpace = (endY - startY) / spaceNum;

                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_start(events, datas[0]);
                            }

                            if (DeviceConfig.Level_6.equals(systemLevel) || DeviceConfig.Level_7_0.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#0#0#0");
                                events.add(datas[0] + "3#48#6");
                                events.add(datas[0] + "#0#0#0");
                                for (int i = 1; i < spaceNum; i++) {
                                    events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                    events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                    events.add(datas[0] + "3#48#" + i + 6);
                                    events.add(datas[0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                }
                                events.add(datas[0] + "#3#53#" + datas[3]);
                                events.add(datas[0] + "#3#54#" + datas[4]);
                                events.add(datas[0] + "3#48#" + (6 + spaceNum));
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#3#48#1");
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                                for (int i = 1; i < spaceNum; i++) {
                                    events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                    events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                    events.add(datas[0] + "3#48#" + i + 6);
                                    events.add(datas[0] + "#3#58#1000");
                                    events.add(datas[0] + "#1#330#1");
                                    events.add(datas[0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                }
                                events.add(datas[0] + "#3#53#" + datas[3]);
                                events.add(datas[0] + "#3#54#" + datas[4]);
                                events.add(datas[0] + "3#48#" + (6 + spaceNum));
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                            }

                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_end(events, datas[0]);
                            }

                        } else if (script.startsWith(ScriptConstants.SWIPE_CMD) && script.contains(ScriptConstants.LONG_CLICK_CMD) && datas.length == 10) { //滑动+长按
                            int spaceNum = Integer.parseInt(datas[5]) / 10;
                            LLog.e("spaceNum:" + spaceNum);
                            float startX = Float.parseFloat(datas[1]);
                            float startY = Float.parseFloat(datas[2]);
                            float endX = Float.parseFloat(datas[3]);
                            float endY = Float.parseFloat(datas[4]);
                            float xSpace = (endX - startX) / spaceNum;
                            float ySpace = (endY - startY) / spaceNum;

                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_start(events, datas[0]);
                            }

                            if (DeviceConfig.Level_6.equals(systemLevel) || DeviceConfig.Level_7_0.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#0#0#0");
                                events.add(datas[0] + "3#48#6");
                                events.add(datas[0] + "#0#0#0");
                                for (int i = 1; i < spaceNum; i++) {
                                    events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                    events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                    events.add(datas[0] + "3#48#" + i + 6);
                                    events.add(datas[0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                }
                                events.add(datas[0] + "#3#53#" + datas[3]);
                                events.add(datas[0] + "#3#54#" + datas[4]);
                                events.add(datas[0] + "3#48#" + (6 + spaceNum));
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + datas[9]);

                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#3#48#1");
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                                for (int i = 1; i < spaceNum; i++) {
                                    events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                    events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                    events.add(datas[0] + "3#48#" + i + 6);
                                    events.add(datas[0] + "#3#58#1000");
                                    events.add(datas[0] + "#1#330#1");
                                    events.add(datas[0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                }
                                events.add(datas[0] + "#3#53#" + datas[3]);
                                events.add(datas[0] + "#3#54#" + datas[4]);
                                events.add(datas[0] + "3#48#" + (6 + spaceNum));
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + datas[9]);
                            }

                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_end(events, datas[0]);
                            }
                        } else if (script.startsWith(ScriptConstants.KEYEVENT_CMD) && datas.length >= 2) {//按键
                            events.add(datas[0] + "#1#" + datas[1] + "#1");
                            events.add(datas[0] + "#0#0#0");
                            events.add(datas[0] + "#1#" + datas[1] + "#0");
                            events.add(datas[0] + "#0#0#0");
                        } else if (script.startsWith(ScriptConstants.LONG_CLICK_CMD) && !script.contains(ScriptConstants.SWIPE_CMD) && datas.length >= 4) {//长按
                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_start(events, datas[0]);
                            }
                            if (DeviceConfig.Level_6.equals(systemLevel) || DeviceConfig.Level_7_0.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + datas[3]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#3#48#1");
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + datas[3]);
                            }
                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_end(events, datas[0]);
                            }
                        } else if (script.startsWith(ScriptConstants.LONG_CLICK_CMD) && script.contains(ScriptConstants.SWIPE_CMD) && datas.length == 10) {//长按+滑动
                            int spaceNum = Integer.parseInt(datas[9]) / 10;
                            LLog.e("spaceNum:" + spaceNum);
                            float startX = Float.parseFloat(datas[1]);
                            float startY = Float.parseFloat(datas[2]);
                            float endX = Float.parseFloat(datas[7]);
                            float endY = Float.parseFloat(datas[8]);
                            float xSpace = (endX - startX) / spaceNum;
                            float ySpace = (endY - startY) / spaceNum;

                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_start(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_start(events, datas[0]);
                            }

                            if (DeviceConfig.Level_6.equals(systemLevel) || DeviceConfig.Level_7_0.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + (datas[3]));

                                events.add(datas[0] + "3#48#6");
                                events.add(datas[0] + "#0#0#0");
                                for (int i = 1; i < spaceNum; i++) {
                                    events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                    events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                    events.add(datas[0] + "3#48#" + i + 6);
                                    events.add(datas[0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                }
                                events.add(datas[0] + "#3#53#" + datas[7]);
                                events.add(datas[0] + "#3#54#" + datas[8]);
                                events.add(datas[0] + "3#48#" + (6 + spaceNum));
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                events.add(datas[0] + "#3#53#" + datas[1]);
                                events.add(datas[0] + "#3#54#" + datas[2]);
                                events.add(datas[0] + "#3#48#1");
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + (datas[3]));

                                events.add(datas[0] + "3#48#6");
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                                for (int i = 1; i < spaceNum; i++) {
                                    events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                    events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                    events.add(datas[0] + "3#48#" + i + 6);
                                    events.add(datas[0] + "#3#58#1000");
                                    events.add(datas[0] + "#1#330#1");
                                    events.add(datas[0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                }
                                events.add(datas[0] + "#3#53#" + datas[7]);
                                events.add(datas[0] + "#3#54#" + datas[8]);
                                events.add(datas[0] + "3#48#" + (6 + spaceNum));
                                events.add(datas[0] + "#3#58#1000");
                                events.add(datas[0] + "#1#330#1");
                                events.add(datas[0] + "#0#0#0");
                            }

                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_end(events, datas[0]);
                            }
                        } else if (script.startsWith(ScriptConstants.LONG_CLICK_CMD) && script.contains(ScriptConstants.SWIPE_CMD) && datas.length > 10) {//多段滑动
                            int index = datas.length;
                            int sum = index / 10;
                            LLog.e("sum ========= " + sum);
                            for (int num = 0; num < sum; num++) {
                                if (DeviceConfig.Level_6.equals(systemLevel)) {
                                    eventAndroid_6_MI_start(events, datas[10 * num + 0]);
                                } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                    eventAndroid_7_MI_start(events, datas[10 * num + 0]);
                                } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                    eventAndroid_7_1_2_MI_start(events, datas[10 * num + 0]);
                                }

                                int spaceNum = Integer.parseInt(datas[10 * num + 9]) / 10;
                                LLog.e("spaceNum:" + spaceNum);
                                float startX = Float.parseFloat(datas[10 * num + 1]);
                                float startY = Float.parseFloat(datas[10 * num + 2]);
                                float endX = Float.parseFloat(datas[10 * num + 7]);
                                float endY = Float.parseFloat(datas[10 * num + 8]);
                                float xSpace = (endX - startX) / spaceNum;
                                float ySpace = (endY - startY) / spaceNum;

                                if (DeviceConfig.Level_6.equals(systemLevel) || DeviceConfig.Level_7_0.equals(systemLevel)) {
                                    events.add(datas[10 * num + 0] + "#3#53#" + datas[10 * num + 1]);
                                    events.add(datas[10 * num + 0] + "#3#54#" + datas[10 * num + 2]);
                                    events.add(datas[10 * num + 0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + (datas[10 * num + 3]));

                                    events.add(datas[10 * num + 0] + "3#48#6");
                                    events.add(datas[10 * num + 0] + "#0#0#0");
                                    for (int i = 1; i < spaceNum; i++) {
                                        events.add(datas[10 * num + 0] + "#3#53#" + (int) (Float.parseFloat(datas[10 * num + 1]) + i * xSpace));
                                        events.add(datas[10 * num + 0] + "#3#54#" + (int) (Float.parseFloat(datas[10 * num + 2]) + i * ySpace));
                                        events.add(datas[10 * num + 0] + "3#48#" + i + 6);
                                        events.add(datas[10 * num + 0] + "#0#0#0");
                                        events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                    }
                                    events.add(datas[10 * num + 0] + "#3#53#" + datas[10 * num + 7]);
                                    events.add(datas[10 * num + 0] + "#3#54#" + datas[10 * num + 8]);
                                    events.add(datas[10 * num + 0] + "3#48#" + (6 + spaceNum));
                                } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                    events.add(datas[10 * num + 0] + "#3#53#" + datas[10 * num + 1]);
                                    events.add(datas[10 * num + 0] + "#3#54#" + datas[10 * num + 2]);
                                    events.add(datas[10 * num + 0] + "#3#58#1000");
                                    events.add(datas[10 * num + 0] + "#1#330#1");
                                    events.add(datas[10 * num + 0] + "#0#0#0");
                                    events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + (datas[10 * num + 3]));

                                    events.add(datas[10 * num + 0] + "3#48#6");
                                    events.add(datas[10 * num + 0] + "#3#58#1000");
                                    events.add(datas[10 * num + 0] + "#1#330#1");
                                    events.add(datas[10 * num + 0] + "#0#0#0");
                                    for (int i = 1; i < spaceNum; i++) {
                                        events.add(datas[10 * num + 0] + "#3#53#" + (int) (Float.parseFloat(datas[10 * num + 1]) + i * xSpace));
                                        events.add(datas[10 * num + 0] + "#3#54#" + (int) (Float.parseFloat(datas[10 * num + 2]) + i * ySpace));
                                        events.add(datas[10 * num + 0] + "3#48#" + i + 6);
                                        events.add(datas[10 * num + 0] + "#3#58#1000");
                                        events.add(datas[10 * num + 0] + "#1#330#1");
                                        events.add(datas[10 * num + 0] + "#0#0#0");
                                        events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                                    }
                                    events.add(datas[10 * num + 0] + "#3#53#" + datas[10 * num + 7]);
                                    events.add(datas[10 * num + 0] + "#3#54#" + datas[10 * num + 8]);
                                    events.add(datas[10 * num + 0] + "3#48#" + (6 + spaceNum));
                                    events.add(datas[10 * num + 0] + "#3#58#1000");
                                    events.add(datas[10 * num + 0] + "#1#330#1");
                                    events.add(datas[10 * num + 0] + "#0#0#0");
                                }

                            }
                            if (DeviceConfig.Level_6.equals(systemLevel)) {
                                eventAndroid_6_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_0.equals(systemLevel)) {
                                eventAndroid_7_MI_end(events, datas[0]);
                            } else if (DeviceConfig.Level_7_1_2.equals(systemLevel)) {
                                eventAndroid_7_1_2_MI_end(events, datas[0]);
                            }

                        }
                        break;

                    case DeviceConfig.HUAWEI:
                        if (script.startsWith(ScriptConstants.TAP_CMD) && datas.length >= 3) {
                            events.add(datas[0] + "#3#57#269");
                            events.add(datas[0] + "#3#53#" + datas[1]);
                            events.add(datas[0] + "#3#54#" + datas[2]);
                            events.add(datas[0] + "#3#58#114");
                            events.add(datas[0] + "#0#0#0");
                            events.add(datas[0] + "#3#57#-1");
                            events.add(datas[0] + "#0#0#0");
                        } else if (script.startsWith(ScriptConstants.SWIPE_CMD) && !script.contains(ScriptConstants.LONG_CLICK_CMD) && datas.length >= 6) {
                            int spaceNum = Integer.parseInt(datas[5]) / 10;
                            float startX = Float.parseFloat(datas[1]);
                            float startY = Float.parseFloat(datas[2]);
                            float endX = Float.parseFloat(datas[3]);
                            float endY = Float.parseFloat(datas[4]);
                            float xSpace = (endX - startX) / spaceNum;
                            float ySpace = (endY - startY) / spaceNum;

                            events.add(datas[0] + "#3#57#269");
                            events.add(datas[0] + "#3#53#" + datas[1]);
                            events.add(datas[0] + "#3#54#" + datas[2]);
                            events.add(datas[0] + "3#58#86");
                            events.add(datas[0] + "#0#0#0");
                            events.add(datas[0] + "#0#0#0");
                            for (int i = 1; i < spaceNum; i++) {
                                events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                events.add(datas[0] + "3#58#" + i + 86);
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                            }
                            events.add(datas[0] + "#3#53#" + datas[3]);
                            events.add(datas[0] + "#3#54#" + datas[4]);
                            events.add(datas[0] + "#3#58#" + (86 + spaceNum));
                            events.add(datas[0] + "#0#0#0");
                            events.add(datas[0] + "#3#57#-1");
                            events.add(datas[0] + "#0#0#0");
                        } else if (script.startsWith(ScriptConstants.SWIPE_CMD) && script.contains(ScriptConstants.LONG_CLICK_CMD) && datas.length >= 10) {
                            int spaceNum = Integer.parseInt(datas[5]) / 10;
                            float startX = Float.parseFloat(datas[1]);
                            float startY = Float.parseFloat(datas[2]);
                            float endX = Float.parseFloat(datas[3]);
                            float endY = Float.parseFloat(datas[4]);
                            float xSpace = (endX - startX) / spaceNum;
                            float ySpace = (endY - startY) / spaceNum;

                            events.add(datas[0] + "#3#57#269");
                            events.add(datas[0] + "#3#53#" + datas[1]);
                            events.add(datas[0] + "#3#54#" + datas[2]);
                            events.add(datas[0] + "3#58#86");
                            events.add(datas[0] + "#0#0#0");
                            events.add(datas[0] + "#0#0#0");
                            for (int i = 1; i < spaceNum; i++) {
                                events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                events.add(datas[0] + "3#58#" + i + 86);
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                            }
                            events.add(datas[0] + "#3#53#" + datas[3]);
                            events.add(datas[0] + "#3#54#" + datas[4]);
                            events.add(datas[0] + "#3#58#" + (86 + spaceNum));
                            events.add(datas[0] + "#0#0#0");
                            events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + datas[9]);
                            events.add(datas[0] + "#3#57#-1");
                            events.add(datas[0] + "#0#0#0");
                        } else if (script.startsWith(ScriptConstants.LONG_CLICK_CMD) && !script.contains(ScriptConstants.SWIPE_CMD) && datas.length >= 4) {
                            events.add(datas[0] + "#3#57#269");
                            events.add(datas[0] + "#3#53#" + datas[1]);
                            events.add(datas[0] + "#3#54#" + datas[2]);
                            events.add(datas[0] + "#3#58#114");
                            events.add(datas[0] + "#0#0#0");
                            events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + datas[3]);
                            events.add(datas[0] + "#3#57#-1");
                            events.add(datas[0] + "#0#0#0");
                        } else if (script.startsWith(ScriptConstants.LONG_CLICK_CMD) && script.contains(ScriptConstants.SWIPE_CMD) && datas.length >= 10) {
                            events.add(datas[0] + "#3#57#269");
                            events.add(datas[0] + "#3#53#" + datas[1]);
                            events.add(datas[0] + "#3#54#" + datas[2]);
                            events.add(datas[0] + "#3#58#114");
                            events.add(datas[0] + "#0#0#0");
                            events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + (datas[3]));

                            int spaceNum = Integer.parseInt(datas[9]) / 10;
                            float startX = Float.parseFloat(datas[1]);
                            float startY = Float.parseFloat(datas[2]);
                            float endX = Float.parseFloat(datas[7]);
                            float endY = Float.parseFloat(datas[8]);
                            float xSpace = (endX - startX) / spaceNum;
                            float ySpace = (endY - startY) / spaceNum;

                            events.add(datas[0] + "3#58#86");
                            events.add(datas[0] + "#0#0#0");
                            events.add(datas[0] + "#0#0#0");
                            for (int i = 1; i < spaceNum; i++) {
                                events.add(datas[0] + "#3#53#" + (int) (Float.parseFloat(datas[1]) + i * xSpace));
                                events.add(datas[0] + "#3#54#" + (int) (Float.parseFloat(datas[2]) + i * ySpace));
                                events.add(datas[0] + "3#58#" + i + 86);
                                events.add(datas[0] + "#0#0#0");
                                events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + "10");
                            }
                            events.add(datas[0] + "#3#53#" + datas[7]);
                            events.add(datas[0] + "#3#54#" + datas[8]);
                            events.add(datas[0] + "#3#58#" + (86 + spaceNum));
                            events.add(datas[0] + "#0#0#0");
                            events.add(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT + datas[9]);
                            events.add(datas[0] + "#3#57#-1");
                            events.add(datas[0] + "#0#0#0");
                        } else if (script.startsWith(ScriptConstants.KEYEVENT_CMD) && datas.length >= 2) {
                            /*events.add(datas[0] + "#1#" + datas[1] + "#1");
                            events.add(datas[0] + "#0#0#0");
                            events.add(datas[0] + "#1#" + datas[1] + "#0");
                            events.add(datas[0] + "#0#0#0");*/
                        }
                        break;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * 小米手机6.0系统事件起始语
     *
     * @param events
     * @param data
     */
    public static void eventAndroid_6_MI_start(List<String> events, String data) {
        events.add(data + "#3#57#53");
        events.add(data + "#1#330#1");
        events.add(data + "#1#325#1");
    }

    /**
     * 小米手机6.0系统事件结束语
     *
     * @param events
     * @param data
     */
    public static void eventAndroid_6_MI_end(List<String> events, String data) {
        events.add(data + "#0#0#0");
        events.add(data + "#3#57#-1");
        events.add(data + "#3#330#0");
        events.add(data + "#3#325#0");
        events.add(data + "#0#0#0");
    }

    /**
     * 小米手机7.0系统事件起始语
     *
     * @param events
     * @param data
     */
    public static void eventAndroid_7_MI_start(List<String> events, String data) {
        events.add(data + "#3#57#53");
        events.add(data + "#1#330#1");
        events.add(data + "#1#325#1");
    }

    /**
     * 小米手机7.0系统事件结束语
     *
     * @param events
     * @param data
     */
    public static void eventAndroid_7_MI_end(List<String> events, String data) {
        events.add(data + "#3#57#0");
        events.add(data + "#3#330#0");
        events.add(data + "#3#325#0");
        events.add(data + "#0#2#0");
        events.add(data + "#0#0#0");
        events.add(data + "#1#330#0");
        events.add(data + "#0#0#0");
    }

    /**
     * 小米手机7.1.2系统事件起始语
     *
     * @param events
     * @param data
     */
    public static void eventAndroid_7_1_2_MI_start(List<String> events, String data) {
        events.add(data + "#3#57#0");
    }

    /**
     * 小米手机7.1.2系统事件结束语
     *
     * @param events
     * @param data
     */
    public static void eventAndroid_7_1_2_MI_end(List<String> events, String data) {
        events.add(data + "#3#48#0");
        events.add(data + "#3#58#0");
        events.add(data + "#3#57#-1");
        events.add(data + "#1#330#0");
        events.add(data + "#0#0#0");
    }


    /**
     * 发送数据流  此方法不对外开放
     *
     * @param events 事件合集
     */

    private static void flushData(final List<String> events) {
        if (events != null && events.size() > 0) {
            for (String event : events) {
                if (event.startsWith(ScriptConstants.SLEEP_CMD)) {
                    //SystemClock.sleep(Integer.parseInt(event.split(ScriptConstants.SPLIT)[1]));
                    long length = Integer.parseInt(event.split(ScriptConstants.SPLIT)[1]);
                    long time = System.currentTimeMillis();
                    while (true) {
                        if ((System.currentTimeMillis() - time) > length) {
                            break;
                        }
                    }
                } else if (!event.startsWith(ScriptConstants.KEYEVENT_CMD)) {
                    inputEvent(event, EventUtil.EVENT_ABS);
                } else {
                    inputEvent(event, EventUtil.EVENT_KEY);
                }
            }
        }
    }

}

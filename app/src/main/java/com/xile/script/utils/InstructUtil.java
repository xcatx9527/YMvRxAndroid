package com.xile.script.utils;

import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.bean.ScriptInfo;
import script.tools.config.KyeEvent;
import script.tools.config.ScriptConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * date: 2017/5/6 21:30
 *
 * @scene 指令工具类
 */
public class InstructUtil {

    /**
     * 脚本转换成C端指令
     *
     * @param cmdStr 脚本
     * @return 指令
     */
    public static String script2Cmd(String cmdStr) {
        String cmdStrScript = "";
        try {
            if (!TextUtils.isEmpty(cmdStr)) {
                if (cmdStr.startsWith(ScriptConstants.TAP_SCRIPT + ScriptConstants.SPLIT)) {//点击 -> tap
                    cmdStrScript = cmdStr.replace(ScriptConstants.TAP_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.TAP_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT)) {//滑动 -> swipe
                    cmdStrScript = cmdStr.replace(ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT)
                            .replace(ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.TEXT_SCRIPT + ScriptConstants.SPLIT)) {//文本 -> text
                    cmdStrScript = cmdStr.replace(ScriptConstants.TEXT_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.TEXT_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT)) {//睡眠 -> sleep
                    cmdStrScript = cmdStr.replace(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT)) {//长按 -> longclick
                    cmdStrScript = cmdStr.replace(ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT)
                            .replace(ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT)) {//按键 -> keyevent
                    if (cmdStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_POWER)) {//电源
                        cmdStrScript = cmdStr.replace(ScriptConstants.KEYEVENT_SCRIPT, ScriptConstants.KEYEVENT_CMD).replace(ScriptConstants.KEYEVENT_POWER, KyeEvent.KEYCODE_POWER);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_MENU)) {//菜单
                        cmdStrScript = cmdStr.replace(ScriptConstants.KEYEVENT_SCRIPT, ScriptConstants.KEYEVENT_CMD).replace(ScriptConstants.KEYEVENT_MENU, KyeEvent.KEYCODE_MENU);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_HOME)) {//桌面
                        cmdStrScript = cmdStr.replace(ScriptConstants.KEYEVENT_SCRIPT, ScriptConstants.KEYEVENT_CMD).replace(ScriptConstants.KEYEVENT_HOME, KyeEvent.KEYCODE_HOME);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_VOLUMN_UP)) {//音量加
                        cmdStrScript = cmdStr.replace(ScriptConstants.KEYEVENT_SCRIPT, ScriptConstants.KEYEVENT_CMD).replace(ScriptConstants.KEYEVENT_VOLUMN_UP, KyeEvent.KEYCODE_VOLUME_UP);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_VOLUMN_DOWN)) {//音量减
                        cmdStrScript = cmdStr.replace(ScriptConstants.KEYEVENT_SCRIPT, ScriptConstants.KEYEVENT_CMD).replace(ScriptConstants.KEYEVENT_VOLUMN_DOWN, KyeEvent.KEYCODE_VOLUME_DOWN);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_BACK)) {//回退
                        cmdStrScript = cmdStr.replace(ScriptConstants.KEYEVENT_SCRIPT, ScriptConstants.KEYEVENT_CMD).replace(ScriptConstants.KEYEVENT_BACK, KyeEvent.KEYCODE_BACK);
                    }
                } else if (cmdStr.startsWith(ScriptConstants.INPUT_SCRIPT + ScriptConstants.SPLIT)) {//输入 -> input
                    cmdStrScript = cmdStr.replace(ScriptConstants.INPUT_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.CHANGE_IME_SCRIPT + ScriptConstants.SPLIT)) {//切换输入法 -> ime
                    cmdStrScript = cmdStr.replace(ScriptConstants.CHANGE_IME_SCRIPT + ScriptConstants.SPLIT, ScriptConstants.IME_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.RANDOM_CLICK + ScriptConstants.SPLIT)) {//随机点击 ->randomclick
                    cmdStrScript = cmdStr.replaceAll(ScriptConstants.RANDOM_CLICK + ScriptConstants.SPLIT, ScriptConstants.RANDOM_CLICK_CMD + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.RANDOM_SWIPE + ScriptConstants.SPLIT)) {//随机滑动 ->randomswipe
                    cmdStrScript = cmdStr.replaceAll(ScriptConstants.RANDOM_SWIPE + ScriptConstants.SPLIT, ScriptConstants.RANDOM_SWIPE_CMD + ScriptConstants.SPLIT);
                } else {
                    cmdStrScript = cmdStr;
                }
            }
        } catch (Exception e) {
            LLog.e("脚本转换指令异常!");
        }
        return cmdStrScript;
    }


    /**
     * 指令转换成脚本
     *
     * @param cmdStr 指令
     * @return 脚本
     */
    public static synchronized String cmd2Script(String cmdStr) {
        String temp = "";
        try {
            if (!TextUtils.isEmpty(cmdStr)) {
                if (cmdStr.startsWith(ScriptConstants.TAP_CMD + ScriptConstants.SPLIT)) {//tap -> 点击
                    temp = cmdStr.replace(ScriptConstants.TAP_CMD + ScriptConstants.SPLIT, ScriptConstants.TAP_SCRIPT + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT)) {//swipe -> 滑动
                    temp = cmdStr.replace(ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT, ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.TEXT_CMD + ScriptConstants.SPLIT)) {//text -> 文本
                    temp = cmdStr.replace(ScriptConstants.TEXT_CMD + ScriptConstants.SPLIT, ScriptConstants.TEXT_SCRIPT + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT)) {//sleep -> 睡眠
                    temp = cmdStr.replace(ScriptConstants.SLEEP_CMD + ScriptConstants.SPLIT, ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT)) {//longclick -> 长按
                    temp = cmdStr.replace(ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT, ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT);
                } else if (cmdStr.startsWith(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT)) {//keyevent -> 按键
                    if (cmdStr.contains(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT + KyeEvent.KEYCODE_POWER)) {//电源
                        temp = cmdStr.replace(ScriptConstants.KEYEVENT_CMD, ScriptConstants.KEYEVENT_SCRIPT).replace(KyeEvent.KEYCODE_POWER, ScriptConstants.KEYEVENT_POWER);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT + KyeEvent.KEYCODE_MENU)) {//菜单
                        temp = cmdStr.replace(ScriptConstants.KEYEVENT_CMD, ScriptConstants.KEYEVENT_SCRIPT).replace(KyeEvent.KEYCODE_MENU, ScriptConstants.KEYEVENT_MENU);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT + KyeEvent.KEYCODE_HOME)) {//桌面
                        temp = cmdStr.replace(ScriptConstants.KEYEVENT_CMD, ScriptConstants.KEYEVENT_SCRIPT).replace(KyeEvent.KEYCODE_HOME, ScriptConstants.KEYEVENT_HOME);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT + KyeEvent.KEYCODE_VOLUME_UP)) {//音量加
                        temp = cmdStr.replace(ScriptConstants.KEYEVENT_CMD, ScriptConstants.KEYEVENT_SCRIPT).replace(KyeEvent.KEYCODE_VOLUME_UP, ScriptConstants.KEYEVENT_VOLUMN_UP);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT + KyeEvent.KEYCODE_VOLUME_DOWN)) {//音量减
                        temp = cmdStr.replace(ScriptConstants.KEYEVENT_CMD, ScriptConstants.KEYEVENT_SCRIPT).replace(KyeEvent.KEYCODE_VOLUME_DOWN, ScriptConstants.KEYEVENT_VOLUMN_DOWN);
                    } else if (cmdStr.contains(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT + KyeEvent.KEYCODE_BACK)) {//回退
                        temp = cmdStr.replace(ScriptConstants.KEYEVENT_CMD, ScriptConstants.KEYEVENT_SCRIPT).replace(KyeEvent.KEYCODE_BACK, ScriptConstants.KEYEVENT_BACK);
                    }
                }
            }
        } catch (Exception e) {
            LLog.e("指令转换脚本异常!");
        }
        return temp;
    }

    /**
     * 脚本转换成Su指令
     *
     * @param cmdstrList 脚本集合
     * @return 指令集合
     */
    public static List<String> script2SuCmdList(List<String> cmdstrList) {
        List<String> suCmdList = new ArrayList<String>();
        if (cmdstrList != null && cmdstrList.size() > 0) {
            for (int i = 0; i < cmdstrList.size(); i++) {
                String suCmdStr = script2SuCmd(cmdstrList.get(i));
                suCmdList.add(suCmdStr);
            }
        }
        return suCmdList;
    }

    /**
     * 脚本转换成Su指令
     *
     * @param cmdStr 脚本
     * @return 指令
     */
    public static String script2SuCmd(String cmdStr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (cmdStr.startsWith(ScriptConstants.IME_CMD + ScriptConstants.SPLIT)) {
            stringBuffer.append(ScriptConstants.SU_IME_INIT);
        } else if (cmdStr.startsWith(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT)) {
            stringBuffer.append(ScriptConstants.SU_AM_INIT);
        } else {
            stringBuffer.append(ScriptConstants.SU_INPUT_INIT);
        }
        try {
            if (!TextUtils.isEmpty(cmdStr)) {
                if (cmdStr.startsWith(ScriptConstants.TAP_CMD + ScriptConstants.SPLIT)) {
                    String cmd = cmdStr.replace(ScriptConstants.SPLIT, ScriptConstants.SPACE);
                    stringBuffer.append(cmd);
                } else if (cmdStr.startsWith(ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT)) {
                    String[] strs = cmdStr.split(ScriptConstants.SPLIT);
                    if (strs.length >= 5) {
                        String cmd = cmdStr.replace(ScriptConstants.SPLIT, ScriptConstants.SPACE);
                        stringBuffer.append(cmd);
                    }
                } else if (cmdStr.startsWith(ScriptConstants.KEYEVENT_CMD + ScriptConstants.SPLIT)) {
                    String cmd = cmdStr.replace(ScriptConstants.SPLIT, ScriptConstants.SPACE);
                    stringBuffer.append(cmd);
                } else if (cmdStr.startsWith(ScriptConstants.TEXT_CMD + ScriptConstants.SPLIT)) {
                    String cmd = cmdStr.replace(ScriptConstants.SPLIT, ScriptConstants.SPACE);
                    stringBuffer.append(cmd);
                } else if (cmdStr.startsWith(ScriptConstants.IME_CMD + ScriptConstants.SPLIT)) {
                    String[] data = cmdStr.split(ScriptConstants.SPLIT);
                    if (data.length >= 1) {
                        stringBuffer.append(data[1]);
                    }
                } else if (cmdStr.startsWith(ScriptConstants.INPUT_CMD + ScriptConstants.SPLIT)) {
                    String[] data = cmdStr.split(ScriptConstants.SPLIT);
                    if (data.length >= 1) {
                        stringBuffer.append("'").append(data[1]).append("'");
                    }
                } else {
                    stringBuffer.append(cmdStr);
                }
            }
        } catch (Exception e) {
            LLog.e("脚本转换成Su指令异常!");
        }
        return stringBuffer.toString();
    }


    /**
     * 将字符串集合转换为脚本集合
     *
     * @param strList
     * @return scriptList
     */
    public static List<ScriptInfo> getScriptList(List<String> strList) {
        List<ScriptInfo> scriptList = new ArrayList<>();
        if (strList != null && strList.size() > 0) {
            for (int i = 0; i < strList.size(); i++) {
                scriptList.add(new ScriptInfo(strList.get(i)));
            }
        }
        return scriptList;
    }

}

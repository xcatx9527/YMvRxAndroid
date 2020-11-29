package com.xile.script.utils.script;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatactivity.BaseFloatActivity;
import com.xile.script.base.ui.view.floatactivity.FunctionMenuActivity;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.InstructInfo;
import com.xile.script.bean.ScriptInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.RecordEnum;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.InstructUtil;
import com.xile.script.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import script.tools.config.KyeEvent;
import script.tools.config.ScriptConstants;


/**
 * 作者：赵小飞<br>
 * 时间 2017/2/27.
 */

public class CommandUtil {

    private static String lastTouchId = "-1";

    public static synchronized void commandConversion(String command) {
        if ((Constants.RECORD_STATE != RecordEnum.STOP_RECORD) && !BaseFloatActivity.isFloatActivityShow) {
            String[] strCommand = command.split(ScriptConstants.SPLIT);
            String touchId = strCommand[0];
            String instructId = strCommand[1];
            String type = strCommand[2];
            String code = strCommand[3];
            String value = strCommand[4];
            long sleepTime = Long.valueOf(strCommand[5]);
            if (!touchId.equals(lastTouchId)) {
                ScriptApplication.instructInfoList.clear();
            }
            InstructInfo instructInfo = new InstructInfo(touchId, instructId, type, code, value, sleepTime / 1000);
            if (code.equals("57") && !value.equals("-1") || (!code.equals("57") && !touchId.equals("-1"))) {
                ScriptApplication.getInstructInfoList().add(instructInfo);
                lastTouchId = touchId;
            } else if (code.equals("57") && value.equals("-1")) {
                ScriptApplication.getInstructInfoList().add(instructInfo);
                //调用方法进行转化成指令
                String commandStr = generateCmds(ScriptApplication.getInstructInfoList());
                if (commandStr.indexOf(ScriptConstants.CMD_SPLIT) > 0) {
                    String[] cmdList = commandStr.split(ScriptConstants.CMD_SPLIT);
                    for (String cmd : cmdList) {
                        ScriptInfo scriptInfo = new ScriptInfo(ScriptApplication.getInstructInfoList().get(ScriptApplication.getInstructInfoList().size() - 1).getTime(), cmd);
                        ScriptApplication.scriptInfoRecord.add(scriptInfo);
                    }
                } else {
                    if (Constants.RECORD_STATE != RecordEnum.PAUSE_RECORD) {
                        saveScript(commandStr, false);
                    }
                }
            } else if (code.equals(KyeEvent.KEYCODE_MENU) || code.equals(KyeEvent.KEYCODE_HOME) || code.equals(KyeEvent.KEYCODE_BACK) || code.equals(KyeEvent.KEYCODE_VOLUME_UP) || code.equals(KyeEvent.KEYCODE_VOLUME_DOWN) || code.equals(KyeEvent.KEYCODE_POWER)) {
                ScriptApplication.instructInfoList.add(instructInfo);
                String commandStr = generateCmds(ScriptApplication.instructInfoList);
                if (commandStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_VOLUMN_DOWN)) {  //音量－
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
                    AppUtil.startActivity(ScriptApplication.getInstance(), FunctionMenuActivity.class);
                } else if (commandStr.contains(ScriptConstants.KEYEVENT_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.KEYEVENT_VOLUMN_UP)) {  //音量+
                    saveScript(" ========== " + TimeUtil.formatTimeStamp(System.currentTimeMillis()) + " 你没看错 我就是分割线 ==========  ", true);
                }
                /*else if (commandStr.contains(ScriptConstants.CAPTURE_SCRIPT)) {
                    if (Constants.RECORD_STATE != RecordEnum.PAUSE_RECORD) {
                        saveScript(commandStr, false);//客服专版
                    }
                }*/
            }
        }
    }

    /**
     * 保存指令
     *
     * @param commandStr   指令
     * @param insertByUser 是否手动插入脚本
     */
    public static void saveScript(String commandStr, boolean insertByUser) {
        if (!insertByUser) {
            long timeSleep = System.currentTimeMillis();
            List<ScriptInfo> scriptInfoList = ScriptApplication.scriptInfoRecord;
            if (scriptInfoList.size() > 0) {
                ScriptInfo ScriptInfoTemp = scriptInfoList.get(scriptInfoList.size() - 1);
                long timeTemp = ScriptInfoTemp.getScriptTime();
                long sleepTime = (timeSleep - timeTemp);
                if (timeTemp != 0 && sleepTime > 1) {
                    ScriptInfo scriptInfoSleep = new ScriptInfo(timeSleep, ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT + sleepTime);
                    ScriptApplication.scriptInfoRecord.add(scriptInfoSleep);
                }
            }
            long time = System.currentTimeMillis();
            ScriptInfo scriptInfo = new ScriptInfo(time, commandStr);
            LLog.d(scriptInfo.toString());
            ScriptApplication.scriptInfoRecord.add(scriptInfo);
        } else {
            ScriptApplication.scriptInfoRecord.add(new ScriptInfo(commandStr));
        }

    }


    /**
     * 转换指令
     *
     * @param infoList 未转换的原始指令集合
     * @return 转换后封装好的指令字符串
     */
    public static String generateCmds(List<InstructInfo> infoList) {

        List<InstructInfo> infos = new ArrayList<>();
        List<InstructInfo> keycodes = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        Iterator<InstructInfo> it = infoList.iterator();
        while (it.hasNext()) {
            InstructInfo info = it.next();
            if ("53".equals(info.getCode()) || "54".equals(info.getCode())) {
                infos.add(new InstructInfo(info.getTime(), info.getCode(), info.getValue()));
            } else {
                keycodes.add(new InstructInfo(info.getTime(), info.getCode(), info.getValue()));
            }
        }

        try {
            if (infos.size() == 0) {  //keyevent事件
                LLog.d("keyevent事件!");
                try {
                    switch (keycodes.get(0).getCode()) {
                        case KyeEvent.KEYCODE_MENU:  //MENU
                            builder.append(ScriptConstants.KEYEVENT_CMD).append(ScriptConstants.SPLIT).append(Integer.parseInt(KyeEvent.KEYCODE_MENU));
                            break;

                        case KyeEvent.KEYCODE_HOME:  //HOME
                            builder.append(ScriptConstants.KEYEVENT_CMD).append(ScriptConstants.SPLIT).append(Integer.parseInt(KyeEvent.KEYCODE_HOME));
                            break;

                        case KyeEvent.KEYCODE_BACK:  //BACK
                            builder.append(ScriptConstants.KEYEVENT_CMD).append(ScriptConstants.SPLIT).append(Integer.parseInt(KyeEvent.KEYCODE_BACK));
                            break;

                        case KyeEvent.KEYCODE_VOLUME_UP:  //VOLUME UP 音量加
                            builder.append(ScriptConstants.KEYEVENT_CMD).append(ScriptConstants.SPLIT).append(Integer.parseInt(KyeEvent.KEYCODE_VOLUME_UP));
                            break;

                        case KyeEvent.KEYCODE_VOLUME_DOWN:  //VOLUME DOWN 音量减
                            builder.append(ScriptConstants.KEYEVENT_CMD).append(ScriptConstants.SPLIT).append(Integer.parseInt(KyeEvent.KEYCODE_VOLUME_DOWN));
                            break;

                        case KyeEvent.KEYCODE_POWER:  //POWER
                            builder.append(ScriptConstants.KEYEVENT_CMD).append(ScriptConstants.SPLIT).append(Integer.parseInt(KyeEvent.KEYCODE_POWER));
                            break;

                        default:
                            LLog.e("未收到任何事件!");
                            break;
                    }
                } catch (Exception e) {
                    LLog.e("keycode size = " + keycodes.size());
                }
            } else if (infos.size() == 2) {  //点击事件
                LLog.d("tap事件!");
                builder.append(ScriptConstants.TAP_CMD).append(ScriptConstants.SPLIT).append(infos.get(0).getValue()).append(ScriptConstants.SPLIT).append(infos.get(1).getValue());
            } else if (infos.size() > 2) {  //滑动事件
                try {
                    LLog.d("swipe事件!");
                    List<InstructInfo> cmds = new ArrayList<>();
                    cmds.add(infos.get(0));
                    cmds.add(infos.get(1));
                    if ("53".equals(infos.get(infos.size() - 1).getCode())) {
                        for (int i = infos.size() - 1; i >= 0; i--) {
                            if ("54".equals(infos.get(i).getCode())) {
                                cmds.add(infos.get(infos.size() - 1));
                                cmds.add(new InstructInfo(infos.get(infos.size() - 1).getTime(), "54", infos.get(i).getValue()));
                                break;
                            }
                        }
                    } else if ("54".equals(infos.get(infos.size() - 1).getCode())) {
                        for (int i = infos.size() - 1; i >= 0; i--) {
                            if ("53".equals(infos.get(i).getCode())) {
                                cmds.add(new InstructInfo(infos.get(infos.size() - 1).getTime(), "53", infos.get(i).getValue()));
                                cmds.add(infos.get(infos.size() - 1));
                                break;
                            }
                        }
                    }
                    long sleepTime = 0;
                    sleepTime = Math.abs((cmds.get(cmds.size() - 1).getTime() - cmds.get(0).getTime()));
                    builder.append(ScriptConstants.SWIPE_CMD).append(ScriptConstants.SPLIT).append(cmds.get(0).getValue()).append(ScriptConstants.SPLIT).append(cmds.get(1).getValue()).append(ScriptConstants.SPLIT)
                            .append(cmds.get(2).getValue()).append(ScriptConstants.SPLIT).append(cmds.get(3).getValue()).append(ScriptConstants.SPLIT).append(sleepTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        if (builder.toString().contains(ScriptConstants.CMD_SPLIT)) {
            LLog.d("转换后指令:" + builder.substring(0, builder.lastIndexOf(ScriptConstants.CMD_SPLIT)));
            return InstructUtil.cmd2Script(builder.substring(0, builder.lastIndexOf(ScriptConstants.CMD_SPLIT)));
        } else {
            LLog.d("转换后指令:" + builder.toString());
            return InstructUtil.cmd2Script(builder.toString());
        }

    }

    /**
     * 点处理
     *
     * @param tempInstructList 未处理的指令集合
     * @return cmdsTemp 处理后的指令集合
     */
    public static List<InstructInfo> filterInstruct(List<InstructInfo> tempInstructList) {
        List<InstructInfo> singleNum = new ArrayList<InstructInfo>();
        List<InstructInfo> noSingleNum = new ArrayList<InstructInfo>();
        List<InstructInfo> cmdsTemp = new ArrayList<InstructInfo>();
        for (int i = 0; i < tempInstructList.size(); i++) {
            if (i % 2 == 0) {//存奇数
                if (i != 0) {
                    singleNum.add(tempInstructList.get(i));
                    singleNum.add(tempInstructList.get(i));
                } else {
                    singleNum.add(tempInstructList.get(i));
                }
            } else {// 存偶数
                if (i != 1) {
                    noSingleNum.add(tempInstructList.get(i));
                    noSingleNum.add(tempInstructList.get(i));
                } else {
                    noSingleNum.add(tempInstructList.get(i));
                }
            }
        }
        singleNum.remove(singleNum.size() - 1);
        noSingleNum.remove(noSingleNum.size() - 1);
        for (int i = 0; i < noSingleNum.size(); i++) {
            cmdsTemp.add(singleNum.get(i));
            cmdsTemp.add(noSingleNum.get(i));
        }
        return cmdsTemp;
    }


}

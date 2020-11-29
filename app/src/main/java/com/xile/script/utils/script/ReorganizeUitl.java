package com.xile.script.utils.script;

import android.text.TextUtils;
import com.xile.script.base.ScriptApplication;
import com.xile.script.utils.ScreenUtil;
import script.tools.config.ScriptConstants;

/**
 * Created by chsh on 2017/4/12.
 * 横竖屏状态重组指令
 */

public class ReorganizeUitl {

    /**
     * 竖屏坐标转横屏坐标重组指令
     *
     * @param instrctstr
     * @return
     */
    public static String portraitToLandscape(String instrctstr) {

        if (!TextUtils.isEmpty(instrctstr)) {
            String[] split = instrctstr.split(ScriptConstants.SPLIT);
            if (split[0].equals(ScriptConstants.TAP_CMD) || (split[0].equals(ScriptConstants.LONG_CLICK_CMD) && !instrctstr.contains(ScriptConstants.SWIPE_CMD))) {//发生的是点击事件或长按事件
                /**
                 * 横屏状态重新计算点击的坐标
                 */
                String x_old = split[1];
                String y_old = split[2];
                String x_new = y_old;
                String y_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old));
                /**
                 * 产生的新点击的坐标指令
                 */
                if (split[0].equals(ScriptConstants.TAP_CMD)) {
                    instrctstr = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new;
                } else if (split[0].equals(ScriptConstants.LONG_CLICK_CMD)) {
                    instrctstr = ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new + ScriptConstants.SPLIT + split[3];
                }
            } else if (split[0].equals(ScriptConstants.SWIPE_CMD)) {  //发生的是滑动事件
                String x_old_start = split[1];
                String y_old_start = split[2];
                String x_old_end = split[3];
                String y_old_end = split[4];
                String time = split[5];

                String x_new_start = y_old_start;
                String y_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_start));
                String x_new_end = y_old_end;
                String y_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_end));
                if (!instrctstr.contains(ScriptConstants.LONG_CLICK_CMD)) {//不包含长按  -  纯滑动事件
                    instrctstr = ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time;
                } else if (instrctstr.contains(ScriptConstants.LONG_CLICK_CMD)) {//包含长按  -  滑动+长按事件
                    String x_longclick_old = split[7];
                    String y_longclick_old = split[8];
                    String x_longclick_new = y_longclick_old;
                    String y_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_longclick_old));
                    String longclick_time = split[9];
                    instrctstr = ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time + ScriptConstants.SPLIT + ScriptConstants.LONG_CLICK_CMD
                            + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time;
                }
            } else if (split[0].equals(ScriptConstants.LONG_CLICK_CMD) && instrctstr.contains(ScriptConstants.SWIPE_CMD)) {//长按+滑动事件
                String x_old_start = split[5];
                String y_old_start = split[6];
                String x_old_end = split[7];
                String y_old_end = split[8];
                String time = split[9];

                String x_new_start = y_old_start;
                String y_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_start));
                String x_new_end = y_old_end;
                String y_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_end));

                String x_longclick_old = split[1];
                String y_longclick_old = split[2];
                String x_longclick_new = y_longclick_old;
                String y_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_longclick_old));
                String longclick_time = split[3];
                instrctstr = ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time
                        + ScriptConstants.SPLIT + ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start +
                        ScriptConstants.SPLIT + x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time;
            }
        }
        return instrctstr;
    }

    /**
     * 横屏坐标转竖屏坐标重组指令
     */
    public static String landscapeToPortrait(String instrctstr) {

        if (!TextUtils.isEmpty(instrctstr)) {
            String[] split = instrctstr.split(ScriptConstants.SPLIT);
            if (split[0].equals(ScriptConstants.TAP_CMD) || (split[0].equals(ScriptConstants.LONG_CLICK_CMD) && !instrctstr.contains(ScriptConstants.SWIPE_CMD))) {//发生的是点击事件或长按事件
                /**
                 * 横屏状态重新计算点击的坐标
                 */
                String x_old = split[1];
                String y_old = split[2];
                String x_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old));
                String y_new = x_old;
                /**
                 * 产生的新点击的坐标指令
                 */
                if (split[0].equals(ScriptConstants.TAP_CMD)) {
                    instrctstr = ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new;
                } else if (split[0].equals(ScriptConstants.LONG_CLICK_CMD)) {
                    instrctstr = ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new + ScriptConstants.SPLIT + split[3];
                }
            } else if (split[0].equals(ScriptConstants.SWIPE_CMD)) {  //发生的是滑动事件
                String x_old_start = split[1];
                String y_old_start = split[2];
                String x_old_end = split[3];
                String y_old_end = split[4];
                String time = split[5];

                String x_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_start));
                String y_new_start = x_old_start;
                String x_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_end));
                String y_new_end = x_old_end;
                if (!instrctstr.contains(ScriptConstants.LONG_CLICK_CMD)) {//不包含长按  -  纯滑动事件
                    instrctstr = ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time;
                } else if (instrctstr.contains(ScriptConstants.LONG_CLICK_CMD)) {//包含长按  -  滑动+长按事件
                    String x_longclick_old = split[7];
                    String y_longclick_old = split[8];
                    String x_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_longclick_old));
                    String y_longclick_new = x_longclick_old;
                    String longclick_time = split[9];
                    instrctstr = ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time + ScriptConstants.SPLIT + ScriptConstants.LONG_CLICK_CMD
                            + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time;
                }
            } else if (split[0].equals(ScriptConstants.LONG_CLICK_CMD) && instrctstr.contains(ScriptConstants.SWIPE_CMD)) {//长按+滑动事件
                String x_old_start = split[5];
                String y_old_start = split[6];
                String x_old_end = split[7];
                String y_old_end = split[8];
                String time = split[9];

                String x_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_start));
                String y_new_start = x_old_start;
                String x_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_end));
                String y_new_end = x_old_end;


                String x_longclick_old = split[1];
                String y_longclick_old = split[2];
                String x_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_longclick_old));
                String y_longclick_new = x_longclick_old;
                String longclick_time = split[3];
                instrctstr = ScriptConstants.LONG_CLICK_CMD + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time
                        + ScriptConstants.SPLIT + ScriptConstants.SWIPE_CMD + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start +
                        ScriptConstants.SPLIT + x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time ;
            }
        }
        return instrctstr;
    }

    /**
     * 竖屏坐标转横屏坐标重组指令
     *
     * @param instrctstr
     * @return
     */
    public static String portraitToLandscapeInChainess(String instrctstr) {

        if (!TextUtils.isEmpty(instrctstr)) {
            String[] split = instrctstr.split(ScriptConstants.SPLIT);
            if (split[0].equals(ScriptConstants.TAP_SCRIPT) || (split[0].equals(ScriptConstants.LONG_CLICK_SCRIPT) && !instrctstr.contains(ScriptConstants.SWIPE_SCRIPT))) {//发生的是点击事件或长按事件
                /**
                 * 横屏状态重新计算点击的坐标
                 */
                String x_old = split[1];
                String y_old = split[2];
                String x_new = y_old;
                String y_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old));
                /**
                 * 产生的新点击的坐标指令
                 */
                if (split[0].equals(ScriptConstants.TAP_SCRIPT)) {
                    instrctstr = ScriptConstants.TAP_SCRIPT + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new;
                } else if (split[0].equals(ScriptConstants.LONG_CLICK_SCRIPT)) {
                    instrctstr = ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new + ScriptConstants.SPLIT + split[3];
                }
            } else if (split[0].equals(ScriptConstants.SWIPE_SCRIPT)) {  //发生的是滑动事件
                String x_old_start = split[1];
                String y_old_start = split[2];
                String x_old_end = split[3];
                String y_old_end = split[4];
                String time = split[5];

                String x_new_start = y_old_start;
                String y_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_start));
                String x_new_end = y_old_end;
                String y_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_end));
                if (!instrctstr.contains(ScriptConstants.LONG_CLICK_SCRIPT)) {//不包含长按  -  纯滑动事件
                    instrctstr = ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time;
                } else if (instrctstr.contains(ScriptConstants.LONG_CLICK_SCRIPT)) {//包含长按  -  滑动+长按事件
                    String x_longclick_old = split[7];
                    String y_longclick_old = split[8];
                    String x_longclick_new = y_longclick_old;
                    String y_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_longclick_old));
                    String longclick_time = split[9];
                    instrctstr = ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time + ScriptConstants.SPLIT + ScriptConstants.LONG_CLICK_SCRIPT
                            + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time;
                }
            } else if (split[0].equals(ScriptConstants.LONG_CLICK_SCRIPT) && instrctstr.contains(ScriptConstants.SWIPE_SCRIPT)) {//长按+滑动事件
                String x_old_start = split[5];
                String y_old_start = split[6];
                String x_old_end = split[7];
                String y_old_end = split[8];
                String time = split[9];

                String x_new_start = y_old_start;
                String y_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_start));
                String x_new_end = y_old_end;
                String y_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_old_end));

                String x_longclick_old = split[1];
                String y_longclick_old = split[2];
                String x_longclick_new = y_longclick_old;
                String y_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(x_longclick_old));
                String longclick_time = split[3];
                instrctstr = ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time
                        + ScriptConstants.SPLIT + ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start +
                        ScriptConstants.SPLIT + x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time;
            }
        }
        return instrctstr;
    }

    /**
     * 横屏坐标转竖屏坐标重组指令
     */
    public static String landscapeToPortraitInChainess(String instrctstr) {

        if (!TextUtils.isEmpty(instrctstr)) {
            String[] split = instrctstr.split(ScriptConstants.SPLIT);
            if (split[0].equals(ScriptConstants.TAP_SCRIPT) || (split[0].equals(ScriptConstants.LONG_CLICK_SCRIPT) && !instrctstr.contains(ScriptConstants.SWIPE_SCRIPT))) {//发生的是点击事件或长按事件
                /**
                 * 横屏状态重新计算点击的坐标
                 */
                String x_old = split[1];
                String y_old = split[2];
                String x_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old));
                String y_new = x_old;
                /**
                 * 产生的新点击的坐标指令
                 */
                if (split[0].equals(ScriptConstants.TAP_SCRIPT)) {
                    instrctstr = ScriptConstants.TAP_SCRIPT + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new;
                } else if (split[0].equals(ScriptConstants.LONG_CLICK_SCRIPT)) {
                    instrctstr = ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT + x_new + ScriptConstants.SPLIT + y_new + ScriptConstants.SPLIT + split[3];
                }
            } else if (split[0].equals(ScriptConstants.SWIPE_SCRIPT)) {  //发生的是滑动事件
                String x_old_start = split[1];
                String y_old_start = split[2];
                String x_old_end = split[3];
                String y_old_end = split[4];
                String time = split[5];

                String x_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_start));
                String y_new_start = x_old_start;
                String x_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_end));
                String y_new_end = x_old_end;
                if (!instrctstr.contains(ScriptConstants.LONG_CLICK_SCRIPT)) {//不包含长按  -  纯滑动事件
                    instrctstr = ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time;
                } else if (instrctstr.contains(ScriptConstants.LONG_CLICK_SCRIPT)) {//包含长按  -  滑动+长按事件
                    String x_longclick_old = split[7];
                    String y_longclick_old = split[8];
                    String x_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_longclick_old));
                    String y_longclick_new = x_longclick_old;
                    String longclick_time = split[9];
                    instrctstr = ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start + ScriptConstants.SPLIT +
                            x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time + ScriptConstants.SPLIT + ScriptConstants.LONG_CLICK_SCRIPT
                            + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time;
                }
            } else if (split[0].equals(ScriptConstants.LONG_CLICK_SCRIPT) && instrctstr.contains(ScriptConstants.SWIPE_SCRIPT)) {//长按+滑动事件
                String x_old_start = split[5];
                String y_old_start = split[6];
                String x_old_end = split[7];
                String y_old_end = split[8];
                String time = split[9];

                String x_new_start = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_start));
                String y_new_start = x_old_start;
                String x_new_end = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_old_end));
                String y_new_end = x_old_end;


                String x_longclick_old = split[1];
                String y_longclick_old = split[2];
                String x_longclick_new = String.valueOf(ScreenUtil.getDisplayParams(ScriptApplication.getInstance())[1] - Integer.parseInt(y_longclick_old));
                String y_longclick_new = x_longclick_old;
                String longclick_time = split[3];
                instrctstr = ScriptConstants.LONG_CLICK_SCRIPT + ScriptConstants.SPLIT + x_longclick_new + ScriptConstants.SPLIT + y_longclick_new + ScriptConstants.SPLIT + longclick_time
                        + ScriptConstants.SPLIT + ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + x_new_start + ScriptConstants.SPLIT + y_new_start +
                        ScriptConstants.SPLIT + x_new_end + ScriptConstants.SPLIT + y_new_end + ScriptConstants.SPLIT + time ;
            }
        }
        return instrctstr;
    }

}

package com.xile.script.utils.script;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.BaseFloatView;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.PopupInfo;
import com.xile.script.config.GameConfig;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.SleepConfig;
import com.xile.script.imagereact.ImageIdentifyUtil;
import com.xile.script.imagereact.ScreenShotFb;
import com.xile.script.utils.BitmapUtil;

import script.tools.config.ScriptConstants;

/**
 * date: 2017/5/8 15:33
 */
public class PlatformUtil {

    /**
     * 检测是否读取完进度条到达特定页面
     *
     * @return loading finished or not
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean loadingFinished() {
        boolean isIdentified = false;
        ScriptUtil.getPictureDetected();
        SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
        ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
        SystemClock.sleep(50);
        Bitmap largeBitmap = ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCapture();
        if (largeBitmap != null && BitmapUtil.needRotate(largeBitmap)) {
            largeBitmap = BitmapUtil.rotateBitmap(largeBitmap, 90);
        }
        LLog.d("largeBitmap!------------" + largeBitmap);
        if (largeBitmap != null) {
            if (PopupUtil.getPopupImgsList() != null && PopupUtil.getPopupImgsList().size() > 0) {  //登录平台图片
                for (PopupInfo info : PopupUtil.getPopupImgsList()) {
                    if ((info != null && PlatformConfig.IMG_TYPE_2.equals(info.getType()))) {
                        Rect rect = ImageIdentifyUtil.imageCompare(largeBitmap, info.getBitmap());
                        if (rect != null) {
                            if (info.getName().equals("AND退出平台.png")) {
                                LLog.d("识别AND退出平台!" + info.getName());
                                //配置#老虎平台报警退出#605,1900,375,1675
                                quitOut();
                                return false;
                            }
                            LLog.d("识别到界面!" + info.getName());
                            SocketUtil.currentTryLoadingNum = 1;
                            largeBitmap.recycle();
                            largeBitmap = null;
                            return true;
                        } else {
                            isIdentified = false;
                        }
                    }
                }

            }
        }
        return isIdentified;
    }


    /**
     * 初始化悬浮按钮状态
     *
     * @param x
     * @param y
     */

    public static void initRecordView(final int x, final int y) {
        final RecordFloatView recordFloatView = RecordFloatView.getInstance(ScriptApplication.getInstance());
        if (recordFloatView != null) {
            BaseFloatView.sFloatHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (RecordFloatView.getInstance(ScriptApplication.getInstance()).isShow()) {
                        recordFloatView.setNotOpen();
                        recordFloatView.setPosition(x, y);
                    }
                }
            }, 0);
        }
    }

    public static void quitOut() {
        //配置#老虎平台报警退出#605,1900,375,1675
        String exitStr = (String) GameConfig.getAreaAndRoleConfigs().get(GameConfig.GAME_ALERT_QUIT);
        if (!TextUtils.isEmpty(exitStr)) {
            String[] split = exitStr.split(ScriptConstants.CMD_SPLIT);
            int count = split.length / 2;
            for (int i = 0; i < count; i++) {
                SocketUtil.sendInstruct(ScriptConstants.TAP_CMD + ScriptConstants.SPLIT + split[0 + i * 2] + ScriptConstants.SPLIT + split[1 + i * 2], false);
                SystemClock.sleep(3000);
            }
        }
    }

}

package com.xile.script.base.ui.view.floatactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.service.FloatingService;
import com.xile.script.utils.common.HomeWatcher;

/**
 * date: 2017/4/8 19:20
 */
public class BaseFloatActivity extends Activity {
    public static boolean isFloatActivityShow;
    private HomeWatcher hw;
    protected boolean isFunctionMenuActivityShow;
    protected boolean isCollectActivityShow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFloatActivityShow = true;
        if (hw == null) {
            hw = new HomeWatcher(getApplicationContext());
            setHomeListener();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFloatActivityShow = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFloatActivityShow = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFloatActivityShow = false;
        stopHomeListener();
        if (!isCollectActivityShow) {
            ScriptApplication.getInstance().startService(new Intent(ScriptApplication.getInstance(), FloatingService.class));
        }
    }

    /**
     * Home键监听
     */
    protected void setHomeListener() {
        if (hw != null) {
            hw.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
                @Override
                public void onHomePressed() {
                    System.out.println("HomePressed");
                    LLog.d("HomePressed");
                    if (isFunctionMenuActivityShow) {
                        ScriptApplication.getInstance().startService(new Intent(ScriptApplication.getInstance(), FloatingService.class));
                    } else {
                        finish();
                    }
                }

                @Override
                public void onHomeLongPressed() {
                    LLog.d("HomeLongPressed");
                }
            });
            hw.startWatch();
        }
    }

    /**
     * 注销Home键监听
     */
    protected void stopHomeListener() {
        if (hw != null) {
            hw.stopWatch();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {  //keyCode==KeyEvent.KEYCODE_BACK 暂不屏蔽     Home键屏蔽不生效
            if (isFloatActivityShow) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

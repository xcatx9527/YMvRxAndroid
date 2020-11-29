package com.xile.script.base.ui.view.floatview;


import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.utils.ScreenUtil;
import com.xile.script.utils.common.HomeWatcher;

/**
 * date: 2017/3/17 15:07
 *
 * @scene 悬浮框基础类
 */
public abstract class BaseFloatView {

    public static final int LENGTH_ALWAYS = 0;

    protected Context mContext;
    protected int mDuration = LENGTH_ALWAYS;
    protected boolean isShow = false;
    protected WindowManager mWM;
    protected WindowManager.LayoutParams params;
    protected View mView;
    protected float xInScreen;
    protected float yInScreen;
    protected float xDownInScreen;
    protected float yDownInScreen;
    protected float xInView;
    protected float yInView;
    private HomeWatcher hw = new HomeWatcher(ScriptApplication.getInstance());

    public static Handler sFloatHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:  //录制及播放状态toast
                    if (!TextUtils.isEmpty((CharSequence) msg.obj)) {
                        String content = (String) msg.obj;
                        Toast.makeText(ScriptApplication.getInstance(), content, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:  //展示脚本轨迹
                    if (!TextUtils.isEmpty((CharSequence) msg.obj)) {
                        String command = (String) msg.obj;
                        PositionFloatView positionFloatView = new PositionFloatView(ScriptApplication.getInstance());
                        positionFloatView.showOrbit(command);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    public BaseFloatView(Context context) {
        this.mContext = context;
        mWM = (WindowManager) mContext.getSystemService(Service.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        //fix if sdk>=7.1.1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
    }

    public abstract void initView();


    protected Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Show the view for the specified duration.
     */
    public void show() {
        if (isShow) return;
        initTN(0, 0);
        isShow = true;
        //判断duration，如果大于#LENGTH_ALWAYS 则设置消失时间
        if (mDuration > LENGTH_ALWAYS) {
            sFloatHandler.postDelayed(hideRunnable, mDuration);
        }
    }

    /**
     * Show the view for the specified duration.
     *
     * @param x x-location
     * @param y y-location
     */
    public void show(int x, int y) {
        if (isShow) return;
        initTN(x, y);
        isShow = true;
        //判断duration，如果大于#LENGTH_ALWAYS 则设置消失时间
        if (mDuration > LENGTH_ALWAYS) {
            sFloatHandler.postDelayed(hideRunnable, mDuration);
        }
    }

    /**
     * Close the view if it's showing, or don't show it if it isn't showing yet.
     * You do not normally have to call this.  Normally view will disappear on its own
     * after the appropriate duration.
     */
    public void hide() {
        if (!isShow) return;
        mWM.removeView(mView);
        isShow = false;
    }

    public boolean isShow() {
        return this.isShow;
    }

    /**
     * Set how long to show the view for.
     *
     * @see #LENGTH_ALWAYS
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getDuration() {
        return mDuration;
    }


    public void setGravity(int gravity, int xOffset, int yOffset) {
        params.gravity = gravity;
        params.x = xOffset;
        params.y = yOffset;
    }


    protected void initTN(int x, int y) {
        try {
            setWindowParams();
            initPosition(x, y);
            mWM.addView(mView, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void initPosition(int x, int y) {
        setGravity(Gravity.LEFT | Gravity.TOP, x, y);
    }

    /**
     * 监听home键
     */
    protected void setHomeListener() {
        hw.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                LLog.d("HomePressed");
                if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()) == ScreenUtil.SCREEN_LANDSCAPE) {
                    hide();
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).setDuration(BaseFloatView.LENGTH_ALWAYS);
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).show();
                }
            }

            @Override
            public void onHomeLongPressed() {

            }
        });
        hw.startWatch();
    }

    /**
     * 注销home键监听
     */
    protected void stopHomeListener() {
        hw.stopWatch();
    }


    /**
     * 设置window参数
     */
    protected void setWindowParams() {
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public void setPosition(int x, int y) {
        updateViewPosition(x, y);
    }


    protected void updateViewPosition() {
        //更新浮动窗口位置参数
        params.x = (int) (xInScreen - xInView);
        params.y = (int) (yInScreen - yInView);
        mWM.updateViewLayout(mView, params);  //刷新显示
    }

    protected void updateViewPosition(int x, int y) {
        //更新浮动窗口位置参数
        params.x = x;
        params.y = y;
        mWM.updateViewLayout(mView, params);  //刷新显示
    }

}
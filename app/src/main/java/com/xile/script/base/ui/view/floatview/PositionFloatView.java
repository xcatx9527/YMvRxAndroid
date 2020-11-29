package com.xile.script.base.ui.view.floatview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.xile.script.base.ScriptApplication;
import com.xile.script.utils.ScreenUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.ReorganizeUitl;
import com.yzy.example.R;

import script.tools.config.ScriptConstants;

/**
 * date: 2017/4/15 20:20
 *
 * @scene 脚本播放手势轨迹显示
 */
public class PositionFloatView extends LinearLayout implements Animator.AnimatorListener {
    private WindowManager mManager;
    private WindowManager.LayoutParams mLayoutParams;
    private Context mContext;
    private View mView;


    public PositionFloatView(Context context) {
        super(context);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.float_position_point_layout, this);
        mView = view;
    }


    /**
     * 初始化view
     *
     * @param param
     */
    public void initView(int param) {
        if (mManager == null) {
            mManager = (WindowManager) mContext.getSystemService(Service.WINDOW_SERVICE);
        }
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.width = param;
        mLayoutParams.height = param;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        //fix if sdk>=7.1.1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        setLayoutParams(mLayoutParams);
        mManager.addView(this, mLayoutParams);
    }


    /**
     * 展示轨迹
     *
     * @param instruct 指令
     */
    public void showOrbit(String instruct) {
        try {
            if (!StringUtil.isEmpty(instruct)) {
                if (instruct.contains(ScriptConstants.TAP_CMD) || instruct.contains(ScriptConstants.SWIPE_CMD) || instruct.contains(ScriptConstants.LONG_CLICK_CMD)) {
                    if (ScreenUtil.getDisplayOrientation(ScriptApplication.getInstance()).equals(ScreenUtil.SCREEN_LANDSCAPE)) {  //横屏重组指令
                        instruct = ReorganizeUitl.portraitToLandscape(instruct);
                    }
                    String[] details = instruct.split(ScriptConstants.SPLIT);

                    if (details.length > 0) {
                        if (instruct.contains(ScriptConstants.TAP_CMD) || (details[0].equals(ScriptConstants.LONG_CLICK_CMD) && !instruct.contains(ScriptConstants.SWIPE_CMD))) {//发生的是点击事件或长按事件
                            initView(WindowManager.LayoutParams.WRAP_CONTENT);
                            if (ScreenUtil.getDisplayOrientation(mContext) == ScreenUtil.SCREEN_PORTRAIT) {
                                updatePosition(Integer.parseInt(details[1]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5), (int) (Integer.parseInt(details[2]) - ScreenUtil.getStatusBarHeight(mContext)) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5));
                            } else {
                                updatePosition(Integer.parseInt(details[1]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5), Integer.parseInt(details[2]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5));
                            }
                            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
                            if (!instruct.contains(ScriptConstants.LONG_CLICK_CMD)) {
                                animator.setDuration(200);
                            } else {
                                animator.setDuration(Integer.parseInt(details[3]));
                            }
                            animator.start();
                            animator.addListener(this);
                        } else if (instruct.startsWith(ScriptConstants.SWIPE_CMD)) {
                            initView(WindowManager.LayoutParams.MATCH_PARENT);
                            ObjectAnimator animatorX = null;
                            ObjectAnimator animatorY = null;
                            if (ScreenUtil.getDisplayOrientation(mContext) == ScreenUtil.SCREEN_PORTRAIT) { //竖屏状态
                                animatorX = ObjectAnimator.ofFloat(this, "translationX", Integer.parseInt(details[1]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5), Integer.parseInt(details[3]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5));
                                animatorY = ObjectAnimator.ofFloat(this, "translationY", Integer.parseInt(details[2]) - ScreenUtil.getStatusBarHeight(mContext) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5), (int) (Integer.parseInt(details[4]) - ScreenUtil.getStatusBarHeight(mContext)) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5));
                            } else {
                                animatorX = ObjectAnimator.ofFloat(this, "translationX", Integer.parseInt(details[1]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5), Integer.parseInt(details[3]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5));
                                animatorY = ObjectAnimator.ofFloat(this, "translationY", Integer.parseInt(details[2]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5), Integer.parseInt(details[4]) - ScreenUtil.dip2px(ScriptApplication.getInstance(), 5));
                            }
                            final ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
                            AnimatorSet set = new AnimatorSet();
                            set.playTogether(animatorX, animatorY);
                            if (Integer.parseInt(details[5]) < 100) {
                                set.setDuration(100);
                            } else {
                                set.setDuration(Integer.parseInt(details[5]));
                            }
                            if (!instruct.contains(ScriptConstants.LONG_CLICK_CMD)) {
                                animatorAlpha.setDuration(200);
                            } else {
                                animatorAlpha.setDuration(Integer.parseInt(details[9]));
                            }
                            animatorAlpha.addListener(this);
                            set.start();
                            set.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    animatorAlpha.start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLayoutParams = null;
        }

    }


    /**
     * 更新轨迹
     *
     * @param x 坐标X
     * @param y 坐标Y
     */
    public void updatePosition(int x, int y) {
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        mManager.updateViewLayout(this, mLayoutParams);
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mManager.removeView(mView);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

}

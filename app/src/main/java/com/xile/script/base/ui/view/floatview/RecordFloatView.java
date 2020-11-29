package com.xile.script.base.ui.view.floatview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xile.script.base.ScriptApplication;
import com.xile.script.base.activity.HomeMainActivity;
import com.xile.script.base.ui.promptdialog.PopupDialog;
import com.xile.script.base.ui.view.cutimage.CutImageActivity;
import com.xile.script.config.CollectEnum;
import com.xile.script.config.Constants;
import com.xile.script.config.ExecEnum;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.config.RecordEnum;
import com.xile.script.config.SleepConfig;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.brush.bean.BrushOrderInfo;
import com.xile.script.http.helper.brush.mina.BIMSocketClient;
import com.xile.script.http.helper.brush.mina.BOrderCaptureConfig;
import com.xile.script.http.helper.brush.mina.BOrderTypeConfig;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.http.helper.manager.bean.GamesOrderInfo;
import com.xile.script.http.helper.manager.mina.AlertConfig;
import com.xile.script.http.helper.manager.mina.CaptureConfig;
import com.xile.script.http.helper.manager.mina.IMSocketClient;
import com.xile.script.http.helper.manager.mina.NioTask;
import com.xile.script.imagereact.CaptureUtil;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.ScreenUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.ExecuteUtil;
import com.xile.script.utils.script.PlatformUtil;
import com.xile.script.utils.script.ScriptUtil;
import com.xile.script.utils.script.SocketUtil;
import com.yzy.example.R;

import script.tools.EventUtil;
import script.tools.config.ScriptConstants;

import static com.xile.script.config.RecordEnum.STOP_RECORD;


/**
 * date: 2017/3/17 15:07
 *
 * @scene: 录制悬浮框
 */
public class RecordFloatView extends BaseFloatView implements View.OnTouchListener, View.OnClickListener {
    private static RecordFloatView instance;
    private Context mContext;
    private ImageView img_float_small;  //悬浮球
    public ImageView img_float_big;  //展开的悬浮条
    public ImageView img_float_back;  //返回按钮
    private boolean isOpen = false;  //是否展开
    public static String bigFloatState = "record";  //展开后悬浮条的附属状态 --- 录制 or 播放 or 取图 or 采集区服角色  or  执行
    public static final String RECORD = "record";
    public static final String PLAY = "play";
    public static final String COLLECT = "collect";
    public static final String EXEC = "exec";


    private RecordFloatView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public static RecordFloatView getInstance(Context context) {
        if (instance == null) {
            synchronized (RecordFloatView.class) {
                if (instance == null) {
                    instance = new RecordFloatView(context);
                }
            }
        }
        return instance;
    }

    public void reset() {
        instance = null;
    }

    @Override
    public void initView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflate.inflate(R.layout.float_window, null);
        initImgFloat(mView);
    }

    /**
     * 初始化录制悬浮按钮
     *
     * @param view
     */
    private void initImgFloat(View view) {
        img_float_small = view.findViewById(R.id.img_small_float);
        img_float_big = view.findViewById(R.id.img_big_float);
        img_float_back = view.findViewById(R.id.img_back_float);
        img_float_big.setVisibility(View.GONE);
        img_float_back.setVisibility(View.GONE);
        img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle));
        img_float_small.setOnTouchListener(this);
        img_float_big.setOnTouchListener(this);
        img_float_big.setOnClickListener(this);
        img_float_back.setOnClickListener(this);
    }


    /**
     * 初始化展开后悬浮条的状态
     */
    public void initState() {
        if (bigFloatState != EXEC) {
            img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle_open));
            img_float_big.setVisibility(View.VISIBLE);
            img_float_back.setVisibility(View.VISIBLE);
            isOpen = true;
        } else {
            img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle));
            img_float_big.setVisibility(View.GONE);
            img_float_back.setVisibility(View.GONE);
            isOpen = false;
        }
        if (bigFloatState == RECORD && Constants.RECORD_STATE == STOP_RECORD) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_begin));
        } else if (bigFloatState == PLAY && Constants.PLAY_STATE == PlayEnum.START_PLAY) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_play_stop));
        } else if (bigFloatState == PLAY && Constants.PLAY_STATE == PlayEnum.STOP_PLAY) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_play_begin));
        } else if (bigFloatState == COLLECT && Constants.COLLECT_STATE == CollectEnum.COLLECT_PHOTO) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_collect_photo));
        } else if (bigFloatState == RECORD && Constants.RECORD_STATE == RecordEnum.PAUSE_RECORD) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_recover));
        } else if (bigFloatState == RECORD && Constants.RECORD_STATE == RecordEnum.RECOVERY_RECORD) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_stop));
        } else if (bigFloatState == EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_exec_stop));
        } else if (bigFloatState == EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_STOP) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_exec_begin));
        }
    }


    public static BaseFloatView makeText(Context context, CharSequence text, int duration) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        BaseFloatView exToast = getInstance(context);
        //exToast.toast = toast;
        exToast.mDuration = duration;
        return exToast;
    }

    public static BaseFloatView makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public void setNotOpen() {
        isOpen = false;
        img_float_big.setVisibility(View.GONE);
        img_float_back.setVisibility(View.GONE);
        img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle));
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - ScreenUtil.getStatusBarHeight(mContext);
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - ScreenUtil.getStatusBarHeight(mContext);
                if (v.getId() == R.id.img_small_float) {
                    break;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:   //捕获手指触摸移动动作
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - ScreenUtil.getStatusBarHeight(mContext);
                if (!isOpen) {
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    if (ScreenUtil.getDisplayOrientation(mContext) == ScreenUtil.SCREEN_PORTRAIT) {  //竖屏过滤
                        ScriptApplication.floatPointList.add(ScriptConstants.TAP_SCRIPT + ScriptConstants.SPLIT + Math.round(xInScreen) + ScriptConstants.SPLIT + (Math.round(yInScreen + ScreenUtil.getStatusBarHeight(mContext))));//需要过滤的点  tap
                    } else {  //横屏过滤
                        ScriptApplication.floatPointList.add(ScriptConstants.TAP_SCRIPT + ScriptConstants.SPLIT + Math.round(ScreenUtil.getDisplayParams(mContext)[1] - (yInScreen + ScreenUtil.getStatusBarHeight(mContext))) + ScriptConstants.SPLIT + Math.round(xInScreen));//需要过滤的点  tap
                    }
                    if (v.getId() == R.id.img_small_float) {
                        //展开大悬浮窗
                        if (!isOpen) {
                            img_float_big.setVisibility(View.VISIBLE);
                            img_float_back.setVisibility(View.VISIBLE);
                            img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle_open));
                            isOpen = true;
                        } else {
                            img_float_big.setVisibility(View.GONE);
                            img_float_back.setVisibility(View.GONE);
                            img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle));
                            isOpen = false;
                        }
                    }

                } else {
                    if (ScreenUtil.getDisplayOrientation(mContext) == ScreenUtil.SCREEN_PORTRAIT) {  //竖屏过滤
                        ScriptApplication.floatPointList.add(ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + Math.round(xDownInScreen) + ScriptConstants.SPLIT + (Math.round(yDownInScreen + ScreenUtil.getStatusBarHeight(mContext)))
                                + ScriptConstants.SPLIT + Math.round(xInScreen) + ScriptConstants.SPLIT + (Math.round(yInScreen + ScreenUtil.getStatusBarHeight(mContext))));//需要过滤的点 swipe
                    } else {  //横屏过滤
                        ScriptApplication.floatPointList.add(ScriptConstants.SWIPE_SCRIPT + ScriptConstants.SPLIT + Math.round(ScreenUtil.getDisplayParams(mContext)[1] - (yDownInScreen + ScreenUtil.getStatusBarHeight(mContext))) + ScriptConstants.SPLIT + Math.round(xDownInScreen)
                                + ScriptConstants.SPLIT + Math.round(ScreenUtil.getDisplayParams(mContext)[1] - (yInScreen + ScreenUtil.getStatusBarHeight(mContext))) + ScriptConstants.SPLIT + Math.round(xInScreen));//需要过滤的点 swipe
                    }
                }
                if (!isOpen) {
                    updateViewPosition(0, (int) (yInScreen - yInView));
                }
                if (v.getId() == R.id.img_small_float) {
                    break;
                } else {
                    return false;
                }
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_big_float:
                img_float_big.setVisibility(View.GONE);
                img_float_back.setVisibility(View.GONE);
                img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle));
                isOpen = false;
                if (bigFloatState == RECORD) {
                    switch (Constants.RECORD_STATE) {
                        case START_RECORD:  //开始录制状态
                            setPauseOrStopRecord();
                            break;

                        case PAUSE_RECORD:  //暂停录制状态
                            setRecoveryRecord();
                            break;

                        case RECOVERY_RECORD:  //恢复录制状态
                            setStopRecord();
                            break;

                        case STOP_RECORD:  //停止录制状态
                            setStartRecord();
                            break;

                        default:
                            break;
                    }
                } else if (bigFloatState == PLAY) {
                    switch (Constants.PLAY_STATE) {
                        case START_PLAY:  //正在播放状态
                            setPlayFinished();
                            break;

                        case PAUSE_PLAY:  //暂停播放状态
                            setPlayRecovery();
                            break;

                        case STOP_PLAY:  //未播放状态
                            setPlayStart();
                            break;

                        default:
                            break;
                    }
                } else if (bigFloatState == COLLECT) {
                    switch (Constants.COLLECT_STATE) {
                        case COLLECT_PHOTO:  //采集图片
                            if (Build.VERSION.SDK_INT >= 21) {
                                hide();
                                ScriptApplication.getService().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        CaptureUtil.takeScreen(ScriptApplication.getInstance(), Constants.SCRIPT_FOLDER_TAKE_TEMP_PNG_PATH, true, Bitmap.CompressFormat.PNG, CutImageActivity.class);//调用截屏工具类进行取图功能的截取
                                    }
                                });
                            } else {
                                updateMessage("您的手机暂不支持取图功能！");
                            }
                            break;

                        default:
                            break;
                    }
                } else if (bigFloatState == EXEC) {
                    switch (Constants.EXEC_STATE) {
                        case EXEC_START://正在执行状态
                            switch (Constants.PLAY_STATE) {
                                case PAUSE_PLAY:
                                    setPlayRecovery();
                                    break;
                                default:
                                    setExecFinished();
                                    break;
                            }
                            break;

                        case EXEC_STOP://停止执行状态
                            setStartExec();
                            break;

                        default:
                            break;
                    }
                }

                break;

            case R.id.img_back_float:  //返回
                AppUtil.jumpToHome(mContext, HomeMainActivity.class);
                ScriptApplication.getService().execute(() -> CMDUtil.execShell("pkill main_exec"));
                if (Constants.PLAY_STATE == PlayEnum.START_PLAY || Constants.PLAY_STATE == PlayEnum.PAUSE_PLAY) {
                    BrushOrderHelper.getInstance().closeVPN();
                }
                break;

            default:
                break;
        }

    }


    /**
     * 开始录制
     *
     * @description 上一状态为停止录制
     */
    public void setStartRecord() {
        if (Build.VERSION.SDK_INT >= 21) {
            ScriptApplication.getService().execute(() -> {
                CaptureUtil.takeScreen(ScriptApplication.getInstance(), Constants.SCRIPT_FOLDER_TEMP_PNG_PATH, false, Bitmap.CompressFormat.JPEG, CutImageActivity.class);//调用截屏工具类，进行录制截屏
            });
        }
        Constants.RECORD_STATE = RecordEnum.START_RECORD;
        if (Constants.needPauseRecord) {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_pause));
        } else {
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_stop));
        }
        updateMessage(mContext.getResources().getString(R.string.text_begin_record));
        ScriptApplication.getService().execute(() -> CMDUtil.execShell("./data/local/tmp/init"));
    }


    /**
     * 暂停或停止录制
     *
     * @description 上一状态为正在录制
     */
    public void setPauseOrStopRecord() {
        if (Constants.needPauseRecord) {
            Constants.RECORD_STATE = RecordEnum.PAUSE_RECORD;
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_recover));
            updateMessage(mContext.getResources().getString(R.string.text_pause_record));
        } else {
            Constants.RECORD_STATE = STOP_RECORD;
            Constants.needSave = true;
            img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_begin));
            updateMessage(mContext.getResources().getString(R.string.text_stop_record));
            AppUtil.jumpToHome(mContext, HomeMainActivity.class);
            ScriptApplication.getService().execute(() -> CMDUtil.execShell("pkill main_exec"));
        }
    }


    /**
     * 恢复录制
     *
     * @description 上一状态为暂停录制
     */
    public void setRecoveryRecord() {
        Constants.RECORD_STATE = RecordEnum.RECOVERY_RECORD;
        img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_stop));
        updateMessage(mContext.getResources().getString(R.string.text_recovery_record));
    }


    /**
     * 停止录制
     *
     * @description 上一状态为正在录制或恢复录制
     */
    public void setStopRecord() {
        Constants.RECORD_STATE = STOP_RECORD;
        Constants.needSave = true;
        img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_record_begin));
        updateMessage(mContext.getResources().getString(R.string.text_stop_record));
        AppUtil.jumpToHome(mContext, HomeMainActivity.class);
        ScriptApplication.getService().execute(() -> CMDUtil.execShell("pkill main_exec"));
    }


    /**
     * 开始播放
     *
     * @description 上一状态为停止播放
     */
    public void setPlayStart() {
        img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_play_stop));
        hide();
        ScriptApplication.getService().execute(() -> {
            ExecuteUtil.execLocalScript(1, Constants.SCRIPT_FOLDER_TEMP_PATH);
        });
    }


    /**
     * 暂停播放
     *
     * @description 上一状态为正在播放
     */
    public void setPlayPause() {
        Constants.PLAY_STATE = PlayEnum.PAUSE_PLAY;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_play_recory));
                img_float_small.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_circle_open));
                img_float_big.setVisibility(View.VISIBLE);
                img_float_back.setVisibility(View.VISIBLE);
                isOpen = true;
            }
        });
        updateMessage(mContext.getResources().getString(R.string.text_pause_play));
    }


    /**
     * 恢复播放
     *
     * @description 上一状态为暂停播放
     */
    public void setPlayRecovery() {
        if (Constants.execServerScript) {
            long pauseTime = System.currentTimeMillis() - Constants.getOrderTime;
            Constants.getOrderTime += pauseTime;
            String description = "";
            if (BOrderTypeConfig.ORDER_TYPE_PAY == Constants.sBrushOrderInfo.getOrderType() || BOrderTypeConfig.ORDER_TYPE_AUTO_PAY == Constants.sBrushOrderInfo.getOrderType()) {
                description = "充值备注:<font color=\"#FF0000\">" + Constants.sBrushOrderInfo.getDesc() + "</font>";
            } else if (BOrderTypeConfig.ORDER_TYPE_UPGRADE == Constants.sBrushOrderInfo.getOrderType()) {
                description = "升级备注:<font color=\"#FF0000\">" + Constants.sBrushOrderInfo.getDesc() + "</font>";
            }
            final PopupDialog dialog = new PopupDialog(ScriptApplication.getInstance(), 6, true);
            dialog.setOneButtonText("恢复播放,继续执行此订单!");
            dialog.setTwoButtonText("废除当前订单,开始获取下一订单!");
            dialog.setThreeButtonText("误操作,点此取消!");
            dialog.setFourButtonText("截屏当前操作界面!");
            dialog.setFiveButtonText("截屏充值界面并上传图片!");
            dialog.setSixButtonText(Html.fromHtml(description));
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH && (BOrderTypeConfig.ORDER_TYPE_PAY == Constants.sBrushOrderInfo.getOrderType() || BOrderTypeConfig.ORDER_TYPE_AUTO_PAY == Constants.sBrushOrderInfo.getOrderType())) {
                dialog.setFiveButtonVisiable();
            }
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!BrushOrderHelper.getInstance().rechargePicUploaded) {
                        final PopupDialog secondDialog = new PopupDialog(ScriptApplication.getInstance(), 1, false);
                        secondDialog.setTitle("您还未上传过充值截图!");
                        secondDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
                        secondDialog.setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                secondDialog.dismiss();
                            }
                        });
                        secondDialog.show();
                        return;
                    }
                    final PopupDialog secondDialog = new PopupDialog(ScriptApplication.getInstance(), 2, false);
                    secondDialog.setTitle("确定要恢复播放吗？");
                    secondDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
                    secondDialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondDialog.dismiss();
                            Constants.PLAY_STATE = PlayEnum.START_PLAY;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    RecordFloatView.getInstance(ScriptApplication.getInstance()).img_float_big.setImageDrawable(ScriptApplication.getInstance().getDrawable(R.drawable.float_exec_stop));
                                }
                            });
                            updateMessage(ScriptApplication.getInstance().getResources().getString(R.string.text_recovery_play));
                            Constants.currentRobotState = CaptureConfig.ROBOT_IS_CAPTURING;
                            SocketUtil.continueExec();
                            dialog.dismiss();
                        }
                    });
                    secondDialog.setOnNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondDialog.dismiss();
                        }
                    });
                    secondDialog.show();
                }
            });
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupDialog secondDialog = new PopupDialog(ScriptApplication.getInstance(), 2, false);
                    secondDialog.setTitle("确定要废除此订单吗？");
                    secondDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
                    secondDialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondDialog.dismiss();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    RecordFloatView.getInstance(ScriptApplication.getInstance()).img_float_big.setImageDrawable(ScriptApplication.getInstance().getDrawable(R.drawable.float_exec_stop));
                                }
                            });
                            if (Constants.execServerScript) {
                                if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                                    BaseOrderHelper.callAlertAndFinish(AlertConfig.ALERT_AND_ABOLISH_NONE_ROLE, "人工协助废除订单_NULL");
                                } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                                    BrushOrderHelper.getInstance().orderDealFailure("确定要废除此订单吗!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                                }
                            }
                            dialog.dismiss();
                        }
                    });
                    secondDialog.setOnNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondDialog.dismiss();
                        }
                    });
                    secondDialog.show();
                }
            });
            dialog.setOtherListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCaptureListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ScriptApplication.getService().execute(new Runnable() {
                        @Override
                        public void run() {
                            RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
                            SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
                            SpUtil.putKeyString(PlatformConfig.CURRENT_ORDER_CAPTURE_TYPE, BOrderCaptureConfig.ORDER_CAPTURE_TYPE_ROLE);
                            ScriptUtil.dealWithCapture("截屏#角色_1");
                        }
                    });
                }
            });
            dialog.setRechargeCaptureListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ScriptApplication.getService().execute(new Runnable() {
                        @Override
                        public void run() {
                            RecordFloatView.getInstance(ScriptApplication.getInstance()).hide();
                            SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
                            ScriptUtil.dealWithCapture("截屏#充值_1");
                            SystemClock.sleep(SleepConfig.SLEEP_TIME_1000);
                            ScriptUtil.dealWithUploadCaptureImg("上传截图#充值截图");
                        }
                    });
                }
            });
            dialog.show();
        } else {
            SocketUtil.continueExec();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    RecordFloatView.getInstance(ScriptApplication.getInstance()).img_float_big.setImageDrawable(ScriptApplication.getInstance().getDrawable(R.drawable.float_play_stop));
                }
            });
            Constants.PLAY_STATE = PlayEnum.START_PLAY;
            updateMessage(ScriptApplication.getInstance().getResources().getString(R.string.text_recovery_play));
        }

    }


    /**
     * 停止播放
     *
     * @description 上一状态为正在播放
     */
    public void setPlayFinished() {
//        ScriptUtil.upLoadCountTime();

        if (Constants.PLAY_STATE == PlayEnum.STOP_PLAY) {
            return;
        }
        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
        try {
            EventUtil.closeEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bigFloatState != EXEC) {//不是执行状态
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_play_begin));
                }
            });
        }
        updateMessage(mContext.getResources().getString(R.string.text_stop_play));
        if (Constants.execServerScript) {
            if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                if (SpUtil.getKeyBoolean(PlatformConfig.NEED_KILL_APP, false)) {
                    PlatformUtil.quitOut();
                    AppUtil.killApp(SpUtil.getKeyString(PlatformConfig.NEED_KILL_PACKAGENAME, ""));
                    //返回订单处理成功与否
                    if (SpUtil.getKeyBoolean(PlatformConfig.ORDER_EXEC_SUCCESS, false)) {
                        NioTask.getInstance().sendEmptyMessageDelayed(NioTask.MSG_TYPE_REQUEST_UPLOAD_IMG, 1000);
                    } else {
                        NioTask.getInstance().sendEmptyMessageDelayed(NioTask.MSG_TYPE_CALL_ALERT, 1000);
                    }
                }
            } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                if (SpUtil.getKeyBoolean(PlatformConfig.NEED_KILL_APP, false)) {
                    //订单结束再返回一次IP信息  返回订单处理成功与否已放到IP调取协议里
                    ScriptUtil.dealWithReturnIpInfo(1);
                }
            }
        } else {
            BrushOrderHelper.getInstance().closeVPN();
            SocketUtil.clearMessage();
            ScriptApplication.cancelAll();
        }

    }


    /**
     * 开始执行
     *
     * @description 上一状态为停止执行
     */
    public void setStartExec() {
        String jsonStr = null;
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            jsonStr = FileHelper.getFileString(Constants.MANAGER_ORDER_FOLDER_TEMP_PATH);
            if (!StringUtil.isEmpty(jsonStr)) {
                Constants.sGamesOrderInfo = ScriptApplication.getGson().fromJson(jsonStr, GamesOrderInfo.class);
            }
        } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
            jsonStr = FileHelper.getFileString(Constants.BRUSH_ORDER_FOLDER_TEMP_PATH);
            if (!StringUtil.isEmpty(jsonStr)) {
                Constants.sBrushOrderInfo = ScriptApplication.getGson().fromJson(jsonStr, BrushOrderInfo.class);
            }
        }
        if (Constants.sGamesOrderInfo != null || Constants.sBrushOrderInfo != null) {
            final PopupDialog dialog = new PopupDialog(ScriptApplication.getInstance(), 2, false);
            dialog.setTitle(ScriptApplication.getInstance().getString(R.string.dialog_flush_count));
            dialog.setOneButtonText("继续");
            dialog.setTwoButtonText("忽略");
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.EXEC_STATE = ExecEnum.EXEC_START;
                    Constants.PLAY_STATE = PlayEnum.START_PLAY;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            RecordFloatView.getInstance(ScriptApplication.getInstance()).img_float_big.setImageDrawable(ScriptApplication.getInstance().getDrawable(R.drawable.float_exec_stop));
                        }
                    });
                    dialog.dismiss();
                    if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
                        Constants.currentRobotState = CaptureConfig.ROBOT_IS_FREE;
                        NioTask.getInstance().sendEmptyMessageDelayed(NioTask.MSG_TYPE_GET_ORDER, 5000);
                        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_DEAL_WITH_ORDER, Constants.sGamesOrderInfo, 1000);
                    } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
                        BrushTask.getInstance().sendEmptyMessageDelayed(BrushTask.B_MSG_TYPE_GET_ORDER, 5000);
                        BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_DEAL_WITH_ORDER, Constants.sBrushOrderInfo, 1000);
                    }
                }
            });
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    initGetOrder();
                }
            });
            dialog.show();
        } else {
            initGetOrder();
        }
    }


    /**
     * 获取订单初始化
     */
    public void initGetOrder() {
        Constants.EXEC_STATE = ExecEnum.EXEC_START;
        Constants.PLAY_STATE = PlayEnum.START_PLAY;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_exec_stop));
            }
        });
        updateMessage(mContext.getResources().getString(R.string.text_start_exec));
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            Constants.currentRobotState = CaptureConfig.ROBOT_IS_FREE;
            NioTask.getInstance().sendEmptyMessageDelayed(NioTask.MSG_TYPE_GET_ORDER, 5000);
        } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
            BrushTask.getInstance().sendEmptyMessageDelayed(BrushTask.B_MSG_TYPE_GET_ORDER, 5000);
        }
    }


    /**
     * 停止执行
     *
     * @description 上一状态为正在执行
     */
    public void setExecFinished() {
        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
        Constants.EXEC_STATE = ExecEnum.EXEC_STOP;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                img_float_big.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_exec_begin));
            }
        });
        updateMessage(mContext.getResources().getString(R.string.text_stop_exec));
        if (Constants.currentPlatform == ScriptConstants.PLATFORM_MANAGER) {
            for (int i = 1; i <= NioTask.NIO_TOTAL_MSG_TYPE; i++) {
                NioTask.getInstance().removeMessages(i);
            }
        } else if (Constants.currentPlatform == ScriptConstants.PLATFORM_BRUSH) {
            BrushOrderHelper.getInstance().closeVPN();
            for (int i = 1; i <= BrushTask.getInstance().BRUSH_TOTAL_MSG_TYPE; i++) {
                BrushTask.getInstance().removeMessages(i);
            }
        }
        SocketUtil.clearMessage();
        ScriptApplication.cancelAll();
        BaseOrderHelper.resetData();
        IMSocketClient.destroy();
        BIMSocketClient.destroy();
        NioTask.release();
        BrushTask.release();
    }


    /**
     * 更新消息状态
     *
     * @param content 消息内容
     */
    public static void updateMessage(String content) {
        Message message = Message.obtain();
        message.what = 1;
        message.obj = content;
        sFloatHandler.sendMessage(message);
    }



}

package com.xile.script.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chenyang.lloglib.LLog;
import com.xile.script.adapter.ViewPagerAdapter;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.fragment.PlayFragment;
import com.xile.script.base.fragment.RecordFragment;
import com.xile.script.base.fragment.SettingFragment;
import com.xile.script.base.fragment.ToolFragment;
import com.xile.script.base.ui.promptdialog.PopupDialog;
import com.xile.script.base.ui.promptdialog.PromptDialog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.CollectEnum;
import com.xile.script.config.Constants;
import com.xile.script.config.ExecEnum;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.config.RecordEnum;
import com.xile.script.http.helper.BaseOrderHelper;
import com.xile.script.http.helper.brush.bean.BrushOrderInfo;
import com.xile.script.http.helper.brush.mina.BIMSocketClient;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.http.helper.manager.bean.GamesOrderInfo;
import com.xile.script.http.helper.manager.mina.IMSocketClient;
import com.xile.script.http.helper.manager.mina.NioTask;
import com.xile.script.imagereact.ScreenShotFb;
import com.xile.script.service.FloatingService;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.ImageUtil;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.CommandUtil;
import com.yzy.example.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import script.tools.config.ScriptConstants;

/**
 * date: 2017/6/20 21:03
 */
public class HomeMainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPagerAdapter pagerAdapter;
    private RecordFragment recordFragment;
    private PlayFragment playFragment;
    private SettingFragment settingFragment;
    private ToolFragment toolFragment;
    private ViewPager viewPager;
    private ImageView mImgRecord;
    private TextView mTvRecord;
    private LinearLayout mLlRecord;
    private ImageView mImgPlay;
    private TextView mTvPlay;
    private LinearLayout mLlPlay;
    private ImageView mImgSet;
    private TextView mTvSet;
    private LinearLayout mLlSet;
    private ImageView mImgTool;
    private TextView mTvTool;
    private LinearLayout mLlTool;
    private long nowTime;
    private PromptDialog dialog;
    private EditText editText;
    private MediaProjectionManager mMediaProjectionManager;
    private int REQUEST_MEDIA_PROJECTION = 1;
    private Intent intent1;
    private String bootType;

    @Override
    protected void setView() {
        super.setView();
        setContentView(R.layout.home_activity_main);
        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        mImgRecord = findViewById(R.id.img_record);
        mTvRecord = findViewById(R.id.tv_record);
        mLlRecord = findViewById(R.id.ll_record);
        mImgPlay = findViewById(R.id.img_play);
        mTvPlay = findViewById(R.id.tv_play);
        mLlPlay = findViewById(R.id.ll_play);
        mImgSet = findViewById(R.id.img_set);
        mTvSet = findViewById(R.id.tv_set);
        mLlSet = findViewById(R.id.ll_set);
        mImgTool = findViewById(R.id.img_tool);
        mTvTool = findViewById(R.id.tv_tool);
        mLlTool = findViewById(R.id.ll_tool);
    }

    /**
     * 初始化截图工具
     */
    protected void initData() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            if (mMediaProjectionManager == null) {
                mMediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            }
            if (ScreenShotFb.getInstance(ScriptApplication.getInstance()).getResult() == 0 || ScreenShotFb.getInstance(ScriptApplication.getInstance()).getIntent() == null) {
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
                ScreenShotFb.getInstance(ScriptApplication.getInstance()).setMediaProjectionManager(mMediaProjectionManager);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).setResult(resultCode);
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).setIntent(data);
                }
            }
        }
    }


    @Override
    protected void init() {
        super.init();
        final List<Fragment> list = new ArrayList<>();
        recordFragment = new RecordFragment();
        playFragment = new PlayFragment();
        toolFragment = new ToolFragment();
        settingFragment = new SettingFragment();
        list.add(recordFragment);
        list.add(playFragment);
        list.add(toolFragment);
        list.add(settingFragment);
        pagerAdapter = new ViewPagerAdapter(list);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTvRecord.setTextColor(getResources().getColor(R.color.color_8CA5FA));
                        mTvPlay.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvSet.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvTool.setTextColor(getResources().getColor(R.color.color_979797));
                        mImgRecord.setImageResource(R.drawable.tab_record_h);
                        mImgPlay.setImageResource(R.drawable.tab_play);
                        mImgSet.setImageResource(R.drawable.tab_setting);
                        mImgTool.setImageResource(R.drawable.tab_tool);
                        break;
                    case 1:
                        mTvRecord.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvPlay.setTextColor(getResources().getColor(R.color.color_8CA5FA));
                        mTvSet.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvTool.setTextColor(getResources().getColor(R.color.color_979797));
                        mImgRecord.setImageResource(R.drawable.tab_record);
                        mImgPlay.setImageResource(R.drawable.tab_play_h);
                        mImgSet.setImageResource(R.drawable.tab_setting);
                        mImgTool.setImageResource(R.drawable.tab_tool);
                        if (playFragment != null) {
                            playFragment.initAdapter();
                        }
                        break;
                    case 2:
                        mTvRecord.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvPlay.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvSet.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvTool.setTextColor(getResources().getColor(R.color.color_8CA5FA));
                        mImgRecord.setImageResource(R.drawable.tab_record);
                        mImgPlay.setImageResource(R.drawable.tab_play);
                        mImgSet.setImageResource(R.drawable.tab_setting);
                        mImgTool.setImageResource(R.drawable.tab_tool_h);
                        break;
                    case 3:
                        mTvRecord.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvPlay.setTextColor(getResources().getColor(R.color.color_979797));
                        mTvSet.setTextColor(getResources().getColor(R.color.color_8CA5FA));
                        mTvTool.setTextColor(getResources().getColor(R.color.color_979797));
                        mImgRecord.setImageResource(R.drawable.tab_record);
                        mImgPlay.setImageResource(R.drawable.tab_play);
                        mImgSet.setImageResource(R.drawable.tab_setting_h);
                        mImgTool.setImageResource(R.drawable.tab_tool);
                        break;
                }
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffPx) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setCurrentItem(1, true);
        viewPager.setCurrentItem(0, true);
        setViewPagerOnclick();
    }


    /**
     * 点击事件的监听
     */
    private void setViewPagerOnclick() {
        mLlRecord.setOnClickListener(this);
        mImgRecord.setOnClickListener(this);
        mTvRecord.setOnClickListener(this);
        mLlPlay.setOnClickListener(this);
        mImgPlay.setOnClickListener(this);
        mTvPlay.setOnClickListener(this);
        mLlSet.setOnClickListener(this);
        mImgSet.setOnClickListener(this);
        mTvSet.setOnClickListener(this);
        mLlTool.setOnClickListener(this);
        mImgTool.setOnClickListener(this);
        mTvTool.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intent1 = intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!AppUtil.isAppForeground(ScriptApplication.getInstance())) {
            return;
        }
        initData();
        if (RecordFloatView.getInstance(getApplicationContext()).isShow()) {
            RecordFloatView.getInstance(getApplicationContext()).hide();
            RecordFloatView.getInstance(getApplicationContext()).reset();
        }
        Constants.RECORD_STATE = RecordEnum.STOP_RECORD;
        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
        Constants.EXEC_STATE = ExecEnum.EXEC_STOP;
        Constants.execServerScript = false;
        BaseOrderHelper.resetData();
        IMSocketClient.destroy();
        BIMSocketClient.destroy();
        NioTask.release();
        BrushTask.release();
        if ((Constants.RECORD_STATE == RecordEnum.STOP_RECORD || RecordFloatView.bigFloatState == RecordFloatView.COLLECT) && Constants.needSave) {
            // 弹窗
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            dialog = new PromptDialog(HomeMainActivity.this);
            editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
            if (RecordFloatView.bigFloatState == RecordFloatView.RECORD) {
            } else if (RecordFloatView.bigFloatState == RecordFloatView.COLLECT && Constants.COLLECT_STATE == CollectEnum.COLLECT_PHOTO) {
                dialog.setTitleText(Html.fromHtml(getResources().getString(R.string.dialog_pic_title)));
            }
            dialog.setOnPositiveListener(v -> {
                if (StringUtil.isEmpty(editText.getText().toString())) {
                    Toast.makeText(ScriptApplication.getInstance(), "文件名不能为空，无法进行保存。", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (RecordFloatView.bigFloatState == RecordFloatView.RECORD) {
                        checkScriptExits();
                    } else if (RecordFloatView.bigFloatState == RecordFloatView.COLLECT && Constants.COLLECT_STATE == CollectEnum.COLLECT_PHOTO) {
                        checkPictureExits();
                    }
                }
            });
            dialog.show();
        }


        if (dialog == null || (dialog != null && !dialog.isShowing())) {
            clearCollections();
        }

        if (checkFromBoot()) {
            //执行优化脚本
            new Thread(() -> {
                SystemClock.sleep(5000);
                if (!TextUtils.isEmpty(bootType)) {
                    if (ScriptConstants.PLATFORM_MANAGER.equals(bootType)) {
                        Constants.currentPlatform = PlatformConfig.getPlatform(PlatformConfig.ZHUXIAN_PACKAGE_NAME);
                    } else if (ScriptConstants.PLATFORM_BRUSH.equals(bootType)) {
                        Constants.currentPlatform = PlatformConfig.getPlatform(PlatformConfig.ASHES_PACKAGE_NAME);
                    }
                    RecordFloatView.bigFloatState = RecordFloatView.EXEC;
                    Constants.execServerScript = true;
                    AppUtil.runToBackground(HomeMainActivity.this);
                    HomeMainActivity.this.startService(new Intent(HomeMainActivity.this, FloatingService.class));
                    SystemClock.sleep(3000);

                    if (Constants.currentPlatform == PlatformConfig.getPlatform(PlatformConfig.ZHUXIAN_PACKAGE_NAME)) {
                        try {
                            String jsonStr = FileHelper.getFileString(Constants.MANAGER_ORDER_FOLDER_TEMP_PATH);
                            if (!StringUtil.isEmpty(jsonStr)) {
                                Constants.EXEC_STATE = ExecEnum.EXEC_START;
                                Constants.PLAY_STATE = PlayEnum.START_PLAY;
                                Constants.sGamesOrderInfo = ScriptApplication.getGson().fromJson(jsonStr, GamesOrderInfo.class);
                                NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_DEAL_WITH_ORDER, Constants.sGamesOrderInfo, 1000);
                            } else {
                                getNewOrder();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (Constants.currentPlatform == PlatformConfig.getPlatform(PlatformConfig.ASHES_PACKAGE_NAME)) {
                        try {
                            if (SpUtil.getKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, false)) {
                                String jsonStr = FileHelper.getFileString(Constants.BRUSH_ORDER_FOLDER_TEMP_PATH);
                                if (!StringUtil.isEmpty(jsonStr)) {
                                    Constants.EXEC_STATE = ExecEnum.EXEC_START;
                                    Constants.PLAY_STATE = PlayEnum.START_PLAY;
                                    Constants.sBrushOrderInfo = ScriptApplication.getGson().fromJson(jsonStr, BrushOrderInfo.class);
                                    BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_DEAL_WITH_ORDER, Constants.sBrushOrderInfo, 1000);
                                } else {
                                    getNewOrder();
                                }
                            } else {
                                getNewOrder();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }

    /**
     * 检测脚本是否已经存在
     */
    private void checkScriptExits() {
        if (new File(Constants.SCRIPT_FOLDER_PATH + "/" + editText.getText().toString() + ".txt").exists()) {
            dialog.hide();
            final PopupDialog popupDialog = new PopupDialog(HomeMainActivity.this, 2, false);
            popupDialog.setTitle("该脚本已存在,是否覆盖?");
            popupDialog.setOnNegativeListener(view -> {
                popupDialog.dismiss();
                dialog.show();
            });
            popupDialog.setOnPositiveListener(view -> {
                CommandUtil.saveScript(editText.getText().toString(), true);
                dialog.dismiss();
                popupDialog.dismiss();
            });
            popupDialog.show();
        } else {
            CommandUtil.saveScript(editText.getText().toString(), true);
        }
    }

    /**
     * 检测图片是否已经存在
     */
    private void checkPictureExits() {
        if (new File(Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH + "/" + editText.getText().toString() + ".png").exists()) {
            dialog.hide();
            final PopupDialog popupDialog = new PopupDialog(HomeMainActivity.this, 2, false);
            popupDialog.setTitle("该文件已存在,是否覆盖?");
            popupDialog.setOnNegativeListener(view -> {
                popupDialog.dismiss();
                dialog.show();
            });
            popupDialog.setOnPositiveListener(view -> {
                ImageUtil.saveBitmap(ScriptApplication.bitmapTemp, Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH + "/" + editText.getText().toString() + ".png", Bitmap.CompressFormat.PNG);
                dialog.dismiss();
                clearCollections();
                popupDialog.dismiss();
                Toast.makeText(HomeMainActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
            });
            popupDialog.show();
        } else {
            ImageUtil.saveBitmap(ScriptApplication.bitmapTemp, Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH + "/" + editText.getText().toString() + ".png", Bitmap.CompressFormat.PNG);
            dialog.dismiss();
            clearCollections();
            Toast.makeText(HomeMainActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 检测是不是来自重启
     *
     * @return
     */
    public boolean checkFromBoot() {
        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                boolean fromBoot = bundle.getBoolean("fromBoot");
                bootType = bundle.getString("bootType");
                this.setIntent(null);
                LLog.e("fromBoot===" + fromBoot);
                return fromBoot;
            }
        }
        return false;
    }

    /**
     * 获取新单子
     */
    public void getNewOrder() {
        runOnUiThread(() -> RecordFloatView.getInstance(ScriptApplication.getInstance()).initGetOrder());
    }


    /**
     * 清空数据集
     */
    public void clearCollections() {
        Constants.needSave = false;
        ScriptApplication.scriptInfoRecord.clear();
        ScriptApplication.floatPointList.clear();
        ScriptApplication.areaOrRoleScriptList.clear();
    }


    /**
     * 点击事件的响应
     *
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_record:
            case R.id.img_record:
            case R.id.tv_record:
                viewPager.setCurrentItem(0, true);
                break;

            case R.id.ll_play:
            case R.id.img_play:
            case R.id.tv_play:
                if (playFragment != null) {
                    playFragment.initAdapter();
                }
                viewPager.setCurrentItem(1, true);
                break;

            case R.id.ll_tool:
            case R.id.img_tool:
            case R.id.tv_tool:
                viewPager.setCurrentItem(2, true);
                break;

            case R.id.ll_set:
            case R.id.img_set:
            case R.id.tv_set:
                viewPager.setCurrentItem(3, true);
                break;
        }
    }

    public void saveCollectData(String fileName, Intent intent) {
        if (intent != null) {
            String filePath = "";
            String strData = intent.getExtras().getString("strData");  //存储的数据
            FileUtil.saveFile(strData, filePath);
        }
    }

    public void successSaveScript() {
        if (dialog != null) {
            dialog.dismiss();
        }
        clearCollections();
    }

    public void successSaveCollectData() {
        if (dialog != null) {
            dialog.dismiss();
        }
        clearCollections();
    }

    public void successClearScript() {
        if (dialog != null) {
            dialog.dismiss();
        }
        clearCollections();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - nowTime > 2000) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_quit_tip), Toast.LENGTH_SHORT).show();
            nowTime = System.currentTimeMillis();
            return;
        }
        stopService(new Intent(this, FloatingService.class));
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        ScreenShotFb.getInstance(ScriptApplication.getInstance()).closeObject();
        super.onDestroy();
    }


}

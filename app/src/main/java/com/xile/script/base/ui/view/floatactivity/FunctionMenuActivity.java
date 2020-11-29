package com.xile.script.base.ui.view.floatactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xile.script.adapter.FeaturesMenuAdapter;
import com.xile.script.adapter.OnRecycleItemClicklistener;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.TitleLayout;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.AppInfo;
import com.xile.script.bean.FeatureInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.PlayEnum;
import com.xile.script.config.RecordEnum;
import com.xile.script.config.SleepConfig;
import com.xile.script.service.FloatingService;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.InstructUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.script.CommandUtil;
import com.yzy.example.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import script.tools.config.ScriptConstants;


/**
 * date: 2017/3/28 19:05
 *
 * @scene: 功能菜单悬浮框
 */

public class FunctionMenuActivity extends BaseFloatActivity implements View.OnClickListener {
    public static FunctionMenuActivity instance;
    private List<FeatureInfo> mFunctionList = new ArrayList<>();
    private List<FeatureInfo> mFunctionItemList = new ArrayList<>();
    private List<AppInfo> appInfos = new ArrayList<>();
    private TitleLayout tl_title;
    private TextView tv_choose_function;
    private RecyclerView rcy_features;
    private Context mContext;
    private FeaturesMenuAdapter mMenuAdapter;
    private boolean isChildOpen = false;
    private int childItemNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.float_function_menu);
        mContext = this;
        instance = this;
        isFunctionMenuActivityShow = true;
        RecordFloatView.bigFloatState = RecordFloatView.RECORD;
        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
        if (Constants.RECORD_STATE == RecordEnum.PAUSE_RECORD) {
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu_pause_re), mContext.getResources().getString(R.string.text_function_restore_pause)));
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu_area), mContext.getResources().getString(R.string.text_function_insert_area)));
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu__role), mContext.getResources().getString(R.string.text_function_insert_role)));
        } else {
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu_applist), mContext.getResources().getString(R.string.text_function_applist)));
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu_pause), mContext.getResources().getString(R.string.text_function_pause)));
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu__capture), mContext.getResources().getString(R.string.text_function_capture)));
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu_area), mContext.getResources().getString(R.string.text_function_area)));
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu__role), mContext.getResources().getString(R.string.text_function_role)));
            mFunctionList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.feasure_menu_applist), mContext.getResources().getString(R.string.text_function_platform)));
            mFunctionItemList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.features_script), "诛仙脚本"));
            mFunctionItemList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.features_script), "诛仙区服"));
        }
        mMenuAdapter = new FeaturesMenuAdapter(this);
        mMenuAdapter.setObjects(mFunctionList);
        initView();
    }


    public void initView() {
        tl_title = this.findViewById(R.id.tl_tle);
        tl_title.setTitle(mContext.getResources().getString(R.string.text_function_title));
        tl_title.mBtnBack.setOnClickListener(this);
        tv_choose_function = this.findViewById(R.id.tv_choose_function);
        rcy_features = this.findViewById(R.id.rcy_features);
        rcy_features.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rcy_features.setAdapter(mMenuAdapter);
        rcy_features.addOnItemTouchListener(new OnRecycleItemClicklistener(rcy_features) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                if (!isChildOpen) {
                    if (!TextUtils.isEmpty(mFunctionList.get(viewHolder.getAdapterPosition()).getFunction())) {
                        switch (mFunctionList.get(viewHolder.getLayoutPosition()).getFunction()) {
                            case "应用列表":
                                isChildOpen = true;
                                childItemNum = 1;
                                tv_choose_function.setText(mContext.getResources().getString(R.string.text_choose_game));
                                appInfos = AppUtil.getAppInfoList(ScriptApplication.getInstance());
                                mFunctionItemList.clear();
                                for (int i = 0; i < appInfos.size(); i++) {
                                    mFunctionItemList.add(new FeatureInfo(appInfos.get(i).getIcon(), appInfos.get(i).getLabel()));
                                }
                                flushWindow();
                                break;

                            case "暂停":
                                Constants.RECORD_STATE = RecordEnum.PAUSE_RECORD;
                                closeFunctionMenu();
                                break;

                            case "插入截图动作":
                                CommandUtil.saveScript(ScriptConstants.CAPTURE_SCRIPT, true);
                                closeFunctionMenu();
                                RecordFloatView.updateMessage("截图成功");
                                break;

                            case "暂停并插入选区服":
                                isChildOpen = true;
                                childItemNum = 4;
                                tv_choose_function.setText(mContext.getResources().getString(R.string.text_choose));
                                initAreaOrRoleData(Constants.SCRIPT_AREA_PATH);
                                flushWindow();
                                break;

                            case "暂停并插入选角色":
                                isChildOpen = true;
                                childItemNum = 5;
                                tv_choose_function.setText(mContext.getResources().getString(R.string.text_choose));
                                initAreaOrRoleData(Constants.SCRIPT_ROLE_PATH);
                                flushWindow();
                                break;

                            case "暂停并插入登录平台":
                                isChildOpen = true;
                                childItemNum = 6;
                                mFunctionItemList.clear();
                                mFunctionItemList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.features_script), "老虎平台"));
                                tv_choose_function.setText(mContext.getResources().getString(R.string.text_choose));
                                flushWindow();
                                break;

                            case "恢复录制":
                                Constants.RECORD_STATE = RecordEnum.RECOVERY_RECORD;
                                closeFunctionMenu();
                                break;

                            case "插入区服":
                                isChildOpen = true;
                                childItemNum = 4;
                                tv_choose_function.setText(mContext.getResources().getString(R.string.text_choose));
                                initAreaOrRoleData(Constants.SCRIPT_AREA_PATH);
                                flushWindow();
                                break;

                            case "插入角色":
                                isChildOpen = true;
                                childItemNum = 5;
                                tv_choose_function.setText(mContext.getResources().getString(R.string.text_choose));
                                initAreaOrRoleData(Constants.SCRIPT_ROLE_PATH);
                                flushWindow();
                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    switch (childItemNum) {//子条目  待处理
                        case 1:
                            CommandUtil.saveScript(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT + SleepConfig.SLEEP_TIME_2000, true);
                            CommandUtil.saveScript(ScriptConstants.START_APP_SCRIPT + " " + appInfos.get(viewHolder.getLayoutPosition()).getLabel() + "(" + appInfos.get(viewHolder.getLayoutPosition()).getPackageName() + ")", true);
                            CommandUtil.saveScript(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT + SleepConfig.SLEEP_TIME_1000, true);
                            AppUtil.startApp(ScriptApplication.getInstance(), appInfos.get(viewHolder.getLayoutPosition()).getPackageName());
                            closeFunctionMenu();
                            break;

                        case 4:
                            String areaName = mFunctionItemList.get(viewHolder.getLayoutPosition()).getFunction();
                            CommandUtil.saveScript(ScriptConstants.INSERT_AREA_SCRIPT + ScriptConstants.SPLIT + areaName.substring(0, areaName.indexOf(".txt")), true);
                            CommandUtil.saveScript(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT + SleepConfig.SLEEP_TIME_1000, true);
                            Constants.RECORD_STATE = RecordEnum.PAUSE_RECORD;
                            insertAreaOrRoleData(Constants.SCRIPT_AREA_PATH + "/", mFunctionItemList.get(viewHolder.getLayoutPosition()).getFunction());
                            closeFunctionMenu();
                            RecordFloatView.updateMessage("插入区服成功");
                            break;

                        case 5:
                            String roleName = mFunctionItemList.get(viewHolder.getLayoutPosition()).getFunction();
                            CommandUtil.saveScript(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT + SleepConfig.SLEEP_TIME_1000, true);
                            CommandUtil.saveScript(ScriptConstants.INSERT_ROLE_SCRIPT + ScriptConstants.SPLIT + roleName.substring(0, roleName.indexOf(".txt")), true);
                            CommandUtil.saveScript(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT + SleepConfig.SLEEP_TIME_1000, true);
                            Constants.RECORD_STATE = RecordEnum.PAUSE_RECORD;
                            insertAreaOrRoleData(Constants.SCRIPT_ROLE_PATH + "/", mFunctionItemList.get(viewHolder.getLayoutPosition()).getFunction());
                            closeFunctionMenu();
                            RecordFloatView.updateMessage("插入角色成功");
                            break;

                        case 6:
                            CommandUtil.saveScript(ScriptConstants.INSERT_PLATFORM_SCRIPT + ScriptConstants.SPLIT + ScriptConstants.INSERT_PLATFORM_TIGER, true);
                            CommandUtil.saveScript(ScriptConstants.SLEEP_SCRIPT + ScriptConstants.SPLIT + SleepConfig.SLEEP_TIME_1000, true);
                            Constants.RECORD_STATE = RecordEnum.PAUSE_RECORD;
                            closeFunctionMenu();
                            RecordFloatView.updateMessage("插入平台成功");
                            break;

                        default:
                            break;

                    }

                }
            }

            @Override
            public void onLongClick(RecyclerView.ViewHolder viewHolder) {

            }
        });
    }


    /**
     * 刷新界面
     */
    public void flushWindow() {
        mMenuAdapter.setObjects(mFunctionItemList);
        mMenuAdapter.notifyDataSetChanged();
    }


    /**
     * 获取适配器
     *
     * @return adapter
     */
    public FeaturesMenuAdapter getMenuAdapter() {
        return mMenuAdapter;
    }

    /**
     * 初始化区服或角色脚本信息
     *
     * @param path 区服或角色存储路径
     */
    private void initAreaOrRoleData(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                mFunctionItemList.clear();
                for (File f : files) {
                    mFunctionItemList.add(new FeatureInfo(mContext.getResources().getDrawable(R.drawable.features_script), f.getName()));
                }
            }
        }
    }

    /**
     * 临时存储区服或角色信息
     *
     * @param name 区服或角色存储名字
     * @param path 区服或角色存储路径
     */
    private void insertAreaOrRoleData(String path, String name) {
        ScriptApplication.areaOrRoleScriptList.addAll(InstructUtil.getScriptList(FileHelper.readFile(path + name)));
    }

    /**
     * 获取功能菜单集合
     *
     * @return FunctionList
     */
    public List<FeatureInfo> getFunctionList() {
        return mFunctionList;
    }

    /**
     * 设置功能菜单集合
     *
     * @param functionList
     */
    public void setFunctionList(List<FeatureInfo> functionList) {
        mFunctionList = functionList;
    }

    /**
     * 获取功能菜单子条目集合
     *
     * @return FunctionItemList
     */
    public List<FeatureInfo> getFunctionItemList() {
        return mFunctionItemList;
    }

    /**
     * 设置功能菜单子条目集合
     *
     * @param functionItemList
     */
    public void setFunctionItemList(List<FeatureInfo> functionItemList) {
        mFunctionItemList = functionItemList;
    }


    /**
     * 回退状态逻辑处理
     */
    private void goBack() {
        if (isChildOpen) {
            isChildOpen = false;
            mMenuAdapter.setObjects(mFunctionList);
            mMenuAdapter.notifyDataSetChanged();
        } else {
            closeFunctionMenu();
        }
    }

    /**
     * 关闭功能菜单界面
     */
    private void closeFunctionMenu() {
        finish();
        instance = null;
        ScriptApplication.getInstance().startService(new Intent(ScriptApplication.getInstance(), FloatingService.class));
    }

    @Override
    public void onClick(View v) {
        goBack();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}

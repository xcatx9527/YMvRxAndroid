package com.xile.script.base.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xile.script.adapter.FeaturesMenuAdapter;
import com.xile.script.base.ScriptApplication;
import com.xile.script.bean.AppInfo;
import com.xile.script.bean.FeatureInfo;
import com.xile.script.utils.AppUtil;
import com.yzy.example.R;

import java.util.ArrayList;
import java.util.List;

/**
 * date: 2017/6/20 21:03
 */
public class AppInfoActivity extends BaseActivity {
    private List<AppInfo> appInfos = new ArrayList<>();
    private List<FeatureInfo> mFunctionList = new ArrayList<>();
    private FeaturesMenuAdapter mMenuAdapter;
    private RecyclerView rcy_features;

    @Override
    protected void setView() {
        super.setView();
        setContentView(R.layout.bund_layout);
        appInfos = AppUtil.getAppInfoList(ScriptApplication.app);
        for (int i = 0; i < appInfos.size(); i++) {
            mFunctionList.add(new FeatureInfo(appInfos.get(i).getIcon(), appInfos.get(i).getPackageName()));
        }
        mMenuAdapter = new FeaturesMenuAdapter(this);
        mMenuAdapter.setObjects(mFunctionList);

    }

    @Override
    protected void init() {
        super.init();
        rcy_features = this.findViewById(R.id.rcy_features);
        rcy_features.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcy_features.setAdapter(mMenuAdapter);
    }



}
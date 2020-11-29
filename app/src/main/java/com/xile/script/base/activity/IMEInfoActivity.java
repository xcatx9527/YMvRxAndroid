package com.xile.script.base.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xile.script.adapter.FeaturesMenuAdapter;
import com.xile.script.bean.FeatureInfo;
import com.xile.script.utils.IMEUtil;
import com.yzy.example.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * date: 2017/09/19 13:03
 */

public class IMEInfoActivity extends BaseActivity {
    private List<Map> imeNames = new ArrayList<>();
    private List<FeatureInfo> mFunctionList = new ArrayList<>();
    private FeaturesMenuAdapter mMenuAdapter;
    private RecyclerView rcy_features;

    @Override
    protected void setView() {
        super.setView();
        setContentView(R.layout.bund_layout);
        imeNames = IMEUtil.getInstance().getIME(this);
        for (int i = 0; i < imeNames.size(); i++) {
            mFunctionList.add(new FeatureInfo(null, (String) imeNames.get(i).get("IMEid")));
        }
        mMenuAdapter = new FeaturesMenuAdapter(this);
        mMenuAdapter.setObjects(mFunctionList);

    }

    @Override
    protected void init() {
        rcy_features = this.findViewById(R.id.rcy_features);
        rcy_features.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcy_features.setAdapter(mMenuAdapter);
    }

}
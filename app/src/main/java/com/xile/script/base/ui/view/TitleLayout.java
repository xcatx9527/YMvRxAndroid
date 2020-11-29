package com.xile.script.base.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xile.script.utils.common.StringUtil;
import com.yzy.example.R;


/**
 * 全局通用标题栏
 */
public class TitleLayout extends LinearLayout implements View.OnClickListener {
    public Button mBtnBack;//回退按钮
    private TextView mTvTitle;//中间标题
    private Button mBtnRight;//右边按钮

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_title_general, this);
        mBtnBack = findViewById(R.id.tl_back);
        mTvTitle = findViewById(R.id.tl_title);
        mBtnRight = findViewById(R.id.tl_more);
        mBtnBack.setOnClickListener(this);
        mBtnRight.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tl_back:
                ((Activity) getContext()).onBackPressed();
                break;

        }
    }

    public void setTitle(String title) {//设置中间标题-string
        if (!StringUtil.isNullOrEmpty(title)) {
            mTvTitle.setText(title);
        }
    }

    public void setTitle(int resId) {//设置中间标题-资源ID
        try {
            mTvTitle.setText(getResources().getString(resId));
        } catch (Resources.NotFoundException e) {
            Log.e("TitleLayout", "标题资源ID未找到");
        }
    }

    public void setTitleVisiable(boolean visibable) {//设置中间标题隐藏显示
        if (visibable) {
            mTvTitle.setVisibility(View.VISIBLE);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
    }

    public void setRightBtnText(String text) {//设置右边按钮文本-string
        if (!StringUtil.isNullOrEmpty(text)) {
            mBtnRight.setText(text);
        }
    }

    public void setRightBtnText(int resId) {//设置右边按钮文本-资源ID
        try {
            mBtnRight.setText(getResources().getString(resId));
        } catch (Resources.NotFoundException e) {
            Log.e("TitleLayout", "标题右边按钮文本资源ID未找到");
        }
    }


    public String getRightBtnText() {//设置右边按钮文本-资源ID
        if (!StringUtil.isNullOrEmpty(mBtnRight.getText().toString()))
            return mBtnRight.getText().toString();
        return "";
    }


    public void setRightBtnDrawable(int resId) {//设置右边按钮背景图
        mBtnRight.setBackgroundDrawable(getResources().getDrawable(resId));
    }

    public void setRightBtnVisiable(boolean visibable) {//设置右边按钮是否隐藏
        if (visibable) {
            mBtnRight.setVisibility(View.VISIBLE);
        } else {
            mBtnRight.setVisibility(View.GONE);
        }
    }

    public void setLeftBtnVisiable(boolean visibable) {//设置左边按钮是否隐藏
        if (visibable) {
            mBtnBack.setVisibility(View.VISIBLE);
        } else {
            mBtnBack.setVisibility(View.GONE);
        }
    }

    public void setRightBtnClick(OnClickListener listener) {//设置右边按钮的点击监听
        mBtnRight.setOnClickListener(listener);
    }

}

package com.xile.script.base.ui.view.cutimage;

import android.content.Context;

import com.xile.script.base.ScriptApplication;

/**
 * @author flk
 * @description 裁剪参数
 */
public class Options {
    //默认裁剪高度
    public int max_height = dp2px(ScriptApplication.getInstance(), 700);
    public int min_height = dp2px(ScriptApplication.getInstance(), 10);

    public int max_with = dp2px(ScriptApplication.getInstance(), 700);
    public int min_with = dp2px(ScriptApplication.getInstance(), 10);
    //默认可裁剪高度两边与屏幕边距
    public int paddingHeight = dp2px(ScriptApplication.getInstance(), 0);
    //默认可裁剪宽度两边与屏幕边距
    public int paddingWidth = dp2px(ScriptApplication.getInstance(), 0);
    //默认裁剪高度
    public int cilpHeight = dp2px(ScriptApplication.getInstance(), 150);
    //图标可点击范围
    public int iconClick = 50;

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

package com.xile.script.http.common;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/12.
 */

public class FileCallBack extends BaseHttpRequestCallback {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    protected void onSuccess(Object o) {
        super.onSuccess(o);
    }

    @Override
    public void onProgress(int progress, long networkSpeed, boolean done) {
        super.onProgress(progress, networkSpeed, done);
    }

    @Override
    public void onFailure(int errorCode, String msg) {
        super.onFailure(errorCode, msg);
    }
}

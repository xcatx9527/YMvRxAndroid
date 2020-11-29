package com.xile.script.http.common;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/12.
 * 文件相关的http请求
 */

public class FileHttpRequest {
    /**
     * 文件上传
     *
     * @param url
     * @param params
     * @param filecallBack
     */
    public void post(String url, RequestParams params, final FileCallBack filecallBack) {


        HttpRequest.post(url, params, new FileCallBack() {
            @Override
            protected void onSuccess(Object o) {
                super.onSuccess(o);
                filecallBack.onSuccess(o);
            }

            @Override
            public void onStart() {
                super.onStart();
                filecallBack.onStart();

            }

            @Override
            public void onFinish() {
                super.onFinish();
                filecallBack.onFinish();
            }


            @Override
            public void onProgress(int progress, long networkSpeed, boolean done) {
                super.onProgress(progress, networkSpeed, done);
                filecallBack.onProgress(progress, networkSpeed, done);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                filecallBack.onFailure(errorCode, msg);
            }
        });
    }
}

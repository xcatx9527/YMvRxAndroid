package com.xile.script.http.helper.other;

import android.content.Context;

import com.xile.script.http.common.FileCallBack;
import com.xile.script.http.common.FileHttpRequest;

import java.io.File;

import cn.finalteam.okhttpfinal.RequestParams;

/**
 * Created by chsh on 2017/5/10.
 */

public class UploadFileHelper {

    public UploadFileHelper() {

    }

    /**
     * 上传单个文件动作
     *
     * @param mContext 上下文
     * @param url      接口
     * @param file 要上传的文件名
     * @param listener 监听接口
     */
    public void uploadFileAction(final Context mContext, String url, File file, final OnUploadFileListener listener) {

        RequestParams requestParams = new RequestParams();
        requestParams.addFormDataPart("file", file);
        new FileHttpRequest().post(url, requestParams, new FileCallBack() {
            @Override
            protected void onSuccess(Object o) {
                listener.onSuccess(o);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.onFailure(errorCode, msg);
            }
        });

    }

    /**
     * 上传文件集合动作
     *
     * @param mContext  上下文
     * @param url       接口
     * @param requestParams 请求参数
     * @param listener  监听接口
     */
    public void uploadFileListAction(final Context mContext, String url, RequestParams requestParams, final OnUploadFileListListener listener) {
        new FileHttpRequest().post(url, requestParams, new FileCallBack() {
            @Override
            protected void onSuccess(Object o) {
                listener.onSuccess(o);

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.onFailure(errorCode, msg);
            }
        });

    }


    public interface OnUploadFileListener {
        void onSuccess(Object o);

        void onFailure(int errorCode, String msg);
    }

    public interface OnUploadFileListListener {
        void onSuccess(Object o);

        void onFailure(int errorCode, String msg);
    }
}

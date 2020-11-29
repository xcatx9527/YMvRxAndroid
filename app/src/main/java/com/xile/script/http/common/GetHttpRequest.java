package com.xile.script.http.common;

import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by zxf on 2016/8/26.
 */
public class GetHttpRequest {
    /**
     * 只有一个url
     *  @param url
     * @param jsonCallback
     */
    public void get(String url, final JsonCallback jsonCallback) {
        OkHttpUtils.get().url(url).build().execute(new JsonCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                jsonCallback.onError(call, e, id);
            }

            @Override
            public void onResponse(JSONObject response, int id) {
                super.onResponse(response, id);
                jsonCallback.onResponse(response, id);
            }
        });
    }

    /**
     * 带参数的get请求
     *
     * @param url
     * @param params
     * @param jsonCallback
     */
    public void get(String url, Map<String, String> params,
                    final JsonCallback jsonCallback) {
        OkHttpUtils.get().url(url).params(params).build().execute(new JsonCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                jsonCallback.onError(call, e, id);
            }

            @Override
            public void onResponse(JSONObject response, int id) {
                super.onResponse(response, id);
                jsonCallback.onResponse(response, id);
            }
        });
    }

    /**
     * 带参数的get请求
     *
     * @param url
     * @param params
     * @param stringCallback
     */
    public void getString(String url, Map<String, String> params,
                          final StringCallback stringCallback) {
        OkHttpUtils.get().url(url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                stringCallback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                stringCallback.onResponse(response, id);
            }
        });
    }


    /**
     * 带参数以及请求的get请求
     *
     * @param url
     * @param headers
     * @param params
     * @param jsonCallback
     */
    public void get(String url, Map<String, String> headers,
                    Map<String, String> params,
                    final JsonCallback jsonCallback) {
        OkHttpUtils.get().url(url).params(params).headers(headers).build().execute(new JsonCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                jsonCallback.onError(call, e, id);
            }

            @Override
            public void onResponse(JSONObject response, int id) {
                super.onResponse(response, id);
                jsonCallback.onResponse(response, id);
            }

        });
    }

    /**
     * 带请求头不带参数的的get请求
     *
     * @param url
     * @param headers
     * @param jsonCallback
     */
    public void getHeader(String url, Map<String, String> headers, final JsonCallback jsonCallback) {
        OkHttpUtils.get().url(url).headers(headers).build().execute(new JsonCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                jsonCallback.onError(call, e, id);
            }

            @Override
            public void onResponse(JSONObject response, int id) {
                super.onResponse(response, id);

                jsonCallback.onResponse(response, id);
            }

        });
    }
}

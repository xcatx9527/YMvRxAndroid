package com.xile.script.http.common;

import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/12.
 */

public class PostHttpRequest {

    private static PostHttpRequest httpRequest;

    private PostHttpRequest() {
    }

    public static PostHttpRequest getInstance() {
        if (httpRequest == null) {
            httpRequest = new PostHttpRequest();
        }
        return httpRequest;
    }


    /**
     * post 请求有参数
     *
     * @param url
     * @param params
     * @param jsonCallback
     */
    public void post(String url, Map<String, String> params,
                     final JsonCallback jsonCallback) {
        OkHttpUtils.post().url(url).params(params).build().execute(new JsonCallback() {
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
     * post 请求有参数
     *
     * @param url
     * @param params
     * @param stringCallback
     */
    public void post(String url, Map<String, String> params, final StringCallback stringCallback) {
        OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
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
     * post请求 string实体参数
     *
     * @param url
     * @param entity
     * @param contentType
     * @param jsonCallback
     */
    public void post(String url, String entity, String contentType, final JsonCallback jsonCallback) {
        OkHttpUtils.postString().url(url).content(entity).mediaType(MediaType.parse(contentType)).build().execute(new JsonCallback() {
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
     * post请求 带headers与params
     *
     * @param url
     * @param headers
     * @param params
     * @param jsonCallback
     */
    public void post(String url, Map<String, String> headers,
                     Map<String, String> params,
                     final JsonCallback jsonCallback) {
        OkHttpUtils.post().url(url).headers(headers).params(params).build().execute(new JsonCallback() {
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
     * postString 请求 带headers与实体
     *
     * @param url
     * @param headers
     * @param entity
     * @param contentType
     * @param jsonCallback
     */
    public void post(String url, Map<String, String> headers,
                     String entity, String contentType,
                     final JsonCallback jsonCallback) {


        OkHttpUtils.postString().url(url).headers(headers).content(entity).mediaType(MediaType.parse(contentType)).build().execute(new JsonCallback() {
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
     * postString 请求 带headers与实体
     *
     * @param url
     * @param headers
     * @param jsonCallback
     */
    public void postHead(String url, Map<String, String> headers,
                         String contentType,
                         final JsonCallback jsonCallback) {
        MediaType mediaType = MediaType.parse("utf-8");
        RequestBody body = RequestBody.create(mediaType, "");
        OkHttpUtils.postString().url(url).headers(headers).content(body.toString()).mediaType(MediaType.parse(contentType)).build().execute(new JsonCallback() {
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

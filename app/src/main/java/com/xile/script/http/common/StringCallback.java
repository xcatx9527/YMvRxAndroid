package com.xile.script.http.common;


import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/12.
 */
public class StringCallback extends Callback<String> {


    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        return string;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
    }

    @Override
    public void onResponse(String response, int id) {

    }


}

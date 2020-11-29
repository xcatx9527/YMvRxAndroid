package com.xile.script.http.common;


import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/12.
 */
public class JsonCallback extends Callback<JSONObject> {


    @Override
    public JSONObject parseNetworkResponse(Response response, int id) throws Exception {
        JSONObject jsonObject=new JSONObject(response.body().string());
        return jsonObject;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
    }

    @Override
    public void onResponse(JSONObject response, int id) {

    }
}

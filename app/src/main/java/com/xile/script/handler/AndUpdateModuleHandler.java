package com.xile.script.handler;

import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * date: 2017/12/8 15:01
 *
 * @scene AndServer  更新python模块执行结果
 */
public class AndUpdateModuleHandler implements RequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);

        // Request params.
        String orderId = params.get("orderId");
        String module = URLDecoder.decode(params.get("module"), "utf-8");

        LLog.i("Python Script -- current module:" + module + "   current orderId:" + orderId);

        if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(orderId)) {
            if (Constants.sBrushOrderInfo != null && orderId.equals(Constants.sBrushOrderInfo.getUserId())) {
                response.setEntity(new StringEntity("200", "utf-8"));
                BrushTask.getInstance().sendMessageObject(BrushTask.B_MSG_TYPE_UPDATE_MODULE, module, 1000);
            } else {
                response.setEntity(new StringEntity("405", "utf-8"));
                LLog.e("从Python脚本返回的orderId与内存中订单的orderId对应不上!" + " orderId:" + orderId);
                RecordFloatView.updateMessage("从Python脚本返回的orderId与内存中订单的orderId对应不上!" + " orderId:" + orderId);
            }
        } else {
            response.setEntity(new StringEntity("405", "utf-8"));
            LLog.e("从Python脚本返回的module或orderId为空!" + " module:" + module + " orderId:" + orderId);
            RecordFloatView.updateMessage("从Python脚本返回的module或orderId为空!" + " module:" + module + " orderId:" + orderId);
        }

    }
}

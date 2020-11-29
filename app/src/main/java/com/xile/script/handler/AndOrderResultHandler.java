package com.xile.script.handler;

import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.http.helper.brush.mina.BOrderStateConfig;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.utils.common.SpUtil;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;

/**
 * date: 2017/12/11 11:00
 *
 * @scene AndServer  更新python脚本订单执行结果
 */
public class AndOrderResultHandler implements RequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);

        // Request params.
        String orderId = params.get("orderId");
        String orderStatus = params.get("orderStatus");

        LLog.e("Python Script -- current orderId:" + orderId + "   current orderStatus:" + orderStatus);

        if (!TextUtils.isEmpty(orderStatus) && !TextUtils.isEmpty(orderId)) {
            if (Constants.sBrushOrderInfo != null && orderId.equals(Constants.sBrushOrderInfo.getUserId())) {
                if (BOrderStateConfig.ORDER_SUCCESS.equals(orderStatus)) {
                    response.setEntity(new StringEntity("200", "utf-8"));
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_SUCCESS);
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                    Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                } else if (BOrderStateConfig.ORDER_FAIL.equals(orderStatus)) {
                    response.setEntity(new StringEntity("200", "utf-8"));
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_FAIL);
                    SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "更新python脚本失败");
                    BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
                    Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
                } else {
                    response.setEntity(new StringEntity("405", "utf-8"));
                    LLog.e("从Python脚本返回的订单执行结果不正确!");
                    RecordFloatView.updateMessage("从Python脚本返回的订单执行结果不正确!");
                    BrushOrderHelper.getInstance().orderDealFailure("从Python脚本返回的订单执行结果不正确!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                }
            } else {
                response.setEntity(new StringEntity("405", "utf-8"));
                LLog.e("从Python脚本返回的orderId与内存中订单的orderId对应不上!" + " orderId:" + orderId);
                RecordFloatView.updateMessage("从Python脚本返回的orderId与内存中订单的orderId对应不上!" + " orderId:" + orderId);
                BrushOrderHelper.getInstance().orderDealFailure("从Python脚本返回的orderId与内存中订单的orderId对应不上!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
            }
        } else {
            response.setEntity(new StringEntity("405", "utf-8"));
            LLog.e("从Python脚本返回的orderStatus或orderId为空!" + " orderStatus:" + orderStatus + " orderId:" + orderId);
            RecordFloatView.updateMessage("从Python脚本返回的orderStatus或orderId为空!" + " orderStatus:" + orderStatus + " orderId:" + orderId);
            BrushOrderHelper.getInstance().orderDealFailure("从Python脚本返回的orderStatus或orderId为空!","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
        }

    }
}

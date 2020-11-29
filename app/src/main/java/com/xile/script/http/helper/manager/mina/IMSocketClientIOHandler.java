package com.xile.script.http.helper.manager.mina;

import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * date: 2017/5/23 10:42
 *
 * @scene MINA 连接监听
 */
public class IMSocketClientIOHandler extends IoHandlerAdapter {


    //会话创建时被触发
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
    }

    //会话开始时被触发
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        System.out.println("IMSocketClientIOHandler.sessionOpened");
    }

    //会话关闭时被触发
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        LLog.e("IMSocketClientIOHandler.sessionClosed");
        RecordFloatView.updateMessage("会话已关闭!");
        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_MINA_RESET, 5000);
    }

    //发送消息时被触发
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    //接收到消息后被触发
    @Override
    public void messageReceived(IoSession session, Object message)  {
        System.out.println("IMSocketClientIOHandler.messageReceived");
        if (message != null) {
            try {
                LLog.e("接收到消息 message:" + message);
                JSONObject jsonObject = new JSONObject(message.toString());
                String type = jsonObject.optString("type");
                if (!TextUtils.isEmpty(type)) {
                    LLog.e("接收到消息 type:" + type);
                    NioHelper.getInstance().getMessage(type, jsonObject);
                } else {
                    LLog.e("接收到消息 type为NULL！");
                    RecordFloatView.updateMessage("接收消息 message type为NULL！");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LLog.e("接收消息 message 时出现异常！");
                RecordFloatView.updateMessage("接收消息 message 时出现异常！");
            }
        } else {
            LLog.e("接收消息 message 为NULL！");
            RecordFloatView.updateMessage("接收消息 message 为NULL！");
        }

    }

    //接口中其他方法抛出异常未被捕获时触发此方法
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        LLog.e("IMSocketClientIOHandler.exceptionCaught");
        NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_MINA_RESET, 15000);
    }


}

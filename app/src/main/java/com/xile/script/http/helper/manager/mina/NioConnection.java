package com.xile.script.http.helper.manager.mina;

import android.os.SystemClock;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.ExecEnum;

/**
 * date: 2017/8/17 17:06
 *
 * @scene Mina连接
 */
public class NioConnection extends Thread {
    public static boolean needReconnect = false;//是否需要重连

    public NioConnection() {
        needReconnect = true;
    }

    @Override
    public void run() {
        super.run();
        while (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
            if (needReconnect) {
                LLog.e("初始化Mina");
                if (NioHelper.getInstance().imSocketClient == null) {
                    NioHelper.getInstance().imSocketClient = new IMSocketClient(NioHelper.getInstance().nioServerIp, NioHelper.getInstance().nioServerPort);
                    LLog.e("重新初始化Mina imSocketClient为空！");
                }
                if (NioHelper.getInstance().imSocketClient != null) {
                    if (NioHelper.getInstance().connectFuture == null) {
                        LLog.e("重新初始化Mina connectFuture为空！");
                        NioHelper.getInstance().connectFuture = NioHelper.getInstance().imSocketClient.socketConnect();
                    }
                    if (NioHelper.getInstance().connectFuture != null) {
                        if (!NioHelper.getInstance().connectFuture.isConnected() || !NioHelper.getInstance().connectFuture.getSession().isConnected()) {
                            LLog.e("Mina未连接成功,将继续重连!");
                            NioHelper.getInstance().connectFuture = NioHelper.getInstance().imSocketClient.socketConnect();
                            needReconnect = false;
                            NioTask.getInstance().sendMessage(NioTask.MSG_TYPE_MINA_RESET, 10000);
                        } else {
                            needReconnect = false;
                        }
                    }
                }
            } else {
                SystemClock.sleep( 15 * 1000);
            }
        }
    }

}

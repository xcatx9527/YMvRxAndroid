package com.xile.script.http.helper.brush.mina;

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
public class BNioConnection extends Thread {
    public static boolean b_needReconnect = false;//是否需要重连

    public BNioConnection() {
        b_needReconnect = true;
    }

    @Override
    public void run() {
        super.run();
        while (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
            if (b_needReconnect) {
                LLog.e("优化 - 初始化Mina");
                if (BrushHelper.getInstance().mBIMSocketClient == null) {
                    BrushHelper.getInstance().mBIMSocketClient = new BIMSocketClient(BrushHelper.getInstance().brushServerIp, BrushHelper.getInstance().brushServerPort);
                    LLog.e("优化 - 重新初始化Mina imSocketClient为空！");
                }
                if (BrushHelper.getInstance().mBIMSocketClient != null) {
                    if (BrushHelper.getInstance().mBConnectFuture == null) {
                        LLog.e("优化 - 重新初始化Mina connectFuture为空！");
                        BrushHelper.getInstance().mBConnectFuture = BrushHelper.getInstance().mBIMSocketClient.socketConnect();
                    }
                    if (BrushHelper.getInstance().mBConnectFuture != null) {
                        if (!BrushHelper.getInstance().mBConnectFuture.isConnected() || !BrushHelper.getInstance().mBConnectFuture.getSession().isConnected()) {
                            LLog.e("优化 - Mina未连接成功,将继续重连!");
                            BrushHelper.getInstance().mBConnectFuture = BrushHelper.getInstance().mBIMSocketClient.socketConnect();
                            b_needReconnect = false;
                            BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_MINA_RESET, 10000);
                        } else {
                            b_needReconnect = false;
                        }
                    }
                }
            } else {
                SystemClock.sleep( 15 * 1000);
            }
        }
    }

}

package com.xile.script.utils.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.config.Constants;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.script.SocketUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

/**
 * Created by chsh on 2017/7/3.
 * APK静默安装与卸载 管理类
 */

public class ApkOperateManager {

    public static Handler APKHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!TextUtils.isEmpty((CharSequence) msg.obj)) {
                        String content = (String) msg.obj;
                        Toast.makeText(ScriptApplication.getInstance(), content, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * 静默安装需要root权限
     *
     * @param appPath
     */
    public static void installSlient(String appPath) {
        String cmd = "pm install -r " + appPath;
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            process.waitFor();
            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
            if ("Success".equals(successMsg.toString())) {
                SocketUtil.continueExec();//继续播放脚本
            } else {
                if (errorMsg.toString().contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
                    CMDUtil.execShell("reboot");
                }
                if (Constants.execServerScript) {
                    BrushOrderHelper.getInstance().orderDealFailure("静默安装失败","执行时间 ："+ ((System.currentTimeMillis() - Constants.getOrderTime)/1000)+"s");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //显示结果

        Message message = Message.obtain();
        message.what = 1;
        message.obj = "成功消息：" + successMsg != null ? successMsg.toString() : "" + "\n" + "错误消息: " + errorMsg != null ? errorMsg.toString() : "";
        LLog.i("成功消息：" + successMsg != null ? successMsg.toString() : "" + "\n" + "错误消息: " + errorMsg != null ? errorMsg.toString() : "");
        if (APKHandler != null) {
            APKHandler.sendMessage(message);
        }
    }


    /**
     * 静默卸载需要root权限
     *
     * @param packName
     */
    public static void uninstallSlient(String packName) {
        String cmd = "pm uninstall " + packName;
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //卸载也需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            process.waitFor();
            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
//            if ("Success".equals(successMsg.toString())){
//                SocketUtil.continueExec();//继续播放脚本
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //显示结果
        Message message = Message.obtain();
        message.what = 1;
        message.obj = "成功消息：" + successMsg.toString() + "\n" + "错误消息: " + errorMsg.toString();
        APKHandler.sendMessage(message);
    }
}

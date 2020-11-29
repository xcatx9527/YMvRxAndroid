package com.xile.script.utils.appupdate;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.appupdate.util.StorageUtils;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.script.ScriptUtil;
import com.yzy.example.BuildConfig;
import com.yzy.example.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chsh on 2018/1/2.
 */

public class DownloadService extends IntentService {
    public static DownloadService instance;
    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K
    private static final String TAG = "DownloadService";
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private boolean empty;
    private static int num;
    private static int maxNum;

    public DownloadService() {
        super("DownloadService");
    }

    public static synchronized DownloadService getInstance() {
        if (instance == null) {
            instance = new DownloadService();
        }
        return instance;
    }

    public void setNum(int num) {
        DownloadService.num = num;
    }

    public void setMaxNum(int maxNum) {
        DownloadService.maxNum = maxNum;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        //String appName = getString(getApplicationInfo().labelRes);
        int icon = getApplicationInfo().icon;
        String urlStr = intent.getStringExtra(Constants.APK_DOWNLOAD_URL);
        String title = intent.getStringExtra(Constants.APK_DOWNLOAD_TITLE);
        String type = intent.getStringExtra(Constants.APK_DOWNLOAD_TYPE);
        boolean autoUpdate = intent.getBooleanExtra(Constants.APK_DOWNLOAD_AUTO, false);
        String appName = type;
        empty = TextUtils.isEmpty(title);
        appName = empty ? appName : title;
        mBuilder.setContentTitle(appName).setSmallIcon(icon);

        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            urlConnection.connect();
            long bytetotal = urlConnection.getContentLength();
            long bytesum = 0;
            int byteread = 0;
            in = urlConnection.getInputStream();
            File dir = StorageUtils.getCacheDirectory(this);
            String apkName = type + ".apk";
            File apkFile = new File(dir, apkName);
            out = new FileOutputStream(apkFile);
            byte[] buffer = new byte[BUFFER_SIZE];

            int oldProgress = 0;

            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);

                int progress = (int) (bytesum * 100L / bytetotal);
                // 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
                if (progress != oldProgress) {
                    updateProgress(progress);
                }
                oldProgress = progress;
            }
            // 下载完成
            mBuilder.setContentText(getString(R.string.download_success)).setProgress(0, 0, false);

            Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);
            //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            if (!autoUpdate) {
                if ("script".equals(type) || "deviceHook".equals(type) || "file".equals(type)) {
                    // 判断版本大于等于7.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        installAPKIntent.setDataAndType(FileProvider.getUriForFile(ScriptApplication.getInstance(), BuildConfig.APPLICATION_ID + ".provider", apkFile), "application/vnd.android.package-archive");
                        installAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        installAPKIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        installAPKIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    startActivity(installAPKIntent);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
                    mBuilder.setContentIntent(pendingIntent);
                    Notification noti = mBuilder.build();
                    noti.flags = Notification.FLAG_AUTO_CANCEL;
                    mNotifyManager.notify(0, noti);
                    mNotifyManager.cancelAll();
                    if (empty) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                } else if ("systemvpn".equals(type)) {
                    CMDUtil.execShell("mkdir -p /system/priv-app/SystemVPN");
                    if (!CMDUtil.execShellWaitFor("cp -fr " + Environment.getExternalStorageDirectory() + "/Android/data/" + BuildConfig.APPLICATION_ID + "/cache/download/" + apkFile.getName() + " /system/priv-app/SystemVPN/SystemVPN.apk")) {
                        RecordFloatView.updateMessage("系统推送失败，请重试！");
                        return;
                    }
                    CMDUtil.execShell("chmod -R 777 /system/priv-app/SystemVPN");
                    DownloadService.dealWithXpose();
                }
            } else {
                num++;
                LLog.i("开始安装文件:" + apkName);
                if (num >= maxNum) {
                    toReboot();
                }
                if (installSuccess(apkFile.getAbsolutePath())) {
                    LLog.i("文件安装成功:" + apkName);
                    if ((num >= maxNum) && "file.apk".equals(apkName)) {
                        toReboot();
                    }
                }
            }
        } catch (Exception e) {
            LLog.e("download apk file error !   " + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 静默安装需要root权限
     *
     * @param appPath
     */
    private boolean installSuccess(String appPath) {
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

            String message = "成功消息：" + successMsg != null ? successMsg.toString() : "" + "\n" + "错误消息: " + errorMsg != null ? errorMsg.toString() : "";
            LLog.i(message);

            if ("Success".equals(successMsg.toString())) {
                return true;
            } else {
                FileHelper.deletCacheFile();
                FileHelper.deleteFolderFile(com.xile.script.config.Constants.SCRIPT_FOLDER_PATH);
                ScriptUtil.dealWithUninstallOtherApp();
                SystemClock.sleep(2000);
                CMDUtil.execShell("reboot");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
                return false;
            }
        }
    }

    private void toReboot() {
        SystemClock.sleep(2000);
        AppUtil.startAppByADB(ScriptApplication.getInstance(), "com.filesync.manual");
        SystemClock.sleep(2000);
        LLog.i("开启远程通信--->FileSync");
    }

    public static void dealWithXpose() {
        /*AppUtil.killApp("de.robv.android.xposed.installer");
        SystemClock.sleep(2000);
        String disabled_modules = "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>";
        ScriptUtil.dealWithGetOwn("获取所有者#/data/data/#de.robv.android.xposed.installer");
        CMDUtil.execCommand(new String[]{"echo \"" + disabled_modules + "\" > " + "/data/data/de.robv.android.xposed.installer/shared_prefs/enabled_modules.xml"}, true, false);
        CMDUtil.execShell("chmod -R 0660 /data/data/de.robv.android.xposed.installer/shared_prefs/enabled_modules.xml");
        ScriptUtil.dealWithModifyOwn("更改所有者#/data/data/#de.robv.android.xposed.installer");
        SystemClock.sleep(3000);
        AppUtil.startAppByADB(ScriptApplication.getInstance(),"de.robv.android.xposed.installer");
        SystemClock.sleep(5000);

        AppUtil.killApp("de.robv.android.xposed.installer");
        SystemClock.sleep(2000);
        String enabled_modules = "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
                "<map>\n" +
                "    <int name=\\\"com.pyler.xinternalsd\\\" value=\\\"1\\\" />\n" +
                "    <int name=\\\"com.example.devicehook\\\" value=\\\"1\\\" />\n" +
                "</map>";
        ScriptUtil.dealWithGetOwn("获取所有者#/data/data/#de.robv.android.xposed.installer");
        CMDUtil.execCommand(new String[]{"echo \"" + enabled_modules + "\" > " + "/data/data/de.robv.android.xposed.installer/shared_prefs/enabled_modules.xml"}, true, false);
        CMDUtil.execShell("chmod -R 0660 /data/data/de.robv.android.xposed.installer/shared_prefs/enabled_modules.xml");
        ScriptUtil.dealWithModifyOwn("更改所有者#/data/data/#de.robv.android.xposed.installer");
        SystemClock.sleep(3000);
        AppUtil.startAppByADB(ScriptApplication.getInstance(),"de.robv.android.xposed.installer");
        SystemClock.sleep(5000);*/

        ScriptApplication.getService().execute(() -> {
            SystemClock.sleep(2000);
            AppUtil.startAppByADB(ScriptApplication.getInstance(), "de.robv.android.xposed.installer");
            SystemClock.sleep(5000);
            CMDUtil.execShell("input swipe 10 600 800 600 1500");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 540 290");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 968 317");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 960 540");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 968 317");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 968 540");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input swipe 10 600 800 600 1500");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 540 140");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 1017 138");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 740 145");
            SystemClock.sleep(3000);
            CMDUtil.execShell("input tap 835 1095");
            SystemClock.sleep(5000);
            CMDUtil.execShell("reboot");
        });

    }

    @SuppressLint("StringFormatInvalid")
    private void updateProgress(int progress) {
        //"正在下载:" + progress + "%"
        mBuilder.setContentText(this.getString(R.string.download_progress, progress) + " " + progress + "%").setProgress(100, progress, false);
        //setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);//PendingIntent.FLAG_CANCEL_CURRENT
        mBuilder.setContentIntent(pendingintent);
        mNotifyManager.notify(0, mBuilder.build());
    }

}

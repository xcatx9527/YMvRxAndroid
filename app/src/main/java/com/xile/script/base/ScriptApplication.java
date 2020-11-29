package com.xile.script.base;

import android.graphics.Bitmap;

import com.chenyang.lloglib.LLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.xile.script.bean.InstructInfo;
import com.xile.script.bean.ScriptInfo;
import com.xile.script.config.Constants;
import com.xile.script.utils.common.AssetsUtil;
import com.xile.script.utils.common.FileHelper;
import com.yzy.example.app.App;
import com.zhima.proxy.ZMProxy;
import com.zhy.http.okhttp.BuildConfig;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import script.tools.EventUtil;
import script.tools.config.LogConfig;


public class ScriptApplication  {
    public static List<InstructInfo> instructInfoList = new ArrayList<InstructInfo>();  //指令集合
    public static List<ScriptInfo> scriptInfoRecord = new ArrayList<>();  //最终保存的指令合集
    public static List<String> floatPointList = new ArrayList<String>();   //过滤点的集合
    public static List<ScriptInfo> areaOrRoleScriptList = new ArrayList<>();  //临时存取区服或角色数据集合
    private static ExecutorService service;  //线程池
    private static Gson gson;
    public static OkHttpClient okHttpClient;
    public static Bitmap bitmapTemp;
    private static final String APP_KEY = "1499680504909349";//芝麻SDK KEY
    private static final String APP_SECRET = "a0dd2b20600f82da02dc7a56613de2e8";//芝麻SDK SECRET
    public static App app;

    /**
     * ScriptApplication单例
     *
     * @return instance
     */
    public static synchronized App getInstance() {
        return app;
    }

    /**
     * 线程池单例
     *
     * @return service
     */
    public static ExecutorService getService() {
        if (service == null) {
            synchronized (ScriptApplication.class) {
                if (service == null) {
                    service = Executors.newFixedThreadPool(100);
                }
            }
        }
        return service;
    }

    public static void init(App app) {
        ScriptApplication.app = app;
        //initLeakCanary();
//        adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -c android.intent.category.HOME -n com.xile.script/com.xile.script.receiver.BootReceiver

        initOkHttp();
        initZMProxy();
        initBugly();
        makeDirectory();
        LLog.initLLog(BuildConfig.DEBUG, false, "##", "script");
        LogConfig.DEBUG_MODE = true;
        EventUtil.initEventPermission();
//        AssetsUtil.checkAndInstallAPP();//安装假app
        AssetsUtil.checkAndPushRecord();
        try {
            int c = Integer.TYPE.newInstance();
            LLog.e("------------------->" + c);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        // TODO: 2020/10/11 暂时关闭两个服务
       /* if (!ServiceUtil.isServiceWork(this, "com.xile.script.service.CheckRebootService")) {
            startService(new Intent(this, CheckRebootService.class));
        }
        if (!ServiceUtil.isServiceWork(this, "com.xile.script.service.UploadKeychainService")) {
            startService(new Intent(this, UploadKeychainService.class));
        }
*/

    }


    /**
     * LeakCanary初始化
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(ScriptApplication.app)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(ScriptApplication.app);
    }

    /**
     * OkHttp初始化
     */
    private static void initOkHttp() {
        //OkHttpUtils初始化
        okHttpClient = new OkHttpClient.Builder().connectTimeout(20000L, TimeUnit.MILLISECONDS).readTimeout(20000L, TimeUnit.MILLISECONDS).writeTimeout(20000L, TimeUnit.MILLISECONDS).build();
        OkHttpUtils.initClient(okHttpClient);
        //OkHttpFinal初始化
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        builder.setTimeout(40000L);
        OkHttpFinal.getInstance().init(builder.build());
    }

    /**
     * 芝麻VPN初始化
     */
    private static void initZMProxy() {
        ZMProxy.getInstance().init(ScriptApplication.app, APP_KEY, APP_SECRET);
        ZMProxy.getInstance().getVersionName();
    }

    /**
     * Bugly初始化
     */
    private static void initBugly() {
        CrashReport.initCrashReport(ScriptApplication.app, "a8437a5e9d", false);
    }

    public static synchronized List<InstructInfo> getInstructInfoList() {
        if (instructInfoList == null) {
            instructInfoList = new ArrayList<>();
        }
        return instructInfoList;
    }




    /**
     * 初始化Gson
     */
    public static synchronized Gson getGson() {
        if (gson == null) {
            //gson = new Gson();
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
        }
        return gson;
    }

    /**
     * 取消所有网络请求
     */
    public static void cancelAll() {
        if (okHttpClient == null) return;
        try {
            for (Call call : okHttpClient.dispatcher().queuedCalls()) {
                call.cancel();
            }
            for (Call call : okHttpClient.dispatcher().runningCalls()) {
                call.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void makeDirectory() {
        FileHelper.makeRootDirectory(Constants.INSTALL_APP_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_EXCEL_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_MATCH_PATH);
        FileHelper.makeRootDirectory(Constants.CERT_FOLDER_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_ROOT_POP_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_POP_LOCAL_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_ALERT_LOCAL_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_PLATFORM_LOCAL_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_COMPARE_LOCAL_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_SAMPLE_LOCAL_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_MODULE_LOCAL_IMG_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_COMMENT_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_LOCAL_COMMENT_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_TEMP_FILE_DOWNLOAD_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_GAME_APK_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_GAME_RESOURCE_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_APPLICATION_DATA_PATH);
        FileHelper.makeRootDirectory(Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_UPLOAD_PATH);

    }

}

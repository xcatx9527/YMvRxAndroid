package com.xile.script.utils.common;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.config.Constants;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * date: 2018/4/26 11:15
 */
public class AssetsUtil {
    public static List<String> appNamesList = new ArrayList<>();   //需要安装的应用的集合

    static {
        String[] appNames = new String[]{"应用宝.apk", "京东.apk", "今日头条.apk", "钉钉.apk", "拉勾.apk", "中国农业银行.apk", "蚂蚁财富.apk", "高德地图.apk", "百度网盘.apk", "北京移动.apk",
                "薄荷健康.apk", "唱吧.apk", "Keep.apk", "会分期.apk", "货拉拉.apk", "猎聘同道.apk", "摩拜单车.apk", "网易云音乐.apk", "夸克浏览器.apk", "滴滴出行.apk", "美团.apk", "手机淘宝.apk",
                "飞猪.apk", "脉脉.apk", "QQ音乐.apk", "同城.apk", "迅雷.apk", "喜马拉雅FM.apk", "优酷.apk", "知乎.apk", "微信.apk", "支付宝.apk", "QQ.apk"};
        appNamesList.addAll(Arrays.asList(appNames));
    }


    public static void checkAndInstallAPP() {
        ScriptApplication.getService().execute(() -> {
            try {
                for (String name : AssetsUtil.appNamesList) {
                    if (!new File(Constants.INSTALL_APP_PATH + "/" + name).exists()) {
                        FileHelper.copyAssetFile(name, Constants.INSTALL_APP_PATH + "/" + name);
                    }
                }
                List<String> appNames = FileHelper.getFileNames(Constants.INSTALL_APP_PATH);
                if (appNames != null && appNames.size() > 0) {
                    PackageManager packageManager = ScriptApplication.getInstance().getPackageManager();
                    List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
                    List<String> installedAppNames = new ArrayList<>();
                    for (PackageInfo info : packageInfos) {
                        if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                            installedAppNames.add(info.applicationInfo.loadLabel(packageManager).toString() + ".apk");
                        }
                    }
                    for (String name : appNames) {
                        if (!installedAppNames.contains(name)) {
                            ApkOperateManager.installSlient(Constants.INSTALL_APP_PATH + "/" + name);
                        }
                    }
                } else {
                    LLog.e("APP列表安装失败...");
                }
            } catch (Exception e) {
                e.printStackTrace();
                LLog.e("APP列表安装失败...");
            }
        });
    }

    public static void checkAndPushRecord() {
        ScriptApplication.getService().execute(() -> {
            String[] recordFile = new String[]{"init", "main_exec"};
            for (String rFileName : recordFile) {
                if (!FileUtil.isFileExistRoot(Constants.RECORD_FILE_PATH + rFileName)) {
                    FileHelper.copyAssetFile("record/" + rFileName, Constants.RECORD_FILE_PATH + rFileName);
                    CMDUtil.execShell("cp  -R -f " + Constants.RECORD_FILE_PATH + rFileName + " " + "/data/local/tmp/" + rFileName);
                    CMDUtil.execShell("chmod 777 /data/local/tmp/" + rFileName);
                } else {
                    CMDUtil.execShell("chmod 777 /data/local/tmp/" + rFileName);
                }
            }
        });
    }

}

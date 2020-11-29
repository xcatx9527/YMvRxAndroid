package com.xile.script.utils.appupdate.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.gson.Gson;

import com.xile.script.http.common.GetHttpRequest;
import com.xile.script.http.common.HttpConstants;
import com.xile.script.http.common.StringCallback;
import com.xile.script.utils.appupdate.SystemUpdate;
import com.xile.script.utils.appupdate.UpdateDialog;
import com.xile.script.bean.CheckForUpdateBean;
import com.xile.script.utils.common.StringUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 应用版本更新工具类
 * Created by chsh on 2018/1/2.
 */

public class UpdateUtils {

    public UpdateUtils() {

    }

    /**
     * 检查更新
     * <p>
     */
    public static void changeUpdateVersion(final Context context, final String type) {
        Map params = new HashMap();

        params.put("type", type);

        GetHttpRequest request = new GetHttpRequest();
        request.getString(HttpConstants.UPDATA_PACKAGE, params, new StringCallback() {
            @Override
            public void onResponse(String versionJson, int id) {
                try {
                    String packageName = "";
                    if ("script".equals(type)) {
                        packageName = context.getPackageName();
                    } else if ("systemvpn".equals(type)) {
                        packageName = "com.systemvpn";
                    }
                    String versionName = "0";
                    try {
                        versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    CheckForUpdateBean bean = gson.fromJson(versionJson, CheckForUpdateBean.class);
                    int code1 = bean.getCode();

                    if (code1 == 0) {
                        final String log = bean.getChangelog();
                        final String url = bean.getInstallUrl();
                        String version = bean.getVersion();
                        if (Integer.parseInt(StringUtil.getNumber(version)) > Integer.parseInt(StringUtil.getNumber(versionName))) {
                            UpdateDialog dialog = new UpdateDialog(context, log,
                                    "更新", "取消", new UpdateDialog.OnInputMileageChanged() {
                                @Override
                                public void onConfirm() {
                                    SystemUpdate.showNotification(context, log, url, false, type, false);
                                }

                                @Override
                                public void onCancel() {

                                }
                            });

                            dialog.show();
                        } else {
                            Toast.makeText(context, "已是最新版本", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "获取版本信息出现错误" + '\n' + "code==" + code1, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(context, "获取版本信息出现错误" + '\n' + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                Toast.makeText(context, "获取版本信息失败,请检查网络连接" + '\n' + e, Toast.LENGTH_LONG).show();
            }
        });
    }
}

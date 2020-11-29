package com.xile.script.utils.script;

import android.content.Context;
import android.os.Build;

import com.xile.script.bean.DeviceInfo;

import java.util.HashMap;

/**
 * date: 2017/5/9 19:46
 *
 * @scene 用于Hook设置设备信息
 */
public class HookUtil {

    /**
     * @param packageName       包名
     * @param deviceConfigMap   设备配置
     * @param randomDeviceParam 是否随机设备信息
     * @param context           上下文
     */

    public static void getDeviceInfo(String packageName, HashMap<String, Integer> deviceConfigMap, boolean randomDeviceParam, Context context) {

    }


    /**
     * @return RealSDK
     */
    public static Object[] getRealSDK() {
        return new Object[]{Build.VERSION.RELEASE, Build.VERSION.SDK, Build.VERSION.SDK_INT};
    }

    public static void setDeviceInfo(String packageName, DeviceInfo deviceInfo) {

    }


}

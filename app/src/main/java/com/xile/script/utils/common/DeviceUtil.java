package com.xile.script.utils.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.bean.DeviceInfo;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.ScreenUtil;
import com.xile.script.utils.script.HookUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * on 2017/5/6 10:52
 */
public class DeviceUtil {
    private static final String TAG = "Device:";
    /**
     * Unknown network class
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;

    /**
     * wifi net work
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * "2G" networks
     */
    public static final int NETWORK_CLASS_2_G = 2;

    /**
     * "3G" networks
     */
    public static final int NETWORK_CLASS_3_G = 3;

    /**
     * "4G" networks
     */
    public static final int NETWORK_CLASS_4_G = 4;


    /**
     * 获取Android Id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 通过反射获取IMEI
     *
     * @param context
     * @return
     */
    public static void getIMEIByReflex(Context context) {
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method TelephonyManager_private_getSubscriberInfo = tel.getClass().getDeclaredMethod("getSubscriberInfo");
            TelephonyManager_private_getSubscriberInfo.setAccessible(true);

            Object iphonesubinfo = TelephonyManager_private_getSubscriberInfo.invoke(tel);
            LLog.e(TAG, iphonesubinfo.toString());

            Method getDeviceId = iphonesubinfo.getClass().getDeclaredMethod("getDeviceId");
            LLog.e(TAG, getDeviceId.toString());
            getDeviceId.setAccessible(true);

            Object deviceid = null;
            deviceid = getDeviceId.invoke(iphonesubinfo);
            LLog.e(TAG, "deviceid:" + deviceid.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获取当前网络连接的类型信息
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (null != context) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (null != mNetworkInfo && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }


    public static int getNetWorkStatus(Context context) {
        int netWorkType = NETWORK_CLASS_UNKNOWN;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetWorkClass(context);
            }
        }

        return netWorkType;
    }


    public static int getNetWorkClass(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;

            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * 获取WIFI名称
     *
     * @param context
     * @return
     */
    public static String getWifiName(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiName = info != null ? info.getSSID() : null;
        return wifiName;
    }

    /**
     * 获取路由器的mac
     *
     * @param context
     * @return
     */
    public static String getBSSID(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String bssId = info != null ? info.getBSSID() : null;
        return bssId;
    }


    /**
     * 获取MAC地址 6.0以前
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * 获取MAC地址 6.0以后
     *
     * @return
     */
    public static String getHardwareAddress() {
        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    String hardwareAddress = displayMac(mac);
                    LLog.e("hardwareAddress:" + hardwareAddress);
                    return Arrays.toString(mac).replace("[", "").replace("]", "").replace(" ", "");
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static String displayMac(byte[] mac) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            byte b = mac[i];
            int intValue = 0;
            if (b >= 0) intValue = b;
            else intValue = 256 + b;
            String value = Integer.toHexString(intValue);
            if (value.length() == 1)
                stringBuilder.append("0").append(value);
            else {
                stringBuilder.append(value);
            }
            if (i != mac.length - 1)
                stringBuilder.append(":");
        }
        return stringBuilder.toString();
    }


    /**
     * 获取电话号码
     */
    @SuppressLint("MissingPermission")
    public static String getNativePhoneNumber(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }


    /**
     * 获取网络运营商名称
     * <p>如中国联通、中国移动、中国电信</p>
     *
     * @param context 上下文
     * @return 移动网络运营商名称
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }


    /**
     * 获取IMSI
     * IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMSI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获取sim卡序列号
     *
     * @param context 上下文
     */
    @SuppressLint("MissingPermission")
    public static String getSimNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimSerialNumber() : null;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        String strIP = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        strIP = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            LLog.e("msg", ex.toString());
        }
        return strIP;
    }


    /**
     * 获取WIFI IP地址
     *
     * @return
     */
    public static String getLocalWifiIpAddress(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        LLog.e("本地IP:" + ip);
        return String.valueOf(ipAddress);
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获取 build.pro 属性
     *
     * @param paramString (such as: ro.build.fingerprint)
     * @return
     */
    public static String getBuildProp(String paramString) {
        HashMap<String, String> hashMap = new HashMap();
        if (TextUtils.isEmpty(paramString)) return "";
        CMDUtil.CommandResult commandResult = CMDUtil.execCommand(new String[]{"getprop"}, false, true);
        if (commandResult == null || TextUtils.isEmpty(commandResult.successMsg)) return "fail";
        List<String> results = new ArrayList<>();
        Collections.addAll(results, commandResult.successMsg.split("\n"));
        if (results != null && results.size() > 0) {
            Pattern pattern = Pattern.compile("\\[(.+)\\]: \\[(.*)\\]");
            Iterator<String> it = results.iterator();
            while (it.hasNext()) {
                Matcher localMatcher = pattern.matcher(it.next());
                if (!localMatcher.find())
                    continue;
                hashMap.put(localMatcher.group(1), localMatcher.group(2));
            }
        }
        if (hashMap.containsKey(paramString))
            return hashMap.get(paramString);
        return "fail";
    }


    public static String printCustomDeviceInfo(Context context) {
        int state = DeviceUtil.getNetWorkStatus(context);
        LLog.e("state:", state + "");
        String androdId = DeviceUtil.getAndroidId(context);
        String IMEI = DeviceUtil.getIMEI(context);
        String connectType = DeviceUtil.getConnectedType(context) + "";
        String wifiName = DeviceUtil.getWifiName(context);
        String macAddress = DeviceUtil.getMacAddress(context);
        String macArray = DeviceUtil.getHardwareAddress();
        String phoneNumber = DeviceUtil.getNativePhoneNumber(context);
        String networkName = DeviceUtil.getNetworkOperatorName(context);
        String IMSI = DeviceUtil.getIMSI(context);
        String ipAddress = DeviceUtil.getLocalIpAddress();
        String display = ScreenUtil.getDisplayParams(context)[0] + "x" + ScreenUtil.getDisplayParams(context)[1];
        String simNumber = getSimNumber(context);
        CMDUtil.CommandResult cidResult = CMDUtil.execCommand(new String[]{"cat /sys/block/mmcblk0/device/cid"}, false, true);
        String cid = cidResult.successMsg;
        CMDUtil.CommandResult coreVersionResult = CMDUtil.execCommand(new String[]{"cat /proc/version"}, false, true);
        String coreVersion = coreVersionResult.successMsg;
        CMDUtil.CommandResult arpResult = CMDUtil.execCommand(new String[]{"cat /proc/net/arp"}, false, true);
        String arp = arpResult.successMsg;
        String description = Build.BRAND + "-user " + Build.VERSION.RELEASE + " " + Build.DISPLAY + " " + " release-keys";
        String bssid = getBSSID(context);
        DeviceInfo deviceInfo = new DeviceInfo(androdId, IMEI, IMSI, Build.BRAND, Build.DEVICE, Build.DISPLAY
                , Build.MANUFACTURER, Build.MODEL, Build.PRODUCT, Build.SERIAL, Build.VERSION.RELEASE, Build.VERSION.SDK, Build.VERSION.SDK_INT
                , wifiName, connectType, macAddress, macArray, phoneNumber, networkName, ipAddress, display, simNumber, cid, coreVersion, arp, Build.FINGERPRINT, description, bssid);
        LLog.e("本机设备信息:", deviceInfo.getDeviceInfo());
        DeviceUtil.printDeviceHardwareInfo();

        return ScriptApplication.getGson().toJson(deviceInfo);
    }


    public static String getDeviceInfo(Context context) {
        Object[] sdkObject = HookUtil.getRealSDK();
        String androdId = DeviceUtil.getAndroidId(context);
        String IMEI = DeviceUtil.getIMEI(context);
        String IMSI = DeviceUtil.getIMSI(context);
        String wifiName = DeviceUtil.getWifiName(context);
        String macAddress = DeviceUtil.getMacAddress(context);
        String macArray = DeviceUtil.getHardwareAddress();
        String ipAddress = DeviceUtil.getLocalWifiIpAddress(context);
        String phoneNumber = getNativePhoneNumber(context);
        String display = ScreenUtil.getDisplayParams(context)[0] + "x" + ScreenUtil.getDisplayParams(context)[1];
        CMDUtil.CommandResult commandResult = CMDUtil.execCommand(new String[]{"cat /sys/block/mmcblk0/device/cid"}, false, true);
        String simNumber = getSimNumber(context);
        String cid = commandResult.successMsg;
        CMDUtil.CommandResult coreVersionResult = CMDUtil.execCommand(new String[]{"cat /proc/version"}, false, true);
        String coreVersion = coreVersionResult.successMsg;
        CMDUtil.CommandResult arpResult = CMDUtil.execCommand(new String[]{"cat /proc/net/arp"}, false, true);
        String arp = arpResult.successMsg;
        String description = Build.BRAND + "-user " + Build.VERSION.RELEASE + " " + Build.DISPLAY + " " + " release-keys";
        String bssid = getBSSID(context);
        DeviceInfo deviceInfo = new DeviceInfo(androdId, IMEI, IMSI, Build.BRAND, Build.DEVICE, Build.DISPLAY
                , Build.MANUFACTURER, Build.MODEL, Build.PRODUCT, Build.SERIAL, (String) sdkObject[0], (String) sdkObject[1], (int) sdkObject[2]
                , wifiName, "", macAddress, macArray, phoneNumber, "", ipAddress, display, simNumber, cid, coreVersion, arp, Build.FINGERPRINT, description, bssid);
        LLog.e("设备信息:", deviceInfo.getDeviceInfo());
        return ScriptApplication.getGson().toJson(deviceInfo);
    }


    /**
     * 打印设备硬件信息
     */
    public static void printDeviceHardwareInfo() {
        LLog.e(TAG, "---------------Build------------");
        LLog.e(TAG, "Build.VERSION.SDK: " + Build.VERSION.SDK);//api级数
        LLog.e(TAG, "Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);//api级数，int型返回
        LLog.e(TAG, "Build.VERSION.RELEASE: " + Build.VERSION.RELEASE);//api级数
    }


}

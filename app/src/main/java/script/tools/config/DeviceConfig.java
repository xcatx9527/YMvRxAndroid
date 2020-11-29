package script.tools.config;

import android.os.Environment;

import java.util.HashMap;

/**
 * date: 2017/5/18 19:40
 *
 * @scene 设备机型
 */
public class DeviceConfig {
    public static final String SHANZHAI = "山寨";
    public static final String HUAWEI = "华为";
    public static final String XIAOMI = "小米";
    public static final String MEIZU = "魅族";
    public static final String LENOVO = "联想";



    public static final String Level_adb = "adb";
    public static final String Level_6 = "6.0";
    public static final String Level_7_0 = "7.0";
    public static final String Level_7_1_2 = "7.1.2";

    public static final String DEVICE_PATH = Environment.getExternalStorageDirectory() + "/LocalInfo/LocalDeviceName.txt";

    public static HashMap<String, Integer> deviceConfigMap = new HashMap<>();//配置设备合集
    public static boolean deviceRandomParam = true;

}

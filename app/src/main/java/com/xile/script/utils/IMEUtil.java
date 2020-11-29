package com.xile.script.utils;


import android.content.Context;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import com.chenyang.lloglib.LLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chsh on 2017/4/21.
 * 获取手机输入法列表的工具类
 */

public class IMEUtil {
    private static IMEUtil instance = new IMEUtil();

    private IMEUtil() {

    }

    public static synchronized IMEUtil getInstance() {
        return instance;
    }


    /**
     * 获取输入法集合
     *
     * @return
     */
    public ArrayList<Map> getIME(Context context) {
        ArrayList<Map> arrayList = new ArrayList<>();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> methodList = imm.getInputMethodList();
        LLog.e("getIME", "methodList: " + methodList.size());
        for (InputMethodInfo mi : methodList) {
            HashMap<String, Object> miMap = new HashMap<>();
            String id = mi.getId(); //输入法id
            boolean isSystemIm = invokePrivateMothod(mi);//是否是系统默认的输入法
            miMap.put("IMEid", id);
            miMap.put("isSystemIm", isSystemIm);
            arrayList.add(miMap);
            LLog.e("getIME", "id: " + id);
            LLog.e("getIME", "是否是系统默认的输入法: " + isSystemIm);
        }
        return arrayList;
    }

    /**
     * 获取默认输入法
     *
     * @return
     */
    public String getDefaltIme(Context context) {
        ArrayList<Map> imeLists = getIME(context);
        for (int i = 0; i < imeLists.size(); i++) {
            Map map = imeLists.get(i);
            String imEid = (String) map.get("IMEid");
            Boolean isSystemIm = (Boolean) map.get("isSystemIm");
            if (isSystemIm) { //是默认的输入法
                return imEid;
            }
        }
        return null;
    }

    /**
     * 判断ADBIMe是否存在
     *
     * @return
     */
    public Boolean getADBIme(Context context) {
        ArrayList<Map> imeLists = getIME(context);
        for (int i = 0; i < imeLists.size(); i++) {
            Map map = imeLists.get(i);
            String imEid = (String) map.get("IMEid");
            if (imEid != null && "com.xile.script/.service.AdbIME".equals(imEid)) {
                return true;
            }
        }
        return false;
    }
    private static InputMethodInfo inputMethodInfo;
    private static Class<?> inputMothodUtis;

    /**
     * 获得实例对象
     */
    static {
        try {
            String inputMethonName = "com.android.internal.inputmethod.InputMethodUtils";
            inputMothodUtis = Class.forName(inputMethonName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取并调用私有方法
     */
    public static boolean invokePrivateMothod(InputMethodInfo mi) {
        try {
            // isSystemIme，参数为InputMethodInfo类型的方法
            Method method = inputMothodUtis.getDeclaredMethod("isSystemIme", InputMethodInfo.class);
            // 若调用私有方法，必须抑制java对权限的检查
            method.setAccessible(true);
            // 使用invoke调用方法，并且获取方法的返回值，需要传入一个方法所在类的对象，new Object[]
            // {mi}是需要传入的参数，InputMethodInfo.class相对应
            Boolean flag = (Boolean) method.invoke(inputMethodInfo,
                    new Object[]{mi});
            return flag;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}

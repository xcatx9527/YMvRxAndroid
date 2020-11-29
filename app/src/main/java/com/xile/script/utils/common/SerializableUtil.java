package com.xile.script.utils.common;

import com.chenyang.lloglib.LLog;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerializableUtil {


    /**
     * 序列化
     *
     * @param fileName
     */
    public static void toSerialize(String fileName, Object obj) {
        File file = new File(fileName);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(obj);
            oos.close();
        } catch (IOException e) {
            LLog.e("序列化异常");
        }

    }

    /**
     * 反序列化
     *
     * @param fileName
     */
    public static Object fromSerialize(String fileName) {
        ObjectInputStream ois = null;
        Object obj = null;
        File file = new File(fileName);
        if (file.exists()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                obj = ois.readObject();
                ois.close();
            } catch (IOException e) {
                LLog.e("反序列化异常");
            } catch (ClassNotFoundException e) {
                LLog.e("反序列化异常");
            }
        }

        return obj;
    }




    /**
     * 保存Log
     *
     * @param fielName
     * @param saveValue
     */
    public static void toSaveLog(String fielName, String saveValue) {
        String time = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + "--->";
        try {
            FileOutputStream output = new FileOutputStream(fielName, true);
            output.write((time + saveValue + "\r\n").getBytes());
            output.close();
        } catch (FileNotFoundException e) {
            LLog.e("Log输出异常");
        } catch (IOException e) {
            LLog.e("Log输出异常");
        }
    }

}

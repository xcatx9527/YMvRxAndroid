package com.xile.script.utils.common;

import com.xile.script.config.Constants;
import com.xile.script.utils.TimeUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtUtils {

    /**
     * 根据行读取内容
     *
     * @return
     */
    public static List<String> getTxtList(String filename) {
        //将读出来的一行行数据使用List存储
        String filePath = filename;

        List newList = new ArrayList<String>();
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        newList.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
                addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + " 文件行数为 0 : \n" + filename + "   " + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n" + "");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }


    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void addFileContent(String fileName, String content) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

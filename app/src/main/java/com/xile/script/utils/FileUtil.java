package com.xile.script.utils;

import android.content.Context;

import java.io.*;

public class FileUtil {
    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String 原文件路径 如：/aa
     * @param newPath String 复制后路径 如：xx:/cc
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String[] fileNames = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                File file = new File(newPath);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // 如果捕捉到错误则通知UI线程
        }
    }


    /**
     * 读取文件内容
     *
     * @param filename
     * @return
     */
    public static String readFile(String filename) {
        String content = "";
        File file = new File(filename);
        if (file.exists()) {
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] bs = new byte[input.available()];
                int total = input.read(bs);
                input.close();
                content = new String(bs, 0, total);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 保存字符串到指定路径的文件
     *
     * @param content
     * @param filePath
     */
    public static void saveFile(String content, String filePath) {
        try {
            File file = new File(filePath);
            File parentDir = new File(file.getParent() + File.separator);
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            file.createNewFile();
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(content.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断指定路径的文件是否存在
     *
     * @param filepath
     * @return
     */
    public static boolean isFileExist(String filepath) {

        try {
            File file = new File(filepath);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ROOT方式判断指定路径的文件是否存在
     *
     * @param filepath
     * @return
     */
    public static boolean isFileExistRoot(String filepath) {
        CMDUtil.CommandResult commandResult = CMDUtil.execCommand(new String[]{"ls " + filepath}, true, true);
        String successMsg = commandResult.successMsg;
        if (successMsg != null) {
            return !successMsg.contains("No such file or directory");
        }
        return false;
    }

}

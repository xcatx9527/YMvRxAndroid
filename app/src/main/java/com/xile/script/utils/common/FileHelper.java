package com.xile.script.utils.common;


import android.text.TextUtils;
import android.util.Base64;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.FileInfo;
import com.xile.script.bean.ScriptInfo;
import com.xile.script.config.Constants;
import com.xile.script.utils.TimeUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：赵小飞<br>
 * 时间 2017/3/10.
 */

public class FileHelper {


    /**
     * 保存脚本
     *
     * @param pathName
     * @param scriptInfoList
     */
    public static void saveScript(String pathName, List<ScriptInfo> scriptInfoList) {
        makeRootDirectory(new File(pathName).getParent());
        File file = new File(pathName);
        PrintWriter output = null;
        try {
            output = new PrintWriter(new FileWriter(file));
            for (int i = 0; i < scriptInfoList.size(); i++) {
                if (!StringUtil.isEmpty(scriptInfoList.get(i).getScriptStr())) {
                    output.println(scriptInfoList.get(i).getScriptStr());
                    output.flush();
                }
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对文件夹的存在与否进行判断，如果没有就创建
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 读取文件内容  一行一行读取  存进 string 集合
     *
     * @param filename
     * @return
     */
    public static List<String> readFile(String filename) {
        List<String> strList = null;
        try {
            strList = new ArrayList<String>();
            BufferedReader input = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = input.readLine()) != null) {
                strList.add(line);
            }
            input.close();
            if (strList.size() <= 0){
                addFileContent(Constants.LOG_PATH, TimeUtil.formatTimeStamp(System.currentTimeMillis()) + " 文件行数为 0 : \n" + filename + "   " + ((System.currentTimeMillis() - Constants.getOrderTime) / 1000) + "s" + ": \n\n" + "");
            }
        } catch (IOException e) {

              e.printStackTrace();
        }
        return strList;
    }





    /**
     * 读取文件内容
     *
     * @param filename
     * @return
     */
    public static String getFileString(String filename) {
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
     * 获取文件的最后一次修改时间
     *
     * @param strPath
     * @return List<FileInfo>
     */
    public static List<FileInfo> getFileLastModified(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        List<FileInfo> filelist = new ArrayList<FileInfo>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileLastModified(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else if (fileName.endsWith("txt")) { // 判断文件名是否以.txt结尾
                    //String strFileName = files[i].getAbsolutePath();
                    long time = files[i].lastModified();
                    //System.out.println("---" + strFileName);
                    FileInfo fileInfo = new FileInfo(fileName, time);
                    filelist.add(fileInfo);
                } else {
                    continue;
                }
            }
        }
        return filelist;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        makeRootDirectory(new File(newPath).getParent());
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            List<String> contentList = getStrList(content, 1024);
            if (contentList != null && contentList.size() > 0) {
                for (String con : contentList) {
                    outStream.write(con.getBytes());
                }
            }
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @return
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @param size        指定列表大小
     * @return
     */
    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str 原始字符串
     * @param f   开始位置
     * @param t   结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f);
        } else {
            return str.substring(f, t);
        }
    }


    /**
     * 删除文件
     *
     * @param fileName
     */

    public static void deleteFile(String fileName) {

        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除指定目录下文件及目录
     *
     * @param fileDirPath
     */

    public static void deleteFolderFile(String fileDirPath) {
        if (!TextUtils.isEmpty(fileDirPath)) {
            try {
                File dir = new File(fileDirPath);
                if (dir.exists() && dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    if (files != null && files.length > 0) {
                        for (File f : files) {
                            if (f.isDirectory()) {
                                deleteFolderFile(f.getAbsolutePath());
                            } else {
                                f.delete();
                            }
                        }
                    }
                    dir.delete();
                } else if (dir.exists() && !dir.isDirectory()) {
                    dir.delete();
                }
            } catch (Exception e) {
                LLog.e("删除文件异常");
                e.printStackTrace();
            }
        }
    }


    /**
     * 删除指定目录下文件但不删除目录
     *
     * @param folderPath
     */
    public static void deleteFileWithoutFolder(String folderPath) {
        if (TextUtils.isEmpty(folderPath)) return;
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFileWithoutFolder(f.getAbsolutePath());
                    } else {
                        f.delete();
                    }
                }
                //folder.delete();
            }
        }
    }


    public static void deletCacheFile() {
//        /storage/emulated/0
        try {
            File dir = new File("/storage/emulated/0");
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                int length = files.length;
                for (int i = 0; i < length; i++) {
                    File file = files[i];
                    String fileName = files[i].getName();
                    if (!"DCIM".equals(fileName)
                            && !"Android".equals(fileName)
                            && !"sogou".equals(fileName)
                            && !"GameData".equals(fileName)
                            && !"Linux".equals(fileName)
                            && !"tesseract".equals(fileName)
                            && !"XileScript".equals(fileName)
                            && !"Xiaomi".equals(fileName)
                            && !"LocalInfo".equals(fileName)
                            && !"我的文件夹".equals(fileName)) {
                        if (file.isDirectory()) {
                            deleteFolderFile(file.getAbsolutePath());
                        } else {
                            file.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LLog.e("删除文件异常");
            e.printStackTrace();
        }

    }

    /**
     * 将assets目录下的文件拷贝到其他地方
     *
     * @param fileName 用作拷贝的文件名
     * @param filePath 目标路径
     * @return
     */
    public static void copyAssetFile(String fileName, String filePath) {
        if (!StringUtil.isEmpty(fileName) && !StringUtil.isEmpty(filePath)) {
            try {
                InputStream is = ScriptApplication.app.getAssets().open(fileName);
                if (is != null) {
                    makeRootDirectory(new File(filePath).getParent());
                    FileOutputStream fos = new FileOutputStream(new File(filePath));
                    byte[] temp = new byte[1024];
                    int i;
                    while ((i = is.read(temp)) > 0) {
                        fos.write(temp, 0, i);
                    }
                    fos.close();
                    is.close();
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 获取文件
     *
     * @param path 根目录路径
     */
    public static List<File> getFiles(String path) {
        List<File> fileList = new ArrayList<>();
        try {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length != 0) {
                    for (File f : files) {
                        fileList.add(f);
                    }
                } else {

                }
            } else {
                LLog.e("文件根目录不存在!   + path  = " + path);
            }
        } catch (Exception e) {
        }
        return fileList;
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

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void addFileContent2(String fileName, String content) {
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


    /**
     * 创建文件夹
     *
     * @param fileDirectory
     */
    public static void createDirectory(String fileDirectory) {
        File file = new File(fileDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    /**
     * 获取文件名字
     *
     * @param path 根目录路径
     */
    public static List<String> getFileNames(String path) {
        List<String> fileList = new ArrayList<>();
        try {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        fileList.add(f.getName());
                    }
                } else {
                    LLog.e("该目录下没有文件! --> " + path);
                    RecordFloatView.updateMessage("该目录下没有文件! --> " + path);
                }

            } else {
                LLog.e("根目录不存在! --> " + path);
                RecordFloatView.updateMessage("根目录不存在! --> " + path);
            }
        } catch (Exception e) {
        }
        return fileList;
    }

    /**
     * 获取需要上传的图片名字
     *
     * @param path 根目录路径
     */
    public static String getUploadImgNames(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        String name = null;
        try {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        stringBuilder.append(f.getName()).append(",");
                    }
                    name = stringBuilder.toString().substring(0, stringBuilder.toString().lastIndexOf(","));
                } else {
                    LLog.e("没有可传图片!");
                    RecordFloatView.updateMessage("没有可传图片!");
                }
            } else {
                LLog.e("上传图片根目录不存在!");
                RecordFloatView.updateMessage("上传图片根目录不存在!");
            }
        } catch (Exception e) {
        }
        return name;
    }

    /**
     * 获取需要上传的图片
     *
     * @param file
     */
    public static String getUploadFile(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        String fileData = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] bs = new byte[1024];
            int temp;
            while ((temp = fis.read(bs)) != -1) {
                bos.write(bs, 0, temp);
            }
            bos.close();
            fis.close();
            byte[] base64buffer = Base64.encode(bos.toByteArray(), Base64.NO_WRAP);
            fileData = new String(base64buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }


    /**
     * 获取需要上传的警报图片
     *
     * @param path 图片路径
     */
    public static File getUploadAlertImg(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }


    /**
     * 将文件夹拷贝到指定的路径 (默认在sd卡下)
     *
     * @param srcbefor
     * @param desbefor
     */
    public static void copyFileDirectory(String srcbefor, String desbefor) {

        String src = srcbefor;
        String des = desbefor;
        File file1 = new File(src);

        if (file1 != null && file1.isDirectory()) {  //文件夹
            File[] fs = file1.listFiles();
            File file2 = new File(des);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            for (File f : fs) {
                LLog.e("fileName==" + f.getName());
                if (f.isFile()) {
                    nioBufferCopy(new File(f.getPath()), new File(des + File.separator + f.getName()));//调用文件拷贝的方法
                } else if (f.isDirectory()) {
                    copyFileDirectory(f.getPath(), des + File.separator + f.getName());
                }
            }
        } else if (file1 != null && file1.isFile()) {  //文件
            File file2 = new File(des);
            if (!file2.exists()) {
                try {
                    file2.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            nioBufferCopy(file1, file2);//调用文件拷贝的方法
        }
    }

    /**
     * 文件拷贝的方法
     * 由此可见，FileChannel复制文件的速度比BufferedInputStream/BufferedOutputStream复制文件的速度快了近三分之一。
     * 在复制大文件的时候更加体现出FileChannel的速度优势。而且FileChannel是多并发线程安全的。
     */
    private static void nioBufferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                in.close();
                outStream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static String getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        String s = FormetFileSize(size);
        return s;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        return fileSizeString;
    }
}

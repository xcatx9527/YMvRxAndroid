package com.xile.script.utils.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * 作者：赵小飞<br>
 * 时间 2017/4/13.
 */

public class EncryptUtil {

    /**
     * 加密
     *
     * @param inputText
     * @return
     */
    public static String e(String inputText) {
        return md5(inputText,32);
    }

    /**
     * 二次加密，应该破解不了了吧？
     *
     * @param inputText
     * @return
     */
    public static String md5AndSha(String inputText) {
        return sha(md5(inputText,32));
    }

    /**
     * md5加密
     *
     * @param inputText
     * @return
     */
    public static String md5(String inputText,int digits) {
        String md5Str=encrypt(inputText, "md5");
        if (digits==16){
            md5Str= md5Str.substring(8,24);
        }
        return md5Str;
    }

    public static  String getSign(TreeMap<String, String> args, int digits){
        if (args==null){
            throw  new NullPointerException("数据字典为ull");
        }
        StringBuffer sb=new StringBuffer();
        for (String key:args.keySet()){
            sb.append(args.get(key));
        }
        return  md5(sb.toString(),32);
    }

    /**
     * sha加密
     *
     * @param inputText
     * @return
     */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }

    /**
     * md5或者sha-1加密
     *
     * @param inputText
     *            要加密的内容
     * @param algorithmName
     *            加密算法名称：md5或者sha-1，不区分大小写
     * @return
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes(StandardCharsets.UTF_8));
            byte[] s = m.digest();
            // m.digest(inputText.getBytes("UTF8"));
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    /**
     * 返回十六进制字符串
     *
     * @param arr
     * @return
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /**
     * 生成随机数
     * @return
     */
    public static int getRandom(){
        java.util.Random random=new java.util.Random();// 定义随机类
        int result=random.nextInt(10000);// 返回[0,10)集合中的整数，注意不包括10
        return result+1;              // +1后，[0,10)集合变为[1,11)集合，满足要求
    }




    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        // md5加密测试
        String md5_1 = md5("222222",32);
        String md5_2 = md5("习大大",32);
        System.out.println(md5_1 + "\n" + md5_2);

        // sha加密测试
        String sha_1 = sha("123456");
        String sha_2 = sha("习大大");
        System.out.println(sha_1 + "\n" + sha_2);

    }
}

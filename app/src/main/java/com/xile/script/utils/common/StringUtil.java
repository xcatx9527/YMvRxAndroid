package com.xile.script.utils.common;

import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 处理字符串工具类
 */
public class StringUtil {

    /**
     * 判断是否为空
     *
     * @param text
     * @return
     */
    public static boolean isNullOrEmpty(String text) {
        return text == null || "".equals(text.trim()) || text.trim().length() == 0
                || "null".equals(text.trim());
    }

    /**
     * 截取数字和中文汉字 “级”
     */
    public static String getNumbers(String content) {
        String regEx = "[^0-9\\u7ea7-\\u7ea7]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        String trim = m.replaceAll("").trim();
        return trim;
    }

    public static String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 判断字符串数组texts中是否有一个字符串为空
     *
     * @param texts
     * @return 如果字符串数组texts中有一个为空或texts为空，返回true;otherwise return false;
     */
    public static boolean isEmpty(String... texts) {
        if (texts == null || texts.length == 0) {
            return true;
        }
        for (String text : texts) {
            if (text == null || "".equals(text.trim()) || text.trim().length() == 0
                    || "null".equals(text.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得MD5加密字符串
     *
     * @param str 字符串
     * @return
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 得到字符串长度
     *
     * @param text
     * @return
     */
    public static int getCharCount(String text) {
        String Reg = "^[\u4e00-\u9fa5]{1}$";
        int result = 0;
        for (int i = 0; i < text.length(); i++) {
            String b = Character.toString(text.charAt(i));
            if (b.matches(Reg))
                result += 2;
            else
                result++;
        }
        return result;
    }

    /**
     * 获取截取后的字符串
     *
     * @param text   原字符串
     * @param length 截取长度
     * @return
     */
    public static String getSubString(String text, int length) {
        return getSubString(text, length, true);
    }

    /**
     * 获取截取后的字符串
     *
     * @param text   原字符串
     * @param length 截取长度
     * @param isOmit 是否加上省略号
     * @return
     */
    public static String getSubString(String text, int length, boolean isOmit) {
        if (isNullOrEmpty(text)) {
            return "";
        }
        if (getCharCount(text) <= length + 1) {
            return text;
        }

        StringBuffer sb = new StringBuffer();
        String Reg = "^[\u4e00-\u9fa5]{1}$";
        int result = 0;
        for (int i = 0; i < text.length(); i++) {
            String b = Character.toString(text.charAt(i));
            if (b.matches(Reg)) {
                result += 2;
            } else {
                result++;
            }

            if (result <= length + 1) {
                sb.append(b);
            } else {
                if (isOmit) {
                    sb.append("...");
                }
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 电话号码验证
     *
     * @param phoneNumber 手机号码
     * @return
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = pattern.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 邮箱验证
     *
     * @param mail 邮箱
     * @return
     */
    public static boolean validateEmail(String mail) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = pattern.matcher(mail);
        return m.matches();
    }

    /**
     * 判断是否为浮点数，小数点2位
     *
     * @return
     * @param:mail 邮箱
     */
    public static boolean validateFloatNum(String str) {
        Pattern pattern = Pattern.compile("^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d{1,2})?$");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * 验证输入的身份证号是否符合格式要求
     *
     * @param IDNum 身份证号
     * @return 符合国家的格式要求为 true;otherwise,false;
     */
    public static boolean validateIDcard(String IDNum) {
        String id_regEx1 = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3}[0-9Xx])$";
        Pattern pattern = Pattern.compile(id_regEx1);
        Matcher m = pattern.matcher(IDNum);
        return m.matches();
    }

    /**
     * 验证字符串内容是否合法
     *
     * @param content 字符串内容
     * @return
     */
    public static boolean validateLegalString(String content) {
        String illegal = "`~!#%^&*=+\\|{};:'\",<>/?○●★☆☉♀♂※¤╬の〆";
        boolean legal = true;
        L1:
        for (int i = 0; i < content.length(); i++) {
            for (int j = 0; j < illegal.length(); j++) {
                if (content.charAt(i) == illegal.charAt(j)) {
                    legal = false;
                    break L1;
                }
            }
        }
        return legal;
    }

    /**
     * 验证字符串内容是否合法(进一步验证字符串内容是否仅仅包含汉字或者0-9、a-z、A-Z)
     *
     * @param content 被校验的字符串内容
     * @return 合法：返回true;otherwise,false.
     */
    public static boolean validataLegalString2(String content) {
        if (validateLegalString(content)) {
            // 进一步验证字符串是否是汉字或者0-9、a-z、A-Z
            for (int i = 0; i < content.length(); i++) {
                if (!isRightChar(content.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean validataLegalString3(String content) {
        if (validateLegalString(content)) {
            // 进一步验证字符串是否是汉字
            for (int i = 0; i < content.length(); i++) {
                if (!isChinese(content.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean validataLegalString4(String content) {
        if (validateLegalString(content)) {
            // 进一步验证字符串是否是0-9、a-z、A-Z，下划线
            for (int i = 0; i < content.length(); i++) {
                if (!isWord(content.charAt(i))) {
                    return false;
                }
                if (isChinese(content.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证字符串内容是否合法(进一步验证字符串内容是否仅仅包含汉字或者0-9、a-z、A-Z)
     *
     * @param content 被校验的字符串内容
     * @return 合法：返回true;otherwise,false.
     */
    public static boolean validataLegalString5(String content) {

        // 进一步验证字符串是否是汉字或者0-9、a-z、A-Z
        for (int i = 0; i < content.length(); i++) {
            if (!isRightChar(content.charAt(i))) {
                return false;
            }
        }
        return true;

    }

    /**
     * 验证是否是汉字或者0-9、a-z、A-Z
     *
     * @param c 被验证的char
     * @return true代表符合条件
     */
    public static boolean isRightChar(char c) {
        return isChinese(c) || isWord(c);
    }

    /**
     * 校验某个字符是否是a-z、A-Z、_、0-9
     *
     * @param c 被校验的字符
     * @return true代表符合条件
     */
    public static boolean isWord(char c) {
        return Pattern.compile("[\\w]").matcher(String.valueOf(c)).matches();
    }

    /**
     * 判定输入的是否是汉字
     *
     * @param c 被校验的字符
     * @return true代表是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /* 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 对聊天的图片进行动态调整
     */
    public static String getChatImgUrl(String url, int width, int height) {
        int temp = url.indexOf("?");
        if (temp > -1) {
            String[] urls = url.split("\\?");
            if (urls.length > 1) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(urls[0]);
                stringBuffer.append("?");
                stringBuffer.append("px=");
                stringBuffer.append(width);
                stringBuffer.append("x");
                stringBuffer.append(height);
                stringBuffer.append("&");
                stringBuffer.append(urls[1]);
                return stringBuffer.toString();
            }
        }
        return null;
    }

    /**
     * 如果为空显示暂无信息
     *
     * @param tv  控件名
     * @param str 信息
     */
    public static void viewText(TextView tv, String str) {
        if (tv == null)
            return;
        if (isNullOrEmpty(str)) {
            tv.setText("暂无资料");
        } else {
            tv.setText(str);
        }
    }

    /**
     * 如果str为空，tv的值为"";否则，tv的值为str.
     *
     * @param tv
     * @param str 判断该参数是否为空
     */
    public static void setTextViewString(TextView tv, String str) {
        if (tv == null)
            return;
        tv.setText(isNullOrEmpty(str) ? "" : str);
    }

    /**
     * 如果str为空，tv的值为"";否则tv的值为default2.
     *
     * @param tv
     * @param str         判断该参数是否为空
     * @param defaultStr2 str不为空的话，tv的值
     */
    public static void setTextViewString(TextView tv, String str, String defaultStr2) {
        if (tv == null)
            return;
        tv.setText(isNullOrEmpty(str) ? "" : defaultStr2);
    }

    /**
     * 如果str为空，tv的值为default1;否则tv的值为default2.
     *
     * @param tv
     * @param str         判断该参数是否为空
     * @param defaultStr1 str为空的话，tv的值
     * @param defaultStr2 str不为空的话，tv的值
     */
    public static void setTextViewString(TextView tv, String str, String defaultStr1,
                                         String defaultStr2) {
        if (tv == null)
            return;
        tv.setText(isNullOrEmpty(str) ? defaultStr1 : defaultStr2);
    }

    /**
     * 设置参数中的views显示状态为View.VISIBLE
     */
    public static void setViewVisible(View... views) {
        for (View view : views) {
            if (view != null)
                view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置参数中的views显示状态为View.GONE
     */
    public static void setViewGone(View... views) {
        for (View view : views) {
            if (view != null)
                view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置参数中的views显示状态为View.INVISIBLE
     */
    public static void setViewInvisible(View... views) {
        for (View view : views) {
            if (view != null)
                view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 对流转化成字符串
     *
     * @param is
     * @return
     */
    public static String getContentByString(InputStream is) {
        try {
            if (is == null)
                return null;
            byte[] b = new byte[1024];
            int len = -1;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对流转化成字符串
     *
     * @param is
     * @return
     */
    public static String getStringByStream(InputStream is) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString().replaceAll("\n\n", "\n");
        } catch (OutOfMemoryError o) {
            System.gc();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 截取字符串，去掉sign后边的
     *
     * @param source 原始字符串
     * @param sign
     * @return
     */
    public static String splitByIndex(String source, String sign) {
        String temp = "";
        if (isNullOrEmpty(source)) {
            return temp;
        }
        int length = source.indexOf(sign);
        if (length > -1) {
            temp = source.substring(0, length);
        } else {
            return source;
        }
        return temp;
    }

    /**
     * 截取字符串，返回sign分隔的字符串
     */
    public static String splitNumAndStr(String res, String sign) {
        StringBuffer buffer;
        String reg = "\\d+";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(res);
        if (m.find()) {
            buffer = new StringBuffer();
            String s = m.group();
            buffer.append(s);
            buffer.append(sign);
            buffer.append(res.replace(s, ""));
            return buffer.toString();
        }
        return null;
    }

    /**
     * 字符串 转换 double
     *
     * @param s 需要转化的数据
     * @return 转化后的double数据
     */

    public static double parseDouble(String s) {
        String ss = "";
        if (!isNullOrEmpty(s) && s.contains(",")) {
            int len = s.split(",").length;
            for (int i = 0; i < len; i++) {
                ss = ss + s.split(",")[i];
            }
            return canParseDouble(ss) ? Double.parseDouble(ss) : 0;
        }
        return canParseDouble(s) ? Double.parseDouble(s) : 0;
    }

    /**
     * @param d   double数据
     * @param len 小数点位数
     * @return
     */
    public static String formatNumber(double d, int len) {
        try {
            DecimalFormat df = null;
            if (len == 0) {
                df = new DecimalFormat("###0");
            } else {
                String s = "#,##0.";
                String ss = "";
                for (int i = 0; i < len; i++) {
                    s = s + "0";
                    ss = ss + "0";
                }
                df = new DecimalFormat(s);
                if (df.format(d).split("\\.")[1].equals(ss)) {
                    return df.format(d).split("\\.")[0];
                }
            }
            return df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 保留小数点后一位
     */
    public static String formatNumber(double d) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.0");
            return df.format(d);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 保留小数点后两位
     */
    public static String formatNumber2(double d) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(d);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 保留小数点后两位,三位分割(价格)
     */
    public static String formatNumber2(double d, boolean bl) {
        if (bl) {
            try {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                return df.format(d);
            } catch (Exception e) {
            }
        }
        return "";
    }

    /**
     * 保留小数点后三位
     */
    public static String formatNumber3(double d) {
        DecimalFormat df = new DecimalFormat("#,##0.000");
        return df.format(d);
    }

    /**
     * 保留小数点后四位
     */
    public static String formatNumber4(double d) {
        try {
            DecimalFormat df = new DecimalFormat("0.0000");
            return df.format(d);
        } catch (Exception e) {
        }
        return "";
    }

    public static String formatNumber(String d) {
        return formatNumber(Double.parseDouble(d));
    }

    /**
     * 把对象放进map里
     *
     * @param o 实体
     */
    public static Map<String, String> getMapForEntry(Object o) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Field[] fields = o.getClass().getFields();
            for (Field f : fields) {
                String key = f.getName();
                try {
                    String value = (String) f.get(o);
                    if (StringUtil.isNullOrEmpty(value) || value.indexOf("不限") > -1) {
                        continue;
                    }
                    map.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * map 转化为实体
     *
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T setMapForEntry(Map<String, String> map, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            for (Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                Field field = t.getClass().getField(key);
                field.set(t, entry.getValue());
            }
        } catch (Exception e) {
        }
        return t;
    }

    /**
     * 实体转化
     *
     * @param o
     * @return
     */
    public static <T> T convertEntry(Object o) {
        T t = null;
        try {
            t = (T) o.getClass().newInstance();
            Field[] fields = o.getClass().getFields();
            for (Field f : fields) {
                try {
                    String value = (String) f.get(o);
                    if (StringUtil.isNullOrEmpty(value)
                            || ((value.indexOf("不限") > -1) && !value.contains("不限购"))) {
                        f.set(t, "");
                    } else {
                        f.set(t, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
        return t;
    }

    private static final String TAG = "StringUtil";

    /**
     * 截取sign后边的数字
     */
    public static String getStringNum(String str, String sign) {
        String reg = ":split:";
        return str.replace(sign, reg).replaceAll(reg + "\\d+", "").replaceAll(" ", "").trim();

    }

    public static String getRegText(String xml, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.UNICODE_CASE
                | Pattern.DOTALL);
        Matcher m = pattern.matcher(xml);
        if (m.find()) {
            xml = m.group(1);
            xml = xml.replace("<![CDATA[", "").replace("<![cdata[", "").replace("]]>", "");
            return xml;
        } else {
            return null;
        }
    }

    /**
     * 判断价格是否为0或空
     */
    public static boolean isPriceZero(String price) {
        if (isNullOrEmpty(price)) {
            return true;
        }
        price = splitByIndex(price, ".");
        return "0".equals(price);

    }

    /**
     * 取价格的整数，去掉单位
     *
     * @param price
     * @return
     */
    public static String getPrice(String price) {
        if (price == null) {
            return "";
        }
        Pattern p = Pattern.compile("^\\d+");
        Matcher m = p.matcher(price);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    /**
     * 去掉单位
     *
     * @param price
     * @return
     */
    public static String getPriceAll(String price) {
        if (price == null) {
            return "";
        }
        if (price.contains(".")) {
            Pattern p = Pattern.compile("^\\d+.\\d+");
            Matcher m = p.matcher(price);
            if (m.find()) {
                return m.group();
            }
        } else {
            Pattern p = Pattern.compile("^\\d+");
            Matcher m = p.matcher(price);
            if (m.find()) {
                return m.group();
            }
        }
        return "";
    }

    /**
     * 判断是否全为数字
     *
     * @param content
     * @return
     */
    public static boolean isAllNumber(String content) {
        boolean isAllNumber = true;
        if (isNullOrEmpty(content)) {
            return false;
        }
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) < '0' || content.charAt(i) > '9') {
                isAllNumber = false;
            }
        }
        return isAllNumber;
    }

    /**
     * 整数转字节数组
     *
     * @param i
     * @return
     */
    public static byte[] intToByte(int i) {
        byte[] bt = new byte[4];
        bt[0] = (byte) (0xff & i);
        bt[1] = (byte) ((0xff00 & i) >> 8);
        bt[2] = (byte) ((0xff0000 & i) >> 16);
        bt[3] = (byte) ((0xff000000 & i) >> 24);
        return bt;
    }

    /**
     * 字节数组转整数
     *
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        int num = bytes[0] & 0xFF;
        num |= ((bytes[1] << 8) & 0xFF00);
        num |= ((bytes[2] << 16) & 0xFF0000);
        num |= ((bytes[3] << 24) & 0xFF000000);
        return num;
    }


    /**
     * 拼接短信内容
     */
    public static String getMSG(String district, String title, String room, String mj,
                                String price, String type) {
        StringBuffer sb = new StringBuffer();
        if (!isNullOrEmpty(district))
            sb.append(district + ",");
        if (!isNullOrEmpty(title))
            sb.append(title + ",");
        if (!isNullOrEmpty(room))
            sb.append(room + ",");
        if (!isNullOrEmpty(mj) && !"暂无资料".equals(mj) && !"暂无信息".equals(mj))
            sb.append(mj + "平米,");
        if (!isNullOrEmpty(price))
            sb.append(price.replace("/套", "").replace("/月", ""));
        if (!isNullOrEmpty(type))
            sb.append(type);
        if (!isNullOrEmpty(sb.toString())) {
            if (sb.toString().endsWith(",")) {
                String str = sb.toString().substring(0, sb.toString().length() - 1);
                return str;
            }
        }
        return sb.toString();
    }

    private static float PRICE_FLOAT = 0.05f;

    public static String getPriceFloat(String price) {
        return getPriceFloat(price, true);
    }

    /**
     * 获取上下浮动价格
     *
     * @return
     */
    public static String getPriceFloat(String price, boolean isMax) {
        price = getPrice(price);
        float price_f = Float.parseFloat(price);
        if (!isMax) {
            price_f = price_f - price_f * PRICE_FLOAT;
        } else {
            price_f += price_f * PRICE_FLOAT;
        }

        return getPrice(String.valueOf(price_f));
    }

    /**
     * 获取字符串中的数字
     *
     * @return
     */
    public static String getPriceNum(String price) {
        String regEx = "[^0-9.]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(price);
        String pricetype = m.replaceAll("").trim();

        return pricetype;
    }

    /**
     * 按字节截取字符串
     *
     * @param orignal 原始字符串
     * @param count   截取位数
     * @return 截取后的字符串
     * @throws UnsupportedEncodingException 使用了JAVA不支持的编码格式
     */
    public static String substring(String orignal, int count) throws UnsupportedEncodingException {
        // 原始字符不为null，也不是空字符串
        if (null != orignal && !"".equals(orignal)) {
            // 将原始字符串转换为GBK编码格式
            String orignal_byte = new String(orignal.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            if (count > 0 && count < orignal.getBytes(StandardCharsets.UTF_8).length) {
                StringBuffer buff = new StringBuffer();
                char c;
                String s = "";
                int num = 0;
                for (int i = 0; i < count; i++) {
                    // charAt(int index)也是按照字符来分解字符串的
                    if (orignal_byte.length() > i) {
                        c = orignal_byte.charAt(i);
                        buff.append(c);
                        if (isChineseChar(c)) {// 遇到中文汉字，字节总数+2
                            num += 2; // 一般汉字在utf-8中为3个字节长度
                        } else {
                            num += 1;
                        }
                        if (num == count) {
                            s = buff.toString() + "...";
                            continue;
                        } else if (num > count) {
                            if (num == 15) {
                                return buff.toString() + "...";
                            } else {
                                return s;
                            }
                        }
                    }

                }
                return buff.toString();
            }
            // 要截取的字节数大于0，且小于原始字符串的字节数
        }
        return orignal;
    }

    public static boolean isChineseChar(char c) {
        // 如果字节数大于1，是汉字
        return String.valueOf(c).getBytes(StandardCharsets.UTF_8).length > 1;
    }


    /**
     * 返回汉字个数
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static int getChineseCount(String s) {// 获得汉字的长度
        char c;
        int chineseCount = 0;
        if (!"".equals("")) {// 判断是否为空
            s = new String(s.getBytes(), StandardCharsets.UTF_8);
        }
        for (int i = 0; i < s.length(); i++) {// for循环
            c = s.charAt(i); // 获得字符串中的每个字符
            if (isChineseChar(c)) {// 调用方法进行判断是否是汉字
                chineseCount++; // 等同于chineseCount=chineseCount+1
            }
        }
        return chineseCount; // 返回汉字个数
    }


    /**
     * 聊天去除l:
     *
     * @param name
     * @return
     */
    public static String getChatNameString(String name) {
        String chatname = name;
        if (chatname != null && chatname.contains(":")) {
            try {
                return chatname.substring(chatname.indexOf(":") + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chatname;
    }

    /**
     * 返回间隔空格的title
     *
     * @param args
     * @return
     */
    public static String getStringName(String... args) {
        String name = "";
        for (int i = 0; i < args.length; i++) {
            if (!isNullOrEmpty(args[i])) {
                name += args[i] + " ";
            }
        }
        return name;
    }

    /**
     * 检验字符串是否包含不合法字符
     *
     * @param Str
     * @return
     */
    public static boolean validateStr(String Str) {
        Pattern pattern = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9\u4e00-\u9fa5]+$");
        Matcher m = pattern.matcher(Str);
        return m.matches();
    }

    /**
     * 验证一个字符串是否能解析成整数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseInt(String numberStr) {
        try {
            Integer.parseInt(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 验证一个字符串是否能解析成双精度浮点数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseDouble(String numberStr) {
        try {
            Double.parseDouble(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 验证一个字符串是否能解析成浮点数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseFloat(String numberStr) {
        try {
            Float.parseFloat(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 验证一个字符串是否能解析成长整型数
     *
     * @param numberStr
     * @return
     */
    public static boolean canParseLong(String numberStr) {
        try {
            Long.parseLong(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean validateString(String Str) {
        // 按照顺序汉字，英文字母数字和英文符号，标点，中日韩标点，特殊标点,希腊文，几何形状各种运算符之类
        Pattern pattern = Pattern
                .compile("^[\u4e00-\u9fbf\u0020-\u007F\u2000-\u206F\u3000-\u303F\uFF00-\uFFEF\u0370-\u03FF\u2100-\u25FF]+$");
        Matcher m = pattern.matcher(Str);
        return m.matches();
    }

    /**
     * 是否判断
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {

        String encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }


    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    /**
     * 去除重复的字符串
     *
     * @param s
     * @return
     */
    public static String removerepeatedchar(String s) {
        StringBuffer sb = new StringBuffer();
        char[] chars = s.toCharArray();
        TreeSet<String> tr = new TreeSet<String>();
        for (int i = 0; i < s.length(); i++) {
            tr.add(String.valueOf(s.charAt(i)));
        }

        for (String index : tr) {
            sb.append(index);
        }

        return sb.toString();
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    /**
     * 过滤除数字之外的其他符号
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String getNumber(String str) throws Exception {
        String regEx = "[^0-9]";
        return Pattern.compile(regEx).matcher(str).replaceAll("").trim();
    }

    /**
     * 过滤设置的特殊符号
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String filtrationText(String str) throws Exception {
        String regEx = "[《》·`~!@#$%^&*()+=|{}:;\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\[\\]]";
        return Pattern.compile(regEx).matcher(str).replaceAll("").trim();
    }

}

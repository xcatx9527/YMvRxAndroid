package com.xile.script.utils.common;

import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 date: 2017/5/9 19:46
 *
 * @scene 生成随机数
 */

public class RandomUtil {


    /**
     * 随机生成数字
     *
     * @param length
     * @return
     */

    public static String getRandomNum(int length) {
        String base = "0123456789";
        return getRandomStr(length, base);
    }

    /**
     * 随机生成数字
     *
     * @param min
     * @param max
     * @return
     */

    public static int getRandomNum(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    /**
     * 随机生成数字
     *
     * @param space
     * @return
     */

    public static int getRandomSpace(int space) {
        int[] data = new int[]{-1, 1};
        return new Random().nextInt(space) * data[new Random().nextInt(data.length)];
    }

    /**
     * 随机生成数字
     *
     * @param arr
     * @return
     */

    public static int getRandomNum(int[] arr) {
        return arr[new Random().nextInt(arr.length)];
    }

    /**
     * 随机生成字符串
     *
     * @param length
     * @param base
     * @return
     */
    public static String getRandomStr(int length, String base) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 将list集合，元素随机打乱
     *
     * @param list
     * @param <T>
     */
    public static <T> void shuffle(List<T> list) {
        int size = list.size();
        Random random = new Random();

        for (int i = 0; i < size; i++) {

            int randomPos = random.nextInt(size);

            Collections.swap(list, i, randomPos);
        }
    }

}

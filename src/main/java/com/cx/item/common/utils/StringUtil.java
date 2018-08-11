package com.cx.item.common.utils;

import java.util.Random;

/**
 * Created by hwm on 2018/6/8.
 */
public class StringUtil {

    /**
     * 获取字符串随机码
     *
     * @param size int 获取的长度
     * @return String
     */
    public static String getStringRandom(int size) {

        StringBuffer randomStr = new StringBuffer();
        Random random = new Random();
        // 参数length，表示生成几位随机数
        for (int i = 0; i < size; i++) {
            // 输出字母还是数字
            if (random.nextBoolean()) {
                // 输出是大写字母还是小写字母
                int temp = random.nextBoolean() ? 65 : 97;
                randomStr.append((char) (random.nextInt(26) + temp));
            } else {
                randomStr.append(String.valueOf(random.nextInt(10)));
            }
        }
        return randomStr.toString();
    }

    /**
     * 获取位随机数字
     *
     * @param size 生成几位随机数
     * @return 随机字符
     */
    public static String randomNumber(int size) {
        double dSize = Double.valueOf(size);
        int min = (int) Math.pow(10.0, dSize - 1);
        Random r = new Random();
        int rand = r.nextInt(min * 10 - min) + min;
        return String.valueOf(rand);
    }

}

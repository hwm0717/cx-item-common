/**
 * @FileName : com.chengxie.fc.api.utils.StringRandom.java
 * @author : zhan
 * @date : 2017/1/13
 */
package com.cx.item.common.utils;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * @author : zhan
 * @date : 2017/1/13
 */
public class TokenUtil {

    public static String generateMD5(String src) {
        try {
            return DigestUtil.md5Hex(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * tokenstr 时间
     *
     * @param userId
     * @param tokenSalt
     * @return
     */
    public static String generateToken(String userId, String tokenSalt) {
        StringBuilder sb = new StringBuilder();
        String nowTime = String.valueOf(System.currentTimeMillis());
        String timeFlag = String.valueOf(nowTime).substring(nowTime.length() - 6, nowTime.length());
        sb.append(userId).append(tokenSalt).append(timeFlag);
        return generateMD5(sb.toString());
    }
}

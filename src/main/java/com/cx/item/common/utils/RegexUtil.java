package com.cx.item.common.utils;

import java.util.regex.Pattern;

/**
 * Created by hwm on 2018/6/4.
 */
public class RegexUtil {

    /**
     * 验证手机号码11位
     *
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {

        String regex = "(13\\d|14[579]|15[^4\\D]|17[^49\\D]|18\\d)\\d{8}";

        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证电话号码或者手机号码
     * 电话号码正则表达式（支持手机号码，3-4位区号，7-8位直播号码，1－4位分机号）
     * @param mobile
     * @return
     */
    public static boolean checkMobileOrTel(String mobile) {

        String regex = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d)|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d))$)";

        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证日期格式2012-03-25
     *
     * @param dateStr
     * @return
     */
    public static boolean checkDate(String dateStr) {

        String regex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

        return Pattern.matches(regex, dateStr);
    }
}

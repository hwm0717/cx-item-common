package com.cx.item.common.utils;

/**
 * Created by hwm on 2018/6/13.
 */
public class ProfileUtil {

    /**
     * 判断是linux系统还是其他系统
     * 如果是Linux系统，返回true，否则返回false
     */
    public static boolean isLinuxSystem() {

        return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;
    }

}

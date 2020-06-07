package com.automannn.plugin.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class TimeUtils {
    private static volatile TimeUtils timeUtils;

    /**
     * 单例模式
     *
     * @return 实例对象
     */
    public static TimeUtils getInstance() {
        if (timeUtils == null) {
            synchronized (TimeUtils.class) {
                if (timeUtils == null) {
                    timeUtils = new TimeUtils();
                }
            }
        }
        return timeUtils;
    }

    private TimeUtils() {

    }

    /**
     * 获取指定格式的时间字符串
     *
     * @param pattern 格式
     * @return 时间字符串
     */
    public String currTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    /**
     * 获取默认格式的时间字符串（yyyy-MM-dd HH:mm:ss）
     *
     * @return 时间字符串
     */
    public String currTime() {
        return currTime("yyyy-MM-dd HH:mm:ss");
    }
}

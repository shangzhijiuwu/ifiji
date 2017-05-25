package me.iszhenyu.ifiji.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xiaoz
 * @since 2017/5/24
 */
public class TimeUtils {

    private static DateFormat GMT_NORMAL_FORMAT;
    private static DateFormat LOCAL_NORMAL_FORMAT;
    static {
        GMT_NORMAL_FORMAT = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        GMT_NORMAL_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        LOCAL_NORMAL_FORMAT = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
    }

    public static Date gmtNow() {
        Date d = new Date();
        System.out.println(GMT_NORMAL_FORMAT.format(d));
        return d;
    }

    public static void main(String[] args) {
        TimeUtils.gmtNow();
    }

}

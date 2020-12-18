package com.mer.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Program: zq-web-api
 * @Description: 日期工具
 * @Author: 赵旗
 * @Create: 2020-12-18 12:10
 */
public class DateUtils {
    private static String YYYY_MM_DD = "yyyy-MM-dd";
    private static String HH_MM_SS = "HH:mm:ss";
    private static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String yyyyMMddHHmmssSss() {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSS).format(new Date());
    }

    /**
     * 获取 耗时 分，秒
     * @param startTime
     * @param endTime
     */
    public static String getMinSec(String startTime, String endTime) {
        //时间格式，自己可以随便定义
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        //一天的毫秒数
        long nd = 1000 * 60 * 60 * 24;
        //一小时的毫秒数
        long nh = 1000 * 60 * 60;
        //一分钟的毫秒数
        long nm = 1000 * 60;
        //一秒钟的毫秒数
        long ns = 1000;

        long diff = 0;
        try {
            //获取两个时间的毫秒时间差
            diff = format.parse(endTime).getTime() - format.parse(startTime).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //计算相差的天数
        long day = diff / nd;
        //计算相差的小时
        long hour = diff % nd / nh;
        //计算相差的分钟
        long min = diff % nd % nh / nm;
        //计算相差的秒2
        long sec = diff % nd % nh % nm / ns;
//        System.err.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
        return min + "分" + sec + "秒。";
    }


    /**
     * 获取两个时间的时间差(天，小时，分钟，秒)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public static void differenceTime(String startTime, String endTime) {
        //时间格式，自己可以随便定义
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //一天的毫秒数
        long nd = 1000 * 60 * 60 * 24;
        //一小时的毫秒数
        long nh = 1000 * 60 * 60;
        //一分钟的毫秒数
        long nm = 1000 * 60;
        //一秒钟的毫秒数
        long ns = 1000;

        long diff = 0;
        try {
            //获取两个时间的毫秒时间差
            diff = format.parse(endTime).getTime() - format.parse(startTime).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //计算相差的天数
        long day = diff / nd;
        //计算相差的小时
        long hour = diff % nd / nh;
        //计算相差的分钟
        long min = diff % nd % nh / nm;
        //计算相差的秒2
        long sec = diff % nd % nh % nm / ns;
        System.err.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
    }

    public static void main(String[] args) {
        differenceTime("2020-12-18 14:11:12","2020-12-18 14:16:22");
    }

}

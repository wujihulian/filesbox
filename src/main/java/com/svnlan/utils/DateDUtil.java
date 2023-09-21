package com.svnlan.utils;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 时间工具类
 * @author
 * @date
 */
public class DateDUtil {

    public static String yyyy = "yyyy";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String yyyy_MM_dd = "yyyy-MM-dd";
    public static String yyyyMMdd = "yyyyMMdd";
    public static String yyyyMM = "yyyyMM";
    public static String yyyy_MM = "yyyy-MM";
    public static String yyyy_MM_dd_HH_00 = "yyyy-MM-dd HH:00";
    public static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static String yyyyMMddHHmm = "yyyyMMddHHmm";
    public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static String yyMMdd = "yyMMdd";
    public static String yyyy_MM_dd_00_00 = "yyyy-MM-dd 00:00";

    public static String HH_mm = "HH:mm";
    public static String HH_mm_ss = "HH:mm:ss";

    /**
     * @description: 时间转字符串
     * @param date
     * @param format
     * @return java.lang.String
     */
    public static String DateToString(Date date, String format) {
        try {
            DateFormat dateFormatter = new SimpleDateFormat(format);
            return dateFormatter.format(date);
        } catch (Exception e) {
            LogUtil.error(String.format("@@@DateDUtil.DateToString: date=%tx format=%s cause=%s", date, format, e.getMessage()));
        }
        return null;
    }

    /**
     * @description: 得当前时间
     * @param
     * @return java.lang.String
     */
    public static String getCurrentTime() {
        try {
            String nowTime = DateDUtil.DateToString(new Date(), YYYY_MM_DD_HH_MM_SS);
            return nowTime;
        } catch (Exception e) {
            LogUtil.error(String.format("@@@DateDUtil.getCurrentTime: cause=%s", e.getMessage()));
        }
        return null;
    }

    /**
     * @description: 将字符串时间改成Date类型
     * @param format
     * @param dateStr
     * @return java.util.Date
     */
    public  static Date strToDate(String format,String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * @description: 返回某时间加几天后的指定格式的时间字符串
     * @param format
     * @param date
     * @param plus
     * @return java.lang.String
     */
    public static String getPlusDaysStr(String format, Date date, long plus){

        Date date1 = getPlusDays(date, plus);
        return DateToStr(format, date1);
    }

    /**
     * 将Date时间转成字符串
     * @param format
     * @param date
     * @return
     */
    public static String DateToStr(String format,Date date){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * @description: Date按指定格式返回（用于截取对应的日期值（如：HH:mm），以保持一致）
     * @param date
     * @param format
     * @return java.util.Date
     */
    public static Date dateFormat(Date date, String format) {
        Date newDate = null;
        try {
            String dateStr = DateDUtil.DateToString(date,format);
            if(null == dateStr) return null;
            newDate = DateDUtil.strToDate(format, dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * @description: 给出时间添加几个分钟后的时间
     * @param format
     * @param date
     * @param plus
     * @return java.util.Date
     */
    public static Date getPlusMinutes(String format, Date date, long plus){
        long time = date.getTime()+ plus*60*1000;
        return DateDUtil.dateFormat(new Date(time), format);
    }

    /**
     * @description: 获取2个日期的天数差
     * @param l_startDate
     * @param l_endDate
     * @return long
     */
    public static long getDaysOfTowDiffDate( Date l_startDate, Date l_endDate ){

        long l_startTime = l_startDate.getTime();
        long l_endTime = l_endDate.getTime();
        long betweenDays = (long) ( ( l_endTime - l_startTime ) / ( 1000 * 60 * 60 * 24 ) );
        return betweenDays;
    }

    /**
     * @description: 增加几天后的时间
     * @param format
     * @param date
     * @param plus
     * @return java.util.Date
     */
    public static Date getPlusDays(String format, Date date, long plus){
        long time = date.getTime()+ plus * 1000 * 60 * 60 * 24;
        return DateDUtil.dateFormat(new Date(time), format);
    }

    /**
     * @description: 返回某时间加几天后的日期
     * @param date
     * @param plus
     * @return java.util.Date
     */
    public static Date getPlusDays(Date date, long plus) {

        long time = date.getTime() + plus * 24 * 60 * 60 * 1000;
        Date date1 = new Date(time);
        return date1;
    }

    /**
     * @description: 判断该日期是否是该月的第一天
     * @param date
     * @return boolean
     */
    public static boolean isFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * @description: 判断该日期是否是该月的最后一天
     * @param date
     * @return boolean
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * @description: 查开始日期到结束日期的日期集合
     * @param startDate
     * @param endDate
     * @return java.util.List<java.util.Date>
     */
    public static List<Date> findIntervalDateList(Date startDate, Date endDate) {
        return findIntervalDateList(startDate, endDate, Calendar.DATE);
    }

    /**
     * @description: 查开始日期到结束日期的日期集合
     * @param startDate
     * @param endDate
     * @param field
     * @return java.util.List<java.util.Date>
     */
    public static List<Date> findIntervalDateList(Date startDate, Date endDate, int field) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (calendar.getTime().getTime() <= endDate.getTime()) {
            Date tempDate = calendar.getTime();
            dateList.add(tempDate);
            calendar.add(field, 1);
        }
        return dateList;
    }
    /**
     * 获取指定时间的“年-月-日 HH:mm:ss”信息
     */

    public static String getYearMonthDayHMS(Date date, String resultDateFormat) {
        if (date == null) {
            return "";
        }
        DateFormatter dateFormatter = new DateFormatter(resultDateFormat);
        String dateTo = dateFormatter.print(date, Locale.CHINA);
        return dateTo;
    }

    /**
     * 将int类型数字转换成时分秒毫秒的格式数据
     *
     * @param timeParam long类型的数据  单位：毫秒
     * @return HH:mm:ss.SSS
     */
    public static String msecToTime(Long timeParam) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int millisecond = 0;
        int time = ObjectUtils.isEmpty(timeParam) ? 0 : timeParam.intValue();
        if (time <= 0) {
            return "00:00:00.000";
        }else {
            second = time / 1000;
            minute = second / 60;
            millisecond = time % 1000;
            if (second < 60) {
                timeStr = "00:00:" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else if (minute < 60) {
                second = second % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else {// 数字>=3600 000的时候
                hour = minute / 60;
                minute = minute % 60;
                second = second - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) + "."
                        + unitFormat2(millisecond);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {// 时分秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    public static String unitFormat2(int i) {// 毫秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "00" + Integer.toString(i);
        else if (i >= 10 && i < 100) {
            retStr = "0" + Integer.toString(i);
        } else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 时间戳转字符串日期
     * @param date
     * @param format
     * @return
     */
    public static String LongTimeToString(Long date, String format) {
        if (String.valueOf(date).length() == 10){
            date = date * 1000;
        }
        try {
            DateFormat dateFormatter = new SimpleDateFormat(format);
            return dateFormatter.format(date);
        } catch (Exception e) {
            LogUtil.error(String.format("@@@DateDUtil.DateToString: date=%tx format=%s cause=%s", date, format, e.getMessage()));
        }
        return null;
    }
}

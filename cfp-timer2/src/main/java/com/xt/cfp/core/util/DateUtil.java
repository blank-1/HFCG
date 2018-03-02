package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Date Utilities
 *
 * @version 1.0
 */

public class DateUtil {
    static SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat sdfLongCn = new SimpleDateFormat("yyyy年MM月dd日");
    static SimpleDateFormat sdfShortU = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
    static SimpleDateFormat sdfLongU = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);
    static SimpleDateFormat sdfLongTime = new SimpleDateFormat("yyyyMMddHHmmss");
    static SimpleDateFormat sdfLongTimePlus = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat sdfShortLongTimePlusCn = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    static SimpleDateFormat sdfLongTimePlusMill = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
    static SimpleDateFormat sdfMd = new SimpleDateFormat("MM月dd日");
    static SimpleDateFormat sdfYm = new SimpleDateFormat("yyyy-MM");
    static SimpleDateFormat sdfLongCnhs = new SimpleDateFormat("MM月dd日 HH:mm");
    public static final int PATTERNTYPE_DATE = 0;
    public static final int PATTERNTYPE_TIME = 1;

    public DateUtil() {
    }

    /**
     * @return String
     * @throws Exception
     * @author caizheng
     * get Date format Example：2008-05-15
     */
    public static String getDateLong(Date date) {
        String nowDate = "";
        try {
            if (date != null)
                nowDate = sdfLong.format(date);
            return nowDate;
        } catch (Exception e) {
            System.out.println("Error at getDate:" + e.getMessage());
            return "";
        }
    }

    /**
     * @return String
     * @throws Exception
     * @author caizheng
     * get Date format Example：05月21日 12:50
     */
    public static String getDateLongMD(Date date) {
        String nowDate = "";
        try {
            if (date != null)
                nowDate = sdfLongCnhs.format(date);
            return nowDate;
        } catch (Exception e) {
            System.out.println("Error at getDate:" + e.getMessage());
            return "";
        }
    }
    
    /**
     * 日期增加
     *
     * @param date
     * @param column
     * @param value
     * @return
     */
    public static Date addDate(Date date, int column, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(column, value);
        return calendar.getTime();
    }
    
    /**
     * 增加年
     * @param date
     * @param value
     * @return
     */
    public static Date addYear(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, value);
        return calendar.getTime();
    }
    
    /**
     * 增加月
     * @param date
     * @param value
     * @return
     */
    public static Date addMonth(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, value);
        return calendar.getTime();
    }
    
    /**
     * 增加日
     * @param date
     * @param value
     * @return
     */
    public static String getDateLongCn(Date date) {
        String nowDate = "";
        try {
            if (date != null)
                nowDate = sdfLongCn.format(date);
            return nowDate;
        } catch (Exception e) {
            System.out.println("Error at getDate:" + e.getMessage());
            return "";
        }
    }
    
    /**
     * 增加秒
     * @param date
     * @param value
     * @return
     */
    public static Date addSecond(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, value);
        return calendar.getTime();
    }
    /**
     * @return String
     * @throws Exception
     * @author caizheng
     * get Date format Example：05月15日
     */
    public static String getDateLongCnD(Date date) {
        String nowDate = "";
        try {
            if (date != null)
                nowDate = sdfMd.format(date);
            return nowDate;
        } catch (Exception e) {
            System.out.println("Error at getDate:" + e.getMessage());
            return "";
        }
    }

    /**
     * @return String
     * @throws Exception
     * @author caizheng
     * get Date format Example：2008年-05月-15日 11:05
     */
    public static String getDateShortLongTimeCn(Date date) {
        String nowDate = "";
        try {
            if (date != null)
                nowDate = sdfShortLongTimePlusCn.format(date);
            return nowDate;
        } catch (Exception e) {
            System.out.println("Error at getDate:" + e.getMessage());
            return "";
        }
    }

    /**
     * get current date,fomart:YYYYMMDDHHMISS
     *
     * @return String
     * @throws Exception
     */
    public static String getNowLongTime() throws Exception {
        String nowTime = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new Date().getTime());
            nowTime = sdfLongTime.format(date);
            return nowTime;
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * get current date,fomart:YYYYMMDDHHMISS
     *
     * @return String
     * @throws Exception
     */
    public static String getNowLongTime(Date date){
        String nowTime = "";
        try {
            nowTime = sdfLongTime.format(date);
            return nowTime;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * get current date,fomart:YYYYMMDD
     *
     * @return String
     * @throws Exception
     */
    public static String getNowShortDate() throws Exception {
        String nowDate = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new Date().getTime());
            nowDate = sdfShort.format(date);
            return nowDate;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * get current date,fomart:YYYY-MM-DD
     *
     * @return String
     * @throws Exception
     */
    public static String getNowFormateDate() throws Exception {
        String nowDate = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new Date().getTime());
            nowDate = sdfLong.format(date);
            return nowDate;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * get current date,fomart:yyyy-MM-dd HH:mm:ss
     *
     * @return String
     * @throws Exception
     */
    public static String getNowPlusTime() throws Exception {
        String nowDate = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new Date().getTime());
            nowDate = sdfLongTimePlus.format(date);
            return nowDate;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * get specified date,fomart:yyyy-MM-dd HH:mm:ss
     *
     * @return String
     * @throws Exception
     */
    public static String getPlusTime(Date date) throws Exception {
        if (date == null) return null;
        try {
            String nowDate = sdfLongTimePlus.format(date);
            return nowDate;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取年月
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String getYearMonth(Date date) throws Exception {
        if (date == null) return null;
        try {
            String nowDate = sdfYm.format(date);
            return nowDate;
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * difference between months
     *
     * @param dealMonth
     * @param alterMonth
     * @return alterMonth
     */
    public static int calBetweenTwoMonth(String dealMonth, String alterMonth) {
        int length = 0;
        if ((dealMonth.length() != 6) || (alterMonth.length() != 6)) {
            length = -1;
        } else {
            int dealInt = Integer.parseInt(dealMonth);
            int alterInt = Integer.parseInt(alterMonth);
            if (dealInt < alterInt) {
                length = -2;
            } else {
                int dealYearInt = Integer.parseInt(dealMonth.substring(0, 4));
                int dealMonthInt = Integer.parseInt(dealMonth.substring(4, 6));
                int alterYearInt = Integer.parseInt(alterMonth.substring(0, 4));
                int alterMonthInt = Integer.parseInt(alterMonth.substring(4, 6));
                length = (dealYearInt - alterYearInt) * 12 + (dealMonthInt - alterMonthInt);
            }
        }
        return length;
    }

    /**
     * difference between days
     *
     * @param newDate
     * @param oldDate
     * @return newDate-oldDate
     */
    public static int daysBetweenDates(Date newDate, Date oldDate) {
        int days = 0;
        Calendar calo = Calendar.getInstance();
        Calendar caln = Calendar.getInstance();
        calo.setTime(oldDate);
        caln.setTime(newDate);
        int oday = calo.get(Calendar.DAY_OF_YEAR);
        int nyear = caln.get(Calendar.YEAR);
        int oyear = calo.get(Calendar.YEAR);
        while (nyear > oyear) {
            calo.set(Calendar.MONTH, 11);
            calo.set(Calendar.DATE, 31);
            days = days + calo.get(Calendar.DAY_OF_YEAR);
            oyear = oyear + 1;
            calo.set(Calendar.YEAR, oyear);
        }
        int nday = caln.get(Calendar.DAY_OF_YEAR);
        days = days + nday - oday;

        return days;
    }

    public static String getFormattedDateUtil(Date dtDate, String strFormatTo) {
        if (dtDate == null) {
            return "";
        }
        strFormatTo = strFormatTo.replace('/', '-');
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(strFormatTo);
            return formatter.format(dtDate);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获得指定日期的年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得指定日期的月
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得指定日期的日
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 将字符串解析为日期对象
     *
     * @param dateStr
     * @param formatter
     * @return
     */
    public static Date parseStrToDate(String dateStr, String formatter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatter);
        try {
            Date parse = dateFormat.parse(dateStr);
            return parse;
        } catch (ParseException e) {
            throw new SystemException(ValidationErrorCode.ERROR_STR_NOT_DATE);
        }
    }

    public static boolean before(Date d1, Date d2, int patternType) {
        if (patternType == PATTERNTYPE_DATE) {
            Calendar cd1 = new GregorianCalendar();
            cd1.setTime(d1);
            Calendar c1 = new GregorianCalendar();
            c1.set(Calendar.YEAR, cd1.get(Calendar.YEAR));
            c1.set(Calendar.MONTH, cd1.get(Calendar.MONTH));
            c1.set(Calendar.DAY_OF_YEAR, cd1.get(Calendar.DAY_OF_YEAR));
            c1.set(Calendar.HOUR, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);

            Calendar cd2 = new GregorianCalendar();
            cd2.setTime(d2);
            Calendar c2 = new GregorianCalendar();
            c2.set(Calendar.YEAR, cd2.get(Calendar.YEAR));
            c2.set(Calendar.MONTH, cd2.get(Calendar.MONTH));
            c2.set(Calendar.DAY_OF_YEAR, cd2.get(Calendar.DAY_OF_YEAR));
            c2.set(Calendar.HOUR, 0);
            c2.set(Calendar.MINUTE, 0);
            c2.set(Calendar.SECOND, 0);
            return c1.getTime().before(c2.getTime());

        } else {
            return d1.before(d2);
        }
    }

    /**
     * 计算两个日期之间相差的天数，如果返回负数，表示计算有异常
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(sdf.format(smdate)));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(sdf.format(bdate)));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 计算两个日期之间相差的秒数数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差秒数数
     * @throws ParseException
     */
    public static int secondBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        if (time1>=time2)
            return 0;
        long between_second = (time2 - time1) / 1000;

        return Integer.parseInt(String.valueOf(between_second));
    }

    public static boolean equal(Date d1, Date d2, int patternType) {
        if (patternType == PATTERNTYPE_DATE) {
            Calendar cd1 = new GregorianCalendar();
            cd1.setTime(d1);
            Calendar c1 = new GregorianCalendar();
            c1.set(Calendar.YEAR, cd1.get(Calendar.YEAR));
            c1.set(Calendar.MONTH, cd1.get(Calendar.MONTH));
            c1.set(Calendar.DAY_OF_YEAR, cd1.get(Calendar.DAY_OF_YEAR));

            Calendar cd2 = new GregorianCalendar();
            cd2.setTime(d2);
            Calendar c2 = new GregorianCalendar();
            c2.set(Calendar.YEAR, cd2.get(Calendar.YEAR));
            c2.set(Calendar.MONTH, cd2.get(Calendar.MONTH));
            c2.set(Calendar.DAY_OF_YEAR, cd2.get(Calendar.DAY_OF_YEAR));
            return c1.getTime().equals(c2.getTime());

        } else {
            return d1.equals(d2);
        }
    }

    public static boolean after(Date d1, Date d2, int patternType) {
        if (patternType == PATTERNTYPE_DATE) {
            Calendar cd1 = new GregorianCalendar();
            cd1.setTime(d1);
            Calendar c1 = new GregorianCalendar();
            c1.set(Calendar.YEAR, cd1.get(Calendar.YEAR));
            c1.set(Calendar.MONTH, cd1.get(Calendar.MONTH));
            c1.set(Calendar.DAY_OF_YEAR, cd1.get(Calendar.DAY_OF_YEAR));

            Calendar cd2 = new GregorianCalendar();
            cd2.setTime(d2);
            Calendar c2 = new GregorianCalendar();
            c2.set(Calendar.YEAR, cd2.get(Calendar.YEAR));
            c2.set(Calendar.MONTH, cd2.get(Calendar.MONTH));
            c2.set(Calendar.DAY_OF_YEAR, cd2.get(Calendar.DAY_OF_YEAR));
            return c1.getTime().after(c2.getTime());

        } else {
            return d1.after(d2);
        }
    }

    public static Date getShortDateWithZeroTime(Date param) {
        return parseStrToDate(sdfLong.format(param), "yyyy-MM-dd");
    }

    public static void main(String[] args) throws ParseException {
        Date date = DateUtil.parseStrToDate("2015-10-31", "yyyy-MM-dd");

        System.out.println(DateUtil.daysBetween(date, new Date()));
        System.out.println(DateUtil.daysBetweenDates(date, new Date()));
    }
    
    /**
     *  返回当天剩余的时间（seconds）
     * @throws ParseException 
     * */
	public static int getLeftTimesToday() throws ParseException{
    	Date date = new Date();
    	String now = sdfLongTime.format(date);
    	String end = sdfShort.format(date) + "235959";
    	return secondBetween(sdfLongTime.parse(now),sdfLongTime.parse(end));
    }

}

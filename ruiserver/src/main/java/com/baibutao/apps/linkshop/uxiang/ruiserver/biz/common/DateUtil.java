package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author niepeng
 *
 * @date 2012-9-5 下午6:46:31
 */
public class DateUtil extends wint.lang.utils.DateUtil{
	
	public static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DEFAULT_DATE_FMT_NODATE = "HH:mm:ss";
	
	public static final String DEFAULT_DATE_FMT_NO = "yyyy-MM-dd";

	public static final String DEFAULT_DATE_FMT_NOSS = "yyyy-MM-dd HH:mm";
	
	public static final String DATE_FMT_YMD = "yyyy年MM月dd日";
	
	public static final String DATE_FMT_MD = "MM月dd日"; 
	
	public static final String DATE_FMT_MD_HM = "MM月dd日 HH:mm"; 
	
	public static final String DATE_YMDHMS = "yyyyMMddHHmmss";
 
	
	public static Date parse(String input) {
		return parse(input, DEFAULT_DATE_FMT);
	}
	
	public static Date parseNoException(String input) {
		try {
			return parse(input, DEFAULT_DATE_FMT);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date parseNoSSNoException(String input) {
		try {
			return parse(input, DEFAULT_DATE_FMT);
		} catch (Exception e) { 
			try {
				return parse(input, DEFAULT_DATE_FMT_NOSS);
			} catch (Exception e1) {
				return null;
			}
		}
	}
	
	public static String format(Date date, String fmt) {
		try {
		    if(date == null){
		       return ""; 
		    }
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String format(Date date) {
		return format(date, DEFAULT_DATE_FMT);
	}
	
	public static String formatNoException(Date date) {
		try {
			return format(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatNoTimeNoException(Date date) {
		try {
			return format(date,DEFAULT_DATE_FMT_NO);
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatNoDateNoException(Date date) {
		try {
			return format(date, DEFAULT_DATE_FMT_NODATE);
		} catch (Exception e) { 
			return null;
		}
	}
	
	public static String formatNoYearAndNoTime(Date date){
	    try {
            return format(date, DATE_FMT_MD);
        } catch (Exception e) { 
            return null;
        }
	}
	
	public static String formatToYMDNoException(Date date){
	    try {
	        return format(date, DATE_FMT_YMD);
	    } catch (Exception e) { 
	        return null;
	    }
	}
	
	public static Date parse(String input, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			return sdf.parse(input);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Date formatCurrentDayMax(Date date) {
		if(date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	
	public static Date formatCurrentDayMin(Date date) {
		if(date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	public static int dayOfWeek(Date date) {
		if(date == null) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date currentMonthMin(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 计算月份
	 * @param date
	 * @param changeValue  正数为增加，负数为减去
	 * @return
	 */
	public static Date changeMonth(Date date , int changeValue) {
		if(date == null || changeValue == 0) {
			return date;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, changeValue);
		return c.getTime();
	}
	
	/**
	 * 计算天
	 * @param date
	 * @param changeValue  正数为增加，负数为减去
	 * @return
	 */
	public static Date changeDay(Date date , int changeValue) {
		if(date == null || changeValue == 0) {
			return date;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, changeValue);
		return c.getTime();
	}
	
	/**
	 * 计算秒
	 * @Title: changeMin 
	 * @param date
	 * @param changeValue
	 * @return Date  
	 * @throws
	 */
	public static Date changeMin(Date date, int changeValue) {
		if (date == null || changeValue == 0) {
			return date;
		}
		long flag = changeValue * 60 * 1000;
		return new Date(date.getTime() + flag);
	}

	/**
	 * @Title: getYear 
	 * @Description: 获取年份
	 * @param date
	 * @return int  
	 * @throws
	 */
	public static int getYear(Date date){
	    Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        
        return year;
	}
	
	/**
	 * @Title: getYear 
	 * @Description: 获取月份
	 * @param date
	 * @return int  
	 * @throws
	 */
	public static int getMonth(Date date){
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    int month = c.get(Calendar.MONTH) + 1;
	    
	    return month;
	}
	
	/**
	 * @Title: getYear 
	 * @Description: 获日期
	 * @param date
	 * @return int  
	 * @throws
	 */
	public static int getDay(Date date){
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    int day = c.get(Calendar.DATE);
	    
	    return day;
	}
	
	/**
	 * 获取date日期的月的第一天
	 * @Title: getMonthBeginDate 
	 * @Description: 获取date日期的月的第一天
	 * @param date
	 * @return Date  
	 * @throws
	 */
	public static Date getMonthBeginDate(Date date){
	    Calendar cal = Calendar.getInstance();
	    // 当前月的第一天
	    cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date beginTime = cal.getTime();
        
        return beginTime;
	}
	
	/**
	 * 获取date日期的月的最后一天
	 * @Title: getMonthEndDate 
	 * @Description: 获取date日期的月的最后一天
	 * @param date
	 * @return Date  
	 * @throws
	 */
	public static Date getMonthEndDate(Date date){
	    Calendar cal = Calendar.getInstance();
        // 当前月的最后一天
	    cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        cal.roll(Calendar.DATE, -1);
        Date endTime = cal.getTime();
        
        return endTime;
	}
}
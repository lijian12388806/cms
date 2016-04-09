package com.xkw.utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: DateUtil</p>
 * <p>Company: Enorbus</p>
 */

public class DateUtils {
	
	public static final int MSECONDS_PER_DAY = 24 * 60 * 60 * 1000;
	public static final int MSECONDS_PER_HOUR = 60 * 60 * 1000;
	public static final int MSECONDS_PER_MINUTES = 60 * 1000;
	public static final int WORKING_HOURS_PER_DAY = 8;
	
	public static java.util.Date getDaysIntervalDateBegin(int days) {
		java.util.Date intervalDate = getDaysIntervalDate(days);
		
		String dateStr = getDateStr(intervalDate)+" 00:00:00";
		
		return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static java.util.Date getDaysIntervalDateEnd(int days) {
		java.util.Date intervalDate = getDaysIntervalDate(days);
		
		String dateStr = getDateStr(intervalDate)+" 23:59:59";
		
		return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}
	
	

	/**
	 * 获取系统当前日期
	 * 
	 * @return java.sql.Date 系统当前日期
	 */
	public static Date getSysDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = Date.valueOf(df.format(new java.util.Date()));
		return d;
	}
	
	/**
	 * 当前系统时间零时
	 * @return
	 */
	public static Timestamp getSysDateMidnight() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = Date.valueOf(df.format(new java.util.Date()));
		return new Timestamp(d.getTime());
	}
	
	/**
	 * 得到周一 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp getSysMondayMidnight() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = Date.valueOf(df.format(new java.util.Date()));
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());	
		return new Timestamp(c.getTimeInMillis());		
	}
	/**
	 * 得到月第一天 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp getSysMonthMidnight() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = Date.valueOf(df.format(new java.util.Date()));
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, 1);	
		return new Timestamp(c.getTimeInMillis());		
	}
	

	/**
	 * @param dateStr
	 * @return
	 */
	public static Date getSysDate(String dateStr) {
		DateFormat df = new SimpleDateFormat(dateStr);
		Date d = Date.valueOf(df.format(new java.util.Date()));
		return d;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return Time 系统当前日期
	 */
	public static Time getSysTime() {
		java.util.Date d = new java.util.Date();

		return new Time(d.getTime());
	}
	
	/**
	 * 四舍五入为second
	 * @return
	 */
	public static long getSysTimeInSeconds() {
		return (new java.util.Date().getTime()+500)/1000;
	}	
	
	public static long getDateInSeconds(java.util.Date date) {
		return (date.getTime()+500)/1000;
	}

	/**
	 * 获取系统当前时间戳
	 * 
	 * @return java.sql.Date 系统当前日期
	 */
	public static Timestamp getSysTimestamp() {
		java.util.Date d = new java.util.Date();

		return new Timestamp(d.getTime());
	}
	

	/**
	 * 取yyyy-mm-dd格式的当前日期字符串
	 */
	public static String getSysDateStr() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new java.util.Date());
	}

	/**
	 * 取年月日时分 bot创建目录的时候需要
	 * 
	 * @return 0605201534
	 */
	public static String getSysDateTimeStr() {
		DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
		return df.format(new java.util.Date());
	}

	/**
	 * 格式化的当前日期
	 * 
	 * @return
	 */
	public static String getSysDateTimeStrFormat() {
		DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
		return df.format(new java.util.Date());
	}
	
	public static String getSysTimeStrFormat() {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(new java.util.Date());
	}
	
	public static String getSysTimeMicroStrFormat() {
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		return df.format(new java.util.Date());
	}
	
	

	/**
	 * 将时间格式化成yyyy-MM的格式
	 * 
	 * @param date
	 * @return String
	 */
	public static String getDateMonthStr(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		return df.format(date);
	}

	/**
	 * 取指定java.sql.Date的yyyy-mm-dd格式的字符串
	 */
	public static String getDateStr(Date date) {
		return getDateTimeFormatStr(date, "yyyy-MM-dd");
	}

	public static String getDateTimeStr(java.util.Date date) {
		return getDateTimeFormatStr(date, "yyyy-MM-dd HH:mm");
	}
	
	public static String getDateTimeHtmStr(java.util.Date date) {
		return getDateTimeFormatStr(date, "yy-MM-dd HH:mm:ss");
	}
	
	
	public static String getDateTimeFormatStr(java.util.Date date, String formatter) {
		DateFormat df = new SimpleDateFormat(formatter);
		return df.format(date);
	}	

	/**
	 * 取指定java.util.Date的yyyy-mm-dd格式的字符串
	 */
	public static String getDateStr(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null) {
			return "";
		} else {
			return df.format(date);
		}
	}

	public static String getDateFormatStr(java.util.Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 取指定java.util.Date的yyyy-mm-dd格式的字符串
	 */
	public static String getDateStr(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(Date.valueOf(date));
	}

	/**
	 * 取指定java.util.Date的yyyy-mm-dd格式的字符串
	 */
	public static Date valueOf(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null) {
			return null;
		} else {
			return Date.valueOf(df.format(date));
		}

	}

	/**
	 * 取指定java.util.Date的yyyy-mm-dd格式的字符串
	 */
	public static Date valueOf(String date) {
		return valueOf(Date.valueOf(date));
	}

	/**
	 * 取指定java.util.Date的yyyy-mm-dd格式的字符串
	 */
	public static Date valueOf(Date date, String dateFormatModel) {
		DateFormat df = new SimpleDateFormat(dateFormatModel);
		if (date == null) {
			return null;
		} else {
			return Date.valueOf(df.format(date));
		}
	}
	
	public static Date getDateFromSeconds(long seconds) {
		return new Date(seconds*1000);
	}
	
	public static Date getDateFromMicroSeconds(long microSeconds) {
		return new Date(microSeconds);
	}
	

	/**
	 * 得到一个月之前的日子
	 * 
	 * @return String
	 */
	public static java.util.Date getOneMonthBeforeDate() {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.MONTH, -1);
		// cl.add(Calendar.DATE, 1);
		return parseDate(cl);
	}

	public static java.util.Date getOneMonthAfterDate() {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.MONTH, 1);
		return parseDate(cl);
	}

	/**
	 * 得到一年之前的日子
	 * 
	 * @return String
	 */
	public static java.util.Date getOneYearBeforeDate() {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.YEAR, -1);
		cl.add(Calendar.DATE, 1);
		return parseDate(cl);
	}

	/**
	 * 得到指定时间之前或者之后的日子
	 * 
	 * @param pDays
	 * @return
	 */
	// public static Date getDaysBeforeDate(int pDays) {
	// Calendar cl = Calendar.getInstance();
	// cl.add(Calendar.DATE, -pDays);
	// return parseDate(cl);
	// }
	/**
	 * 得到指定的下一月时间 如 2005-12-01 -> 2006-01-01
	 * 
	 * @param pFromDate
	 * @return
	 */
	public static java.util.Date getNextMonthFirstDay(java.util.Date pFromDate) {
		Calendar cl = Calendar.getInstance();
		// 设置时间
		cl.setTime(pFromDate);
		cl.add(Calendar.MONTH, 1);
		return parseDate(cl);
	}

	public static java.util.Date getPrevMonthFirstDay(java.util.Date pFromDate) {
		Calendar cl = Calendar.getInstance();
		// 设置时间
		cl.setTime(pFromDate);
		cl.add(Calendar.MONTH, -1);
		return parseDate(cl);
	}

	/**
	 * get the last day of some month
	 * 
	 * @param date
	 * @return
	 */
	public static java.util.Date getLastDayOfMonth(Date date) {
		Calendar cl = Calendar.getInstance();
		if (date != null)
			cl.setTime(date);
		cl.add(Calendar.MONTH, 1);
		cl.add(Calendar.DATE, -cl.get(Calendar.DAY_OF_MONTH));
		return parseDate(cl);
	}

	/**
	 * get the last day of current month
	 * 
	 * @return
	 */
	public static java.util.Date getLastDayOfMonth() {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.MONTH, 1);
		cl.add(Calendar.DATE, -cl.get(Calendar.DAY_OF_MONTH));
		return parseDate(cl);
	}

	/**
	 * get the last day of some month
	 * 
	 * @param dateStr
	 * @return
	 */
	public static java.util.Date getLastDayOfMonth(String dateStr) {
		Calendar cl = Calendar.getInstance();
		if (dateStr != null)
			cl.setTime(DateUtils.getSysDate(dateStr));
		cl.add(Calendar.MONTH, 1);
		cl.add(Calendar.DATE, -cl.get(Calendar.DAY_OF_MONTH));
		return parseDate(cl);
	}

	/**
	 * get the first day of some month
	 * 
	 * @param date
	 * @return
	 */
	public static java.util.Date getFirstDayOfMonth(Date date) {
		Calendar cl = Calendar.getInstance();
		if (date != null)
			cl.setTime(date);
		cl.add(Calendar.DATE, -cl.get(Calendar.DAY_OF_MONTH) + 1);
		return parseDate(cl);
	}

	/**
	 * get the first day of current month
	 * 
	 * @return
	 */
	public static java.util.Date getFirstDayOfMonth() {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DATE, -cl.get(Calendar.DAY_OF_MONTH) + 1);
		return parseDate(cl);
	}

	/**
	 * get the first day of some month
	 * 
	 * @param dateStr
	 * @return
	 */
	public static java.util.Date getFirstDayOfMonth(String dateStr) {
		Calendar cl = Calendar.getInstance();
		if (dateStr != null)
			cl.setTime(DateUtils.getSysDate(dateStr));
		cl.add(Calendar.DATE, -cl.get(Calendar.DAY_OF_MONTH) + 1);
		return parseDate(cl);
	}

	/**
	 * get days of current month
	 * 
	 * @return
	 */
	public static int getDaysOfMonth() {
		Calendar cl = Calendar.getInstance();
		int daysOfMonth = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
		return daysOfMonth;
	}

	/**
	 * convert a Calender to a Date
	 * 
	 * @param calendar
	 * @return
	 */
	public static Date parseDateOld(Calendar calendar) {
		if (calendar == null)
			return null;
		Date date = null;
		java.util.Date udate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(udate);
		date = Date.valueOf(dateStr);
		return date;
	}

	public static java.util.Date parseDate(Calendar calendar) {
		if (calendar == null)
			return null;
		Date date = null;
		java.util.Date udate = calendar.getTime();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String dateStr = sdf.format(udate);
		
		
		//date = new Date(udate.getTime());
		
		return udate;
	}

	public static java.util.Date parseDate(String pDateStr) {
		return parseDate(pDateStr, null);
	}

	public static java.util.Date parseDate(String pDateStr, String pFormatStr) {
		java.util.Date retDate = null;
		if (pFormatStr == null) {
			pFormatStr = "yyyy-MM-dd";
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat(pFormatStr);
		try {
			retDate = sdf.parse(pDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// retDate = Date.valueOf(sdf.format(retDate));
		return retDate;
	}

	/**
	 * 得到指定时间之前或者之后的日子
	 * 
	 * @param pDays
	 * @return
	 */
	public static java.util.Date getDaysIntervalDate(int pDays) {
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DATE, pDays);
		return parseDate(cl);
	}
	
	

	public static java.util.Date getDaysIntervalDate(java.util.Date pDate, int pDays) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(pDate);
		cl.add(Calendar.DATE, pDays);
		return parseDate(cl);
	}
	
	public static java.util.Date getMinutesIntervalDate(java.util.Date pDate, int minutes) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(pDate);
		cl.add(Calendar.MINUTE, minutes);
		return parseDate(cl);
	}
	
	public static java.util.Date getMillisecondIntervalDate(java.util.Date pDate, int milliseconds) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(pDate);
		cl.add(Calendar.MILLISECOND, milliseconds);
		return parseDate(cl);
	}
	
	

	/**
	 * 取得日期数据信息 参数 interval 表示数据类型 y 年 m月 d日 w星期 h时 n分 s秒
	 * 
	 * @param myDateStr
	 *            ,interval
	 * @return partStr
	 */
	public static String datePart(String myDateStr, char interval) {
		Date myDate = DateUtils.valueOf(myDateStr);
		String partStr = "";
		String[] Week = { "Su", "M", "Tu", "W", "Th", "Fr", "Sa" };
		switch (interval) {
		case 'y':
			partStr = String.valueOf(myDate.getYear());
			break;
		case 'm':
			partStr = String.valueOf(myDate.getMonth()) + 1;
			break;
		case 'd':
			partStr = String.valueOf(myDate.getDate());
			break;
		case 'w':
			partStr = String.valueOf(Week[myDate.getDay()]);
			break;
		case 'h':
			partStr = String.valueOf(myDate.getHours());
			break;
		case 'n':
			partStr = String.valueOf(myDate.getMinutes());
			break;
		case 's':
			partStr = String.valueOf(myDate.getSeconds());
			break;
		}
		return partStr;
	}

	public static java.util.Date getSecondIntervalDate(java.util.Date pDate, int seconds) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(pDate);
		cl.add(Calendar.SECOND, seconds);
		return parseDate(cl);
	}
	
	
	/**
	 * 求两个时间的天数差 日期格式为 YYYY-MM-dd
	 * 
	 * @param dateOne
	 *            ,dateTwo
	 * @return
	 */
	public static long daysBetween(String dateOne, String dateTwo) {
		String[] splitDateOne = dateOne.split("-");
		String[] splitDateTwo = dateTwo.split("-");
		long cha = ((Date.parse(splitDateOne[1] + '/' + splitDateOne[2] + '/'
				+ splitDateOne[0]) - Date.parse(splitDateTwo[1] + '/'
				+ splitDateTwo[2] + '/' + splitDateTwo[0])) / 86400000);
		return Math.abs(cha);
	}

	/**
	 * 求两个时间的天数差 日期格式为 YYYY-MM-dd
	 * 
	 * @param dateOne
	 *            ,dateTwo 0 >0后者大 <0前者大
	 * @return
	 */
	public static double daysBetween(java.util.Date date1, java.util.Date date2) {
		double time1, time2;
		time1 = date1.getTime();
		time2 = date2.getTime();
		return (time2 - time1) / MSECONDS_PER_DAY;
	}
	
	public static double minutesBetween(java.util.Date date1, java.util.Date date2) {
		double time1, time2;
		time1 = date1.getTime();
		time2 = date2.getTime();
		return (time2 - time1) / MSECONDS_PER_MINUTES;
	}
	
	public static int getMonthOfYear(java.util.Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		int intWeek = cl.get(Calendar.MONTH);
		return intWeek;
	}
	
	
	
	public static int getDayOfMonth(java.util.Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		int intWeek = cl.get(Calendar.DAY_OF_MONTH);
		return intWeek;
	}
	
	
	public static int getYear(java.util.Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		int intWeek = cl.get(Calendar.YEAR);
		return intWeek;
	}	

	/**
	 * 有Calendar判断日期是星期几 返回1234567,1为星期日
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(java.util.Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		int intWeek = cl.get(Calendar.DAY_OF_WEEK);
		return intWeek;
	}
	
	/**
	 * getDayOfWeek 本地化
	 * 
	 * @param date
	 * @return
	 */
	public static int getChineseDayOfWeek(java.util.Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		int intWeek = cl.get(Calendar.DAY_OF_WEEK);		
		return intWeek == Calendar.SUNDAY ?7 : intWeek -1;
	}
	
	public static int getEnglistDayOfWeek(int week) {
		
		return week == 7?1:week+1;
	}

	// 周操作 樊硕添加

	/**
	 * 取得当前日期所在周的第一天(周日) 返回Date
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstDateOfWeek(java.util.Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 取得当前日期所在周的最后一天(周六) 返回Date
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastDateOfWeek(java.util.Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 取得当前日期所在周的第一天(周日)
	 * 
	 * @param date
	 * @return
	 */
	public static java.util.Date getFirstDayOfWeek(java.util.Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Sunday
		return (java.util.Date) c.getTime();
	}

	/**
	 * 取得当前日期所在周的最后一天(周六)
	 * 
	 * @param date
	 * @return
	 */
	public static java.util.Date getLastDayOfWeek(java.util.Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.SATURDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // SATURDAY
		return (java.util.Date) c.getTime();
	}

	/**
	 * 取得当前日期是多少周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(java.util.Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到某一年周的总数
	 * 
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

		return getWeekOfYear(c.getTime());
	}

	/**
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static java.util.Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 得到某年某周的最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static java.util.Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 将分钟转换为小时
	 * 
	 * @param minutes
	 * @return
	 */
	public static double minutes2hours(double minutes) {
		return minutes / 60;
	}

	public static void main1(String[] orgs) throws Exception {

		// System.out.println(DateUtil.getLastDayOfMonth());
		System.out.println(DateUtils.getFirstDayOfMonth());

		System.out.println(DateUtils.getLastDayOfMonth("2008-2-25"));
		System.out.println(DateUtils.getLastDayOfMonth("4580-2-2"));

		System.out.println(DateUtils.datePart("2008-2-25", 'w'));

		System.out.println(DateUtils.getOneMonthBeforeDate());
		System.out.println(DateUtils.getNextMonthFirstDay(DateUtils
				.getSysDate("2005-1-27")));

		java.util.Date date1 = null, date2 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date1 = sdf.parse("2008-01-30 00:00:00");
		date2 = sdf.parse("2008-01-30 23:59:59");

		System.out.println(DateUtils.daysBetween(date1, date2));

		// System.out.println(CommonUtil.round(5.235, 2));
	}

	public static void main(String[] orgs) throws Exception {

		java.util.Date date1 = null, date2 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date1 = sdf.parse("2009-09-01 23:59:01");
		date2 = sdf.parse("2009-09-10 09:59:54");
		
		// date1.compareTo(anotherDate)

		System.out.println(daysBetween(date1, date2));
		
		
		Date da = getDateFromMicroSeconds(1381549712518l);
		
		System.out.println(da);
		
		
		System.out.println((int)Math.round(1.5020));
		
//		System.out.println(workingHoursBetween(date2, date1));

//		java.util.Date date3 = getDaysIntervalWorkingDate(date2, -5);
//		System.out.println(getDateTimeStr(date3));
//
//		System.out.println(ServerCommonUtil.round(5.235, 2));
		
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		date1 = sdf.parse("2013-12-15");
		
		System.out.println(daysBetween(date1, getSysTime()));
		
		List l = new ArrayList<String>();
		l.add("[1,2,3]");
		l.add("[4,5]");
		l.add("[6]");
		
		System.out.println(l);
		
		String dateAbbr = DateUtils.getDateTimeFormatStr(new java.util.Date(),"yyyyMMdd");
		System.out.println(dateAbbr);
		
		//java.util.Date d0 = new java.util.Date();
		java.util.Date d0 = getSysDate();
		System.out.println("!!!"+d0.getTime());
//		d0 = getSysDate("yymmdd");
//		System.out.println(d0.getTime());
		
		java.util.Date d1 = parseDate("2014-02-28", "yyyy-MM-dd");
		System.out.println(d1.getTime()+"-=-=");
		
		java.util.Date d2 = getDaysIntervalDate(d1,-100);		
		System.out.println(d2.getTime());
		
		System.out.println((int)daysBetween(d1, d2));
		
		System.out.println(new Timestamp(getSysDate().getTime()));
		
		
		System.out.println(getSysMondayMidnight());
		
		//tmp test
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = Date.valueOf(df.format(new java.util.Date()));
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.THURSDAY);
		//c.setMinimalDaysInFirstWeek(7);
		c.setTime(d);
		//c.set(Calendar.DAY_OF_WEEK, 6);	
		
//		Timestamp ttt = new Timestamp(c.getTimeInMillis());
//		
//		Calendar c = new GregorianCalendar();
//		c.setFirstDayOfWeek(Calendar.MONDAY);
//		c.setMinimalDaysInFirstWeek(7);
//		c.setTime(date);

		
		System.out.println(c.get(Calendar.DAY_OF_WEEK));

		
		System.out.println((c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) ?7 : c.get(Calendar.DAY_OF_WEEK) - 1);		
		
		System.out.println(Integer.valueOf("1073741824"));
		
		String ssss = "1,2,3,4,5";
		System.out.println(StringUtils.removeStart(ssss, "1,2"));		
		
		System.out.println(DateUtils.getDayOfWeek(DateUtils.getSysTimestamp()));
		System.out.println(Calendar.MONDAY);
		
		System.out.println(DateUtils.getDayOfWeek(DateUtils.getSysTimestamp())!=Calendar.MONDAY);
		
		//(DateUtils.getDayOfWeek(DateUtils.getSysTimestamp())!=Calendar.MONDAY)
	}
	

}
/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.net.SyslogAppender;

/**
 * 日志级别的封装类 
 * 定义封装一些常用的日志级别
 * @version 1.0
 * @author LiaoGang
 */

public interface LogLevel {
	public static final Level USERLOG_LEVEL = 
			new UserLogLevel(Priority.FATAL_INT, "USERLOG"
					, SyslogAppender.LOG_LOCAL0);
}

/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.utils;

import org.apache.log4j.Level;

/**
 * log4j日志级别的扩展 
 * @version 1.0
 * @author LiaoGang
 */

public class UserLogLevel extends Level {

	private static final long serialVersionUID = 7804104683935074652L;
	protected UserLogLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}
}

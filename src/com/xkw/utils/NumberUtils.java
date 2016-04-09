/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-02-03
 */
package com.xkw.utils;

import java.text.NumberFormat;

/**
 * 
 * 数字工具
 * @author lms
 * @version1.0
 */
public class NumberUtils {

	/**
	 * 
	 * @param number
	 * @param count 保留小数位数
	 * @return
	 */
	public static Float numberFormat(float number,int count){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(count);
		
		return Float.parseFloat(nf.format(number));
	}
	
	/**
	 * 
	 * @param number
	 * @param count 保留小数位数
	 * @return
	 */
	public static  Double numberFormat(double number,int count){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(count);
		
		return Double.parseDouble(nf.format(number));
	}
	
	
}

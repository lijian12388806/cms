/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-05-04
 */
package com.xkw.mc.web.filter;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * request中请求参数的编码处理, 将编码转换成UTF-8
 * @author anonymous
 *
 */
public class MyRequest extends HttpServletRequestWrapper {

	private HttpServletRequest request;
	
	public MyRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	
	@Override
	public String getParameter(String name) {
		String value = request.getParameter(name);
		try {
			if(value!=null) {
				value = new String(value.getBytes("ISO8859-1"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}
	
}

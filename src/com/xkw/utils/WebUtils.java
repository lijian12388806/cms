/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-27
 */
package com.xkw.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xkw.mc.entity.AdminUser;


/**
 * web表示层相关的工具类,封装通用的方法
 * @version v1.0
 * @author LiaoGang
 */
public class WebUtils {
	
	/**
	 * 自动登录时回写的cookie名称
	 */
	private static final String LOGIN_COOKIE_NAME = "TS_SECURITY_REMEMBER_ME";
	
	private static final String LOWER_CHAR = "abcdefghijkimlopqrstuvwxyz";
	
	public static final String SESSION_USER = "user";
	
	
	/**
	 * session中获取当前登录的user用户信息,若未登录,则返回为空
	 * @param request
	 * @return
	 */
	public static AdminUser getLoginUser(HttpServletRequest request){
		HttpSession session = request.getSession();
		AdminUser user = null;
		if(session!=null) {
			user = (AdminUser) session.getAttribute(SESSION_USER);
		}
		return user;
	}
	
	
	/**
	 * 根据cookie的name值，返回cookie对象本身的通用方法
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request,String name) {
		Cookie[] cookies = request.getCookies();
		for(int k=0; cookies!=null && k<cookies.length; k++){
			String cookieName = cookies[k].getName();
			if(name.equals(cookieName)){
				return cookies[k];
			}
		}
		return null;
	}
	
	
	/**
	 * 用户登录时，其他信息将入session域中
	 * 若用户选择了"记住我"，
	 * 则将一部分信息回写到cookie中，以便之后读取
	 * 即实现自动登录
	 * @param request
	 * @param response
	 * @param user
	 * @param remeber
	 */
	/**
	 * @param request
	 * @param response
	 * @param user
	 * @param remember
	 */
	public static void login(final HttpServletRequest request,
			final HttpServletResponse response, AdminUser user, boolean remember) {
		request.getSession().setAttribute(SESSION_USER, user);
		if(remember){
			String securityCookie = Encodes.encodeCookie(user.getId()+"_"+user.getUsername());
			Cookie cookie = new Cookie(LOGIN_COOKIE_NAME,securityCookie);
			cookie.setMaxAge(3600*24*30);
			cookie.setPath(request.getServletContext().getContextPath());
			response.addCookie(cookie);
		}
	}
	
	
	/**
	 * 读取登录时生成的cookie数据，
	 * 返回用户的id
	 * @param request
	 */
	public static Integer checkUser(HttpServletRequest request){
		HttpSession session = request.getSession();
		AdminUser user = (AdminUser) session.getAttribute(SESSION_USER);
		
		if(user == null) {//用户未登录
			Cookie cookie = getCookie(request, LOGIN_COOKIE_NAME);
			if(cookie != null) {
				String info = Encodes.decodeCookie(cookie.getValue());
				if(info!=null) {
					String[] infos = info.split("_");
					if(infos!=null) {
						//用户未登录但是有登录的cookie信息时
						return Integer.parseInt(infos[0]);
					}
				}
			}
			//用户未登录并且没有登录的cookie信息时
			return null;
		}
		//用户已登录时
		return -1;
	}
	
	
	/**
	 * 清除登录时产生的自动登录的cookie
	 * @param request
	 * @param response
	 */
	public static void clearLoginCookie(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null && cookies.length>0) {
			for(int k=0; k<cookies.length; k++) {
				Cookie cookie = cookies[k];
				String cname = cookie.getName();
				if(cname.startsWith(LOGIN_COOKIE_NAME)){
					cookie.setMaxAge(0);
					cookie.setPath(request.getServletContext().getContextPath());
					response.addCookie(cookie);
				}
			}
		}
	}
	
	
	/**
	 * 添加cookie的通用方法,对cookie的value值添加了URL编码
	 * first_Name与last_Name的结合可以对cookie的name添加前缀或后缀
	 * 若不需要添加前缀或后缀，则将其中一个赋为空串即可
	 * expiryTime可以对cookie的失效时间进行设置
	 * @param request
	 * @param response
	 * @param first_Name
	 * @param last_Name
	 * @param value
	 * @param expiryTime 失效时间,为0，则删除此cookie
	 */
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, 
			String first_Name, String last_Name, String value,int expiryTime) {
		try {
			if(value!=null)
				value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		Cookie cookie = new Cookie(first_Name+""+last_Name, value);
		cookie.setMaxAge(expiryTime);
		cookie.setPath(request.getServletContext().getContextPath());
		response.addCookie(cookie);
	}
	
	
	/**
	 * 生成6位数的数字短信验证码
	 * @return
	 */
	public static String getCheckNum() {
		Random random = new Random();
		return random.nextInt(899999)+100000+"";
	}
	
	
	/**
	 * 邮箱格式的验证
	 * @param mobile
	 * @return
	 */
	public static boolean checkEmail(String email) {
		String regex = "[\\w]+@[\\w]+(.[\\w]+)+";
		return Pattern.matches(regex, email);
	}
	
	
	/**
	 * 生成指定长度的随机字符串
	 * @param count
	 * @return
	 */
	public static String makeRandomStr(int count) {
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for(int k=0; k<count; k++) {
			char ch = LOWER_CHAR.charAt(random.nextInt(LOWER_CHAR.length()-1));
			buf.append(ch);
		}
		return buf.toString();
	}
	
	
	/**
	 * 生成当前年月的文件夹名
	 * @return
	 */
	public static String getDateMonthDir() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return dateFormat.format(new Date());
	}
	
}

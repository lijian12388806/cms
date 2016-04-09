/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-15
 */
package com.xkw.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 定义一些常量，
 * 这些常量通常定义到const.properties文件中以方便修改
 * 从Const
 * @version 1.0
 * @author LiaoGang
 */

public class Const {
	
	public static final  int defPageNum=1;
	public static final  int pageSize=20;
	//项目中的文件上传下载根目录
	public static String BASE_DIR = null;
	public static String BASE_URL=null;
	public static String UPLOAD_ROOT_PATH="/upload/";
	public static String UPLOAD_IMAGES_PATH="/upload/image/";
	
	public static final String LEFT_BRACE = "{";
	public static final String RIGHT_BRACE = "}";
	public static final String RIGHT_LEFT_BRACE = "}{";
	public static final String COMMA = ",";
	public static final String SEMICOLON = ";";
	public static final String COLON = ":";

	public static final Long NULL_ID = -1l;
	
	
	//cms后台登录页面的url地址
	public static final String CMS_LOGIN_PAGE_URL = "/cms/login-page";
	//cms后台不受保护的url
	public static String EXURLS = "/ts/cms/admin/login,/ts/cms/login-page";
	
	//微信分享所需要的appid以及appsecret
	public static String WX_APP_ID = null;
	public static String WX_APP_SECRET = null;
	public static final String QUARTZ_APPNAME = "ts_wx";
	static {
		InputStream inStream = null;
		Properties props = null;
		try {
			inStream = Const.class.getClassLoader().getResourceAsStream("/const.properties");
			props = new Properties();
			props.load(inStream);
			
			BASE_DIR = props.getProperty("base_dir");
			BASE_URL = props.getProperty("base_url");
			
			WX_APP_ID = props.getProperty("wx_app_id");
			WX_APP_SECRET = props.getProperty("wx_app_secret");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

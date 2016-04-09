/**
 * 软件著作权：学科网
 * 系统名称：学易汇
 * 创建日期： 2015-03-01
 */

package com.xkw.utils;

import java.util.TreeMap;

/**
 * 处理一些有公共用途请求的handler
 * @author LiaoGang
 * @version 1.0
 */

public class StaticUtils {
	
	private static String APP_NAME = "xy360_cms";
	private static String APP_KEY = "ac10d4604e4a68c077390256644ed997";
	
	
	/**
	 * 获取访问接口的签名
	 * 加密方式signature = md5(md5(URI+param1+param2.....+AppName)+AppKey)
	 * @param IntefaceAddress 接口地址,如:home
	 * @param params 访问参数的TreeMap集合,key需按自然顺序排序,因此使用TreeMap<String, String>,这其中appKey与appName不需添加
	 * @return 
	 */
	public static String getSignature(String intefaceAddress, TreeMap<String, Object> params) {
		StringBuffer buf = new StringBuffer(intefaceAddress);
		if(params==null) {
			params = new TreeMap<>();
		}
		params.put("appName", StaticUtils.APP_NAME);
		params.put("appKey", StaticUtils.APP_KEY);
		for(String key : params.keySet()) {
			buf.append(params.get(key));
		}
		buf.append(StaticUtils.APP_NAME);
		
		//第一次加密md5(URI+param1+param2......+AppName)
		String firstEncode = Encodes.encodeByMD5(buf.toString());
		//第二次加密即签名signature = md5(md5(URI+param1+param2.....+AppName)+AppKey)
		String signature = Encodes.encodeByMD5(firstEncode+StaticUtils.APP_KEY);
		
		return signature;
	}
	
	public static void evictClass(String modName){
		//前台的缓存更新
		TreeMap<String, Object> params = new TreeMap<>();
		params.put("className", modName);
		String signature = StaticUtils.getSignature("evictClass", params);
		HttpUtil.getHTML(Const.BASE_URL+"/evictClass", "signature="+signature+"&className="+modName, "utf-8");
	}
	
}

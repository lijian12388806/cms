/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

/**  
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 * @version 1.0
 * @author LiaoGang
 */
public class Encodes {

	private static Logger logger = Logger.getLogger(Encodes.class);
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	public static String encodeUrl(String url){
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	
	/**
	 * 对给定的字符串进行sha方式加密, 返回加密后的字符串
	 * @param content
	 * @return
	 */
	public static String shaEncode(String content) {
		return Hex.encodeHexString(shaEncodeToByteArr(content));
	}
	
	/**
	 * 对给定的字符串进行sha方式加密,近回二进制数组
	 * @param content
	 * @return
	 */
	public static byte[] shaEncodeToByteArr(String content) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
			return md.digest(content.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * Hex解码.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * Base62编码。
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * Html 转码.
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
			throw Exceptions.unchecked(e);
		}
	}
	
	
	/**
	 * Md5加密算法
	 * @param inStr
	 * @return
	 */
	public static String encodeByMD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){
        	logger.error("", e);
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
    }
	
	
	/**
	 * 对cookie信息加密的加密算法
	 * @param info
	 * @return
	 */
	public static String encodeCookie(String info) {
		byte[] encode = Base64.encodeBase64URLSafe(info.getBytes());
		encode = Hex.encodeHexString(encode).getBytes();
		encode = Base64.encodeBase64URLSafe(encode);
		return new String(encode);
	}
	
	
	/**
	 * 对已加密的cookie信息的解密算法
	 * @param info
	 * @return
	 */
	public static String decodeCookie(String securityInfo) {
		byte[] decode = Base64.decodeBase64(securityInfo.getBytes());
		try {
			decode = Hex.decodeHex(new String(decode).toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		decode = Base64.decodeBase64(decode);
		
		return new String(decode);
	}
	
	
	/**
	 * 针对url称参数进行的加密官
	 * @param param
	 * @return
	 */
	public static String encodeUrlParam(String param) {
		return new String(Hex.encodeHex(param.getBytes(), true));
	}
	
	
	/**
	 * 针对url参数进行的解密
	 * @param param
	 * @return
	 * @throws DecoderException 
	 */
	public static String decodeUrlParam(String param) throws DecoderException {
		return new String(Hex.decodeHex((param).toCharArray()));
	}
}

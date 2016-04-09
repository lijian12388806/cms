package com.xkw.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * 
 * @author lms
 * @version 1.0
 */
public class TextUtils {
	public static final Pattern VARIABLE_PATTERN = Pattern
			.compile("\\$\\{(\\w+)\\}");
	
	public static String toString(Object o) {
		return o==null?"":o.toString();
	}

	public static boolean isBlank(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static String truncate(String text, int limit) {
		if (text.length() > limit) {
			if (limit > 3) {
				text = text.substring(0, limit - 3) + "...";
			}
		}
		return text;
	}

	public static String truncateBytes(String text, int limit)
			throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("UTF-8");
		if (bytes.length > limit) {
			text = new String(bytes, 0, limit);
			if (limit > 3) {
				text = text.substring(0, text.length() - 3) + "...";
			}
		}
		return text;
	}

	public static boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		} else if ((o1 == null && o2 != null) || (o1 != null && o2 == null)) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}
	
	@SuppressWarnings("unused")
	public static boolean strValEquals(String o1, String o2) {
		o1 = o1==null?"":o1;
		o2 = o2==null?"":o2;
		if (o1 == null && o2 == null) {
			return true;
		} else if ((o1 == null && o2 != null) || (o1 != null && o2 == null)) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}
	

	@SuppressWarnings("rawtypes")
	public static boolean equals(List l1, List l2) {
		if (l1 == null && l2 == null) {
			return true;
		} else if (l1 == null && l2 != null || l1 != null && l2 == null) {
			return false;
		} else {
			return l1.equals(l2);
		}
	}

	public static String quotize(Object o) {
		if (o == null) {
			o = "";
		}
		return "\"" + o + "\"";
	}
	
	public static Long getRandomLong(long period) {
		Random random = new Random();
		return  new Long((int)(random.nextFloat()*period));
	}

	public static String quotizeAndEscape(Object o) {
		if (o == null) {
			o = "";
		}
		return quotize(((String) o).replaceAll("\"", "\\\\\""));
	}

	public static List<Long> parseLongs(String longsList) {
		List<Long> longs = new LinkedList<Long>();
		if (longsList != null) {
			StringTokenizer tokenizer = new StringTokenizer(longsList, ",");
			while (tokenizer.hasMoreTokens()) {
				longs.add(Long.parseLong(tokenizer.nextToken()));
			}
		}
		return longs;
	}

	public static List<Integer> parseIntegers(String integersList) {
		List<Integer> integers = new LinkedList<Integer>();
		if (integersList != null) {
			StringTokenizer tokenizer = new StringTokenizer(integersList, ",");
			while (tokenizer.hasMoreTokens()) {
				integers.add(Integer.parseInt(tokenizer.nextToken()));
			}
		}
		return integers;
	}

	public static void writePlainTextResponse(HttpServletResponse response,
			String text) throws UnsupportedEncodingException, IOException {
		writePlainTextResponse(response, text, 200);
	}

	public static void writePlainTextResponse(HttpServletResponse response,
			String text, int statusCode) throws UnsupportedEncodingException,
			IOException {
		byte[] bytes = text.getBytes("UTF-8");
		response.setStatus(statusCode);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.getOutputStream().close();
	}

	public static String md5(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(data);
		byte messageDigest[] = algorithm.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < messageDigest.length; i++) {
			String hexByte = Integer.toHexString(0xFF & messageDigest[i]);
			if (hexByte.length() == 1) {
				hexByte = "0" + hexByte;
			}
			hexString.append(hexByte);
		}
		return hexString + "";
	}

	public static String getFileNameExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index < 0) {
			return null;
		} else {
			return fileName.substring(index + 1);
		}
	}

	public static String exceptionStacktrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		return stringWriter.toString();
	}

	// public static String projectCodes(Collection<? extends Project> projects)
	// {
	// StringBuilder result = new StringBuilder();
	// for (Project project : projects) {
	// if (result.length() > 0) {
	// result.append(", ");
	// }
	// result.append(project.getCode());
	// }
	// return result.toString();
	// }

	// public static String subProjectCodes(Collection<? extends SubProject>
	// subProjects) {
	// StringBuilder result = new StringBuilder();
	// for (SubProject subProject : subProjects) {
	// if (result.length() > 0) {
	// result.append(", ");
	// }
	// result.append(subProject.getCode());
	// }
	// return result.toString();
	// }

	public static String evalParams(String text, Map<String, Object> params) {
		Matcher m = VARIABLE_PATTERN.matcher(text);
		StringBuffer result = new StringBuffer();
		while (m.find()) {
			String paramName = m.group(1);
			Object value = params.get(paramName);
			if (value == null) {
				throw new NullPointerException("param '" + paramName
						+ "' is null");
			}
			m.appendReplacement(result,
					Matcher.quoteReplacement(value.toString()));
		}
		m.appendTail(result);
		return result.toString();
	}

	/**
	 * change \r \n \t to blank
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {

		if (str == null) {
			return "";
		}

		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		// Pattern p = Pattern.compile("\t|\r|\n");

		Matcher m = p.matcher(str);

		String after = m.replaceAll("");

		return after;

	}

	public static int getYYYYMMDDInteger(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1)
				* 100 + cal.get(Calendar.DATE);
	}

	

	/**
	 * 此方法没有意义
	 * @deprecated
	 * @param src
	 * @return
	 */
	public static byte[] getSendMessageConentBytes(String src) {
		byte[] retBytes = null;
		try {
			if (SystemUtils.IS_OS_WINDOWS_XP) {
				retBytes = src.getBytes("UTF-8");
			} else if (SystemUtils.IS_OS_LINUX) {
				retBytes = src.getBytes("GBK");
			} else {
				retBytes = src.getBytes("UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return retBytes;
	}
	
	private static String generateKey() {
        return StringUtils.leftPad( StringUtils.leftPad(Long.toString(Math.abs(new Random().nextLong()) % 100000, 30)
                + "z"
                + Long.toString(System.currentTimeMillis(), 30)
                + "x"
                + Long.toString(Math.abs(new Random().nextLong()) % 10000000, 30),10,'0'),20,'x');
        
	}     
	
	
	/**
	 * arr to set
	 * 
	 * @param oArr
	 * @return
	 */
	public static<T extends Object> Set<T> arrToSet(T [] oArr) {
		Set<T> retS = new HashSet<T>();
//		
		for (int i=0;oArr!=null&&i<oArr.length;i++) {
			retS.add(oArr[i]);
		}
		return retS;
	}
	
//	/**
//	 * list to map
//	 * 
//	 * K,V key: string value: T 
//	 * 
//	 * @param redisList
//	 * @return
//	 */
//	public static<T extends MisRedisBase> Map<String, T> toMisRedisMap(List<T> redisList) {
//		Map<String, T> retMap = new HashMap<String, T>();
//		
//		for (int i=0;redisList!=null&&i<redisList.size();i++) {
//			T obj = redisList.get(i);
//			
//			if (obj.getId()!=null&&obj.getId().getClass().equals(String.class)) {
//				retMap.put((String)obj.getId(), obj);
//			}
//		}
//		
//		return retMap;
//	}
	
	public static<T extends Object> Map<Integer, T> stringMap2IntMap(Map<String, T> rawMap) {
		Map<Integer, T> retMap = new HashMap<Integer, T>();

		if (rawMap!=null) {
			for (Iterator<String> it=rawMap.keySet().iterator();it.hasNext();) {
				String key = it.next();
				retMap.put(Integer.valueOf(key), rawMap.get(key));
			}			
		}
		
		return retMap;
	}
	public static<T extends Object> Map<String, T> intMap2StringMap(Map<Integer, T> rawMap) {
		Map<String, T> retMap = new HashMap<String, T>();
		
		if (rawMap!=null) {
			for (Iterator<Integer> it=rawMap.keySet().iterator();it.hasNext();) {
				Integer key = it.next();
				retMap.put(String.valueOf(key), rawMap.get(key));
			}			
		}
		
		return retMap;
	}
	
	
	
	public static Set<Integer> toIntegerSet(String fromStr) {
		Set<Integer> toSet = new HashSet<Integer>();
		
		if (StringUtils.isBlank(fromStr)) {
			return toSet;
		}
		String [] strArr = StringUtils.splitPreserveAllTokens(fromStr, Const.COMMA);
		
		for (int i=0;strArr!=null&&i<strArr.length;i++) {
			if (!StringUtils.isBlank(strArr[i])) {
				toSet.add(Integer.valueOf(strArr[i]));
			}
		}
		
		return toSet;
	}
	
	/**
	 * 
	 * @param strs values
	 * 
	 * @return K integer key, V strings
	 */
	public static Map<Integer, String> parseSemiValuesMap(String strs) {
		Map<Integer, String> retMap = new HashMap<Integer, String>();
		
		if (!StringUtils.isBlank(strs)) {
			String [] detailArr = StringUtils.splitPreserveAllTokens(strs, Const.SEMICOLON);
			for (String params : detailArr) {
				String [] detailParmArr = StringUtils.splitPreserveAllTokens(params, Const.COMMA);
				retMap.put(Integer.valueOf(detailParmArr[0]), params);
			}
		}
		
		return retMap;
	}
	
	/**
	 * semiMap to string
	 * 
	 * @param semiMap
	 * @return
	 */
	public static String toSemiString(Map<Integer, String> semiMap) {
//		StringBuffer sb = new StringBuffer();
//		
//		for (Iterator<String> it=semiMap.values().iterator();it.hasNext();) {
//			String value = it.next();
//			if (sb.length()!=0) {
//				sb.append(Const.SEMICOLON);
//			}
//			sb.append(value);
//		}
		
		//return sb.toString();
		return StringUtils.join(semiMap.values(), Const.SEMICOLON);
	}

	/**
	 * String set/Integer set to STRING
	 * 
	 * @param fromSet
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toString(Set fromSet) {
//		StringBuffer sb = new StringBuffer();
//		
//		for (Iterator it=fromSet.iterator();it.hasNext();) {
//			String s = String.valueOf(it.next());
//			if (sb.length()!=0) {
//				sb.append(Const.COMMA);
//			}
//			sb.append(s);
//		}
//		
//		return sb.toString();
		return StringUtils.join(fromSet, Const.COMMA);
	}
	
	/**
	 * List to String
	 * 
	 * @param fromSet
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toString(List fromList) {
		StringBuffer sb = new StringBuffer();
		
		for (int i=0;fromList!=null&&i<fromList.size();i++) {
			String s = String.valueOf(fromList.get(i));
			if (sb.length()!=0) {
				sb.append(Const.COMMA);
			}
			sb.append(s);			
		}
		
		return sb.toString();
	}	

	/**
	 * 字符串数据按照符号组成字符串
	 * @param str
	 * @param symbol
	 * @return
	 */
	public static String toString(String str[] ,String symbol)
	{
		StringBuffer sb = new StringBuffer();
		for(int i =0  ; i< str.length ; i++)
		{
			sb.append(str[i]);
			if(i != str.length-1)
				sb.append(symbol);
			
		}
		return sb.toString();
	}
	
	
	public static String fromClient(String clientStr) {
		if (clientStr!=null&&clientStr.length()>2) {
			clientStr = clientStr.substring(1,clientStr.length()-1);
			return StringUtils.replace(clientStr, Const.RIGHT_LEFT_BRACE, Const.SEMICOLON);
		} else {
			return null;
		}
	}

	/**
	 * 1,2;2,3 ==> {1,2}{2,3}
	 * 1,2,3 ==> {1,2,3}
	 * 
	 * @param serverStr
	 * @return
	 */
	public static String toClient(String serverStr) {
		if (StringUtils.isBlank(serverStr)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Const.LEFT_BRACE);
		if (!StringUtils.isBlank(serverStr)) {
			serverStr = serverStr.replace(Const.SEMICOLON, Const.RIGHT_LEFT_BRACE);
			sb.append(serverStr);
		}
		sb.append(Const.RIGHT_BRACE);
		return sb.toString();
	}
	
	/**
	 * add string item 
	 * 
	 * @param oldStr
	 * @param addedStr
	 * @return
	 */
	public static String addStringItem(String oldStr, String addedStr) {		
		if (StringUtils.isBlank(oldStr)) {
			return addedStr;
		}
		StringBuffer sb = new StringBuffer(oldStr);
		sb.append(Const.COMMA);			
		sb.append(addedStr);
		return sb.toString();
	}
	
	/**
	 * remove string item
	 * 
	 * @param oldStr
	 * @param addedStr
	 * @return
	 */
	public static String removeStringItem(String oldStr, String addedStr) {		
		if (StringUtils.isBlank(oldStr)) {
			return oldStr;
		}
		Set<String> oldSet = toStringSet(oldStr);
		oldSet.remove(addedStr);
		
		return StringUtils.join(oldSet, Const.COMMA);
	}
	
	
	
	/**
	 * null -1 ==> null
	 * 
	 * @param realPostion
	 * @return
	 */
	public static Integer toClientPosition(Integer realPosition) {
		return realPosition==null||realPosition.intValue()==Const.NULL_ID.intValue()?null:realPosition;
	}
	
	public static Set<String> toStringSet(String fromStr) {
		Set<String> toSet = new HashSet<String>();
		
		if (StringUtils.isBlank(fromStr)) {
			return toSet;
		}
		String [] strArr = StringUtils.splitPreserveAllTokens(fromStr, Const.COMMA);
		
		for (int i=0;strArr!=null&&i<strArr.length;i++) {
			if (!StringUtils.isBlank(strArr[i])) {
				toSet.add(strArr[i]);
			}
		}
		
		return toSet;
	}
	
	public static String arrToString(Object [] oArr) {
//		StringBuffer sb = new StringBuffer();
//		
//		for (int i=0;oArr!=null&&i<oArr.length;i++) {
//			if (oArr[i]!=null) {
//				if (sb.length()!=0) {
//					sb.append(Const.COMMA);					
//				}
//				sb.append(String.valueOf(oArr[i]));
//			}
//		}
		return StringUtils.join(oArr, Const.COMMA);
	}
	

	public static void main(String[] args) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("n", "10");
		String text = "bla trum ${n} tum tu dum.";
		String result = evalParams(text, params);
		System.out.println("result: " + result);
		
		
		System.out.println(Math.max(1, Math.ceil(60/10))); // 5

		
		System.out.println(Math.ceil(4.12)); // 5
		System.out.println(Math.ceil(4.5)); // 5
		System.out.println(Math.ceil(4.9));// 5

		System.out.println(Math.floor(4.12));// 4
		System.out.println(Math.floor(4.5));// 4
		System.out.println(Math.floor(4.9));// 4

		System.out.println(Math.ceil(10d / 2.41d));

		System.out.println((double) (100 / 50));

		System.out.println((10d / 6d));

		for (int i=0;i<100;i++) {
			System.out.println(getRandomLong(2));	
		}
		
		System.out.println(UUID.randomUUID().toString());  
		
		for (int i=0;i<10;i++) {
			String key = generateKey();
			System.out.println(key.length()+" "+key);
		}
		
		System.out.println(NumberUtils.toInt(TextUtils.toString(Integer.valueOf(122))));

		
	}
}

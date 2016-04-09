package com.xkw.utils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

public class JstlFunctions {
	
//	@Autowired
//	private  AppCategoryServiceImpl appCategoryService;
	   public static Logger logger = Logger.getLogger(JstlFunctions.class);

	    public JstlFunctions() {
	    }

	    public static void test(int i) {
	        i++;
	        logger.info("JSTL function test... " + i);
	    }

	    public static Object call(Object object, String methodName) {
	        if(object == null) {
	            return null;
	        }
	        try {
	            Method method = object.getClass().getMethod(methodName, new Class[]{});
	            return method.invoke(object, new Object[]{});
	        } catch (Exception e) {
	            logger.warn("Failed to call method '" + methodName + "' on object: " + object, e);
	        }
	        return null;
	    }

	    public static Object call1(Object object, String methodName, Object arg) {
	        if(object == null) {
	            return null;
	        }
	        try {
	            Method method = object.getClass().getMethod(methodName, arg.getClass());
	            return method.invoke(object, arg);
	        } catch (Exception e) {
	            logger.warn("Failed to call method '" + methodName + "' on object: " + object + " with arguments: " + arg, e);
	        }
	        return null;
	    }

	    public static Object call2(Object object, String methodName, Object arg1, Object arg2) {
	        if(object == null) {
	            return null;
	        }
	        try {
	            Method method = object.getClass().getMethod(methodName, new Class[]{arg1.getClass(), arg2.getClass()});
	            return method.invoke(object, new Object[]{arg1, arg2});
	        } catch (Exception e) {
	            logger.warn("Failed to call method '" + methodName + "' on object: " + object + " with arguments: " + arg1 + ", " + arg2, e);
	        }
	        return null;
	    }

	    private static String formatDate(String pattern, Date date) {
	        if(date == null) {
	            return null;
	        }
	        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	     
	        return dateFormat.format(date);
	    }

	    public static String simpleDate(Date date) {
	    	return formatDate("yyyy-MM-dd", date);
	    }

	    public static String simpleTime(Date date) {
	    	return formatDate("HH:mm:ss", date);
	    }

	    public static String simpleDateTimeLong(Long dateLong) {
	        return simpleDateTime(DateUtils.getDateFromMicroSeconds(dateLong));
	    }
	    public static String simpleDateTimeInteger(Integer dateInteger) {
	        return simpleDateTime(DateUtils.getDateFromMicroSeconds(dateInteger));
	    }
	    public static String simpleDateLong(Long dateLong) {
	    	return simpleDate(DateUtils.getDateFromMicroSeconds(dateLong));
	    }

	    public static String simpleTimeLong(Long dateLong) {
	    	return simpleTime(DateUtils.getDateFromMicroSeconds(dateLong));
	    }

	    public static String simpleDateTime(Date date) {
	        return formatDate("yy-MM-dd HH:mm:ss", date);
	    }
	    

	    public static String trunc(String text, int limit) {
	        if(text == null) {
	            return null;
	        }
	        return TextUtils.truncate(text, limit);
	    }

	    @SuppressWarnings("rawtypes")
		public static Object evalModelPath(Map model, String path) {
	        if (path.indexOf('.') < 0) {
	            return model.get(path);
	        }
	        String[] tokens = path.split("\\.");
	        Object object = model.get(tokens[0]);
	        if (object == null) {
	            return null;
	        }
	        path = path.replaceFirst(".*?\\.", "");
	        try {
	            return ReflectionUtils.getPropertyValue(object, path);
	        } catch (Exception e) {
	            return null;
	        }
	    }

	    /**
	     * 对html文本解析
	     * @param str
	     * @return
	     */
	    public static String htmlspecialchars(String str) {
	    	str = str.replaceAll("&", "&amp;");
	    	str = str.replaceAll("<", "&lt;");
	    	str = str.replaceAll(">", "&gt;");
	    	str = str.replaceAll("\"", "&quot;");
	    	return str;
	    }
	    /**
	     * 解析App截图路径
	     * @return
	     */
	   public static String splitCoverPath(String coverPath){
		   String[] strAry = coverPath.split(",");
		   StringBuilder stb=new  StringBuilder();
		   for(int i=0;i<strAry.length;i++){
			   if(strAry[i].startsWith("/")){
				   stb.append(Const.BASE_URL+strAry[i]).append(",");  
			   }else{
				   stb.append(Const.BASE_URL+"/").append(",");  
			   }
			  
		   }
		  String str =stb.toString();
		   return str.substring(0, str.length()-1);
	   }
	   /**
	    * 
	    * @return
	    */
	 public static String convert(Integer status){
		 switch(status){
		 case 1:
			 return "未审核";
		 case 2:
			 return "审核中";	 
		 case 3:
			 return "审核不通过";	 
		 case 4:
			 return "已审核";	 
		 case 5:
			 return "已下线";
	     default:
	    	 return "";
		 }
		
	 }
}

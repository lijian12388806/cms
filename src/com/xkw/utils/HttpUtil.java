/**
 * 
 */
package com.xkw.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;


public class HttpUtil {

	private static Logger logger = Logger.getLogger(HttpUtil.class);
	
	
    /**
     * 
     * 文件静态化方法
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param url http地址
     * @param fileDir 静态文件所在目录
     * @param fileName 静态文件名
     * @param charcode url字符集
     * @param charcode2 保存文件的字符集
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
	public static void createStaticPages(String url, String fileDir,String fileName,String charcode,String charcode2) 
           throws MalformedURLException, IOException, FileNotFoundException{        
        FileUtil.writeStoFile(HttpUtil.getHTML(url, "", charcode), fileDir, fileName,charcode2);        
    }
	
	
	/**
     * 
     * 文件静态化方法
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param url http地址
     * @param fileDir 静态文件所在目录
     * @param fileName 静态文件名
     * @param charcode url字符集
     * @param charcode2 保存文件的字符集
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
	public static void createStaticPagesByGet(String url, String fileDir,String fileName,String charcode,String charcode2) 
			throws MalformedURLException, IOException, FileNotFoundException{        
		FileUtil.writeStoFile(HttpUtil.getHTMLByGet(url, charcode), fileDir, fileName,charcode2);        
	}
    
	
    /**
     * 山寨文件静态化方法2
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param staticParam 静态化常量文件(StaticHtmlConstant)中定义的变量名
     * @param filenameReplaces filename中的需要将<#urlcanshu#>替换成的内容
     * @param urlReplaces url中的需要将<#urlcanshu#>替换成的内容
     * @param pathReplaces path中的需要将<#pathcanshu#>替换成的内容
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void createStaticPages(String[] staticParam,String filenameReplaces,String urlReplaces,String pathReplaces){
        //读取url地址中的内容
    	String url = staticParam[1].replaceAll("<#urlcanshu#>", urlReplaces);
    	String content = HttpUtil.getHTMLByGet(url,"UTF-8");    	
    	content=content.replaceAll("<!@@#", "<!--#");    	
        FileUtil.writeStoFile(content, staticParam[2].replaceAll("<#pathcanshu#>", pathReplaces), staticParam[0].replaceAll("<#filenamecanshu#>",filenameReplaces), "UTF-8");
        
    }
    
    /**
     * 山寨文件静态化方法3 页面UTF-8转为gb2312
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * 
     * @param staticParam 静态化常量文件(StaticHtmlConstant)中定义的变量名
     * @param filenameReplaces filename中的需要将<#urlcanshu#>替换成的内容
     * @param urlReplaces url中的需要将<#urlcanshu#>替换成的内容
     * @param pathReplaces path中的需要将<#pathcanshu#>替换成的内容
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void createStaticPagesGBK(String[] staticParam,String filenameReplaces,String urlReplaces,String pathReplaces){
        //读取url地址中的内容
    	String url = staticParam[1].replaceAll("<#urlcanshu#>", urlReplaces);
    	String content = HttpUtil.getHTML(url, "", "gb2312");
    	System.out.println(content);
    	content=content.replaceAll("<!@@#", "<!--#").replaceAll("UTF-8", "gb2312");    	
        FileUtil.writeStoFile(content, staticParam[2].replaceAll("<#pathcanshu#>", pathReplaces), staticParam[0].replaceAll("<#filenamecanshu#>",filenameReplaces), "gb2312");
        
    }
    
	/**
	 * URL页面抓取
	 * @param destURL URL地址
	 * @oaran charcode  URL的字符集编码
	 * @return 得到页面的HTML字符串
	 */
	public static String getHTML(String destURL, String param, String charcode) {
		StringBuffer buf = new StringBuffer();
		HttpURLConnection httpConn = null;
		PrintWriter out = null;
		BufferedReader reader = null;
		try {
			// 与对方建立一个连接
			URL url = new URL(destURL);
			// 打开连接
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			out = new PrintWriter(httpConn.getOutputStream());
			out.print(param);	//参数
			out.flush();
			httpConn.connect();
			reader = new BufferedReader(new InputStreamReader(httpConn
					.getInputStream(),charcode));

			String line;
			while ((line = reader.readLine()) != null) {
				buf.append(line).append("\r\n");
			}
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally {
			try {
				if(reader!=null) {
					reader.close();
				}
				if(out!=null) {
					out.close();
				}
				if(httpConn!=null) {
					httpConn.disconnect();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return buf.toString();
	}
	
	/**
	 * URL页面抓取
	 * @param destURL URL地址
	 * @oaran charcode  URL的字符集编码
	 * @return 得到页面的HTML字符串
	 */
	@SuppressWarnings("unused")
	public static String getHTMLByGet(String destURL,String charcode) {
		StringBuffer buf = new StringBuffer();
		HttpURLConnection httpConn = null;
		PrintWriter out = null;
		BufferedReader reader = null;
		try {
			// 与对方建立一个连接
			URL url = new URL(destURL);
			// 打开连接
			httpConn = (HttpURLConnection) url.openConnection();

			httpConn.connect();
			reader = new BufferedReader(new InputStreamReader(httpConn
					.getInputStream(),charcode));

			String line;
			while ((line = reader.readLine()) != null) {
				buf.append(line).append("\r\n");
			}
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally {
			try {
				if(reader!=null) {
					reader.close();
				}
				if(httpConn!=null) {
					httpConn.disconnect();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return buf.toString();
	}
	/**
	 * 接口调用方法 <br>
	 * 修改历史： <br>
	 * 修改日期 修改者 BUG小功能修改申请单号 <br>
	 * @param destURL
	 *            接口地址
	 * @param paramStr
	 *            接口参数
	 * @return 发送是否成功
	 */
	public static boolean httpClientAPI(String destURL, String paramStr) {
		StringBuffer buf = new StringBuffer();
		HttpURLConnection httpConn = null;
		PrintWriter out = null;
		BufferedReader reader = null;
		try {
//			 与对方建立一个连接
			URL url = new URL(destURL);
			// 打开连接
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			// URLEncoder.encode ("xx")
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			out = new PrintWriter(httpConn.getOutputStream());
			out.print(paramStr);
			out.flush();
			httpConn.connect();
			reader = new BufferedReader(new InputStreamReader(httpConn
					.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				buf.append(line);
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(),e);
			return false;			//访问ＵＲＬ地址出错
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			return false;
		} finally {
			try {
				if(reader!=null){
					reader.close();
				}
				if(out!=null){
					out.close();
				}
				if(httpConn!=null){
					httpConn.disconnect();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return true;
	}


}

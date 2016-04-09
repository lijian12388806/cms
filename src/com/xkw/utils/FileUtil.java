package com.xkw.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;


/**
 * <pre>
 * 文件操作类.
 * 
 * 创建日期： 2009-10-16
 * 
 * 修改历史：
 * 修改日期       修改者       BUG小功能修改申请单号
 * YYYY/MM/DD     ###         ******* 
 * </pre>
 * @version  $Revision: 1.2 $
 * @author $Author:  $
 */
public class FileUtil {
	/**
	*功能的简:log4j设置.
	* <br>修改历史：
	* <br>修改日期  修改者 BUG小功能修改申请单号
	* <br>
	*/
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileUtil.class.getName());
    
     /**
     * 删除文件或空目录
     * @param fullFilePath 文件或目录的绝对路径
     */
    public static void deleteFile(String fullFilePath) {
        try {

            fullFilePath = fullFilePath.replaceAll("//html", "/html").replaceAll("//index.html", "/index.html").replaceAll("//", "/");    //替换可能出现的//现象   
            File file = new File(fullFilePath);
            if (file.exists()) {
                file.delete();
            }
        }catch (Exception e) {
        	e.printStackTrace();
			logger.error(e.getMessage(),e);
        }
    }

	public static void deleteFiles(String[] fullFilePaths) {
		try {
			for (String fullFilePath : fullFilePaths) {
				deleteFile(Const.BASE_DIR+fullFilePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
    
    
    /**
     * 拷贝目录(cp)
     * @param srcDir 源路径
     * @param dstDir 目标路径
     * @throws IOException
     */
    public static void copyDir(String srcDir, String dstDir) throws IOException {
        File fdstDir = new File(dstDir);
        if (!fdstDir.exists()) {
            fdstDir.mkdirs();
        }

        String[] fileList = new File(srcDir).list();
        boolean isDir;
        for (int i = 0; i < fileList.length; i++) {
        	isDir = new File(srcDir + File.separator+ fileList[i]).isDirectory();
            if (isDir) {
                copyDir(srcDir + File.separator
                        + fileList[i], dstDir
                        + File.separator
                        + fileList[i]);
            }else{
                copyFile(srcDir + File.separator
                         + fileList[i], dstDir
                         + File.separator
                         + fileList[i]);
            }
        }
    }
    
    /**  
     * 删除目录（文件夹）以及目录下的文件  
     * @param   dir 被删除目录的文件路径  
     * @return  目录删除成功返回true,否则返回false  
     */  
    public static boolean deleteDir(String dir){   
        //如果dir不以文件分隔符结尾，自动添加文件分隔符   
        if(!dir.endsWith(File.separator)){   
            dir = dir+File.separator;   
        }   
        File dirFile = new File(dir);   
        //如果dir对应的文件不存在，或者不是一个目录，则退出   
        if(!dirFile.exists() || !dirFile.isDirectory()){   
            System.out.println("删除目录失败"+dir+"目录不存在！");   
            return false;   
        }   
        boolean flag = true;   
        //删除文件夹下的所有文件(包括子目录)   
        File[] files = dirFile.listFiles();   
        for(int i=0;i<files.length;i++){   
            //删除子文件   
            if(files[i].isFile()){   
                deleteFile(files[i].getAbsolutePath());
            }   
            //删除子目录   
            else{   
                flag = deleteDir(files[i].getAbsolutePath()); 
            }   
        }   
           
        if(!flag){   
            System.out.println("删除目录失败");   
            return false;   
        }   
           
        //删除当前目录   
        if(dirFile.delete()){   
            System.out.println("删除目录"+dir+"成功！");   
            return true;   
        }else{   
            System.out.println("删除目录"+dir+"失败！");   
            return false;   
        }   
    }   

    /**
     * 拷贝文件
     *
     * @param srcFile 源文件
     * @param dstFile 目标文件
     * @throws IOException
     */
    private static void copyFile(String srcFile, String dstFile) throws IOException {
        FileInputStream inp = new FileInputStream(srcFile);
        FileOutputStream out = new FileOutputStream(dstFile);
        byte[] buff = new byte[40960];
        int count;
        while ( (count = inp.read(buff)) != -1) {
            out.write(buff, 0, count);
        }
        out.close();
        inp.close();
    }

    /**
     * 拷贝文件(cp)
     * @param sourceFilePath 源文件路径
     * @param sourceFileName 源文件名
     * @param destFilePath 目标文件路径
     * @param destFileName 目标文件名
     * @return 操作是否成功
     */
    public static boolean copeFile(String sourceFilePath, String sourceFileName, String destFilePath,
                               String destFileName) {
        boolean ok = false;
        try {
            File dest = new File(destFilePath);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            copyFile(sourceFilePath + File.separator + sourceFileName,
                     destFilePath + File.separator + destFileName);
            ok = true;
        }
        catch (Exception e) {
        	e.printStackTrace();
			logger.error(e.getMessage(),e);
        }
        return ok;
    }
    
    /**
     * 移动文件(mv)
     * @param sourceFilePath 源文件路径
     * @param sourceFileName 源文件名
     * @param destFilePath 目标文件路径
     * @param destFileName 目标文件名
     * @return 操作是否成功
     */
    public static boolean moveFile(String sourceFilePath, String sourceFileName, String destFilePath,
                               String destFileName) {
        boolean ok = false;
        try {
            File dest = new File(destFilePath);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            copyFile(sourceFilePath + File.separator + sourceFileName,
                     destFilePath + File.separator + destFileName);
            deleteFile(sourceFilePath + File.separator + sourceFileName);
            ok = true;
        }
        catch (Exception e) {
        	e.printStackTrace();
			logger.error(e.getMessage(),e);
        }
        return ok;
    }


    /**
     * 判断文件是否存在
     * @param PathFile 文件名（带绝对路径）
     * @return 文件是否存在
     */
    public static boolean FileIfExist(String PathFile) {
        File file = new File(PathFile);
        if (file.exists())
            return true;
        else
            return false;
    }
    
    /**
     * 读取文件内容
     * @param filename 文件名
     * @param charSet  文件的编码/utf-8/gbk等
     * @return 文件内容
     */
    public static String readFromFile(String filename,String charSet) {
        BufferedReader in = null;
        StringBuffer result=new StringBuffer("");
        try {
            File file = new File(filename);
            if (file.exists()) {
            	in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charSet));//设置Encoding，必须设置，否则显示中文会有问题
                String s="";
                while((s=in.readLine())!=null){
                	result.append(s+"\r\n");
                }
            }
        }catch (Exception e) {
        	e.printStackTrace();
			logger.error(e.getMessage(),e);
        }finally{
			try{
				in.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
        return result.toString();
    }
    
    /**
	 * 写字符串到文件
	 * @param fileDir 要输出的文件目录
	 * @param fileName 要输出的文件名
	 * @param s		要输出的字符串
	 * @param charSet 文件的字符集
	 * @return 操作是否成功
	 */
	public final static boolean writeStoFile(String s, String fileDir, String fileName,String charSet) {
		boolean ok = false;
		if(s==null) return false;
//		s=s.replaceAll("//html", "/html").replaceAll("//index.html", "/index.html");	//替换可能出现的//现象		
		BufferedWriter to = getOut(fileDir,fileName,true,charSet);		
		if (to == null)
			return false;
		try {
			to.write(s);
			to.flush();
			to.close();
			ok=true;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return ok;
	}
	public final static boolean writeStoFile(JSONObject s, String fileDir, String fileName,String charSet) {
		boolean ok = false;
		if(s==null) return false;
//		s=s.replaceAll("//html", "/html").replaceAll("//index.html", "/index.html");	//替换可能出现的//现象		
		BufferedWriter to = getOut(fileDir,fileName,true,charSet);		
		if (to == null)
			return false;
		try {
			to.write(s+"");
			to.flush();
			to.close();
			ok=true;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return ok;
	}
	
    /**
	 * 得到文件写句丙 
	 * @param filePath	要输出的文件目录
	 * @param fileName	要输出的文件名
	 * @param flag  为真则覆盖原文件
	 * @param charset 文件编码 如 gbk ,utf-8等
	 * @return 文件写句丙
	 */
	public final static BufferedWriter getOut(String fileDir,String fileName, boolean flag , String charset) {
		BufferedWriter out = null;

		try {
			File fileDirs = new File(fileDir);
			if ( !fileDirs.isDirectory() ){
				fileDirs.mkdirs();
		    }
			File htmlFile= new File(fileDir+"/"+fileName);
			if (flag) {
				if (htmlFile.exists()) {
					//System.out.println("------------------nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnncccc----------");
				} else {// 建立新文件
					htmlFile.createNewFile();
				}
			} else {// 如果flag为false则不覆盖文件
				if (htmlFile.exists()) {// 如果文件已经存在则返回null
					return null;
				} else {// 建立新文件
					htmlFile.createNewFile();
				}
			}
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(htmlFile,false), charset));// 设置Encoding
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}

		return out;

	}
	
	
    /**
	 * 上传文件到服务器上上指定的文件夹下 
	 * @param formFile 前台上传的FORMFILE对象
	 * @param fileDir  上传到服务器的目录绝对路径(如:/www/tuku.china.com/pic)
	 * @param fileName 上传到服务器的文件名
	 * @return 上传是否成功
	 */
	public static boolean uploadFileToServer(InputStream formFile, String fileDir, String fileName){
		InputStream streamIn = null;
		OutputStream streamOut = null;
		try {			
			File fileDirs = new File(fileDir);
			if ( !fileDirs.isDirectory() ){
				fileDirs.mkdirs();
		    }
			File htmlFile= new File(fileDir+"/"+fileName);
			
			streamIn = formFile;
			streamOut = new FileOutputStream(htmlFile);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = streamIn.read(buffer, 0, 8192)) != -1) {
				streamOut.write(buffer, 0, bytesRead);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			return false;
		} finally{
			try{
				streamOut.close();
				streamIn.close();
			}catch(Exception e){
				e.printStackTrace();
				logger.error(e.getMessage(),e);
			}
		}
		return true;
	}
	   /**
     * 把给定的 document 写入 filename 文件中
     * 
     * @param document -
     *            xml document
     * @param filename -
     *            要写入的文件路径加文件名
     * @param encoding -
     *            文件编码
     * @return 成功 true，失败 false
     */
    public static boolean writeXML(String document, String filename, String encoding){
        File file = new File(filename);
        try{
            FileUtils.forceMkdir(new File(file.getParent()));
            FileUtils.writeStringToFile(file, document, encoding);
//            Logger.getRootLogger().info("FileUtil's Log:"+filename+",size:"+((double)file.length()/1024)+"K");
            throw new Exception();
        }catch(IOException e){
            Logger.getRootLogger().error("", e);
            try{
                FileUtils.forceDelete(file);
            }catch(Exception ex){
            }
            return false;
        }catch(Exception e){
        	try{
	        	StringWriter out = new StringWriter();
				e.printStackTrace(new PrintWriter(out));
				String outstr = out.toString().replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
				if(outstr.indexOf(".Controller")>-1){
//					Logger.getRootLogger().info("FileUtil2'sLog:"+filename+",size:"+((double)file.length()/1024)+"K"+",log stack:"+outstr.substring(0,outstr.indexOf(".Controller")));
				}else {
//					Logger.getRootLogger().info("FileUtil2'sLog:"+filename+",size:"+((double)file.length()/1024)+"K"+",log stack:"+outstr);
				}
				
        	}catch(Exception e1){
				
			}
        }

        return true;
    }
    private static ArrayList<String> filelist = new ArrayList<String>();
    static void getFiles(String filePath){
    		  File root = new File(filePath);
    		    File[] files = root.listFiles();
    		    for(File file:files){    
    		     if(file.isDirectory()){
    		      /*
    		       * 递归调用
    		       */
    		      getFiles(file.getAbsolutePath());
    		      filelist.add(file.getAbsolutePath());
    		      System.out.println("显示"+filePath+"下所有子目录及其文件"+file.getAbsolutePath());
    		     }else{
    		      System.out.println("显示"+filePath+"下所有子目录"+file.getAbsolutePath());
    		     }    
    		    }
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

package com.xkw.utils;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * fastdfs文件上传下载处理的工具类
 * @author anonymous
 *
 */
public class FastDfsUtils {
	
	private static Logger logger = Logger.getLogger(FastDfsUtils.class);
	
	static {//加载配置文件
		String config = FastDfsUtils.class.getClassLoader().getResource("fdfs_client.conf").getPath();
		try {
			ClientGlobal.init(config);
		} catch (IOException | MyException e) {
			e.printStackTrace();
			logger.error("FastDFS配置文件fdfs_client.conf加载异常", e);
		}
	}
	
	/**
	 * 上传文件
	 * @param localPath 文件所在的绝对路径,例:/alidata/temp/1.jpg
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	public static String upload(String localPath) throws IOException, MyException {
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		
		String extName = localPath.substring(localPath.lastIndexOf(".")+1);
		String[] results = storageClient.upload_file(localPath, extName , null);
		if(results!=null && results.length>0) {
			return "/"+results[0]+"/"+results[1];
		}
		return "";
	}
	
	/**
	 * 删除文件
	 * @param path	远程文件路径, 例:/group1/M00/00/61/CqNtcVbmI8uASahMAAAXJ1Mxw5A353.jpg
	 * @throws IOException
	 * @throws MyException 
	 */
	public static void delete(String path) throws IOException, MyException {
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		if(StringUtils.isNotEmpty(path)) {
			path = path.substring(1);
			int index = path.indexOf("/");
			String group = path.substring(0, index); //获取组
			String remoteFilePath = path.substring(index+1); //获取远程文件路径
			storageClient.delete_file(group, remoteFilePath);
		}
	}
	
	/** 下载文件
	 * @param sourcePath	远程文件路径,例:/group1/M00/00/61/CqNtcVbmI8uASahMAAAXJ1Mxw5A353.jpg
	 * @throws IOException 
	 * @throws MyException 
	 */
	public static byte[] download(String sourcePath) throws IOException, MyException {
		sourcePath = sourcePath.substring(1);
		int index = sourcePath.indexOf("/");
		String group = sourcePath.substring(0, index);
		String remoteFilePath = sourcePath.substring(index+1);
		
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		return storageClient.download_file(group, remoteFilePath);
	}
}

package com.xkw.utils;

import java.io.*;
import java.util.Iterator;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 图片压缩处理 工具类
 */
public class ImgUtil {
	private Image img;
	private int width;
	private int height;
	
	public ImgUtil(String imagePath) throws IOException {
		File file = new File(imagePath);
		this.img = ImageIO.read(file);
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
	}
	
	/**
	 * 获取图片的宽
	 * @param filePath
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 获取图片的高
	 * @param filePath
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	
	/**
	 * 图片裁剪通用接口
	 * @param src
	 * @param dest
	 * @param type
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @throws IOException
	 */
    @SuppressWarnings("rawtypes")
	public static void cutImage(String src, String dest, String type, int x, int y, int w, int h) throws IOException{   
           Iterator iterator = ImageIO.getImageReadersByFormatName(type);   
           ImageReader reader = (ImageReader)iterator.next();   
           InputStream in=new FileInputStream(src);  
           ImageInputStream iis = ImageIO.createImageInputStream(in);   
           reader.setInput(iis, true);   
           ImageReadParam param = reader.getDefaultReadParam();   
           Rectangle rect = new Rectangle(x, y, w,h);    
           param.setSourceRegion(rect);   
           BufferedImage bi = reader.read(0,param);     
           ImageIO.write(bi, type, new File(dest));             
    }
    
    
   /**
    * 图片压缩,图片尺寸的修改, 按原图等比例修改尺寸
    * @param src
    * @param dest
    * @param w
    * @param h
    * @throws Exception
    */
	public static void resize(String src, String dest, int w, int h) throws Exception {
		File file = new File(src);
		BufferedImage bufImg = ImageIO.read(file);
		int width = bufImg.getWidth(null);
		int height = bufImg.getHeight(null);
		if (((float) width / height) > w / h) {
			// 以宽度为基准，等比例放缩图片
			int newHight = (int) (((float)height * w) / width);
			zoomImage(bufImg, dest, w, newHight);
		} else {
			// 以高度为基准，等比例缩放图片
			int newWidth = (int) (((float)width * h) / height);
			zoomImage(bufImg, dest, newWidth, h);
		}
	}
	
    
	/**
	 * 图片压缩, 图片尺寸的修改, 不等比例缩放
	 * @param src
	 * @param dest
	 * @param w
	 * @param h
	 * @throws Exception
	 */
	public static void resizeFix(String src, String dest, int w, int h) throws Exception {  
		File file = new File(src);
		BufferedImage bufImg = ImageIO.read(file);
		zoomImage(bufImg, dest, w, h);
	}
	
	
	/**
	 * 按指定尺寸缩放或放大, 不等比例绽放
	 * @param bufImg
	 * @param dest
	 * @param w
	 * @param h
	 * @throws Exception
	 */
	private static void zoomImage(BufferedImage bufImg, String dest, int w, int h) throws Exception {  
        double wr=0,hr=0;  
        File destFile = new File(dest);  
        Image Itemp = bufImg.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);  
        wr=w*1.0/bufImg.getWidth();  
        hr=h*1.0 / bufImg.getHeight();  
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);  
        Itemp = ato.filter(bufImg, null);  
        ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
    }
}
package com.xkw.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageMarkLogoByIcon {

	/**   
     * @param args   
	 * @throws FileNotFoundException 
     */    
    public static void main(String[] args) throws FileNotFoundException {     
        String srcImgPath = "F:\\xiaoti\\3388817_";     
        String iconPath = "f:\\水印.png";     
        String targerPath = "F:\\xiaoti\\";   
        for(int i=0;i<5;i++){
        	  File file = new File(srcImgPath+i+".jpg");
              // 给图片添加水印     1452566939603  1452566939715 1452566939849  1452566940004 1452566940118
             FileInputStream in = new FileInputStream(file);
             String temp="";
             switch(i){
             case 0:
            	 temp=targerPath+"1452566939603.jpg";
            	 break;
             case 1:
            	 temp=targerPath+"1452566939715.jpg";
            	 break;
             case 2:
            	 temp=targerPath+"1452566939849.jpg";
            	 break;
             case 3:
            	 temp=targerPath+"1452566940004.jpg";
            	 break;
             case 4:
            	 temp=targerPath+"1452566940118.jpg";
            	 break;
             }
             ImageMarkLogoByIcon.markImageByIcon(iconPath, in, temp , 0);
        }
        
        System.out.println("kkkkl");
    }     
    /**   
     * 给图片添加水印   
     * @param iconPath 水印图片路径   
     * @param srcImgPath 源图片路径   
     * @param targerPath 目标图片路径   
     */    
//    public static void markImageByIcon(String iconPath, String srcImgPath,     
//            String targerPath) {     
//        markImageByIcon(iconPath, srcImgPath, targerPath, null) ;   
//    }     
    /**   
     * 给图片添加水印、可设置水印图片旋转角度   
     * @param iconPath 水印图片路径   
     * @param srcImgPath 源图片路径   
     * @param targerPath 目标图片路径   
     * @param degree 水印图片旋转角度 
     */    
    public static void markImageByIcon(String iconPath, InputStream in,     
            String targerPath, Integer degree) {     
        OutputStream os = null;     
        try {     
        	
            Image srcImg = ImageIO.read(in);  
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),     
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);   
            // 得到画笔对象     
            // Graphics g= buffImg.getGraphics();     
            Graphics2D g = buffImg.createGraphics();     
    
            // 设置对线段的锯齿状边缘处理     
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,     
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);     
    
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg     
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);     
    
            if (null != degree) {     
                // 设置水印旋转     
                g.rotate(Math.toRadians(degree),     
                        (double) buffImg.getWidth() / 2, (double) buffImg     
                                .getHeight() / 2);     
            }     
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度    
            ImageIcon imgIcon = new ImageIcon(iconPath);     
            // 得到Image对象。     
            Image img = imgIcon.getImage();     
            float alpha = 1f; // 透明度     
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,     
                    alpha));     
            // 表示水印图片的位置     
           int startX = buffImg.getWidth() - imgIcon.getIconWidth();
           int startY = buffImg.getHeight() - imgIcon.getIconHeight();
            g.drawImage(img, startX, startY, null);     
//            drawImage(maskImg, rop, startX, startY);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));     
            g.dispose();     
            os = new FileOutputStream(targerPath);     
            // 生成图片     
            ImageIO.write(buffImg, "JPG", os);     
        } catch (Exception e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if (null != os)     
                    os.close();     
            } catch (Exception e) {     
                e.printStackTrace();     
            }     
        }     
    }   
}

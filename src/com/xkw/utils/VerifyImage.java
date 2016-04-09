/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * web中验证码图片生成类
 * @version 1.0
 * @author LiaoGang
 */

public class VerifyImage {
	private int width = 75;
	private int height = 30;
	//生成的验证码内容
	private String content = null;
	private BufferedImage image = null;
	
	public VerifyImage() {
		super();
		init();
	}
	public VerifyImage(int width, int height) {
		super();
		init();;
		this.width = width;
		this.height = height;
	}
	
	private void init(){
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		//设置背景色
		setBackground(g);
		//1.设置背景色
		setBackground(g);
		//2.设置边框
		setBorder(g);
		//3.画干扰线
		darwRandomLine(g);
		//4.写随机文字,旋转并返回随机内容
		content = drawRandomString(g);
	}
	
	public void getImage(HttpServletResponse response){
		//设置浏览器不缓存图片
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg"); 
		try {
			ImageIO.write(image, "jpg", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getContent(){
		return content;
	}
	
	private void setBackground(Graphics2D g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
	}
	
	private void setBorder(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, width, height);
	}
	
	private void darwRandomLine(Graphics2D g) {
		g.setColor(Color.BLUE);
		//干扰线的数量,5-10条
		int count = new Random().nextInt(5)+5;
		for(int k=0; k<count; k++){
			int w1 = new Random().nextInt(width);
			int h1 = new Random().nextInt(height);
			int w2 = new Random().nextInt(width);
			int h2 = new Random().nextInt(height);
			g.drawLine(w1, h1, w2, h2);
		}
	}
	
	//4.写随机文字,旋转并返回随机内容
	private String drawRandomString(Graphics2D g) {
		g.setColor(Color.BLACK);
		//设置文字的字体,大小等
		g.setFont(new Font("宋体", Font.BOLD, 25));
		
		//首字母的左边距
		int x = 5;
		String str = "";
		char[] chs = getCharArray();
		for(int k=0; k<4; k++) {
			//旋转角度
			int degree = (new Random().nextInt())%30;
			g.rotate(degree*Math.PI/180, x, 20);
			int i = new Random().nextInt(chs.length);
			String ch = chs[i]+"";
			//画出对应字符，x是对应的横坐标，20是控制上下间距
			g.drawString(ch, x, 20);
			g.rotate(-degree*Math.PI/180, x, 20);
			//字符间距
			x += 15;
			str += ch;
		}
		return str; 
	}
	
	//存放验证码可以出现的内容:大小写字母及数字0-9,若需汉字,则使用汉字的范围:[\u4e00 - \u9fa5]
	private char[] getCharArray(){
		StringBuffer buffer = new StringBuffer();
		for(char ch='A'; ch<='Z'; ch++){
			buffer.append(ch);
			buffer.append((char)(ch+32));
			if(ch<'K'){
				buffer.append(ch-'A');
			}
		}
		
		return buffer.toString().toCharArray();
	}
	
}

/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-02-06
 */
package com.xkw.mc.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 用于获取spring中bean对象的工具类
 * 可以在任意地方使用工具类的静态方法获取Spring的context对象以及Spring所管理的bean对象
 * @version 1.0
 * @author LiaoGang
 */

@Component
public class GlobalContext implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	
	@Override
	public void setApplicationContext(ApplicationContext contex)
			throws BeansException {
		context = contex;
	}
	
	
	/**
	 * 获取Spring的context对象
	 * @return
	 */
	public static ApplicationContext getContext() {
		return context;
	}
	
	
	/**
	 * 根据beanName获取Bean对象
	 * @param beanName
	 * @return 返回Object对象,需做类型转换
	 */
	public final static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
	/**
	 * 根据beanName获取Bean对象
	 * @param beanName
	 * @return 返回Object对象,需做类型转换
	 */
	public final static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	
	/**
	 * 根据beanName以及bean的类型获取bean对象,
	 * @param beanName
	 * @param requiredType
	 * @return 返回class所对应的对象,传入正确参数即可,无需再传类型转换
	 */
	public final static <T> T getBean(String beanName, Class<T> requiredType) {
		return context.getBean(beanName, requiredType);
	}
	
	
}
/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.mc.component.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

/**
 * 对bean的属性做修改
 * FilterSecurityInterceptor的属性：
 * securityMetadataSource
 * @version 1.0
 * @author LiaoGang
 * 
 */

@Component
public class FilterSecurityInterceptorPostProcessor implements
		BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	private McefaultFilterInvocationSecurityMetadataSource securityMetadataSource = null;
	private FilterSecurityInterceptor filterSecurityInterceptor = null;
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		
		if(bean instanceof FilterSecurityInterceptor) {
			filterSecurityInterceptor = (FilterSecurityInterceptor) bean;
			if(securityMetadataSource != null) {
				try {
					filterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource.getObject());
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(bean instanceof McefaultFilterInvocationSecurityMetadataSource) {
			securityMetadataSource = (McefaultFilterInvocationSecurityMetadataSource) bean;
			if(filterSecurityInterceptor != null) {
				try {
					filterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource.getObject());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return bean;
	}
}

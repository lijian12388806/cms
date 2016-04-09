/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.mc.component.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * 在此实现定义的逻辑:访问数据库,获取哪些资源url需要受保护,
 * 以及访问这些资源需要具备的权限authority
 * 
 * @version 1.0
 * @author LiaoGang
 */

@Component("securityMetadataSource")
public class McefaultFilterInvocationSecurityMetadataSource implements
		FactoryBean<DefaultFilterInvocationSecurityMetadataSource> {

	@Autowired
	private SecurityMetadataSourceMapBuilder mapBuilder;
	
	@Override
	public DefaultFilterInvocationSecurityMetadataSource getObject()
			throws Exception {
		
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
		AntPathRequestMatcher matcher = null;
		Collection<ConfigAttribute> attributes = null;
		Map<String, List<String>> srcMap = mapBuilder.buildSrcMap();
		
		for(Map.Entry<String, List<String>> entry : srcMap.entrySet()) {
			String url = entry.getKey();
			List<String> attributyString = entry.getValue();
			
			matcher = new AntPathRequestMatcher(url);
			attributes = new ArrayList<>();
			
			for(String attributy : attributyString) {
				attributes.add(new SecurityConfig(attributy));
			}
			
			requestMap.put(matcher, attributes);
		}
		
		DefaultFilterInvocationSecurityMetadataSource securityMetadataSource
			= new DefaultFilterInvocationSecurityMetadataSource(requestMap);
		return securityMetadataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return DefaultFilterInvocationSecurityMetadataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

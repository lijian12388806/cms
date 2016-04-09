/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-09
 */
package com.xkw.mc.component.security;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xkw.mc.entity.Authority;
import com.xkw.mc.entity.Resource;
import com.xkw.mc.service.AuthorityService;

/**
 * SecurityMetadataSourceMapBuilder接口的具体实现
 * @version 1.0
 * @author LiaoGang
 */

@Component
public class McSecurityMetadataSourceMapBuilder implements SecurityMetadataSourceMapBuilder{
	
	@Autowired
	private AuthorityService authorityService; 
	
	/**
	 * 构造资源与权限对应的Map集合
	 * 	     哪些权限可以访问哪些资源
	 * @return
	 */
	@Transactional
	public LinkedHashMap<String, List<String>> buildSrcMap() {
		LinkedHashMap<String, List<String>> srcMap = new LinkedHashMap<>();
		List<Authority>  authoritys = authorityService.getAll();
		for(Authority authority : authoritys) {
			Set<Resource> resources = authority.getResources();
			for(Resource resource : resources) {
				String url = resource.getUrl();
				List<String> roleNames = new ArrayList<>();
				roleNames.add(authority.getRoleName());
				srcMap.put(url, roleNames);
			}
		}
		return srcMap;
	}
}

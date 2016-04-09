package com.xkw.mc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkw.mc.dao.AuthorityDao;
import com.xkw.mc.dao.ResourceDao;
import com.xkw.mc.entity.Authority;
import com.xkw.mc.entity.Resource;
import com.xkw.mc.service.AuthorityService;


@Service
public class AuthorityServiceImpl extends BaseServiceImpl<Authority, Integer> implements AuthorityService {

	@Autowired
	private AuthorityDao authorityDao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Override
	public List<Authority> getRootAuthorities() {
		return authorityDao.getRootAuthorities();
	}

	@Override
	public void saveOrUpdateResource(Authority authority, List<String> urls) {
		if(authority.getId()!=null) {
			resourceDao.deleteByAuthId(authority.getId());
		}
		
		if(authority.getParentAuthority()!=null && authority.getParentAuthority().getId()==null) {
			authority.setParentAuthority(null);//避免发生unsave的异常
		}
		
		for(String url : urls) {
			Resource resource = new Resource(url);
			resource.setAuthority(authority);
			authority.getResources().add(resource);
			
			resourceDao.saveOrUpdate(resource);
		}
		
		authorityDao.saveOrUpdate(authority);
	}

}

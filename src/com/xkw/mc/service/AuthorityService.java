package com.xkw.mc.service;

import java.util.List;

import com.xkw.mc.entity.Authority;

public interface AuthorityService extends BaseService<Authority, Integer> {

	/**
	 * 获取所有根权限
	 * @return
	 */
	public List<Authority> getRootAuthorities();

	
	/**
	 * 保存或修改权限以及资源
	 * @param authority
	 * @param resources
	 */
	public void saveOrUpdateResource(Authority authority, List<String> urls);

}

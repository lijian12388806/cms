package com.xkw.mc.dao;

import java.util.List;

import com.xkw.mc.entity.Authority;

public interface AuthorityDao extends BaseDao<Authority, Integer> {

	/**
	 * 获取所有根权限
	 * @return
	 */
	public List<Authority> getRootAuthorities();

}

package com.xkw.mc.dao;

import com.xkw.mc.entity.AdminUser;

public interface AdminUserDao extends BaseDao<AdminUser, Integer> {

	/**
	 * 通过用户名以及密码查询用户
	 * @param username
	 * @param password
	 * @return
	 */
	AdminUser getByUserAndPwd(String username, String password);

}

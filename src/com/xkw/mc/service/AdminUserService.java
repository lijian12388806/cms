package com.xkw.mc.service;

import com.xkw.mc.entity.AdminUser;

public interface AdminUserService extends BaseService<AdminUser, Integer> {
	
	/**
	 * 添加一个用户并对密码加密
	 * @param user
	 */
	public void saveUserBySecurity(AdminUser user);

	
	/**
	 * 通过用户名以及密码查询用户
	 * @param username
	 * @param password
	 * @return
	 */
	public AdminUser getByUserAndPwd(String username, String password);
	
}

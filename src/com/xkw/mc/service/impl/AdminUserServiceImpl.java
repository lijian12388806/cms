package com.xkw.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xkw.mc.dao.AdminUserDao;
import com.xkw.mc.entity.AdminUser;
import com.xkw.mc.service.AdminUserService;

@Service
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser, Integer> implements AdminUserService {

	private PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
	
	@Autowired
	AdminUserDao userDao;
	
	@Override
	public void saveUserBySecurity(AdminUser user) {
		if(user!=null && user.getPassword()!=null) {
			String password = user.getPassword();
			user.setPassword(passwordEncoder.encodePassword(password, user.getUsername()));
			userDao.save(user);
		}
	}

	@Override
	public AdminUser getByUserAndPwd(String username, String password) {
		if(password!=null) {
			return userDao.getByUserAndPwd(username, passwordEncoder.encodePassword(password, username));
		}
		return null;
	}

}

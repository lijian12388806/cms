package com.xkw.mc.dao.impl;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.xkw.mc.dao.AdminUserDao;
import com.xkw.mc.entity.AdminUser;


@Repository
public class AdminUserDaoImpl extends BaseDaoImpl<AdminUser, Integer> implements AdminUserDao {

	@Override
	public AdminUser getByUserAndPwd(String username, String password) {
		Criterion criterion1 = Restrictions.eq("username", username);
		Criterion criterion2 = Restrictions.eq("password", password);
		return (AdminUser) getCriteria(criterion1, criterion2).uniqueResult();
	}

}

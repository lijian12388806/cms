package com.xkw.mc.dao.impl;


import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.xkw.mc.dao.AuthorityDao;
import com.xkw.mc.entity.Authority;


@Repository
public class AuthorityDaoImpl extends BaseDaoImpl<Authority, Integer> implements AuthorityDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Authority> getRootAuthorities() {
		String sql = "FROM Authority o WHERE o.parentAuthority IS NULL";
		Query query = getSession().createQuery(sql);
		return query.list();
	}

	
}

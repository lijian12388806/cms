package com.xkw.mc.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xkw.mc.dao.ResourceDao;
import com.xkw.mc.entity.Resource;


@Repository
public class ResourceDaoImpl extends BaseDaoImpl<Resource, Integer> implements ResourceDao {

	@Override
	public void deleteByAuthId(Integer id) {
		String sql = "delete from Resource r where r.authority.id=:id";
		Query query = getSession().createQuery(sql);
		query.setParameter("id", id);
		
		query.executeUpdate();
	}

}

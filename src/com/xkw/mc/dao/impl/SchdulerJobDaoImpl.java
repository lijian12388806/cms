package com.xkw.mc.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xkw.mc.dao.SchdulerJobDao;
import com.xkw.mc.entity.SchdulerJob;


@Repository(value="schdulerJobRepository")
public class SchdulerJobDaoImpl extends BaseDaoImpl<SchdulerJob, Integer>
		implements SchdulerJobDao {

	@Override
	public int deleteByGroupAndName(String jobGroup, String jobName) {
		String sql = "DELETE FROM SchdulerJob sj WHERE sj.jobGroup=:jobGroup AND sj.jobName=:jobName";
		Query query = getSession().createQuery(sql);
		query.setParameter("jobGroup", jobGroup);
		query.setParameter("jobName", jobName);
		
		return query.executeUpdate();
	}

	@Override
	public void deleteByProperty(String key, String value) {
		String sql = "DELETE FROM SchdulerJob sj WHERE sj."+key+"=:"+key;
		Query query = getSession().createQuery(sql);
		query.setParameter(key, value);
		query.executeUpdate();
	}
	
}

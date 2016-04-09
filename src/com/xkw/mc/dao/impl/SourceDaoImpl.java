package com.xkw.mc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xkw.mc.dao.SourceDao;
import com.xkw.mc.entity.Source;
import com.xkw.utils.Page;
@Repository
public class SourceDaoImpl extends BaseDaoImpl<Source, Integer> implements SourceDao{

	/**
	 * 素材列表数据加开始、结束时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<Source> getSourceList(Integer page, Integer rows, Long sDate, Long eDate) {
		StringBuffer sql = new StringBuffer("SELECT s, u.username "
				+ "FROM Source s, User u "
				+ "WHERE s.uid=u.id "
				+ "AND s.type!=:type");
		StringBuffer countSql = new StringBuffer("SELECT count(s.id) FROM Source s,User u WHERE s.uid=u.id AND s.type!=:type");
		if(sDate!=null) {
			sql.append("and s.createTime >= :sDate ");
			countSql.append("and s.createTime >= :sDate ");
		}
		if(eDate!=null) {
			sql.append("and s.createTime <= :eDate ");
			countSql.append("and s.createTime <= :eDate ");
		}
		Query query = getSession().createQuery(sql.toString());
		Query countQuery = getSession().createQuery(countSql.toString());
		query.setFirstResult((page-1)*rows).setMaxResults(rows);
		query.setParameter("type", Source.SourceTypes.GROUP);
		countQuery.setParameter("type", Source.SourceTypes.GROUP);
		if(sDate!=null) {
			query.setParameter("sDate", sDate);
			countQuery.setParameter("sDate", sDate);
		}
		if(eDate!=null) {
			query.setParameter("eDate", eDate);
			countQuery.setParameter("eDate", eDate);
		}
		Long total = Long.parseLong(countQuery.uniqueResult().toString());
		List<Source> sources = new ArrayList<>();
		List<Object[]> content = query.list();
		for(Object[] objArr : content) {
			Source source = (Source) objArr[0];
			source.setUsername((String)objArr[1]);
			sources.add(source);
		}
		return new Page<>(sources, page, total, rows);
	}
	

}
